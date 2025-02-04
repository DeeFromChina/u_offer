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

import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtAgencyService;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistCoop;
import com.golead.art.artist.service.ArtArtistCoopService;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtistCoopAction extends BaseAction {

   private Log                  log       = LogFactory.getLog(ArtistCoopAction.class);

   public static String         FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private File                 files;
   private String               filesFileName;

   @Resource
   private ArtArtistCoopService artArtistCoopService;

   @Resource
   private ArtAgencyService     artAgencyService;
   
   @Resource
   private ArtSysLogService           artSysLogService;
   
   @Resource
   private ArtArtistService artArtistService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) {
         forward = LIST;
      }
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listCoop();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageCoop();
         else if (ADD.equalsIgnoreCase(action)) forward = addCoop();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveCoop();
         else if (EDIT.equalsIgnoreCase(action)) forward = editCoop();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateCoop();
         else if ("AGENCY".equalsIgnoreCase(action)) forward = agency();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteCoop();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewCoop();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importCoop();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
      return forward;
   }

   public String listCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'listCoop()' method");
      try {
         initForm();
         return LIST;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String pageCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering pageCoop() method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pq.getParameters().put("artistId", artistId);
         pq = artArtistCoopService.queryArtArtistCoop(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String coopLink = "^javascript:view(" + item.get("artAgencyId") + ")^_self";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("agencyCName")+coopLink, item.get("coopTime"), item.get("coopDesc") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家合作机构管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addCoop()' method");
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveCoop()' method");
      try {
         ArtArtistCoop artArtistCoop = new ArtArtistCoop();
         ConvertUtil.convertToModel(artArtistCoop, form.getRecord());
         ArtArtist artArtist = artArtistService.getArtArtist(artArtistCoop.getArtistId());
         artArtistCoopService.createArtArtistCoop(artArtistCoop);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家合作机构管理", "添加合作机构："+artArtist.getCname()+":"+"["+ artArtistCoop.getCoopDesc() +"]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String editCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editCoop()' method");
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtistCoop artArtistCoop = artArtistCoopService.getArtArtistCoop(id);
         if (artArtistCoop.getAgencyId() != null) {
            ArtAgency agency = artAgencyService.getArtAgency(artArtistCoop.getAgencyId());
            form.getRecord().put("agencyName", agency.getAgencyCName());
            form.getRecord().put("agencyId", agency.getId().toString());
         }
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistCoop);
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateCoop()' method");
      try {
         Map<String,String> record = getForm().getRecord();
         Integer artistId = Integer.valueOf(record.get(ArtArtistCoop.PROP_ARTIST_ID));
         ArtArtist artArtist = artArtistService.getArtArtist(artistId);
         artArtistCoopService.updateArtArtistCoop(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家合作机构管理", "修改艺术家合作机构："+artArtist.getCname()+":"+"["+ record.get(ArtArtistCoop.PROP_COOP_DESC) + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private String agency() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'country' method");
      try {
         List<ArtAgency> agencies = artAgencyService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtAgency artAgency : agencies) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artAgency);
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "AGENCY";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public String deleteCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteCoop()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtArtistCoop artArtistCoop = artArtistCoopService.getArtArtistCoop(del_ids[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(artArtistCoop.getArtistId());
            if(log.length()>0)log.append(",");
            log.append(artArtist.getCname()).append(":").append("[").append(artArtistCoop.getCoopDesc()).append("]");
         }
         artArtistCoopService.deleteArtArtistCoops(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家合作机构管理", "修改艺术家合作机构："+log.toString());
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

   public String viewCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewCoop()' method");
      try {
         initForm();
         ArtArtistCoop artArtistCoop = artArtistCoopService.getArtArtistCoop(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistCoop);
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String importCoop() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importCoop()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "coop");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "coop" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            int artistId = Integer.valueOf(getForm().getRecord().get("artistId"));
            String message = artArtistCoopService.importCoops(path, artistId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家合作机构管理", "导入艺术家合作机构");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("合作机构导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist_coop.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家合作机构管理", "下载艺术家合作机构模板");
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

}
