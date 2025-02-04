package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.works.dao.ArtWorksStyleDao;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.service.ArtWorksStyleService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class ArtWorksStyleServiceImpl extends BaseServiceImpl implements ArtWorksStyleService {
   private static final long serialVersionUID = 1L;

   public ArtWorksStyle getArtWorksStyle(Serializable id) throws ServiceException {
      try {
         return artWorksStyleDao.get(id);
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

   public void createArtWorksStyle(ArtWorksStyle artWorksStyle) throws ServiceException {
      try {
         artWorksStyleDao.save(artWorksStyle);
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

   public void updateArtWorksStyle(Map<String, String> artWorksStyleMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artWorksStyleMap.get(ArtWorksStyle.PROP_ID));
         ArtWorksStyle tmp = artWorksStyleDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artWorksStyleMap, true);
         artWorksStyleDao.update(tmp);
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

   public void deleteArtWorksStyle(Serializable id) throws ServiceException {
      try {
         artWorksStyleDao.delete(id);
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

   public void deleteArtWorksStyles(Serializable[] ids) throws ServiceException {
      try {
         artWorksStyleDao.deleteAll(ids);
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

   public PageQuery queryArtWorksStyle(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksStyleList", pageQuery);
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
      String str = map.get("styleName");
      if (str != null && !"".equals(str.trim())) {
         tmp = "t.style_name like '%" + str.trim() + "%'";
      }
      if (tmp.length() > 0) map.put("paras", tmp);
   }
   
   public List<ArtWorksStyle> findAll() throws ServiceException{
      try {
         return artWorksStyleDao.findAll();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Resource
   private ArtWorksStyleDao artWorksStyleDao;

   public void setArtWorksStyleDao(ArtWorksStyleDao artWorksStyleDao) {
      this.artWorksStyleDao = artWorksStyleDao;
   }
}
