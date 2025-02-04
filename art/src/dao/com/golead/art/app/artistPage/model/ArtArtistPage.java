package com.golead.art.app.artistPage.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_artist_page table.
 */
@Entity  
@Table(name="art_artist_page")  
public class ArtArtistPage implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   // id

   // properties


   @Column(name = "seq_no")
   private java.lang.String seqNo;   // 序列号(1:基本档案、2:活动履历、3:照片手迹、4:作品、5:拍卖、6:出版著作、7:展览图文、8:经典文献、9:衍生品、10:艺虎个案、11:最新动态)

   @Column(name = "color_self")
   private java.lang.String colorSelf;   // 自定义颜色代码

   @Column(name = "color")
   private java.lang.String color;   // 颜色(黑色，白色，蓝色，绿色，红色，橙色，灰色)

   @Column(name = "remark")
   private java.lang.String remark;   // 备注

   @Column(name = "photo")
   private java.lang.String photo;   // 图片

   @Column(name = "temp_id")
   private java.lang.Integer tempId;   // 模板id

   // Constructors
   public ArtArtistPage() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getSeqNo() { 
      return this.seqNo; 
   }
   public void setSeqNo(java.lang.String seqNo) { 
      this.seqNo = seqNo; 
   }


   public java.lang.String getColorSelf() { 
      return this.colorSelf; 
   }
   public void setColorSelf(java.lang.String colorSelf) { 
      this.colorSelf = colorSelf; 
   }


   public java.lang.String getColor() { 
      return this.color; 
   }
   public void setColor(java.lang.String color) { 
      this.color = color; 
   }


   public java.lang.String getRemark() { 
      return this.remark; 
   }
   public void setRemark(java.lang.String remark) { 
      this.remark = remark; 
   }


   public java.lang.String getPhoto() { 
      return this.photo; 
   }
   public void setPhoto(java.lang.String photo) { 
      this.photo = photo; 
   }


   public java.lang.Integer getTempId() { 
      return this.tempId; 
   }
   public void setTempId(java.lang.Integer tempId) { 
      this.tempId = tempId; 
   }


   public static String REF_CLASS = "ArtArtistPage";
   public static String PROP_ID = "id";
   public static String PROP_SEQ_NO = "seqNo";
   public static String PROP_COLOR_SELF = "colorSelf";
   public static String PROP_COLOR = "color";
   public static String PROP_REMARK = "remark";
   public static String PROP_PHOTO = "photo";
   public static String PROP_TEMP_ID = "tempId";

   public static String REF_TABLE = "art_artist_page";
   public static String COL_ID = "id";
   public static String COL_SEQ_NO = "seq_no";
   public static String COL_COLOR_SELF = "color_self";
   public static String COL_COLOR = "color";
   public static String COL_REMARK = "remark";
   public static String COL_PHOTO = "photo";
   public static String COL_TEMP_ID = "temp_id";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.app.artistPage.model.ArtArtistPage)) return false;
      else {
         com.golead.art.app.artistPage.model.ArtArtistPage o = (com.golead.art.app.artistPage.model.ArtArtistPage) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtArtistPage:");
		buffer.append(" id:").append(id);
		buffer.append(" seqNo:").append(dealNull(seqNo));
		buffer.append(" colorSelf:").append(dealNull(colorSelf));
		buffer.append(" color:").append(dealNull(color));
		buffer.append(" remark:").append(dealNull(remark));
		buffer.append(" photo:").append(dealNull(photo));
		buffer.append(" tempId:").append(dealNull(tempId));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"seqNo\":\"").append(dealNull(seqNo)).append("\"");
		buffer.append(",\"colorSelf\":\"").append(dealNull(colorSelf)).append("\"");
		buffer.append(",\"color\":\"").append(dealNull(color)).append("\"");
		buffer.append(",\"remark\":\"").append(dealNull(remark)).append("\"");
		buffer.append(",\"photo\":\"").append(dealNull(photo)).append("\"");
		buffer.append(",\"tempId\":\"").append(dealNull(tempId)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
