<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">
	var fulllink = contextpath + "artist/ArtistInformation.do";	
	
	var ret=new Object();

	function goCancel(){
		parent.closedialog('false');
		
	}

	function goSubmit() {
		if(checkFormInput()){
		    var url =fulllink+"?action=SAVE";
		  	document.forms[0].action=url;
			document.forms[0].submit();
		}
	}
	
	function checkFormInput(){
		if(!checkString(getElement('form.record.cName'),"中文名",30,true)) return;
		if(!checkString(getElement('form.record.eName'),"英文名",30,true)) return;
		if(!checkString(getElement('form.record.sex'), "性别", 30, true)) return;
		//if(!checkString(getElement('form.record.nhom'), "现居住工作", 30, true)) return;
		 if(!checkNumeric(getElement('form.record.birthYear'),"生(年)",4,true,false)) return;
		 if(!checkNumeric(getElement('form.record.birthMonth'),"生(月)",2,true,false)) return;
		 if(!checkNumeric(getElement('form.record.birthDay'),"生(日)",2,true,false)) return;
		 if(!checkNumeric(getElement('form.record.deathYear'),"卒(年)",4,true,false)) return;
		 if(!checkNumeric(getElement('form.record.deathMonth'),"卒(月)",2,true,false)) return;
		 if(!checkNumeric(getElement('form.record.deathDay'),"卒(日)",2,true,false)) return;
		return true;
	}
	
	function checkFile() {
		var isPass = true;
		$(".checkFile").each(function(){
			if(!checkString(this,"相片",60,true)) {
				isPass = false;
				return false;
			}
			var format = this.value.split(".");
			var i = format.length;
			if(format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png"){
				alert("只能上传图片格式！");
				isPass = false;
				return false;
			}
		});
		return isPass;
	}
	
	function closedialog(ret) {
		var res=ret['ret'];
		var birthCountry=ret['birthCountry'];
		var country=ret['country'];
		var place=ret['place'];
		if (ret == 'true') {
			goSearch();
		}
		if(birthCountry==true){
	    	 getElement('record.birthCountry').value=ret['str'];
	    	 getElement('record.birthCountryName').value=ret['str1'];
	     }
		if(country==true){
	    	 getElement('record.nationality').value=ret['str'];
	    	 getElement('record.nationalityName').value=ret['str1'];
	     }
		editMode="";
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		calendar1 = initCalendar("birthdate","imgBirthdate");
	}

	function goArtCountry(type){
		var ids;
		var type;
		if(type == '1'){
			ids = $$("birthCountry").value;
			type="1";
		}
		if(type == '2'){
			ids = $$("nationality").value;
			type="2";
		}
		var url_link=contextpath+'system/artCountry.do?ids='+ids+"&record.type="+type+"&query.parameters.type=1";
	    openWindow("选择国家",url_link,0.8,0.8);
	}
	
	function delArtCountry(id,name){
		document.getElementById(id).value="";
		document.getElementById(name).value="";
	}
	
	</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtistInformation" id="ArtistInformationForm" method="post" namespace="/artist" enctype="multipart/form-data">
			<s:hidden name="record.id"/>
			<div id="tblForm"></div>
			<div id="page_0"  style="padding:2px; height:100%; width:100%;">
			<div id="formDiv" class="formDiv" style="padding:2px;">
				<table cellpadding="0" cellspacing="0" class="formTable" style="width: 99%;height:90%">
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>中文名：</td>
					<td width="35%" ><s:textfield name="form.record.cName"/></td>
					<td class="textR" width="15%" ><font color="red">*</font>英文名：</td>
					<td width="35%"><s:textfield name="form.record.eName" /></td>
				</tr>
				
				<tr>
					<td class="textR" width="15%" ><font color="red">*</font>性别：</td>
					<td ><s:select list="codeSets.GENDER" listKey="value" listValue="codeName" name="form.record.sex"/></td>
					<td/><td/>
				</tr>
				<tr>	
					<td class="textR" width="15%">生日期：</td>
					<td class="textL">
						<s:textfield name="form.record.birthYear" cssStyle="width:80px;" placeholder="xxxx" id="birthYear"  />年 
						<s:textfield name="form.record.birthMonth" cssStyle="width:50px;" placeholder="xx" id="birthMonth"  />月
						<s:textfield name="form.record.birthDay" cssStyle="width:50px;" placeholder="xx" id="birthDay"  />日
					</td>
					<td class="textR" width="15%">卒日期：</td>
					<td class="textL">
						<s:textfield name="form.record.deathYear" cssStyle="width:80px;" placeholder="xxxx" id="deathYear"  />年 
						<s:textfield name="form.record.deathMonth" cssStyle="width:50px;" placeholder="xx" id="deathMonth"  />月
						<s:textfield name="form.record.deathDay" cssStyle="width:50px;"  placeholder="xx"  id="deathDay"  />日
					</td>
				</tr>
				<tr>
					<td class="textR">出生国家：</td>
					<td class="textL">
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
								<s:textfield readonly="true" name="record.birthCountryName" id="birthCountryName"/>
		  	                    <s:hidden name="record.birthCountry" id="birthCountry"/>
		  	                    </td>
					            <td width="45px">
  	                    			<gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtCountry('1')">选择</gl:button>
  	                    		 </td>
					            <td width="45px">
  	                    			<gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="delArtCountry('birthCountry','birthCountryName')">清空</gl:button>
  	                    		 </td>
					        </tr>
					    </table>
					</td>
				 	<td class="textR">出生地点：</td>
					<td ><s:textfield name="form.record.birthplace"/></td>
				</tr>
				<tr>
					<td class="textR"  >祖籍：</td>
					<td class="textL">
						<s:textfield name="form.record.ancestralHome"/>
					</td>
					<td class="textR" >国籍：</td>
					<td class="textL">
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
								<s:textfield readonly="true" name="record.nationalityName" id="nationalityName"/>
		  	                    <s:hidden name="record.nationality" id="nationality" />
		  	                    </td>
		  	                    <td width="45px">
  	                    			<gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtCountry('2')">选择</gl:button>
  	                    		</td>
  	                    		<td width="45px">
					            	<gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="delArtCountry('nationality','nationalityName')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
					</td>
				</tr>
				<tr>
				    <td class="textR" >星座：</td>
					<td><s:select list="codeSets.ZODIAC" listKey="value" listValue="codeName" name="form.record.zodiac"/></td>
				    <td class="textR">现居住工作地：</td>
					<td class="textL"><s:textfield name="form.record.nhom" /></td>
				</tr>
				<tr>
				    <td class="textR" >艺术家时期类型：</td>
					<td><s:select list="codeSets.ARTIST_TYPE" listKey="value" listValue="codeName" name="form.record.artistType"/></td>
					<td class="textR" >主要创作媒介：</td>
					<td class="textL"><s:checkboxlist list="codeSets.MEDIUM_TYPE" listKey="value" listValue="codeName" name="form.record.mainMedia" cssStyle="width:20px;"/></td>
				</tr>
				<tr>
					<td class="textR" >相片显示：</td>
					<td class="textL" colspan="3">
						<input type="file" name="files" id="file" class="checkFile" />
					</td>
				</tr>
				<tr>
					<td class="textR" >个人主页：</td>
					<td colspan="3" ><s:textarea name="form.record.personalPage" style="height:40px" placeholder="有多个可以以、分隔开" ></s:textarea></td>
				</tr>
				<tr>
					<td class="textR" valign="top">简介：</td>
					<td colspan="3" ><s:textarea name="form.record.cResume" style="height:60px" ></s:textarea></td>
			    </tr>
		 	</table>
		  </div>
		 </div>
