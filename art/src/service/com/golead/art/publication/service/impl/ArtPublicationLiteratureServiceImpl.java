package com.golead.art.publication.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.publication.dao.ArtPublicationLiteratureDao;
import com.golead.art.publication.model.ArtPublicationLiterature;
import com.golead.art.publication.service.ArtPublicationLiteratureService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class ArtPublicationLiteratureServiceImpl extends BaseServiceImpl implements ArtPublicationLiteratureService {
   private static final long serialVersionUID = 1L;

   public ArtPublicationLiterature getArtPublicationLiterature(Serializable id) throws ServiceException {
      try {
         return artPublicationLiteratureDao.get(id);
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

   public void createArtPublicationLiterature(ArtPublicationLiterature artPublicationLiterature) throws ServiceException {
      try {
         artPublicationLiteratureDao.save(artPublicationLiterature);
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

   public void updateArtPublicationLiterature(Map<String, String> artPublicationLiteratureMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artPublicationLiteratureMap.get(ArtPublicationLiterature.PROP_ID));
         ArtPublicationLiterature tmp = artPublicationLiteratureDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artPublicationLiteratureMap, true);
         artPublicationLiteratureDao.update(tmp);
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

   public void deleteArtPublicationLiterature(Serializable id) throws ServiceException {
      try {
         artPublicationLiteratureDao.delete(id);
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

   public void deleteArtPublicationLiteratures(Serializable[] ids) throws ServiceException {
      try {
         artPublicationLiteratureDao.deleteAll(ids);
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

   public PageQuery queryArtPublicationLiterature(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artPublicationLiteratureList", pageQuery);
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
      String tmp = "";
      Map<String, String> map = page.getParameters();
      String str = map.get("pubId");
      if (str != null && !"".equals(str.trim())) {
         tmp += "p.pub_id =" + str.trim();
      }
      str = map.get("publicationName");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += "w.literature_title like '%" + str.trim() + "%'";
      }
      if (tmp.length() > 0) map.put("paras", tmp);
   }

   @Resource
   private ArtPublicationLiteratureDao artPublicationLiteratureDao;

   public void setArtPublicationLiteratureDao(ArtPublicationLiteratureDao artPublicationLiteratureDao) {
      this.artPublicationLiteratureDao = artPublicationLiteratureDao;
   }
}
