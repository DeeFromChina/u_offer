<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <%@include file="/common/header.jsp" %>

	<script language="javascript">		
		var hyperlink = "../activity/activityAbmb.do";
		var fulllink = contextpath + "activity/activityAbmb.do";

		// 用于浏览该风格下的用户
		function view(id) {
			/* editMode="VIEW";
			openWindow("查看艺博会", fulllink + "?action=view&ids=" + id, 0.6, 0.5); */
			var urlink=fulllink+'?action=EDIT&ids='+id;
    		editMode="EDIT";
			openWindow("修改艺博会", urlink, 0.6, 0.8);
		}
		
		function worksView(id) {
			 url_link = fulllink+'?action=EDIT&ids='+id;
				editMode="EDIT";
			    openWindow("修改作品基本信息",url_link,0.8,0.8);
		}
   
		function goDel() {
			var id = findMultiSelected("删除",abmb_grid);
			if(id == "") return;
			var res = confirm("是否真的要删除?");
			if(res == true) {
				var data=ajaxSubmit(fulllink+"?action=delete&ids=" + id,"abmbForm");
		 		if(data.exit>0){
		 			  alert(data.message);
		 			  return;
		 		}
		 		else {	  
		 			goSearch();
		 		} 
			}
		}
		
		function goRowDblClick() {
			var btn = $$('btnModify');
			if(btn==null) {
				var id = findSelected("修改",abmb_grid);
				if(id == "") return;
				view(id);
			}
			else {
				goModify();
			}
		}
		
		function goModify() {
			var id = findSelected("修改",abmb_grid);
			if(id == "") return;
    		var urlink=fulllink+'?action=EDIT&ids='+id;
    		editMode="EDIT";
			openWindow("修改艺博会", urlink, 0.6, 0.8);
		}

		function goAdd() {
			editMode="ADD";
			openWindow("添加艺博会", fulllink+'?action=ADD', 0.6, 0.8);
		}

		function renew() {
			var order = getElement("query.order");                  order.value="";
			var desc =  getElement("query.orderDirection");         desc.value="";
			var pn =    getElement("query.pageNumber");             pn.value="1";
			var ps =    getElement("query.pageSize");               if(ps!=null) ps.value="15";
			var v0 =    getElement("query.parameters.abmbName");    v0.value="";
			var v1 =    getElement("query.parameters.sponsor");     v1.value="";
			var v2 =    getElement("query.parameters.abmbYear");    v2.value="";
			
			goSearch();
		}

		function closedialog(ret) {
			if(ret=='true' || ret==true) {
				goSearch();
			}
			if(ret == 'false' && editMode=="EXCEL"){
				goSearch();
			}
			editMode="";
		}
		
		function init(){
			renew(); 
		}

		function goSearch(){ 
			goabmbGridSearch(); 
		}

		function goExport(){
			editMode="EXCEL";
			document.forms[0].action = fulllink + "?action=EXPORT";
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}
	</script>

<style type="text/css">
   html, body {width:100%; height:100%;}
</style>
</head>

<body onload="init()">
<div class="special-padding"  id="mainDiv">  
<s:form action="activityAbmb" id="abmbForm" method="post" namespace="/activity">
<s:hidden name="query.order" />
<s:hidden name="query.orderDirection" />
<s:hidden name="query.pageNumber" />
<s:hidden name="query.recordCount" />
<s:hidden name="query.pageCount" />
	<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
	<tbody>
		<tr>
			<td class="textL">
  				&nbsp;&nbsp;名称：<s:textfield style="width:200px;" name="query.parameters.abmbName" /> 
  				&nbsp;&nbsp;主办方：<s:textfield style="width:150px;" name="query.parameters.sponsor" /> 
  				&nbsp;&nbsp;开幕年份：<s:textfield style="width:100px;" name="query.parameters.abmbYear" /> 
  				&nbsp;&nbsp;艺术家：<s:textfield style="width:100px;" name="query.parameters.artistName" />
  				
  				&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
			</td>
		</tr>
	</tbody>
	</table>
<table id="btn" width="100%" cellspacing="1" cellpadding="1" class="controlTable">
	<tr>
		<td>
				  <gl:button styleClass="sbuBtnStyle"  icon="addIcon" onClick="goAdd()">增加</gl:button>
			      <gl:button id="btnModify" styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button>
			      <gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
			      <%--<gl:button name="btnQuery" onClick="goExport()">导出</gl:button>--%>
		</td>
	</tr>	
</table>
<gl:grid styleClass="pageTurn" id="abmb" page="true" form="abmbForm"  property="query" cellPadding="0" cellSpacing="0" >
   var grid = new dhtmlXGridObject();
   grid.setImagePath(imagePath);
   var headAlign=[tLeft,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
   grid.setHeader("#master_checkbox,艺术家,名称,届数,主办方,艺术总监,开幕时间,国家,城市",null,headAlign);
   grid.setInitWidthsP("5,10,15,10,20,10,10,10,10");
   grid.setColAlign("center,center,center,center,center,center,center,center,center");
   grid.setColTypes("ch,ro,link,ro,ro,ro,ro,ro,ro");
   grid.setColSorting("na,str,str,str,str,str,str,str");
   grid.attachEvent("onRowDblClicked", goRowDblClick);  
   gridSort("false,false,false,false,false,false,false,false");
   grid.setSkin("dhx_terrace");
   grid.enableMultiline(true);
   grid.init();

   var gDiv=$$('abmb_box');
</gl:grid>
</s:form>
</div>
<%@include file="/common/dialog.jsp"%>
<%@include file="/common/resizeList1.jsp" %>
</body>
</html>