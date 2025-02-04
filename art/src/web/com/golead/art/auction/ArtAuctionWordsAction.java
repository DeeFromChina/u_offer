package com.golead.art.auction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.golead.art.auction.model.ArtAuctionWords;
import com.golead.art.auction.service.ArtAuctionWordsService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtAuctionWordsAction extends BaseAction {

   private Log                           log         = LogFactory.getLog(ArtAuctionWordsAction.class);

   private final static SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

   public static String                  FILE_PATH   = ServletActionContext.getServletContext().getRealPath("");
   private File                          importFile;
   private List<File>                    files;
   private String                        filesFileName;
   private List<Map<String, String>>     filesList   = new ArrayList<Map<String, String>>();

   @Resource
   private ArtAuctionWordsService        artAuctionWordsService;
   
   @Resource
   private ArtSysLogService              artSysLogService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();

      if (action == null) {
         action = LIST;
      }
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = pageWords();
         else if (ADD.equalsIgnoreCase(action)) forward = addWords();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveWords();
         else if (EDIT.equalsIgnoreCase(action)) forward = editWords();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateWords();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteWords();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewWords();
         else if ("SHOW".equalsIgnoreCase(action)) forward = showPicture();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importWords();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
         else if ("DOWNLOADFILE".equalsIgnoreCase(action)) forward = downloadFile();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String pageWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageWords' method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         pq = artAuctionWordsService.queryArtAuctionWords(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String link = "^javascript:view(" + item.get("id") + ")^_self";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("wordsTheme") + link, item.get("wordsSource"), formateDate(item.get("wordsTime")) });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关文字管理", "拍卖相关文字管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
         setResponse(res);
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   public String addWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addWords' method");
      try {
         initForm();
         return "ADD";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveWords' method");
      try {
         initForm();
         ArtAuctionWords artAuctionWords = new ArtAuctionWords();
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator
               + getForm().getRecord().get("auctionId");
         File file = new File(path);
         if (file != null) {
            file.mkdirs();
         }
         ConvertUtil.mapToObject(artAuctionWords, getForm().getRecord(), false);
         artAuctionWordsService.createArtAuctionWords(artAuctionWords, files, filesFileName, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关文字管理", "拍卖相关文字管理", "添加拍卖相关文字：" + artAuctionWords.getAttachment());
         return returnForward(RETURN_NORMAL);
      }
      catch (ServiceException e) {
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(getForm(), "保存操作失败!");
         return ADD;
      }
   }

   public String editWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editWords' method");
      try {
         initForm();
         int id = Integer.valueOf(getForm().getRecord().get("id"));
         ArtAuctionWords artAuctionWords = artAuctionWordsService.getArtAuctionWords(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artAuctionWords);
         String attachment = artAuctionWords.getAttachment();
         if (attachment != null && !("").equals(attachment)) {
            String[] attachmentsNames = attachment.split(";");
            for (String attachmentsName : attachmentsNames) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("name", attachmentsName);
               map.put("path", FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator
                     + artAuctionWords.getAuctionId().toString() + File.separator + attachmentsName);
               filesList.add(map);
            }
         }
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateWords' method");
      try {
         initForm();
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator
               + getForm().getRecord().get("auctionId");
         File file = new File(path);
         if (file != null) {
            file.mkdirs();
         }
         artAuctionWordsService.updateArtAuctionWords(getForm().getRecord(), files, getForm().getRecord().get("fileName"), filesFileName, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关文字管理", "拍卖相关文字管理", "修改拍卖相关文字：" + getForm().getRecord().get(ArtAuctionWords.PROP_WORDS_THEME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteWords' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            if(log.length()>0)log.append(",");
            ArtAuctionWords artAuctionWords = artAuctionWordsService.getArtAuctionWords(del_ids[i]);
            log.append(artAuctionWords.getWordsTheme());
         }
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator
               + getForm().getRecord().get("auctionId");
         artAuctionWordsService.deleteArtAuctionWordss(del_ids, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关文字管理", "拍卖相关文字管理", "删除拍卖相关文字：" + log.toString());
         return pageWords();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public String viewWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewWords' method");
      try {
         initForm();
         ArtAuctionWords artAuctionWords = artAuctionWordsService.getArtAuctionWords(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artAuctionWords);
         String attachment = artAuctionWords.getAttachment();
         if (attachment != null && !("").equals(attachment)) {
            String[] attachmentsNames = attachment.split(";");
            for (String attachmentsName : attachmentsNames) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("name", attachmentsName);
               map.put("path", FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator
                     + artAuctionWords.getAuctionId().toString() + File.separator + attachmentsName);
               filesList.add(map);
            }
         }
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public String showPicture() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'showPicture' method");
      try {
         return "SHOW";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public String importWords() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importWords' method");
      try {
         if (importFile != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "auction" + File.separator + "works");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "auction" + File.separator + "works" + File.separator
                  + importFile.getName();
            FileUtils.fileUpload(path, importFile);
            int auctionId = Integer.valueOf(getForm().getRecord().get("auctionId"));
            String message = artAuctionWordsService.importWords(path, auctionId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关文字管理", "拍卖相关文字管理", "导入拍卖相关文字");
                  request.put("msg", message);
               }
               else {
                  throw new Exception(message);
               }
            }
            return returnForward(RETURN_NORMAL);
         }
         else return "IMPORT";
      }
      catch (Exception e) {
         e.printStackTrace();
         if (e.getMessage() != null) {
            addMessage(getForm(), e.getMessage());
         }
         return "IMPORT";
      }

   }

   public String downloadTemplate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'downloadTemplate' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("相关文字导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/auction/art_auction_words.xls");
         File templateFile = new File(templatePath);
         PrintExcel printExcel = new PrintExcel();
         InputStream inputStream = new FileInputStream(templateFile);
         HSSFWorkbook hssfWorkbook = null;
         POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
         hssfWorkbook = new HSSFWorkbook(poifsFileSystem);

         HSSFDataFormat format = hssfWorkbook.createDataFormat();

         HSSFCellStyle csString = hssfWorkbook.createCellStyle();
         csString.setBorderLeft(CellStyle.BORDER_THIN);
         csString.setBorderRight(CellStyle.BORDER_THIN);
         csString.setBorderTop(CellStyle.BORDER_THIN);
         csString.setBorderBottom(CellStyle.BORDER_THIN);
         csString.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         csString.setDataFormat(format.getFormat("@"));

         HSSFCellStyle csDecimal = hssfWorkbook.createCellStyle();
         csDecimal.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal.setBorderBottom(CellStyle.BORDER_THIN);
         csDecimal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

         HSSFCellStyle csDecimal1 = hssfWorkbook.createCellStyle();
         csDecimal1.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal1.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal1.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal1.setBorderBottom(CellStyle.BORDER_THIN);
         HSSFDataFormat csformat = hssfWorkbook.createDataFormat();
         csDecimal1.setDataFormat(csformat.getFormat("0.000"));

         List<PrintPoint> printPoints = new ArrayList<PrintPoint>();

         printExcel.setPrintPointList(printPoints);// 填写数据
         printExcel.doFillSheet(hssfWorkbook, 0);// 写入workbook的第一页
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关文字管理", "拍卖相关文字管理", "下载导入拍卖相关文字模板");
         printExcel.doPrint(response, templateFile, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public String downloadFile() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'downloadFile' method");
      try {
         String name = getForm().getRecord().get("allName");
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "words" + File.separator
               + getForm().getRecord().get("auctionId") + File.separator + name;//
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         File file = new File(path);
         // 以流的形式下载文件。
         InputStream fis = new BufferedInputStream(new FileInputStream(path));
         byte[] buffer = new byte[fis.available()];
         fis.read(buffer);
         fis.close();
         // 清空response
         response.reset();
         // 设置response的Header
         response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("GBK"), "ISO-8859-1"));
         response.addHeader("Content-Length", "" + file.length());
         OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
         response.setCharacterEncoding("UTF-8");
         response.setContentType("application/octet-stream");
         toClient.write(buffer);
         toClient.flush();
         toClient.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public void initForm() throws Exception {
      setCode(getForm(), "WORDS_SOURCE");
   }

   private String formateDate(Object obj) {
      if (obj == null) return "";
      return dateFormate.format(obj);
   }

   public List<File> getFiles() {
      return files;
   }

   public void setFiles(List<File> files) {
      this.files = files;
   }

   public String getFilesFileName() {
      return filesFileName;
   }

   public void setFilesFileName(String filesFileName) {
      this.filesFileName = filesFileName;
   }

   public List<Map<String, String>> getFilesList() {
      return filesList;
   }

   public void setFilesList(List<Map<String, String>> filesList) {
      this.filesList = filesList;
   }

   public File getImportFile() {
      return importFile;
   }

   public void setImportFile(File importFile) {
      this.importFile = importFile;
   }

}
