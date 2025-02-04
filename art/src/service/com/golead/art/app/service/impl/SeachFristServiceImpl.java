package com.golead.art.app.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.golead.art.app.service.SeachFristService;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.literature.dao.ArtLiteratureMediumDao;
import com.golead.art.literature.dao.ArtLiteratureNetworkDao;
import com.golead.art.literature.dao.ArtLiteratureWordsDao;
import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureNetwork;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.util.ChinaInitial;
import com.golead.art.works.dao.ArtMediumDao;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class SeachFristServiceImpl extends BaseServiceImpl implements SeachFristService {

   @Override
   public List<Map<String, Object>> seachArt(Map<String, String> parameters) throws ServiceException {
      try {
         String pageName = parameters.get("pageName");
         if (pageName.equalsIgnoreCase("search")) return search(parameters);// 艺术家/艺术品搜索
         if (pageName.equalsIgnoreCase("allArtist")) return allArtist(parameters);// 艺术家搜索
         if (pageName.equalsIgnoreCase("artistWorks")) return artistWorks(parameters);// 艺术品搜索
         if (pageName.equalsIgnoreCase("artistLiterature")) return artistLiterature(parameters);// 文献搜索
      }
      catch (ServiceException e) {
         e.printStackTrace();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   private List<Map<String, Object>> search(Map<String, String> parameters) throws ServiceException {
      System.out.println("search...");
      try {
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         String condition = returnString(parameters.get("condition")).trim();
         if ("".equals(returnString(condition).trim())) { throw new ServiceException("筛选条件错误！"); }
         condition = URLDecoder.decode(condition, "UTF-8");
         String sql = "";
         sql = "SELECT artist.id AS id,artist.c_name AS name FROM art_artist artist " + "WHERE artist.c_name LIKE '%" + condition + "%'";
         list = jdbcDao.queryBySql(sql);
         if (list != null && list.size() > 0) { return list; }
         sql = "SELECT artist.id AS id,artist.e_name AS name FROM art_artist artist " + "WHERE artist.e_name LIKE '%" + condition + "%'";
         list = jdbcDao.queryBySql(sql);
         if (list != null && list.size() > 0) { return list; }
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

   private List<Map<String, Object>> allArtist(Map<String, String> parameters) throws ServiceException {
      System.out.println("allArtist...");
      try {
         String sql = "SELECT artist.id AS id,artist.c_name AS name,"
               + "CONCAT('auction/',artist.folder_name,'/',artist.works_photo) AS worksPhoto FROM art_artist artist ORDER BY convert(artist.c_name using gbk) ASC";
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         List<Map<String, Object>> artistList = jdbcDao.queryBySql(sql);
         for (Map<String, Object> artistMap : artistList) {
            String name = returnString(artistMap.get("name"));
            String firstStr = "#";
            if (name.length() > 0) {
               firstStr = ChinaInitial.getPYIndexStr(name.substring(0, 1), true);
            }
            if(map.get(firstStr) != null){
               List<Map<String, Object>> newArtistList = (List<Map<String, Object>>) map.get(firstStr);
               newArtistList.add(artistMap);
               map.put(firstStr, newArtistList);
            }else{
               List<Map<String, Object>> newArtistList = new ArrayList<Map<String, Object>>();
               newArtistList.add(artistMap);
               map.put(firstStr, newArtistList);
            }
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

   private List<Map<String, Object>> artistWorks(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistWorks...");
      try {
         String artistId = parameters.get("artistId");
         String type = returnString(parameters.get("type"));
         if ("artist".equalsIgnoreCase(type)) {
        	 if ("".equals(returnString(artistId).trim())) { throw new ServiceException("艺术家id错误！"); }
         }
         String condition = parameters.get("condition");
         if ("".equals(returnString(condition).trim())) { throw new ServiceException("筛选条件错误！"); }
         if(!isInteger(returnString(parameters.get("startNum")))){
            throw new ServiceException("开始数量错误！"); 
         }
         int startNum = Integer.valueOf(returnString(parameters.get("startNum"))) - 1;
         String requestNum = returnString(parameters.get("requestNum"));
         List<ArtMedium> artMediums = artMediumDao.findAll();
         Map<String, String> mediumMap = new HashMap<String, String>();
         for(ArtMedium artMedium : artMediums){
            mediumMap.put(artMedium.getId().toString(), artMedium.getMediumName());
         }
         String where = "";
         if("artist".equals(type)){
            where = " WHERE works.artist_id=" + artistId
                  + " AND (works.works_c_name LIKE '%" + condition + "%' OR works.works_e_name LIKE '%" + condition + "%')";
         }else{
            where = " WHERE works.works_c_name LIKE '%" + condition + "%' OR works.works_e_name LIKE '%" + condition + "%'";
         }
         if("".equals(requestNum.trim())){
            requestNum = "10";
         }
         String sql = "SELECT works.id,CONCAT(works.works_c_name,' ',works.works_e_name) AS name,CONCAT('auction/',artist.folder_name,'/',works.thumbnail) AS photo,"
               + " artist.c_name AS artistName,works.create_year AS createYear,"
               + " works.medium_material AS material,works.medium_shape AS shape,"
               + " works_status AS descript "
               + " FROM art_works works "
               + " LEFT JOIN art_artist artist ON artist.id=works.artist_id "
               + where
               + " ORDER BY works.id LIMIT "
               + startNum + "," + requestNum;
         List<Map<String, Object>> list = jdbcDao.queryBySql(sql);
         for(Map<String, Object> map : list){
            String medium = findMedium(map, mediumMap);
            map.put("medium", medium);
            map.remove("material");
            map.remove("shape");
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
   
   private String findMedium(Map<String, Object> map, Map<String, String> mediumMap){
      try {
         String medium = returnString(map.get("material"));
         if(medium.endsWith(",")){
            medium+= returnString(map.get("shape"));
         }else{
            medium+= "," + returnString(map.get("shape"));
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

   private List<Map<String, Object>> artistLiterature(Map<String, String> parameters) throws ServiceException {
      System.out.println("artistLiterature...");
      try {
         String artistId = parameters.get("artistId");
         if ("".equals(returnString(artistId).trim())) { throw new ServiceException("艺术家id错误！"); }
         String condition = parameters.get("condition");
         if ("".equals(returnString(condition).trim())) { throw new ServiceException("筛选条件错误！"); }
         List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
         Map<String, Object> map = new HashMap<String, Object>();
         String Hql = "FROM ArtLiteratureNetwork network WHERE network.artistId=" + artistId + " AND network.literatureTitle LIKE '%" + condition + "%'";
         List<ArtLiteratureNetwork> artLiteratureNetworks = artLiteratureNetworkDao.findByHql(Hql);
         //         List<ArtLiteratureNetwork> artLiteratureNetworks = artLiteratureNetworkDao.findByField(ArtLiteratureNetwork.PROP_ARTIST_ID, Integer.valueOf(artistId));
         if (artLiteratureNetworks != null) {
            map.put("netNumber", artLiteratureNetworks.size());
         }else{
            map.put("netNumber", "0");
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
         Hql = "FROM ArtLiteratureWords words WHERE words.artistId=" + artistId + " AND words.literatureTitle LIKE '%" + condition + "%'";
         List<ArtLiteratureWords> artLiteratureWords = artLiteratureWordsDao.findByHql(Hql);
         if (artLiteratureWords != null) {
            map.put("wordNumber", artLiteratureWords.size());
         }else{
            map.put("wordNumber", "0");
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
         Hql = "FROM ArtLiteratureMedium medium WHERE medium.artistId=" + artistId + " AND medium.literatureTitle LIKE '%" + condition + "%'";
         List<ArtLiteratureMedium> artLiteratureMediums = artLiteratureMediumDao.findByHql(Hql);
         if (artLiteratureMediums != null) {
            map.put("musicNumber", artLiteratureMediums.size());
         }else{
            map.put("musicNumber", "0");
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

   private boolean isInteger(String account) {
      try {
         Integer.valueOf(account);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }
   
   @Resource
   private ArtMediumDao  artMediumDao;

   @Resource
   private ArtLiteratureMediumDao  artLiteratureMediumDao;

   @Resource
   private ArtLiteratureNetworkDao artLiteratureNetworkDao;

   @Resource
   private ArtLiteratureWordsDao   artLiteratureWordsDao;
}
