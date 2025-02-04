<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/common/header.jsp" %>

    <script language="javascript">
        var hyperlink = "../system/sysLog.do";
        var fulllink = contextpath + "/system/sysLog.do";


        function goDel() {
            if ($$('period').style.display != "none") {
                var beginTime = $$('beginTime').value;
                var endTime = $$('endTime').value;

                if (isEndDateGreater(beginTime, endTime)) {
                    if (confirm("是否要清除该时间段内的日志么？")) {
                        console.log(beginTime);
                        console.log(endTime);
                        var data = ajaxSubmit(fulllink + "?action=delete", "sysLogForm");
                        if (data.exit > 0) {
                            alert(data.message);
                            return;
                        } else {
                            cleanTime();
                            goSearch();
                        }
                    }
                }
            } else {
                $$('period').style.display = "";
                alert("请填写时间段");
            }
        }

        function cleanTime() {
            $$('period').style.display = "none";
            $$('beginTime').value = "";
            $$('endTime').value = "";
        }

        function renew() {
            var order = getElement("query.order");
            order.value = "";
            var desc = getElement("query.orderDirection");
            desc.value = "";
            var pn = getElement("query.pageNumber");
            pn.value = "1";
            var ps = getElement("query.pageSize");
            ps.value = "20";
            goSearch();
        }

        function closedialog(ret) {
            if (ret == true || ret == 'true') {
                goSearch();
            }
            editMode = "";
        }

        function goSearch() {

            gosysLogGridSearch();
        }

        function compareDate(date1, date2) {
            return Date.parse(date1) <= Date.parse(date2);
        }

        function isEndDateGreater(startDate, endDate) {
            var dateS = new Date(startDate.replace(/-/g, "/"));
            var dateE = new Date(endDate.replace(/-/g, "/"));
            if (compareDate(dateS, dateE)) {
                return true;
            } else {
                alert("开始时间应该早于结束时间");
                return false;
            }
        }

        function init() {
            showMessage('<s:property value="errorMessage" escape="false"/>');
            var beginTime = initCalendar("beginTime", "imgBeginTime");
            var endTime = initCalendar("endTime", "imgEndTime");
            renew();
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
    <s:form action="sysLog" id="sysLogForm" method="post" namespace="/system">
        <s:hidden name="record.hide" id="hide"/>
        <s:hidden name="query.order"/>
        <s:hidden name="query.orderDirection"/>
        <s:hidden name="query.pageNumber"/>
        <s:hidden name="query.recordCount"/>
        <s:hidden name="query.pageCount"/>
        <table width="99%" id="tbl" cellpadding="0" cellspacing="0" class="queryTable">
            <tr>
                <td class="textL" style="width: 80%">
                    &nbsp;&nbsp;操作人：<s:textfield style="width:100px;" name="query.parameters.operator"/>
                    &nbsp;&nbsp;模块：<s:textfield style="width:100px;" name="query.parameters.operModule"/>
                    &nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
                </td>
            </tr>
        </table>
        <table id="btn" width="99%" cellspacing="1" cellpadding="1" class="controlTable">
            <tr>
                <td>
                    <sapn id="period" style="display: none">
                        &nbsp;&nbsp;开始时间：<s:textfield id="beginTime" readonly="true" name="record.beginTime"/>
                        <span style="width: 15px;">
                            <img id="imgBeginTime" style="cursor:pointer;"
                                 src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif"/>
                        </span>
                        &nbsp;&nbsp;结束时间：<s:textfield id="endTime" readonly="true" name="record.endTime"/>
                        <span style="width: 15px;">
                            <img id="imgEndTime" style="cursor:pointer;"
                                 src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif"/>
                        </span>
                    </sapn>
                    <gl:button styleClass="sbuBtnStyle" icon="delIcon" onClick="goDel()">删除</gl:button>
                </td>
            </tr>
        </table>
        <gl:grid styleClass="pageTurn" id="sysLog" page="true" form="sysLogForm" property="query"
                 cellPadding="0" cellSpacing="0">
            var grid = new dhtmlXGridObject();
            grid.setImagePath(imagePath);
            var headAlign = [tCenter,tCenter,tCenter,tCenter,tCenter];
            grid.setHeader("操作人,时间,模块,功能,结果",null, headAlign);
            grid.setInitWidthsP("8,10,10,10,62");
            grid.setColAlign("center,center,center,center,left");
            grid.setColTypes("ro,ro,ro,ro,ro");
            grid.setColSorting("str,str,str,str,str");
            grid.enableMultiline(true);
            grid.init();
            var gDiv = $$('sysLog_box');
        </gl:grid>
    </s:form>
</div>
<%@include file="/common/dialog.jsp" %>
<%@include file="/common/resizeList1.jsp" %>
</body>
</html>