package com.golead.art.publication.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_publication_literature table.
 */
@Entity  
@Table(name="art_publication_literature")  
public class ArtPublicationLiterature implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "pub_id")
   private java.lang.Integer pubId;   // 出版物ID

   @Column(name = "literature_id")
   private java.lang.Integer literatureId;   // 文献id

   @Column(name = "page_number")
   private java.lang.String pageNumber;   // 页码

   // Constructors
   public ArtPublicationLiterature() {
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


   public java.lang.Integer getLiteratureId() { 
      return this.literatureId; 
   }
   public void setLiteratureId(java.lang.Integer literatureId) { 
      this.literatureId = literatureId; 
   }


   public java.lang.String getPageNumber() { 
      return this.pageNumber; 
   }
   public void setPageNumber(java.lang.String pageNumber) { 
      this.pageNumber = pageNumber; 
   }


   public static String REF_CLASS = "ArtPublicationLiterature";
   public static String PROP_ID = "id";
   public static String PROP_PUB_ID = "pubId";
   public static String PROP_LITERATURE_ID = "literatureId";
   public static String PROP_PAGE_NUMBER = "pageNumber";

   public static String REF_TABLE = "art_publication_literature";
   public static String COL_ID = "id";
   public static String COL_PUB_ID = "pub_id";
   public static String COL_LITERATURE_ID = "literature_id";
   public static String COL_PAGE_NUMBER = "page_number";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.publication.model.ArtPublicationLiterature)) return false;
      else {
         com.golead.art.publication.model.ArtPublicationLiterature o = (com.golead.art.publication.model.ArtPublicationLiterature) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtPublicationLiterature:");
		buffer.append(" id:").append(id);
		buffer.append(" pubId:").append(dealNull(pubId));
		buffer.append(" literatureId:").append(dealNull(literatureId));
		buffer.append(" pageNumber:").append(dealNull(pageNumber));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"pubId\":\"").append(dealNull(pubId)).append("\"");
		buffer.append(",\"literatureId\":\"").append(dealNull(literatureId)).append("\"");
		buffer.append(",\"pageNumber\":\"").append(dealNull(pageNumber)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
