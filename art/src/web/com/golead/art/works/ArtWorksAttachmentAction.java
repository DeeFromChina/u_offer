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
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksAttachment;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksAttachmentService;
import com.golead.art.works.service.ArtWorksPeriodService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtWorksAttachmentAction extends BaseAction {

   private final Log                 logger    = LogFactory.getLog(ArtWorksAttachmentAction.class);

   private static final String       FILE_PATH = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator
         + "works" + File.separator + "period";

   private SimpleDateFormat          sdf       = new SimpleDateFormat("yyyy-MM-dd");

   private List<Map<String, String>> cookie    = new ArrayList<Map<String, String>>();

   @Resource
   private ArtWorksPeriodService     artWorksPeriodService;

   @Resource
   private ArtWorksService           artWorksService;

   @Resource
   private ArtArtistService          artArtistService;

   @Resource
   ArtWorksAttachmentService         artWorksAttachmentService;

   @Resource
   private ArtSysLogService          artSysLogService;

   private File                      importFile;
   private String                    importFileFileName;
   private String                    importFileContentType;

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
         //         else if (EDIT.equalsIgnoreCase(action)) forward = editPeriod();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updatePeriod();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if ("SHOW".equalsIgnoreCase(action)) forward = show();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public void initForm() throws Exception {
      setCode(form, "ART_TYPE,DATE_YEAR,MEDIUM_TYPE,SIGNATURE,PERIOD_TYPE");
   }

   private String getPageDate() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageDate' method");
      try {
         initForm();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         //         Map<String, Integer> worksMap = new HashMap<String, Integer>();
         //         List<ArtWorks> artWorks = artWorksService.findAll();
         //         List<ArtArtist> artArtists = artArtistService.findAllArtArtist();
         //         Map<Integer, String> cNameMap = new HashMap<Integer, String>();
         //         Map<Integer, String> eNameMap = new HashMap<Integer, String>();
         //         for(ArtArtist artArtist : artArtists){
         //            cNameMap.put(artArtist.getId(), artArtist.getCname());
         //            eNameMap.put(artArtist.getId(), artArtist.getEname());
         //         }
         pageQuery = artWorksAttachmentService.queryArtWorksAttachment(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               if (item.get("id") == null) {
                  break;
               }
               String flag = "";
               if (!"".equals(item.get("fileName").toString())) {
                  flag = "查看作品^null^" + item.get("id").toString();
               }
               else {
                  flag = "null^查看作品";
               }
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               map.put("data", new Object[] { item.get("style_type").toString(), item.get("cName") + "(" + item.get("eName") + ")",
                     item.get("chineseName") + "(" + item.get("englishName") + ")", item.get("fileDesc"), flag });
               list.add(map);
               //               worksMap.put(item.get("id").toString(), Integer.valueOf(item.get("id").toString()));
            }
         }
         //         pageQuery.setRecordCount(String.valueOf(artWorks.size()));
         //         int pageCount = 1;
         //         pageCount = artWorks.size()/10;
         //         pageQuery.setPageCount(String.valueOf(pageCount+1));
         //         for(ArtWorks aWorks : artWorks){
         //            if(worksMap.get(aWorks.getId().toString()) == null){
         //               Map<String, Object> map = new HashMap<String, Object>();
         //               map.put("id", aWorks.getId());
         //               map.put("data", new Object[]{
         //                     style_typeaWorks.getStyle_type(),
         //                     aWorks.getWorksCName() + "(" + aWorks.getWorksEName() + ")",
         //                     cNameMap.get(aWorks.getArtistId()) + "(" + eNameMap.get(aWorks.getArtistId()) + ")",
         //                     "",
         //                     "null^查看作品"
         //               });
         //               list.add(map);
         //            }
         //         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品附件管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
         form.getRecord().put("worksId", form.getRecord().get("worksId"));
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
         String worksId = form.getRecord().get("worksId");
         ArtWorksAttachment attachment = artWorksAttachmentService.findByWorksId(Integer.valueOf(worksId));
         String name = form.getRecord().get("name");
         ArtWorks artWorks = artWorksService.getArtWorks(Integer.valueOf(worksId));
         if (attachment != null) {
            artWorksAttachmentService.deleteByWorksId(Integer.valueOf(worksId));
         }
         ArtWorksAttachment artWorksAttachment = new ArtWorksAttachment();
         artWorksAttachment.setFileDesc(form.getRecord().get("fileDesc"));
         if (artWorks.getThumbnail().length() != 0) {
            String[] thumbnails = artWorks.getThumbnail().split(",");
            for (int i = 0; i < thumbnails.length; i++) {
               if (thumbnails[i].endsWith(name)) {
                  artWorksAttachment.setFileName(thumbnails[i]);
                  break;
               }
            }
         }
         artWorksAttachment.setIsCover("1");
         artWorksAttachmentService.createArtWorksAttachment(artWorksAttachment);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品附件管理",
               "添加作品作品附件：" + artWorks.getWorksCName() + ":" + "[" + artWorksAttachment.getFileName() + "]");
         //         request.put("msg", "成功设置！");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   //   private String editPeriod() throws Exception {
   //      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
   //      String forward = null;
   //      try {
   //         initForm();
   //         String id = form.getRecord().get("id");
   //         ArtWorksPeriod artWorksPeriod = artWorksPeriodService.getArtWorksPeriod(Integer.valueOf(id));
   //         ConvertUtil.objectToMap(getForm().getRecord(), artWorksPeriod);
   //         ArtArtistWorksPeriod onePeriod = artArtistWorksPeriodService.findOneArtArtistWorksPeriod(form.getRecord());
   //         if(onePeriod != null){
   //            form.getRecord().put("artistworksPeriodId", onePeriod.getId().toString());
   //         }else{
   //            form.getRecord().put("artistworksPeriodId", "");
   //         }
   //         List<ArtArtistWorksPeriod> periods = artArtistWorksPeriodService.findAllGroupPeriods();
   //         for(ArtArtistWorksPeriod period : periods){
   //            Map<String, String> map = new HashMap<String, String>();
   //            map.put("name", period.getPeriodName());
   //            map.put("id", period.getId().toString());
   //            map.put("type", period.getPeriodType());
   //            map.put("marquee", "0");
   //            if(period.getPeriodName().length() > 11){
   //               map.put("marquee", "1");
   //            }
   //            cookie.add(map);
   //         }
   //         return EDIT;
   //      }
   //      catch (Exception e) {
   //         e.printStackTrace();
   //         forward = ERROR;
   //      }
   //      return forward;
   //   }

   private String updatePeriod() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'updateWorks' method");
      try {
         initForm();
         Map<String,String> record = form.getRecord();
         Integer worksId = Integer.valueOf(record.get(ArtWorksAttachment.PROP_WORKS_ID));
         ArtWorks artWorks = artWorksService.getArtWorks(worksId);
         artWorksPeriodService.updateArtWorksPeriod(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品附件管理",
               "添加作品作品附件：" + artWorks.getWorksCName() + ":" + "[" + record.get(ArtWorksAttachment.PROP_FILE_NAME) + "]");
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

   private String show() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'show' method");
      try {
         String id = form.getRecord().get("id");
         ArtWorksAttachment artWorksAttachment = artWorksAttachmentService.getArtWorksAttachment(Integer.valueOf(id));
         String name = artWorksAttachment.getFileName();
         name = name.split("/")[1];
         form.getRecord().put("name", name);
         form.getRecord().put("worksId", artWorksAttachment.getWorksId().toString());
         form.getRecord().put("fileDesc", artWorksAttachment.getFileDesc());
         return "SHOW";
      }
      catch (Exception e) {
         e.printStackTrace();
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
         String message = artWorksPeriodService.importArtWorksPeriod(path, Integer.valueOf(id));
         if (!"".equals(message)) {
            if (message.startsWith("成功")) {
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品附件管理",
                     "导入作品附件");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品附件管理",
               "下载作品附件模板");
         printExcel.doPrint(response, tempFile, wb);// workbook输出到response中。
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

}
