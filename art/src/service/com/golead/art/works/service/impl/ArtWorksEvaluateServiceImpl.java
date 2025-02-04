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
import com.golead.common.service.system.SysCodeService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.works.dao.ArtWorksEvaluateDao;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksEvaluate;
import com.golead.art.works.service.ArtWorksEvaluateService;
import com.golead.art.works.service.ArtWorksService;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtWorksEvaluateServiceImpl extends BaseServiceImpl implements ArtWorksEvaluateService {
	private static final long serialVersionUID = 1L;

	public ArtWorksEvaluate getArtWorksEvaluate(Serializable id) throws ServiceException {
		try {
			return artWorksEvaluateDao.get(id);
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

	public void createArtWorksEvaluate(ArtWorksEvaluate artWorksEvaluate) throws ServiceException {
		try {
			artWorksEvaluateDao.save(artWorksEvaluate);
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

	public void updateArtWorksEvaluate(Map<String, String> artWorksEvaluateMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artWorksEvaluateMap.get(ArtWorksEvaluate.PROP_ID));
			ArtWorksEvaluate tmp = artWorksEvaluateDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artWorksEvaluateMap, true);			
			artWorksEvaluateDao.update(tmp);
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

	public void deleteArtWorksEvaluate(Serializable id) throws ServiceException {
		try {
			artWorksEvaluateDao.delete(id);
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

	public void deleteArtWorksEvaluates(Serializable[] ids) throws ServiceException {
		try {
			artWorksEvaluateDao.deleteAll(ids);
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

	public PageQuery queryArtWorksEvaluate(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksEvaluateList", pageQuery);
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
         paras += " AND evaluate.works_id =" + worksId.trim();
      }
      
      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }
	
	public String importArtWorksEvaluate(String path, Integer id) throws ServiceException{
      String message = "";
      try {
         ArtWorks artWorks = artWorksService.getArtWorks(id);
         List<SysCode> sysCodes = sysCodeService.findCodeBySetId(18);
         Map<String, String> map = new HashMap<String, String>();
         for(SysCode sysCode : sysCodes){
            map.put(sysCode.getItemName(), sysCode.getItemValue());
         }
         InputStream is = new FileInputStream(path);
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
         // 循环工作表Sheet
         HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
         if (hssfSheet != null) {
            for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = hssfSheet.getRow(rowNum);
               if (hssfRow == null) continue;
               
               ArtWorksEvaluate artWorksEvaluate = new ArtWorksEvaluate();
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
               //评价类型
               String evaluateType = getValue(hssfRow.getCell(1));
               if(map.get(evaluateType) == null){
                  message = "第" + String.valueOf(rowNum) + "行找不到评价类型";
                  break;
               }
               evaluateType = map.get(evaluateType);
               artWorksEvaluate.setEvaluateType(evaluateType);
               //评价
               String evaluate = getValue(hssfRow.getCell(2));
               if("".equals(evaluate)){
                  message = "第" + String.valueOf(rowNum) + "行评价不能为空";
                  break;
               }
               artWorksEvaluate.setEvaluate(evaluate);
               artWorksEvaluate.setWorksId(id);
               createArtWorksEvaluate(artWorksEvaluate);
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
	
    @Resource
	private ArtWorksEvaluateDao	artWorksEvaluateDao;
    
    @Resource
    private ArtWorksService  artWorksService;
    
    @Resource
    private SysCodeService  sysCodeService;

	public void setArtWorksEvaluateDao(ArtWorksEvaluateDao artWorksEvaluateDao) {
		this.artWorksEvaluateDao = artWorksEvaluateDao;
	}
}

