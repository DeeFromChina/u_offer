<%@ page language="java" contentType="text/html;charset=UTF-8"
	import="com.golead.core.util.Const,com.golead.core.web.UserSession"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<%
   String path = request.getContextPath();
			String CONTEXT_PATH = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%
   UserSession userSession = (UserSession) request.getSession()
					.getAttribute(Const.SESSION);
			String userString = "当前用户： " + userSession.getUserName() + "("
					+ userSession.getAccountName() + ")";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=7;ie=8" />
<link rel="stylesheet" type="text/css"
	href="<%=CONTEXT_PATH%>dhtmlx/dhtmlx.css" />
<link rel="stylesheet" type="text/css"
	href="<%=CONTEXT_PATH%>dhtmlx/dhtmlx_custom.css" />
<script src="<%=CONTEXT_PATH%>dhtmlx/dhtmlx.js"></script>
<script language="javascript" src="<%=CONTEXT_PATH%>js/ctrl_util.js"></script>
<style type="text/css">
html,body {
	width: 100%;
	height: 100%;
	border: none;
	overflow: hidden;
}
/* --------------以下为新增css---------------- */
#topBanner {
	height: 20px;
	position: relative;
	background-color: #aaccee;
}

#container {
	width: 100%;
	height: 100%;
	zoom: 1;
	overflow: hidden;
	position: absolute;
}

#back_frame {
	position: absolute;
	left: 0px;
	top: 0px;
	height: 100%;
	width: 100%;
	display: none;
}
.dhxlayout_base_dhx_terrace div.dhx_cell_layout div.dhx_cell_toolbar_def{
   padding:0px;
}
</style>
<script type="text/javascript">
	var contextpath = "<%=CONTEXT_PATH%>";	
	var imgPath="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/";
	
	var editMode;       
	
	function go(link,title,fName){
		var f="leaf.gif";
		if(fName) f=fName;
		var img="<img src='"+imgPath+f+"' />";  
		
		var t="<font style='font-size:14px; font-weight:bold'>"+title+"</font>";
		dhxToolbar.removeItem("img");
		dhxToolbar.removeItem("title");
		dhxToolbar.addText("img", 0,img);
		dhxToolbar.addText("title", 1,t);
		if(title=='首页'||title=='帮助') centerPanel.hideToolbar(); else centerPanel.showToolbar();
		var cont=window.document.getElementById('container');
		cont.src=contextpath+link;
	}
	
	function changetitle(title){
		var t="&lt;font style='font-size:12px; font-weight:bold'&gt;"+title+"&lt;/font&gt;";
		dhxToolbar.removeItem("title");
		dhxToolbar.addText("title", 0,t);
	}
	
	function showmain(){
		window.location.href='<%=CONTEXT_PATH%>main.jsp';
	}
	
	function logout(){
		if(confirm('是否真的要注销并重新登录?')==true){
			window.location.href='<%=CONTEXT_PATH%>login.do?action=logout';
		}
	}
	
	function checklogin(){
		var url='<%=CONTEXT_PATH%>login.do?action=checklogin';
		var loader=dhtmlxAjax.postSync(url,'');
		var value = loader.xmlDoc.responseText;
		if(value!='OK'){
		alert('您还未登录系统');
			window.location.href='login.jsp';
		}
	}
	   
	function goCategray()  {
		//        var url_link=contextpath+'jsp/demo/standard/stdCategary_list.jsp';
		var url_link=contextpath+'/meta/stdCategory.do';
		editMode=="CATEGRAY";
		openWindow("数据分类管理",url_link,800,500,this);	  
	}
	   
	function goChangePwd()  {
		var url_link=contextpath+'/system/sysUser.do?action=MYPWD';
		editMode=="PWD";
		openWindow("修改密码",url_link,350,240,this);	  
	}
	
	function goProperty()  {
		var url_link=contextpath+'/system/sysProperty.do';
		editMode=="PROPERTY";
		openWindow("首选项",url_link,500,300,this);
	}
	
	function goPlan(){
		alert("数据录入点击事件！");
	}
	
	function goHelp(){
	 
	}
   
