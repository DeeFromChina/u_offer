package com.golead.art.works.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.golead.art.artist.dao.ArtArtistCollectorDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.artist.model.ArtArtistCollectAgency;
import com.golead.art.artist.model.ArtArtistCollector;
import com.golead.art.artist.model.ArtArtistWorksSeries;
import com.golead.art.artist.model.ArtCountry;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.ArtArtistWorksSeriesService;
import com.golead.art.artist.service.ArtCountryService;
import com.golead.art.auction.dao.ArtAuctionDao;
import com.golead.art.auction.model.ArtAuction;
import com.golead.art.utils.FileUtils;
import com.golead.art.works.dao.ArtArtistCollectAgencyDao;
import com.golead.art.works.dao.ArtWorksAbmbDao;
import com.golead.art.works.dao.ArtWorksAttachmentDao;
import com.golead.art.works.dao.ArtWorksCaseDao;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.dao.ArtWorksEvaluateDao;
import com.golead.art.works.dao.ArtWorksExhibitDao;
import com.golead.art.works.dao.ArtWorksMediumDao;
import com.golead.art.works.dao.ArtWorksNetworkDao;
import com.golead.art.works.dao.ArtWorksPeriodDao;
import com.golead.art.works.dao.ArtWorksSomeDao;
import com.golead.art.works.dao.ArtWorksWordsDao;
import com.golead.art.works.model.ArtMedium;
import com.golead.art.works.model.ArtWorks;
import com.golead.art.works.model.ArtWorksAbmb;
import com.golead.art.works.model.ArtWorksAttachment;
import com.golead.art.works.model.ArtWorksCase;
import com.golead.art.works.model.ArtWorksEvaluate;
import com.golead.art.works.model.ArtWorksExhibit;
import com.golead.art.works.model.ArtWorksMedium;
import com.golead.art.works.model.ArtWorksNetwork;
import com.golead.art.works.model.ArtWorksPeriod;
import com.golead.art.works.model.ArtWorksSome;
import com.golead.art.works.model.ArtWorksStyle;
import com.golead.art.works.model.ArtWorksTheme;
import com.golead.art.works.model.ArtWorksWords;
import com.golead.art.works.service.ArtMediumService;
import com.golead.art.works.service.ArtWorksService;
import com.golead.art.works.service.ArtWorksStyleService;
import com.golead.art.works.service.ArtWorksThemeService;
import com.golead.common.model.SysCode;
import com.golead.common.service.system.SysCodeService;
import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;
import com.golead.core.web.form.QueryForm;

@Service
public class ArtWorksServiceImpl extends BaseServiceImpl implements ArtWorksService {
   private static final long serialVersionUID = 1L;

