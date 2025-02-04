<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<%@include file="/common/header.jsp"%>
<script type="text/javascript">
	var hyperlink = "../appManager/appUserManager.do";
	var fulllink = contextpath + "appManager/appUserManager.do";
	
	function checkProhibition(id){
		var data = ajaxSubmit(fulllink+"?action=CHECK_PROHIBITION&ids=" + id,"appUserManagerForm");
		return data;
	}
	
	function checkUserEnable(id){
		var data = ajaxSubmit(fulllink+"?action=CHECK_USER_ENABLE&ids=" + id,"appUserManagerForm");
		return data;
	}
	
	function goProhibition(){
		var id = findSelected("禁言",appUserManager_grid);
		if (id == "") return;
		url_link = fulllink+'?action=TO_PROHIBITION&ids='+id;
		var data = checkProhibition(id);
		if(data.exit > 0){
			alert(data.message);
			return;
		}else{
			editMode="EDIT";
			openWindow("禁言",url_link,0.5,0.6);
		}
	}
	
	function goDisable(){
		var id = findSelected("停用",appUserManager_grid);
		if (id == "") return;
		url_link = fulllink+'?action=TO_USER_DISABLE&ids='+id;
		var  data = checkUserEnable(id);
		if(data.exit > 0){
			editMode="EDIT";
			openWindow("停用",url_link,0.5,0.6);
		}else{
			alert(data.message);
			return;
		}
	}
   
	function goNotProhibition(){
		var id = findSelected("解除禁言",appUserManager_grid);
		if (id == "") return;
		var isEnable = checkProhibition(id);
		if(isEnable.exit>0){
			var res = confirm("是否要解除禁言？");
			if (res == true){
				var data = ajaxSubmit(fulllink+"?action=NOT_PROHIBITION&ids=" + id,"appUserManagerForm");
				if(data.exit>0){
		 			  alert(data.message);
		 			  return;
		 		}else {	  
		 			goSearch();
		 		} 
			}
		}else{
			alert('该用户没有被禁言');
		}
	}
	
	function goEnable(){
		var id = findSelected("启用",appUserManager_grid);
		if (id == "") return;
		var isEnable = checkUserEnable(id);
		if(isEnable.exit>0){
			alert('该用户已经被启用');
		}else{
			var res = confirm("是否要启用？");
			if (res == true){
				var data = ajaxSubmit(fulllink+"?action=USER_ENABLE&ids=" + id,"appUserManagerForm");
				if(data.exit>0){
		 			  alert(data.message);
		 			  return;
		 		}else {	  
		 			goSearch();
		 		} 
			}
		}
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
		goappUserManagerGridSearch();
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
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
        <s:form action="appUserManager" id="appUserManagerForm" method="post" namespace="/appManager">
            <s:hidden name="query.order" />
            <s:hidden name="query.orderDirection" />
            <s:hidden name="query.pageNumber" />
            <s:hidden name="query.recordCount" />
            <s:hidden name="query.pageCount" />
            <table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
            	<tr>
	            	<td width="100%" style="height:30px;" class="textL">
	            		&nbsp;&nbsp;用户昵称:<s:textfield style="width:150px;"  name="query.parameters.userName"/>
	            		&nbsp;&nbsp;用户账户:<s:textfield style="width:150px;"  name="query.parameters.accountName"/>
	            		&nbsp;&nbsp;用户状态：<s:select style="width:120px;" list="codeSets.START_OR_STOP" listKey="value" listValue="codeName" name="query.parameters.userStatus"/>
	            		&nbsp;&nbsp;用户是否被禁言：<s:select style="width:120px;" list="codeSets.YES_OR_NO" listKey="value" listValue="codeName" name="query.parameters.noComment"/>
	            		&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
	            	</td>
            	</tr>
            </table>
            <table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
                <tr>
                    <td>
                        <gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goProhibition()">禁言</gl:button>
                        <gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goNotProhibition()">解除禁言</gl:button>
                        <gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDisable()">停用用户</gl:button>
                        <gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goEnable()">启用用户</gl:button>
                    </td>
                </tr>
            </table>
            <gl:grid styleClass="pageTurn" id="appUserManager" page="true" form="appUserManagerForm"  property="query" cellPadding="0" cellSpacing="0">
                var grid = new dhtmlXGridObject();
                grid.setImagePath(imagePath);
                var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
                grid.setHeader("#master_checkbox,用户账号,用户昵称,用户状态（启用/停用）,启用时间,禁言（是/否）,解除禁言时间",null, headAlign);
                grid.setInitWidthsP("10,15,15,15,15,15,15");
                grid.setColAlign("center,center,center,center,center,center,center");
                grid.setColTypes("ch,ro,ro,ro,ro,ro,ro");
                grid.setColSorting("na,str,str,str,str,str,str");
                grid.init();
                var gDiv = $$('appUserManager_box');
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
	    
		 var gDiv=window.document.getElementById('appUserManager_box');
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