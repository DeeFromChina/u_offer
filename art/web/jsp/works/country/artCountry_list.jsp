<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "system/artCountry.do";
	var hyperlink = contextpath + "system/artCountry.do";
	function closedialog(ret) {
		if(ret=='true' || ret==true) {
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
	}
	
	function goSearch() {
		goartCountryGridSearch();
	}
	
	function goAdd()  {
		editMode="ADD";
	    openWindow("作品时期信息",fulllink+'?action=ADD',0.4,0.5);
	}

	function goSelect(){
		var selectType=$$("selectType").value;
		if(selectType.indexOf('m')>-1){
			var id = findMultiSelected("选择国家",artCountry_grid);
			if (id == "") return;
			var str="";
			var str1="";
			var ids=id.split(",");
			  for(var i=0;i<ids.length;i++){
				  var name = artCountry_grid.cellById(ids[i],5).getValue();
				  str+=ids[i];
				  str1+=name;
				  if((i+1)!=ids.length){
					  str+=",";
					  str1+="、";
				  }
			  }
			  var ret=new Object();
			  ret['ret']=true;
			  ret['str']=str;
			  ret['str1']=str1;
			  parent.closedialog(ret);
			
		} else {
			var id = findSelected("选择国家",artCountry_grid);
			if (id == "") return;
			var name = artCountry_grid.cellById(id,1).getValue();
			var type = $$('type').value;
			if(type == '1'){
				type='birthCountry';
			}
			if(type == '2'){
				type='country';
			}
			var ret=new Object();
			ret[type]=true;
			ret['str']=id;
			ret['str1']=name;
			parent.closedialog(ret);
		}
	}
	
	function goModify(){
		var id = findSelected("修改",artCountry_grid);
		if (id == "") return;
		url_link = fulllink+'?action=EDIT&record.id='+id;
		editMode="EDIT";
	    openWindow("作品时期信息",url_link,0.4,0.5);  
	}
	
	function goDel(val) {
		var id = findMultiSelected("删除",artCountry_grid);
		if (id == "") return;
		var res = confirm("是否真的要删除?");
		if(res == true) {
			var data=ajaxSubmit(fulllink+"?action=DELETE&ids=" + id,"artCountryForm");
	 		if(data.exit>0){
	 			  alert(data.message);
	 			  return;
	 		}
	 		else {	  
	 			goSearch();
	 		} 
		}
		
	}
	
</script>

<style type="text/css">
html, body {
	width: 100%;
	height: 100%;
}
</style>
</head>

<body onload="init()">
	<div class="special-padding" id="mainDiv">
		<s:form action="artCountry" id="artCountryForm" method="post" namespace="/system">
			<s:hidden name="record.id" id="id"/>
			<s:hidden name="record.type" id="type"/>
			<s:hidden name="query.order" />
			<s:hidden name="query.orderDirection" />
			<s:hidden name="query.pageNumber" />
			<s:hidden name="query.recordCount" />
			<s:hidden name="query.pageCount" />
			<s:hidden name="query.parameters.selectType" id="selectType" />
			<table width="100%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
				<tbody>
					<tr>
						<td class="textL" style="width: 80%">
						&nbsp;&nbsp;国家名称：<s:textfield style="width:100px;" name="query.parameters.countryName" /> 
  						&nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
						</td>
					</tr>
				</tbody>
			</table>
			<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
				<tr>
					<td>
					<s:if test="query.parameters.type==1">
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goSelect()">确定</gl:button>
					</s:if>
					<s:else>
						<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()">增加</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="subIcon" onClick="goModify()">修改</gl:button> 
						<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
					</s:else>
					</td>
				</tr>
			</table>
			<gl:grid styleClass="pageTurn" id="artCountry" page="true" form="artCountryForm" property="query" cellPadding="0" cellSpacing="0">
			var grid = new dhtmlXGridObject();
			grid.setImagePath(imagePath);
			var headAlign=[tCenter,tCenter,tCenter,tCenter];
			grid.setHeader("#master_checkbox,国家名称,描述",null,headAlign);
			grid.setInitWidthsP("5,35,60");
			grid.setColAlign("center,center,left");
			grid.setColTypes("ch,ro,ro");
			grid.setColSorting("str,str,str");
			grid.setSkin("dhx_terrace");
			grid.init();
			var gDiv=$$('artCountry_box');
			</gl:grid>
		</s:form>
	</div>
<%@include file="/common/dialog.jsp"%>
<script type="text/javascript">
    if(window.addEventListener) {
 	     window.addEventListener("resize",goResize,false); 
    }
    else {
 	     window.attachEvent('onresize',goResize);
    }
    
	 var gDiv=window.document.getElementById('artCountry_box');
	 var mDiv=window.document.getElementById('mainDiv');
	 var btn=window.document.getElementById('btn');
	    
    function goResize(){
   	 var he;
	    if(isIE()) he=window.document.body.offsetHeight; else he=window.innerHeight;

		 mDiv.style.height=he-42+"px";
		 gDiv.style.height=mDiv.offsetHeight-btn.offsetHeight-btn.offsetTop+1+"px";
		 gDiv.style.width=mDiv.offsetWidth-6+"px";
    }
    goResize();   
</script>
</body>
</html>