package com.golead.art.works;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.tools.framedump;

import javax.annotation.Resource;
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
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.ArtArtistWorksSeriesService;
import com.golead.art.artist.service.ArtCountryService;
import com.golead.art.utils.FileUtils;
import com.golead.art.utils.PrintExcel;
import com.golead.art.utils.PrintPoint;
import com.golead.art.utils.QTool;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.model.ArtWorksTheme;
import com.golead.art.works.service.ArtMediumService;
import com.golead.art.works.service.ArtSysLogService;
import com.golead.art.works.service.ArtWorksAttachmentService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.art.works.service.ArtWorksStyleService;
import com.golead.art.works.service.ArtWorksThemeService;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.action.BaseAction;

public class ArtWorksAction extends BaseAction {

   private final Log                   logger        = LogFactory.getLog(ArtWorksAction.class);

   private SimpleDateFormat            sdf           = new SimpleDateFormat("yyyy-MM-dd");

   private DecimalFormat               decimalFormat = new DecimalFormat("#,###,###,###.##");

   public static String                FILE_PATH2    = ServletActionContext.getServletContext().getRealPath("/upload/auction/");
   public static String                FILE_PATH     = ServletActionContext.getServletContext().getRealPath("");

   private List<Map<String, String>>   partSizeList  = new ArrayList<Map<String, String>>();
   private List<Map<String, String>>   thumbnailList = new ArrayList<Map<String, String>>();
   private List<File>                  files;
   private String                      filesFileName;
   private String                      filesContentType;
   private File                        importFile;
   private String                      importFileFileName;
   private String                      importFileContentType;

   private String[]                    strs;
   private String[]                    writs;

   @Resource
   private ArtMediumService            artMediumService;

   @Resource
   private ArtWorksService             artWorksService;

   @Resource
   private ArtArtistService            artArtistService;

   @Resource
   private ArtWorksAttachmentService   artWorksAttachmentService;

   @Resource
   private ArtCountryService           artCountryService;

   @Resource
   private ArtWorksThemeService        artWorksThemeService;

   @Resource
   private ArtWorksStyleService        artWorksStyleService;

   @Resource
   private ArtArtistWorksSeriesService artArtistWorksSeriesService;

