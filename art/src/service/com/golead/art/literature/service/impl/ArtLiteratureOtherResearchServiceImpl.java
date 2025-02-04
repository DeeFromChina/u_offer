package com.golead.art.literature.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.literature.dao.ArtLiteratureOtherResearchDao;
import com.golead.art.literature.model.ArtLiteratureOtherResearch;
import com.golead.art.literature.service.ArtLiteratureOtherResearchService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArtLiteratureOtherResearchServiceImpl extends BaseServiceImpl implements ArtLiteratureOtherResearchService {
   private static final long serialVersionUID = 1L;

   public ArtLiteratureOtherResearch getArtLiteratureOtherResearchByArtLiteratureOtherId(Serializable artLiteratureOtherId) throws ServiceException {
      try {
         return artLiteratureOtherResearchDao.findByField(ArtLiteratureOtherResearch.PROP_OTHER_ID, artLiteratureOtherId).get(0);
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

   public ArtLiteratureOtherResearch getArtLiteratureOtherResearch(Serializable id) throws ServiceException {
      try {
         return artLiteratureOtherResearchDao.get(id);
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

   public void createArtLiteratureOtherResearch(ArtLiteratureOtherResearch artLiteratureOtherResearch) throws ServiceException {
      try {
         artLiteratureOtherResearchDao.save(artLiteratureOtherResearch);
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

   public void updateArtLiteratureOtherResearch(Map<String, String> artLiteratureOtherResearchMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artLiteratureOtherResearchMap.get(ArtLiteratureOtherResearch.PROP_ID));
         ArtLiteratureOtherResearch tmp = artLiteratureOtherResearchDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artLiteratureOtherResearchMap, true);
         artLiteratureOtherResearchDao.update(tmp);
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
   public void updateArtLiteratureOtherResearch(ArtLiteratureOtherResearch artLiteratureOtherResearch) throws ServiceException {
      try {
         artLiteratureOtherResearchDao.update(artLiteratureOtherResearch);
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

   public void deleteArtLiteratureOtherResearch(Serializable id) throws ServiceException {
      try {
         artLiteratureOtherResearchDao.delete(id);
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
   public void deleteArtLiteratureOtherResearchByOtherId(Serializable otherId) throws ServiceException {
      try {
         artLiteratureOtherResearchDao.deleteByField(ArtLiteratureOtherResearch.PROP_OTHER_ID, otherId);
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

   public void deleteArtLiteratureOtherResearchs(Serializable[] ids) throws ServiceException {
      try {
         artLiteratureOtherResearchDao.deleteAll(ids);
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

   public PageQuery queryArtLiteratureOtherResearch(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artLiteratureOtherResearchList", pageQuery);
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

   public PageQuery queryArtLiteratureOtherRelatedWorks(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("ArtLiteratureOtherRelatedWorksList", pageQuery);
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

      String otherId = parameters.get("otherId");
      if (otherId != null && !otherId.isEmpty()) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" awo.other_id = ").append(otherId);
      }
   }

   @Resource
   private ArtLiteratureOtherResearchDao artLiteratureOtherResearchDao;

   public void setArtLiteratureOtherResearchDao(ArtLiteratureOtherResearchDao artLiteratureOtherResearchDao) {
      this.artLiteratureOtherResearchDao = artLiteratureOtherResearchDao;
   }
}
