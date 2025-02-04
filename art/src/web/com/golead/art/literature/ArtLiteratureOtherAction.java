package com.golead.art.literature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.golead.art.literature.model.ArtLiteratureOther;
import com.golead.art.literature.model.ArtLiteratureOtherResearch;
import com.golead.art.literature.service.ArtLiteratureOtherResearchService;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksOther;
import com.golead.art.works.service.ArtWorksOtherService;
import com.golead.art.works.service.ArtWorksService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.literature.service.ArtLiteratureOtherService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtLiteratureOtherAction extends BaseAction {

   private Log                               log                 = LogFactory.getLog(ArtLiteratureOtherAction.class);

   public static String                      FILE_PATH           = ServletActionContext.getServletContext().getRealPath("upload/literature/other");

   private File                              importFile;

   private List<File>                        files;

   private String                            filesFileName;

   private String                            attachment;

   private List<Map<String, String>>         cookie              = new ArrayList<Map<String, String>>();

   @Resource
   private ArtLiteratureOtherService         artLiteratureOtherService;

   @Resource
   private ArtLiteratureOtherResearchService artLiteratureOtherResearchService;

   @Resource
   private ArtWorksOtherService              artWorksOtherService;

   @Resource
   private ArtArtistService                  artArtistService;

   @Resource
   private ArtWorksService                   artWorksService;

   @Resource
   private ArtSysLogService                  artSysLogService;

   private String                            cellImgFormat       = "<a href='%s' target='_blank'><img src='%s' /></a>";

   private String                            thumbnailPathFormat = "%supload/auction/%s/thumbnail/%s";

   private String                            imgPathFormat       = "%supload/auction/%s/%s";

   private String                            viewFormat          = "^javascript:view(%s)^_self";

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      initForm();
      if (action == null) {
         action = LIST;
      }
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = list();
         else if (PAGE.equalsIgnoreCase(action)) forward = page();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
         else if (EDIT.equalsIgnoreCase(action)) forward = edit();
         else if (UPDATE.equalsIgnoreCase(action)) forward = update();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();

         else if ("RELATED_WORKS".equalsIgnoreCase(action)) forward = relatedWorks();
         else if ("RELATED_WORKS_PAGE".equalsIgnoreCase(action)) forward = relatedWorksPage();
         else if ("RELATED_WORKS_ADD".equalsIgnoreCase(action)) forward = relatedWorksAdd();
         else if ("RELATED_WORKS_ADD_PAGE".equalsIgnoreCase(action)) forward = relatedWorksAddPage();
         else if ("RELATED_WORKS_SAVE".equalsIgnoreCase(action)) forward = relatedWorksSave();
         else if ("RELATED_WORKS_DELETE".equalsIgnoreCase(action)) forward = relatedWorksDelete();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   private String getValue(Map<String, Object> map, String key) {
      Object value = map.get(key);
      return value == null || "".equals(value) ? "" : value.toString();
   }

   private String getCell(Map<String, Object> item) {
      String folderName = getValue(item, "folderName");
      String thumbnailName = getValue(item, "thumbnail");
      String cell = "暂无图片";
      if (!thumbnailName.equals("")) {
         String thumbnailPath = String.format(thumbnailPathFormat, request.get("CONTEXT_PATH"), folderName, thumbnailName);
         String imgPath = String.format(imgPathFormat, request.get("CONTEXT_PATH"), folderName, thumbnailName);
         cell = String.format(cellImgFormat, imgPath, thumbnailPath);
      }
      return cell;
   }

   /**
    * 此注释以下属于其他收录的相关作品操作
    */
   public String relatedWorks() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'relatedWorks' method");
      return "RELATED_WORKS";
   }

   public String relatedWorksPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'relatedWorksPage' method");
      try {
         PageQuery pageQuery = getForm().getQuery();
         pageQuery = artWorksOtherService.queryArtWorksOther(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String cell = getCell(item);
            String worksCname = getValue(item, "worksCname");
            String worksEname = getValue(item, "worksEname");
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cell, worksCname, worksEname });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录相关作品管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String relatedWorksAdd() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'relatedWorksAdd' method");
      return "RELATED_WORKS_ADD";
   }

   public String relatedWorksAddPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'relatedWorksAddPage' method");
      try {
         PageQuery pageQuery = getForm().getQuery();
         pageQuery = artLiteratureOtherResearchService.queryArtLiteratureOtherRelatedWorks(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String cell = getCell(item);
            String cName = getValue(item, "cName");
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cell, cName });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录相关作品管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String relatedWorksSave() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'relatedWorksSave' method");

      StringBuffer log = new StringBuffer();

      Map<String, String> record = form.getRecord();
      Integer otherId = Integer.valueOf(record.get("otherId"));//
      ArtLiteratureOther artLiteratureOther = artLiteratureOtherService.getArtLiteratureOther(otherId);
      String[] split = ids.split(",");
      List<ArtWorksOther> artWorksOthers = new ArrayList<ArtWorksOther>();

      log.append(artLiteratureOther.getLiteratureTitle()).append(":").append("[");

      for (int i = 0; i < split.length; i++) {
         Integer worksId = Integer.valueOf(split[i]);
         ArtWorksOther artWorksOther = new ArtWorksOther();
         artWorksOther.setOtherId(otherId);
         artWorksOther.setWorksId(worksId);
         artWorksOthers.add(artWorksOther);

         ArtWorks artWorks = artWorksService.getArtWorks(worksId);
         log.append(artWorks.getWorksCName());
         if (i != (split.length - 1)) log.append(",");
      }

      log.append("]");
      artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录相关作品管理", "添加其他收录相关作品：" + log.toString());
      artWorksOtherService.createArtWorksOther(artWorksOthers);
      return returnForward(RETURN_NORMAL);
   }

   public String relatedWorksDelete() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'relatedWorksDelete' method");
      try {
         StringBuffer log = new StringBuffer();
         String[] split = ids.split(",");
         String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "other";
         Integer[] del_ids = new Integer[split.length];
         for (int i = 0; i < split.length; i++) {
            del_ids[i] = Integer.valueOf(split[i]);

            ArtWorksOther artWorksOther = artWorksOtherService.getArtWorksOther(del_ids[i]);
            ArtLiteratureOther artLiteratureOther = artLiteratureOtherService.getArtLiteratureOther(artWorksOther.getOtherId());
            ArtWorks artWorks = artWorksService.getArtWorks(artWorksOther.getWorksId());
            log.append("[").append(artLiteratureOther.getLiteratureTitle()).append(":").append(artWorks.getWorksCName()).append("]");
            if (log.length() > 0) log.append(",");
         }
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录相关作品管理", "删除其他收录相关作品：" + log.toString());
         artWorksOtherService.deleteArtWorksOthers(del_ids);
         setResponse("{exit:0}");
         return null;
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"删除操作失败!\"}");
      }
      return null;
   }

   /**
    * 此注释以下的部分属于其他收录的操作
    */
   public String list() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'list' method");
      return LIST;
   }

   public String page() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'page' method");
      try {
         PageQuery pageQuery = getForm().getQuery();
         pageQuery = artLiteratureOtherService.queryArtLiteratureOther(pageQuery);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pageQuery.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String cName = getValue(item, "cName");
            String literatureTitle = getValue(item, "literatureTitle");
            String currerTime = getValue(item, "currerTime");
            String attachmentSource = getValue(item, "attachmentSource");
            String press = getValue(item, "press");
            String publicationPeriod = getValue(item, "publicationPeriod");
            String pubName = getValue(item, "pubName");
            String relevantPages = getValue(item, "relevantPages");
            String auther = getValue(item, "auther");
            map.put("id", item.get("id"));
            map.put("data", new Object[] { "", cName, literatureTitle + String.format(viewFormat, item.get("id")), auther, currerTime, pubName, press,
                  publicationPeriod, relevantPages, attachmentSource });
            list.add(map);
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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
      if (log.isDebugEnabled()) log.debug("Entering 'addMedium' method");
      try {
         return "ADD";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String save() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveMedium' method");
      try {
         Map<String, String> record = form.getRecord();

         ArtLiteratureOther artLiteratureOther = new ArtLiteratureOther();
         ConvertUtil.mapToObject(artLiteratureOther, record, false);
         artLiteratureOtherService.createArtLiteratureOther(artLiteratureOther);
         String subjectiveEval = record.get(ArtLiteratureOtherResearch.PROP_SUBJECTIVE_EVAL);
         String objectiveEval = record.get(ArtLiteratureOtherResearch.PROP_OBJECTIVE_EVAL);
         String coreThesis = record.get(ArtLiteratureOtherResearch.PROP_CORE_THESIS);

         ArtLiteratureOtherResearch artLiteratureOtherResearch = new ArtLiteratureOtherResearch();
         artLiteratureOtherResearch.setCoreThesis(coreThesis);
         artLiteratureOtherResearch.setObjectiveEval(objectiveEval);
         artLiteratureOtherResearch.setSubjectiveEval(subjectiveEval);
         artLiteratureOtherResearch.setOtherId(artLiteratureOther.getId());
         artLiteratureOtherResearchService.createArtLiteratureOtherResearch(artLiteratureOtherResearch);

         if (files != null) artLiteratureOtherService.saveFile(artLiteratureOther, files, filesFileName, FILE_PATH);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录管理", "添加其他收录：" + "[" + artLiteratureOther.getLiteratureTitle() + "]");
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
      if (log.isDebugEnabled()) log.debug("Entering 'editMedium' method");
      try {
         Map<String, String> record = form.getRecord();

         ArtLiteratureOther artLiteratureOther = artLiteratureOtherService.getArtLiteratureOther(Integer.valueOf(ids));
         ArtLiteratureOtherResearch artLiteratureOtherResearch = artLiteratureOtherResearchService
               .getArtLiteratureOtherResearchByArtLiteratureOtherId(artLiteratureOther.getId());
         ArtArtist artArtist = artArtistService.getArtArtist(artLiteratureOther.getArtistId());
         ConvertUtil.objectToMap(record, artLiteratureOtherResearch);
         ConvertUtil.objectToMap(record, artLiteratureOther);
         form.getRecord().put("artistName", artArtist.getCname());
         form.getRecord().put("artistId", artArtist.getId().toString());
         String attachment = artLiteratureOther.getAttachment();
         if (attachment != null) {
            String[] attachments = attachment.split(",");
            for (int i = 0; i < attachments.length; i++) {
               if (attachments[i].split("/").length == 2) {
                  Map<String, String> map = new HashMap<String, String>();
                  map.put("name", attachments[i].split("/")[0]);
                  map.put("saveName", attachments[i].split("/")[1]);
                  cookie.add(map);
               }
            }
         }

         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String update() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateMedium' method");
      try {
         Map<String, String> record = form.getRecord();
         Integer literatureOtherId = Integer.valueOf(record.get("id"));
         ArtLiteratureOther artLiteratureOther = artLiteratureOtherService.getArtLiteratureOther(literatureOtherId);
         String[] filesName = filesFileName != null ? filesFileName.split(",") : null;
         String[] attachmentName = attachment != null ? attachment.split(",") : null;
         artLiteratureOtherService.updateArtLiteratureOther(record, files, filesName, attachmentName, FILE_PATH);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录管理", "修改其他收录：" + artLiteratureOther.getLiteratureTitle());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(getForm(), e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String delete() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteMedium' method");
      try {
         StringBuffer log = new StringBuffer();
         String[] split = ids.split(",");
         String path = FILE_PATH + File.separator + "upload" + File.separator + "literature" + File.separator + "other";
         Integer[] del_ids = new Integer[split.length];
         for (int i = 0; i < split.length; i++) {
            del_ids[i] = Integer.valueOf(split[i]);
            artLiteratureOtherResearchService.deleteArtLiteratureOtherResearchByOtherId(del_ids[i]);
            deleteFile(path);
         }
         artLiteratureOtherService.deleteArtLiteratureOthers(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "其他收录管理", "其他收录管理", "删除其他收录：" + log.toString());
         setResponse("{exit:0}");
         return null;
      }
      catch (ServiceException e) {
         setResponse("{exit:1,message:\"" + e.getMessage() + "\"}");
      }
      catch (Exception e) {
         setResponse("{exit:1,message:\"删除操作失败!\"}");
      }
      return null;
   }

   private void deleteFile(String path) throws Exception {
      try {
         File file = new File(path);
         if (file.isFile()) {
            file.delete();
         }
         else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
               deleteFile(files[i].getPath());
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   public String view() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewMedium' method");
      try {
         Map<String, String> record = form.getRecord();
         ArtArtist artArtist = artArtistService.getArtArtist(Integer.valueOf(ids));
         ConvertUtil.convertToModel(artArtist, record);
         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private String artist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'artist' method");
      try {
         List<ArtArtist> artArtists = artArtistService.findAllArtArtist();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtArtist artArtist : artArtists) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artArtist);
            if ((";" + ids).indexOf(";" + map.get("id") + ";") > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "ARTIST";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   public void initForm() throws Exception {
      setCode(getForm(), "COMMENT_LEVEL");
   }

   public File getImportFile() {
      return importFile;
   }

   public void setImportFile(File importFile) {
      this.importFile = importFile;
   }

   public List<File> getFiles() {
      return files;
   }

   public void setFiles(List<File> files) {
      this.files = files;
   }

   public String getFilesFileName() {
      return filesFileName;
   }

   public void setFilesFileName(String filesFileName) {
      this.filesFileName = filesFileName;
   }

   public List<Map<String, String>> getCookie() {
      return cookie;
   }

   public void setCookie(List<Map<String, String>> cookie) {
      this.cookie = cookie;
   }

   public String getAttachment() {
      return attachment;
   }

   public void setAttachment(String attachment) {
      this.attachment = attachment;
   }
}
