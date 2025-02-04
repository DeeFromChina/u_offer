package com.golead.art.works.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.golead.art.works.service.ArtWorksIdenService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;

@Service
public class ArtWorksIdenServiceImpl extends BaseServiceImpl implements ArtWorksIdenService{

   public List<Map<String, Object>> groupByArtistId(Serializable id) throws ServiceException{
      StringBuffer sql = new StringBuffer();
      sql.append("SELECT works.works_c_name AS name FROM art_works works ");
      sql.append(" INNER JOIN (");
      sql.append(" SELECT works.works_c_name FROM art_works works ");
      sql.append(" GROUP BY works.works_c_name ");
      sql.append(" HAVING COUNT(works.works_c_name) > 1 ");
      sql.append(" ) n ON n.works_c_name = works.works_c_name ");
      sql.append(" WHERE works.artist_id=");
      sql.append(id);
      sql.append(" GROUP BY works.works_c_name ");
      sql.append(" ORDER BY convert(works.works_c_name using gbk) ASC ");
      return jdbcDao.queryBySql(sql.toString());
   }
   
   public PageQuery queryArtWorks(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksList", pageQuery);
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
      String order = " ORDER BY ";
      String where = ""; 
      boolean isJoin = false;
      StringBuffer join = new StringBuffer();
      StringBuffer param1 = new StringBuffer();
      StringBuffer param2 = new StringBuffer();
      StringBuffer param3 = new StringBuffer();
      Map<String, String> map = page.getParameters();

      String code = map.get("code");
      if (code != null && code.trim().length() > 0) {
         paras += " AND works.works_no like '%" + code.trim() + "%'";
      }

      String artistId = map.get("artistId");
      if (artistId != null && artistId.trim().length() > 0) {
         paras += " AND artist.id =" + artistId.trim();
      }
      
      String name = map.get("name");
      if (name != null && name.trim().length() > 0) {
         paras += " AND works.works_c_name ='" + name.trim() + "'";
         where = " WHERE works.works_c_name ='" + name.trim() + "'";
      }
      
      String createYear = map.get("createYear");
      if (createYear != null && createYear.trim().length() > 0) {
         if(param1.toString().length() > 0){
            param1.append(",");
         }
         param1.append("works.create_year");
         param2.append(" create_year is null");
         if(param3.toString().length() > 0){
            param3.append(" AND ");
         }
         param3.append(" IFNULL(n.create_year,0) = IFNULL(works.create_year,0) ");
         isJoin = true;
         if(order.length() > 10){
            order+=",";
         }
         order+=" works.create_year";
      }
      
      String sizeCm = map.get("sizeCm");
      if (sizeCm != null && sizeCm.trim().length() > 0) {
         if(param1.toString().length() > 0){
            param1.append(",");
         }
         param1.append("works.size_cm_length,works.size_cm_width,works.size_cm_height");
         if(param2.toString().length() > 0){
            param2.append(" AND ");
         }
         param2.append(" size_cm_length is null and size_cm_width is null and size_cm_height is null ");
         if(param3.toString().length() > 0){
            param3.append(" AND ");
         }
         param3.append(" IFNULL(n.size_cm_length,0) = IFNULL(works.size_cm_length,0) AND IFNULL(n.size_cm_width,0) = IFNULL(works.size_cm_width,0) AND IFNULL(n.size_cm_height,0) = IFNULL(works.size_cm_height,0) ");
         isJoin = true;
         if(order.length() > 10){
            order+=",";
         }
         order+=" works.size_cm_width";
      }
      
      String sizeIn = map.get("sizeIn");
      if (sizeIn != null && sizeIn.trim().length() > 0) {
         if(param1.toString().length() > 0){
            param1.append(",");
         }
         param1.append("works.size_in_length,works.size_in_width,works.size_in_height");
         if(param2.toString().length() > 0){
            param2.append(" AND ");
         }
         param2.append(" size_in_length is null and size_in_width is null and size_in_height is null ");
         if(param3.toString().length() > 0){
            param3.append(" AND ");
         }
         param3.append(" IFNULL(n.size_in_length,0) = IFNULL(works.size_in_length,0) AND IFNULL(n.size_in_width,0) = IFNULL(works.size_in_width,0) AND IFNULL(n.size_in_height,0) = IFNULL(works.size_in_height,0) ");
         isJoin = true;
         if(order.length() > 10){
            order+=",";
         }
         order+=" works.size_in_width";
      }
      
      String sizeRule = map.get("sizeRule");
      if (sizeRule != null && sizeRule.trim().length() > 0) {
         if(param1.toString().length() > 0){
            param1.append(",");
         }
         param1.append("works.size_rule_length,works.size_rule_width,works.size_rule_height");
         if(param2.toString().length() > 0){
            param2.append(" AND ");
         }
         param2.append(" size_rule_length is null and size_rule_width is null and size_rule_height is null ");
         if(param3.toString().length() > 0){
            param3.append(" AND ");
         }
         param3.append(" IFNULL(n.size_rule_length,0) = IFNULL(works.size_rule_length,0) AND IFNULL(n.size_rule_width,0) = IFNULL(works.size_rule_width,0) AND IFNULL(n.size_rule_height,0) = IFNULL(works.size_rule_height,0) ");
         isJoin = true;
         if(order.length() > 10){
            order+=",";
         }
         order+=" works.size_rule_width";
      }
      if(order.length() > 10){
         order+= " ASC ";
         paras+=order;
      }
      if (paras.length() > 0) page.getParameters().put("paras", paras);
      
      if(isJoin){
         join.append(" INNER JOIN (select ");
         join.append(param1.toString());
         join.append(",COUNT(*) c ");
         join.append(" FROM art_works works ");
         join.append(where);
         join.append(" GROUP BY ");
         join.append(param1.toString());
         join.append(" HAVING ");
         join.append(" COUNT(*) > 1 OR (");
         join.append(param2.toString());
         join.append(" )");
         join.append(" ) n ON ");
         join.append(param3.toString());
         page.getParameters().put("join", join.toString());
      }
   }
}
