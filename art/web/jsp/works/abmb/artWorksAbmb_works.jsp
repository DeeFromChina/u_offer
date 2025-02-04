<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <%@include file="/common/header.jsp" %>

	<script language="javascript">		
	var hyperlink = "../works/ArtWorksAbmb.do";
	var fulllink = contextpath + "works/ArtWorksAbmb.do";
	var fulllink_exhibit = contextpath + "activity/activityAbmb.do";

		// 用于浏览该题材下的用户
		function worksView(id) {
			url_link = contextpath + "works/artWorks.do"+'?action=EDIT&ids='+id;
		    openWindow("修改作品基本信息",url_link,0.8,0.8);  
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
				var id = ret['str1'];
				var data=ajaxSubmit(fulllink+"?action=SAVEWORKS&ids=" + id,"publicationForm");
			}
 			goSearch();
		}

		function goAdd() {
			var url_link = contextpath + "works/artWorks.do?query.parameters.type=2&query.parameters.selectType=m";
		    openWindow("选择作品",url_link,0.8,0.8);	
		}

		function renew() {
			var order = getElement("query.order");                  order.value="";
			var desc =  getElement("query.orderDirection");         desc.value="";
			var pn =    getElement("query.pageNumber");             pn.value="1";
			var ps =    getElement("query.pageSize");               if(ps!=null) ps.value="5";
			goSearch();
		}
		
		function init(){
			renew(); 
			hide();
		}

		function hide(){
			if(document.getElementById('hide').value==1){
				$(".controlTable").each(function(){
					this.style.display="none";
				});
			}
			if(document.getElementById('hide').value==1){
				$(".queryTable").each(function(){
					this.style.display="none";
				});
			}
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
<s:hidden name="record.hide" id="hide"/>
<s:hidden name="query.order" />
<s:hidden name="query.orderDirection" />
<s:hidden name="query.pageNumber" />
<s:hidden name="query.recordCount" />
<s:hidden name="query.pageCount" />
<s:hidden name="record.type" id="type"/>
<s:hidden name="record.abmbId" id="abmbId" />
	<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
	<tbody>
		<tr>
			<td class="textL">
  &nbsp;&nbsp;名称：<s:textfield style="width:200px;" name="query.parameters.worksName" /> 
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
   var headAlign=[tLeft,tCenter,tCenter,tCenter,tCenter,tCenter];
   grid.setHeader("#master_checkbox,作品编号,艺术家,作品中文名,作品英文名,创作时间",null,headAlign);
   grid.setInitWidthsP("5,25,20,15,15,20");
   grid.setColAlign("center,center,center,center,center,center");
   grid.setColTypes("ch,link,ro,ro,ro,ro");
   grid.setColSorting("na,str,str,str,str,str");
   gridSort("false,false,false,false,false,false");
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