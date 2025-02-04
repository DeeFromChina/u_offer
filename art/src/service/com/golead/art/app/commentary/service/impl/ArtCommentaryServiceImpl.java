package com.golead.art.app.commentary.service.impl;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.golead.art.app.commentary.dao.ArtCommentaryDao;
import com.golead.art.app.commentary.model.ArtCommentary;
import com.golead.art.app.commentary.service.ArtCommentaryService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtCommentaryServiceImpl extends BaseServiceImpl implements ArtCommentaryService {
   private static final long serialVersionUID = 1L;

   public ArtCommentary getArtCommentary(Serializable id) throws ServiceException {
      try {
         return artCommentaryDao.get(id);
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

   public void createArtCommentary(ArtCommentary artCommentary) throws ServiceException {
      try {
         artCommentaryDao.save(artCommentary);
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

   public void updateArtCommentary(Map<String, String> artCommentaryMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artCommentaryMap.get(ArtCommentary.PROP_ID));
         ArtCommentary tmp = artCommentaryDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artCommentaryMap, true);
         artCommentaryDao.update(tmp);
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
   public void updateArtCommentary(ArtCommentary artCommentary) throws ServiceException {

      try {

         artCommentaryDao.update(artCommentary);
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

   public void deleteArtCommentary(Serializable id) throws ServiceException {
      try {
         artCommentaryDao.delete(id);
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

   public void deleteArtCommentarys(Serializable[] ids) throws ServiceException {
      try {
         artCommentaryDao.deleteAll(ids);
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

   public PageQuery queryArtCommentary(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artCommentaryList", pageQuery);
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
   public PageQuery queryArtFeedback(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artFeedbackList", pageQuery);
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
      if(year!=null && !"".equals(year)){
         if(tmp.length()>0) tmp.append(" AND ");
         tmp.append(" YEAR(ac.commentary_time) = ").append(year);
      }
      
      String month = parameters.get("MONTH");
      if(month!=null && !"".equals(month)){
         if(tmp.length()>0) tmp.append(" AND ");
         tmp.append(" MONTH(ac.commentary_time) = ").append(month);
      }
      
      String commentaryStatus = parameters.get("commentaryStatus");
      if(commentaryStatus!=null && !"".equals(commentaryStatus)){
         if(tmp.length()>0) tmp.append(" AND ");
         tmp.append(" ac.commentary_status = ").append(commentaryStatus);
      }
      
      String appAccountName = parameters.get("appAccountName");
      if(appAccountName!=null && !"".equals(appAccountName)){
         if(tmp.length()>0) tmp.append(" AND ");
         tmp.append(" aau.account_name LIKE ").append("'%").append(appAccountName).append("%'");
      } 
      
      String userAccount = parameters.get("userAccount");
      if(userAccount!=null && !"".equals(userAccount)){
         if(tmp.length()>0) tmp.append(" AND ");
         tmp.append(" su.USER_ACCOUNT LIKE ").append("'%").append(userAccount).append("%'");
      } 
      
      String worksName = parameters.get("worksName");
      if(worksName!=null && !"".equals(worksName)){
         if(tmp.length()>0) tmp.append(" AND ");
         tmp.append(" aw.works_c_name LIKE ").append("'%").append(worksName).append("%'").append(" OR ").append(" aw.works_e_name LIKE ").append("'%").append(worksName).append("%'");
      }
      
      if (tmp.length() > 0) page.getParameters().put("paras", tmp.toString());
   }

   @Resource
   private ArtCommentaryDao artCommentaryDao;

   public void setArtCommentaryDao(ArtCommentaryDao artCommentaryDao) {
      this.artCommentaryDao = artCommentaryDao;
   }
}