   public ArtWorks getArtWorks(Serializable id) throws ServiceException {
      try {
         return artWorksDao.get(id);
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

   public void createArtWorks(ArtWorks artWorks) throws ServiceException {
      try {
         artWorksDao.save(artWorks);
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

   private void fileWorks(ArtWorks tmp, Map<String, String> artWorksMap, List<File> files, String fileFileName, String FILE_PATH) throws ServiceException {
      try {
         ArtArtist artist = artArtistService.getArtArtist(tmp.getArtistId());
         String enName = artist.getFolderName();

         String fileName = artWorksMap.get("fileName") == null ? "" : artWorksMap.get("fileName");
         //没有需要保留的照片
         if ("".equals(fileName)) {
            String thumbnail = tmp.getThumbnail() == null ? "" : tmp.getThumbnail();
            String worksStoreName = tmp.getWorksStoreName() == null ? "" : tmp.getWorksStoreName();
            String[] worksStoreNames = worksStoreName.split("、");
            String path = FILE_PATH + File.separator + "upload/auction/" + File.separator + enName + File.separator + "thumbnail" + File.separator + thumbnail;
            if (!"".equals(thumbnail)) FileUtils.deleteFile(path);
            for (int i = 0; i < worksStoreNames.length; i++) {
               String nameString = worksStoreNames[i].substring(worksStoreNames[i].lastIndexOf(":") + 1);
               String path2 = FILE_PATH + File.separator + "upload/auction/" + File.separator + enName + File.separator + nameString;
               if (!"".equals(nameString)) FileUtils.deleteFile(path2);
            }
            tmp.setThumbnail("");
            tmp.setWorksImage("");
            tmp.setWorksStoreName("");
            if (files != null && files.size() > 0) {
               saveThumbnail(tmp, fileFileName, FILE_PATH, files);
            }
         }
         //有需要保留的照片
         else {
            String[] fileNames = fileName.split(",");
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < fileNames.length; i++) {
               if (fileNames[i] != null && !"".equals(fileNames[i])) {
                  map.put(fileNames[i], "");
               }
            }

            String thumbnail = tmp.getThumbnail() == null ? "" : tmp.getThumbnail();
            if (map.get(thumbnail) == null) {
               String thumbnailpath = FILE_PATH + File.separator + "upload/auction/" + File.separator + enName + File.separator + "thumbnail";
               if (thumbnail != null) {
                  File delFile = new File(thumbnailpath + File.separator + thumbnail);
                  if (delFile.exists()) {
                     delFile.delete();//删除缩略图
                  }
               }
               thumbnail = "";
            }

            //保存新的文件名
            String worksStoreName = tmp.getWorksStoreName() == null ? "" : tmp.getWorksStoreName();
            String[] worksStoreNames = null;
            String saveStoreName = "";
            if (worksStoreName != null) {
               worksStoreNames = worksStoreName.split("、");
               for (int i = 0; i < worksStoreNames.length; i++) {
                  if (map.get(worksStoreNames[i].split(":")[1]) != null) {
                     map.put(worksStoreNames[i].split(":")[0], worksStoreNames[i].split(":")[1]);
                     map.put(worksStoreNames[i].split(":")[1], worksStoreNames[i].split(":")[0]);
                     if (!saveStoreName.equals("")) {
                        saveStoreName = saveStoreName + ",";
                     }
                     saveStoreName = saveStoreName + worksStoreNames[i].split(":")[1];
                  }
               }
            }

            //保存新的文件名
            String worksImage = tmp.getWorksImage() == null ? "" : tmp.getWorksImage();
            String[] worksImages = null;
            if (worksImage != null) {
               worksImages = worksImage.split("、");
               worksImage = "";
               for (int i = 0; i < worksImages.length; i++) {
                  if (map.get(worksImages[i].split(":")[0]) != null) {
                     if (!worksImage.equals("")) {
                        worksImage = worksImage + ",";
                     }
                     //保留文件名
                     worksImage = worksImage + worksImages[i].split(":")[1];
                  }
               }
            }

            //重新定义缩略图
            if (thumbnail.equals("")) {
               String thumbnailpath = FILE_PATH + File.separator + "upload/auction/" + File.separator + enName;
               String imagePath = thumbnailpath + File.separator + saveStoreName.split(",")[0];
               int i = saveStoreName.split(",")[0].lastIndexOf(".") - 1;
               String extension = "";
               if (i > 0) {
                  extension = saveStoreName.split(",")[0].substring(saveStoreName.split(",")[0].lastIndexOf(".") + 1);
               }
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
               Thumbnails.of(imagePath).size(width, height).outputFormat(extension).outputQuality(0.8f)
                     .toFile(thumbnailpath + File.separator + "thumbnail" + File.separator + saveStoreName.split(",")[0]);//保存小图
               tmp.setThumbnail(saveStoreName.split(",")[0]);
            }

            if (files != null && files.size() > 0) {
               fileNames = fileFileName.split(",");
               for (int i = 0; i < files.size(); i++) {
                  String path = FILE_PATH + File.separator + "upload/auction/" + File.separator + enName;
                  String cName = String.valueOf(System.currentTimeMillis());
                  String endless = fileNames[i].substring(fileNames[i].lastIndexOf("."));
                  FileUtils.fileUpload(path + File.separator + cName + endless, files.get(i));
                  if (!saveStoreName.equals("")) {
                     saveStoreName = saveStoreName + ",";
                  }
                  if (!worksImage.equals("")) {
                     worksImage = worksImage + ",";
                  }
                  saveStoreName += cName + endless;
                  worksImage += fileNames[i];
               }
            }

            String[] storeNames = saveStoreName.split(",");
            String[] images = worksImage.split(",");
            String StoreName = "";
            String image = "";
            for (int i = 0; i < storeNames.length; i++) {
               if (!StoreName.equals("")) {
                  StoreName += "、";
                  image += "、";
               }
               StoreName += String.valueOf(i + 1) + ":" + storeNames[i];
               image += String.valueOf(i + 1) + ":" + images[i];
            }

            tmp.setWorksImage(image);
            tmp.setWorksStoreName(StoreName);
            updateArtWorks(tmp);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void changepath(ArtWorks tmp, String FILE_PATH, String ArtistId) {
      try {
         Integer id = tmp.getArtistId();
         ArtArtist artArtist = artArtistService.getArtArtist(id);
         String enName = artArtist.getFolderName();
         String thumbnail = tmp.getThumbnail();
         String worksStoreName = tmp.getWorksStoreName();
         String[] worksStoreNames = worksStoreName.split("、");
         List<String> fileNames = new ArrayList<String>();
         //原来路径的文件名
         for (int i = 0; i < worksStoreNames.length; i++) {
            if (worksStoreNames[i].split(":").length > 1) {
               fileNames.add(worksStoreNames[i].split(":")[1]);
            }
         }

         List<File> files = new ArrayList<File>();
         String path = FILE_PATH + File.separator + "upload/auction/" + enName + File.separator;
         for (String name : fileNames) {
            File file = new File(path + name);
            if (file.exists() && file.isFile()) {
               files.add(file);
            }
         }

         ArtArtist afterArtist = artArtistService.getArtArtist(Integer.valueOf(ArtistId));
         String changeEnName = afterArtist.getFolderName();
         String changepath = FILE_PATH + File.separator + "upload/auction/" + changeEnName + File.separator;
         File changFile = new File(changepath);
         if (!changFile.exists()) {
            changFile.mkdirs();
         }
         for (File file : files) {
            FileUtils.fileUpload(changepath + file.getName(), file);
         }
         String thumbnailpath = path + "thumbnail" + File.separator + thumbnail;
         File thumbnailFile = new File(thumbnailpath);
         File changThumbnail = new File(changepath + "thumbnail");
         if (!changThumbnail.exists()) {
            changThumbnail.mkdirs();
         }
         if (thumbnailFile.exists() && thumbnailFile.isFile()) {
            FileUtils.fileUpload(changepath + "thumbnail" + File.separator + thumbnailFile.getName(), thumbnailFile);
            thumbnailFile.delete();
         }

         for (File file : files) {
            file.delete();
         }

      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("移动文件错误。");
      }
   }

   public void updateArtWorks(Map<String, String> artWorksMap, List<File> files, String fileFileName, String FILE_PATH) throws ServiceException {
      try {
         Integer pkId = new Integer(artWorksMap.get(ArtWorks.PROP_ID));
         ArtWorks tmp = artWorksDao.get(pkId);
         String partSize_name = artWorksMap.get("partSize_name");
         String partSize = "";
         if (partSize_name != null) {
            partSize_name = partSize_name.replace(" ", "");
            String partSize_l = artWorksMap.get("partSize_l");
            partSize_l = partSize_l.replace(" ", "");
            String partSize_w = artWorksMap.get("partSize_w");
            partSize_w = partSize_w.replace(" ", "");
            String partSize_unit = artWorksMap.get("partSize_unit");
            partSize_unit = partSize_unit.replace(" ", "");
            if (partSize_l != null) {
               String[] partSize_ls = partSize_l.split(",");
               String[] partSize_ws = partSize_w.split(",");
               String[] partSize_names = partSize_name.split(",");
               String[] partSize_units = partSize_unit.split(",");
               for (int i = 0; i < partSize_ls.length; i++) {
                  partSize = partSize + partSize_names[i] + ":" + partSize_ls[i] + "*" + partSize_ws[i] + " " + partSize_units[i] + ";";
               }
            }
         }
         artWorksMap.put("partSize", partSize);
         if (!artWorksMap.get("artistId").equals(tmp.getArtistId().toString())) {
            changepath(tmp, FILE_PATH, artWorksMap.get("artistId"));
         }
         ConvertUtil.convertToModel(tmp, artWorksMap);
         artWorksDao.update(tmp);
         fileWorks(tmp, artWorksMap, files, fileFileName, FILE_PATH);
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

   public void updateArtWorks(ArtWorks artWorks) throws ServiceException {
      try {
         artWorksDao.update(artWorks);
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

   public void deleteArtWorks(Serializable id) throws ServiceException {
      try {
         artWorksDao.delete(id);
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

   public void deleteArtWorkss(Serializable[] ids) throws ServiceException {
      try {
         for (Serializable id : ids) {
            boolean pass = false;
            List<ArtAuction> artAuctions = artAuctionDao.findByField(ArtAuction.PROP_WORKS_ID, id);
            String hql = "SELECT * FROM art_artist_collect_agency agency WHERE FIND_IN_SET(" + id + ",agency.collect_works)";
            String sql = "SELECT * FROM art_artist_collector collector WHERE FIND_IN_SET(" + id + ",collector.collect_works)";
            List<Map<String, Object>> artArtistCollectAgencies = jdbcDao.queryBySql(hql);
            List<Map<String, Object>> artArtistCollectors = jdbcDao.queryBySql(sql);
            if (artAuctions != null && artAuctions.size() > 0) {
               pass = true;
            }
            if (artArtistCollectAgencies != null && artArtistCollectAgencies.size() > 0) {
               pass = true;
            }
            if (artArtistCollectors != null && artArtistCollectors.size() > 0) {
               pass = true;
            }
            if (pass) { throw new ServiceException(); }
            artWorksPeriodDao.deleteByField(ArtWorksPeriod.PROP_WORKS_ID, id);
            artWorksCaseDao.deleteByField(ArtWorksCase.PROP_WORKS_ID, id);
            artWorksEvaluateDao.deleteByField(ArtWorksEvaluate.PROP_WORKS_ID, id);
            artWorksSomeDao.deleteByField(ArtWorksSome.PROP_WORKS_ID, id);
            artWorksAbmbDao.deleteByField(ArtWorksAbmb.PROP_WORKS_ID, id);
            artWorksAttachmentDao.deleteByField(ArtWorksAttachment.PROP_WORKS_ID, id);
            artWorksExhibitDao.deleteByField(ArtWorksExhibit.PROP_WORKS_ID, id);
            artWorksMediumDao.deleteByField(ArtWorksMedium.PROP_WORKS_ID, id);
            artWorksNetworkDao.deleteByField(ArtWorksNetwork.PROP_WORKS_ID, id);
            artWorksWordsDao.deleteByField(ArtWorksWords.PROP_WORKS_ID, id);
         }
         for (Serializable id : ids) {
            ArtWorks works = artWorksDao.get(id);
            if (works.getArtistId() != null) {
               ArtArtist artArtist = artArtistService.getArtArtist(works.getArtistId());
               if (artArtist.getFolderName() != null) {
                  String enName = artArtist.getFolderName();
                  String path = ServletActionContext.getServletContext().getRealPath("upload/auction/");
                  String thumbnailPath = path + File.separator + enName + File.separator + works.getThumbnail();
                  String imagePath = path + File.separator + enName + File.separator + "thumbnail" + File.separator + works.getThumbnail();
                  FileUtils.deleteFile(thumbnailPath);
                  FileUtils.deleteFile(imagePath);
               }
            }
         }
         artWorksDao.deleteAll(ids);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw new ServiceException("作品被引用！");
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public PageQuery queryArtWorks(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artWorksList", pageQuery);
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
      String paras = " 1=1 ";
      Map<String, String> map = page.getParameters();

      String artist = map.get("artist");
      if (artist != null && artist.trim().length() > 0) {
         paras += " AND (artist.c_name like '%" + artist.trim() + "%'" + " OR artist.e_name like '%" + artist.trim() + "%')";
      }

      String code = map.get("code");
      if (code != null && code.trim().length() > 0) {
         paras += " AND works.works_no like '%" + code.trim() + "%'";
      }

      String name = map.get("name");
      if (name != null && name.trim().length() > 0) {
         paras += " AND (works.works_c_name like '%" + name.trim() + "%'" + " OR works.works_e_name like '%" + name.trim() + "%')";
      }

      String styleType = map.get("styleType");
      if (styleType != null && styleType.trim().length() > 0) {
         paras += " AND works.style_type like '%" + styleType.trim() + "%'";
      }

      String mediumMaterialId = map.get("mediumMaterialId");
      if (mediumMaterialId != null && mediumMaterialId.trim().length() > 0) {
         paras += " AND works.medium_material =" + mediumMaterialId.trim();
      }

      String mediumShapeId = map.get("mediumShapeId");
      if (mediumShapeId != null && mediumShapeId.trim().length() > 0) {
         paras += " AND works.medium_shape =" + mediumShapeId.trim();
      }

      String artistId = map.get("artistId");
      if (artistId != null && artistId.trim().length() > 0) {
         paras += " AND artist.id =" + artistId.trim();
      }
      
      String seriesId = map.get("seriesId");
      if (seriesId != null && seriesId.trim().length() > 0) {
         paras += " AND works.works_series ='" + seriesId.trim() + "'";
      }

      String orderBy = map.get("orderBy");
      if (orderBy.equals("false")) {
         page.getParameters().put("orderBy", "convert(artist.c_name using gbk),convert(works.works_c_name using gbk),works.create_year,works.create_month,works.create_day");
      }
      else {
         page.getParameters().put("orderBy", "works.works_no");
      }

      String worksSeries = map.get("worksSeries");
      if (worksSeries != null && worksSeries.trim().length() > 0) {
         paras += "AND artist.works_series = " + worksSeries;
      }

      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }

   public String importArtWorks(String path) throws ServiceException {
      String message = "";
      try {
         List<ArtArtist> artArtists = artArtistService.findAllArtArtist();
         Map<String, String> artistMap = new HashMap<String, String>();
         for (ArtArtist artArtist : artArtists) {
            artistMap.put(artArtist.getCname(), artArtist.getId().toString());
            artistMap.put(artArtist.getEname(), artArtist.getId().toString());
         }
         List<ArtMedium> material = artMediumService.findByCategory("1");
         Map<String, String> materialMap = new HashMap<String, String>();
         for (ArtMedium artMedium : material) {
            materialMap.put(artMedium.getMediumName(), artMedium.getId().toString());
         }
         List<ArtMedium> shape = artMediumService.findByCategory("2");
         Map<String, String> shapeMap = new HashMap<String, String>();
         for (ArtMedium artMedium : shape) {
            shapeMap.put(artMedium.getMediumName(), artMedium.getId().toString());
         }
         List<ArtWorksStyle> styles = artWorksStyleService.findAll();
         Map<String, String> styleMap = new HashMap<String, String>();
         for (ArtWorksStyle style : styles) {
            styleMap.put(style.getStyleName(), style.getId().toString());
         }
         List<ArtWorksTheme> themes = artWorksThemeService.findAll();
         Map<String, String> themeMap = new HashMap<String, String>();
         for (ArtWorksTheme theme : themes) {
            themeMap.put(theme.getThemeName(), theme.getId().toString());
         }
         List<ArtArtistWorksSeries> series = artArtistWorksSeriesService.findAll();
         Map<String, String> seriesMap = new HashMap<String, String>();
         for (ArtArtistWorksSeries serie : series) {
            seriesMap.put(serie.getSeriesName(), serie.getId().toString());
         }
         //签名位置
         List<SysCode> sysCodes = sysCodeService.findCodeBySetId(19);
         Map<String, String> map = new HashMap<String, String>();
         for (SysCode sysCode : sysCodes) {
            map.put(sysCode.getItemName(), sysCode.getItemValue());
            map.put(sysCode.getItemValue(), sysCode.getItemName());
         }
         //艺术类型
//         sysCodes = sysCodeService.findCodeBySetId(15);
//         Map<String, String> artType = new HashMap<String, String>();
//         for (SysCode sysCode : sysCodes) {
//            artType.put(sysCode.getItemName(), sysCode.getItemValue());
//         }
         //社会功能
         sysCodes = sysCodeService.findCodeBySetId(6);
         Map<String, String> socialMap = new HashMap<String, String>();
         for (SysCode sysCode : sysCodes) {
            socialMap.put(sysCode.getItemName(), sysCode.getItemValue());
         }
         //图片来源
         sysCodes = sysCodeService.findCodeBySetId(31);
         Map<String, String> photoSoureMap = new HashMap<String, String>();
         for (SysCode sysCode : sysCodes) {
            photoSoureMap.put(sysCode.getItemName(), sysCode.getItemValue());
         }
         //习作
         sysCodes = sysCodeService.findCodeBySetId(32);
         Map<String, String> worksWritingMap = new HashMap<String, String>();
         for (SysCode sysCode : sysCodes) {
            worksWritingMap.put(sysCode.getItemName(), sysCode.getItemValue());
         }
         //创作方式
         sysCodes = sysCodeService.findCodeBySetId(36);
         Map<String, String> creativeWayMap = new HashMap<String, String>();
         for (SysCode sysCode : sysCodes) {
            creativeWayMap.put(sysCode.getItemName(), sysCode.getItemValue());
         }
         InputStream is = new FileInputStream(path);
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
         // 循环工作表Sheet
         HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
         if (hssfSheet != null) {
            for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = hssfSheet.getRow(rowNum);
               if (hssfRow == null) continue;

               ArtWorks artWorks = new ArtWorks();
               if (hssfRow.getCell(0) == null) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               if ("".equals(getValue(hssfRow.getCell(0)))) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               //作品图片名
               String picName = getValue(hssfRow.getCell(0));
               if (picName == null || "".equals(picName)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到作品图片名";
                  break;
               }
               artWorks.setThumbnail(picName);
               artWorks.setWorksStoreName("1:" + picName);
               artWorks.setWorksImage("1:" + picName);
               //艺术家中文名
               String artist = getValue(hssfRow.getCell(1));
               if (artistMap.get(artist.trim()) == null) {
                  message = "第" + String.valueOf(rowNum) + "行找不到艺术家";
                  break;
               }
               Integer artistId = Integer.valueOf(artistMap.get(artist));
               artWorks.setArtistId(artistId);
               //作品编号
               String worksNo = getValue(hssfRow.getCell(2));
               if ("".equals(worksNo)) {
                  message = "第" + String.valueOf(rowNum) + "行作品编号不能为空";
                  break;
               }
               artWorks.setWorksNo(worksNo);
               //创作时间
               String createTime = getValue(hssfRow.getCell(3));
               if (createTime != null && !"".equals(createTime)) {
                  String createYear = createTime.split("年")[0];
                  artWorks.setCreateYear(createYear);
                  if (createTime.split("年").length == 2) {
                     String createMonth = createTime.split("年")[1].split("月")[0];
                     artWorks.setCreateMonth(createMonth);
                     if (createTime.split("年")[1].split("月").length == 2) {
                        String createDay = createTime.split("年")[1].split("月")[1].split("日")[0];
                        artWorks.setCreateDay(createDay);
                     }
                  }
               }
               //               if("".equals(time)){
               //                  message = "第" + String.valueOf(rowNum) + "行创作时间不能为空";
               //                  break;
               //               }
               //创作时期
               //               String createPeriod = getValue(hssfRow.getCell(3));
               //               if("".equals(createPeriod)){
               //                  message = "第" + String.valueOf(rowNum) + "行创作时期不能为空";
               //                  break;
               //               }
               //               artWorks.setCreatePeriod(createPeriod);
               //作品系列
               String worksSeries = getValue(hssfRow.getCell(4));
               if (!"".equals(worksSeries)) {
                  if (seriesMap.get(worksSeries.trim()) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到作品系列";
                     break;
                  }
                  else {
                     artWorks.setWorksSeries(seriesMap.get(worksSeries.trim()));
                  }
               }
               //作品中文名
               String worksCName = getValue(hssfRow.getCell(5));
               if ("".equals(worksCName)) {
                  message = "第" + String.valueOf(rowNum) + "行作品中文名不能为空";
                  break;
               }
               artWorks.setWorksCName(worksCName);
               //作品英文名
               String worksEName = getValue(hssfRow.getCell(6));
               artWorks.setWorksEName(worksEName);
               //国内媒介(材料)
               String materialMediums = returnString(getValue(hssfRow.getCell(7)));
               if(!"".equals(materialMediums)){
                  String[] mediums = materialMediums.split(",");
                  String mediumId = "";
                  for(int i = 0; i < mediums.length; i++){
                     String materialMedium = mediums[i];
                     if(materialMap.get(materialMedium) == null){
                        message = "第" + String.valueOf(rowNum) + "行找不到媒介(材料)";
                        break;
                     }
                     if(!"".equals(mediumId)){
                        mediumId+=",";
                     }
                     mediumId+=materialMap.get(materialMedium);
                  }
                  artWorks.setMediumMaterial(mediumId);
               }
               //国内媒介（形式）
               String shapeMediums = returnString(getValue(hssfRow.getCell(8)));
               if(!"".equals(shapeMediums)){
                  String[] mediums = shapeMediums.split(",");
                  String mediumId = "";
                  for(int i = 0; i < mediums.length; i++){
                     String shapeMedium = mediums[i];
                     if(shapeMap.get(shapeMedium) == null){
                        message = "第" + String.valueOf(rowNum) + "行找不到媒介(形式)";
                        break;
                     }
                     if(!"".equals(mediumId)){
                        mediumId+=",";
                     }
                     mediumId+=shapeMap.get(shapeMedium);
                  }
                  artWorks.setMediumShape(mediumId);
               }
               //尺寸长(厘米)
               String sizeCmLength = getValue(hssfRow.getCell(9));
               if (!"".equals(sizeCmLength)) {
                  artWorks.setSizeCmLength(isDouble(sizeCmLength));
               }
               //尺寸宽(厘米)
               String sizeCmWidth = getValue(hssfRow.getCell(10));
               if (!"".equals(sizeCmWidth)) {
                  artWorks.setSizeCmWidth(isDouble(sizeCmWidth));
               }
               //尺寸高(厘米)
               String sizeCmHeight = getValue(hssfRow.getCell(11));
               if (!"".equals(sizeCmHeight)) {
                  artWorks.setSizeCmHeight(isDouble(sizeCmHeight));
               }
               //尺寸长(英寸)
               String sizeInLength = getValue(hssfRow.getCell(12));
               if (!"".equals(sizeInLength)) {
                  artWorks.setSizeInLength(isDouble(sizeInLength));
               }
               //尺寸宽(英寸)
               String sizeInWidth = getValue(hssfRow.getCell(13));
               if (!"".equals(sizeInWidth)) {
                  artWorks.setSizeInWidth(isDouble(sizeInWidth));
               }
               //尺寸高(英寸)
               String sizeInHeight = getValue(hssfRow.getCell(14));
               if (!"".equals(sizeInHeight)) {
                  artWorks.setSizeInHeight(isDouble(sizeInHeight));
               }
               //尺寸长(尺)
               String sizeRuleLength = getValue(hssfRow.getCell(15));
               if (!"".equals(sizeRuleLength)) {
                  artWorks.setSizeRuleLength(isDouble(sizeRuleLength));
               }
               //尺寸宽(尺)
               String sizeRuleWidth = getValue(hssfRow.getCell(16));
               if (!"".equals(sizeRuleWidth)) {
                  artWorks.setSizeRuleWidth(isDouble(sizeRuleWidth));
               }
               //尺寸高(尺)
               String sizeRuleHeight = getValue(hssfRow.getCell(17));
               if (!"".equals(sizeRuleHeight)) {
                  artWorks.setSizeRuleHeight(isDouble(sizeRuleHeight));
               }
               //签名内容1
               String signatureContent = getValue(hssfRow.getCell(18));
               artWorks.setSignatureContent(signatureContent);
               //签名位置1
               String signature = getValue(hssfRow.getCell(19));
               if (!"".equals(signature)) {
                  if (map.get(signature) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到签名位置1";
                     break;
                  }
                  else {
                     artWorks.setSignature(map.get(signature));
                  }
               }
               if (signature.length() == 2) {
                  artWorks.setSignature(signature);
               }
               //签名内容2
               String signatureContent2 = getValue(hssfRow.getCell(20));
               artWorks.setSignatureContent2(signatureContent2);
               //签名位置2
               String signature2 = getValue(hssfRow.getCell(21));
               if (!"".equals(signature2)) {
                  if (map.get(signature2) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到签名位置2";
                     break;
                  }
                  else {
                     artWorks.setSignature2(map.get(signature2));
                  }
               }
               if (signature2.length() == 2) {
                  artWorks.setSignature2(signature2);
               }
               //签名内容3
               String signatureContent3 = getValue(hssfRow.getCell(22));
               artWorks.setSignatureContent3(signatureContent3);
               //签名位置3
               String signature3 = getValue(hssfRow.getCell(23));
               if (!"".equals(signature3)) {
                  if (map.get(signature3) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到签名位置3";
                     break;
                  }
                  else {
                     artWorks.setSignature3(map.get(signature3));
                  }
               }
               if (signature3.length() == 2) {
                  artWorks.setSignature3(signature3);
               }
               //风格类型
               String worksCategory = getValue(hssfRow.getCell(24));
               if (!"".equals(worksCategory)) {
                  if (styleMap.get(worksCategory) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到风格类型";
                     break;
                  }
                  else {
                     artWorks.setStyleType(styleMap.get(worksCategory));
                  }
               }
               //作品题材
               String worksTheme = getValue(hssfRow.getCell(25));
               if (!"".equals(worksTheme)) {
                  if (themeMap.get(worksTheme) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到作品题材";
                     break;
                  }
                  else {
                     artWorks.setWorksTheme1(themeMap.get(worksTheme));
                  }
               }
               //风格技法内容描述
               String worksStatus = getValue(hssfRow.getCell(26));
               artWorks.setWorksStatus(worksStatus);
               //创作地点
               String createPlace = getValue(hssfRow.getCell(27));
               artWorks.setCreatePlace(createPlace);
               //创作时间段，开始年份
               String createFrom = getValue(hssfRow.getCell(28));
               artWorks.setCreateFrom(createFrom);
               //创作时间段，结束年份
               String createTo = getValue(hssfRow.getCell(29));
               artWorks.setCreateTo(createTo);
               //局部尺寸（多个以;隔开）
               String partSize = getValue(hssfRow.getCell(30));
               artWorks.setPartSize(partSize);
               //社会功能
               String social = returnString(getValue(hssfRow.getCell(31)));
               if(!"".equals(social)){
                  String[] socials = social.split(",");
                  String socialIds = "";
                  boolean isPass = false;
                  for(int i = 0 ; i < socials.length; i++){
                     if(socialMap.get(socials[i]) == null){
                        message = "第" + String.valueOf(rowNum) + "行找不到社会功能";
                        isPass = true;
                        break;
                     }
                     if(socialIds.length() > 0){
                        socialIds += ",";
                     }
                     socialIds += socialMap.get(socials[i]);
                  }
                  if(isPass){
                     break;
                  }
                  social = socialIds;
               }
               artWorks.setSocialFunction(social);
               //图片来源
               String photoSoure = returnString(getValue(hssfRow.getCell(32)));
               if(!"".equals(photoSoure) && photoSoureMap.get(photoSoure) == null){
                  message = "第" + String.valueOf(rowNum) + "行找不到图片来源";
                  break;
               }
               artWorks.setPhotoSoure(photoSoure);
               //习作
               String worksWriting = returnString(getValue(hssfRow.getCell(33)));
               if(!"".equals(worksWriting) && worksWritingMap.get(worksWriting) == null){
                  message = "第" + String.valueOf(rowNum) + "行找不到习作";
                  break;
               }
               artWorks.setWorksWriting(worksWriting);
               //媒介材料说明
               String materialRemark = returnString(getValue(hssfRow.getCell(34)));
               artWorks.setMaterialRemark(materialRemark);
               //媒介形式说明
               String shapeRemark = returnString(getValue(hssfRow.getCell(35)));
               artWorks.setShapeRemark(shapeRemark);
               //创作方式
               String createStyle = returnString(getValue(hssfRow.getCell(36)));
               if(!"".equals(createStyle)){
                  String[] createStyles = createStyle.split(",");
                  String createStyleIds = "";
                  boolean isPass = false;
                  for(int i = 0 ; i < createStyles.length; i++){
                     if(creativeWayMap.get(createStyles[i]) == null){
                        message = "第" + String.valueOf(rowNum) + "行找不到创作方式";
                        isPass = true;
                        break;
                     }
                     if(createStyleIds.length() > 0){
                        createStyleIds += ",";
                     }
                     createStyleIds += creativeWayMap.get(createStyles[i]);
                  }
                  if(isPass){
                     break;
                  }
                  createStyle = createStyleIds;
               }
               artWorks.setCreateStyle(createStyle);
               artWorks.setRepeatMarker("0");
               createArtWorks(artWorks);
            }
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         message = "数据错误";
      }
      File file = new File(path);
      if (file.exists() && file.isFile()) {
         file.delete();
      }
      return message;
   }

   private String getValue(HSSFCell hssfCell) {
      if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
         // 返回布尔类型的值   
         return String.valueOf(hssfCell.getBooleanCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
         // 返回数值类型的值  
         DecimalFormat df = new DecimalFormat("###.##");
         return df.format(hssfCell.getNumericCellValue());
      }
      else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_FORMULA) {
         // 返回数值类型的值  
         return String.valueOf(hssfCell.getNumericCellValue());
      }
      else {
         // 返回字符串类型的值      
         return String.valueOf(hssfCell.getStringCellValue());
      }
   }

   public List<ArtWorks> findAll() throws ServiceException {
      try {
         return artWorksDao.findAll();
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

   public Map<String, String> changeArtWorks(QueryForm form) throws ServiceException {
      try {
         String partSize = "";
         if (form.getRecord().get("partSize_name") != null) {
            String partSize_name = form.getRecord().get("partSize_name");
            partSize_name = partSize_name.replace(" ", "");
            String partSize_unit = form.getRecord().get("partSize_unit");
            partSize_unit = partSize_unit.replace(" ", "");
            String partSize_l = form.getRecord().get("partSize_l");
            partSize_l = partSize_l.replace(" ", "");
            String partSize_w = form.getRecord().get("partSize_w");
            partSize_w = partSize_w.replace(" ", "");
            String[] partSize_ls = partSize_l.split(",");
            String[] partSize_ws = partSize_w.split(",");
            String[] partSize_names = partSize_name.split(",");
            String[] partSize_units = partSize_unit.split(",");
            for (int i = 0; i < partSize_ls.length; i++) {
               partSize = partSize + partSize_names[i] + ":" + partSize_ls[i] + "*" + partSize_ws[i] + " " + partSize_units[i] + ";";
            }
         }
         form.getRecord().put("partSize", partSize);
         return form.getRecord();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public void saveThumbnail(ArtWorks artWorks, String filesFileName, String FILE_PATH, List<File> files) throws ServiceException {
      try {
         String enName = artArtistService.getArtArtist(artWorks.getArtistId()).getFolderName();
         String[] names = filesFileName.split(",");
         String path = FILE_PATH + File.separator + "upload/auction/" + File.separator + enName;
         if (!new File(path).exists()) {
            new File(path).mkdirs();
         }
         StringBuffer fileName = new StringBuffer();
         StringBuffer saveName = new StringBuffer();
         Map<Integer, String> map = new HashMap<Integer, String>();
         if (files != null) {
            String thumbnail = "";
            for (int i = 0; i < files.size(); i++) {
               File file = files.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String[] endless = names[i].split("\\.");
               FileUtils.fileUpload(path + File.separator + cName + "." + endless[endless.length - 1], file);
               fileName.append(String.valueOf(i + 1) + ":" + names[i]);
               saveName.append(String.valueOf(i + 1) + ":" + cName + "." + endless[endless.length - 1]);
               thumbnail = cName + "." + endless[endless.length - 1];
               map.put(i, thumbnail);
               if ((i + 1) != files.size()) fileName.append("、");
               if ((i + 1) != files.size()) saveName.append("、");
            }
            String savePath = path + File.separator + "thumbnail";
            if (!new File(savePath).exists()) {
               new File(savePath).mkdirs();
            }
            String imagePath = path + File.separator + map.get(0);
            String extension = map.get(0).substring(map.get(0).lastIndexOf(".") + 1);
            BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            /* 调整后的图片的宽度和高度 */
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
            Thumbnails.of(imagePath).size(width, height).outputFormat(extension).outputQuality(1.0f).toFile(savePath + File.separator + map.get(0));//保存小图

            artWorks.setThumbnail(map.get(0));
            artWorks.setWorksImage(fileName.toString());
            artWorks.setWorksStoreName(saveName.toString());
            updateArtWorks(artWorks);
         }
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private double isDouble(String arg) {
      try {
         return Double.valueOf(arg);
      }
      catch (Exception e) {
         return 0D;
      }
   }

   public int findByCountry(String ids) throws ServiceException {
      try {
         String str = "SELECT COUNT(*) FROM ART_WORKS works WHERE works.create_country IN (" + ids + ")";
         return jdbcDao.queryIntBySql(str);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public String getMaxWorksNo(int artistId) throws ServiceException {
      try {
         String str = "SELECT max(works_no) as maxNo FROM ART_WORKS where artist_id =" + artistId;
         ArtArtist artist = artArtistService.getArtArtist(artistId);
         String enName = artist.getEname().trim().substring(0, 1).toUpperCase();
         String sqlStr = "select count(*) as cn from art_artist where e_name like '" + enName + "%'";
         List<Map<String, Object>> counts = jdbcDao.queryBySql(sqlStr);
         List<Map<String, Object>> list = jdbcDao.queryBySql(str);
         int number = 1;
         if (list != null && list.size() > 0) {
            Map<String, Object> map = list.get(0);
            String maxNo = map.get("maxNo") == null ? null : map.get("maxNo").toString();
            //找到某艺术家的最大作品编号
            if (maxNo != null && !"".equals(maxNo)) {
               enName = maxNo.substring(0, maxNo.length() - 6);//前缀
               Integer noInt = Integer.valueOf(maxNo.substring(maxNo.length() - 6, maxNo.length())) + 1;//拿后六位数字
               String rsNo = "";
               Integer len = 6 - noInt.toString().length();
               if (len > 0) {
                  for (int i = 6 - len; i < 6; i++) {
                     rsNo += "0";
                  }
               }
               rsNo += noInt;
               return enName + rsNo;
            }
            else {//找不到最大编号(有同前缀没有编号)
               String sql = "SELECT works_no AS maxNo FROM ART_WORKS where works_no LIKE '" + enName + "%'" + "order by length(works_no),works_no DESC";
               list = jdbcDao.queryBySql(sql);//最大编号
               if (list != null && list.size() > 0) {
                  Map<String, Object> map2 = list.get(0);
                  maxNo = map2.get("maxNo") == null ? null : map2.get("maxNo").toString();
                  if (maxNo != null && !"".equals(maxNo)) {
                     number = Integer.valueOf(maxNo.substring(1, maxNo.length() - 6)) + 1;
                  }
               }
            }
         }
         return enName + number + "000001";
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @SuppressWarnings("deprecation")
   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException {
      try {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
         HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
         HSSFRow row = sheet.createRow((int) 0);

         HSSFCell cell = row.createCell((short) 0);
         cell.setCellValue("作品编号");
         cell = row.createCell((short) 1);
         cell.setCellValue("艺术家");
         cell = row.createCell((short) 2);
         cell.setCellValue("作品名称");
         cell = row.createCell((short) 3);
         cell.setCellValue("英文名称");
         cell = row.createCell((short) 4);
         cell.setCellValue("创作时间");
         cell = row.createCell((short) 5);
         cell.setCellValue("创作时间段");
         cell = row.createCell((short) 6);
         cell.setCellValue("尺寸（cm）");
         cell = row.createCell((short) 7);
         cell.setCellValue("尺寸（in）");
         cell = row.createCell((short) 8);
         cell.setCellValue("尺寸（尺）");
         cell = row.createCell((short) 9);
         cell.setCellValue("局部尺寸");
         cell = row.createCell((short) 10);
         cell.setCellValue("媒介材料");
         cell = row.createCell((short) 11);
         cell.setCellValue("媒介形式");
         cell = row.createCell((short) 12);
         cell.setCellValue("作品系列");
         cell = row.createCell((short) 13);
         cell.setCellValue("签名位置1");
         cell = row.createCell((short) 14);
         cell.setCellValue("签名内容1");
         cell = row.createCell((short) 15);
         cell.setCellValue("签名位置2");
         cell = row.createCell((short) 16);
         cell.setCellValue("签名内容2");
         cell = row.createCell((short) 17);
         cell.setCellValue("签名位置3");
         cell = row.createCell((short) 18);
         cell.setCellValue("签名内容3");
         cell = row.createCell((short) 19);
         cell.setCellValue("创作地点");
         cell = row.createCell((short) 20);
         cell.setCellValue("作品题材");
         cell = row.createCell((short) 21);
         cell.setCellValue("风格类型");
         cell = row.createCell((short) 22);
         cell.setCellValue("内容关键词");
         cell = row.createCell((short) 23);
         cell.setCellValue("作品描述");

         List<ArtCountry> artCountries = artCountryService.findAll();
         Map<String, String> countryMap = new HashMap<String, String>();
         for (ArtCountry artCountry : artCountries) {
            countryMap.put(artCountry.getId().toString(), artCountry.getCountryName());
         }
         pageQuery.setPageSize("10000");
         PageQuery pageQuery2 = queryArtWorks(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            String createYear = returnString(item.get("createYear"));
            String createMonth = returnString(item.get("createMonth"));
            String createDay = returnString(item.get("createDay"));
            String createTime = "";
            createTime = addString(createTime, createYear, "年");
            createTime = addString(createTime, createMonth, "月");
            createTime = addString(createTime, createDay, "日");

            String createFrom = returnString(item.get("createFrom"));
            String createTo = returnString(item.get("createTo"));
            String createTimeD = "";
            createTimeD = addString(createTimeD, createFrom, "~");
            createTimeD = addString(createTimeD, createTo, "");

            String sizeCmLength = returnString(item.get("sizeCmLength"));
            String sizeCmWidth = returnString(item.get("sizeCmWidth"));
            String sizeCmHeight = returnString(item.get("sizeCmHeight"));
            String sizeCm = "";
            sizeCm = addString(sizeCm, sizeCmLength, "X");
            sizeCm = addString(sizeCm, sizeCmWidth, "X");
            sizeCm = addString(sizeCm, sizeCmHeight, "");

            String sizeInLength = returnString(item.get("sizeInLength"));
            String sizeInWidth = returnString(item.get("sizeInWidth"));
            String sizeInHeight = returnString(item.get("sizeInHeight"));
            String sizeIn = "";
            sizeIn = addString(sizeIn, sizeInLength, "X");
            sizeIn = addString(sizeIn, sizeInWidth, "X");
            sizeIn = addString(sizeIn, sizeInHeight, "");

            String sizeRuleLength = returnString(item.get("sizeRuleLength"));
            String sizeRuleWidth = returnString(item.get("sizeRuleWidth"));
            String sizeRuleHeight = returnString(item.get("sizeRuleHeight"));
            String sizeRule = "";
            sizeRule = addString(sizeRule, sizeRuleLength, "X");
            sizeRule = addString(sizeRule, sizeRuleWidth, "X");
            sizeRule = addString(sizeRule, sizeRuleHeight, "");

            String partSize = returnString(item.get("partSize"));
            if (partSize.equals(":* ;")) {
               partSize = "";
            }

            String createCountry = returnString(countryMap.get(returnString(item.get("createCountry"))));
            String createPlace = returnString(item.get("createPlace"));
            String place = "";
            place = addString(place, createCountry, " ");
            place = addString(place, createPlace, "");

            String mediumMaterial = returnString(item.get("mediumMaterial"));
            if (!"".equals(mediumMaterial)) {
               mediumMaterial = findCodeName(form, "MATERIALS", returnString(item.get("mediumMaterial")));
            }
            String mediumShape = returnString(item.get("mediumShape"));
            if (!"".equals(mediumShape)) {
               mediumShape = findCodeName(form, "SHAPES", returnString(item.get("mediumShape")));
            }
            String worksSeries = returnString(item.get("worksSeries"));
            if (!"".equals(worksSeries)) {
               worksSeries = findCodeName(form, "WORKSSERIES", returnString(item.get("worksSeries")));
            }
            String signature = returnString(item.get("signature"));
            if (!"".equals(signature)) {
               signature = findCodeName(form, "SIGNATURE", returnString(item.get("signature")));
            }
            String signature2 = returnString(item.get("signature2"));
            if (!"".equals(signature2)) {
               signature2 = findCodeName(form, "SIGNATURE", returnString(item.get("signature2")));
            }
            String signature3 = returnString(item.get("signature3"));
            if (!"".equals(signature3)) {
               signature3 = findCodeName(form, "SIGNATURE", returnString(item.get("signature3")));
            }

            String worksTheme1 = returnString(item.get("worksTheme"));
            String worksTheme2 = returnString(item.get("worksTheme2"));
            String worksTheme3 = returnString(item.get("worksTheme3"));
            String worksTheme = "";
            worksTheme = addString(worksTheme, findCodeName(form, "WORKSTHEME", worksTheme1), " ");
            worksTheme = addString(worksTheme, findCodeName(form, "WORKSTHEME", worksTheme2), " ");
            worksTheme = addString(worksTheme, findCodeName(form, "WORKSTHEME", worksTheme3), " ");
            String styleType = returnString(item.get("styleType"));
            String[] styleTypes = styleType.split(";");
            if (!"".equals(styleType)) {
               styleType = "";
               for (int m = 0; m < styleTypes.length; m++) {
                  styleType += findCodeName(form, "WORKSSTYLE", styleTypes[m]) + " ";
               }
            }

            String keyWords = "";
            keyWords = addEndString(keyWords, returnString(item.get("keywordAddr")), "地点:");
            keyWords = addEndString(keyWords, returnString(item.get("keywordCharacter")), "人物:");
            keyWords = addEndString(keyWords, returnString(item.get("keywordEvent")), "事件:");
            keyWords = addEndString(keyWords, returnString(item.get("keywordThing")), "物件:");
            keyWords = addEndString(keyWords, returnString(item.get("keywordOther1")), "其他1:");
            keyWords = addEndString(keyWords, returnString(item.get("keywordOther2")), "其他2:");

            row = sheet.createRow(i + 1);
            row.createCell((short) 0).setCellValue(returnString(item.get("no")));
            row.createCell((short) 1).setCellValue(returnString(item.get("c_name")));
            row.createCell((short) 2).setCellValue(returnString(item.get("chineseName")));
            row.createCell((short) 3).setCellValue(returnString(item.get("englishName")));
            row.createCell((short) 4).setCellValue(createTime);
            row.createCell((short) 5).setCellValue(createTimeD);
            row.createCell((short) 6).setCellValue(sizeCm);
            row.createCell((short) 7).setCellValue(sizeIn);
            row.createCell((short) 8).setCellValue(sizeRule);
            row.createCell((short) 9).setCellValue(partSize);
            row.createCell((short) 10).setCellValue(mediumMaterial);
            row.createCell((short) 11).setCellValue(mediumShape);
            row.createCell((short) 12).setCellValue(worksSeries);
            row.createCell((short) 13).setCellValue(signature);
            row.createCell((short) 14).setCellValue(returnString(item.get("signatureContent")));
            row.createCell((short) 15).setCellValue(signature2);
            row.createCell((short) 16).setCellValue(returnString(item.get("signatureContent2")));
            row.createCell((short) 17).setCellValue(signature3);
            row.createCell((short) 18).setCellValue(returnString(item.get("signatureContent3")));
            row.createCell((short) 19).setCellValue(place);
            row.createCell((short) 20).setCellValue(worksTheme);
            row.createCell((short) 21).setCellValue(styleType);
            row.createCell((short) 22).setCellValue(keyWords);
            row.createCell((short) 23).setCellValue(returnString(item.get("worksStatus")));
            i++;
         }
         return hssfWorkbook;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public int findByMedium(String id) throws ServiceException{
      try {
         String sql = "SELECT COUNT(*) FROM art_works works WHERE FIND_IN_SET(" + id + ",medium_material) "
               + " OR FIND_IN_SET(" + id + ",medium_shape) ";
         return jdbcDao.queryIntBySql(sql);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public List<Map<String, Object>> findAllWorks() throws ServiceException{
      try {
         StringBuffer sql = new StringBuffer();
         sql.append("SELECT ");
         sql.append(" works.id,works.works_no,works.create_year,");
         sql.append(" works.works_series,works.works_c_name,");
         sql.append(" works.works_e_name,works.medium_material,");
         sql.append(" works.signature,works.style_type,");
         sql.append(" works.thumbnail,works.works_theme1,");
         sql.append(" works.style_content,works.create_place,");
         sql.append(" works.create_to,works.create_from,");
         sql.append(" works.part_size,works.works_status,");
         sql.append(" works.artist_id,works.signature_content,");
         sql.append(" works.medium_shape,works.signature2,");
         sql.append(" works.signature3,works.signature_content2,");
         sql.append(" works.signature_content3,works.create_month,");
         sql.append(" works.create_day,works.create_country,");
         sql.append(" works.works_theme2,works.keyword_character,");
         sql.append(" works.keyword_event,works.keyword_addr,");
         sql.append(" works.keyword_thing,works.keyword_other1,");
         sql.append(" works.keyword_other2,works.size_cm_length,");
         sql.append(" works.size_cm_width,works.size_cm_height,");
         sql.append(" works.size_in_length,works.size_in_width,");
         sql.append(" works.size_in_height,works.size_rule_length,");
         sql.append(" works.size_rule_width,works.size_rule_height,");
         sql.append(" works.social_function,works.photo_soure,");
         sql.append(" works.works_writing,works.auction_overview,");
         sql.append(" works.material_remark,works.shape_remark,");
         sql.append(" works.create_style,works.integrity ");
         sql.append(" FROM art_works works ");
         return jdbcDao.queryBySql(sql.toString());
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public void batchUpdateWorks(String[] sqls) throws ServiceException{
      try {
         jdbcDao.getJdbcTemplate().batchUpdate(sqls);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
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

   private String addEndString(String str, String equalStr, String addStr) {
      if (!"".equals(equalStr)) {
         if (equalStr.indexOf(addStr) > -1) return str + equalStr;
         else return str + addStr + equalStr;
      }
      return str;
   }

   private String findCodeName(Object objForm, String codeNo, Serializable value) {
      BaseForm bf = (BaseForm) objForm;

      List<Code> list = bf.getCodeSets().get(codeNo);
      if (value == null) return "";
      if (list == null) return "";
      for (int i = 0; i < list.size(); i++) {
         Code code = list.get(i);
         String v;
         if (value instanceof String) v = ((String) value).trim();
         else v = value.toString();
         if (v.equals(code.getValue())) return code.getCodeName();
      }
      return "";
   }

   @Resource
   private ArtWorksPeriodDao           artWorksPeriodDao;

   @Resource
   private ArtWorksEvaluateDao         artWorksEvaluateDao;

   @Resource
   private ArtWorksCaseDao             artWorksCaseDao;

   @Resource
   private ArtWorksSomeDao             artWorksSomeDao;

   @Resource
   private ArtWorksAbmbDao             artWorksAbmbDao;

   @Resource
   private ArtWorksAttachmentDao       artWorksAttachmentDao;

   @Resource
   private ArtWorksExhibitDao          artWorksExhibitDao;

   @Resource
   private ArtWorksMediumDao           artWorksMediumDao;

   @Resource
   private ArtWorksNetworkDao          artWorksNetworkDao;

   @Resource
   private ArtWorksWordsDao            artWorksWordsDao;

   @Resource
   private ArtArtistService            artArtistService;

   @Resource
   private ArtMediumService            artMediumService;

   @Resource
   private ArtWorksStyleService        artWorksStyleService;

   @Resource
   private ArtWorksThemeService        artWorksThemeService;

   @Resource
   private ArtArtistWorksSeriesService artArtistWorksSeriesService;

   @Resource
   private SysCodeService              sysCodeService;

   @Resource
   private ArtWorksDao                 artWorksDao;

   @Resource
   private ArtCountryService           artCountryService;

   @Resource
   private ArtAuctionDao               artAuctionDao;

   @Resource
   private ArtArtistCollectAgencyDao   artArtistCollectAgencyDao;

   @Resource
   private ArtArtistCollectorDao       artArtistCollectorDao;
}
