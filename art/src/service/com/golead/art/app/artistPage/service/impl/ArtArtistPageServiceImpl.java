package com.golead.art.app.artistPage.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.golead.art.app.artistPage.dao.ArtArtistPageDao;
import com.golead.art.app.artistPage.model.ArtArtistPage;
import com.golead.art.app.artistPage.service.ArtArtistPageService;
import com.golead.art.utils.FileUtils;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtArtistPageServiceImpl extends BaseServiceImpl implements ArtArtistPageService {
   private static final long serialVersionUID = 1L;

   public ArtArtistPage getArtArtistPage(Serializable id) throws ServiceException {
      try {
         return artArtistPageDao.get(id);
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

   public void createArtArtistPage(ArtArtistPage artArtistPage) throws ServiceException {
      try {
         artArtistPageDao.save(artArtistPage);
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

   public void updateArtArtistPage(Map<String, String> artArtistPageMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistPageMap.get(ArtArtistPage.PROP_ID));
         ArtArtistPage tmp = artArtistPageDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistPageMap, true);
         artArtistPageDao.update(tmp);
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

   public void deleteArtArtistPage(Serializable id) throws ServiceException {
      try {
         artArtistPageDao.delete(id);
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

   public void deleteArtArtistPages(Serializable[] ids) throws ServiceException {
      try {
         artArtistPageDao.deleteAll(ids);
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

   public PageQuery queryArtArtistPage(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artArtistPageList", pageQuery);
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

   }

   @Override
   public List<Map<String, Object>> queryArtArtistPage(Integer tempId) throws ServiceException {
      try {
         return jdbcDao.queryBySqlId("artArtistPageList", tempId);
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

   private void setFile(ArtArtistPage artArtistPage, File file, String fileFileName, String path) {
      if (file != null) {
         String photo = artArtistPage.getPhoto();
         File file2 = new File(path);
         if (!file2.exists()) {
            file2.mkdirs();
         }
         if (photo != null) {
            File tFile = new File(path + File.pathSeparator + photo);
            tFile.delete();
         }
         StringBuffer buffer = new StringBuffer();
         String cName = String.valueOf(System.currentTimeMillis());
         String extension = fileFileName.substring(fileFileName.lastIndexOf(".") + 1);
         buffer.append(fileFileName + "/" + cName + "." + extension);
         FileUtils.fileUpload(path + File.separator + cName + "." + extension, file);
         artArtistPage.setPhoto(buffer.toString());
      }
   }

   @Override
   public void createArtArtistPage(ArtArtistPage artArtistPage, File file, String fileFileName, String dir) throws ServiceException {
      try {
         setFile(artArtistPage, file, fileFileName, dir);
         this.createArtArtistPage(artArtistPage);
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
   public void updateArtArtistPage(Map<String, String> artArtistPageMap, File file, String fileName, String dir) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistPageMap.get("id"));
         ArtArtistPage tmp = artArtistPageDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistPageMap, true);
         setFile(tmp, file, fileName, dir);
         artArtistPageDao.update(tmp);
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
   public void deleteArtArtistPages(Serializable[] ids, String dir) throws ServiceException {
      try {
         for (int i = 0; i < ids.length; i++) {
            ArtArtistPage tmp = artArtistPageDao.get(ids[i]);
            File file = new File((dir + File.separator + tmp.getPhoto().split("/")[1]));
            if (file.exists()) {
               file.delete();
            }
            artArtistPageDao.delete(ids[i]);
         }

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

   @Resource
   private ArtArtistPageDao artArtistPageDao;

   public void setArtArtistPageDao(ArtArtistPageDao artArtistPageDao) {
      this.artArtistPageDao = artArtistPageDao;
   }
}
