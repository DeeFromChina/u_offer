package com.golead.art.app.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.golead.art.app.service.ScreenFristService;
import com.golead.art.artist.dao.ArtArtistDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.works.dao.ArtMediumDao;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksYear;
import com.golead.common.service.system.SysCodeService;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;

@Service
public class ScreenFristServiceImpl extends BaseServiceImpl implements ScreenFristService {

   private DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###.##");

   @Override
   public List<Map<String, Object>> selectScreen(Map<String, String> parameters) throws ServiceException {
      try {
         String pageName = parameters.get("pageName");
         if (pageName.equalsIgnoreCase("screen")) return screen(parameters);// 筛选
         else if (pageName.equalsIgnoreCase("screenNum")) return screenNum(parameters);// 筛选数量
         else if (pageName.equalsIgnoreCase("screenWorks")) return screenWorks(parameters);// 筛选作品
         else if (pageName.equalsIgnoreCase("series")) return series(parameters);// 作品系列
         else if (pageName.equalsIgnoreCase("oldyear")) return year(parameters);// 作品年代
         else if (pageName.equalsIgnoreCase("selectYear")) return selectYear(parameters);// 作品选择年代
         else if (pageName.equalsIgnoreCase("yearNum")) return yearNum(parameters);// 作品选择年代数量
      }
      catch (ServiceException e) {
         e.printStackTrace();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   //筛选
   private List<Map<String, Object>> screen(Map<String, String> parameters) throws ServiceException {
      System.out.println("screen...");
      try {
         String artistId = parameters.get("artistId");
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         justSql(list, artistId);
         sqlByWorks(list, artistId);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private void putScreen(List<Map<String, Object>> inList, List<Map<String, Object>> outList, String className) {
      if (outList != null && outList.size() > 0) {
         Map<String, Object> map = new HashMap<String, Object>();
         map.put(className, outList);
         inList.add(map);
      }
   }

   private void justSql(List<Map<String, Object>> list, String artistId) {
      String sql = "SELECT series.id,series.series_name AS name " + " FROM art_artist_works_series series WHERE series.artist_id=" + artistId
            + " ORDER BY series.series_important DESC ";
      putScreen(list, jdbcDao.queryBySql(sql), "series");
      sql = "SELECT year.id,CONCAT(year.start_year,'~',year.end_year) AS name " + " FROM art_works_year year WHERE year.artist_id=" + artistId
            + " ORDER BY year.year_important DESC ";
      List<Map<String, Object>> yearList = jdbcDao.queryBySql(sql);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("id", "-1");
      map.put("name", "未知");
      yearList.add(map);
      putScreen(list, yearList, "year");
      sql = "SELECT medium.id,medium.medium_name AS name " + " FROM art_medium medium " + " WHERE medium.id IN "
            + " (SELECT works.medium_material AS mediumId FROM art_works works " + " WHERE works.artist_id=" + artistId + ") ";
      putScreen(list, jdbcDao.queryBySql(sql), "material");
      sql = "SELECT medium.id,medium.medium_name AS name " + " FROM art_medium medium " + " WHERE medium.id IN "
            + " (SELECT works.medium_shape AS mediumId FROM art_works works " + " WHERE works.artist_id=" + artistId + ") ";
      putScreen(list, jdbcDao.queryBySql(sql), "shape");
   }
   
   private void sqlByWorks(List<Map<String, Object>> list, String artistId){
      String sql = "";
      List<ArtWorks> artWorks = artWorksDao.findByField(ArtWorks.PROP_ARTIST_ID, Integer.valueOf(artistId));
      String styleId = "";
      String theme = "";
      String social = "";
      String createStyle = "";
      if (artWorks != null && artWorks.size() > 0) {
         for (ArtWorks works : artWorks) {
            if (works.getStyleType() != null && !"".equals(works.getStyleType())) {
               String style = returnString(works.getStyleType());
               String[] styles = style.split(";");
               for (int i = 0; i < styles.length; i++) {
                  if (!"".equals(styles[i].trim())) {
                     if (!"".equals(styleId)) {
                        styleId += ",";
                     }
                     styleId += styles[i].trim();
                  }
               }
            }
            if (works.getWorksTheme1() != null && !"".equals(works.getWorksTheme1().trim())) {
               if (!"".equals(theme)) {
                  theme += ",";
               }
               theme += works.getWorksTheme1().trim();
            }
            if (works.getWorksTheme2() != null && !"".equals(works.getWorksTheme2().trim())) {
               if (!"".equals(theme)) {
                  theme += ",";
               }
               theme += works.getWorksTheme2().trim();
            }
            if (works.getWorksTheme3() != null && !"".equals(works.getWorksTheme3().trim())) {
               if (!"".equals(theme)) {
                  theme += ",";
               }
               theme += works.getWorksTheme3().trim();
            }
            if (works.getSocialFunction() != null && !"".equals(works.getSocialFunction().trim())) {
               social = works.getSocialFunction().trim();
            }
            if (works.getWorksWriting() != null && !"".equals(works.getWorksWriting().trim())) {
               createStyle = works.getWorksWriting().trim();
            }
         }
      }
      if (!"".equals(styleId)) {
         sql = "SELECT style.id,style.style_name AS name " + " FROM art_works_style style WHERE style.id IN (" + styleId + ")";
         putScreen(list, jdbcDao.queryBySql(sql), "style");
      }
      if (!"".equals(theme)) {
         sql = "SELECT theme.id,theme.theme_name AS name " + " FROM art_works_theme theme WHERE theme.id IN (" + theme + ")";
         putScreen(list, jdbcDao.queryBySql(sql), "theme");
      }
      if (!"".equals(social)) {
         sql = "SELECT code.ITEM_VALUE AS id,code.ITEM_NAME AS name " + " FROM sys_code code WHERE code.CS_ID=6 " + " AND code.ITEM_VALUE IN (" + social
               + ")";
         putScreen(list, jdbcDao.queryBySql(sql), "social");
      }
      if (!"".equals(createStyle)) {
         sql = "SELECT code.ITEM_VALUE AS id,code.ITEM_NAME AS name " + " FROM sys_code code WHERE code.CS_ID=32 " + " AND code.ITEM_VALUE IN ("
               + createStyle + ")";
         putScreen(list, jdbcDao.queryBySql(sql), "createStyle");
      }
   }
   
   //筛选数量
   private List<Map<String, Object>> screenNum(Map<String, String> parameters) throws ServiceException {
      System.out.println("screenNum...");
      try {
         String artistId = parameters.get("artistId");
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         String id = parameters.get("id");
         if ("".equals(returnString(parameters.get("id")))) { throw new ServiceException("筛选id错误！"); }
         String type = parameters.get("type");
         if ("".equals(returnString(parameters.get("type")))) { throw new ServiceException("筛选类型错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         int number = 0;
         if("series".equalsIgnoreCase(type)){
            String condition = "WHERE works.artist_id=" + artistId + " AND works.works_series IN (" + id + ")";
            number = returnScreenNum(condition);
         }
         else if("year".equalsIgnoreCase(type)){
            String sql = "SELECT y.id,y.start_year AS sy,y.end_year AS ey FROM art_works_year y "
                  + " LEFT JOIN art_works_year_research research ON research.year_id = y.id "
                  + " WHERE research.year_id IN (" + id + ") GROUP BY y.id ";
            List<Map<String, Object>> yearlist = jdbcDao.queryBySql(sql);
            String workssql = "SELECT COUNT(*) FROM art_works works ";
            String condition = " WHERE ";
            for(Map<String, Object> yearMap : yearlist){
               if(!" WHERE ".equals(condition)){
                  condition+=" OR ";
               }
               condition+= "(works.create_year>=" + yearMap.get("sy") + " AND " + "works.create_year<=" + yearMap.get("ey") + ")";
            }
            if(!" WHERE ".equals(condition)){
               number = jdbcDao.queryIntBySql(workssql + condition);
            }
            if(id.indexOf("-1") >= 0){
               String nullSql = "SELECT COUNT(*) FROM art_works works WHERE LENGTH(works.create_year) > 0 AND works.artist_id=" + artistId;
               number+= jdbcDao.queryIntBySql(nullSql);
            }
         }
         else if("material".equalsIgnoreCase(type)){
            String[] ids = returnString(id).split(",");
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) FROM art_works works WHERE works.artist_id=");
            sql.append(artistId);
            sql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               sql.append(" FIND_IN_SET(" + ids[i] + ", works.medium_material)");
               if(i+1 != ids.length){
                  sql.append(" OR ");
               }
            }
            sql.append(")");
            number = jdbcDao.queryIntBySql(sql.toString());
         }
         else if("shape".equalsIgnoreCase(type)){
            String[] ids = returnString(id).split(",");
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) FROM art_works works WHERE works.artist_id=");
            sql.append(artistId);
            sql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               sql.append(" FIND_IN_SET(" + ids[i] + ", works.medium_shape)");
               if(i+1 != ids.length){
                  sql.append(" OR ");
               }
            }
            sql.append(")");
            number = jdbcDao.queryIntBySql(sql.toString());
         }
         else if("style".equalsIgnoreCase(type)){
            String[] ids = returnString(id).split(",");
            Map<String, String> idMap = new HashMap<String, String>();
            for(int i = 0; i < ids.length; i++){
               if(!"".equals(ids[i].trim())){
                  idMap.put(ids[i].trim(), "");
               }
            }
            List<ArtWorks> artWorks = artWorksDao.findByField(ArtWorks.PROP_ARTIST_ID, Integer.valueOf(artistId));
            for(ArtWorks works : artWorks){
               String styleType = returnString(works.getStyleType());
               String[] styleTypes = styleType.split(";");
               for(int i = 0; i < styleTypes.length; i++){
                  if(idMap.get(styleTypes[i]) != null){
                     number++;
                     break;
                  }
               }
            }
         }
         else if("theme".equalsIgnoreCase(type)){
            String condition = "WHERE works.artist_id=" + artistId
                  + " AND (works.works_theme1=" + id
                  + " OR works.works_theme2=" + id
                  + " OR works.works_theme3=" + id + ")";
            number = returnScreenNum(condition);
         }
         else if("social".equalsIgnoreCase(type)){
            String[] ids = returnString(id).split(",");
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) FROM art_works works WHERE works.artist_id=");
            sql.append(artistId);
            sql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               sql.append(" FIND_IN_SET(" + ids[i] + ", works.social_function)");
               if(i+1 != ids.length){
                  sql.append(" OR ");
               }
            }
            sql.append(")");
            number = jdbcDao.queryIntBySql(sql.toString());
         }
         else if("createStyle".equalsIgnoreCase(type)){
            String[] ids = returnString(id).split(",");
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) FROM art_works works WHERE works.artist_id=");
            sql.append(artistId);
            sql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               sql.append(" FIND_IN_SET(" + ids[i] + ", works.works_writing)");
               if(i+1 != ids.length){
                  sql.append(" OR ");
               }
            }
            sql.append(")");
            number = jdbcDao.queryIntBySql(sql.toString());
         }
         map.put("number", number);
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private int returnScreenNum(String condition){
      try {
         String sql = "SELECT COUNT(*) FROM art_works works "
               + condition;
         return jdbcDao.queryIntBySql(sql);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return 0;
   }
   
   //筛选作品
   private List<Map<String, Object>> screenWorks(Map<String, String> parameters) throws ServiceException {
      System.out.println("screenWorks...");
      try {
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/");
         String artistId = parameters.get("artistId");
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         String id = parameters.get("id");
         if ("".equals(returnString(parameters.get("id")))) { throw new ServiceException("筛选id错误！"); }
         String type = parameters.get("type");
         if ("".equals(returnString(parameters.get("type")))) { throw new ServiceException("筛选类型错误！"); }
         int pageNum = 1;
         if (isInteger(returnString(parameters.get("pageNum")))) {
            pageNum = Integer.valueOf(returnString(parameters.get("pageNum")));
         }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//         Map<String, Object> map = new HashMap<String, Object>();
         ArtArtist artArtist = artArtistDao.get(Integer.valueOf(artistId));
         if (artArtist == null) { throw new ServiceException("艺术家id错误！"); }
         if("series".equalsIgnoreCase(type)){
            String hql = "FROM ArtWorks works ";
            String condition = " WHERE works.artistId=" + artistId + " AND works.worksSeries IN (" + id + ")";
            List<ArtWorks> artWorks = artWorksDao.findByHql(hql+condition);
            putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
         }
         else if("year".equalsIgnoreCase(type)){
            String sql = "SELECT year.id,year.start_year sy,year.end_year ey FROM art_works_year year "
                  + " LEFT JOIN art_works_year_research research ON research.year_id=year.id "
                  + " WHERE year.id IN(" + id + ")"
                        + " GROUP BY year.id ";
            List<Map<String, Object>> yearList = jdbcDao.queryBySql(sql);
            String hql = "SELECT works.id FROM art_works works ";
            String condition = " WHERE ";
            int a = id.indexOf("-1");
            if(a >= 0){
               condition+=" (ISNULL(works.create_year) OR works.create_year = '') ";
            }
            for(Map<String, Object> yearMap : yearList){
               if(!" WHERE ".equals(condition)){
                  condition+=" OR ";
               }
               condition+= "(cast(works.create_year as unsigned)>="+yearMap.get("sy")+" AND "
                     +"cast(works.create_year as unsigned)<="+yearMap.get("ey")+")";
            }
            hql+= condition;
            String ids = "";
            List<Map<String, Object>> worksList = jdbcDao.queryBySql(hql);
            for(Map<String, Object> map : worksList){
               if(!"".equals(ids)){ids+=",";}
               ids+=map.get("id");
            }
            String ppql = "FROM ArtWorks works WHERE works.id IN(" + ids + ")";
            if(!"".equals(ids)){
               List<ArtWorks> artWorks = artWorksDao.findByHql(ppql);
               putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
            }else{
               list = null;
            }
         }
         else if("material".equalsIgnoreCase(type)){
            String[] ids = id.split(",");
            StringBuffer hql = new StringBuffer();
            hql.append("SELECT works.id FROM art_works works WHERE works.artist_Id=" + artistId);
            hql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               hql.append(" FIND_IN_SET(" + ids[i] + ", works.medium_material)");
               if(i+1 != ids.length){
                  hql.append(" OR ");
               }
            }
            hql.append(")");
            List<Map<String, Object>> worksList = jdbcDao.queryBySql(hql.toString());
            String worksIds = "";
            for(Map<String, Object> worksMap : worksList){
               if(!"".equals(worksIds)){
                  worksIds+=",";
               }
               worksIds+=worksMap.get("id");
            }
            if(!"".equals(worksIds)){
               String sql = "FROM ArtWorks works WHERE works.id IN(" + worksIds + ")";
               List<ArtWorks> artWorks = artWorksDao.findByHql(sql);
               putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
            }
         }
         else if("shape".equalsIgnoreCase(type)){
            String[] ids = id.split(",");
            StringBuffer hql = new StringBuffer();
            hql.append("SELECT works.id FROM art_works works WHERE works.artist_Id=" + artistId);
            hql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               hql.append(" FIND_IN_SET(" + ids[i] + ", works.medium_shape)");
               if(i+1 != ids.length){
                  hql.append(" OR ");
               }
            }
            hql.append(")");
            List<Map<String, Object>> worksList = jdbcDao.queryBySql(hql.toString());
            String worksIds = "";
            for(Map<String, Object> worksMap : worksList){
               if(!"".equals(worksIds)){
                  worksIds+=",";
               }
               worksIds+=worksMap.get("id");
            }
            if(!"".equals(worksIds)){
               String sql = "FROM ArtWorks works WHERE works.id IN(" + worksIds + ")";
               List<ArtWorks> artWorks = artWorksDao.findByHql(sql);
               putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
            }
//            List<ArtWorks> artWorks = artWorksDao.findByHql(hql.toString());
//            putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
         }
         else if("style".equalsIgnoreCase(type)){
            String[] ids = returnString(id).split(",");
            Map<String, String> idMap = new HashMap<String, String>();
            for(int i = 0; i < ids.length; i++){
               if(!"".equals(ids[i].trim())){
                  idMap.put(ids[i].trim(), "");
               }
            }
            List<ArtWorks> artWorks = artWorksDao.findByField(ArtWorks.PROP_ARTIST_ID, Integer.valueOf(artistId));
            int j = 1;
            for(ArtWorks works : artWorks){
               String styleType = returnString(works.getStyleType());
               String[] styleTypes = styleType.split(";");
               for(int i = 0; i < styleTypes.length; i++){
                  if(idMap.get(styleTypes[i]) != null){
                     String message = putYear(works, j, list, 10, pageNum, artArtist, FILE_PATH, "1");
                     if (message == null) {
                        throw new ServiceException("数据错误！");
                     }
                     else if ("continue".equals(message)) {
                        j++;
                        continue;
                     }
                     else if ("break".equals(message)) {
                        break;
                     }
                     j++;
                     break;
                  }
               }
            }
         }
         else if("theme".equalsIgnoreCase(type)){
            String hql = "FROM ArtWorks works ";
            String condition = "WHERE works.artistId=" + artistId
                  + " AND (works.worksTheme1=" + id
                  + " OR works.worksTheme2=" + id
                  + " OR works.worksTheme3=" + id + ")";
            List<ArtWorks> artWorks = artWorksDao.findByHql(hql + condition);
            putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
         }
         else if("social".equalsIgnoreCase(type)){
            String[] ids = id.split(",");
            StringBuffer hql = new StringBuffer();
            hql.append("SELECT works.id FROM art_works works WHERE works.artist_Id=" + artistId);
            hql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               hql.append(" FIND_IN_SET(" + ids[i] + ", works.social_function)");
               if(i+1 != ids.length){
                  hql.append(" OR ");
               }
            }
            hql.append(")");
            List<Map<String, Object>> worksList = jdbcDao.queryBySql(hql.toString());
            String worksIds = "";
            for(Map<String, Object> worksMap : worksList){
               if(!"".equals(worksIds)){
                  worksIds+=",";
               }
               worksIds+=worksMap.get("id");
            }
            if(!"".equals(worksIds)){
               String sql = "FROM ArtWorks works WHERE works.id IN(" + worksIds + ")";
               List<ArtWorks> artWorks = artWorksDao.findByHql(sql);
               putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
            }
//            List<ArtWorks> artWorks = artWorksDao.findByHql(hql.toString());
//            putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
         }
         else if("createStyle".equalsIgnoreCase(type)){
            String[] ids = id.split(",");
            StringBuffer hql = new StringBuffer();
            hql.append("SELECT works.id FROM art_works works WHERE works.artist_Id=" + artistId);
            hql.append(" AND (");
            for(int i = 0; i < ids.length; i++){
               hql.append(" FIND_IN_SET(" + ids[i] + ", works.works_writing)");
               if(i+1 != ids.length){
                  hql.append(" OR ");
               }
            }
            hql.append(")");
            List<Map<String, Object>> worksList = jdbcDao.queryBySql(hql.toString());
            String worksIds = "";
            for(Map<String, Object> worksMap : worksList){
               if(!"".equals(worksIds)){
                  worksIds+=",";
               }
               worksIds+=worksMap.get("id");
            }
            if(!"".equals(worksIds)){
               String sql = "FROM ArtWorks works WHERE works.id IN(" + worksIds + ")";
               List<ArtWorks> artWorks = artWorksDao.findByHql(sql);
               putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
            }
//            List<ArtWorks> artWorks = artWorksDao.findByHql(hql.toString());
//            putWorks(artWorks, list, pageNum, artArtist, FILE_PATH);
         }
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private void putWorks(List<ArtWorks> artWorks, List<Map<String, Object>> list, int pageNum, ArtArtist artArtist, String FILE_PATH){
      int i = 1;
      for(ArtWorks works : artWorks){
         String message = putYear(works, i, list, 10, pageNum, artArtist, FILE_PATH, "1");
         if (message == null) {
            throw new ServiceException("数据错误！");
         }
         else if ("continue".equals(message)) {
            i++;
            continue;
         }
         else if ("break".equals(message)) {
            break;
         }
         i++;
      }
   }

