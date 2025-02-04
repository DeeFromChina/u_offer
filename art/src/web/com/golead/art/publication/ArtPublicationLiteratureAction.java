package com.golead.art.publication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.literature.service.ArtLiteratureWordsService;
import com.golead.art.publication.model.ArtPublicationLiterature;
import com.golead.art.publication.service.ArtPublicationLiteratureService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtPublicationLiteratureAction extends BaseAction {

   private final Log logger = LogFactory.getLog(ArtPublicationLiteratureAction.class);

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachPublication();
         else if (PAGE.equalsIgnoreCase(action)) forward = pagePublication();
         else if (ADD.equalsIgnoreCase(action)) forward = addPublication();
         else if (SAVE.equalsIgnoreCase(action)) forward = savePublication();
         else if (EDIT.equalsIgnoreCase(action)) forward = editPublication();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updatePublication();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewPublication();
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
         pageQuery = artPublicationLiteratureService.queryArtPublicationLiterature(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> rs = pageQuery.getRecordSet();
            if (rs != null) {
               List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < rs.size(); i++) {
                  Map<String, Object> publication = rs.get(i);
                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("id", publication.get("id"));
                  String link = "^javascript:view(" + publication.get("literatureId") + ")^_self";
                  map.put("data",
                        new Object[] { "", publication.get("publicationName") + link, publication.get("literatureAuther"), publication.get("writeTime"), publication.get("pageNumber")});
                  list.add(map);
               }
               String res = genGridJson(pageQuery, list);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String addPublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addPublication' method");
      try {
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String savePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'savePublication' method");
      try {
         ArtPublicationLiterature literature = new ArtPublicationLiterature();
         ConvertUtil.mapToObject(literature, form.getRecord(), false);
         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(literature.getLiteratureId());
         literature.setPageNumber(artLiteratureWords.getRelevantPages());
         artPublicationLiteratureService.createArtPublicationLiterature(literature);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "添加出版物：" + artLiteratureWords.getPublicationName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
   }

   public String editPublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editPublication' method");
      try {
         ArtPublicationLiterature literature =  artPublicationLiteratureService.getArtPublicationLiterature(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), literature);
         ArtLiteratureWords words = artLiteratureWordsService.getArtLiteratureWords(literature.getLiteratureId());
         form.getRecord().put("literatureName", words.getLiteratureTitle());
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updatePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'updatePublication' method");
      try {
         String id = form.getRecord().get("literatureId");
         ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(Integer.valueOf(id));
         form.getRecord().put("pageNumber", artLiteratureWords.getRelevantPages());
         artPublicationLiteratureService.updateArtPublicationLiterature(form.getRecord());
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "修改出版物：" + artLiteratureWords.getPublicationName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return EDIT;
      }
   }

   public String deletePublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'deleteAuction' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtPublicationLiterature artPublicationLiterature = artPublicationLiteratureService.getArtPublicationLiterature(del_ids[i]);
            ArtLiteratureWords artLiteratureWords = artLiteratureWordsService.getArtLiteratureWords(artPublicationLiterature.getLiteratureId());
            if(log.length()>0)log.append(",");
            log.append(artLiteratureWords.getPublicationName());
         }
         artPublicationLiteratureService.deleteArtPublicationLiteratures(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物管理", "修改出版物：" + log.toString());
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

   public String viewPublication() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'viewPublication' method");
      String forward = null;
      try {
         ArtPublicationLiterature literature =  artPublicationLiteratureService.getArtPublicationLiterature(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), literature);
         ArtLiteratureWords words = artLiteratureWordsService.getArtLiteratureWords(literature.getLiteratureId());
         form.getRecord().put("literatureName", words.getLiteratureTitle());
         forward = "VIEW";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("err", e);
         forward = "ERROR";
      }
      return forward;
   }

   @Resource
   private ArtPublicationLiteratureService artPublicationLiteratureService;
   
   @Resource
   private ArtLiteratureWordsService artLiteratureWordsService;
   
   @Resource
   private ArtSysLogService                artSysLogService;

}
