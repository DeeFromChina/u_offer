package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.golead.art.works.dao.ArtWorksThemeDao;
import com.golead.art.works.model.ArtWorksTheme;
import com.golead.art.works.service.ArtWorksThemeService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtWorksThemeServiceImpl extends BaseServiceImpl implements ArtWorksThemeService {
   private static final long serialVersionUID = 1L;

   public ArtWorksTheme getArtWorksTheme(Serializable id) throws ServiceException {
      try {
         return artWorksThemeDao.get(id);
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

   public void createArtWorksTheme(ArtWorksTheme artWorksTheme) throws ServiceException {
      try {
         artWorksThemeDao.save(artWorksTheme);
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

   public void updateArtWorksTheme(Map<String, String> artWorksThemeMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artWorksThemeMap.get(ArtWorksTheme.PROP_ID));
         ArtWorksTheme tmp = artWorksThemeDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artWorksThemeMap, true);
         artWorksThemeDao.update(tmp);
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

   public void deleteArtWorksTheme(Serializable id) throws ServiceException {
      try {
         artWorksThemeDao.delete(id);
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

   public void deleteArtWorksThemes(Serializable[] ids) throws ServiceException {
      try {
         artWorksThemeDao.deleteAll(ids);
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

   public PageQuery queryArtWorksTheme(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksThemeList", pageQuery);
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
      String str = map.get("themeName");
      if (str != null && !"".equals(str.trim())) {
         tmp = "t.theme_name like '%" + str.trim() + "%'";
      }
      if (tmp.length() > 0) map.put("paras", tmp);
   }
   
   public List<ArtWorksTheme> findAll() throws ServiceException{
      try {
         return artWorksThemeDao.findAll();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Resource
   private ArtWorksThemeDao artWorksThemeDao;

   public void setArtWorksThemeDao(ArtWorksThemeDao artWorksThemeDao) {
      this.artWorksThemeDao = artWorksThemeDao;
   }
}
