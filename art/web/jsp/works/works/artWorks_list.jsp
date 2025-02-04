﻿<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "works/artWorks.do";
	var hyperlink = contextpath + "works/artWorks.do";
	var artistlink = contextpath + "artist/ArtistInformation.do";
	function closedialog(ret) {
		var medium=ret['medium'];
		if(medium==true){
	    	 if(ret['mediumCategory']=="1"){
	    	 	getElement('query.parameters.mediumMaterial').value=ret['str1'];
	    	 	getElement('query.parameters.mediumMaterialId').value=ret['str'];
	    	 }
	    	 if(ret['mediumCategory']=="2"){
	    	 	getElement('query.parameters.mediumShape').value=ret['str1'];
	    	 	getElement('query.parameters.mediumShapeId').value=ret['str'];
	    	 }
	     }
		if(ret=='true'||ret==true) {
			goSearch();
		}	
		if(ret == 'false' && editMode=="EXCEL"){
			goSearch();
		}
		editMode="";
	}
	
	function renew()  {
		var order = getElement("query.order");                  order.value="";
		var desc = getElement("query.orderDirection");          desc.value="";
		var pn = getElement("query.pageNumber");                pn.value="1";
		var ps = getElement("query.pageSize");                  ps.value="10";
	    goSearch();
	}

	function init() {
		showMessage('<s:property value="errorMessage" escape="false"/>');
		renew();
		hide();
	}

	function goSearch() {
		goartWorksGridSearch();
	}
	
	function goAdd()  {
		editMode="ADD";
	    openWindow("添加作品基本信息",fulllink+'?action=ADD',0.99,0.99);
	}
	
	function goSelect(){
		var selectType=$$("selectType").value;
		if(selectType.indexOf('m')>-1){
			var id = findMultiSelected("选择作品",artWorks_grid);
			if (id == "") return;
			var str="";
			var str1="";
			var ids=id.split(",");
			  for(var i=0;i<ids.length;i++){
				  var name = artWorks_grid.cellById(ids[i],7).getValue();
				  name = name.split("^")[0];
				  str+=ids[i];
				  str1+=name;
				  if((i+1)!=ids.length){
					  str+=",";
					  str1+="、";
				  }
			  }
			  var ret=new Object();
			  ret['ret']=true;
			  ret['str1']=str;
			  ret['str2']=str1;
			  parent.closedialog(ret);
			
		} else {
			var id = findSelected("选择作品",artWorks_grid);
			if (id == "") return;
			var name = artWorks_grid.cellById(id,7).getValue();
			console.log(name);
			name = name.split("^")[0];
			console.log(name);
			var ret=new Object();
			ret['ret']=true;
			ret['str1']=id;
			ret['str2']=name;
			parent.closedialog(ret);
		}
	}

	function goModify(){
		var id = findSelected("修改",artWorks_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品基本信息",url_link,0.99,0.99);
	}

	/*function goModify(id){
		url_link = fulllink+'?action=EDIT&record.id='+id;
		editMode="EDIT";
	    openWindow("修改作品基本信息",url_link,0.8,0.8);
	}*/

	function artistview(id) {
//		url_link = artistlink+'?action=EDIT&ids='+id;
		var url_link=artistlink+'?action=VIEW&ids='+id;
	    editMode="VIEW";
	    openWindow("艺术家信息",url_link,0.99,0.99);
	}
	
	function workview(id) {
		//var url_link=fulllink+'?action=VIEW&record.id='+id;
	    //editMode="VIEW";
	    //openWindow("查看作品基本信息",url_link,0.8,0.8);
	    url_link = fulllink+'?action=EDIT&ids='+id;
		editMode="EDIT";
	    openWindow("修改作品基本信息",url_link,0.99,0.99);
//		url_link = fulllink+'?action=FRAME&ids='+id;
//		editMode="EDIT";
//	    openWindow("修改作品基本信息",url_link,0.99,0.99);
	}
	
	function goDel(val) {
		var id = findMultiSelected("删除",artWorks_grid);
		if (id == "") return;
		var res = confirm("是否真的要删除?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=DELETE&ids=" + id,"artWorksForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
		
	}
	
	function goCel() {
		editMode="EXCEL";
		var url_link=fulllink+'?action=GOEXCEL';
	    openWindow("导入",url_link,0.4,0.2);
	}
	
	function goMedium(category){
		if(category == "material"){
			var id = $$('mediumMaterialId').value;
			var url_link=contextpath+'works/artWorks.do?action=MEDIUM&record.id='+id+"&record.mediumCategory=1";
		    openWindow("选择媒介",url_link,400,500);
		}
		if(category == "shape"){
			var id = $$('mediumShapeId').value;
			var url_link=contextpath+'works/artWorks.do?action=MEDIUM&record.id='+id+"&record.mediumCategory=2";
		    openWindow("选择媒介",url_link,400,500);
		}
	}
	
	function clear(){
		var mediumMaterial= document.getElementById('mediumMaterial').value;
		if(mediumMaterial == ''){
			document.getElementById('mediumMaterialId').value = "";
		}
		var ediumShape= document.getElementById('mediumShape').value
		if(ediumShape == ''){
			document.getElementById('mediumShapeId').value = "";
		}
	}
	
	function goTrue(){
		var id = findMultiSelected("重复",artWorks_grid);
		if (id == "") return;
		var res = confirm("是否确认要加重复标记?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=ISSAME&ids=" + id +"&record.issame=1","artWorksForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
	}

	function goExport(){
		document.forms[0].action = fulllink + "?action=EXPORT";
		document.forms[0].target = "_self";
		document.forms[0].submit();
	}
	
	function goFalse(){
		var id = findMultiSelected("取消重复",artWorks_grid);
		if (id == "") return;
		var res = confirm("是否确认要取消重复标记?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=ISSAME&ids=" + id +"&record.issame=0","artWorksForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
	}
	
	function hide(){
		var hide = document.getElementById('hide');
		if(hide != undefined && hide.value == 'true'){
			document.getElementById('btn').style.display="none";
		}
	}
	
	function goIden(){
		var url_link=contextpath+'works/artWorksIden.do?action=TREE';
		//var url_link=contextpath+'works/artWorksIden.do?action=LIST';
	    openWindow("添加去重标记",url_link,1,1);
	}
	
</script>

<style type="text/css">
html, body {
	width: 100%;
	height: 100%;
}
a img{ border:none}
</style>
</head>

<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="artWorks" id="artWorksForm" method="post" namespace="/artist">
			<s:hidden name="record.hide" id="hide"/>
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.seriesId" id="seriesId" />
			<s:hidden name="query.parameters.selectType" id="selectType" />
			<s:hidden name="query.parameters.artistId" id="artistId" />
			<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tbody>
					<tr>
						<td class="textL" style="width: 80%">
						&nbsp;&nbsp;艺术家：<s:textfield style="width:100px;" name="query.parameters.artist" /> 
						&nbsp;&nbsp;作品编号：<s:textfield style="width:100px;" name="query.parameters.code" /> 
						&nbsp;&nbsp;作品名称：<s:textfield style="width:100px;" name="query.parameters.name" /> 
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
  						&nbsp;&nbsp;<s:checkbox name="query.parameters.orderBy" onClick="goSearch()" style="width:15px;"></s:checkbox>按作品编号排序
						</td>
					</tr>
				</tbody>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
					  <s:if test="query.parameters.type==1">
						<gl:button styleClass="sbuBtnStyle"  code="A200101" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle"  code="A200102" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle"  code="A200103" icon="delIcon" onClick="goDel()">删除</gl:button>
						<gl:button styleClass="sbuBtnStyle"  code="A200104" icon="delIcon" onClick="goCel()">导入</gl:button>
						<%--<gl:button styleClass="sbuBtnStyle"  code="A200104" icon="delIcon" onClick="goExport()">导出</gl:button>--%>
						<gl:button styleClass="sbuBtnStyle"  code="A200105" icon="delIcon" onClick="goTrue()">加重复标记</gl:button>
						<gl:button styleClass="sbuBtnStyle"  code="A200106" icon="delIcon" onClick="goFalse()">取消重复标记</gl:button>
						<gl:button styleClass="sbuBtnStyle"  code="A200107" icon="delIcon" onClick="goIden()">疑似重复</gl:button>
					 </s:if>
					 <s:else>
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goSelect()">确认</gl:button>
					</s:else>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="artWorks" page="true" form="artWorksForm" property="query" cellPadding="0" cellSpacing="0">
			var grid = new dhtmlXGridObject();
			grid.setImagePath(imagePath);
			var headAlign=[tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter,tCenter];
			grid.setHeader("#master_checkbox,是否</br>假画,是否</br>重复,作品</br>完成度,作品缩略图,作品编号,艺术家,作品名称,英文名称,创作时间,尺寸</br>(cm),签名位置,签名内容,媒介材料,媒介形式,作品系列,创作地点</br>(国家),作品题材,风格类型,拍卖时间,拍卖行,最低估价,最高估价,成交价,交易价",null,headAlign);
			grid.setInitWidths("30,50,50,50,120,90,80,120,120,80,100,100,90,60,60,80,80,100,100,100,100,100,100,100,100");
			grid.setColAlign("center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center,center");
			grid.setColTypes("ch,ro,ro,ro,ro,ro,link,link,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
			grid.setColSorting("str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str,str");
			grid.setSkin("dhx_terrace");
 			grid.enableMultiline(true);
			grid.init();
			var gDiv=$$('artWorks_box');

			</gl:grid>
		</s:form>
	</div>
<%@include file="/common/dialog.jsp"%>
	<%--<%@include file="/common/resizeList1.jsp"%>--%>
<script type="text/javascript">
    if(window.addEventListener) {
        window.addEventListener("resize",goResize,false);
    }
    else {
        window.attachEvent('onresize',goResize);
    }

    var mDiv=window.document.getElementById('mainDiv');
    var tDiv=window.document.getElementById('tbl');
    var btn=window.document.getElementById('btn');

    function goResize(){
        var he;
        if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;
        mDiv.style.height = he - 42 + "px";
        gDiv.style.height = mDiv.offsetHeight - btn.offsetHeight - btn.offsetTop + 1 + "px";
        tDiv.style.width = mDiv.offsetWidth - 4 + "px";
        gDiv.style.width = mDiv.offsetWidth - 6 + "px";
        btn.style.width = tDiv.offsetWidth + "px";
        console.log(gDiv.style.width);
    }

<<<<<<< .mine
		 mDiv.style.height=he-42+"px";
		 gDiv.style.height=mDiv.offsetHeight-btn.offsetHeight-btn.offsetTop+1+"px";
		 tDiv.style.width=mDiv.offsetWidth-4+"px";
		 gDiv.style.width=mDiv.offsetWidth-6+"px";
	     btn.style.width=tDiv.offsetWidth+"px";
    }
    goResize();
</script> 
=======
    goResize();

</script>
>>>>>>> .r23903
</body>
</html>