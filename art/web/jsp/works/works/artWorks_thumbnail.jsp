<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/common/header.jsp"%>

<script language="javascript">
	var fulllink = contextpath + "works/artWorks.do";
	var hyperlink = contextpath + "works/artWorks.do";
	var coverlink = contextpath + "works/artAttachment.do";

	function init() {
		document.getElementById("cancel2").style.display="none";
		document.getElementById("cancel1").style.display="none";
		document.getElementById("sumbit").style.display="none";
		hide();
		showMessage('<s:property value="errorMessage" escape="false"/>');
	}
	
	function hide(){
		if(document.getElementById('hide').value==1){
			$(".controlTable").each(function(){
				this.style.display="none";
			});
		}
	}
	
	function closedialog(ret){
		alert("成功设置！");
		cancel2();
		var id = $$('worksId').value;
		var url =fulllink+"?action=THUMBNAIL&record.id="+id;
	  	document.forms[0].action=url;
		document.forms[0].submit();
	}
	
	function openImg(obj){
		var worksId = document.getElementById("worksId").value;
		openWindow("浏览作品图片",fulllink+'?action=SHOW&record.name='+obj+"&record.worksId="+worksId,0.8,0.8);
	}
	
	function goAdd(){
		document.getElementById("file").style.display="";
		document.getElementById("add").style.display="none";
		document.getElementById("del2").style.display="none";
		document.getElementById("cover").style.display="none";
		document.getElementById("cancel1").style.display="";
	}
	
	function changeValue(){
		var res = confirm("是否真的要添加?");
		if(res == true){
			var f = document.getElementById('file').value;
			var format = f.split(".");
			var i = format.length;
			if(format[i-1] != "jpg" && format[i-1] != "jpeg" && format[i-1] != "gif" && format[i-1] != "png"){
				alert("只能上传图片格式！");
				return;
			}
			var b = document.getElementById("button");
			b.click();
		}else{
			document.getElementById("file").style.display="none";
			document.getElementById("add").style.display="";
			document.getElementById("del2").style.display="";
			document.getElementById("cover").style.display="";
			document.getElementById("cancel1").style.display="";
		}
	}
	
	function goSubmit() {
		var id = $$("worksId").value;
		var url =fulllink+"?action=SAVETHUMBNAIL&record.id="+id;
	  	document.forms[0].action=url;
		document.forms[0].submit();
	}
	
	function goDel(real){
		if(real==1){
			$(".check").each(function(){
				this.style.display="";
			});
		}
		if(real==2){
			var res = confirm("是否真的要添加?");
			if(res == true){
				var isPass = true;
				var name = "";
				$(".check").each(function(){
					if(this.checked){
						name = name + this.value + ",";
						if(this.name == "(封面)"){
							alert("不能删除封面！");
							isPass = false;
							return;
						}
					}
				});
				if(name == ""){
					alert("请选择一张图片");
					return;
				}
				if(!isPass){
					return;
				}
				var url =fulllink+"?action=DELETETHUMBNAIL&record.name="+name;
			  	document.forms[0].action=url;
				document.forms[0].submit();
			}
		}
	}
	
	function cancel2(){
		dis("hide");
		$(".check").each(function(){
			this.style.display="";
		});
		document.getElementById("cancel2").style.display="none";
		document.getElementById("sumbit").style.display="none";
		document.getElementById("cover").style.display="";
		document.getElementById("add").style.display="";
		document.getElementById("del2").style.display="";
	}
	
	function cancel1(){
		document.getElementById("file").style.display="none";
		document.getElementById("add").style.display="";
		document.getElementById("del2").style.display="";
		document.getElementById("cover").style.display="";
		document.getElementById("cancel1").style.display="none";
	}
	
	function cover(){
		document.getElementById("cancel2").style.display="";
		document.getElementById("sumbit").style.display="";
		document.getElementById("cover").style.display="none";
		document.getElementById("del2").style.display="none";
		document.getElementById("add").style.display="none";
		dis("");
		$(".check").each(function(){
			this.style.display="none";
		});
	}
	
	function dis(isHide){
		if(isHide == "hide"){
			$(".cover").each(function(){
				this.style.display="none";
			});
		}
		if(isHide == ""){
			$(".cover").each(function(){
				this.style.display="";
			});
		}
	}
	
	function sumbit(){
		var name = "";
		$(".cover").each(function(){
			if(this.checked){
				name = this.value;
			}
		});
		if(name == ""){
			alert("请选择一张图片");
			return;
		}
		var worksId = $$('worksId').value;
		openWindow("设置封面",coverlink+"?action=ADD&record.name="+name+"&record.worksId="+worksId,0.4,0.4);
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
		<s:form action="artWorks" id="artWorksForm" method="post" namespace="/works"  enctype="multipart/form-data">
	<s:hidden name="record.worksId" id="worksId"/>
	<s:hidden name="record.hide" id="hide"/>
	<div class="special-padding" id="mainDiv">
		<table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
			<tr>
				<td>
					<input type="file" name="files" id="file" style="display:none" onchange="changeValue()"/>
					<gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goAdd()" id="add">增加</gl:button> 
					<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel(2)" id="del2">删除</gl:button>
					<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="cover()" id="cover">设置封面</gl:button>
					<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="sumbit()" id="sumbit">确定</gl:button>
					<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="cancel2()" id="cancel2">取消</gl:button>
					<gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="cancel1()" id="cancel1">取消</gl:button>
				</td>
			</tr>
		</table>
		<div style="overflow-y:scroll;width:100%;height:350px">
		<table>
			<s:iterator value="thumbnailList" id="tl" status="tltl">
				<s:if test='%{#tltl.index%4==0}'>
					<tr>
				</s:if>
				<td width="25%" align="center">
					<table>
						<tr>
							<td><img id="${tl.realName}" src="<%=CONTEXT_PATH%>upload/works/thumbnail/${tl.worksId}/${tl.realName}" width="100px" height="100px" ondblclick="openImg('${tl.realName}')"/></td>
						</tr>
						<tr>
							<td>
								<input type="radio" class="cover" value="${tl.realName}" style="display:none" name="cover"/>
								<input type="checkbox" class="check" value="${tl.realName}" name="${tl.cover}" checked="checked"/>
								<a href="#" onclick="javascript:openImg('${tl.realName}')">
									${tl.name}${tl.cover}
								</a>
							</td>
						</tr>
					</table>
				</td>
				<s:if test='%{#tltl.index%4==3}'>
					</tr>
				</s:if>
			</s:iterator>
			</tr>
		</table></div>
		<input type="submit" id="button" style="display:none" onclick="goSubmit()"/>
	</div>
		</s:form>
<%@include file="/common/dialog.jsp"%>
</body>
</html>