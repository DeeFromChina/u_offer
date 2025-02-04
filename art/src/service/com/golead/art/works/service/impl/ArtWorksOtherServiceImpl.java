package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.works.dao.ArtWorksOtherDao;
import com.golead.art.works.model.ArtWorksOther;
import com.golead.art.works.service.ArtWorksOtherService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArtWorksOtherServiceImpl extends BaseServiceImpl implements ArtWorksOtherService {
   private static final long serialVersionUID = 1L;

   public ArtWorksOther getArtWorksOther(Serializable id) throws ServiceException {
      try {
         return artWorksOtherDao.get(id);
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

   public void createArtWorksOther(ArtWorksOther artWorksOther) throws ServiceException {
      try {
         artWorksOtherDao.save(artWorksOther);
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

   public void createArtWorksOther(List<ArtWorksOther> artWorksOthers) throws ServiceException {
      try {
         for (int i = 0;i<artWorksOthers.size();i++) {
            artWorksOtherDao.save(artWorksOthers.get(i));
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

   public void updateArtWorksOther(Map<String, String> artWorksOtherMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artWorksOtherMap.get(ArtWorksOther.PROP_ID));
         ArtWorksOther tmp = artWorksOtherDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artWorksOtherMap, true);
         artWorksOtherDao.update(tmp);
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

   public void deleteArtWorksOther(Serializable id) throws ServiceException {
      try {
         artWorksOtherDao.delete(id);
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

   public void deleteArtWorksOthers(Serializable[] ids) throws ServiceException {
      try {
         artWorksOtherDao.deleteAll(ids);
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

   public PageQuery queryArtWorksOther(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksOtherList", pageQuery);
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
      if (otherId != null && !"".equals(otherId)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" awo.other_id = ").append(otherId);
      }

      if (tmp.length() > 0) parameters.put("paras", tmp.toString());
   }

   @Resource
   private ArtWorksOtherDao artWorksOtherDao;

   public void setArtWorksOtherDao(ArtWorksOtherDao artWorksOtherDao) {
      this.artWorksOtherDao = artWorksOtherDao;
   }
}