</s:form>

<div id="btnDiv" class="btnDiv">
     <gl:button name="btnAdd" onClick="goSubmit()" >下一步</gl:button>&nbsp;
	 <gl:button name="btnAdd" onClick="goCancel()" >取消</gl:button>
</div>

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
	   fDiv.style.width=mainbody.offsetWidth-fDiv.offsetLeft*2+"px";
	   bDiv.style.width=mainbody.offsetWidth-bDiv.offsetLeft*2+"px";
	  fDiv.style.height=mainbody.offsetHeight-bDiv.offsetHeight-fDiv.offsetTop*2+"px";
	   bDiv.style.top=fDiv.offsetHeight+fDiv.offsetTop*2-5+"px";
	   if(btbl){
		   btbl.style.width=fDiv.offsetWidth-fDiv.offsetLeft-9+"px";
		   btbl.style.height=fDiv.offsetHeight-fDiv.offsetTop-12+"px";
	   }
	}
	goResize();   
    
	var tabbar = new dhtmlXTabBar("tblForm", "top");
	tabbar.setSkin('dhx_terrace');
	tabbar.setArrowsMode("auto");
	tabbar.addTab("a0", "基本信息", "100px");
	tabbar.addTab("a1", "教育与工作", "130px");
	tabbar.addTab("a2", "获奖与荣誉", "100px");
	tabbar.addTab("a3", "收藏机构", "100px");
	tabbar.addTab("a4", "收藏家", "100px");
	tabbar.addTab("a5", "捐赠", "80px");
	tabbar.addTab("a6", "合作机构", "100px");
	tabbar.addTab("a7", "人生年表", "100px");
	tabbar.addTab("a8", "作品系列", "100px");
	tabbar.tabs("a0").attachObject("formDiv");
	tabbar.tabs("a0").setActive();
	tabbar.tabs("a1").disable();
	tabbar.tabs("a2").disable();
	tabbar.tabs("a3").disable();
	tabbar.tabs("a4").disable();
	tabbar.tabs("a5").disable();
	tabbar.tabs("a6").disable();
	tabbar.tabs("a7").disable();
	tabbar.tabs("a8").disable();
	//tabbar.setSize("800", "600");
</script>
</body>
</html>