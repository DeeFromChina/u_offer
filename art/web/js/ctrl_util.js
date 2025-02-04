﻿/**
 * 响应页面控件的回车键事件，跳转到下一控件

 * 用法：在控件的onkeypress事件中调用该方法
 * onkeypress="enterNext(this, window.event);"
 */
 
 /*    
     王川 2004-6-25 
 不用这么麻烦：	if (event.keyCode == 13) event.keyCode == 9；就可以了。
 */
function enterNext(property, event)  {
	var count = 0;
	if (event.keyCode == 13) {
		var elm = document.forms[0].elements;
		for (i = 0; i < elm.length; i++) {
			event.keyCode = 0;
			if (property == elm[i]) {
				while (true) {
					if (elm[(i + 1) % elm.length].type.indexOf("select") == -1 && elm[(i + 1) % elm.length].readOnly != false) {
						i++;
						continue;
					}
					try {
						elm[(i + 1) % elm.length].focus();
						break;
					}
					catch (errorObj) {
						i++;
						continue;
					}
				}
			}
		}
	}
} 


function isIE() {
	if (window.navigator.userAgent.indexOf("MSIE")>=1) {
		return true; 
	}
	else {
		return false;
	}
} 

function windowopen(urllink,para,w,h)  {
	var res;
	var t=urllink.charAt(0);
  if(t=="/")  
    res=window.showModalDialog(contextpath+"/common/dframe.htm",urllink,"dialogHeight:"+h+"px;dialogWidth:"+w+"px;status:no");
  else
    res=window.showModalDialog(contextpath+"/common/dframe.htm","../"+urllink,"dialogHeight:"+h+"px;dialogWidth:"+w+"px;status:no");
  return res;
}

function getElements(name)  {
   return window.document.getElementsByName(name);
}

function getElement(name)  {
   return window.document.getElementsByName(name)[0];
}

function findSelected(type,mygrid) {
	 if(mygrid.getRowsNum()==0) {
		 alert("没有记录可以" + type + "。");
	 	 return "";
	 }
	 var ids=mygrid.getSelectedRowId();
	 if(ids==null||ids=="")  { 
		 var sid = mygrid.getCheckedRows(0);
		 if(sid==null||sid=="")  { 
			 alert("请选择要" + type + "的记录。");
			 return "";
		 }else if(sid.indexOf(',')!=-1) {
			 alert("只能" + type + "一条记录。");
			 return "";
		 }
		 else return sid;
	 }else{
		 return ids;
	 }
}

function findMultiSelected(type,mygrid) {
	 if(mygrid.getRowsNum()==0) {
		 alert("没有记录可以" + type + "。");
	 	 return "";
	 }
	 var ids = mygrid.getCheckedRows(0);
	 if(ids=="")  { 
		 var sid=mygrid.getSelectedRowId();
		 if(sid==null||sid=="")  { 
			 alert("请选择要" + type + "的记录。");
			 return "";
	     }
		 else return sid;
	 }else{
		 return ids;
	 }
}

function findSelected2(type,mygrid) {
	 if(mygrid.getRowsNum()==0) {
		 alert("没有记录可以" + type + "。");
	 	 return "";
	 }
	 var ids =mygrid.getCheckedRows(0);
	 if(ids=="")  { 
		 alert("请选择要" + type + "的记录。");
		 return "";
	 }else if(ids.indexOf(',')!=-1) {
		 alert("只能" + type + "一条记录。");
		 return "";
	 }else{
		 return ids;
	 }
}

function findMultiSelected2(type,mygrid) {
	 if(mygrid.getRowsNum()==0) {
		 alert("没有记录可以" + type + "。");
	 	 return "";
	 }
	 var ids = mygrid.getCheckedRows(0);
	 if(ids=="")  { 
		 alert("请选择要" + type + "的记录。");
		 return "";
	 }else{
		 return ids;
	 }
}

function gosearch(act)  {
	  var rc = getElement("query.pageSize");
    if(rc!=null) {
	     if(!checkNumeric(rc,"[每页记录数]",false,true)) return false;
    }
    if(act==null) {
    	 document.forms[0].action = hyperlink + "?action=list";
    }
    else{
    	 document.forms[0].action = hyperlink + "?action="+act;
    }
    document.forms[0].target = "_self";
    document.forms[0].submit();
}

