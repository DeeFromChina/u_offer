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

import com.golead.art.app.artistFollow.service.ArtArtistFollowService;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.web.action.BaseAction;

public class AppArtistFollowAction extends BaseAction {

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();
      initForm();
      if (action == null) forward = LIST;
      try {
         if (LIST.equalsIgnoreCase(action)) forward = artistFollowList();
         else if (PAGE.equalsIgnoreCase(action)) forward = artistFollowPage();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
      return forward;
   }

   private void initForm() {
      setCode(form, "DATE_YEAR");
      setCode(form, "DATE_MONTH");
   }

   /**
    * 艺术家关注列表页面
    */
   public String artistFollowList() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'worksFollowList()' method");
      return LIST;
   }

   /**
    * 艺术家关注列表数据
    */
   public String artistFollowPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'worksFollowPage()' method");
      initForm();
      try {
         PageQuery pageQuery = form.getQuery() == null ? new PageQuery() : form.getQuery();
         pageQuery = artArtistFollowService.queryArtArtistFollow(pageQuery);
         if (pageQuery != null) {
            List<Map<String, Object>> recordSet = pageQuery.getRecordSet();
            if (recordSet != null) {
               List<Map<String, Object>> gridRowList = new ArrayList<Map<String, Object>>();
               for (int i = 0; i < recordSet.size(); i++) {
                  Map<String, Object> map = recordSet.get(i);
                  Map<String, Object> row = new HashMap<String, Object>();

                  String contextPath = request.get("CONTEXT_PATH").toString();
                  String folderName = map.get("folderName") == null || "".equals(map.get("folderName")) ? "" : map.get("folderName") + "";

                  String photoName = map.get("photo") == null || "".equals(map.get("photo")) ? "" : map.get("photo").toString().split("/")[1];

                  String cell = "暂无图片";
                  if (!"".equals(photoName)) {
                     String imgPath = String.format(artistPhotoPathFormat, contextPath, folderName, photoName);

                     String thumbnailPath = String.format(thumbnailPathFormat, contextPath, folderName, photoName);

                     cell = String.format(cellImgFormat, imgPath, thumbnailPath);
                  }
                  String cName = map.get("cName") == null || "".equals(map.get("cName")) ? "" : map.get("cName").toString();

                  String eName = map.get("eName") == null || "".equals(map.get("eName")) ? "" : map.get("eName").toString();

                  String accountName = map.get("accountName") == null || "".equals(map.get("accountName")) ? "" : map.get("accountName").toString();

                  row.put("id", map.get("id"));
                  row.put("data", new Object[] { "", cell, cName, eName, accountName, dateFormat.format((Date) map.get("followTime")) });
                  gridRowList.add(row);
               }
               String res = genGridJson(pageQuery, gridRowList);
               artSysLogService.createArtSysLog(currentUser.getUserName(), "App管理", "艺术家关注管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private Log                    log                   = LogFactory.getLog(AppArtistFollowAction.class);

   @Resource
   private ArtArtistFollowService artArtistFollowService;

   @Resource
   private ArtSysLogService       artSysLogService;

   private DateFormat             dateFormat            = new SimpleDateFormat("yyyy-MM-dd");

   private String                 cellImgFormat         = "<a href='%s' target='_blank'><img src='%s' /></a>";
   /**
    * 艺术家照片地址
    */
   private String                 artistPhotoPathFormat = "%supload/photo/%s/%s";
   /**
    * 艺术家照片缩略图地址
    */
   private String                 thumbnailPathFormat   = "%supload/photo/%s/thumbnails/%s";
}
