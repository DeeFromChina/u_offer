package com.golead.art.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.activity.model.ArtActivityExhibit;
import com.golead.art.activity.model.ArtActivityExhibitArtist;
import com.golead.art.activity.service.ArtActivityExhibitArtistService;
import com.golead.art.activity.service.ArtActivityExhibitService;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

public class ArtActivityExhibitArtistAction extends BaseAction {

   private final Log logger = LogFactory.getLog(ArtActivityExhibitArtistAction.class);

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         initForm();
         if (LIST.equalsIgnoreCase(action)) forward = sreachPublication();
         else if (PAGE.equalsIgnoreCase(action)) forward = pagePublication();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if ("ADDPAGE".equalsIgnoreCase(action)) forward = addpage();
         else if (SAVE.equalsIgnoreCase(action)) forward = savePublication();
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
      setCode(form, "GENDER");
   }

   public String sreachPublication() throws Exception {
      return LIST;
   }

   public String pagePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'pageRole' method");
      try {
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery = artActivityExhibitArtistService.queryArtActivityExhibitArtist(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> rs = pageQuery.getRecordSet();
            if (rs != null) {
               List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < rs.size(); i++) {
                  Map<String, Object> publication = rs.get(i);
                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("id", publication.get("id"));
                  String link = "^javascript:view(" + publication.get("artistId") + ")^_self";
                  map.put("data",
                        new Object[] { "", publication.get("cName") + link,
                              findCodeName(form, "GENDER", publication.get("sex") == null ? "" : publication.get("sex").toString()),
                              publication.get("countryName") });
                  list.add(map);
               }
               String res = genGridJson(pageQuery, list);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家展览管理", "艺术家展览管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
         pq = artArtistService.queryArtArtist(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String artistLink = "^javascript:view(" + item.get("id") + ");^_self";
            String sex = findCodeName(getForm(), "GENDER", item.get("sex") + "");
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("cName") + artistLink, item.get("eName"), sex, item.get("nationalityName"), item.get("nhom") });
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
         String exhibitId = form.getRecord().get("exhibitId");
         ArtActivityExhibit activityExhibit = artActivityExhibitService.getArtActivityExhibit(Integer.valueOf(exhibitId));

         StringBuffer log = new StringBuffer();
         log.append(activityExhibit.getExhibitName()).append(":").append("[");
         for (int i = 0; i < add_ids.length; i++) {
            add_ids[i] = Integer.valueOf(id[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(add_ids[i]);
            if (i > 0) log.append(",");
            log.append(artArtist.getCname());
         }
         log.append("]");
         artActivityExhibitArtistService.createArtActivityExhibitArtist(Integer.valueOf(exhibitId), add_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家展览管理", "艺术家展览管理", "添加艺术家展览：" + log.toString());
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
      return null;
   }

   public String deletePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'deleteAuction' method");
      try {
         StringBuffer log = new StringBuffer();
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtActivityExhibitArtist activityExhibitArtist = artActivityExhibitArtistService.getArtActivityExhibitArtist(del_ids[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(activityExhibitArtist.getArtistId());
            ArtActivityExhibit activityExhibit = artActivityExhibitService.getArtActivityExhibit(activityExhibitArtist.getExhibitId());
            if (log.length() > 0) log.append(",");
            log.append("[").append(activityExhibit.getExhibitName()).append(":").append(artArtist.getCname()).append("]");
         }
         artActivityExhibitArtistService.deleteArtActivityExhibitArtists(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家展览管理", "艺术家展览管理", "删除艺术家展览：" + log.toString());
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

   @Resource
   private ArtActivityExhibitArtistService artActivityExhibitArtistService;

   @Resource
   private ArtArtistService                artArtistService;

   @Resource
   private ArtActivityExhibitService       artActivityExhibitService;

   @Resource
   private ArtSysLogService                artSysLogService;

}
