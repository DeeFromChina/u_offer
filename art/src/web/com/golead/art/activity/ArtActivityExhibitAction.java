package com.golead.art.activity;

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

import com.golead.art.activity.model.ArtActivityExhibit;
import com.golead.art.activity.model.ArtActivityExhibitArtist;
import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtActivityExhibitArtistService;
import com.golead.art.activity.service.ArtActivityExhibitService;
import com.golead.art.activity.service.ArtAgencyService;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.ArtCountryService;
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

public class ArtActivityExhibitAction extends BaseAction {

   private final Log                       logger    = LogFactory.getLog(ArtActivityExhibitAction.class);

   private static final String             FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/photo/");

   private List<Map<String, String>>       cookie    = new ArrayList<Map<String, String>>();

   @Resource
   private ArtArtistService                artArtistService;

   @Resource
   private ArtAgencyService                artAgencyService;

   @Resource
   private ArtCountryService               artCountryService;

   @Resource
   private ArtWorksService                 artWorksService;

   @Resource
   private ArtActivityExhibitService       artActivityExhibitService;

   @Resource
   private ArtSysLogService                artSysLogService;

   @Resource
   private ArtActivityExhibitArtistService artActivityExhibitArtistService;

   private File                            files;
   private String                          filesFileName;
   private String                          filesContentType;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         initForm();
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageDate();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if ("ARTAGENCY".equalsIgnoreCase(action)) forward = artAgency();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();
         else if ("COUNTRY".equalsIgnoreCase(action)) forward = country();
         else if ("ARTWORKS".equalsIgnoreCase(action)) forward = artworks();
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

   public void initForm() throws Exception {
      List<ArtAgency> artAgencies = artAgencyService.findAll();
      setCode(form, "ARTAGENCY", artAgencies, ArtAgency.PROP_AGENCY_C_NAME, ArtAgency.PROP_ID, true);
      setCode(form, "PHOTO_TYPE");
      List<ArtCountry> artCountries = artCountryService.findAll();
      setCode(form, "COUNTRY", artCountries, ArtCountry.PROP_COUNTRY_NAME, ArtCountry.PROP_ID, true);
   }

