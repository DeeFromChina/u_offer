package com.golead.art.works;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.activity.model.ArtActivityAbmb;
import com.golead.art.activity.service.ArtActivityAbmbService;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksAbmb;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksAbmbService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

public class ArtWorksAbmbAction extends BaseAction {

   private final Log logger = LogFactory.getLog(ArtWorksAbmbAction.class);

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         initForm();
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if ("WORKS".equalsIgnoreCase(action)) forward = "WORKS";
         else if (PAGE.equalsIgnoreCase(action)) forward = pagePublication();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if ("ADDPAGE".equalsIgnoreCase(action)) forward = addpage();
         else if (SAVE.equalsIgnoreCase(action)) forward = savePublication();
         else if ("SAVEWORKS".equalsIgnoreCase(action)) forward = save();
         else if (DELETE.equalsIgnoreCase(action)) forward = deletePublication();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public void initForm() throws Exception {
      setCode(form, "EXHIB_TYPE");
   }

   public String pagePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'pageRole' method");
      try {
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery.getParameters().put("abmbId", form.getRecord().get("abmbId"));
         pageQuery = artWorksAbmbService.queryArtWorksAbmb(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> rs = pageQuery.getRecordSet();
            if (rs != null) {
               List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < rs.size(); i++) {
                  Map<String, Object> publication = rs.get(i);
                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("id", publication.get("id"));
                  String link = "^javascript:view(" + publication.get("abmbId") + ")^_self";
                  String workslink = "^javascript:worksView(" + publication.get("worksId") + ")^_self";
                  String createTime = "";
                  String createYear = returnString(publication.get("createYear"));
                  createTime = addString(createTime, createYear, "年");
                  String createMonth = returnString(publication.get("createMonth"));
                  createTime = addString(createTime, createMonth, "月");
                  String createDay = returnString(publication.get("createDay"));
                  createTime = addString(createTime, createDay, "日");
                  String sizeCm = returnString(publication.get("sizeCmLength")) + (!returnString(publication.get("sizeCmWidth")).equals("") ? "X" : "")
                        + returnString(publication.get("sizeCmWidth")) + (!returnString(publication.get("sizeCmHeight")).equals("") ? "X" : "")
                        + returnString(publication.get("sizeCmHeight"));
                  String sizeIn = returnString(publication.get("sizeInLength")) + (!returnString(publication.get("sizeInWidth")).equals("") ? "X" : "")
                        + returnString(publication.get("sizeInWidth")) + (!returnString(publication.get("sizeInHeight")).equals("") ? "X" : "")
                        + returnString(publication.get("sizeInHeight"));
                  if (sizeCm.length() <= 2) {
                     sizeCm = "";
                  }
                  if (sizeIn.length() <= 2) {
                     sizeIn = "";
                  }
                  sizeCm = sizeCm.replace(".00", "");
                  sizeIn = sizeIn.replace(".00", "");
                  if (form.getRecord().get("type") != null) {
                     map.put("data", new Object[] { "", publication.get("no") + workslink, publication.get("c_name"), publication.get("chineseName"),
                           publication.get("englishName"), createTime, sizeCm, sizeIn });
                  }
                  else {
                     changePublication(publication);
                     map.put("data", new Object[] { "", publication.get("abmbName") + link, publication.get("c_name"), publication.get("sponsor"), publication.get("abmbNumber"),
                           publication.get("countryName"), publication.get("city") });
                  }
                  list.add(map);
               }
               String res = genGridJson(pageQuery, list);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品艺博会管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
               setResponse(res);
            }
            else setResponse("{exit:1,message:\"数据访问错误。\"}");
         }
         else setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }
   
   public void changePublication(Map<String, Object> publication){
      String abmbId = returnString(publication.get("worksAbmbId"));
      if(!"".equals(abmbId)){
         String name = artWorksAbmbService.findArtistNameByAbmbId(abmbId);
         publication.put("c_name", name);
      }
   }

   public String add() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'add()' method");
      try {
         return ADD;
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   public String addpage() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addpage()' method");
      try {
         PageQuery pq = form.getQuery();
         pq.getParameters().put("groupby", "id");
         pq = artActivityAbmbService.queryArtActivityAbmb(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String link = "^javascript:view(" + item.get("id") + ");^_self";
            map.put("id", item.get("id"));
            map.put("data",
                  new Object[] { "", item.get("abmbName") + link, item.get("sponsor"), item.get("abmbNumber"), item.get("country"), item.get("city") });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         setResponse(res);

      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   public String savePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'savePublication' method");
      try {
         String[] id = ids.split(",");
         int[] add_ids = new int[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < add_ids.length; i++) {
            add_ids[i] = Integer.valueOf(id[i]);
            ArtActivityAbmb abmb = artActivityAbmbService.getArtActivityAbmb(add_ids[i]);
            if (log.length() > 0) log.append(",");
            log.append(abmb.getAbmbName());
         }
         int worksId = Integer.valueOf(form.getRecord().get("worksId"));
         ArtWorks artWorks = artWorksService.getArtWorks(worksId);
         artWorksAbmbService.createArtWorksAbmb(worksId, add_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品艺博会管理",
               "添加作品艺博会关联：" + artWorks.getWorksCName() + ":" + "[" + log.toString() + "]");
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
      return null;
   }

   public String save() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'save' method");
      try {
         String[] id = ids.split(",");
         int[] add_ids = new int[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < add_ids.length; i++) {
            add_ids[i] = Integer.valueOf(id[i]);
            ArtWorks artWorks = artWorksService.getArtWorks(add_ids[i]);
            if(log.length()>0)log.append(",");
            log.append(artWorks.getWorksCName());
         }
         int abmbId = Integer.valueOf(form.getRecord().get("abmbId"));
         ArtActivityAbmb activityAbmb = artActivityAbmbService.getArtActivityAbmb(abmbId);
         artWorksAbmbService.createArtWorksAbmb2(abmbId, add_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品艺博会管理",
               "添加作品艺博会关联：" + activityAbmb.getAbmbName() + ":" + "[" + log.toString() + "]");
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
      return null;
   }

   public String deletePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'deleteAuction' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtWorksAbmb artWorksAbmb = artWorksAbmbService.getArtWorksAbmb(del_ids[i]);
            ArtActivityAbmb abmb = artActivityAbmbService.getArtActivityAbmb(artWorksAbmb.getAbmbId());
            ArtWorks artWorks =artWorksService.getArtWorks(artWorksAbmb.getWorksId());
            if(log.length()>0)log.append(",");
            log.append(artWorks.getWorksCName()).append(":").append(abmb.getAbmbName());
         }
         artWorksAbmbService.deleteArtWorksAbmbs(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品艺博会管理",
               "删除作品艺博会关联：" +log.toString());
         setResponse("{exit:0}");
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"删除操作失败!\"}");
      }
      return null;
   }

   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   private String addString(String str, String equalStr, String addStr) {
      if (!"".equals(equalStr)) {
         if (equalStr.indexOf(addStr) > -1) return str + equalStr;
         else return str + equalStr + addStr;
      }
      return str;
   }

   @Resource
   private ArtActivityAbmbService artActivityAbmbService;

   @Resource
   private ArtSysLogService       artSysLogService;

   @Resource
   private ArtWorksService        artWorksService;

   @Resource
   private ArtWorksAbmbService    artWorksAbmbService;

}
