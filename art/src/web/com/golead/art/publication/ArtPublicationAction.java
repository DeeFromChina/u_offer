package com.golead.art.publication;

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
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.publication.model.ArtPublication;
import com.golead.art.publication.service.ArtPublicationLiteratureService;
import com.golead.art.publication.service.ArtPublicationService;
import com.golead.art.publication.service.ArtPublicationWorksService;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtPublicationAction extends BaseAction {

   private final Log     logger    = LogFactory.getLog(ArtPublicationAction.class);

   private static String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/artist/album/");

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();
      initForm();
      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachPublication();
         else if (PAGE.equalsIgnoreCase(action)) forward = pagePublication();
         else if (ADD.equalsIgnoreCase(action)) forward = addPublication();
         else if (SAVE.equalsIgnoreCase(action)) forward = savePublication();
         else if (EDIT.equalsIgnoreCase(action)) forward = editPublication();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updatePublication();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();
         else if ("GOEXCEL".equalsIgnoreCase(action)) forward = goExcel();
         else if ("EXCEL".equalsIgnoreCase(action)) forward = excel();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = download();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String sreachPublication() throws Exception {
      return LIST;
   }

   public void initForm() throws Exception {
      setCode(form, "PUBLI_TYPE,CURRENCY_TYPE");
   }

   private String pagePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'pagePublication' method");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery = artPublicationService.queryArtPublication(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               String link = "^javascript:view(" + item.get("id") + ");^_self";
               String priceUnit = findCodeName(form, "CURRENCY_TYPE", item.get("priceUnit") == null ? "" : item.get("priceUnit").toString());
               if (!"".equals(priceUnit)) {
                  priceUnit = "(" + priceUnit + ")";
               }
               String time = item.get("publicationYear") == null ? "" : item.get("publicationYear").toString();
               if (!"".equals(time)) {
                  time += "年";
               }
               if (item.get("publicationMonth") != null) {
                  time += item.get("publicationMonth").toString() + "月";
               }
               map.put("id", item.get("id"));
               map.put("data",
                     new Object[] { "", findCodeName(form, "PUBLI_TYPE", item.get("publicationType") + ""), item.get("publicationName") + link,
                           item.get("press"), time, item.get("editor"), item.get("circulation"), item.get("impression"), item.get("pageCount"),
                           (item.get("price") == null ? "" : item.get("price").toString()) + priceUnit });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String addPublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addPublication' method");
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

   private String savePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'savePublication' method");
      try {
         Map<String, String> record = form.getRecord();
         ArtPublication artPublication = new ArtPublication();
         ConvertUtil.convertToModel(artPublication, record);

         artPublicationService.createArtPublication(artPublication, cover, coverFileName, backCover, backCoverFileName, files, filesFileName, FILE_PATH);

         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "添加出版物：" + artPublication.getPublicationName());

         ids = artPublication.getId().toString();
         record.put("next", "true");
         return editPublication();
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   private String editPublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
      String forward = null;
      try {
         ArtPublication artPublication = artPublicationService.getArtPublication(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), artPublication);
         if(artPublication.getArtistId()!=null){
            ArtArtist artArtist = artArtistService.getArtArtist(artPublication.getArtistId());
            form.getRecord().put("artArtistName", artArtist.getCname());
            form.getRecord().put("eName", artArtist.getFolderName());
         }else {
            form.getRecord().put("eName", artPublication.getCatalogName());
         }
         String cover = artPublication.getCover() == null ? "" : artPublication.getCover();
         String backCover = artPublication.getBackCover() == null ? "" : artPublication.getBackCover();
         if (cover.split("/").length > 1) {
            form.getRecord().put("cover", cover.split("/")[1]);
            form.getRecord().put("coverName", cover.split("/")[0]);
         }
         if (backCover.split("/").length > 1) {
            form.getRecord().put("backCover", backCover.split("/")[1]);
            form.getRecord().put("backCoverName", backCover.split("/")[0]);
         }
         String catalogOther = artPublication.getCatalogOther();
         if (catalogOther != null) {
            String[] split = catalogOther.split(",");
            for (int i = 0; i < split.length; i++) {
               if (split[i].split("/").length == 2) {
                  Map<String, String> map = new HashMap<String, String>();
                  map.put("name", split[i].split("/")[0]);
                  map.put("saveName", split[i].split("/")[1]);
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

   private String updatePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'updateWorks' method");
      try {
         Map<String, String> record = form.getRecord();
         artPublicationService.updateArtPublication(record, cover, coverFileName, backCover, backCoverFileName, files, filesFileName, catalogOtherNames,
               FILE_PATH);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "修改出版物：" + record.get(ArtPublication.PROP_PUBLICATION_NAME));
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
      try {
         String id = form.getRecord().get("id");
         ArtWorks works = artWorksService.getArtWorks(Integer.valueOf(id));
         ConvertUtil.objectToMap(getForm().getRecord(), works);
         ArtArtist artist = artArtistService.getArtArtist(works.getArtistId());
         form.getRecord().put("artArtistName", artist.getCname());
         form.getRecord().put("artArtistId", artist.getId().toString());
         form.getRecord().put("chineseName", works.getWorksCName());
         form.getRecord().put("englishName", works.getWorksEName());
         form.getRecord().put("no", works.getWorksNo());
         String partSize = works.getPartSize();
         String[] partSizes = partSize.split(";");
         for (int i = 0; i < partSizes.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            String size = partSizes[i];
            String[] sizes = size.split("\\*");
            String partSize_l = sizes[0];
            String partSize_w = sizes[1];
            map.put("l_" + String.valueOf(i), partSize_l);
            map.put("w_" + String.valueOf(i), partSize_w);
            map.put("id", String.valueOf(i));
            //            partSizeList.add(map);
         }
      }
      catch (Exception e) {
         initForm();
         return ERROR;
      }
      return VIEW;
   }

   //删除的时候把根文件夹路径传进Service然后根据id和艺术家id找到文件，然后删掉
   private String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      try {
         String[] idlist = ids.split(",");
         Integer[] dels = new Integer[idlist.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < idlist.length; i++) {
            dels[i] = Integer.valueOf(idlist[i]);
            ArtPublication artPublication = artPublicationService.getArtPublication(dels[i]);
            if (log.length() > 0) log.append(",");
            log.append(artPublication.getPublicationName());
         }
         artPublicationService.deleteArtPublications(dels, FILE_PATH);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "删除出版物：" + log.toString());
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
         //         String id = form.getRecord().get("id");
         //         File file = new File(FILE_PATH);
         //         if(!file.exists()){
         //            file.mkdirs();
         //         }
         //         String path = FILE_PATH + File.separator + importFileFileName;
         //         FileUtils.fileUpload(path, importFile);
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "下载出版物模板");
         printExcel.doPrint(response, tempFile, wb);// workbook输出到response中。
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("出版物导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artPublicationService.export(form, pq);

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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "导出出版物");
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   @Resource
   private ArtWorksService                 artWorksService;

   @Resource
   private ArtArtistService                artArtistService;

   @Resource
   private ArtPublicationService           artPublicationService;

   @Resource
   private ArtPublicationLiteratureService artPublicationLiteratureService;

   @Resource
   private ArtPublicationWorksService      artPublicationWorksService;

   @Resource
   private ArtSysLogService                artSysLogService;

   private File                            cover;

   private File                            backCover;

   private List<File>                      files;

   private String                          coverFileName;

   private String                          backCoverFileName;

   private String[]                        filesFileName;

   private String                          catalogOtherNames;

   private List<Map<String, String>>       cookie = new ArrayList<Map<String, String>>();

   public File getCover() {
      return cover;
   }

   public void setCover(File cover) {
      this.cover = cover;
   }

   public String getCoverFileName() {
      return coverFileName;
   }

   public void setCoverFileName(String coverFileName) {
      this.coverFileName = coverFileName;
   }

   public File getBackCover() {
      return backCover;
   }

   public void setBackCover(File backCover) {
      this.backCover = backCover;
   }

   public String getBackCoverFileName() {
      return backCoverFileName;
   }

   public void setBackCoverFileName(String backCoverFileName) {
      this.backCoverFileName = backCoverFileName;
   }

   public List<File> getFiles() {
      return files;
   }

   public void setFiles(List<File> files) {
      this.files = files;
   }

   public String[] getFilesFileName() {
      return filesFileName;
   }

   public void setFilesFileName(String[] filesFileName) {
      this.filesFileName = filesFileName;
   }

   public List<Map<String, String>> getCookie() {
      return cookie;
   }

   public void setCookie(List<Map<String, String>> cookie) {
      this.cookie = cookie;
   }

   public String getCatalogOtherNames() {
      return catalogOtherNames;
   }

   public void setCatalogOtherNames(String catalogOtherNames) {
      this.catalogOtherNames = catalogOtherNames;
   }
}
