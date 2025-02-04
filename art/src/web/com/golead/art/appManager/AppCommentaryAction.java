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

import com.golead.art.app.commentary.model.ArtCommentary;
import com.golead.art.app.commentary.service.ArtCommentaryService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

/**
 * 用户留言管理Action
 * */
public class AppCommentaryAction extends BaseAction {

   private final String DISPASS = "DISPASS";

   private final String PASS    = "PASS";

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      initForm();
      if (action == null) forward = LIST;
      try {
         if (LIST.equalsIgnoreCase(action)) forward = commentaryList();
         else if (PAGE.equalsIgnoreCase(action)) forward = commentaryPage();
         else if (DISPASS.equalsIgnoreCase(action)) forward = setToDisPass();
         else if (PASS.equalsIgnoreCase(action)) forward = setToPass();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
      return forward;
   }

   private void initForm() {
      setCode(form, "COMMENTARY_STATUS");
      setCode(form, "DATE_YEAR");
      setCode(form, "DATE_MONTH");
   }

   /**
    * 评论列表页面
    */
   public String commentaryList() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'commentaryList()' method");
      return LIST;
   }

   /**
    * 评论设为不通过
    */
   public String setToDisPass() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'setToDisPass()' method");
      ArtCommentary commentary = artCommentaryService.getArtCommentary(Integer.valueOf(ids));
      if (check(commentary, "2")) {
         setResponse("{exit:1,message:\"该评论已经处于未通过状态\"}");
      }
      else {
         commentary.setCommentaryStatus("2");
         commentary.setUserId(Integer.valueOf(currentUser.getUserId()));
         commentary.setCommentaryTime(new Date());
         artCommentaryService.updateArtCommentary(commentary);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "用户留言管理", "不通过评论：" + commentary.getContent());
         setResponse("{exit:0}");
      }
      return null;
   }

   /**
    * 评论设为通过
    */
   public String setToPass() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'setToPass()' method");
      ArtCommentary commentary = artCommentaryService.getArtCommentary(Integer.valueOf(ids));
      if (check(commentary, "1")) {
         setResponse("{exit:1,message:\"该评论已经处于通过状态\"}");
      }
      else {
         commentary.setCommentaryStatus("1");
         commentary.setUserId(Integer.valueOf(currentUser.getUserId()));
         commentary.setCommentaryTime(new Date());
         artCommentaryService.updateArtCommentary(commentary);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "用户留言管理", "通过评论：" + commentary.getContent());
         setResponse("{exit:0}");
      }
      return null;
   }

   /**
    * 检查评论的状态
    */
   private boolean check(ArtCommentary commentary, String commentaryStatus) {
      if (commentaryStatus.equals(commentary.getCommentaryStatus())) return true;
      else return false;
   }

   /**
    * 评论列表数据
    */
   public String commentaryPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'commentaryPage()' method");
      try {
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery = artCommentaryService.queryArtCommentary(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> recordSet = pageQuery.getRecordSet();
            if (recordSet != null) {
               List<Map<String, Object>> gridRowList = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < recordSet.size(); i++) {
                  Map<String, Object> map = recordSet.get(i);
                  Map<String, Object> row = new HashMap<String, Object>();
                  
                  String commentaryStatus = this.findCodeName(form, "COMMENTARY_STATUS", map.get("commentaryStatus") + "");
                  String folderName = map.get("folderName").toString();
                  String thumbnailName = map.get("thumbnail").toString();
                  String thumbnailPath = String.format(thumbnailPathFormat, request.get("CONTEXT_PATH"),folderName, thumbnailName);
                  String imgPath = String.format(imgPathFormat, request.get("CONTEXT_PATH"),folderName,thumbnailName);
                  String cell = String.format(cellImgFormat, imgPath,thumbnailPath);
                  
                  String commentaryTime = map.get("commentaryTime")==null?"":dateFormat.format(map.get("commentaryTime"));
                  
                  row.put(ArtCommentary.PROP_ID, map.get(ArtCommentary.PROP_ID));
                  row.put("data",
                        new Object[] { 
                              ""
                              , cell
                              , map.get("worksCName")
                              , map.get("worksEName")
                              , map.get("appAccountName")
                              , map.get("userAccount")
                              , commentaryTime
                              , commentaryStatus
                              , map.get("content")
                         });
                  gridRowList.add(row);
               }
               String res = genGridJson(pageQuery, gridRowList);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "用户留言管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
               setResponse(res);
            }
         }
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

   private Log                  log = LogFactory.getLog(AppCommentaryAction.class);

   @Resource
   private ArtCommentaryService artCommentaryService;
   
   @Resource
   private ArtSysLogService              artSysLogService;
   
   private DateFormat             dateFormat    = new SimpleDateFormat("yyyy-MM-dd");

   private String                 cellImgFormat   = "<a href='%s' target='_blank'><img src='%s' /></a>";

   private String                 thumbnailPathFormat = "%supload/auction/%s/thumbnail/%s";

   private String                 imgPathFormat   = "%supload/auction/%s/%s";
}
