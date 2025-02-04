package com.golead.art.appManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.golead.art.app.artistPage.model.ArtArtistPage;
import com.golead.art.app.artistPage.model.ArtArtistTemplate;
import com.golead.art.app.artistPage.service.ArtArtistPageService;
import com.golead.art.app.artistPage.service.ArtArtistTemplateService;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class AppArtistPageAction extends BaseAction {

   private Log                      log           = LogFactory.getLog(AppArtistPageAction.class);

   @Resource
   private ArtArtistPageService     artArtistPageService;

   @Resource
   private ArtArtistTemplateService artArtistTemplateService;

   @Resource
   private ArtArtistService         artArtistService;

   @Resource
   private ArtSysLogService         artSysLogService;

   private String                   path          = ServletActionContext.getServletContext().getRealPath("/upload/app/artistpage");

   private String                   imgPath       = "/upload/app/artistpage/";

   private String                   cellImgFormat = "<a href='%s' target='_blank'><img width='100px' height='100px' src='%s' /></a>";

   private File                     file;

   private String                   fileFileName;

   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      initForm();
      if (action == null) {
         forward = LIST;
      }
      try {
         if (PAGE.equalsIgnoreCase(action)) forward = page();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   private void initForm() {
      setCode(form, "ARTISRT_PAGE");
   }

   /**
    * 模板内容列表
    */
   public String page() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'artistPageResearchpage()' method");
      try {
         Map<String, String> record = form.getRecord();
         Integer templateId = Integer.valueOf(record.get("id"));
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         List<Map<String, Object>> items = artArtistPageService.queryArtArtistPage(templateId);
         for (Map<String, Object> item : items) {
            Map<String, Object> map = new HashMap<String, Object>();
            String photo = item.get("photo") != null ? item.get("photo").toString().split("/")[1] : "";
            String color = item.get("color") != null ? item.get("color").toString() : "";
            String colorSelf = item.get("colorSelf") != null ? item.get("colorSelf") + "" : "";
            String seqNo = item.get("seqNo") != null ? this.findCodeName(form, "ARTISRT_PAGE", item.get("seqNo") + "") : "";
            String remark = item.get("remark") != null ? item.get("remark") + "" : "";

            String photoPath = request.get("CONTEXT_PATH") + imgPath + "/" + photo;
            String cell = String.format(cellImgFormat, photoPath, photoPath);
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cell, color, colorSelf, seqNo, remark });
            list.add(map);
         }
         String res = genGridJson(list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家首页管理", "查询条件为：" + "templateId=" + templateId);
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
         ArtArtistPage artArtistPage = new ArtArtistPage();
         ConvertUtil.convertToModel(artArtistPage, record);
         ArtArtistTemplate artistTemplate = artArtistTemplateService.getArtArtistTemplate(artArtistPage.getTempId());
         artArtistPageService.createArtArtistPage(artArtistPage, file, fileFileName, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家首页管理",
               "添加艺术家主页条目：" + "[" + artistTemplate.getTemplateName() + ":" + this.findCodeName(form, "ARTISRT_PAGE", artArtistPage.getSeqNo()) + "]");
         return returnForward(RETURN_NORMAL);
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
         ArtArtistPage artArtistPage = artArtistPageService.getArtArtistPage(id);
         ConvertUtil.objectToMap(record, artArtistPage);
         String imgName;
         if (artArtistPage.getPhoto() != null) {
            imgName = artArtistPage.getPhoto().split("/")[1];
         }
         else {
            imgName = "";
         }

         String img = request.get("CONTEXT_PATH") + imgPath + "/" + imgName;
         record.put("img", img);
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
         ArtArtistPage artArtistPage = new ArtArtistPage();
         ConvertUtil.convertToModel(artArtistPage, record);
         ArtArtistTemplate artistTemplate = artArtistTemplateService.getArtArtistTemplate(artArtistPage.getTempId());
         artArtistPageService.updateArtArtistPage(record, file, fileFileName, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家首页管理",
               "修改艺术家主页条目：" + "[" + artistTemplate.getTemplateName() + ":" + this.findCodeName(form, "ARTISRT_PAGE", artArtistPage.getSeqNo()) + "]");
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
            ArtArtistPage artArtistPage = artArtistPageService.getArtArtistPage(dels[i]);
            ArtArtistTemplate artistTemplate = artArtistTemplateService.getArtArtistTemplate(artArtistPage.getTempId());
            if (log.length() > 0) log.append(",");
            log.append("[").append(artistTemplate.getTemplateName()).append(":").append(this.findCodeName(form, "ARTISRT_PAGE", artArtistPage.getSeqNo()))
                  .append("]");
         }
         artArtistPageService.deleteArtArtistPages(dels, path);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家首页管理", "删除艺术家主页条目：" + log.toString());
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

   public File getFile() {
      return file;
   }

   public void setFile(File file) {
      this.file = file;
   }

   public String getFileFileName() {
      return fileFileName;
   }

   public void setFileFileName(String fileFileName) {
      this.fileFileName = fileFileName;
   }
}
