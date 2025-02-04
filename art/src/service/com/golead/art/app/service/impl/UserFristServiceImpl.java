package com.golead.art.app.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.golead.art.app.appUser.dao.ArtAppUserDao;
import com.golead.art.app.appUser.model.ArtAppUser;
import com.golead.art.app.appUser.service.ArtAppUserService;
import com.golead.art.app.service.UserFristService;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.Encryption;

@Service
public class UserFristServiceImpl extends BaseServiceImpl implements UserFristService {

   //   private static String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/");

   @Override
   public List<Map<String, Object>> appUser(Map<String, String> parameters) throws ServiceException {
      try {
         String pageName = parameters.get("pageName");
         if (pageName.equalsIgnoreCase("login")) return login(parameters);// 登陆
         if (pageName.equalsIgnoreCase("modifyPSW")) return modifyPSW(parameters);// 修改密码
         if (pageName.equalsIgnoreCase("worksFollow")) return worksFollow(parameters);// 关注艺术品
      }
      catch (ServiceException e) {
         e.printStackTrace();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   @Override
   public String addUser(Map<String, String> parameters, File photo) throws ServiceException {
      try {
         String pageName = parameters.get("pageName");
         if (pageName.equalsIgnoreCase("register")) return register(parameters, photo);// 登陆
      }
      catch (ServiceException e) {
         e.printStackTrace();
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

   private List<Map<String, Object>> login(Map<String, String> parameters) throws ServiceException {
      System.out.println("login...");
      try {
         String account = returnString(parameters.get("account"));
         String password = returnString(parameters.get("password"));
         if ("".equals(account) || "".equals(password)) { throw new ServiceException("账号或密码为空！"); }
         int id = isLogin(account, password);
         if (id == 0) { throw new ServiceException("没有找到用户！"); }
         if (id == -1) { throw new ServiceException("该用户被禁用！"); }
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

   private List<Map<String, Object>> modifyPSW(Map<String, String> parameters) throws ServiceException {
      System.out.println("modifyPSW...");
      try {
         int userId = isLogin(parameters.get("account"), parameters.get("oldpassword"));
         String newpassword = returnString(parameters.get("newpassword"));
         if (userId <= 1) { throw new ServiceException("请先登陆！"); }
         if ("".equals(newpassword)) { throw new ServiceException("密码不能为空！"); }
         ArtAppUser artAppUser = artAppUserDao.get(Integer.valueOf(userId));
         if (artAppUser == null) { throw new ServiceException("没有找到用户！"); }
         newpassword = Encryption.encrypt(newpassword.getBytes());
         artAppUser.setPassword(newpassword);
         artAppUserDao.update(artAppUser);
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

   private List<Map<String, Object>> worksFollow(Map<String, String> parameters) throws ServiceException {
      System.out.println("worksFollow...");
      try {
         int userId = isLogin(parameters.get("account"), parameters.get("password"));
         if (userId <= 1) { throw new ServiceException("请先登陆！"); }
         ArtAppUser artAppUser = artAppUserDao.get(Integer.valueOf(userId));
         if (artAppUser == null) { throw new ServiceException("没有找到用户！"); }
         String type = returnString(parameters.get("type"));
         if ("number".equalsIgnoreCase(type)) {
            String sql = "SELECT " + " COUNT(*) AS number " + " FROM art_works_follow follow " + " WHERE follow.app_user_id=" + userId;
            return jdbcDao.queryBySql(sql);
         }
         if ("works".equalsIgnoreCase(type)) {
            String sql = "SELECT " + " works.id AS worksId,CONCAT(works.works_c_name,' ',works.works_e_name) AS name,works.works_status AS descript,"
                  + " CONCAT('auction/',artist.folder_name,'/thumbnail/',works.thumbnail) AS photo " + " FROM art_works_follow follow "
                  + " LEFT JOIN art_works works ON follow.works_id=works.id " + " LEFT JOIN art_artist artist ON works.artist_id=artist.id "
                  + " WHERE follow.app_user_id=" + userId + " ORDER BY follow.follow_time DESC ";
            return jdbcDao.queryBySql(sql);
         }
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

   private String register(Map<String, String> parameters, File photo) throws ServiceException {
      try {
         String userName = parameters.get("userName");
         if (userName == null) { throw new ServiceException("昵称为空！"); }
         String password = parameters.get("password");
         String sex = parameters.get("sex");
         if (password == null) { throw new ServiceException("密码为空！"); }
         if (sex == null) { throw new ServiceException("性别为空！"); }
         if (artAppUserService.existsArtAppUsers(userName)) { throw new Exception("该用户已经被注册！"); }
         artAppUserService.createArtAppUser(parameters, photo);
         return null;
      }
      catch (Exception e) {
         e.printStackTrace();
         parameters.put("error", "系统错误！");
         if (e.getMessage() != null && "".equals(e.getMessage())) {
            parameters.put("error", e.getMessage());
         }
      }
      return null;
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
   private ArtAppUserDao     artAppUserDao;

   @Resource
   private ArtAppUserService artAppUserService;

}
