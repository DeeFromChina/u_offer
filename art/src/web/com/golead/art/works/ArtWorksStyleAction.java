package com.golead.art.works;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksStyleService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

/**
 * 作品风格
 * 
 * @author 尹湘
 * @version 1.0
 * @since 2016年9月3日 下午2:38:13
 */
public class ArtWorksStyleAction extends BaseAction {

   private Log              log         = LogFactory.getLog(ArtWorksStyleAction.class);
   private SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      if (action == null) action = LIST;
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = sreachStyle();
         else if (PAGE.equalsIgnoreCase(action)) forward = pageStyle();
         else if (ADD.equalsIgnoreCase(action)) forward = addStyle();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveStyle();
         else if (EDIT.equalsIgnoreCase(action)) forward = editStyle();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateStyle();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteStyle();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewStyle();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String sreachStyle() throws Exception {
      return LIST;
   }

   public String pageStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageStyle' method");
      try {
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery = artWorksStyleService.queryArtWorksStyle(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> rs = pageQuery.getRecordSet();
            if (rs != null) {
               List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < rs.size(); i++) {
                  Map<String, Object> style = rs.get(i);
                  Map<String, Object> map = new HashMap<String, Object>();
                  map.put("id", style.get("id"));
                  String link = "^javascript:view(" + style.get("id") + ")^_self";
                  map.put("data",
                        new Object[] { "", style.get("styleName") + link, style.get("styleDesc"), style.get("createPerson"), dateFormate.format(style.get("createTime")) });
                  list.add(map);
               }
               String res = genGridJson(pageQuery, list);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品风格管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String addStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addStyle' method");
      try {
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String saveStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveStyle' method");
      try {
         ArtWorksStyle style = new ArtWorksStyle();
         ConvertUtil.mapToObject(style, form.getRecord(), false);
         style.setCreatePerson(currentUser.getUserName());
         style.setCreateTime(new Date());
         artWorksStyleService.createArtWorksStyle(style);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品风格管理", "添加风格：" + style.getStyleName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(getForm(), e.getMessage());
         return ADD;
      }
   }

   public String editStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editStyle' method");
      try {
         ArtWorksStyle style = artWorksStyleService.getArtWorksStyle(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), style);
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String updateStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateStyle' method");
      try {
         Map<String, String> record = form.getRecord();
         record.put("updatePerson", currentUser.getUserName());
         record.put("updateTime", dateFormate.format(new Date()));
         artWorksStyleService.updateArtWorksStyle(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品风格管理", "修改风格：" + record.get(ArtWorksStyle.PROP_STYLE_NAME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         return EDIT;
      }
   }

   public String deleteStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteStyle' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < del_ids.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtWorksStyle artWorksStyle = artWorksStyleService.getArtWorksStyle(del_ids[i]);
            if(log.length()>0)log.append(",");
            log.append(artWorksStyle.getStyleName());
         }
         artWorksStyleService.deleteArtWorksStyles(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品风格管理", "删除风格：" + log.toString());
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

   public String viewStyle() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewStyle' method");
      String forward = null;
      try {
         ArtWorksStyle style = artWorksStyleService.getArtWorksStyle(Integer.valueOf(ids));
         ConvertUtil.objectToMap(form.getRecord(), style);
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
   private ArtWorksStyleService artWorksStyleService;
   
   @Resource
   private ArtSysLogService          artSysLogService;
}
