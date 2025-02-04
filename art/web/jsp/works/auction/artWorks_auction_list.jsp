<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">  
	var hyperlink = "../auction/ArtAuction.do";	
	var fulllink = contextpath + "auction/ArtAuction.do";		
	var fulllink_works = contextpath + "works/artWorks.do";		
	var fulllink_house = contextpath + "auction/ArtAuctionHouses.do";		
      
	function goAdd()  {
		editMode="ADD";
	    openWindow("添加作品拍卖",fulllink+'?action=ADD',0.8,0.8);
	}
	
	function view123(id) {
	    /* var url_link=fulllink+'?action=VIEW&ids='+id;
	    editMode="VIEW";
	    openWindow("查看作品拍卖信息",url_link,0.8,0.8);	 */
	    url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品拍卖信息",url_link,0.8,0.8);
	}
	
	function worksview(id) {
	    /**var url_link=fulllink+'?action=VIEW&ids='+id;
	    editMode="VIEW";
	    openWindow("查看作品拍卖信息",url_link,0.8,0.8);	
		url_link = fulllink_works+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品基本信息",url_link,0.8,0.8);
		url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品拍卖信息",url_link,0.8,0.8);	   */
		editMode="VIEW";
		openWindow("修改作品", fulllink_works + "?action=EDIT&ids=" + id, 1, 1);
	}
	
	function viewhang(id) {
	       /* var url_link=fulllink+'?action=VIEW&ids='+id;
	       editMode="VIEW";
	       openWindow("查看作品拍卖信息",url_link,0.8,0.8);	 */
		url_link = fulllink_house+'?action=EDIT&record.id='+id;
		editMode="EDIT";
	    openWindow("修改拍卖行信息",url_link,0.5,0.5);	 
	}
	
	function goModify(){
		var id = findSelected("修改",ArtWorksAuction_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品拍卖信息",url_link,0.8,0.8);	  
	}

	function goDel()  {
		var id = findMultiSelected("删除",ArtWorksAuction_grid);
		if (id == "") return;
		var res = confirm("是否要删除？");
		if (res == true){
			var data = ajaxSubmit(fulllink+"?action=delete&ids=" + id,"ArtWorksAuctionForm");
			if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}else {	  
	 			goSearch();
	 		} 
		}
	}
	
	function renew()  {
		var order = getElement("query.order");                  order.value="";
		var desc = getElement("query.orderDirection");          desc.value="";
		var pn = getElement("query.pageNumber");                pn.value="1";
		var ps = getElement("query.pageSize");                  ps.value="15";
		var v0 =    getElement("query.parameters.worksName");    v0.value="";
		var v1 =    getElement("query.parameters.createYear");    v1.value="";
		var v2 =    getElement("query.parameters.cName");    v2.value="";
	    goSearch();
	}

	function closedialog(ret){
	    if(ret==true || ret=='true') {
	   		goSearch();
	    }
	    if(ret == 'false' && editMode=="EXCEL"){
			goSearch();
		}
		editMode="";
	}
	
	function goSearch(){
		goArtWorksAuctionGridSearch(); 
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
		hide();
	} 
	
	function hide(){
		var view = $$('view').value;
		if(view == 'view'){
			document.getElementById('tbl').style.display="none";
			document.getElementById('btn').style.display="none"; 
		}
	}

	function goExport(){
		document.forms[0].action = fulllink + "?action=EXPORT";
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}

	function goCel() {
		editMode="EXCEL";
		var url_link=fulllink+'?action=GOEXCEL';
	    openWindow("导入",url_link,0.4,0.2);
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
		<s:form action="ArtAuction" id="ArtWorksAuctionForm" method="post" namespace="/auction" enctype="multipart/form-data">
			<s:hidden name="query.parameters.worksId" />
			<s:hidden name="record.view" id="view" />
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tr>
					<td width="100%" style="height:30px;" class="textL">&nbsp;&nbsp;
						&nbsp;&nbsp; 作品名称：<s:textfield style="width:200px;"  name="query.parameters.worksName"/>&nbsp;&nbsp;
						&nbsp;&nbsp; 创作年份：<s:textfield style="width:150px;"  name="query.parameters.createYear"/>&nbsp;&nbsp;
						&nbsp;&nbsp; 艺术家：<s:textfield style="width:100px;"  name="query.parameters.cName"/>&nbsp;&nbsp;
						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
					</td>
				</tr>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
						<gl:button styleClass="sbuBtnStyle"  icon="subIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle"  icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle"  icon="delIcon" onClick="goDel()">删除</gl:button>
						<gl:button styleClass="sbuBtnStyle"  icon="delIcon" onClick="goCel()">导入</gl:button>
						<gl:button name="btnQuery" onClick="goExport()">导出</gl:button>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtWorksAuction" page="true" form="ArtWorksAuctionForm"  property="query" cellPadding="0" cellSpacing="0">
				var view = document.getElementById('view');
				var header = "#master_checkbox,拍卖<br>状态,拍卖作品缩略图,拍卖编号,作品编号,作品名称,作品英文名称,艺术家,尺寸(cm),创作年份,拍卖行,拍卖会,拍场,拍卖时间,季节,成交价,最高估价,最低估价";
				var colType = "ch,ro,img,ro,link,link,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro";
				if(view != undefined && view.value == 'view'){
					header = "序号,拍卖<br>状态,拍卖作品缩略图,拍卖编号,作品编号,作品名称,作品英文名称,艺术家,尺寸(cm),创作年份,拍卖行,拍卖会,拍场,拍卖时间,季节,成交价,最高估价,最低估价";
					colType = "cntr,ro,img,ro,link,link,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro";
				}
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader(header,null, headAlign);
				grid.setInitWidths("50,50,120,80,100,100,100,100,120,100,150,150,150,100,120,100,120,120");
				grid.setColAlign("center,center,center,center,center,center,center,center,center,center,center,center,,center,center,right,right,right");
				grid.setColTypes(colType);
				grid.setColSorting("na,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str");
				grid.init();
				var gDiv = $$('ArtWorksAuction_box');
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
    
	 var gDiv=window.document.getElementById('ArtWorksAuction_box');
	 var mDiv=window.document.getElementById('mainDiv');
	 var tDiv=window.document.getElementById('tbl');
	 var btn=window.document.getElementById('btn');
	 
	 function goResize(){
	   	 var he;
		    if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;

			 mDiv.style.height=he-42+"px";
			 if($$('view').value == 'view'){
				 gDiv.style.height=mDiv.offsetHeight+"px";
			 }else{
			 	gDiv.style.height=mDiv.offsetHeight-btn.offsetHeight-btn.offsetTop+1+"px";
			 }
			 tDiv.style.width=mDiv.offsetWidth-4+"px";
			 gDiv.style.width=mDiv.offsetWidth-6+"px";
		    btn.style.width=tDiv.offsetWidth+"px";
	    }
	    goResize();
    
</script> 

</body>
</html>