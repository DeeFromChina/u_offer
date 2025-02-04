package com.golead.art.appManager;

import com.golead.art.app.homePage.model.ArtAppHomePage;
import com.golead.art.app.homePage.service.ArtAppHomePageService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import javax.annotation.Resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppHomePageAction extends BaseAction {

   private Log                   log        = LogFactory.getLog(AppHomePageAction.class);

   @Resource
   private ArtAppHomePageService artAppHomePageService;
   
   @Resource
   private ArtSysLogService              artSysLogService;

   private String                uploadPath = ServletActionContext.getServletContext().getRealPath("/upload/app/homepage/");

   private String                imgPath    = "/upload/app/homepage/";

   private String                cell       = "<img width='150px' height='150px' src='%s' />";

   private File                  file;

   private String                fileFileName;

   @Override
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
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   private void initForm() {
      setCode(form, "START_OR_STOP");
   }

   public String list() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'list()' method");
      return LIST;
   }

   public String page() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageExperience()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         pageQuery = artAppHomePageService.queryArtAppHomePage(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            Object o = item.get("homepagePhoto");
            String seqNo = item.get("seqNo")+"";
            String cellImg;
            if (o != null) {
               String homepagePhotoPath = request.get("CONTEXT_PATH") + imgPath + o.toString().split("/")[1];
               cellImg = String.format(cell, homepagePhotoPath);
            }
            else {
               cellImg = "暂无图片";
            }
            String homepageStatus = this.findCodeName(form, "START_OR_STOP", item.get("homepageStatus") + "");
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cellImg, homepageStatus, seqNo });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "App首页管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
         ArtAppHomePage artAppHomePage = new ArtAppHomePage();
         ConvertUtil.convertToModel(artAppHomePage, record);
         artAppHomePageService.createArtAppHomePage(artAppHomePage, file, fileFileName, uploadPath);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "App首页管理", "添加首页：" + artAppHomePage.toString());
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
         ArtAppHomePage artAppHomePage = artAppHomePageService.getArtAppHomePage(id);
         String img = request.get("CONTEXT_PATH") + imgPath + artAppHomePage.getHomepagePhoto().split("/")[1];
         record.put("img", img);
         ConvertUtil.objectToMap(record, artAppHomePage);
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
         artAppHomePageService.updateArtAppHomePage(record, file, fileFileName, uploadPath);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "App首页管理", "修改首页：" + record);
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
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < id.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            if(log.length()>0)
               log.append(",");
            log.append(artAppHomePageService.getArtAppHomePage(del_ids[i]).toString());
         }
         artAppHomePageService.deleteArtAppHomePages(del_ids, uploadPath);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "App首页管理", "删除首页：" +log.toString());
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
