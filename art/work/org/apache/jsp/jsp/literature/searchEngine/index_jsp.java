/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.42
 * Generated at: 2017-01-20 07:15:52 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.literature.searchEngine;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.HashMap<java.lang.String,java.lang.Long>(1);
    _jspx_dependants.put("/WEB-INF/tag/gl-tag.tld", Long.valueOf(1469518854769L));
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fform_0026_005fnamespace_005fmethod_005fid_005faction;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fvalue_005fname_005fcssStyle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fcheckboxlist_0026_005fname_005flistValue_005flistKey_005flist_005fcssStyle_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fs_005fform_0026_005fnamespace_005fmethod_005fid_005faction = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fvalue_005fname_005fcssStyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fcheckboxlist_0026_005fname_005flistValue_005flistKey_005flist_005fcssStyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fs_005fform_0026_005fnamespace_005fmethod_005fid_005faction.release();
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fvalue_005fname_005fcssStyle_005fnobody.release();
    _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.release();
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005fcheckboxlist_0026_005fname_005flistValue_005flistKey_005flist_005fcssStyle_005fnobody.release();
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

String path = request.getContextPath();
String CONTEXT_PATH = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

      out.write("\r\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n");
      out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n");
      out.write("<head>\r\n");
      out.write("\t<style type=\"text/css\">\r\n");
      out.write("\t\thtml,body{ background:url(");
      out.print(CONTEXT_PATH);
      out.write("jsp/searchEngine/body_bg.jpg) bottom right no-repeat #fff; border:1px solid #f7f7f7; padding:0; margin:0; text-align:center; height:100%; overflow:hidden;}\r\n");
      out.write("\t\t</style>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(CONTEXT_PATH);
      out.write("dhtmlx/dhtmlx.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(CONTEXT_PATH);
      out.write("dhtmlx/skins/dhtmlxcalendar_simplecolorsand.css\" />\r\n");
      out.write("<script src=\"");
      out.print(CONTEXT_PATH);
      out.write("dhtmlx/dhtmlx.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(CONTEXT_PATH);
      out.write("css/main.css\" />\r\n");
      out.write("<script language=\"javascript\" src=\"");
      out.print( CONTEXT_PATH );
      out.write("js/ctrl_util.js\"></script>\r\n");
      out.write("<script language=\"javascript\" src=\"");
      out.print( CONTEXT_PATH );
      out.write("js/date_validate.js\"></script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body onload=\"init()\">\r\n");
      out.write("\t<h1>全文搜索查询</h1>\r\n");
      out.write("\t");
      if (_jspx_meth_s_005fform_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write(" <script>\t \r\n");
      out.write("   \tvar contextpath = \"");
      out.print(CONTEXT_PATH);
      out.write("\";\r\n");
      out.write("   \tvar fulllink = contextpath + \"literature/searchEngine.do\";\t\r\n");
      out.write("   \tvar mainbody=window.document.body;\r\n");
      out.write(" \r\n");
      out.write("     function goSubmit(){\r\n");
      out.write("   \t\t  document.forms[0].action=fulllink+\"?action=RESULT\";\r\n");
      out.write("       \t  document.forms[0].submit();\r\n");
      out.write("   \t}\r\n");
      out.write("     \r\n");
      out.write("    function goCondition(){\t\r\n");
      out.write("    \t$$('cond').style.display='inline';//   \r\n");
      out.write("    }\r\n");
      out.write("    function goStatic(){\r\n");
      out.write("    \t$$('cond').style.display='none';\r\n");
      out.write("    \tgoSubmit();\r\n");
      out.write("    }\r\n");
      out.write("    function goCancel(){\r\n");
      out.write("    \t$$('cond').style.display='none';\r\n");
      out.write("    }  \r\n");
      out.write("    function init(){\r\n");
      out.write("    \t$$('cond').style.display='none';//inline   \r\n");
      out.write("    }\r\n");
      out.write("</script>   \r\n");
      out.write("        \r\n");
      out.write("</body>\r\n");
      out.write("\r\n");
      out.write("</html>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_s_005fform_005f0(javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:form
    org.apache.struts2.views.jsp.ui.FormTag _jspx_th_s_005fform_005f0 = (org.apache.struts2.views.jsp.ui.FormTag) _005fjspx_005ftagPool_005fs_005fform_0026_005fnamespace_005fmethod_005fid_005faction.get(org.apache.struts2.views.jsp.ui.FormTag.class);
    _jspx_th_s_005fform_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fform_005f0.setParent(null);
    // /jsp/literature/searchEngine/index.jsp(26,1) name = action type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fform_005f0.setAction("searchEngine");
    // /jsp/literature/searchEngine/index.jsp(26,1) name = method type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fform_005f0.setMethod("post");
    // /jsp/literature/searchEngine/index.jsp(26,1) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fform_005f0.setId("searchEngineForm");
    // /jsp/literature/searchEngine/index.jsp(26,1) name = namespace type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fform_005f0.setNamespace("/literature");
    int _jspx_eval_s_005fform_005f0 = _jspx_th_s_005fform_005f0.doStartTag();
    if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fform_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t<div style=\"padding-left:150px;height:32px;padding-top:10px; \">\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005ftextfield_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t  \t&nbsp;&nbsp;显示记录数:&nbsp;");
        if (_jspx_meth_s_005ftextfield_005f1(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t  \t&nbsp;&nbsp;");
        if (_jspx_meth_gl_005fbutton_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t  \t&nbsp;&nbsp;");
        if (_jspx_meth_gl_005fbutton_005f1(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t</div>\r\n");
        out.write("\t\t<div id=\"cond\" style=\"overflow: hidden;text-align:left;display:none;\">\t\t \r\n");
        out.write("\t\t <table id=\"tbl\" cellpadding=\"0\" cellspacing=\"0\" class=\"formTable\" style=\"width:600px;\" align=\"center\">\r\n");
        out.write("\t\t\t  <tr>\r\n");
        out.write("\t\t\t    <td class=\"textR\" width=\"20%\">作者： </td>  \r\n");
        out.write("\t\t\t    <td class=\"textL\"  width=\"80%\"> ");
        if (_jspx_meth_s_005ftextfield_005f2(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t    </td>\r\n");
        out.write("\t\t\t </tr>\t\r\n");
        out.write("\t\t\t <tr>  \r\n");
        out.write("\t\t\t    <td class=\"textR\" >出版物名称： </td>  \r\n");
        out.write("\t\t\t    <td class=\"textL\"> ");
        if (_jspx_meth_s_005ftextfield_005f3(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t    </td>\r\n");
        out.write("\t\t\t</tr>\t\r\n");
        out.write("\t\t\t<tr>\r\n");
        out.write("\t\t\t    <td class=\"textR\" >刊登时间： </td>  \r\n");
        out.write("\t\t\t    <td class=\"textL\" >");
        if (_jspx_meth_s_005ftextfield_005f4(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("&nbsp;至&nbsp;");
        if (_jspx_meth_s_005ftextfield_005f5(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t    </td>\r\n");
        out.write("\t\t\t</tr>\r\n");
        out.write("\t\t\t<tr>\r\n");
        out.write("\t\t\t    <td class=\"textR\" >文献类型： </td>  \r\n");
        out.write("\t\t\t    <td class=\"textL\">\t\t\t       \r\n");
        out.write("\t\t\t      ");
        if (_jspx_meth_s_005fcheckboxlist_005f0(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t    </td>\r\n");
        out.write("\t\t\t</tr>\r\n");
        out.write("\t\t    </table>\r\n");
        out.write("\t\t    <div id=\"divBtn\" style=\"position:relative;height: 30px;text-align: center;padding-top:10px;\">\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_gl_005fbutton_005f2(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\t&nbsp;\r\n");
        out.write("\t\t\t\t");
        if (_jspx_meth_gl_005fbutton_005f3(_jspx_th_s_005fform_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t</div>\r\n");
        out.write("\t</div>\r\n");
        out.write("\t\t\r\n");
        out.write("\t\t<div style=\"height:200px;\">&nbsp;</div>\r\n");
        out.write("    ");
        int evalDoAfterBody = _jspx_th_s_005fform_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fform_0026_005fnamespace_005fmethod_005fid_005faction.reuse(_jspx_th_s_005fform_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fform_0026_005fnamespace_005fmethod_005fid_005faction.reuse(_jspx_th_s_005fform_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005ftextfield_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_005ftextfield_005f0 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_005ftextfield_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftextfield_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(28,2) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f0.setName("query.parameters.text");
    // /jsp/literature/searchEngine/index.jsp(28,2) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f0.setCssStyle("width:500px;");
    int _jspx_eval_s_005ftextfield_005f0 = _jspx_th_s_005ftextfield_005f0.doStartTag();
    if (_jspx_th_s_005ftextfield_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005ftextfield_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_005ftextfield_005f1 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fvalue_005fname_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_005ftextfield_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftextfield_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(29,28) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f1.setName("record.maxresults");
    // /jsp/literature/searchEngine/index.jsp(29,28) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f1.setValue("100");
    // /jsp/literature/searchEngine/index.jsp(29,28) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f1.setCssStyle("width:50px;");
    int _jspx_eval_s_005ftextfield_005f1 = _jspx_th_s_005ftextfield_005f1.doStartTag();
    if (_jspx_th_s_005ftextfield_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fvalue_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fvalue_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f1);
    return false;
  }

  private boolean _jspx_meth_gl_005fbutton_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  gl:button
    com.golead.core.web.tag.ButtonTag _jspx_th_gl_005fbutton_005f0 = (com.golead.core.web.tag.ButtonTag) _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.get(com.golead.core.web.tag.ButtonTag.class);
    _jspx_th_gl_005fbutton_005f0.setPageContext(_jspx_page_context);
    _jspx_th_gl_005fbutton_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(30,16) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f0.setName("btnAdd");
    // /jsp/literature/searchEngine/index.jsp(30,16) name = onClick type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f0.setOnClick("goSubmit()");
    int _jspx_eval_gl_005fbutton_005f0 = _jspx_th_gl_005fbutton_005f0.doStartTag();
    if (_jspx_eval_gl_005fbutton_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_gl_005fbutton_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_gl_005fbutton_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_gl_005fbutton_005f0.doInitBody();
      }
      do {
        out.write('检');
        out.write('索');
        int evalDoAfterBody = _jspx_th_gl_005fbutton_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_gl_005fbutton_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_gl_005fbutton_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f0);
    return false;
  }

  private boolean _jspx_meth_gl_005fbutton_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  gl:button
    com.golead.core.web.tag.ButtonTag _jspx_th_gl_005fbutton_005f1 = (com.golead.core.web.tag.ButtonTag) _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.get(com.golead.core.web.tag.ButtonTag.class);
    _jspx_th_gl_005fbutton_005f1.setPageContext(_jspx_page_context);
    _jspx_th_gl_005fbutton_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(31,16) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f1.setName("btnAdd");
    // /jsp/literature/searchEngine/index.jsp(31,16) name = onClick type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f1.setOnClick("goCondition()");
    int _jspx_eval_gl_005fbutton_005f1 = _jspx_th_gl_005fbutton_005f1.doStartTag();
    if (_jspx_eval_gl_005fbutton_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_gl_005fbutton_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_gl_005fbutton_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_gl_005fbutton_005f1.doInitBody();
      }
      do {
        out.write("高级检索");
        int evalDoAfterBody = _jspx_th_gl_005fbutton_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_gl_005fbutton_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_gl_005fbutton_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005ftextfield_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_005ftextfield_005f2 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_005ftextfield_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftextfield_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(37,39) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f2.setName("query.parameters.author");
    int _jspx_eval_s_005ftextfield_005f2 = _jspx_th_s_005ftextfield_005f2.doStartTag();
    if (_jspx_th_s_005ftextfield_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftextfield_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftextfield_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005ftextfield_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_005ftextfield_005f3 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_005ftextfield_005f3.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftextfield_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(42,26) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f3.setName("query.parameters.publicationName");
    int _jspx_eval_s_005ftextfield_005f3 = _jspx_th_s_005ftextfield_005f3.doStartTag();
    if (_jspx_th_s_005ftextfield_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftextfield_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fnobody.reuse(_jspx_th_s_005ftextfield_005f3);
    return false;
  }

  private boolean _jspx_meth_s_005ftextfield_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_005ftextfield_005f4 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_005ftextfield_005f4.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftextfield_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(47,26) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f4.setName("query.parameters.startDate");
    // /jsp/literature/searchEngine/index.jsp(47,26) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f4.setCssStyle("width:100px;");
    int _jspx_eval_s_005ftextfield_005f4 = _jspx_th_s_005ftextfield_005f4.doStartTag();
    if (_jspx_th_s_005ftextfield_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f4);
    return false;
  }

  private boolean _jspx_meth_s_005ftextfield_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:textfield
    org.apache.struts2.views.jsp.ui.TextFieldTag _jspx_th_s_005ftextfield_005f5 = (org.apache.struts2.views.jsp.ui.TextFieldTag) _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.TextFieldTag.class);
    _jspx_th_s_005ftextfield_005f5.setPageContext(_jspx_page_context);
    _jspx_th_s_005ftextfield_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(47,111) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f5.setName("query.parameters.endDate");
    // /jsp/literature/searchEngine/index.jsp(47,111) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ftextfield_005f5.setCssStyle("width:100px;");
    int _jspx_eval_s_005ftextfield_005f5 = _jspx_th_s_005ftextfield_005f5.doStartTag();
    if (_jspx_th_s_005ftextfield_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ftextfield_0026_005fname_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ftextfield_005f5);
    return false;
  }

  private boolean _jspx_meth_s_005fcheckboxlist_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  s:checkboxlist
    org.apache.struts2.views.jsp.ui.CheckboxListTag _jspx_th_s_005fcheckboxlist_005f0 = (org.apache.struts2.views.jsp.ui.CheckboxListTag) _005fjspx_005ftagPool_005fs_005fcheckboxlist_0026_005fname_005flistValue_005flistKey_005flist_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.CheckboxListTag.class);
    _jspx_th_s_005fcheckboxlist_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fcheckboxlist_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(53,9) name = list type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fcheckboxlist_005f0.setList("codeSets.WORDSTYPE");
    // /jsp/literature/searchEngine/index.jsp(53,9) name = listKey type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fcheckboxlist_005f0.setListKey("value");
    // /jsp/literature/searchEngine/index.jsp(53,9) name = listValue type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fcheckboxlist_005f0.setListValue("codeName");
    // /jsp/literature/searchEngine/index.jsp(53,9) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fcheckboxlist_005f0.setName("query.parameters.wordsType");
    // /jsp/literature/searchEngine/index.jsp(53,9) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fcheckboxlist_005f0.setCssStyle("width:15px;");
    int _jspx_eval_s_005fcheckboxlist_005f0 = _jspx_th_s_005fcheckboxlist_005f0.doStartTag();
    if (_jspx_th_s_005fcheckboxlist_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fcheckboxlist_0026_005fname_005flistValue_005flistKey_005flist_005fcssStyle_005fnobody.reuse(_jspx_th_s_005fcheckboxlist_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fcheckboxlist_0026_005fname_005flistValue_005flistKey_005flist_005fcssStyle_005fnobody.reuse(_jspx_th_s_005fcheckboxlist_005f0);
    return false;
  }

  private boolean _jspx_meth_gl_005fbutton_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  gl:button
    com.golead.core.web.tag.ButtonTag _jspx_th_gl_005fbutton_005f2 = (com.golead.core.web.tag.ButtonTag) _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.get(com.golead.core.web.tag.ButtonTag.class);
    _jspx_th_gl_005fbutton_005f2.setPageContext(_jspx_page_context);
    _jspx_th_gl_005fbutton_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(58,4) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f2.setName("btnQuery");
    // /jsp/literature/searchEngine/index.jsp(58,4) name = onClick type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f2.setOnClick("goStatic()");
    int _jspx_eval_gl_005fbutton_005f2 = _jspx_th_gl_005fbutton_005f2.doStartTag();
    if (_jspx_eval_gl_005fbutton_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_gl_005fbutton_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_gl_005fbutton_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_gl_005fbutton_005f2.doInitBody();
      }
      do {
        out.write('检');
        out.write('索');
        int evalDoAfterBody = _jspx_th_gl_005fbutton_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_gl_005fbutton_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_gl_005fbutton_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f2);
    return false;
  }

  private boolean _jspx_meth_gl_005fbutton_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, javax.servlet.jsp.PageContext _jspx_page_context)
          throws java.lang.Throwable {
    javax.servlet.jsp.PageContext pageContext = _jspx_page_context;
    javax.servlet.jsp.JspWriter out = _jspx_page_context.getOut();
    //  gl:button
    com.golead.core.web.tag.ButtonTag _jspx_th_gl_005fbutton_005f3 = (com.golead.core.web.tag.ButtonTag) _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.get(com.golead.core.web.tag.ButtonTag.class);
    _jspx_th_gl_005fbutton_005f3.setPageContext(_jspx_page_context);
    _jspx_th_gl_005fbutton_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/literature/searchEngine/index.jsp(60,4) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f3.setName("btnCancel");
    // /jsp/literature/searchEngine/index.jsp(60,4) name = onClick type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_gl_005fbutton_005f3.setOnClick("goCancel()");
    int _jspx_eval_gl_005fbutton_005f3 = _jspx_th_gl_005fbutton_005f3.doStartTag();
    if (_jspx_eval_gl_005fbutton_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_gl_005fbutton_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_gl_005fbutton_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_gl_005fbutton_005f3.doInitBody();
      }
      do {
        out.write('关');
        out.write('闭');
        int evalDoAfterBody = _jspx_th_gl_005fbutton_005f3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_gl_005fbutton_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_gl_005fbutton_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fgl_005fbutton_0026_005fonClick_005fname.reuse(_jspx_th_gl_005fbutton_005f3);
    return false;
  }
}
