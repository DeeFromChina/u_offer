package com.golead.art.works;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
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

import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksCase;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksCaseService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtWorksCaseAction extends BaseAction {

   private final Log                 logger    = LogFactory.getLog(ArtWorksCaseAction.class);

   private SimpleDateFormat          sdf       = new SimpleDateFormat("yyyy-MM-dd");

   public static String              FILE_PATH = ServletActionContext.getServletContext().getRealPath("upload/works/case");
   private List<Map<String, String>> cookie    = new ArrayList<Map<String, String>>();
   private List<Map<String, String>> pic       = new ArrayList<Map<String, String>>();
   private List<File>                files;
   private String                    filesFileName;
   private String                    filesContentType;
   private File                      importFile;
   private String                    importFileFileName;
   private String                    importFileContentType;

   @Resource
   private ArtWorksCaseService       artWorksCaseService;

   @Resource
   private ArtWorksService           artWorksService;

   @Resource
   private ArtArtistService          artArtistService;

   @Resource
   private ArtSysLogService          artSysLogService;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         initForm();
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageDate();
         else if (ADD.equalsIgnoreCase(action)) forward = addCase();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveCase();
         else if (EDIT.equalsIgnoreCase(action)) forward = editCase();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateCase();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
         else if ("SHOW".equalsIgnoreCase(action)) forward = show();
         else if ("GOEXCEL".equalsIgnoreCase(action)) forward = goExcel();
         else if ("EXCEL".equalsIgnoreCase(action)) forward = excel();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = download();
         else if ("DOWNLOADFILE".equalsIgnoreCase(action)) forward = downloadFile();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public void initForm() throws Exception {
      setCode(form, "ART_TYPE,DATE_YEAR,MEDIUM_TYPE,SIGNATURE,PERIOD_TYPE,EVAL_TYPE");
   }

   private String getPageDate() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageDate' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery.getParameters().put("id", id);
         pageQuery = artWorksCaseService.queryArtWorksCase(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               String artistlink = "^javascript:view(" + item.get("id") + ");^_self";
               map.put("data", new Object[] { "", item.get("caseName") + artistlink, item.get("caseAuther"), sdf.format(item.get("caseTime")),
                     item.get("researchTopic"), item.get("caseContent") });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品个案管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
         setResponse(res);
      }
      catch (ServiceException e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   private String addCase() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addWorks' method");
      String forward = null;
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String saveCase() throws Exception {
      try {
         ArtWorksCase artWorksCase = new ArtWorksCase();
         String path = FILE_PATH + File.separator + form.getRecord().get("worksId");
         File file = new File(path);
         if (!file.exists()) {
            file.mkdirs();
         }
         ConvertUtil.mapToObject(artWorksCase, form.getRecord(), false);
         artWorksCaseService.createArtWorksCase(artWorksCase, files, getFilesFileName(), path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品个案管理", "添加作品个案：" + artWorksCase.getCaseName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   private String editCase() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
      String forward = null;
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtWorksCase artWorksCase = artWorksCaseService.getArtWorksCase(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artWorksCase);
         String picture = artWorksCase.getAttachment();
         if (picture != null && !"".equals(picture)) {
            String[] pictures = picture.split(",");
            for (String str : pictures) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("name", str.split("/")[0]);
               map.put("realName", str.split("/")[1]);
               map.put("path", FILE_PATH + File.separator + artWorksCase.getWorksId().toString() + File.separator + str.split("/")[1]);
               pic.add(map);
            }
         }
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String updateCase() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'updateWorks' method");
      try {
         Map<String, String> record = form.getRecord();
         String path = FILE_PATH + File.separator + form.getRecord().get("worksId");
         artWorksCaseService.updateArtWorksCase(record, files, record.get("fileName"), getFilesFileName(), path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品个案管理", "修改作品个案：" + record.get(ArtWorksCase.PROP_CASE_NAME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'view' method");
      String forward = null;
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtWorksCase artWorksCase = artWorksCaseService.getArtWorksCase(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artWorksCase);
         String picture = artWorksCase.getAttachment();
         if (picture != null && !"".equals(picture)) {
            String[] pictures = picture.split(",");
            for (String str : pictures) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("name", str.split("/")[0]);
               map.put("realName", str.split("/")[1]);
               pic.add(map);
            }
         }
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      try {
         String[] idlist = ids.split(",");
         Integer[] dels = new Integer[idlist.length];
         Integer worksId = Integer.valueOf(form.getRecord().get("worksId"));
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < idlist.length; i++) {
            dels[i] = Integer.valueOf(idlist[i]);
            ArtWorksCase artWorksCase = artWorksCaseService.getArtWorksCase(dels[i]);
            ArtWorks artWorks = artWorksService.getArtWorks(worksId);
            if (log.length() > 0) log.append(",");
            log.append(artWorks.getWorksCName()).append(":").append("[").append(artWorksCase.getCaseName()).append("]");
         }
         String path = FILE_PATH + File.separator + "upload" + File.separator + "works" + File.separator + "case" + File.separator + worksId;
         artWorksCaseService.deleteArtWorksCases(dels, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品个案管理", "删除作品个案：" + log.toString());
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return null;
   }

   private String goExcel() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'goExcel' method");
      try {
         return "GOEXCEL";
      }
      catch (Exception e) {
         e.printStackTrace();
         return LIST;
      }
   }

   private String excel() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'excel' method");
      try {
         String id = form.getRecord().get("id");
         File file = new File(FILE_PATH);
         if (!file.exists()) {
            file.mkdirs();
         }
         String path = FILE_PATH + File.separator + importFileFileName;
         FileUtils.fileUpload(path, importFile);
         String message = artWorksCaseService.importArtWorksCase(path, Integer.valueOf(id));
         if (!"".equals(message)) {
            if (message.startsWith("成功")) {
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品个案管理", "导入作品个案");
               request.put("msg", message);
            }
            else {
               throw new Exception(message);
            }
         }
         return returnForward(message);
      }
      catch (Exception e) {
         e.printStackTrace();
         if (e.getMessage() != null) {
            addMessage(form, e.getMessage());
         }
         return "GOEXCEL";
      }
   }

   private String download() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'download' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("个案研究模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String tempPath = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator + "works" + File.separator
               + "art_works_case.xls";
         File tempFile = new File(tempPath);
         if (!tempFile.exists()) { throw new Exception(); }

         PrintExcel printExcel = new PrintExcel();
         InputStream inp = null;
         inp = new FileInputStream(tempFile);
         HSSFWorkbook wb = null;
         POIFSFileSystem f = new POIFSFileSystem(inp);
         wb = new HSSFWorkbook(f);

         HSSFDataFormat format = wb.createDataFormat();

         HSSFCellStyle csString = wb.createCellStyle();
         csString.setBorderLeft(CellStyle.BORDER_THIN);
         csString.setBorderRight(CellStyle.BORDER_THIN);
         csString.setBorderTop(CellStyle.BORDER_THIN);
         csString.setBorderBottom(CellStyle.BORDER_THIN);
         csString.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         csString.setDataFormat(format.getFormat("@"));

         HSSFCellStyle csDecimal = wb.createCellStyle();
         csDecimal.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal.setBorderBottom(CellStyle.BORDER_THIN);
         csDecimal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

         HSSFCellStyle csDecimal1 = wb.createCellStyle();
         csDecimal1.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal1.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal1.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal1.setBorderBottom(CellStyle.BORDER_THIN);
         HSSFDataFormat csformat = wb.createDataFormat();
         csDecimal1.setDataFormat(csformat.getFormat("0.000"));

         List<PrintPoint> printPointList = new ArrayList<PrintPoint>();

         printExcel.setPrintPointList(printPointList);// 填写数据
         printExcel.doFillSheet(wb, 0);// 写入workbook的第一页
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品个案管理", "下载作品个案导入模板");
         printExcel.doPrint(response, tempFile, wb);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private String show() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'show' method");
      try {
         return "SHOW";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String downloadFile() throws Exception {
      try {
         String id = form.getRecord().get("id");
         String name = form.getRecord().get("name");
         ArtWorksCase artWorksCase = artWorksCaseService.getArtWorksCase(Integer.valueOf(id));
         String tempPath = FILE_PATH + File.separator + "upload" + File.separator + "works" + File.separator + "case" + File.separator
               + artWorksCase.getWorksId().toString() + File.separator + name;
         String[] attachments = artWorksCase.getAttachment().split(",");
         for (int i = 0; i < attachments.length; i++) {
            if (attachments[i].endsWith(name)) {
               name = attachments[i].split("/")[0];
               break;
            }
         }
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("multipart/form-data");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("gb2312"), "ISO-8859-1"));
         File tempFile = new File(tempPath);
         if (!tempFile.exists()) { throw new Exception(); }
         ServletOutputStream out;
         FileInputStream inputStream = new FileInputStream(tempFile);

         //3.通过response获取ServletOutputStream对象(out)  
         out = response.getOutputStream();

         int b = 0;
         byte[] buffer = new byte[512];
         while (b != -1) {
            b = inputStream.read(buffer);
            //4.写到输出流(out)中  
            out.write(buffer, 0, b);
         }
         inputStream.close();
         out.close();
         out.flush();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public List<Map<String, String>> getCookie() {
      return cookie;
   }

   public void setCookie(List<Map<String, String>> cookie) {
      this.cookie = cookie;
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

   public String getFilesContentType() {
      return filesContentType;
   }

   public void setFilesContentType(String filesContentType) {
      this.filesContentType = filesContentType;
   }

   public List<Map<String, String>> getPic() {
      return pic;
   }

   public void setPic(List<Map<String, String>> pic) {
      this.pic = pic;
   }

   public File getImportFile() {
      return importFile;
   }

   public void setImportFile(File importFile) {
      this.importFile = importFile;
   }

   public String getImportFileFileName() {
      return importFileFileName;
   }

   public void setImportFileFileName(String importFileFileName) {
      this.importFileFileName = importFileFileName;
   }

   public String getImportFileContentType() {
      return importFileContentType;
   }

   public void setImportFileContentType(String importFileContentType) {
      this.importFileContentType = importFileContentType;
   }

}
