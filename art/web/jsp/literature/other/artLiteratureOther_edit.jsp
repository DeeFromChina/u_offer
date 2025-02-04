<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/common/header.jsp" %>

    <script language="javascript">
        var hyperlink = contextpath + 'literature/ArtLiteratureOther.do';
        var fulllink = contextpath + 'literature/ArtLiteratureOther.do';

        function goArtArtist() {
            var url_link = fulllink + '?action=ARTIST&ids=' + $$("artistId").value;
            openWindow("选择作者", url_link, 200, 300);
        }

        function filerow() {
            var t = document.getElementById('tblUpload');
            var rows = t.rows.length;
            for (; rows < 1; rows++) {
                var x = t.insertRow(rows);
                var one = x.insertCell(0);
                one.className = "textL";
                var two = x.insertCell(1);
                one.style.width = "90%";
                one.innerHTML = '<input type="file" id="file" class="checkFile" name="files" />';
            }
        }

        function addFileRow() {
            var t = document.getElementById('tblUpload');
            var rows = t.rows.length;
            if (rows > 5) {
                alert("超过最大上传文件数！");
                return;
            } else {
                var x = t.insertRow(rows);
                var one = x.insertCell(0);
                one.className = "textL";
                var two = x.insertCell(1);
                one.innerHTML = '<input type="file" id="file" class="checkFile" name="files" />';
                two.innerHTML = '<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
            }
        }

        function delRow(obj) {
            var childNode = obj.parentNode.parentNode;
            var parentNode = obj.parentNode.parentNode.parentNode;
            parentNode.removeChild(childNode);
            filerow();
        }

        function checkFormInput() {
            if (!checkString(getElement('record.artistId'), "艺术家", 100, true))
                return;
            if (!checkString(getElement('form.record.literatureTitle'), "文献标题",
                    200, true))
                return;
            return true;
        }

        function closedialog(ret) {
            var res = ret['ret'];
            if (res == true) {
                getElement('record.artistId').value = ret['str'];
                getElement('record.artistName').value = ret['str1'];
            }
            if (ret == 'true') {
                goSearch();
            }
            editMode = "";
        }

        function goCancel() {
            parent.closedialog('false');
        }

        function goSubmit() {
            if (checkFormInput()) {
                var url = fulllink + '?action=UPDATE';
                document.forms[0].action = url;
                document.forms[0].submit();
            }
        }

        function init() {
            showMessage('<s:property value="errorMessage" escape="false"/>');
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
<s:form action="ArtLiteratureOther" id="ArtLiteratureOtherForm"
        method="post" namespace="/literature" enctype="multipart/form-data">
    <s:hidden name="record.id" id="id"/>
    <div id="tblForm"></div>
    <div id="page_0" style="padding:2px; height:100%; width:100%;">
        <div id="formDiv" class="formDiv" style="padding:2px;">
            <table cellpadding="0" cellspacing="0" style="width: 99%;" class="formTable">
                <tr>
                    <td class="textR" width="15%"><font color="red">*</font>艺术家：</td>
                    <td width="35%" class="textL">
                        <s:textfield readonly="true" name="record.artistName" style="width:80%;"/>
                        <s:hidden name="record.artistId" id="artistId"/>
                        <gl:button name="btnArtArtist" styleClass="btnFormStyle" onClick="goArtArtist()">选择</gl:button>
                    </td>
                    <td class="textR" width="15%"><font color="red">*</font>文献标题：</td>
                    <td width="35%"><s:textfield name="form.record.literatureTitle"/></td>
                </tr>
                <tr>
                    <td class="textR">主编：</td>
                    <td class="textL"><s:textfield name="form.record.auther"/></td>
                    <td class="textR">刊登时间：</td>
                    <td class="textL"><s:textfield name="form.record.currerTime" placeholder="xxxx年xx月xx日"/></td>
                </tr>
                <tr>
                    <td class="textR">出版物名称：</td>
                    <td class="textL"><s:textfield name="form.record.pubName"/></td>
                    <td class="textR">出版物期数：</td>
                    <td class="textL"><s:textfield
                            name="form.record.publicationPeriod"/></td>
                </tr>
                <tr>
                    <td class="textR">出版社：</td>
                    <td class="textL"><s:textfield name="form.record.press"/></td>
                    <td class="textR">相关页：</td>
                    <td class="textL"><s:textfield
                            name="form.record.relevantPages"/></td>
                </tr>
                <tr>
                    <td class="textR">附件来源：</td>
                    <td class="textL" colspan="3"><s:textfield
                            name="form.record.attachmentSource"/></td>
                </tr>
                <tr>
                    <td>附件：</td>
                    <td colspan="3" class="textL">
                        <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0"
                               id="tblUpload">
                            <tr>
                                <td class="textL" style="width:90%;"></td>
                                <td>
                                    <input type="button" style="width:50px;" value="增加"
                                           onclick="addFileRow('tblUpload1')"/>
                                </td>
                            </tr>
                            <s:iterator value="cookie" id="c" status="co">
                                <tr>
                                    <td class="textL" style="width:90%;">
                                        <input type="hidden" class="fileName" id="${c.saveName}" name="attachment"
                                               value="${c.name}/${c.saveName}"/>
                                        <a href="<%=CONTEXT_PATH%>upload/literature/other/<s:property value="record.id"/>/${c.saveName}"
                                           target="_blank">${c.name}</a>
                                    </td>
                                    <td>
                                        <input type="button" style="width:50px;" name="del" value="删除"
                                               onclick="delRow(this)"/>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="4" class="textC">文献研究</td>
                </tr>
                <tr>
                    <td class="textR" width="15%">主观重要度评级：</td>
                    <td><s:select list="codeSets.COMMENT_LEVEL" listKey="value"
                                  listValue="codeName" name="record.subjectiveEval"/></td>
                    <td class="textR" width="15%">客观重要度评级：</td>
                    <td><s:select list="codeSets.COMMENT_LEVEL" listKey="value"
                                  listValue="codeName" name="record.objectiveEval"/></td>
                </tr>
                <tr>
                    <td class="textR" width="15%">核心论断：</td>
                    <td colspan="3" class="textL"><s:textarea height="80px"
                                                              name="record.coreThesis"/></td>
                </tr>
            </table>
        </div>
        <iframe id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    </div>
</s:form>
<div id="btnDiv" class="btnDiv">
    <gl:button name="btnAdd" onClick="goSubmit()">保存</gl:button>
    &nbsp;
    <gl:button name="btnAdd" onClick="goCancel()">关闭</gl:button>
</div>
<%@include file="/common/dialog.jsp" %>
<script type="text/javascript">
    var mainbody = window.document.body;
    if (window.addEventListener) {
        window.addEventListener("resize", goResize, false);
    }
    else {
        window.attachEvent('onresize', goResize);
    }

    var fDiv = window.document.getElementById('formDiv');
    var bDiv = window.document.getElementById('btnDiv');
    var btbl = window.document.getElementById('tblForm');

    function goResize() {
        fDiv.style.width = mainbody.offsetWidth - fDiv.offsetLeft * 2 + "px";
        bDiv.style.width = mainbody.offsetWidth - bDiv.offsetLeft * 2 + "px";
        fDiv.style.height = mainbody.offsetHeight - bDiv.offsetHeight - fDiv.offsetTop * 2 + 5 + "px";
        bDiv.style.top = fDiv.offsetHeight + fDiv.offsetTop * 2 - 5 + "px";
        if (btbl) {
            btbl.style.width = fDiv.offsetWidth - fDiv.offsetLeft - 9 + "px";
            btbl.style.height = fDiv.offsetHeight - fDiv.offsetTop - 12 + "px";
        }
    }
    goResize();

    var tabbar = new dhtmlXTabBar("tblForm", "top");
    tabbar.setSkin('dhx_terrace');
    tabbar.setArrowsMode("auto");
    tabbar.addTab("a0", "文献基本信息", "120px");
    tabbar.addTab("a1", "相关作品", "120px");
    tabbar.tabs("a0").attachObject("formDiv");
    tabbar.tabs("a0").setActive();
    tabbar.tabs("a1").attachObject("page_1");

    window.frames["page_1"].location.href = fulllink + '?action=RELATED_WORKS&query.parameters.otherId=' + $$('id').value;
</script>
</body>
</html>