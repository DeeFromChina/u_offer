package com.golead.art.app.artistPage.service.impl;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.golead.art.app.artistPage.dao.ArtArtistPageResearchDao;
import com.golead.art.app.artistPage.model.ArtArtistPageResearch;
import com.golead.art.app.artistPage.service.ArtArtistPageResearchService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtArtistPageResearchServiceImpl extends BaseServiceImpl implements ArtArtistPageResearchService {
   private static final long serialVersionUID = 1L;

   public ArtArtistPageResearch getArtArtistPageResearch(Serializable id) throws ServiceException {
      try {
         return artArtistPageResearchDao.get(id);
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

   public void createArtArtistPageResearch(ArtArtistPageResearch artArtistPageResearch) throws ServiceException {
      try {
         artArtistPageResearchDao.save(artArtistPageResearch);
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

   public void updateArtArtistPageResearch(Map<String, String> artArtistPageResearchMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistPageResearchMap.get(ArtArtistPageResearch.PROP_ID));
         ArtArtistPageResearch tmp = artArtistPageResearchDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistPageResearchMap, true);
         artArtistPageResearchDao.update(tmp);
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

   public void deleteArtArtistPageResearch(Serializable id) throws ServiceException {
      try {
         artArtistPageResearchDao.delete(id);
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

   public void deleteArtArtistPageResearchs(Serializable[] ids) throws ServiceException {
      try {
         artArtistPageResearchDao.deleteAll(ids);
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

   public PageQuery queryArtArtistPageResearch(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artArtistPageResearchList", pageQuery);
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
   public PageQuery queryArtArtistPageNotResearch(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("operationAddPage", pageQuery);
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
      Map<String, String> map = page.getParameters();
      String tempId = map.get("tempId");
      if (tempId != null && tempId.trim().length() > 0) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aapr.temp_id = ").append(tempId);
      }

      String artistName = map.get("artistName");
      if (artistName != null && artistName.trim().length() > 0) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aa.c_name LIKE '%").append(artistName).append("%'").append(" OR ").append(" aa.e_name LIKE '%").append(artistName).append("%'");
      }

      if (tmp.length() > 0) page.getParameters().put("paras", tmp.toString());
   }

   @Resource
   private ArtArtistPageResearchDao artArtistPageResearchDao;

   public void setArtArtistPageResearchDao(ArtArtistPageResearchDao artArtistPageResearchDao) {
      this.artArtistPageResearchDao = artArtistPageResearchDao;
   }
}
