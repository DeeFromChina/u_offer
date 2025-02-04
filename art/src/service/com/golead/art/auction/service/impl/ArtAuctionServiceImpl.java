package com.golead.art.auction.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.golead.art.artist.dao.ArtArtistDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.artist.model.ArtCollectArtist;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.auction.dao.ArtAuctionDao;
import com.golead.art.auction.dao.ArtAuctionExchangeDao;
import com.golead.art.auction.dao.ArtAuctionHousesDao;
import com.golead.art.auction.model.ArtAuction;
import com.golead.art.auction.model.ArtAuctionExchange;
import com.golead.art.auction.model.ArtAuctionHouses;
import com.golead.art.auction.service.ArtAuctionService;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.model.ArtWorksTheme;
import com.golead.common.model.SysCode;
import com.golead.common.service.system.SysCodeService;
import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;

@Service
public class ArtAuctionServiceImpl extends BaseServiceImpl implements ArtAuctionService {
   private static final long serialVersionUID = 1L;

   private SimpleDateFormat  outFormat        = new SimpleDateFormat("yyyy-MM-dd");

   public ArtAuction getArtAuction(Serializable id) throws ServiceException {
      try {
         return artAuctionDao.get(id);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void createArtAuction(ArtAuction artAuction) throws ServiceException {
      try {
         artAuctionDao.save(artAuction);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private String returnX(String a, String b){
      if(a == null || b == null){
         return "";
      }
      else if(!"".equals(a.trim()) && !"".equals(b.trim())){
         return "X";
      }
      return "";
   }
   
   public void updateArtAuction(Map<String, String> artAuctionMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artAuctionMap.get(ArtAuction.PROP_ID));
         ArtAuction tmp = artAuctionDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artAuctionMap, true);
         ArtWorks works = artWorksDao.get(tmp.getWorksId());
         ArtArtist artist = artArtistDao.get(works.getArtistId());
         String author = artist.getCname();
         if (author == null || "".equals(author)) author = artist.getEname();
         DecimalFormat    deFormat     = new DecimalFormat("###.##");
         StringBuffer SizeCm = new StringBuffer();
         SizeCm.append(works.getSizeCmLength() == null ? "" : deFormat.format(works.getSizeCmLength()));
         String length = works.getSizeCmLength() == null ? "" : works.getSizeCmLength().toString();
         String width = works.getSizeCmWidth() == null ? "" : works.getSizeCmWidth().toString();
         String height = works.getSizeCmHeight() == null ? "" : works.getSizeCmHeight().toString();
         SizeCm.append(returnX(length, width));
         SizeCm.append(works.getSizeCmWidth() == null ? "" : deFormat.format(works.getSizeCmWidth()));
         SizeCm.append(returnX(width, height));
         if("".equals(width) && !"".equals(height) && !"".equals(length)){SizeCm.append("X");}
         SizeCm.append(works.getSizeCmHeight() == null ? "" : deFormat.format(works.getSizeCmHeight()));
         tmp.setSizeCm(SizeCm.toString());
         StringBuffer SizeIn = new StringBuffer();
         String lengthIn = works.getSizeInLength() == null ? "" : works.getSizeInLength().toString();
         String widthIn = works.getSizeInWidth() == null ? "" : works.getSizeInWidth().toString();
         String heightIn = works.getSizeInHeight() == null ? "" : works.getSizeInHeight().toString();
         SizeIn.append(works.getSizeInLength() == null ? "" : deFormat.format(works.getSizeInLength()));
         SizeIn.append(returnX(lengthIn, widthIn));
         SizeIn.append(works.getSizeInWidth() == null ? "" : deFormat.format(works.getSizeInWidth()));
         SizeIn.append(returnX(widthIn, heightIn));
         if("".equals(widthIn) && !"".equals(heightIn) && !"".equals(lengthIn)){SizeIn.append("X");}
         SizeIn.append(works.getSizeInHeight() == null ? "" : deFormat.format(works.getSizeInHeight()));
         tmp.setSizeIn(SizeIn.toString());
         StringBuffer SizeRule = new StringBuffer();
         String lengthRule = works.getSizeRuleLength() == null ? "" : works.getSizeRuleLength().toString();
         String widthRule = works.getSizeRuleWidth() == null ? "" : works.getSizeRuleWidth().toString();
         String heightRule = works.getSizeRuleHeight() == null ? "" : works.getSizeRuleHeight().toString();
         SizeRule.append(works.getSizeRuleLength() == null ? "" : deFormat.format(works.getSizeRuleLength()));
         SizeRule.append(returnX(lengthRule, widthRule));
         SizeRule.append(works.getSizeRuleWidth() == null ? "" : deFormat.format(works.getSizeRuleWidth()));
         SizeRule.append(returnX(widthRule, heightRule));
         if("".equals(widthRule) && !"".equals(heightRule) && !"".equals(lengthRule)){SizeRule.append("X");}
         SizeRule.append(works.getSizeRuleHeight() == null ? "" : deFormat.format(works.getSizeRuleHeight()));
         tmp.setSizeRule(SizeRule.toString());
         tmp.setArtId(works.getArtistId());
         tmp.setAuthor(artist.getCname());
         tmp.setCnName(works.getWorksCName());
         tmp.setEnName(works.getWorksEName());
         tmp.setImageUrl(works.getThumbnail());
         artAuctionDao.update(tmp);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void deleteArtAuction(Serializable id) throws ServiceException {
      try {
         artAuctionDao.delete(id);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void deleteArtAuctions(Serializable[] ids) throws ServiceException {
      try {
         artAuctionDao.deleteAll(ids);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public PageQuery queryArtAuction(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artAuctionList", pageQuery);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void createSqlFilter(PageQuery page) {
      String tmp = "";
      Map<String, String> map = page.getParameters();
      String str = map.get("createYear");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += "a.create_year like '%" + str.trim() + "%'";
      }
      str = map.get("worksName");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += " (a.cn_name like '%" + str.trim() + "%' or a.en_name like '%" + str.trim() + "%' ) ";
      }
      str = map.get("cName");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += "a.author like '%" + str.trim() + "%'";
      }
      str = map.get("worksId");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += "a.works_id=" + str.trim();
      }
      if (tmp.length() > 0) map.put("paras", tmp);
   }

   public void createArtAuction(Map<String, Object> map, List<ArtCollectArtist> artists, List<ArtAuctionHouses> houses) throws ServiceException {
      try {
         String author = map.get("author") == null ? null : map.get("author").toString().trim();//作者
         if (author != null && !"".equals(author)) {
            String number = map.get("number") == null ? null : map.get("number").toString();//编号
            String cnName = map.get("cnName") == null ? null : map.get("cnName").toString();//作品中文名
            String enName = map.get("enName") == null ? null : map.get("enName").toString();//作品英文名
            String createYear = map.get("createYear") == null || "".equals(map.get("createYear")) ? null : map.get("createYear").toString();//创作年份
            String sizeCm = map.get("artSize") == null ? null : map.get("artSize").toString();//尺寸
            String auctionPlace = map.get("auctionPlace") == null ? null : map.get("auctionPlace").toString();//拍卖地
            String auctionTime = map.get("auctionDate") == null || "".equals(map.get("auctionDate")) ? null : map.get("auctionDate").toString().trim();//拍卖时间
            String worksCategory = map.get("type") == null ? null : map.get("type").toString();//类别
            String auctionHouse = map.get("auctionCompany") == null ? "" : map.get("auctionCompany").toString();//拍卖行
            String nationality = map.get("country") == null ? null : map.get("country").toString();//国家
            String source = map.get("source") == null ? null : map.get("source").toString();//来源
            String auctionLocal = map.get("auctionLocal") == null ? null : map.get("auctionLocal").toString();//拍卖会
            String priceUnit = map.get("priceUnit") == null ? "" : map.get("priceUnit").toString().trim();//价格单位
            String lastPrice = map.get("lastPrice") == null || "".equals(map.get("lastPrice")) ? null : map.get("lastPrice").toString();//最底价
            String maxPrice = map.get("maxPrice") == null || "".equals(map.get("maxPrice")) ? null : map.get("maxPrice").toString();//最高价
            String price = map.get("price") == null || "".equals(map.get("price")) ? null : map.get("price").toString();//成交价
            String imageUrl = map.get("imageUrl") == null || "".equals(map.get("imageUrl")) ? null : map.get("imageUrl").toString();//图片
            String content = map.get("content") == null || "".equals(map.get("content")) ? null : map.get("content").toString();//作品描述
            Integer artistId = null;
            Integer houseId = null;
            String eName = null;
            ArtAuctionExchange exchange = null;
            if (auctionTime != null && !"".equals(auctionTime)) {
               String year = outFormat.format(auctionTime).substring(0, 4);
               List<ArtAuctionExchange> exchanges = artAuctionExchangeDao.findByField(ArtAuctionExchange.PROP_YEAR, Integer.valueOf(year));
               if (exchanges != null && exchanges.size() > 0) exchange = exchanges.get(0);
            }

            for (ArtCollectArtist artist : artists) {
               // 查找是否能在艺术家库找到对应的艺术家
               if (author.indexOf(artist.getArtistName()) > -1) {
                  artistId = artist.getArtistId();
                  eName = artist.getFolderName();
                  break;
               }
            }
            for (ArtAuctionHouses house : houses) {
               // 查找是否能在拍卖行库找到对应的拍卖行
               if (house.getAuctionHouse().indexOf(auctionHouse) > -1) {
                  houseId = house.getId();
                  break;
               }
            }

            String storeName = imageUrl == null ? null : imageUrl.substring(0, imageUrl.lastIndexOf("."));
            // 判断是否为图片，如果不是则不保存图片名称
            if (imageUrl != null) {
               String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();
               if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("tiff")
                     || extension.equals("bmp")) {
                  imageUrl = imageUrl == null ? null : imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
               }
               else {
                  imageUrl = null;
               }
            }
            if (artistId != null) {
               ArtWorks works = new ArtWorks();
               works.setArtistId(artistId);
               works.setCreateYear(createYear);
               works.setWorksCName(cnName);
               works.setWorksEName(enName);
               works.setWorksNo(number);
               works.setWorksImage(imageUrl);
               works.setWorksStoreName(storeName);
               works.setThumbnail(imageUrl);
               works.setSizeCm(sizeCm);
               works.setRepeatMarker("0");
               works.setWorksStatus(content);
               Integer worksId = (Integer) artWorksDao.save(works);
               houseId = saveAuctionHouse(auctionHouse, nationality, houseId);
               ArtAuction auction = new ArtAuction();
               auction.setArtId(artistId);
               auction.setWorksId(worksId);
               auction.setAuctionHousesId(houseId);
               auction.setAuctionNo(number);
               auction.setCreateYear(createYear);
               auction.setAuction(auctionLocal);
               auction.setAuctionTime(auctionTime == null ? null : outFormat.parse(auctionTime));
               auction.setWorksCategory(worksCategory);
               auction.setSaleName(auctionPlace);
               auction.setWorksSource(source);
               auction.setImageUrl(imageUrl == null ? null : imageUrl);
               auction.setAuctionDesc(content);
               auction.setSizeCm(sizeCm);
               auction.setAuthor(author);
               auction.setCnName(cnName);
               auction.setEnName(enName);
               auction.setAuctionNo(number);
               acuctionPrice(priceUnit, lastPrice, maxPrice, price, auction, exchange);
               artAuctionDao.save(auction);
            }
         }
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }

   }

   private void acuctionPrice(String priceUnit, String lastPrice, String maxPrice, String price, ArtAuction auction, ArtAuctionExchange exchange) {
      if (priceUnit.toUpperCase().indexOf("RMB") > -1) {//
         auction.setCtradePrice(price == null ? null : Double.valueOf(price));
         auction.setChighestPrice(maxPrice == null ? null : Double.valueOf(maxPrice));
         auction.setClowestPrice(lastPrice == null ? null : Double.valueOf(lastPrice));
         auction.setCtranPrice(price == null ? null : Double.valueOf(price));
      }
      if (priceUnit.toUpperCase().indexOf("HKD") > -1) {//港币
         auction.setHkTradePrice(price == null ? null : Double.valueOf(price));
         auction.setHkHighestPrice(maxPrice == null ? null : Double.valueOf(maxPrice));
         auction.setHkLowestPrice(lastPrice == null ? null : Double.valueOf(lastPrice));
         auction.setHkTranPrice(price == null ? null : Double.valueOf(price));
      }
      if (priceUnit.toUpperCase().indexOf("GBP") > -1) {//英镑
         auction.setPtradePrice(price == null ? null : Double.valueOf(price));
         auction.setPhighestPrice(maxPrice == null ? null : Double.valueOf(maxPrice));
         auction.setPlowestPrice(lastPrice == null ? null : Double.valueOf(lastPrice));
         auction.setPtranPrice(price == null ? null : Double.valueOf(price));
      }
      if (priceUnit.toUpperCase().indexOf("USD") > -1) {//美元
         auction.setDtradePrice(price == null ? null : Double.valueOf(price));
         auction.setDhighestPrice(maxPrice == null ? null : Double.valueOf(maxPrice));
         auction.setDlowestPrice(lastPrice == null ? null : Double.valueOf(lastPrice));
         auction.setDtranPrice(price == null ? null : Double.valueOf(price));
      }
      if (priceUnit.toUpperCase().indexOf("EUR") > -1) {//欧元
         auction.setEtradePrice(price == null ? null : Double.valueOf(price));
         auction.setEhighestPrice(maxPrice == null ? null : Double.valueOf(maxPrice));
         auction.setElowestPrice(lastPrice == null ? null : Double.valueOf(lastPrice));
         auction.setEtranPrice(price == null ? null : Double.valueOf(price));
      }
      setPrice(priceUnit, lastPrice, maxPrice, price, auction, exchange);
   }

   private Integer saveAuctionHouse(String auctionHouse, String nationality, Integer houseId) {
      if (houseId == null) {
         ArtAuctionHouses house = new ArtAuctionHouses();
         house.setAuctionHouse(auctionHouse);
         house.setNationality(nationality);
         houseId = (Integer) artAuctionHousesDao.save(house);
      }
      return houseId;
   }

   private void setPrice(String priceUnit, String lastPrice, String maxPrice, String price, ArtAuction auction, ArtAuctionExchange exchange) {
      Double dExchangeRate = exchange.getDexchangeRate();//美元
      Double pExchangeRate = exchange.getPexchangeRate();//英镑
      Double eExchangeRate = exchange.getEexchangeRate();//欧元
      Double hkExchangeRate = exchange.getHkExchangeRate();//港币

      if (priceUnit.toUpperCase().indexOf("RMB") > -1) {//
         //港币
         auction.setHkTradePrice(price == null ? null : hkExchangeRate * Double.valueOf(price));
         auction.setHkHighestPrice(maxPrice == null ? null : hkExchangeRate * Double.valueOf(maxPrice));
         auction.setHkLowestPrice(lastPrice == null ? null : hkExchangeRate * Double.valueOf(lastPrice));
         auction.setHkTranPrice(price == null ? null : hkExchangeRate * Double.valueOf(price));
         //英镑
         auction.setPtradePrice(price == null ? null : pExchangeRate * Double.valueOf(price));
         auction.setPhighestPrice(maxPrice == null ? null : pExchangeRate * Double.valueOf(maxPrice));
         auction.setPlowestPrice(lastPrice == null ? null : pExchangeRate * Double.valueOf(lastPrice));
         auction.setPtranPrice(price == null ? null : pExchangeRate * Double.valueOf(price));
         //美元
         auction.setDtradePrice(price == null ? null : dExchangeRate * Double.valueOf(price));
         auction.setDhighestPrice(maxPrice == null ? null : dExchangeRate * Double.valueOf(maxPrice));
         auction.setDlowestPrice(lastPrice == null ? null : dExchangeRate * Double.valueOf(lastPrice));
         auction.setDtranPrice(price == null ? null : dExchangeRate * Double.valueOf(price));
         //欧元
         auction.setEtradePrice(price == null ? null : eExchangeRate * Double.valueOf(price));
         auction.setEhighestPrice(maxPrice == null ? null : eExchangeRate * Double.valueOf(maxPrice));
         auction.setElowestPrice(lastPrice == null ? null : eExchangeRate * Double.valueOf(lastPrice));
         auction.setEtranPrice(price == null ? null : eExchangeRate * Double.valueOf(price));

      }
      if (priceUnit.toUpperCase().indexOf("HKD") > -1) {
         
         Double rmbExchangeRate = 1.0 / hkExchangeRate;//转换人民币汇率
         
         Double rmbPrice= price == null ? null : rmbExchangeRate * Double.valueOf(price);//转换人民币
         Double rmbMaxPrice = price == null ? null : rmbExchangeRate * Double.valueOf(maxPrice);//转换人民币
         Double rmbLastPrice = price == null ? null : rmbExchangeRate * Double.valueOf(lastPrice);//转换人民币
         
         auction.setCtradePrice(rmbPrice);
         auction.setChighestPrice(rmbMaxPrice);
         auction.setClowestPrice(rmbLastPrice);
         auction.setCtranPrice(rmbPrice);
         
         //英镑
         auction.setPtradePrice(price == null ? null : pExchangeRate * rmbPrice);
         auction.setPhighestPrice(maxPrice == null ? null : pExchangeRate * rmbMaxPrice);
         auction.setPlowestPrice(lastPrice == null ? null : pExchangeRate * rmbLastPrice);
         auction.setPtranPrice(price == null ? null : pExchangeRate * rmbPrice);
         //美元
         auction.setDtradePrice(price == null ? null : dExchangeRate * rmbPrice);
         auction.setDhighestPrice(maxPrice == null ? null : dExchangeRate * rmbMaxPrice);
         auction.setDlowestPrice(lastPrice == null ? null : dExchangeRate * rmbLastPrice);
         auction.setDtranPrice(price == null ? null : dExchangeRate * rmbPrice);
         //欧元
         auction.setEtradePrice(price == null ? null : eExchangeRate * rmbPrice);
         auction.setEhighestPrice(maxPrice == null ? null : eExchangeRate * rmbMaxPrice);
         auction.setElowestPrice(lastPrice == null ? null : eExchangeRate * rmbLastPrice);
         auction.setEtranPrice(price == null ? null : eExchangeRate * rmbPrice);
         
      }
      if (priceUnit.toUpperCase().indexOf("GBP") > -1) {//英镑
         Double rmbExchangeRate = 1.0 / pExchangeRate;//转换人民币汇率
         
         Double rmbPrice= price == null ? null : rmbExchangeRate * Double.valueOf(price);//转换人民币
         Double rmbMaxPrice = price == null ? null : rmbExchangeRate * Double.valueOf(maxPrice);//转换人民币
         Double rmbLastPrice = price == null ? null : rmbExchangeRate * Double.valueOf(lastPrice);//转换人民币
         
         auction.setCtradePrice(rmbPrice);
         auction.setChighestPrice(rmbMaxPrice);
         auction.setClowestPrice(rmbLastPrice);
         auction.setCtranPrice(rmbPrice);
         
         auction.setHkTradePrice(price == null ? null : hkExchangeRate * rmbPrice);
         auction.setHkHighestPrice(maxPrice == null ? null : hkExchangeRate * rmbMaxPrice);
         auction.setHkLowestPrice(lastPrice == null ? null : hkExchangeRate * rmbLastPrice);
         auction.setHkTranPrice(price == null ? null : hkExchangeRate * rmbPrice);
         //美元
         auction.setDtradePrice(price == null ? null : dExchangeRate * rmbPrice);
         auction.setDhighestPrice(maxPrice == null ? null : dExchangeRate * rmbMaxPrice);
         auction.setDlowestPrice(lastPrice == null ? null : dExchangeRate * rmbLastPrice);
         auction.setDtranPrice(price == null ? null : dExchangeRate * rmbPrice);
         //欧元
         auction.setEtradePrice(price == null ? null : eExchangeRate * rmbPrice);
         auction.setEhighestPrice(maxPrice == null ? null : eExchangeRate * rmbMaxPrice);
         auction.setElowestPrice(lastPrice == null ? null : eExchangeRate * rmbLastPrice);
         auction.setEtranPrice(price == null ? null : eExchangeRate * rmbPrice);
      }
      if (priceUnit.toUpperCase().indexOf("USD") > -1) {//美元
         Double rmbExchangeRate = 1.0 / dExchangeRate;//转换人民币汇率
         
         Double rmbPrice= price == null ? null : rmbExchangeRate * Double.valueOf(price);//转换人民币
         Double rmbMaxPrice = price == null ? null : rmbExchangeRate * Double.valueOf(maxPrice);//转换人民币
         Double rmbLastPrice = price == null ? null : rmbExchangeRate * Double.valueOf(lastPrice);//转换人民币
         
         auction.setCtradePrice(rmbPrice);
         auction.setChighestPrice(rmbMaxPrice);
         auction.setClowestPrice(rmbLastPrice);
         auction.setCtranPrice(rmbPrice);
         
         auction.setHkTradePrice(price == null ? null : hkExchangeRate * rmbPrice);
         auction.setHkHighestPrice(maxPrice == null ? null : hkExchangeRate * rmbMaxPrice);
         auction.setHkLowestPrice(lastPrice == null ? null : hkExchangeRate * rmbLastPrice);
         auction.setHkTranPrice(price == null ? null : hkExchangeRate * rmbPrice);
         //英镑
         auction.setPtradePrice(price == null ? null : pExchangeRate * rmbPrice);
         auction.setPhighestPrice(maxPrice == null ? null : pExchangeRate * rmbMaxPrice);
         auction.setPlowestPrice(lastPrice == null ? null : pExchangeRate * rmbLastPrice);
         auction.setPtranPrice(price == null ? null : pExchangeRate * rmbPrice);
         //欧元
         auction.setEtradePrice(price == null ? null : eExchangeRate * rmbPrice);
         auction.setEhighestPrice(maxPrice == null ? null : eExchangeRate * rmbMaxPrice);
         auction.setElowestPrice(lastPrice == null ? null : eExchangeRate * rmbLastPrice);
         auction.setEtranPrice(price == null ? null : eExchangeRate * rmbPrice);
      }
      if (priceUnit.toUpperCase().indexOf("EUR") > -1) {//欧元
         Double rmbExchangeRate = 1.0 / eExchangeRate;//转换人民币汇率
         
         Double rmbPrice= price == null ? null : rmbExchangeRate * Double.valueOf(price);//转换人民币
         Double rmbMaxPrice = price == null ? null : rmbExchangeRate * Double.valueOf(maxPrice);//转换人民币
         Double rmbLastPrice = price == null ? null : rmbExchangeRate * Double.valueOf(lastPrice);//转换人民币
         
         auction.setCtradePrice(rmbPrice);
         auction.setChighestPrice(rmbMaxPrice);
         auction.setClowestPrice(rmbLastPrice);
         auction.setCtranPrice(rmbPrice);
         
         auction.setHkTradePrice(price == null ? null : hkExchangeRate * rmbPrice);
         auction.setHkHighestPrice(maxPrice == null ? null : hkExchangeRate * rmbMaxPrice);
         auction.setHkLowestPrice(lastPrice == null ? null : hkExchangeRate * rmbLastPrice);
         auction.setHkTranPrice(price == null ? null : hkExchangeRate * rmbPrice);
         //美元
         auction.setDtradePrice(price == null ? null : dExchangeRate * rmbPrice);
         auction.setDhighestPrice(maxPrice == null ? null : dExchangeRate * rmbMaxPrice);
         auction.setDlowestPrice(lastPrice == null ? null : dExchangeRate * rmbLastPrice);
         auction.setDtranPrice(price == null ? null : dExchangeRate * rmbPrice);
         //英镑
         auction.setPtradePrice(price == null ? null : pExchangeRate * rmbPrice);
         auction.setPhighestPrice(maxPrice == null ? null : pExchangeRate * rmbMaxPrice);
         auction.setPlowestPrice(lastPrice == null ? null : pExchangeRate * rmbLastPrice);
         auction.setPtranPrice(price == null ? null : pExchangeRate * rmbPrice);
      }
   }
   
   private String decimalFormatString(String d){
      DecimalFormat    decimalFormat     = new DecimalFormat("###.##");
      if("".equals(d)){
         return "";
      }
      return decimalFormat.format(Double.valueOf(d));
   }
   
   private Double stringToDouble(String str){
      if(str == null || "".equals(str.trim())){
         return null;
      }
      return Double.valueOf(str);
   }

   public String importArtAuction(String path) throws ServiceException {
      String message = "";
      try {
         List<ArtWorks> artWorks = artWorksDao.findAll();
         Map<String, String> artWorksMap = new HashMap<String, String>();
         for (ArtWorks artWorks2 : artWorks) {
            artWorksMap.put(artWorks2.getWorksNo(), artWorks2.getId().toString());
         }
         List<ArtAuctionHouses> artAuctionHouses = artAuctionHousesDao.findAll();
         Map<String, String> artAuctionHousesMap = new HashMap<String, String>();
         for (ArtAuctionHouses artAuctionHouses2 : artAuctionHouses) {
            artAuctionHousesMap.put(artAuctionHouses2.getAuctionHouse(), artAuctionHouses2.getId().toString());
         }
         List<SysCode> sysCodes = sysCodeService.findCodeBySetId(25);
         Map<String, String> map = new HashMap<String, String>();
         for (SysCode sysCode : sysCodes) {
            map.put(sysCode.getItemName(), sysCode.getItemValue());
         }
         InputStream is = new FileInputStream(path);
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
         // 循环工作表Sheet
         HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
         if (hssfSheet != null) {
            for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = hssfSheet.getRow(rowNum);
               if (hssfRow == null) continue;

               ArtAuction artAuction = new ArtAuction();
               if (hssfRow.getCell(0) == null) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               if ("".equals(getValue(hssfRow.getCell(0)).trim())) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               //作品编号
               String worksNo = getValue(hssfRow.getCell(0)) == null ? "" : getValue(hssfRow.getCell(0)).trim();
               if (artWorksMap.get(worksNo) == null) {
                  message = "第" + String.valueOf(rowNum) + "行找不到作品编号";
                  break;
               }
               artAuction.setWorksId(Integer.valueOf(artWorksMap.get(worksNo)));
               ArtWorks works = artWorksDao.get(Integer.valueOf(artWorksMap.get(worksNo)));
               String sizeCmWidth = decimalFormatString(works.getSizeCmWidth() == null ? "" : works.getSizeCmWidth().toString());
               String sizeCmHeight = decimalFormatString(works.getSizeCmHeight() == null ? "" : works.getSizeCmHeight().toString());
               String sizeCmLength = decimalFormatString(works.getSizeCmLength() == null ? "" : works.getSizeCmLength().toString());
               String sizeCm = sizeCmLength + ("".equals(sizeCmWidth) ? "" : "X") + sizeCmWidth + ("".equals(sizeCmHeight) ? "" : "X") + sizeCmHeight;
               String sizeInWidth = decimalFormatString(works.getSizeInWidth() == null ? "" : works.getSizeInWidth().toString());
               String sizeInHeight = decimalFormatString(works.getSizeInHeight() == null ? "" : works.getSizeInHeight().toString());
               String sizeInLength = decimalFormatString(works.getSizeInLength() == null ? "" : works.getSizeInLength().toString());
               String sizeIn = sizeInLength + ("".equals(sizeInWidth) ? "" : "X") + sizeInWidth + ("".equals(sizeInHeight) ? "" : "X") + sizeInHeight;
               String sizeRuleWidth = decimalFormatString(works.getSizeRuleWidth() == null ? "" : works.getSizeRuleWidth().toString());
               String sizeRuleHeight = decimalFormatString(works.getSizeRuleHeight() == null ? "" : works.getSizeRuleHeight().toString());
               String sizeRuleLength = decimalFormatString(works.getSizeRuleLength() == null ? "" : works.getSizeRuleLength().toString());
               String sizeRule = sizeRuleLength + ("".equals(sizeRuleWidth) ? "" : "X") + sizeRuleWidth + ("".equals(sizeRuleHeight) ? "" : "X") + sizeRuleHeight;
               artAuction.setSizeCm(sizeCm);
               artAuction.setSizeIn(sizeIn);
               artAuction.setSizeRule(sizeRule);
               artAuction.setArtId(works.getArtistId());
               artAuction.setImageUrl(works.getThumbnail());
               artAuction.setCnName(works.getWorksCName());
               artAuction.setEnName(works.getWorksEName());
               artAuction.setWorksCategory(works.getStyleType());
               artAuction.setCreateYear(works.getCreateYear());
               artAuction.setCreateMonth(works.getCreateMonth());
               artAuction.setCreateDay(works.getCreateDay());
               ArtArtist artist = artArtistDao.get(works.getArtistId());
               String author = artist.getCname();
               if (author == null || "".equals(author)) author = artist.getEname();
               artAuction.setAuthor(author);
               //标识
               String auctionIdentity = getValue(hssfRow.getCell(3));
               artAuction.setAuctionIdentity(auctionIdentity);
               //拍卖编号
               String auctionNo = getValue(hssfRow.getCell(4));
               artAuction.setAuctionNo(auctionNo);
               //拍场
               String saleName = getValue(hssfRow.getCell(5));
               artAuction.setSaleName(saleName);
               //拍卖时间
               HSSFCell auctionTime = hssfRow.getCell(6);
               if (auctionTime == null) {
                  message = "第" + String.valueOf(rowNum) + "行找不到拍卖时间";
                  break;
               }else{
                  Date timeDate = hssfRow.getCell(6).getDateCellValue();
                  artAuction.setAuctionTime(timeDate);
               }
               //拍卖行
               String auctionHouses = getValue(hssfRow.getCell(7)) == null ? "" : getValue(hssfRow.getCell(7)).trim();
               if (artAuctionHousesMap.get(auctionHouses) == null) {
                  message = "第" + String.valueOf(rowNum) + "行拍卖行不能为空";
                  break;
               }
               artAuction.setAuctionHousesId(Integer.valueOf(artAuctionHousesMap.get(auctionHouses)));
               //拍卖会
               String auction = getValue(hssfRow.getCell(8));
               artAuction.setAuction(auction);
               //季节
               String autionSerson = getValue(hssfRow.getCell(9)) == null ? "" : getValue(hssfRow.getCell(9)).trim();
               if (!"".equals(autionSerson)) {
                  if (map.get(autionSerson) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到季节";
                     break;
                  }
                  else {
                     artAuction.setAutionSeason(map.get(autionSerson));
                  }
               }
               //成交价(人民币)
               String cTranPrice = getValue(hssfRow.getCell(10));
               artAuction.setCtranPrice(stringToDouble(cTranPrice));
               //成交价(美元)
               String dTranPrice = getValue(hssfRow.getCell(11));
               artAuction.setDtranPrice(stringToDouble(dTranPrice));
               //成交价(欧元)
               String eTranPrice = getValue(hssfRow.getCell(12));
               artAuction.setEtranPrice(stringToDouble(eTranPrice));
               //成交价(英镑)
               String pTranPrice = getValue(hssfRow.getCell(13));
               artAuction.setPtranPrice(stringToDouble(pTranPrice));
               //成交价(港币)
               String hkTranPrice = getValue(hssfRow.getCell(14));
               artAuction.setHkTranPrice(stringToDouble(hkTranPrice));
               //交易价(人民币)
               String cTradePrice = getValue(hssfRow.getCell(15));
               artAuction.setCtradePrice(stringToDouble(cTradePrice));
               //交易价(美元)
               String dTradePrice = getValue(hssfRow.getCell(16));
               artAuction.setDtradePrice(stringToDouble(dTradePrice));
               //交易价(欧元)
               String eTradePrice = getValue(hssfRow.getCell(17));
               artAuction.setEtradePrice(stringToDouble(eTradePrice));
               //交易价(英镑)
               String pTradePrice = getValue(hssfRow.getCell(18));
               artAuction.setPtradePrice(stringToDouble(pTradePrice));
               //交易价(港币)
               String hkTradePrice = getValue(hssfRow.getCell(19));
               artAuction.setHkTradePrice(stringToDouble(hkTradePrice));
               //最低估价(人民币)
               String cLowestPrice = getValue(hssfRow.getCell(20));
               artAuction.setClowestPrice(stringToDouble(cLowestPrice));
               //最低估价(美元)
               String dLowestPrice = getValue(hssfRow.getCell(21));
               artAuction.setDlowestPrice(stringToDouble(dLowestPrice));
               //最低估价(欧元)
               String eLowestPrice = getValue(hssfRow.getCell(22));
               artAuction.setElowestPrice(stringToDouble(eLowestPrice));
               //最低估价(英镑)
               String pLowestPrice = getValue(hssfRow.getCell(23));
               artAuction.setPlowestPrice(stringToDouble(pLowestPrice));
               //最低估价(港币)
               String hkLowestPrice = getValue(hssfRow.getCell(24));
               artAuction.setHkLowestPrice(stringToDouble(hkLowestPrice));
               //最高估价(人民币)
               String cHighestPrice = getValue(hssfRow.getCell(25));
               artAuction.setChighestPrice(stringToDouble(cHighestPrice));
               //最高估价(美元)
               String dHighestPrice = getValue(hssfRow.getCell(26));
               artAuction.setDhighestPrice(stringToDouble(dHighestPrice));
               //最高估价(欧元)
               String eHighestPrice = getValue(hssfRow.getCell(27));
               artAuction.setEhighestPrice(stringToDouble(eHighestPrice));
               //最高估价(英镑)
               String pHighestPrice = getValue(hssfRow.getCell(28));
               artAuction.setPhighestPrice(stringToDouble(pHighestPrice));
               //最高估价(港币)
               String hkHighestPrice = getValue(hssfRow.getCell(29));
               artAuction.setHkHighestPrice(stringToDouble(hkHighestPrice));
               //拍卖说明
               String auctionDesc = getValue(hssfRow.getCell(30));
               artAuction.setAuctionDesc(auctionDesc);
               //作品来源
               String worksSource = getValue(hssfRow.getCell(31));
               artAuction.setWorksSource(worksSource);
               createArtAuction(artAuction);
            }
            if ("".equals(message)) {
               message = "成功操作到第" + String.valueOf(hssfSheet.getLastRowNum()) + "行";
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         message = "数据错误";
      }
      File file = new File(path);
      if (file.exists() && file.isFile()) {
         file.delete();
      }
      return message;
   }

   private String getValue(HSSFCell hssfCell) {
      if(hssfCell == null){
         return null;
      }
      if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
         // 返回布尔类型的值   
         return String.valueOf(hssfCell.getBooleanCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
         // 返回数值类型的值  
         DecimalFormat df = new DecimalFormat("###.##");
         return df.format(hssfCell.getNumericCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
         // 返回数值类型的值  
         return String.valueOf(hssfCell.getNumericCellValue());
      }
      else {
         // 返回字符串类型的值      
         return String.valueOf(hssfCell.getStringCellValue());
      }
   }

   
   @SuppressWarnings("deprecation")
   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException{
      try {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
         HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
         HSSFRow row = sheet.createRow((int) 0);  
         
         HSSFCell cell = row.createCell((short) 0);  
         cell.setCellValue("拍卖编号");  
         cell = row.createCell((short) 1);  
         cell.setCellValue("作品");  
         cell = row.createCell((short) 2);  
         cell.setCellValue("拍卖会");  
         cell = row.createCell((short) 3);  
         cell.setCellValue("拍场");  
         cell = row.createCell((short) 4);  
         cell.setCellValue("拍卖行");  
         cell = row.createCell((short) 5);  
         cell.setCellValue("拍卖时间");  
         cell = row.createCell((short) 6);  
         cell.setCellValue("标识");  
         cell = row.createCell((short) 7);  
         cell.setCellValue("季节");  
         cell = row.createCell((short) 8);  
         cell.setCellValue("作品来源");  
         cell = row.createCell((short) 9);  
         cell.setCellValue("创作时期");  
         cell = row.createCell((short) 10);  
         cell.setCellValue("拍卖说明");  
         cell = row.createCell((short) 11);  
         cell.setCellValue("成交价(人民币)");  
         cell = row.createCell((short) 12);  
         cell.setCellValue("成交价(美元)");  
         cell = row.createCell((short) 13);  
         cell.setCellValue("成交价(欧元)");  
         cell = row.createCell((short) 14);  
         cell.setCellValue("成交价(英镑)");  
         cell = row.createCell((short) 15);  
         cell.setCellValue("成交价(港币)");  
         cell = row.createCell((short) 16);  
         cell.setCellValue("交易价(人民币)");  
         cell = row.createCell((short) 17);  
         cell.setCellValue("交易价(美元)");  
         cell = row.createCell((short) 18);  
         cell.setCellValue("交易价(欧元)");  
         cell = row.createCell((short) 19);  
         cell.setCellValue("交易价(英镑)");  
         cell = row.createCell((short) 20);  
         cell.setCellValue("交易价(港币)");  
         cell = row.createCell((short) 21);  
         cell.setCellValue("最低估价(人民币)");  
         cell = row.createCell((short) 22);  
         cell.setCellValue("最低估价(美元)");  
         cell = row.createCell((short) 23);  
         cell.setCellValue("最低估价(欧元)");  
         cell = row.createCell((short) 24);  
         cell.setCellValue("最低估价(英镑)");  
         cell = row.createCell((short) 25);  
         cell.setCellValue("最低估价(港币)");  
         cell = row.createCell((short) 26);  
         cell.setCellValue("最高估价(人民币)");  
         cell = row.createCell((short) 27);  
         cell.setCellValue("最高估价(美元)");  
         cell = row.createCell((short) 28);  
         cell.setCellValue("最高估价(欧元)");  
         cell = row.createCell((short) 29);  
         cell.setCellValue("最高估价(英镑)");  
         cell = row.createCell((short) 30);  
         cell.setCellValue("最高估价(港币)");  
         
         pageQuery.setPageSize("-1");
         PageQuery pageQuery2 = queryArtAuction(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            row = sheet.createRow(i+1); 
            row.createCell((short) 0).setCellValue(returnString(item.get("auctionNo"))); 
            row.createCell((short) 1).setCellValue(returnString(item.get("worksCName"))); 
            row.createCell((short) 2).setCellValue(returnString(item.get("auction"))); 
            row.createCell((short) 3).setCellValue(returnString(item.get("saleName"))); 
            row.createCell((short) 4).setCellValue(returnString(item.get("auctionHouse"))); 
            row.createCell((short) 5).setCellValue(returnString(item.get("auctionTime"))); 
            row.createCell((short) 6).setCellValue(returnString(item.get("auctionIdentity"))); 
            row.createCell((short) 7).setCellValue(returnString(item.get("autionSeason"))); 
            row.createCell((short) 8).setCellValue(returnString(item.get("worksSource"))); 
            row.createCell((short) 9).setCellValue(returnString(item.get("createYear"))); 
            row.createCell((short) 10).setCellValue(returnString(item.get("auctionDesc"))); 
            row.createCell((short) 11).setCellValue(returnString(item.get("cTranPrice"))); 
            row.createCell((short) 12).setCellValue(returnString(item.get("dTranPrice"))); 
            row.createCell((short) 13).setCellValue(returnString(item.get("eTranPrice"))); 
            row.createCell((short) 14).setCellValue(returnString(item.get("pTranPrice"))); 
            row.createCell((short) 15).setCellValue(returnString(item.get("hkTranPrice"))); 
            row.createCell((short) 16).setCellValue(returnString(item.get("cTradePrice"))); 
            row.createCell((short) 17).setCellValue(returnString(item.get("dTradePrice"))); 
            row.createCell((short) 18).setCellValue(returnString(item.get("eTradePrice"))); 
            row.createCell((short) 19).setCellValue(returnString(item.get("pTradePrice"))); 
            row.createCell((short) 20).setCellValue(returnString(item.get("hkTradePrice"))); 
            row.createCell((short) 21).setCellValue(returnString(item.get("cLowestPrice"))); 
            row.createCell((short) 22).setCellValue(returnString(item.get("dLowestPrice"))); 
            row.createCell((short) 23).setCellValue(returnString(item.get("eLowestPrice"))); 
            row.createCell((short) 24).setCellValue(returnString(item.get("pLowestPrice"))); 
            row.createCell((short) 25).setCellValue(returnString(item.get("hkLowestPrice"))); 
            row.createCell((short) 26).setCellValue(returnString(item.get("cHighestPrice"))); 
            row.createCell((short) 27).setCellValue(returnString(item.get("dHighestPrice"))); 
            row.createCell((short) 28).setCellValue(returnString(item.get("eHighestPrice"))); 
            row.createCell((short) 29).setCellValue(returnString(item.get("pHighestPrice"))); 
            row.createCell((short) 30).setCellValue(returnString(item.get("hkHighestPrice"))); 
            i++;
         }
         return hssfWorkbook;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   @Resource
   private ArtAuctionDao         artAuctionDao;

   @Resource
   private SysCodeService         sysCodeService;
   
   @Resource
   private ArtAuctionHousesDao   artAuctionHousesDao;

   @Resource
   private ArtWorksDao           artWorksDao;

   @Resource
   private ArtArtistDao          artArtistDao;

   @Resource
   private ArtAuctionExchangeDao artAuctionExchangeDao;

}
