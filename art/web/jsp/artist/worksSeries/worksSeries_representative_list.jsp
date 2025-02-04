<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
	<title>系列代表作页面</title>
	<script language="javascript">

		var hyperlink = "../artist/ArtistWorksSeries.do";	
		var fulllink = contextpath + "artist/ArtistWorksSeries.do";

		var pageAction = '?action=REPRESENTATIVE_PAGE';
		
		function goAdd()  {
			editMode="ADD";
			var artistId = $$('artistId').value;
			var seriesId = $$('seriesId').value;
			openWindow("添加代表作品",fulllink+'?action=REPRESENTATIVE_ADD&query.parameters.artistId=' + artistId + '&query.parameters.seriesId=' + seriesId,0.8,0.8);
		}

		function renew()  {
			var order = getElement("query.order");                  order.value="";
			var desc = getElement("query.orderDirection");          desc.value="";
			var pn = getElement("query.pageNumber");                pn.value="1";
			var ps = getElement("query.pageSize");                  ps.value="10";
			goSearch();
		}

		function closedialog(ret){
			if(ret == true || ret == 'true') {
				goSearch();
			}
			editMode="";
		}

		function goDel()  {
			var id = findMultiSelected("删除",ArtistWorksSeries_grid);
			console.log(id);
			if (id == "") return;
			var res = confirm("是否要删除？");
			if (res == true){
				var data = ajaxSubmit(fulllink+"?action=REPRESENTATIVE_DELETE&ids=" + id,"ArtistWorksSeriesForm");
				console.log(data);
				if(data.exit>0){
					alert(data.message);
					return;
				}else {
					goSearch();
				}
			}
		}

		function goSearch(){
			goArtistWorksSeriesGridSearch();
		}

		function init(){
			showMessage('<s:property value="errorMessage" escape="false"/>');
			renew();
		}

	</script>

	<style type="text/css">
		html,body {  width: 100%;  height: 100%;  }
	</style>
</head>

<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="ArtistWorksSeries" id="ArtistWorksSeriesForm" method="post" namespace="/artist" enctype="multipart/form-data">
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.artistId" id="artistId"/>
			<s:hidden name="query.parameters.seriesId" id="seriesId"/>
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtistWorksSeries" page="true" form="ArtistWorksSeriesForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,系列代表作,缩略图,艺术家,作品编号,作品名称,作品英文名,尺寸(cm),媒介材料,媒介形式,签名位置,作品系列",null, headAlign);
				grid.setInitWidthsP("3,8,11,8,8,10,10,8,8,8,8,10");
				grid.setColAlign("center,center,center,center,center,center,center,center,center,center,center,center");
				grid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str,str,str,str,str,str,str,str");
               	grid.enableMultiline(true);
				grid.init();
				var gDiv = $$('ArtistWorksSeries_box');
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