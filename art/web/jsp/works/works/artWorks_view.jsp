<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
var contextpath = "<%=CONTEXT_PATH%>";
var fulllink = contextpath + "works/artWorks.do";	
var periodlink = contextpath + "works/artPeriod.do";	
var evaluatelink = contextpath + "works/artEvaluate.do";	
var caselink = contextpath + "works/artCase.do";	
var somelink = contextpath + "works/artSome.do";	
var exhibitlink = contextpath + "activity/ArtWorksExhibit.do";	
var abmblink = contextpath + "works/ArtWorksAbmb.do";	
var wordlink = contextpath + "works/ArtWorksWords.do";	
var networklink = contextpath + "works/ArtWorksNetwork.do";	
var mediumlink = contextpath + "works/artMedium.do";	
var auctionlink = contextpath + "auction/ArtAuction.do";	
var agencylink = contextpath + "works/ArtWorksAgency.do";	
var serieslink = contextpath + "artist/ArtistWorksSeries.do";

	function goCancel(){
		parent.closedialog('true');
	}

	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		//hidebtn();
	}

	function hidebtn(){
		$(".dhxtabbar_tab_text").each(function(){
			this.addEventListener('click',function(){
				if(this.innerHTML != "作品基本信息"){
					document.getElementById("btnDiv").style.display="none";
				}else{
					document.getElementById("btnDiv").style.display="";
				}
			});
		});
	}
	
	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="artWorks" id="artWorksForm" method="post" namespace="/works" enctype="multipart/form-data">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.next" id="next"/>
			<s:hidden name="record.Psize" id="Psize"/>
			<s:hidden name="record.artistId" id="artistId"/>
			<div id="tblForm"></div>
			<div id="page_0">
			<div id="formDiv" class="formDiv" style="padding:0px;">
			<table cellpadding="0" cellspacing="0" width="90%" class="formTable">
				<tr>
					<td width="30%" rowspan="9" colspan="6" class="textC">
						<a href="<%=CONTEXT_PATH%>upload/auction/<s:property value='record.photoPath'/>/<s:property value='record.thumbnail'/>" target="_blank" >
						<img height="353px" src="<%=CONTEXT_PATH%>upload/auction/<s:property value='record.photoPath'/>/<s:property value='record.thumbnail'/>"/>
						</a>
					</td>
					<td width="5%" class="textR"><font color="red">*</font>作品编号：</td>
					<td width="10%" class="textL"><s:property value="record.worksNo"/></td>
					<td width="5%" class="textR"><font color="red">*</font>艺术家：</td>
					<td width="10%" class="textL">
						<s:property value="record.artArtistName"  />
  	                </td>
				</tr>
				<tr>
					<td class="textR"><font color="red">*</font>作品名称：</td>
					<td class="textL"><s:property value="record.worksCName"/></td>
					<td class="textR">英文名称：</td>
					<td class="textL"><s:property value="record.worksEName"/></td>
				</tr>
				<tr>
					<td class="textR">创作时间：</td>
					<td class="textL" colspan="3">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td class="textL"><s:property value="record.createYear"/></td>
					            <td class="textL"><s:property value="record.createMonth"/></td>
					            <td class="textL"><s:property value="record.createDay"/></td>
					        </tr>
					    </table>
					</td>
				</tr>
				<tr>
					<td class="textR">创作时间段：</td>
					<td class="textL" colspan="3">
						<s:property value="record.createFromTo"/>
					</td>
				</tr>
				<tr>
					<td class="textR">尺寸(cm):</td>
					<td class="textL" colspan="3">
						<s:property value="record.sizeCm"/>
					</td>
				</tr>
				<tr>
					<td class="textR">尺寸(in):</td>
					<td class="textL" colspan="3">
						<s:property value="record.sizeIn"/>
					</td>
				</tr>
				<tr>
					<td class="textR">尺寸(尺):</td>
					<td class="textL" colspan="3">
						<s:property value="record.sizeRule"/>
					</td>
				</tr>
				<tr>
					<td class="textR">假画：</td>
					<td class="textL">
					<s:radio list="codeSets.YES_OR_NO" listKey="value" listValue="codeName" name="record.fakePaintings" cssStyle="width:15px;"/>
					</td>
					<td class="textR">习作：</td>
					<td class="textL"><s:property value="record.worksWriting"/></td>
				</tr>
				<tr>
					<td class="textR">作品系列：</td>
					<td class="textL" colspan="3">
						<s:property value="record.worksSeriesName"/>
					</td>
				</tr>
				<tr>
					<td class="textR">创作方式：</td>
					<td class="textL" colspan="5">
					<s:checkboxlist list="codeSets.CREATIVE_WAY" listKey="value" listValue="codeName" name="writs" cssStyle="width:20px;"/>
					</td>
					<td class="textR">社会功能：</td>
					<td class="textL" colspan="3"><s:checkboxlist list="codeSets.SOCIAL_FUNCTION" listKey="value" listValue="codeName" name="strs" cssStyle="width:20px;"/></td>
				</tr>
				<tr >
					<td>局部尺寸：</td>
					<td colspan="9" class="textL">
					<table  id="tblUpload" cellpadding="0" cellspacing="0" width="100%"  class="tbnBtnStyle" align="left">
						   <s:iterator value="partSizeList" id="partSize" status="ps">
							   <s:if  test='#partSize.id != undefined'>
							   	<c:set var='name' value="name_${ps.index}" scope="page"/>
							   	<c:set var='unit' value="unit_${ps.index}" scope="page"/>
							   	<c:set var='l' value="l_${ps.index}" scope="page"/>
							   	<c:set var='w' value="w_${ps.index}" scope="page"/>
							   		<tr>
								      <td class="textL">
										   <input type="text" class="partSize_l" placeholder="位置名称"  name="record.partSize_name" value="${partSize[name]}" style="width:23%;"
										   />&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;<input type="text" placeholder="长" class="partSize_l" name="record.partSize_l" value="${partSize[l]}" style="width:23%;"
										   />&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;<input type="text" placeholder="宽" class="partSize_l" name="record.partSize_w" value="${partSize[w]}" style="width:23%;"
										   />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" placeholder="尺寸单位" class="partSize_l" name="record.partSize_unit" value="${partSize[unit]}" style="width:23%;"/>
									   </td>
								   </tr>
							   </s:if>
						 </s:iterator>
					</table>
					</td>
				</tr>
				<tr>
					<td class="textR">媒介材料：</td>
					<td class="textL" colspan="5">
						<s:property value="record.mediumMaterialName"/>
  	                </td>
					<td class="textR">媒介形式：</td>
					<td class="textL" colspan="3">
						<s:property value="record.mediumShapeName"/>
  	                </td>
				</tr>
				<tr>
					<td class="textR" valign="top">媒介材料说明：</td>
					<td valign="top" colspan="5" class="textL">
						<s:property value="record.materialRemark"  />
					</td>
					<td class="textR" valign="top">媒介形式说明：</td>
					<td valign="top" colspan="3" class="textL">
						<s:property value="record.shapeRemark"/>
					</td>
				</tr>
				<tr>
					<td class="textR">签名位置1：</td>
					<td class="textL"><s:property value="record.signature"/></td>
					<td class="textR">签名内容1：</td>
					<td class="textL" colspan="3"><s:property value="record.signatureContent"/></td>
					<td class="textR">内容关键词</td>
					<td colspan="3" rowspan="3" class="textL">
						<s:property value="record.keywordAddr"/>
						<s:property value="record.keywordCharacter"/>
						<s:property value="record.keywordEvent"/>
						<s:property value="record.keywordThing"/>
						<s:property value="record.keywordOther1"/>
						<s:property value="record.keywordOther2"/>
					</td>
				</tr>
				<tr>
					<td class="textR">签名位置2：</td>
					<td class="textL"><s:property value="record.signature2"/></td>
					<td class="textR">签名内容2：</td>
					<td class="textL" colspan="3"><s:property value="record.signatureContent2"/></td>
				</tr>
				<tr>
					<td class="textR">签名位置3：</td>
					<td class="textL"><s:property value="record.signature3"/></td>
					<td class="textR">签名内容3：</td>
					<td class="textL" colspan="3"><s:property value="record.signatureContent3"/></td>
				</tr>
				<tr>
					<td rowspan="5">附件：</td>
					<td colspan="5" rowspan="5" class="textL">
						<table  id="tblUpload1" cellpadding="0" cellspacing="0" width="100%"  class="tbnBtnStyle" align="left">
						   <s:iterator value="thumbnailList" id="tl" status="tltl">
							<tr>
									<td class="textL">
										<input type="hidden" class="fileName" id="${tl.key}"/>
										<a href="<%=CONTEXT_PATH%>upload/auction/<s:property value='record.photoPath'/>/${tl.key}" target="_blank" >${tl.value}</a>
									</td>
									</tr>
								</s:iterator>
						</table>
					</td>
					<td class="textR">作品题材：</td>
					<td colspan="3" class="textL">
						<s:property value="record.worksThemeName1"/>
						<s:property value="record.worksThemeName2"/>
						<s:property value="record.worksThemeName3"/>
					</td>
				</tr>
				<tr>
					<td class="textR">风格类型：</td>
					<td colspan="3" class="textL">
						<s:property value="record.worksStyle"/>
  	                </td>
				</tr>
				<tr>
					<td class="textR">图片来源</td>
					<td class="textL" colspan="3"><s:property value="record.photoSoure"/></td>
				</tr>
				<tr>
					<td class="textR">创作国家：</td>
					<td colspan="3" class="textL">
						<s:property value="record.createCountryName"/>
					</td>
				</tr>
				<tr>
					<td class="textR">创作地点：</td>
					<td colspan="3" class="textL"><s:property value="record.createPlace"/></td>
				</tr>
				<tr>
					<td class="textR" valign="top">作品描述：</td>
					<td colspan="9" valign="top" class="textL">
						<s:property value="record.worksStatus"/>
					</td>
				</tr>
		 	</table>
		 </div>
		 </div>
		 <iframe  id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_2" name="page_2" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_3" name="page_3" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_4" name="page_4" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_5" name="page_5" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_7" name="page_7" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_8" name="page_8" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_9" name="page_9" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_10" name="page_10" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_11" name="page_11" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_12" name="page_12" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
		 <iframe  id="page_13" name="page_13" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
