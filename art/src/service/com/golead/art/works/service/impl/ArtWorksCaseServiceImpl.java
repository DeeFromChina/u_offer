package com.golead.art.works.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.golead.art.utils.FileUtils;
import com.golead.art.works.dao.ArtWorksCaseDao;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksCase;
import com.golead.art.works.service.ArtWorksCaseService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.common.service.system.SysCodeService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtWorksCaseServiceImpl extends BaseServiceImpl implements ArtWorksCaseService {
	private static final long serialVersionUID = 1L;

	public ArtWorksCase getArtWorksCase(Serializable id) throws ServiceException {
		try {
			return artWorksCaseDao.get(id);
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

	public void createArtWorksCase(ArtWorksCase artWorksCase, List<File> files, String fileNames, String path) throws ServiceException {
		try {
		   StringBuffer fileName = new StringBuffer();
		   String[] names = fileNames.split(",");
         if (files != null) {
            for (int i = 0; i < files.size(); i++) {
               File file = files.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String[] endless = names[i].split("\\.");
               FileUtils.fileUpload(path + File.separator + cName + "." + endless[endless.length -1], file);
               fileName.append(names[i] + "/" + cName + "." + endless[endless.length -1]);
               if ((i + 1) != files.size()) fileName.append(",");
            }
         }
         artWorksCase.setAttachment(fileName.toString());
			artWorksCaseDao.save(artWorksCase);
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

	public void updateArtWorksCase(Map<String, String> artWorksCaseMap, List<File> files, String fileNames, String addFileName, String path) throws ServiceException {
		try {
         Integer pkId = new Integer(artWorksCaseMap.get(ArtWorksCase.PROP_ID));
			ArtWorksCase tmp = artWorksCaseDao.get(pkId);
			ArtWorksCase artWorksCase = tmp; 
//			List<ArtWorksCase> artWorksCases = findByWorksId(tmp.getWorksId());
			String allattachment = "";
			String yufileNames = fileNames;
//			for(ArtWorksCase artWorksCase : artWorksCases){
			   if(!"".equals(allattachment)){
			      allattachment = allattachment + ",";
			   }
			   allattachment = allattachment + artWorksCase.getAttachment();
			   if(artWorksCase.getId() != tmp.getId()){
			      String[] ats = artWorksCase.getAttachment().split(",");
			      for(int i = 0; i<ats.length; i++){
			         if(!"".equals(yufileNames)){
			            yufileNames = yufileNames + ",";
			         }
			         yufileNames = yufileNames + ats[i].split("/")[1];
			      }
			   }
//			}
			StringBuffer fileName = new StringBuffer();
			
			String[] attachments = allattachment.split(",");
         String[] names = null;
         if(addFileName != null){
            names = addFileName.split(",");//新增加的附件
         }  
         String[] saveNames = yufileNames.split(",");//剩下的附件
         Map<String, String> fileManagerMap = new HashMap<String, String>();
         for(String attachment : attachments){
            for(String saveName : saveNames){
               if(saveName.equals(attachment.split("/")[1])){
                  fileManagerMap.put(attachment.split("/")[1], "true");
               }
            }
         }
         File[] fileList = new File(path).listFiles();
         if(fileList!=null){
            for(File file : fileList){
               if(fileManagerMap.get(file.getName()) == null){
                  file.delete();
               }
            }
         }
         attachments = tmp.getAttachment().split(",");
         for(String attachment : attachments){
            if(fileManagerMap.get(attachment.split("/")[1]) != null){
               fileName.append(attachment + ",");
            }
         }
         
         if (files != null) {
            String[] addnames = names;
            for (int i = 0; i < files.size(); i++) {
               File file = files.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String[] endless = addnames[i].split("\\.");
               File fFile = new File(path);
               if(!fFile.exists()){
                  fFile.mkdirs();
               }
               FileUtils.fileUpload(path + File.separator + cName + "." + endless[endless.length -1], file);
               if(!"".equals(fileName.toString())){
                  if(!fileName.toString().endsWith(",")){
                     fileName.append(",");
                  }
               }
               fileName.append(addnames[i] + "/" + cName + "." + endless[endless.length -1]);
            }
         }
         tmp.setAttachment(fileName.toString());
			ConvertUtil.mapToObject(tmp, artWorksCaseMap, true);			
			artWorksCaseDao.update(tmp);
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

	public void deleteArtWorksCase(Serializable id) throws ServiceException {
		try {
			artWorksCaseDao.delete(id);
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

	public void deleteArtWorksCases(Serializable[] ids, String path) throws ServiceException {
		try {
		   List<String> fileNames = new ArrayList<String>();
		   for(Serializable id : ids){
		      ArtWorksCase artWorksCase = getArtWorksCase(id);
		      String[] attachments = artWorksCase.getAttachment().split(",");
		      for(String attachment : attachments){
		         fileNames.add(attachment.split("/")[1]);
		      }
		   }
		   File[] fileLies = new File(path).listFiles();
		   if(fileLies != null){
		      for(File file : fileLies){
		         for(String fileName : fileNames){
		            if(fileName.equals(file.getName())){
		               file.delete();
		            }
		         }
		      }
		      fileLies = new File(path).listFiles();
		      if(fileLies.length == 0 ){
		         File file = new File(path);
		         file.delete();
		      }
		   }
			artWorksCaseDao.deleteAll(ids);
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

	public PageQuery queryArtWorksCase(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksCaseList", pageQuery);
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
	   String paras = " 1=1 ";
      Map<String, String> map = page.getParameters();
      
      String worksId = map.get("id");
      if (worksId != null && worksId.trim().length() > 0) {
         paras += " AND works.id =" + worksId.trim();
      }
      
      if (paras.length() > 0) page.getParameters().put("paras", paras);
	}
	
	public void deleteArtWorksCases(Serializable[] ids) throws ServiceException{
	   try {
         artWorksCaseDao.deleteAll(ids);
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

   public String importArtWorksCase(String path, Integer id) throws ServiceException{
      String message = "";
      try {
         ArtWorks artWorks = artWorksService.getArtWorks(id);
         InputStream is = new FileInputStream(path);
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
         // 循环工作表Sheet
         HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
         if (hssfSheet != null) {
            for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = hssfSheet.getRow(rowNum);
               if (hssfRow == null) continue;
               
               ArtWorksCase artWorksCase = new ArtWorksCase();
               if(hssfRow.getCell(0) == null){
                  message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
                  break;
               }
               if("".equals(getValue(hssfRow.getCell(0))) || (!artWorks.getWorksCName().equals(getValue(hssfRow.getCell(0))) && !artWorks.getWorksEName().equals(getValue(hssfRow.getCell(0))))){
                  message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
                  break;
               }
               if(!artWorks.getWorksCName().equals(getValue(hssfRow.getCell(0))) && !artWorks.getWorksEName().equals(getValue(hssfRow.getCell(0)))){
                  message = "第" + String.valueOf(rowNum) + "行作品名称不对";
                  break;
               }
               //作者
               String caseAuther = getValue(hssfRow.getCell(1));
               if("".equals(caseAuther)){
                  message = "第" + String.valueOf(rowNum) + "行作者不能为空";
                  break;
               }
               artWorksCase.setCaseAuther(caseAuther);
               //时间
               String caseTime = getValue(hssfRow.getCell(2));
               if("".equals(caseTime)){
                  message = "第" + String.valueOf(rowNum) + "行时间不能为空";
                  break;
               }
               Date time = hssfRow.getCell(2).getDateCellValue();
               artWorksCase.setCaseTime(time);
               //个案名称
               String caseName = getValue(hssfRow.getCell(3));
               if("".equals(caseName)){
                  message = "第" + String.valueOf(rowNum) + "行个案名称不能为空";
                  break;
               }
               artWorksCase.setCaseName(caseName);
               //研究主题
               String researchTopic = getValue(hssfRow.getCell(4));
               if("".equals(researchTopic)){
                  message = "第" + String.valueOf(rowNum) + "行研究主题不能为空";
                  break;
               }
               artWorksCase.setResearchTopic(researchTopic);
               //内容
               String caseContent = getValue(hssfRow.getCell(5));
               if("".equals(caseContent)){
                  message = "第" + String.valueOf(rowNum) + "行内容不能为空";
                  break;
               }
               artWorksCase.setCaseContent(caseContent);
               artWorksCase.setWorksId(id);
               createArtWorksCase(artWorksCase);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      File file = new File(path);
      if(file.exists() && file.isFile()){
         file.delete();
      }
      return message;
   }
   
   private String getValue(HSSFCell hssfCell) {
      if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
         // 返回布尔类型的值   
         return String.valueOf(hssfCell.getBooleanCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
         // 返回数值类型的值  
         DecimalFormat df = new DecimalFormat("0");
         return df.format(hssfCell.getNumericCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
         // 返回数值类型的值  
         return String.valueOf(hssfCell.getNumericCellValue());
      }
      else {
         // 返回字符串类型的值      
         return String.valueOf(hssfCell.getStringCellValue());
      }
   }
   
   public void createArtWorksCase(ArtWorksCase artWorksCase) throws ServiceException{
      try {
         artWorksCaseDao.save(artWorksCase);
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
   
   public List<ArtWorksCase> findByWorksId(Serializable id) throws ServiceException{
      try {
         return artWorksCaseDao.findByField(ArtWorksCase.PROP_WORKS_ID, id);
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
	
   @Resource
   private ArtWorksService	artWorksService;
   
   @Resource
   private SysCodeService	sysCodeService;
   
    @Resource
	private ArtWorksCaseDao	artWorksCaseDao;

	public void setArtWorksCaseDao(ArtWorksCaseDao artWorksCaseDao) {
		this.artWorksCaseDao = artWorksCaseDao;
	}
}

