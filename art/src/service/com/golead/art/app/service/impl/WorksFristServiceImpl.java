package com.golead.art.app.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.golead.art.activity.model.ArtActivityAbmb;
import com.golead.art.activity.model.ArtActivityExhibit;
import com.golead.art.app.service.WorksFristService;
import com.golead.art.artist.dao.ArtArtistDao;
import com.golead.art.artist.dao.ArtArtistWorksSeriesDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistDonation;
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.literature.dao.ArtLiteratureWordsDao;
import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureNetwork;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.works.dao.ArtMediumDao;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.dao.ArtWorksPeriodDao;
import com.golead.art.works.dao.ArtWorksStyleDao;
import com.golead.art.works.dao.ArtWorksThemeDao;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksPeriod;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.model.ArtWorksTheme;
import com.golead.common.service.system.SysCodeService;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;

@Service
public class WorksFristServiceImpl extends BaseServiceImpl implements WorksFristService {
   
   private DecimalFormat               decimalFormat = new DecimalFormat("#,###,###,###.##");

   @Override
   public List<Map<String, Object>> selectWorks(Map<String, String> parameters) throws ServiceException {
      try {
         String pageName = parameters.get("pageName");
         if (pageName.equalsIgnoreCase("worksMain")) return worksMain(parameters);// 艺术品主页
         else if (pageName.equalsIgnoreCase("worksBase")) return worksBase(parameters);// 基本信息
         else if (pageName.equalsIgnoreCase("worksAuction")) return worksAuction(parameters);// 相关拍卖
         else if (pageName.equalsIgnoreCase("worksActivity")) return worksActivity(parameters);// 相关展览
         else if (pageName.equalsIgnoreCase("worksLiterature")) return worksLiterature(parameters);// 相关文献
         else if (pageName.equalsIgnoreCase("worksPublication")) return worksPublication(parameters);// 相关画册
         else if (pageName.equalsIgnoreCase("worksCommentary")) return worksCommentary(parameters);// 对作品留言
      }
      catch (ServiceException e) {
         e.printStackTrace();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   private List<Map<String, Object>> worksMain(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksMain...");
      try {
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/");
         List<ArtMedium> artMediums = artMediumDao.findAll();
         Map<String, Object> mediumMap = new HashMap<String, Object>();
         for (ArtMedium medium : artMediums) {
            mediumMap.put(medium.getId().toString(), medium.getMediumName());
         }
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         String sql = "SELECT " + " works.id,CONCAT('auction/',artist.folder_name,'/',works.thumbnail) AS photo,"
               + " works.works_c_name AS cname,works.works_e_name AS ename,works.create_year AS createYear,"
               + " works.medium_material AS material,works.medium_shape AS shape,works.works_series AS series,works.auction_overview AS remark " + " FROM art_works works "
               + " LEFT JOIN art_artist artist ON artist.id=works.artist_id "
               + " LEFT JOIN art_artist_works_series artistSeries ON artistSeries.id IN(works.works_series) "
               + " WHERE works.id=" + worksId;
         List<Map<String, Object>> list = jdbcDao.queryBySql(sql);
         for (Map<String, Object> map : list) {
            if (map.get("photo") != null) {
               String path = FILE_PATH + File.separator + returnString(map.get("photo"));
               File file = new File(path);
               if (file.exists() && file.isFile()) {
                  BufferedImage bufferedImage = ImageIO.read(file);
                  int width = bufferedImage.getWidth();
                  int height = bufferedImage.getHeight();
                  map.put("width", width);
                  map.put("height", height);
               }
            }
            String material = findMedium(map, mediumMap);
//            if (map.get("material") != null) {
//               material+=mediumMap.get(map.get("material").toString());
//            }
//            if(map.get("shape") != null){
//               map.put("shape", mediumMap.get(map.get("shape").toString()));
//            }
            map.put("material", material);
         }
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private String findMedium(Map<String, Object> map, Map<String, Object> mediumMap){
      try {
         String medium = returnString(map.get("material")).trim();
         if(medium.endsWith(",")){
            medium+= returnString(map.get("shape")).trim();
         }else{
            medium+= "," + returnString(map.get("shape")).trim();
         }
         String[] mediums = medium.split(",");
         String returnMedium = "";
         for(int i = 0; i < mediums.length; i++){
            if(!"".equals(returnMedium)){
               returnMedium+=",";
            }
            if(mediumMap.get(mediums[i]) != null){
               returnMedium+=mediumMap.get(mediums[i]);
            }
         }
         return returnMedium;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   }

   private List<Map<String, Object>> worksBase(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksBase...");
      try {
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         ArtWorks artWorks = artWorksDao.get(Integer.valueOf(worksId));
         if (artWorks == null) { return null; }
         if (artWorks.getArtistId() == null) { return null; }
         ArtArtist artArtist = artArtistDao.get(artWorks.getArtistId());
         if (artArtist == null) { return null; }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         worksToList(list, artWorks, artArtist);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> worksAuction(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksAuction...");
      try {
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         String sql = "SELECT "
//               + " CONCAT('第',(@rowNO := @rowNo + 1),'次上拍') AS rowno,"
               + " artauction.auction_house AS shop,artauction.auction AS expo,artauction.sale_name AS place,"
               + " artauction.auction_no AS no,"
               + " DATE_FORMAT(artauction.auction_time,'%Y-%m-%d') AS time,"
               + " artauction.works_source AS soure, "
               + " artauction.auction_desc AS descript,"
               + " artauction.c_tran_price AS cTranPrice,"
               + " artauction.c_trade_price AS cTradePrice,"
               + " artauction.d_tran_price AS dTranPrice,"
               + " artauction.d_trade_price AS dTradePrice,"
               + " artauction.e_tran_price AS eTranPrice,"
               + " artauction.e_trade_price AS eTradePrice,"
               + " artauction.p_tran_price AS pTranPrice,"
               + " artauction.p_trade_price AS pTradePrice,"
               + " artauction.hk_tran_price AS hkTranPrice,"
               + " artauction.hk_trade_price AS hkTradePrice,"
               + " artauction.tran_status AS status,"
               + " artauction.c_lowest_price AS cLowestPrice,"
               + " artauction.d_lowest_price AS dLowestPrice,"
               + " artauction.e_lowest_price AS eLowestPrice,"
               + " artauction.p_lowest_price AS pLowestPrice,"
               + " artauction.hk_lowest_price AS hkLowestPrice,"
               + " artauction.c_highest_price AS cHighestPrice,"
               + " artauction.d_highest_price AS dHighestPrice,"
               + " artauction.e_highest_price AS eHighestPrice,"
               + " artauction.p_highest_price AS pHighestPrice,"
               + " artauction.hk_highest_price AS hkHighestPrice "
//               + " IF (LENGTH(artauction.c_lowest_price) > 0,"
//               + " IF (LENGTH(artauction.c_highest_price) > 0,CONCAT(cast(artauction.c_lowest_price as char),'~',cast(artauction.c_highest_price as char)), artauction.c_lowest_price),"
//               + " IF (LENGTH(artauction.c_highest_price) > 0,artauction.c_highest_price, null)) AS cPrice,"
//               + " IF (LENGTH(artauction.d_lowest_price) > 0,"
//               + " IF (LENGTH(artauction.d_highest_price) > 0,CONCAT(CONVERT(artauction.d_lowest_price,char),'~',CONVERT(artauction.d_highest_price,char)), artauction.d_lowest_price),"
//               + " IF (LENGTH(artauction.d_highest_price) > 0,artauction.d_highest_price, null)) AS dPrice,"
//               + " IF (LENGTH(artauction.e_lowest_price) > 0,"
//               + " IF (LENGTH(artauction.e_highest_price) > 0,CONCAT(CONVERT(artauction.e_lowest_price,char),'~',CONVERT(artauction.e_highest_price,char)), artauction.e_lowest_price),"
//               + " IF (LENGTH(artauction.e_highest_price) > 0,artauction.e_highest_price, null)) AS ePrice,"
//               + " IF (LENGTH(artauction.p_lowest_price) > 0,"
//               + " IF (LENGTH(artauction.p_highest_price) > 0,CONCAT(CONVERT(artauction.p_lowest_price,char),'~',CONVERT(artauction.p_highest_price,char)), artauction.p_lowest_price),"
//               + " IF (LENGTH(artauction.p_highest_price) > 0,artauction.p_highest_price, null)) AS pPrice,"
//               + " IF (LENGTH(artauction.hk_lowest_price) > 0,"
//               + " IF (LENGTH(artauction.hk_highest_price) > 0,CONCAT(CONVERT(artauction.hk_lowest_price,char),'~',CONVERT(artauction.hk_highest_price,char)), artauction.hk_lowest_price),"
//               + " IF (LENGTH(artauction.hk_highest_price) > 0,artauction.hk_highest_price, null)) AS hkPrice "
               + " FROM "
               + " (SELECT a.*,houses.auction_house FROM art_auction a "
               + " LEFT JOIN art_auction_houses houses ON houses.id=a.auction_houses_id "
               + " WHERE a.works_id="+ worksId
               + " ORDER BY a.auction_time ASC) artauction ";
//               + " (SELECT @rowNO := 0) b ";
         List<Map<String, Object>> list = jdbcDao.queryBySql(sql);
         int i = 1;
         for(Map<String, Object> map : list){
            map.put("rowno", "第" + String.valueOf(i) + "次上拍");
            decimalFormatString(map);
            if(map.get("status") != null){
               String status = sysCodeService.findCodeByItemValue(returnString(map.get("status")), 37);
               map.put("status", status);
            }else{
               map.put("status", null);
            }
         }
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private void decimalFormatString(Map<String, Object> map){
      String cPrice = removeZero(map.get("cLowestPrice")) + "~" + removeZero(map.get("cHighestPrice"));
      String dPrice = removeZero(map.get("dLowestPrice")) + "~" + removeZero(map.get("dHighestPrice"));
      String ePrice = removeZero(map.get("eLowestPrice")) + "~" + removeZero(map.get("eHighestPrice"));
      String pPrice = removeZero(map.get("pLowestPrice")) + "~" + removeZero(map.get("pHighestPrice"));
      String hkPrice = removeZero(map.get("hkLowestPrice")) + "~" + removeZero(map.get("hkHighestPrice"));
      map.put("cPrice", "~".equals(cPrice) ? "" : cPrice);
      map.put("dPrice", "~".equals(dPrice) ? "" : dPrice);
      map.put("ePrice", "~".equals(ePrice) ? "" : ePrice);
      map.put("pPrice", "~".equals(pPrice) ? "" : pPrice);
      map.put("hkPrice", "~".equals(hkPrice) ? "" : hkPrice);
   }
   
   private String removeZero(Object obj){
      try {
//         if(obj != null){
//            String num = "";
//            String[] nums = returnString(obj).split("~");
//            for(int i = 0; i < nums.length; i++){
//               if(!"".equals(num)){
//                  num+="~";
//               }
//               num+=decimalFormat.format(Double.valueOf(nums[i]));
//            }
//            return num;
//         }
         if(obj != null){
            return decimalFormat.format(Double.valueOf(obj.toString()));
         }
         return "";
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private List<Map<String, Object>> worksActivity(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksActivity...");
      try {
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         
         String sql = "SELECT * FROM art_activity_exhibit exhibit "
               + " LEFT JOIN art_works_exhibit worksExhibit ON worksExhibit.exhibit_id=exhibit.id "
               + " WHERE worksExhibit.works_id=" + worksId;
         List<Map<String, Object>> exhibitList = jdbcDao.queryBySql(sql);
         List<Map<String, Object>> personList = new ArrayList<Map<String, Object>>();
         List<Map<String, Object>> peopleList = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> exhibitMap : exhibitList) {
            Map<String, Object> exhibit = new HashMap<String, Object>();
            String time = "";
            time += returnString(exhibitMap.get(ArtActivityExhibit.COL_ACTIVITY_YEAR)).equals("") ? ""
                  : (exhibitMap.get(ArtActivityExhibit.COL_ACTIVITY_YEAR) + "年");
            time += returnString(exhibitMap.get(ArtActivityExhibit.COL_ACTIVITY_MONTH)).equals("") ? "" : (exhibitMap
                  .get(ArtActivityExhibit.COL_ACTIVITY_MONTH) + "月");
            exhibit.put("time", time);
            exhibit.put("message", exhibitMap.get(ArtActivityExhibit.COL_EXHIBIT_NAME));
            if ("1".equals(exhibitMap.get(ArtActivityExhibit.COL_EXHIB_TYPE).toString())) {
               personList.add(exhibit);
            }
            if ("2".equals(exhibitMap.get(ArtActivityExhibit.COL_EXHIB_TYPE).toString())) {
               peopleList.add(exhibit);
            }
         }
         map.put("person", personList);
         map.put("people", peopleList);
         sql = "SELECT activityAbmb.abmb_name,activityAbmb.abmb_year,activityAbmb.abmb_month FROM art_activity_abmb activityAbmb "
               + " LEFT JOIN art_works_abmb worksAbmb ON worksAbmb.abmb_id=activityAbmb.id "
               + " WHERE worksAbmb.works_id=" + worksId;
         List<Map<String, Object>> abmbList = jdbcDao.queryBySql(sql);
         List<Map<String, Object>> abmbs = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> abmbMap : abmbList) {
            Map<String, Object> abmb = new HashMap<String, Object>();
            String time = "";
            time += returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_YEAR)).equals("") ? "" : (returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_YEAR)) + "年");
            time += returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_MONTH)).equals("") ? ""
                  : (returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_MONTH)) + "月");
            abmb.put("time", time);
            abmb.put("message", abmbMap.get(ArtActivityAbmb.COL_ABMB_NAME));
            abmbs.add(abmb);
         }
         map.put("abmb", abmbs);
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private List<Map<String, Object>> worksLiterature(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksLiterature...");
      try {
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         String sql = "SELECT network.literature_title AS title,network.literature_auther AS auther,"
               + " network.literature_source AS name,network.publication_time AS time "
               + " FROM art_literature_network network "
               + " LEFT JOIN art_works_network worksNetwork ON worksNetwork.network_id=network.id "
               + " WHERE worksNetwork.works_id=" + worksId;
         List<Map<String, Object>> netList = jdbcDao.queryBySql(sql);
         map.put("net", netList);
         sql = "SELECT words.literature_title AS title,words.literature_auther AS auther,"
               + " words.publication_name AS name,words.write_time AS time "
               + " FROM art_literature_words words "
               + " LEFT JOIN art_works_words worksWords ON worksWords.words_id=words.id "
               + " WHERE worksWords.works_id=" + worksId;
         List<Map<String, Object>> wordsList = jdbcDao.queryBySql(sql);
         map.put("word", wordsList);
         sql = "SELECT medium.literature_title AS title,medium.shot_people AS auther,"
               + " medium.site_link AS link,medium.content_desc AS name,medium.shot_time AS time "
               + " FROM art_literature_medium medium "
               + " LEFT JOIN art_works_medium worksMedium ON worksMedium.medium_id=medium.id "
               + " WHERE worksMedium.works_id=" + worksId;
         List<Map<String, Object>> mediumList = jdbcDao.queryBySql(sql);
         map.put("music", mediumList);
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private List<Map<String, Object>> worksPublication(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksPublication...");
      try {
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         String sql = "SELECT publication.publication_name,publication.press,a.time FROM art_publication publication "
               + " LEFT JOIN art_publication_works worksPublication ON worksPublication.pub_id=publication.id "
               + " LEFT JOIN (SELECT p.id,"
               + "IF (LENGTH(p.publication_year) > 0,"
               + "IF (LENGTH(p.publication_month) > 0,CONCAT(CONVERT(p.publication_year,char), '年',CONVERT(p.publication_month,char), '月'),CONCAT(CONVERT(p.publication_year,char), '年')), "
               + "IF (LENGTH(p.publication_month) > 0,CONCAT(CONVERT(p.publication_month,char), '月'),NULL)) AS time "
               + " FROM art_publication p) a ON a.id=publication.id "
               + " WHERE worksPublication.works_id=" + worksId;
         return jdbcDao.queryBySql(sql);
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private List<Map<String, Object>> worksCommentary(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksCommentary...");
      try {
         String worksId = returnString(parameters.get("worksId"));
         if ("".equals(returnString(worksId).trim())) { throw new ServiceException("作品id错误！"); }
         String sql = "SELECT user.user_name,commentary.content,commentary.create_time FROM art_commentary commentary "
               + " LEFT JOIN art_app_user user ON commentary.app_user_id=user.id "
               + " WHERE commentary.works_id=" + worksId;
         return jdbcDao.queryBySql(sql);
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private void worksToList(List<Map<String, Object>> list, ArtWorks artWorks, ArtArtist artArtist) {
      List<ArtMedium> artMediums = artMediumDao.findAll();
      Map<String, Object> mediumMap = new HashMap<String, Object>();
      for (ArtMedium medium : artMediums) {
         mediumMap.put(medium.getId().toString(), medium.getMediumName());
      }
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("artistCName", artArtist.getCname());
      map.put("artistEName", artArtist.getEname());
      map.put("createYear", artWorks.getCreateYear());
      String material = returnString(artWorks.getMediumMaterial());
      String shape = returnString(artWorks.getMediumShape());
      map.put("material", material);
      map.put("shape", shape);
      String medium = findMedium(map, mediumMap);
      map.put("medium", medium);
      map.remove("material");
      map.remove("shape");
      if (artWorks.getWorksSeries() != null && !"".equals(artWorks.getWorksSeries())) {
         ArtArtistWorksSeries artArtistWorksSeries = artArtistWorksSeriesDao.get(Integer.valueOf(artWorks.getWorksSeries()));
         if (artArtistWorksSeries != null) {
            map.put("series", artArtistWorksSeries.getSeriesName());
         }else{
            map.put("series", null);
         }
      }

      List<Map<String, Object>> signatureList = new ArrayList<Map<String, Object>>();
      Map<String, Object> signatureMap = new HashMap<String, Object>();
      if (artWorks.getSignature() != null && !"".equals(artWorks.getSignature().trim())) {
         String signature = sysCodeService.findCodeByItemValue(artWorks.getSignature(), 19);
         signatureMap.put("signature", signature);
         signatureMap.put("signatureContent", artWorks.getSignatureContent());
      }
      if (artWorks.getSignature2() != null && !"".equals(artWorks.getSignature2().trim())) {
         String signature = sysCodeService.findCodeByItemValue(artWorks.getSignature2(), 19);
         signatureMap.put("signature2", signature);
         signatureMap.put("signatureContent2", artWorks.getSignatureContent2());
      }
      if (artWorks.getSignature3() != null && !"".equals(artWorks.getSignature3().trim())) {
         String signature = sysCodeService.findCodeByItemValue(artWorks.getSignature3(), 19);
         signatureMap.put("signature3", signature);
         signatureMap.put("signatureContent3", artWorks.getSignatureContent3());
      }
      if (signatureMap != null && signatureMap.size() > 0) {
         signatureList.add(signatureMap);
         map.put("signature", signatureList);
      }else{
         map.put("signature", null);
      }

      List<Map<String, Object>> sizeList = new ArrayList<Map<String, Object>>();
      Map<String, Object> sizeMap = new HashMap<String, Object>();
      String sizeCm = "";
      sizeCm += returnString(artWorks.getSizeCmLength());
      sizeCm = "".equals(sizeCm.trim()) ? "" : (sizeCm + "X" + returnString(artWorks.getSizeCmWidth()));
      sizeCm = sizeCm.endsWith("X") ? sizeCm + returnString(artWorks.getSizeCmHeight()) : 
         ("".equals(sizeCm) ? returnString(artWorks.getSizeCmHeight())
            : sizeCm + "X" + returnString(artWorks.getSizeCmHeight()));
      if(sizeCm.endsWith("X")){
         sizeCm=sizeCm.substring(0, sizeCm.length()-1);
      }
      sizeMap.put("sizeCm", sizeCm);
      String sizeIn = "";
      sizeIn += returnString(artWorks.getSizeInLength());
      sizeIn = "".equals(sizeIn.trim()) ? "" : (sizeIn + "X" + returnString(artWorks.getSizeInWidth()));
      sizeIn = sizeIn.endsWith("X") ? sizeIn + returnString(artWorks.getSizeInHeight()) : 
         ("".equals(sizeIn) ? returnString(artWorks.getSizeInHeight())
            : sizeIn + "X" + returnString(artWorks.getSizeInHeight()));
      if(sizeIn.endsWith("X")){
         sizeIn=sizeIn.substring(0, sizeIn.length()-1);
      }
      sizeMap.put("sizeIn", sizeIn);
      String sizeRule = "";
      sizeRule += returnString(artWorks.getSizeRuleLength());
      sizeRule = "".equals(sizeRule.trim()) ? "" : (sizeRule + "X" + returnString(artWorks.getSizeRuleWidth()));
      sizeRule = sizeRule.endsWith("X") ? sizeRule + returnString(artWorks.getSizeRuleHeight()) : ("".equals(sizeRule) ? returnString(artWorks
            .getSizeRuleHeight()) : sizeCm + "X" + returnString(artWorks.getSizeRuleHeight()));
      if(sizeRule.endsWith("X")){
         sizeRule=sizeRule.substring(0, sizeRule.length()-1);
      }
      sizeMap.put("sizeRule", sizeRule);
      sizeList.add(sizeMap);
      map.put("size", sizeList);

      map.put("createPlace", artWorks.getCreatePlace());
      String theme = "";
      if (artWorks.getWorksTheme1() != null && !"".equals(artWorks.getWorksTheme1().trim())) {
         ArtWorksTheme artWorksTheme = artWorksThemeDao.get(Integer.valueOf(artWorks.getWorksTheme1()));
         if(artWorksTheme != null){
            theme += artWorksTheme.getThemeName();
         }
      }
      if (artWorks.getWorksTheme2() != null && !"".equals(artWorks.getWorksTheme2().trim())) {
         ArtWorksTheme artWorksTheme = artWorksThemeDao.get(Integer.valueOf(artWorks.getWorksTheme2()));
         if(!"".equals(theme)){
            theme+=",";
         }
         if(artWorksTheme != null){
            theme += artWorksTheme.getThemeName();
         }
      }
      if (artWorks.getWorksTheme3() != null && !"".equals(artWorks.getWorksTheme3().trim())) {
         ArtWorksTheme artWorksTheme = artWorksThemeDao.get(Integer.valueOf(artWorks.getWorksTheme3()));
         if(!"".equals(theme)){
            theme+=",";
         }
         if(artWorksTheme != null){
            theme += artWorksTheme.getThemeName();
         }
      }
      map.put("theme", theme);
      if (artWorks.getStyleType() != null && !"".equals(artWorks.getStyleType())) {
         String styleType = artWorks.getStyleType();
         String worksStyleName = "";
         for (int i = 0; i < styleType.split(";").length; i++) {
            String styleTypeId = styleType.split(";")[i];
            ArtWorksStyle artWorksStyle = artWorksStyleDao.get(Integer.valueOf(styleTypeId));
            worksStyleName = worksStyleName + artWorksStyle.getStyleName() + "、";
         }
         map.put("style", worksStyleName);
      }else{
         map.put("style", null);
      }
      String period = "";
      List<ArtWorksPeriod> artWorksPeriods = artWorksPeriodDao.findByField(ArtWorksPeriod.PROP_WORKS_ID, artWorks.getId());
      for (ArtWorksPeriod artWorksPeriod : artWorksPeriods) {
         if (!"".equals(period)) {
            period += ",";
         }
         period += artWorksPeriod.getPeriodName();
      }
      map.put("period", period);
      map.put("descript", artWorks.getWorksStatus());
      list.add(map);
   }

   private String returnString(Object obj) {
      if (obj == null) { return ""; }
      return obj.toString();
   }

   private List<Map<String, Object>> addError(String message) {
      Map<String, Object> map = new HashMap<String, Object>();
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      map.put("error", message);
      list.add(map);
      return list;
   }

   @Resource
   private ArtMediumDao            artMediumDao;

   @Resource
   private ArtWorksDao             artWorksDao;

   @Resource
   private ArtArtistDao            artArtistDao;

   @Resource
   private ArtWorksThemeDao        artWorksThemeDao;

   @Resource
   private ArtArtistWorksSeriesDao artArtistWorksSeriesDao;

   @Resource
   private ArtWorksStyleDao        artWorksStyleDao;

   @Resource
   private ArtWorksPeriodDao       artWorksPeriodDao;

   @Resource
   private SysCodeService          sysCodeService;
}