</s:form>
<div id="btnDiv" class="btnDiv">
	 <gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
</div>


<script type="text/javascript"> 
 
</script>
<%@include file="/common/dialog.jsp"%>
<script type="text/javascript"> 
var mainbody=window.document.body;
if(window.addEventListener) {
	     window.addEventListener("resize",goResize,false); 
}
else {
	     window.attachEvent('onresize',goResize);
}

var fDiv=window.document.getElementById('formDiv');
var bDiv=window.document.getElementById('btnDiv');
var btbl=window.document.getElementById('tblForm');

function goResize(){
	   btbl.style.width=mainbody.offsetWidth-9+"px";
	   btbl.style.height=mainbody.offsetHeight-40+"px";
	   bDiv.style.width=mainbody.offsetWidth-10+"px";
	   bDiv.style.top=btbl.offsetHeight+5+"px";
	}
	goResize(); 

var tabbar = new dhtmlXTabBar("tblForm", "top");
tabbar.setSkin('dhx_terrace');
tabbar.setArrowsMode("auto");
tabbar.addTab("a0", "作品基本信息", "120px");
tabbar.addTab("a1", "作品时期", "90px");
tabbar.addTab("a2", "意义与评价", "90px");
tabbar.addTab("a3", "个案研究", "90px");
tabbar.addTab("a4", "相关作品", "90px");
tabbar.addTab("a5", "相关展览", "90px");
tabbar.addTab("a7", "相关艺博会", "90px");
tabbar.addTab("a8", "相关纸媒", "100px");
tabbar.addTab("a9", "相关网媒", "100px");
tabbar.addTab("a10", "相关音视频", "100px");
tabbar.addTab("a11", "拍卖信息", "100px");
tabbar.addTab("a12", "合作机构信息", "100px");
tabbar.tabs("a0").attachObject("formDiv");
tabbar.tabs("a0").setActive();
if(document.getElementById('next').value=="true"){
	tabbar.tabs("a1").setActive();
	document.getElementById("save").style.display="none";
} 
tabbar.tabs("a1").attachObject("page_1");
tabbar.tabs("a2").attachObject("page_2");
tabbar.tabs("a3").attachObject("page_3");
tabbar.tabs("a4").attachObject("page_4");
tabbar.tabs("a5").attachObject("page_5");
tabbar.tabs("a7").attachObject("page_7");
tabbar.tabs("a8").attachObject("page_8");
tabbar.tabs("a9").attachObject("page_9");
tabbar.tabs("a10").attachObject("page_10");
tabbar.tabs("a11").attachObject("page_11");
tabbar.tabs("a12").attachObject("page_12");
tabbar.enableAutoReSize(true);
tabbar.enableTabCloseButton(true);
tabbar.attachEvent("onTabClick", function(idClicked, idSelected){
	if(idClicked != "a0"){
		document.getElementById("btnDiv").style.display="none";
	}else{
		document.getElementById("btnDiv").style.display="";
	}
});
var hide = "&record.hide=1";

