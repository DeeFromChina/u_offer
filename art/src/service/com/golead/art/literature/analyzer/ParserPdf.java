package com.golead.art.literature.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class ParserPdf {
   private static Logger log = Logger.getLogger(ParserPdf.class);

   public static StringBuffer parserPDF(String path) {
      StringBuffer content = new StringBuffer("");// 文档内容 
      FileInputStream fis = null;
      PDDocument pdd = null;

      try {
         File f = new File(path);
         if (!f.exists()) return content;
         fis = new FileInputStream(path);
         PDFParser p = new PDFParser(fis);
         PDFTextStripper ts = new PDFTextStripper();
         p.parse();
         pdd = p.getPDDocument();
         if (pdd.isEncrypted()) content.append("EncryptError");
         else content.append(ts.getText(pdd));

      }
      catch (IOException ioe) {
         log.error("PDF IO error:", ioe);
         System.out.println("提取文件" + path + "的文本内容出错！，附件索引未建成功！");
      }
      catch (Exception e) {
         log.error("PDF Parser error:", e);
         System.out.println("提取文件" + path + "的文本内容出错！，附件索引未建成功！");
      }
      finally {
         try {
            if (pdd != null) pdd.close();
            if (fis != null) fis.close();
         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }
      return content;

   }
}
