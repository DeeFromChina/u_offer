package com.golead.art.literature;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureMediumResearch;
import com.golead.art.literature.service.ArtLiteratureMediumResearchService;
import com.golead.art.literature.service.ArtLiteratureMediumService;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtLiteratureMediumAction extends BaseAction {

   private Log                               log       = LogFactory.getLog(ArtLiteratureMediumAction.class);

   public static String                      FILE_PATH = ServletActionContext.getServletContext().getRealPath("upload/literature/medium");
   private File                              importFile;
   private List<File>                        files;
   private String                            filesFileName;
   private List<Map<String, String>>         cookie    = new ArrayList<Map<String, String>>();

   @Resource
   public ArtLiteratureMediumService         artLiteratureMediumService;

   @Resource
   public ArtLiteratureMediumResearchService artLiteratureMediumResearchService;

   @Resource
   public ArtArtistService                   artArtistService;

   @Resource
   private ArtSysLogService                  artSysLogService;

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
         else if (PAGE.equalsIgnoreCase(action)) forward = pageMedium();
         else if (ADD.equalsIgnoreCase(action)) forward = addMedium();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveMedium();
         else if (EDIT.equalsIgnoreCase(action)) forward = editMedium();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateMedium();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteMedium();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewMedium();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
         else if ("DOWNLOADFILE".equalsIgnoreCase(action)) forward = downloadFile();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String pageMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageMedium' method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         pq = artLiteratureMediumService.queryArtLiteratureMedium(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", item.get("id"));
            map.put("data",
                  new Object[] { "", item.get("CName"), item.get("literatureTitle") + "^javascript:view(" + item.get("id") + ");^_self", item.get("whenLong"),
                        item.get("shotPeople"), item.get("shotTime"), item.get("contentDesc"), item.get("literatureWorks"), item.get("personInvolved"),
                        item.get("relatedExhib"), item.get("relatedEvent") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addMedium' method");
      try {
         initForm();
         return "ADD";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveMedium' method");
      try {
         ArtLiteratureMedium artLiteratureMedium = new ArtLiteratureMedium();
         ConvertUtil.convertToModel(artLiteratureMedium, form.getRecord());
         artLiteratureMediumService.createArtLiteratureMedium(artLiteratureMedium);
         ArtLiteratureMediumResearch artLiteratureMediumResearch = new ArtLiteratureMediumResearch();
         artLiteratureMediumResearch.setMediumId(artLiteratureMedium.getId());
         ConvertUtil.convertToModel(artLiteratureMediumResearch, form.getRecord());
         artLiteratureMediumResearch.setMediumId(artLiteratureMedium.getId());
         artLiteratureMediumResearchService.createArtLiteratureMediumResearch(artLiteratureMediumResearch);
         if (files != null) {
            artLiteratureMediumService.saveFile(artLiteratureMedium, files, filesFileName, FILE_PATH);
         }
         artSysLogService.createArtSysLog(currentUser.getUserName(), "音视频管理", "音视频管理", "添加音视频：" + artLiteratureMedium.getLiteratureTitle());
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

   public String editMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editMedium' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtLiteratureMedium artLiteratureMedium = artLiteratureMediumService.getArtLiteratureMedium(Integer.valueOf(id));
         String literatureWorks = artLiteratureMedium.getLiteratureWorks();
         String personInvolved = artLiteratureMedium.getPersonInvolved();
         String relatedExhib = artLiteratureMedium.getRelatedExhib();
         String relatedEvent = artLiteratureMedium.getRelatedEvent();
         ArtArtist artist = artArtistService.getArtArtist(artLiteratureMedium.getArtistId());
         form.getRecord().put("artistName", artist.getCname());
         form.getRecord().put("artistId", artist.getId().toString());
         ArtLiteratureMediumResearch artLiteratureMediumResearch = artLiteratureMediumResearchService.findByMediumId(Integer.valueOf(id));
         if (artLiteratureMediumResearch != null) {
            ConvertUtil.objectToMap(form.getRecord(), artLiteratureMediumResearch);
         }
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureMedium);
         getEditPageParam("literatureWorks", literatureWorks);
         getEditPageParam("personInvolved", personInvolved);
         getEditPageParam("relatedExhib", relatedExhib);
         getEditPageParam("relatedEvent", relatedEvent);
         String attachment = artLiteratureMedium.getAttachment();
         if (attachment != null) {
            String[] attachments = attachment.split(",");
            for (int i = 0; i < attachments.length; i++) {
               if (attachments[i].split("/").length == 2) {
                  Map<String, String> map = new HashMap<String, String>();
                  map.put("name", attachments[i].split("/")[0]);
                  map.put("saveName", attachments[i].split("/")[1]);
                  cookie.add(map);
               }
            }
         }
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private void getEditPageParam(String key, String param) throws Exception {
      try {
         if (param == null) { return; }
         String[] params = param.split(",");
         for (int i = 0; i < params.length; i++) {
            form.getRecord().put(key + String.valueOf(i + 1), params[i]);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String updateMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateMedium' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtLiteratureMedium artLiteratureMedium = artLiteratureMediumService.getArtLiteratureMedium(Integer.valueOf(id));
         form.getRecord().get("literatureWorks");
         form.getRecord().put("literatureWorks", form.getRecord().get("literatureWorks"));
         ConvertUtil.convertToModel(artLiteratureMedium, form.getRecord());
         ArtLiteratureMediumResearch artLiteratureMediumResearch = artLiteratureMediumResearchService.findByMediumId(Integer.valueOf(id));
         artLiteratureMediumResearch.setCoreThesis(form.getRecord().get("coreThesis"));
         artLiteratureMediumResearch.setObjectiveEval(form.getRecord().get("objectiveEval"));
         artLiteratureMediumResearch.setSubjectiveEval(form.getRecord().get("subjectiveEval"));
         artLiteratureMediumService.updateFile(artLiteratureMediumResearch, artLiteratureMedium, files, filesFileName, FILE_PATH,
               form.getRecord().get("fileName"));
         artSysLogService.createArtSysLog(currentUser.getUserName(), "音视频管理", "音视频管理", "修改音视频：" + artLiteratureMedium.getLiteratureTitle());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(getForm(), e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteMedium' method");
      try {
         String[] id = ids.split(",");
         String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "medium";
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            deleteFile(path + File.separator + id[i]);
            artLiteratureMediumResearchService.deleteByMediumId(del_ids[i]);
            
            if(log.length()>0)log.append(",");
            
            ArtLiteratureMedium artLiteratureMedium = artLiteratureMediumService.getArtLiteratureMedium(del_ids[i]);
            log.append(artLiteratureMedium.getLiteratureTitle());
         }
         artLiteratureMediumService.deleteArtLiteratureMediums(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "音视频管理", "音视频管理", "删除音视频：" + log.toString());
         return pageMedium();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private void deleteFile(String path) throws Exception {
      try {
         File file = new File(path);
         if (file.isFile()) {
            file.delete();
         }
         else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
               deleteFile(files[i].getPath());
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String viewMedium() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewMedium' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtLiteratureMedium artLiteratureMedium = artLiteratureMediumService.getArtLiteratureMedium(Integer.valueOf(id));
         String literatureWorks = artLiteratureMedium.getLiteratureWorks();
         String personInvolved = artLiteratureMedium.getPersonInvolved();
         String relatedExhib = artLiteratureMedium.getRelatedExhib();
         String relatedEvent = artLiteratureMedium.getRelatedEvent();
         ArtArtist artist = artArtistService.getArtArtist(artLiteratureMedium.getArtistId());
         form.getRecord().put("artistName", artist.getCname());
         ArtLiteratureMediumResearch artLiteratureMediumResearch = artLiteratureMediumResearchService.findByMediumId(Integer.valueOf(id));
         if (artLiteratureMediumResearch != null) {
            ConvertUtil.objectToMap(form.getRecord(), artLiteratureMediumResearch);
            form.getRecord().put("subjectiveEval", findCodeName(form, "COMMENT_LEVEL", artLiteratureMediumResearch.getSubjectiveEval()));
            form.getRecord().put("objectiveEval", findCodeName(form, "COMMENT_LEVEL", artLiteratureMediumResearch.getObjectiveEval()));
         }
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureMedium);
         getEditPageParam("literatureWorks", literatureWorks);
         getEditPageParam("personInvolved", personInvolved);
         getEditPageParam("relatedExhib", relatedExhib);
         getEditPageParam("relatedEvent", relatedEvent);
         String attachment = artLiteratureMedium.getAttachment();
         if (attachment != null) {
            String[] attachments = attachment.split(",");
            for (int i = 0; i < attachments.length; i++) {
               if (attachments[i].split("/").length == 2) {
                  Map<String, String> map = new HashMap<String, String>();
                  map.put("name", attachments[i].split("/")[0]);
                  map.put("saveName", attachments[i].split("/")[1]);
                  cookie.add(map);
               }
            }
         }
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
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

   public String downloadTemplate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'downloadTemplate' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("收藏导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/literature/art_literature_collect.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "音视频管理", "音视频管理", "下载音视频导入模板");
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
         String name = getForm().getRecord().get("name");
         String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "medium" + File.separator
               + getForm().getRecord().get("id") + File.separator + name;
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

   public String export() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'export()' method");
      try {
         initForm();
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("音媒导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artLiteratureMediumService.export(form, pq);

         PrintExcel printExcel = new PrintExcel();

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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "音视频管理", "音视频管理", "导出音视频");
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public void initForm() throws Exception {
      setCode(getForm(), "MEDIUM_TYPE,COMMENT_LEVEL");
   }

   public File getImportFile() {
      return importFile;
   }

   public void setImportFile(File importFile) {
      this.importFile = importFile;
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

   public List<Map<String, String>> getCookie() {
      return cookie;
   }

   public void setCookie(List<Map<String, String>> cookie) {
      this.cookie = cookie;
   }

}
