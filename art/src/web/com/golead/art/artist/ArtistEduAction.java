package com.golead.art.artist;

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
import com.golead.art.artist.model.ArtArtistEdu;
import com.golead.art.artist.service.ArtArtistEduService;
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

public class ArtistEduAction extends BaseAction {

   private Log                           log             = LogFactory.getLog(ArtistEduAction.class);

   public static String                  FILE_PATH       = ServletActionContext.getServletContext().getRealPath("");
   private File                          files;
   private String                        filesFileName;

   private final static SimpleDateFormat dateFormate     = new SimpleDateFormat("yyyy-MM-dd");
   private final static SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

   @Resource
   private ArtArtistEduService           artArtistEduService;

   @Resource
   private ArtSysLogService           artSysLogService;
   
   @Resource
   private ArtArtistService artArtistService;
   
   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) forward = LIST;
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listEdu();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageEdu();
         else if (ADD.equalsIgnoreCase(action)) forward = addEdu();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveEdu();
         else if (EDIT.equalsIgnoreCase(action)) forward = editEdu();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateEdu();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteEdu();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewEdu();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importEdu();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String listEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'listEdu()' method");
      initForm();
      return LIST;
   }

   public String pageEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageEdu()' method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pq.getParameters().put("artistId", artistId);
         pq = artArtistEduService.queryArtArtistEdu(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String eduLink = "^javascript:view(" + item.get("id") + ")^_self";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("eduTime") + eduLink, item.get("eduDesc") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家教育经历与工作管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addEdu()' method");
      try {
         initForm();
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return ADD;
   }

   public String saveEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveEdu()' method");
      try {
         initForm();
         ArtArtistEdu artArtistEdu = new ArtArtistEdu();
         ConvertUtil.mapToObject(artArtistEdu, form.getRecord(), false);
         ArtArtist artArtist = artArtistService.getArtArtist(artArtistEdu.getArtistId());
         artArtistEduService.createArtArtistEdu(artArtistEdu);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家教育经历与工作管理", "添加艺术家教育经历：" + artArtist.getCname() + ":" + "["+artArtistEdu.getEduDesc()+"]");
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

   public String editEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editEdu()' method");
      try {
         initForm();
         ArtArtistEdu artArtistEdu = artArtistEduService.getArtArtistEdu(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistEdu);
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editEdu()' method");
      try {
         initForm();
         Map<String,String> record = getForm().getRecord();
         ArtArtist artArtist = artArtistService.getArtArtist(Integer.valueOf(record.get(ArtArtistEdu.PROP_ARTIST_ID)));
         artArtistEduService.updateArtArtistEdu(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家教育经历与工作管理", "添加艺术家教育经历：" + artArtist.getCname() + ":" + "["+record.get(ArtArtistEdu.PROP_EDU_DESC)+"]");
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editEdu()' method");
      try {
         StringBuffer log = new StringBuffer();
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         for (int i = 0; i < id.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtArtistEdu artArtistEdu = artArtistEduService.getArtArtistEdu(del_ids[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(artArtistEdu.getArtistId());
            if(log.length()>0)
               log.append(",");
            log.append(artArtist.getCname()).append(":").append("[").append(artArtistEdu.getEduDesc()).append("]");
         }
         artArtistEduService.deleteArtArtistEdus(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家教育经历与工作管理", "删除艺术家教育经历：" + log.toString());
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

   public String viewEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editEdu()' method");
      try {
         initForm();
         ArtArtistEdu artArtistEdu = artArtistEduService.getArtArtistEdu(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artArtistEdu);
         return VIEW;
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ERROR;
      }
   }

   public String importEdu() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importEdu()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "edu");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "edu" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            int artistId = Integer.valueOf(getForm().getRecord().get("artistId"));
            String message = artArtistEduService.importEdu(path, artistId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家教育经历与工作管理", "导入艺术家教育经历");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("教育经历与工作导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist_edu.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家教育经历与工作管理", "下载艺术家教育经历模板");
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

   private String formateDate(Object obj) {
      if (obj == null) return "";
      return dateFormate.format(obj);
   }

   private String formateDatetime(Object obj) {
      if (obj == null) return "";
      return datetimeFormate.format(obj);
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
