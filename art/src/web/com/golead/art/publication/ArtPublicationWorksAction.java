package com.golead.art.publication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.publication.model.ArtPublication;
import com.golead.art.publication.model.ArtPublicationWorks;
import com.golead.art.publication.service.ArtPublicationService;
import com.golead.art.publication.service.ArtPublicationWorksService;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

public class ArtPublicationWorksAction extends BaseAction {

   private final Log logger = LogFactory.getLog(ArtPublicationWorksAction.class);
   
   private DecimalFormat    decimalFormat = new DecimalFormat("###.##");

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachPublication();
         else if (PAGE.equalsIgnoreCase(action)) forward = pagePublication();
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

   public String sreachPublication() throws Exception {
      return LIST;
   }

   public String pagePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'pageRole' method");
      try {
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery = artPublicationWorksService.queryArtPublicationWorks(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> rs = pageQuery.getRecordSet();
            if (rs != null) {
               List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < rs.size(); i++) {
                  Map<String, Object> publication = rs.get(i);
                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("id", publication.get("id"));
                  String link = "^javascript:view(" + publication.get("wid") + ")^_self";
                  String sizeCm = "";
                  String sizeCmWidth = publication.get("sizeCmWidth") == null ? "" : decimalFormat.format(publication.get("sizeCmWidth"));
                  String sizeCmHeight = publication.get("sizeCmHeight") == null ? "" : decimalFormat.format(publication.get("sizeCmHeight"));
                  String sizeCmLength = publication.get("sizeCmLength") == null ? "" : decimalFormat.format(publication.get("sizeCmLength"));
                  sizeCm = sizeCmLength + returnX(sizeCmLength, sizeCmWidth) + sizeCmWidth + returnX(sizeCmWidth, sizeCmHeight) + sizeCmHeight; 
                  map.put("data", new Object[] { "", publication.get("worksName") + link, publication.get("createYear"), sizeCm });
                  list.add(map);
               }
               String res = genGridJson(pageQuery, list);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物作品管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String returnX(String a, String b){
      if(!"".equals(a) && !"".equals(b)){
         return "X";
      }
      return "";
   }
   
   public String savePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'savePublication' method");
      try {
         String[] id = ids.split("、");
         int[] add_ids = new int[id.length];
         StringBuffer log = new StringBuffer();
         
         int pubId = Integer.valueOf(form.getQuery().getParameters().get("pubId"));
         ArtPublication artPublication = artPublicationService.getArtPublication(pubId);
         log.append(artPublication.getPublicationName()).append(":").append("[");
         
         for (int i = 0; i < add_ids.length; i++) {
            add_ids[i] = Integer.valueOf(id[i]);
            
            ArtWorks artWorks = artWorksService.getArtWorks(add_ids[i]);
            if(i>0)log.append(",");
            log.append(artWorks.getWorksCName());
         }
         log.append("]");
         artPublicationWorksService.createArtPublicationWorks(add_ids, pubId);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物作品管理", "添加出版物作品：" + log.toString());
         
         setResponse("{exit:0}");
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"保存操作失败!\"}");
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
            ArtPublicationWorks artPublicationWorks = artPublicationWorksService.getArtPublicationWorks(del_ids[i]);
            ArtPublication artPublication = artPublicationService.getArtPublication(artPublicationWorks.getPubId());
            ArtWorks artWorks = artWorksService.getArtWorks(artPublicationWorks.getWorksId());
            if(log.length()>0)log.append(",");
            log.append("[").append(artWorks.getWorksCName()).append(":").append(artPublication.getPublicationName()).append("]");
         }
         artPublicationWorksService.deleteArtPublicationWorkss(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物作品管理", "删除出版物作品：" + log.toString());
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
   private ArtPublicationWorksService artPublicationWorksService;
   
   @Resource
   private ArtSysLogService                artSysLogService;
   
   @Resource
   private ArtWorksService artWorksService;
   
   @Resource
   private ArtPublicationService artPublicationService;

}
