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
	var workslink = contextpath + "works/artWorks.do";
	function closedialog(ret) {
		var res=ret['ret'];
		if(res==true){
			if(editMode=="ADD") {
				var worksId = $$("id").value;
				var data=ajaxSubmit(fulllink+"?action=AJAXSAVE&ids=" + ret['str1']+"&record.worksId="+worksId,"artSomeForm");
		 		if(data.exit>0){
		 			  alert(data.message);
		 			  return;
		 		}
		 		else {	  
		 			goSearch();
		 		} 
			}
		}
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
		goartSomeGridSearch();
	}
	
	function goAdd()  {
		
		var url_link = contextpath + "works/artWorks.do?query.parameters.type=2&query.parameters.selectType=m";
	    openWindow("选择作品",url_link,0.8,0.8);	
	    editMode="ADD";
	}
	
	function goModify(){
		var artistId = $$("artistId").value;
		var id = findSelected("修改",artCase_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id+"&record.artistId="+artistId+"&query.parameters.orderBy=false";
		editMode="EDIT";
	    openWindow("修改作品时期信息",url_link,0.6,0.6);  
	}
	
	function view(id) {
		var url_link=workslink+'?action=VIEW&record.id='+id+"&query.parameters.orderBy=false";
	    editMode="VIEW";
	    openWindow("查看作品基本信息",url_link,0.8,0.8);
	}
	
	function goDel(val) {
		var id = findMultiSelected("删除",artSome_grid);
		if (id == "") return;
		var res = confirm("是否真的要删除?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=DELETE&ids=" + id,"artSomeForm");
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
		<s:form action="artSome" id="artSomeForm" method="post" namespace="/works">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.hide" id="hide"/>
			<s:hidden name="query.parameters.orderBy" id="orderBy" value="false"/>
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
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
						<%--<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goCel()">导入</gl:button>--%>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="artSome" page="true" form="artSomeForm" property="query" cellPadding="0" cellSpacing="0">
			var grid = new dhtmlXGridObject();
			grid.setImagePath(imagePath);
			var headAlign=[tCenter,tCenter,tCenter,tCenter,tCenter];
			grid.setHeader("#master_checkbox,相关作品名称,年份,媒介(材料),媒介(形式)",null,headAlign);
			grid.setInitWidthsP("20,20,20,20,20");
			grid.setColAlign("center,center,center,center,center");
			grid.setColTypes("ch,link,ro,ro,ro");
			grid.setColSorting("str,str,str,str,str");
			grid.setSkin("dhx_terrace");
			grid.init();
			var gDiv=$$('artSome_box');
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
    
	 var gDiv=window.document.getElementById('artSome_box');
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