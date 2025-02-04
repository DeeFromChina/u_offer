package com.golead.art.literature.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;
import com.golead.art.artist.dao.ArtArtistDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.literature.dao.ArtLiteratureMediumDao;
import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureMediumResearch;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.literature.service.ArtLiteratureMediumResearchService;
import com.golead.art.literature.service.ArtLiteratureMediumService;
import com.golead.art.utils.FileUtils;
import com.golead.art.works.dao.ArtWorksMediumDao;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksMedium;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

@Service
public class ArtLiteratureMediumServiceImpl extends BaseServiceImpl implements ArtLiteratureMediumService {
	private static final long serialVersionUID = 1L;

	public ArtLiteratureMedium getArtLiteratureMedium(Serializable id) throws ServiceException {
		try {
			return artLiteratureMediumDao.get(id);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void createArtLiteratureMedium(ArtLiteratureMedium artLiteratureMedium) throws ServiceException {
		try {
			artLiteratureMediumDao.save(artLiteratureMedium);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void updateArtLiteratureMedium(Map<String, String> artLiteratureMediumMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artLiteratureMediumMap.get(ArtLiteratureMedium.PROP_ID));
			ArtLiteratureMedium tmp = artLiteratureMediumDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artLiteratureMediumMap, true);
			artLiteratureMediumDao.update(tmp);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtLiteratureMedium(Serializable id) throws ServiceException {
		try {
			artLiteratureMediumDao.delete(id);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public void deleteArtLiteratureMediums(Serializable[] ids) throws ServiceException {
		try {
			String FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
			boolean pass = false;
			for(Serializable id : ids){
			   List<ArtWorksMedium> artWorksMediums = artWorksMediumDao.findByField(ArtWorksMedium.PROP_MEDIUM_ID, id);
			   if(artWorksMediums != null && artWorksMediums.size() > 0){
               pass = true;
            }
            if(pass){
               throw new ServiceException();
            }
			}
			for (Serializable id : ids) {
				String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "medium" + File.separator + id.toString();
				File file = new File(path);
				if (file.exists()) {
					File[] fileList = file.listFiles();
					for (File inFile : fileList) {
						inFile.delete();
					}
					File[] fileList2 = file.listFiles();
					if (fileList2.length == 0) {
						file.delete();
					}
				}
			}
			artLiteratureMediumDao.deleteAll(ids);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw new ServiceException("文献正在被引用!");
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}

	public PageQuery queryArtLiteratureMedium(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artLiteratureMediumList", pageQuery);
        } catch (DAOException e) {
          e.printStackTrace();
          throw new ServiceException("数据库操作错误。");
        } catch (ServiceException se) {
          se.printStackTrace();
          throw se;
        } catch (Exception e) {
          e.printStackTrace();
          throw new ServiceException("系统错误。");
        }
	}
	
	private void createSqlFilter(PageQuery page) {
		String paras = "";
		Map<String, String> map = page.getParameters();
		String artArtistId = map.get("artArtistId");
		if (artArtistId != null && !"".equals(artArtistId)) {
		   paras = " medium.artist_id =" + artArtistId.trim();
		}
		
		String artArtistName = map.get("artArtistName");
		if (artArtistName != null && !"".equals(artArtistName)) {
		   paras = " artist.c_name LIKE '%" + artArtistName.trim() + "%'";
		}
		
		String literatureTitle = map.get("literatureTitle");
		if (literatureTitle != null && !"".equals(literatureTitle)) {
			paras = " medium.literature_title like '%" + literatureTitle.trim() + "%'";
		}
		
		String shotPeople = map.get("shotPeople");
		if (shotPeople != null && !"".equals(shotPeople)) {
		   paras = " medium.shot_people like '%" + shotPeople.trim() + "%'";
		}
		
		String literatureWorks = map.get("literatureWorks");
      if (literatureWorks != null && !"".equals(literatureWorks)) {
         paras += " medium.literature_works like '%" + literatureWorks.trim() + "%'";
      }

      String relatedExhib = map.get("relatedExhib");
      if (relatedExhib != null && !"".equals(relatedExhib)) {
         paras += " medium.related_exhib like '%" + relatedExhib.trim() + "%'";
      }
      
      String relatedEvent = map.get("relatedEvent");
      if (relatedEvent != null && !"".equals(relatedEvent)) {
         paras += " medium.related_event like '%" + relatedEvent.trim() + "%'";
      }
      
      String personInvolved = map.get("personInvolved");
      if (personInvolved != null && !"".equals(personInvolved)) {
         paras += " medium.person_involved like '%" + personInvolved.trim() + "%'";
      }
      
      String quoteLiterature = map.get("quoteLiterature");
      if (quoteLiterature != null && !"".equals(quoteLiterature)) {
         paras += " medium.quote_literature like '%" + quoteLiterature.trim() + "%'";
      }
		if (paras.length() > 0) {
			page.getParameters().put("paras", paras);
		}
	}

   public boolean saveFile(ArtLiteratureMedium artLiteratureMedium, List<File> files, String filesFileName, String FILE_PATH) throws ServiceException{
      try {
         String[] names = filesFileName.split(",");
         String path = FILE_PATH + File.separator + artLiteratureMedium.getId().toString();
         if (!new File(path).exists()) {
            new File(path).mkdirs();
         }
         StringBuffer fileName = new StringBuffer();
         if (files != null) {
            for (int i = 0; i < files.size(); i++) {
               if(files.get(i) == null){
                  break;
               }
               File file = files.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String[] endless = names[i].split("\\.");
               FileUtils.fileUpload(path + File.separator + cName + "." + endless[endless.length - 1], file);
               fileName.append(names[i] + "/" + cName + "." + endless[endless.length - 1]);
               if ((i + 1) != files.size()) fileName.append(",");
            }
         }
         artLiteratureMedium.setAttachment(fileName.toString());
         updateArtLiteratureMedium(artLiteratureMedium);
         return false;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public void updateFile(ArtLiteratureMediumResearch artLiteratureMediumResearch, ArtLiteratureMedium artLiteratureMedium, List<File> files, String filesFileName, String FILE_PATH, String fileName) throws ServiceException{
      try {
         artLiteratureMediumResearchService.updateArtLiteratureMediumResearch(artLiteratureMediumResearch);
         String path = FILE_PATH + File.separator + artLiteratureMedium.getId().toString();
         File file = new File(path);
         File[] files2 = null;
         if (file.exists()) {
            files2 = file.listFiles();//原本的文件
         }
         Map<String, String> map = new HashMap<String, String>();
         if(fileName != null && !"".equals(fileName)){
            String[] fileNames = fileName.split(",");
            for (int i = 0; i < fileNames.length; i++) {
               if (fileNames[i] != null && !"".equals(fileNames[i])) {
                  map.put(fileNames[i], "");
               }
            }
         }
         String attachment = artLiteratureMedium.getAttachment();
         StringBuffer newAttachment = new StringBuffer();
         if(attachment != null && !"".equals(attachment)){
            String[] attachments = attachment.split(",");
            for(int i = 0; i<attachments.length; i++){
               if(map.get(attachments[i].split("/")[1]) == null){
                  deleteFile(files2, attachments[i].split("/")[1]);
                  attachments[i] = "";
               }
               if(attachments[i] != ""){
                  newAttachment.append(attachments[i]);
                  newAttachment.append(",");
               }
            }
            
         }
         if(files != null){
            saveFile(artLiteratureMedium, files, filesFileName, FILE_PATH);
            attachment = artLiteratureMedium.getAttachment();
            newAttachment.append(attachment);
         }
         String att = newAttachment.toString();
         if(att.endsWith(",")){
            att.substring(0, att.length()-1);
         }
         artLiteratureMedium.setAttachment(att);
         updateArtLiteratureMedium(artLiteratureMedium);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void deleteFile(File[] files, String fileName){
      if(files == null){
         return;
      }
      for(int i = 0; i<files.length; i++){
         File file = files[i];
         if(file.getName().equals(fileName)){
            file.delete();
         }
      }
   }
   
   public void updateArtLiteratureMedium(ArtLiteratureMedium artLiteratureMedium) throws ServiceException{
      try {
         artLiteratureMediumDao.update(artLiteratureMedium);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @SuppressWarnings("deprecation")
   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException{
      try {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
         HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
         HSSFRow row = sheet.createRow((int) 0);  
         
         HSSFCell cell = row.createCell((short) 0);  
         cell.setCellValue("艺术家");  
         cell = row.createCell((short) 1);  
         cell.setCellValue("文献标题");  
         cell = row.createCell((short) 2);  
         cell.setCellValue("时长");  
         cell = row.createCell((short) 3);  
         cell.setCellValue("制作方");  
         cell = row.createCell((short) 4);  
         cell.setCellValue("拍摄时间");  
         cell = row.createCell((short) 5);  
         cell.setCellValue("内容描述");  
         cell = row.createCell((short) 6);  
         cell.setCellValue("文献提及作品");  
         cell = row.createCell((short) 7);  
         cell.setCellValue("文献相关人物");  
         cell = row.createCell((short) 8);  
         cell.setCellValue("文献相关展览");  
         cell = row.createCell((short) 9);  
         cell.setCellValue("文献相关事件");  
         cell = row.createCell((short) 10);  
         cell.setCellValue("链接");  
         pageQuery.setPageSize("10000");
         PageQuery pageQuery2 = queryArtLiteratureMedium(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            row = sheet.createRow(i+1); 
            row.createCell((short) 0).setCellValue(returnString(item.get("CName"))); 
            row.createCell((short) 1).setCellValue(returnString(item.get("literatureTitle"))); 
            row.createCell((short) 2).setCellValue(returnString(item.get("whenLong"))); 
            row.createCell((short) 3).setCellValue(returnString(item.get("shotPeople"))); 
            row.createCell((short) 4).setCellValue(returnString(item.get("shotTime"))); 
            row.createCell((short) 5).setCellValue(returnString(item.get("contentDesc"))); 
            row.createCell((short) 6).setCellValue(returnString(item.get("literatureWorks"))); 
            row.createCell((short) 7).setCellValue(returnString(item.get("personInvolved"))); 
            row.createCell((short) 8).setCellValue(returnString(item.get("relatedExhib"))); 
            row.createCell((short) 9).setCellValue(returnString(item.get("relatedEvent"))); 
            row.createCell((short) 10).setCellValue(returnString(item.get("siteLink"))); 
            i++;
         }
         return hssfWorkbook;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   private String addString(String str, String equalStr, String addStr) {
      if (!"".equals(equalStr)) {
         if (equalStr.indexOf(addStr) > -1) return str + equalStr;
         else return str + equalStr + addStr;
      }
      return str;
   }
   
   private String findCodeName(Object objForm, String codeNo, Serializable value){
      BaseForm bf = (BaseForm) objForm;

      List<Code> list = bf.getCodeSets().get(codeNo);
      if (value == null) return "";
      if (list == null) return "";
      for (int i = 0; i < list.size(); i++) {
         Code code = list.get(i);
         String v;
         if (value instanceof String) v = ((String) value).trim();
         else v = value.toString();
         if (v.equals(code.getValue())) return code.getCodeName();
      }
      return "";
   }

    @Resource
	private ArtLiteratureMediumDao	artLiteratureMediumDao;
    
    @Resource
    private ArtArtistDao	artArtistDao;
    
    @Resource
    private ArtWorksMediumDao	artWorksMediumDao;
    
    @Resource
    private ArtLiteratureMediumResearchService	artLiteratureMediumResearchService;

	public void setArtLiteratureMediumDao(ArtLiteratureMediumDao artLiteratureMediumDao) {
		this.artLiteratureMediumDao = artLiteratureMediumDao;
	}
}

