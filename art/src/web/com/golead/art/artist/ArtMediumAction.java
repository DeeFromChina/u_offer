package com.golead.art.artist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.service.ArtMediumService;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtMediumAction extends BaseAction {

   private final Log        logger = LogFactory.getLog(ArtMediumAction.class);

   private String           id;

   private SimpleDateFormat sdf    = new SimpleDateFormat("yyyy-MM-dd");

   @Resource
   private ArtMediumService artMediumService;

   @Resource
   private ArtWorksService  artWorksService;

   @Resource
   private ArtArtistService artArtistService;

   @Resource
   private ArtSysLogService artSysLogService;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         initForm();
         form.getRecord();
         if (LIST.equalsIgnoreCase(action)) forward = LIST;
         else if ("GETTREE".equalsIgnoreCase(action)) forward = getTreeNode(); // 打开树
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageDate();
         else if (ADD.equalsIgnoreCase(action)) forward = addWorks();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveWorks();
         else if (EDIT.equalsIgnoreCase(action)) forward = editWorks();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateWorks();
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
      setCode(form, "ART_TYPE,DATE_YEAR,MEDIUM_TYPE,SIGNATURE");
   }

   public String list() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'list' method");
      try {

      }
      catch (Exception e) {
         // TODO: handle exception
      }
      return LIST;
   }

   public String getTreeNode() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getTree' method");
      HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
      response.setContentType("text/xml;charset=UTF-8");
      response.setHeader("Cache_Control", "no-cache");
      try {
         PageQuery pageQuery = form.getQuery();
         if (id.equals("0") && form.getRecord().get("mediumCategory") != null) {
            response.getWriter().write(getTopNodes(form.getRecord().get("mediumCategory")));
         }
         else if (id.indexOf("material") == 0) {
            String mediumId = id.split("_")[2];
            String treeCode = id.split("_")[1];
            if (treeCode.equals("m1")) {
               mediumId = null;
            }
            pageQuery.getParameters().put("upId", mediumId);
            pageQuery.getParameters().put("mediumCategory", form.getRecord().get("mediumCategory"));
            pageQuery = artMediumService.queryArtMedium(pageQuery);
            response.getWriter().write(getDeptsNodes(pageQuery.getRecordSet(), "1"));
         }
         else if (id.indexOf("shape") == 0) {
            String shapeId = id.split("_")[2];
            String treeCode = id.split("_")[1];
            if (treeCode.equals("s1")) {
               shapeId = null;
            }
            pageQuery.getParameters().put("upId", shapeId);
            pageQuery.getParameters().put("mediumCategory", form.getRecord().get("mediumCategory"));
            pageQuery = artMediumService.queryArtMedium(pageQuery);
            response.getWriter().write(getDeptsNodes(pageQuery.getRecordSet(), "2"));
         }
         response.getWriter().close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private String getDeptsNodes(List<Map<String, Object>> list, String category) {
      StringBuffer sb = new StringBuffer("");
      sb.append("<?xml version='1.0' encoding='UTF-8' ?>");
      sb.append("<tree id=\"" + id + "\">");
      if ("1".equals(category)) {
         for (Map<String, Object> map : list) {
            sb.append("<item id=\"material_" + map.get("treeCode") + "_" + map.get("id") + "\" text=\"" + map.get("mediumName")
                  + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\" child=\"1\">");
            sb.append("<userdata name=\"name\">" + map.get("mediumName") + "</userdata>");
            sb.append("<userdata name=\"category\">" + map.get("mediumCategory") + "</userdata>");
            sb.append("<userdata name=\"materialId\">" + map.get("id") + "</userdata>");
            sb.append("<userdata name=\"parentId\">"
                  + (map.get("upId") == null ? "material_m1_1" : ("material_" + map.get("upId").toString() + "_" + map.get("id"))) + "</userdata>");
            sb.append("<userdata name=\"treeCode\">" + (map.get("treeCode") == null ? "" : map.get("treeCode").toString()) + "</userdata>");
            sb.append("</item>");
         }
      }
      else if ("2".equals(category)) {
         for (Map<String, Object> map : list) {
            sb.append("<item id=\"shape_" + map.get("treeCode") + "_" + map.get("id") + "\" text=\"" + map.get("mediumName")
                  + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\" child=\"1\">");
            sb.append("<userdata name=\"name\">" + map.get("mediumName") + "</userdata>");
            sb.append("<userdata name=\"category\">" + map.get("mediumCategory") + "</userdata>");
            sb.append("<userdata name=\"shapeId\">" + map.get("id") + "</userdata>");
            sb.append("<userdata name=\"parentId\">" + (map.get("upId") == null ? "shape_s1_1" : ("shape_" + map.get("upId").toString() + "_" + map.get("id")))
                  + "</userdata>");
            sb.append("<userdata name=\"treeCode\">" + (map.get("treeCode") == null ? "" : map.get("treeCode").toString()) + "</userdata>");
            sb.append("</item>");
         }
      }
      sb.append("</tree>");
      return sb.toString();
   }

   private String getTopNodes(String mediumCategory) {
      StringBuffer sb = new StringBuffer("");
      sb.append("<?xml version='1.0' encoding='UTF-8' ?>");
      sb.append("<tree id=\"0\">");
      ArtMedium artMedium = new ArtMedium();
      if (mediumCategory.equals("1")) {
         artMedium = artMediumService.findByTreeCode("m1");
         if (artMedium == null) {
         }
         sb.append("<item id=\"material_" + "m1" + "_" + "1" + "\" text=\"" + "材料"
               + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\" child=\"1\" open=\"1\">");
         sb.append("<userdata name=\"name\">" + "材料" + "</userdata>");
         sb.append("<userdata name=\"category\">" + "1" + "</userdata>");
         sb.append("<userdata name=\"parentId\">" + "" + "</userdata>");
         sb.append("<userdata name=\"treeCode\">" + "m1" + "</userdata>");
         sb.append("</item>");
      }
      else if (mediumCategory.equals("2")) {
         sb.append("<item id=\"shape_" + "s1" + "_" + "2" + "\" text=\"" + "形式"
               + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\" child=\"1\" open=\"1\">");
         sb.append("<userdata name=\"name\">" + "形式" + "</userdata>");
         sb.append("<userdata name=\"category\">" + "2" + "</userdata>");
         sb.append("<userdata name=\"parentId\">" + "" + "</userdata>");
         sb.append("<userdata name=\"treeCode\">" + "s1" + "</userdata>");
         sb.append("</item>");
      }
      sb.append("</tree>");
      return sb.toString();
   }

   private String getPageDate() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageDate' method");
      try {
         initForm();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         PageQuery pageQuery = form.getQuery();
         pageQuery = artWorksService.queryArtWorks(pageQuery);
         if (pageQuery.getRecordSet() != null) {
            for (Map<String, Object> item : pageQuery.getRecordSet()) {
               Map<String, Object> map = new HashMap<String, Object>();
               String artistlink = "^javascript:artistview(" + item.get("artistId") + ");^_self";
               String worklink = "^javascript:workview(" + item.get("id") + ");^_self";
               map.put("id", item.get("id"));
               map.put("data",
                     new Object[] { "", item.get("c_name") + "(" + item.get("e_name") + ")" + artistlink, item.get("no") + worklink, item.get("chineseName"),
                           item.get("englishName"), item.get("styleType").toString(), item.get("worksSeries"), item.get("worksTheme"),
                           sdf.format(item.get("createTime")), item.get("sizeIn") });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "媒介管理", "媒介信息管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String addWorks() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addWorks' method");
      String forward = null;
      try {
         initForm();
         if (!form.getRecord().get("treeCode").equals("m1") && !form.getRecord().get("treeCode").equals("s1")) {
            form.getRecord().put("pid", form.getRecord().get("pid"));
         }
         return ADD;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String saveWorks() throws Exception {
      try {
         ArtMedium parethArtMedium = new ArtMedium();
         if (form.getRecord().get("treeCode").equals("m1") || form.getRecord().get("treeCode").equals("s1")) {
            parethArtMedium.setTreeLevel(1);
            parethArtMedium.setId(null);
            if (form.getRecord().get("treeCode").equals("m1")) {
               parethArtMedium.setMediumCategory("1");
            }
            else if (form.getRecord().get("treeCode").equals("s1")) {
               parethArtMedium.setMediumCategory("2");
            }
         }
         else {
            parethArtMedium = artMediumService.getArtMedium(Integer.valueOf(form.getRecord().get("pid")));
         }

         ArtMedium artMedium = new ArtMedium();
         artMedium.setMediumName(form.getRecord().get("chineseName"));
         String TreeCode = "";
         if ("1".equals(parethArtMedium.getMediumCategory())) {
            TreeCode = "m" + String.valueOf(parethArtMedium.getTreeLevel() + 1);
         }
         else if ("2".equals(parethArtMedium.getMediumCategory())) {
            TreeCode = "s" + String.valueOf(parethArtMedium.getTreeLevel() + 1);
         }
         artMedium.setMediumCategory(parethArtMedium.getMediumCategory());
         artMedium.setTreeCode(TreeCode);
         artMedium.setUpMediumId(parethArtMedium.getId());
         artMedium.setTreeLevel(parethArtMedium.getTreeLevel() + 1);
         artMedium.setMediumSide("1");
         artMediumService.createArtMedium(artMedium);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "媒介管理", "媒介信息管理", "新增媒介：" + artMedium.getMediumName());
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         initForm();
         return ADD;
      }
   }

   private String editWorks() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
      String forward = null;
      try {
         initForm();
         ArtMedium artMedium = artMediumService.getArtMedium(Integer.valueOf(form.getRecord().get("id")));
         ConvertUtil.mapToObject(artMedium, form.getRecord(), true);
         form.getRecord().put("mediumName", artMedium.getMediumName());
         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String updateWorks() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'updateWorks' method");
      try {
         initForm();
         form.getRecord().put("mediumName", form.getRecord().get("chineseName"));
         artMediumService.updateArtMedium(form.getRecord());
         artSysLogService.createArtSysLog(currentUser.getUserName(), "媒介管理", "媒介信息管理", "修改媒介：" + form.getRecord().get("mediumName"));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'view' method");
      try {
         initForm();
         String id = form.getRecord().get("id");
         ArtWorks works = artWorksService.getArtWorks(Integer.valueOf(id));
         ConvertUtil.objectToMap(getForm().getRecord(), works);
         ArtArtist artist = artArtistService.getArtArtist(works.getArtistId());
         form.getRecord().put("artArtistName", artist.getCname());
         form.getRecord().put("artArtistId", artist.getId().toString());
         form.getRecord().put("chineseName", works.getWorksCName());
         form.getRecord().put("englishName", works.getWorksEName());
         form.getRecord().put("no", works.getWorksNo());
         String partSize = works.getPartSize();
         String[] partSizes = partSize.split(";");
         for (int i = 0; i < partSizes.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            String size = partSizes[i];
            String[] sizes = size.split("\\*");
            String partSize_l = sizes[0];
            String partSize_w = sizes[1];
            map.put("l_" + String.valueOf(i), partSize_l);
            map.put("w_" + String.valueOf(i), partSize_w);
            map.put("id", String.valueOf(i));
         }
      }
      catch (Exception e) {
         initForm();
         return ERROR;
      }
      return VIEW;
   }

   private String delete() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'delete' method");
      HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
      response.setContentType("text/plain;charset=UTF-8");
      response.setHeader("Cache_Control", "no-cache");
      StringBuffer sb = new StringBuffer();
      StringBuffer log = new StringBuffer();
      try {
         String id = form.getRecord().get("id");
         List<ArtMedium> artMediums = artMediumService.findChild(Integer.valueOf(id));
         int count = artWorksService.findByMedium(id);
         if(count > 0){
            throw new Exception("作品正在使用该媒介,不能删除！");
         }
         Integer[] dels = new Integer[artMediums.size() + 1];
         dels[0] = Integer.valueOf(id);
         artMediumService.deleteArtMediums(dels);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "媒介管理", "媒介信息管理", "删除媒介：" + log.toString());
         sb.append("{\"exit\":\"0\"}");
      }
      catch (Exception e) {
         if (e.getMessage() != null) {
            sb.append(JsonErrorMessage(1, e.getMessage()));
         }
         else {
            sb.append(JsonErrorMessage(1, "媒介删除失败！"));
         }
      }
      response.getWriter().write(sb.toString());
      response.getWriter().close();
      return null;
   }

   protected String JsonErrorMessage(int errNo, String msg) {
      return "{\"exit\":\"" + errNo + "\",\"message\":\"" + msg + "\"}";
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

}
