package com.golead.art.artist.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.ServiceException;
import com.golead.core.exception.DAOException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.art.artist.dao.ArtArtistDonationDao;
import com.golead.art.artist.model.ArtArtistCoop;
import com.golead.art.artist.model.ArtArtistDonation;
import com.golead.art.artist.service.ArtArtistDonationService;
import com.golead.art.works.dao.ArtWorksDao;
import com.golead.art.works.model.ArtWorks;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class ArtArtistDonationServiceImpl extends BaseServiceImpl implements ArtArtistDonationService {
   private static final long serialVersionUID = 1L;

   public ArtArtistDonation getArtArtistDonation(Serializable id) throws ServiceException {
      try {
         return artArtistDonationDao.get(id);
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

   public void createArtArtistDonation(ArtArtistDonation artArtistDonation) throws ServiceException {
      try {
         artArtistDonationDao.save(artArtistDonation);
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

   public void updateArtArtistDonation(Map<String, String> artArtistDonationMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artArtistDonationMap.get(ArtArtistDonation.PROP_ID));
         ArtArtistDonation tmp = artArtistDonationDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artArtistDonationMap, true);
         artArtistDonationDao.update(tmp);
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

   public void deleteArtArtistDonation(Serializable id) throws ServiceException {
      try {
         artArtistDonationDao.delete(id);
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

   public void deleteArtArtistDonations(Serializable[] ids) throws ServiceException {
      try {
         artArtistDonationDao.deleteAll(ids);
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

   public void deleteArtArtistWithDonations(Serializable[] ids) throws ServiceException {
      try {
         for (Serializable id : ids) {
            artArtistDonationDao.deleteByField("artistId", id);
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

   public String importDonations(String path, int artistId) throws ServiceException {
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
               ArtArtistDonation artArtistDonation = new ArtArtistDonation();
               String str = "";
               artArtistDonation.setArtistId(artistId);
               str = getValue(hssfRow.getCell(0));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到事件";
                  break;
               }
               artArtistDonation.setDonationDesc(str);
               str = getValue(hssfRow.getCell(1));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到时间";
                  break;
               }
               artArtistDonation.setDonationTime(str);
               str = getValue(hssfRow.getCell(2));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到捐赠作品";
                  break;
               }
               artArtistDonation.setDonationWorks(str);
               createArtArtistDonation(artArtistDonation);
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

   public PageQuery queryArtArtistDonation(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artArtistDonationList", pageQuery);
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
      String artistId = page.getParameters().get("artistId");
      String paras = "";
      paras = " aad.artist_id = " + artistId + " ";
      if (paras.length() > 0) {
         page.getParameters().put("paras", paras);
      }
   }

   public String findByWorksId(String ids) throws ServiceException{
      try {
         String hql = "FROM ArtWorks works WHERE works.id IN (" + ids + ")";
         List<ArtWorks> artWorks = artWorksDao.findByHql(hql);
         if(artWorks == null || artWorks.size() == 0){
            return "";
         }
         StringBuffer worksName = new StringBuffer();
         for(ArtWorks artWorks2 : artWorks){
            if(worksName.length() != 0){
               worksName.append(",");
            }
            worksName.append(artWorks2.getWorksCName());
         }
         return worksName.toString();
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   public List<ArtWorks> findWorksByWorksId(String ids) throws ServiceException{
      try {
         String hql = "FROM ArtWorks works WHERE works.id IN (" + ids + ")";
         List<ArtWorks> artWorks = artWorksDao.findByHql(hql);
         if(artWorks == null || artWorks.size() == 0){
            return null;
         }
         return artWorks;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }
   
   @Resource
   private ArtArtistDonationDao artArtistDonationDao;
   
   @Resource
   private ArtWorksDao artWorksDao;

   public void setArtArtistDonationDao(ArtArtistDonationDao artArtistDonationDao) {
      this.artArtistDonationDao = artArtistDonationDao;
   }
}
