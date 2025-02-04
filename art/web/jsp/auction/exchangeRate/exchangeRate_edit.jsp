<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <%@include file="/common/header.jsp" %>

	<script type="text/javascript">
		var fulllink = contextpath + "auction/exchangeRate.do";

		function goCancel() {
			parent.closedialog(false);
		}

		function goSubmit() {
		  if(!checkString(getElement('record.year'),"年份",100,true)) return;
		  if(!checkString(getElement('record.season'),"季度",100,true)) return;
		  if(!checkNumeric(getElement('record.hkExchangeRate'),"港币",20,true,true)) return;					
		  if(!checkNumeric(getElement('record.dExchangeRate'),"美元",20,true,true)) return;	
		  if(!checkNumeric(getElement('record.eExchangeRate'),"欧元",20,true,true)) return;	
		  if(!checkNumeric(getElement('record.pExchangeRate'),"英镑",20,true,true)) return;	
		  document.forms[0].action=fulllink+"?action=UPDATE";
    	  document.forms[0].submit();
		}
		
		  function init(){
			  showMessage('<s:property value="errorMessage" escape="false" />');
		  }
  </script>
  <style type="text/css">
     html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
  </style>
</head>

<body onload="init()">
<div id="formDiv" class="formDiv">
  <s:form action="exchangeRate" id="exchangeForm" method="post" namespace="/auction">
  		<s:hidden name="form.record.id"/>
			<table id="tblForm" cellpadding="0" cellspacing="0" width="98%" class="formTable">
			    <col width="15%"/>
			    <col width="35%"/>
			    <col width="15%"/>
			    <col width="35%"/>
				<tr>
					<td class="textR"><font color="red">*</font>年份：</td>
				  	<td>
				  		<s:select  list="codeSets.DATE_YEAR" listKey="value" listValue="codeName" name="record.year"></s:select>
				  	</td>
				  	<td class="textL" colspan="2">兑换规则：人民币1元兑换其他货币的汇率</td>
				</tr>
				<tr>
					<td class="textR"><font color="red">*</font>季度：</td>
				  	<td>
				  		<s:select  list="codeSets.SEASON" listKey="value" listValue="codeName" name="record.season"></s:select>
				  	</td>
				  	<td class="textL" colspan="2"/>
				</tr>
				<tr>
			    	<td class="textR"><font color="red">*</font>港币：</td>
				  	<td >
				  	  <s:textfield name="record.hkExchangeRate" />
				  	</td>
				  	<td class="textR"><font color="red">*</font>美元：</td>
				  	<td>
				  	  <s:textfield name="record.dExchangeRate" />
				  	</td>
				</tr>
				<tr>
				  	<td class="textR"><font color="red">*</font>欧元：</td>
				  	<td>
				  	  <s:textfield name="record.eExchangeRate" />
				  	</td>
				  		<td class="textR"><font color="red">*</font>英镑：</td>
				  	<td >
				  	  <s:textfield name="record.pExchangeRate" />
				  	</td>
				</tr>
	 		</table>
 </s:form>	
</div>
<div id="btnDiv" class="btnDiv">
		<gl:button name="btnAdd" onClick="goSubmit()">保存</gl:button>
		&nbsp;
	   <gl:button name="btnCancel" onClick="goCancel()">取消</gl:button>
</div>	  
<%@include file="/common/dialog.jsp" %>
<%@include file="/common/resize.jsp" %>
<script type="text/javascript">

</script>
</body>
</html>