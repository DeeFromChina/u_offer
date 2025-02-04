package com.golead.art.auction;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import com.golead.art.auction.model.ArtAuction;
import com.golead.art.auction.model.ArtAuctionHouses;
import com.golead.art.auction.service.ArtAuctionHousesService;
import com.golead.art.auction.service.ArtAuctionService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.art.works.service.ArtWorksStyleService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtAuctionAction extends BaseAction {

   private Log              log           = LogFactory.getLog(ArtAuctionAction.class);

   private SimpleDateFormat dateFormate   = new SimpleDateFormat("yyyy-MM-dd");

   private DecimalFormat    decimalFormat = new DecimalFormat("#,###,###,###.##");

   private DecimalFormat    deFormat      = new DecimalFormat("###.##");

   public static String     FILE_PATH     = ServletActionContext.getServletContext().getRealPath("/template/auction/");
   private File             importFile;
   private String           importFileFileName;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) action = LIST;
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = pageAuction();
         else if (ADD.equalsIgnoreCase(action)) forward = addAuction();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveAuction();
         else if (EDIT.equalsIgnoreCase(action)) forward = editAuction();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateAuction();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteAuction();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewAuction();
         else if ("GOEXCEL".equalsIgnoreCase(action)) forward = goExcel();
         else if ("EXCEL".equalsIgnoreCase(action)) forward = excel();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = download();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String pageAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageAuction' method");
      try {
         initForm();
         PageQuery pq = getForm().getQuery();
         pq = artAuctionService.queryArtAuction(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", item.get("id"));
            String link = "^javascript:view123(" + item.get("id") + ");^_self";
            String workslink = "^javascript:worksview(" + item.get("worksId") + ");^_self";
            returnPrice(item);
            String cTranPrice = item.get("tprice").toString();
//            String cTradePrice = item.get("cTradePrice") == null ? "" : decimalFormat.format(item.get("cTradePrice"));
            String cLowestPrice = item.get("lprice").toString();
            String cHighestPrice = item.get("hprice").toString();
            String imageUrl = item.get("imageUrl") == null ? "" : item.get("imageUrl").toString();
            String eName = item.get("folderName") == null ? "" : item.get("folderName").toString();
            String worksCategory = "";
            if (item.get("worksCategory") != null) {
               String category = item.get("worksCategory").toString();
               String[] categorys = category.split(";");
               for (int i = 0; i < categorys.length; i++) {
                  if (findCodeName(form, "STYLE", categorys[i]) != null) {
                     if (!worksCategory.trim().equals("")) {
                        worksCategory += ",";
                     }
                     worksCategory += findCodeName(form, "STYLE", categorys[i]);
                  }
               }
            }
            if (!imageUrl.equals("")) {
               imageUrl = request.get("CONTEXT_PATH") + "/upload/auction/" + eName + File.separator + "thumbnail" + File.separator + imageUrl;
            }
            String autionSeason = findCodeName(getForm(), "AUCTION_SEASON", item.get("autionSeason") + "");
            String auctionStatus = item.get("tranStatus") == null ? "" : item.get("tranStatus").toString();
            if(!"".equals(auctionStatus)){
               auctionStatus = findCodeName(form, "TRAN_STATUS", auctionStatus);
            }
            map.put("data",
                  new Object[] { "", auctionStatus, imageUrl, (item.get("auctionNo") == null ? "" : item.get("auctionNo").toString()) + link, item.get("worksNo"), item.get("worksCName") + workslink,item.get("worksEName"), item.get("cName"), item.get("sizeCm"),
                        item.get("createYear"), item.get("auctionHouse"), item.get("auction"), item.get("saleName"), formateDate(item.get("auctionTime")),
                         autionSeason, cTranPrice, cHighestPrice, cLowestPrice });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖关系管理", "作品拍卖关系管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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
   
   private String decimalFormatString(Object obj){
      if(obj != null){
         return decimalFormat.format(obj);
      }
      return "";
   }

   public String addAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addAuction' method");
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private String decimalFormatString(String d) {
      if ("".equals(d.trim())) { return ""; }
      return decimalFormat.format(Double.valueOf(d.trim()));
   }

   private String returnX(String a, String b) {
      if (!"".equals(a) && !"".equals(b)) { return "X"; }
      return "";
   }

   public String saveAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveAuction' method");
      try {
         ArtAuction artAuction = new ArtAuction();
         ConvertUtil.mapToObject(artAuction, getForm().getRecord(), false);
         ArtWorks works = artWorksService1.getArtWorks(artAuction.getWorksId());
         ArtArtist artist = artArtistService.getArtArtist(works.getArtistId());
         String author = artist.getCname();
         if (author == null || "".equals(author)) author = artist.getEname();
         String sizeCmWidth = decimalFormatString(works.getSizeCmWidth() == null ? "" : works.getSizeCmWidth().toString());
         String sizeCmHeight = decimalFormatString(works.getSizeCmHeight() == null ? "" : works.getSizeCmHeight().toString());
         String sizeCmLength = decimalFormatString(works.getSizeCmLength() == null ? "" : works.getSizeCmLength().toString());
         String sizeCm = sizeCmLength + returnX(sizeCmLength, sizeCmWidth) + sizeCmWidth + returnX(sizeCmWidth, sizeCmHeight) + sizeCmHeight;
         if ("".equals(sizeCmWidth) && !"".equals(sizeCmHeight) && !"".equals(sizeCmLength)) {
            sizeCm = sizeCmLength + "X" + sizeCmHeight;
         }
         String sizeInWidth = decimalFormatString(works.getSizeInWidth() == null ? "" : works.getSizeInWidth().toString());
         String sizeInHeight = decimalFormatString(works.getSizeInHeight() == null ? "" : works.getSizeInHeight().toString());
         String sizeInLength = decimalFormatString(works.getSizeInLength() == null ? "" : works.getSizeInLength().toString());
         String sizeIn = sizeInLength + returnX(sizeInLength, sizeInWidth) + sizeInWidth + returnX(sizeInWidth, sizeInHeight) + sizeInHeight;
         if ("".equals(sizeInWidth) && !"".equals(sizeInHeight) && !"".equals(sizeInLength)) {
            sizeIn = sizeInLength + "X" + sizeInHeight;
         }
         String sizeRuleWidth = decimalFormatString(works.getSizeRuleWidth() == null ? "" : works.getSizeRuleWidth().toString());
         String sizeRuleHeight = decimalFormatString(works.getSizeRuleHeight() == null ? "" : works.getSizeRuleHeight().toString());
         String sizeRuleLength = decimalFormatString(works.getSizeRuleLength() == null ? "" : works.getSizeRuleLength().toString());
         String sizeRule = sizeRuleLength + returnX(sizeRuleLength, sizeRuleWidth) + sizeRuleWidth + returnX(sizeRuleWidth, sizeRuleHeight) + sizeRuleHeight;
         if ("".equals(sizeRuleWidth) && !"".equals(sizeRuleHeight) && !"".equals(sizeRuleLength)) {
            sizeRule = sizeRuleLength + "X" + sizeRuleHeight;
         }
         artAuction.setSizeCm(sizeCm);
         artAuction.setSizeIn(sizeIn);
         artAuction.setSizeRule(sizeRule);
         artAuction.setArtId(works.getArtistId());
         artAuction.setImageUrl(works.getThumbnail());
         artAuction.setAuthor(author);
         artAuction.setCnName(works.getWorksCName());
         artAuction.setEnName(works.getWorksEName());
         artAuction.setWorksCategory(works.getStyleType());
         artAuctionService.createArtAuction(artAuction);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖信息管理", "作品拍卖信息管理",
               "添加作品拍卖信息：" + artAuction.getAuction() + ":" + "[" + works.getWorksCName() + "]");
         ids = artAuction.getId().toString();
         return editAuction();
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         initForm();
         return ADD;
      }
   }

   public String editAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editAuction' method");
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtAuction artAuction = artAuctionService.getArtAuction(id);
         ConvertUtil.objectToMap(form.getRecord(), artAuction);
         setPriceToMap(artAuction);
         Integer worksId = artAuction.getWorksId();
         if (worksId != null) {
            ArtWorks artWorks = artWorksService1.getArtWorks(worksId);
            if (artWorks != null) {
               form.getRecord().put("worksName", artWorks.getWorksCName());
            }
         }
         Integer auctionHousesId = artAuction.getAuctionHousesId();
         if (auctionHousesId != null) {
            ArtAuctionHouses artAuctionHouses = artAuctionHousesService.getArtAuctionHouses(auctionHousesId);
            if (artAuctionHouses != null) {
               form.getRecord().put("housesName", artAuctionHouses.getAuctionHouse());
            }
         }
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private String deFormatString(String str) {
      if ("".equals(str.trim())) { return ""; }
      return deFormat.format(Double.valueOf(str));
   }

   private void setPriceToMap(ArtAuction artAuction) {
      form.getRecord().put("cTranPrice", artAuction.getCtranPrice() == null ? null : deFormatString(artAuction.getCtranPrice().toString()));
      form.getRecord().put("cTradePrice", artAuction.getCtradePrice() == null ? null : deFormatString(artAuction.getCtradePrice().toString()));
      form.getRecord().put("cHighestPrice", artAuction.getChighestPrice() == null ? null : deFormatString(artAuction.getChighestPrice().toString()));
      form.getRecord().put("cLowestPrice", artAuction.getClowestPrice() == null ? null : deFormatString(artAuction.getClowestPrice().toString()));

      form.getRecord().put("eTranPrice", artAuction.getEtranPrice() == null ? null : deFormatString(artAuction.getEtranPrice().toString()));
      form.getRecord().put("eTradePrice", artAuction.getEtradePrice() == null ? null : deFormatString(artAuction.getEtradePrice().toString()));
      form.getRecord().put("eHighestPrice", artAuction.getEhighestPrice() == null ? null : deFormatString(artAuction.getEhighestPrice().toString()));
      form.getRecord().put("eLowestPrice", artAuction.getElowestPrice() == null ? null : deFormatString(artAuction.getElowestPrice().toString()));

      form.getRecord().put("dTranPrice", artAuction.getDtranPrice() == null ? null : deFormatString(artAuction.getDtranPrice().toString()));
      form.getRecord().put("dTradePrice", artAuction.getDtradePrice() == null ? null : deFormatString(artAuction.getDtradePrice().toString()));
      form.getRecord().put("dHighestPrice", artAuction.getDhighestPrice() == null ? null : deFormatString(artAuction.getDhighestPrice().toString()));
      form.getRecord().put("dLowestPrice", artAuction.getDlowestPrice() == null ? null : deFormatString(artAuction.getDlowestPrice().toString()));

      form.getRecord().put("pTranPrice", artAuction.getPtranPrice() == null ? null : deFormatString(artAuction.getPtranPrice().toString()));
      form.getRecord().put("pTradePrice", artAuction.getPtradePrice() == null ? null : deFormatString(artAuction.getPtradePrice().toString()));
      form.getRecord().put("pHighestPrice", artAuction.getPhighestPrice() == null ? null : deFormatString(artAuction.getPhighestPrice().toString()));
      form.getRecord().put("pLowestPrice", artAuction.getPlowestPrice() == null ? null : deFormatString(artAuction.getPlowestPrice().toString()));

   }

   public String updateAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateAuction' method");
      try {
         Map<String, String> record = getForm().getRecord();
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖信息管理", "作品拍卖信息管理",
               "修改作品拍卖信息：" + record.get(ArtAuction.PROP_AUCTION) + ":" + "[" + record.get(ArtAuction.PROP_CN_NAME) + "]");
         artAuctionService.updateArtAuction(record);
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return EDIT;
      }
   }

   public String deleteAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteAuction' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            if(log.length()>0)log.append(",");
            ArtAuction artAuction = artAuctionService.getArtAuction(del_ids[i]);
            log.append("[").append(artAuction.getAuction()).append(":").append(artAuction.getCnName()).append("]");
         }
         artAuctionService.deleteArtAuctions(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖信息管理", "作品拍卖信息管理",
               "删除作品拍卖信息：" + log.toString());
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

   public String viewAuction() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewAuction' method");
      try {
         initForm();
         ArtAuction artAuction = artAuctionService.getArtAuction(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), artAuction);
         setPriceToMap(artAuction);
         form.getRecord().put("autionSeason", findCodeName(form, "AUCTION_SEASON", artAuction.getAutionSeason()));
         Integer worksId = artAuction.getWorksId();
         if (worksId != null) {
            ArtWorks artWorks = artWorksService1.getArtWorks(worksId);
            if (artWorks != null) {
               form.getRecord().put("worksName", artWorks.getWorksCName());
            }
         }
         Integer auctionHousesId = artAuction.getAuctionHousesId();
         if (auctionHousesId != null) {
            ArtAuctionHouses artAuctionHouses = artAuctionHousesService.getArtAuctionHouses(auctionHousesId);
            if (artAuctionHouses != null) {
               form.getRecord().put("housesName", artAuctionHouses.getAuctionHouse());
            }
         }
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private String goExcel() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'goExcel' method");
      try {
         return "GOEXCEL";
      }
      catch (Exception e) {
         e.printStackTrace();
         return LIST;
      }
   }

   private String excel() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'excel' method");
      try {
         File file = new File(FILE_PATH + "auction");
         if (!file.exists()) {
            file.mkdirs();
         }
         String path = FILE_PATH + "auction" + File.separator + importFileFileName;
         FileUtils.fileUpload(path, importFile);
         String message = artAuctionService.importArtAuction(path);
         if (!"".equals(message)) {
            if (message.startsWith("成功")) {
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖信息管理", "作品拍卖信息管理",
                     "导出作品拍卖信息");
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
      if (log.isDebugEnabled()) log.debug("Entering 'download' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("拍卖信息模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String tempPath = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator + "auction" + File.separator
               + "art_auction.xls";
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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖信息管理", "作品拍卖信息管理",
               "下载作品拍卖信息");
         printExcel.doPrint(response, tempFile, wb);// workbook输出到response中。
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
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("作品拍卖导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artAuctionService.export(form, pq);

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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品拍卖信息管理", "作品拍卖信息管理",
               "导出作品拍卖信息");
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private void returnPrice(Map<String, Object> map){
      StringBuffer tprice = new StringBuffer();
      StringBuffer lprice = new StringBuffer();
      StringBuffer hprice = new StringBuffer();
      tprice.append("<table>");
      lprice.append("<table>");
      hprice.append("<table>");
      String[] name = new String[]{"c","d","e","p","hk"};
      String TranPrice = "TranPrice";
      String LowestPrice = "LowestPrice";
      String HighestPrice = "HighestPrice";
      for(int i = 0; i<name.length; i++){
         String tname = name[i] + TranPrice;
         String lname = name[i] + LowestPrice;
         String hname = name[i] + HighestPrice;
         if("c".equals(name[i])){
            if(map.get(tname) != null){
               tprice.append("<tr>");
               tprice.append("<td>RMB</td>");
               tprice.append("<td class='textR'>");
               tprice.append(decimalFormatString(map.get(tname)));
               tprice.append("</td>");
               tprice.append("</tr>");
            }
            if(map.get(lname) != null){
               lprice.append("<tr>");
               lprice.append("<td>RMB</td>");
               lprice.append("<td class='textR'>");
               lprice.append(decimalFormatString(map.get(lname)));
               lprice.append("</td>");
               lprice.append("</tr>");
            }
            if(map.get(hname) != null){
               hprice.append("<tr>");
               hprice.append("<td>RMB</td>");
               hprice.append("<td class='textR'>");
               hprice.append(decimalFormatString(map.get(hname)));
               hprice.append("</td>");
               hprice.append("</tr>");
            }
         }else if("d".equals(name[i])){
            if(map.get(tname) != null){
               tprice.append("<tr>");
               tprice.append("<td>USD</td>");
               tprice.append("<td class='textR'>");
               tprice.append(decimalFormatString(map.get(tname)));
               tprice.append("</td>");
               tprice.append("</tr>");
            }
            if(map.get(lname) != null){
               lprice.append("<tr>");
               lprice.append("<td>USD</td>");
               lprice.append("<td class='textR'>");
               lprice.append(decimalFormatString(map.get(lname)));
               lprice.append("</td>");
               lprice.append("</tr>");
            }
            if(map.get(hname) != null){
               hprice.append("<tr>");
               hprice.append("<td>USD</td>");
               hprice.append("<td class='textR'>");
               hprice.append(decimalFormatString(map.get(hname)));
               hprice.append("</td>");
               hprice.append("</tr>");
            }
         }else if("e".equals(name[i])){
            if(map.get(tname) != null){
               tprice.append("<tr>");
               tprice.append("<td>EUR</td>");
               tprice.append("<td class='textR'>");
               tprice.append(decimalFormatString(map.get(tname)));
               tprice.append("</td>");
               tprice.append("</tr>");
            }
            if(map.get(lname) != null){
               lprice.append("<tr>");
               lprice.append("<td>EUR</td>");
               lprice.append("<td class='textR'>");
               lprice.append(decimalFormatString(map.get(lname)));
               lprice.append("</td>");
               lprice.append("</tr>");
            }
            if(map.get(hname) != null){
               hprice.append("<tr>");
               hprice.append("<td>EUR</td>");
               hprice.append("<td class='textR'>");
               hprice.append(decimalFormatString(map.get(hname)));
               hprice.append("</td>");
               hprice.append("</tr>");
            }
         }else if("p".equals(name[i])){
            if(map.get(tname) != null){
               tprice.append("<tr>");
               tprice.append("<td>GBP</td>");
               tprice.append("<td class='textR'>");
               tprice.append(decimalFormatString(map.get(tname)));
               tprice.append("</td>");
               tprice.append("</tr>");
            }
            if(map.get(lname) != null){
               lprice.append("<tr>");
               lprice.append("<td>GBP</td>");
               lprice.append("<td class='textR'>");
               lprice.append(decimalFormatString(map.get(lname)));
               lprice.append("</td>");
               lprice.append("</tr>");
            }
            if(map.get(hname) != null){
               hprice.append("<tr>");
               hprice.append("<td>GBP</td>");
               hprice.append("<td class='textR'>");
               hprice.append(decimalFormatString(map.get(hname)));
               hprice.append("</td>");
               hprice.append("</tr>");
            }
         }else if("hk".equals(name[i])){
            if(map.get(tname) != null){
               tprice.append("<tr>");
               tprice.append("<td>HKD</td>");
               tprice.append("<td class='textR'>");
               tprice.append(decimalFormatString(map.get(tname)));
               tprice.append("</td>");
               tprice.append("</tr>");
            }
            if(map.get(lname) != null){
               lprice.append("<tr>");
               lprice.append("<td>HKD</td>");
               lprice.append("<td class='textR'>");
               lprice.append(decimalFormatString(map.get(lname)));
               lprice.append("</td>");
               lprice.append("</tr>");
            }
            if(map.get(hname) != null){
               hprice.append("<tr>");
               hprice.append("<td>HKD</td>");
               hprice.append("<td class='textR'>");
               hprice.append(decimalFormatString(map.get(hname)));
               hprice.append("</td>");
               hprice.append("</tr>");
            }
         }
      }
      tprice.append("</table>");
      lprice.append("</table>");
      hprice.append("</table>");
      map.put("tprice", tprice.toString());
      map.put("lprice", lprice.toString());
      map.put("hprice", hprice.toString());
      return;
   }

   public void initForm() throws Exception {
      setCode(getForm(), "AUCTION_SEASON,TRAN_STATUS");
      List<ArtWorksStyle> artWorksStyles = artWorksStyleService.findAll();
      setCode(form, "STYLE", artWorksStyles, ArtWorksStyle.PROP_STYLE_NAME, ArtWorksStyle.PROP_ID, false);
   }

   private String formateDate(Object obj) {
      if (obj == null) return "";
      return dateFormate.format(obj);
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

   @Resource
   private ArtAuctionService       artAuctionService;

   @Resource
   private ArtAuctionHousesService artAuctionHousesService;

   @Resource
   private ArtWorksService         artWorksService1;

   @Resource
   private ArtWorksStyleService    artWorksStyleService;

   @Resource
   private ArtArtistService        artArtistService;

   @Resource
   private ArtSysLogService        artSysLogService;
}
