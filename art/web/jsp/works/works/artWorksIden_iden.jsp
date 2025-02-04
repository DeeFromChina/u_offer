<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "works/artWorksIden.do";
	var hyperlink = contextpath + "works/artWorksIden.do";
	var artistlink = contextpath + "artist/ArtistInformation.do";
	var workslink = contextpath + "works/artWorks.do";
	
	function renew()  {
		var order = getElement("query.order");                  order.value="";
		var desc = getElement("query.orderDirection");          desc.value="";
		var pn = getElement("query.pageNumber");                pn.value="1";
		var ps = getElement("query.pageSize");                  ps.value="10";
	    goSearch();
	}

	function init() {
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
	}

	function goSearch() {
		goartWorksGridSearch();
	}
	
	
	function artistview(id) {
		url_link = artistlink+'?action=EDIT&ids='+id;
	    editMode="VIEW";
	    openWindow("艺术家信息",url_link,0.8, 0.8);
	}
	
	function workview(id) {
	    url_link = workslink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品基本信息",url_link,0.99,0.99);  
	}
	
	
	function goTrue(){
		var id = findMultiSelected("重复",artWorks_grid);
		if (id == "") return;
		var res = confirm("是否确认要加重复标记?");
		if(res == true) {
			var data=ajaxSubmit(workslink+"?action=ISSAME&ids=" + id +"&record.issame=1","artWorksForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
	}

	function goFalse(){
		var id = findMultiSelected("取消重复",artWorks_grid);
		if (id == "") return;
		var res = confirm("是否确认要取消重复标记?");
		if(res == true) {
			var data=ajaxSubmit(workslink+"?action=ISSAME&ids=" + id +"&record.issame=0","artWorksForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
	}
</script>

<style type="text/css">
html, body {
	width: 100%;
	height: 100%;
}
a img{ border:none}
</style>
</head>

<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="artWorks" id="artWorksForm" method="post" namespace="/artist">
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.artistId" id="artistId" />
			<s:hidden name="query.parameters.name" id="name" />
			<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tbody>
					<tr>
						<td class="textL" style="width: 80%">
						&nbsp;&nbsp;作品编号：<s:textfield style="width:100px;" name="query.parameters.code" /> 
  						&nbsp;&nbsp;<s:checkboxlist list="codeSets.CONDITION" listKey="value" listValue="codeName" name="record.condition"/>
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
						</td>
					</tr>
				</tbody>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<gl:button styleClass="sbuBtnStyle"  code="A200105" icon="delIcon" onClick="goTrue()">加重复标记</gl:button>
						<gl:button styleClass="sbuBtnStyle"  code="A200106" icon="delIcon" onClick="goFalse()">取消重复标记</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="artWorks" page="true" form="artWorksForm" property="query" cellPadding="0" cellSpacing="0">
			var grid = new dhtmlXGridObject();
			grid.setImagePath(imagePath);
			var headAlign=[tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
			grid.setHeader("#master_checkbox,是否</br>处理,是否</br>假画,是否</br>重复,作品缩略图,作品编号,艺术家,作品名称,英文名称,创作时间,尺寸(cm),尺寸(in),尺寸(rule),签名位置,签名内容,媒介材料,媒介形式,作品系列,创作地点(国家),风格类型,拍卖时间,拍卖行,最低估价(元),最高估价(元),成交价(元),交易价(元)",null,headAlign);
			grid.setInitWidths("40,60,60,60,120,90,80,150,150,100,120,120,120,100,100,100,100,100,100,100,100,100,150,100,100,100");
			grid.setColAlign("center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center");
			grid.setColTypes("ch,ro,ro,ro,ro,ro,link,link,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
			grid.setColSorting("str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str");
			grid.setSkin("dhx_terrace");
 			grid.enableMultiline(true);
			grid.init();
			var gDiv=$$('artWorks_box');

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
    
	 var gDiv=window.document.getElementById('artWorks_box');
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