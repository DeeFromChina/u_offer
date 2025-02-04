package com.golead.art.activity.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;
import com.golead.art.activity.dao.ArtActivityExhibitArtistDao;
import com.golead.art.activity.dao.ArtActivityExhibitDao;
import com.golead.art.activity.model.ArtActivityExhibit;
import com.golead.art.activity.model.ArtActivityExhibitArtist;
import com.golead.art.activity.service.ArtActivityExhibitService;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.works.dao.ArtWorksExhibitDao;
import com.golead.art.works.model.ArtWorksExhibit;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtActivityExhibitServiceImpl extends BaseServiceImpl implements ArtActivityExhibitService {
   private static final long serialVersionUID = 1L;

   public ArtActivityExhibit getArtActivityExhibit(Serializable id) throws ServiceException {
      try {
         return artActivityExhibitDao.get(id);
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

   public int createArtActivityExhibit(Map<String, String> artActivityExhibitMap) throws ServiceException {
      try {
         ArtActivityExhibit artActivityExhibit = new ArtActivityExhibit();
         ConvertUtil.mapToObject(artActivityExhibit, artActivityExhibitMap, false);
         if ("person".equals(artActivityExhibitMap.get("type"))) {
            artActivityExhibit.setExhibType("1");
         }
         if ("people".equals(artActivityExhibitMap.get("type"))) {
            artActivityExhibit.setExhibType("2");
         }
         artActivityExhibitDao.save(artActivityExhibit);
         if (artActivityExhibitMap.get("artistId") != null && !"".equals(artActivityExhibitMap.get("artistId").trim())) {
            ArtActivityExhibitArtist artActivityExhibitArtist = new ArtActivityExhibitArtist();
            ConvertUtil.mapToObject(artActivityExhibitArtist, artActivityExhibitMap, false);
            artActivityExhibitArtist.setExhibitId(artActivityExhibit.getId());
            artActivityExhibitArtistDao.save(artActivityExhibitArtist);
         }
         return artActivityExhibit.getId();
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

   public void updateArtActivityExhibit(Map<String, String> artActivityExhibitMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artActivityExhibitMap.get(ArtActivityExhibit.PROP_ID));
         ArtActivityExhibit tmp = artActivityExhibitDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artActivityExhibitMap, true);
         List<ArtActivityExhibitArtist> artActivityExhibitArtists = artActivityExhibitArtistDao.findByField(ArtActivityExhibitArtist.PROP_EXHIBIT_ID, pkId);
         ArtActivityExhibitArtist artActivityExhibitArtist = new ArtActivityExhibitArtist();
         if (artActivityExhibitArtists != null && artActivityExhibitArtists.size() > 0) {
            if (artActivityExhibitMap.get(ArtActivityExhibitArtist.PROP_ARTIST_ID) != null) {
               artActivityExhibitArtist = artActivityExhibitArtists.get(0);
               artActivityExhibitArtist.setArtistId(Integer.valueOf(artActivityExhibitMap.get(ArtActivityExhibitArtist.PROP_ARTIST_ID)));
               artActivityExhibitArtistDao.update(artActivityExhibitArtist);
            }
         }
         else {
            artActivityExhibitArtist.setExhibitId(pkId);
            if (artActivityExhibitMap.get(ArtActivityExhibitArtist.PROP_ARTIST_ID) != null
                  && !"".equals(artActivityExhibitMap.get(ArtActivityExhibitArtist.PROP_ARTIST_ID).trim())) {
               artActivityExhibitArtist.setArtistId(Integer.valueOf(artActivityExhibitMap.get(ArtActivityExhibitArtist.PROP_ARTIST_ID)));
               artActivityExhibitArtistDao.save(artActivityExhibitArtist);
            }
         }
         artActivityExhibitDao.update(tmp);
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

   public void deleteArtActivityExhibit(Serializable id) throws ServiceException {
      try {
         artActivityExhibitDao.delete(id);
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

   public void deleteArtActivityExhibits(Serializable[] ids) throws ServiceException {
      try {
         for (Serializable id : ids) {
            artActivityExhibitArtistDao.deleteByField(ArtActivityExhibitArtist.PROP_EXHIBIT_ID, id);
            artWorksExhibitDao.deleteByField(ArtWorksExhibit.PROP_EXHIBIT_ID, id);
         }
         artActivityExhibitDao.deleteAll(ids);
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

   public PageQuery queryArtActivityExhibit(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artActivityExhibitList", pageQuery);
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

   public PageQuery queryArtActivityExhibit2(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artActivityExhibitList2", pageQuery);
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
      String paras = " 1=1 ";
      Map<String, String> map = page.getParameters();

      String exhibitName = map.get("exhibitName");
      if (exhibitName != null && exhibitName.trim().length() > 0) {
         paras += " AND exhibit.exhibit_name like '%" + exhibitName.trim() + "%'";
      }

      String artAgencyName = map.get("artAgencyName");
      if (artAgencyName != null && artAgencyName.trim().length() > 0) {
         paras += " AND agency.agency_c_name like '%" + artAgencyName.trim() + "%'";
      }

      String countryName = map.get("countryName");
      if (countryName != null && countryName.trim().length() > 0) {
         paras += " AND country.country_name like '%" + countryName.trim() + "%'";
      }

      String artAgencyId = map.get("artAgencyId");
      if (artAgencyId != null && artAgencyId.trim().length() > 0) {
         paras += " AND exhibit.exhibitors=" + artAgencyId.trim();
      }

      String artArtistId = map.get("artArtistId");
      if (artArtistId != null && artArtistId.trim().length() > 0) {
         paras += " AND exhibit_artist.artist_id=" + artArtistId.trim();
      }

      String countryId = map.get("countryId");
      if (countryId != null && countryId.trim().length() > 0) {
         paras += " AND exhibit.country_id=" + countryId.trim();
      }

      String artWorksId = map.get("artWorksId");
      if (artWorksId != null && artWorksId.trim().length() > 0) {
         paras += " AND works_exhibit.works_id=" + artWorksId.trim();
      }

      String exhibType = map.get("exhibType");
      if (exhibType != null && exhibType.trim().length() > 0) {
         paras += " AND exhibit.exhib_Type='" + exhibType.trim() + "'";
      }

      //      String groupby = map.get("groupby");
      //      if (groupby != null && groupby.trim().length() > 0) {
      //         paras += " GROUP BY exhibit.id";
      //      }

      String cName = map.get("cName");
      if (cName != null && cName.trim().length() > 0) {
         paras += " AND t.cName LIKE '%" + cName + "%'";
      }

      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }

   public int findByCountry(String ids) throws ServiceException {
      try {
         String str = "SELECT COUNT(*) FROM art_activity_exhibit exhibit WHERE exhibit.country_id IN (" + ids + ")";
         return jdbcDao.queryIntBySql(str);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public int findByAgency(String ids) throws ServiceException {
      try {
         String str = "SELECT COUNT(*) FROM art_activity_exhibit exhibit WHERE exhibit.exhibitors IN (" + ids + ")";
         return jdbcDao.queryIntBySql(str);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @SuppressWarnings("deprecation")
   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException {
      try {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
         HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
         HSSFRow row = sheet.createRow((int) 0);

         HSSFCell cell = row.createCell((short) 0);
         cell.setCellValue("展览名");
         cell = row.createCell((short) 1);
         cell.setCellValue("展览机构名");
         cell = row.createCell((short) 2);
         cell.setCellValue("艺术家");
         cell = row.createCell((short) 3);
         cell.setCellValue("策展人");
         cell = row.createCell((short) 4);
         cell.setCellValue("年");
         cell = row.createCell((short) 5);
         cell.setCellValue("月");
         cell = row.createCell((short) 6);
         cell.setCellValue("国家");
         cell = row.createCell((short) 7);
         cell.setCellValue("城市");
         pageQuery.setPageSize("-1");
         PageQuery pageQuery2 = queryArtActivityExhibit2(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            String country = returnString(item.get("country_id"));
            if (!"".equals(country)) {
               country = findCodeName(form, "COUNTRY", returnString(item.get("country_id")));
            }
            String exhibitors = returnString(item.get("exhibitors"));
            if (!"".equals(exhibitors)) {
               exhibitors = findCodeName(form, "ARTAGENCY", returnString(item.get("exhibitors")));
            }

            row = sheet.createRow(i + 1);
            row.createCell((short) 0).setCellValue(returnString(item.get("exhibit_name")));
            row.createCell((short) 1).setCellValue(exhibitors);
            row.createCell((short) 2).setCellValue(returnString(item.get("GROUP_CONCAT(a.c_name)")));
            row.createCell((short) 3).setCellValue(returnString(item.get("curator")));
            row.createCell((short) 4).setCellValue(returnString(item.get("activity_year")));
            row.createCell((short) 5).setCellValue(returnString(item.get("activity_month")));
            row.createCell((short) 6).setCellValue(country);
            row.createCell((short) 7).setCellValue(returnString(item.get("city")));
            i++;
         }
         return hssfWorkbook;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   private String findCodeName(Object objForm, String codeNo, Serializable value) {
      BaseForm bf = (BaseForm) objForm;

      List<Code> list = bf.getCodeSets().get(codeNo);
      if (value == null) return "";
      if (list == null) return "";
      for (int i = 0; i < list.size(); i++) {
         Code code = list.get(i);
         String v;
         if (value instanceof String) v = ((String) value).trim();
         else v = value.toString();
         if (v.equals(code.getValue())) return code.getCodeName();
      }
      return "";
   }

   @Resource
   private ArtActivityExhibitDao       artActivityExhibitDao;

   @Resource
   private ArtActivityExhibitArtistDao artActivityExhibitArtistDao;

   @Resource
   private ArtWorksExhibitDao          artWorksExhibitDao;

   public void setArtActivityExhibitDao(ArtActivityExhibitDao artActivityExhibitDao) {
      this.artActivityExhibitDao = artActivityExhibitDao;
   }
}
