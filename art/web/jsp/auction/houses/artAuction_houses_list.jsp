<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">  
	var hyperlink = "../auction/ArtAuctionHouses.do";	
	var fulllink = contextpath + "auction/ArtAuctionHouses.do";		
      
	function goAdd()  {
		editMode="ADD";
	    openWindow("添加拍卖行",fulllink+'?action=ADD',0.5,0.5);
	}
	
	function view(id) {
	      /*  var url_link=fulllink+'?action=VIEW&ids='+id;
	       editMode="VIEW";
	       openWindow("查看拍卖行信息",url_link,0.5,0.5); */	
		url_link = fulllink+'?action=EDIT&record.id='+id;
		editMode="EDIT";
	    openWindow("修改拍卖行信息",url_link,0.5,0.5);	 
	}
	function goSelect(){
		var id = findSelected("选择拍卖行",ArtAuctionHouses_grid);
		if (id == "") return;
		var name = ArtAuctionHouses_grid.cellById(id,1).getValue();;
		var ret=new Object();
		ret['ret']=true;
		ret['str1']=id;
		ret['str2']=name;
		parent.closedialog(ret);
	}
	
	function goModify(){
		var id = findSelected("修改",ArtAuctionHouses_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id;
		editMode="EDIT";
	    openWindow("修改拍卖行信息",url_link,0.5,0.5);	  
	}

	function goDel()  {
		var id = findMultiSelected("删除",ArtAuctionHouses_grid);
		if (id == "") return;
		var res = confirm("是否要删除？");
		if (res == true){
			var data = ajaxSubmit(fulllink+"?action=delete&ids=" + id,"ArtAuctionHousesForm");
			if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}else {	  
	 			goSearch();
	 		} 
		}
	}
	
	function goImport() {
		editMode="EXCEL";
		url_link = fulllink+'?action=IMPORT';
		openWindow("导入拍卖行",url_link,0.5,0.5);
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
	    if(ret == 'false' && editMode=="EXCEL"){
			goSearch();
		}
		editMode="";
	}
	
	function goSearch(){
		goArtAuctionHousesGridSearch(); 
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
	} 

	function goExport(){
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
		<s:form action="ArtAuctionHouses" id="ArtAuctionHousesForm" method="post" namespace="/auction" enctype="multipart/form-data">
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tr>
					<td width="100%" style="height:30px;" class="textL">&nbsp;&nbsp;
						&nbsp;&nbsp; 拍卖行名称：<s:textfield style="width:150px;"  name="query.parameters.auctionHouse"/>&nbsp;&nbsp;
						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
					</td>
				</tr>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
					   <s:if test="query.parameters.type==1">
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goImport()">导入</gl:button>
						<%--<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goExport()">导出</gl:button>--%>
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="downloadTemp()">下载模板</gl:button>
						</s:if>
						<s:else>
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goSelect()">确认</gl:button>
					   </s:else>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtAuctionHouses" page="true" form="ArtAuctionHousesForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,拍卖行,国家,地址,网址,描述",null, headAlign);
				grid.setInitWidthsP("5,20,15,20,20,20");
				grid.setColAlign("center,center,center,center,center,center");
				grid.setColTypes("ch,link,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str,str");
				grid.init();
				var gDiv = $$('ArtAuctionHouses_box');
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
    
	 var gDiv=window.document.getElementById('ArtAuctionHouses_box');
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