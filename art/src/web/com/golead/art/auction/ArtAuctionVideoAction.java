package com.golead.art.auction;

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

import com.golead.art.auction.model.ArtAuction;
import com.golead.art.auction.model.ArtAuctionVideo;
import com.golead.art.auction.service.ArtAuctionService;
import com.golead.art.auction.service.ArtAuctionVideoService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtAuctionVideoAction extends BaseAction {

   private Log                           log         = LogFactory.getLog(ArtAuctionVideoAction.class);

   private final static SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

   public static String                  FILE_PATH   = ServletActionContext.getServletContext().getRealPath("");
   private File                          importFile;
   private List<File>                    files;
   private String                        filesFileName;
   private List<Map<String, String>>     video       = new ArrayList<Map<String, String>>();

   @Resource
   private ArtAuctionVideoService        artAuctionVideoService;

   @Resource
   private ArtAuctionService             artAuctionService;

   @Resource
   private ArtSysLogService              artSysLogService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();

      if (action == null) {
         action = LIST;
      }
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = pageVideo();
         else if (ADD.equalsIgnoreCase(action)) forward = addVideo();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveVideo();
         else if (EDIT.equalsIgnoreCase(action)) forward = editVideo();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateVideo();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteVideo();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewVideo();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importVideo();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String pageVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageVideo' method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         pq = artAuctionVideoService.queryArtAuctionVideo(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String link = "^javascript:view(" + item.get("id") + ")^_self";
            map.put("id", item.get("id"));
            map.put("data",
                  new Object[] { "", item.get("videoTheme") + link, formateDate(item.get("videoTime")), item.get("videoSource"), item.get("videoLinkaddr") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关视频管理", "拍卖相关视频管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addVideo' method");
      try {
         initForm();
         return "ADD";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveVideo' method");
      try {
         ArtAuctionVideo artAuctionVideo = new ArtAuctionVideo();
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "video" + File.separator
               + getForm().getRecord().get("auctionId");
         File file = new File(path);
         if (!file.exists()) {
            file.mkdirs();
         }
         ConvertUtil.mapToObject(artAuctionVideo, getForm().getRecord(), false);
         ArtAuction artAuction = artAuctionService.getArtAuction(artAuctionVideo.getAuctionId());
         artAuctionVideoService.createArtAuctionVideo(artAuctionVideo, files, filesFileName, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关视频管理", "拍卖相关视频管理",
               "添加拍卖相关视频：" + artAuction.getAuction() + ":" + artAuctionVideo.getVideoTheme());
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

   public String editVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editVideo' method");
      try {
         initForm();
         int id = Integer.valueOf(getForm().getRecord().get("id"));
         ArtAuctionVideo artAuctionVideo = artAuctionVideoService.getArtAuctionVideo(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artAuctionVideo);
         String videoLink = artAuctionVideo.getVideoLink();
         if (videoLink != null && !"".equals(videoLink)) {
            String[] videoLinks = videoLink.split(";");
            for (String str : videoLinks) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("name", str);
               //               map.put("path", FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "video" + File.separator
               //                     + artAuctionVideo.getAuctionId().toString() + File.separator + str);
               map.put("path", artAuctionVideo.getAuctionId().toString() + File.separator + str);
               video.add(map);
            }
         }
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateVideo' method");
      try {
         initForm();
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "video" + File.separator
               + getForm().getRecord().get("auctionId");
         File file = new File(path);
         if (!file.exists()) {
            file.mkdirs();
         }
         Map<String, String> record = getForm().getRecord();
         Integer auctionId = Integer.valueOf(record.get(ArtAuctionVideo.PROP_AUCTION_ID));
         ArtAuction artAuction = artAuctionService.getArtAuction(auctionId);
         artAuctionVideoService.updateArtAuctionVideo(record, files, record.get("fileName"), filesFileName, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关视频管理", "拍卖相关视频管理",
               "修改拍卖相关视频：" + artAuction.getAuction() + ":" + getForm().getRecord().get(ArtAuctionVideo.PROP_VIDEO_THEME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteVideo' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtAuctionVideo artAuctionVideo = artAuctionVideoService.getArtAuctionVideo(del_ids[i]);
            ArtAuction artAuction = artAuctionService.getArtAuction(artAuctionVideo.getAuctionId());
            if(log.length()>0)log.append(",");
            log.append("[").append(artAuction.getAuction()).append(":").append(artAuctionVideo.getVideoTheme()).append("]");
         }
         String path = FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "video" + File.separator
               + getForm().getRecord().get("auctionId");
         artAuctionVideoService.deleteArtAuctionVideos(del_ids, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关视频管理", "拍卖相关视频管理",
               "删除拍卖相关视频：" + log.toString());
         return pageVideo();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public String viewVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewVideo' method");
      try {
         initForm();
         ArtAuctionVideo artAuctionVideo = artAuctionVideoService.getArtAuctionVideo(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artAuctionVideo);
         String videoLink = artAuctionVideo.getVideoLink();
         if (videoLink != null && !"".equals(videoLink)) {
            String[] videoLinks = videoLink.split(";");
            for (String str : videoLinks) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("name", str);
               //               map.put("path", FILE_PATH + File.separator + "upload" + File.separator + "auction" + File.separator + "video" + File.separator
               //                     + artAuctionVideo.getAuctionId().toString() + File.separator + str);
               map.put("path", artAuctionVideo.getAuctionId().toString() + File.separator + str);
               video.add(map);
            }
         }
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String importVideo() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importVideo' method");
      try {
         if (importFile != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "auction" + File.separator + "video");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "auction" + File.separator + "video" + File.separator
                  + importFile.getName();
            FileUtils.fileUpload(path, importFile);
            int auctionId = Integer.valueOf(getForm().getRecord().get("auctionId"));
            String message = artAuctionVideoService.importVideo(path, auctionId);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关视频管理", "拍卖相关视频管理",
                        "导入拍卖相关视频");
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
      if (log.isDebugEnabled()) log.debug("Entering 'downloadTemplate' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("相关视频导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/auction/art_auction_video.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖相关视频管理", "拍卖相关视频管理",
               "下载导入拍卖相关视频模板");
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

   public String getFilesFileName() {
      return filesFileName;
   }

   public void setFilesFileName(String filesFileName) {
      this.filesFileName = filesFileName;
   }

   public List<Map<String, String>> getVideo() {
      return video;
   }

   public void setVideo(List<Map<String, String>> video) {
      this.video = video;
   }

   public List<File> getFiles() {
      return files;
   }

   public void setFiles(List<File> files) {
      this.files = files;
   }

   public File getImportFile() {
      return importFile;
   }

   public void setImportFile(File importFile) {
      this.importFile = importFile;
   }

}