function query(field)  {
    var order = getElement("query.order");
    var desc  = getElement("query.orderDirection");
	  var pn =    getElement("query.pageNumber");

    order.value = field;
    if(desc.value == "") desc.value = "asc";
    else if(desc.value == "asc") desc.value = "desc"; 
    else desc.value = "asc";
    pn.value="1";
    gosearch();
}

function goFirstPage(act)  {
	  var rc = getElement("query.recordCount");
	  if(rc.value == 0) return;
	  var pn = getElement("query.pageNumber");
	  pn.value = "1";
    gosearch(act);
}

function goPreviousPage(act)  {
	  var i = 0;

	  var rc = getElement("query.recordCount");
	  if(rc.value == 0) return;
	  var pn = getElement("query.pageNumber");
	  i = pn.value;
	  pn.value--;

    gosearch(act);
}

function goNextPage(act)  {
	  var rc = getElement("query.recordCount");
	  if(rc.value == 0) return;
	  var pn = getElement("query.pageNumber");
	  pn.value++;
    gosearch(act);
}

function goLastPage(act)  {
	  var rc = getElement("query.recordCount");
	  if(rc.value == 0) return;
	  var pn = getElement("query.pageNumber");
	  var pc = getElement("query.pageCount");
	  pn.value = pc.value;
    gosearch(act);
}

var rowMouseOverBgColor = "#99eedd";
var rowBgColor = "#d9edff";

function setPageSize(act)  {
	 if(event.keyCode == 13) gosearch(act);
}

function headerOver(obj) {
   obj.style.cursor = "hand";
   obj.style.color  = "#bb0000";
}

function headerOut(obj) {
   obj.style.cursor = "default";
   obj.style.color  = "#333333";
}

function $$(id){
	return window.document.getElementById(id);
}

function $TAG(name){
	return window.document.getElementsByTagName(name);
}

function $NAME(name){
	return window.document.getElementsByName(name);
}
	
function goReset(){
   document.forms[0].reset();
}
	
function showMessage(message){	
	if(""!=message){alert(message);}
}
	
function changeSelection(name){	
	 var group = $NAME(name);
	 if(group==null) return "";
	 
	 var sl=$$('selectAll');

	 for(var i=0;i<group.length;i++) {
	 	  if(sl.disabled==false) {
	       if(sl.checked) group[i].checked = true; else group[i].checked = false;
	    }
	 }
}

//iframe 自动适应脚本 frmid  iframe id
function dynamicFrame(frmid){   
 var frm=document.getElementById(frmid);   
	//frm.contentWindow.document为IE下 使用，获得子页面各个对象   
	var sub=frm.contentDocument ? frm.contentDocument:frm.contentWindow.document;    
 
 	if(frm!=null&&sub!=null){   
 		if (frm.contentDocument && frm.contentDocument.body.offsetHeight) //如果用户的浏览器是NetScape
     	 frm.height = sub.body.offsetHeight; 
   		 else if (frm.Document && frm.Document.body.scrollHeight){ //如果用户的浏览器是IE
		{
			frm.height = sub.body.scrollHeight+10;
		}
      }
 	}   
}   

function rowMouseOver(obj) {
	var tmp=obj.className;
	obj.className=tmp+" selectItem";
}

function rowMouseOut(obj) {
	var tmp=obj.className;
  obj.className=tmp.replace(" selectItem","");
}

function secondRowMouseOver(obj) {
	var tmp=obj.className;
	obj.className=tmp+" selectItem";
}

function secondRowMouseOut(obj) {
	var tmp=obj.className;
  obj.className=tmp.replace(" selectItem","");
}

