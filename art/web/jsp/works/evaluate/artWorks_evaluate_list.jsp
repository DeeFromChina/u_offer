<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "works/artEvaluate.do";
	var hyperlink = contextpath + "works/artEvaluate.do";
	function closedialog(ret) {
		if(ret=='true' || ret==true) {
			goSearch();
		}	
		editMode="";
	}
	
	function renew()  {
		var order = getElement("query.order");                  order.value="";
		var desc = getElement("query.orderDirection");          desc.value="";
		var pn = getElement("query.pageNumber");                pn.value="1";
		var ps = getElement("query.pageSize");                  ps.value="10";
	    goSearch();
	}

	function init() {
		showMessage('<s:property value="errorMessage" escape="false"/>');
		hide();
		renew();
	}

	function hide(){
		if(document.getElementById('hide').value==1){
			$(".controlTable").each(function(){
				this.style.display="none";
			});
		}
	}
	
	function goSearch() {
		goartEvaluateGridSearch();
	}
	
	function goAdd()  {
		var artistId = $$("artistId").value;
		var worksId = $$("id").value;
		editMode="ADD";
	    openWindow("意义与评价",fulllink+'?action=ADD&record.artistId='+artistId+"&record.worksId="+worksId,0.4,0.4);
	}
	
	function goModify(){
		var artistId = $$("artistId").value;
		var id = findSelected("修改",artEvaluate_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id+"&record.artistId="+artistId;
		editMode="EDIT";
	    openWindow("意义与评价",url_link,0.4,0.4);  
	}
	
	function goDel(val) {
		var id = findMultiSelected("删除",artEvaluate_grid);
		if (id == "") return;
		var res = confirm("是否真的要删除?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=DELETE&ids=" + id,"artEvaluateForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
		
	}
	
	function goCel() {
		var id = $$('id').value;
		var url_link=fulllink+'?action=GOEXCEL&record.id='+id;
	    openWindow("导入",url_link,0.4,0.2);
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
		<s:form action="artEvaluate" id="artEvaluateForm" method="post" namespace="/works">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.hide" id="hide"/>
			<s:hidden name="record.artistId" id="artistId"/>
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goCel()">导入</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="artEvaluate" page="true" form="artEvaluateForm" property="query" cellPadding="0" cellSpacing="0">
			var grid = new dhtmlXGridObject();
			grid.setImagePath(imagePath);
			var headAlign=[tCenter,tCenter,tCenter,tCenter];
			grid.setHeader("#master_checkbox,评价类型,评价",null,headAlign);
			grid.setInitWidthsP("15,45,40");
			grid.setColAlign("center,center,center");
			grid.setColTypes("ch,ro,ro");
			grid.setColSorting("str,str,str");
			grid.setSkin("dhx_terrace");
			grid.init();
			var gDiv=$$('artEvaluate_box');
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
    
	 var gDiv=window.document.getElementById('artEvaluate_box');
	 var mDiv=window.document.getElementById('mainDiv');
	 var btn=window.document.getElementById('btn');
	    
    function goResize(){
   	 var he;
	    if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;

		 mDiv.style.height=he-42+"px";
		 var hide2=window.document.getElementById('hide');
		 if(hide2.value==1){
			 gDiv.style.height=mDiv.offsetHeight+"px";
		 }else{
		 	gDiv.style.height=mDiv.offsetHeight-btn.offsetHeight-btn.offsetTop+1+"px";
		 }
		 gDiv.style.width=mDiv.offsetWidth-6+"px";
    }
    goResize();   
</script>
</body>
</html>