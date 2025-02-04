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
	<s:hidden name="record.mediumCategory" id="mediumCategory"></s:hidden>
	<div id="left_div" style="background-color: #ffffff; height: 100%; width: 100%;"></div>

	<script type="text/javascript">
	
	function closedialog(ret){
	     if(ret=='true') {
	       var nodeId = tree.getSelectedItemId();
	     	 if(editMode=="ADD") {
	          tree.refreshItem(nodeId);
	       }
	       else if(editMode=="EDIT"){
	          tree.closeItem(nodeId);
            var parentId = tree.getParentId(nodeId);
            tree.selectItem(parentId);
	          tree.refreshItem(parentId);
	       }
	     }
	     editMode="";	
   }

	function add(id){
    	editMode="ADD";
    	var pid = id.split("_")[2];
    	var treeCode = id.split("_")[1];
    	var url_link="<%=CONTEXT_PATH%>artist/medium.do?action="+editMode+"&record.treeCode="+treeCode+"&record.pid="+pid+"&record.mediumCategory="+$$('mediumCategory').value;
    	openWindow("增加媒介",url_link,500,150);
    }	

    function modify(id){
    	editMode="EDIT";
    	var url_link="<%=CONTEXT_PATH%>artist/medium.do?action="+editMode+"&record.id="+id.split("_")[2]+"&record.mediumCategory="+$$('mediumCategory').value;
    	openWindow("修改媒介",url_link,500,150);
    }	

    function del(id){
    	var res=confirm("将要删除该媒介以及下属所有媒介\r\n        是否真的要删除该媒介？");
    	if(res==false) return;
    	editMode="DELETE";
    	var url_link="<%=CONTEXT_PATH%>artist/medium.do?action="+editMode+"&record.id="+id.split("_")[2]+"&record.mediumCategory="+$$('mediumCategory').value;
    	var loader = dhx4.ajax.postSync(url_link,"");
    	eval("var ret="+loader.xmlDoc.responseText);
		if(ret.exit=="0") tree.deleteItem(id,true); else alert(ret.message);
    	editMode="";
    	<%-- if(res == true) {
			var data=ajaxSubmit(<%=CONTEXT_PATH%>artist/medium.do?action=DELETE&ids=" + id.split("_")[2]);
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		} --%>
    }
    
    function showMenuItems(id){
        toolbar.disableItem("new");
        toolbar.hideItem("modify");
        toolbar.hideItem("del");
        toolbar.showItem("sep1");
	   	if(id.indexOf("material")>-1 || id.indexOf("shape")>-1){
	   		var treeCode = id.split("_")[1];
	    	if(treeCode == "m1" || treeCode == "s1"){
	    		toolbar.enableItem("new");
	    		return;
	    	}else{
	    		toolbar.enableItem("new");
		        toolbar.showItem("modify");
		        toolbar.showItem("del");
	    	}
   		}
	}
    
		var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
    	dhxLayout.setSkin("dhx_terrace");
    	var leftPanel=dhxLayout.cells("a");
    	leftPanel.hideHeader();
    	leftPanel.attachObject("left_div");
    	
    	var toolbar = leftPanel.attachToolbar();
        toolbar.setIconsPath("<%=CONTEXT_PATH%>dhtmlx/imgs/icon/");
//        toolbar.setSkin("dhx_terrace");
        toolbar.addButton("new", 0, "添加子媒介", "house_big_1.gif", "house_big_1.gif");
        toolbar.addSeparator("sep1", 1);
        toolbar.addButton("modify", 2, "修改", "house_big_2.gif", "house_big_2.gif");
        toolbar.addSeparator("sep2", 3);
        toolbar.addButton("del", 4, "删除", "house_big_3.gif", "house_big_3.gif");
        toolbar.attachEvent("onClick", function(id){
        	var nodeId=tree.getSelectedItemId();
        	if(id=="new") add(nodeId);
        	else if(id=="modify") modify(nodeId);
        	else if(id=="del") del(nodeId);
        });

		var tree = new dhtmlXTreeObject("left_div","100%","100%",0);
		tree.setImagePath("<%=CONTEXT_PATH%>dhtmlx/imgs/dhxtree_terrace/");
		tree.attachEvent("onSelect", function(id){
	    	 showMenuItems(id);
	    });
	    tree.attachEvent("onDblClick", function(id){
	       if(id.indexOf("material")>-1 || id.indexOf("shape")>-1) modify(id);
	    });
	    tree.attachEvent("onMouseIn", function(id){
	    	 var str=tree.getUserData(id,"name");
	    	 tree.setItemText(id,"<font color='#bb4422'>"+str+"</font>");
	    });
	    tree.attachEvent("onMouseOut", function(id){
	    	 var str=tree.getUserData(id,"name");
	    	 tree.setItemText(id,str);
	    });
	    tree.enableCheckBoxes(false);
    	tree.setSkin('dhx_terrace');
    	tree.loadXML("<%= CONTEXT_PATH%>artist/medium.do?action=getTree&id=0&record.mediumCategory="+$$('mediumCategory').value);
		tree.setXMLAutoLoading("<%= CONTEXT_PATH%>artist/medium.do?action=getTree&record.mediumCategory="+$$('mediumCategory').value);
		showMenuItems("0");

	</script>
	<%@include file="/common/dialog.jsp" %>
</body>
</html>