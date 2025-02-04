<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%
   String path = request.getContextPath();
			String CONTEXT_PATH = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<style type="text/css">
body {
	border: none;
	font-family: 微软雅黑, 宋体, Verdana, Geneva, sans-serif;
	font-size: 14px;
	margin: 0px;
	overflow: hidden;
	text-align:center;
}

#userName {
    position:absolute;
	font-family: "微软雅黑";
    font-size:18px;
	border: 0px solid #ffffff;
	outline: none;
	background-color:white;
}

#userName:-webkit-autofill {  
    -webkit-box-shadow: 0 0 0px 1000px white inset !important;  
}  


#password {
    position:absolute;
 	font-family: "微软雅黑";
    font-size:18px;
	border: 0px solid #ffffff;
	outline: none;
	background-color:white;
}

#password:-webkit-autofill {  
    -webkit-box-shadow: 0 0 0px 1000px white inset !important;  
}  


#btnSubmit {
    position:absolute;
    font-family: "微软雅黑";
    font-size:18px;
    color:white;
   	border: 1px;
	background-color:#0132cd;
	cursor: pointer;
}

#imageLogin {
   position:absolute;
   top:0px;
   left:0px;
   width:100%;
   height:100%;
}

</style>

<script language="javascript" type="text/javascript">  
var msg="";
function checkLength(o,n,min,max) {
   if(o.value.length > max || o.value.length < min) {
	    msg=msg+n+("长度必须介于"+min+"和"+max+"之间。\r\n");
			return false;
	 } 
	 else {
      return true;
   }
}

function checkRegexp(o,regexp,n) {
   if(regexp.test(o.value)) {
      return true;
   } 
   else {
	    msg=msg+n+"\r\n";
      return false;
	 }
}

function checkInput(userName,password){
   var bValid = true;

   bValid = bValid & checkLength(userName,"用户名",4,30);
   //bValid = bValid & checkRegexp(userName,/^[a-z]([0-9a-z_])+$/i,"用户名必须是字母或数字。");
   bValid = bValid & checkLength(password,"密码",8,30);
					
   return bValid;
}
         
function goValidate(){
   var uName = document.getElementById("userName");
   var password =document.getElementById("password");
   if(checkInput(uName,password)){
//	   document.forms[0].action="<%=CONTEXT_PATH%>login.do?action=login";
	   document.forms[0].submit();
   }
   else {
      alert(msg);
      msg="";
   }
}

function login(){
	var msg='<%=request.getAttribute("errMsg")%>';
	if(msg!='null') alert(msg);
	document.getElementById("userName").focus();
}

function goKeyPress() {
	if(window.event.keyCode == 13) goValidate();  
}

</script>
</head>
<body onload="login()">
<form id="loginForm" action="<%=CONTEXT_PATH%>login.do?action=login" method="post">
   <img id="imageLogin" src="<%=CONTEXT_PATH%>images/login_bg.jpg" />
   <input name="userName" id="userName" type="text"  onkeypress="goKeyPress()" value="admin"  maxlength="20" />
   <input name="password" id="password" type="password"  onkeypress="goKeyPress()" value="CHENYANG" />
   <input id="btnSubmit" type="button" onclick="goValidate()" onkeypress="goKeyPress()" value="登     录"/>
</form>
<script language="javascript" type="text/javascript">  
var iobj=window.document.getElementById('imageLogin');
var uobj=window.document.getElementById('userName');
var pobj=window.document.getElementById('password');
var bobj=window.document.getElementById('btnSubmit');

if(window.addEventListener) {
     window.addEventListener("resize",goResize,false); 
}
else {
     window.attachEvent('onresize',goResize);
}

function goResize() {
  if(iobj.offsetWidth<1024) iobj.style.width="1024px";
  if(iobj.offsetHeight<768) iobj.style.height="768px";

  var he=iobj.offsetHeight;
  var wd=iobj.offsetWidth;
  var rtHe=he/760;
  var rtWd=wd/1440;

  uobj.style.top=parseInt(290*rtHe)+"px";
  pobj.style.top=parseInt(370*rtHe)+"px";
  bobj.style.top=parseInt(440*rtHe)-4+"px";

  uobj.style.left=parseInt(925*rtWd)+"px";
  pobj.style.left=parseInt(925*rtWd)+"px";
  bobj.style.left=parseInt(883*rtWd)-1+"px";

  uobj.style.width=parseInt(300*rtWd)+"px";
  pobj.style.width=parseInt(300*rtWd)+"px";
  bobj.style.width=parseInt(350*rtWd)+"px";

  bobj.style.height=parseInt(44*rtHe)+"px";
}
goResize();  
</script>
</body>

</html>