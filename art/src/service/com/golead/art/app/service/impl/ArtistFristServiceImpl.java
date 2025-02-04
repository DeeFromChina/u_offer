package com.golead.art.app.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.golead.art.activity.dao.ArtActivityAbmbDao;
import com.golead.art.activity.dao.ArtActivityExhibitDao;
import com.golead.art.activity.dao.ArtAgencyDao;
import com.golead.art.activity.model.ArtActivityAbmb;
import com.golead.art.activity.model.ArtActivityExhibit;
import com.golead.art.activity.model.ArtAgency;
import com.golead.art.app.appUser.dao.ArtAppUserDao;
import com.golead.art.app.appUser.model.ArtAppUser;
import com.golead.art.app.artistFollow.dao.ArtArtistFollowDao;
import com.golead.art.app.artistFollow.model.ArtArtistFollow;
import com.golead.art.app.artistPage.model.ArtArtistPage;
import com.golead.art.app.service.ArtistFristService;
import com.golead.art.app.service.WorksFristService;
import com.golead.art.artist.dao.ArtArtistCollectorDao;
import com.golead.art.artist.dao.ArtArtistCoopDao;
import com.golead.art.artist.dao.ArtArtistDonationDao;
import com.golead.art.artist.dao.ArtArtistEduDao;
import com.golead.art.artist.dao.ArtArtistHonorsDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistCollectAgency;
import com.golead.art.artist.model.ArtArtistCollector;
import com.golead.art.artist.model.ArtArtistCoop;
import com.golead.art.artist.model.ArtArtistDonation;
import com.golead.art.artist.model.ArtArtistEdu;
import com.golead.art.artist.model.ArtArtistHonors;
import com.golead.art.artist.service.ArtArtistCollectAgencyService;
import com.golead.art.artist.service.ArtArtistCollectorService;
import com.golead.art.artist.service.ArtArtistCoopService;
import com.golead.art.artist.service.ArtArtistEduService;
import com.golead.art.artist.service.ArtArtistHonorsService;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.impl.ArtArtistCollectorServiceImpl;
import com.golead.art.literature.dao.ArtLiteratureMediumDao;
import com.golead.art.literature.dao.ArtLiteratureNetworkDao;
import com.golead.art.literature.dao.ArtLiteratureWordsDao;
import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureNetwork;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.photo.model.ArtPhoto;
import com.golead.art.works.dao.ArtArtistCollectAgencyDao;
import com.golead.art.works.dao.ArtArtistHeatDao;
import com.golead.art.works.dao.ArtMediumDao;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtArtistHeat;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.common.model.SysCode;
import com.golead.common.service.system.SysCodeService;
import com.golead.common.service.system.SysCodesetService;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.util.Encryption;
import com.mysql.jdbc.Util;

@Service
public class ArtistFristServiceImpl extends BaseServiceImpl implements ArtistFristService {

