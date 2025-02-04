package com.golead.art.app.artistPage.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.app.artistPage.dao.ArtArtistTemplateDao;
import com.golead.art.app.artistPage.model.ArtArtistTemplate;
import com.golead.art.app.artistPage.service.ArtArtistTemplateService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArtArtistTemplateServiceImpl extends BaseServiceImpl implements ArtArtistTemplateService {
   private static final long serialVersionUID = 1L;

   public ArtArtistTemplate getArtArtistTemplate(Serializable id) throws ServiceException {
      try {
         return artArtistTemplateDao.get(id);
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

   public void createArtArtistTemplate(ArtArtistTemplate artArtistTemplate) throws ServiceException {
      try {
         artArtistTemplateDao.save(artArtistTemplate);
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

   public void updateArtArtistTemplate(Map<String, String> artArtistTemplateMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistTemplateMap.get(ArtArtistTemplate.PROP_ID));
         ArtArtistTemplate tmp = artArtistTemplateDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistTemplateMap, true);
         artArtistTemplateDao.update(tmp);
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

   public void deleteArtArtistTemplate(Serializable id) throws ServiceException {
      try {
         artArtistTemplateDao.delete(id);
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

   public void deleteArtArtistTemplates(Serializable[] ids) throws ServiceException {
      try {
         artArtistTemplateDao.deleteAll(ids);
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

   public PageQuery queryArtArtistTemplate(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artArtistTemplateList", pageQuery);
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

      Map<String, String> parameters = page.getParameters();

      StringBuffer tmp = new StringBuffer();

      String artistName;
   }

   @Resource
   private ArtArtistTemplateDao artArtistTemplateDao;

   public void setArtArtistTemplateDao(ArtArtistTemplateDao artArtistTemplateDao) {
      this.artArtistTemplateDao = artArtistTemplateDao;
   }
}
