<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
   <%@include file="/common/header.jsp" %>

	<script language="javascript">		
		var hyperlink = "../activity/ArtActivityExhibitArtist.do";
		var fulllink = contextpath + "activity/ArtActivityExhibitArtist.do";
		var fulllink_artist = contextpath + "artist/ArtistInformation.do";

		// 用于浏览该题材下的用户
		function view(id) {
			/* editMode="VIEW";
			openWindow("查看艺术家", fulllink_artist + "?action=VIEW&record.id=" + id, 0.8, 0.8); */
			url_link = fulllink_artist+'?action=EDIT&ids='+id;
			editMode="EDIT";
		    openWindow("修改艺术家信息",url_link,0.8,0.8);
		}
   
		function goDel() {
			var id = findMultiSelected("删除",pub_grid);
			if(id == "") return;
			var res = confirm("是否真的要删除?");
			if(res == true) {
				var data=ajaxSubmit(fulllink+"?action=DELETE&ids=" + id,"publicationForm");
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
			goSearch();
			editMode="";
		}

		function goAdd() {
			editMode="ADD";
			var exhibitId = $$('exhibitId').value;
			var url_link = contextpath + "activity/ArtActivityExhibitArtist.do?action=ADD&record.exhibitId=" + exhibitId;
		    openWindow("选择艺术家",url_link,0.8,0.8);	
		}

		function renew() {
			var order = getElement("query.order");                  order.value="";
			var desc =  getElement("query.orderDirection");         desc.value="";
			var pn =    getElement("query.pageNumber");             pn.value="1";
			var ps =    getElement("query.pageSize");               if(ps!=null) ps.value="15";
			goSearch();
		}
		
		function init(){
			renew(); 
			hide();
		}

		function goSearch(){ 
			gopubGridSearch();
			
		}
		
		function hide(){
			var hide = $$('hide').value;
			if(hide=='1'){
				document.getElementById('tbl').style.display="none";
			}
		}
	</script>

<style type="text/css">
   html, body {width:100%; height:100%;}
</style>
</head>

<body onload="init()">
<div class="special-padding"  id="mainDiv">  
<s:form action="pubWorks" id="publicationForm" method="post">
<s:hidden name="query.parameters.exhibitId" id="exhibitId"/>
<s:hidden name="record.hide" id="hide"/>
<s:hidden name="query.order" />
<s:hidden name="query.orderDirection" />
<s:hidden name="query.pageNumber" />
<s:hidden name="query.recordCount" />
<s:hidden name="query.pageCount" />
	<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
	<tbody>
		<tr>
			<td class="textL">
				  <gl:button styleClass="sbuBtnStyle"  icon="addIcon" onClick="goAdd()">增加</gl:button>
			      <gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
			</td>
			<td class="textR">
  &nbsp;&nbsp;艺术家：<s:textfield style="width:200px;" name="query.parameters.name" /> 
  &nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
			</td>
		</tr>
	</tbody>
	</table>
<gl:grid styleClass="pageTurn" id="pub" page="true" form="publicationForm"  property="query" cellPadding="0" cellSpacing="0" >
   var grid = new dhtmlXGridObject();
   grid.setImagePath(imagePath);
   var headAlign=[tLeft,tCenter,tCenter,tCenter];
   grid.setHeader("#master_checkbox,艺术家,性别,国籍",null,headAlign);
   grid.setInitWidthsP("5,30,30,35");
   grid.setColAlign("center,center,center,center");
   grid.setColTypes("ch,link,ro,ro");
   grid.setColSorting("na,str,str,str");
   gridSort("false,false,false,false,false");
   grid.setSkin("dhx_terrace");
   grid.init();

   var gDiv=$$('pub_box');
</gl:grid>
</s:form>
</div>
<%@include file="/common/dialog.jsp"%>
<script type="text/javascript">
    if(window.addEventListener) {
 	     window.addEventListener("resize",goResize,false); 
    }
    else {
 	     window.attachEvent('onresize',goResize);
    }
    
	 var gDiv=window.document.getElementById('pub_box');
	 var mDiv=window.document.getElementById('mainDiv');
	 var tDiv=window.document.getElementById('tbl');
	    
    function goResize(){
   	 var he;
	    if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;

		 mDiv.style.height=he-42+"px";
		 gDiv.style.height=mDiv.offsetHeight+1+"px";
		 tDiv.style.width=mDiv.offsetWidth-4+"px";
		 gDiv.style.width=mDiv.offsetWidth-6+"px";
    }
    goResize();   
</script> 
</body>
</html>