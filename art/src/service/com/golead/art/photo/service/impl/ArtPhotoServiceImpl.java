package com.golead.art.photo.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.stereotype.Service;

import com.golead.art.artist.dao.ArtArtistDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.photo.dao.ArtPhotoDao;
import com.golead.art.photo.model.ArtPhoto;
import com.golead.art.photo.service.ArtPhotoService;
import com.golead.art.publication.dao.ArtPublicationDao;
import com.golead.art.publication.model.ArtPublication;
import com.golead.art.utils.FileUtils;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;

@Service
public class ArtPhotoServiceImpl extends BaseServiceImpl implements ArtPhotoService {
   private static final long serialVersionUID = 1L;

   public ArtPhoto getArtPhoto(Serializable id) throws ServiceException {
      try {
         return artPhotoDao.get(id);
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

   public void createArtPhoto(Map<String, String> artPhotoMap, File file, String fileName, String path) throws ServiceException {
      try {
         List<File> files = new ArrayList<File>();
         files.add(file);
         artPhotoMap.putAll(saveFile(artPhotoMap, files, fileName, path));
         if (artPhotoMap.get("photo") == null) { throw new ServiceException("不能保存图片"); }
         ArtPhoto artPhoto = new ArtPhoto();
         ConvertUtil.mapToObject(artPhoto, artPhotoMap, false);
         artPhotoDao.save(artPhoto);
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

   private String parentPath(String artistId) {
      ArtArtist artist = artArtistDao.get(Integer.valueOf(artistId));
      if (artist == null) { return ""; }
      return artist.getFolderName();
   }

   public Map<String, String> saveFile(Map<String, String> artPhotoMap, List<File> files, String filesFileName, String FILE_PATH) throws ServiceException {
      try {
         String[] names = filesFileName.split(",");
         String parentPath = FILE_PATH + File.separator;
         if (artPhotoMap.get("artistId") != null && !"".equals(artPhotoMap.get("artistId"))) {
            parentPath = parentPath + parentPath(artPhotoMap.get("artistId"));
         }
         if (parentPath.trim().equals(FILE_PATH + File.separator)) { return artPhotoMap; }
         if (!new File(parentPath).exists()) {
            new File(parentPath).mkdirs();
         }
         String path = parentPath;
         StringBuffer fileName = new StringBuffer();
         if (files != null) {
            for (int i = 0; i < files.size(); i++) {
               if (files.get(i) == null) {
                  break;
               }
               File file = files.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String endless = names[i].substring(names[i].lastIndexOf(".") + 1);
               FileUtils.fileUpload(path + File.separator + cName + "." + endless, file);
               fileName.append(cName + "." + endless);
               if ((i + 1) != files.size()) fileName.append(",");
               
               String fileNameStr = path + File.separator + cName + "." + endless;
               String extension = endless;
               BufferedImage bufferedImage = ImageIO.read(new File(fileNameStr));
               int width = bufferedImage.getWidth();
               int height = bufferedImage.getHeight();
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
               File file2 = new File(path + File.separator + "thumbnails");
               file2.mkdirs();
               Thumbnails.of(fileNameStr).size(width, height).outputFormat(extension).outputQuality(0.6f).toFile(path + File.separator + "thumbnails" + File.separator + cName);//保存小图
            }
         }
         artPhotoMap.put("photo", fileName.toString());
         return artPhotoMap;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void changepath(ArtPhoto tmp, String FILE_PATH, String ArtistId) throws ServiceException {
      try {
         Integer id = tmp.getArtistId();
         ArtArtist artArtist = artArtistDao.get(id);
         String artistPhoto = artArtist.getPhoto() == null ? "" : artArtist.getPhoto();
         String enName = artArtist.getFolderName();
         String photo = tmp.getPhoto() == null ? "" : tmp.getPhoto();
         if(artistPhoto.endsWith(photo)){
            throw new Exception();
         }
         //新的路径
         ArtArtist afterArtist = artArtistDao.get(Integer.valueOf(ArtistId));
         String changeEnName = afterArtist.getFolderName();
         String changepath = FILE_PATH + File.separator + changeEnName;
         File changFile = new File(changepath);
         if(!changFile.exists()){
            changFile.mkdirs();
         }
         
         String path = FILE_PATH + File.separator + enName + File.separator + photo;
         File file = new File(path);
         String extension = file.getName().substring(file.getName().lastIndexOf(".")+1);
         if(!file.exists() || !file.isFile()){return;}
         BufferedImage bufferedImage = ImageIO.read(file);
         int width = bufferedImage.getWidth();
         int height = bufferedImage.getHeight();
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
         File file2 = new File(changepath + File.separator + "thumbnails");
         file2.mkdirs();
         Thumbnails.of(path).size(width, height).outputFormat(extension).outputQuality(0.6f).toFile(changepath + File.separator + "thumbnails" + File.separator + file.getName());//保存小图
         if(file.exists() && file.isFile()){
            FileUtils.fileUpload(changepath + File.separator + file.getName(), file);
            file.delete();
         }
         String thumbnailsPath = FILE_PATH + File.separator + enName + File.separator + "thumbnails" + File.separator + photo;
         file = new File(thumbnailsPath);
         if(file.exists() && file.isFile()){
            file.delete();
         }
      }
      catch (ServiceException e) {
         e.printStackTrace();
         throw new ServiceException("移动文件错误。");
      }
      catch(Exception e){
         e.printStackTrace();
         throw new ServiceException("不能更改个人照片!");
      }
   }

   public void updateArtPhoto(Map<String, String> artPhotoMap, File file, String fileName, String path) throws ServiceException {
      try {
         Integer pkId = new Integer(artPhotoMap.get(ArtPhoto.PROP_ID));
         ArtPhoto tmp = artPhotoDao.get(pkId);
         //artist
         if (file != null && tmp.getArtistId().toString().equals(artPhotoMap.get("artistId"))) {
            Integer id = tmp.getArtistId();
            ArtArtist artArtist = artArtistDao.get(id);
            String artistPhoto = artArtist.getPhoto() == null ? "" : artArtist.getPhoto();
            String photo = tmp.getPhoto() == null ? "" : tmp.getPhoto();
            if(artistPhoto.endsWith(photo)){
               throw new ServiceException();
            }
         }
         if(!tmp.getArtistId().toString().equals(artPhotoMap.get("artistId"))){
            changepath(tmp, path, artPhotoMap.get("artistId"));
         }
         if (file != null) {
            deletePhoto(tmp.getPhoto(), artPhotoMap.get("artistId"), artPhotoMap.get("pubId"), path);
            List<File> files = new ArrayList<File>();
            files.add(file);
            saveFile(artPhotoMap, files, fileName, path);
            //save photo
            if (artPhotoMap.get("photo") == null) { throw new ServiceException("不能保存图片"); }
         }
         ConvertUtil.mapToObject(tmp, artPhotoMap, true);
         artPhotoDao.update(tmp);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw new ServiceException("不能更改个人照片!");
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void deletePhoto(String fileName, String artistId, String pubId, String path) throws ServiceException {
      try {
         String filePath = "";
         if (pubId != null && !"".equals(pubId.trim())) {
            List<ArtPublication> artPublications = artPublicationDao.findByField(ArtPublication.PROP_ID, Integer.valueOf(pubId));
            if (artPublications == null || artPublications.size() == 0) { return; }
            filePath = path + File.separator + parentPath(artPublications.get(0).getArtistId().toString());
         }
         else if (artistId != null && !"".equals(artistId.trim())) {
            filePath = path + File.separator + parentPath(artistId);
         }
         if ("".equals(filePath.trim())) { throw new ServiceException("操作图片失败。"); }
         String filePath2 = filePath + File.separator + "thumbnails" + File.separator + fileName;
         filePath = filePath + File.separator + fileName;
         File file = new File(filePath);
         if (file.exists() && file.isFile()) {
            file.delete();
         }
         file = new File(filePath2);
         if (file.exists() && file.isFile()) {
            file.delete();
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void deleteArtPhoto(Serializable id) throws ServiceException {
      try {
         artPhotoDao.delete(id);
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

   public void deleteArtPhotos(Serializable[] ids, String path) throws ServiceException {
      try {
         for (Serializable id : ids) {
            ArtPhoto artPhoto = getArtPhoto(id);
            deletePhoto(artPhoto.getPhoto(), artPhoto.getArtistId() == null ? null : artPhoto.getArtistId().toString(), artPhoto.getPubId() == null ? null
                  : artPhoto.getPubId().toString(), path);
         }
         artPhotoDao.deleteAll(ids);
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

   public PageQuery queryArtPhoto(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artPhotoList", pageQuery);
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
      String tmp = "";
      Map<String, String> map = page.getParameters();
      String str = map.get("photoType");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += " artPhoto.photo_type ='" + str.trim() + "'";
      }
      str = map.get("artArtistId");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += " artPhoto.artist_id =" + str.trim();
      }
      str = map.get("artArtistName");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += " artist.c_name LIKE '%" + str.trim() + "%'";
      }
      str = map.get("pubId");
      if (str != null && !"".equals(str.trim())) {
         if (tmp.length() > 0) tmp += " and ";
         tmp += " artPhoto.pub_id =" + str.trim();
      }

      if (tmp.length() > 0) map.put("paras", tmp);
   }

   @Resource
   private ArtPhotoDao       artPhotoDao;

   @Resource
   private ArtPublicationDao artPublicationDao;

   @Resource
   private ArtArtistDao      artArtistDao;

   public void setArtPhotoDao(ArtPhotoDao artPhotoDao) {
      this.artPhotoDao = artPhotoDao;
   }
}
