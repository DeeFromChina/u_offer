<%@page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/header.jsp" %>
    <script language="javascript">
        var contextpath = "<%=CONTEXT_PATH%>";
        var fulllink = contextpath + "publication/artPublication.do";
        var fulllink_literature = contextpath + "publication/pubLiterature.do";
        var fulllink_works = contextpath + "publication/pubWorks.do";
        var fulllink_photo = contextpath + "artist/ArtPhoto.do";
        var fulllink_agency = contextpath + "publication/pubAency.do";
        var fulllink_artist = contextpath + "publication/pubArtist.do";
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
            if (!checkString(getElement('record.publicationName'), "出版物名称", 30, true)) return;
            if (!checkNumeric(getElement('record.publicationYear'), "出版年份", 30, true, false)) return;
            if (!checkNumeric(getElement('record.publicationMonth'), "出版月份", 30, true, false)) return;
            if (!checkNumeric(getElement('record.price'), "价格", 30, true, false)) return;
            if (!checkString(getElement('record.publicationType'), "出版物类型", 30, true)) return;
            return true;
        }

        function closedialog(ret) {
            var res = ret['ret'];
            if (res == true) {
                getElement('record.artistId').value = ret['str'];
                getElement('record.artArtistName').value = ret['str1'];
            }
            editMode = "";
        }

        function hidebtn() {
            $(".dhxtabbar_tab_text").each(function () {
                this.addEventListener('click', function () {
                    if (this.innerHTML != "出版物信息") {
                        document.getElementById("save").style.display = "none";
                    } else {
                        document.getElementById("save").style.display = "";
                    }
                });
            });
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
                one.innerHTML = '<input type="file" class="checkFile" name="files" />';
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
                one.innerHTML = '<input type="file" class="checkFile" name="files" />';
                two.innerHTML = '<input type="button" style="width:50px;" name="del" value="删除" onclick="delRow(this)"/>';
            }
        }

        function delRow(obj) {
            var childNode = obj.parentNode.parentNode;
            var parentNode = obj.parentNode.parentNode.parentNode;
            parentNode.removeChild(childNode);
            filerow();
        }

        function init() {
            showMessage('<s:property value="errorMessage" escape="false"/>');
            hidebtn();
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
<s:form action="artPublication" id="artPublicationForm" method="post" namespace="/publication"
        enctype="multipart/form-data">
    <s:hidden name="record.id" id="id"/>
    <s:hidden name="record.next" id="next"/>
    <div id="tblForm"></div>
    <div id="page_0" style="padding:2px; height:100%; width:100%;">
        <div id="formDiv" class="formDiv" style="padding:2px;">
            <table cellpadding="0" cellspacing="0" class="formTable" style="width: 99%;">
                <tr>
                    <td width="15%" class="textR"><font color="red">*</font>出版物名称：</td>
                    <td width="35%"><s:textfield name="record.publicationName"/></td>
                    <td width="15%" class="textR">出版机构：</td>
                    <td width="35%"><s:textfield name="record.press"/></td>
                </tr>
                <tr>
                    <td class="textR">出版时间：</td>
                    <td class="textL"><s:textfield name="record.publicationYear" cssStyle="width:40%"
                                                   placeholder="xxxx"/>年<s:textfield name="record.publicationMonth"
                                                                                     placeholder="xx"
                                                                                     cssStyle="width:40%"/>月
                    </td>
                    <td class="textR">编辑信息：</td>
                    <td><s:textfield name="record.editor"/></td>
                </tr>
                <tr>
                    <td class="textR">发行量：</td>
                    <td><s:textfield name="record.circulation"/></td>
                    <td class="textR">印次：</td>
                    <td><s:textfield name="record.impression"/></td>
                </tr>
                <tr>
                    <td class="textR"><font color="red">*</font>出版物类型：</td>
                    <td><s:select list="codeSets.PUBLI_TYPE" listKey="value" listValue="codeName"
                                  name="record.publicationType"></s:select></td>
                    <td class="textR">总页数：</td>
                    <td><s:textfield name="record.pageCount" placeholder="只填写数字"/></td>
                </tr>
                <tr>
                    <td class="textR">价格：</td>
                    <td class="textL"><s:textfield name="record.price" cssStyle="width:50%"/>&nbsp;<s:select
                            list="codeSets.CURRENCY_TYPE" listKey="value" listValue="codeName" cssStyle="width:45%"
                            name="record.priceUnit" placeholder="单位"/></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td class="textR">封面：</td>
                    <td class="textL" colspan="3">
                        <a href="<%=CONTEXT_PATH%>upload/artist/album/<s:property value='record.eName'/>/<s:property value='record.cover'/>"
                           target="_blank"><s:property value="record.coverName"/></a>
                        <s:file name="cover" cssStyle="width:70%"></s:file></td>
                </tr>
                <tr>
                    <td class="textR">封底：</td>
                    <td class="textL" colspan="3">
                        <a href="<%=CONTEXT_PATH%>upload/artist/album/<s:property value='record.eName'/>/<s:property value='record.backCover'/>"
                           target="_blank"><s:property value="record.backCoverName"/></a>
                        <s:file name="backCover" cssStyle="width:70%"></s:file>
                    </td>
                </tr>
                <tr>
                    <td class="textR">目录/其他：</td>
                    <td colspan="3">
                        <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0"
                               id="tblUpload">
                            <tr>
                                <td></td>
                                <td>
                                    <input type="button" style="width:50px;" value="增加"
                                           onclick="addFileRow('tblUpload1')"/>
                                </td>
                            </tr>
                            <s:iterator value="cookie" id="c" status="co">
                                <tr>
                                    <td class="textL" style="width:90%;">
                                        <input type="hidden" class="fileName" id="${c.saveName}"
                                               name="catalogOtherNames" value="${c.name}/${c.saveName}"/>
                                        <a href="<%=CONTEXT_PATH%>/upload/artist/album/<s:property value="record.eName"/>/${c.saveName}"
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
            </table>
        </div>
    </div>
    <iframe id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_2" name="page_2" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_3" name="page_3" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_4" name="page_4" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_5" name="page_5" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
</s:form>

<div id="btnDiv" class="btnDiv">
    <gl:button name="btnAdd" onClick="goSubmit()" id="save">保存</gl:button>
    &nbsp;
    <gl:button name="btnAdd" onClick="goCancel()">关闭</gl:button>
</div>

<script type="text/javascript">

</script>

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
    tabbar.addTab("a0", "出版物信息", "120px");
    tabbar.addTab("a1", "相关艺术家", "120px");
    tabbar.addTab("a2", "相关文章", "120px");
    tabbar.addTab("a3", "相关作品", "120px");
    tabbar.addTab("a4", "相关照片", "120px");
    tabbar.addTab("a5", "合作机构", "120px");
    tabbar.tabs("a0").attachObject("formDiv");
    tabbar.tabs("a0").setActive();
    if (document.getElementById('next').value == "true") {
        tabbar.tabs("a1").setActive();
        //document.getElementById("save").style.display="none";
    }
    tabbar.tabs("a1").attachObject("page_1");
    tabbar.tabs("a2").attachObject("page_2");
    tabbar.tabs("a3").attachObject("page_3");
    tabbar.tabs("a4").attachObject("page_4");
    tabbar.tabs("a5").attachObject("page_5");
    tabbar.enableAutoReSize(true);
    tabbar.enableTabCloseButton(true);
    tabbar.attachEvent("onTabClick", function (idClicked, idSelected) {
        if (idClicked != "a0") {
            document.getElementById("btnDiv").style.display = "none";
        } else {
            document.getElementById("btnDiv").style.display = "";
        }
    });
    window.frames["page_1"].location.href = fulllink_artist + '?query.parameters.publicationId=' + $$('id').value;
    window.frames["page_2"].location.href = fulllink_literature + '?query.parameters.pubId=' + $$('id').value;
    window.frames["page_3"].location.href = fulllink_works + '?query.parameters.pubId=' + $$('id').value;
    window.frames["page_4"].location.href = fulllink_photo + '?query.parameters.type=1&query.parameters.pubId=' + $$('id').value;
    window.frames["page_5"].location.href = fulllink_agency + '?query.parameters.publicationId=' + $$('id').value;
</script>
</body>
</html>