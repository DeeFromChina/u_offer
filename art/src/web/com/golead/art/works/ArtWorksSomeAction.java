package com.golead.art.works;

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
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.ArtArtistWorksSeriesService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksSome;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.service.ArtMediumService;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.art.works.service.ArtWorksSomeService;
import com.golead.art.works.service.ArtWorksStyleService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtWorksSomeAction extends BaseAction {

   private final Log                   logger    = LogFactory.getLog(ArtWorksSomeAction.class);
   public static String                FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private List<Map<String, String>>   cookie    = new ArrayList<Map<String, String>>();
   private List<Map<String, String>>   pic       = new ArrayList<Map<String, String>>();
   private List<File>                  files;
   private String                      filesFileName;
   private String                      filesContentType;
   private File                        importFile;
   private String                      importFileFileName;
   private String                      importFileContentType;

   @Resource
   private ArtWorksSomeService         artWorksSomeService;

   @Resource
   private ArtArtistWorksSeriesService artArtistWorksSeriesService;

   @Resource
   private ArtWorksService             artWorksService;

   @Resource
   private ArtWorksStyleService        artWorksStyleService;

   @Resource
   private ArtMediumService            artMediumService;

   @Resource
   private ArtArtistService            artArtistService;
   
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
         else if (ADD.equalsIgnoreCase(action)) forward = addSome();
         else if ("ADDPAGE".equalsIgnoreCase(action)) forward = getAddSome();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveSome();
         else if ("AJAXSAVE".equalsIgnoreCase(action)) forward = ajaxSaveSome();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();
         else if ("SHOW".equalsIgnoreCase(action)) forward = show();
         else if ("GOEXCEL".equalsIgnoreCase(action)) forward = goExcel();
         else if ("EXCEL".equalsIgnoreCase(action)) forward = excel();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = download();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public void initForm() throws Exception {
      List<ArtArtist> artArtists = artArtistService.findAllArtArtist();
      setCode(form, "ART_TYPE,DATE_YEAR,MEDIUM_TYPE,SIGNATURE,PERIOD_TYPE,EVAL_TYPE");
      setCode(form, "artist", artArtists, ArtArtist.PROP_C_NAME, ArtArtist.PROP_ID, true);
      List<ArtArtistWorksSeries> artArtistWorksSeries = artArtistWorksSeriesService.findAll();
      setCode(form, "WORKSSERIES", artArtistWorksSeries, ArtArtistWorksSeries.PROP_SERIES_NAME, ArtArtistWorksSeries.PROP_ID, true);
   }

   private String getPageDate() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageDate' method");
      try {
         initForm();
         String worksId = form.getRecord().get("id");
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery.getParameters().put("worksId", worksId);
         pageQuery = artWorksSomeService.queryArtWorksSome(pageQuery);
         Map<String, String> medium = new HashMap<String, String>();
         List<ArtMedium> artMediums = artMediumService.findAll();
         for (ArtMedium artMedium : artMediums) {
            medium.put(artMedium.getId().toString(), artMedium.getMediumName());
         }
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               String link = "^javascript:view(" + item.get("someWorksId") + ");^_self";
               map.put(
                     "data",
                     new Object[] { "", item.get("worksName") + link, item.get("year"),
                           medium.get(item.get("material") == null ? "" : item.get("material").toString()),
                           medium.get(item.get("shape") == null ? "" : item.get("shape").toString()) });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "相关作品管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String addSome() throws Exception {
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

   private String getAddSome() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getAddSome' method");
      try {
         initForm();
         String worksId = form.getRecord().get("worksId");
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         List<ArtWorksSome> artWorksSomes = artWorksSomeService.findByWorksId(Integer.valueOf(worksId));
         PageQuery pageQuery = form.getQuery();
         pageQuery = artWorksService.queryArtWorks(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               String createTime = "";
               String createYear = returnString(item.get("createYear"));
               createTime = addString(createTime, createYear, "年");
               String createMonth = returnString(item.get("createMonth"));
               createTime = addString(createTime, createMonth, "月");
               String createDay = returnString(item.get("createDay"));
               createTime = addString(createTime, createDay, "日");
               String styleType = returnString(item.get("styleType"));
               String tmpStyle = "";
               if (styleType != null && !"".equals(styleType)) {
                  String[] styleTypes = styleType.split(";");
                  for (int i = 0; i < styleTypes.length; i++) {
                     ArtWorksStyle style = artWorksStyleService.getArtWorksStyle(Integer.valueOf(styleTypes[i]));
                     tmpStyle += style.getStyleName() + ";";
                  }
               }
               map.put(
                     "data",
                     new Object[] { "", item.get("c_name") + "(" + item.get("e_name") + ")",
                           item.get("chineseName") + "(" + (item.get("englishName") == null ? "" : item.get("englishName")) + ")", tmpStyle,
                           findCodeName(form, "WORKSSERIES", returnString(item.get("worksSeries"))), createTime });
               boolean isPass = true;
               for (ArtWorksSome some : artWorksSomes) {
                  if (some.getSomeWorksId() == Integer.valueOf(item.get("id").toString())) {
                     isPass = false;
                  }
               }
               if (isPass) {
                  list.add(map);
               }
            }
         }
         String res = genGridJson(list);
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

   private String addString(String str, String equalStr, String addStr) {
      if (!"".equals(equalStr)) {
         if (equalStr.indexOf(addStr) > -1) return str + equalStr;
         else return str + equalStr + addStr;
      }
      return str;
   }

   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   private String saveSome() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'saveSome' method");
      try {
         String worksId = form.getRecord().get("worksId");
         StringBuffer log = new StringBuffer();
         ArtWorks artWorks = artWorksService.getArtWorks(worksId);
         log.append(artWorks.getWorksCName()).append(":").append("[");
         String[] idlist = ids.split(",");
         Integer[] adds = new Integer[idlist.length];
         for (int i = 0; i < idlist.length; i++) {
            adds[i] = Integer.valueOf(idlist[i]);
            if(i>0)log.append(",");
            log.append(artWorksService.getArtWorks(adds[i]).getWorksCName());
         }
         log.append("]");
         artWorksSomeService.createArtWorksSome(adds, worksId);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "相关作品管理", "添加相关作品：" + log.toString());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }
   
   private String ajaxSaveSome() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'ajaxSaveSome' method");
      try {
         String worksId = form.getRecord().get("worksId");
         String[] idlist = ids.split(",");
         Integer[] adds = new Integer[idlist.length];
         for (int i = 0; i < idlist.length; i++) {
            adds[i] = Integer.valueOf(idlist[i]);
         }
         artWorksSomeService.createArtWorksSome(adds, worksId);
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
   

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'view' method");
      try {
         initForm();
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

   private String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      try {
         String[] idlist = ids.split(",");
         Integer[] dels = new Integer[idlist.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < idlist.length; i++) {
            dels[i] = Integer.valueOf(idlist[i]);
            if(log.length()>0)log.append(",");
            ArtWorksSome artWorksSome = artWorksSomeService.getArtWorksSome(dels[i]);
            ArtWorks artWorks = artWorksService.getArtWorks(artWorksSome.getWorksId());
            ArtWorks someWorks = artWorksService.getArtWorks(artWorksSome.getSomeWorksId());
            log.append("[").append(artWorks.getWorksCName()).append(":").append(someWorks.getWorksCName()).append("]");
         }
         artWorksSomeService.deleteArtWorksSomes(dels);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "相关作品管理", "删除相关作品：" + log.toString());
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
         String id = form.getRecord().get("id");
         File file = new File(FILE_PATH);
         if (!file.exists()) {
            file.mkdirs();
         }
         String path = FILE_PATH + File.separator + importFileFileName;
         FileUtils.fileUpload(path, importFile);
         String message = artWorksSomeService.importArtWorksSome(path, Integer.valueOf(id));
         if (!"".equals(message)) {
            if (message.startsWith("成功")) {
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "相关作品管理", "导入相关作品");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("相关作品模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String tempPath = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator + "works" + File.separator
               + "art_works_some.xls";
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "相关作品管理", "下载导入相关作品模板");
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
