package com.golead.art.artist;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.ArtCountryService;
import com.golead.art.util.service.AdministrativeDivisionService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.common.service.system.SysCodeService;
import com.golead.common.service.system.SysUserService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

import net.coobird.thumbnailator.Thumbnails;

public class ArtistInformationAction extends BaseAction {

   private Log                           log       = LogFactory.getLog(ArtistInformationAction.class);

   public static String                  FILE_PATH = ServletActionContext.getServletContext().getRealPath("upload/photo/");
   private File                          files;
   private String                        filesFileName;
   private List<Map<String, String>>     pic       = new ArrayList<Map<String, String>>();

   private int                           id;

   private String[]                      strs;

   @Resource
   private ArtArtistService              artArtistService;

   @Resource
   private AdministrativeDivisionService administrativeDivisionService;

   @Resource
   private SysUserService                sysUserService;

   @Resource
   private SysCodeService                sysCodeService;

   @Resource
   private ArtCountryService             artCountryService;

   @Resource
   private ArtWorksService               artWorksService;

   @Resource
   private ArtSysLogService              artSysLogService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = getForm().getAction();

      if (action == null) {
         action = LIST;
      }
      if (log.isDebugEnabled()) log.debug("action" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = listArtist();
         else if (PAGE.equalsIgnoreCase(action)) forward = page();
         else if (ADD.equalsIgnoreCase(action)) forward = addArtist();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveArtist();
         else if (EDIT.equalsIgnoreCase(action)) forward = editArtist();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateArtist();
         else if (DELETE.equalsIgnoreCase(action)) forward = deleteArtist();
         else if (VIEW.equalsIgnoreCase(action)) forward = viewArtist();
         else if ("PLACE".equalsIgnoreCase(action)) forward = placeList(action);
         else if ("COUNTRY".equalsIgnoreCase(action)) forward = country();
         else if ("IMPORT".equalsIgnoreCase(action)) forward = importArtist();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = downloadTemplate();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String listArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'list()' method");
      initForm();
      return LIST;
   }

   public String page() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'page()' method");
      try {
         initForm();
         PageQuery pq = form.getQuery();
         pq = artArtistService.queryArtArtist(pq);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> item : pq.getRecordSet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            String artistLink = "^javascript:view(" + item.get("id") + ");^_self";
            String birthYear = item.get("birthYear") == null ? "" : item.get("birthYear") + "年";
            String birthMonth = item.get("birthMonth") == null ? "" : item.get("birthMonth") + "月";
            String birthDay = item.get("birthDay") == null ? "" : item.get("birthDay") + "日";
            String deathYear = item.get("deathYear") == null ? "" : item.get("deathYear") + "年";
            String deathMonth = item.get("deathMonth") == null ? "" : item.get("deathMonth") + "月";
            String deathDay = item.get("deathDay") == null ? "" : item.get("deathDay") + "日";
            String zodiac = findCodeName(getForm(), "ZODIAC", item.get("zodiac") + "");
            String artistType = findCodeName(getForm(), "ARTIST_TYPE", item.get("artistType") + "");
            String sex = findCodeName(getForm(), "GENDER", item.get("sex") + "");
            String photo = item.get("photo") == null ? null : item.get("photo") + "";
            String ename = item.get("folderName") == null ? "" : item.get("folderName").toString();
            String photo2 = "";
            if (photo != null && photo.split("/").length > 1) {
               photo2 = request.get("CONTEXT_PATH") + "/upload/photo/" + ename + File.separator + photo.split("/")[1];
               photo = request.get("CONTEXT_PATH") + "/upload/photo/" + ename + File.separator + "thumbnails/" + photo.split("/")[1];
            }
            String str = "<a href='" + photo2 + "' target='_blank'><img src='" + photo + "' /></a>";
            if (photo2.equals("")) {
               str = "暂无图片";
            }
            map.put("id", item.get("id"));
            map.put("data",
                  new Object[] { "", str, item.get("cName") + artistLink, item.get("eName"), sex, birthYear + birthMonth + birthDay,
                        deathYear + deathMonth + deathDay, item.get("birthCountryName"), item.get("birthplace"), item.get("ancestralHome"),
                        item.get("nationalityName"), zodiac, item.get("nhom"), artistType });
            list.add(map);
         }
         String res = genGridJson(pq, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "查询条件为：" + QTool.getParamter(pq.getParameters()));
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

   public String addArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'addArtist()' method");
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

