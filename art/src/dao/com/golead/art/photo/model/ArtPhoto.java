package com.golead.art.photo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_photo table.
 */
@Entity  
@Table(name="art_photo")  
public class ArtPhoto implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "pub_id")
   private java.lang.Integer pubId;   // 出版物

   @Column(name = "photo_desc")
   private java.lang.String photoDesc;   // 描述

   @Column(name = "photo_type")
   private java.lang.String photoType;   // 类型

   @Column(name = "photo_time")
   private java.lang.String photoTime;   //  

   @Column(name = "photo_name")
   private java.lang.String photoName;   // 名称

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家

   @Column(name = "photo_addr")
   private java.lang.String photoAddr;   // 地点

   @Column(name = "photo")
   private java.lang.String photo;   // 存储名称

   @Column(name = "photo_source")
   private java.lang.String photoSource;   // 来源

   // Constructors
   public ArtPhoto() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.Integer getPubId() { 
      return this.pubId; 
   }
   public void setPubId(java.lang.Integer pubId) { 
      this.pubId = pubId; 
   }


   public java.lang.String getPhotoDesc() { 
      return this.photoDesc; 
   }
   public void setPhotoDesc(java.lang.String photoDesc) { 
      this.photoDesc = photoDesc; 
   }


   public java.lang.String getPhotoType() { 
      return this.photoType; 
   }
   public void setPhotoType(java.lang.String photoType) { 
      this.photoType = photoType; 
   }


   public java.lang.String getPhotoTime() { 
      return this.photoTime; 
   }
   public void setPhotoTime(java.lang.String photoTime) { 
      this.photoTime = photoTime; 
   }


   public java.lang.String getPhotoName() { 
      return this.photoName; 
   }
   public void setPhotoName(java.lang.String photoName) { 
      this.photoName = photoName; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public java.lang.String getPhotoAddr() { 
      return this.photoAddr; 
   }
   public void setPhotoAddr(java.lang.String photoAddr) { 
      this.photoAddr = photoAddr; 
   }


   public java.lang.String getPhoto() { 
      return this.photo; 
   }
   public void setPhoto(java.lang.String photo) { 
      this.photo = photo; 
   }


   public java.lang.String getPhotoSource() { 
      return this.photoSource; 
   }
   public void setPhotoSource(java.lang.String photoSource) { 
      this.photoSource = photoSource; 
   }


   public static String REF_CLASS = "ArtPhoto";
   public static String PROP_ID = "id";
   public static String PROP_PUB_ID = "pubId";
   public static String PROP_PHOTO_DESC = "photoDesc";
   public static String PROP_PHOTO_TYPE = "photoType";
   public static String PROP_PHOTO_TIME = "photoTime";
   public static String PROP_PHOTO_NAME = "photoName";
   public static String PROP_ARTIST_ID = "artistId";
   public static String PROP_PHOTO_ADDR = "photoAddr";
   public static String PROP_PHOTO = "photo";
   public static String PROP_PHOTO_SOURCE = "photoSource";

   public static String REF_TABLE = "art_photo";
   public static String COL_ID = "id";
   public static String COL_PUB_ID = "pub_id";
   public static String COL_PHOTO_DESC = "photo_desc";
   public static String COL_PHOTO_TYPE = "photo_type";
   public static String COL_PHOTO_TIME = "photo_time";
   public static String COL_PHOTO_NAME = "photo_name";
   public static String COL_ARTIST_ID = "artist_id";
   public static String COL_PHOTO_ADDR = "photo_addr";
   public static String COL_PHOTO = "photo";
   public static String COL_PHOTO_SOURCE = "photo_source";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.photo.model.ArtPhoto)) return false;
      else {
         com.golead.art.photo.model.ArtPhoto o = (com.golead.art.photo.model.ArtPhoto) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtPhoto:");
		buffer.append(" id:").append(id);
		buffer.append(" pubId:").append(dealNull(pubId));
		buffer.append(" photoDesc:").append(dealNull(photoDesc));
		buffer.append(" photoType:").append(dealNull(photoType));
		buffer.append(" photoTime:").append(dealNull(photoTime));
		buffer.append(" photoName:").append(dealNull(photoName));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append(" photoAddr:").append(dealNull(photoAddr));
		buffer.append(" photo:").append(dealNull(photo));
		buffer.append(" photoSource:").append(dealNull(photoSource));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"pubId\":\"").append(dealNull(pubId)).append("\"");
		buffer.append(",\"photoDesc\":\"").append(dealNull(photoDesc)).append("\"");
		buffer.append(",\"photoType\":\"").append(dealNull(photoType)).append("\"");
		buffer.append(",\"photoTime\":\"").append(dealNull(photoTime)).append("\"");
		buffer.append(",\"photoName\":\"").append(dealNull(photoName)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append(",\"photoAddr\":\"").append(dealNull(photoAddr)).append("\"");
		buffer.append(",\"photo\":\"").append(dealNull(photo)).append("\"");
		buffer.append(",\"photoSource\":\"").append(dealNull(photoSource)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
