package com.golead.art.literature;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;

import com.golead.art.literature.analyzer.FullIndexPagination;
import com.golead.art.literature.service.FullIndexQueryService;
import com.golead.art.literature.service.SearchEngineService;
import com.golead.core.common.Code;
import com.golead.core.common.CodeSet;
import com.golead.core.dao.model.PageQuery;
import com.golead.core.web.action.BaseAction;

public class SearchEngineAction extends BaseAction {

   private final Log             log = LogFactory.getLog(SearchEngineAction.class);

   @Resource
   private SearchEngineService   searchEngineService;

   @Resource
   private FullIndexQueryService fullIndexQueryService;

   @Override
   public String doExecute() throws Exception {
      String forward = "";
      String action = form.getAction();

      if (action == null) action = "INDEX";
      if (log.isDebugEnabled()) log.debug("action:" + action);
      try {
         if ("INDEX".equalsIgnoreCase(action)) forward = goIndex(); // 打开主页面
         else if ("RESULT".equalsIgnoreCase(action)) forward = toResultPage(); // 打开查询列表页面
         else if (VIEW.equalsIgnoreCase(action)) forward = goViewDocument();
         else if ("DOWNLOAD".equalsIgnoreCase(action)) forward = getData();
         else forward = ERROR;
      }
      catch (Exception e) {// 其他系统出错
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         forward = ERROR;
      }
      return forward;
   }

   public String goIndex() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'goIndex' method");
      try {
         initFrom();
         return "INDEX";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }
   }

   private void initFrom() {
      setCode(form, "WORDS_TYPE");
      List<Code> type = form.getCodeSets().get("WORDS_TYPE");
      if(type != null && type.size() > 0){
         List<Code> types = new ArrayList<Code>();
         for(Code code : type){
            if(!"".equals(code.getValue())){
               Code c = new Code();
               c = code;
               types.add(c);
            }
         }
         setCode(form, "WORDSTYPE", types, "codeName", "value", false);
      }
//      if (form.getCodeSets().get("WORDS_TYPE") != null) {
//         if ("".equals(form.getCodeSets().get("WORDS_TYPE").get(0).getCodeName())) {
//            form.getCodeSets().get("WORDS_TYPE").remove(0);
//         }
//      }
   }

   public String toResultPage() throws Exception {
      if (log.isDebugEnabled()) log.debug("Entering 'toResultPage' method");

      try {
         initFrom();
         HttpServletRequest httpRequest = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
         String text = form.getQuery().getParameters().get("text");
         if (text == null) {
            text = httpRequest.getParameter("text");
            form.getQuery().getParameters().put("text", text);
         }
         String startDate = form.getQuery().getParameters().get("startDate");
         String endDate = form.getQuery().getParameters().get("endDate");
         String title = form.getQuery().getParameters().get("title");
         String wordsType = form.getQuery().getParameters().get("wordsType");
         String author = form.getQuery().getParameters().get("author");
         String maxresults = form.getRecord().get("maxresults");
         if (maxresults == null) {
            maxresults = httpRequest.getParameter("maxresults");
            form.getRecord().put("maxresults", maxresults);
         }
         String pageIndex = httpRequest.getParameter("pageIndex");
         String pageSize = httpRequest.getParameter("pageSize");
         if (pageSize == null) {
            pageSize = (maxresults == null ? "100" : maxresults);
         }
         else {
            form.getRecord().put("maxresults", pageSize);
         }
         Map<String, String> queryStrs = new HashMap<String, String>();
         queryStrs.put("text", text);
         queryStrs.put("wordsType", wordsType);
         queryStrs.put("author", author);
         queryStrs.put("title", title);
         queryStrs.put("startDate", startDate);
         queryStrs.put("endDate", endDate);
         
         FullIndexPagination fullIndexPageinantion = searchEngineService.searchTalent(queryStrs,
               (pageIndex == null || pageIndex == "") ? 1 : Integer.parseInt(pageIndex), Integer.parseInt(pageSize));

         List<Map<String, Object>> list = fullIndexPageinantion.getSearchlist();
         if (list == null) list = new ArrayList<Map<String, Object>>();
         httpRequest.setAttribute("pageIndex", fullIndexPageinantion.getPageNo());
         httpRequest.setAttribute("pageSize", fullIndexPageinantion.getPageSize());
         httpRequest.setAttribute("totalNum", fullIndexPageinantion.getTotalNum());
         httpRequest.setAttribute("totalPage", fullIndexPageinantion.getTotalPage());
         httpRequest.setAttribute("results", list);
         httpRequest.setAttribute("text", text);
         httpRequest.setAttribute("startDate", startDate);
         httpRequest.setAttribute("endDate", endDate);
         httpRequest.setAttribute("author", author);
         httpRequest.setAttribute("title", title);
         httpRequest.setAttribute("wordsType", wordsType);
         httpRequest.setAttribute("maxresults", maxresults);
         PageQuery pq = form.getQuery();
         pq.setRecordSet(list);
         return "RESULT";
      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("errMsg", e.getMessage());
         return ERROR;
      }

   }

   public String goViewDocument() {
      try {
         Integer docId = Integer.valueOf(form.getRecord().get("docId"));
         form.getRecord().putAll(fullIndexQueryService.readDocContent(docId));

      }
      catch (Exception e) {
         e.printStackTrace();
         e.printStackTrace();
         request.put("msg", e.getMessage());
         request.put("returnUrl", "../literature/searchEngine.do");
         return MESSAGE;
      }

      return VIEW;
   }

   public String getData() {
      BufferedInputStream fis = null;
      BufferedOutputStream toClient = null;
      try {
         Integer docId = Integer.valueOf(form.getRecord().get("docId"));
         Map<String, String> map = fullIndexQueryService.readDocContent(docId);
         File file = new File(map.get("fileFullName"));
         String filename = map.get("fileName");

         // 以流的形式下载文件。
         fis = new BufferedInputStream(new FileInputStream(file));
         byte[] buffer = new byte[fis.available()];
         fis.read(buffer);

         HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
         response.reset();
         response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO-8859-1"));
         response.addHeader("Content-Length", "" + file.length());
         response.setContentType("application/octet-stream");

         toClient = new BufferedOutputStream(response.getOutputStream());
         toClient.write(buffer);
         toClient.flush();

      }
      catch (Exception e) {
         e.printStackTrace();
         request.put("msg", e.getMessage());
         request.put("returnUrl", "../literature/FullIndexQuery.do");

      }
      finally {
         try {
            if (fis != null) fis.close();
            if (toClient != null) toClient.close();

         }
         catch (IOException e) {
            e.printStackTrace();
         }
      }

      return null;
   }

}
