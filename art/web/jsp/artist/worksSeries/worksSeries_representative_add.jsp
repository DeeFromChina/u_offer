<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
	<title>系列作品--在系列里但是又不是代表作的作品</title>
	<script language="javascript">

		var hyperlink = "../artist/ArtistWorksSeries.do";
		var fulllink = contextpath + "artist/ArtistWorksSeries.do";

		var pageAction = '?action=SERIES_WORKS_PAGE';
		
		function goAdd()  {
			var artistId = $$('artistId').value;
			var seriesId = $$('seriesId').value;
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

		function goSearch(){
			goArtistWorksSeriesGridSearch();
		}

		function init(){
			showMessage('<s:property value="errorMessage" escape="false"/>');
			renew();
		}

		function goCancel() {
			parent.closedialog(false);
		}

		function goSubmit() {
			var id = findMultiSelected("保存", ArtistWorksSeries_grid);
			if(id == "") return;
			var form = document.forms[0];
			form.action = fulllink + '?action=REPRESENTATIVE_SAVE&form.record.seriesId='+$$('seriesId').value+'&ids=' + id;
			form.submit();
		}
	</script>

	<style type="text/css">
		html,body {width: 100%;height: 100%;}
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
					<td class="textL">
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goSubmit()">添加到系列</gl:button>
						<gl:button styleClass="sbuBtnStyle"  icon="delIcon" onClick="goCancel()">取消</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtistWorksSeries" page="true" form="ArtistWorksSeriesForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,系列名称,缩略图,作品名称（中文）,作品名称（英文）",null, headAlign);
				grid.setInitWidthsP("5,25,35,20,15");
				grid.setColAlign("center,center,center,center,center");
				grid.setColTypes("ch,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str");
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

		var gDiv=window.document.getElementById('ArtistWorksSeries_box');
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