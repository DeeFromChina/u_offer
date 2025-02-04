package com.golead.art.auction.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_auction table.
 */
@Entity  
@Table(name="art_auction")  
public class ArtAuction implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties

   @Column(name = "auction")
   private java.lang.String auction;   // 拍卖会

   @Column(name = "auction_no")
   private java.lang.String auctionNo;   // 拍卖编号

   @Column(name = "e_trade_price")
   private java.lang.Double eTradePrice;   // 交易价(欧元)

   @Column(name = "c_tran_price")
   private java.lang.Double cTranPrice;   // 成交价(人民币)

   @Column(name = "sale_name")
   private java.lang.String saleName;   // 拍场

   @Column(name = "c_trade_price")
   private java.lang.Double cTradePrice;   // 交易价(人民币)

   @Column(name = "hk_highest_price")
   private java.lang.Double hkHighestPrice;   // 最高估价(港币)

   @Column(name = "en_name")
   private java.lang.String enName;   // 作品英文名称

   @Column(name = "auction_identity")
   private java.lang.String auctionIdentity;   // 标识


   @Column(name = "art_id")
   private java.lang.Integer artId;   // 作者

   @Column(name = "author")
   private java.lang.String author;   // 作者

   @Column(name = "create_month")
   private java.lang.String createMonth;   // 创作月份

   @Column(name = "p_trade_price")
   private java.lang.Double pTradePrice;   // 交易价(英镑)

   @Column(name = "image_url")
   private java.lang.String imageUrl;   // 图片

   @Column(name = "works_category")
   private java.lang.String worksCategory;   // 作品分类

   @Column(name = "works_id")
   private java.lang.Integer worksId;   // 作品id

   @Column(name = "size_in")
   private java.lang.String sizeIn;   // 尺寸(英寸)

   @Column(name = "hk_lowest_price")
   private java.lang.Double hkLowestPrice;   // 最低估价(港币)

   @Column(name = "cn_name")
   private java.lang.String cnName;   // 中文名称

   @Column(name = "p_tran_price")
   private java.lang.Double pTranPrice;   // 成交价(英镑)

   @Column(name = "d_lowest_price")
   private java.lang.Double dLowestPrice;   // 最低估价(美元)

   @Column(name = "d_trade_price")
   private java.lang.Double dTradePrice;   // 交易价(美元)

   @Column(name = "works_source")
   private java.lang.String worksSource;   // 作品来源

   @Column(name = "auction_time")
   private java.util.Date auctionTime;   // 拍卖时间

   @Column(name = "c_highest_price")
   private java.lang.Double cHighestPrice;   // 最高估价(人民币)

   @Column(name = "d_highest_price")
   private java.lang.Double dHighestPrice;   // 最高估价(美元)

   @Column(name = "aution_season")
   private java.lang.String autionSeason;   // 季节

   @Column(name = "c_lowest_price")
   private java.lang.Double cLowestPrice;   // 最低估价(人民币)

   @Column(name = "p_lowest_price")
   private java.lang.Double pLowestPrice;   // 最低估价(英镑)

   @Column(name = "create_day")
   private java.lang.String createDay;   // 创作日

   @Column(name = "auction_desc")
   private java.lang.String auctionDesc;   // 拍卖说明

   @Column(name = "size_cm")
   private java.lang.String sizeCm;   // 尺寸(cm)

   @Column(name = "e_lowest_price")
   private java.lang.Double eLowestPrice;   // 最低估价(欧元)

   @Column(name = "hk_tran_price")
   private java.lang.Double hkTranPrice;   // 成交价(港币)

   @Column(name = "d_tran_price")
   private java.lang.Double dTranPrice;   // 成交价(美元)

   @Column(name = "e_highest_price")
   private java.lang.Double eHighestPrice;   // 最高估价(欧元)

   @Column(name = "size_rule")
   private java.lang.String sizeRule;   // 尺寸(尺)

   @Column(name = "hk_trade_price")
   private java.lang.Double hkTradePrice;   // 交易价(港币)

   @Column(name = "p_highest_price")
   private java.lang.Double pHighestPrice;   // 最高估价(英镑)

   @Column(name = "e_tran_price")
   private java.lang.Double eTranPrice;   // 成交价(欧元)

   @Column(name = "auction_houses_id")
   private java.lang.Integer auctionHousesId;   // 拍卖行ID

   @Column(name = "create_year")
   private java.lang.String createYear;   // 创作年代
   
   @Column(name = "tran_status")
   private java.lang.String tranStatus;   // 创作年代
   
   @Column(name = "version_num")
   private java.lang.String versionNum;   // 创作年代

   // Constructors
   public ArtAuction() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getAuction() { 
      return this.auction; 
   }
   public void setAuction(java.lang.String auction) { 
      this.auction = auction; 
   }


   public java.lang.String getAuctionNo() { 
      return this.auctionNo; 
   }
   public void setAuctionNo(java.lang.String auctionNo) { 
      this.auctionNo = auctionNo; 
   }


   public java.lang.Double getEtradePrice() { 
      return this.eTradePrice; 
   }
   public void setEtradePrice(java.lang.Double eTradePrice) { 
      this.eTradePrice = eTradePrice; 
   }


   public java.lang.Double getCtranPrice() { 
      return this.cTranPrice; 
   }
   public void setCtranPrice(java.lang.Double cTranPrice) { 
      this.cTranPrice = cTranPrice; 
   }


   public java.lang.String getSaleName() { 
      return this.saleName; 
   }
   public void setSaleName(java.lang.String saleName) { 
      this.saleName = saleName; 
   }


   public java.lang.Double getCtradePrice() { 
      return this.cTradePrice; 
   }
   public void setCtradePrice(java.lang.Double cTradePrice) { 
      this.cTradePrice = cTradePrice; 
   }


   public java.lang.Double getHkHighestPrice() { 
      return this.hkHighestPrice; 
   }
   public void setHkHighestPrice(java.lang.Double hkHighestPrice) { 
      this.hkHighestPrice = hkHighestPrice; 
   }


   public java.lang.String getEnName() { 
      return this.enName; 
   }
   public void setEnName(java.lang.String enName) { 
      this.enName = enName; 
   }


   public java.lang.String getAuctionIdentity() { 
      return this.auctionIdentity; 
   }
   public void setAuctionIdentity(java.lang.String auctionIdentity) { 
      this.auctionIdentity = auctionIdentity; 
   }


   public java.lang.Integer getArtId() { 
      return this.artId; 
   }
   public void setArtId(java.lang.Integer artId) { 
      this.artId = artId; 
   }


   public java.lang.String getAuthor() { 
      return this.author; 
   }
   public void setAuthor(java.lang.String author) { 
      this.author = author; 
   }


   public java.lang.String getCreateMonth() { 
      return this.createMonth; 
   }
   public void setCreateMonth(java.lang.String createMonth) { 
      this.createMonth = createMonth; 
   }


   public java.lang.Double getPtradePrice() { 
      return this.pTradePrice; 
   }
   public void setPtradePrice(java.lang.Double pTradePrice) { 
      this.pTradePrice = pTradePrice; 
   }


   public java.lang.String getImageUrl() { 
      return this.imageUrl; 
   }
   public void setImageUrl(java.lang.String imageUrl) { 
      this.imageUrl = imageUrl; 
   }


   public java.lang.String getWorksCategory() { 
      return this.worksCategory; 
   }
   public void setWorksCategory(java.lang.String worksCategory) { 
      this.worksCategory = worksCategory; 
   }


   public java.lang.Integer getWorksId() { 
      return this.worksId; 
   }
   public void setWorksId(java.lang.Integer worksId) { 
      this.worksId = worksId; 
   }


   public java.lang.String getSizeIn() { 
      return this.sizeIn; 
   }
   public void setSizeIn(java.lang.String sizeIn) { 
      this.sizeIn = sizeIn; 
   }


   public java.lang.Double getHkLowestPrice() { 
      return this.hkLowestPrice; 
   }
   public void setHkLowestPrice(java.lang.Double hkLowestPrice) { 
      this.hkLowestPrice = hkLowestPrice; 
   }


   public java.lang.String getCnName() { 
      return this.cnName; 
   }
   public void setCnName(java.lang.String cnName) { 
      this.cnName = cnName; 
   }


   public java.lang.Double getPtranPrice() { 
      return this.pTranPrice; 
   }
   public void setPtranPrice(java.lang.Double pTranPrice) { 
      this.pTranPrice = pTranPrice; 
   }


   public java.lang.Double getDlowestPrice() { 
      return this.dLowestPrice; 
   }
   public void setDlowestPrice(java.lang.Double dLowestPrice) { 
      this.dLowestPrice = dLowestPrice; 
   }


   public java.lang.Double getDtradePrice() { 
      return this.dTradePrice; 
   }
   public void setDtradePrice(java.lang.Double dTradePrice) { 
      this.dTradePrice = dTradePrice; 
   }


   public java.lang.String getWorksSource() { 
      return this.worksSource; 
   }
   public void setWorksSource(java.lang.String worksSource) { 
      this.worksSource = worksSource; 
   }


   public java.util.Date getAuctionTime() { 
      return this.auctionTime; 
   }
   public void setAuctionTime(java.util.Date auctionTime) { 
      this.auctionTime = auctionTime; 
   }


   public java.lang.Double getChighestPrice() { 
      return this.cHighestPrice; 
   }
   public void setChighestPrice(java.lang.Double cHighestPrice) { 
      this.cHighestPrice = cHighestPrice; 
   }


   public java.lang.Double getDhighestPrice() { 
      return this.dHighestPrice; 
   }
   public void setDhighestPrice(java.lang.Double dHighestPrice) { 
      this.dHighestPrice = dHighestPrice; 
   }


   public java.lang.String getAutionSeason() { 
      return this.autionSeason; 
   }
   public void setAutionSeason(java.lang.String autionSeason) { 
      this.autionSeason = autionSeason; 
   }


   public java.lang.Double getClowestPrice() { 
      return this.cLowestPrice; 
   }
   public void setClowestPrice(java.lang.Double cLowestPrice) { 
      this.cLowestPrice = cLowestPrice; 
   }


   public java.lang.Double getPlowestPrice() { 
      return this.pLowestPrice; 
   }
   public void setPlowestPrice(java.lang.Double pLowestPrice) { 
      this.pLowestPrice = pLowestPrice; 
   }


   public java.lang.String getCreateDay() { 
      return this.createDay; 
   }
   public void setCreateDay(java.lang.String createDay) { 
      this.createDay = createDay; 
   }


   public java.lang.String getAuctionDesc() { 
      return this.auctionDesc; 
   }
   public void setAuctionDesc(java.lang.String auctionDesc) { 
      this.auctionDesc = auctionDesc; 
   }


   public java.lang.String getSizeCm() { 
      return this.sizeCm; 
   }
   public void setSizeCm(java.lang.String sizeCm) { 
      this.sizeCm = sizeCm; 
   }


   public java.lang.Double getElowestPrice() { 
      return this.eLowestPrice; 
   }
   public void setElowestPrice(java.lang.Double eLowestPrice) { 
      this.eLowestPrice = eLowestPrice; 
   }


   public java.lang.Double getHkTranPrice() { 
      return this.hkTranPrice; 
   }
   public void setHkTranPrice(java.lang.Double hkTranPrice) { 
      this.hkTranPrice = hkTranPrice; 
   }


   public java.lang.Double getDtranPrice() { 
      return this.dTranPrice; 
   }
   public void setDtranPrice(java.lang.Double dTranPrice) { 
      this.dTranPrice = dTranPrice; 
   }


   public java.lang.Double getEhighestPrice() { 
      return this.eHighestPrice; 
   }
   public void setEhighestPrice(java.lang.Double eHighestPrice) { 
      this.eHighestPrice = eHighestPrice; 
   }


   public java.lang.String getSizeRule() { 
      return this.sizeRule; 
   }
   public void setSizeRule(java.lang.String sizeRule) { 
      this.sizeRule = sizeRule; 
   }


   public java.lang.Double getHkTradePrice() { 
      return this.hkTradePrice; 
   }
   public void setHkTradePrice(java.lang.Double hkTradePrice) { 
      this.hkTradePrice = hkTradePrice; 
   }


   public java.lang.Double getPhighestPrice() { 
      return this.pHighestPrice; 
   }
   public void setPhighestPrice(java.lang.Double pHighestPrice) { 
      this.pHighestPrice = pHighestPrice; 
   }


   public java.lang.Double getEtranPrice() { 
      return this.eTranPrice; 
   }
   public void setEtranPrice(java.lang.Double eTranPrice) { 
      this.eTranPrice = eTranPrice; 
   }


   public java.lang.Integer getAuctionHousesId() { 
      return this.auctionHousesId; 
   }
   public void setAuctionHousesId(java.lang.Integer auctionHousesId) { 
      this.auctionHousesId = auctionHousesId; 
   }


   public java.lang.String getCreateYear() { 
      return this.createYear; 
   }
   public void setCreateYear(java.lang.String createYear) { 
      this.createYear = createYear; 
   }


   public java.lang.String getTranStatus() {
      return tranStatus;
   }

   public void setTranStatus(java.lang.String tranStatus) {
      this.tranStatus = tranStatus;
   }


   public java.lang.String getVersionNum() {
      return versionNum;
   }

   public void setVersionNum(java.lang.String versionNum) {
      this.versionNum = versionNum;
   }


   public static String REF_CLASS = "ArtAuction";
   public static String PROP_AUCTION = "auction";
   public static String PROP_AUCTION_NO = "auctionNo";
   public static String PROP_E_TRADE_PRICE = "eTradePrice";
   public static String PROP_C_TRAN_PRICE = "cTranPrice";
   public static String PROP_SALE_NAME = "saleName";
   public static String PROP_C_TRADE_PRICE = "cTradePrice";
   public static String PROP_HK_HIGHEST_PRICE = "hkHighestPrice";
   public static String PROP_EN_NAME = "enName";
   public static String PROP_AUCTION_IDENTITY = "auctionIdentity";
   public static String PROP_ID = "id";
   public static String PROP_ART_ID = "artId";
   public static String PROP_AUTHOR = "author";
   public static String PROP_CREATE_MONTH = "createMonth";
   public static String PROP_P_TRADE_PRICE = "pTradePrice";
   public static String PROP_IMAGE_URL = "imageUrl";
   public static String PROP_WORKS_CATEGORY = "worksCategory";
   public static String PROP_WORKS_ID = "worksId";
   public static String PROP_SIZE_IN = "sizeIn";
   public static String PROP_HK_LOWEST_PRICE = "hkLowestPrice";
   public static String PROP_CN_NAME = "cnName";
   public static String PROP_P_TRAN_PRICE = "pTranPrice";
   public static String PROP_D_LOWEST_PRICE = "dLowestPrice";
   public static String PROP_D_TRADE_PRICE = "dTradePrice";
   public static String PROP_WORKS_SOURCE = "worksSource";
   public static String PROP_AUCTION_TIME = "auctionTime";
   public static String PROP_C_HIGHEST_PRICE = "cHighestPrice";
   public static String PROP_D_HIGHEST_PRICE = "dHighestPrice";
   public static String PROP_AUTION_SEASON = "autionSeason";
   public static String PROP_C_LOWEST_PRICE = "cLowestPrice";
   public static String PROP_P_LOWEST_PRICE = "pLowestPrice";
   public static String PROP_CREATE_DAY = "createDay";
   public static String PROP_AUCTION_DESC = "auctionDesc";
   public static String PROP_SIZE_CM = "sizeCm";
   public static String PROP_E_LOWEST_PRICE = "eLowestPrice";
   public static String PROP_HK_TRAN_PRICE = "hkTranPrice";
   public static String PROP_D_TRAN_PRICE = "dTranPrice";
   public static String PROP_E_HIGHEST_PRICE = "eHighestPrice";
   public static String PROP_SIZE_RULE = "sizeRule";
   public static String PROP_HK_TRADE_PRICE = "hkTradePrice";
   public static String PROP_P_HIGHEST_PRICE = "pHighestPrice";
   public static String PROP_E_TRAN_PRICE = "eTranPrice";
   public static String PROP_AUCTION_HOUSES_ID = "auctionHousesId";
   public static String PROP_CREATE_YEAR = "createYear";
   public static String PROP_TRAN_STATUS = "tranStatus";
   public static String PROP_VERSION_NUM = "versionNum";

   public static String REF_TABLE = "art_auction";
   public static String COL_AUCTION = "auction";
   public static String COL_AUCTION_NO = "auction_no";
   public static String COL_E_TRADE_PRICE = "e_trade_price";
   public static String COL_C_TRAN_PRICE = "c_tran_price";
   public static String COL_SALE_NAME = "sale_name";
   public static String COL_C_TRADE_PRICE = "c_trade_price";
   public static String COL_HK_HIGHEST_PRICE = "hk_highest_price";
   public static String COL_EN_NAME = "en_name";
   public static String COL_AUCTION_IDENTITY = "auction_identity";
   public static String COL_ID = "id";
   public static String COL_ART_ID = "art_id";
   public static String COL_AUTHOR = "author";
   public static String COL_CREATE_MONTH = "create_month";
   public static String COL_P_TRADE_PRICE = "p_trade_price";
   public static String COL_IMAGE_URL = "image_url";
   public static String COL_WORKS_CATEGORY = "works_category";
   public static String COL_WORKS_ID = "works_id";
   public static String COL_SIZE_IN = "size_in";
   public static String COL_HK_LOWEST_PRICE = "hk_lowest_price";
   public static String COL_CN_NAME = "cn_name";
   public static String COL_P_TRAN_PRICE = "p_tran_price";
   public static String COL_D_LOWEST_PRICE = "d_lowest_price";
   public static String COL_D_TRADE_PRICE = "d_trade_price";
   public static String COL_WORKS_SOURCE = "works_source";
   public static String COL_AUCTION_TIME = "auction_time";
   public static String COL_C_HIGHEST_PRICE = "c_highest_price";
   public static String COL_D_HIGHEST_PRICE = "d_highest_price";
   public static String COL_AUTION_SEASON = "aution_season";
   public static String COL_C_LOWEST_PRICE = "c_lowest_price";
   public static String COL_P_LOWEST_PRICE = "p_lowest_price";
   public static String COL_CREATE_DAY = "create_day";
   public static String COL_AUCTION_DESC = "auction_desc";
   public static String COL_SIZE_CM = "size_cm";
   public static String COL_E_LOWEST_PRICE = "e_lowest_price";
   public static String COL_HK_TRAN_PRICE = "hk_tran_price";
   public static String COL_D_TRAN_PRICE = "d_tran_price";
   public static String COL_E_HIGHEST_PRICE = "e_highest_price";
   public static String COL_SIZE_RULE = "size_rule";
   public static String COL_HK_TRADE_PRICE = "hk_trade_price";
   public static String COL_P_HIGHEST_PRICE = "p_highest_price";
   public static String COL_E_TRAN_PRICE = "e_tran_price";
   public static String COL_AUCTION_HOUSES_ID = "auction_houses_id";
   public static String COL_CREATE_YEAR = "create_year";
   public static String COL_TRAN_STATUS = "tran_status";
   public static String COL_VERSION_NUM = "version_num";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.auction.model.ArtAuction)) return false;
      else {
         com.golead.art.auction.model.ArtAuction o = (com.golead.art.auction.model.ArtAuction) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtAuction:");
		buffer.append(" id:").append(id);
		buffer.append(" auction:").append(dealNull(auction));
		buffer.append(" auctionNo:").append(dealNull(auctionNo));
		buffer.append(" eTradePrice:").append(dealNull(eTradePrice));
		buffer.append(" cTranPrice:").append(dealNull(cTranPrice));
		buffer.append(" saleName:").append(dealNull(saleName));
		buffer.append(" cTradePrice:").append(dealNull(cTradePrice));
		buffer.append(" hkHighestPrice:").append(dealNull(hkHighestPrice));
		buffer.append(" enName:").append(dealNull(enName));
		buffer.append(" auctionIdentity:").append(dealNull(auctionIdentity));
		buffer.append(" artId:").append(dealNull(artId));
		buffer.append(" author:").append(dealNull(author));
		buffer.append(" createMonth:").append(dealNull(createMonth));
		buffer.append(" pTradePrice:").append(dealNull(pTradePrice));
		buffer.append(" imageUrl:").append(dealNull(imageUrl));
		buffer.append(" worksCategory:").append(dealNull(worksCategory));
		buffer.append(" worksId:").append(dealNull(worksId));
		buffer.append(" sizeIn:").append(dealNull(sizeIn));
		buffer.append(" hkLowestPrice:").append(dealNull(hkLowestPrice));
		buffer.append(" cnName:").append(dealNull(cnName));
		buffer.append(" pTranPrice:").append(dealNull(pTranPrice));
		buffer.append(" dLowestPrice:").append(dealNull(dLowestPrice));
		buffer.append(" dTradePrice:").append(dealNull(dTradePrice));
		buffer.append(" worksSource:").append(dealNull(worksSource));
		buffer.append(" auctionTime:").append(dealNull(auctionTime));
		buffer.append(" cHighestPrice:").append(dealNull(cHighestPrice));
		buffer.append(" dHighestPrice:").append(dealNull(dHighestPrice));
		buffer.append(" autionSeason:").append(dealNull(autionSeason));
		buffer.append(" cLowestPrice:").append(dealNull(cLowestPrice));
		buffer.append(" pLowestPrice:").append(dealNull(pLowestPrice));
		buffer.append(" createDay:").append(dealNull(createDay));
		buffer.append(" auctionDesc:").append(dealNull(auctionDesc));
		buffer.append(" sizeCm:").append(dealNull(sizeCm));
		buffer.append(" eLowestPrice:").append(dealNull(eLowestPrice));
		buffer.append(" hkTranPrice:").append(dealNull(hkTranPrice));
		buffer.append(" dTranPrice:").append(dealNull(dTranPrice));
		buffer.append(" eHighestPrice:").append(dealNull(eHighestPrice));
		buffer.append(" sizeRule:").append(dealNull(sizeRule));
		buffer.append(" hkTradePrice:").append(dealNull(hkTradePrice));
		buffer.append(" pHighestPrice:").append(dealNull(pHighestPrice));
		buffer.append(" eTranPrice:").append(dealNull(eTranPrice));
		buffer.append(" auctionHousesId:").append(dealNull(auctionHousesId));
		buffer.append(" createYear:").append(dealNull(createYear));
		buffer.append(" tranStatus:").append(dealNull(tranStatus));
		buffer.append(" versionNum:").append(dealNull(versionNum));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"auction\":\"").append(dealNull(auction)).append("\"");
		buffer.append(",\"auctionNo\":\"").append(dealNull(auctionNo)).append("\"");
		buffer.append(",\"eTradePrice\":\"").append(dealNull(eTradePrice)).append("\"");
		buffer.append(",\"cTranPrice\":\"").append(dealNull(cTranPrice)).append("\"");
		buffer.append(",\"saleName\":\"").append(dealNull(saleName)).append("\"");
		buffer.append(",\"cTradePrice\":\"").append(dealNull(cTradePrice)).append("\"");
		buffer.append(",\"hkHighestPrice\":\"").append(dealNull(hkHighestPrice)).append("\"");
		buffer.append(",\"enName\":\"").append(dealNull(enName)).append("\"");
		buffer.append(",\"auctionIdentity\":\"").append(dealNull(auctionIdentity)).append("\"");
		buffer.append(",\"artId\":\"").append(dealNull(artId)).append("\"");
		buffer.append(",\"author\":\"").append(dealNull(author)).append("\"");
		buffer.append(",\"createMonth\":\"").append(dealNull(createMonth)).append("\"");
		buffer.append(",\"pTradePrice\":\"").append(dealNull(pTradePrice)).append("\"");
		buffer.append(",\"imageUrl\":\"").append(dealNull(imageUrl)).append("\"");
		buffer.append(",\"worksCategory\":\"").append(dealNull(worksCategory)).append("\"");
		buffer.append(",\"worksId\":\"").append(dealNull(worksId)).append("\"");
		buffer.append(",\"sizeIn\":\"").append(dealNull(sizeIn)).append("\"");
		buffer.append(",\"hkLowestPrice\":\"").append(dealNull(hkLowestPrice)).append("\"");
		buffer.append(",\"cnName\":\"").append(dealNull(cnName)).append("\"");
		buffer.append(",\"pTranPrice\":\"").append(dealNull(pTranPrice)).append("\"");
		buffer.append(",\"dLowestPrice\":\"").append(dealNull(dLowestPrice)).append("\"");
		buffer.append(",\"dTradePrice\":\"").append(dealNull(dTradePrice)).append("\"");
		buffer.append(",\"worksSource\":\"").append(dealNull(worksSource)).append("\"");
		buffer.append(",\"auctionTime\":\"").append(dealNull(auctionTime)).append("\"");
		buffer.append(",\"cHighestPrice\":\"").append(dealNull(cHighestPrice)).append("\"");
		buffer.append(",\"dHighestPrice\":\"").append(dealNull(dHighestPrice)).append("\"");
		buffer.append(",\"autionSeason\":\"").append(dealNull(autionSeason)).append("\"");
		buffer.append(",\"cLowestPrice\":\"").append(dealNull(cLowestPrice)).append("\"");
		buffer.append(",\"pLowestPrice\":\"").append(dealNull(pLowestPrice)).append("\"");
		buffer.append(",\"createDay\":\"").append(dealNull(createDay)).append("\"");
		buffer.append(",\"auctionDesc\":\"").append(dealNull(auctionDesc)).append("\"");
		buffer.append(",\"sizeCm\":\"").append(dealNull(sizeCm)).append("\"");
		buffer.append(",\"eLowestPrice\":\"").append(dealNull(eLowestPrice)).append("\"");
		buffer.append(",\"hkTranPrice\":\"").append(dealNull(hkTranPrice)).append("\"");
		buffer.append(",\"dTranPrice\":\"").append(dealNull(dTranPrice)).append("\"");
		buffer.append(",\"eHighestPrice\":\"").append(dealNull(eHighestPrice)).append("\"");
		buffer.append(",\"sizeRule\":\"").append(dealNull(sizeRule)).append("\"");
		buffer.append(",\"hkTradePrice\":\"").append(dealNull(hkTradePrice)).append("\"");
		buffer.append(",\"pHighestPrice\":\"").append(dealNull(pHighestPrice)).append("\"");
		buffer.append(",\"eTranPrice\":\"").append(dealNull(eTranPrice)).append("\"");
		buffer.append(",\"auctionHousesId\":\"").append(dealNull(auctionHousesId)).append("\"");
		buffer.append(",\"createYear\":\"").append(dealNull(createYear)).append("\"");
		buffer.append(",\"tranStatus\":\"").append(dealNull(tranStatus)).append("\"");
		buffer.append(",\"versionNum\":\"").append(dealNull(versionNum)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
