package com.golead.art.auction.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_auction_video table.
 */
@Entity  
@Table(name="art_auction_video")  
public class ArtAuctionVideo implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "auction_id")
   private java.lang.Integer auctionId;   // 作品拍卖id

   @Column(name = "video_theme")
   private java.lang.String videoTheme;   // 视频主题

   @Column(name = "video_time")
   private java.util.Date videoTime;   // 时间

   @Column(name = "video_link")
   private java.lang.String videoLink;   // 附件

   @Column(name = "video_source")
   private java.lang.String videoSource;   // 出处
   
   @Column(name = "video_linkaddr")
   private java.lang.String videoLinkaddr;   // 链接

   // Constructors
   public ArtAuctionVideo() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.Integer getAuctionId() { 
      return this.auctionId; 
   }
   public void setAuctionId(java.lang.Integer auctionId) { 
      this.auctionId = auctionId; 
   }


   public java.lang.String getVideoTheme() { 
      return this.videoTheme; 
   }
   public void setVideoTheme(java.lang.String videoTheme) { 
      this.videoTheme = videoTheme; 
   }


   public java.util.Date getVideoTime() { 
      return this.videoTime; 
   }
   public void setVideoTime(java.util.Date videoTime) { 
      this.videoTime = videoTime; 
   }


   public java.lang.String getVideoLink() { 
      return this.videoLink; 
   }
   public void setVideoLink(java.lang.String videoLink) { 
      this.videoLink = videoLink; 
   }


   public java.lang.String getVideoSource() { 
      return this.videoSource; 
   }
   public void setVideoSource(java.lang.String videoSource) { 
      this.videoSource = videoSource; 
   }


   public java.lang.String getVideoLinkaddr() {
      return videoLinkaddr;
   }

   public void setVideoLinkaddr(java.lang.String videoLinkaddr) {
      this.videoLinkaddr = videoLinkaddr;
   }


   public static String REF_CLASS = "ArtAuctionVideo";
   public static String PROP_ID = "id";
   public static String PROP_AUCTION_ID = "auctionId";
   public static String PROP_VIDEO_THEME = "videoTheme";
   public static String PROP_VIDEO_TIME = "videoTime";
   public static String PROP_VIDEO_LINK = "videoLink";
   public static String PROP_VIDEO_SOURCE = "videoSource";
   public static String PROP_VIDEO_LINKADDR = "videoLinkaddr";

   public static String REF_TABLE = "art_auction_video";
   public static String COL_ID = "id";
   public static String COL_AUCTION_ID = "auction_id";
   public static String COL_VIDEO_THEME = "video_theme";
   public static String COL_VIDEO_TIME = "video_time";
   public static String COL_VIDEO_LINK = "video_link";
   public static String COL_VIDEO_SOURCE = "video_source";
   public static String COL_VIDEO_LINKADDR = "video_linkaddr";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.auction.model.ArtAuctionVideo)) return false;
      else {
         com.golead.art.auction.model.ArtAuctionVideo o = (com.golead.art.auction.model.ArtAuctionVideo) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtAuctionVideo:");
		buffer.append(" id:").append(id);
		buffer.append(" auctionId:").append(dealNull(auctionId));
		buffer.append(" videoTheme:").append(dealNull(videoTheme));
		buffer.append(" videoTime:").append(dealNull(videoTime));
		buffer.append(" videoLink:").append(dealNull(videoLink));
		buffer.append(" videoSource:").append(dealNull(videoSource));
		buffer.append(" videoLinkaddr:").append(dealNull(videoLinkaddr));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"auctionId\":\"").append(dealNull(auctionId)).append("\"");
		buffer.append(",\"videoTheme\":\"").append(dealNull(videoTheme)).append("\"");
		buffer.append(",\"videoTime\":\"").append(dealNull(videoTime)).append("\"");
		buffer.append(",\"videoLink\":\"").append(dealNull(videoLink)).append("\"");
		buffer.append(",\"videoSource\":\"").append(dealNull(videoSource)).append("\"");
		buffer.append(",\"videoLinkaddr\":\"").append(dealNull(videoLinkaddr)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
