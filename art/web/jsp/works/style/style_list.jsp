<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <%@include file="/common/header.jsp" %>

	<script language="javascript">		
		var hyperlink = "../works/worksStyle.do";
		var fulllink = contextpath + "works/worksStyle.do";

		// 用于浏览该风格下的用户
		function view(id) {
			editMode="VIEW";
			openWindow("查看风格", fulllink + "?action=view&ids=" + id, 600, 300);
		}
   
		function goDel() {
			var id = findMultiSelected("删除",style_grid);
			if(id == "") return;
			var res = confirm("是否真的要删除?");
			if(res == true) {
				var data=ajaxSubmit(fulllink+"?action=delete&ids=" + id,"styleForm");
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
				var id = findSelected("修改",style_grid);
				if(id == "") return;
				view(id);
			}
			else {
				goModify();
			}
		}
		
		function goModify() {
			var id = findSelected("修改",style_grid);
			if(id == "") return;
    		var urlink=fulllink+'?action=EDIT&ids='+id;
    		editMode="EDIT";
			openWindow("修改风格", urlink, 600, 300);
		}

		function goAdd() {
			editMode="ADD";
			openWindow("添加风格", fulllink+'?action=ADD', 600, 300);
		}

		function renew() {
			var order = getElement("query.order");                  order.value="";
			var desc =  getElement("query.orderDirection");         desc.value="";
			var pn =    getElement("query.pageNumber");             pn.value="1";
			var ps =    getElement("query.pageSize");               if(ps!=null) ps.value="15";
			var v0 =    getElement("query.parameters.styleName");    v0.value="";
			goSearch();
		}

		function closedialog(ret) {
			if(ret=='true'||ret==true) {
				goSearch();
			}	
			editMode="";
		}
		
		function init(){
			renew(); 
		}

		function goSearch(){ 
			gostyleGridSearch(); 
		}
	</script>

<style type="text/css">
   html, body {width:100%; height:100%;}
</style>
</head>

<body onload="init()">
<div class="special-padding"  id="mainDiv">  
<s:form action="worksStyle" id="styleForm" method="post" namespace="/works">
<s:hidden name="query.order" />
<s:hidden name="query.orderDirection" />
<s:hidden name="query.pageNumber" />
<s:hidden name="query.recordCount" />
<s:hidden name="query.pageCount" />
	<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
	<tbody>
		<tr>
			<td class="textL">
  &nbsp;&nbsp;风格名称：<s:textfield style="width:200px;" name="query.parameters.styleName" /> 
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
<gl:grid styleClass="pageTurn" id="style" page="true" form="styleForm"  property="query" cellPadding="0" cellSpacing="0" >
   var grid = new dhtmlXGridObject();
   grid.setImagePath(imagePath);
   var headAlign=[tLeft,tCenter,tCenter,tCenter,tCenter];
   grid.setHeader("#master_checkbox,风格名称,风格描述,创建人,创建时间",null,headAlign);
   grid.setInitWidthsP("5,35,40,10,10");
   grid.setColAlign("center,left,left,center,center");
   grid.setColTypes("ch,link,ro,ro,ro");
   grid.setColSorting("na,str,str,str,str");
   grid.attachEvent("onRowDblClicked", goRowDblClick);  
   gridSort("false,false,false,false,false");
   grid.setSkin("dhx_terrace");
   grid.init();

   var gDiv=$$('style_box');
</gl:grid>
</s:form>
</div>
<%@include file="/common/dialog.jsp"%>
<%@include file="/common/resizeList1.jsp" %>
</body>
</html>