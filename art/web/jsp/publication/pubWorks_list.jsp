﻿<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <%@include file="/common/header.jsp" %>

	<script language="javascript">		
		var hyperlink = "../publication/pubWorks.do";
		var fulllink = contextpath + "publication/pubWorks.do";
		var fulllink_works = contextpath + "works/artWorks.do";

		// 用于浏览该题材下的用户
		function view(id) {
			editMode="VIEW";
			openWindow("查看作品", fulllink_works + "?action=view&record.id=" + id, 1, 1);
		}
   
		function goDel() {
			var id = findMultiSelected("删除",pub_grid);
			if(id == "") return;
			var res = confirm("是否真的要删除?");
			if(res == true) {
				var data=ajaxSubmit(fulllink+"?action=delete&ids=" + id,"publicationForm");
		 		if(data.exit>0){
		 			  alert(data.message);
		 			  return;
		 		}
		 		else {	  
		 			goSearch();
		 		} 
			}
		}
		
		function closedialog(ret) {
			var res=ret['ret'];
			if(res==true) {
				if(editMode=="ADD") {
					var data=ajaxSubmit(fulllink+"?action=SAVE&ids=" +  ret['str1'],"publicationForm");
			 		if(data.exit>0){
			 			  alert(data.message);
			 			  return;
			 		}
			 		else {	  
			 			goSearch();
			 		} 
				}
			}
			editMode="";
		}

		function goAdd() {
			editMode="ADD";
			var url_link = contextpath + "works/artWorks.do?query.parameters.type=2&query.parameters.selectType=m";
		    openWindow("选择作品",url_link,0.8,0.8);	
		}

		function renew() {
			var order = getElement("query.order");                  order.value="";
			var desc =  getElement("query.orderDirection");         desc.value="";
			var pn =    getElement("query.pageNumber");             pn.value="1";
			var ps =    getElement("query.pageSize");               if(ps!=null) ps.value="15";
			var v0 =    getElement("query.parameters.worksName");    v0.value="";
			goSearch();
		}
		
		function init(){
			renew(); 
		}

		function goSearch(){ 
			gopubGridSearch(); 
		}
	</script>

<style type="text/css">
   html, body {width:100%; height:100%;}
</style>
</head>

<body onload="init()">
<div class="special-padding"  id="mainDiv">  
<s:form action="pubWorks" id="publicationForm" method="post" namespace="/publication">
<s:hidden name="query.order" />
<s:hidden name="query.orderDirection" />
<s:hidden name="query.pageNumber" />
<s:hidden name="query.recordCount" />
<s:hidden name="query.pageCount" />
<s:hidden name="query.parameters.pubId" id="pubId" />
	<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
	<tbody>
		<tr>
			<td class="textL">
  &nbsp;&nbsp;作品名称：<s:textfield style="width:200px;" name="query.parameters.worksName" /> 
  &nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
			</td>
		</tr>
	</tbody>
	</table>
<table id="btn" width="100%" cellspacing="1" cellpadding="1" class="controlTable">
	<tr>
		<td>
				  <gl:button styleClass="sbuBtnStyle"  icon="addIcon" onClick="goAdd()">增加</gl:button>
			      <gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
		</td>
	</tr>	
</table>
<gl:grid styleClass="pageTurn" id="pub" page="true" form="publicationForm"  property="query" cellPadding="0" cellSpacing="0" >
   var grid = new dhtmlXGridObject();
   grid.setImagePath(imagePath);
   var headAlign=[tLeft,tCenter,tCenter,tCenter];
   grid.setHeader("#master_checkbox,作品名称,创作年份,尺寸(cm)",null,headAlign);
   grid.setInitWidthsP("5,60,15,20");
   grid.setColAlign("center,center,center,center");
   grid.setColTypes("ch,link,ro,ro");
   grid.setColSorting("na,str,str,str");
   gridSort("false,false,false,false");
   grid.setSkin("dhx_terrace");
   grid.init();

   var gDiv=$$('pub_box');
</gl:grid>
</s:form>
</div>
<%@include file="/common/dialog.jsp"%>
<%@include file="/common/resizeList1.jsp" %>
</body>
</html>