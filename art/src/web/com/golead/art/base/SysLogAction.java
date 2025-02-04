package com.golead.art.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

public class SysLogAction extends BaseAction {

   private final Log        log = LogFactory.getLog(SysLogAction.class);

   @Resource
   private ArtSysLogService artSysLogService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (log.isDebugEnabled()) log.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = page();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   private String getValue(Map map, String key) {
      Object value = map.get(key);
      return value == null || "".equals(value) ? "" : value.toString();
   }

   public String page() throws Exception {
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery = artSysLogService.queryArtSysLog(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               String id = getValue(item, "id");
               String operator = getValue(item, "operator");
               String operDate = getValue(item, "operDate");
               String operModule = getValue(item, "operModule");
               String operFunction = getValue(item, "operFunction");
               String operResult = getValue(item, "operResult");
               map.put("id", id);
               map.put("data", new Object[] { operator, operDate, operModule, operFunction, operResult });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         setResponse(res);
      }
      catch (ServiceException e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   public String delete() throws Exception {
      try {
         Map<String, String> record = form.getRecord();
         String beginTime = record.get("beginTime");
         String endTime = record.get("endTime");
         artSysLogService.deleteArtSysLogs(beginTime,endTime);
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      return null;
   }

}
