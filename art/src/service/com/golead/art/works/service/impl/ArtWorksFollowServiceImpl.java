package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.golead.art.works.dao.ArtWorksFollowDao;
import com.golead.art.works.model.ArtWorksFollow;
import com.golead.art.works.service.ArtWorksFollowService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtWorksFollowServiceImpl extends BaseServiceImpl implements ArtWorksFollowService {
   private static final long serialVersionUID = 1L;

   public ArtWorksFollow getArtWorksFollow(Serializable id) throws ServiceException {
      try {
         return artWorksFollowDao.get(id);
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

   public void createArtWorksFollow(ArtWorksFollow artWorksFollow) throws ServiceException {
      try {
         artWorksFollowDao.save(artWorksFollow);
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

   public void updateArtWorksFollow(Map<String, String> artWorksFollowMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artWorksFollowMap.get(ArtWorksFollow.PROP_ID));
         ArtWorksFollow tmp = artWorksFollowDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artWorksFollowMap, true);
         artWorksFollowDao.update(tmp);
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

   public void deleteArtWorksFollow(Serializable id) throws ServiceException {
      try {
         artWorksFollowDao.delete(id);
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

   public void deleteArtWorksFollows(Serializable[] ids) throws ServiceException {
      try {
         artWorksFollowDao.deleteAll(ids);
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

   public PageQuery queryArtWorksFollow(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksFollowList", pageQuery);
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

      String year = parameters.get("YEAR");
      if (year != null && !"".equals(year)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" YEAR(awf.follow_time) = ").append(year);
      }

      String month = parameters.get("MONTH");
      if (month != null && !"".equals(month)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" MONTH(awf.follow_time) = ").append(month);
      }

      String accountName = parameters.get("accountName");
      if (accountName != null && !"".equals(accountName)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" apu.account_name LIKE ").append("'%").append(accountName).append("%'");
      }

      String worksName = parameters.get("worksName");
      if (worksName != null && !"".equals(worksName)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aw.works_c_name LIKE ").append("'%").append(worksName).append("%'").append("OR").append(" aw.works_e_name LIKE ").append("'%").append(worksName).append("%'");
      }

      if (tmp.length() > 0) parameters.put("paras", tmp.toString());
   }

   @Resource
   private ArtWorksFollowDao artWorksFollowDao;

   public void setArtWorksFollowDao(ArtWorksFollowDao artWorksFollowDao) {
      this.artWorksFollowDao = artWorksFollowDao;
   }
}
