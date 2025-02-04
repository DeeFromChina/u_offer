<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var fulllink = contextpath + "literature/ArtLiteratureWords.do";	
	function goCancel(){
		parent.closedialog('false');
		
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
	}

	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
   .ten {width:17% !important;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtLiteratureMedium" id="ArtLiteratureMediumForm" method="post" namespace="/literature" enctype="multipart/form-data">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.cname" id="cname"/>
			<div id="formDiv" class="formDiv" style="padding: 0px;">
			<table cellpadding="0" cellspacing="0" width="99%" class="formTable">
				<tr>
					<td class="textR" width="15%" >艺术家：</td>
					<td width="35%" class="textL">
							<s:property value="record.artistName"/>
  	                </td>
					<td class="textR" width="15%" >文献标题：</td>
					<td width="35%"  class="textL"><s:property value="form.record.literatureTitle"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >作者：</td>
					<td  class="textL" ><s:property value="form.record.literatureAuther"/></td>
					<td class="textR" width="15%" >刊登时间：</td>
					<td  class="textL" >
						 <s:property value="form.record.writeTime" />
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >出版物名称：</td>
					<td class="textL"><s:property value="form.record.publicationName"/></td>
					<td class="textR" width="15%" >出版物期数：</td>
					<td class="textL"><s:property value="form.record.publicationPeriod"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >出版社：</td>
					<td class="textL"><s:property value="form.record.press"/></td>
					<td class="textR" width="15%" >栏目：</td>
					<td  class="textL"><s:property value="form.record.literatureColumn"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >相关页：</td>
					<td class="textL"><s:property value="form.record.relevantPages"/></td>
					<td class="textR" width="15%" >文献类型：</td>
					<td class="textL"><s:property value="form.record.wordsType"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" >文献提及作品：</td>
					<td colspan="3" class="textL">
						<s:property value="form.record.literatureWorks"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >引用文献：</td>
					<td colspan="3" class="textL">
						<s:property value="form.record.quoteLiterature"/>
					</td>
				</tr>
				<tr>
					<td class="textR">文献相关人物：</td>
					<td colspan="3" class="textL">
						<s:property value="form.record.personInvolved"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >文献相关展览：</td>
					<td colspan="3" class="textL">
						<s:property value="form.record.relatedExhib"/>
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >文献相关事件：</td>
					<td colspan="3" class="textL">
						<s:property value="form.record.relatedEvent"/>
					</td>
				</tr>
				<tr>
					<td>附件：</td>
					<td colspan="3" class="textL">
						 <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
								<s:iterator value="cookie" id="c" status="co">
									<tr>
									<td class="textL" >
										<a href="<%=CONTEXT_PATH%>upload/literature/words/<s:property value="record.cname"/>/${c.saveName}"  target="_blank">${c.name}</a>
									</td>
									</tr>
								</s:iterator>
						</table>
					</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >附件来源：</td>
					<td colspan="3" class="textL">
						<s:property value="record.attachmentSource"/>
					</td>
				</tr>
				<tr>
					<td colspan="4" class="textC">文献研究</td>
				</tr>
				<tr>
					<td class="textR" width="15%" >主观重要度评级：</td>
					<td class="textL"><s:property value="record.subjectiveEval"/></td>
					<td class="textR" width="15%" >客观重要度评级：</td>
					<td  class="textL"><s:property value="record.objectiveEval"/></td>
				</tr>
				<tr>
					<td class="textR" width="15%" valign="top">核心论断：</td>
					<td colspan="3" class="textL"  height="80px" valign="top">
						<s:property value="record.coreThesis"/>
					</td>
				</tr>
		 	</table>
	</div>
	<div id="page_1" style="padding:1px;"> </div>
	<div id="page_2" style="padding:1px;"> </div>
</s:form>

<div id="btnDiv" class="btnDiv">
	<gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp"%>

</body>
</html>