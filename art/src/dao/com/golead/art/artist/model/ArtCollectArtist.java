package com.golead.art.artist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_collect_artist table.
 */
@Entity  
@Table(name="art_collect_artist")  
public class ArtCollectArtist implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties

   @Column(name = "folder_name")
   private java.lang.String folderName;   //  


   @Column(name = "artist_name")
   private java.lang.String artistName;   //  

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   //  

   // Constructors
   public ArtCollectArtist() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getFolderName() { 
      return this.folderName; 
   }
   public void setFolderName(java.lang.String folderName) { 
      this.folderName = folderName; 
   }


   public java.lang.String getArtistName() { 
      return this.artistName; 
   }
   public void setArtistName(java.lang.String artistName) { 
      this.artistName = artistName; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public static String REF_CLASS = "ArtCollectArtist";
   public static String PROP_FOLDER_NAME = "folderName";
   public static String PROP_ID = "id";
   public static String PROP_ARTIST_NAME = "artistName";
   public static String PROP_ARTIST_ID = "artistId";

   public static String REF_TABLE = "art_collect_artist";
   public static String COL_FOLDER_NAME = "folder_name";
   public static String COL_ID = "id";
   public static String COL_ARTIST_NAME = "artist_name";
   public static String COL_ARTIST_ID = "artist_id";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.artist.model.ArtCollectArtist)) return false;
      else {
         com.golead.art.artist.model.ArtCollectArtist o = (com.golead.art.artist.model.ArtCollectArtist) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtCollectArtist:");
		buffer.append(" id:").append(id);
		buffer.append(" folderName:").append(dealNull(folderName));
		buffer.append(" artistName:").append(dealNull(artistName));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"folderName\":\"").append(dealNull(folderName)).append("\"");
		buffer.append(",\"artistName\":\"").append(dealNull(artistName)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
