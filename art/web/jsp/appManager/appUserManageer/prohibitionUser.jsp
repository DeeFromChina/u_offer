<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/common/header.jsp"%>
    <script type="text/javascript">
        var fulllink = contextpath + "appManager/appUserManager.do";
        
        function goSubmit() {
    		if(checkFormInput()){
    		    var url =fulllink+"?action=PROHIBITION";
    		  	document.forms[0].action=url;
    			document.forms[0].submit();
    		}
    	}
        
        function goCancel(){
    		parent.closedialog('false');
    		
    	}
        
        function closedialog(ret) {
    		if(ret=='true') {
    			goSearch();
    		}
    		editMode="";
    	}
        
        function init(){
    		showMessage('<s:property value="errorMessage" escape="false"/>');
    		var recommentTime = initCalendar('recommentTime','imgEndDate');
    	}
        
        function checkFormInput(){
        	if (!checkString(getElement('record.recommentTime'), "重新发言时间", 30, true)) return;
    		return true;
    	}
    </script>
    <style type="text/css">
        html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
    </style>
</head>
<body onload="init()">

<s:form action="appUserManager" id="appUserManagerForm" method="post" namespace="/appManager">
    <s:hidden name="form.record.id" id="userId"/>
    <div id="formDiv" class="formDiv" style="padding: 0px;">
        <table cellpadding="0" cellspacing="0" width="99%" class="formTable">
            <tr>
                <td class="textR" width="15%"><font color="red">*</font>重新发言时间：</td>
                <td width="85%">
   					<table cellspacing="0" width="100%" class="tbnBtnStyle">
   						<tr>
   							<td>
                				<s:textfield name="record.recommentTime" readonly="true" id="recommentTime"></s:textfield>
                			</td>
                			<td width="20px"> 
						   	       <img id="imgEndDate" style="cursor:pointer;" src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif"/>
							</td>
						</tr>
                	</table>
                </td>
            </tr>
        </table>
    </div>
</s:form>

<div id="btnDiv" class="btnDiv">
    <gl:button name="btnAdd" onClick="goSubmit()" >保存</gl:button>&nbsp;
    <gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>
</body>
</html>
