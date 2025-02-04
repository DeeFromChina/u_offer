package com.golead.art.job.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

public class ImageUtils {

   private final String FROM_FILE_PATH = "d:/art/zhangxiaogang";           //源文件夹

   private final String TO_FILE_PATH   = "d:/art/zhangxiaogang/thumbnail"; //目标文件夹

   public static void main(String[] args) {
      new ImageUtils().handlerImage();

   }

   private void handlerImage() {
      int newSize = 100;
      try {
         File file = new File(FROM_FILE_PATH);
         File[] tempList = file.listFiles();
         System.out.println("该目录下对象个数：" + tempList.length);
         for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
               String imageName = tempList[i].getName();
               String extension = imageName.substring(imageName.lastIndexOf(".") + 1).toLowerCase();
               if (extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("tiff")
                     || extension.equals("bmp")) {
                  BufferedImage img = ImageIO.read(tempList[i]);
                  int width = img.getWidth();
                  int height = img.getHeight();

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
                  /**
                   * OutputStream out = new FileOutputStream(new
                   * File(TO_FILE_PATH + File.separator + imageName));
                   * BufferedImage img2 = new BufferedImage(width, height,
                   * BufferedImage.TYPE_INT_RGB);
                   * img2.getGraphics().drawImage(img, 0, 0, width, height,
                   * null); ImageIO.write(img2, "jpg", out);
                   **/
                  Thumbnails.of(tempList[i]).size(width, height).outputFormat(extension).outputQuality(1.0f).toFile(TO_FILE_PATH + File.separator + imageName);//保存小图

               }
            }
         }

      }
      catch (Exception e) {
         e.printStackTrace();
      }

   }
}
