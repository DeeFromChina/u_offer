package com.golead.art.artist;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistCollector;
import com.golead.art.artist.service.ArtArtistCollectorService;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtistCollectorAction extends BaseAction {

   private Log                       log       = LogFactory.getLog(ArtistCollectorAction.class);

   public static String              FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private File                      files;
   private String                    filesFileName;

   private List<Map<String, String>> cookies   = new ArrayList<Map<String, String>>();

   @Resource
   private ArtArtistCollectorService artArtistCollectorService;

   @Resource
   private ArtSysLogService          artSysLogService;

   @Resource
   private ArtArtistService          artArtistService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) {
         forward = LIST;
      }
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listCollect();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageCollect();
         else if (ADD.equalsIgnoreCase(action)) forward = addCollect();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveCollect();
         else if (EDIT.equalsIgnoreCase(action)) forward = editCollect();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateCollect();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteCollect();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewCollect();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importCollect();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
      return forward;
   }

   public String listCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'listCollect()' method");
      try {
         initForm();
         return LIST;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String pageCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering pageCollect() method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pq.getParameters().put("artistId", artistId);
         pq = artArtistCollectorService.queryArtArtistCollector(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String collectWorks = item.get("collectWorks") == null ? "" : item.get("collectWorks").toString();
            StringBuffer worksName = new StringBuffer();
            String a = "<a href='#' onclick='goWorks(";
            if ("".equals(collectWorks.trim())) {
               collectWorks = "0";
            }
            List<ArtWorks> artWorks = artArtistCollectorService.findWorksByWorksId(collectWorks.replace("、", ","));
            if (artWorks != null) {
               for (ArtWorks artWorks2 : artWorks) {
                  worksName.append(a);
                  worksName.append(artWorks2.getId().toString());
                  worksName.append(")'>");
                  worksName.append(artWorks2.getWorksCName());
                  worksName.append("</a>");
                  worksName.append(" , ");
               }
            }

            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("collector"), item.get("collectTime"), worksName.toString(), item.get("collectDesc") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "收藏家管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addCollect()' method");
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveCollect()' method");
      try {
         ArtArtistCollector artArtistCollect = new ArtArtistCollector();
         ConvertUtil.convertToModel(artArtistCollect, form.getRecord());
         ArtArtist artArtist = artArtistService.getArtArtist(artArtistCollect.getArtistId());
         artArtistCollectorService.createArtArtistCollector(artArtistCollect);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "收藏家管理",
               "添加收藏家：" + artArtist.getCname() + ":" + "[" + artArtistCollect.getCollectDesc() + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String editCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editCollect()' method");
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtistCollector artArtistCollect = artArtistCollectorService.getArtArtistCollector(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistCollect);
         String collectWorks = artArtistCollect.getCollectWorks() == null ? "" : artArtistCollect.getCollectWorks();
         if ("".equals(collectWorks.trim())) { return EDIT; }
         List<ArtWorks> artWorks = artArtistCollectorService.findWorksByWorksId(collectWorks.replace("、", ","));
         if (artWorks == null) { return EDIT; }
         for (ArtWorks artWorks2 : artWorks) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", artWorks2.getId().toString());
            map.put("name", artWorks2.getWorksCName());
            cookies.add(map);
         }
         String name = artArtistCollectorService.findByWorksId(collectWorks.replace("、", ","));
         form.getRecord().put("collectWorksName", name);
         form.getRecord().put("worksName", "1");
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateCollect()' method");
      try {
         Map<String, String> record = getForm().getRecord();
         Integer artistId =Integer.valueOf(record.get(ArtArtistCollector.PROP_ARTIST_ID));
         ArtArtist artArtist = artArtistService.getArtArtist(artistId);
         artArtistCollectorService.updateArtArtistCollector(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "收藏家管理",
               "修改收藏家：" + artArtist.getCname() + ":" + "[" + record.get(ArtArtistCollector.PROP_COLLECT_DESC) + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }

   }

   public String deleteCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteCollect()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);

            ArtArtistCollector artArtistCollector = artArtistCollectorService.getArtArtistCollector(del_ids[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(artArtistCollector.getArtistId());
            if (log.length() > 0) log.append(",");
            log.append(artArtist.getCname()).append(":").append("[").append(artArtistCollector.getCollectDesc()).append("]");
         }
         artArtistCollectorService.deleteArtArtistCollectors(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "收藏家管理", "删除收藏家：" + log.toString());
         setResponse("{exit:0}");
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"删除操作失败!\"}");
      }
      return null;
   }

   public String viewCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewCollect()' method");
      try {
         initForm();
         ArtArtistCollector artArtistCollect = artArtistCollectorService.getArtArtistCollector(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistCollect);
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String importCollect() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importCollect()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "collect");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "collect" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            int artistId = Integer.valueOf(getForm().getRecord().get("artistId"));
            String message = artArtistCollectorService.importCollects(path, artistId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "收藏家管理", "导入收藏家");
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
      if (log.isDebugEnabled()) log.debug("Entering 'downloadTemplate()' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("收藏家导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist_collect.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "收藏家管理", "下载收藏家模板");
         printExcel.doPrint(response, templateFile, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public void initForm() throws Exception {
      setCode(getForm(), "");
   }

   public File getFiles() {
      return files;
   }

   public void setFiles(File files) {
      this.files = files;
   }

   public String getFilesFileName() {
      return filesFileName;
   }

   public void setFilesFileName(String filesFileName) {
      this.filesFileName = filesFileName;
   }

   public List<Map<String, String>> getCookies() {
      return cookies;
   }

   public void setCookies(List<Map<String, String>> cookies) {
      this.cookies = cookies;
   }

}
