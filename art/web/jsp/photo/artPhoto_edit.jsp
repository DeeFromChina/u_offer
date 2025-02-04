<%@page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/header.jsp" %>
    <script language="javascript">
        var fulllink = contextpath + "artist/ArtPhoto.do";

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
            if (!checkString(getElement('record.artistName'), "艺术家", 100, true)) return;
            if (!checkString(getElement('record.photoName'), "图片名称", 100, true)) return;
            if (getElement('files').value != undefined && getElement('files').value != '') {
                if (!checkFile()) return;
            }
            return true;
        }

        function checkFile() {
            var isPass = true;
            $(".checkFile").each(function () {
                var format = this.value.split(".");
                var i = format.length;
                if (format[i - 1] != "jpg" && format[i - 1] != "jpeg" && format[i - 1] != "gif" && format[i - 1] != "png") {
                    alert("只能上传图片格式！");
                    isPass = false;
                    return false;
                }
            });
            return isPass;
        }

        function closedialog(ret) {
            var res=ret['ret'];
            if(res=='artist') {
                getElement('record.artistId').value=ret['str'];
                getElement('record.artistName').value=ret['str1'];
            }
            if(res=='publication') {
                console.log(ret);
                $$('pubId').value = ret['str1'];
                $$('publicationName').value = ret['str2'];
            }
            editMode = "";
        }


        function init() {
            showMessage('<s:property value="errorMessage" escape="false"/>');
        }

        function goArtArtist(ret) {
            if (ret == 'choose') {
                var url_link = contextpath + 'artist/ArtPhoto.do?action=ARTIST&ids=' + $$("artistId").value;
                openWindow("选择作者", url_link, 200, 300);
            }
            if (ret == 'cancel') {
                $$("artistName").value = "";
                $$("artistId").value = "";
            }
        }

        function goArtPublication(ret){
            if(ret == 'choose'){
                var url_link=contextpath+'publication/artPublication.do?query.parameters.type=1';
                openWindow("选择出版物",url_link,0.8,0.8);
            }
            if(ret == 'cancel'){
                $$("publicationName").value = "";
                $$("publicationId").value = "";
            }
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
<s:form action="ArtPhoto" id="ArtPhotoForm" method="post" namespace="/artist" enctype="multipart/form-data">
    <s:hidden name="record.id" id="id"/>
    <s:hidden name="record.pubId" id="pubId"/>
    <s:hidden name="record.eName" id="eName"/>
    <div id="formDiv" class="formDiv" style="padding: 0px;">
        <table cellpadding="0" cellspacing="0" width="99%" class="formTable">
            <tr>
                <td class="textR" width="15%"><font color="red">*</font>图片名称：</td>
                <td class="textL" width="35%"><s:textfield name="record.photoName"/></td>
                <td class="textR" width="15%"><font color="red">*</font>图片：</td>
                <td class="textL" width="35%">
                    <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
                        <s:iterator value="cookie" id="p" status="pp">
                            <tr>
                                <td class="textL">
                                    <a href="<%=CONTEXT_PATH%>upload/photo/<s:property value='record.photoPath'/>/${p.saveName}"
                                       target="_blank">${p.name}</a>
                                    <input type="file" name="files" id="file" class="checkFile" style="width:70%"/>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="textR"><font color="red">*</font>艺术家：</td>
                <td>
                    <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
                        <tr>
                            <td>
                                <s:textfield readonly="true" name="record.artistName" id="artistName"/>
                                <s:hidden name="record.artistId" id="artistId"/>
                            </td>
                            <td width="45px">
                                <gl:button name="btnArtMedium" styleClass="btnFormStyle"
                                           onClick="goArtArtist('choose')">选择</gl:button>
                            </td>
                            <td width="45px">
                                <gl:button name="btnArtMedium" styleClass="btnFormStyle"
                                           onClick="goArtArtist('cancel')">清空</gl:button>
                            </td>
                        </tr>
                    </table>
                </td>
                <s:if test='record.type=="1"'>
						<td colspan="2"></td>
				</s:if>
				<s:else>
                <td class="textR">出版物：</td>
                <td class="textL">
                    <table cellpadding="0" cellspacing="0" width="100%" class="tbnBtnStyle" border="0">
                        <tr>
                            <td>
                                <s:textfield readonly="true" name="record.publicationName" id="publicationName"/>
                            </td>
                            <td width="45px">
                                <gl:button name="btnArtMedium" styleClass="btnFormStyle"
                                           onClick="goArtPublication('choose')">选择</gl:button>
                            </td>
                            <td width="45px">
                                <gl:button name="btnArtMedium" styleClass="btnFormStyle"
                                           onClick="goArtPublication('cancel')">清空</gl:button>
                            </td>
                        </tr>
                    </table>
                </td>
                </s:else>
            </tr>
            <tr>
                <td class="textR">类型：</td>
                <td class="textL"><s:select list="codeSets.PHOTO_TYPE" listKey="value" listValue="codeName"
                                            name="record.photoType"/></td>
                <td class="textR">时间：</td>
                <td class="textL">
                    <s:textfield name="record.photoTime" placeholder="xxxx年xx月xx日" id="photoTime"/>
                </td>
            </tr>

            <tr>
                <td class="textR">地点：</td>
                <td colspan="3" class="textL"><s:textfield name="record.photoAddr"/></td>
            </tr>
            <tr>
                <td class="textR">图片来源：</td>
                <td colspan="3" class="textL"><s:textfield name="record.photoSource"/></td>
            </tr>
            <tr>
                <td class="textR" valign="top">描述：</td>
                <td colspan="3" class="textL">
                    <s:textarea height="80px" name="record.photoDesc"/>
                </td>
            </tr>
        </table>
    </div>
</s:form>

<div id="btnDiv" class="btnDiv">
    <gl:button name="btnAdd" onClick="goSubmit()">保存</gl:button>&nbsp;
    <gl:button name="btnAdd" onClick="goCancel()">关闭</gl:button>
</div>

<%@include file="/common/resize.jsp" %>
<%@include file="/common/dialog.jsp" %>

</body>
</html>