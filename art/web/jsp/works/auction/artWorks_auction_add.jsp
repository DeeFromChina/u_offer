<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp" %>
<script language="javascript">
	var contextpath = "<%=CONTEXT_PATH%>";
	
	var fulllink = contextpath + "auction/ArtAuction.do";	
	
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
		//if(!checkString(getElement('form.record.auction'),"拍卖会",100,false)) return;
		//if(!checkString(getElement('form.record.auctionNo'),"拍卖编号",50,false)) return;
		//if(!checkString(getElement('form.record.auctionIdentity'),"标识",100,false)) return;
		if(!checkString(getElement('form.record.auctionTime'),"拍卖时间",30,true)) return;
		//if(!checkString(getElement('form.record.auctionDesc'),"拍卖说明",500,false)) return;
		if(!checkString(getElement('form.record.worksName'),"作品",100,true)) return;
		if(!checkString(getElement('form.record.housesName'),"拍卖行",100,true)) return;
		//if(!checkString(getElement('form.record.worksSource'),"作品来源",200,false)) return;
		//if(!checkString(getElement('form.record.autionSeason'),"季节",30,false)) return;
		//if(!checkString(getElement('form.record.createyear'),"创作年份",30,true)) return;
		if(!checkNumeric(getElement('form.record.cTranPrice'),"成交价(人民币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.cTradePrice'),"交易价(人民币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.cLowestPrice'),"最低估价(人民币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.cHighestPrice'),"最高估价(人民币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.dTranPrice'),"成交价(美元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.eTranPrice'),"成交价(欧元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.pTranPrice'),"成交价(英镑)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.dTradePrice'),"交易价(美元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.eTradePrice'),"交易价(欧元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.pTradePrice'),"交易价(英镑)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.dLowestPrice'),"最低估价(美元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.eLowestPrice'),"最低估价(欧元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.pLowestPrice'),"最低估价(英镑)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.dHighestPrice'),"最高估价(美元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.eHighestPrice'),"最高估价(欧元)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.pHighestPrice'),"最高估价(英镑)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.hkTranPrice'),"成交价(港币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.hkTradePrice'),"交易价(港币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.hkLowestPrice'),"最低估价(港币)",20,true,false)) return;
		if(!checkNumeric(getElement('form.record.hkHighestPrice'),"最高估价(港币)",20,true,false)) return;
		return true;
	}
	
	function checkFile() {
		var isPass = true;
		$(".checkFile").each(function(){
			if(!checkString(this,"视频",400,true)) {
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
		if(res==true) {
			if(editMode=="WORKS") {
				getElement('form.record.worksId').value = ret['str1'];
				getElement('form.record.worksName').value = ret['str2'];
			}
			if(editMode=="HOUSESLIST") {
				getElement('form.record.auctionHousesId').value = ret['str1'];
				var name = ret['str2'];
				name = name.split("^")[0];
				getElement('form.record.housesName').value = name;
			}
		}
		editMode="";
	}

	function goWorksName(){
		var url_link = contextpath + 'works/artWorks.do?query.parameters.type=2';
	    openWindow("选择作品",url_link,0.8,0.8);	
	    editMode="WORKS";
	}
	
	function goAuctionHouses(){
		editMode="HOUSESLIST";
		var url_link = contextpath + 'auction/ArtAuctionHouses.do?query.parameters.type=2';
	    openWindow("选择拍卖行",url_link,0.8,0.8);	
	}
	
	function init(){
		showMessage('<s:property value="errorMessage" escape="false"/>');
		calendar = initCalendar("auctionTime","imgAuctionTime");
	}
	
	
	function addFileRow() {
		var t = document.getElementById('tblUpload');
		var rows = t.rows.length;
		if (rows >= 3) {
			alert("超过最大上传文件数！");
			return;
		} else {
			var x = t.insertRow(rows);
			var one = x.insertCell(0);
			one.className = "textL";
			var two = x.insertCell(1);
			one.innerHTML = '<input type="file" id="file" class="checkFile" name="files" />';
			two.innerHTML = '<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
		}
	}
	
	function delRow(obj) { 
		var childNode=obj.parentNode.parentNode;
		var parentNode=obj.parentNode.parentNode.parentNode;
		parentNode.removeChild(childNode);
	}
	
</script>
<style type="text/css"> 
   html, body {width:100%; height:100%; margin:0px; padding:0px; overflow:hidden;}
</style>
</head>
 
<body onload="init()">
	<s:form action="ArtAuction" id="ArtWorksAuctionForm" method="post" namespace="/auction" enctype="multipart/form-data">
			<s:hidden name="record.id"/>
			<div id="tblForm"></div>
			<div id="page_0"  style="padding:2px; height:100%; width:100%;">
			<div id="formDiv" class="formDiv" style="padding:2px;">
							<table cellpadding="0" cellspacing="0" class="formTable" style="width: 99%;">
				<col width="15%"/>
				<col width="15%"/>
				<col width="20%"/>
				<col width="15%"/>
				<col width="15%"/>
				<col width="20%"/>
				<tr>
					<td class="textR"  >拍卖编号：</td>
					<td  colspan="2"><s:textfield name="form.record.auctionNo" /></td>
					<td class="textR"  ><font color="red">*</font>作品：</td>
					<td  colspan="2" class="textL">
					 <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					    <tr>
					       <td>
							<s:textfield name="form.record.worksName" readonly="true"/>
							<s:hidden name="form.record.worksId" />
						   </td>
						   <td width="45px">
							<gl:button name="btnBirthPlace" styleClass="btnFormStyle" onClick="goWorksName()">选择</gl:button>
							</td>
  	                        </tr>
  	                    </table>    
					</td>
				</tr>
				
				<tr>
				    <td class="textR"  >拍卖会：</td>
					<td   colspan="2"><s:textfield name="form.record.auction"/></td>
					<td class="textR"  >拍场：</td>
					<td  colspan="2" ><s:textfield name="form.record.saleName"/></td>
				</tr>
				<tr>
					
					<td class="textR"  ><font color="red">*</font>拍卖行：</td>
					<td  colspan="2" class="textL">
					 <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					    <tr>
					       <td>
							<s:textfield name="form.record.housesName" readonly="true"/>
							<s:hidden name="form.record.auctionHousesId" />
							</td>
						    <td width="45px">
							<gl:button name="btnBirthPlace" styleClass="btnFormStyle" onClick="goAuctionHouses()">选择</gl:button>
							</td>
  	                     </tr>
  	                  </table>    
					</td>
					<td class="textR"  ><font color="red">*</font>拍卖时间：</td>
					<td  colspan="2" class="textL">
					 <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
					    <tr>
					       <td>
							<s:textfield name="form.record.auctionTime" readonly="true" id="auctionTime"/> 
							</td>
						    <td width="15px">
							<img id="imgAuctionTime" style="cursor:pointer;" src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif"/>
							</td>
  	                     </tr>
  	                  </table>    
					</td>
				</tr>
				<tr>
					<td class="textR"  >标识：</td>
					<td  colspan="2" ><s:textfield name="form.record.auctionIdentity"/></td>
					<td class="textR"  >季节：</td>
					<td  colspan="2"><s:select list="codeSets.AUCTION_SEASON" listKey="value" listValue="codeName" name="form.record.autionSeason"/></td>
				</tr>
				<tr>
					<td class="textR"  >作品来源：</td>
					<td colspan="2"><s:textfield name="form.record.worksSource"/></td>
					<td class="textR"  >创作时期：</td>
					<td  colspan="2" class="textL"><s:textfield name="form.record.createYear"  cssStyle="width:30%" placeholder="xxxx年"/>&nbsp;<s:textfield  cssStyle="width:30%"  name="form.record.createMonth" placeholder="xx月"/>&nbsp;<s:textfield cssStyle="width:30%"  name="form.record.createDay" placeholder="xx日"/></td>
				</tr>
				<tr>
					<td class="textR"  >版数：</td>
					<td colspan="2" class="textL"><s:textfield name="form.record.versionNum"/></td>
					<td class="textR"  >成交状态：</td>
					<td colspan="2" class="textL"><s:select list="codeSets.TRAN_STATUS" listKey="value" listValue="codeName" name="record.tranStatus" /></td>
				</tr>
				
				<tr>
					<td class="textR" valign="top">拍卖说明：</td>
					<td colspan="5" ><s:textarea name="form.record.auctionDesc" style="height:60px" /></td>
				</tr>
				
				<tr>
					<td rowspan="5" class="textR">成交价：</td>
					<td class="textR">(人民币)</td>
					<td class="textL"><s:textfield name="form.record.cTranPrice" /></td>
				
					<td rowspan="5" class="textR">交易价：</td>
					<td class="textR">(人民币)</td>
					<td class="textL"><s:textfield name="form.record.cTradePrice" /></td>
				</tr>
				<tr>
					<td  class="textR">(美元)</td>
					<td  class="textL"><s:textfield name="form.record.dTranPrice" /></td>
					<td  class="textR">(美元)</td>
					<td  class="textL"><s:textfield name="form.record.dTradePrice" /></td>
				</tr>
				
				<tr>
					<td  class="textR">(欧元)</td>
					<td  class="textL"><s:textfield name="form.record.eTranPrice" /></td>
					<td  class="textR">(欧元)</td>
					<td  class="textL"><s:textfield name="form.record.eTradePrice" /></td>
				</tr>
				
				<tr>
					<td  class="textR">(英镑)</td>
					<td  class="textL"><s:textfield name="form.record.pTranPrice" /></td>
					<td  class="textR">(英镑)</td>
					<td  class="textL"><s:textfield name="form.record.pTradePrice" /></td>
				</tr>
				<tr>
					<td  class="textR">(港币)</td>
					<td  class="textL"><s:textfield name="form.record.hkTranPrice" /></td>
					<td  class="textR">(港币)</td>
					<td  class="textL"><s:textfield name="form.record.hkTradePrice" /></td>
				</tr>
				
				<tr>
					<td rowspan="5" class="textR">最低估价：</td>
					<td class="textR">(人民币)</td>
					<td class="textL"><s:textfield name="form.record.cLowestPrice" /></td>
				
					<td rowspan="5" class="textR">最高估价：</td>
					<td class="textR">(人民币)</td>
					<td class="textL"><s:textfield name="form.record.cHighestPrice" /></td>
				</tr>		
				
				<tr>
					<td  class="textR">(美元)</td>
					<td  class="textL"><s:textfield name="form.record.dLowestPrice" /></td>
					<td  class="textR">(美元)</td>
					<td  class="textL"><s:textfield name="form.record.dHighestPrice" /></td>
				</tr>
				<tr>
					<td  class="textR">(欧元)</td>
					<td  class="textL"><s:textfield name="form.record.eLowestPrice" /></td>
					<td  class="textR">(欧元)</td>
					<td  class="textL"><s:textfield name="form.record.eHighestPrice" /></td>
				</tr>
				<tr>
					<td  class="textR">(英镑)</td>
					<td  class="textL"><s:textfield name="form.record.pLowestPrice" /></td>
					<td  class="textR">(英镑)</td>
					<td  class="textL"><s:textfield name="form.record.pHighestPrice" /></td>
				</tr>
				<tr>
					<td  class="textR">(港币)</td>
					<td  class="textL"><s:textfield name="form.record.hkLowestPrice" /></td>
					<td  class="textR">(港币)</td>
					<td  class="textL"><s:textfield name="form.record.hkHighestPrice" /></td>
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
	 	   btbl.style.width=mainbody.offsetWidth-9+"px";
		   btbl.style.height=mainbody.offsetHeight-40+"px";
		   bDiv.style.width=mainbody.offsetWidth-10+"px";
		   bDiv.style.top=btbl.offsetHeight+5+"px";
		}
		goResize();
	
	/* function goResize(){
	   fDiv.style.width=mainbody.offsetWidth-fDiv.offsetLeft*2+"px";
	   bDiv.style.width=mainbody.offsetWidth-bDiv.offsetLeft*2+"px";
	  fDiv.style.height=mainbody.offsetHeight-bDiv.offsetHeight-fDiv.offsetTop*2+"px";
	   bDiv.style.top=fDiv.offsetHeight+fDiv.offsetTop*2-5+"px";
	   if(btbl){
		   btbl.style.width=fDiv.offsetWidth-fDiv.offsetLeft-9+"px";
		   btbl.style.height=fDiv.offsetHeight-fDiv.offsetTop-12+"px";
	   }
	}
	goResize(); */   
    
	var tabbar = new dhtmlXTabBar("tblForm", "top");
	tabbar.setSkin('dhx_terrace');
	tabbar.setArrowsMode("auto");
	tabbar.addTab("a0", "作品拍卖基本信息", "140px");
	tabbar.addTab("a1", "拍卖相关视频", "120px");
	tabbar.addTab("a2", "拍卖相关文字", "120px");
	tabbar.tabs("a0").attachObject("formDiv");
	tabbar.tabs("a0").setActive();
	tabbar.tabs("a1").disable();
	tabbar.tabs("a2").disable();
	//tabbar.setSize("800", "600");
	
	    fDiv.style.height=btbl.offsetHeight-40+"px";
</script>
</body>
</html>