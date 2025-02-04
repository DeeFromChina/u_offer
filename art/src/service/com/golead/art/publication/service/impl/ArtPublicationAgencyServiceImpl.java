package com.golead.art.publication.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.publication.dao.ArtPublicationAgencyDao;
import com.golead.art.publication.model.ArtPublicationAgency;
import com.golead.art.publication.service.ArtPublicationAgencyService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArtPublicationAgencyServiceImpl extends BaseServiceImpl implements ArtPublicationAgencyService {
   private static final long serialVersionUID = 1L;

   public ArtPublicationAgency getArtPublicationAgency(Serializable id) throws ServiceException {
      try {
         return artPublicationAgencyDao.get(id);
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

   public void createArtPublicationAgency(ArtPublicationAgency artPublicationAgency) throws ServiceException {
      try {
         artPublicationAgencyDao.save(artPublicationAgency);
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

   public void createArtPublicationAgency(List<ArtPublicationAgency> artPublicationAgencies) throws ServiceException {
      try {
         for (ArtPublicationAgency artPublicationAgency : artPublicationAgencies) {
            artPublicationAgencyDao.save(artPublicationAgency);
         }
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

   public void updateArtPublicationAgency(Map<String, String> artPublicationAgencyMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artPublicationAgencyMap.get(ArtPublicationAgency.PROP_ID));
         ArtPublicationAgency tmp = artPublicationAgencyDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artPublicationAgencyMap, true);
         artPublicationAgencyDao.update(tmp);
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

   public void deleteArtPublicationAgency(Serializable id) throws ServiceException {
      try {
         artPublicationAgencyDao.delete(id);
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

   public void deleteArtPublicationAgencys(Serializable[] ids) throws ServiceException {
      try {
         artPublicationAgencyDao.deleteAll(ids);
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

   public PageQuery queryArtPublicationAgency(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artPublicationAgencyList", pageQuery);
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

   public PageQuery queryAgency(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artAgencyList2", pageQuery);
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

   private void createSqlFilter(PageQuery page) {
      StringBuffer tmp = new StringBuffer();
      Map<String, String> parameters = page.getParameters();
      String publicationId = parameters.get("publicationId");

      if (publicationId != null && !"".equals(publicationId)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" apa.publication_id = ").append(publicationId);
      }

      if (tmp.length() > 0) parameters.put("paras", tmp.toString());
   }

   @Resource
   private ArtPublicationAgencyDao artPublicationAgencyDao;

   public void setArtPublicationAgencyDao(ArtPublicationAgencyDao artPublicationAgencyDao) {
      this.artPublicationAgencyDao = artPublicationAgencyDao;
   }
}
