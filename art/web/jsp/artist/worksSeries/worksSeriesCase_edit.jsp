<%@page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/header.jsp" %>
    <script language="javascript">

        var fulllink = contextpath + "artist/ArtistWorksSeriesCase.do";

        function goCancel() {
            parent.closedialog('false');
        }

        function goSubmit() {
            if (checkFormInput()) {
                var url = fulllink + "?action=UPDATE";
                document.forms[0].action = url;
                document.forms[0].submit();
            }
        }

        function checkFormInput() {
            if (!checkString(getElement('form.record.caseName'), "个案名称", 30, true)) return;
            if (!checkString(getElement('form.record.author'), "作者", 30, true)) return;
            if (!checkString(getElement('form.record.caseContent'), "内容", 30, true)) return;
            return true;
        }

        function closedialog(ret) {
            if (ret == 'true') {
                goSearch();
            }
            editMode = "";
        }


        function init() {
            showMessage('<s:property value="errorMessage" escape="false"/>');
            var reopenTime = initCalendar('caseTime','imgEndDate');
        }


    </script>
    <style type="text/css">
        html, body {
            width: 100%;
            height: 100%;
            margin: 0px;
            padding: 0px;
            overflow: hidden;
        }
    </style>
</head>

<body onload="init()">
    <s:form action="ArtistWorksSeries" id="ArtistWorksSeriesForm" method="post" namespace="/artist">
        <s:hidden name="record.seriesId" id="seriesId"/>
        <s:hidden name="record.id" />
        <div id="formDiv" class="formDiv" style="padding: 0px;">
            <table cellpadding="0" cellspacing="0" width="99%" class="formTable">
                <tr>
                    <td class="textR" width="15%"><font color="red">*</font>个案名称：</td>
                    <td width="35%"><s:textfield name="form.record.caseName"/></td>
                    <td class="textR" width="15%"><font color="red">*</font>作者：</td>
                    <td width="35%"><s:textfield name="form.record.author"/></td>
                </tr>
                <tr>
                    <td class="textR" width="15%"><font color="red">*</font>时间：</td>
                    <td colspan="3">
                        <table cellspacing="0" width="100%" class="tbnBtnStyle">
                            <tr>
                                <td>
                                    <s:textfield readonly="true" name="form.record.caseTime" id="caseTime"/>
                                </td>
                                <td width="20px">
                                    <img id="imgEndDate" style="cursor:pointer;" src="<%=CONTEXT_PATH%>dhtmlx/imgs/icon/7-4.gif"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="textR" width="15%">内容：</td>
                    <td colspan="3"><s:textarea name="form.record.caseContent"/></td>
                </tr>
            </table>
        </div>
    </s:form>

    <div id="btnDiv" class="btnDiv">
        <gl:button name="btnAdd" onClick="goSubmit()">保存</gl:button>&nbsp;
        <gl:button name="btnAdd" onClick="goCancel()">关闭</gl:button>
    </div>

    <%@include file="/common/dialog.jsp"%>
    <%@include file="/common/resize.jsp" %>
</body>
</html>