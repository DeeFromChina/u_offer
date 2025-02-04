package com.golead.art.activity.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.activity.dao.ArtAgencyDao;
import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtAgencyService;

import javax.annotation.Resource; 

import org.springframework.stereotype.Service;

@Service
public class ArtAgencyServiceImpl extends BaseServiceImpl implements ArtAgencyService {
	private static final long serialVersionUID = 1L;

	public ArtAgency getArtAgency(Serializable id) throws ServiceException {
		try {
			return artAgencyDao.get(id);
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

	public void createArtAgency(ArtAgency artAgency) throws ServiceException {
		try {
			artAgencyDao.save(artAgency);
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

	public void updateArtAgency(Map<String, String> artAgencyMap) throws ServiceException {
		try {
            Integer pkId = new Integer(artAgencyMap.get(ArtAgency.PROP_ID));
			ArtAgency tmp = artAgencyDao.get(pkId);
			ConvertUtil.mapToObject(tmp, artAgencyMap, true);			
			artAgencyDao.update(tmp);
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

	public void deleteArtAgency(Serializable id) throws ServiceException {
		try {
			artAgencyDao.delete(id);
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

	public void deleteArtAgencys(Serializable[] ids) throws ServiceException {
		try {
			artAgencyDao.deleteAll(ids);
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

	public PageQuery queryArtAgency(PageQuery pageQuery) throws ServiceException {
		try {
		  createSqlFilter(pageQuery);
          return jdbcDao.queryBySqlId("artAgencyList", pageQuery);
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

      String agencyCName = map.get("agencyCName");
      if (agencyCName != null && agencyCName.trim().length() > 0) {
         paras += " AND agency.agency_c_name like '%" + agencyCName.trim() + "%'";
      }
      
      String country = map.get("country");
      if (country != null && country.trim().length() > 0) {
         paras += " AND country.country_name like '%" + country.trim() + "%'";
      }
      
      String city = map.get("city");
      if (city != null && city.trim().length() > 0) {
         paras += " AND agency.city like '%" + city.trim() + "%'";
      }
      
      String agencyType = map.get("agencyType");
      if (agencyType != null && agencyType.trim().length() > 0) {
         paras += " AND agency.agency_type =" + agencyType.trim();
      }
      
      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }
	
	public List<ArtAgency> findAll() throws ServiceException{
	   try {
         return artAgencyDao.findAll();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
	}
	
	public int findByCountry(String ids) throws ServiceException{
      try {
         String str = "SELECT COUNT(*) FROM art_agency agency WHERE agency.country_id IN (" + ids + ")";
         return jdbcDao.queryIntBySql(str);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
	
    @Resource
	private ArtAgencyDao	artAgencyDao;

	public void setArtAgencyDao(ArtAgencyDao artAgencyDao) {
		this.artAgencyDao = artAgencyDao;
	}
}

