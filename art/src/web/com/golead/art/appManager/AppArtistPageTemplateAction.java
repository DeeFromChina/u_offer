package com.golead.art.appManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.app.artistPage.model.ArtArtistPageResearch;
import com.golead.art.app.artistPage.model.ArtArtistTemplate;
import com.golead.art.app.artistPage.service.ArtArtistPageResearchService;
import com.golead.art.app.artistPage.service.ArtArtistPageService;
import com.golead.art.app.artistPage.service.ArtArtistTemplateService;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class AppArtistPageTemplateAction extends BaseAction {

   private Log                          log                   = LogFactory.getLog(AppArtistPageTemplateAction.class);

   private DateFormat                   format                = new SimpleDateFormat("yyyy-MM-dd");

   @Resource
   private ArtArtistTemplateService     artArtistTemplateService;

   @Resource
   private ArtArtistPageService         artArtistPageService;

   @Resource
   private ArtArtistService             artArtistService;

   @Resource
   private ArtArtistPageResearchService artArtistPageResearchService;

   @Resource
   private ArtSysLogService             artSysLogService;

   private String                       thumbnailPathFormat   = "%supload/photo/%s/thumbnails/%s";

   private String                       artistPhotoPathFormat = "%supload/photo/%s/%s";

   private String                       cellImgFormat         = "<a href='%s' target='_blank'><img width='100px' height='100px' src='%s' /></a>";

   /**
    * 其他页面列表信息
    */
   private final String                 OPERATION             = "OPERATION";
   private final String                 OPERATION_PAGE        = "OPERATION_PAGE";
   private final String                 OPERATION_DELETE      = "OPERATION_DELETE";
   private final String                 OPERATION_ADD         = "OPERATION_ADD";
   private final String                 OPERATION_ADD_PAGE    = "OPERATION_ADD_PAGE";
   private final String                 OPERATION_SAVE        = "OPERATION_SAVE";

   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      initForm();
      if (action == null) {
         forward = LIST;
      }
      try {
         if (LIST.equalsIgnoreCase(action)) forward = list();
         else if (PAGE.equalsIgnoreCase(action)) forward = page();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();

         else if (OPERATION.equalsIgnoreCase(action)) forward = operation();
         else if (OPERATION_PAGE.equalsIgnoreCase(action)) forward = operationPage();
         else if (OPERATION_ADD.equalsIgnoreCase(action)) forward = operationAdd();
         else if (OPERATION_ADD_PAGE.equalsIgnoreCase(action)) forward = operationAddPage();
         else if (OPERATION_SAVE.equalsIgnoreCase(action)) forward = operationSave();
         else if (OPERATION_DELETE.equalsIgnoreCase(action)) forward = operationDelete();

      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   private void initForm() {
      //将代码集里的常量放入form.CodeSet
      setCode(form, "ARTISRT_PAGE");
   }

   public String operation() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'operation()' method");
      return OPERATION;
   }

   public String operationPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'operationPage()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         pageQuery = artArtistPageResearchService.queryArtArtistPageResearch(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String contextPath = request.get("CONTEXT_PATH").toString();
            String folderName = item.get("folderName") == null || "".equals(item.get("folderName")) ? "" : item.get("folderName") + "";
            Object photoItem = item.get("photo");
            String photoName = photoItem == null || "".equals(photoItem) ? "" : item.get("photo").toString().split("/")[1];

            String cell = null;
            if (!"".equals(photoName)) {
               String imgPath = String.format(artistPhotoPathFormat, contextPath, folderName, photoName);
               String thumbnailPath = String.format(thumbnailPathFormat, contextPath, folderName, photoName);
               cell = String.format(cellImgFormat, imgPath, thumbnailPath);
            }
            else {
               cell = "暂无图片";
            }
            String cName = item.get("cName") + "";
            String eName = item.get("eName") + "";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cell, cName, eName });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家列表", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String operationDelete() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'operationDelete()' method");
      try {
         String[] del = ids.split(",");
         Integer[] dels = new Integer[del.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del.length; i++) {
            dels[i] = Integer.valueOf(del[i]);
            ArtArtistPageResearch artArtistPageResearch = artArtistPageResearchService.getArtArtistPageResearch(dels[i]);
            ArtArtist artArtist = artArtistService.getArtArtist(artArtistPageResearch.getArtistId());
            ArtArtistTemplate artArtistTemplate = artArtistTemplateService.getArtArtistTemplate(artArtistPageResearch.getTempId());
            if (log.length() > 0) log.append(",");
            log.append("[").append(artArtist.getCname()).append(":").append(artArtistTemplate.getTemplateName()).append("]");
         }
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家主页模板管理", "删除艺术家模板设定：" + log.toString());
         artArtistPageResearchService.deleteArtArtistPageResearchs(dels);
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

   public String operationAdd() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'operationAdd()' method");
      return OPERATION_ADD;
   }

   public String operationAddPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'operationAddPage()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         pageQuery = artArtistPageResearchService.queryArtArtistPageNotResearch(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String contextPath = request.get("CONTEXT_PATH").toString();
            String folderName = item.get("folderName") == null || "".equals(item.get("folderName")) ? "" : item.get("folderName") + "";
            Object photoItem = item.get("photo");
            String photoName = photoItem == null || "".equals(photoItem) ? "" : item.get("photo").toString().split("/")[1];

            String cell = null;
            if (!"".equals(photoName)) {
               String imgPath = String.format(artistPhotoPathFormat, contextPath, folderName, photoName);
               String thumbnailPath = String.format(thumbnailPathFormat, contextPath, folderName, photoName);
               cell = String.format(cellImgFormat, imgPath, thumbnailPath);
            }
            else {
               cell = "暂无图片";
            }
            String cName = item.get("cName") + "";
            String eName = item.get("eName") + "";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cell, cName, eName });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家列表管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String operationSave() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'operationSave()' method");
      String[] idArray = ids.split(",");
      Integer tempId = Integer.valueOf(form.getRecord().get("tempId"));
      StringBuffer log = new StringBuffer();
      ArtArtistTemplate artArtistTemplate = artArtistTemplateService.getArtArtistTemplate(tempId);
      log.append(artArtistTemplate.getTemplateName()).append(":").append("[");
      for (int i = 0; i < idArray.length; i++) {
         if (idArray != null) {
            ArtArtist artArtist = artArtistService.getArtArtist(Integer.valueOf(idArray[i]));
            if (i > 0) log.append(",");
            log.append(artArtist.getCname());
         }
      }
      log.append("]");
      for (int i = 0; i < idArray.length; i++) {
         Integer artistId = Integer.valueOf(idArray[i]);
         ArtArtistPageResearch artArtistPageResearch = new ArtArtistPageResearch();
         artArtistPageResearch.setArtistId(artistId);
         artArtistPageResearch.setTempId(tempId);
         artArtistPageResearchService.createArtArtistPageResearch(artArtistPageResearch);
      }
      artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家主页模板管理", "关联艺术家模板设定：" + log.toString());
      return returnForward(RETURN_NORMAL);
   }

   public String list() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'list()' method");
      return LIST;
   }

   public String page() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'page()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         pageQuery = artArtistTemplateService.queryArtArtistTemplate(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String id = item.get("id") + "";
            String templateName = item.get(ArtArtistTemplate.PROP_TEMPLATE_NAME) != null ? item.get(ArtArtistTemplate.PROP_TEMPLATE_NAME) + "" : "";
            String templateRemark = item.get(ArtArtistTemplate.PROP_TEMPLATE_REMARK) != null ? item.get(ArtArtistTemplate.PROP_TEMPLATE_REMARK) + "" : "";
            String createTime = item.get(ArtArtistTemplate.PROP_CREATE_TIME) != null ? format.format(item.get(ArtArtistTemplate.PROP_CREATE_TIME)) : "";
            String creater = item.get(ArtArtistTemplate.PROP_CREATER) != null ? item.get(ArtArtistTemplate.PROP_CREATER) + "" : "";
            String button = "<input type='button' style='height:18px;' onclick='goOperation(" + id + ")' value='操作'>";
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", templateName, creater, createTime, templateRemark, button });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家主页模板列表", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String add() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'add()' method");
      String forward = null;
      try {
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   public String save() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'save()' method");
      try {
         Map<String, String> record = form.getRecord();
         ArtArtistTemplate template = new ArtArtistTemplate();
         ConvertUtil.convertToModel(template, record);
         template.setCreater(currentUser.getUserName());
         template.setCreateTime(new Date());
         artArtistTemplateService.createArtArtistTemplate(template);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家主页模板管理", "保存艺术家主页模板：" + template.getTemplateName());
         ids = template.getId().toString();
         return edit();
      }
      catch (ServiceException e) {
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(getForm(), "保存操作失败!");
         return ADD;
      }
   }

   public String edit() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'edit()' method");
      String forward = null;
      try {
         int id = Integer.valueOf(ids);
         Map<String, String> record = form.getRecord();
         ArtArtistTemplate artistTemplate = artArtistTemplateService.getArtArtistTemplate(id);
         ConvertUtil.objectToMap(record, artistTemplate);
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   public String update() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'update()' method");
      try {
         Map<String, String> record = form.getRecord();
         artArtistTemplateService.updateArtArtistTemplate(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家主页模板管理", "修改艺术家主页模板：" + record.get(ArtArtistTemplate.PROP_TEMPLATE_NAME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String delete() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'delete()' method");
      try {
         String[] del = ids.split(",");
         Integer[] dels = new Integer[del.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del.length; i++) {
            dels[i] = Integer.valueOf(del[i]);
            if (log.length() > 0) log.append(",");
            log.append(artArtistTemplateService.getArtArtistTemplate(dels[i]).getTemplateName());
         }
         artArtistTemplateService.deleteArtArtistTemplates(dels);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家主页模板管理", "删除艺术家主页模板：" + log.toString());
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

}
