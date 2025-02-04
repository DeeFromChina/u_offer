<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <%@include file="/common/header.jsp" %>

	<script language="javascript">		
		var hyperlink = "../publication/pubLiterature.do";
		var fulllink = contextpath + "publication/pubLiterature.do";
		var fulllink_literature = contextpath + "literature/literatureWords.do";

		// 用于浏览该题材下的用户
		function view(id) {
			var urlink=contextpath + "literature/ArtLiteratureWords.do"+'?action=VIEW&record.id='+id;
    		editMode="EDIT";
			openWindow("查看相关文章", urlink, 0.7,0.9);
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
		
		function goModify() {
			var id = findSelected("修改",pub_grid);
			if(id == "") return;
    		var urlink=fulllink+'?action=EDIT&ids='+id;
    		editMode="EDIT";
			openWindow("修改相关文章", urlink, 0.4,0.2);
		}

		function goAdd() {
			editMode="ADD";
			openWindow("添加相关文章", fulllink+'?action=ADD&record.pubId='+$$("pubId").value, 0.4,0.2);
		}

		function renew() {
			var order = getElement("query.order");                  order.value="";
			var desc =  getElement("query.orderDirection");         desc.value="";
			var pn =    getElement("query.pageNumber");             pn.value="1";
			var ps =    getElement("query.pageSize");               if(ps!=null) ps.value="15";
			var v0 =    getElement("query.parameters.publicationName");    v0.value="";
			goSearch();
		}

		function closedialog(ret) {
			if(ret=='true' || ret==true) {
				goSearch();
			}	
			editMode="";
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
<s:form action="pubLiterature" id="publicationForm" method="post" namespace="/publication">
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
  &nbsp;&nbsp;文献名称：<s:textfield style="width:200px;" name="query.parameters.publicationName" /> 
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
		</td>
	</tr>	
</table>
<gl:grid styleClass="pageTurn" id="pub" page="true" form="publicationForm"  property="query" cellPadding="0" cellSpacing="0" >
   var grid = new dhtmlXGridObject();
   grid.setImagePath(imagePath);
   var headAlign=[tLeft,tCenter,tCenter,tCenter,tCenter];
   grid.setHeader("#master_checkbox,文献名称,作者,刊登时间,相关页码",null,headAlign);
   grid.setInitWidthsP("5,50,15,15,15");
   grid.setColAlign("center,left,left,center,center");
   grid.setColTypes("ch,link,ro,ro,ro");
   grid.setColSorting("na,str,str,str,str");
   gridSort("false,false,false,false,false");
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