package com.golead.art.publication.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_publication table.
 */
@Entity  
@Table(name="art_publication")  
public class ArtPublication implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties

   @Column(name = "catalog_name")
   private java.lang.String catalogName;   // 附件文件夹名称

   @Column(name = "press")
   private java.lang.String press;   // 出版社

   @Column(name = "publication_type")
   private java.lang.String publicationType;   // 出版物类型

   @Column(name = "impression")
   private java.lang.String impression;   // 印次

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家id

   @Column(name = "page_count")
   private java.lang.Integer pageCount;   // 总页数

   @Column(name = "publication_name")
   private java.lang.String publicationName;   // 出版物信息


   @Column(name = "editor")
   private java.lang.String editor;   // 主编

   @Column(name = "cover")
   private java.lang.String cover;   // 封面

   @Column(name = "publication_month")
   private java.lang.Integer publicationMonth;   // 出版月份

   @Column(name = "price")
   private java.lang.Double price;   // 定价(元)

   @Column(name = "circulation")
   private java.lang.String circulation;   // 发行量

   @Column(name = "isbn")
   private java.lang.String isbn;   // 书号

   @Column(name = "back_cover")
   private java.lang.String backCover;   // 封底

   @Column(name = "catalog_other")
   private java.lang.String catalogOther;   // 目录/其他

   @Column(name = "publication_year")
   private java.lang.Integer publicationYear;   // 出版年份

   @Column(name = "price_unit")
   private java.lang.String priceUnit;   // 价格单位

   // Constructors
   public ArtPublication() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getCatalogName() { 
      return this.catalogName; 
   }
   public void setCatalogName(java.lang.String catalogName) { 
      this.catalogName = catalogName; 
   }


   public java.lang.String getPress() { 
      return this.press; 
   }
   public void setPress(java.lang.String press) { 
      this.press = press; 
   }


   public java.lang.String getPublicationType() { 
      return this.publicationType; 
   }
   public void setPublicationType(java.lang.String publicationType) { 
      this.publicationType = publicationType; 
   }


   public java.lang.String getImpression() { 
      return this.impression; 
   }
   public void setImpression(java.lang.String impression) { 
      this.impression = impression; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public java.lang.Integer getPageCount() { 
      return this.pageCount; 
   }
   public void setPageCount(java.lang.Integer pageCount) { 
      this.pageCount = pageCount; 
   }


   public java.lang.String getPublicationName() { 
      return this.publicationName; 
   }
   public void setPublicationName(java.lang.String publicationName) { 
      this.publicationName = publicationName; 
   }


   public java.lang.String getEditor() { 
      return this.editor; 
   }
   public void setEditor(java.lang.String editor) { 
      this.editor = editor; 
   }


   public java.lang.String getCover() { 
      return this.cover; 
   }
   public void setCover(java.lang.String cover) { 
      this.cover = cover; 
   }


   public java.lang.Integer getPublicationMonth() { 
      return this.publicationMonth; 
   }
   public void setPublicationMonth(java.lang.Integer publicationMonth) { 
      this.publicationMonth = publicationMonth; 
   }


   public java.lang.Double getPrice() { 
      return this.price; 
   }
   public void setPrice(java.lang.Double price) { 
      this.price = price; 
   }


   public java.lang.String getCirculation() { 
      return this.circulation; 
   }
   public void setCirculation(java.lang.String circulation) { 
      this.circulation = circulation; 
   }


   public java.lang.String getIsbn() { 
      return this.isbn; 
   }
   public void setIsbn(java.lang.String isbn) { 
      this.isbn = isbn; 
   }


   public java.lang.String getBackCover() { 
      return this.backCover; 
   }
   public void setBackCover(java.lang.String backCover) { 
      this.backCover = backCover; 
   }


   public java.lang.String getCatalogOther() { 
      return this.catalogOther; 
   }
   public void setCatalogOther(java.lang.String catalogOther) { 
      this.catalogOther = catalogOther; 
   }


   public java.lang.Integer getPublicationYear() { 
      return this.publicationYear; 
   }
   public void setPublicationYear(java.lang.Integer publicationYear) { 
      this.publicationYear = publicationYear; 
   }


   public java.lang.String getPriceUnit() { 
      return this.priceUnit; 
   }
   public void setPriceUnit(java.lang.String priceUnit) { 
      this.priceUnit = priceUnit; 
   }


   public static String REF_CLASS = "ArtPublication";
   public static String PROP_CATALOG_NAME = "catalogName";
   public static String PROP_PRESS = "press";
   public static String PROP_PUBLICATION_TYPE = "publicationType";
   public static String PROP_IMPRESSION = "impression";
   public static String PROP_ARTIST_ID = "artistId";
   public static String PROP_PAGE_COUNT = "pageCount";
   public static String PROP_PUBLICATION_NAME = "publicationName";
   public static String PROP_ID = "id";
   public static String PROP_EDITOR = "editor";
   public static String PROP_COVER = "cover";
   public static String PROP_PUBLICATION_MONTH = "publicationMonth";
   public static String PROP_PRICE = "price";
   public static String PROP_CIRCULATION = "circulation";
   public static String PROP_ISBN = "isbn";
   public static String PROP_BACK_COVER = "backCover";
   public static String PROP_CATALOG_OTHER = "catalogOther";
   public static String PROP_PUBLICATION_YEAR = "publicationYear";
   public static String PROP_PRICE_UNIT = "priceUnit";

   public static String REF_TABLE = "art_publication";
   public static String COL_CATALOG_NAME = "catalog_name";
   public static String COL_PRESS = "press";
   public static String COL_PUBLICATION_TYPE = "publication_type";
   public static String COL_IMPRESSION = "impression";
   public static String COL_ARTIST_ID = "artist_id";
   public static String COL_PAGE_COUNT = "page_count";
   public static String COL_PUBLICATION_NAME = "publication_name";
   public static String COL_ID = "id";
   public static String COL_EDITOR = "editor";
   public static String COL_COVER = "cover";
   public static String COL_PUBLICATION_MONTH = "publication_month";
   public static String COL_PRICE = "price";
   public static String COL_CIRCULATION = "circulation";
   public static String COL_ISBN = "isbn";
   public static String COL_BACK_COVER = "back_cover";
   public static String COL_CATALOG_OTHER = "catalog_other";
   public static String COL_PUBLICATION_YEAR = "publication_year";
   public static String COL_PRICE_UNIT = "price_unit";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.publication.model.ArtPublication)) return false;
      else {
         com.golead.art.publication.model.ArtPublication o = (com.golead.art.publication.model.ArtPublication) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtPublication:");
		buffer.append(" id:").append(id);
		buffer.append(" catalogName:").append(dealNull(catalogName));
		buffer.append(" press:").append(dealNull(press));
		buffer.append(" publicationType:").append(dealNull(publicationType));
		buffer.append(" impression:").append(dealNull(impression));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append(" pageCount:").append(dealNull(pageCount));
		buffer.append(" publicationName:").append(dealNull(publicationName));
		buffer.append(" editor:").append(dealNull(editor));
		buffer.append(" cover:").append(dealNull(cover));
		buffer.append(" publicationMonth:").append(dealNull(publicationMonth));
		buffer.append(" price:").append(dealNull(price));
		buffer.append(" circulation:").append(dealNull(circulation));
		buffer.append(" isbn:").append(dealNull(isbn));
		buffer.append(" backCover:").append(dealNull(backCover));
		buffer.append(" catalogOther:").append(dealNull(catalogOther));
		buffer.append(" publicationYear:").append(dealNull(publicationYear));
		buffer.append(" priceUnit:").append(dealNull(priceUnit));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"catalogName\":\"").append(dealNull(catalogName)).append("\"");
		buffer.append(",\"press\":\"").append(dealNull(press)).append("\"");
		buffer.append(",\"publicationType\":\"").append(dealNull(publicationType)).append("\"");
		buffer.append(",\"impression\":\"").append(dealNull(impression)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append(",\"pageCount\":\"").append(dealNull(pageCount)).append("\"");
		buffer.append(",\"publicationName\":\"").append(dealNull(publicationName)).append("\"");
		buffer.append(",\"editor\":\"").append(dealNull(editor)).append("\"");
		buffer.append(",\"cover\":\"").append(dealNull(cover)).append("\"");
		buffer.append(",\"publicationMonth\":\"").append(dealNull(publicationMonth)).append("\"");
		buffer.append(",\"price\":\"").append(dealNull(price)).append("\"");
		buffer.append(",\"circulation\":\"").append(dealNull(circulation)).append("\"");
		buffer.append(",\"isbn\":\"").append(dealNull(isbn)).append("\"");
		buffer.append(",\"backCover\":\"").append(dealNull(backCover)).append("\"");
		buffer.append(",\"catalogOther\":\"").append(dealNull(catalogOther)).append("\"");
		buffer.append(",\"publicationYear\":\"").append(dealNull(publicationYear)).append("\"");
		buffer.append(",\"priceUnit\":\"").append(dealNull(priceUnit)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
