<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <%@include file="/common/header.jsp" %>
    <script type="text/javascript">
        var hyperlink = "../appManager/appArtistPageTemplateManager.do";
        var fulllink = contextpath + "appManager/appArtistPageTemplateManager.do";

        function renew() {
            var order = getElement("query.order");
            order.value = "";
            var desc = getElement("query.orderDirection");
            desc.value = "";
            var pn = getElement("query.pageNumber");
            pn.value = "1";
            var ps = getElement("query.pageSize");
            ps.value = "10";
            goSearch();
        }

        var pageAction = '?action=OPERATION_ADD_PAGE';

        function closedialog(ret) {
            if (ret == true || ret == 'true') {
                goSearch();
            }
            editMode = "";
        }

        function goSubmit() {
            var id = findMultiSelected("添加", appArtistPageTemplateManager_grid);
            if (id == null) return;
            var url = fulllink + "?action=OPERATION_SAVE&record.tempId=" + $$('tempId').value + '&ids=' + id;
            document.forms[0].action = url;
            document.forms[0].submit();

        }

        function goSearch() {
            goappArtistPageTemplateManagerGridSearch();
        }

        function init() {
            showMessage('<s:property value="errorMessage" escape="false"/>');
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
    <s:form action="appArtistPageTemplateManager" id="appArtistPageTemplateManagerForm" method="post"
            namespace="/appManager">
        <s:hidden name="query.order"/>
        <s:hidden name="query.orderDirection"/>
        <s:hidden name="query.pageNumber"/>
        <s:hidden name="query.recordCount"/>
        <s:hidden name="query.pageCount"/>
        <s:hidden name="query.parameters.tempId" id="tempId"/>
        <table width="99%" id="tbl" cellpadding="0" cellspacing="0"
               class="queryTable">
            <tr>
                <td width="100%" style="height:30px;" class="textL">
                    &nbsp;&nbsp;艺术家名：<s:textfield style="width:120px;" name="query.parameters.artistName"></s:textfield>
                    &nbsp;&nbsp;<gl:button name="btnQuery" onClick="goSearch()">查询</gl:button>
                </td>
            </tr>
        </table>
        <table id="btn" width="99%" cellspacing="1" cellpadding="1"
               class="controlTable">
            <tr>
                <td>
                    <gl:button styleClass="sbuBtnStyle" icon="addIcon" onClick="goSubmit()">添加</gl:button>
                </td>
            </tr>
        </table>
        <gl:grid styleClass="pageTurn" id="appArtistPageTemplateManager" page="true"
                 form="appArtistPageTemplateManagerForm" property="query" cellPadding="0"
                 cellSpacing="0">
            var grid = new dhtmlXGridObject();
            grid.setImagePath(imagePath);
            var headAlign = [tCenter,tCenter,tCenter,tCenter];
            grid.setHeader("#master_checkbox,艺术家照片,中文名,英文名",null, headAlign);
            grid.setInitWidthsP("5,25,25,45");
            grid.setColAlign("center,center,center,center");
            grid.setColTypes("ch,ro,ro,ro");
            grid.setColSorting("na,str,str,str");
            grid.init();
            var gDiv = $$('appArtistPageTemplateManager_box');
        </gl:grid>
    </s:form>
</div>

<%@include file="/common/dialog.jsp" %>
<%@include file="/common/resizeList1.jsp" %>

</body>
</html>