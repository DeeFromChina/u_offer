package com.golead.art.works.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an object that contains data related to the art_works_attachment table.
 */
@Entity  
@Table(name="art_works_attachment")  
public class ArtWorksAttachment implements Serializable {
    private static final long serialVersionUID = 1L;

   // primary key  
   @Id  
   @GeneratedValue(strategy=GenerationType.AUTO)  
   @Column(name = "id", nullable = false)
   private java.lang.Integer id;   //  

   // properties


   @Column(name = "is_cover")
   private java.lang.String isCover;   // 设置为封面

   @Column(name = "works_id")
   private java.lang.Integer worksId;   // 作品id

   @Column(name = "file_store_name")
   private java.lang.String fileStoreName;   // 文件存储名称

   @Column(name = "file_name")
   private java.lang.String fileName;   // 文件名

   @Column(name = "file_desc")
   private java.lang.String fileDesc;   // 描述

   // Constructors
   public ArtWorksAttachment() {
   }

   public java.lang.Integer getId() { 
      return this.id; 
   }

   public void setId(java.lang.Integer id) { 
      this.id = id; 
   }


   public java.lang.String getIsCover() { 
      return this.isCover; 
   }
   public void setIsCover(java.lang.String isCover) { 
      this.isCover = isCover; 
   }


   public java.lang.Integer getWorksId() { 
      return this.worksId; 
   }
   public void setWorksId(java.lang.Integer worksId) { 
      this.worksId = worksId; 
   }


   public java.lang.String getFileStoreName() { 
      return this.fileStoreName; 
   }
   public void setFileStoreName(java.lang.String fileStoreName) { 
      this.fileStoreName = fileStoreName; 
   }


   public java.lang.String getFileName() { 
      return this.fileName; 
   }
   public void setFileName(java.lang.String fileName) { 
      this.fileName = fileName; 
   }


   public java.lang.String getFileDesc() { 
      return this.fileDesc; 
   }
   public void setFileDesc(java.lang.String fileDesc) { 
      this.fileDesc = fileDesc; 
   }


   public static String REF_CLASS = "ArtWorksAttachment";
   public static String PROP_ID = "id";
   public static String PROP_IS_COVER = "isCover";
   public static String PROP_WORKS_ID = "worksId";
   public static String PROP_FILE_STORE_NAME = "fileStoreName";
   public static String PROP_FILE_NAME = "fileName";
   public static String PROP_FILE_DESC = "fileDesc";

   public static String REF_TABLE = "art_works_attachment";
   public static String COL_ID = "id";
   public static String COL_IS_COVER = "is_cover";
   public static String COL_WORKS_ID = "works_id";
   public static String COL_FILE_STORE_NAME = "file_store_name";
   public static String COL_FILE_NAME = "file_name";
   public static String COL_FILE_DESC = "file_desc";
   
   public boolean equals(Object obj) {
      if (null == obj) return false;
      if (!(obj instanceof com.golead.art.works.model.ArtWorksAttachment)) return false;
      else {
         com.golead.art.works.model.ArtWorksAttachment o = (com.golead.art.works.model.ArtWorksAttachment) obj;
         if (null == this.getId() || null == o.getId()) return false;
         else return (this.getId().equals(o.getId()));
      }
   }

   public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ArtWorksAttachment:");
		buffer.append(" id:").append(id);
		buffer.append(" isCover:").append(dealNull(isCover));
		buffer.append(" worksId:").append(dealNull(worksId));
		buffer.append(" fileStoreName:").append(dealNull(fileStoreName));
		buffer.append(" fileName:").append(dealNull(fileName));
		buffer.append(" fileDesc:").append(dealNull(fileDesc));
		buffer.append("]");
		return buffer.toString();
   }

   public String toJson() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("\"id\":\"").append(id).append("\"");
		buffer.append(",\"isCover\":\"").append(dealNull(isCover)).append("\"");
		buffer.append(",\"worksId\":\"").append(dealNull(worksId)).append("\"");
		buffer.append(",\"fileStoreName\":\"").append(dealNull(fileStoreName)).append("\"");
		buffer.append(",\"fileName\":\"").append(dealNull(fileName)).append("\"");
		buffer.append(",\"fileDesc\":\"").append(dealNull(fileDesc)).append("\"");
		buffer.append("}");
		return buffer.toString();
   }

	private String dealNull(Object str) {
		if(str==null) return ""; else return str.toString();
	}
}
