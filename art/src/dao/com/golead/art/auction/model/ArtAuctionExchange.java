package com.golead.art.auction.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_auction_exchange table.
 */
@Entity  
@Table(name="art_auction_exchange")  
public class ArtAuctionExchange implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "p_exchange_rate")
   private java.lang.Double pExchangeRate;   // 英镑汇率

   @Column(name = "d_exchange_rate")
   private java.lang.Double dExchangeRate;   // 美元汇率

   @Column(name = "e_exchange_rate")
   private java.lang.Double eExchangeRate;   // 欧元汇率

   @Column(name = "hk_exchange_rate")
   private java.lang.Double hkExchangeRate;   // 港币汇率

   @Column(name = "year")
   private java.lang.Integer year;   // 年
   
   @Column(name = "season")
   private java.lang.String season;   // 年

   // Constructors
   public ArtAuctionExchange() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.Double getPexchangeRate() { 
      return this.pExchangeRate; 
   }
   public void setPexchangeRate(java.lang.Double pExchangeRate) { 
      this.pExchangeRate = pExchangeRate; 
   }


   public java.lang.Double getDexchangeRate() { 
      return this.dExchangeRate; 
   }
   public void setDexchangeRate(java.lang.Double dExchangeRate) { 
      this.dExchangeRate = dExchangeRate; 
   }


   public java.lang.Double getEexchangeRate() { 
      return this.eExchangeRate; 
   }
   public void setEexchangeRate(java.lang.Double eExchangeRate) { 
      this.eExchangeRate = eExchangeRate; 
   }


   public java.lang.Double getHkExchangeRate() { 
      return this.hkExchangeRate; 
   }
   public void setHkExchangeRate(java.lang.Double hkExchangeRate) { 
      this.hkExchangeRate = hkExchangeRate; 
   }


   public java.lang.Integer getYear() { 
      return this.year; 
   }
   public void setYear(java.lang.Integer year) { 
      this.year = year; 
   }


   public java.lang.String getSeason() {
      return season;
   }

   public void setSeason(java.lang.String season) {
      this.season = season;
   }


   public static String REF_CLASS = "ArtAuctionExchange";
   public static String PROP_ID = "id";
   public static String PROP_P_EXCHANGE_RATE = "pExchangeRate";
   public static String PROP_D_EXCHANGE_RATE = "dExchangeRate";
   public static String PROP_E_EXCHANGE_RATE = "eExchangeRate";
   public static String PROP_HK_EXCHANGE_RATE = "hkExchangeRate";
   public static String PROP_YEAR = "year";
   public static String PROP_SEASON = "season";

   public static String REF_TABLE = "art_auction_exchange";
   public static String COL_ID = "id";
   public static String COL_P_EXCHANGE_RATE = "p_exchange_rate";
   public static String COL_D_EXCHANGE_RATE = "d_exchange_rate";
   public static String COL_E_EXCHANGE_RATE = "e_exchange_rate";
   public static String COL_HK_EXCHANGE_RATE = "hk_exchange_rate";
   public static String COL_YEAR = "year";
   public static String COL_SEASON = "season";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.auction.model.ArtAuctionExchange)) return false;
      else {
         com.golead.art.auction.model.ArtAuctionExchange o = (com.golead.art.auction.model.ArtAuctionExchange) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtAuctionExchange:");
		buffer.append(" id:").append(id);
		buffer.append(" pExchangeRate:").append(dealNull(pExchangeRate));
		buffer.append(" dExchangeRate:").append(dealNull(dExchangeRate));
		buffer.append(" eExchangeRate:").append(dealNull(eExchangeRate));
		buffer.append(" hkExchangeRate:").append(dealNull(hkExchangeRate));
		buffer.append(" year:").append(dealNull(year));
		buffer.append(" season:").append(dealNull(season));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"pExchangeRate\":\"").append(dealNull(pExchangeRate)).append("\"");
		buffer.append(",\"dExchangeRate\":\"").append(dealNull(dExchangeRate)).append("\"");
		buffer.append(",\"eExchangeRate\":\"").append(dealNull(eExchangeRate)).append("\"");
		buffer.append(",\"hkExchangeRate\":\"").append(dealNull(hkExchangeRate)).append("\"");
		buffer.append(",\"year\":\"").append(dealNull(year)).append("\"");
		buffer.append(",\"season\":\"").append(dealNull(season)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
