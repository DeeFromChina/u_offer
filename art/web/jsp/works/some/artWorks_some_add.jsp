<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "works/artSome.do";
	var hyperlink = contextpath + "works/artSome.do";
	
	function closedialog(ret) {
		if(ret=='true') {
			goSearch();
		}	
		editMode="";
	}
	
	function init() {
		showMessage('<s:property value="errorMessage" escape="false"/>');
		goSearch();
	}

	function goSearch() {
		var data=ajaxSubmit(fulllink+"?action=ADDPAGE","artSomeForm");
		 if(data.exit>0){
			  alert(data.message);
			  return;
		  }else{
			  grid.clearAll();
			  grid.parse(data,'json');
			  grid.checkAll(false);
		  }
	}
	
	function goSubmit() {
		var worksId = $$("worksId").value;
		var id = findMultiSelected("增加",grid);
		if (id == "") return;
		    var url =fulllink+"?action=SAVE&ids=" + id;
		  	document.forms[0].action=url;
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
		<s:form action="artSome" id="artSomeForm" method="post" namespace="/works">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="query.parameters.orderBy" id="orderBy" value="false"/>
			<s:hidden name="record.worksId" id="worksId"/>
			<s:hidden name="record.artistId" id="artistId"/>
			<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tbody>
					<tr>
						<td class="textL" style="width: 80%">
						&nbsp;&nbsp;作者：<s:textfield style="width:100px;" name="query.parameters.artist" />
						&nbsp;&nbsp;作品名称：<s:textfield style="width:100px;" name="query.parameters.name" /> 
						&nbsp;&nbsp;风格类型：<s:textfield style="width:100px;" name="query.parameters.styleType" />
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSubmit()">添加</gl:button>
						</td>
					</tr>
				</tbody>
			</table>
			<div id="artSome_box" style="background-color: white;"></div>
		</s:form>
	</div>
<%@include file="/common/dialog.jsp"%>

<script type="text/javascript">
	var grid = new dhtmlXGridObject('artSome_box');
	grid.setImagePath(imagePath);
	var headAlign=[tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
	grid.setHeader("#master_checkbox,作者,作品名称,风格类型,作品系列,创作时间",null,headAlign);
	grid.setInitWidthsP("5,15,30,15,20,15");
	grid.setColAlign("center,center,center,center,center,center");
	grid.setColTypes("ch,ro,ro,ro,ro,ro");
	grid.setColSorting("str,str,str,str,str,str");
	grid.setSkin("dhx_terrace");
	grid.init();
	var gDiv=$$('artSome_box');
</script>
<script type="text/javascript">
	 var mainbody=window.document.body;
    if(window.addEventListener) {
 	     window.addEventListener("resize",goResize,false); 
    }
    else {
 	     window.attachEvent('onresize',goResize);
    }
    
    var mDiv=window.document.getElementById('mainDiv');
    var btbl=window.document.getElementById('tbl');
    function goResize(){
    	mDiv.style.width=mainbody.offsetWidth-mDiv.offsetLeft*2-4+"px";
       mDiv.style.height=mainbody.offsetHeight-mDiv.offsetTop*2-8+"px";
       
       gDiv.style.height=mDiv.offsetHeight-btbl.offsetHeight+"px";
		gDiv.style.width=mDiv.offsetWidth-4+"px";
       if(btbl)btbl.style.width=mDiv.clientWidth-2+"px";
    }
    goResize();   
</script> 
</body>
</html>