   private String getPageDate() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageDate' method");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         String exhibType = "";
         String deng = "";
         if ("person".equals(form.getRecord().get("type"))) {
            exhibType = "1";
         }
         if ("people".equals(form.getRecord().get("type"))) {
            exhibType = "2";
            deng = "...等";
         }
         pageQuery.getParameters().put("exhibType", exhibType);
         pageQuery = artActivityExhibitService.queryArtActivityExhibit(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            pageQuery.setRecordCount(String.valueOf(pageQuery.getRecordSet().size()));
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               String[] artists = item.get("cName") == null ? new String[] {} : item.get("cName").toString().split(",");
               StringBuffer atr = new StringBuffer();
               if (artists.length >= 2) {
                  atr.append(artists[0]).append(",").append(artists[1]).append("等");
               }
               else {
                  if (artists.length > 0) {
                     atr.append(artists[0]);
                  }
               }
               map.put("id", item.get("id"));
               map.put("data", new Object[] { "", atr.toString(), item.get("exhibitName") + "^javascript:view(" + item.get("id") + ");^_self",
                     item.get("agencyCName"), item.get("curator"), item.get("activityYear"), item.get("activityMonth")
                     //                           item.get("countryName"), item.get("city"), item.get("worksCName")
               });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "展览管理", "展览管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
         int id = artActivityExhibitService.createArtActivityExhibit(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "展览管理", "展览管理", "添加展览：" + record.get(ArtActivityExhibit.PROP_EXHIBIT_NAME));
         if (id == 0) { throw new Exception(); }
         record.put("id", String.valueOf(id));
         record.put("next", "1");
         return edit();
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
         String id = form.getRecord().get("id");
         ArtActivityExhibit artActivityExhibit = artActivityExhibitService.getArtActivityExhibit(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artActivityExhibit);
         Map<String, String> record = form.getRecord();
         ArtActivityExhibitArtist artActivityExhibitArtist = artActivityExhibitArtistService.findByExhibitId(Integer.valueOf(id));
         if (artActivityExhibitArtist != null) {
            form.getRecord().put("artistId", artActivityExhibitArtist.getArtistId().toString());
            form.getRecord().put("exhibitId", artActivityExhibitArtist.getExhibitId().toString());
         }
         if (artActivityExhibit.getExhibitors() != null && !"".equals(artActivityExhibit.getExhibitors().toString())) {
            ArtAgency artAgency = artAgencyService.getArtAgency(artActivityExhibit.getExhibitors());
            form.getRecord().put("artAgencyName", artAgency.getAgencyCName());
         }
         if (artActivityExhibit.getCountryId() != null && !"".equals(artActivityExhibit.getCountryId().toString())) {
            ArtCountry artCountry = artCountryService.getArtCountry(artActivityExhibit.getCountryId());
            form.getRecord().put("countryName", artCountry.getCountryName());
         }
         if (artActivityExhibitArtist != null) {
            if (artActivityExhibitArtist.getArtistId() != null && !"".equals(artActivityExhibitArtist.getArtistId().toString())) {
               ArtArtist artArtist = artArtistService.getArtArtist(artActivityExhibitArtist.getArtistId());
               form.getRecord().put("artArtistName", artArtist.getCname());
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

   private String update() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'update' method");
      try {
         Map<String, String> record = form.getRecord();
         artActivityExhibitService.updateArtActivityExhibit(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "展览管理", "展览管理", "修改展览：" + record.get(ArtActivityExhibit.PROP_EXHIBIT_NAME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   private String artAgency() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'artAgency' method");
      try {
         List<ArtAgency> artAgencies = artAgencyService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtAgency artAgency : artAgencies) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artAgency);
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "ARTAGENCY";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
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
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
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

   private String artworks() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'artworks' method");
      try {
         List<ArtWorks> artWorks = artWorksService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtWorks works : artWorks) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, works);
            map.put("name", works.getWorksCName());
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "ARTWORKS";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String country() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'country' method");
      try {
         List<ArtCountry> artCountries = artCountryService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtCountry artCountry : artCountries) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artCountry);
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "COUNTRY";
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
      if (logger.isDebugEnabled()) logger.debug("Entering 'view' method");
      String forward = null;
      try {
         String id = form.getRecord().get("id");
         ArtActivityExhibit artActivityExhibit = artActivityExhibitService.getArtActivityExhibit(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artActivityExhibit);
         ArtActivityExhibitArtist artActivityExhibitArtist = artActivityExhibitArtistService.findByExhibitId(Integer.valueOf(id));
         if (artActivityExhibitArtist != null) {
            form.getRecord().put("artistId", artActivityExhibitArtist.getArtistId().toString());
            form.getRecord().put("exhibitId", artActivityExhibitArtist.getExhibitId().toString());
         }
         if (artActivityExhibit.getExhibitors() != null && !"".equals(artActivityExhibit.getExhibitors().toString())) {
            ArtAgency artAgency = artAgencyService.getArtAgency(artActivityExhibit.getExhibitors());
            form.getRecord().put("artAgencyName", artAgency.getAgencyCName());
         }
         if (artActivityExhibit.getCountryId() != null && !"".equals(artActivityExhibit.getCountryId().toString())) {
            ArtCountry artCountry = artCountryService.getArtCountry(artActivityExhibit.getCountryId());
            form.getRecord().put("countryName", artCountry.getCountryName());
         }
         if (artActivityExhibitArtist != null) {
            if (artActivityExhibitArtist.getArtistId() != null && !"".equals(artActivityExhibitArtist.getArtistId().toString())) {
               ArtArtist artArtist = artArtistService.getArtArtist(artActivityExhibitArtist.getArtistId());
               form.getRecord().put("artArtistName", artArtist.getCname());
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
            ArtActivityExhibit activityExhibit = artActivityExhibitService.getArtActivityExhibit(dels[i]);
            if (log.length() > 0) log.append(",");
            log.append(activityExhibit.getExhibitName());
         }
         artSysLogService.createArtSysLog(currentUser.getUserName(), "展览管理", "展览管理", "删除展览：" + log.toString());
         artActivityExhibitService.deleteArtActivityExhibits(dels);
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "展览管理", "展览管理", "下载展览");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("展览导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artActivityExhibitService.export(form, pq);

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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "展览管理", "展览管理", "导出展览");
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

   public String getFilesContentType() {
      return filesContentType;
   }

   public void setFilesContentType(String filesContentType) {
      this.filesContentType = filesContentType;
   }

}