   public String saveArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'saveArtist()' method");
      try {
         initForm();
         ArtArtist artArtist = new ArtArtist();
         ConvertUtil.mapToObject(artArtist, form.getRecord(), false);
         String folderName = QTool.getCodeNo("E");
         artArtist.setFolderName(folderName);
         artArtistService.createArtArtist(artArtist, files, getFilesFileName());
         ids = artArtist.getId().toString();
         if (files != null) {
            String path = FILE_PATH + File.separator + folderName;
            File file = new File(path);
            if (!file.exists()) {
               file.mkdirs();
            }

            String extension = filesFileName.substring(filesFileName.lastIndexOf(".") + 1);
            String name = artArtist.getPhoto().split("/")[1];
            String imagePath = path + File.separator + name;
            FileUtils.fileUpload(imagePath, files);
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            /**
             * 压缩计算 float resizeTimes = 0.3f; //这个参数是要转化成的倍数,如果是1就是转化成1倍//
             * 调整后的图片的宽度和高度 int toWidth = (int) (width * resizeTimes); int
             * toHeight = (int) (height * resizeTimes);
             **/
            int newSize = 100;
            if (newSize >= width) {
               if (newSize < height) {
                  width = (int) (width * newSize / height);
                  height = newSize;
               }
            }
            else {
               if (newSize >= height) {
                  height = (int) (height * newSize / width);
                  width = newSize;
               }
               else {
                  if (height > width) {
                     width = (int) (width * newSize / height);
                     height = newSize;
                  }
                  else {
                     height = (int) (height * newSize / width);
                     width = newSize;
                  }
               }
            }

            String thumbnailsPath = path + File.separator + "thumbnails";
            file = new File(thumbnailsPath);
            if (!file.exists()) {
               file.mkdirs();
            }
            Thumbnails.of(imagePath).size(width, height).outputFormat(extension).outputQuality(0.8f).toFile(thumbnailsPath + File.separator + name);//保存小图
         }
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "新增艺术家:" + artArtist.getCname());
         return editArtist();
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

