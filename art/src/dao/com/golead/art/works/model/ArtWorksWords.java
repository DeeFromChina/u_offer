package com.golead.art.works.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_works_words table.
 */
@Entity  
@Table(name="art_works_words")  
public class ArtWorksWords implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "works_id")
   private java.lang.Integer worksId;   // 作品id

   @Column(name = "words_desc")
   private java.lang.String wordsDesc;   // 描述

   @Column(name = "words_id")
   private java.lang.Integer wordsId;   // 文字id

   // Constructors
   public ArtWorksWords() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.Integer getWorksId() { 
      return this.worksId; 
   }
   public void setWorksId(java.lang.Integer worksId) { 
      this.worksId = worksId; 
   }


   public java.lang.String getWordsDesc() { 
      return this.wordsDesc; 
   }
   public void setWordsDesc(java.lang.String wordsDesc) { 
      this.wordsDesc = wordsDesc; 
   }


   public java.lang.Integer getWordsId() { 
      return this.wordsId; 
   }
   public void setWordsId(java.lang.Integer wordsId) { 
      this.wordsId = wordsId; 
   }


   public static String REF_CLASS = "ArtWorksWords";
   public static String PROP_ID = "id";
   public static String PROP_WORKS_ID = "worksId";
   public static String PROP_WORDS_DESC = "wordsDesc";
   public static String PROP_WORDS_ID = "wordsId";

   public static String REF_TABLE = "art_works_words";
   public static String COL_ID = "id";
   public static String COL_WORKS_ID = "works_id";
   public static String COL_WORDS_DESC = "words_desc";
   public static String COL_WORDS_ID = "words_id";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.works.model.ArtWorksWords)) return false;
      else {
         com.golead.art.works.model.ArtWorksWords o = (com.golead.art.works.model.ArtWorksWords) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtWorksWords:");
		buffer.append(" id:").append(id);
		buffer.append(" worksId:").append(dealNull(worksId));
		buffer.append(" wordsDesc:").append(dealNull(wordsDesc));
		buffer.append(" wordsId:").append(dealNull(wordsId));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"worksId\":\"").append(dealNull(worksId)).append("\"");
		buffer.append(",\"wordsDesc\":\"").append(dealNull(wordsDesc)).append("\"");
		buffer.append(",\"wordsId\":\"").append(dealNull(wordsId)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