//dhtmlxcalendar 日期控件
dhtmlXCalendarObject.prototype.langData["cn"] = {
	    dateformat: '%Y-%m-%d',
	    monthesFNames: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
	    monthesSNames: ["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
	    daysFNames: ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
	    daysSNames: ["日","一","二","三","四","五","六"],
	    weekstart: 7
}

function initCalendar(inputId,iconId){
	  var calendar=new dhtmlxCalendarObject({input:inputId,button:iconId});
	  calendar.loadUserLanguage('cn');
	  calendar.setSkin(skinName);
	  calendar.hideTime();
	  return calendar;
} 

function toggle(calendar,calendaricon){
    if(calendar.isVisible()){ 
        calendar.hide(); 
    } 
    else{
  	  var calendarWidth=200;
  	  var calendarHeight=170;
  	  var calendarLeft=0;
  	  var calendarTop=0;
  	  var absoluteLocation=GetAbsoluteLocation(calendaricon);
  	  var spaceToRight=window.document.body.offsetWidth-absoluteLocation.absoluteLeft;
  	  var spaceToBottom=window.document.body.offsetHeight-absoluteLocation.absoluteTop-40;
  	 
  	  if(spaceToRight-calendarWidth<0){
  		  calendarLeft=-calendarWidth;
  		  calendarTop=absoluteLocation.offsetHeight+5;
  		  
  		  if(spaceToBottom-calendarHeight<0){
  			  calendarTop=-calendarHeight;
      		  calendarLeft-=absoluteLocation.offsetWidth;
  	      }
  	  }else{
  		  if(spaceToBottom-calendarHeight<0){
  			  calendarTop=-calendarHeight;
  	      }
  	  }
  	  calendar.setPosition(calendaricon,calendarTop,calendarLeft); 
        calendar.show();
        calendar.activeCon.focus();
    } 
} 
function toggleWheel(calendar,calendaricon){ 
	calendar.hide();
}
function GetAbsoluteLocation(element) 
{ 
    if ( arguments.length != 1 || element == null ) 
    { 
        return null; 
    } 
    var elmt = element; 
    var offsetTop = elmt.offsetTop; 
    var offsetLeft = elmt.offsetLeft; 
    var offsetWidth = elmt.offsetWidth; 
    var offsetHeight = elmt.offsetHeight; 
    while( elmt = elmt.offsetParent ) 
    { 
          // add this judge 
        if ( elmt.style.position == 'absolute' || elmt.style.position == 'relative'  
            || ( elmt.style.overflow != 'visible' && elmt.style.overflow != '' ) ) 
        { 
            break; 
        }  
        offsetTop += elmt.offsetTop; 
        offsetLeft += elmt.offsetLeft; 
    } 
    return { absoluteTop: offsetTop, absoluteLeft: offsetLeft, 
        offsetWidth: offsetWidth, offsetHeight: offsetHeight }; 
}

var tLeft="text-align:left;vertical-align:middle;";
var tCenter="text-align:center;vertical-align:middle;";
var tRight="text-align:right;vertical-align:middle;";

function getForm(frmID){
	   var frm=document.getElementById(frmID).elements;        
	   var i,queryString = "", and = "";       
	   var item; // for each form's object       
	   var itemValue;// store each form object's value
	   var iname;
	   for( i=0;i<frm.length;i++) {
	      item = frm[i];// get form's each object                            
	      if ( item.name!='' ) {                  
	         if ( item.type == 'select-one' ) {
	            if(item.selectedIndex>-1) itemValue = item.options[item.selectedIndex].value;
	         }
	         else if ( item.type=='checkbox' || item.type=='radio') {
	            if ( item.checked == false ) continue;
	            itemValue = item.value;
	         }
	         else if ( item.type == 'button' || item.type == 'submit' || item.type == 'reset' || item.type == 'image') {// ignore this type
	            continue;
	         }
	         else itemValue = item.value;
	      //   itemValue = encodeURIComponent(itemValue);
	      //   if(item.name.startWith(frmID)){
	      //  	 iname=item.name.replace(frmID+'_','');
	      //   }
	      //   else iname=item.name; 
	         iname=item.name; 
	         queryString += and + iname + '=' + encodeURIComponent(itemValue);
	         and="&";
	      }
	   }
	   return queryString;
}

   function getFormQueryString(frmID){ 
	   var frm=document.getElementById(frmID).elements;        
	   var i,queryString = "", and = "";       
	   var item; // for each form's object       
	   var itemValue;// store each form object's value
	   var iname;
	   for( i=0;i<frm.length;i++) {
	      item = frm[i];// get form's each object                            
	      if ( item.name!='' ) {                  
	         if ( item.type == 'select-one' ) {
	            if(item.selectedIndex>-1) itemValue = item.options[item.selectedIndex].value;
	         }
	         else if ( item.type=='checkbox' || item.type=='radio') {
	            if ( item.checked == false ) continue;
	            itemValue = item.value;
	         }
	         else if ( item.type == 'button' || item.type == 'submit' || item.type == 'reset' || item.type == 'image') {// ignore this type
	            continue;
	         }
	         else itemValue = item.value;
	      //   itemValue = encodeURIComponent(itemValue);
	      //   if(item.name.startWith(frmID)){
	      //  	 iname=item.name.replace(frmID+'_','');
	      //   }
	      //   else iname=item.name; 
	         iname=item.name; 
	         queryString += and + iname + '=' + itemValue;
	         and="&";
	      }
	   }

	   return queryString;
	}
   
	
	function ajaxSubmit(url,formName){
		  var loader;
		  if(formName==null) loader = dhx4.ajax.postSync(url);
		  else loader = dhx4.ajax.postSync(url,getFormQueryString(formName));
		  var data;
		  eval("data="+loader.xmlDoc.responseText+";");
		  return data;
	}

	function ajaxGridSubmit(url,formName,gridCallback){
		  if(formName==null) dhx4.ajax.post(url,gridCallback);
		  else dhx4.ajax.post(url,getFormQueryString(formName),gridCallback);
	}
	
	
	var showNow=false;
	function aferAjaxGridSubmit(){
		showNow=true;
		setTimeout(showLoading,2000);
	}
	
	function showLoading(){
		if(showNow==true) loading(true);
	}
	
	function ajaxGridSubmitBack(){
		showNow=false;
		loading(false);
	}
	
	function loading(isShow) {
		if($(".mask").length == 0) {
			$(document.body).append("<div class='mask'></div><image class='loadingImg' src='../images/loading2.gif' />");
		}
		if(isShow) {
			$(".mask").css("height",$(document).height());
			$(".mask").css("width",$(document).width());
			$(".mask").show();
			$(".loadingImg").show();
		} else {
			$(".mask").hide();
			$(".loadingImg").hide();
		}
	}
	
	
	   /*
	      xmlxtr：xml数据，其中value放在第一个子对象中，文本放在第二个子对象中,比如：
	      <user><id>100</id><name>张三</name></user><user><id>101</id><name>李四</name></user>
	      tagName：xml对象中包含级联对象的标签，比如：user
	      selectObj：select对象。
	   */
	   function showSelect(xmlStr,tagName,selectObj){
		    var xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		    xmlDoc.async="false";
		    xmlDoc.loadXML(xmlStr);

		   var obj=xmlDoc.getElementsByTagName(tagName);
		    
		    var opt;
		    var txt;
		    selectObj.innerHTML="";//清空现有的option。
		    for(i=0;i<obj.length;i++)  {
		    	opt = document.createElement('OPTION');
		    	opt.setAttribute('value', obj[i].childNodes[0].text);//第一个子对象放value
	            txt = document.createTextNode(obj[i].childNodes[1].text);//第二个子对象放option文本
	            opt.appendChild(txt);
	            selectObj.appendChild(opt);
		    }
	   }
	
	   function formIsDirty(form) {
		  for (var i = 0; i < form.elements.length; i++) {
		     var element = form.elements[i];
		     var type = element.type;
		     if (type == "checkbox" || type == "radio") {
		        if (element.checked != element.defaultChecked) {
		          return true;
		        }
		     }
		     else if (type == "hidden" || type == "password" || type == "text" || type == "textarea") {
		        if (element.value != element.defaultValue) {
		          return true;
		        }
		     }
		     else if (type == "select-one" || type == "select-multiple") {
		        for (var j = 0; j < element.options.length; j++) {
		           if (element.options[j].selected != element.options[j].defaultSelected) {
		              return true;
		           }
		        }
		     }
		  }
		  return false;
	   }
	
	   
	   
	   
	   