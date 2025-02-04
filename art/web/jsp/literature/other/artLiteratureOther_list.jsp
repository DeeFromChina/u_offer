<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var hyperlink = contextpath + "literature/ArtLiteratureOther.do";
	var fulllink = contextpath + "literature/ArtLiteratureOther.do";

	function goAdd() {
		editMode = "ADD";
		openWindow("添加", fulllink + '?action=ADD', 0.7, 0.9);
	}

	function goDel() {
		var id = findMultiSelected("删除", ArtLiteratureOther_grid);
		if (id == "")
			return;
		var res = confirm("是否要删除？");
		if (res == true) {
			var data = ajaxSubmit(fulllink + "?action=delete&ids=" + id,
					"ArtLiteratureOtherForm");
			if (data.exit > 0) {
				alert(data.message);
				return;
			} else {
				goSearch();
			}
		}
	}

	function goModify() {
		var id = findSelected("修改", ArtLiteratureOther_grid);
		if (id == "")
			return;
		url_link = fulllink + '?action=EDIT&ids=' + id;
		editMode = "EDIT";
		openWindow("修改", url_link, 0.7, 0.9);
	}

	function view(id) {
		/*  var url_link=fulllink+'?action=VIEW&record.id='+id;
		 editMode="VIEW";
		 openWindow("查看纸媒",url_link,0.7,0.95);	 */
		url_link = fulllink + '?action=EDIT&ids=' + id;
		editMode = "EDIT";
		openWindow("修改", url_link, 0.7, 0.9);
	}

	function goSearch() {
		goArtLiteratureOtherGridSearch();
	}

	function renew() {
		var order = getElement("query.order");
		order.value = "";
		var desc = getElement("query.orderDirection");
		desc.value = "";
		var pn = getElement("query.pageNumber");
		pn.value = "1";
		var ps = getElement("query.pageSize");
		ps.value = "10";
		goSearch();
	}

	function closedialog(ret) {
		if (ret == true || ret == 'true') {
			goSearch();
		}
		editMode = "";
	}

	function init() {
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
	}
</script>

<style type="text/css">
html, body {
	width: 100%;
	height: 100%;
}
</style>
</head>
<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="ArtLiteratureOther" id="ArtLiteratureOtherForm"
			method="post" namespace="/literature" enctype="multipart/form-data">
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<table width="99%" id="tbl" cellpadding="0" cellspacing="0"
				class="queryTable">
				<tr>
					<td width="100%" style="height: 30px;" class="textL">艺术家： <s:textfield
							name="query.parameters.cName" style="width:150px;" />
						&nbsp;&nbsp;&nbsp;&nbsp; 文献标题：<s:textfield style="width:150px;"
							name="query.parameters.literatureTitle" />
						&nbsp;&nbsp;&nbsp;&nbsp; 作者：<s:textfield style="width:150px;"
							name="query.parameters.auther" />&nbsp;&nbsp; &nbsp;&nbsp;<gl:button
							name="btnQuery" onClick="goSearch()">查询</gl:button>
					</td>
				</tr>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1"
				class="controlTable">
				<tr>
					<td><gl:button styleClass="sbuBtnStyle" icon="addIcon"
							onClick="goAdd()">增加</gl:button> <gl:button
							styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button>
						<gl:button styleClass="sbuBtnStyle" icon="delIcon"
							onClick="goDel()">删除</gl:button></td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtLiteratureOther" page="true"
				form="ArtLiteratureOtherForm" property="query" cellPadding="0"
				cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,艺术家,文献标题,主编,刊登时间,出版物名称,出版社,出版物期数,相关页,附件来源",null, headAlign);
				grid.setInitWidthsP("3,8,15,10,8,12,12,12,8,12");
				grid.setColAlign("center,center,center,center,center,center,center,center,center,center");
				grid.setColTypes("ch,ro,link,ro,ro,ro,ro,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str,str,str,str,str,str");				
				grid.enableMultiline(true);
				grid.init();
				var gDiv = $$('ArtLiteratureOther_box');
			</gl:grid>
		</s:form>
	</div>
	<%@include file="/common/dialog.jsp"%>
	<script type="text/javascript">
		if (window.addEventListener) {
			window.addEventListener("resize", goResize, false);
		} else {
			window.attachEvent('onresize', goResize);
		}

		var mDiv = window.document.getElementById('mainDiv');
		var tDiv = window.document.getElementById('tbl');
		var btn = window.document.getElementById('btn');

		function goResize() {
			var he;
			if (isIE())
				he = window.document.body.offsetHeight;
			else
				he = window.innerHeight;
			mDiv.style.height = he - 42 + "px";
			gDiv.style.height = mDiv.offsetHeight - btn.offsetHeight
					- btn.offsetTop + 1 + "px";
			tDiv.style.width = mDiv.offsetWidth - 4 + "px";
			gDiv.style.width = mDiv.offsetWidth - 6 + "px";
			btn.style.width = tDiv.offsetWidth + "px";
		}
		goResize();
	</script>
</body>
</html>