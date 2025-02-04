<%@page language="java" pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tag/gl-tag.tld" prefix="gl" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@include file="/common/header.jsp" %>
    <script language="javascript">
        var contextpath = "<%=CONTEXT_PATH%>";

        var fulllink = contextpath + "artist/ArtistInformation.do";
        var experienceLink = contextpath + "artist/ArtistExperience.do";
        var albumLink = contextpath + "artist/ArtistAlbum.do";
        var eduLink = contextpath + "artist/ArtistEdu.do";
        var honorsLink = contextpath + "artist/ArtistHonors.do";
        var collectAgencyLink = contextpath + "artist/ArtistCollectAgency.do";
        var collectLink = contextpath + "artist/ArtistCollector.do";
        var coopLink = contextpath + "artist/ArtistCoop.do";
        var donationLink = contextpath + "artist/ArtistDonation.do";
        var seriesLink = contextpath + "artist/ArtistWorksSeries.do";
        var yearLink = contextpath + "artist/ArtistWorksYear.do";

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
            if (!checkString(getElement('form.record.cName'), "中文名", 30, true)) return;
            if (!checkString(getElement('form.record.eName'), "英文名", 30, true)) return;
            if (!checkString(getElement('form.record.sex'), "性别", 30, true)) return;
            //if(!checkString(getElement('form.record.nhom'), "现居住工作", 30, true)) return;
            if (!checkNumeric(getElement('form.record.birthYear'), "生(年)", 4, true, false)) return;
            if (!checkNumeric(getElement('form.record.birthMonth'), "生(月)", 2, true, false)) return;
            if (!checkNumeric(getElement('form.record.birthDay'), "生(日)", 2, true, false)) return;
            if (!checkNumeric(getElement('form.record.deathYear'), "卒(年)", 4, true, false)) return;
            if (!checkNumeric(getElement('form.record.deathMonth'), "卒(月)", 2, true, false)) return;
            if (!checkNumeric(getElement('form.record.deathDay'), "卒(日)", 2, true, false)) return;
            return true;
        }

        function checkFile() {
            var isPass = true;
            $(".checkFile").each(function () {
                var format = this.value.split(".");
                var length = this.value.length;
                var i = format.length;
                if (length != 0) {
                    if (!checkString(this, "相片", 60, true)) {
                        isPass = false;
                        return false;
                    }
                    if (format[i - 1] != "jpg" && format[i - 1] != "jpeg" && format[i - 1] != "gif" && format[i - 1] != "png") {
                        alert("只能上传图片格式！");
                        isPass = false;
                        return false;
                    }
                }
            });
            return isPass;
        }

        function closedialog(ret) {
            var res = ret['ret'];
            var birthCountry = ret['birthCountry'];
            var country = ret['country'];
            var place = ret['place'];
            if (ret == 'true') {
                goSearch();
            }
            if (place == true) {
                if (ret['name'] == "BIRTHPLACE") {
                    getElement('form.record.birthplaceCode').value = ret['areaCode'];
                    getElement('form.record.birthplaceName').value = ret['areaName'];
                }
                if (ret['name'] == "ANCESTRALHOME") {
                    getElement('form.record.ancestralHomeCode').value = ret['areaCode'];
                    getElement('form.record.ancestralHomeName').value = ret['areaName'];
                }
                if (ret['name'] == "NHOM") {
                    getElement('form.record.nhomCode').value = ret['areaCode'];
                    getElement('form.record.nhomName').value = ret['areaName'];
                }

            }
            if (birthCountry == true) {
                getElement('record.birthCountry').value = ret['str'];
                getElement('record.birthCountryName').value = ret['str1'];
            }
            if (country == true) {
                getElement('record.nationality').value = ret['str'];
                getElement('record.nationalityName').value = ret['str1'];
            }
            if (editMode == "WORKS") {
                console.log(ret);
                getElement('record.worksId').value = ret['str1'];
                getElement('record.worksPhoto').value = ret['str2'];
            }
            editMode = "";
        }

        function init() {
            document.getElementById("cancel").style.display = "none";
            document.getElementById("worksChang").style.display = "none";
            document.getElementById("cancelWorks").style.display = "none";
            document.getElementById("deleteTag").style.display = "none";
            showMessage('<s:property value="errorMessage" escape="false"/>');
            calendar1 = initCalendar("birthdate", "imgBirthdate");
            hidebtn();

        }

        function goChange(re) {
            if (re == "1") {
                document.getElementById("file").style.display = "";
                document.getElementById("cancel").style.display = "";
                document.getElementById("deleteTag").style.display = "";
                document.getElementById("change").style.display = "none";
                $(".fileName").each(function () {
                    this.style.display = "none";
                });
            }
            if (re == "2") {
                document.getElementById("file").style.display = "none";
                document.getElementById("cancel").style.display = "none";
                document.getElementById("deleteTag").style.display = "none";
                document.getElementById("change").style.display = "";
                $(".fileName").each(function () {
                    this.style.display = "";
                });
            }
        }

        function removePhotoTag() {
            $('#photoTable').remove();
        }

        function hidebtn() {
            $(".dhxtabbar_tab_text").each(function () {
                this.addEventListener("click", function () {
                    if (this.innerHTML != "基本信息") {
                        document.getElementById("btnDiv").style.display = "none";
                    } else {
                        document.getElementById("btnDiv").style.display = "";
                    }
                });
            });
        }

        function goArtCountry(type) {
            var ids;
            var type;
            if (type == '1') {
                ids = $$("birthCountry").value;
                type = "1";
            }
            if (type == '2') {
                ids = $$("nationality").value;
                type = "2";
            }
            var url_link = contextpath + 'system/artCountry.do?ids=' + ids + "&record.type=" + type + "&query.parameters.type=1";
            openWindow("选择国家", url_link, 0.8, 0.8);
        }

        function goWorksPhoto() {
            var worksPhoto = document.getElementById("worksPhoto");
            if (worksPhoto.style.display == "none") worksPhoto.style.display = "";
            var url_link = contextpath + 'works/artWorks.do?query.parameters.type=2&query.parameters.artistId=' + $$('artistId').value;
            openWindow("选择作品", url_link, 0.8, 0.8);
            editMode = "WORKS";
        }

        function delArtCountry(id, name) {
            document.getElementById(id).value = "";
            document.getElementById(name).value = "";
        }

        function cancelWorks(id, name) {
            delArtCountry(id, name);
            getElement('record.worksPhoto').style.idsplay = "none";
        }

        function changeWorks(type) {
            if (type == '1') {
                document.getElementById("worksChang").style.display = "";
                document.getElementById("cancelWorks").style.display = "";
                document.getElementById("changeWorks").style.display = "none";
            }
            if (type == '2') {
                document.getElementById("worksChang").style.display = "none";
                document.getElementById("cancelWorks").style.display = "none";
                document.getElementById("changeWorks").style.display = "";
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
        #imgBox img{
            max-width: 335px;
            max-height: 335px;
            width: auto;
            height: auto;
        }
    </style>
</head>

<body onload="init()">
<s:form action="ArtistInformation" id="ArtistInformationForm" method="post" namespace="/artist"
        enctype="multipart/form-data">
    <s:hidden name="record.id" id="artistId"/>
    <s:textfield cssStyle="display:none"></s:textfield>
    <div id="tblForm"></div>
    <div id="page_0" style="padding:2px; height:100%; width:100%;">
        <div id="formDiv" class="formDiv" style="padding:2px;">
            <table cellpadding="0" cellspacing="0" style="width: 99%;" class="formTable">
                <tr>
                    <td id="imgBox" width="30%" rowspan="9" colspan="3" class="textC">
                        <a href="<%=CONTEXT_PATH%>upload/photo/<s:property value='record.photoPath'/>/<s:property value='record.realName'/>"
                           target="_blank">
                            <img src="<%=CONTEXT_PATH%>upload/photo/<s:property value='record.photoPath'/>/<s:property value='record.realName'/>"/></a>
                    </td>
                    <td class="textR" width="10%"><font color="red">*</font>中文名：</td>
                    <td class="textL" width="20%"><s:textfield name="form.record.cName"/></td>
                    <td class="textR" width="10%">祖籍：</td>
                    <td class="textL" width="20%">
                        <s:textfield name="form.record.ancestralHome"/>
                    </td>
                </tr>
                <tr>
                    <td class="textR"><font color="red">*</font>英文名：</td>
                    <td class="textL"><s:textfield name="form.record.eName"/></td>
                    <td class="textR">国籍：</td>
                    <td class="textL">
                        <table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
                            <tr>
                                <td>
                                    <s:textfield readonly="true" name="record.nationalityName" id="nationalityName"/>
                                    <s:hidden name="record.nationality" id="nationality"/>
                                </td>
                                <td width="45px">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle"
                                               onClick="goArtCountry('2')">选择</gl:button>
                                </td>
                                <td width="45px">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle"
                                               onClick="delArtCountry('nationality','nationalityName')">清空</gl:button>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="textR" width="15%"><font color="red">*</font>性别：</td>
                    <td><s:select list="codeSets.GENDER" listKey="value" listValue="codeName"
                                  name="form.record.sex"/></td>
                    <td class="textR">星座：</td>
                    <td><s:select list="codeSets.ZODIAC" listKey="value" listValue="codeName"
                                  name="form.record.zodiac"/></td>
                </tr>
                <tr>
                    <td class="textR" width="15%">生日期：</td>
                    <td class="textL">
                        <s:textfield name="form.record.birthYear" cssStyle="width:80px;" placeholder="xxxx"/>年
                        <s:textfield name="form.record.birthMonth" cssStyle="width:50px;" placeholder="xx"/>月
                        <s:textfield name="form.record.birthDay" cssStyle="width:50px;" placeholder="xx"/>日
                    </td>
                    <td class="textR">现居住工作地：</td>
                    <td class="textL">
                        <s:textfield name="form.record.nhom"/>
                    </td>
                </tr>
                <tr>
                    <td class="textR" width="15%">卒日期：</td>
                    <td class="textL">
                        <s:textfield name="form.record.deathYear" cssStyle="width:80px;" id="deathYear"
                                     placeholder="xxxx"/>年
                        <s:textfield name="form.record.deathMonth" cssStyle="width:50px;" placeholder="xx"/>月
                        <s:textfield name="form.record.deathDay" cssStyle="width:50px;" placeholder="xx"/>日
                    </td>
                    <td class="textR">艺术家时期类型：</td>
                    <td><s:select list="codeSets.ARTIST_TYPE" listKey="value" listValue="codeName"
                                  name="form.record.artistType"/></td>
                </tr>
                <tr>
                    <td class="textR">出生国家：</td>
                    <td class="textL">
                        <table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
                            <tr>
                                <td>
                                    <s:textfield readonly="true" name="record.birthCountryName" id="birthCountryName"/>
                                    <s:hidden name="record.birthCountry" id="birthCountry"/>
                                </td>
                                <td width="45px">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle"
                                               onClick="goArtCountry('1')">选择</gl:button>
                                </td>
                                <td width="45px">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle"
                                               onClick="delArtCountry('birthCountry','birthCountryName')">清空</gl:button>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="textR" width="15%">相片显示：</td>
                    <td class="textL" id="picphoto">
                        <table id="photoTable" width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
                            <tr>
                                <td class="textL">
                                    <s:iterator value="pic" id="p" status="pp">
                                        <a href="<%=CONTEXT_PATH%>upload/photo/<s:property value='record.photoPath'/>/${p.realName}"
                                           target="_blank">${p.name}</a>
                                    </s:iterator>
                                    <input type="hidden" id="tag" name="photoTag" value="photo"/>
                                    <input type="file" name="files" id="file" class="checkFile" style="display:none"/>
                                </td>
                                <td width="45px" class="textL">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" id="change"
                                               onClick="goChange('1')">更改</gl:button>
                                </td>
                                <td width="45px" class="textL">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" id="cancel"
                                               onClick="goChange('2')">取消</gl:button>
                                </td>
                                <td width="45px" class="textL">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" id="deleteTag"
                                               onClick="removePhotoTag()">删除</gl:button>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="textR">出生地点：</td>
                    <td><s:textfield name="form.record.birthplace"/></td>
                    <td class="textR">代表作：</td>
                    <td class="textL">
                        <table width="100%" cellpadding="0" cellspacing="0" class="tbnBtnStyle" border="0">
                            <tr>
                                <td width="*" class="textL">
                                    <a href="<%=CONTEXT_PATH%>upload/auction/<s:property value='record.photoPath'/>/<s:property value='record.worksPhoto'/>"
                                       target="_blank"><s:property value='record.worksPhoto'/></a>
                                </td>
                                <td width="90px" class="textL">
                                    <gl:button name="btnWorksPhoto" styleClass="btnFormStyle" id="changeWorks"
                                               onClick="changeWorks('1')">更改</gl:button>
                                </td>
                            </tr>
                            <tr id="worksChang">
                                <td width="*">
                                    <s:textfield name="record.worksPhoto" id="worksPhoto" readonly="true"/>
                                    <s:hidden name="record.worksId" id="worksPhotoId"/>
                                </td>
                                <td width="45px" class="textL">
                                    <gl:button name="btnWorksPhoto" styleClass="btnFormStyle" id="changeWorks"
                                               onClick="goWorksPhoto()">选择</gl:button>
                                </td>
                                <td width="45px" class="textL">
                                    <gl:button name="btnArtArtist" styleClass="btnFormStyle" id="cancelWorks"
                                               onClick="changeWorks('2')">取消</gl:button>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="textR">主要创作媒介：</td>
                    <td class="textL" colspan="3"><s:checkboxlist list="codeSets.MEDIUM_TYPE" listKey="value"
                                                                  listValue="codeName" name="strs"
                                                                  cssStyle="width:20px;"/></td>
                </tr>
                <tr>
                    <td class="textR">个人主页：</td>
                    <td colspan="3"><s:textarea name="form.record.personalPage" style="height:60px"
                                                placeholder="有多个可以以、分隔开"></s:textarea></td>
                </tr>
                <tr>
                    <td class="textR" valign="top">简介：</td>
                    <td colspan="6"><s:textarea name="form.record.cresume" style="height:200px"></s:textarea></td>
                </tr>
            </table>
        </div>
    </div>
    <iframe id="page_1" name="page_1" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_2" name="page_2" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_3" name="page_3" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_4" name="page_4" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_5" name="page_5" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_6" name="page_6" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_7" name="page_7" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_8" name="page_8" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>
    <iframe id="page_9" name="page_9" width="100%" height="100%" scrolling="no" frameborder="0"></iframe>

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
        fDiv.style.height = mainbody.offsetHeight - bDiv.offsetHeight - fDiv.offsetTop * 2 + "px";
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
    tabbar.addTab("a0", "基本信息", "100px");
    tabbar.addTab("a1", "教育与工作", "130px");
    tabbar.addTab("a2", "获奖与荣誉", "110px");
    tabbar.addTab("a3", "收藏机构", "100px");
    tabbar.addTab("a4", "收藏家", "100px");
    tabbar.addTab("a5", "捐赠", "80px");
    tabbar.addTab("a6", "合作机构", "100px");
    tabbar.addTab("a7", "人生年表", "100px");
    tabbar.addTab("a8", "作品年代", "100px");
    tabbar.addTab("a9", "作品系列", "100px");

    tabbar.tabs("a0").attachObject("page_0");
    tabbar.tabs("a0").setActive();
    tabbar.tabs("a1").attachObject("page_1");
    tabbar.tabs("a2").attachObject("page_2");
    tabbar.tabs("a3").attachObject("page_3");
    tabbar.tabs("a4").attachObject("page_4");
    tabbar.tabs("a5").attachObject("page_5");
    tabbar.tabs("a6").attachObject("page_6");
    tabbar.tabs("a7").attachObject("page_7");
    tabbar.tabs("a8").attachObject("page_8");
    tabbar.tabs("a9").attachObject("page_9");

    tabbar.enableAutoReSize(true);
    tabbar.enableTabCloseButton(true);
    tabbar.attachEvent("onTabClick", function (idClicked, idSelected) {
        if (idClicked != "a0") {
            document.getElementById("btnDiv").style.display = "none";
        } else {
            document.getElementById("btnDiv").style.display = "";
        }
    });
    window.frames["page_1"].location.href = eduLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_2"].location.href = honorsLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_3"].location.href = collectAgencyLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_4"].location.href = collectLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_5"].location.href = donationLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_6"].location.href = coopLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_7"].location.href = experienceLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_8"].location.href = yearLink + '?action=LIST&record.artistId=' + $$('artistId').value;
    window.frames["page_9"].location.href = seriesLink + '?action=LIST&record.artistId=' + $$('artistId').value;

</script>
</body>
</html>