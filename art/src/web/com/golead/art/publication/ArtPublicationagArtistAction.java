package com.golead.art.publication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.works.model.ArtPublicationArtist;
import com.golead.art.works.model.ArtSysLog;
import com.golead.art.works.service.ArtPublicationArtistService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.golead.art.activity.model.ArtAgency;
import com.golead.art.activity.service.ArtAgencyService;
import com.golead.art.publication.model.ArtPublication;
import com.golead.art.publication.model.ArtPublicationAgency;
import com.golead.art.publication.service.ArtPublicationAgencyService;
import com.golead.art.publication.service.ArtPublicationService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

/**
 * @author JiaHu 2016/12/8.
 */
public class ArtPublicationagArtistAction extends BaseAction {

   private final Log                   logger                = LogFactory.getLog(ArtPublicationagArtistAction.class);

   @Resource
   private ArtSysLogService            artSysLogService;

   @Resource
   private ArtPublicationService       artPublicationService;

   @Resource
   private ArtPublicationArtistService artPublicationArtistService;

   @Resource
   private ArtArtistService            artArtistService;

   private String                      cellImgFormat         = "<a href='%s' target='_blank'><img src='%s' /></a>";
   /**
    * 艺术家照片地址
    */
   private String                      artistPhotoPathFormat = "%supload/photo/%s/%s";
   /**
    * 艺术家照片缩略图地址
    */
   private String                      thumbnailPathFormat   = "%supload/photo/%s/thumbnails/%s";

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();
      initForm();
      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = list();
         else if (PAGE.equalsIgnoreCase(action)) forward = page();
         else if (ADD.equalsIgnoreCase(action)) forward = add();
         else if ("ADD_PAGE".equalsIgnoreCase(action)) forward = addPage();
         else if (SAVE.equalsIgnoreCase(action)) forward = save();
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
      return value == null ? "" : value.toString();
   }

   private void initForm() {

   }

   public String list() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'list' method");
      return LIST;
   }

   public String page() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'page' method");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery = artPublicationArtistService.queryArtPublicationArtist(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               String id = getValue(item, "id");
               String contextPath = request.get("CONTEXT_PATH").toString();
               String folderName = getValue(item, "folderName");
               String photoName = getFileName(getValue(item, "photo"));
               String cell = "暂无图片";
               if (!"".equals(photoName)) {
                  String imgPath = String.format(artistPhotoPathFormat, contextPath, folderName, photoName);
                  String thumbnailPath = String.format(thumbnailPathFormat, contextPath, folderName, photoName);
                  cell = String.format(cellImgFormat, imgPath, thumbnailPath);
               }
               String cName = getValue(item, "cName");
               String eName = getValue(item, "eName");
               map.put("id", id);
               map.put("data", new Object[] { "", cell, cName + "/" + eName });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物相关艺术家管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String add() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'add' method");
      try {
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   private String getFileName(String fileName){
      if(StringUtils.isNotEmpty(fileName)){
         String[] split = fileName.split("/");
         if(split.length>1){
            return split[1];
         }else {
            return split[0];
         }
      }
      return "";
   }

   public String addPage() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'page' method");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery = artPublicationArtistService.queryArtist(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               String id = getValue(item, "id");
               String contextPath = request.get("CONTEXT_PATH").toString();
               String folderName = getValue(item, "folderName");
               String photoName = getFileName(getValue(item, "photo"));
               String cell = "暂无图片";
               if (!"".equals(photoName)) {
                  String imgPath = String.format(artistPhotoPathFormat, contextPath, folderName, photoName);
                  String thumbnailPath = String.format(thumbnailPathFormat, contextPath, folderName, photoName);
                  cell = String.format(cellImgFormat, imgPath, thumbnailPath);
               }
               String cName = getValue(item, "cName");
               String eName = getValue(item, "eName");
               map.put("id", id);
               map.put("data", new Object[] { "", cell, cName + "/" + eName });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "可关联的出版物相关艺术家", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   public String save() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'save' method");
      try {
         Map<String, String> record = form.getRecord();
         Integer publicationId = Integer.valueOf(record.get("publicationId"));
         ArtPublication artPublication = artPublicationService.getArtPublication(publicationId);
         String[] split = ids.split(",");
         List<ArtPublicationArtist> artPublicationArtists = new ArrayList<ArtPublicationArtist>();
         StringBuffer log = new StringBuffer();
         log.append(artPublication.getPublicationName()).append(":").append("[");
         for (int i = 0; i < split.length; i++) {
            ArtArtist artArtist = artArtistService.getArtArtist(Integer.valueOf(split[i]));
            ArtPublicationArtist artPublicationArtist = new ArtPublicationArtist();
            artPublicationArtist.setArtistId(artArtist.getId());
            artPublicationArtist.setPubId(publicationId);
            log.append(artArtist.getCname() + "/" + artArtist.getEname());
            if (i != split.length - 1) log.append(",");
            artPublicationArtists.add(artPublicationArtist);
         }
         log.append("]");
         artPublicationArtistService.createArtPublicationArtist(artPublicationArtists);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物相关艺术家管理", "添加出版物相关艺术家" + log.toString());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return ADD;
      }
   }

   public String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      try {
         String[] split = ids.split(",");
         Integer[] idArray = new Integer[split.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < split.length; i++) {
            idArray[i] = Integer.valueOf(split[i]);
            ArtPublicationArtist artPublicationArtist = artPublicationArtistService.getArtPublicationArtist(idArray[i]);
            ArtPublication artPublication = artPublicationService.getArtPublication(artPublicationArtist.getPubId());
            ArtArtist artArtist = artArtistService.getArtArtist(artPublicationArtist.getArtistId());
            log.append(artPublication.getPublicationName()).append(":").append(artArtist.getCname() + "/" + artArtist.getEname());
            if (log.length() > 0) log.append(",");
         }
         artPublicationArtistService.deleteArtPublicationArtists(idArray);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "出版物管理", "出版物相关艺术家管理", "删除出版物艺术家机构" + log.toString());
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
