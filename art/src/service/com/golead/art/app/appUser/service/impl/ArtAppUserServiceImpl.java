package com.golead.art.app.appUser.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.util.Encryption;
import com.golead.art.app.appUser.dao.ArtAppUserDao;
import com.golead.art.app.appUser.model.ArtAppUser;
import com.golead.art.app.appUser.service.ArtAppUserService;
import com.golead.art.utils.FileUtils;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

@Service
public class ArtAppUserServiceImpl extends BaseServiceImpl implements ArtAppUserService {
   private static final long serialVersionUID = 1L;

   public ArtAppUser getArtAppUser(Serializable id) throws ServiceException {
      try {
         return artAppUserDao.get(id);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void createArtAppUser(ArtAppUser artAppUser) throws ServiceException {
      try {
         artAppUserDao.save(artAppUser);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void updateArtAppUser(Map<String, String> artAppUserMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artAppUserMap.get(ArtAppUser.PROP_ID));
         ArtAppUser tmp = artAppUserDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artAppUserMap, true);
         artAppUserDao.update(tmp);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @Override
   public void updateArtAppUser(ArtAppUser artAppUser) throws ServiceException {
      try {
         artAppUserDao.update(artAppUser);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void deleteArtAppUser(Serializable id) throws ServiceException {
      try {
         artAppUserDao.delete(id);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void deleteArtAppUsers(Serializable[] ids) throws ServiceException {
      try {
         artAppUserDao.deleteAll(ids);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public PageQuery queryArtAppUser(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artAppUserList", pageQuery);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void createSqlFilter(PageQuery page) {
      StringBuffer tmp = new StringBuffer();
      Map<String, String> map = page.getParameters();

      String userName = map.get(ArtAppUser.PROP_USER_NAME);

      if (userName != null && !"".equals(userName)) {
         if (tmp.length() > 0) tmp .append(" and ");
         tmp.append(" aau.user_name LIKE '%").append(userName).append("%'");
      }

      String accountName = map.get(ArtAppUser.PROP_ACCOUNT_NAME);

      if (accountName != null && !"".equals(accountName)) {
         if (tmp.length() > 0) tmp .append(" AND ");
         tmp.append(" aau.account_name LIKE '%").append(accountName).append("%'");
      }
      
      String userStatus = map.get(ArtAppUser.PROP_USER_STATUS);
      
      if(userStatus != null && !"".equals(userStatus)){
         if(tmp.length() > 0)tmp.append(" AND ");
         tmp.append(" aau.user_status = ").append(userStatus);
      }
      
      String noComment = map.get(ArtAppUser.PROP_NO_COMMENT);
      
      if(noComment != null && !"".equals(noComment)){
         if(tmp.length() > 0)tmp.append(" AND ");
         tmp.append(" aau.no_comment = ").append(noComment);
      }
      
      if (tmp.length() > 0) map.put("paras", tmp.toString());
   }

   public boolean existsArtAppUsers(String account) throws ServiceException {
      try {
         List<ArtAppUser> artAppUsers = artAppUserDao.findByField(ArtAppUser.PROP_ACCOUNT_NAME, account);
         if (artAppUsers != null && artAppUsers.size() > 0) { return true; }
         if (!isInteger(account)) { return false; }
         artAppUsers = artAppUserDao.findByField(ArtAppUser.PROP_PHONE_NUMBER, account);
         if (artAppUsers != null && artAppUsers.size() > 0) { return true; }
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
      return false;
   }

   public void createArtAppUser(Map<String, String> artAppUserMap, File doc) throws ServiceException {
      try {
         ArtAppUser appUser = new ArtAppUser();
         ConvertUtil.mapToObject(appUser, artAppUserMap, false);
         String password = Encryption.encrypt(appUser.getPassword().getBytes());
         appUser.setPassword(password);
         appUser.setUserStatus("1");
         appUser.setRegTime(new Date());
         artAppUserDao.save(appUser);

         String fileName = artAppUserMap.get("fileName");
         String endless = fileName.substring(fileName.lastIndexOf("."));
         String cName = String.valueOf(System.currentTimeMillis());
         cName = cName + endless;
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("/upload/app/");
         String path = FILE_PATH + appUser.getId().toString();
         File file = new File(path);
         if (!file.exists()) {
            file.mkdirs();
         }
         String fullPaht = path + File.separator + cName;
         // 上传文件
         FileUtils.fileUpload(fullPaht, doc);
         appUser.setPhoto(appUser.getId().toString() + "/" + cName);
         artAppUserDao.update(appUser);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw se;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
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
   private ArtAppUserDao artAppUserDao;

   public void setArtAppUserDao(ArtAppUserDao artAppUserDao) {
      this.artAppUserDao = artAppUserDao;
   }
}
