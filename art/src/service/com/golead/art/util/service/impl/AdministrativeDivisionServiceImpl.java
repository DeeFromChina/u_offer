package com.golead.art.util.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.golead.art.util.service.AdministrativeDivisionService;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;

@Service
public class AdministrativeDivisionServiceImpl extends BaseServiceImpl implements AdministrativeDivisionService {

   @Override
   public List<Map<String, Object>> findAllProvince() throws ServiceException {
      try {
         String sqlStr = "select t.area_code,t.area_name from view_ol_province t";
         return jdbcDao.queryBySql(sqlStr);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   
   @Override
   public List<Map<String, Object>> findAllCity() throws ServiceException {
      try {
         String sqlStr = "select t.area_code,t.area_name from view_ol_city t";
         return jdbcDao.queryBySql(sqlStr);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Override
   public List<Map<String, Object>> findAllCounty() throws ServiceException {
      try {
         String sqlStr = "select t.area_code,t.area_name from view_ol_county t";
         return jdbcDao.queryBySql(sqlStr);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Override
   public List<Map<String, Object>> findCityByParentCode(String parentCode) throws ServiceException {
      try {
         String sqlStr = "select t.area_code,t.area_name from view_ol_city t where t.parent_code='" + parentCode + "'";
         return jdbcDao.queryBySql(sqlStr);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Override
   public List<Map<String, Object>> findCountyByParentCode(String parentCode) throws ServiceException {
      try {
         String sqlStr = "select t.area_code,t.area_name from view_ol_county t where t.parent_code='" + parentCode + "'";
         return jdbcDao.queryBySql(sqlStr);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Override
	public List<Map<String, Object>> findNameByCode(String code) throws ServiceException {
		try {
			String sqlStr = "select t.area_code,t.parent_code,t.area_name from ol_city_area t where t.area_code='" + code + "'";
			return jdbcDao.queryBySql(sqlStr);
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
   
   @Override
	public List<Map<String, Object>> findMunicipalityOrAreaOrCounty(String code) throws ServiceException {
		try {
			String sqlStr = "select t.area_code,t.area_name from view_ol_city t where t.area_code = '" + code + "'" + " and (t.area_name like '%市辖%' or t.area_name like '%县%')";
			return jdbcDao.queryBySql(sqlStr);
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
   
   @Override
	public List<Map<String, Object>> findMunicipalityOrAreaOrCountyByParentCode(String code) throws ServiceException {
		try {
			String sqlStr = "select t.area_code,t.area_name from view_ol_city t where t.parent_code = '" + code + "'" + " and (t.area_name like '%市辖%' or t.area_name like '%县%')";
			return jdbcDao.queryBySql(sqlStr);
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
   
   @Override
   public List<Map<String, Object>> findCodeByName(String name) throws ServiceException {
	   try {
			String sqlStr = "select t.area_code,t.area_name from ol_city_area t where t.area_name like '%" + name + "%'" + " ORDER BY t.area_level";
			return jdbcDao.queryBySql(sqlStr);
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
   
}
