<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "publication/artPublication.do";
	var hyperlink = contextpath + "publication/artPublication.do";
	function closedialog(ret) {
		if(ret=='true' || ret==true) {
			goSearch();
		}	
		if(ret == 'false' && editMode=="EXCEL"){
			goSearch();
		}
		editMode="";
	}
	
	function renew()  {
		var order = getElement("query.order");                  order.value="";
		var desc = getElement("query.orderDirection");          desc.value="";
		var pn = getElement("query.pageNumber");                pn.value="1";
		var ps = getElement("query.pageSize");                  ps.value="15";
	    goSearch();
	}

	function init() {
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
	}

    function goSelect(){
	    var id = findSelected("选择出版物",artPublication_grid);
	    if(id=="")return;
	    var name = artPublication_grid.cellById(id,2).getValue();
	    var ret = new Object();
	    ret['ret'] = 'publication';
	    ret['str1'] = id;
	    ret['str2'] = name.split('^')[0];
	    parent.closedialog(ret);
	}

	function goSearch() {
		goartPublicationGridSearch();
	}
	
	function goAdd()  {
		editMode="ADD";
	    openWindow("增加出版物信息",fulllink+'?action=ADD',0.8,0.85);
	}
	
	function goModify(){
		var id = findSelected("修改",artPublication_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改出版物信息",url_link,0.8,0.85);  
	}
	
	function view(id) {
	    url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改出版物信息",url_link,0.8,0.85);  
	}
	
	function goDel(val) {
		var id = findMultiSelected("删除",artPublication_grid);
		if (id == "") return;
		var res = confirm("是否真的要删除?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=DELETE&ids=" + id,"artPublicationForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
		
	}

	function goExport(){
		editMode="EXCEL";
		document.forms[0].action = fulllink + "?action=EXPORT";
		document.forms[0].target = "_self";
		document.forms[0].submit();
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
		<s:form action="artPublication" id="artPublicationForm" method="post" namespace="/publication">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.selectType" id="selectType" />
			<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tbody>
					<tr>
						<td class="textL" style="width: 80%">
						&nbsp;&nbsp;艺术家：<s:textfield style="width:100px;" name="query.parameters.artistName" /> 
						&nbsp;&nbsp;出版物名称：<s:textfield style="width:100px;" name="query.parameters.publicationName" /> 
						&nbsp;&nbsp;出版年份：<s:textfield style="width:100px;" name="query.parameters.publicationYear" /> 
						&nbsp;&nbsp;出版物类型：<s:select style="width:80px;" list="codeSets.PUBLI_TYPE" listKey="value" listValue="codeName" name="query.parameters.publicationType"></s:select>
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
						</td>
					</tr>
				</tbody>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<s:if test="query.parameters.type == 1">
							<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goSelect()">确认</gl:button>
						</s:if>
						<s:else>
							<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button>
							<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button>
							<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
							<%--<gl:button name="btnQuery" onClick="goExport()">导出</gl:button>--%>
						</s:else>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="artPublication" page="true" form="artPublicationForm" property="query" cellPadding="0" cellSpacing="0">
			var grid = new dhtmlXGridObject();
			grid.setImagePath(imagePath);
			var headAlign=[tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
			grid.setHeader("#master_checkbox,出版物类型,出版物名称,出版机构,出版时间,编辑信息,发行量,印次,总页数,价格",null,headAlign);
			grid.setInitWidthsP("5,8,15,15,10,15,8,8,8,8");
			grid.setColAlign("center,center,center,center,center,center,center,center,center,center");
			grid.setColTypes("ch,ro,link,ro,ro,ro,ro,ro,ro,ro");
			grid.setColSorting("str,str,str,str,str,str,str,str,str,str");
			grid.setSkin("dhx_terrace");
            grid.enableMultiline(true);
			grid.init();
			var gDiv=$$('artPublication_box');
			</gl:grid>
		</s:form>
	</div>
<%@include file="/common/dialog.jsp"%>
<%@include file="/common/resizeList1.jsp" %>
</body>
</html>