   @Override
   public List<Map<String, Object>> selectArtist(Map<String, String> parameters) throws ServiceException {
      try {
         String pageName = parameters.get("pageName");
         if (pageName.equalsIgnoreCase("aristPage")) return aristPage(parameters);// 艺术家主页
         else if (pageName.equalsIgnoreCase("aristHeat")) return aristHeat(parameters);// 艺术家关注
         else if (pageName.equalsIgnoreCase("aristFollow")) return aristFollow(parameters);// 艺术家关注
         else if (pageName.equalsIgnoreCase("baseFile")) return baseFile(parameters);// 基本档案
         else if (pageName.equalsIgnoreCase("activity")) return activity(parameters);// 活动履历
         else if (pageName.equalsIgnoreCase("artistPhoto")) return artistPhoto(parameters);// 照片手迹
         else if (pageName.equalsIgnoreCase("artistWorks")) return artistWorks(parameters);// 作品
         else if (pageName.equalsIgnoreCase("artistAuction")) return artistAuction(parameters);// 拍卖
         else if (pageName.equalsIgnoreCase("artistPublication")) return artistPublication(parameters);// 出版著作
         else if (pageName.equalsIgnoreCase("artistActivity")) return artistActivity(parameters);// 展览
         else if (pageName.equalsIgnoreCase("artistLiterature")) return artistLiterature(parameters);// 经典文献
         else if (pageName.equalsIgnoreCase("artistDerivative")) return artistDerivative(parameters);// 衍生品
         else if (pageName.equalsIgnoreCase("artistCase")) return artistCase(parameters);// 艺虎个案
         else if (pageName.equalsIgnoreCase("artistNew")) return artistNew(parameters);// 最新动态
         else if (pageName.equalsIgnoreCase("artistExperience")) return artistExperience(parameters);// 人生年表
      }
      catch (ServiceException e) {
         e.printStackTrace();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   private List<Map<String, Object>> aristPage(Map<String, String> parameters) throws ServiceException {
      System.out.println("aristPage...");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         String artistId = returnString(parameters.get("artistId"));
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         artistToMap(map, artistId);
         if (map.get("artistId") == null) { return null; }
         researchToMap(map, artistId);
//         experienceToMap(map, artistId);
         numToMap(map, artistId);
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }
   
   private List<Map<String, Object>> aristHeat(Map<String, String> parameters) throws ServiceException {
      System.out.println("aristHeat...");
      try {
         String artistId = returnString(parameters.get("artistId"));
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         List<ArtArtistHeat> artArtistHeats = artArtistHeatDao.findByField(ArtArtistHeat.PROP_ARTIST_ID, Integer.valueOf(artistId));
         if(artArtistHeats == null || artArtistHeats.size() == 0){
            ArtArtistHeat artArtistHeat = new ArtArtistHeat();
            artArtistHeat.setArtistId(Integer.valueOf(artistId));
            artArtistHeat.setHeatNum(1);
            artArtistHeat.setHeatTime(new Date());
            artArtistHeatDao.save(artArtistHeat);
         }else{
            ArtArtistHeat artArtistHeat = artArtistHeats.get(0);
            artArtistHeat.setHeatNum(artArtistHeat.getHeatNum()+1);
            artArtistHeat.setHeatTime(new Date());
            artArtistHeatDao.update(artArtistHeat);
         }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("message", "success");
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> aristFollow(Map<String, String> parameters) throws ServiceException {
      System.out.println("aristFollow...");
      try {
         int userId = isLogin(parameters.get("account"), parameters.get("password"));
         if (userId <= 1) { throw new ServiceException("请先登陆！"); }
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         int artistId = Integer.valueOf(returnString(parameters.get("artistId")));
         List<ArtArtistFollow> artArtistFollows = artArtistFollowDao.findByFields(new String[] { ArtArtistFollow.PROP_APP_USER_ID,
               ArtArtistFollow.PROP_ARTIST_ID }, new Object[] { userId, artistId });
         if (artArtistFollows != null && artArtistFollows.size() > 0) { throw new ServiceException("已经关注该艺术家！"); }
         ArtArtistFollow artArtistFollow = new ArtArtistFollow();
         artArtistFollow.setAppUserId(userId);
         artArtistFollow.setArtistId(artistId);
         artArtistFollow.setFollowTime(new Date());
         artArtistFollowDao.save(artArtistFollow);
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         map.put("message", "success");
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> baseFile(Map<String, String> parameters) throws ServiceException {
      System.out.println("baseFile...");
      try {
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         int artistId = Integer.valueOf(returnString(parameters.get("artistId")));

         List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
         dataToBase(allList, artistId);
         return allList;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> activity(Map<String, String> parameters) throws ServiceException {
      System.out.println("activity...");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         String artistId = parameters.get("artistId");
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         String sql = "SELECT * FROM art_activity_exhibit exhibit " + " LEFT JOIN art_activity_exhibit_artist artist ON artist.exhibit_id=exhibit.id "
               + " WHERE artist.artist_id=" + artistId;
         List<Map<String, Object>> exhibitList = jdbcDao.queryBySql(sql);
         List<Map<String, Object>> personList = new ArrayList<Map<String, Object>>();
         List<Map<String, Object>> peopleList = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> exhibitMap : exhibitList) {
            Map<String, Object> exhibit = new HashMap<String, Object>();
            String time = "";
            time += returnString(exhibitMap.get(ArtActivityExhibit.COL_ACTIVITY_YEAR)).equals("") ? ""
                  : (exhibitMap.get(ArtActivityExhibit.COL_ACTIVITY_YEAR) + "年");
            time += returnString(exhibitMap.get(ArtActivityExhibit.COL_ACTIVITY_MONTH)).equals("") ? "" : (exhibitMap
                  .get(ArtActivityExhibit.COL_ACTIVITY_MONTH) + "月");
            exhibit.put("time", time);
            exhibit.put("message", exhibitMap.get(ArtActivityExhibit.COL_EXHIBIT_NAME));
            if ("1".equals(exhibitMap.get(ArtActivityExhibit.COL_EXHIB_TYPE).toString())) {
               personList.add(exhibit);
            }
            if ("2".equals(exhibitMap.get(ArtActivityExhibit.COL_EXHIB_TYPE).toString())) {
               peopleList.add(exhibit);
            }
         }
         map.put("person", personList);
         map.put("people", peopleList);
         sql = "SELECT activityAbmb.abmb_name,activityAbmb.abmb_year,activityAbmb.abmb_month FROM art_activity_abmb activityAbmb "
               + " LEFT JOIN art_works_abmb worksAbmb ON worksAbmb.abmb_id=activityAbmb.id " + " LEFT JOIN art_works works ON works.id=worksAbmb.works_id "
               + " LEFT JOIN art_artist artist ON artist.id=works.artist_id " + " WHERE artist.id=" + artistId;
         List<Map<String, Object>> abmbList = jdbcDao.queryBySql(sql);
         List<Map<String, Object>> abmbs = new ArrayList<Map<String, Object>>();
         for (Map<String, Object> abmbMap : abmbList) {
            Map<String, Object> abmb = new HashMap<String, Object>();
            String time = "";
            time += returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_YEAR)).equals("") ? "" : (returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_YEAR)) + "年");
            time += returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_MONTH)).equals("") ? ""
                  : (returnString(abmbMap.get(ArtActivityAbmb.COL_ABMB_MONTH)) + "月");
            abmb.put("time", time);
            abmb.put("message", abmbMap.get(ArtActivityAbmb.COL_ABMB_NAME));
            abmbs.add(abmb);
         }
         map.put("abmb", abmbs);
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> artistPhoto(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistPhoto...");
      try {
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/");
         String artistId = returnString(parameters.get("artistId"));
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         int startNum = Integer.valueOf(returnString(parameters.get("startNum"))) - 1;
         String requestNum = returnString(parameters.get("requestNum"));
         if(requestNum == null){
            requestNum = "10";
         }
         String sql = "SELECT CONCAT('photo/',artist.folder_name,'/',artPhoto.photo) AS photoPath FROM art_photo artPhoto "
               + " LEFT JOIN art_artist artist ON artist.id=artPhoto.artist_id " + " WHERE artPhoto.artist_id=" + artistId
               + " ORDER BY artPhoto.id LIMIT " + startNum + "," + requestNum;
         List<Map<String, Object>> list = jdbcDao.queryBySql(sql);
         for (Map<String, Object> map : list) {
            String path = FILE_PATH + File.separator + returnString(map.get("photoPath"));
            File file = new File(path);
            if (file.exists() && file.isFile()) {
               BufferedImage bufferedImage = ImageIO.read(file);
               int width = bufferedImage.getWidth();
               int height = bufferedImage.getHeight();
               map.put("width", width);
               map.put("height", height);
            }
         }
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> artistWorks(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistWorks...");
      try {
         Map<String, Object> map = new HashMap<String, Object>();
         String artistId = returnString(parameters.get("artistId"));
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         int startNum = Integer.valueOf(returnString(parameters.get("startNum"))) - 1;
         String requestNum = returnString(parameters.get("requestNum"));
         if(requestNum == null){
            requestNum = "10";
         }
         List<ArtMedium> artMediums = artMediumDao.findAll();
         Map<String, Object> mediumMap = new HashMap<String, Object>();
         for (ArtMedium medium : artMediums) {
            mediumMap.put(medium.getId().toString(), medium.getMediumName());
         }
         String sql = "SELECT works.id,CONCAT('auction/',artist.folder_name,'/',works.thumbnail) AS photo,"
               + " works.medium_material AS material,works.medium_shape AS shape,works.works_status AS descript"
               + " FROM art_works works " + " LEFT JOIN art_artist artist ON artist.id=works.artist_id " + " WHERE works.artist_id=" + artistId
               + " ORDER BY works.id LIMIT " + startNum + "," + requestNum;
         List<Map<String, Object>> worksList = jdbcDao.queryBySql(sql);
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/");
         for (Map<String, Object> worksMap : worksList) {
            String medium = findMedium(worksMap, mediumMap);
            worksMap.put("medium", medium);
            worksMap.remove("material");
            worksMap.remove("shape");
            String path = FILE_PATH + File.separator + returnString(worksMap.get("photo"));
            File file = new File(path);
            if (file.exists() && file.isFile()) {
               BufferedImage bufferedImage = ImageIO.read(file);
               int width = bufferedImage.getWidth();
               int height = bufferedImage.getHeight();
               worksMap.put("width", width);
               worksMap.put("height", height);
            }else{
               worksMap.put("width", "50");
               worksMap.put("height", "50");
            }
         }
         map.put("works", worksList);
         String count = "SELECT COUNT(*) AS number,artist.photo AS artistPhoto,artist.folder_name AS folderName," + " artist.c_name AS name "
               + " FROM art_works works " + " LEFT JOIN art_artist artist ON artist.id=works.artist_id " + " WHERE works.artist_id=" + artistId
               + " GROUP BY artist.id ";
         List<Map<String, Object>> list = jdbcDao.queryBySql(count);
         for(Map<String, Object> artistMap : list){
            if(!"".equals(returnString(artistMap.get("artistPhoto")))){
               String photo = returnString(artistMap.get("artistPhoto"));
               if(photo.split("/").length>1){
                  String photoPath = "photo/" + returnString(artistMap.get("folderName")) + "/" + photo.split("/")[1];
                  artistMap.put("artistPhoto", photoPath);
               }else{
                  artistMap.put("artistPhoto", null);
               }
            }
            artistMap.remove("folderName");
         }
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private String findMedium(Map<String, Object> map, Map<String, Object> mediumMap){
      try {
         String medium = returnString(map.get("material")).trim();
         if(medium.endsWith(",")){
            medium+= returnString(map.get("shape")).trim();
         }else{
            medium+= "," + returnString(map.get("shape")).trim();
         }
         String[] mediums = medium.split(",");
         String returnMedium = "";
         for(int i = 0; i < mediums.length; i++){
            if(!"".equals(returnMedium)){
               returnMedium+=",";
            }
            if(mediumMap.get(mediums[i]) != null){
               returnMedium+=mediumMap.get(mediums[i]);
            }
         }
         return returnMedium;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return "";
   }
   
   private List<Map<String, Object>> artistAuction(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistAuction...");
      try {

      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> artistPublication(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistPublication...");
      return null;
   }

   private List<Map<String, Object>> artistActivity(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistActivity...");
      return null;
   }

   private List<Map<String, Object>> artistLiterature(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistLiterature...");
      try {
         String artistId = returnString(parameters.get("artistId"));
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         List<ArtLiteratureNetwork> artLiteratureNetworks = artLiteratureNetworkDao.findByField(ArtLiteratureNetwork.PROP_ARTIST_ID, Integer.valueOf(artistId));
         if (artLiteratureNetworks != null) {
            map.put("netNumber", artLiteratureNetworks.size());
         }
         List<Map<String, Object>> netList = new ArrayList<Map<String, Object>>();
         for (ArtLiteratureNetwork network : artLiteratureNetworks) {
            Map<String, Object> netMap = new HashMap<String, Object>();
            netMap.put("title", network.getLiteratureTitle());
            netMap.put("auther", network.getLiteratureAuther());
            netMap.put("soure", network.getLiteratureSource());
            netMap.put("time", network.getPublicationTime());
            netList.add(netMap);
         }
         map.put("net", netList);
         List<ArtLiteratureWords> artLiteratureWords = artLiteratureWordsDao.findByField(ArtLiteratureNetwork.PROP_ARTIST_ID, Integer.valueOf(artistId));
         if (artLiteratureWords != null) {
            map.put("wordNumber", artLiteratureWords.size());
         }
         List<Map<String, Object>> wordsList = new ArrayList<Map<String, Object>>();
         for (ArtLiteratureWords words : artLiteratureWords) {
            Map<String, Object> wordsMap = new HashMap<String, Object>();
            wordsMap.put("title", words.getLiteratureTitle());
            wordsMap.put("auther", words.getLiteratureAuther());
            wordsMap.put("publicationName", words.getPublicationName());
            wordsMap.put("time", words.getWriteTime());
            wordsList.add(wordsMap);
         }
         map.put("word", wordsList);
         List<ArtLiteratureMedium> artLiteratureMediums = artLiteratureMediumDao.findByField(ArtLiteratureNetwork.PROP_ARTIST_ID, Integer.valueOf(artistId));
         if (artLiteratureMediums != null) {
            map.put("musicNumber", artLiteratureMediums.size());
         }
         List<Map<String, Object>> mediumList = new ArrayList<Map<String, Object>>();
         for (ArtLiteratureMedium medium : artLiteratureMediums) {
            Map<String, Object> mediumMap = new HashMap<String, Object>();
            mediumMap.put("title", medium.getLiteratureTitle());
            mediumMap.put("auther", medium.getShotPeople());
            mediumMap.put("link", medium.getSiteLink());
            mediumMap.put("time", medium.getShotTime());
            mediumList.add(mediumMap);
         }
         map.put("music", mediumList);
         list.add(map);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> artistDerivative(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistDerivative...");
      return null;
   }

   private List<Map<String, Object>> artistCase(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistCase...");
      return null;
   }

   private List<Map<String, Object>> artistNew(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistNew...");
      return null;
   }

   private void dataToBase(List<Map<String, Object>> allList, int artistId) {
      List<ArtWorks> artWorks = artWorksDao.findAll();
      Map<String, Object> worksMap = new HashMap<String, Object>();
      for (ArtWorks works : artWorks) {
         worksMap.put(works.getId().toString(), works.getWorksCName());
      }
      List<ArtAgency> artAgencies = artAgencyDao.findAll();
      Map<String, Object> agencyMap = new HashMap<String, Object>();
      for (ArtAgency agency : artAgencies) {
         agencyMap.put(agency.getId().toString(), agency.getAgencyCName());
      }

      Map<String, Object> map = new HashMap<String, Object>();
      ArtArtist artArtist = artArtistService.getArtArtist(artistId);
      
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      Map<String, Object> artistMap = new HashMap<String, Object>();
      artistMap.put("message", artArtist.getCresume());
      map.put("resume", artistMap);
      //工作教育
      list = new ArrayList<Map<String, Object>>();
      List<ArtArtistEdu> artArtistEdus = artArtistEduDao.findByField(ArtArtistEdu.PROP_ARTIST_ID, artistId);
      for (ArtArtistEdu artArtistEdu : artArtistEdus) {
         Map<String, Object> eduMap = new HashMap<String, Object>();
         eduMap.put("time", artArtistEdu.getEduTime());
         eduMap.put("message", artArtistEdu.getEduDesc());
         list.add(eduMap);
      }
      map.put("edu", list);
      //获得荣誉
      list = new ArrayList<Map<String, Object>>();
      List<ArtArtistHonors> artArtistHonors = artArtistHonorsDao.findByField(ArtArtistEdu.PROP_ARTIST_ID, artistId);
      for (ArtArtistHonors honors : artArtistHonors) {
         Map<String, Object> honorsMap = new HashMap<String, Object>();
         honorsMap.put("time", honors.getHonorTime());
         honorsMap.put("message", honors.getHonorDesc());
         list.add(honorsMap);
      }
      map.put("honors", list);
      //收藏与捐赠
      list = new ArrayList<Map<String, Object>>();
      List<ArtArtistCollector> artArtistCollectors = artArtistCollectorDao.findByField(ArtArtistEdu.PROP_ARTIST_ID, artistId);
      for (ArtArtistCollector collector : artArtistCollectors) {
         Map<String, Object> collectorMap = new HashMap<String, Object>();
         collectorMap.put("time", collector.getCollectTime());
         String works = collector.getCollectWorks();
         String[] collectWorks = works.split(",");
         String message = "";
         for (int i = 0; i < collectWorks.length; i++) {
            if (!"".equals(message)) {
               message += ",";
            }
            message += returnString(worksMap.get(collectWorks[i]));
         }
         collectorMap.put("message", returnString(collector.getCollector()) + " " + message);
         list.add(collectorMap);
      }
      List<ArtArtistCollectAgency> artArtistCollectAgencys = artArtistCollectAgencyDao.findByField(ArtArtistEdu.PROP_ARTIST_ID, artistId);
      for (ArtArtistCollectAgency agency : artArtistCollectAgencys) {
         Map<String, Object> collectorMap = new HashMap<String, Object>();
         collectorMap.put("time", agency.getCollectTime());
         String works = agency.getCollectWorks();
         String[] collectWorks = works.split(",");
         String message = "";
         for (int i = 0; i < collectWorks.length; i++) {
            if (!"".equals(message)) {
               message += ",";
            }
            message += returnString(worksMap.get(collectWorks[i]));
         }
         collectorMap.put("message", returnString(agencyMap.get(agency.getAgencyId().toString())) + " " + message);
         list.add(collectorMap);
      }
      List<ArtArtistDonation> artArtistDonations = artArtistDonationDao.findByField(ArtArtistDonation.PROP_ARTIST_ID, artistId);
      for (ArtArtistDonation donation : artArtistDonations) {
         Map<String, Object> collectorMap = new HashMap<String, Object>();
         collectorMap.put("time", donation.getDonationTime());
         String works = donation.getDonationWorks();
         String[] collectWorks = works.split(",");
         String message = "";
         for (int i = 0; i < collectWorks.length; i++) {
            if (!"".equals(message)) {
               message += ",";
            }
            message += returnString(worksMap.get(collectWorks[i]));
         }
         collectorMap.put("message", returnString(donation.getDonationDesc()) + " " + message);
         list.add(collectorMap);
      }
      map.put("collect", list);
      //合作机构
      list = new ArrayList<Map<String, Object>>();
      List<ArtArtistCoop> artArtistCoops = artArtistCoopDao.findByField(ArtArtistEdu.PROP_ARTIST_ID, artistId);
      for (ArtArtistCoop coop : artArtistCoops) {
         Map<String, Object> coopMap = new HashMap<String, Object>();
         coopMap.put("time", coop.getCoopTime());
         coopMap.put("message", returnString(agencyMap.get(coop.getAgencyId().toString())) + " " + returnString(coop.getCoopDesc()));
         list.add(coopMap);
      }
      map.put("coop", list);
      allList.add(map);
   }

   private void artistToMap(Map<String, Object> map, String artistId) {
      String sql = "";
      sql = "SELECT"
      + " artist.id AS artistId,artist.artist_type AS artistType,artist.*,COUNT(follow.id) AS follow," + " COUNT(worksfollow.id) AS commentaryNum,heat.heat_num AS heatNum "
            + " FROM art_artist artist "
            + " LEFT JOIN art_artist_follow follow ON follow.artist_id = artist.id "
            + " LEFT JOIN art_works works ON works.artist_id = artist.id "
            + " LEFT JOIN art_works_follow worksfollow ON worksfollow.works_id = works.id "
            + " LEFT JOIN art_artist_heat heat ON heat.artist_id = artist.id " + " WHERE artist.id=" + artistId
            + " group by artist.id ,artist.artist_type ,heat.heat_num ";
      List<Map<String, Object>> artistList = jdbcDao.queryBySql(sql);
      if (artistList == null || artistList.size() == 0) { return; }
      for (Map<String, Object> artistMap : artistList) {
         map.put("artistId", artistMap.get("artistId"));
         map.put("cname", artistMap.get(ArtArtist.COL_C_NAME));
         map.put("ename", artistMap.get(ArtArtist.COL_E_NAME));
         String photo = "";
         if (artistMap.get(ArtArtist.COL_PHOTO) != null) {
            if (artistMap.get(ArtArtist.COL_PHOTO).toString().split("/").length > 0) {
               photo = "photo/" + returnString(artistMap.get(ArtArtist.COL_FOLDER_NAME)) + "/"
                     + artistMap.get(ArtArtist.COL_PHOTO).toString().split("/")[1];
            }
         }
         map.put("photo", photo);
         map.put("follow", artistMap.get("follow"));
         String artistType = "";
         if(artistMap.get("artistType") != null && !"".equals(artistMap.get("artistType"))){
            artistType = sysCodeService.findCodeByItemValue(artistMap.get("artistType").toString(), 20);
         }
         map.put("artistType", artistType);
         map.put("commentaryNum", artistMap.get("commentaryNum"));
         String mainMedia = "";
         if (artistMap.get(ArtArtist.COL_MAIN_MEDIA) != null) {
            mainMedia = artistMap.get(ArtArtist.COL_MAIN_MEDIA).toString();
            String[] mainMedias = mainMedia.split(",");
            mainMedia = "";
            for (int i = 0; i < mainMedias.length; i++) {
               if (!mainMedia.equals("")) {
                  mainMedia += ",";
               }
               if(!"".equals(mainMedias[i].trim())){
                  mainMedia += sysCodeService.findCodeByItemValue(mainMedias[i], 13);
               }
            }
         }
         map.put("mainMedia", mainMedia);
         map.put("heatNum", artistMap.get("heatNum"));
      }
   }

   private void researchToMap(Map<String, Object> map, String artistId) {
      String researchSql = "select page.* FROM art_artist_page_research research "
            + " LEFT JOIN art_artist_template tem ON research.temp_id = tem.id "
            + " LEFT JOIN art_artist_page page ON page.temp_id=tem.id "
            + " WHERE research.artist_id=" + artistId;
      List<Map<String, Object>> researchList = jdbcDao.queryBySql(researchSql);
      String path = "app/artistpage/";
      String[] colors = new String[] { ArtArtistPage.COL_COLOR, ArtArtistPage.COL_COLOR_SELF };
      for (Map<String, Object> researchMap : researchList) {
         if (researchMap.get(ArtArtistPage.COL_SEQ_NO) == null) {
            continue;
         }
         else if ("1".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "artistPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "artistFontColor", colors);
         }
         else if ("2".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "honorsPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "honorsFontColor", colors);
         }
         else if ("3".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "photoPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "photoFontColor", colors);
         }
         else if ("4".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "worksPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "worksFontColor", colors);
         }
         else if ("5".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "auctionPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "auctionFontColor", colors);
         }
         else if ("6".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "publicationPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "publicationFontColor", colors);
         }
         else if ("7".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "activityPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "activityFontColor", colors);
         }
         else if ("8".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "literaturePhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "literatureFontColor", colors);
         }
         else if ("9".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "derivativePhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "derivativeFontColor", colors);
         }
         else if ("10".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "casePhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "caseFontColor", colors);
         }
         else if ("11".equals(researchMap.get(ArtArtistPage.COL_SEQ_NO).toString())) {
            mapPut(map, "newPhoto", researchMap.get(ArtArtistPage.COL_PHOTO), true, path);
            mapTomap(map, researchMap, "newFontColor", colors);
         }
      }
   }
   
   private List<Map<String, Object>> artistExperience(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistExperience...");
      try {
         String artistId = returnString(parameters.get("artistId"));
         if (!isInteger(returnString(parameters.get("artistId")))) { throw new ServiceException("艺术家id错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         list = experienceToMap(list, artistId);
         return list;
      }
      catch (ServiceException e) {
         e.printStackTrace();
         List<Map<String, Object>> list = addError(e.getMessage());
         return list;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   private List<Map<String, Object>> experienceToMap(List<Map<String, Object>> list, String artistId) {
      String sql = "select SUBSTRING(experience.expe_time, 1, 4) AS time,experience.life_experience AS life,experience.history_experience AS history FROM art_artist_experience experience "
            + " WHERE SUBSTRING(experience.expe_time, 1, 4) IN ("
            + " SELECT SUBSTRING(experience.expe_time, 1, 4) AS time FROM art_artist_experience experience "
            + " WHERE experience.art_id =" + artistId + " GROUP BY experience.expe_time)"
            + " AND experience.art_id=" + artistId
            + " GROUP BY time ORDER BY time DESC";
      list = jdbcDao.queryBySql(sql);
      return list;
   }

   private void mapTomap(Map<String, Object> map, Map<String, Object> outMap, String inMapKey, String[] outMapKey) {
      if (!"".equals(returnString(outMap.get(outMapKey[0])))) {
         mapPut(map, inMapKey, outMap.get(outMapKey[0]), false, "");
      }
      else if (!"".equals(returnString(outMap.get(outMapKey[1])))) {
         mapPut(map, inMapKey, outMap.get(outMapKey[1]), false, "");
      }
   }

   private void mapPut(Map<String, Object> map, String mapKey, Object mapValue, boolean addStr, String addValue) {
      if (addStr) {
         if (mapValue != null) {
            if(mapValue.toString().split("/").length >1){
               mapValue = addValue + mapValue.toString().split("/")[1];
            }else{
               mapValue = addValue + mapValue;
            }
         }
      }
      map.put(mapKey, mapValue);
   }
   
   private void numToMap(Map<String, Object> map, String artistId){
      String photoSql = "SELECT COUNT(*) FROM art_photo photo WHERE photo.artist_id=" + artistId;
      map.put("photoNum", jdbcDao.queryIntBySql(photoSql));
      String worksSql = "SELECT COUNT(*) FROM art_works works WHERE works.artist_id=" + artistId;
      map.put("worksNum", jdbcDao.queryIntBySql(worksSql));
      String auctionSql = "SELECT COUNT(*) FROM art_auction auction "
            + " LEFT JOIN art_works works ON auction.works_id = works.id "
            + " WHERE works.artist_id=" + artistId;
      map.put("auctionNum", jdbcDao.queryIntBySql(auctionSql));
      String publicationSql = "SELECT COUNT(*) FROM art_publication publication WHERE publication.artist_id=" + artistId;
      map.put("publicationNum", jdbcDao.queryIntBySql(publicationSql));
      String wordSql = "SELECT COUNT(*) FROM art_literature_words words WHERE words.artist_id=" + artistId;
      String netSql = "SELECT COUNT(*) FROM art_literature_network network WHERE network.artist_id=" + artistId;
      String mediumSql = "SELECT COUNT(*) FROM art_literature_medium medium WHERE medium.artist_id=" + artistId;
      map.put("mediumNum", jdbcDao.queryIntBySql(wordSql)+jdbcDao.queryIntBySql(netSql)+jdbcDao.queryIntBySql(mediumSql));
   }

   private int isLogin(String account, String password) {
      if (account == null || password == null || "".equals(account) || "".equals(password)) { return 0; }
      List<ArtAppUser> artAppUsers = artAppUserDao.findByField(ArtAppUser.PROP_ACCOUNT_NAME, account);
      password = Encryption.encrypt(password.getBytes());
      if (artAppUsers != null && artAppUsers.size() == 1) {
         ArtAppUser artAppUser = artAppUsers.get(0);
         if ("0".equals(artAppUser.getUserStatus())) { return -1; }
         if (password.equals(returnString(artAppUser.getPassword()))) { return artAppUser.getId(); }
      }
      if (!isInteger(account)) { return 0; }
      artAppUsers = artAppUserDao.findByField(ArtAppUser.PROP_PHONE_NUMBER, Integer.valueOf(account));
      if (artAppUsers != null && artAppUsers.size() == 1) {
         ArtAppUser artAppUser = artAppUsers.get(0);
         if ("0".equals(artAppUser.getUserStatus())) { return -1; }
         if (password.equals(returnString(artAppUser.getPassword()))) { return artAppUser.getId(); }
      }
      return 0;
   }

   private boolean isInteger(String account) {
      try {
         Integer.valueOf(account);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }

   private String returnString(Object obj) {
      if (obj == null) { return ""; }
      return obj.toString();
   }

   private List<Map<String, Object>> addError(String message) {
      Map<String, Object> map = new HashMap<String, Object>();
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      map.put("error", message);
      list.add(map);
      return list;
   }

   @Resource
   private ArtAppUserDao             artAppUserDao;

   @Resource
   private ArtArtistFollowDao        artArtistFollowDao;

   @Resource
   private ArtArtistService          artArtistService;

   @Resource
   private ArtArtistEduDao           artArtistEduDao;

   @Resource
   private ArtArtistHonorsDao        artArtistHonorsDao;

   @Resource
   private ArtArtistCollectorDao     artArtistCollectorDao;

   @Resource
   private ArtArtistCollectAgencyDao artArtistCollectAgencyDao;

   @Resource
   private ArtArtistCoopDao          artArtistCoopDao;

   @Resource
   private ArtArtistDonationDao      artArtistDonationDao;

   @Resource
   private ArtWorksDao               artWorksDao;
   
   @Resource
   private ArtMediumDao               artMediumDao;

   @Resource
   private ArtAgencyDao              artAgencyDao;

   @Resource
   private ArtLiteratureMediumDao    artLiteratureMediumDao;

   @Resource
   private ArtLiteratureNetworkDao   artLiteratureNetworkDao;

   @Resource
   private ArtLiteratureWordsDao     artLiteratureWordsDao;
   
   @Resource
   private ArtArtistHeatDao     artArtistHeatDao;

   @Resource
   private SysCodeService            sysCodeService;
}
