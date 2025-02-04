package com.golead.art.auction.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.golead.art.artist.model.ArtCountry;
import com.golead.art.auction.dao.ArtAuctionHousesDao;
import com.golead.art.auction.model.ArtAuctionHouses;
import com.golead.art.auction.service.ArtAuctionHousesService;
import com.golead.art.auction.service.ArtAuctionService;
import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;

@Service
public class ArtAuctionHousesServiceImpl extends BaseServiceImpl implements ArtAuctionHousesService {
   private static final long serialVersionUID = 1L;

   public ArtAuctionHouses getArtAuctionHouses(Serializable id) throws ServiceException {
      try {
         return artAuctionHousesDao.get(id);
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

   public void createArtAuctionHouses(ArtAuctionHouses artAuctionHouses) throws ServiceException {
      try {
         artAuctionHousesDao.save(artAuctionHouses);
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

   public void updateArtAuctionHouses(Map<String, String> artAuctionHousesMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artAuctionHousesMap.get(ArtAuctionHouses.PROP_ID));
         ArtAuctionHouses tmp = artAuctionHousesDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artAuctionHousesMap, true);
         artAuctionHousesDao.update(tmp);
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

   public void deleteArtAuctionHouses(Serializable id) throws ServiceException {
      try {
         artAuctionHousesDao.delete(id);
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

   public String deleteArtAuctionHousess(Serializable[] ids) throws ServiceException {
      String message = "";
      try {
         PageQuery pq = new PageQuery();
         List<Integer> worksAuction = new ArrayList<Integer>();
         for (Serializable id : ids) {
            pq.getParameters().put("worksId", id.toString());
            pq = artAuctionService.queryArtAuction(pq);
            if (pq.getRecordSet() != null) {
               for (Map<String, Object> item : pq.getRecordSet()) {
                  worksAuction.add(Integer.valueOf(item.get("id").toString()));
               }
            }
            if (worksAuction.size() > 0) {
               message = "该拍卖行有作品拍卖，不可以删除！（如果需要请先删除该拍卖行相关作品拍卖）";
               return message;
            }
         }
         Integer[] worksAuctionIds = delete(worksAuction);
         artAuctionService.deleteArtAuctions(worksAuctionIds);
         artAuctionHousesDao.deleteAll(ids);
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

   public String importHouses(String path) throws ServiceException {
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
               ArtAuctionHouses artAuctionHouses = new ArtAuctionHouses();
               String str = "";
               str = getValue(hssfRow.getCell(0));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到拍卖行";
                  break;
               }
               artAuctionHouses.setAuctionHouse(str);
               str = getValue(hssfRow.getCell(1));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到国家";
                  break;
               }
               artAuctionHouses.setNationality(str);
               str = getValue(hssfRow.getCell(2));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到地址";
                  break;
               }
               artAuctionHouses.setAddr(str);
               str = getValue(hssfRow.getCell(3));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到描述";
                  break;
               }
               artAuctionHouses.setRemark(str);
               str = getValue(hssfRow.getCell(4));
               if ("".equals(str)) {
                  message = "第" + String.valueOf(rowNum) + "行找不到网址";
                  break;
               }
               artAuctionHouses.setWebsite(str);
               createArtAuctionHouses(artAuctionHouses);
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

   public List<ArtAuctionHouses> getAllHouses() throws ServiceException {
      try {
         return artAuctionHousesDao.findAll();
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

   public PageQuery queryArtAuctionHouses(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artAuctionHousesList", pageQuery);
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
      String auctionHouse = map.get("auctionHouse");
      if (auctionHouse != null && auctionHouse.trim().length() > 0) {
         if (paras.length() > 0) paras += " and ";
         paras += " aah.auction_house like '%" + auctionHouse.trim() + "%'";
      }
      if (paras.length() > 0) page.getParameters().put("paras", paras);
   }
   
   public int findByCountry(String ids) throws ServiceException{
      try {
         String str = "SELECT COUNT(*) FROM art_auction_houses houses WHERE houses.nationality IN (" + ids + ")";
         return jdbcDao.queryIntBySql(str);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   @SuppressWarnings("deprecation")
   public HSSFWorkbook export(BaseForm form, PageQuery pageQuery) throws ServiceException{
      try {
         HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
         HSSFSheet sheet = hssfWorkbook.createSheet("sheet1");
         HSSFRow row = sheet.createRow((int) 0);  
         
         HSSFCell cell = row.createCell((short) 0);  
         cell.setCellValue("拍卖行");  
         cell = row.createCell((short) 1);  
         cell.setCellValue("国家");  
         cell = row.createCell((short) 2);  
         cell.setCellValue("地址");  
         cell = row.createCell((short) 3);  
         cell.setCellValue("网址");  
         cell = row.createCell((short) 4);  
         cell.setCellValue("描述");  
         pageQuery.setPageSize("10000");
         PageQuery pageQuery2 = queryArtAuctionHouses(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            row = sheet.createRow(i+1); 
            row.createCell((short) 0).setCellValue(returnString(item.get("auctionHouse"))); 
            row.createCell((short) 1).setCellValue(returnString(item.get("nationality"))); 
            row.createCell((short) 2).setCellValue(returnString(item.get("addr"))); 
            row.createCell((short) 3).setCellValue(returnString(item.get("website"))); 
            row.createCell((short) 4).setCellValue(returnString(item.get("remark"))); 
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
   
   private String findCodeName(Object objForm, String codeNo, Serializable value){
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
   private ArtAuctionHousesDao artAuctionHousesDao;

   @Resource
   private ArtAuctionService   artAuctionService;

}
