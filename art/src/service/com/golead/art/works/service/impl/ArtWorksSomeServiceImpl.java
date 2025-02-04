package com.golead.art.works.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golead.common.model.SysCode;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.works.dao.ArtWorksSomeDao;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksPeriod;
import com.golead.art.works.model.ArtWorksSome;
import com.golead.art.works.service.ArtMediumService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.art.works.service.ArtWorksSomeService;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtWorksSomeServiceImpl extends BaseServiceImpl implements ArtWorksSomeService {
	private static final long serialVersionUID = 1L;

	public ArtWorksSome getArtWorksSome(Serializable id) throws ServiceException {
		try {
			return artWorksSomeDao.get(id);
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

	public void createArtWorksSome(ArtWorksSome artWorksSome) throws ServiceException {
		try {
			artWorksSomeDao.save(artWorksSome);
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

	public void updateArtWorksSome(Map<String, String> artWorksSomeMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artWorksSomeMap.get(ArtWorksSome.PROP_ID));
			ArtWorksSome tmp = artWorksSomeDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artWorksSomeMap, true);			
			artWorksSomeDao.update(tmp);
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

	public void deleteArtWorksSome(Serializable id) throws ServiceException {
		try {
			artWorksSomeDao.delete(id);
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

	public void deleteArtWorksSomes(Serializable[] ids) throws ServiceException {
		try {
			artWorksSomeDao.deleteAll(ids);
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

	public PageQuery queryArtWorksSome(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksSomeList", pageQuery);
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
      
      String worksId = map.get("worksId");
      if (worksId != null && worksId.trim().length() > 0) {
         paras += " AND some.works_id =" + worksId.trim();
      }
      
      if (paras.length() > 0) 
         page.getParameters().put("paras", paras);
	}
	
	public List<ArtWorksSome> findByWorksId(Serializable id) throws ServiceException{
	   try {
	          return artWorksSomeDao.findByField(ArtWorksSome.PROP_WORKS_ID, id);
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
	
	public void createArtWorksSome(Serializable[] ids, String worksId) throws ServiceException{
	   try {
	      for(Serializable id : ids){
	         ArtWorks artWorks = artWorksService.getArtWorks(id);
	         ArtWorksSome artWorksSome = new ArtWorksSome();
	         artWorksSome.setMediumMaterial(artWorks.getMediumMaterial());
	         artWorksSome.setMediumShape(artWorks.getMediumShape());
	         artWorksSome.setWorksName(artWorks.getWorksCName());
	         artWorksSome.setSomeWorksId(artWorks.getId());
	         artWorksSome.setWorksId(Integer.valueOf(worksId));
	         artWorksSome.setYear(artWorks.getCreateTo());
	         createArtWorksSome(artWorksSome);
	      }
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

   public String importArtWorksSome(String path, Integer id) throws ServiceException{
      String message = "";
      try {
         ArtWorks artWorks = artWorksService.getArtWorks(id);
         List<ArtMedium> material = artMediumService.findByCategory("1");
         Map<String, String> materialMap = new HashMap<String, String>();
         for(ArtMedium artMedium : material){
            materialMap.put(artMedium.getMediumName(), artMedium.getId().toString());
         }
         List<ArtMedium> shape = artMediumService.findByCategory("2");
         Map<String, String> shapeMap = new HashMap<String, String>();
         for(ArtMedium artMedium : shape){
            shapeMap.put(artMedium.getMediumName(), artMedium.getId().toString());
         }
         List<ArtWorks> artWorks2 = artWorksService.findAll();
         Map<String, String> artWorksMap = new HashMap<String, String>();
         for(ArtWorks works : artWorks2){
            artWorksMap.put(works.getWorksCName(), works.getId().toString());
            artWorksMap.put(works.getWorksEName(), works.getId().toString());
         }
         InputStream is = new FileInputStream(path);
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
         // 循环工作表Sheet
         HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
         if (hssfSheet != null) {
            for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = hssfSheet.getRow(rowNum);
               if (hssfRow == null) continue;
               
               ArtWorksSome artWorksSome = new ArtWorksSome();
               if(hssfRow.getCell(0) == null){
                  message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
                  break;
               }
               if("".equals(getValue(hssfRow.getCell(0)))){
                  message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
                  break;
               }
               if(!artWorks.getWorksCName().equals(getValue(hssfRow.getCell(0))) && !artWorks.getWorksEName().equals(getValue(hssfRow.getCell(0)))){
                  message = "第" + String.valueOf(rowNum) + "行作品名称不对";
                  break;
               }
               //名称
               String worksName = getValue(hssfRow.getCell(1));
               if("".equals(worksName)){
                  message = "第" + String.valueOf(rowNum) + "行名称不能为空";
                  break;
               }
               artWorksSome.setWorksName(worksName);
               //年代
               String year = getValue(hssfRow.getCell(2));
               if("".equals(year)){
                  message = "第" + String.valueOf(rowNum) + "行年代不能为空";
                  break;
               }
               artWorksSome.setYear(year);
               //相关作品ID
               String someWorksId = getValue(hssfRow.getCell(3));
               if(artWorksMap.get(someWorksId) == null){
                  message = "第" + String.valueOf(rowNum) + "行相关作品名称找不到";
                  break;
               }
               artWorksSome.setSomeWorksId(Integer.valueOf(artWorksMap.get(someWorksId)));
               //媒介(材料)
               String materialMediums = returnString(getValue(hssfRow.getCell(4)));
               if(!"".equals(materialMediums)){
                  String[] mediums = materialMediums.split(",");
                  String mediumId = "";
                  for(int i = 0; i < mediums.length; i++){
                     String materialMedium = mediums[i];
                     if(materialMap.get(materialMedium) == null){
                        message = "第" + String.valueOf(rowNum) + "行找不到媒介(材料)";
                        break;
                     }
                     if(!"".equals(mediumId)){
                        mediumId+=",";
                     }
                     mediumId+=materialMap.get(materialMedium);
                  }
                  artWorksSome.setMediumMaterial(mediumId);
               }
               //媒介(形式)
               String shapeMediums = returnString(getValue(hssfRow.getCell(5)));
               if(!"".equals(shapeMediums)){
                  String[] mediums = shapeMediums.split(",");
                  String mediumId = "";
                  for(int i = 0; i < mediums.length; i++){
                     String shapeMedium = mediums[i];
                     if(shapeMap.get(shapeMedium) == null){
                        message = "第" + String.valueOf(rowNum) + "行找不到媒介(形式)";
                        break;
                     }
                     if(!"".equals(mediumId)){
                        mediumId+=",";
                     }
                     mediumId+=shapeMap.get(shapeMedium);
                  }
                  artWorksSome.setMediumShape(mediumId);
               }
               artWorksSome.setWorksId(id);
               createArtWorksSome(artWorksSome);
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
   
   private String returnString(Object obj){
      if(obj == null){
         return "";
      }
      return obj.toString();
   }
	
	@Resource
	private ArtMediumService	artMediumService;
	
	@Resource
	private ArtWorksService	artWorksService;
	
    @Resource
	private ArtWorksSomeDao	artWorksSomeDao;

	public void setArtWorksSomeDao(ArtWorksSomeDao artWorksSomeDao) {
		this.artWorksSomeDao = artWorksSomeDao;
	}
}

