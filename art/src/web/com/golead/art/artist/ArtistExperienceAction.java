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
import com.golead.art.artist.model.ArtArtistExperience;
import com.golead.art.artist.model.ArtArtistHonors;
import com.golead.art.artist.service.ArtArtistExperienceService;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.common.service.system.SysUserService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtistExperienceAction extends BaseAction {

   private Log                        log       = LogFactory.getLog(ArtArtistExperienceService.class);

   public static String               FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private File                       files;
   private String                     filesFileName;

   @Resource
   private ArtArtistExperienceService artArtistExperienceService;

   @Resource
   private ArtArtistService           artArtistService;

   @Resource
   private SysUserService             sysUserService;

   @Resource
   private ArtSysLogService           artSysLogService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();

      if (action == null) {
         forward = LIST;
      }
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listExperience();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageExperience();
         else if (ADD.equalsIgnoreCase(action)) forward = addExperience();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveExperience();
         else if (EDIT.equalsIgnoreCase(action)) forward = editExperience();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateExperience();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteExperience();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewExperience();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importExperience();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String listExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'list()' method");
      initForm();
      return LIST;
   }

   public String pageExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageExperience()' method");
      try {
         initForm();
         PageQuery pq = form.getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pq.getParameters().put("artistId", artistId);
         pq = artArtistExperienceService.queryArtArtistExperience(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String artistLink = "^javascript:view(" + item.get("id") + ")^_self";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("expeTime") + artistLink, item.get("lifeExperience"), item.get("historyExperience") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家经历管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addExperience()' method");
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

   public String saveExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveExperience()' method");
      try {
         initForm();
         
         ArtArtistExperience artArtistExperience = new ArtArtistExperience();
         ConvertUtil.mapToObject(artArtistExperience, form.getRecord(), false);
         
         Integer artistId = Integer.valueOf(form.getRecord().get("artistId"));
         ArtArtist artist = artArtistService.getArtArtist(artistId);
         
         artArtistExperience.setArtId(artistId);
         
         artArtistExperienceService.createArtArtistExperience(artArtistExperience);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家经历管理",
               "添加艺术家经历：" + artist.getCname() + ":" + "[" + artArtistExperience.getLifeExperience() + "," + artArtistExperience.getHistoryExperience() + "]");
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

   public String editExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editExperience()' method");
      String forward = null;
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtistExperience artArtistExperience = artArtistExperienceService.getArtArtistExperience(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistExperience);
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   public String updateExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateExperience()' method");
      try {
         initForm();
         Map<String, String> record = getForm().getRecord();
         Integer artistId = Integer.valueOf(form.getRecord().get("artistId"));
         ArtArtist artist = artArtistService.getArtArtist(artistId);
         artArtistExperienceService.updateArtArtistExperience(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家经历管理",
               "添加艺术家经历：" + artist.getCname() + ":" + "[" + record.get(ArtArtistExperience.PROP_LIFE_EXPERIENCE) + "," + record.get(ArtArtistExperience.PROP_HISTORY_EXPERIENCE) + "]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteExperience()' method");
      try {
         StringBuffer log = new StringBuffer();
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         for (int i = 0; i < id.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtArtistExperience artistExperience = artArtistExperienceService.getArtArtistExperience(del_ids[i]);
            ArtArtist artist = artArtistService.getArtArtist(artistExperience.getArtId());
            if (log.length() > 0) log.append(",");
            log.append(artist.getCname()).append(":").append("[").append(artistExperience.getLifeExperience()).append(",").append(artistExperience.getHistoryExperience()).append("]");
         }
         artArtistExperienceService.deleteArtArtistExperiences(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家经历管理", "删除艺术家经历：" + log.toString());
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

   public String viewExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewExperience()' method");
      try {
         initForm();
         ArtArtistExperience artArtistExperience = artArtistExperienceService.getArtArtistExperience(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistExperience);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return VIEW;
   }

   public String importExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importExperience()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "experience");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "experience" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            int artistId = Integer.valueOf(getForm().getRecord().get("artistId"));
            String message = artArtistExperienceService.importExperience(path, artistId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家经历管理", "导入艺术家经历");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("人生年表导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist_experience.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家经历管理", "下载艺术家经历模板");
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