</script>
</head>
<body onload="">
	<div id="topBanner" style="display: none;">
		<div id="sysMenu"></div>
	</div>
	<script type="text/javascript">
		var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
		dhxLayout.setSkin("dhx_terrace");
		var centerPanel=dhxLayout.cells("a");
		var menu = centerPanel.attachMenu();
		var menustr="<menu>";
		<gl:menu id="div1" code="A10" title="艺术家管理" img="css/navImg/men.gif">
			<item code="A1001" onclick="go('artist/ArtistInformation.do','艺术家信息管理','3-8.png')" text="艺术家信息管理" img="css/navImg/3-8.png" />
		</gl:menu>
		<gl:menu id="div2" code="A20" title="作品管理" img="css/navImg/1-1.png">
			<item code="A2001" onclick="go('works/artWorks.do?query.parameters.type=1','作品信息管理','1-2.png')" text="作品信息管理" img="css/navImg/1-2.png" />
		</gl:menu>
		<gl:menu id="div3" code="A30" title="拍卖管理" img="css/navImg/5-1.png">
		    <item code="A3001" onclick="go('auction/ArtAuctionHouses.do?query.parameters.type=1','拍卖行管理','3-3.png')" text="拍卖行管理" img="css/navImg/3-3.png" />
			<item code="A3002" onclick="go('auction/ArtAuction.do','拍卖信息管理','5-3.gif')" text="拍卖信息管理" img="css/navImg/5-3.gif" />
		</gl:menu>
		<gl:menu id="div4" code="A40" title="文献管理" img="css/navImg/1-3.png">
			<item code="A4001" onclick="go('literature/ArtLiteratureWords.do?query.parameters.type=1','纸媒文章管理','2-8.png')" text="纸媒文章管理" img="css/navImg/2-8.png" />
			<item code="A4002" onclick="go('literature/ArtLiteratureNetwork.do','网媒文章管理','9-9.png')" text="网媒文章管理" img="css/navImg/9-9.png" />
			<item code="A4003" onclick="go('literature/ArtLiteratureMedium.do','音视频管理','8-8.png')" text="音视频管理" img="css/navImg/8-8.png" />
			<item code="" onclick="go('literature/ArtLiteratureOther.do','其他收录管理','9-9.png')" text="其他收录管理" img="css/navImg/9-9.png" />
		    <item code="" onclick="go('literature/searchEngine.do','文献检索','1-2.gif')" text="文献检索" img="css/navImg/1-2.gif" />
		</gl:menu>
		<gl:menu id="div5" code="A50" title="活动管理" img="css/navImg/1-2.png" >
			<item code="A5001" onclick="go('activity/ArtActivityExhibit.do?record.type=person','个展管理','1-2.png')" text="个展管理" img="css/navImg/1-2.png" />
			<item code="A5002" onclick="go('activity/ArtActivityExhibit.do?record.type=people','群展管理','2-2.gif')" text="群展管理" img="css/navImg/2-2.gif" />
			<item code="A5003" onclick="go('activity/activityAbmb.do','艺博会管理','2-2.gif')" text="艺博会管理" img="css/navImg/2-2.gif" />
		</gl:menu>
		<gl:menu id="div6" code="A60" title="出版物管理" img="css/navImg/1-2.png" >
			<item code="A6001" onclick="go('publication/artPublication.do','出版物信息管理','2-1.gif')" text="出版物信息管理" img="css/navImg/2-1.gif" />
		</gl:menu>
		<gl:menu id="div7" code="A70" title="照片管理" img="css/navImg/1-2.png" >
			<item code="A7001" onclick="go('artist/ArtPhoto.do','照片管理','2-1.gif')" text="照片管理" img="css/navImg/2-1.gif" /> 
		</gl:menu>
		<gl:menu id="div8" code="A80" title="app管理" img="css/navImg/1-2.png" >
			<item code="" onclick="go('appManager/appArtistFollowManager.do','艺术家关注','2-1.gif')" text="艺术家关注" img="css/navImg/2-1.gif" />
			<item code="" onclick="go('appManager/appArtistPageTemplateManager.do','艺术家主页设置','2-1.gif')" text="艺术家主页设置" img="css/navImg/2-1.gif" />
			<item code="" onclick="go('appManager/appWorksFollowManager.do','艺术品关注','2-1.gif')" text="艺术品关注" img="css/navImg/2-1.gif" />
			<item code="" onclick="go('appManager/appUserManager.do','app用户管理','2-1.gif')" text="app用户管理" img="css/navImg/2-1.gif" />
			<item code="" onclick="go('appManager/appCommentaryManager.do','留言管理','2-1.gif')" text="留言管理" img="css/navImg/2-1.gif" /> 
			<item code="" onclick="go('appManager/appFeedbackManager.do','意见反馈','2-1.gif')" text="意见反馈" img="css/navImg/2-1.gif" /> 
			<item code="" onclick="go('appManager/appHomePageManager.do','app首页管理','2-1.gif')" text="app首页管理" img="css/navImg/2-1.gif" /> 
		</gl:menu>
		<gl:menu code="A99" id="div9" title="基础数据管理" img="css/navImg/settings-icon16.png">
		    <item code="A9901" onclick="go('system/sysOrg.do','组织机构管理','7-1.gif')" text="组织机构管理" img="css/navImg/7-1.gif" />
			<item code="A9902" onclick="go('system/sysRole.do','角色权限管理','7-2.gif')" text="角色权限管理" img="css/navImg/7-2.gif" />
			<item code="A9903" onclick="go('system/sysUser.do','用户管理','7-3.gif')" text="用户管理" img="css/navImg/7-3.gif" />
			<item code="A9904" onclick="go('system/sysCode.do','系统代码管理','7-7.gif')" text="系统代码管理" img="css/navImg/7-7.gif" />
            <item code="" onclick="go('system/sysLog.do','系统日志','7-7.gif')" text="系统日志" img="css/navImg/7-7.gif" />
			<item code="" type="separator" />
			<item code="A9905" onclick="go('system/artCountry.do','国家管理','2-1.gif')" text="国家管理" img="css/navImg/2-1.gif" />
			<item code="A9906" onclick="go('system/artAgency.do','机构管理','2-1.gif')" text="机构管理" img="css/navImg/2-1.gif" />
			<item code="A9907"  text="作品媒介" img="css/navImg/1-2.png" >
				<item code="A990701" onclick="go('artist/medium.do?record.mediumCategory=1','媒介材料管理','1-2.png')" text="媒介材料管理" img="css/navImg/1-2.png" />
				<item code="A990702" onclick="go('artist/medium.do?record.mediumCategory=2','媒介形式管理','1-2.png')" text="媒介形式管理" img="css/navImg/1-2.png" />
		    </item>
			<item code="A9908" onclick="go('works/worksTheme.do','作品题材管理','2-1.gif')" text="作品题材管理" img="css/navImg/2-1.gif" />
			<item code="A9909" onclick="go('works/worksStyle.do','作品风格管理','2-1.gif')" text="作品风格管理" img="css/navImg/2-1.gif" />
			<item code="A9910" onclick="go('auction/exchangeRate.do','汇率管理','2-1.gif')" text="汇率管理" img="css/navImg/2-1.gif" />
			<item code="" type="separator" />
			<item code="" onclick="goChangePwd()" text="修改密码" img="css/navImg/8-9.png" />
		</gl:menu>
		menustr+="</menu>";
		menu.loadStruct(menustr);
		menu.attachEvent("onClick", menuclick);
		function menuclick(id){
			eval(menu.getUserData(id,"onclick"));
		} 
	</script>
	<div id="mainFrame">
		<iframe id="container" src="blank.jsp" style="overflow-y: hidden !important; overflow-x: hidden !important; width: 100%; height: 100%;" frameborder="0"></iframe>
	</div>
	<iframe id="back_frame" name="back_frame" src="#" allowtransparency="true" frameborder="0" scrolling="no"></iframe>
	<script type="text/javascript">
		centerPanel.hideHeader();
		centerPanel.attachObject("mainFrame");
		var dhxToolbar = centerPanel.attachToolbar();
		dhxToolbar.addText("title", 0,"");
		centerPanel.hideToolbar();
	</script>
	<%@include file="/common/topDialog.jsp"%>
</body>
</html>