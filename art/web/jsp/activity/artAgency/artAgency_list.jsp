<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">  
	var hyperlink = contextpath + "system/artAgency.do";	
	var fulllink = contextpath + "system/artAgency.do";		
      
	function goAdd()  {
		editMode="ADD";
	    openWindow("添加机构",fulllink+'?action=ADD',0.6,0.5);
	}
	
	function view(id) {
	       var url_link=fulllink+'?action=VIEW&ids=' + id;
	       editMode="VIEW";
	       openWindow("查看收藏机构或收藏家",url_link,0.6,0.5);	
	}
	
	function goModify(){
		var id = findSelected("修改",ArtAgency_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id;
		editMode="EDIT";
	    openWindow("修改收藏机构或收藏家",url_link,0.6,0.5);	  
	}

	function goDel()  {
		var id = findMultiSelected("删除",ArtAgency_grid);
		if (id == "") return;
		var res = confirm("是否要删除？");
		if (res == true){
			var data = ajaxSubmit(fulllink+"?action=delete&ids=" + id,"ArtAgencyForm");
			if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}else {	  
	 			goSearch();
	 		} 
		}
	}

	function goSelect(){
		var selectType=$$("selectType").value;
		if(selectType.indexOf('m')>-1){
			var id = findMultiSelected("选择机构",ArtAgency_grid);
			if (id == "") return;
			var str="";
			var str1="";
			var ids=id.split(",");
			  for(var i=0;i<ids.length;i++){
				  var name = ArtAgency_grid.cellById(ids[i],5).getValue();
				  str+=ids[i];
				  str1+=name;
				  if((i+1)!=ids.length){
					  str+=",";
					  str1+="、";
				  }
			  }
			  var ret=new Object();
			  ret['ret']=true;
			  ret['str']=str;
			  ret['str1']=str1;
			  parent.closedialog(ret);
			
		} else {
			var id = findSelected("选择机构",ArtAgency_grid);
			if (id == "") return;
			var name = ArtAgency_grid.cellById(id,1).getValue();
			var ret=new Object();
			ret['ret']=true;
			ret['str']=id;
			ret['str1']=name;
			parent.closedialog(ret);
		}
	}
	
	function goImport() {
		var artistId = $$('artistId').value;
		url_link = fulllink+'?action=IMPORT&record.artistId=' + artistId;
		openWindow("导入收藏机构或收藏家",url_link,0.5,0.5);
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
		goArtAgencyGridSearch(); 
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
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
		<s:form action="ArtAgency" id="ArtAgencyForm" method="post" namespace="/system" enctype="multipart/form-data">
			<s:hidden name="record.artistId" id="artistId"/>
			<s:hidden name="record.hide" id="hide"/>
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.selectType" id="selectType" />
		 	<table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
		 		<tr>
						<td class="textL" style="width: 80%">
						&nbsp;&nbsp;机构中文名：<s:textfield style="width:100px;" name="query.parameters.agencyCName" /> 
						&nbsp;&nbsp;国家：<s:textfield style="width:100px;" name="query.parameters.country" /> 
						&nbsp;&nbsp;城市：<s:textfield style="width:100px;" name="query.parameters.city" /> 
						&nbsp;&nbsp;机构类型：<s:select list="codeSets.AGENCY_TYPE" listKey="value" listValue="codeName" name="query.parameters.agencyType"/> 
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
						</td>
					</tr>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
					<s:if test="query.parameters.type==1">
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goSelect()">确定</gl:button>
					</s:if>
					<s:else>
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
					</s:else>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="ArtAgency" page="true" form="ArtAgencyForm"  property="query" cellPadding="0" cellSpacing="0">
				var grid = new dhtmlXGridObject();
         	    grid.setImagePath(imagePath);
				var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
				grid.setHeader("#master_checkbox,机构中文名,机构英文名,国家,城市,机构类型",null, headAlign);
				grid.setInitWidthsP("5,20,20,15,20,20");
				grid.setColAlign("center,center,center,center,center,center");
				grid.setColTypes("ch,ro,ro,ro,ro,ro");
				grid.setColSorting("na,str,str,str,str,str");
				grid.enableMultiline(true);
				grid.init();
				var gDiv = $$('ArtAgency_box');
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
    
	 var gDiv=window.document.getElementById('ArtAgency_box');
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