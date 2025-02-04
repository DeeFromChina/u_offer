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
import com.golead.art.literature.model.ArtLiteratureNetwork;
import com.golead.art.literature.model.ArtLiteratureNetworkResearch;
import com.golead.art.literature.service.ArtLiteratureNetworkResearchService;
import com.golead.art.literature.service.ArtLiteratureNetworkService;
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

public class ArtLiteratureNetworkAction extends BaseAction {

   private final Log                   logger    = LogFactory.getLog(ArtLiteratureNetworkAction.class);

   private static final String         FILE_PATH = ServletActionContext.getServletContext().getRealPath("");

   private List<Map<String, String>>   cookie    = new ArrayList<Map<String, String>>();

   @Resource
   private ArtWorksService             artWorksService;

   @Resource
   private ArtArtistService            artArtistService;

   @Resource
   ArtLiteratureNetworkService         artLiteratureNetworkService;

   @Resource
   ArtLiteratureNetworkResearchService artLiteratureNetworkResearchService;
   
   @Resource
   private ArtSysLogService          artSysLogService;

   private List<File>                  files;
   private String                      filesFileName;
   private String                      filesFileContentType;
   private File                        importFile;
   private String                      importFileFileName;
   private String                      importFileContentType;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachNetwork();
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageNetwork();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();
         else if ("SHOW".equalsIgnoreCase(action)) forward = show();
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

   public String sreachNetwork() throws Exception {
      initForm();
      return LIST;
   }

   public void initForm() throws Exception {
      setCode(form, "WORDS_TYPE,COMMENT_LEVEL");
   }

   private String getPageNetwork() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageNetwork' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery.getParameters().put("id", id);
         pageQuery = artLiteratureNetworkService.queryArtLiteratureNetwork(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               if (item.get("id") == null) {
                  break;
               }
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               map.put(
                     "data",
                     new Object[] { "", item.get("CName"), item.get("literatureTitle") + "^javascript:view(" + item.get("id") + ");^_self",
                           item.get("literatureAuther"), item.get("publicationTime"), item.get("literatureSource"),
                           findCodeName(form, "WORDS_TYPE", item.get("literatureType") == null ? "" : item.get("literatureType").toString()),
                           item.get("literatureWorks"), item.get("quoteLiterature"), item.get("personInvolved"), item.get("relatedExhib"),
                           item.get("relatedEvent") });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
         initForm();
         ArtLiteratureNetwork artLiteratureNetwork = new ArtLiteratureNetwork();
         ConvertUtil.convertToModel(artLiteratureNetwork, form.getRecord());
         artLiteratureNetworkService.createArtLiteratureNetwork(artLiteratureNetwork);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "添加网络文献：" + artLiteratureNetwork.getLiteratureTitle());
         ArtLiteratureNetworkResearch artLiteratureNetworkResearch = new ArtLiteratureNetworkResearch();
         ConvertUtil.convertToModel(artLiteratureNetworkResearch, form.getRecord());
         artLiteratureNetworkResearch.setNetworkId(artLiteratureNetwork.getId());
         artLiteratureNetworkResearchService.createArtLiteratureNetworkResearch(artLiteratureNetworkResearch);
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
         ArtLiteratureNetwork artLiteratureNetwork = artLiteratureNetworkService.getArtLiteratureNetwork(Integer.valueOf(id));
         String literatureWorks = artLiteratureNetwork.getLiteratureWorks();
         String quoteLiterature = artLiteratureNetwork.getQuoteLiterature();
         String personInvolved = artLiteratureNetwork.getPersonInvolved();
         String relatedExhib = artLiteratureNetwork.getRelatedExhib();
         String relatedEvent = artLiteratureNetwork.getRelatedEvent();
         ArtArtist artist = artArtistService.getArtArtist(artLiteratureNetwork.getArtistId());
         form.getRecord().put("artistName", artist.getCname());
         form.getRecord().put("artistId", artist.getId().toString());
         ArtLiteratureNetworkResearch artLiteratureNetworkResearch = artLiteratureNetworkResearchService.findByNetworkId(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureNetworkResearch);
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureNetwork);
         getEditPageParam("literatureWorks", literatureWorks);
         getEditPageParam("quoteLiterature", quoteLiterature);
         getEditPageParam("personInvolved", personInvolved);
         getEditPageParam("relatedExhib", relatedExhib);
         getEditPageParam("relatedEvent", relatedEvent);
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
         initForm();
         Map<String, String> record = form.getRecord();
         artLiteratureNetworkService.updateArtLiteratureNetwork(form.getRecord());
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "修改网络文献：" + record.get(ArtLiteratureNetwork.PROP_LITERATURE_TITLE));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
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

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'edit' method");
      String forward = null;
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtLiteratureNetwork artLiteratureNetwork = artLiteratureNetworkService.getArtLiteratureNetwork(Integer.valueOf(id));
         String literatureWorks = artLiteratureNetwork.getLiteratureWorks();
         String quoteLiterature = artLiteratureNetwork.getQuoteLiterature();
         String personInvolved = artLiteratureNetwork.getPersonInvolved();
         String relatedExhib = artLiteratureNetwork.getRelatedExhib();
         String relatedEvent = artLiteratureNetwork.getRelatedEvent();
         ArtArtist artist = artArtistService.getArtArtist(artLiteratureNetwork.getArtistId());
         form.getRecord().put("artistName", artist.getCname());
         form.getRecord().put("artistId", artist.getId().toString());
         ArtLiteratureNetworkResearch artLiteratureNetworkResearch = artLiteratureNetworkResearchService.findByNetworkId(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureNetworkResearch);
         ConvertUtil.objectToMap(form.getRecord(), artLiteratureNetwork);
         form.getRecord().put("subjectiveEval", findCodeName(form, "COMMENT_LEVEL", artLiteratureNetworkResearch.getSubjectiveEval()));
         form.getRecord().put("objectiveEval", findCodeName(form, "COMMENT_LEVEL", artLiteratureNetworkResearch.getObjectiveEval()));
         form.getRecord().put("literatureType", findCodeName(form, "WORDS_TYPE", artLiteratureNetwork.getLiteratureType()));
         getEditPageParam("literatureWorks", literatureWorks);
         getEditPageParam("quoteLiterature", quoteLiterature);
         getEditPageParam("personInvolved", personInvolved);
         getEditPageParam("relatedExhib", relatedExhib);
         getEditPageParam("relatedEvent", relatedEvent);
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
            if(log.length()>0)log.append(",");
            ArtLiteratureNetwork artLiteratureNetwork = artLiteratureNetworkService.getArtLiteratureNetwork(dels[i]);
            log.append(artLiteratureNetwork.getLiteratureTitle());
            artLiteratureNetworkResearchService.deleteByNetworkId(dels[i]);
         }
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "删除网络文献："+log.toString());
         artLiteratureNetworkService.deleteArtLiteratureNetworks(dels);
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return null;
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "上传网络文献");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "下载网络文献");
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
         //         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(Integer.valueOf(id));
         String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "words" + File.separator + id + File.separator
               + name;
         //         String[] attachments = artLiteratureWords.getAttachment().split(",");
         //         for(int i = 0; i<attachments.length; i++){
         //            if(attachments[i].endsWith(name)){
         //               name = attachments[i].split("/")[0];
         //               break;
         //            }
         //         }
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
         HSSFWorkbook hssfWorkbook = artLiteratureNetworkService.export(form, pq);
         
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "网络文献管理", "网络文献管理", "下载导入网络文献模板");
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
