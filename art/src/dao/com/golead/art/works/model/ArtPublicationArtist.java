package com.golead.art.works.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_publication_artist table.
 */
@Entity  
@Table(name="art_publication_artist")  
public class ArtPublicationArtist implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "pub_id")
   private java.lang.Integer pubId;   // 出版物id

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家id

   // Constructors
   public ArtPublicationArtist() {
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


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public static String REF_CLASS = "ArtPublicationArtist";
   public static String PROP_ID = "id";
   public static String PROP_PUB_ID = "pubId";
   public static String PROP_ARTIST_ID = "artistId";

   public static String REF_TABLE = "art_publication_artist";
   public static String COL_ID = "id";
   public static String COL_PUB_ID = "pub_id";
   public static String COL_ARTIST_ID = "artist_id";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.works.model.ArtPublicationArtist)) return false;
      else {
         com.golead.art.works.model.ArtPublicationArtist o = (com.golead.art.works.model.ArtPublicationArtist) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtPublicationArtist:");
		buffer.append(" id:").append(id);
		buffer.append(" pubId:").append(dealNull(pubId));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"pubId\":\"").append(dealNull(pubId)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
