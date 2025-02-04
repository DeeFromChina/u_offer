package com.golead.art.job.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.golead.art.artist.model.ArtCollectArtist;
import com.golead.art.auction.model.ArtAuctionHouses;
import com.golead.art.job.service.CheckArtAuction;

import net.coobird.thumbnailator.Thumbnails;

public class ArtAuctionGeneImpl extends BaseCheckAuctionImpl implements CheckArtAuction {

   public void executeCheck() throws Exception {
      // 艺术家库
      /**List<ArtCollectArtist> artists = artCollectArtistDao.findAll();
      
      // 采集数据
      List<Map<String, Object>> list = new DataCollectHandler().executeCollect();
      if (list != null && list.size() > 0) {
         for (Map<String, Object> map : list) {
            // 拍卖行库
            List<ArtAuctionHouses> houses = artAuctionHousesDao.findAll();
            artAuctiosService.createArtAuction(map, artists, houses);
         }
      }
      // 下载图片
      download(list, artists);**/
   }

   public void download(List<Map<String, Object>> list, List<ArtCollectArtist> artists) throws Exception {
      String path = System.getProperty("artApplication.root") + "upload/auction/";
      String tmpPath = "";
      int newSize = 100;
      for (Map<String, Object> map : list) {
         String author = map.get("author") == null ? "" : map.get("author").toString();//作者
         String eName = null;
         for (ArtCollectArtist artist : artists) {
            // 查找是否能在艺术家库找到对应的艺术家
            if (author.indexOf(artist.getArtistName()) > -1) {
               eName = artist.getFolderName();
               break;
            }
         }
         if (eName != null) {
            tmpPath = path + File.separator + eName;
            File file = new File(tmpPath);
            // 判断文件夹是否存在，不存在则新建
            if (!file.exists()) file.mkdir();

            file = new File(tmpPath + File.separator + "thumbnail");
            // 判断文件夹是否存在，不存在则新建
            if (!file.exists()) file.mkdir();
         } else {
            continue;
         }
      
         try {
            String imageUrl = map.get("imageUrl") == null ? null : map.get("imageUrl").toString();//图片
            if (imageUrl != null) {
               String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
               String imagePath = tmpPath + File.separator + imageName;
               String extension = imageUrl.substring(imageUrl.lastIndexOf(".") + 1).toLowerCase();
               if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("tiff")
                     || extension.equals("bmp")) {
                  byte[] btImg = getImageFromNetByUrl(imageUrl);
                  if (null != btImg && btImg.length > 0) {
                     writeImageToDisk(btImg, imagePath);
                     //保存缩略图
                     saveThumbnail(tmpPath, newSize, imageName, imagePath, extension);
                  }
               }
            }
         }
         catch (MalformedURLException e) {
            e.printStackTrace();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   /**
    * 保存缩略图
    * @param tmpPath
    * @param newSize
    * @param imageName
    * @param imagePath
    * @param extension
    * @throws IOException
    */
   private void saveThumbnail(String tmpPath, int newSize, String imageName, String imagePath, String extension) throws IOException {
      BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
      int width = bufferedImage.getWidth();
      int height = bufferedImage.getHeight();
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
            .toFile(tmpPath + File.separator + "thumbnail/" + imageName);//保存小图
   }

   /**
    * 将图片写入到磁盘
    * 
    * @param img
    *           图片数据流
    * @param fileName
    *           文件保存时的名称
    */
   public static void writeImageToDisk(byte[] img, String fileName) {
      try {
         File file = new File(fileName);
         FileOutputStream fops = new FileOutputStream(file);
         fops.write(img);
         fops.flush();
         fops.close();
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * 根据地址获得数据的字节流
    * 
    * @param strUrl
    *           网络连接地址
    * @return
    */
   private byte[] getImageFromNetByUrl(String strUrl) {
      try {
         URL url = new URL(strUrl);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         conn.setConnectTimeout(5 * 1000);
         InputStream inStream = conn.getInputStream();//通过输入流获取图片数据  
         byte[] btImg = readInputStream(inStream);//得到图片的二进制数据  
         return btImg;
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

   /**
    * 从输入流中获取数据
    * 
    * @param inStream
    *           输入流
    * @return
    * @throws Exception
    */
   private byte[] readInputStream(InputStream inStream) throws Exception {
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inStream.read(buffer)) != -1) {
         outStream.write(buffer, 0, len);
      }
      inStream.close();
      return outStream.toByteArray();
   }

}
