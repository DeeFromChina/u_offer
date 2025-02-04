package com.golead.art.artist.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.artist.dao.ArtArtistCollectorDao;
import com.golead.art.artist.model.ArtArtistCollector;
import com.golead.art.artist.service.ArtArtistCollectorService;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtWorks;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtArtistCollectorServiceImpl extends BaseServiceImpl implements ArtArtistCollectorService {
	private static final long serialVersionUID = 1L;

	public ArtArtistCollector getArtArtistCollector(Serializable id) throws ServiceException {
		try {
			return artArtistCollectorDao.get(id);
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

	public void createArtArtistCollector(ArtArtistCollector artArtistCollector) throws ServiceException {
		try {
			artArtistCollectorDao.save(artArtistCollector);
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

	public void updateArtArtistCollector(Map<String, String> artArtistCollectorMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artArtistCollectorMap.get(ArtArtistCollector.PROP_ID));
			ArtArtistCollector tmp = artArtistCollectorDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artArtistCollectorMap, true);			
			artArtistCollectorDao.update(tmp);
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

	public void deleteArtArtistCollector(Serializable id) throws ServiceException {
		try {
			artArtistCollectorDao.delete(id);
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

	public void deleteArtArtistCollectors(Serializable[] ids) throws ServiceException {
		try {
			artArtistCollectorDao.deleteAll(ids);
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

	public PageQuery queryArtArtistCollector(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artArtistCollectorList", pageQuery);
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
	   String artistId = page.getParameters().get("artistId");
      String paras = "";
      paras = " aac.artist_id = " + artistId + " ";
      if (paras.length() > 0) {
         page.getParameters().put("paras", paras);
      }
	}
	
	public String importCollects(String path, int artistId) throws ServiceException {
      String message = "";
      try {
         FileInputStream fileInputStream = new FileInputStream(path);
         HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
         HSSFSheet sht = wb.getSheetAt(0);
         if (sht != null) {
            for (int rowNum = 2; rowNum < sht.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = sht.getRow(rowNum);
               if(hssfRow == null) continue;
               if (hssfRow.getCell(0) == null) {
                  message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
                  break;
               }
               if(("").equals(getValue(hssfRow.getCell(0)))) {
                  message = "成功操作到第" + String.valueOf(rowNum-1) + "行";
                  break;
               }
               ArtArtistCollector artArtistCollect = new ArtArtistCollector();
               String str = "";
               artArtistCollect.setArtistId(artistId);
               str = getValue(hssfRow.getCell(0));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到收藏家";
                  break;
               }
               artArtistCollect.setCollector(str);
               str = getValue(hssfRow.getCell(1));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到时间";
                  break;
               }
               artArtistCollect.setCollectTime(str);
               str = getValue(hssfRow.getCell(2));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到作品";
                  break;
               }
               artArtistCollect.setCollectWorks(str);
               str = getValue(hssfRow.getCell(3));
               artArtistCollect.setCollectDesc(str);
               createArtArtistCollector(artArtistCollect);
            }
         }
         fileInputStream.close();
      } catch (Exception e) {
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
      } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
         // 返回数值类型的值
         DecimalFormat df = new DecimalFormat("0");
         return df.format(hssfCell.getNumericCellValue());
      } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
         // 返回数值类型的值
         return String.valueOf(hssfCell.getNumericCellValue());
      } else {
         // 返回字符串类型的值
         return String.valueOf(hssfCell.getStringCellValue());
      }
   }
   
   public String findByWorksId(String ids) throws ServiceException{
      try {
         String hql = "FROM ArtWorks works WHERE works.id IN (" + ids + ")";
         List<ArtWorks> artWorks = artWorksDao.findByHql(hql);
         if(artWorks == null || artWorks.size() == 0){
            return "";
         }
         StringBuffer worksName = new StringBuffer();
         for(ArtWorks artWorks2 : artWorks){
            if(worksName.length() != 0){
               worksName.append(",");
            }
            worksName.append(artWorks2.getWorksCName());
         }
         return worksName.toString();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public List<ArtWorks> findWorksByWorksId(String ids) throws ServiceException{
      try {
         String hql = "FROM ArtWorks works WHERE works.id IN (" + ids + ")";
         List<ArtWorks> artWorks = artWorksDao.findByHql(hql);
         if(artWorks == null || artWorks.size() == 0){
            return null;
         }
         return artWorks;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
	
    @Resource
	private ArtArtistCollectorDao	artArtistCollectorDao;
    
    @Resource
    private ArtWorksDao	artWorksDao;

	public void setArtArtistCollectorDao(ArtArtistCollectorDao artArtistCollectorDao) {
		this.artArtistCollectorDao = artArtistCollectorDao;
	}
}

