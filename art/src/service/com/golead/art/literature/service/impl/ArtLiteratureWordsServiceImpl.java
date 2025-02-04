package com.golead.art.literature.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.golead.art.artist.dao.ArtArtistDao;
import com.golead.art.artist.model.ArtArtist;
import com.golead.art.literature.analyzer.ParserPdf;
import com.golead.art.literature.dao.ArtLiteratureWordsDao;
import com.golead.art.literature.model.ArtLiteratureWords;
import com.golead.art.literature.model.ArtLiteratureWordsResearch;
import com.golead.art.literature.service.ArtLiteratureWordsResearchService;
import com.golead.art.literature.service.ArtLiteratureWordsService;
import com.golead.art.utils.FileUtils;
import com.golead.art.works.dao.ArtWorksWordsDao;
import com.golead.art.works.model.ArtWorksWords;
import com.golead.core.common.Code;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.exception.DAOException;
import com.golead.core.exception.ServiceException;
import com.golead.core.service.impl.BaseServiceImpl;
import com.golead.core.util.ConvertUtil;
import com.golead.core.web.form.BaseForm;

@Service
public class ArtLiteratureWordsServiceImpl extends BaseServiceImpl implements ArtLiteratureWordsService {
   private static final long serialVersionUID = 1L;

   public ArtLiteratureWords getArtLiteratureWords(Serializable id) throws ServiceException {
      try {
         return artLiteratureWordsDao.get(id);
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

   public void createArtLiteratureWords(ArtLiteratureWords artLiteratureWords) throws ServiceException {
      try {
         artLiteratureWordsDao.save(artLiteratureWords);
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

   public void createArtLiteratureWords(Map<String, String> map, List<File> files, String filesFileName, String path, String pathIndex,
         Map<String, Object> reauest) throws ServiceException {
      try {
         ArtLiteratureWords artLiteratureWords = new ArtLiteratureWords();
         ConvertUtil.mapToObject(artLiteratureWords, map, false);
         artLiteratureWordsDao.save(artLiteratureWords);
         ArtLiteratureWordsResearch artLiteratureWordsResearch = new ArtLiteratureWordsResearch();
         artLiteratureWordsResearch.setWordsId(artLiteratureWords.getId());
         ConvertUtil.mapToObject(artLiteratureWordsResearch, map, false);
         artLiteratureWordsResearchService.createArtLiteratureWordsResearch(artLiteratureWordsResearch);
         if (files != null) {
            saveFile(artLiteratureWords, files, filesFileName, path, pathIndex, reauest);
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

   public void updateArtLiteratureWords(Map<String, String> artLiteratureWordsMap) throws ServiceException {
      try {
         Integer pkId = new Integer(artLiteratureWordsMap.get(ArtLiteratureWords.PROP_ID));
         ArtLiteratureWords tmp = artLiteratureWordsDao.get(pkId);
         ConvertUtil.mapToObject(tmp, artLiteratureWordsMap, true);
         artLiteratureWordsDao.update(tmp);
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

   public void deleteArtLiteratureWords(Serializable id) throws ServiceException {
      try {
         artLiteratureWordsDao.delete(id);
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

   public void deleteArtLiteratureWordss(Serializable[] ids) throws ServiceException {
      try {
         boolean pass = false;
         for (Serializable id : ids) {
            List<ArtWorksWords> artWorksWords = artWorksWordsDao.findByField(ArtWorksWords.PROP_WORDS_ID, id);
            if (artWorksWords != null && artWorksWords.size() > 0) {
               pass = true;
            }
            if (pass) { throw new ServiceException(); }
         }
         artLiteratureWordsDao.deleteAll(ids);
      }
      catch (DAOException e) {
         e.printStackTrace();
         throw new ServiceException("数据库操作错误。");
      }
      catch (ServiceException se) {
         se.printStackTrace();
         throw new ServiceException("文献正在被引用!");
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   public PageQuery queryArtLiteratureWords(PageQuery pageQuery) throws ServiceException {
      try {
         createSqlFilter(pageQuery);
         return jdbcDao.queryBySqlId("artLiteratureWordsList", pageQuery);
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
      String artArtistId = map.get("artArtistId");
      if (artArtistId != null && !"".equals(artArtistId)) {
         paras += " AND words.artist_id =" + artArtistId.trim();
      }

      String artArtistName = map.get("artArtistName");
      if (artArtistName != null && !"".equals(artArtistName)) {
         paras += " AND artist.c_name LIKE '%" + artArtistName.trim() + "%'";
      }

      String literatureTitle = map.get("literatureTitle");
      if (literatureTitle != null && !"".equals(literatureTitle)) {
         paras += " AND words.literature_title like '%" + literatureTitle.trim() + "%'";
      }

      String literatureAuther = map.get("literatureAuther");
      if (literatureAuther != null && !"".equals(literatureAuther)) {
         paras += " AND words.literature_auther like '%" + literatureAuther.trim() + "%'";
      }

      String wordsType = map.get("wordsType");
      if (wordsType != null && !"".equals(wordsType)) {
         paras += " AND words.words_type =" + wordsType.trim();
      }

      String literatureWorks = map.get("literatureWorks");
      if (literatureWorks != null && !"".equals(literatureWorks)) {
         paras += " AND words.literature_works like '%" + literatureWorks.trim() + "%'";
      }

      String relatedExhib = map.get("relatedExhib");
      if (relatedExhib != null && !"".equals(relatedExhib)) {
         paras += " AND words.related_exhib like '%" + relatedExhib.trim() + "%'";
      }
      String relatedEvent = map.get("relatedEvent");
      if (relatedEvent != null && !"".equals(relatedEvent)) {
         paras += " AND words.related_event like '%" + relatedEvent.trim() + "%'";
      }
      String personInvolved = map.get("personInvolved");
      if (personInvolved != null && !"".equals(personInvolved)) {
         paras += " AND words.person_involved like '%" + personInvolved.trim() + "%'";
      }
      String quoteLiterature = map.get("quoteLiterature");
      if (quoteLiterature != null && !"".equals(quoteLiterature)) {
         paras += " AND words.quote_literature like '%" + quoteLiterature.trim() + "%'";
      }

      if (paras.length() > 0) {
         page.getParameters().put("paras", paras);
      }
   }

   private String artistName(String path, int id) {
      List<ArtArtist> artArtists = artArtistDao.findByField(ArtArtist.PROP_ID, id);
      ArtArtist artArtist = null;
      if (artArtists != null && artArtists.size() > 0) {
         artArtist = artArtists.get(0);
      }
      if (artArtist == null) { return path; }
      path = path + File.separator + artArtist.getFolderName();
      return path;
   }

   private boolean saveFile(ArtLiteratureWords words, List<File> files, String filesFileName, String path, String pathIndex, Map<String, Object> request)
         throws ServiceException {
      try {
         String[] names = filesFileName.split(",");
         path = artistName(path, words.getArtistId());
         String fName = "";
         if (!new File(path).exists()) {
            new File(path).mkdirs();
         }

         StringBuffer fileName = new StringBuffer();
         if (files != null) {
            for (int i = 0; i < files.size(); i++) {
               File file = files.get(i);
               String cName = String.valueOf(System.currentTimeMillis());
               String endless = names[i].substring(names[i].lastIndexOf("."));
               fName = path + File.separator + cName + endless;
               String tmpFileStr = request.get("CONTEXT_PATH") + "/upload/literature/words" + File.separator + cName + endless;
               FileUtils.fileUpload(fName, file);
               fileName.append(names[i] + "/" + cName + endless);
               if ((i + 1) != files.size()) fileName.append(",");
               //建立全文索引
               createLiteratureWordsIndex(tmpFileStr, words, pathIndex, names[i]);
            }
         }
         words.setAttachment(fileName.toString());
         updateArtLiteratureWords(words);
         return false;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void createLiteratureWordsIndex(String path, ArtLiteratureWords words, String pathIndex, String attachment) throws ServiceException {
      IndexWriter writer = null;
      try {
         // 采用标准的分析器。
         Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
         Directory directory = FSDirectory.open(new File(pathIndex)); //打开索引存放目录
         IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_31, analyzer);
         writer = new IndexWriter(directory, config);
         StringBuffer content = ParserPdf.parserPDF(path);
         Document doc = new Document();
         doc.add(new Field("title", words.getLiteratureTitle(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("author", words.getLiteratureAuther(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("time", words.getWriteTime(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("content", content.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("fileFullName", path, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("fileName", attachment, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("wordsType", words.getWordsType(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("publicationName", words.getPublicationName(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         doc.add(new Field("press", words.getPress(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS));
         writer.addDocument(doc);
      }
      catch (IOException e) {
         e.printStackTrace();
         throw new ServiceException("打开索引文件目录错误。");
      }
      finally {
         try {
            if (writer != null) writer.close();
         }
         catch (IOException e) {
            e.printStackTrace();
            throw new ServiceException("关闭IndexWriter，索引文件写入流错误。");
         }
      }

   }

   public void changepath(ArtLiteratureWords artLiteratureWords, String FILE_PATH, String ArtistId) {
      try {
         Integer id = artLiteratureWords.getArtistId();
         ArtArtist artArtist = artArtistDao.get(id);
         String enName = artArtist.getFolderName();
         String attachment = artLiteratureWords.getAttachment();
         String fileName = null;
         //原来路径的文件名
         if (attachment.split("/").length > 1) {
            fileName = attachment.split("/")[1];
            fileName = fileName.replace(",", "");
         }
         //新的路径
         ArtArtist afterArtist = artArtistDao.get(Integer.valueOf(ArtistId));
         String changeEnName = afterArtist.getFolderName();
         String changepath = FILE_PATH + File.separator + changeEnName;
         File changFile = new File(changepath);
         if (!changFile.exists()) {
            changFile.mkdirs();
         }

         if (fileName == null) { return; }
         String path = FILE_PATH + File.separator + enName + File.separator + fileName;
         File file = new File(path);
         if (file.exists() && file.isFile()) {
            FileUtils.fileUpload(changepath + File.separator + file.getName(), file);
            file.delete();
         }

      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("移动文件错误。");
      }
   }

   public void updateFile(ArtLiteratureWordsResearch research, ArtLiteratureWords artLiteratureWords, List<File> files, String filesFileName, String FILE_PATH,
         String fileName, String pathIndex, Map<String, Object> request) throws ServiceException {
      try {
         if (research.getId() != null) {
            artLiteratureWordsResearchService.updateArtLiteratureWordsResearch(research);
         }
         else {
            research.setWordsId(artLiteratureWords.getId());
            artLiteratureWordsResearchService.createArtLiteratureWordsResearch(research);
         }

         String path = artistName(FILE_PATH, artLiteratureWords.getArtistId());
         File file = new File(path);
         File[] files2 = null;
         if (file.exists()) {
            files2 = file.listFiles();//原本的文件
         }
         Map<String, String> map = new HashMap<String, String>();
         if (fileName != null && !"".equals(fileName)) {
            String[] fileNames = fileName.split(",");
            for (int i = 0; i < fileNames.length; i++) {
               if (fileNames[i] != null && !"".equals(fileNames[i])) {
                  map.put(fileNames[i], "");
               }
            }
         }
         String attachment = artLiteratureWords.getAttachment();
         StringBuffer newAttachment = new StringBuffer();
         if (attachment != null && !"".equals(attachment)) {
            String[] attachments = attachment.split(",");
            for (int i = 0; i < attachments.length; i++) {
               if (map.get(attachments[i].split("/")[1]) == null) {
                  deleteFile(files2, attachments[i].split("/")[1]);
                  attachments[i] = "";
               }
               if (attachments[i] != "") {
                  newAttachment.append(attachments[i]);
                  newAttachment.append(",");
               }
            }

         }
         if (files != null) {
            saveFile(artLiteratureWords, files, filesFileName, FILE_PATH, pathIndex, request);
            attachment = artLiteratureWords.getAttachment();
            newAttachment.append(attachment);
         }
         String att = newAttachment.toString();
         if (att.endsWith(",")) {
            att.substring(0, att.length() - 1);
         }
         artLiteratureWords.setAttachment(att);
         updateArtLiteratureWords(artLiteratureWords);
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServiceException("系统错误。");
      }
   }

   private void deleteFile(File[] files, String fileName) {
      for (int i = 0; i < files.length; i++) {
         File file = files[i];
         if (file.getName().equals(fileName)) {
            file.delete();
         }
      }
   }

   public void updateArtLiteratureWords(ArtLiteratureWords artLiteratureWords) throws ServiceException {
      try {
         artLiteratureWordsDao.update(artLiteratureWords);
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
         cell.setCellValue("艺术家");
         cell = row.createCell((short) 1);
         cell.setCellValue("文献标题");
         cell = row.createCell((short) 2);
         cell.setCellValue("作者");
         cell = row.createCell((short) 3);
         cell.setCellValue("刊登时间");
         cell = row.createCell((short) 4);
         cell.setCellValue("出版物名称");
         cell = row.createCell((short) 5);
         cell.setCellValue("出版物期数");
         cell = row.createCell((short) 6);
         cell.setCellValue("出版社");
         cell = row.createCell((short) 7);
         cell.setCellValue("栏目");
         cell = row.createCell((short) 8);
         cell.setCellValue("相关页");
         cell = row.createCell((short) 9);
         cell.setCellValue("文献类型");
         cell = row.createCell((short) 10);
         cell.setCellValue("文献提及作品");
         cell = row.createCell((short) 11);
         cell.setCellValue("引用文献");
         cell = row.createCell((short) 12);
         cell.setCellValue("文献相关人物");
         cell = row.createCell((short) 13);
         cell.setCellValue("文献相关展览");
         cell = row.createCell((short) 14);
         cell.setCellValue("文献相关事件");
         pageQuery.setPageSize("10000");
         PageQuery pageQuery2 = queryArtLiteratureWords(pageQuery);
         int i = 0;
         for (Map<String, Object> item : pageQuery2.getRecordSet()) {
            String wordsType = returnString(item.get("wordsType"));
            if (!"".equals(wordsType)) {
               wordsType = findCodeName(form, "WORDS_TYPE", returnString(item.get("wordsType")));
            }

            row = sheet.createRow(i + 1);
            row.createCell((short) 0).setCellValue(returnString(item.get("CName")));
            row.createCell((short) 1).setCellValue(returnString(item.get("literatureTitle")));
            row.createCell((short) 2).setCellValue(returnString(item.get("literatureAuther")));
            row.createCell((short) 3).setCellValue(returnString(item.get("writeTime")));
            row.createCell((short) 4).setCellValue(returnString(item.get("publicationName")));
            row.createCell((short) 5).setCellValue(returnString(item.get("publicationPeriod")));
            row.createCell((short) 6).setCellValue(returnString(item.get("press")));
            row.createCell((short) 7).setCellValue(returnString(item.get("literatureColumn")));
            row.createCell((short) 8).setCellValue(returnString(item.get("relevantPages")));
            row.createCell((short) 9).setCellValue(wordsType);
            row.createCell((short) 10).setCellValue(returnString(item.get("literatureWorks")));
            row.createCell((short) 11).setCellValue(returnString(item.get("quoteLiterature")));
            row.createCell((short) 12).setCellValue(returnString(item.get("personInvolved")));
            row.createCell((short) 13).setCellValue(returnString(item.get("relatedExhib")));
            row.createCell((short) 14).setCellValue(returnString(item.get("relatedEvent")));
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
   private ArtLiteratureWordsDao             artLiteratureWordsDao;

   @Resource
   private ArtArtistDao                      artArtistDao;

   @Resource
   private ArtWorksWordsDao                  artWorksWordsDao;

   @Resource
   private ArtLiteratureWordsResearchService artLiteratureWordsResearchService;

   public void setArtLiteratureWordsDao(ArtLiteratureWordsDao artLiteratureWordsDao) {
      this.artLiteratureWordsDao = artLiteratureWordsDao;
   }
}
