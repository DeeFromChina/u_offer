<%@page language="java" pageEncoding="UTF-8"%>
<%
   String path = request.getContextPath();
	String CONTEXT_PATH = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<title></title>
<script src="<%=CONTEXT_PATH%>dhtmlxScheduler/dhtmlxscheduler.js" type="text/javascript" charset="utf-8"></script>
<script src="<%=CONTEXT_PATH%>dhtmlxScheduler/locale_cn.js" type="text/javascript" charset="utf-8"></script>
<link rel="stylesheet" href="<%=CONTEXT_PATH%>dhtmlxScheduler/dhtmlxscheduler.css" type="text/css" media="screen" title="no title" charset="utf-8">
<link rel="stylesheet" href="<%=CONTEXT_PATH%>dhtmlxScheduler/dhtmlxscheduler_glossy.css" type="text/css" media="screen" title="no title" charset="utf-8">

<style type="text/css" media="screen">
html,body {
	margin: 0px;
	padding: 0px;
	height: 100%;
	overflow: hidden;
}
</style>

<script type="text/javascript" charset="utf-8">
	function init() {
		scheduler.config.multi_day = true;

		scheduler.config.xml_date = "%Y-%m-%d %H:%i";
		scheduler.init('scheduler_here', new Date(2011, 11, 25), "month");
		scheduler.load("<%=CONTEXT_PATH%>dhtmlxScheduler/events.xml");

	}
</script>
</head>

<body onload="init();">
	<div id="scheduler_here" class="dhx_cal_container" style='width: 100%; height: 100%;'>
		<div class="dhx_cal_navline">
			<div class="dhx_cal_prev_button">&nbsp;</div>
			<div class="dhx_cal_next_button">&nbsp;</div>
			<div class="dhx_cal_date"></div>
			<div class="dhx_cal_tab" name="month_tab" style="display:none;right: 76px;"></div>
		</div>
		<div class="dhx_cal_header"></div>
		<div class="dhx_cal_data"></div>
	</div>
</body>