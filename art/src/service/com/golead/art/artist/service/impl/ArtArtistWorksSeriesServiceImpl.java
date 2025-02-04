package com.golead.art.artist.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.golead.art.artist.dao.ArtArtistWorksSeriesDao;
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.artist.service.ArtArtistWorksSeriesService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtArtistWorksSeriesServiceImpl extends BaseServiceImpl implements ArtArtistWorksSeriesService {
   private static final long serialVersionUID = 1L;

   public ArtArtistWorksSeries getArtArtistWorksSeries(Serializable id) throws ServiceException {
      try {
         return artArtistWorksSeriesDao.get(id);
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

   public void createArtArtistWorksSeries(ArtArtistWorksSeries artArtistWorksSeries) throws ServiceException {
      try {
         artArtistWorksSeriesDao.save(artArtistWorksSeries);
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

   public void updateArtArtistWorksSeries(Map<String, String> artArtistWorksSeriesMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistWorksSeriesMap.get(ArtArtistWorksSeries.PROP_ID));
         ArtArtistWorksSeries tmp = artArtistWorksSeriesDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistWorksSeriesMap, true);
         artArtistWorksSeriesDao.update(tmp);
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

   public void deleteArtArtistWorksSeries(Serializable id) throws ServiceException {
      try {
         artArtistWorksSeriesDao.delete(id);
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

   public void deleteArtArtistWorksSeriess(Serializable[] ids) throws ServiceException {
      try {
         artArtistWorksSeriesDao.deleteAll(ids);
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

   public PageQuery queryArtArtistWorksSeries(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artArtistWorksSeriesList", pageQuery);
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
      String paras = "";
      Map<String, String> parameters = page.getParameters();

      String art_id = parameters.get("artistId");
      if (art_id != null && art_id.trim().length() > 0) {
         paras = " series.artist_id = " + art_id + " ";
      }

      String worksId = parameters.get("worksId");
      if (worksId != null && worksId.trim().length() > 0) {
         if(!"".equals(paras)){
            paras+="AND";
         }
         paras+= " aw.id=" + worksId;
      }
      if(paras.length() > 0){
         parameters.put("paras", paras);
      }

   }

   @Override
   public PageQuery queryArtArtistSeriesWorks(PageQuery pageQuery) throws ServiceException {
      try {
         createArtArtistSeriesWorksFilter(pageQuery);
         return jdbcDao.queryBySqlId("SeriesWorksList", pageQuery);
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

   public void createArtArtistSeriesWorksFilter(PageQuery pageQuery) {
      StringBuffer tmp = new StringBuffer();

      Map<String, String> parameters = pageQuery.getParameters();

      String artistId = parameters.get("artistId");

      if (artistId != null && !artistId.isEmpty()) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aw.artist_id = ").append(artistId);
      }

      String worksCName = parameters.get("worksCName");
      if (worksCName != null && !worksCName.isEmpty()) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append(" aw.works_c_name LIKE ").append("'%").append(worksCName).append("%'");
      }

      String seriesId = parameters.get("seriesId");
      if (seriesId != null && !seriesId.isEmpty()) {
         if (tmp.length() > 0) tmp.append(" AND ");
         tmp.append("(").append(" aw.works_series = ").append(seriesId).append(" OR ").append(" aw.works_series IS NULL ").append(" OR ")
               .append(" aw.works_series = '' ").append(" AND ").append(" aw.id NOT IN ").append("(")
               .append("SELECT aawsr.works_id FROM art_artist_works_series_research aawsr WHERE aawsr.works_id = aw.id").append(")").append(")");
      }

      if (tmp.length() > 0) parameters.put("paras", tmp.toString());
   }

   public List<ArtArtistWorksSeries> findByArtArtistId(Serializable id) throws ServiceException {
      try {
         return artArtistWorksSeriesDao.findByField(ArtArtistWorksSeries.PROP_ARTIST_ID, id);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public List<ArtArtistWorksSeries> findAll() throws ServiceException {
      try {
         return artArtistWorksSeriesDao.findAll();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public String importFile(String path, int artistId) throws ServiceException {
      String message = "";
      try {
         FileInputStream fileInputStream = new FileInputStream(path);
         HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
         HSSFSheet sht = wb.getSheetAt(0);
         if (sht != null) {
            for (int rowNum = 2; rowNum < sht.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = sht.getRow(rowNum);
               if (hssfRow == null) continue;
               if (hssfRow.getCell(0) == null) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               if (("").equals(getValue(hssfRow.getCell(0)))) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               ArtArtistWorksSeries artArtistWorksSeries = new ArtArtistWorksSeries();
               String str = "";
               artArtistWorksSeries.setArtistId(artistId);
               str = getValue(hssfRow.getCell(0));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到系列名称";
                  break;
               }
               artArtistWorksSeries.setSeriesName(str);
               createArtArtistWorksSeries(artArtistWorksSeries);
            }
         }
         fileInputStream.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      File file = new File(path);
      if (file.exists() && file.isFile()) {
         file.delete();
      }
      return message;
   }

   private String getValue(HSSFCell hssfCell) {
      if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
         // 返回布尔类型的值
         return String.valueOf(hssfCell.getBooleanCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
         // 返回数值类型的值
         DecimalFormat df = new DecimalFormat("0");
         return df.format(hssfCell.getNumericCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
         // 返回数值类型的值
         return String.valueOf(hssfCell.getNumericCellValue());
      }
      else {
         // 返回字符串类型的值
         return String.valueOf(hssfCell.getStringCellValue());
      }
   }

   @Resource
   private ArtArtistWorksSeriesDao artArtistWorksSeriesDao;

   public void setArtArtistWorksSeriesDao(ArtArtistWorksSeriesDao artArtistWorksSeriesDao) {
      this.artArtistWorksSeriesDao = artArtistWorksSeriesDao;
   }
}
