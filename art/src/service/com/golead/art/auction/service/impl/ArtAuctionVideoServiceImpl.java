package com.golead.art.auction.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.auction.dao.ArtAuctionVideoDao;
import com.golead.art.auction.model.ArtAuctionVideo;
import com.golead.art.auction.service.ArtAuctionVideoService;
import com.golead.art.utils.FileUtils;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ArtAuctionVideoServiceImpl extends BaseServiceImpl implements ArtAuctionVideoService {
   private static final long serialVersionUID = 1L;

   public ArtAuctionVideo getArtAuctionVideo(Serializable id) throws ServiceException {
      try {
         return artAuctionVideoDao.get(id);
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

   public void createArtAuctionVideo(ArtAuctionVideo artAuctionVideo, List<File> videoFiles, String filesFileName, String path) throws ServiceException {
      try {
         StringBuffer videoLink = new StringBuffer();
         String[] names;
         if (videoFiles != null) {
            names = filesFileName.split(",");
            for (int i = 0; i < videoFiles.size(); i++) {
               File file = videoFiles.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String endless = names[i].split("\\.")[1];
               FileUtils.fileUpload(path + File.separator + cName + "." + endless, file);
               videoLink.append(cName + "." + endless);
               if ((i + 1) != videoFiles.size()) videoLink.append(";");
            }
         }
         artAuctionVideo.setVideoLink(videoLink.toString());
         artAuctionVideoDao.save(artAuctionVideo);
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

   public void updateArtAuctionVideo(Map<String, String> artAuctionVideoMap, List<File> videoFiles, String fileName, String filesFileName, String path)
         throws ServiceException {
      try {
         Integer pkId = new Integer(artAuctionVideoMap.get(ArtAuctionVideo.PROP_ID));
         ArtAuctionVideo tmp = artAuctionVideoDao.get(pkId);
         StringBuffer newVideoLink = new StringBuffer();

         String videoLink;
         String[] deleteVideoLinks;//获取删除的videoLink值
         if (tmp.getVideoLink() != null && !"".equals(tmp.getVideoLink())) {
            videoLink = tmp.getVideoLink();
            deleteVideoLinks = videoLink.replace(fileName, "").split(";");
            File file = new File(path);
            File[] fileList = file.listFiles();
            if (fileList != null) {
               for (File inFile : fileList) {
                  for (String deleteVideoLink : deleteVideoLinks) {
                     if (deleteVideoLink.equals(inFile.getName())) {
                        inFile.delete();
                     }
                  }
               }
//               File[] fileList2 = new File(path).listFiles();
//               if (fileList2.length == 0) {
//                  file.delete();
//               }
            }
            if (fileName.length() > 0) {
               newVideoLink.append(fileName + ";");
            }
         }

         if (videoFiles != null) {
            String[] names = filesFileName.split(",");
            for (int i = 0; i < videoFiles.size(); i++) {
               File file = videoFiles.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String endless = names[i].split("\\.")[1];
               FileUtils.fileUpload(path + File.separator + cName + "." + endless, file);
               newVideoLink.append(cName + "." + endless);
               if ((i + 1) != videoFiles.size()) newVideoLink.append(";");
            }
         }

         tmp.setVideoLink(newVideoLink.toString());
         ConvertUtil.mapToObject(tmp, artAuctionVideoMap, true);
         artAuctionVideoDao.update(tmp);
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

   public void deleteArtAuctionVideo(Serializable id) throws ServiceException {
      try {
         artAuctionVideoDao.delete(id);
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

   public void deleteArtAuctionVideos(Serializable[] ids, String path) throws ServiceException {
      try {
         List<String> fileNames = new ArrayList<String>();
         for (Serializable id : ids) {
            ArtAuctionVideo artAuctionVideo = getArtAuctionVideo(id);
            String[] videoLinks = artAuctionVideo.getVideoLink().split(";");
            if (artAuctionVideo.getVideoLink().length() > 0) {
               for (String videoLink : videoLinks) {
                  fileNames.add(videoLink);
               }
            }
         }
         File[] fileLies = new File(path).listFiles();
         if (fileLies != null) {
            for (File file : fileLies) {
               for (String fileName : fileNames) {
                  if (fileName.equals(file.getName())) {
                     file.delete();
                  }
               }
            }
            fileLies = new File(path).listFiles();
            if (fileLies.length == 0) {
               File file = new File(path);
               file.delete();
            }
         }
         artAuctionVideoDao.deleteAll(ids);
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

   public void deleteArtAuctionVideos(Serializable[] ids) throws ServiceException {
      try {
         artAuctionVideoDao.deleteAll(ids);
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

   public String importVideo(String path, int auctionId) throws ServiceException {
      String message = "";
      try {
         FileInputStream fileInputStream = new FileInputStream(path);
         HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
         HSSFSheet sht = wb.getSheetAt(0);
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
               ArtAuctionVideo artAuctionVideo = new ArtAuctionVideo();
               String str = "";
               Date time = null;
               artAuctionVideo.setAuctionId(auctionId);
               str = getValue(hssfRow.getCell(1));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到视频主题";
                  break;
               }
               artAuctionVideo.setVideoTheme(str);
               time = hssfRow.getCell(2).getDateCellValue();
               if ("".equals(time)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到时间";
                  break;
               }
               artAuctionVideo.setVideoTime(time);
               str = getValue(hssfRow.getCell(3));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到出处";
                  break;
               }
               artAuctionVideo.setVideoSource(str);

               artAuctionVideoDao.save(artAuctionVideo);

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

   public PageQuery queryArtAuctionVideo(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artAuctionVideoList", pageQuery);
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
      String auctionId = map.get("auctionId");
      if (auctionId != null && !"".equals(auctionId.trim())) {
         paras = " auction_id = " + auctionId.trim();
      }
      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }

   @Resource
   private ArtAuctionVideoDao artAuctionVideoDao;

   public void setArtAuctionVideoDao(ArtAuctionVideoDao artAuctionVideoDao) {
      this.artAuctionVideoDao = artAuctionVideoDao;
   }
}
