<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>
<script type="text/javascript">
	var hyperlink = "../appManager/appFeedbackManager.do";
	var fulllink = contextpath + "appManager/appFeedbackManager.do";
		
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
		goappFeedbackManagerGridSearch();
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
        <s:form action="appFeedbackManager" id="appFeedbackManagerForm" method="post" namespace="/appManager">
            <s:hidden name="query.order" />
            <s:hidden name="query.orderDirection" />
            <s:hidden name="query.pageNumber" />
            <s:hidden name="query.recordCount" />
            <s:hidden name="query.pageCount" />
            <table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
            	<tr>
	            	<td width="100%" style="height:30px;" class="textL">
	            		&nbsp;&nbsp;用户账户：<s:textfield style="width:150px;"  name="query.parameters.appAccountName"/>
	            		&nbsp;&nbsp;年：<s:select style="width:120px;" list="codeSets.DATE_YEAR" listKey="value" listValue="codeName" name="query.parameters.YEAR"/>
						&nbsp;&nbsp;月份：<s:select style="width:120px;" list="codeSets.DATE_MONTH" listKey="value" listValue="codeName" name="query.parameters.MONTH"/>
	            		&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
	            	</td>
            	</tr>
            </table>
            <gl:grid styleClass="pageTurn" id="appFeedbackManager" page="true" form="appFeedbackManagerForm"  property="query" cellPadding="0" cellSpacing="0">
                var grid = new dhtmlXGridObject();
                grid.setImagePath(imagePath);
                var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
                grid.setHeader("#master_checkbox,用户账号,反馈时间,内容",null, headAlign);
                grid.setInitWidthsP("5,20,20,55");
                grid.setColAlign("center,center,center,center");
                grid.setColTypes("ch,ro,ro,ro");
                grid.setColSorting("na,str,str,str");
                grid.init();
                var gDiv = $$('appFeedbackManager_box');
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
	    
		 var gDiv=window.document.getElementById('appFeedbackManager_box');
		 var mDiv=window.document.getElementById('mainDiv');
		 var tDiv=window.document.getElementById('tbl');
		    
	    function goResize(){
	   	 var he;
		    if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;
			 mDiv.style.height=he-42+"px";
			 gDiv.style.height=mDiv.offsetHeight - 45 +"px";
			 tDiv.style.width=mDiv.offsetWidth-4+"px";
			 gDiv.style.width=mDiv.offsetWidth-6+"px";
	    }
	    goResize();   
	</script> 

</body>
</html>