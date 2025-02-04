package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

import com.golead.art.works.dao.ArtWorksYearDao;
import com.golead.art.works.model.ArtWorksYear;
import com.golead.art.works.service.ArtWorksYearService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ArtWorksYearServiceImpl extends BaseServiceImpl implements ArtWorksYearService {
   private static final long serialVersionUID = 1L;

   public ArtWorksYear getArtWorksYear(Serializable id) throws ServiceException {
      try {
         return artWorksYearDao.get(id);
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

   public void createArtWorksYear(ArtWorksYear artWorksYear) throws ServiceException {
      try {
         artWorksYearDao.save(artWorksYear);
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
   public void createArtWorksYear(List<ArtWorksYear> artWorksYear) throws ServiceException {
      try {
         artWorksYearDao.saveOrUpdateAll(artWorksYear);
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

   public void updateArtWorksYear(Map<String, String> artWorksYearMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artWorksYearMap.get(ArtWorksYear.PROP_ID));
         ArtWorksYear tmp = artWorksYearDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artWorksYearMap, true);
         artWorksYearDao.update(tmp);
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

   public void deleteArtWorksYear(Serializable id) throws ServiceException {
      try {
         artWorksYearDao.delete(id);
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

   public void deleteArtWorksYears(Serializable[] ids) throws ServiceException {
      try {
         artWorksYearDao.deleteAll(ids);
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

   public PageQuery queryArtWorksYear(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksYearList", pageQuery);
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
   public PageQuery queryArtYearWorks(PageQuery pageQuery) throws ServiceException {
      try {
         createYearWorksListFilter(pageQuery);
         return jdbcDao.queryBySqlId("YearWorksList", pageQuery);
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

   private void createYearWorksListFilter(PageQuery page) {
      StringBuffer tmp = new StringBuffer();

      Map<String, String> parameters = page.getParameters();

      String artistId = parameters.get("artistId");

      if (artistId != null && !"".equals(artistId)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aa.id = ").append(artistId);
      }

      String yearId = parameters.get("yearId");

      if (yearId != null && !"".equals(yearId)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aw.id NOT IN (")
         .append("SELECT aw.id FROM art_works aw ")
         .append(" LEFT JOIN art_works_year_research awyr ON awyr.works_id = aw.id ")
         .append(" LEFT JOIN art_works_year awy ON awy.id = awyr.year_id ")
         .append(" LEFT JOIN art_artist aa ON aw.artist_id = aa.id ")
         .append(" WHERE aa.id = ").append(artistId)
         .append(" AND ")
         .append(" awy.id = ").append(yearId)
         .append(")");
      }

      if (tmp.length() > 0) parameters.put("paras", tmp.toString());
   }

   private void createSqlFilter(PageQuery page) {
      StringBuffer tmp = new StringBuffer();

      Map<String, String> parameters = page.getParameters();

      String artistId = parameters.get("artistId");

      if (artistId != null && !"".equals(artistId)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aa.id = ").append(artistId);
      }

      String yearId = parameters.get("yearId");

      if (yearId != null && !"".equals(yearId)) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" awy.id = ").append(yearId);

         //.append(" AND ").append(" CAST(aw.create_to AS SIGNED) BETWEEN CAST(awy.start_year AS SIGNED) AND CAST(awy.end_year AS SIGNED) ")
      }

      if (tmp.length() > 0) parameters.put("paras", tmp.toString());
   }

   @Resource
   private ArtWorksYearDao artWorksYearDao;

   public void setArtWorksYearDao(ArtWorksYearDao artWorksYearDao) {
      this.artWorksYearDao = artWorksYearDao;
   }
}
