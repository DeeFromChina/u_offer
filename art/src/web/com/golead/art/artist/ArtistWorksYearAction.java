package com.golead.art.artist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksYear;
import com.golead.art.works.model.ArtWorksYearResearch;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.art.works.service.ArtWorksYearResearchService;
import com.golead.art.works.service.ArtWorksYearService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtistWorksYearAction extends BaseAction {

   private Log                 log = LogFactory.getLog(ArtistWorksYearAction.class);

   @Resource
   private ArtWorksYearService artWorksYearService;

   @Resource
   private ArtWorksYearResearchService artWorksYearResearchService;
   
   @Resource
   private ArtWorksService artWorksService;

   @Resource
   private ArtSysLogService              artSysLogService;
   
   private final String REPRESENTATIVE = "REPRESENTATIVE";

   private final String REPRESENTATIVE_PAGE = "REPRESENTATIVE_PAGE";

   private final String REPRESENTATIVE_ADD = "REPRESENTATIVE_ADD";

   private final String REPRESENTATIVE_DELETE = "REPRESENTATIVE_DELETE";

   private final String REPRESENTATIVE_SAVE = "REPRESENTATIVE_SAVE";

   private final String YEAR_WORKS_PAGE = "YEAR_WORKS_PAGE";

   private String cellImgFormat = "<a href='%s' target='_blank'><img src='%s' /></a>";

   private String thumbnailPathFormat = "%supload/auction/%s/thumbnail/%s";

   private String imgPathFormat = "%supload/auction/%s/%s";

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

         else if (REPRESENTATIVE.equalsIgnoreCase(action)) forward = representative();
         else if (REPRESENTATIVE_PAGE.equalsIgnoreCase(action)) forward = representativePage();
         else if (REPRESENTATIVE_ADD.equalsIgnoreCase(action)) forward = representativeAdd();
         else if (REPRESENTATIVE_SAVE.equalsIgnoreCase(action)) forward = representativeSave();
         else if (REPRESENTATIVE_DELETE.equalsIgnoreCase(action)) forward = representativeDelete();
         else if (YEAR_WORKS_PAGE.equalsIgnoreCase(action)) forward = yearWorksPage();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   /**
    * 代表作列表
    */
   public String representative() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'representative()' method");
      return REPRESENTATIVE;
   }

   /**
    * 代表作列表数据
    */
   public String representativePage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'representativePage()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         pageQuery = artWorksYearResearchService.queryArtWorksYearResearch(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            String folderName = item.get("folderName").toString();
            String thumbnail;
            String cell;
            if (item.get("thumbnail") != null) {
               thumbnail = item.get("thumbnail").toString();
               String thumbnailPath = String.format(thumbnailPathFormat, request.get("CONTEXT_PATH"), folderName, thumbnail);
               String imgPath = String.format(imgPathFormat, request.get("CONTEXT_PATH"), folderName, thumbnail);
               cell = String.format(cellImgFormat, imgPath, thumbnailPath);
            } else {
               cell = "暂无图片";
            }
            String worksCName = item.get("worksCName") + "";
            String worksEName = item.get("worksEName") + "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", item.get("id"));
            map.put("data", new Object[]{"", cell, worksCName, worksEName});
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家作品管理", "年代代表作管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
         setResponse(res);
      } catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      } catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   /**
    * 跳转到代表作候选列表
    */
   public String representativeAdd() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'representativeAdd()' method");
      return REPRESENTATIVE_ADD;
   }

   /**
    * 代表作候选列表数据
    */
   public String yearWorksPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'representativeAdd()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         pageQuery = artWorksYearService.queryArtYearWorks(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            String folderName = item.get("folderName").toString();
            String thumbnail;
            String cell;
            if (item.get("thumbnail") != null) {
               thumbnail = item.get("thumbnail").toString();
               String thumbnailPath = String.format(thumbnailPathFormat, request.get("CONTEXT_PATH"), folderName, thumbnail);
               String imgPath = String.format(imgPathFormat, request.get("CONTEXT_PATH"), folderName, thumbnail);
               cell = String.format(cellImgFormat, imgPath, thumbnailPath);
            } else {
               cell = "暂无图片";
            }
            String worksCName = item.get("worksCName") + "";
            String worksEName = item.get("worksEName") + "";
            String createTo = item.get("createTo") + "";
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", item.get("id"));
            map.put("data", new Object[]{"", createTo, cell, worksCName, worksEName});
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家作品管理", "年代代表作候选作品列表", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
         setResponse(res);
      } catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      } catch (Exception e) {
         e.printStackTrace();
         setResponse("{exit:1,message:\"数据访问错误。\"}");
      }
      return null;
   }

   /**
    * 保存代表作列表
    */
   public String representativeSave() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'representativeSave()' method");
      StringBuffer log = new StringBuffer();
      String yearId = form.getRecord().get("yearId");
      String[] worksIds = ids.split(",");
      List<ArtWorksYearResearch> artArtistWorksSeriesResearchList = new ArrayList<ArtWorksYearResearch>();
      for (int i = 0; i < worksIds.length; i++) {
         ArtWorksYearResearch artWorksYearResearch = new ArtWorksYearResearch();
         artWorksYearResearch.setYearId(Integer.valueOf(yearId));
         artWorksYearResearch.setWorksId(Integer.valueOf(worksIds[i]));
         
         ArtWorks artWorks = artWorksService.getArtWorks(Integer.valueOf(worksIds[i]));
         ArtWorksYear artWorksYear = artWorksYearService.getArtWorksYear(Integer.valueOf(yearId));
         if(log.length() > 0) log.append(",");
         log.append(artWorks.getWorksCName()).append(":").append("[").append(artWorksYear.getStartYear()).append(" - ").append(artWorksYear.getEndYear()).append("]");
         artArtistWorksSeriesResearchList.add(artWorksYearResearch);
      }
      artWorksYearResearchService.createArtWorksYearResearch(artArtistWorksSeriesResearchList);
      artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家作品管理", "年代代表作管理", "新增代表作:" + log.toString());
      return returnForward(RETURN_NORMAL);
   }

   /**
    * 删除系列代表作列表
    */
   public String representativeDelete() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'representativeDelete()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         for (int i = 0; i < id.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
         }
         artWorksYearResearchService.deleteArtWorksYearResearchs(del_ids);
         setResponse("{exit:0}");
      } catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      } catch (Exception e) {
         setResponse("{exit:1,message:\"删除操作失败!\"}");
      }
      return null;
   }

   public String list() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'list()' method");
      return LIST;
   }

   public String page() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'pageExperience()' method");
      try {
         PageQuery pageQuery = form.getQuery();
         String artistId = getForm().getRecord().get("artistId");
         pageQuery.getParameters().put("artistId", artistId);
         pageQuery = artWorksYearService.queryArtWorksYear(pageQuery);
         List<Map<String, Object>> list = new ArrayList();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap();
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", item.get("startYear"), item.get("endYear"), this.findCodeName(form, "COMMENT_LEVEL", item.get("yearImportant")+"") });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家年代管理", "年代信息管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
         ArtWorksYear artWorksYear = new ArtWorksYear();
         ConvertUtil.convertToModel(artWorksYear, record);
         artWorksYearService.createArtWorksYear(artWorksYear);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家年代管理", "添加年代信息", "["+ artWorksYear.getStartYear() +" - "+ artWorksYear.getEndYear() +"]");
         ids = artWorksYear.getId() + "";
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
         ArtWorksYear artWorksYear = artWorksYearService.getArtWorksYear(id);
         ConvertUtil.objectToMap(form.getRecord(), artWorksYear);
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
         artWorksYearService.updateArtWorksYear(record);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家年代管理", "添加年代信息", "["+ record.get(ArtWorksYear.PROP_START_YEAR) +" - "+ record.get(ArtWorksYear.PROP_END_YEAR) +"]");
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
         StringBuffer log = new StringBuffer();
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         for (int i = 0; i < id.length; i++) {
            del_ids[i] = Integer.valueOf(id[i]);
            ArtWorksYear worksYear = artWorksYearService.getArtWorksYear(del_ids[i]);
            if(log.length() > 0) log.append(",");
            log.append(":").append("[").append(worksYear.getStartYear()).append(" - ").append(worksYear.getEndYear()).append("]");
         }
         artWorksYearService.deleteArtWorksYears(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家年代管理", "删除年代信息",log.toString());
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

   public String viewExperience() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewExperience()' method");
      try {
         //         ArtArtistExperience artArtistExperience = artArtistExperienceService.getArtArtistExperience(Integer.valueOf(ids));
         //         ConvertUtil.objectToMap(getForm().getRecord(), artArtistExperience);
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return VIEW;
   }

   public void initForm() throws Exception {
      setCode(form, "COMMENT_LEVEL");
   }

}
