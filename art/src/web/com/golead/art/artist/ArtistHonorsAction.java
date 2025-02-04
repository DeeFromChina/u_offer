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
import com.golead.art.artist.model.ArtArtistHonors;
import com.golead.art.artist.service.ArtArtistHonorsService;
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

public class ArtistHonorsAction extends BaseAction {

   private Log                    log       = LogFactory.getLog(ArtistHonorsAction.class);

   public static String           FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private File                   files;
   private String                 filesFileName;

   @Resource
   private ArtArtistHonorsService artArtistHonorsService;

   @Resource
   private ArtArtistService       artArtistService;

   @Resource
   private ArtSysLogService       artSysLogService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) {
         forward = LIST;
      }
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listHonors();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageHonors();
         else if (ADD.equalsIgnoreCase(action)) forward = addHonors();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveHonors();
         else if (EDIT.equalsIgnoreCase(action)) forward = editHonors();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateHonors();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteHonors();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewHonors();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importHonors();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
      return forward;
   }

   public String listHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'listHonors()' method");
      try {
         initForm();
         return LIST;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String pageHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering pageHonors() method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pq.getParameters().put("artistId", artistId);
         pq = artArtistHonorsService.queryArtArtistHonors(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String honorLink = "^javascript:view(" + item.get("id") + ")^_self";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("honorTime") + honorLink, item.get("honorDesc") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家荣誉管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addHonors()' method");
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveHonors()' method");
      try {
         initForm();
         ArtArtistHonors artArtistHonors = new ArtArtistHonors();
         ConvertUtil.mapToObject(artArtistHonors, form.getRecord(), false);
         ArtArtist artist = artArtistService.getArtArtist(artArtistHonors.getArtistId());
         artArtistHonorsService.createArtArtistHonors(artArtistHonors);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家荣誉管理",
               "添加荣誉：" + "[" + artist.getCname() + ":" + artArtistHonors.getHonorDesc() + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String editHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editHonors()' method");
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtistHonors artArtistHonors = artArtistHonorsService.getArtArtistHonors(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistHonors);
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateHonors()' method");
      try {
         initForm();
         Map<String, String> record = getForm().getRecord();
         ArtArtist artist = artArtistService.getArtArtist(Integer.valueOf(record.get(ArtArtistHonors.PROP_ARTIST_ID)));
         artArtistHonorsService.updateArtArtistHonors(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家荣誉管理",
               "修改荣誉：" + "[" + artist.getCname() + ":" + record.get(ArtArtistHonors.PROP_HONOR_DESC) + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String deleteHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteHonors()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);

            ArtArtistHonors artistHonors = artArtistHonorsService.getArtArtistHonors(del_ids[i]);
            ArtArtist artist = artArtistService.getArtArtist(artistHonors.getArtistId());
            if (log.length() > 0) log.append(",");
            log.append("[").append(artist.getCname()).append(":").append(artistHonors.getHonorDesc()).append("]");
         }
         artArtistHonorsService.deleteArtArtistHonorss(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家荣誉管理", "删除荣誉：" + log.toString());
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

   public String viewHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewHonors()' method");
      try {
         initForm();
         ArtArtistHonors artArtistHonors = artArtistHonorsService.getArtArtistHonors(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistHonors);
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String importHonors() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewHonors()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "honors");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "honors" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            int artistId = Integer.valueOf(getForm().getRecord().get("artistId"));
            String message = artArtistHonorsService.importHonors(path, artistId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家荣誉管理", "导入艺术家荣誉");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("获奖与荣誉导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist_honors.xls");
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

         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家荣誉管理", "下载艺术家荣誉模板");
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
