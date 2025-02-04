<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <%@include file="/common/header.jsp" %>

	<script type="text/javascript">
		var fulllink = contextpath + "activity/activityAbmb.do";
		var abmblink = contextpath + "works/ArtWorksAbmb.do";	

		function goCancel() {
			parent.closedialog(false);
		}

		function goArtCountry(type){
			if(type=='choose'){
				editMode="Country";
				var url_link=contextpath+'system/artCountry.do?&record.type=1&query.parameters.type=1';
			    openWindow("选择国家",url_link,0.8,0.8);
			}
			if(type=='cancel'){
				 getElement('form.record.countryId').value="";
		    	 getElement('form.record.countryName').value="";
			}
		}
		
		function closedialog(ret) {
			var res=ret['ret'];
			if(editMode=="Country"){
		    	 getElement('form.record.countryId').value=ret['str'];
		    	 getElement('form.record.countryName').value=ret['str1'];
		     }
			editMode="";
		}

		function goSubmit() {
			  if(!checkString(getElement('form.record.abmbName'),"名称",100,true)) return;
			  if(!checkNumeric(getElement('form.record.abmbNumber'),"届数",10,true,false)) return;			
			  if(!checkNumeric(getElement('form.record.abmbYear'),"开幕年份",4,true,false)) return;
			  if(!checkNumeric(getElement('form.record.abmbMonth'),"开幕月份",2,true,false)) return;
			  document.forms[0].action=fulllink+"?action=UPDATE";
    	      document.forms[0].submit();
		}
		
		  function init(){
			  showMessage('<s:property value="errorMessage" escape="false" />');
			  //hidebtn();
		  }
		  

			function hidebtn(){
				$(".dhxtabbar_tab_text").each(function(){
					this.addEventListener('click',function(){
						if(this.innerHTML != "基本信息"){
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
<s:form action="activityAbmb" id="abmbForm" method="post" namespace="/activity">
  		<s:hidden name="form.record.id" id="id"/>
		<s:hidden name="record.next" id="next"/>
  		<div id="tblForm"></div>
			<div id="page_0"  style="padding:2px; height:100%; width:100%;">
			<div id="formDiv" class="formDiv" style="padding:2px;">
			<table cellpadding="0" cellspacing="0" width="98%" class="formTable">
				<tr>
					<td width="15%" class="textR"><font color="red">*</font>名称：</td>
				  	<td width="35%">
						<s:textfield name="form.record.abmbName" />
				  	</td>
				  	<td width="15%" class="textR">届数：</td>
				  	<td width="35%">
						<s:textfield name="form.record.abmbNumber"  placeholder="填写数字"/>
				  	</td>
				</tr>
				<tr>
			    	<td class="textR">主办方：</td>
				 	<td >
						<s:textfield name="form.record.sponsor" />
				 	</td>
				 	<td class="textR">艺术总监：</td>
				 	<td >
						<s:textfield name="form.record.artDirector" />
				 	</td>
				</tr>
				<tr>
			    	<td class="textR">国家：</td>
				 	<td >
						<table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
					        <tr>
					            <td>
					               <s:textfield  name="form.record.countryName" id="countryName"/>
  	                    			<s:hidden name="form.record.countryId" id="countryId" />
  	                            </td>
					            <td width="45px">
					           	 	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtCountry('choose')">选择</gl:button>
					            </td>
					            <td width="45px">
					            	<gl:button name="btnArtMedium" styleClass="btnFormStyle" onClick="goArtCountry('cancel')">清空</gl:button>
					            </td>
					        </tr>
					    </table>
				 	</td>
				 	<td class="textR">城市：</td>
				 	<td >
						<s:textfield name="form.record.city" />
				 	</td>
				</tr>
				<tr>
			    	<td class="textR">开幕时间：</td>
				 	<td colspan="3" class="textL">
				 	   <s:textfield name="form.record.abmbYear" cssStyle="width:80px" placeholder="xxxx"/>年<s:textfield name="form.record.abmbMonth" cssStyle="width:80px"  placeholder="xx"/>月
					</td>		 	
				</tr>
				
	 		</table>
	 		</div>
	</div>
	<iframe  id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe> 
 </s:form>	
<div id="btnDiv" class="btnDiv">
		<gl:button name="btnAdd" onClick="goSubmit()">保存</gl:button>
		&nbsp;
	   <gl:button name="btnCancel" onClick="goCancel()">取消</gl:button>
</div>	  
<%@include file="/common/dialog.jsp" %>
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
tabbar.addTab("a0", "基本信息", "120px");
tabbar.addTab("a1", "相关作品", "90px");
tabbar.tabs("a0").attachObject("formDiv");
tabbar.tabs("a0").setActive();
tabbar.tabs("a1").attachObject("page_1");
tabbar.enableAutoReSize(true);
tabbar.enableTabCloseButton(true);
var next = $$('next').value;
if(next == '1'){
	tabbar.tabs("a1").setActive();
}else{
    tabbar.tabs("a0").setActive();
}
window.frames["page_1"].location.href=abmblink+'?action=WORKS'+"&record.type=2&record.abmbId="+$$('id').value;
</script>
</body>
</html>