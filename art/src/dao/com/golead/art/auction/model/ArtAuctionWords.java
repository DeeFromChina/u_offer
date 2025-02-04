package com.golead.art.auction.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_auction_words table.
 */
@Entity  
@Table(name="art_auction_words")  
public class ArtAuctionWords implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "words_theme")
   private java.lang.String wordsTheme;   // 文章主题

   @Column(name = "words_source")
   private java.lang.String wordsSource;   // 出处

   @Column(name = "auction_id")
   private java.lang.Integer auctionId;   // 作品拍卖id

   @Column(name = "attachment")
   private java.lang.String attachment;   // 附件

   @Column(name = "words_time")
   private java.util.Date wordsTime;   // 时间

   // Constructors
   public ArtAuctionWords() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getWordsTheme() { 
      return this.wordsTheme; 
   }
   public void setWordsTheme(java.lang.String wordsTheme) { 
      this.wordsTheme = wordsTheme; 
   }


   public java.lang.String getWordsSource() { 
      return this.wordsSource; 
   }
   public void setWordsSource(java.lang.String wordsSource) { 
      this.wordsSource = wordsSource; 
   }


   public java.lang.Integer getAuctionId() { 
      return this.auctionId; 
   }
   public void setAuctionId(java.lang.Integer auctionId) { 
      this.auctionId = auctionId; 
   }


   public java.lang.String getAttachment() { 
      return this.attachment; 
   }
   public void setAttachment(java.lang.String attachment) { 
      this.attachment = attachment; 
   }


   public java.util.Date getWordsTime() { 
      return this.wordsTime; 
   }
   public void setWordsTime(java.util.Date wordsTime) { 
      this.wordsTime = wordsTime; 
   }


   public static String REF_CLASS = "ArtAuctionWords";
   public static String PROP_ID = "id";
   public static String PROP_WORDS_THEME = "wordsTheme";
   public static String PROP_WORDS_SOURCE = "wordsSource";
   public static String PROP_AUCTION_ID = "auctionId";
   public static String PROP_ATTACHMENT = "attachment";
   public static String PROP_WORDS_TIME = "wordsTime";

   public static String REF_TABLE = "art_auction_words";
   public static String COL_ID = "id";
   public static String COL_WORDS_THEME = "words_theme";
   public static String COL_WORDS_SOURCE = "words_source";
   public static String COL_AUCTION_ID = "auction_id";
   public static String COL_ATTACHMENT = "attachment";
   public static String COL_WORDS_TIME = "words_time";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.auction.model.ArtAuctionWords)) return false;
      else {
         com.golead.art.auction.model.ArtAuctionWords o = (com.golead.art.auction.model.ArtAuctionWords) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtAuctionWords:");
		buffer.append(" id:").append(id);
		buffer.append(" wordsTheme:").append(dealNull(wordsTheme));
		buffer.append(" wordsSource:").append(dealNull(wordsSource));
		buffer.append(" auctionId:").append(dealNull(auctionId));
		buffer.append(" attachment:").append(dealNull(attachment));
		buffer.append(" wordsTime:").append(dealNull(wordsTime));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"wordsTheme\":\"").append(dealNull(wordsTheme)).append("\"");
		buffer.append(",\"wordsSource\":\"").append(dealNull(wordsSource)).append("\"");
		buffer.append(",\"auctionId\":\"").append(dealNull(auctionId)).append("\"");
		buffer.append(",\"attachment\":\"").append(dealNull(attachment)).append("\"");
		buffer.append(",\"wordsTime\":\"").append(dealNull(wordsTime)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