   @Resource
   private ArtSysLogService            artSysLogService;

   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = LIST;
      if (logger.isDebugEnabled()) logger.debug("action:" + action);
      try {
         if (LIST.equalsIgnoreCase(action)) forward = searchWorks();
         else if (PAGE.equalsIgnoreCase(action)) forward = getPageData();
         else if (ADD.equalsIgnoreCase(action)) forward = addWorks();
         else if (SAVE.equalsIgnoreCase(action)) forward = saveWorks();
         else if (EDIT.equalsIgnoreCase(action)) forward = editWorks();
         else if (UPDATE.equalsIgnoreCase(action)) forward = updateWorks();
         else if (VIEW.equalsIgnoreCase(action)) forward = view();
         else if (DELETE.equalsIgnoreCase(action)) forward = delete();
         else if ("ARTIST".equalsIgnoreCase(action)) forward = artist();
         else if ("COUNTRY".equalsIgnoreCase(action)) forward = country();
         else if ("THEME".equalsIgnoreCase(action)) forward = theme();
         else if ("STYLE".equalsIgnoreCase(action)) forward = style();
         else if ("SERIES".equalsIgnoreCase(action)) forward = series();
         else if ("MEDIUM".equalsIgnoreCase(action)) forward = medium();
         else if ("SHOW".equalsIgnoreCase(action)) forward = show();
         else if ("GOEXCEL".equalsIgnoreCase(action)) forward = goExcel();
         else if ("EXCEL".equalsIgnoreCase(action)) forward = excel();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = download();
         else if ("ISSAME".equalsIgnoreCase(action)) forward = isSame();
         else if ("EXPORT".equalsIgnoreCase(action)) forward = export();
         else if ("FRAME".equalsIgnoreCase(action)) forward = frame();
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String searchWorks() throws Exception {
      form.getRecord().put("test", "234");
      initForm();
      return LIST;
   }

   public void initForm() throws Exception {
      setCode(form, "YES_OR_NO,ART_TYPE,DATE_YEAR,MEDIUM_TYPE,SIGNATURE,SOCIAL_FUNCTION,PHOTO_SOURE,CREATIVE_WAY,WORKS_WRITING");
      //      List<ArtMedium> chineseMediums = artMediumService.findBySide("1");
      //      List<ArtMedium> englishMediums = artMediumService.findBySide("2");
      //      setCode(form, "chineseMediums", chineseMediums, ArtMedium.PROP_MEDIUM_NAME, ArtMedium.PROP_ID, true);
      //      setCode(form, "englishMediums", englishMediums, ArtMedium.PROP_MEDIUM_NAME, ArtMedium.PROP_ID, true);
      List<ArtMedium> materials = artMediumService.findByCategory("1");
      List<ArtMedium> shapes = artMediumService.findByCategory("2");
      setCode(form, "MATERIALS", materials, ArtMedium.PROP_MEDIUM_NAME, ArtMedium.PROP_ID, true);
      setCode(form, "SHAPES", shapes, ArtMedium.PROP_MEDIUM_NAME, ArtMedium.PROP_ID, true);
      List<ArtCountry> artCountries = artCountryService.findAll();
      setCode(form, "COUNTRY", artCountries, ArtCountry.PROP_COUNTRY_NAME, ArtCountry.PROP_ID, true);
      List<ArtArtistWorksSeries> artArtistWorksSeries = artArtistWorksSeriesService.findAll();
      setCode(form, "WORKSSERIES", artArtistWorksSeries, ArtArtistWorksSeries.PROP_SERIES_NAME, ArtArtistWorksSeries.PROP_ID, true);
      List<ArtWorksStyle> artWorksStyles = artWorksStyleService.findAll();
      setCode(form, "WORKSSTYLE", artWorksStyles, ArtWorksStyle.PROP_STYLE_NAME, ArtWorksStyle.PROP_ID, true);
      List<ArtWorksTheme> artWorksThemes = artWorksThemeService.findAll();
      setCode(form, "WORKSTHEME", artWorksThemes, ArtWorksTheme.PROP_THEME_NAME, ArtWorksTheme.PROP_ID, true);
      if (form.getCodeSets().get("SOCIAL_FUNCTION") != null) {
         if ("".equals(form.getCodeSets().get("SOCIAL_FUNCTION").get(0).getCodeName())) {
            form.getCodeSets().get("SOCIAL_FUNCTION").remove(0);
         }
      }
      if (form.getCodeSets().get("CREATIVE_WAY") != null) {
         if ("".equals(form.getCodeSets().get("CREATIVE_WAY").get(0).getCodeName())) {
            form.getCodeSets().get("CREATIVE_WAY").remove(0);
         }
      }
      if (form.getCodeSets().get("YES_OR_NO") != null) {
         if ("".equals(form.getCodeSets().get("YES_OR_NO").get(0).getCodeName())) {
            form.getCodeSets().get("YES_OR_NO").remove(0);
         }
      }
   }

   private String getPageData() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'getPageData' method");
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
               String enName = item.get("folderName") == null ? "" : item.get("folderName").toString();
               String createTime = "";
               String createYear = returnString(item.get("createYear"));
               createTime = addString(createTime, createYear, "年");
               String createMonth = returnString(item.get("createMonth"));
               createTime = addString(createTime, createMonth, "月");
               String createDay = returnString(item.get("createDay"));
               createTime = addString(createTime, createDay, "日");
               String auctionTime = "";
               if (item.get("auctionTime") != null) {
                  auctionTime = sdf.format(item.get("auctionTime"));
               }
               String thumbnail = returnString(item.get("thumbnail"));
               if (!thumbnail.equals("")) {
                  thumbnail = request.get("CONTEXT_PATH") + "upload/auction/" + enName + "/" + "thumbnail" + "/" + thumbnail;
               }
               String flag = "";
               if ("0".equals(item.get("repeatMarker"))) {
                  flag = "否";
               }
               else {
                  flag = "是";
               }
               String styleType = returnString(item.get("styleType"));
               String tmpStyle = "";
               if (styleType != null && !"".equals(styleType)) {
                  String[] styleTypes = styleType.split(";");
                  for (int i = 0; i < styleTypes.length; i++) {
                     ArtWorksStyle style = artWorksStyleService.getArtWorksStyle(Integer.valueOf(styleTypes[i]));
                     tmpStyle += style.getStyleName() + ";";
                  }
               }
               String sizeCmWidth = decimalFormatString(item.get("sizeCmWidth") == null ? "" : item.get("sizeCmWidth").toString());
               String sizeCmHeight = decimalFormatString(item.get("sizeCmHeight") == null ? "" : item.get("sizeCmHeight").toString());
               String sizeCmLength = decimalFormatString(item.get("sizeCmLength") == null ? "" : item.get("sizeCmLength").toString());
               String sizeCm = sizeCmLength + returnX(sizeCmLength, sizeCmWidth) + sizeCmWidth + returnX(sizeCmWidth, sizeCmHeight) + sizeCmHeight;
               if ("".equals(sizeCmWidth) && !"".equals(sizeCmHeight) && !"".equals(sizeCmLength)) {
                  sizeCm = sizeCmLength + "X" + sizeCmHeight;
               }
               //               String sizeInWidth = decimalFormatString(item.get("sizeInWidth") == null ? "" : item.get("sizeInWidth").toString());
               //               String sizeInHeight = decimalFormatString(item.get("sizeInHeight") == null ? "" : item.get("sizeInHeight").toString());
               //               String sizeInLength = decimalFormatString(item.get("sizeInLength") == null ? "" : item.get("sizeInLength").toString());
               //               String sizeIn = sizeInLength + returnX(sizeInLength, sizeInWidth) + sizeInWidth + returnX(sizeInWidth, sizeInHeight) + sizeInHeight;
               //               if("".equals(sizeInWidth) && !"".equals(sizeInHeight) && !"".equals(sizeInLength)){ sizeIn = sizeInLength + "X" + sizeInHeight;}
               Map<String, String> country = new HashMap<String, String>();
               List<ArtCountry> countries = artCountryService.findAll();
               for (ArtCountry artCountry : countries) {
                  country.put(artCountry.getId().toString(), artCountry.getCountryName());
               }
               String thumbnail2 = request.get("CONTEXT_PATH") + "upload/auction/" + enName + "/" + returnString(item.get("thumbnail"));
               String str = "<a href='" + thumbnail2 + "' target='_blank'><img src='" + thumbnail + "' /></a>";
               if ("".equals(returnString(item.get("thumbnail")))) {
                  str = "暂无图片";
               }
               String fakePaintings = returnString(item.get("fakePaintings"));
               if(fakePaintings.equals("1")){
                  fakePaintings = "假画";
               }else{
                  fakePaintings = "";
               }
               map.put("id", item.get("id"));
               map.put("data",
                     new Object[] { "", fakePaintings, flag, item.get("integrity"), str, item.get("no"), item.get("c_name") + artistlink, item.get("chineseName") + worklink, item.get("englishName"),
                           createTime, sizeCm, findCodeName(form, "SIGNATURE", returnString(item.get("signature"))), item.get("signatureContent"),
                           //                           sizeIn,
                           findCodeName(form, "MATERIALS", returnString(item.get("mediumMaterial"))),
                           findCodeName(form, "SHAPES", returnString(item.get("mediumShape"))),
                           findCodeName(form, "WORKSSERIES", returnString(item.get("worksSeries"))),
                           findCodeName(form, "COUNTRY", returnString(item.get("createCountry"))) + "" + returnString(item.get("createPlace")),
                           item.get("worksTheme1"), tmpStyle, auctionTime, item.get("auctionHouse"), decimalFormatString(item.get("lowestPrice")),
                           decimalFormatString(item.get("highestPrice")), decimalFormatString(item.get("tranPrice")),
                           decimalFormatString(item.get("tradePrice")) });
               list.add(map);
            }
         }
         String res = genGridJson(pageQuery, list);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "查询条件为：" + QTool.getParamter(pageQuery.getParameters()));
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

   private String returnX(String a, String b) {
      if (!"".equals(a) && !"".equals(b)) { return "X"; }
      return "";
   }

   private String decimalFormatString(Object object) {
      String str = object == null ? "" : object.toString().trim();
      if (!"".equals(str)) { return decimalFormat.format(Double.valueOf(str)); }
      return "";
   }

   private String returnString(Object object) {
      String str = object == null ? "" : object.toString();
      return str;
   }

   private String addString(String str, String equalStr, String addStr) {
      if (!"".equals(equalStr)) {
         if (equalStr.indexOf(addStr) > -1) return str + equalStr;
         else return str + equalStr + addStr;
      }
      return str;
   }
   
   private String frame() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addWorks' method");
      String forward = null;
      try {
         form.getRecord().put("id", ids);
         ArtWorks artWorks = artWorksService.getArtWorks(Integer.valueOf(ids));
         form.getRecord().put("artistId", artWorks.getArtistId().toString());
         initForm();
         return "FRAME";
      }
      catch (Exception e) {
         e.printStackTrace();
         forward = ERROR;
      }
      return forward;
   }

   private String addWorks() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'addWorks' method");
      String forward = null;
      try {
         form.getRecord().put("worksNo", "编号系统自动生成");
         initForm();
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
         ArtWorks artWorks = new ArtWorks();
         ConvertUtil.mapToObject(artWorks, artWorksService.changeArtWorks(form), false);
         String worksNo = artWorksService.getMaxWorksNo(artWorks.getArtistId());
         artWorks.setWorksNo(worksNo);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "添加作品：" + artWorks.getWorksCName());
         artWorksService.createArtWorks(artWorks);
         if (filesFileName != null) {
            artWorksService.saveThumbnail(artWorks, filesFileName, FILE_PATH, files);
         }
         ids = artWorks.getId().toString();
         editWorks();
         form.getRecord().put("next", "true");
         return EDIT;
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         e.printStackTrace();
         initForm();
         return ADD;
      }
   }

   private Integer idI(Integer id) {
      if (id == null) { return 0; }
      return id;
   }

   private String editWorks() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'editWorks' method");
      String forward = null;
      try {
         initForm();
         ArtWorks works = artWorksService.getArtWorks(Integer.valueOf(ids));
         ConvertUtil.objectToMap(getForm().getRecord(), works);
         Integer idInteger;
         // 媒介(材料)
         String medium = returnString(works.getMediumMaterial());
         String[] mediums = medium.split(",");
         String mediumMaterialName = "";
         for (int i = 0; i < mediums.length; i++) {
            String mediumName = findCodeName(form, "MATERIALS", mediums[i]);
            if (!"".equals(mediumMaterialName)) {
               mediumMaterialName += ",";
            }
            mediumMaterialName += mediumName;
         }
         form.getRecord().put("mediumMaterialName", mediumMaterialName);
         // 媒介(形式)
         medium = returnString(works.getMediumShape());
         mediums = medium.split(",");
         String mediumShapeName = "";
         for (int i = 0; i < mediums.length; i++) {
            String mediumName = findCodeName(form, "SHAPES", mediums[i]);
            if (!"".equals(mediumShapeName)) {
               mediumShapeName += ",";
            }
            mediumShapeName += mediumName;
         }
         form.getRecord().put("mediumShapeName", mediumShapeName);
         // 社会功能
         if (works.getSocialFunction() != null && !"".equals(works.getSocialFunction())) {
            String social = works.getSocialFunction();
            social = social.replace(" ", "");
            String[] socials = social.split(",");
            strs = socials;
         }
         // 创作方式
         if (works.getCreateStyle() != null && !"".equals(works.getCreateStyle())) {
            String writing = works.getCreateStyle();
            writing = writing.replace(" ", "");
            String[] writings = writing.split(",");
            writs = writings;
         }
         // 艺术家id
         idInteger = idI(works.getArtistId());
         ArtArtist artist = artArtistService.getArtArtist(idInteger);
         if (artist != null) {
            form.getRecord().put("artArtistName", artist.getCname());
            form.getRecord().put("artArtistId", artist.getId().toString());
         }
         // 创作地点
         if (works.getCreateCountry() != null) {
            idInteger = idI(works.getCreateCountry());
            ArtCountry artCountry = artCountryService.getArtCountry(idInteger);
            if (artCountry != null) {
               form.getRecord().put("createCountryName", artCountry.getCountryName());
               form.getRecord().put("createCountry", artCountry.getId().toString());
            }
         }
         // 作品题材
         if (works.getWorksTheme1() != null && !"".equals(works.getWorksTheme1())) {
            idInteger = idI(Integer.valueOf(works.getWorksTheme1()));
            ArtWorksTheme artWorksTheme = artWorksThemeService.getArtWorksTheme(idInteger);
            if (artWorksTheme != null) {
               form.getRecord().put("worksThemeName1", artWorksTheme.getThemeName());
            }
         }
         if (works.getWorksTheme2() != null && !"".equals(works.getWorksTheme2())) {
            idInteger = idI(Integer.valueOf(works.getWorksTheme2()));
            ArtWorksTheme artWorksTheme = artWorksThemeService.getArtWorksTheme(idInteger);
            if (artWorksTheme != null) {
               form.getRecord().put("worksThemeName2", artWorksTheme.getThemeName());
            }
         }
         if (works.getWorksTheme3() != null && !"".equals(works.getWorksTheme3())) {
            idInteger = idI(Integer.valueOf(works.getWorksTheme3()));
            ArtWorksTheme artWorksTheme = artWorksThemeService.getArtWorksTheme(idInteger);
            if (artWorksTheme != null) {
               form.getRecord().put("worksThemeName3", artWorksTheme.getThemeName());
            }
         }
         // 风格类型
         if (works.getStyleType() != null && !"".equals(works.getStyleType())) {
            String styleType = works.getStyleType();
            String worksStyleName = "";
            for (int i = 0; i < styleType.split(";").length; i++) {
               String styleTypeId = styleType.split(";")[i];
               ArtWorksStyle artWorksStyle = artWorksStyleService.getArtWorksStyle(Integer.valueOf(styleTypeId));
               worksStyleName = worksStyleName + artWorksStyle.getStyleName() + "、";
            }
            form.getRecord().put("worksStyle", worksStyleName);
         }
         // 作品系列
         if (works.getWorksSeries() != null && !"".equals(works.getWorksSeries())) {
            String worksSeries = works.getWorksSeries();
            String worksSeriesName = "";
            for (int i = 0; i < worksSeries.split(";").length; i++) {
               String worksSeriesId = worksSeries.split(";")[i];
               idInteger = idI(Integer.valueOf(worksSeriesId));
               ArtArtistWorksSeries artArtistWorksSeries = artArtistWorksSeriesService.getArtArtistWorksSeries(idInteger);
               if (artArtistWorksSeries != null) {
                  worksSeriesName = worksSeriesName + artArtistWorksSeries.getSeriesName() + "";
               }
            }
            form.getRecord().put("worksSeriesName", worksSeriesName);
         }

         form.getRecord().put("chineseName", works.getWorksCName());
         form.getRecord().put("englishName", works.getWorksEName() == null ? "" : works.getWorksEName());
         String worksImage = works.getWorksImage();
         String worksStoreName = works.getWorksStoreName();
         if (worksImage != null && !"".equals(worksImage) && worksStoreName != null && !"".equals(worksStoreName)) {
            String[] worksImages = worksImage.split("、");
            String[] worksStoreNames = worksStoreName.split("、");
            for (int i = 0; i < worksImages.length; i++) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("key", worksStoreNames[i].split(":")[1]);
               map.put("value", worksImages[i].split(":")[1]);
               form.getRecord().put("photoPath", artist.getFolderName());
               thumbnailList.add(map);
            }
         }
         String partSize = works.getPartSize();
         if (partSize != null && !"".equals(partSize)) {
            String[] partSizes = partSize.split(";");
            for (int i = 0; i < partSizes.length; i++) {
               Map<String, String> map = new HashMap<String, String>();
               String size = partSizes[i];
               String[] sizes = size.split(":");
               if (sizes.length == 2) {
                  String partSize_name = sizes[0];
                  map.put("name_" + String.valueOf(i), partSize_name);
                  if (sizes[1].split(" ").length == 2) {
                     String partSize_unit = sizes[1].split(" ")[1];
                     map.put("unit_" + String.valueOf(i), partSize_unit);
                     String lw = sizes[1].split(" ")[0];
                     String[] lws = lw.split("\\*");
                     if (lws.length == 2) {
                        String partSize_l = lws[0];
                        String partSize_w = lws[1];
                        map.put("l_" + String.valueOf(i), partSize_l);
                        map.put("w_" + String.valueOf(i), partSize_w);
                        map.put("id", String.valueOf(i));
                        partSizeList.add(map);
                     }
                  }
               }
            }
            if (partSizeList.size() == 0) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("id", "0");
               partSizeList.add(map);
            }
         }
         else {
            form.getRecord().put("Psize", "no");
         }
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
         //         artWorksService.changeArtWorks(form);
         Map<String, String> record = form.getRecord();
         String social = "";
         for (int i = 0; i < strs.length; i++) {
            if (i != 0) {
               social = social + ",";
            }
            social = social + strs[i];
         }
         form.getRecord().put("socialFunction", social);
         String createStyle = "";
         for (int i = 0; i < writs.length; i++) {
            if (i != 0) {
               createStyle = createStyle + ",";
            }
            createStyle = createStyle + writs[i];
         }
         form.getRecord().put("createStyle", createStyle);
         artWorksService.updateArtWorks(form.getRecord(), files, filesFileName, FILE_PATH);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "修改作品：" + record.get(ArtWorks.PROP_WORKS_C_NAME));
         ids = form.getRecord().get("id");
         return editWorks();
