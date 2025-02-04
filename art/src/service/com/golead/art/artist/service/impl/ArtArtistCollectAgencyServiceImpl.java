package com.golead.art.artist.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.works.dao.ArtArtistCollectAgencyDao;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtAgencyService;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistCollectAgency;
import com.golead.art.artist.model.ArtArtistEdu;
import com.golead.art.artist.service.ArtArtistCollectAgencyService;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtArtistCollectAgencyServiceImpl extends BaseServiceImpl implements ArtArtistCollectAgencyService {
	private static final long serialVersionUID = 1L;

	public ArtArtistCollectAgency getArtArtistCollectAgency(Serializable id) throws ServiceException {
		try {
			return artArtistCollectAgencyDao.get(id);
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

	public void createArtArtistCollectAgency(ArtArtistCollectAgency artArtistCollectAgency) throws ServiceException {
		try {
			artArtistCollectAgencyDao.save(artArtistCollectAgency);
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

	public void updateArtArtistCollectAgency(Map<String, String> artArtistCollectAgencyMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artArtistCollectAgencyMap.get(ArtArtistCollectAgency.PROP_ID));
			ArtArtistCollectAgency tmp = artArtistCollectAgencyDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artArtistCollectAgencyMap, true);			
			artArtistCollectAgencyDao.update(tmp);
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

	public void deleteArtArtistCollectAgency(Serializable id) throws ServiceException {
		try {
			artArtistCollectAgencyDao.delete(id);
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

	public void deleteArtArtistCollectAgencys(Serializable[] ids) throws ServiceException {
		try {
			artArtistCollectAgencyDao.deleteAll(ids);
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

	public PageQuery queryArtArtistCollectAgency(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artArtistCollectAgencyList", pageQuery);
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
      
      Map<String, String> parameters = page.getParameters();
      String art_id = parameters.get("artistId");
      paras = " agency.artist_id = " + art_id + " ";
      
      if (paras.length() > 0)
         page.getParameters().put("paras", paras);
   }
	

   public int findByAgency(String ids) throws ServiceException{
      try {
         String str = "SELECT COUNT(*) FROM ART_ARTIST_COLLECT_AGENCY agency WHERE agency.agency_id IN (" + ids +")";
         return jdbcDao.queryIntBySql(str);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public String importFile(String path,int artistId) throws ServiceException {
      String message = "";
      try {
         FileInputStream fileInputStream = new FileInputStream(path);
         HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
         HSSFSheet sht = wb.getSheetAt(0);
         List<ArtAgency> artAgencies = artAgencyService.findAll();
         Map<String, String> agencyMap = new HashMap<String, String>();
         for (ArtAgency agency : artAgencies) {
            agencyMap.put(agency.getAgencyCName(), agency.getId().toString());
            agencyMap.put(agency.getAgencyEName(), agency.getId().toString());
         }
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
               ArtArtistCollectAgency artArtistCollectAgency = new ArtArtistCollectAgency();
               String str = "";
               artArtistCollectAgency.setArtistId(artistId);
               str = getValue(hssfRow.getCell(0));
               if (agencyMap.get(str) == null) {
                  message = "第" + String.valueOf(rowNum) + "行找不到收藏机构";
                  break;
               }
               Integer agencyId = Integer.valueOf(agencyMap.get(str));
               artArtistCollectAgency.setAgencyId(agencyId);
               str = getValue(hssfRow.getCell(1));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到时间";
                  break;
               }
               artArtistCollectAgency.setCollectTime(str);
               str = getValue(hssfRow.getCell(2));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到作品";
                  break;
               }
               artArtistCollectAgency.setCollectWorks(str);
               str = getValue(hssfRow.getCell(3));
               artArtistCollectAgency.setCollectDesc(str);
               artArtistCollectAgency.setAgencyId(agencyId);
               createArtArtistCollectAgency(artArtistCollectAgency);
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
	private ArtArtistCollectAgencyDao	artArtistCollectAgencyDao;
    
    @Resource
    private ArtWorksDao	artWorksDao;
    
    @Resource
    private ArtAgencyService	artAgencyService;

	public void setArtArtistCollectAgencyDao(ArtArtistCollectAgencyDao artArtistCollectAgencyDao) {
		this.artArtistCollectAgencyDao = artArtistCollectAgencyDao;
	}
}

