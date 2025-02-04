package com.golead.art.artist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_artist_donation table.
 */
@Entity  
@Table(name="art_artist_donation")  
public class ArtArtistDonation implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "donation_works")
   private java.lang.String donationWorks;   // 作品，多个以、隔开

   @Column(name = "donation_time")
   private java.lang.String donationTime;   // 时间

   @Column(name = "donation_desc")
   private java.lang.String donationDesc;   // 描述

   @Column(name = "artist_id")
   private java.lang.Integer artistId;   // 艺术家id

   // Constructors
   public ArtArtistDonation() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getDonationWorks() { 
      return this.donationWorks; 
   }
   public void setDonationWorks(java.lang.String donationWorks) { 
      this.donationWorks = donationWorks; 
   }


   public java.lang.String getDonationTime() { 
      return this.donationTime; 
   }
   public void setDonationTime(java.lang.String donationTime) { 
      this.donationTime = donationTime; 
   }


   public java.lang.String getDonationDesc() { 
      return this.donationDesc; 
   }
   public void setDonationDesc(java.lang.String donationDesc) { 
      this.donationDesc = donationDesc; 
   }


   public java.lang.Integer getArtistId() { 
      return this.artistId; 
   }
   public void setArtistId(java.lang.Integer artistId) { 
      this.artistId = artistId; 
   }


   public static String REF_CLASS = "ArtArtistDonation";
   public static String PROP_ID = "id";
   public static String PROP_DONATION_WORKS = "donationWorks";
   public static String PROP_DONATION_TIME = "donationTime";
   public static String PROP_DONATION_DESC = "donationDesc";
   public static String PROP_ARTIST_ID = "artistId";

   public static String REF_TABLE = "art_artist_donation";
   public static String COL_ID = "id";
   public static String COL_DONATION_WORKS = "donation_works";
   public static String COL_DONATION_TIME = "donation_time";
   public static String COL_DONATION_DESC = "donation_desc";
   public static String COL_ARTIST_ID = "artist_id";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.artist.model.ArtArtistDonation)) return false;
      else {
         com.golead.art.artist.model.ArtArtistDonation o = (com.golead.art.artist.model.ArtArtistDonation) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtArtistDonation:");
		buffer.append(" id:").append(id);
		buffer.append(" donationWorks:").append(dealNull(donationWorks));
		buffer.append(" donationTime:").append(dealNull(donationTime));
		buffer.append(" donationDesc:").append(dealNull(donationDesc));
		buffer.append(" artistId:").append(dealNull(artistId));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"donationWorks\":\"").append(dealNull(donationWorks)).append("\"");
		buffer.append(",\"donationTime\":\"").append(dealNull(donationTime)).append("\"");
		buffer.append(",\"donationDesc\":\"").append(dealNull(donationDesc)).append("\"");
		buffer.append(",\"artistId\":\"").append(dealNull(artistId)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
