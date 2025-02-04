package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;
import com.golead.art.works.dao.ArtWorksAbmbDao;
import com.golead.art.works.model.ArtWorksAbmb;
import com.golead.art.works.service.ArtWorksAbmbService;

import javax.annotation.Resource; 

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtWorksAbmbServiceImpl extends BaseServiceImpl implements ArtWorksAbmbService {
	private static final long serialVersionUID = 1L;

	public ArtWorksAbmb getArtWorksAbmb(Serializable id) throws ServiceException {
		try {
			return artWorksAbmbDao.get(id);
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

	public void createArtWorksAbmb(ArtWorksAbmb artWorksAbmb) throws ServiceException {
		try {
			artWorksAbmbDao.save(artWorksAbmb);
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
	
	public void createArtWorksAbmb(int worksId, int[] ids) throws ServiceException{
	   try {
         for(int i = 0; i<ids.length; i++){
            ArtWorksAbmb artWorksAbmb = new ArtWorksAbmb();
            artWorksAbmb.setWorksId(worksId);
            artWorksAbmb.setAbmbId(ids[i]);
            createArtWorksAbmb(artWorksAbmb);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
	public void createArtWorksAbmb2(int abmbId, int[] ids) throws ServiceException{
	   try {
	      for(int i = 0; i<ids.length; i++){
	         ArtWorksAbmb artWorksAbmb = new ArtWorksAbmb();
	         artWorksAbmb.setWorksId(ids[i]);
	         artWorksAbmb.setAbmbId(abmbId);
	         createArtWorksAbmb(artWorksAbmb);
	      }
	   }
	   catch (Exception e) {
	      e.printStackTrace();
	      throw new ServiceException("系统错误。");
	   }
	}

	public void updateArtWorksAbmb(Map<String, String> artWorksAbmbMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artWorksAbmbMap.get(ArtWorksAbmb.PROP_ID));
			ArtWorksAbmb tmp = artWorksAbmbDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artWorksAbmbMap, true);			
			artWorksAbmbDao.update(tmp);
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

	public void deleteArtWorksAbmb(Serializable id) throws ServiceException {
		try {
			artWorksAbmbDao.delete(id);
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

	public void deleteArtWorksAbmbs(Serializable[] ids) throws ServiceException {
		try {
			artWorksAbmbDao.deleteAll(ids);
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
	
	public String findArtistNameByAbmbId(String id) throws ServiceException{
	   try {
         String sql = "SELECT artist.c_name as name FROM art_works_abmb abmb "
               + " LEFT JOIN art_works works ON works.id = abmb.works_id "
               + " LEFT JOIN art_artist artist ON artist.id = works.artist_id "
               + " WHERE abmb.abmb_id=" + id
               + " GROUP BY artist.c_name ";
         List<Map<String, Object>> list = jdbcDao.queryBySql(sql);
         if(list == null || list.size() < 1){
            return "";
         }
         String name = "";
         for(Map<String, Object> map : list){
            if(map.get("name") != null){
               if(name.length() > 0){
                  name+=",";
               }
               name+=map.get("name").toString();
            }
         }
         return name;
      }
	   catch (DAOException e) {
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

	public PageQuery queryArtWorksAbmb(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artWorksAbmbList", pageQuery);
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
         paras += " AND works_abmb.abmb_id IN ( SELECT works_abmb.abmb_id FROM art_works_abmb works_abmb "
               + " LEFT JOIN art_works works ON works.id = works_abmb.works_id WHERE ";
         paras += " works_abmb.works_id=" + worksId.trim() + ")";
      }
      
      String abmbId = map.get("abmbId");
      if (abmbId != null && abmbId.trim().length() > 0) {
         paras += " AND works_abmb.abmb_id=" + abmbId.trim();
      }
      
      String abmbName = map.get("abmbName");
      if (abmbName != null && abmbName.trim().length() > 0) {
         paras += " AND abmb.abmb_name LIKE '%" + abmbName.trim() + "%'";
      }
      
      String worksName = map.get("worksName");
      if (worksName != null && worksName.trim().length() > 0) {
         paras += " AND (works.works_c_name LIKE '%" + worksName.trim() + "%'" 
               + " OR works.works_e_name LIKE '%" + worksName.trim() + "%')";
      }

      String artistName = map.get("artistName");
      if (artistName != null && artistName.trim().length() > 0) {
         paras += " AND (artist.c_name LIKE '%" + artistName.trim() + "%'"
               + " OR artist.e_name LIKE '%" + artistName.trim() + "%')";
      }
      
      if (worksId != null && worksId.trim().length() > 0) {
         paras += " GROUP BY abmb.id ";
      }
      
      if (paras.length() > 0) page.getParameters().put("paras", paras);
	}

    @Resource
	private ArtWorksAbmbDao	artWorksAbmbDao;

	public void setArtWorksAbmbDao(ArtWorksAbmbDao artWorksAbmbDao) {
		this.artWorksAbmbDao = artWorksAbmbDao;
	}
}

