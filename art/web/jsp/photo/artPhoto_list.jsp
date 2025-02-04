<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">  
	var hyperlink = contextpath + "artist/ArtPhoto.do";
	var fulllink = contextpath + "artist/ArtPhoto.do";		
      
	function goAdd()  {
		editMode="ADD";
	    openWindow("添加照片",fulllink+'?action=ADD&record.pubId='+$$("pubId").value+'&ids='+$$("artistId2").value+'&record.type='+$$("type").value,0.7,0.6);
	}
	
	function view(id) {
	       var url_link=fulllink+'?action=VIEW&ids='+id;
	       editMode="VIEW";
	       openWindow("查看照片",url_link,0.7,0.7);	
	}
	
	function goModify(){
		var id = findSelected("修改",ArtPhoto_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id+'&record.type='+$$("type").value;
		editMode="EDIT";
	    openWindow("修改照片",url_link,0.7,0.7);	  
	}
	
	function modify(id){
		var pubId = $$('pubId').value;
		url_link = fulllink+'?action=EDIT&record.id='+id+"&record.publicationId="+pubId;
		editMode="EDIT";
	    openWindow("修改照片",url_link,0.7,0.7);	  
	}

	function goDel()  {
		var id = findMultiSelected("删除",ArtPhoto_grid);
		if (id == "") return;
		var res = confirm("是否要删除？");
		if (res == true){
			var data = ajaxSubmit(fulllink+"?action=delete&ids=" + id,"ArtPhotoForm");
			if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}else {	  
	 			goSearch();
	 		} 
		}
	}
	
	function goImport() {
		url_link = fulllink+'?action=IMPORT';
		openWindow("导入照片",url_link,0.5,0.5);
	}
	
	function downloadTemp() {
		document.forms[0].action = fulllink + "?action=DOWNLOAD";
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
	
	function renew()  {
		var order = getElement("query.order");                  order.value="";
		var desc = getElement("query.orderDirection");          desc.value="";
		var pn = getElement("query.pageNumber");                pn.value="1";
		var ps = getElement("query.pageSize");                  ps.value="10";
	    goSearch();
	}

	function closedialog(ret){
		var res=ret['ret'];
		if(res==true) {
	    	 getElement('query.parameters.artArtistId').value=ret['str'];
	    	 getElement('query.parameters.artArtistName').value=ret['str1'];
	     }
	    if(ret==true || ret=='true') {
	   		goSearch();
	    }
		editMode="";
	}
	
	function goSearch(){
		goArtPhotoGridSearch(); 
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
	} 

	function goArtArtist(){
		var url_link=contextpath+'artist/ArtPhoto.do?action=ARTIST&ids='+$$("artArtistId").value;
	    openWindow("选择艺术家",url_link,200,300);	
	}
	
	function clear(name){
		$$(name).value="";
	}
	
</script>

<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
}
a img{ border:none}
</style>
</head>

<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="ArtPhoto" id="ArtPhotoForm" method="post" namespace="/artist" enctype="multipart/form-data">
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.pubId" id="pubId"/>
			<s:hidden name="query.parameters.type" id="type"/>
			<s:hidden name="query.parameters.artistId2" id="artistId2"/>
			
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
		 		<tr>
					<td width="100%" style="height:30px;" class="textL">&nbsp;&nbsp;
						&nbsp;&nbsp; 艺术家：
						<s:textfield name="query.parameters.artArtistName" style="width:150px;" onchange="clear('artArtistId')"/>
						&nbsp;&nbsp; 类型：<s:select list="codeSets.PHOTO_TYPE" listKey="value" listValue="codeName" name="query.parameters.photoType"/>&nbsp;&nbsp;
						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
					</td>
				</tr>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtPhoto" page="true" form="ArtPhotoForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,照片,名称,艺术家,时间,地点,描述,图片来源,类别",null, headAlign);
				grid.setInitWidthsP("5,10,15,10,10,15,15,10,10");
				grid.setColAlign("center,center,center,center,center,center,center,center,center");
				grid.setColTypes("ch,ro,link,ro,ro,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str,str,str,str,str");
                grid.enableMultiline(true);
				grid.init();
				var gDiv = $$('ArtPhoto_box');
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
    
	 var gDiv=window.document.getElementById('ArtPhoto_box');
	 var mDiv=window.document.getElementById('mainDiv');
	 var tDiv=window.document.getElementById('tbl');
	 var btn=window.document.getElementById('btn');
	    
    function goResize(){
   	 var he;
	    if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;

		 mDiv.style.height=he-42+"px";
		 gDiv.style.height=mDiv.offsetHeight-btn.offsetHeight-btn.offsetTop+1+"px";
		 tDiv.style.width=mDiv.offsetWidth-4+"px";
		 gDiv.style.width=mDiv.offsetWidth-6+"px";
	    btn.style.width=tDiv.offsetWidth+"px";
    }
    goResize();   
</script> 

</body>
</html>