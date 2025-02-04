<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@include file="/common/header.jsp"%>
<style type="text/css">
html, body { width: 100%; height: 100%; }
</style>
</head>
<body>
	<div id="div_left" style="background-color: #ffffff; height: 100%; width: 100%;"></div>

	<script type="text/javascript">

		var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
    	dhxLayout.setSkin("dhx_terrace");
    	var panel=dhxLayout.cells("a");
    	panel.hideHeader();
    	panel.attachObject("div_left");
    	panel.setWidth(250);
    	
		var tree = new dhtmlXTreeObject("div_left","100%","100%",0);
		tree.setImagePath("<%=CONTEXT_PATH%>dhtmlx/imgs/dhxtree_terrace/");
		tree.attachEvent("onClick", function(id){
			viewCodesetGrid(id);
		});
    	tree.setSkin('dhx_terrace');
		tree.setXMLAutoLoading("<%=CONTEXT_PATH%>artist/ArtistInformation.do?action=BIRTHPLACE");
		tree.loadXML("<%=CONTEXT_PATH%>artist/ArtistInformation.do?action=BIRTHPLACE&id=0");
		function viewCodesetGrid(id){
			var areaCode = tree.getUserData(id, "areaCode");
			var areaName = tree.getUserData(id, "areaName");
			var ret=new Object();
			ret['ret']=true;
			ret['str1']=areaCode;
			ret['str2']=areaName;
			parent.closedialog(ret);
		}

	</script>
</body>
</html>