window.frames["page_1"].location.href=periodlink+'?action=LIST&record.id='+$$('id').value+'&record.artistId='+$$('artistId').value+hide;
window.frames["page_2"].location.href=evaluatelink+'?action=LIST&record.id='+$$('id').value+'&record.artistId='+$$('artistId').value+hide;
window.frames["page_3"].location.href=caselink+'?action=LIST&record.id='+$$('id').value+'&record.artistId='+$$('artistId').value+hide;
window.frames["page_4"].location.href=somelink+'?action=LIST&record.id='+$$('id').value+'&record.artistId='+$$('artistId').value+hide;
window.frames["page_5"].location.href=exhibitlink+'?action=LIST&query.parameters.worksId='+$$('id').value+hide;
window.frames["page_7"].location.href=abmblink+'?action=LIST&query.parameters.worksId='+$$('id').value+hide;
window.frames["page_8"].location.href=wordlink+'?action=LIST&query.parameters.worksId='+$$('id').value+hide;
window.frames["page_9"].location.href=networklink+'?action=LIST&query.parameters.worksId='+$$('id').value+hide;
window.frames["page_10"].location.href=mediumlink+'?action=LIST&query.parameters.worksId='+$$('id').value+hide;
window.frames["page_11"].location.href=auctionlink+'?action=LIST&query.parameters.worksId='+$$('id').value+"&record.view=view";
window.frames["page_12"].location.href=agencylink+'?action=LIST&query.parameters.worksId='+$$('id').value+hide;

fDiv.style.height=btbl.offsetHeight-40+"px";
</script>
</body>
</html>