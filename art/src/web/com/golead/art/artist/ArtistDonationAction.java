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
import com.golead.art.artist.model.ArtArtistDonation;
import com.golead.art.artist.service.ArtArtistDonationService;
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

public class ArtistDonationAction extends BaseAction {

   private Log                       log       = LogFactory.getLog(ArtistDonationAction.class);

   @Resource
   private ArtArtistDonationService  artArtistDonationService;

   @Resource
   private ArtSysLogService          artSysLogService;

   @Resource
   private ArtArtistService          artArtistService;

   public static String              FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private File                      files;
   private String                    filesFileName;

   private List<Map<String, String>> cookies   = new ArrayList<Map<String, String>>();

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) {
         forward = LIST;
      }
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listDonation();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageDonation();
         else if (ADD.equalsIgnoreCase(action)) forward = addDonation();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveDonation();
         else if (EDIT.equalsIgnoreCase(action)) forward = editDonation();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateDonation();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteDonation();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewDonation();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importDonation();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
      return forward;
   }

   public String listDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'listDonation()' method");
      try {
         initForm();
         return LIST;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String pageDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering pageDonation() method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pq.getParameters().put("artistId", artistId);
         pq = artArtistDonationService.queryArtArtistDonation(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String DonationLink = "^javascript:view(" + item.get("id") + ")^_self";

            String collectWorks = item.get("donationWorks") == null ? "" : item.get("donationWorks").toString();
            StringBuffer worksName = new StringBuffer();
            String a = "<a href='#' onclick='goWorks(";
            if ("".equals(collectWorks.trim())) {
               collectWorks = "0";
            }
            List<ArtWorks> artWorks = artArtistDonationService.findWorksByWorksId(collectWorks.replace("、", ","));
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
            //            String donationWorks = item.get("donationWorks") == null ? "" : item.get("donationWorks").toString();
            //            String worksName = "";
            //            if(!"".equals(donationWorks.trim())){
            //               worksName = artArtistDonationService.findByWorksId(donationWorks.replace("、", ","));
            //            }
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("donationDesc") + DonationLink, item.get("donationTime"), worksName.toString() });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家作品捐赠作管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addDonation()' method");
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveDonation()' method");
      try {
         ArtArtistDonation artArtistDonation = new ArtArtistDonation();
         ConvertUtil.mapToObject(artArtistDonation, form.getRecord(), false);
         ArtArtist artArtist = artArtistService.getArtArtist(artArtistDonation.getArtistId());
         artArtistDonationService.createArtArtistDonation(artArtistDonation);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家作品捐赠作管理",
               "添加艺术家作品捐赠作：" + artArtist.getCname() + ":" + "[" + artArtistDonation.getDonationDesc() + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String editDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editDonation()' method");
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtistDonation artArtistDonation = artArtistDonationService.getArtArtistDonation(id);
         ConvertUtil.objectToMap(form.getRecord(), artArtistDonation);
         String collectWorks = artArtistDonation.getDonationWorks() == null ? "" : artArtistDonation.getDonationWorks();
         if ("".equals(collectWorks.trim())) { return EDIT; }
         List<ArtWorks> artWorks = artArtistDonationService.findWorksByWorksId(collectWorks.replace("、", ","));
         if (artWorks == null) { return EDIT; }
         for (ArtWorks artWorks2 : artWorks) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", artWorks2.getId().toString());
            map.put("name", artWorks2.getWorksCName());
            cookies.add(map);
         }
         String name = artArtistDonationService.findByWorksId(collectWorks.replace("、", ","));
         form.getRecord().put("donationWorksName", name);
         form.getRecord().put("worksName", "1");
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateDonation()' method");
      try {
         Map<String, String> record = getForm().getRecord();
         ArtArtist artArtist = artArtistService.getArtArtist(Integer.valueOf(record.get(ArtArtistDonation.PROP_ARTIST_ID)));
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家作品捐赠作管理",
               "添加艺术家作品捐赠作：" + artArtist.getCname() + ":" + "[" + record.get(ArtArtistDonation.PROP_DONATION_DESC) + "]");
         artArtistDonationService.updateArtArtistDonation(record);
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String deleteDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteDonation()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtArtistDonation artArtistDonation = artArtistDonationService.getArtArtistDonation(del_ids[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(artArtistDonation.getArtistId());
            if (log.length() > 0) log.append(",");
            log.append(artArtist.getCname()).append(":").append("[").append(artArtistDonation.getDonationDesc()).append("]");
         }
         artArtistDonationService.deleteArtArtistDonations(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家作品捐赠作管理", "删除艺术家作品捐赠作：" + log.toString());
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

   public String viewDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewDonation()' method");
      try {
         initForm();
         ArtArtistDonation artArtistDonation = artArtistDonationService.getArtArtistDonation(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistDonation);
         String collectWorks = artArtistDonation.getDonationWorks() == null ? "" : artArtistDonation.getDonationWorks();
         if ("".equals(collectWorks.trim())) { return VIEW; }
         List<ArtWorks> artWorks = artArtistDonationService.findWorksByWorksId(collectWorks.replace("、", ","));
         if (artWorks == null) { return VIEW; }
         for (ArtWorks artWorks2 : artWorks) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", artWorks2.getId().toString());
            map.put("name", artWorks2.getWorksCName());
            cookies.add(map);
         }
         String name = artArtistDonationService.findByWorksId(collectWorks.replace("、", ","));
         form.getRecord().put("donationWorksName", name);
         form.getRecord().put("worksName", "1");
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String importDonation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importDonation()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "donation");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "donation" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            int artistId = Integer.valueOf(getForm().getRecord().get("artistId"));
            String message = artArtistDonationService.importDonations(path, artistId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家作品捐赠作管理", "导入艺术家作品捐赠作");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("作品捐赠导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist_donation.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家作品捐赠作管理", "下载艺术家作品捐赠作模板");
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
