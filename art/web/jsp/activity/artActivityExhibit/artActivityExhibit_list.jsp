<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">  
	var hyperlink = contextpath + "activity/ArtActivityExhibit.do";	
	var fulllink = contextpath + "activity/ArtActivityExhibit.do";		
      
	function goAdd()  {
		var type = $$('type').value;
		editMode="ADD";
	    openWindow("添加展览信息",fulllink+'?action=ADD&record.type='+type,0.6,0.8);
	}
	
	function view(id) {
			var type = $$('type').value;
			/* var url_link=fulllink+'?action=VIEW&record.id=' + id+"&record.type="+type;
	       editMode="VIEW";
	       openWindow("查看展览信息",url_link,0.6,0.5);	 */
		url_link = fulllink+'?action=EDIT&record.id='+id+"&record.type="+type;
		editMode="EDIT";
	    openWindow("修改展览信息",url_link,0.6,0.8);
	}
	
	function goModify(){
		var type = $$('type').value;
		var id = findSelected("修改",ArtActivityExhibit_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id+"&record.type="+type;
		editMode="EDIT";
	    openWindow("修改展览信息",url_link,0.6,0.8);	  
	}

	function goDel()  {
		var id = findMultiSelected("删除",ArtActivityExhibit_grid);
		if (id == "") return;
		var res = confirm("是否要删除？");
		if (res == true){
			var data = ajaxSubmit(fulllink+"?action=delete&ids=" + id,"ArtActivityExhibitForm");
			if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}else {	  
	 			goSearch();
	 		} 
		}
	}
	
	function goImport() {
		var artistId = $$('artistId').value;
		url_link = fulllink+'?action=IMPORT&record.artistId=' + artistId;
		openWindow("导入展览信息",url_link,0.5,0.5);
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
		var artAgency=ret['artAgency'];
		var country=ret['country'];
		var artWorks=ret['artWorks'];
		if(res==true) {
	    	 getElement('query.parameters.artArtistId').value=ret['str'];
	    	 getElement('query.parameters.artArtistName').value=ret['str1'];
	     }
		if(artAgency==true) {
	    	 getElement('query.parameters.artAgencyId').value=ret['str'];
	    	 getElement('query.parameters.artAgencyName').value=ret['str1'];
	     }
		if(country==true) {
	    	 getElement('query.parameters.countryId').value=ret['str'];
	    	 getElement('query.parameters.countryName').value=ret['str1'];
	     }
		if(artWorks==true) {
	    	 getElement('query.parameters.artWorksId').value=ret['str'];
	    	 getElement('query.parameters.artWorksName').value=ret['str1'];
	     }
	    if(ret==true || ret=='true') {
	   		goSearch();
	    }
	    if(ret == 'false' && editMode=="EXCEL"){
			goSearch();
		}
		editMode="";
	}
	
	function goSearch(){
		goArtActivityExhibitGridSearch(); 
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
	} 

	function goArtAgency(){
		var url_link=contextpath+'activity/ArtActivityExhibit.do?action=ARTAGENCY&ids='+$$("artAgencyId").value;
	    openWindow("选择机构",url_link,200,300);	
	}

	function goCountry(){
		var url_link=contextpath+'activity/ArtActivityExhibit.do?action=COUNTRY&ids='+$$("countryId").value;
	    openWindow("选择国家",url_link,200,300);	
	}

	function goArtWorks(){
		var url_link=contextpath+'activity/ArtActivityExhibit.do?action=ARTWORKS&ids='+$$("artWorksId").value;
	    openWindow("选择作品",url_link,300,300);	
	}
	
	function clear(name){
		var id = name+"Id";
		name = name+"Name";
		$$(name).value="";
		$$(id).value="";
	}

	function goExport(){
		editMode="EXCEL";
		document.forms[0].action = fulllink + "?action=EXPORT";
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
</script>

<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
}
</style>
</head>

<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="ArtActivityExhibit" id="ArtActivityExhibitForm" method="post" namespace="/system" enctype="multipart/form-data">
			<s:hidden name="record.type" id="type"/>
			<s:hidden name="query.parameters.groupby" value="1" />
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
		 		<tr>
					<td class="textL" style="width: 80%">
                    &nbsp;&nbsp;艺术家：<s:textfield style="width:100px;" name="query.parameters.cName" />
					&nbsp;&nbsp;展览名：<s:textfield style="width:100px;" name="query.parameters.exhibitName" /> 
					&nbsp;&nbsp;展览机构：
					<s:textfield readOnly="true" name="query.parameters.artAgencyName" id="artAgencyName" style="width:150px;"/>
                    <s:hidden name="query.parameters.artAgencyId" id="artAgencyId" />
                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtAgency()">选择</gl:button>&nbsp;&nbsp; 
                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="clear('artAgency')">清除</gl:button>&nbsp;&nbsp; 
					&nbsp;&nbsp;国家：
					<s:textfield readOnly="true" name="query.parameters.countryName" id="countryName" style="width:150px;"/>
                    <s:hidden name="query.parameters.countryId" id="countryId" />
                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goCountry()">选择</gl:button>&nbsp;&nbsp;
                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="clear('country')">清除</gl:button>&nbsp;&nbsp;
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
						<%--<gl:button name="btnQuery" onClick="goExport()">导出</gl:button>--%>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtActivityExhibit" page="true" form="ArtActivityExhibitForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,艺术家,展览名,展览机构名,策展人,年,月",null, headAlign);
				grid.setInitWidthsP("5,15,20,25,15,10,10");
				grid.setColAlign("center,center,center,center,center,center,center");
				grid.setColTypes("ch,ro,link,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str,str,str");
				grid.setSkin("dhx_terrace");
 				grid.enableMultiline(true);
				grid.init();
				var gDiv = $$('ArtActivityExhibit_box');
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
    
	 var gDiv=window.document.getElementById('ArtActivityExhibit_box');
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