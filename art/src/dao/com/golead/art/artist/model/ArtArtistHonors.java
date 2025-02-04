package com.golead.art.artist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_artist_honors table.
 */
@Entity  
@Table(name="art_artist_honors")  
public class ArtArtistHonors implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家id

   @Column(name = "honor_desc")
   private java.lang.String honorDesc;   // 描述

   @Column(name = "honor_time")
   private java.lang.String honorTime;   // 时间

   // Constructors
   public ArtArtistHonors() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public java.lang.String getHonorDesc() { 
      return this.honorDesc; 
   }
   public void setHonorDesc(java.lang.String honorDesc) { 
      this.honorDesc = honorDesc; 
   }


   public java.lang.String getHonorTime() { 
      return this.honorTime; 
   }
   public void setHonorTime(java.lang.String honorTime) { 
      this.honorTime = honorTime; 
   }


   public static String REF_CLASS = "ArtArtistHonors";
   public static String PROP_ID = "id";
   public static String PROP_HONOR_WORKS = "honorWorks";
   public static String PROP_ARTIST_ID = "artistId";
   public static String PROP_HONOR_DESC = "honorDesc";
   public static String PROP_HONOR_TIME = "honorTime";

   public static String REF_TABLE = "art_artist_honors";
   public static String COL_ID = "id";
   public static String COL_HONOR_WORKS = "honor_works";
   public static String COL_ARTIST_ID = "artist_id";
   public static String COL_HONOR_DESC = "honor_desc";
   public static String COL_HONOR_TIME = "honor_time";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.artist.model.ArtArtistHonors)) return false;
      else {
         com.golead.art.artist.model.ArtArtistHonors o = (com.golead.art.artist.model.ArtArtistHonors) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtArtistHonors:");
		buffer.append(" id:").append(id);
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append(" honorDesc:").append(dealNull(honorDesc));
		buffer.append(" honorTime:").append(dealNull(honorTime));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append(",\"honorDesc\":\"").append(dealNull(honorDesc)).append("\"");
		buffer.append(",\"honorTime\":\"").append(dealNull(honorTime)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
