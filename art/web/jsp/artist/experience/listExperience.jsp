<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">  
	var hyperlink = "../artist/ArtistExperience.do";	
	var fulllink = contextpath + "artist/ArtistExperience.do";		
      
	function goAdd()  {
		editMode="ADD";
		var artistId = $$('artistId').value;
	    openWindow("添加人生经历",fulllink+'?action=ADD&record.artistId=' + artistId,0.6,0.6);
	}
	
	function view(id) {
	       var url_link=fulllink+'?action=VIEW&ids=' + id;
	       editMode="VIEW";
	       openWindow("查看人生经历",url_link,0.5,0.5);	
	}
	
	function goModify(){
		var id = findSelected("修改",ArtistExperience_grid);
		if (id == "") return;
		var artistId = $$('artistId').value;
		url_link = fulllink+'?action=EDIT&ids='+id+"&record.artistId="+artistId;
		editMode="EDIT";
	    openWindow("修改人生经历",url_link,0.6,0.6);	  
	}

	function goDel()  {
		var id = findMultiSelected("删除",ArtistExperience_grid);
		if (id == "") return;
		var res = confirm("是否要删除？");
		if (res == true){
			var data = ajaxSubmit(fulllink+"?action=delete&ids=" + id,"ArtistExperienceForm");
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
		openWindow("导入人生经历",url_link,0.5,0.5);
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
	    if(ret==true || ret=='true') {
	   		goSearch();
	    }
		editMode="";
	}
	
	function goSearch(){
		goArtistExperienceGridSearch(); 
	}
	
	function init(){
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
		<s:form action="ArtistExperience" id="ArtistExperienceForm" method="post" namespace="/artist" enctype="multipart/form-data">
			<s:hidden name="record.artistId" id="artistId"/>
			<s:hidden name="record.hide" id="hide"/>
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:textfield cssStyle="display:none"></s:textfield>
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goImport()">导入</gl:button>
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="downloadTemp()">下载模板</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtistExperience" page="true" form="ArtistExperienceForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,年份,人生事件,历史事件",null, headAlign);
				grid.setInitWidthsP("5,25,35,35");
				grid.setColAlign("center,center,center,center");
				grid.setColTypes("ch,link,ro,ro");
				grid.setColSorting("na,str,str,str");
				grid.init();
				var gDiv = $$('ArtistExperience_box');
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
    
	 var gDiv=window.document.getElementById('ArtistExperience_box');
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