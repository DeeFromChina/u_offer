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
	<s:hidden name="record.id" id="mediumId"></s:hidden>
	<div id="formDiv" style="border:1px solid #c1b2f9;background:white;text-align:left!important;">
	   <div id="left_div" style="width:100%;height:100%;overflow-y:auto;">
	   </div>
	</div>
	<div id="btnDiv" class="btnDiv">
		<gl:button name="btnAdd" onClick="goSubmit()" >保存</gl:button>
	     &nbsp;
		 <gl:button name="btnAdd" onClick="goCancel()" >关闭</gl:button>
	</div>
	<script type="text/javascript">
	
	function chkTrue(id){
		if(tree.getUserData(id,"treeCode") == "m1" || tree.getUserData(id,"treeCode") == "s1"){
			return;
		}
		if(tree.isItemChecked(id)){
			tree.setCheck(id,false);
		}else{
			tree.setCheck(id,true);
		}
	}
	
	function goSubmit(){
		var str = "";
		var str1 = "";
		var ids = tree.getAllChecked().split(",");
		for(var i = 0; i<ids.length; i++){
			var id = ids[i];
			if(tree.getUserData(id,"treeCode") == "m1" || tree.getUserData(id,"treeCode") == "s1"){
				continue;
			}
			var mediumCategory=$$('mediumCategory').value;
			  var mediumId;
			  if(mediumCategory == "1"){
				  mediumId = tree.getUserData(id,"materialId");
			  }
			  if(mediumCategory == "2"){
				  mediumId = tree.getUserData(id,"shapeId");
			  }
			  if(str != ""){
				  str = str + ",";
			  }
			  str = str + mediumId;
			  if(str1 != ""){
				  str1 = str1 + ",";
			  }
			  str1 = str1 + tree.getUserData(id,"name");
		}
		 var ret=new Object();
		 ret['medium']=true;
		 ret['str']=str;
		 ret['str1']=str1;
		 ret['mediumCategory']=mediumCategory;
	    parent.closedialog(ret);
	}

    function goCancel(){
        parent.closedialog('false');
    }

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

		/* var dhxLayout = new dhtmlXLayoutObject(document.body, "1C");
    	dhxLayout.setSkin("dhx_terrace");
    	var leftPanel=dhxLayout.cells("a");
    	leftPanel.hideHeader();
    	leftPanel.attachObject("left_div"); */

		var tree = new dhtmlXTreeObject("left_div","100%","100%",0);
		tree.setImagePath("<%=CONTEXT_PATH%>dhtmlx/imgs/dhxtree_terrace/");
	    tree.attachEvent("onClick", function(id){
	    	chkTrue(id);
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
    	tree.enableCheckBoxes(1);
    	tree.loadXML("<%= CONTEXT_PATH%>artist/medium.do?action=getTree&id=0&record.mediumCategory="+$$('mediumCategory').value);
		tree.setXMLAutoLoading("<%= CONTEXT_PATH%>artist/medium.do?action=getTree&record.mediumCategory="+$$('mediumCategory').value);

	</script>
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
    
    function goResize(){
       fDiv.style.width=mainbody.offsetWidth-4+"px";
       bDiv.style.width=mainbody.offsetWidth-bDiv.offsetLeft*3-15+"px";
       fDiv.style.height=mainbody.offsetHeight-bDiv.offsetHeight-4+"px";
       bDiv.style.top=fDiv.offsetHeight+fDiv.offsetTop*3+"px";
    }
    goResize();
	</script>
</body>
</html>