   //作品系列
   private List<Map<String, Object>> series(Map<String, String> parameters) throws ServiceException {
      System.out.println("series...");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         artistSeries(list, parameters);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   //作品年代
   private List<Map<String, Object>> year(Map<String, String> parameters) throws ServiceException {
      System.out.println("year...");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         artistYear(list, parameters);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   //作品选择年代
   private List<Map<String, Object>> selectYear(Map<String, String> parameters) throws ServiceException {
      System.out.println("selectYear...");
      try {
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/");
         int pageNum = 1;
         if (isInteger(returnString(parameters.get("pageNum")))) {
            pageNum = Integer.valueOf(returnString(parameters.get("pageNum")));
         }
         String artistId = parameters.get("artistId");
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         ArtArtist artArtist = artArtistDao.get(Integer.valueOf(artistId));
         if (artArtist == null) { throw new ServiceException("艺术家id错误！"); }
         if (!isInteger(returnString(parameters.get("startYear")))) { throw new ServiceException("开始年代错误！"); }
         ;
         int startYear = Integer.valueOf(returnString(parameters.get("startYear")));
         int endYear = 0;
         if (parameters.get("endYear") != null) {
            endYear = Integer.valueOf(returnString(parameters.get("endYear")));
            if (!isInteger(returnString(parameters.get("endYear")))) {
               endYear = startYear;
            }
         }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         String hql = "FROM ArtWorks works WHERE works.artistId=" + Integer.valueOf(artistId) + " ORDER BY works.id ASC";
         List<ArtWorks> artWorks = artWorksDao.findByHql(hql);
         int pageSize = 10;
         int i = 1;
         for (ArtWorks works : artWorks) {
            if (startYear < 0 && (works.getCreateYear() == null || "".equals(works.getCreateYear()))) {
               String message = putYear(works, i, list, pageSize, pageNum, artArtist, FILE_PATH, "0");
               if (message == null) {
                  throw new ServiceException("数据错误！");
               }
               else if ("continue".equals(message)) {
                  i++;
                  continue;
               }
               else if ("break".equals(message)) {
                  break;
               }
               i++;
            }
            else if (startYear <= returnInteger(works.getCreateYear()) && returnInteger(works.getCreateYear()) <= endYear) {
               String message = putYear(works, i, list, pageSize, pageNum, artArtist, FILE_PATH, "1");
               if (message == null) {
                  throw new ServiceException("数据错误！");
               }
               else if ("continue".equals(message)) {
                  i++;
                  continue;
               }
               else if ("break".equals(message)) {
                  break;
               }
               i++;
            }
         }
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private String putYear(ArtWorks works, int i, List<Map<String, Object>> list, int pageSize, int pageNum, ArtArtist artArtist, String FILE_PATH, String type) {
      try {
         if (i < (pageSize * (pageNum - 1) + 1)) {
            i++;
            return "continue";
         }
         if (i > (pageSize * pageNum)) { return "break"; }
         if (works.getCreateYear() == null) {
            i++;
            return "continue";
         }
//         if (!"0".equals(type)) {
//            if (!isInteger(works.getCreateYear())) {
//               i++;
//               return "continue";
//            }
//         }
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("name", returnString(works.getWorksCName()) + " " + returnString(works.getWorksEName()));
         map.put("worksId", works.getId());
         map.put("createYear", works.getCreateYear());
         List<ArtMedium> artMediums = artMediumDao.findAll();
         Map<String, Object> mediumMap = new HashMap<String, Object>();
         for (ArtMedium medium : artMediums) {
            mediumMap.put(medium.getId().toString(), medium.getMediumName());
         }
         String medium = mediumString(works.getMediumMaterial(), mediumMap) + mediumString(works.getMediumShape(), mediumMap);
         map.put("medium", medium);

         String sizeCm = "";
         sizeCm += decimalFormatString(works.getSizeCmLength());
         sizeCm = "".equals(sizeCm.trim()) ? "" : (sizeCm + "X" + decimalFormatString(works.getSizeCmWidth()));
         sizeCm = sizeCm.endsWith("X") ? sizeCm + decimalFormatString(works.getSizeCmHeight()) : ("".equals(sizeCm) ? decimalFormatString(works
               .getSizeCmHeight()) : sizeCm + "X" + decimalFormatString(works.getSizeCmHeight()));
         if (sizeCm.endsWith("X")) {
            sizeCm = sizeCm.substring(0, sizeCm.length() - 1);
         }
         if (!"".equals(sizeCm)) {
            map.put("size", sizeCm + "cm");
         }
         String sizeIn = "";
         sizeIn += decimalFormatString(works.getSizeInLength());
         sizeIn = "".equals(sizeIn.trim()) ? "" : (sizeIn + "X" + decimalFormatString(works.getSizeInWidth()));
         sizeIn = sizeIn.endsWith("X") ? sizeIn + decimalFormatString(works.getSizeInHeight()) : ("".equals(sizeIn) ? decimalFormatString(works
               .getSizeInHeight()) : sizeIn + "X" + decimalFormatString(works.getSizeInHeight()));
         if (sizeIn.endsWith("X")) {
            sizeIn = sizeIn.substring(0, sizeIn.length() - 1);
         }
         if (!"".equals(sizeIn)) {
            map.put("size", sizeIn + "In");
         }
         String sizeRule = "";
         sizeRule += decimalFormatString(works.getSizeRuleLength());
         sizeRule = "".equals(sizeRule.trim()) ? "" : (sizeRule + "X" + decimalFormatString(works.getSizeRuleWidth()));
         sizeRule = sizeRule.endsWith("X") ? sizeRule + decimalFormatString(works.getSizeRuleHeight()) : ("".equals(sizeRule) ? decimalFormatString(works
               .getSizeRuleHeight()) : sizeCm + "X" + decimalFormatString(works.getSizeRuleHeight()));
         if (sizeRule.endsWith("X")) {
            sizeRule = sizeRule.substring(0, sizeRule.length() - 1);
         }
         if (!"".equals(sizeRule)) {
            map.put("size", sizeRule + "寸");
         }

         map.put("descript", works.getWorksStatus());
         String photo = works.getThumbnail();
         if (photo == null) {
            map.put("photo", null);
         }
         else {
            String appPath = "auction/" + artArtist.getFolderName() + "/" + works.getThumbnail();
            String path = FILE_PATH + File.separator + appPath;
            File file = new File(path);
            if (file.exists() && file.isFile()) {
               BufferedImage bufferedImage = ImageIO.read(file);
               int width = bufferedImage.getWidth();
               int height = bufferedImage.getHeight();
               map.put("width", width);
               map.put("height", height);
               map.put("photo", appPath);
            }
            else {
               map.put("width", "50");
               map.put("height", "50");
               map.put("photo", null);
            }
         }
         list.add(map);
         return "success";
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private String mediumString(String str, Map<String, Object> mediumMap){
      if(str == null || "".equals(str)){
         return "";
      }
      String medium = "";
      String[] strs = str.split(",");
      for(int i = 0; i < strs.length; i++){
         if(!"".equals(strs[i])){
            medium+=mediumMap.get(strs[i]);
         }
      }
      return medium;
   }

   private String decimalFormatString(Object object) {
      String str = object == null ? "" : object.toString().trim();
      if (!"".equals(str)) { return decimalFormat.format(Double.valueOf(str)); }
      return "";
   }

   //作品年代数量
   private List<Map<String, Object>> yearNum(Map<String, String> parameters) throws ServiceException {
      System.out.println("yearNum...");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         String artistId = parameters.get("artistId");
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         ArtArtist artArtist = artArtistDao.get(Integer.valueOf(artistId));
         if (artArtist == null) { throw new ServiceException("艺术家id错误！"); }
         List<ArtWorks> artWorks = artWorksDao.findByField(ArtWorks.PROP_ARTIST_ID, Integer.valueOf(artistId));
         Map<String, Integer> yearMap = new HashMap<String, Integer>();
         for (ArtWorks works : artWorks) {
            if (yearMap.get(returnString(works.getCreateYear())) == null) {
               if ("".equals(returnString(works.getCreateYear()))) {
                  yearMap.put("未知年代", 1);
               }
               else {
                  yearMap.put(returnString(works.getCreateYear()), 1);
               }
            }
            else {
               yearMap.put(returnString(works.getCreateYear()), yearMap.get(returnString(works.getCreateYear())) + 1);
            }
         }
         List<Map<String, Object>> yearList = new ArrayList<Map<String, Object>>();
         for (Map.Entry<String, Integer> entry : yearMap.entrySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("year", entry.getKey());
            map.put("number", entry.getValue());
            yearList.add(map);
         }
         Map<String, Object> newMap = new HashMap<String, Object>();
         newMap.put("ALLYear", yearList);
         list.add(newMap);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private void artistSeries(List<Map<String, Object>> list, Map<String, String> parameters) {
      String artistId = parameters.get("artistId");
      if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
      int count = 0;
      String size = parameters.get("size");
      if ("pair".equalsIgnoreCase(size)) {
         count = 2;
      }
      else if ("all".equalsIgnoreCase(size)) {
         String sql = "select * " + " from art_artist_works_series s1,art_artist_works_series_research r1,art_works w,art_artist t, "
               + " (select s.id,count(r.id) as cnt from art_artist_works_series s,art_artist_works_series_research r " + " where s.artist_id=" + artistId
               + " and s.id=r.series_id  " + " group by  s.id " + " having count(r.id)>0  " + " ) a " + " where t.id=" + artistId
               + " and t.id=s1.artist_id and s1.id=r1.series_id and r1.works_id=w.id and s1.id=a.id " + " group by s1.id ";
         List<Map<String, Object>> seriesList = jdbcDao.queryBySql(sql);
         if (seriesList != null) {
            count = seriesList.size();
         }
      }
      String sql = "select s1.id AS seriesId,s1.series_name AS descript,w.id AS worksId,CONCAT('auction/',t.folder_name,'/',w.thumbnail) AS photo,"
            + " w.works_c_name AS name,a.cnt AS number" + " from art_artist_works_series s1,art_artist_works_series_research r1,art_works w,art_artist t, "
            + " (select s.id,count(r.id) as cnt from art_artist_works_series s,art_artist_works_series_research r " + " where s.artist_id=" + artistId
            + " and s.id=r.series_id  " + " group by  s.id " + " having count(r.id)>0  " + " ) a " + " where t.id=" + artistId
            + " and t.id=s1.artist_id and s1.id=r1.series_id and r1.works_id=w.id and s1.id=a.id " + " order by s1.series_important,s1.id,r1.works_id";
      List<Map<String, Object>> seriesList = new ArrayList<Map<String, Object>>();
      seriesList = jdbcDao.queryBySql(sql);

      if (seriesList == null || seriesList.size() == 0) { return; }
      putSeries(seriesList, count, list);
   }

   private void putSeries(List<Map<String, Object>> seriesList, int count, List<Map<String, Object>> list) {
      //所有系列id
      Map<String, Object> seriesIdMap = new HashMap<String, Object>();
      String[] seriesIds = new String[seriesList.size()];
      int i = 0;
      for (Map<String, Object> seriesMap : seriesList) {
         String seriesId = returnString(seriesMap.get("seriesId"));
         if ("".equals(seriesId.trim())) {
            continue;
         }
         if (seriesIdMap.get(seriesId) == null) {
            seriesIdMap.put(seriesId, "");
            seriesIds[i] = seriesId;
            i++;
         }
      }

      if (seriesIds[0] == null) { return; }
      //取count次放图片
      for (int j = 0; j < count; j++) {
         Map<String, Object> map = new HashMap<String, Object>();
         Map[] objs = new Map[4];
         int n = 0;
         if (seriesIds.length < (j + 1)) {
            break;
         }
         if (seriesIds[j] == null) {
            break;
         }
         //4张图片
         for (Map<String, Object> seriesMap : seriesList) {
            String seriesId = returnString(seriesMap.get("seriesId"));
            if (n == 5) {
               break;
            }
            if (seriesId.equals(seriesIds[j])) {
               objs[n] = seriesMap;
               n++;
            }
         }
         //4张图片放到list
         List<Map<String, Object>> newlist = new ArrayList<Map<String, Object>>();
         for (int m = 0; m < objs.length; m++) {
            Map<String, Object> newMap = objs[m];
            if (newMap != null) {
               newlist.add(newMap);
            }
            else {
               break;
            }
         }
         Map<String, Object> newMap = objs[0];
         map.put("descript", newMap.get("descript"));
         map.put("series", newlist);
         map.put("number", newMap.get("number"));
         list.add(map);
      }
   }

   private void artistYear(List<Map<String, Object>> list, Map<String, String> parameters) {
      String artistId = parameters.get("artistId");
      if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
      int count = 0;
      String size = parameters.get("size");
      if ("pair".equalsIgnoreCase(size)) {
         count = 2;
      }
      else if ("all".equalsIgnoreCase(size)) {
         String sql = "select * " + " from art_works_year s1,art_works_year_research r1,art_works w,art_artist t, "
               + " (select s.id,count(r.id) as cnt from art_works_year s,art_works_year_research r " + " where s.artist_id=" + artistId
               + " and s.id=r.year_id  " + " group by  s.id " + " having count(r.id)>0  " + " ) a " + " where t.id=" + artistId
               + " and t.id=s1.artist_id and s1.id=r1.year_id and r1.works_id=w.id and s1.id=a.id " + " group by s1.id ";
         List<Map<String, Object>> seriesList = jdbcDao.queryBySql(sql);
         if (seriesList != null) {
            count = seriesList.size();
         }
      }
      String sql = "select s1.id AS yearId,s1.start_year AS startYear,s1.end_year AS endYear,w.id AS worksId,CONCAT('auction/',t.folder_name,'/',w.thumbnail) AS photo,"
            + " w.works_c_name AS name,a.cnt AS number"
            + " from art_works_year s1,art_works_year_research r1,art_works w,art_artist t, "
            + " (select s.id,count(r.id) as cnt from art_works_year s,art_works_year_research r "
            + " where s.artist_id="
            + artistId
            + " and s.id=r.year_id  "
            + " group by  s.id "
            + " having count(r.id)>0  "
            + " ) a "
            + " where t.id="
            + artistId
            + " and t.id=s1.artist_id and s1.id=r1.year_id and r1.works_id=w.id and s1.id=a.id " + " order by s1.year_important,s1.id,r1.seq_no,r1.works_id";
      List<Map<String, Object>> seriesList = new ArrayList<Map<String, Object>>();
      seriesList = jdbcDao.queryBySql(sql);

      if (seriesList == null || seriesList.size() == 0) { return; }
      putYear(seriesList, count, list);
   }

   private void putYear(List<Map<String, Object>> seriesList, int count, List<Map<String, Object>> list) {
      //所有系列id
      Map<String, Object> seriesIdMap = new HashMap<String, Object>();
      String[] seriesIds = new String[seriesList.size()];
      int i = 0;
      for (Map<String, Object> seriesMap : seriesList) {
         String seriesId = returnString(seriesMap.get("yearId"));
         if ("".equals(seriesId.trim())) {
            continue;
         }
         if (seriesIdMap.get(seriesId) == null) {
            seriesIdMap.put(seriesId, "");
            seriesIds[i] = seriesId;
            i++;
         }
      }

      if (seriesIds[0] == null) { return; }
      //取2次放图片
      for (int j = 0; j < count; j++) {
         Map<String, Object> map = new HashMap<String, Object>();
         Map[] objs = new Map[4];
         int n = 0;
         if (seriesIds.length < (j + 1)) {
            break;
         }
         if (seriesIds[j] == null) {
            break;
         }
         //4张图片
         for (Map<String, Object> seriesMap : seriesList) {
            String seriesId = returnString(seriesMap.get("yearId"));
            if (n == 5) {
               break;
            }
            if (seriesId.equals(seriesIds[j])) {
               objs[n] = seriesMap;
               n++;
            }
         }
         //4张图片放到list
         List<Map<String, Object>> newlist = new ArrayList<Map<String, Object>>();
         for (int m = 0; m < objs.length; m++) {
            Map<String, Object> newMap = objs[m];
            if (newMap != null) {
               newlist.add(newMap);
            }
            else {
               break;
            }
         }
         Map<String, Object> newMap = objs[0];
         map.put("descript", returnString(newMap.get("startYear")) + "~" + returnString(newMap.get("endYear")));
         map.put("year", newlist);
         map.put("number", newMap.get("number"));
         list.add(map);
      }
   }

   private boolean isInteger(String account) {
      try {
         Integer.valueOf(account);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }

   private String returnString(Object obj) {
      if (obj == null) { return ""; }
      return obj.toString();
   }

   private List<Map<String, Object>> addError(String message) {
      Map<String, Object> map = new HashMap<String, Object>();
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      map.put("error", message);
      list.add(map);
      return list;
   }

   private Integer returnInteger(Object obj) {
      if (obj == null) { return -1; }
      try {
         return Integer.valueOf(obj.toString());
      }
      catch (Exception e) {
         return -1;
      }
   }

   @Resource
   private ArtArtistDao   artArtistDao;

   @Resource
   private ArtWorksDao    artWorksDao;

   @Resource
   private SysCodeService sysCodeService;

   @Resource
   private ArtMediumDao   artMediumDao;
}
