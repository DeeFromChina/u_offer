package com.golead.art.literature.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_literature_network table.
 */
@Entity  
@Table(name="art_literature_network")  
public class ArtLiteratureNetwork implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "literature_source")
   private java.lang.String literatureSource;   // 来源

   @Column(name = "literature_type")
   private java.lang.String literatureType;   // 文献类型

   @Column(name = "literature_link")
   private java.lang.String literatureLink;   // 链接

   @Column(name = "person_involved")
   private java.lang.String personInvolved;   // 参与人物，多个以;分开

   @Column(name = "literature_works")
   private java.lang.String literatureWorks;   // 文献提及作品，多个以;分开

   @Column(name = "publication_time")
   private java.lang.String publicationTime;   // 发布时间

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家

   @Column(name = "quote_literature")
   private java.lang.String quoteLiterature;   // 引用文献，多个以;分开

   @Column(name = "related_exhib")
   private java.lang.String relatedExhib;   // 相关展览，多个以;分开

   @Column(name = "literature_title")
   private java.lang.String literatureTitle;   // 名称

   @Column(name = "related_event")
   private java.lang.String relatedEvent;   // 相关事件，多个以;分开

   @Column(name = "literature_auther")
   private java.lang.String literatureAuther;   // 作者

   // Constructors
   public ArtLiteratureNetwork() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getLiteratureSource() { 
      return this.literatureSource; 
   }
   public void setLiteratureSource(java.lang.String literatureSource) { 
      this.literatureSource = literatureSource; 
   }


   public java.lang.String getLiteratureType() { 
      return this.literatureType; 
   }
   public void setLiteratureType(java.lang.String literatureType) { 
      this.literatureType = literatureType; 
   }


   public java.lang.String getLiteratureLink() { 
      return this.literatureLink; 
   }
   public void setLiteratureLink(java.lang.String literatureLink) { 
      this.literatureLink = literatureLink; 
   }


   public java.lang.String getPersonInvolved() { 
      return this.personInvolved; 
   }
   public void setPersonInvolved(java.lang.String personInvolved) { 
      this.personInvolved = personInvolved; 
   }


   public java.lang.String getLiteratureWorks() { 
      return this.literatureWorks; 
   }
   public void setLiteratureWorks(java.lang.String literatureWorks) { 
      this.literatureWorks = literatureWorks; 
   }


   public java.lang.String getPublicationTime() { 
      return this.publicationTime; 
   }
   public void setPublicationTime(java.lang.String publicationTime) { 
      this.publicationTime = publicationTime; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public java.lang.String getQuoteLiterature() { 
      return this.quoteLiterature; 
   }
   public void setQuoteLiterature(java.lang.String quoteLiterature) { 
      this.quoteLiterature = quoteLiterature; 
   }


   public java.lang.String getRelatedExhib() { 
      return this.relatedExhib; 
   }
   public void setRelatedExhib(java.lang.String relatedExhib) { 
      this.relatedExhib = relatedExhib; 
   }


   public java.lang.String getLiteratureTitle() { 
      return this.literatureTitle; 
   }
   public void setLiteratureTitle(java.lang.String literatureTitle) { 
      this.literatureTitle = literatureTitle; 
   }


   public java.lang.String getRelatedEvent() { 
      return this.relatedEvent; 
   }
   public void setRelatedEvent(java.lang.String relatedEvent) { 
      this.relatedEvent = relatedEvent; 
   }


   public java.lang.String getLiteratureAuther() { 
      return this.literatureAuther; 
   }
   public void setLiteratureAuther(java.lang.String literatureAuther) { 
      this.literatureAuther = literatureAuther; 
   }


   public static String REF_CLASS = "ArtLiteratureNetwork";
   public static String PROP_ID = "id";
   public static String PROP_LITERATURE_SOURCE = "literatureSource";
   public static String PROP_LITERATURE_TYPE = "literatureType";
   public static String PROP_LITERATURE_LINK = "literatureLink";
   public static String PROP_PERSON_INVOLVED = "personInvolved";
   public static String PROP_LITERATURE_WORKS = "literatureWorks";
   public static String PROP_PUBLICATION_TIME = "publicationTime";
   public static String PROP_ARTIST_ID = "artistId";
   public static String PROP_QUOTE_LITERATURE = "quoteLiterature";
   public static String PROP_RELATED_EXHIB = "relatedExhib";
   public static String PROP_LITERATURE_TITLE = "literatureTitle";
   public static String PROP_RELATED_EVENT = "relatedEvent";
   public static String PROP_LITERATURE_AUTHER = "literatureAuther";

   public static String REF_TABLE = "art_literature_network";
   public static String COL_ID = "id";
   public static String COL_LITERATURE_SOURCE = "literature_source";
   public static String COL_LITERATURE_TYPE = "literature_type";
   public static String COL_LITERATURE_LINK = "literature_link";
   public static String COL_PERSON_INVOLVED = "person_involved";
   public static String COL_LITERATURE_WORKS = "literature_works";
   public static String COL_PUBLICATION_TIME = "publication_time";
   public static String COL_ARTIST_ID = "artist_id";
   public static String COL_QUOTE_LITERATURE = "quote_literature";
   public static String COL_RELATED_EXHIB = "related_exhib";
   public static String COL_LITERATURE_TITLE = "literature_title";
   public static String COL_RELATED_EVENT = "related_event";
   public static String COL_LITERATURE_AUTHER = "literature_auther";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.literature.model.ArtLiteratureNetwork)) return false;
      else {
         com.golead.art.literature.model.ArtLiteratureNetwork o = (com.golead.art.literature.model.ArtLiteratureNetwork) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtLiteratureNetwork:");
		buffer.append(" id:").append(id);
		buffer.append(" literatureSource:").append(dealNull(literatureSource));
		buffer.append(" literatureType:").append(dealNull(literatureType));
		buffer.append(" literatureLink:").append(dealNull(literatureLink));
		buffer.append(" personInvolved:").append(dealNull(personInvolved));
		buffer.append(" literatureWorks:").append(dealNull(literatureWorks));
		buffer.append(" publicationTime:").append(dealNull(publicationTime));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append(" quoteLiterature:").append(dealNull(quoteLiterature));
		buffer.append(" relatedExhib:").append(dealNull(relatedExhib));
		buffer.append(" literatureTitle:").append(dealNull(literatureTitle));
		buffer.append(" relatedEvent:").append(dealNull(relatedEvent));
		buffer.append(" literatureAuther:").append(dealNull(literatureAuther));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"literatureSource\":\"").append(dealNull(literatureSource)).append("\"");
		buffer.append(",\"literatureType\":\"").append(dealNull(literatureType)).append("\"");
		buffer.append(",\"literatureLink\":\"").append(dealNull(literatureLink)).append("\"");
		buffer.append(",\"personInvolved\":\"").append(dealNull(personInvolved)).append("\"");
		buffer.append(",\"literatureWorks\":\"").append(dealNull(literatureWorks)).append("\"");
		buffer.append(",\"publicationTime\":\"").append(dealNull(publicationTime)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append(",\"quoteLiterature\":\"").append(dealNull(quoteLiterature)).append("\"");
		buffer.append(",\"relatedExhib\":\"").append(dealNull(relatedExhib)).append("\"");
		buffer.append(",\"literatureTitle\":\"").append(dealNull(literatureTitle)).append("\"");
		buffer.append(",\"relatedEvent\":\"").append(dealNull(relatedEvent)).append("\"");
		buffer.append(",\"literatureAuther\":\"").append(dealNull(literatureAuther)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