//         return returnForward(RETURN_NORMAL);
      }
      catch (Exception e) {
         addMessage(form, e.getMessage());
         initForm();
         return EDIT;
      }
   }

   private String view() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'view' method");
      String forward = null;
      try {
         initForm();
         ArtWorks works = artWorksService.getArtWorks(Integer.valueOf(form.getRecord().get("id")));
         if(works.getCreateYear() != null && !"".equals(works.getCreateYear().trim())){
            works.setCreateYear(works.getCreateYear() + "年");
         }
         if(works.getCreateMonth() != null && !"".equals(works.getCreateMonth().trim())){
            works.setCreateMonth(works.getCreateMonth() + "月");
         }
         if(works.getCreateDay() != null && !"".equals(works.getCreateDay().trim())){
            works.setCreateDay(works.getCreateDay() + "日");
         }
         ConvertUtil.objectToMap(getForm().getRecord(), works);
         Integer idInteger;
         // 媒介(材料)
         String medium = returnString(works.getMediumMaterial());
         String[] mediums = medium.split(",");
         String mediumMaterialName = "";
         for (int i = 0; i < mediums.length; i++) {
            String mediumName = findCodeName(form, "MATERIALS", mediums[i]);
            if (!"".equals(mediumMaterialName)) {
               mediumMaterialName += ",";
            }
            mediumMaterialName += mediumName;
         }
         form.getRecord().put("mediumMaterialName", mediumMaterialName);
         // 媒介(形式)
         medium = returnString(works.getMediumShape());
         mediums = medium.split(",");
         String mediumShapeName = "";
         for (int i = 0; i < mediums.length; i++) {
            String mediumName = findCodeName(form, "SHAPES", mediums[i]);
            if (!"".equals(mediumShapeName)) {
               mediumShapeName += ",";
            }
            mediumShapeName += mediumName;
         }
         form.getRecord().put("mediumShapeName", mediumShapeName);
         // 社会功能
         if (works.getSocialFunction() != null && !"".equals(works.getSocialFunction())) {
            String social = works.getSocialFunction();
            social = social.replace(" ", "");
            String[] socials = social.split(",");
            strs = socials;
         }
         // 创作方式
         if (works.getCreateStyle() != null && !"".equals(works.getCreateStyle())) {
            String writing = works.getCreateStyle();
            writing = writing.replace(" ", "");
            String[] writings = writing.split(",");
            writs = writings;
         }
         // 艺术家id
         idInteger = idI(works.getArtistId());
         ArtArtist artist = artArtistService.getArtArtist(idInteger);
         if (artist != null) {
            form.getRecord().put("artArtistName", artist.getCname());
            form.getRecord().put("artArtistId", artist.getId().toString());
         }
         // 创作地点
         if (works.getCreateCountry() != null) {
            idInteger = idI(works.getCreateCountry());
            ArtCountry artCountry = artCountryService.getArtCountry(idInteger);
            if (artCountry != null) {
               form.getRecord().put("createCountryName", artCountry.getCountryName());
               form.getRecord().put("createCountry", artCountry.getId().toString());
            }
         }
         // 作品题材
         if (works.getWorksTheme1() != null && !"".equals(works.getWorksTheme1())) {
            idInteger = idI(Integer.valueOf(works.getWorksTheme1()));
            ArtWorksTheme artWorksTheme = artWorksThemeService.getArtWorksTheme(idInteger);
            if (artWorksTheme != null) {
               form.getRecord().put("worksThemeName1", artWorksTheme.getThemeName());
            }
         }
         if (works.getWorksTheme2() != null && !"".equals(works.getWorksTheme2())) {
            idInteger = idI(Integer.valueOf(works.getWorksTheme2()));
            ArtWorksTheme artWorksTheme = artWorksThemeService.getArtWorksTheme(idInteger);
            if (artWorksTheme != null) {
               form.getRecord().put("worksThemeName2", artWorksTheme.getThemeName());
            }
         }
         if (works.getWorksTheme3() != null && !"".equals(works.getWorksTheme3())) {
            idInteger = idI(Integer.valueOf(works.getWorksTheme3()));
            ArtWorksTheme artWorksTheme = artWorksThemeService.getArtWorksTheme(idInteger);
            if (artWorksTheme != null) {
               form.getRecord().put("worksThemeName3", artWorksTheme.getThemeName());
            }
         }
         // 风格类型
         if (works.getStyleType() != null && !"".equals(works.getStyleType())) {
            String styleType = works.getStyleType();
            String worksStyleName = "";
            for (int i = 0; i < styleType.split(";").length; i++) {
               String styleTypeId = styleType.split(";")[i];
               ArtWorksStyle artWorksStyle = artWorksStyleService.getArtWorksStyle(Integer.valueOf(styleTypeId));
               worksStyleName = worksStyleName + artWorksStyle.getStyleName() + "、";
            }
            form.getRecord().put("worksStyle", worksStyleName);
         }
         // 作品系列
         if (works.getWorksSeries() != null && !"".equals(works.getWorksSeries())) {
            String worksSeries = works.getWorksSeries();
            String worksSeriesName = "";
            for (int i = 0; i < worksSeries.split(";").length; i++) {
               String worksSeriesId = worksSeries.split(";")[i];
               idInteger = idI(Integer.valueOf(worksSeriesId));
               ArtArtistWorksSeries artArtistWorksSeries = artArtistWorksSeriesService.getArtArtistWorksSeries(idInteger);
               if (artArtistWorksSeries != null) {
                  worksSeriesName = worksSeriesName + artArtistWorksSeries.getSeriesName() + "";
               }
            }
            form.getRecord().put("worksSeriesName", worksSeriesName);
         }

         form.getRecord().put("chineseName", works.getWorksCName());
         form.getRecord().put("englishName", works.getWorksEName() == null ? "" : works.getWorksEName());
         String worksImage = works.getWorksImage();
         String worksStoreName = works.getWorksStoreName();
         if (worksImage != null && !"".equals(worksImage) && worksStoreName != null && !"".equals(worksStoreName)) {
            String[] worksImages = worksImage.split("、");
            String[] worksStoreNames = worksStoreName.split("、");
            for (int i = 0; i < worksImages.length; i++) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("key", worksStoreNames[i].split(":")[1]);
               map.put("value", worksImages[i].split(":")[1]);
               form.getRecord().put("photoPath", artist.getFolderName());
               thumbnailList.add(map);
            }
         }
         String partSize = works.getPartSize();
         if (partSize != null && !"".equals(partSize)) {
            String[] partSizes = partSize.split(";");
            for (int i = 0; i < partSizes.length; i++) {
               Map<String, String> map = new HashMap<String, String>();
               String size = partSizes[i];
               String[] sizes = size.split(":");
               if (sizes.length == 2) {
                  String partSize_name = sizes[0];
                  map.put("name_" + String.valueOf(i), partSize_name);
                  if (sizes[1].split(" ").length == 2) {
                     String partSize_unit = sizes[1].split(" ")[1];
                     map.put("unit_" + String.valueOf(i), partSize_unit);
                     String lw = sizes[1].split(" ")[0];
                     String[] lws = lw.split("\\*");
                     if (lws.length == 2) {
                        String partSize_l = lws[0];
                        String partSize_w = lws[1];
                        map.put("l_" + String.valueOf(i), partSize_l);
                        map.put("w_" + String.valueOf(i), partSize_w);
                        map.put("id", String.valueOf(i));
                        partSizeList.add(map);
                     }
                  }
               }
            }
            if (partSizeList.size() == 0) {
               Map<String, String> map = new HashMap<String, String>();
               map.put("id", "0");
               partSizeList.add(map);
            }
         }
         else {
            form.getRecord().put("Psize", "no");
         }
         String sizeCmWidth = decimalFormatString(works.getSizeCmWidth() == null ? "" : works.getSizeCmWidth().toString());
         String sizeCmHeight = decimalFormatString(works.getSizeCmHeight() == null ? "" : works.getSizeCmHeight().toString());
         String sizeCmLength = decimalFormatString(works.getSizeCmLength() == null ? "" : works.getSizeCmLength().toString());
         String sizeCm = sizeCmLength + returnX(sizeCmLength, sizeCmWidth) + sizeCmWidth + returnX(sizeCmWidth, sizeCmHeight) + sizeCmHeight;
         if ("".equals(sizeCmWidth) && !"".equals(sizeCmHeight) && !"".equals(sizeCmLength)) {
            sizeCm = sizeCmLength + "X" + sizeCmHeight;
         }
         String sizeInWidth = decimalFormatString(works.getSizeInWidth() == null ? "" : works.getSizeInWidth().toString());
         String sizeInHeight = decimalFormatString(works.getSizeInHeight() == null ? "" : works.getSizeInHeight().toString());
         String sizeInLength = decimalFormatString(works.getSizeInLength() == null ? "" : works.getSizeInLength().toString());
         String sizeIn = sizeInLength + returnX(sizeInLength, sizeInWidth) + sizeInWidth + returnX(sizeInWidth, sizeInHeight) + sizeInHeight;
         if("".equals(sizeInWidth) && !"".equals(sizeInHeight) && !"".equals(sizeInLength)){ sizeIn = sizeInLength + "X" + sizeInHeight;}
         String sizeRuleWidth = decimalFormatString(works.getSizeRuleWidth() == null ? "" : works.getSizeRuleWidth().toString());
         String sizeRuleHeight = decimalFormatString(works.getSizeRuleHeight() == null ? "" : works.getSizeRuleHeight().toString());
         String sizeRuleLength = decimalFormatString(works.getSizeRuleLength() == null ? "" : works.getSizeRuleLength().toString());
         String sizeRule = sizeRuleLength + returnX(sizeRuleLength, sizeRuleWidth) + sizeRuleWidth + returnX(sizeRuleWidth, sizeRuleHeight) + sizeRuleHeight;
         if("".equals(sizeRuleWidth) && !"".equals(sizeRuleHeight) && !"".equals(sizeRuleLength)){ sizeRule = sizeRuleLength + "X" + sizeRuleHeight;}
         form.getRecord().put("sizeCm", sizeCm);
         form.getRecord().put("sizeIn", sizeIn);
         form.getRecord().put("sizeRule", sizeRule);
         String createFromTo = "";
         if(returnString(works.getCreateFrom()).length() > 0 && returnString(works.getCreateTo()).length() > 0){
            createFromTo = returnString(works.getCreateFrom()) + "~" + returnString(works.getCreateTo());
         }
         form.getRecord().put("createFromTo", createFromTo);
         if(!"".equals(returnString(form.getRecord().get("signature")))){
            form.getRecord().put("signature", findCodeName(form, "SIGNATURE", returnString(form.getRecord().get("signature"))));
         }
         if(!"".equals(returnString(form.getRecord().get("signature2")))){
            form.getRecord().put("signature2", findCodeName(form, "SIGNATURE", returnString(form.getRecord().get("signature2"))));
         }
         if(!"".equals(returnString(form.getRecord().get("signature3")))){
            form.getRecord().put("signature3", findCodeName(form, "SIGNATURE", returnString(form.getRecord().get("signature3"))));
         }
         if(!"".equals(returnString(form.getRecord().get("worksWriting")))){
            form.getRecord().put("worksWriting", findCodeName(form, "WORKS_WRITING", returnString(form.getRecord().get("worksWriting"))));
         }
         if(!"".equals(returnString(form.getRecord().get("photoSoure")))){
            form.getRecord().put("photoSoure", findCodeName(form, "PHOTO_SOURE", returnString(form.getRecord().get("photoSoure"))));
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
         String[] idlist = ids.split(",");
         Integer[] dels = new Integer[idlist.length];
         StringBuffer log = new StringBuffer();
         for (int i = 0; i < idlist.length; i++) {
            dels[i] = Integer.valueOf(idlist[i]);
            ArtWorks artWorks = artWorksService.getArtWorks(dels[i]);
            if (log.length() > 0) log.append(",");
            log.append(artWorks.getWorksCName());
         }
         artWorksService.deleteArtWorkss(dels);
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "删除作品：" + log.toString());
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

   private String artist() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'artist' method");
      try {
         List<ArtArtist> artArtists = artArtistService.findAllArtArtist();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtArtist artArtist : artArtists) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artArtist);
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
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

   private String theme() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'theme' method");
      try {
         List<ArtWorksTheme> artWorksThemes = artWorksThemeService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtWorksTheme artWorksTheme : artWorksThemes) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artWorksTheme);
            if ((";" + ids).indexOf(";" + map.get("id")) > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "THEME";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String style() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'style' method");
      try {
         List<ArtWorksStyle> artWorksStyles = artWorksStyleService.findAll();
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtWorksStyle artWorksStyle : artWorksStyles) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artWorksStyle);
            if ((";" + ids).indexOf(";" + map.get("id") + ";") > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "STYLE";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String series() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'series' method");
      try {
         String artArtistId = form.getRecord().get("artArtistId");
         List<ArtArtistWorksSeries> artArtistWorksSeries = artArtistWorksSeriesService.findByArtArtistId(Integer.valueOf(artArtistId));
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         for (ArtArtistWorksSeries artistWorksSeries : artArtistWorksSeries) {
            Map<String, Object> map = new HashMap<String, Object>();
            ConvertUtil.objectToMap(map, artistWorksSeries);
            if ((";" + ids).indexOf(";" + map.get("id") + ";") > -1) map.put("CHECKED", "checked");
            else map.put("CHECKED", "");
            list.add(map);
         }
         form.getQuery().setRecordSet(list);
         return "SERIES";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String medium() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'Medium' method");
      try {
         //         String id = form.getRecord().get("id");
         //         PageQuery pageQuery = new PageQuery();
         //         pageQuery.getParameters().put("upId", "");
         //         pageQuery.getParameters().put("mediumCategory", form.getRecord().get("mediumCategory"));
         //         pageQuery = artMediumService.queryArtMedium(pageQuery);
         //         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         //         for (Map<String, Object> map : pageQuery.getRecordSet()) {
         //            if (id.indexOf(map.get("id").toString()) > -1) map.put("CHECKED", "true");
         //            else map.put("CHECKED", "");
         //            list.add(map);
         //         }
         //         form.getQuery().setRecordSet(list);
         return "MEDIUM";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String show() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'show' method");
      try {
         return "SHOW";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private String goExcel() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'goExcel' method");
      try {
         return "GOEXCEL";
      }
      catch (Exception e) {
         e.printStackTrace();
         return LIST;
      }
   }

   private String excel() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'excel' method");
      try {
         File file = new File(FILE_PATH + File.separator + "template" + File.separator + "works" + File.separator + "works");
         if (!file.exists()) {
            file.mkdirs();
         }
         String path = FILE_PATH + File.separator + "template" + File.separator + "works" + File.separator + "works" + File.separator + importFileFileName;
         FileUtils.fileUpload(path, importFile);
         String message = artWorksService.importArtWorks(path);
         if (!"".equals(message)) {
            if (message.startsWith("成功")) {
               artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "上传作品");
               request.put("msg", message);
            }
            else {
               throw new Exception(message);
            }
         }
         return returnForward(message);
      }
      catch (Exception e) {
         e.printStackTrace();
         if (e.getMessage() != null) {
            addMessage(form, e.getMessage());
         }
         return "GOEXCEL";
      }
   }

   private String download() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'download' method");
      try {
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("作品模板.xls".getBytes("gb2312"), "ISO-8859-1"));
         String tempPath = ServletActionContext.getServletContext().getRealPath("") + File.separator + "template" + File.separator + "works" + File.separator
               + "art_works.xls";
         File tempFile = new File(tempPath);
         if (!tempFile.exists()) { throw new Exception(); }

         PrintExcel printExcel = new PrintExcel();
         InputStream inp = null;
         inp = new FileInputStream(tempFile);
         HSSFWorkbook wb = null;
         POIFSFileSystem f = new POIFSFileSystem(inp);
         wb = new HSSFWorkbook(f);

         HSSFDataFormat format = wb.createDataFormat();

         HSSFCellStyle csString = wb.createCellStyle();
         csString.setBorderLeft(CellStyle.BORDER_THIN);
         csString.setBorderRight(CellStyle.BORDER_THIN);
         csString.setBorderTop(CellStyle.BORDER_THIN);
         csString.setBorderBottom(CellStyle.BORDER_THIN);
         csString.setAlignment(HSSFCellStyle.ALIGN_CENTER);
         csString.setDataFormat(format.getFormat("@"));

         HSSFCellStyle csDecimal = wb.createCellStyle();
         csDecimal.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal.setBorderBottom(CellStyle.BORDER_THIN);
         csDecimal.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

         HSSFCellStyle csDecimal1 = wb.createCellStyle();
         csDecimal1.setBorderLeft(CellStyle.BORDER_THIN);
         csDecimal1.setBorderRight(CellStyle.BORDER_THIN);
         csDecimal1.setBorderTop(CellStyle.BORDER_THIN);
         csDecimal1.setBorderBottom(CellStyle.BORDER_THIN);
         HSSFDataFormat csformat = wb.createDataFormat();
         csDecimal1.setDataFormat(csformat.getFormat("0.000"));

         List<PrintPoint> printPointList = new ArrayList<PrintPoint>();

         printExcel.setPrintPointList(printPointList);// 填写数据
         printExcel.doFillSheet(wb, 0);// 写入workbook的第一页
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "下载作品模板");
         printExcel.doPrint(response, tempFile, wb);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private String isSame() throws Exception {
      try {
         String[] idlist = ids.split(",");
         Integer[] dels = new Integer[idlist.length];
         String isSame = form.getRecord().get("issame");
         for (int i = 0; i < idlist.length; i++) {
            dels[i] = Integer.valueOf(idlist[i]);
            ArtWorks artWorks = artWorksService.getArtWorks(dels[i]);
            artWorks.setRepeatMarker(isSame);
            artWorks.setWorksIden("1");
            artWorksService.updateArtWorks(artWorks);
         }
         setResponse("{exit:0}");
      }
      catch (Exception e) {
         e.printStackTrace();
         return ERROR;
      }
      return null;
   }

   public String export() throws Exception {
      if (logger.isDebugEnabled()) logger.debug("Entering 'export()' method");
      try {
         initForm();
         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.setContentType("APPLICATION/DOWNLOAD");
         response.setContentType("application/octed-stream,charset=gb2312");
         response.setHeader("Content-Disposition", "attachment;filename=" + new String("作品导出数据.xls".getBytes("gb2312"), "ISO-8859-1"));
         PageQuery pq = form.getQuery();
         HSSFWorkbook hssfWorkbook = artWorksService.export(form, pq);

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
         artSysLogService.createArtSysLog(currentUser.getUserName(), "作品管理", "作品管理", "导出作品数据");
         printExcel.doPrint(response, null, hssfWorkbook);// workbook输出到response中。
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   public List<Map<String, String>> getPartSizeList() {
      return partSizeList;
   }

   public void setPartSizeList(List<Map<String, String>> partSizeList) {
      this.partSizeList = partSizeList;
   }

   public List<Map<String, String>> getThumbnailList() {
      return thumbnailList;
   }

   public void setThumbnailList(List<Map<String, String>> thumbnailList) {
      this.thumbnailList = thumbnailList;
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

   public String getFilesContentType() {
      return filesContentType;
   }

   public void setFilesContentType(String filesContentType) {
      this.filesContentType = filesContentType;
   }

   public File getImportFile() {
      return importFile;
   }

   public void setImportFile(File importFile) {
      this.importFile = importFile;
   }

   public String getImportFileFileName() {
      return importFileFileName;
   }

   public void setImportFileFileName(String importFileFileName) {
      this.importFileFileName = importFileFileName;
   }

   public String getImportFileContentType() {
      return importFileContentType;
   }

   public void setImportFileContentType(String importFileContentType) {
      this.importFileContentType = importFileContentType;
   }

   public String[] getStrs() {
      return strs;
   }

   public void setStrs(String[] strs) {
      this.strs = strs;
   }

   public String[] getWrits() {
      return writs;
   }

   public void setWrits(String[] writs) {
      this.writs = writs;
   }

}
