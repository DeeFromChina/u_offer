<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">		
	var editMode;
	var fulllink = contextpath + "works/artWorks.do";	

	function goCancel(){
		parent.closedialog('false');
	}

	function goSubmit() {
		var fileName = document.getElementById('file').value;
		if(fileName != ""){
			var format = fileName.split(".");
			var i = format.length;
			if(format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png"){
				alert("只能上传图片格式！");
				return;
			}
		}
		if(checkFormInput()){
		    var url =fulllink+"?action=SAVE";
		  	document.forms[0].action=url;
			document.forms[0].submit();
		} 
	}
	
	function checkFormInput(){
		if(!checkString(getElement('record.artArtistName'),"艺术家",30,true)) return;
		if (!checkString(getElement('record.worksCName'), "作品名称", 300, true)) return;
		return true;
	}
	
	function closedialog(ret) {
		var res=ret['ret'];
		var medium=ret['medium'];
		var country=ret['country'];
		var theme=ret['theme'];
		var st=ret['style'];
		var series=ret['series'];
	     if(res==true) {
	    	 getElement('record.artistId').value=ret['str'];
	    	 getElement('record.artArtistName').value=ret['str1'];
	    	 getElement('record.worksSeries').value="";
	    	 getElement('record.worksSeriesName').value="";
	     }
	     if(medium==true){
	    	 if(ret['mediumCategory']=="1"){
	    	 	getElement('record.mediumMaterialName').value=ret['str1'];
	    	 	getElement('record.mediumMaterial').value=ret['str'];
	    	 }
	    	 if(ret['mediumCategory']=="2"){
	    	 	getElement('record.mediumShapeName').value=ret['str1'];
	    	 	getElement('record.mediumShape').value=ret['str'];
	    	 }
	     }
	     if(editMode=="Country"){
	    	 getElement('record.createCountry').value=ret['str'];
	    	 getElement('record.createCountryName').value=ret['str1'];
	     }
	     if(series==true){
	    	 getElement('record.worksSeries').value=ret['str'];
	    	 getElement('record.worksSeriesName').value=ret['str1'];
	     }
	     if(theme==true){
	    	 var worksThemes = ret['str'].split(";");
	    	 var worksThemeNames = ret['str1'].split(";");
	    	 for(var i=1;i<=worksThemes.length;i++){
	    		 getElement('record.worksTheme'+i).value=worksThemes[i-1];
	    		 getElement('record.worksThemeName'+i).value=worksThemeNames[i-1];
	    	 }
	     }
	     if(st==true){
	    	 getElement('record.styleType').value=ret['str'];
	    	 getElement('record.worksStyle').value=ret['str1'];
	     }
		if(ret=='true') {
			goSearch();
		}
		editMode="";
	}

	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
	}
	
	function goArtArtist(){
		var url_link=contextpath+'works/artWorks.do?action=ARTIST&ids='+$$("artistId").value;
	    openWindow("选择作者",url_link,200,300);	
	}
	
	function goArtCountry(ret){
		if(ret == 'choose'){
			editMode="Country";
			var url_link=contextpath+'system/artCountry.do?record.type=1&query.parameters.type=1';
		    openWindow("选择国家",url_link,0.8,0.8);
		}
		if(ret == 'cancel'){
			$$("createCountryName").value = "";
			$$("createCountry").value = "";
		}
	}
	
	function goArtWorksTheme(ret){
		var ids ="";
		if($$("worksTheme1").value != undefined && $$("worksTheme1").value != ''){
			ids = $$("worksTheme1").value;
		}
		if($$("worksTheme2").value != undefined && $$("worksTheme2").value != ''){
			ids = ids + ";" + $$("worksTheme2").value;
		}
		if($$("worksTheme3").value != undefined && $$("worksTheme3").value != ''){
			ids = ids + ";" + $$("worksTheme3").value;
		}
		if(ret == 'choose'){
			var url_link=contextpath+'works/artWorks.do?action=THEME&ids='+ids;
		    openWindow("选择题材",url_link,300,500);	
		}
		if(ret == 'cancel'){
			$$("worksThemeName1").value = "";
			$$("worksThemeName2").value = "";
			$$("worksThemeName3").value = "";
			$$("worksTheme1").value = "";
			$$("worksTheme2").value = "";
			$$("worksTheme3").value = "";
		}
	}
	
	function goArtWorksStyle(ret){
		if(ret == 'choose'){
			var url_link=contextpath+'works/artWorks.do?action=STYLE&ids='+$$("styleType").value;
		    openWindow("选择风格类型",url_link,300,500);	
		}
		if(ret == 'cancel'){
			$$("worksStyle").value = "";
			$$("styleType").value = "";
		}
	}
	
	function goArtWorksSeries(ret){
		if(ret == 'choose'){
			var artArtistId = $$("artistId").value;
			if(artArtistId == undefined || artArtistId == ""){
				alert("请先选择艺术家！");
				return;
			}
			var url_link=contextpath+'works/artWorks.do?action=SERIES&ids='+$$("worksSeries").value+"&record.artArtistId="+artArtistId;
		    openWindow("选择作品系列",url_link,200,300);	
		}
		if(ret == 'cancel'){
			$$("worksSeriesName").value = "";
			$$("worksSeries").value = "";
		}
	}
	
	function addFileRow(loadName){
		 var t  = document.getElementById(loadName);
		 var rows = t.rows.length;
		 if('tblUpload' == loadName){
			 if(rows>=5){
				 alert("超过最大上传尺寸数！");
				 return;
			 } else {
				 var x=t.insertRow(rows);
				 var one=x.insertCell(0);
				 one.className="textL";
				 var two=x.insertCell(1);
				 one.innerHTML='<input type="text" placeholder="位置名称" class="partSize_l" name="record.partSize_name" style="width:20%;"'
					   +'/>&nbsp;&nbsp;:&nbsp;&nbsp;&nbsp;<input type="text" placeholder="长" class="partSize_l" name="record.partSize_l" style="width:20%;"'
					   +'/>&nbsp;&nbsp;&nbsp;*&nbsp;&nbsp;&nbsp;<input type="text" placeholder="宽" class="partSize_l" name="record.partSize_w" style="width:20%;"'
					   +'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" placeholder="尺寸单位" class="partSize_l" name="record.partSize_unit" style="width:20%;"'
					   +'/>';
				 two.innerHTML='<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
			 }
		 }
		 if('tblUpload1' == loadName){
			 if(rows>=5){
				 alert("超过最大上传文件数！");
				 return;
			 } else {
				 var x=t.insertRow(rows);
				 var one=x.insertCell(0);
				 one.className="textL";
				 var two=x.insertCell(1);
				 one.innerHTML='<input type="file" id="file" class="checkFile" name="files" />';
				 two.innerHTML='<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
			 }
		 }
	  }
	
	function delRow(obj) { 
		var childNode=obj.parentNode.parentNode;
		var parentNode=obj.parentNode.parentNode.parentNode;
		parentNode.removeChild(childNode);
	}
	
	function goMedium(category){
		if(category == "material"){
			var id = $$('mediumMaterial').value;
			var url_link=contextpath+'works/artWorks.do?action=MEDIUM&record.id='+id+"&record.mediumCategory=1";
		    openWindow("选择媒介",url_link,400,500);
		}
		if(category == "shape"){
			var id = $$('mediumShape').value;
			var url_link=contextpath+'works/artWorks.do?action=MEDIUM&record.id='+id+"&record.mediumCategory=2";
		    openWindow("选择媒介",url_link,400,500);
		}
		if(category == "materialC"){
			$$('mediumMaterial').value = "";
			$$('mediumMaterialName').value = "";
		}
		if(category == "shapeC"){
			$$('mediumShape').value = "";
			$$('mediumShapeName').value = "";
		}
	}
	
	function clear(name){
		$$(name).value="";
	}
	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="artWorks" id="artWorksForm" method="post" namespace="/works" enctype="multipart/form-data">
			<s:hidden name="record.worksNo"/>
			<s:hidden name="record.repeatMarker" value="0"/>
			<div id="tblForm"></div>
			<div id="page_0" style="width:100%">
			<div id="formDiv" class="formDiv" style="padding:2px;">
			<table cellpadding="0" cellspacing="0" class="formTable">
				<tr>
					<td width="15%" class="textR"><font color="red">*</font>作品编号：</td>
					<td width="35%"><s:textfield name="record.worksNo" disabled="true"/></td>
					<td width="15%" class="textR"><font color="red">*</font>艺术家：</td>
					<td width="35%" class="textL">
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					          <td>
								<s:textfield readonly="true" name="record.artArtistName"  />
	  	                        <s:hidden name="record.artistId" id="artistId"/>
  	                         </td>
					         <td width="45px">
  	                        	<gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtArtist()">选择</gl:button>
  	                        </td>
  	                        </tr>
  	                     </table>
  	                </td>
				</tr>
				<tr>
					<td width="10%" class="textR"><font color="red">*</font>作品名称：</td>
					<td width="20%"><s:textfield name="record.worksCName"/></td>
					<td width="10%" class="textR">英文名称：</td>
					<td width="20%"><s:textfield name="record.worksEName"/></td>
				</tr>
				<tr>
					<td width="15%" class="textR">创作时间：</td>
					<td width="35%" class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td><s:textfield name="record.createYear"  placeholder="xxxx" /></td>
					            <td width="10%" class="textC">年</td>
					            <td><s:textfield name="record.createMonth" placeholder="xx"   /></td>
					            <td width="10%" class="textC">月</td>
					            <td><s:textfield name="record.createDay" placeholder="xx"  /></td>
					            <td width="10%" class="textC">日</td>
					        </tr>
					    </table>
					</td>
					<td width="15%" class="textR">创作时间段：</td>
					<td width="35%"  class="textL">
						<s:textfield name="record.createFrom" cssStyle="width:30%;"/>&nbsp;年
						&nbsp;&nbsp;&nbsp;~&nbsp;&nbsp;&nbsp;
						<s:textfield name="record.createTo" cssStyle="width:30%;"/>&nbsp;年
					</td>
				</tr>
				<tr>
					<td class="textR">尺寸（cm）：</td>
					<td class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td><s:textfield name="record.sizeCmLength" placeholder="长"/></td>
					            <td width="10%" class="textC">X</td>
					            <td><s:textfield name="record.sizeCmWidth" placeholder="宽"/></td>
					            <td width="10%" class="textC">X</td>
					            <td><s:textfield name="record.sizeCmHeight" placeholder="高"/></td>
					            <td width="10%" class="textC"></td>
					        </tr>
					    </table>
					</td>
					<td class="textR">尺寸（in）：</td>
					<td class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td><s:textfield name="record.sizeInLength" placeholder="长"/></td>
					            <td width="10%" class="textC">X</td>
					            <td><s:textfield name="record.sizeInWidth" placeholder="宽"/></td>
					            <td width="10%" class="textC">X</td>
					            <td><s:textfield name="record.sizeInHeight" placeholder="高"/></td>
					            <td width="10%" class="textC"></td>
					        </tr>
					    </table>
					</td>
				</tr>
				<tr>
					<td class="textR">尺寸（尺）：</td>
					<td class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td><s:textfield name="record.sizeRuleLength" placeholder="长"/></td>
					            <td width="10%" class="textC">X</td>
					            <td><s:textfield name="record.sizeRuleWidth" placeholder="宽"/></td>
					            <td width="10%" class="textC">X</td>
					            <td><s:textfield name="record.sizeRuleHeight" placeholder="高"/></td>
					            <td width="10%" class="textC"></td>
					        </tr>
					    </table>
					</td>
					<td class="textR">社会功能：</td>
					<td class="textL">
					<s:checkboxlist list="codeSets.SOCIAL_FUNCTION" listKey="value" listValue="codeName" name="record.socialFunction" cssStyle="width:20px;"/>
					</td>
				</tr>
				<tr>
					<td class="textR">创作方式：</td>
					<td class="textL">
					<s:checkboxlist list="codeSets.CREATIVE_WAY" listKey="value" listValue="codeName" name="record.createStyle" cssStyle="width:20px;"/>
					</td>
					<td class="textR">假画：</td>
					<td class="textL">
					<s:radio list="codeSets.YES_OR_NO" listKey="value" listValue="codeName" name="record.fakePaintings" value="0" cssStyle="width:20px;"/>
					</td>
				</tr>
				<tr>
					<td>局部尺寸：</td>
					<td colspan="3" class="textL">
					<table  id="tblUpload" cellpadding="0" cellspacing="0" width="100%"  class="tbnBtnStyle" align="left">
					   <tr>
						   <td class="textL">
						       <input type="text" class="partSize_l" name="record.partSize_name" style="width:20%;" placeholder="位置名称" />&nbsp;&nbsp;:&nbsp;&nbsp;
						       <input type="text" class="partSize_l" name="record.partSize_l" style="width:20%;" placeholder="长"  />&nbsp;&nbsp;&nbsp;X&nbsp;&nbsp;
							   <input type="text" class="partSize_l" name="record.partSize_w" style="width:20%;" placeholder="宽"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							   <input type="text" class="partSize_l" name="record.partSize_unit" style="width:20%;" placeholder="尺寸单位"  />
						   </td>
					      <td width="10%"><input type="button" style="width:50px;" value="增加" onclick="addFileRow('tblUpload')"/></td>
					   </tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="textR">媒介材料：</td>
					<td class="textL">
					    <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield readonly="true" name="record.mediumMaterialName" id="mediumMaterialName"/>
  	                               <s:hidden name="record.mediumMaterial" id="mediumMaterial" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goMedium('material')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goMedium('materialC')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
  	                </td>
					<td class="textR">媒介形式：</td>
					<td class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield readonly="true" name="record.mediumShapeName" id="mediumShapeName"/>
  	                        		<s:hidden name="record.mediumShape" id="mediumShape" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goMedium('shape')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goMedium('shapeC')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
  	                </td>
				</tr>
				<tr>
					<td class="textR" valign="top">媒介材料说明：</td>
					<td valign="top">
						<s:textarea style="height:80px;" name="record.materialRemark"  />
					</td>
					<td class="textR" valign="top">媒介形式说明：</td>
					<td valign="top">
						<s:textarea style="height:80px;" name="record.shapeRemark"/>
					</td>
				</tr>
				<tr>
					<td width="15%" class="textR">作品系列：</td>
					<td width="35%" class="textL">
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield  name="record.worksSeriesName" id="worksSeriesName"/>
  	                    			<s:hidden name="record.worksSeries" id="worksSeries" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtWorksSeries('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtWorksSeries('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
					</td>
					<td></td><td></td>
				</tr>
				<tr>
					<td width="15%" class="textR">签名位置1：</td>
					<td width="35%"><s:select list="codeSets.SIGNATURE" listKey="value" listValue="codeName" name="record.signature"/></td>
					<td width="15%" class="textR">签名内容1：</td>
					<td width="35%"><s:textfield name="record.signatureContent"/></td>
				</tr>
				<tr>
					<td width="15%" class="textR">签名位置2：</td>
					<td width="35%"><s:select list="codeSets.SIGNATURE" listKey="value" listValue="codeName" name="record.signature2"/></td>
					<td width="15%" class="textR">签名内容2：</td>
					<td width="35%"><s:textfield name="record.signatureContent2"/></td>
				</tr>
				<tr>
					<td width="15%" class="textR">签名位置3：</td>
					<td width="35%"><s:select list="codeSets.SIGNATURE" listKey="value" listValue="codeName" name="record.signature3"/></td>
					<td width="15%" class="textR">签名内容3：</td>
					<td width="35%"><s:textfield name="record.signatureContent3"/></td>
				</tr>
				<tr>
					<td width="15%" class="textR">附件：</td>
					<td colspan="3" class="textL">
					<table  id="tblUpload1" cellpadding="0" cellspacing="0" width="100%"  class="tbnBtnStyle" align="left">
					   <tr>
						   <td class="textL">
							   <input type="file" name="files" id="file" class="checkFile" />
						   </td>
					      <td width="10%"><input type="button" style="width:50px;" value="增加" onclick="addFileRow('tblUpload1')"/></td>
					   </tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="textR">图片来源：</td>
					<td class="textL"><s:select list="codeSets.PHOTO_SOURE" listKey="value" listValue="codeName" name="record.photoSoure"/></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td class="textR">创作地点：</td>
					<td colspan="3"  class="textL">
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield name="record.createCountryName" id="createCountryName"/>
	  	               				<s:hidden name="record.createCountry" id="createCountry" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtCountry('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtCountry('cancel')">清空</gl:button>
					            </td>
					            <td>
					            	<s:textfield name="record.createPlace"/>
					            </td>
					        </tr>
					    </table>
					</td>
				</tr>
				<tr>
					<td class="textR">作品题材：</td>
					<td colspan="3" class="textL">
						<table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield name="record.worksThemeName1" id="worksThemeName1" onchange="clear('worksTheme1')"/>
  	                    			<s:hidden name="record.worksTheme1" id="worksTheme1"/>
  	                            </td>
					            <td>
					               <s:textfield name="record.worksThemeName2" id="worksThemeName2" onchange="clear('worksTheme2')"/>
  	                    			<s:hidden name="record.worksTheme2" id="worksTheme2" />
  	                            </td>
					            <td>
					               <s:textfield name="record.worksThemeName3" id="worksThemeName3" onchange="clear('worksTheme3')"/>
  	                    			<s:hidden name="record.worksTheme3" id="worksTheme3" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtWorksTheme('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtWorksTheme('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
					</td>
				</tr>
				<tr>
					<td class="textR">风格类型：</td>
					<td colspan="3" class="textL">
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield name="record.worksStyle" id="worksStyle" onchange="clear('styleType')"/>
	  	               				<s:hidden name="record.styleType" id="styleType" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtWorksStyle('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtWorksStyle('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
  	                </td>
				</tr>
				<tr>
					<td width="15%" class="textR">内容关键词：</td>
					<td width="35%" colspan="3" class="textL">
						<s:textfield name="record.keywordAddr" style="width:30%" placeholder="地点" />
						<s:textfield name="record.keywordCharacter" style="width:30%" placeholder="人物" />
						<s:textfield name="record.keywordEvent" style="width:30%" placeholder="事件" />
						<s:textfield name="record.keywordThing" style="width:30%" placeholder="物件" />
						<s:textfield name="record.keywordOther1" style="width:30%" placeholder="……" />
						<s:textfield name="record.keywordOther2" style="width:30%" placeholder="……" />
					</td>
				</tr>
				
				<tr>
					<td class="textR" valign="top">作品描述：</td>
					<td colspan="3" valign="top">
						<s:textarea style="height:160px;" name="record.worksStatus" placeholder="材质的补充说明，色彩特点，风格、技法、创作内容及背景，原因初衷，作品地位…"  />
					</td>
				</tr>
		 	</table>
		 </div>
		 </div>
		 <div id="page_1" style="padding:1px;"> </div>
</s:form>

<div id="btnDiv" class="btnDiv">
	<gl:button name="btnAdd" onClick="goSubmit()" >保存</gl:button>
     &nbsp;
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
	
	/* var tabbar = new dhtmlXTabBar("tblForm", "top");
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
    tabbar.tabs("a1").disable();
    tabbar.tabs("a2").disable();
    tabbar.tabs("a3").disable();
    tabbar.tabs("a4").disable();
    tabbar.tabs("a5").disable();
    tabbar.tabs("a7").disable();
    tabbar.tabs("a8").disable();
    tabbar.tabs("a9").disable();
    tabbar.tabs("a10").disable();
    tabbar.tabs("a11").disable();
    tabbar.tabs("a12").disable(); */

    fDiv.style.height=btbl.offsetHeight-40+"px";
</script>
</body>
</html>