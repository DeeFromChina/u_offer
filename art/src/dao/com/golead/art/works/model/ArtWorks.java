package com.golead.art.works.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_works table.
 */
@Entity  
@Table(name="art_works")  
public class ArtWorks implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties

   @Column(name = "size_in_length")
   private java.lang.Double sizeInLength;   // 英寸长

   @Column(name = "size_cm_height")
   private java.lang.Double sizeCmHeight;   // cm高

   @Column(name = "keyword_event")
   private java.lang.String keywordEvent;   // 内容关键字事件

   @Column(name = "create_country")
   private java.lang.Integer createCountry;   // 创作地点

   @Column(name = "size_cm_width")
   private java.lang.Double sizeCmWidth;   // cm宽

   @Column(name = "signature_content")
   private java.lang.String signatureContent;   // 签名内容

   @Column(name = "repeat_marker")
   private java.lang.String repeatMarker;   // 是否重复

   @Column(name = "social_function")
   private java.lang.String socialFunction;   // 社会功能

   @Column(name = "size_cm_length")
   private java.lang.Double sizeCmLength;   // cm长

   @Column(name = "style_content")
   private java.lang.String styleContent;   // 风格技法内容描述

   @Column(name = "signature")
   private java.lang.String signature;   // 签名

   @Column(name = "medium_shape")
   private java.lang.String mediumShape;   // 媒介(形式)

   @Column(name = "size_rule_height")
   private java.lang.Double sizeRuleHeight;   // 尺高

   @Column(name = "works_status")
   private java.lang.String worksStatus;   // 作品地位

   @Column(name = "signature_content3")
   private java.lang.String signatureContent3;   // 签名内容3

   @Column(name = "works_no")
   private java.lang.String worksNo;   // 作品编号

   @Column(name = "works_store_name")
   private java.lang.String worksStoreName;   // 作品图片存储名称，多个以、分开

   @Column(name = "signature_content2")
   private java.lang.String signatureContent2;   // 签名内容2

   @Column(name = "signature2")
   private java.lang.String signature2;   // 签名2

   @Column(name = "keyword_other2")
   private java.lang.String keywordOther2;   // 内容关键字其他

   @Column(name = "signature3")
   private java.lang.String signature3;   // 签名3

   @Column(name = "keyword_other1")
   private java.lang.String keywordOther1;   // 内容关键字其他

   @Column(name = "create_day")
   private java.lang.String createDay;   // 创作日

   @Column(name = "size_cm")
   private java.lang.String sizeCm;   // 尺寸(厘米)

   @Column(name = "back_story")
   private java.lang.String backStory;   // 背景故事

   @Column(name = "works_e_name")
   private java.lang.String worksEName;   // 作品英文名

   @Column(name = "create_to")
   private java.lang.String createTo;   // 创作时间段，结束年份

   @Column(name = "size_rule_width")
   private java.lang.Double sizeRuleWidth;   // 尺宽

   @Column(name = "thumbnail")
   private java.lang.String thumbnail;   // 缩略图

   @Column(name = "keyword_thing")
   private java.lang.String keywordThing;   // 内容关键字物件

   @Column(name = "size_rule")
   private java.lang.String sizeRule;   // 尺寸(尺)

   @Column(name = "part_size")
   private java.lang.String partSize;   // 局部尺寸

   @Column(name = "works_theme1")
   private java.lang.String worksTheme1;   // 作品题材

   @Column(name = "medium_material")
   private java.lang.String mediumMaterial;   // 媒介(材料)

   @Column(name = "works_theme3")
   private java.lang.String worksTheme3;   //  

   @Column(name = "works_theme2")
   private java.lang.String worksTheme2;   //  

   @Column(name = "create_year")
   private java.lang.String createYear;   // 创作年

   @Column(name = "size_rule_length")
   private java.lang.Double sizeRuleLength;   // 尺长

   @Column(name = "style_type")
   private java.lang.String styleType;   // 风格类型

   @Column(name = "create_period")
   private java.lang.String createPeriod;   // 创作时期


   @Column(name = "keyword_addr")
   private java.lang.String keywordAddr;   // 内容关键字地点

   @Column(name = "create_month")
   private java.lang.String createMonth;   // 创作月份

   @Column(name = "keyword_character")
   private java.lang.String keywordCharacter;   // 内容关键字人物

   @Column(name = "size_in")
   private java.lang.String sizeIn;   // 尺寸(英寸)

   @Column(name = "create_place")
   private java.lang.String createPlace;   // 创作地点

   @Column(name = "size_in_width")
   private java.lang.Double sizeInWidth;   // 英寸宽

   @Column(name = "works_series")
   private java.lang.String worksSeries;   // 作品系列

   @Column(name = "works_image")
   private java.lang.String worksImage;   // 作品图片，多个以、分开

   @Column(name = "create_from")
   private java.lang.String createFrom;   // 创作时间段，开始年份

   @Column(name = "size_in_height")
   private java.lang.Double sizeInHeight;   // 英寸高

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家id

   @Column(name = "create_cause")
   private java.lang.String createCause;   // 创作事由

   @Column(name = "works_c_name")
   private java.lang.String worksCName;   // 作品中文名
   
   @Column(name = "photo_soure")
   private java.lang.String photoSoure;   // 图片来源
   
   @Column(name = "works_writing")
   private java.lang.String worksWriting;   // 习作
   
   @Column(name = "auction_overview")
   private java.lang.String auctionOverview;   // 拍卖概述
   
   @Column(name = "fake_paintings")
   private java.lang.String fakePaintings;   // 拍卖概述
   
   @Column(name = "material_remark")
   private java.lang.String materialRemark;   // 拍卖概述
   
   @Column(name = "shape_remark")
   private java.lang.String shapeRemark;   // 拍卖概述
   
   @Column(name = "create_style")
   private java.lang.String createStyle;   // 创作方式
   
   @Column(name = "works_iden")
   private java.lang.String worksIden;   // 处理标识
   
   @Column(name = "integrity")
   private java.lang.Double integrity;   // 处理标识

   // Constructors
   public ArtWorks() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.Double getSizeInLength() { 
      return this.sizeInLength; 
   }
   public void setSizeInLength(java.lang.Double sizeInLength) { 
      this.sizeInLength = sizeInLength; 
   }


   public java.lang.Double getSizeCmHeight() { 
      return this.sizeCmHeight; 
   }
   public void setSizeCmHeight(java.lang.Double sizeCmHeight) { 
      this.sizeCmHeight = sizeCmHeight; 
   }


   public java.lang.String getKeywordEvent() { 
      return this.keywordEvent; 
   }
   public void setKeywordEvent(java.lang.String keywordEvent) { 
      this.keywordEvent = keywordEvent; 
   }


   public java.lang.Integer getCreateCountry() { 
      return this.createCountry; 
   }
   public void setCreateCountry(java.lang.Integer createCountry) { 
      this.createCountry = createCountry; 
   }


   public java.lang.Double getSizeCmWidth() { 
      return this.sizeCmWidth; 
   }
   public void setSizeCmWidth(java.lang.Double sizeCmWidth) { 
      this.sizeCmWidth = sizeCmWidth; 
   }


   public java.lang.String getSignatureContent() { 
      return this.signatureContent; 
   }
   public void setSignatureContent(java.lang.String signatureContent) { 
      this.signatureContent = signatureContent; 
   }


   public java.lang.String getRepeatMarker() { 
      return this.repeatMarker; 
   }
   public void setRepeatMarker(java.lang.String repeatMarker) { 
      this.repeatMarker = repeatMarker; 
   }


   public java.lang.String getSocialFunction() { 
      return this.socialFunction; 
   }
   public void setSocialFunction(java.lang.String socialFunction) { 
      this.socialFunction = socialFunction; 
   }


   public java.lang.Double getSizeCmLength() { 
      return this.sizeCmLength; 
   }
   public void setSizeCmLength(java.lang.Double sizeCmLength) { 
      this.sizeCmLength = sizeCmLength; 
   }


   public java.lang.String getStyleContent() { 
      return this.styleContent; 
   }
   public void setStyleContent(java.lang.String styleContent) { 
      this.styleContent = styleContent; 
   }


   public java.lang.String getSignature() { 
      return this.signature; 
   }
   public void setSignature(java.lang.String signature) { 
      this.signature = signature; 
   }


   public java.lang.String getMediumShape() { 
      return this.mediumShape; 
   }
   public void setMediumShape(java.lang.String mediumShape) { 
      this.mediumShape = mediumShape; 
   }


   public java.lang.Double getSizeRuleHeight() { 
      return this.sizeRuleHeight; 
   }
   public void setSizeRuleHeight(java.lang.Double sizeRuleHeight) { 
      this.sizeRuleHeight = sizeRuleHeight; 
   }


   public java.lang.String getWorksStatus() { 
      return this.worksStatus; 
   }
   public void setWorksStatus(java.lang.String worksStatus) { 
      this.worksStatus = worksStatus; 
   }


   public java.lang.String getSignatureContent3() { 
      return this.signatureContent3; 
   }
   public void setSignatureContent3(java.lang.String signatureContent3) { 
      this.signatureContent3 = signatureContent3; 
   }


   public java.lang.String getWorksNo() { 
      return this.worksNo; 
   }
   public void setWorksNo(java.lang.String worksNo) { 
      this.worksNo = worksNo; 
   }


   public java.lang.String getWorksStoreName() { 
      return this.worksStoreName; 
   }
   public void setWorksStoreName(java.lang.String worksStoreName) { 
      this.worksStoreName = worksStoreName; 
   }


   public java.lang.String getSignatureContent2() { 
      return this.signatureContent2; 
   }
   public void setSignatureContent2(java.lang.String signatureContent2) { 
      this.signatureContent2 = signatureContent2; 
   }


   public java.lang.String getSignature2() { 
      return this.signature2; 
   }
   public void setSignature2(java.lang.String signature2) { 
      this.signature2 = signature2; 
   }


   public java.lang.String getKeywordOther2() { 
      return this.keywordOther2; 
   }
   public void setKeywordOther2(java.lang.String keywordOther2) { 
      this.keywordOther2 = keywordOther2; 
   }


   public java.lang.String getSignature3() { 
      return this.signature3; 
   }
   public void setSignature3(java.lang.String signature3) { 
      this.signature3 = signature3; 
   }


   public java.lang.String getKeywordOther1() { 
      return this.keywordOther1; 
   }
   public void setKeywordOther1(java.lang.String keywordOther1) { 
      this.keywordOther1 = keywordOther1; 
   }


   public java.lang.String getCreateDay() { 
      return this.createDay; 
   }
   public void setCreateDay(java.lang.String createDay) { 
      this.createDay = createDay; 
   }


   public java.lang.String getSizeCm() { 
      return this.sizeCm; 
   }
   public void setSizeCm(java.lang.String sizeCm) { 
      this.sizeCm = sizeCm; 
   }


   public java.lang.String getBackStory() { 
      return this.backStory; 
   }
   public void setBackStory(java.lang.String backStory) { 
      this.backStory = backStory; 
   }


   public java.lang.String getWorksEName() { 
      return this.worksEName; 
   }
   public void setWorksEName(java.lang.String worksEName) { 
      this.worksEName = worksEName; 
   }


   public java.lang.String getCreateTo() { 
      return this.createTo; 
   }
   public void setCreateTo(java.lang.String createTo) { 
      this.createTo = createTo; 
   }


   public java.lang.Double getSizeRuleWidth() { 
      return this.sizeRuleWidth; 
   }
   public void setSizeRuleWidth(java.lang.Double sizeRuleWidth) { 
      this.sizeRuleWidth = sizeRuleWidth; 
   }


   public java.lang.String getThumbnail() { 
      return this.thumbnail; 
   }
   public void setThumbnail(java.lang.String thumbnail) { 
      this.thumbnail = thumbnail; 
   }


   public java.lang.String getKeywordThing() { 
      return this.keywordThing; 
   }
   public void setKeywordThing(java.lang.String keywordThing) { 
      this.keywordThing = keywordThing; 
   }


   public java.lang.String getSizeRule() { 
      return this.sizeRule; 
   }
   public void setSizeRule(java.lang.String sizeRule) { 
      this.sizeRule = sizeRule; 
   }


   public java.lang.String getPartSize() { 
      return this.partSize; 
   }
   public void setPartSize(java.lang.String partSize) { 
      this.partSize = partSize; 
   }


   public java.lang.String getWorksTheme1() { 
      return this.worksTheme1; 
   }
   public void setWorksTheme1(java.lang.String worksTheme1) { 
      this.worksTheme1 = worksTheme1; 
   }


   public java.lang.String getMediumMaterial() { 
      return this.mediumMaterial; 
   }
   public void setMediumMaterial(java.lang.String mediumMaterial) { 
      this.mediumMaterial = mediumMaterial; 
   }


   public java.lang.String getWorksTheme3() { 
      return this.worksTheme3; 
   }
   public void setWorksTheme3(java.lang.String worksTheme3) { 
      this.worksTheme3 = worksTheme3; 
   }


   public java.lang.String getWorksTheme2() { 
      return this.worksTheme2; 
   }
   public void setWorksTheme2(java.lang.String worksTheme2) { 
      this.worksTheme2 = worksTheme2; 
   }


   public java.lang.String getCreateYear() { 
      return this.createYear; 
   }
   public void setCreateYear(java.lang.String createYear) { 
      this.createYear = createYear; 
   }


   public java.lang.Double getSizeRuleLength() { 
      return this.sizeRuleLength; 
   }
   public void setSizeRuleLength(java.lang.Double sizeRuleLength) { 
      this.sizeRuleLength = sizeRuleLength; 
   }


   public java.lang.String getStyleType() { 
      return this.styleType; 
   }
   public void setStyleType(java.lang.String styleType) { 
      this.styleType = styleType; 
   }


   public java.lang.String getCreatePeriod() { 
      return this.createPeriod; 
   }
   public void setCreatePeriod(java.lang.String createPeriod) { 
      this.createPeriod = createPeriod; 
   }


   public java.lang.String getKeywordAddr() { 
      return this.keywordAddr; 
   }
   public void setKeywordAddr(java.lang.String keywordAddr) { 
      this.keywordAddr = keywordAddr; 
   }


   public java.lang.String getCreateMonth() { 
      return this.createMonth; 
   }
   public void setCreateMonth(java.lang.String createMonth) { 
      this.createMonth = createMonth; 
   }


   public java.lang.String getKeywordCharacter() { 
      return this.keywordCharacter; 
   }
   public void setKeywordCharacter(java.lang.String keywordCharacter) { 
      this.keywordCharacter = keywordCharacter; 
   }


   public java.lang.String getSizeIn() { 
      return this.sizeIn; 
   }
   public void setSizeIn(java.lang.String sizeIn) { 
      this.sizeIn = sizeIn; 
   }


   public java.lang.String getCreatePlace() { 
      return this.createPlace; 
   }
   public void setCreatePlace(java.lang.String createPlace) { 
      this.createPlace = createPlace; 
   }


   public java.lang.Double getSizeInWidth() { 
      return this.sizeInWidth; 
   }
   public void setSizeInWidth(java.lang.Double sizeInWidth) { 
      this.sizeInWidth = sizeInWidth; 
   }


   public java.lang.String getWorksSeries() { 
      return this.worksSeries; 
   }
   public void setWorksSeries(java.lang.String worksSeries) { 
      this.worksSeries = worksSeries; 
   }


   public java.lang.String getWorksImage() { 
      return this.worksImage; 
   }
   public void setWorksImage(java.lang.String worksImage) { 
      this.worksImage = worksImage; 
   }


   public java.lang.String getCreateFrom() { 
      return this.createFrom; 
   }
   public void setCreateFrom(java.lang.String createFrom) { 
      this.createFrom = createFrom; 
   }


   public java.lang.Double getSizeInHeight() { 
      return this.sizeInHeight; 
   }
   public void setSizeInHeight(java.lang.Double sizeInHeight) { 
      this.sizeInHeight = sizeInHeight; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public java.lang.String getCreateCause() { 
      return this.createCause; 
   }
   public void setCreateCause(java.lang.String createCause) { 
      this.createCause = createCause; 
   }


   public java.lang.String getWorksCName() { 
      return this.worksCName; 
   }
   public void setWorksCName(java.lang.String worksCName) { 
      this.worksCName = worksCName; 
   }


   public java.lang.String getPhotoSoure() {
      return photoSoure;
   }

   public void setPhotoSoure(java.lang.String photoSoure) {
      this.photoSoure = photoSoure;
   }

   public java.lang.String getWorksWriting() {
      return worksWriting;
   }

   public void setWorksWriting(java.lang.String worksWriting) {
      this.worksWriting = worksWriting;
   }


   public java.lang.String getAuctionOverview() {
      return auctionOverview;
   }

   public void setAuctionOverview(java.lang.String auctionOverview) {
      this.auctionOverview = auctionOverview;
   }


   public java.lang.String getFakePaintings() {
      return fakePaintings;
   }

   public void setFakePaintings(java.lang.String fakePaintings) {
      this.fakePaintings = fakePaintings;
   }


   public java.lang.String getMaterialRemark() {
      return materialRemark;
   }

   public void setMaterialRemark(java.lang.String materialRemark) {
      this.materialRemark = materialRemark;
   }

   public java.lang.String getShapeRemark() {
      return shapeRemark;
   }

   public void setShapeRemark(java.lang.String shapeRemark) {
      this.shapeRemark = shapeRemark;
   }


   public java.lang.String getCreateStyle() {
      return createStyle;
   }

   public void setCreateStyle(java.lang.String createStyle) {
      this.createStyle = createStyle;
   }


   public java.lang.String getWorksIden() {
      return worksIden;
   }

   public void setWorksIden(java.lang.String worksIden) {
      this.worksIden = worksIden;
   }


   public java.lang.Double getIntegrity() {
      return integrity;
   }

   public void setIntegrity(java.lang.Double integrity) {
      this.integrity = integrity;
   }


   public static String REF_CLASS = "ArtWorks";
   public static String PROP_SIZE_IN_LENGTH = "sizeInLength";
   public static String PROP_SIZE_CM_HEIGHT = "sizeCmHeight";
   public static String PROP_KEYWORD_EVENT = "keywordEvent";
   public static String PROP_CREATE_COUNTRY = "createCountry";
   public static String PROP_SIZE_CM_WIDTH = "sizeCmWidth";
   public static String PROP_SIGNATURE_CONTENT = "signatureContent";
   public static String PROP_REPEAT_MARKER = "repeatMarker";
   public static String PROP_SOCIAL_FUNCTION = "socialFunction";
   public static String PROP_SIZE_CM_LENGTH = "sizeCmLength";
   public static String PROP_STYLE_CONTENT = "styleContent";
   public static String PROP_SIGNATURE = "signature";
   public static String PROP_MEDIUM_SHAPE = "mediumShape";
   public static String PROP_SIZE_RULE_HEIGHT = "sizeRuleHeight";
   public static String PROP_WORKS_STATUS = "worksStatus";
   public static String PROP_SIGNATURE_CONTENT3 = "signatureContent3";
   public static String PROP_WORKS_NO = "worksNo";
   public static String PROP_WORKS_STORE_NAME = "worksStoreName";
   public static String PROP_SIGNATURE_CONTENT2 = "signatureContent2";
   public static String PROP_SIGNATURE2 = "signature2";
   public static String PROP_KEYWORD_OTHER2 = "keywordOther2";
   public static String PROP_SIGNATURE3 = "signature3";
   public static String PROP_KEYWORD_OTHER1 = "keywordOther1";
   public static String PROP_CREATE_DAY = "createDay";
   public static String PROP_SIZE_CM = "sizeCm";
   public static String PROP_BACK_STORY = "backStory";
   public static String PROP_WORKS_E_NAME = "worksEName";
   public static String PROP_CREATE_TO = "createTo";
   public static String PROP_SIZE_RULE_WIDTH = "sizeRuleWidth";
   public static String PROP_THUMBNAIL = "thumbnail";
   public static String PROP_KEYWORD_THING = "keywordThing";
   public static String PROP_SIZE_RULE = "sizeRule";
   public static String PROP_PART_SIZE = "partSize";
   public static String PROP_WORKS_THEME1 = "worksTheme1";
   public static String PROP_MEDIUM_MATERIAL = "mediumMaterial";
   public static String PROP_WORKS_THEME3 = "worksTheme3";
   public static String PROP_WORKS_THEME2 = "worksTheme2";
   public static String PROP_CREATE_YEAR = "createYear";
   public static String PROP_SIZE_RULE_LENGTH = "sizeRuleLength";
   public static String PROP_STYLE_TYPE = "styleType";
   public static String PROP_CREATE_PERIOD = "createPeriod";
   public static String PROP_ID = "id";
   public static String PROP_KEYWORD_ADDR = "keywordAddr";
   public static String PROP_CREATE_MONTH = "createMonth";
   public static String PROP_KEYWORD_CHARACTER = "keywordCharacter";
   public static String PROP_SIZE_IN = "sizeIn";
   public static String PROP_CREATE_PLACE = "createPlace";
   public static String PROP_SIZE_IN_WIDTH = "sizeInWidth";
   public static String PROP_WORKS_SERIES = "worksSeries";
   public static String PROP_WORKS_IMAGE = "worksImage";
   public static String PROP_CREATE_FROM = "createFrom";
   public static String PROP_SIZE_IN_HEIGHT = "sizeInHeight";
   public static String PROP_ARTIST_ID = "artistId";
   public static String PROP_CREATE_CAUSE = "createCause";
   public static String PROP_WORKS_C_NAME = "worksCName";
   public static String PROP_PHOTO_SOURE = "photoSoure";
   public static String PROP_WORKS_WRITING = "worksWriting";
   public static String PROP_AUCTION_OVERVIEW = "auctionOverview";
   public static String PROP_FAKE_PAINTINGS = "fakePaintings";
   public static String PROP_MATERIAL_REMARK = "materialRemark";
   public static String PROP_SHAPE_REMARK = "shapeRemark";
   public static String PROP_CREATE_STYLE = "createStyle";
   public static String PROP_WORKS_IDEN = "worksIden";
   public static String PROP_INTEGRITY = "integrity";

   public static String REF_TABLE = "art_works";
   public static String COL_SIZE_IN_LENGTH = "size_in_length";
   public static String COL_SIZE_CM_HEIGHT = "size_cm_height";
   public static String COL_KEYWORD_EVENT = "keyword_event";
   public static String COL_CREATE_COUNTRY = "create_country";
   public static String COL_SIZE_CM_WIDTH = "size_cm_width";
   public static String COL_SIGNATURE_CONTENT = "signature_content";
   public static String COL_REPEAT_MARKER = "repeat_marker";
   public static String COL_SOCIAL_FUNCTION = "social_function";
   public static String COL_SIZE_CM_LENGTH = "size_cm_length";
   public static String COL_STYLE_CONTENT = "style_content";
   public static String COL_SIGNATURE = "signature";
   public static String COL_MEDIUM_SHAPE = "medium_shape";
   public static String COL_SIZE_RULE_HEIGHT = "size_rule_height";
   public static String COL_WORKS_STATUS = "works_status";
   public static String COL_SIGNATURE_CONTENT3 = "signature_content3";
   public static String COL_WORKS_NO = "works_no";
   public static String COL_WORKS_STORE_NAME = "works_store_name";
   public static String COL_SIGNATURE_CONTENT2 = "signature_content2";
   public static String COL_SIGNATURE2 = "signature2";
   public static String COL_KEYWORD_OTHER2 = "keyword_other2";
   public static String COL_SIGNATURE3 = "signature3";
   public static String COL_KEYWORD_OTHER1 = "keyword_other1";
   public static String COL_CREATE_DAY = "create_day";
   public static String COL_SIZE_CM = "size_cm";
   public static String COL_BACK_STORY = "back_story";
   public static String COL_WORKS_E_NAME = "works_e_name";
   public static String COL_CREATE_TO = "create_to";
   public static String COL_SIZE_RULE_WIDTH = "size_rule_width";
   public static String COL_THUMBNAIL = "thumbnail";
   public static String COL_KEYWORD_THING = "keyword_thing";
   public static String COL_SIZE_RULE = "size_rule";
   public static String COL_PART_SIZE = "part_size";
   public static String COL_WORKS_THEME1 = "works_theme1";
   public static String COL_MEDIUM_MATERIAL = "medium_material";
   public static String COL_WORKS_THEME3 = "works_theme3";
   public static String COL_WORKS_THEME2 = "works_theme2";
   public static String COL_CREATE_YEAR = "create_year";
   public static String COL_SIZE_RULE_LENGTH = "size_rule_length";
   public static String COL_STYLE_TYPE = "style_type";
   public static String COL_CREATE_PERIOD = "create_period";
   public static String COL_ID = "id";
   public static String COL_KEYWORD_ADDR = "keyword_addr";
   public static String COL_CREATE_MONTH = "create_month";
   public static String COL_KEYWORD_CHARACTER = "keyword_character";
   public static String COL_SIZE_IN = "size_in";
   public static String COL_CREATE_PLACE = "create_place";
   public static String COL_SIZE_IN_WIDTH = "size_in_width";
   public static String COL_WORKS_SERIES = "works_series";
   public static String COL_WORKS_IMAGE = "works_image";
   public static String COL_CREATE_FROM = "create_from";
   public static String COL_SIZE_IN_HEIGHT = "size_in_height";
   public static String COL_ARTIST_ID = "artist_id";
   public static String COL_CREATE_CAUSE = "create_cause";
   public static String COL_WORKS_C_NAME = "works_c_name";
   public static String COL_PHOTO_SOURE = "photo_soure";
   public static String COL_WORKS_WRITING = "works_writing";
   public static String COL_AUCTION_OVERVIEW = "auction_overview";
   public static String COL_FAKE_PAINTINGS = "fake_paintings";
   public static String COL_MATERIAL_REMARK = "material_remark";
   public static String COL_SHAPE_REMARK = "shape_remark";
   public static String COL_CREATE_STYLE = "create_style";
   public static String COL_WORKS_IDEN = "works_iden";
   public static String COL_INTEGRITY = "integrity";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.works.model.ArtWorks)) return false;
      else {
         com.golead.art.works.model.ArtWorks o = (com.golead.art.works.model.ArtWorks) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtWorks:");
		buffer.append(" id:").append(id);
		buffer.append(" sizeInLength:").append(dealNull(sizeInLength));
		buffer.append(" sizeCmHeight:").append(dealNull(sizeCmHeight));
		buffer.append(" keywordEvent:").append(dealNull(keywordEvent));
		buffer.append(" createCountry:").append(dealNull(createCountry));
		buffer.append(" sizeCmWidth:").append(dealNull(sizeCmWidth));
		buffer.append(" signatureContent:").append(dealNull(signatureContent));
		buffer.append(" repeatMarker:").append(dealNull(repeatMarker));
		buffer.append(" socialFunction:").append(dealNull(socialFunction));
		buffer.append(" sizeCmLength:").append(dealNull(sizeCmLength));
		buffer.append(" styleContent:").append(dealNull(styleContent));
		buffer.append(" signature:").append(dealNull(signature));
		buffer.append(" mediumShape:").append(dealNull(mediumShape));
		buffer.append(" sizeRuleHeight:").append(dealNull(sizeRuleHeight));
		buffer.append(" worksStatus:").append(dealNull(worksStatus));
		buffer.append(" signatureContent3:").append(dealNull(signatureContent3));
		buffer.append(" worksNo:").append(dealNull(worksNo));
		buffer.append(" worksStoreName:").append(dealNull(worksStoreName));
		buffer.append(" signatureContent2:").append(dealNull(signatureContent2));
		buffer.append(" signature2:").append(dealNull(signature2));
		buffer.append(" keywordOther2:").append(dealNull(keywordOther2));
		buffer.append(" signature3:").append(dealNull(signature3));
		buffer.append(" keywordOther1:").append(dealNull(keywordOther1));
		buffer.append(" createDay:").append(dealNull(createDay));
		buffer.append(" sizeCm:").append(dealNull(sizeCm));
		buffer.append(" backStory:").append(dealNull(backStory));
		buffer.append(" worksEName:").append(dealNull(worksEName));
		buffer.append(" createTo:").append(dealNull(createTo));
		buffer.append(" sizeRuleWidth:").append(dealNull(sizeRuleWidth));
		buffer.append(" thumbnail:").append(dealNull(thumbnail));
		buffer.append(" keywordThing:").append(dealNull(keywordThing));
		buffer.append(" sizeRule:").append(dealNull(sizeRule));
		buffer.append(" partSize:").append(dealNull(partSize));
		buffer.append(" worksTheme1:").append(dealNull(worksTheme1));
		buffer.append(" mediumMaterial:").append(dealNull(mediumMaterial));
		buffer.append(" worksTheme3:").append(dealNull(worksTheme3));
		buffer.append(" worksTheme2:").append(dealNull(worksTheme2));
		buffer.append(" createYear:").append(dealNull(createYear));
		buffer.append(" sizeRuleLength:").append(dealNull(sizeRuleLength));
		buffer.append(" styleType:").append(dealNull(styleType));
		buffer.append(" createPeriod:").append(dealNull(createPeriod));
		buffer.append(" keywordAddr:").append(dealNull(keywordAddr));
		buffer.append(" createMonth:").append(dealNull(createMonth));
		buffer.append(" keywordCharacter:").append(dealNull(keywordCharacter));
		buffer.append(" sizeIn:").append(dealNull(sizeIn));
		buffer.append(" createPlace:").append(dealNull(createPlace));
		buffer.append(" sizeInWidth:").append(dealNull(sizeInWidth));
		buffer.append(" worksSeries:").append(dealNull(worksSeries));
		buffer.append(" worksImage:").append(dealNull(worksImage));
		buffer.append(" createFrom:").append(dealNull(createFrom));
		buffer.append(" sizeInHeight:").append(dealNull(sizeInHeight));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append(" createCause:").append(dealNull(createCause));
		buffer.append(" worksCName:").append(dealNull(worksCName));
		buffer.append(" photoSoure:").append(dealNull(photoSoure));
		buffer.append(" worksWriting:").append(dealNull(worksWriting));
		buffer.append(" auctionOverview:").append(dealNull(auctionOverview));
		buffer.append(" fakePaintings:").append(dealNull(fakePaintings));
		buffer.append(" materialRemark:").append(dealNull(materialRemark));
		buffer.append(" shapeRemark:").append(dealNull(shapeRemark));
		buffer.append(" createStyle:").append(dealNull(createStyle));
		buffer.append(" worksIden:").append(dealNull(worksIden));
		buffer.append(" integrity:").append(dealNull(integrity));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"sizeInLength\":\"").append(dealNull(sizeInLength)).append("\"");
		buffer.append(",\"sizeCmHeight\":\"").append(dealNull(sizeCmHeight)).append("\"");
		buffer.append(",\"keywordEvent\":\"").append(dealNull(keywordEvent)).append("\"");
		buffer.append(",\"createCountry\":\"").append(dealNull(createCountry)).append("\"");
		buffer.append(",\"sizeCmWidth\":\"").append(dealNull(sizeCmWidth)).append("\"");
		buffer.append(",\"signatureContent\":\"").append(dealNull(signatureContent)).append("\"");
		buffer.append(",\"repeatMarker\":\"").append(dealNull(repeatMarker)).append("\"");
		buffer.append(",\"socialFunction\":\"").append(dealNull(socialFunction)).append("\"");
		buffer.append(",\"sizeCmLength\":\"").append(dealNull(sizeCmLength)).append("\"");
		buffer.append(",\"styleContent\":\"").append(dealNull(styleContent)).append("\"");
		buffer.append(",\"signature\":\"").append(dealNull(signature)).append("\"");
		buffer.append(",\"mediumShape\":\"").append(dealNull(mediumShape)).append("\"");
		buffer.append(",\"sizeRuleHeight\":\"").append(dealNull(sizeRuleHeight)).append("\"");
		buffer.append(",\"worksStatus\":\"").append(dealNull(worksStatus)).append("\"");
		buffer.append(",\"signatureContent3\":\"").append(dealNull(signatureContent3)).append("\"");
		buffer.append(",\"worksNo\":\"").append(dealNull(worksNo)).append("\"");
		buffer.append(",\"worksStoreName\":\"").append(dealNull(worksStoreName)).append("\"");
		buffer.append(",\"signatureContent2\":\"").append(dealNull(signatureContent2)).append("\"");
		buffer.append(",\"signature2\":\"").append(dealNull(signature2)).append("\"");
		buffer.append(",\"keywordOther2\":\"").append(dealNull(keywordOther2)).append("\"");
		buffer.append(",\"signature3\":\"").append(dealNull(signature3)).append("\"");
		buffer.append(",\"keywordOther1\":\"").append(dealNull(keywordOther1)).append("\"");
		buffer.append(",\"createDay\":\"").append(dealNull(createDay)).append("\"");
		buffer.append(",\"sizeCm\":\"").append(dealNull(sizeCm)).append("\"");
		buffer.append(",\"backStory\":\"").append(dealNull(backStory)).append("\"");
		buffer.append(",\"worksEName\":\"").append(dealNull(worksEName)).append("\"");
		buffer.append(",\"createTo\":\"").append(dealNull(createTo)).append("\"");
		buffer.append(",\"sizeRuleWidth\":\"").append(dealNull(sizeRuleWidth)).append("\"");
		buffer.append(",\"thumbnail\":\"").append(dealNull(thumbnail)).append("\"");
		buffer.append(",\"keywordThing\":\"").append(dealNull(keywordThing)).append("\"");
		buffer.append(",\"sizeRule\":\"").append(dealNull(sizeRule)).append("\"");
		buffer.append(",\"partSize\":\"").append(dealNull(partSize)).append("\"");
		buffer.append(",\"worksTheme1\":\"").append(dealNull(worksTheme1)).append("\"");
		buffer.append(",\"mediumMaterial\":\"").append(dealNull(mediumMaterial)).append("\"");
		buffer.append(",\"worksTheme3\":\"").append(dealNull(worksTheme3)).append("\"");
		buffer.append(",\"worksTheme2\":\"").append(dealNull(worksTheme2)).append("\"");
		buffer.append(",\"createYear\":\"").append(dealNull(createYear)).append("\"");
		buffer.append(",\"sizeRuleLength\":\"").append(dealNull(sizeRuleLength)).append("\"");
		buffer.append(",\"styleType\":\"").append(dealNull(styleType)).append("\"");
		buffer.append(",\"createPeriod\":\"").append(dealNull(createPeriod)).append("\"");
		buffer.append(",\"keywordAddr\":\"").append(dealNull(keywordAddr)).append("\"");
		buffer.append(",\"createMonth\":\"").append(dealNull(createMonth)).append("\"");
		buffer.append(",\"keywordCharacter\":\"").append(dealNull(keywordCharacter)).append("\"");
		buffer.append(",\"sizeIn\":\"").append(dealNull(sizeIn)).append("\"");
		buffer.append(",\"createPlace\":\"").append(dealNull(createPlace)).append("\"");
		buffer.append(",\"sizeInWidth\":\"").append(dealNull(sizeInWidth)).append("\"");
		buffer.append(",\"worksSeries\":\"").append(dealNull(worksSeries)).append("\"");
		buffer.append(",\"worksImage\":\"").append(dealNull(worksImage)).append("\"");
		buffer.append(",\"createFrom\":\"").append(dealNull(createFrom)).append("\"");
		buffer.append(",\"sizeInHeight\":\"").append(dealNull(sizeInHeight)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append(",\"createCause\":\"").append(dealNull(createCause)).append("\"");
		buffer.append(",\"worksCName\":\"").append(dealNull(worksCName)).append("\"");
		buffer.append(",\"photoSoure\":\"").append(dealNull(photoSoure)).append("\"");
		buffer.append(",\"worksWriting\":\"").append(dealNull(worksWriting)).append("\"");
		buffer.append(",\"auctionOverview\":\"").append(dealNull(auctionOverview)).append("\"");
		buffer.append(",\"fakePaintings\":\"").append(dealNull(fakePaintings)).append("\"");
		buffer.append(",\"materialRemark\":\"").append(dealNull(materialRemark)).append("\"");
		buffer.append(",\"shapeRemark\":\"").append(dealNull(shapeRemark)).append("\"");
		buffer.append(",\"createStyle\":\"").append(dealNull(createStyle)).append("\"");
		buffer.append(",\"worksIden\":\"").append(dealNull(worksIden)).append("\"");
		buffer.append(",\"integrity\":\"").append(dealNull(integrity)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