   public String editArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'editArtist()' method");
      String forward = null;
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtist artArtist = artArtistService.getArtArtist(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artArtist);
         String mainMedia = artArtist.getMainMedia();
         if (mainMedia != null && !"".equals(mainMedia)) {
            mainMedia = mainMedia.replace(" ", "");
            String[] mainMedias = mainMedia.split(",");
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < mainMedias.length; i++) {
               map.put(mainMedias[i], mainMedias[i]);
            }
            strs = mainMedias;
         }
         form.getRecord().put("cName", artArtist.getCname());
         form.getRecord().put("eName", artArtist.getEname());
         form.getRecord().put("photoPath", artArtist.getFolderName());
         form.getRecord().put("cResume", artArtist.getCresume());
         if (artArtist.getBirthCountry() != null) {
            ArtCountry artCountry = artCountryService.getArtCountry(Integer.valueOf(artArtist.getBirthCountry()));
            form.getRecord().put("birthCountryName", artCountry.getCountryName());
            form.getRecord().put("birthCountry", artCountry.getId().toString());
            if (artArtist.getNationality() != null) {
               artCountry = artCountryService.getArtCountry(Integer.valueOf(artArtist.getNationality()));
               form.getRecord().put("nationalityName", artCountry.getCountryName());
               form.getRecord().put("nationality", artCountry.getId().toString());
            }
         }

         String picture = artArtist.getPhoto();
         if (picture != null && !("").equals(picture)) {
            String[] pictures = picture.split("/");
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", pictures[0]);
            map.put("realName", pictures[1]);
            form.getRecord().put("realName", pictures[1]);
            pic.add(map);
         }

         return EDIT;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   public String updateArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'updateArtist()' method");
      try {
         initForm();
         //         form.getRecord().putAll(artArtistService.changeArtWorks(form));
         Map<String, String> record = form.getRecord();
         
         String worksPhoto = "";
         if (record.get(ArtArtist.PROP_WORKS_ID) != null && !"".equals(record.get(ArtArtist.PROP_WORKS_ID)))
            worksPhoto = artWorksService.getArtWorks(Integer.valueOf(record.get(ArtArtist.PROP_WORKS_ID))).getThumbnail();
         
         
         String mainMedia = "";
         for (int i = 0; i < strs.length; i++) {
            if (i != 0) {
               mainMedia = mainMedia + ",";
            }
            mainMedia = mainMedia + strs[i];
         }
         record.put(ArtArtist.PROP_WORKS_PHOTO, worksPhoto);
         record.put("mainMedia", mainMedia);
         artArtistService.updateArtArtist(record, files, getFilesFileName(), FILE_PATH);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "修改艺术家:" + record.get("cName"));
         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         e.printStackTrace();
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   public String deleteArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'deleteArtist()' method");
      try {
         String[] id = ids.split(",");
         Integer[] del_ids = new Integer[id.length];
         StringBuffer buffer = new StringBuffer();
         for (int i = 0; i < id.length; i++) {
            del_ids[i] = Integer.parseInt(id[i]);
            ArtArtist artist = artArtistService.getArtArtist(del_ids[i]);
            if (buffer.length() > 0) buffer.append(",");
            buffer.append(artist.getCname());
         }
         String message = artArtistService.deleteAllWithArtArtists(del_ids);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "删除艺术家:" + buffer.toString());
         if (!"".equals(message)) {
            setResponse("{exit:1,message:\"" + message + "\"}");
         }
         else {
            setResponse("{exit:0}");
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

   public String viewArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'viewArtist()' method");
      String forward = null;
      try {
         initForm();
         int id = Integer.valueOf(ids);
         ArtArtist artArtist = artArtistService.getArtArtist(id);
         ConvertUtil.objectToMap(getForm().getRecord(), artArtist);
         String mainMedia = artArtist.getMainMedia();
         if (mainMedia != null && !"".equals(mainMedia)) {
            mainMedia = mainMedia.replace(" ", "");
            String[] mainMedias = mainMedia.split(",");
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < mainMedias.length; i++) {
               map.put(mainMedias[i], mainMedias[i]);
            }
            strs = mainMedias;
         }
         form.getRecord().put("cName", artArtist.getCname());
         form.getRecord().put("eName", artArtist.getEname());
         form.getRecord().put("cResume", artArtist.getCresume());
         if (artArtist.getBirthCountry() != null) {
            ArtCountry artCountry = artCountryService.getArtCountry(Integer.valueOf(artArtist.getBirthCountry()));
            form.getRecord().put("birthCountryName", artCountry.getCountryName());
            if (artArtist.getNationality() != null) {
               artCountry = artCountryService.getArtCountry(Integer.valueOf(artArtist.getNationality()));
               form.getRecord().put("nationalityName", artCountry.getCountryName());
            }
         }
         form.getRecord().put("artistType", findCodeName(form, "ARTIST_TYPE", artArtist.getArtistType()));
         form.getRecord().put("sex", findCodeName(form, "GENDER", artArtist.getSex()));
         form.getRecord().put("zodiac", findCodeName(form, "ZODIAC", artArtist.getZodiac()));
         String birthTime = "";
         if (artArtist.getBirthYear() != null && !"".equals(artArtist.getBirthYear())) {
            birthTime += artArtist.getBirthYear() + "年";
         }
         if (artArtist.getBirthMonth() != null && !"".equals(artArtist.getBirthMonth())) {
            birthTime += artArtist.getBirthMonth() + "月";
         }
         if (artArtist.getBirthDay() != null && !"".equals(artArtist.getBirthDay())) {
            birthTime += artArtist.getBirthDay() + "日";
         }
         form.getRecord().put("birthTime", birthTime);
         form.getRecord().put("photoPath", artArtist.getEname().trim().toLowerCase());

         String deathTime = "";
         if (artArtist.getDeathYear() != null && !"".equals(artArtist.getDeathYear())) {
            deathTime += artArtist.getDeathYear() + "年";
         }
         if (artArtist.getDeathMonth() != null && !"".equals(artArtist.getDeathMonth())) {
            deathTime += artArtist.getDeathMonth() + "月";
         }
         if (artArtist.getDeathDay() != null && !"".equals(artArtist.getDeathDay())) {
            deathTime += artArtist.getDeathDay() + "日";
         }
         form.getRecord().put("deathTime", deathTime);

         String picture = artArtist.getPhoto();
         if (picture != null && !("").equals(picture)) {
            String[] pictures = picture.split("/");
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", pictures[0]);
            map.put("realName", pictures[1]);
            pic.add(map);
         }
         form.getRecord().put("hide", "1");

         return VIEW;
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   public String placeList(String action) throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'placeList()' method");
      String forward = "";
      initForm();
      if ("PLACE".equalsIgnoreCase(action)) forward = "PLACE";
      if ("BIRTHPLACE".equalsIgnoreCase(action) || "ANCESTRALHOME".equalsIgnoreCase(action) || "NHOM".equalsIgnoreCase(action)) {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("text/xml;charset=UTF-8");
         response.setHeader("Cache_Control", "no-cache");
         try {
            List<Map<String, Object>> listProvince = null;
            List<Map<String, Object>> listCity = null;
            List<Map<String, Object>> listMunicipalityOrAreaOrCounty = null;
            List<Map<String, Object>> listCounty = null;
            if (id > 0 && id < 100) {
               listCity = administrativeDivisionService.findCityByParentCode(String.valueOf(id));
               response.getWriter().write(getNodes(listCity, id));
            }
            else if (id > 100) {
               listMunicipalityOrAreaOrCounty = administrativeDivisionService.findMunicipalityOrAreaOrCounty(String.valueOf(id));
               if (listMunicipalityOrAreaOrCounty.size() > 0) {
                  listCounty = administrativeDivisionService.findCountyByParentCode(String.valueOf(id));
                  response.getWriter().write(getNodes(listCounty, id));
               }
            }
            else {
               listProvince = administrativeDivisionService.findAllProvince();
               response.getWriter().write(getTopNodes(listProvince));
            }
            response.getWriter().close();
         }
         catch (Exception e) {
            e.printStackTrace();
         }
         forward = null;
      }
      return forward;

   }

   private String country() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'country' method");
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

   public String showPhoto() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'showAlbum()' method");
      try {
         return "SHOW";
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
   }

   public String importArtist() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'importArtist()' method");
      try {
         if (files != null) {
            File file = new File(FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "experience");
            if (!file.exists()) {
               file.mkdirs();
            }
            String path = FILE_PATH + File.separator + "template" + File.separator + "artist" + File.separator + "experience" + File.separator + filesFileName;
            FileUtils.fileUpload(path, files);
            String message = artArtistService.importArtArtist(path);
            if (!"".equals(message)) {
               if (message.startsWith("成功")) {
                  artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "导入艺术家");
                  request.put("msg", message);
               }
               else {
                  throw new Exception(message);
               }
            }
            return returnForward(message);
         }
         else return "IMPORT";
      }
      catch (Exception e) {
         e.printStackTrace();
         if (e.getMessage() != null) {
            addMessage(getForm(), e.getMessage());
         }
         return "IMPORT";
      }
   }

   public String downloadTemplate() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'downloadTemplate()' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("艺术家导入模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String templatePath = ServletActionContext.getServletContext().getRealPath("/template/artist/art_artist.xls");
         File templateFile = new File(templatePath);
         PrintExcel printExcel = new PrintExcel();
         InputStream inputStream = new FileInputStream(templateFile);
         HSSFWorkbook hssfWorkbook = null;
         POIFSFileSystem poifsFileSystem = new POIFSFileSystem(inputStream);
         hssfWorkbook = new HSSFWorkbook(poifsFileSystem);

         HSSFDataFormat format = hssfWorkbook.createDataFormat();

         HSSFCellStyle csString = hssfWorkbook.createCellStyle();
         csString.setBorderLeft(CellStyle.BORDER_THIN);
         csString.setBorderRight(CellStyle.BORDER_THIN);
         csString.setBorderTop(CellStyle.BORDER_THIN);
         csString.setBorderBottom(CellStyle.BORDER_THIN);
         csString.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         csString.setDataFormat(format.getFormat("@"));

         HSSFCellStyle csDecimal = hssfWorkbook.createCellStyle();
         csDecimal.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal.setBorderBottom(CellStyle.BORDER_THIN);
         csDecimal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

         HSSFCellStyle csDecimal1 = hssfWorkbook.createCellStyle();
         csDecimal1.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal1.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal1.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal1.setBorderBottom(CellStyle.BORDER_THIN);
         HSSFDataFormat csformat = hssfWorkbook.createDataFormat();
         csDecimal1.setDataFormat(csformat.getFormat("0.000"));

         List<PrintPoint> printPoints = new ArrayList<PrintPoint>();

         printExcel.setPrintPointList(printPoints);// 填写数据
         printExcel.doFillSheet(hssfWorkbook, 0);// 写入workbook的第一页
         printExcel.doPrint(response, templateFile, hssfWorkbook);// workbook输出到response中。
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "下载艺术家模板");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public String export() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'export()' method");
      try {
         initForm();
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("艺术家导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artArtistService.export(form, pq);

         PrintExcel printExcel = new PrintExcel();

         HSSFDataFormat format = hssfWorkbook.createDataFormat();

         HSSFCellStyle csString = hssfWorkbook.createCellStyle();
         csString.setBorderLeft(CellStyle.BORDER_THIN);
         csString.setBorderRight(CellStyle.BORDER_THIN);
         csString.setBorderTop(CellStyle.BORDER_THIN);
         csString.setBorderBottom(CellStyle.BORDER_THIN);
         csString.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         csString.setDataFormat(format.getFormat("@"));

         HSSFCellStyle csDecimal = hssfWorkbook.createCellStyle();
         csDecimal.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal.setBorderBottom(CellStyle.BORDER_THIN);
         csDecimal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

         HSSFCellStyle csDecimal1 = hssfWorkbook.createCellStyle();
         csDecimal1.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal1.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal1.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal1.setBorderBottom(CellStyle.BORDER_THIN);
         HSSFDataFormat csformat = hssfWorkbook.createDataFormat();
         csDecimal1.setDataFormat(csformat.getFormat("0.000"));

         List<PrintPoint> printPoints = new ArrayList<PrintPoint>();

         printExcel.setPrintPointList(printPoints);// 填写数据
         printExcel.doFillSheet(hssfWorkbook, 0);// 写入workbook的第一页
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
         artSysLogService.createArtSysLog(currentUser.getUserName(), "艺术家管理", "艺术家信息管理", "导出艺术家");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public String getTopNodes(List<Map<String, Object>> list) throws Exception {
      StringBuffer sb = new StringBuffer("");
      sb.append("<?xml version='1.0' encoding='UTF-8' ?>");
      sb.append("<tree id=\"0\">");
      if (list != null) {
         for (int i = 0; i < list.size(); i++) {
            sb.append("<item id=\"" + list.get(i).get("area_code") + "\" text=\"" + list.get(i).get("area_name")
                  + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\" child=\"1\">");
            sb.append("<userdata name=\"areaCode\">" + list.get(i).get("area_code") + "</userdata>");
            sb.append("<userdata name=\"areaName\">" + list.get(i).get("area_name") + "</userdata>");
            sb.append("</item>");
         }
      }
      sb.append("</tree>");
      return sb.toString();
   }

   public String getNodes(List<Map<String, Object>> list, int id) throws Exception {
      StringBuffer sb = new StringBuffer("");
      sb.append("<?xml version='1.0' encoding='UTF-8' ?>");
      sb.append("<tree id=\"" + id + "\">");
      List<Map<String, Object>> listMunicipalityOrAreaOrCounty = null;
      if (list != null) {
         for (int i = 0; i < list.size(); i++) {
            String area_code = list.get(i).get("area_code").toString();
            listMunicipalityOrAreaOrCounty = administrativeDivisionService.findMunicipalityOrAreaOrCounty(area_code);
            String placeName = getPlaceName(area_code).substring(3);
            if (listMunicipalityOrAreaOrCounty.size() > 0) {
               sb.append("<item id=\"" + area_code + "\" text=\"" + list.get(i).get("area_name")
                     + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\" child=\"1\">");
               sb.append("<userdata name=\"findList\">findList</userdata>");//就是listMunicipalityOrAreaOrCounty>0，用来判断是否此时可以选择地点
            }
            else sb.append("<item id=\"" + area_code + "\" text=\"" + list.get(i).get("area_name")
                  + "\" im0=\"../icon/house_big.gif\" im1=\"../icon/house_big.gif\" im2=\"../icon/house_big.gif\">");
            sb.append("<userdata name=\"areaCode\">" + area_code + "</userdata>");
            sb.append("<userdata name=\"areaName\">" + placeName + "</userdata>");
            sb.append("</item>");
         }
      }
      sb.append("</tree>");
      return sb.toString();
   }

   public String getPlaceName(String areaCode) throws Exception {
      StringBuffer placeName = new StringBuffer();
      if (Integer.valueOf(areaCode) != 0) {
         List<Map<String, Object>> list = administrativeDivisionService.findNameByCode(areaCode);
         String parentCode = list.get(0).get("parent_code").toString();
         String areaName = list.get(0).get("area_name").toString();
         placeName.append(getPlaceName(parentCode) + " · " + areaName);
      }

      return placeName.toString();
   }

   public void initForm() throws Exception {
      setCode(getForm(), "ARTIST_TYPE,ZODIAC,GENDER,MEDIUM_TYPE");
      if (form.getCodeSets().get("MEDIUM_TYPE") != null) {
         if ("".equals(form.getCodeSets().get("MEDIUM_TYPE").get(0).getCodeName())) {
            form.getCodeSets().get("MEDIUM_TYPE").remove(0);
         }
      }
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public File getFiles() {
      return files;
   }

   public void setFiles(File files) {
      this.files = files;
   }

   public String getFilesFileName() {
      return filesFileName;
   }

   public void setFilesFileName(String filesFileName) {
      this.filesFileName = filesFileName;
   }

   public List<Map<String, String>> getPic() {
      return pic;
   }

   public void setPic(List<Map<String, String>> pic) {
      this.pic = pic;
   }

   public String[] getStrs() {
      return strs;
   }

   public void setStrs(String[] strs) {
      this.strs = strs;
   }

}
