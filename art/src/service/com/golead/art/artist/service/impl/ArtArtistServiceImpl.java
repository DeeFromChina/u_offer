package com.golead.art.artist.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;

import com.golead.art.activity.dao.ArtActivityExhibitArtistDao;
import com.golead.art.activity.model.ArtActivityExhibitArtist;
import com.golead.art.artist.dao.*;
import com.golead.art.artist.model.*;
import com.golead.art.artist.service.ArtArtistService;
import com.golead.art.artist.service.ArtCountryService;
import com.golead.art.auction.dao.ArtAuctionDao;
import com.golead.art.auction.model.ArtAuction;
import com.golead.art.literature.dao.ArtLiteratureMediumDao;
import com.golead.art.literature.dao.ArtLiteratureNetworkDao;
import com.golead.art.literature.dao.ArtLiteratureWordsDao;
import com.golead.art.literature.model.ArtLiteratureMedium;
import com.golead.art.literature.model.ArtLiteratureNetwork;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.photo.dao.ArtPhotoDao;
import com.golead.art.photo.model.ArtPhoto;
import com.golead.art.publication.dao.ArtPublicationDao;
import com.golead.art.publication.model.ArtPublication;
import com.golead.art.utils.FileUtils;
import com.golead.art.works.dao.ArtArtistCollectAgencyDao;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtWorks;
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

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ArtArtistServiceImpl extends BaseServiceImpl implements ArtArtistService {
   private static final long serialVersionUID = 1L;

   public ArtArtist getArtArtist(Serializable id) throws ServiceException {
      try {
         return artArtistDao.get(id);
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

   public void createArtArtist(ArtArtist artArtist, File files, String filesName) throws ServiceException {
      try {
         if (artArtist.getWorksPhoto() != null && !"".equals(artArtist.getWorksPhoto())) {
            ArtWorks artWorks = artWorksDao.get(Integer.valueOf(artArtist.getWorksPhoto()));
            if (artWorks != null) {
               artArtist.setWorksPhoto(artWorks.getThumbnail());
            }
            else {
               artArtist.setWorksPhoto("");
            }
         }
         StringBuffer fileName = new StringBuffer();
         if (files != null) {
            String cName = String.valueOf(System.currentTimeMillis());
            String extension = filesName.substring(filesName.lastIndexOf(".") + 1);
            fileName.append(filesName + "/" + cName + "." + extension);
         }
         artArtist.setPhoto(fileName.toString());
         artArtistDao.save(artArtist);
         //保存到照片中
         if (!"".equals(fileName.toString())) {
            ArtPhoto photo = new ArtPhoto();
            photo.setArtistId(artArtist.getId());
            photo.setPhotoName(artArtist.getCname() + "个人照");
            photo.setPhoto(fileName.toString().split("/")[1]);
            photo.setPhotoType("1");
            artPhotoDao.save(photo);
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

   public void updateArtArtist(Map<String, String> artArtistMap, File files, String filesName, String path) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistMap.get(ArtArtist.PROP_ID));
         ArtArtist tmp = artArtistDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistMap, true);
         if (tmp.getWorksPhoto() != null && !"".equals(tmp.getWorksPhoto())) {
            if (tmp.getWorksPhoto().split(".").length == 1) {
               ArtWorks artWorks = artWorksDao.get(Integer.valueOf(tmp.getWorksPhoto()));
               if (artWorks != null) {
                  tmp.setWorksPhoto(artWorks.getThumbnail());
               }
               else {
                  tmp.setWorksPhoto("");
               }
            }
         }
         File[] file = new File(path + File.separator + tmp.getFolderName()).listFiles();
         StringBuffer fileName = new StringBuffer();
         if (files != null) {
            String oldPhoto = tmp.getPhoto();
            if (oldPhoto != null && !"".equals(oldPhoto)) {
               for (File oldFile : file) {
                  if (oldFile.getName().equals(oldPhoto.split("/")[1])) {
                     oldFile.delete();
                  }
               }
               File tFile = new File(path + File.separator + tmp.getFolderName() + File.separator + "thumbnails" + File.separator + oldPhoto.split("/")[1]);
               if (tFile.exists() && tFile.isFile()) {
                  tFile.delete();
               }
            }
            String imagePath = path + File.separator + tmp.getFolderName();
            File newfile = new File(imagePath);
            if (!newfile.exists()) {
               newfile.mkdirs();
            }
            String extension = filesName.substring(filesName.lastIndexOf(".") + 1);
            String cName = String.valueOf(System.currentTimeMillis()) + "." + extension;
            String fileNameStr = imagePath + File.separator + cName;
            FileUtils.fileUpload(fileNameStr, files);
            fileName.append(filesName + "/" + cName);
            String thumbnailsPath = imagePath + File.separator + "thumbnails";
            newfile = new File(thumbnailsPath);
            if (!newfile.exists()) {
               newfile.mkdirs();
            }
            BufferedImage bufferedImage = ImageIO.read(new File(fileNameStr));
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
            Thumbnails.of(fileNameStr).size(width, height).outputFormat(extension).outputQuality(0.8f).toFile(thumbnailsPath + File.separator + cName);//保存小图
            tmp.setPhoto(fileName.toString());

            //保存到照片中
            if (!"".equals(fileName.toString())) {
               List<ArtPhoto> photos = artPhotoDao.findByField(ArtPhoto.PROP_ARTIST_ID, pkId);
               if (photos != null && photos.size() > 0) {
                  ArtPhoto photo = photos.get(0);
                  photo.setPhoto(cName);
                  artPhotoDao.update(photo);
               }
               else {
                  ArtPhoto photo = new ArtPhoto();
                  photo.setArtistId(pkId);
                  photo.setPhotoName(tmp.getCname() + "个人照");
                  photo.setPhoto(cName);
                  photo.setPhotoType("1");
                  artPhotoDao.save(photo);
               }
            }
         }
         artArtistDao.update(tmp);
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

   public void deleteArtArtist(Serializable id) throws ServiceException {
      try {
         artArtistDao.delete(id);
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

   public List<Map<String, Object>> queryArtArtistInformation(Map<String, String> parameters) throws ServiceException {
      try {
         StringBuffer sb = new StringBuffer();
         if (!("").equals(parameters.get("id"))) {
            sb.append(" aa.id = " + parameters.get("id") + " ");
         }
         parameters.put("paras", sb.toString());
         return jdbcDao.queryBySqlId("artArtistList", parameters);
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

   public void deleteArtArtists(Serializable[] ids) throws ServiceException {
      try {
         artArtistDao.deleteAll(ids);
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

   public String deleteAllWithArtArtists(Serializable[] ids) throws ServiceException {
      String message = "";
      try {
         String FILE_PATH = ServletActionContext.getServletContext().getRealPath("");
         String path = FILE_PATH + File.separator + "upload" + File.separator + "photo" + File.separator;
         boolean pass = true;
         for (Serializable id : ids) {
            List<ArtWorks> artWorks = artWorksDao.findByField(ArtWorks.PROP_ARTIST_ID, id);
            List<ArtPhoto> artPhotos = artPhotoDao.findByField(ArtPhoto.PROP_ARTIST_ID, id);
            List<ArtActivityExhibitArtist> artActivityExhibitArtists = artActivityExhibitArtistDao.findByField(ArtActivityExhibitArtist.PROP_ARTIST_ID, id);
            List<ArtLiteratureMedium> artLiteratureMedium = artLiteratureMediumDao.findByField(ArtLiteratureMedium.PROP_ARTIST_ID, id);
            List<ArtLiteratureWords> artLiteratureWords = artLiteratureWordsDao.findByField(ArtLiteratureWords.PROP_ARTIST_ID, id);
            List<ArtLiteratureNetwork> artLiteratureNetworks = artLiteratureNetworkDao.findByField(ArtLiteratureNetwork.PROP_ARTIST_ID, id);
            List<ArtPublication> artPublications = artPublicationDao.findByField(ArtPublication.PROP_ARTIST_ID, id);
            List<ArtAuction> artAuctions = artAuctionDao.findByField(ArtAuction.PROP_ART_ID, id);
            if (artWorks != null && artWorks.size() > 0) {
               pass = false;
            }
            if (artPhotos != null && artPhotos.size() > 0) {
               pass = false;
            }
            if (artActivityExhibitArtists != null && artActivityExhibitArtists.size() > 0) {
               pass = false;
            }
            if (artLiteratureMedium != null && artLiteratureMedium.size() > 0) {
               pass = false;
            }
            if (artLiteratureWords != null && artLiteratureWords.size() > 0) {
               pass = false;
            }
            if (artLiteratureNetworks != null && artLiteratureNetworks.size() > 0) {
               pass = false;
            }
            if (artPublications != null && artPublications.size() > 0) {
               pass = false;
            }
            if (artAuctions != null && artAuctions.size() > 0) {
               pass = false;
            }
            if (pass) {
               artArtistExperienceDao.deleteByField(ArtArtistExperience.PROP_ART_ID, id);
               artArtistExperienceDao.deleteByField(ArtArtistExperience.PROP_ART_ID, id);
               artArtistEduDao.deleteByField(ArtArtistEdu.PROP_ARTIST_ID, id);
               artArtistHonorsDao.deleteByField(ArtArtistHonors.PROP_ARTIST_ID, id);
               artArtistCollectorDao.deleteByField(ArtArtistCollector.PROP_ARTIST_ID, id);
               artArtistCollectAgencyDao.deleteByField(ArtArtistCollectAgency.PROP_ARTIST_ID, id);
               artArtistCoopDao.deleteByField(ArtArtistCoop.PROP_ARTIST_ID, id);
               artArtistDonationDao.deleteByField(ArtArtistDonation.PROP_ARTIST_ID, id);
               artArtistWorksSeriesDao.deleteByField(ArtArtistWorksSeries.PROP_ARTIST_ID, id);
               artActivityExhibitArtistDao.deleteByField(ArtActivityExhibitArtist.PROP_ARTIST_ID, id);
            }
            else {
               return "艺术家正在被引用！";
            }
            ArtArtist artArtist = getArtArtist(id);
            if (artArtist.getPhoto() != null && artArtist.getPhoto().split("/").length == 2) {
               String path2 = path + "thumbnails" + File.separator + artArtist.getPhoto().split("/")[1];
               path = path + File.separator + artArtist.getPhoto().split("/")[1];
               File file = new File(path);
               if (file.exists() && file.isFile()) {
                  file.delete();
               }
               file = new File(path2);
               if (file.exists() && file.isFile()) {
                  file.delete();
               }
            }
         }
         artArtistDao.deleteAll(ids);
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
      return message;
   }

   public Integer[] delete(List<Integer> ids) {
      Integer[] del_ids = new Integer[ids.size()];
      int i = 0;
      for (Integer integer : ids) {
         del_ids[i] = integer;
         i++;
      }
      return del_ids;
   }

   public PageQuery queryArtArtist(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artArtistList", pageQuery);
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
      String paras = "";
      Map<String, String> map = page.getParameters();
      String cName = map.get("cName");
      if (cName != null && cName.trim().length() > 0) {
         if (paras.length() > 0) paras += " and ";
         paras += " c_Name like '%" + cName.trim() + "%' OR e_Name like '%" + cName.trim() + "%'";
      }
      String ids = map.get("ids");
      if (ids != null && ids.trim().length() > 0) {
         if (paras.length() > 0) paras += " and ";
         paras += " id  NOT IN(" + ids.trim() + ")";
      }
      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }

   public List<ArtArtist> findAllArtArtist() throws ServiceException {
      try {
         return artArtistDao.findAll();
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

   public String importArtArtist(String path) throws ServiceException {
      String message = "";
      try {
         FileInputStream fileInputStream = new FileInputStream(path);
         HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
         HSSFSheet sht = wb.getSheetAt(0);
         List<ArtCountry> artCountrys = artCountryService.findAll();
         Map<String, Integer> countryMap = new HashMap<String, Integer>();
         for (ArtCountry artCountry : artCountrys) {
            countryMap.put(artCountry.getCountryName(), artCountry.getId());
         }
         if (sht != null) {
            for (int rowNum = 2; rowNum < sht.getLastRowNum(); rowNum++) {
               HSSFRow hssfRow = sht.getRow(rowNum);
               if (hssfRow == null) continue;
               if (hssfRow.getCell(0) == null) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               if (("").equals(getValue(hssfRow.getCell(0)))) {
                  message = "成功操作到第" + String.valueOf(rowNum - 1) + "行";
                  break;
               }
               ArtArtist artArtist = new ArtArtist();
               String str = "";
               SysCode code = null;
               str = getValue(hssfRow.getCell(0));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到中文名";
                  break;
               }
               artArtist.setCname(str);
               str = getValue(hssfRow.getCell(1));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到英文名";
                  break;
               }
               artArtist.setEname(str);
               str = getValue(hssfRow.getCell(2));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到性别";
                  break;
               }
               code = sysCodeService.findCodeByName(str);
               artArtist.setSex(code.getItemValue());
               str = getValue(hssfRow.getCell(3));
               Integer birthYear = isInteger(str) ? null : Integer.valueOf(str);
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到出生年";
               //                  break;
               //               }
               artArtist.setBirthYear(birthYear);
               str = getValue(hssfRow.getCell(4));
               Integer birthMonth = isInteger(str) ? null : Integer.valueOf(str);
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到出生月";
               //                  break;
               //               }
               artArtist.setBirthMonth(birthMonth);
               str = getValue(hssfRow.getCell(5));
               Integer birthDay = isInteger(str) ? null : Integer.valueOf(str);
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到出生日";
               //                  break;
               //               }
               artArtist.setBirthDay(birthDay);
               str = getValue(hssfRow.getCell(6));
               Integer deathYear = isInteger(str) ? null : Integer.valueOf(str);
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到卒年";
               //                  break;
               //               }
               artArtist.setDeathYear(deathYear);
               str = getValue(hssfRow.getCell(7));
               Integer deathMonth = isInteger(str) ? null : Integer.valueOf(str);
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到卒月";
               //                  break;
               //               }
               artArtist.setDeathMonth(deathMonth);
               str = getValue(hssfRow.getCell(8));
               Integer deathDay = isInteger(str) ? null : Integer.valueOf(str);
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到卒日";
               //                  break;
               //               }
               artArtist.setDeathDay(deathDay);
               str = getValue(hssfRow.getCell(9));
               if (str != null && !"".equals(str)) {
                  if (countryMap.get(str) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到出生国家";
                     break;
                  }
               }
               artArtist.setBirthCountry(countryMap.get(str));
               str = getValue(hssfRow.getCell(10));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到出生地点";
               //                  break;
               //               }
               artArtist.setBirthplace(str);
               str = getValue(hssfRow.getCell(11));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到祖籍";
               //                  break;
               //               }
               artArtist.setAncestralHome(str);
               str = getValue(hssfRow.getCell(12));
               if (str != null && !"".equals(str)) {
                  if (countryMap.get(str) == null) {
                     message = "第" + String.valueOf(rowNum) + "行找不到国籍";
                     break;
                  }
               }
               str = getValue(hssfRow.getCell(13));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到星座";
               //                  break;
               //               }
               code = sysCodeService.findCodeByName(str);
               artArtist.setZodiac(code.getItemValue());
               str = getValue(hssfRow.getCell(14));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到现居与工作";
               //                  break;
               //               }
               artArtist.setNhom(str);
               str = getValue(hssfRow.getCell(15));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到艺术家时期类型";
               //                  break;
               //               }
               code = sysCodeService.findCodeByName(str);
               artArtist.setArtistType(code.getItemValue());
               str = getValue(hssfRow.getCell(16));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到主要创作媒价";
               //                  break;
               //               }
               code = sysCodeService.findCodeByName(str);
               artArtist.setMainMedia(code.getItemValue());
               str = getValue(hssfRow.getCell(17));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到个人主页";
               //                  break;
               //               }
               artArtist.setPersonalPage(str);
               str = getValue(hssfRow.getCell(18));
               //               if ("".equals(str)) {
               //                  message = "第" + String.valueOf(rowNum) + "行找不到简历";
               //                  break;
               //               }
               artArtist.setCresume(str);
               artArtistDao.save(artArtist);
            }
         }
         fileInputStream.close();
      }
      catch (Exception e) {
         e.printStackTrace();
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
         DecimalFormat df = new DecimalFormat("0");
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

   public Map<String, String> changeArtWorks(QueryForm form) throws ServiceException {
      try {
         if (!isInteger(form.getRecord().get("birthYear"))) form.getRecord().remove("birthYear");
         if (!isInteger(form.getRecord().get("birthMonth"))) form.getRecord().remove("birthMonth");
         if (!isInteger(form.getRecord().get("birthDay"))) form.getRecord().remove("birthDay");
         if (!isInteger(form.getRecord().get("deathYear"))) form.getRecord().remove("deathYear");
         if (!isInteger(form.getRecord().get("deathMonth"))) form.getRecord().remove("deathMonth");
         if (!isInteger(form.getRecord().get("deathDay"))) form.getRecord().remove("deathDay");
         if (form.getRecord().get("personalPage").equals("有多个可以以、分隔开")) form.getRecord().remove("personalPage");
         return form.getRecord();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private boolean isInteger(String arg) {
      try {
         Integer.valueOf(arg);
         return true;
      }
      catch (Exception e) {
         return false;
      }
   }

   public int findByCountry(String ids) throws ServiceException {
      try {
         String str = "SELECT COUNT(*) FROM art_artist artist WHERE artist.birth_country IN (" + ids + ")" + " OR artist.nationality IN (" + ids + ")";
         return jdbcDao.queryIntBySql(str);
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
         cell.setCellValue("中文名");
         cell = row.createCell((short) 1);
         cell.setCellValue("英文名");
         cell = row.createCell((short) 2);
         cell.setCellValue("性别");
         cell = row.createCell((short) 3);
         cell.setCellValue("生日期");
         cell = row.createCell((short) 4);
         cell.setCellValue("卒日期");
         cell = row.createCell((short) 5);
         cell.setCellValue("出生国家");
         cell = row.createCell((short) 6);
         cell.setCellValue("出生地点");
         cell = row.createCell((short) 7);
         cell.setCellValue("祖籍");
         cell = row.createCell((short) 8);
         cell.setCellValue("国籍");
         cell = row.createCell((short) 9);
         cell.setCellValue("星座");
         cell = row.createCell((short) 10);
         cell.setCellValue("现居住工作");
         cell = row.createCell((short) 11);
         cell.setCellValue("艺术家时期类型");
         cell = row.createCell((short) 12);
         cell.setCellValue("主要创作媒价");
         cell = row.createCell((short) 13);
         cell.setCellValue("个人主页");
         cell = row.createCell((short) 14);
         cell.setCellValue("简介");

         List<ArtCountry> artCountries = artCountryService.findAll();
         Map<String, String> countryMap = new HashMap<String, String>();
         for (ArtCountry artCountry : artCountries) {
            countryMap.put(artCountry.getId().toString(), artCountry.getCountryName());
         }
         pageQuery.setPageSize("10000");
         PageQuery pageQuery2 = queryArtArtist(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            String birthYear = returnString(item.get("birthYear"));
            String birthMonth = returnString(item.get("birthMonth"));
            String birthDay = returnString(item.get("birthDay"));
            String birthTime = "";
            birthTime = addString(birthTime, birthYear, "年");
            birthTime = addString(birthTime, birthMonth, "月");
            birthTime = addString(birthTime, birthDay, "日");
            String deathYear = returnString(item.get("deathYear"));
            String deathMonth = returnString(item.get("deathMonth"));
            String deathDay = returnString(item.get("deathDay"));
            String deathTime = "";
            deathTime = addString(deathTime, deathYear, "年");
            deathTime = addString(deathTime, deathMonth, "月");
            deathTime = addString(deathTime, deathDay, "日");
            String zodiac = returnString(item.get("zodiac"));
            if (!"".equals(zodiac)) {
               zodiac = findCodeName(form, "ZODIAC", returnString(item.get("zodiac")));
            }
            String artistType = returnString(item.get("artistType"));
            if (!"".equals(artistType)) {
               artistType = findCodeName(form, "ARTIST_TYPE", returnString(item.get("artistType")));
            }
            String sex = returnString(item.get("sex"));
            if (!"".equals(sex)) {
               sex = findCodeName(form, "GENDER", returnString(item.get("sex")));
            }
            String mainMedia = returnString(item.get("mainMedia"));
            String[] mainMedias = mainMedia.split(",");
            if (!"".equals(mainMedia)) {
               mainMedia = "";
               for (int m = 0; m < mainMedias.length; m++) {
                  mainMedia += findCodeName(form, "MEDIUM_TYPE", mainMedias[m]) + " ";
               }
            }

            row = sheet.createRow(i + 1);
            row.createCell((short) 0).setCellValue(returnString(item.get("cName")));
            row.createCell((short) 1).setCellValue(returnString(item.get("eName")));
            row.createCell((short) 2).setCellValue(sex);
            row.createCell((short) 3).setCellValue(birthTime);
            row.createCell((short) 4).setCellValue(deathTime);
            row.createCell((short) 5).setCellValue(countryMap.get(returnString(item.get("birthCountry"))));
            row.createCell((short) 6).setCellValue(returnString(item.get("birthplace")));
            row.createCell((short) 7).setCellValue(returnString(item.get("ancestralHome")));
            row.createCell((short) 8).setCellValue(countryMap.get(returnString(item.get("nationality"))));
            row.createCell((short) 9).setCellValue(zodiac);
            row.createCell((short) 10).setCellValue(returnString(item.get("nhom")));
            row.createCell((short) 11).setCellValue(artistType);
            row.createCell((short) 12).setCellValue(mainMedia);
            row.createCell((short) 13).setCellValue(returnString(item.get("personalPage")));
            row.createCell((short) 14).setCellValue(returnString(item.get("cResume")));
            i++;
         }
         return hssfWorkbook;
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
   private ArtArtistDao                artArtistDao;

   @Resource
   private ArtArtistExperienceDao      artArtistExperienceDao;

   @Resource
   private ArtArtistEduDao             artArtistEduDao;

   @Resource
   private ArtArtistHonorsDao          artArtistHonorsDao;

   @Resource
   private ArtArtistCollectorDao       artArtistCollectorDao;

   @Resource
   private ArtArtistCollectAgencyDao   artArtistCollectAgencyDao;

   @Resource
   private ArtArtistCoopDao            artArtistCoopDao;

   @Resource
   private ArtArtistDonationDao        artArtistDonationDao;

   @Resource
   private ArtArtistWorksSeriesDao     artArtistWorksSeriesDao;

   @Resource
   private ArtWorksDao                 artWorksDao;

   @Resource
   private SysCodeService              sysCodeService;

   @Resource
   private ArtPhotoDao                 artPhotoDao;

   @Resource
   private ArtCountryService           artCountryService;

   @Resource
   private ArtActivityExhibitArtistDao artActivityExhibitArtistDao;

   @Resource
   private ArtLiteratureMediumDao      artLiteratureMediumDao;

   @Resource
   private ArtLiteratureWordsDao       artLiteratureWordsDao;

   @Resource
   private ArtLiteratureNetworkDao     artLiteratureNetworkDao;

   @Resource
   private ArtPublicationDao           artPublicationDao;

   @Resource
   private ArtAuctionDao               artAuctionDao;

   public void setArtArtistDao(ArtArtistDao artArtistDao) {
      this.artArtistDao = artArtistDao;
   }
}
