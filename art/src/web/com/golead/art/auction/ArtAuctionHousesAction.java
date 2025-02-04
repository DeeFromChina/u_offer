package com.golead.art.auction;

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

import com.golead.art.auction.model.ArtAuctionHouses;
import com.golead.art.auction.service.ArtAuctionHousesService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtAuctionHousesAction extends BaseAction {

   private Log                     log       = LogFactory.getLog(ArtAuctionHousesAction.class);

   public static String            FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
   private File                    files;
   private String                  filesFileName;

   @Resource
   private ArtAuctionHousesService artAuctionHousesService;
   
   @Resource
   private ArtSysLogService        artSysLogService;

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
         else if (PAGE.equalsIgnoreCase(action)) forward = pageHouses();
         else if (ADD.equalsIgnoreCase(action)) forward = addHouses();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveHouses();
         else if (EDIT.equalsIgnoreCase(action)) forward = editHouses();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateHouses();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteHouses();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewHouses();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importHouses();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String pageHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageHouses()' method");
      try {
         PageQuery pq = getForm().getQuery();
         pq = artAuctionHousesService.queryArtAuctionHouses(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String housesLink = "^javascript:view(" + item.get("id") + ")^_self";
            map.put("id", item.get("id"));
            map.put(
                  "data",
                  new Object[] { "", item.get("auctionHouse") + housesLink, item.get("nationality"), item.get("addr"), item.get("website"), item.get("remark") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addHouses()' method");
      try {
         initForm();
         return "ADD";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveHouses()' method");
      try {
         ArtAuctionHouses artAuctionHouses = new ArtAuctionHouses();
         artAuctionHouses.setAddr(getForm().getRecord().get("addr"));
         artAuctionHouses.setAuctionHouse(getForm().getRecord().get("auctionHouse"));
         artAuctionHouses.setNationality(getForm().getRecord().get("nationality"));
         artAuctionHouses.setRemark(getForm().getRecord().get("remark"));
         artAuctionHouses.setWebsite(getForm().getRecord().get("website"));
         artAuctionHousesService.createArtAuctionHouses(artAuctionHouses);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "添加拍卖行：" + artAuctionHouses.getAuctionHouse());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   public String editHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editHouses()' method");
      try {
         initForm();
         int id = Integer.valueOf(getForm().getRecord().get("id"));
         ArtAuctionHouses artAuctionHouses = artAuctionHousesService.getArtAuctionHouses(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artAuctionHouses);
         return "EDIT";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateHouses()' method");
      try {
         artAuctionHousesService.updateArtAuctionHouses(getForm().getRecord());
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "修改拍卖行：" + getForm().getRecord().get(ArtAuctionHouses.PROP_AUCTION_HOUSE));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteHouses()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            if(log.length()>0)log.append(",");
            ArtAuctionHouses artAuctionHouses = artAuctionHousesService.getArtAuctionHouses(del_ids[i]);
            log.append(artAuctionHouses.getAuctionHouse());
         }
         String message = artAuctionHousesService.deleteArtAuctionHousess(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "删除拍卖行：" + log.toString());
         if (!"".equals(message)) {
            setResponse("{exit:1,message:\"" + message + "\"}");
         }
         else {
            setResponse("{exit:0}");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return null;
   }

   public String viewHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewHouses()' method");
      try {
         initForm();
         ArtAuctionHouses artAuctionHouses = artAuctionHousesService.getArtAuctionHouses(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), artAuctionHouses);
         return "VIEW";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public String importHouses() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importHouses()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "auction" + File.separator + "houses");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "auction" + File.separator + "houses" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            String message = artAuctionHousesService.importHouses(path);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "导入拍卖行");
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("拍卖行维护导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/auction/art_auction_houses.xls");
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "下载导入拍卖行模板");
         printExcel.doPrint(response, templateFile, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public String export() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'export()' method");
      try {
         initForm();
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("拍卖行导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artAuctionHousesService.export(form, pq);
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "拍卖行管理", "拍卖行管理", "导出拍卖行");
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
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
