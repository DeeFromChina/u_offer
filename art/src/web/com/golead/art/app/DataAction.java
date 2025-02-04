package com.golead.art.app;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.app.appUser.service.ArtAppUserService;
import com.golead.art.app.service.DataUploadService;
import com.golead.art.app.service.QueryService;
import com.golead.art.app.service.SaveService;
import com.golead.core.exception.ServiceException;

public class DataAction extends BaseAction {

   private final Log log = LogFactory.getLog(DataAction.class);
   private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();
      if (log.isDebugEnabled()) log.debug("action:" + action);
      try {
         if (action.equalsIgnoreCase("query")) forward = queryData();//查询数据
         else if (action.equalsIgnoreCase("save")) forward = saveData();//上传数据
         else {
            setResponse("{\"exit\":\"1\",\"message\":\"找不到该action方法:" + action + "\"}");
         }
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         setResponse("{\"exit\":\"1\",\"message\":\"服务器错误\"}");
      }
      return forward;
   }

   private String queryData() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering queryData method");
      try {
         if (parameters == null) parameters = form.getQuery().getParameters();
         String res = queryService.sreachData(parameters);
         if(res.startsWith("error:")){
            res = res.replace("error:", "");
            throw new ServiceException(res);
         }
         setResponse("{\"exit\":\"0\",\"data\":" + res + "}");
      }
      catch (ServiceException e) {
         e.printStackTrace();
         setResponse("{\"exit\":\"1\",\"message\":\""+ e.getMessage() +"\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         setResponse("{\"exit\":\"1\",\"message\":\"数据访问错误。\"}");
      }
      return null;

   }
   
   private String saveData() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering modifyUserPsw method");
      try {
         if (parameters == null) parameters = form.getQuery().getParameters();
         String data = parameters.get("data");
         parameters.put("fileName", photoFileName);
         JSONArray jsonArray = JSONArray.fromObject(data);
         List<Map<String, String>> list = (List<Map<String, String>>) JSONArray.toCollection(jsonArray, HashMap.class);
         for (Map<String, String> map : list) {
            saveService.save(map, photo);
            if(map.get("error") != null){
               throw new Exception(map.get("error"));
            }
         }
         setResponse("{\"exit\":\"0\",\"message\":\"创建用户成功。\"}");
      }
      catch (Exception e) {
         e.printStackTrace();
         if(e.getMessage() != null && "".equals(e.getMessage())){
            setResponse("{\"exit\":\"1\",\"message\":\"" + e.getMessage() + "\"}");
         }else{
            setResponse("{\"exit\":\"1\",\"message\":\"创建不了用户。\"}");
         }
      }
      return null;
   }
   
   
   protected Map<String, String> parameters;

   private String                query;

   @Resource
   private QueryService          queryService;
   
   @Resource
   private SaveService          saveService;

   @Resource
   private DataUploadService     dataUploadService;
   
   @Resource
   private ArtAppUserService        artAppUserService;

   private File                  photo;

   private String                photoFileName;

   public File getFile() {
      return photo;
   }

   public void setFile(File file) {
      this.photo = file;
   }

   public String getFileFileName() {
      return photoFileName;
   }

   public void setFileFileName(String fileFileName) {
      this.photoFileName = fileFileName;
   }

   public String getQuery() {
      return query;
   }

   public void setQuery(String query) {
      this.query = query;
   }

   public Map<String, String> getParameters() {
      return parameters;
   }

   public void setParameters(Map<String, String> parameters) {
      this.parameters = parameters;
   }

}
