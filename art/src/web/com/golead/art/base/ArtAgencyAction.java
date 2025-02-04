package com.golead.art.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtActivityExhibitService;
import com.golead.art.activity.service.ArtAgencyService;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.artist.service.ArtArtistCollectAgencyService;
import com.golead.art.artist.service.ArtCountryService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksAgencyService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtAgencyAction extends BaseAction {

   private final Log                     logger = LogFactory.getLog(ArtAgencyAction.class);

   @Resource
   private ArtAgencyService              artAgencyService;

   @Resource
   private ArtWorksService               artWorksService;
   
   @Resource
   private ArtWorksAgencyService               artWorksAgencyService;

   @Resource
   private ArtCountryService             artCountryService;

   @Resource
   private ArtArtistCollectAgencyService artArtistCollectAgencyService;

   @Resource
   private ArtSysLogService              artSysLogService;

   @Resource
   private ArtActivityExhibitService     artActivityExhibitService;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         initForm();
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageDate();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if ("COUNTRY".equalsIgnoreCase(action)) forward = country();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public void initForm() throws Exception {
      setCode(form, "ART_TYPE,DATE_YEAR,MEDIUM_TYPE,SIGNATURE,PERIOD_TYPE,AGENCY_TYPE");
      List<ArtCountry> artCountries = artCountryService.findAll();
      setCode(form, "COUNTRY", artCountries, ArtCountry.PROP_COUNTRY_NAME, ArtCountry.PROP_ID, true);
   }

   private String getPageDate() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageDate' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery.getParameters().put("id", id);
         pageQuery = artAgencyService.queryArtAgency(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               map.put("id", item.get("id"));
               map.put("data", new Object[] { "", item.get("agencyCName"), item.get("agencyEName"), item.get("countryName"), item.get("city"),
                     findCodeName(form, "AGENCY_TYPE", item.get("agencyType").toString()) });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "机构管理", "机构管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String add() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addWorks' method");
      String forward = null;
      try {
         initForm();
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String save() throws Exception {
      try {
         ArtAgency artAgency = new ArtAgency();
         ConvertUtil.convertToModel(artAgency, form.getRecord());
         artAgencyService.createArtAgency(artAgency);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "机构管理", "机构管理", "添加机构：" + artAgency.getAgencyCName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   private String edit() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
      String forward = null;
      try {
         String id = form.getRecord().get("id");
         ArtAgency artAgency = artAgencyService.getArtAgency(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artAgency);
         ArtCountry artCountry = artCountryService.getArtCountry(artAgency.getCountryId());
         form.getRecord().put("countryName", artCountry.getCountryName());
         form.getRecord().put("country", artCountry.getId().toString());
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String update() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'updateWorks' method");
      try {
         Map<String, String> record = form.getRecord();
         artAgencyService.updateArtAgency(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "机构管理", "机构管理", "修改机构：" + record.get(ArtAgency.PROP_AGENCY_C_NAME));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   private String country() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'country' method");
      try {
         List<ArtCountry> artCountries = artCountryService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtCountry artCountry : artCountries) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artCountry);
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "COUNTRY";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
      String forward = null;
      try {
         String id = form.getRecord().get("id");
         ArtAgency artAgency = artAgencyService.getArtAgency(Integer.valueOf(id));
         ConvertUtil.objectToMap(form.getRecord(), artAgency);
         form.getRecord().put("agencyType", findCodeName(form, "AGENCY_TYPE", artAgency.getAgencyType()));
         if (artAgency.getCountryId() != null) {
            ArtCountry artCountry = artCountryService.getArtCountry(artAgency.getCountryId());
            form.getRecord().put("countryName", artCountry.getCountryName());
            form.getRecord().put("country", artCountry.getId().toString());
         }
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      try {
         String[] idsList = ids.split(",");
         Integer[] delids = new Integer[idsList.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < delids.length; i++) {
            delids[i] = Integer.valueOf(idsList[i]);
            if (log.length() > 0) log.append(",");
            ArtAgency agency = artAgencyService.getArtAgency(delids[i]);
            log.append(agency.getAgencyCName());
         }
         boolean isUsed = false;
         if (artArtistCollectAgencyService.findByAgency(ids) != 0) {
            isUsed = true;
         }
         if (artActivityExhibitService.findByAgency(ids) != 0) {
            isUsed = true;
         }
         if (artWorksAgencyService.findByAgency(ids) != 0) {
            isUsed = true;
         }
         if (!isUsed) {
            artAgencyService.deleteArtAgencys(delids);
            artSysLogService.createArtSysLog(currentUser.getUserName(), "机构管理", "机构管理", "删除机构：" + log.toString());
            setResponse("{exit:0}");
         }
         else {
            setResponse("{exit:1,message:\"有机构在被使用！\"}");
         }
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
