package com.golead.art.literature;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.literature.model.ArtLiteratureWordsResearch;
import com.golead.art.literature.service.ArtLiteratureWordsResearchService;
import com.golead.art.literature.service.ArtLiteratureWordsService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtLiteratureWordsAction extends BaseAction {

   private final Log                         logger          = LogFactory.getLog(ArtLiteratureWordsAction.class);

   private static final String               FILE_PATH       = ServletActionContext.getServletContext().getRealPath("upload/literature/words");

   private static final String               FILE_PATH_INDEX = ServletActionContext.getServletContext().getRealPath("fullindex");

   private List<Map<String, String>>         cookie          = new ArrayList<Map<String, String>>();

   @Resource
   private ArtWorksService                   artWorksService;

   @Resource
   private ArtArtistService                  artArtistService;

   @Resource
   ArtLiteratureWordsService                 artLiteratureWordsService;

   @Resource
   private ArtLiteratureWordsResearchService artLiteratureWordsResearchService;

   @Resource
   private ArtSysLogService          artSysLogService;
   
   private List<File>                        files;
   private String                            filesFileName;
   private String                            filesFileContentType;
   private File                              importFile;
   private String                            importFileFileName;
   private String                            importFileContentType;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachWords();
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageWords();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
         else if ("GOEXCEL".equalsIgnoreCase(action)) forward = goExcel();
         else if ("EXCEL".equalsIgnoreCase(action)) forward = excel();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = download();
         else if ("DOWNLOADFILE".equalsIgnoreCase(action)) forward = downloadFile();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String sreachWords() throws Exception {
      initForm();
      return LIST;
   }

   public void initForm() throws Exception {
      setCode(form, "WORDS_TYPE,COMMENT_LEVEL");
   }

   private String getPageWords() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageWords' method");
      try {
         initForm();
         PageQuery pageQuery = form.getQuery();
         pageQuery = artLiteratureWordsService.queryArtLiteratureWords(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               map.put(
                     "data",
                     new Object[] { "", item.get("CName"), item.get("literatureTitle") + "^javascript:view(" + item.get("id") + ");^_self",
                           item.get("literatureAuther"), item.get("writeTime"), item.get("publicationName"), item.get("publicationPeriod"), item.get("press"),
                           item.get("literatureColumn"), item.get("relevantPages"),
                           findCodeName(form, "WORDS_TYPE", item.get("wordsType") == null ? "" : item.get("wordsType").toString()),
                           item.get("literatureWorks"), item.get("quoteLiterature"), item.get("personInvolved"), item.get("relatedExhib"),
                           item.get("relatedEvent") });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String add() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'add' method");
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

   private String save() throws Exception {
      try {
         Map<String, String> record = form.getRecord();
         artLiteratureWordsService.createArtLiteratureWords(form.getRecord(), files, filesFileName, FILE_PATH, FILE_PATH_INDEX, request);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "添加纸媒文献：" +record.get(ArtLiteratureWords.PROP_PUBLICATION_NAME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   private String edit() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'edit' method");
      String forward = null;
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(Integer.valueOf(id));
         String literatureWorks = artLiteratureWords.getLiteratureWorks();
         String quoteLiterature = artLiteratureWords.getQuoteLiterature();
         String personInvolved = artLiteratureWords.getPersonInvolved();
         String relatedExhib = artLiteratureWords.getRelatedExhib();
         String relatedEvent = artLiteratureWords.getRelatedEvent();
         if (artLiteratureWords.getArtistId() != null) {
            ArtArtist artist = artArtistService.getArtArtist(artLiteratureWords.getArtistId());
            if (artist != null) {
               form.getRecord().put("artistName", artist.getCname());
               form.getRecord().put("artistId", artist.getId().toString());
               form.getRecord().put("cname", artist.getFolderName());
            }
         }
         ArtLiteratureWordsResearch artLiteratureWordsResearch = artLiteratureWordsResearchService.findByWordsId(Integer.valueOf(id));
         if (artLiteratureWordsResearch != null) {
            ConvertUtil.objectToMap(form.getRecord(), artLiteratureWordsResearch);
         }
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureWords);
         getEditPageParam("literatureWorks", literatureWorks);
         getEditPageParam("quoteLiterature", quoteLiterature);
         getEditPageParam("personInvolved", personInvolved);
         getEditPageParam("relatedExhib", relatedExhib);
         getEditPageParam("relatedEvent", relatedEvent);
         String attachment = artLiteratureWords.getAttachment();
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
         forward = ERROR;
      }
      return forward;
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

   private String update() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'update' method");
      try {
         String id = form.getRecord().get("id");
         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(Integer.valueOf(id));
         form.getRecord().get("literatureWorks");
         form.getRecord().put("literatureWorks", form.getRecord().get("literatureWorks"));
         ArtLiteratureWordsResearch artLiteratureWordsResearch = artLiteratureWordsResearchService.findByWordsId(Integer.valueOf(id));
         if (artLiteratureWordsResearch == null) {
            artLiteratureWordsResearch = new ArtLiteratureWordsResearch();
         }
         artLiteratureWordsResearch.setCoreThesis(form.getRecord().get("coreThesis"));
         artLiteratureWordsResearch.setObjectiveEval(form.getRecord().get("objectiveEval"));
         artLiteratureWordsResearch.setSubjectiveEval(form.getRecord().get("subjectiveEval"));
         if (!artLiteratureWords.getArtistId().toString().equals(form.getRecord().get("artistId"))) {
            artLiteratureWordsService.changepath(artLiteratureWords, FILE_PATH, form.getRecord().get("artistId"));
         }
         ConvertUtil.convertToModel(artLiteratureWords, form.getRecord());
         artLiteratureWordsService.updateFile(artLiteratureWordsResearch, artLiteratureWords, files, filesFileName, FILE_PATH,
               form.getRecord().get("fileName"), FILE_PATH_INDEX, request);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "修改纸媒文献：" +artLiteratureWords.getPublicationName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   private String artist() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'artist' method");
      try {
         List<ArtArtist> artArtists = artArtistService.findAllArtArtist();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtArtist artArtist : artArtists) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artArtist);
            if ((";" + ids).indexOf(";" + map.get("id") + ";") > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "ARTIST";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'edit' method");
      String forward = null;
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(Integer.valueOf(id));
         String literatureWorks = artLiteratureWords.getLiteratureWorks();
         String quoteLiterature = artLiteratureWords.getQuoteLiterature();
         String personInvolved = artLiteratureWords.getPersonInvolved();
         String relatedExhib = artLiteratureWords.getRelatedExhib();
         String relatedEvent = artLiteratureWords.getRelatedEvent();
         if (artLiteratureWords.getArtistId() != null) {
            ArtArtist artist = artArtistService.getArtArtist(artLiteratureWords.getArtistId());
            form.getRecord().put("artistName", artist.getCname());
            form.getRecord().put("cname", artist.getFolderName());
         }
         ArtLiteratureWordsResearch artLiteratureWordsResearch = artLiteratureWordsResearchService.findByWordsId(Integer.valueOf(id));
         if (artLiteratureWordsResearch != null) {
            ConvertUtil.objectToMap(form.getRecord(), artLiteratureWordsResearch);
            form.getRecord().put("subjectiveEval", findCodeName(form, "COMMENT_LEVEL", artLiteratureWordsResearch.getSubjectiveEval()));
            form.getRecord().put("objectiveEval", findCodeName(form, "COMMENT_LEVEL", artLiteratureWordsResearch.getObjectiveEval()));
         }
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureWords);
         form.getRecord().put("wordsType", findCodeName(form, "WORDS_TYPE", artLiteratureWords.getWordsType()));
         getEditPageParam("literatureWorks", literatureWorks);
         getEditPageParam("quoteLiterature", quoteLiterature);
         getEditPageParam("personInvolved", personInvolved);
         getEditPageParam("relatedExhib", relatedExhib);
         getEditPageParam("relatedEvent", relatedEvent);
         String attachment = artLiteratureWords.getAttachment();
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
         forward = ERROR;
      }
      return forward;
   }

   private String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      try {
         String[] idlist = ids.split(",");
         Integer[] dels = new Integer[idlist.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < idlist.length; i++) {
            dels[i] = Integer.valueOf(idlist[i]);
            ArtLiteratureWords words = artLiteratureWordsService.getArtLiteratureWords(dels[i]);
            FileUtils.deleteFile(FILE_PATH + File.separator + words.getAttachment());
            artLiteratureWordsResearchService.deleteByWordsId(dels[i]);
            if(log.length()>0)
               log.append(",");
            log.append(words.getPublicationName());
         }
         artLiteratureWordsService.deleteArtLiteratureWordss(dels);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "删除纸媒文献：" +log.toString());
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "上传纸媒文献");         
         FileUtils.fileUpload(path, importFile);
         //         String message = artWorksPeriodService.importArtWorksPeriod(path, Integer.valueOf(id));
         //         if(!"".equals(message)){
         //            if(message.startsWith("成功")){
         //               request.put("msg", message);
         //            }else{
         //               throw new Exception(message);
         //            }
         //         }
         //         return returnForward(message);
         return null;
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("作品时期模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String tempPath = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator + "works" + File.separator
               + "art_works_period.xls";
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "下载纸媒文献");
         printExcel.doPrint(response, tempFile, wb);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   private String downloadFile() throws Exception {
      try {
         String id = form.getRecord().get("id");
         String name = form.getRecord().get("name");
         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(Integer.valueOf(id));
         String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "words" + File.separator + id + File.separator
               + name;
         String[] attachments = artLiteratureWords.getAttachment().split(",");
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
         File tempFile = new File(path);
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

   public String export() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'export()' method");
      try {
         initForm();
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("纸媒导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artLiteratureWordsService.export(form, pq);

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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "纸媒文献管理", "纸媒文献管理", "导出纸媒文献");
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
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

   public String getFilesFileContentType() {
      return filesFileContentType;
   }

   public void setFilesFileContentType(String filesFileContentType) {
      this.filesFileContentType = filesFileContentType;
   }

}
