package com.golead.art.literature.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_literature_words table.
 */
@Entity  
@Table(name="art_literature_words")  
public class ArtLiteratureWords implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties

   @Column(name = "press")
   private java.lang.String press;   // 出版社

   @Column(name = "relevant_pages")
   private java.lang.String relevantPages;   // 相关页

   @Column(name = "publication_period")
   private java.lang.String publicationPeriod;   // 出版物期数

   @Column(name = "person_involved")
   private java.lang.String personInvolved;   // 参与人物

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家

   @Column(name = "quote_literature")
   private java.lang.String quoteLiterature;   // 引用文献，多个以;分开

   @Column(name = "attachment")
   private java.lang.String attachment;   // 附件

   @Column(name = "words_type")
   private java.lang.String wordsType;   // 类型，1：纸本，2：网络，3：论文库，4：艺虎独家


   @Column(name = "publication_name")
   private java.lang.String publicationName;   // 出版物名称

   @Column(name = "literature_column")
   private java.lang.String literatureColumn;   // 栏目

   @Column(name = "literature_works")
   private java.lang.String literatureWorks;   // 文献提及作品，多个以;分开

   @Column(name = "write_time")
   private java.lang.String writeTime;   // 撰文时间

   @Column(name = "related_exhib")
   private java.lang.String relatedExhib;   // 相关展览，多个以;分开

   @Column(name = "related_event")
   private java.lang.String relatedEvent;   // 相关事件，多个以;分开

   @Column(name = "literature_title")
   private java.lang.String literatureTitle;   // 标题

   @Column(name = "literature_auther")
   private java.lang.String literatureAuther;   // 作者

   @Column(name = "attachment_source")
   private java.lang.String attachmentSource;   // 附件来源
   
   // Constructors
   public ArtLiteratureWords() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getPress() { 
      return this.press; 
   }
   public void setPress(java.lang.String press) { 
      this.press = press; 
   }


   public java.lang.String getRelevantPages() { 
      return this.relevantPages; 
   }
   public void setRelevantPages(java.lang.String relevantPages) { 
      this.relevantPages = relevantPages; 
   }


   public java.lang.String getPublicationPeriod() { 
      return this.publicationPeriod; 
   }
   public void setPublicationPeriod(java.lang.String publicationPeriod) { 
      this.publicationPeriod = publicationPeriod; 
   }


   public java.lang.String getPersonInvolved() { 
      return this.personInvolved; 
   }
   public void setPersonInvolved(java.lang.String personInvolved) { 
      this.personInvolved = personInvolved; 
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


   public java.lang.String getAttachment() { 
      return this.attachment; 
   }
   public void setAttachment(java.lang.String attachment) { 
      this.attachment = attachment; 
   }


   public java.lang.String getWordsType() { 
      return this.wordsType; 
   }
   public void setWordsType(java.lang.String wordsType) { 
      this.wordsType = wordsType; 
   }


   public java.lang.String getPublicationName() { 
      return this.publicationName; 
   }
   public void setPublicationName(java.lang.String publicationName) { 
      this.publicationName = publicationName; 
   }


   public java.lang.String getLiteratureColumn() { 
      return this.literatureColumn; 
   }
   public void setLiteratureColumn(java.lang.String literatureColumn) { 
      this.literatureColumn = literatureColumn; 
   }


   public java.lang.String getLiteratureWorks() { 
      return this.literatureWorks; 
   }
   public void setLiteratureWorks(java.lang.String literatureWorks) { 
      this.literatureWorks = literatureWorks; 
   }


   public java.lang.String getWriteTime() { 
      return this.writeTime; 
   }
   public void setWriteTime(java.lang.String writeTime) { 
      this.writeTime = writeTime; 
   }


   public java.lang.String getRelatedExhib() { 
      return this.relatedExhib; 
   }
   public void setRelatedExhib(java.lang.String relatedExhib) { 
      this.relatedExhib = relatedExhib; 
   }


   public java.lang.String getRelatedEvent() { 
      return this.relatedEvent; 
   }
   public void setRelatedEvent(java.lang.String relatedEvent) { 
      this.relatedEvent = relatedEvent; 
   }


   public java.lang.String getLiteratureTitle() { 
      return this.literatureTitle; 
   }
   public void setLiteratureTitle(java.lang.String literatureTitle) { 
      this.literatureTitle = literatureTitle; 
   }


   public java.lang.String getLiteratureAuther() { 
      return this.literatureAuther; 
   }
   public void setLiteratureAuther(java.lang.String literatureAuther) { 
      this.literatureAuther = literatureAuther; 
   }


   public java.lang.String getAttachmentSource() {
      return attachmentSource;
   }

   public void setAttachmentSource(java.lang.String attachmentSource) {
      this.attachmentSource = attachmentSource;
   }


   public static String REF_CLASS = "ArtLiteratureWords";
   public static String PROP_PRESS = "press";
   public static String PROP_RELEVANT_PAGES = "relevantPages";
   public static String PROP_PUBLICATION_PERIOD = "publicationPeriod";
   public static String PROP_PERSON_INVOLVED = "personInvolved";
   public static String PROP_ARTIST_ID = "artistId";
   public static String PROP_QUOTE_LITERATURE = "quoteLiterature";
   public static String PROP_ATTACHMENT = "attachment";
   public static String PROP_WORDS_TYPE = "wordsType";
   public static String PROP_ID = "id";
   public static String PROP_PUBLICATION_NAME = "publicationName";
   public static String PROP_LITERATURE_COLUMN = "literatureColumn";
   public static String PROP_LITERATURE_WORKS = "literatureWorks";
   public static String PROP_WRITE_TIME = "writeTime";
   public static String PROP_RELATED_EXHIB = "relatedExhib";
   public static String PROP_RELATED_EVENT = "relatedEvent";
   public static String PROP_LITERATURE_TITLE = "literatureTitle";
   public static String PROP_LITERATURE_AUTHER = "literatureAuther";
   public static String PROP_ATTACHMENT_SOURCE = "attachmentSource";

   public static String REF_TABLE = "art_literature_words";
   public static String COL_PRESS = "press";
   public static String COL_RELEVANT_PAGES = "relevant_pages";
   public static String COL_PUBLICATION_PERIOD = "publication_period";
   public static String COL_PERSON_INVOLVED = "person_involved";
   public static String COL_ARTIST_ID = "artist_id";
   public static String COL_QUOTE_LITERATURE = "quote_literature";
   public static String COL_ATTACHMENT = "attachment";
   public static String COL_WORDS_TYPE = "words_type";
   public static String COL_ID = "id";
   public static String COL_PUBLICATION_NAME = "publication_name";
   public static String COL_LITERATURE_COLUMN = "literature_column";
   public static String COL_LITERATURE_WORKS = "literature_works";
   public static String COL_WRITE_TIME = "write_time";
   public static String COL_RELATED_EXHIB = "related_exhib";
   public static String COL_RELATED_EVENT = "related_event";
   public static String COL_LITERATURE_TITLE = "literature_title";
   public static String COL_LITERATURE_AUTHER = "literature_auther";
   public static String COL_ATTACHMENT_SOURCE = "attachment_source";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.literature.model.ArtLiteratureWords)) return false;
      else {
         com.golead.art.literature.model.ArtLiteratureWords o = (com.golead.art.literature.model.ArtLiteratureWords) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtLiteratureWords:");
		buffer.append(" id:").append(id);
		buffer.append(" press:").append(dealNull(press));
		buffer.append(" relevantPages:").append(dealNull(relevantPages));
		buffer.append(" publicationPeriod:").append(dealNull(publicationPeriod));
		buffer.append(" personInvolved:").append(dealNull(personInvolved));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append(" quoteLiterature:").append(dealNull(quoteLiterature));
		buffer.append(" attachment:").append(dealNull(attachment));
		buffer.append(" wordsType:").append(dealNull(wordsType));
		buffer.append(" publicationName:").append(dealNull(publicationName));
		buffer.append(" literatureColumn:").append(dealNull(literatureColumn));
		buffer.append(" literatureWorks:").append(dealNull(literatureWorks));
		buffer.append(" writeTime:").append(dealNull(writeTime));
		buffer.append(" relatedExhib:").append(dealNull(relatedExhib));
		buffer.append(" relatedEvent:").append(dealNull(relatedEvent));
		buffer.append(" literatureTitle:").append(dealNull(literatureTitle));
		buffer.append(" literatureAuther:").append(dealNull(literatureAuther));
		buffer.append(" attachmentSource:").append(dealNull(attachmentSource));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"press\":\"").append(dealNull(press)).append("\"");
		buffer.append(",\"relevantPages\":\"").append(dealNull(relevantPages)).append("\"");
		buffer.append(",\"publicationPeriod\":\"").append(dealNull(publicationPeriod)).append("\"");
		buffer.append(",\"personInvolved\":\"").append(dealNull(personInvolved)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append(",\"quoteLiterature\":\"").append(dealNull(quoteLiterature)).append("\"");
		buffer.append(",\"attachment\":\"").append(dealNull(attachment)).append("\"");
		buffer.append(",\"wordsType\":\"").append(dealNull(wordsType)).append("\"");
		buffer.append(",\"publicationName\":\"").append(dealNull(publicationName)).append("\"");
		buffer.append(",\"literatureColumn\":\"").append(dealNull(literatureColumn)).append("\"");
		buffer.append(",\"literatureWorks\":\"").append(dealNull(literatureWorks)).append("\"");
		buffer.append(",\"writeTime\":\"").append(dealNull(writeTime)).append("\"");
		buffer.append(",\"relatedExhib\":\"").append(dealNull(relatedExhib)).append("\"");
		buffer.append(",\"relatedEvent\":\"").append(dealNull(relatedEvent)).append("\"");
		buffer.append(",\"literatureTitle\":\"").append(dealNull(literatureTitle)).append("\"");
		buffer.append(",\"literatureAuther\":\"").append(dealNull(literatureAuther)).append("\"");
		buffer.append(",\"attachmentSource\":\"").append(dealNull(attachmentSource)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
