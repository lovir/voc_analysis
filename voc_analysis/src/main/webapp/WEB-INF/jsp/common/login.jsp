<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<title>DiXSi 음성 VOC 분석 시스템 : 로그인</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
<style type="text/css">
<!--
body {background:#333438 url(images/common/bg.png)no-repeat right 0px;}


-->
</style>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script language="JavaScript" type="text/JavaScript">

$(function () {
	$('#userID').focus();
	
	// 로그인
	$('#password').keypress(function(e){
		if(e.which==13){
			fnLogin();
		}
	});
	<c:choose>
		<c:when test="${!empty sso && sso == 'fail_SSO'}">
			alert('SSO 인증 로그인에 실패하였습니다. 담당자에게 문의하세요.  '); 
		</c:when>
		<c:when test="${!empty sso && sso == 'fail_VOC'}">
			alert('해당 사용자가 VOC 내부고객으로 존재하지 않습니다. 담당자에게 문의하세요.  ');
		</c:when>
		<c:when test="${!empty adlogin && adlogin == 'fail_VOC'}">
			alert('해당 사용자가 VOC 내부고객으로 존재하지 않습니다. 담당자에게 문의하세요.  ');
		</c:when>
		<c:when test="${!empty adlogin && adlogin == 'fail_AD'}">
			alert('AD 인증 로그인에 실패하였습니다. 담당자에게 문의하세요.  ');
		</c:when>
	</c:choose>
});


function fnLogin(){
	
	if($('#userID').val().length<=0){
		alert("아이디를 입력 해 주세요");
		return;
	}
	
	if($('#password').val().length<=0){
		alert("패스워드를 입력 해 주세요");
		return;
	}
	
	$("#loginForm").submit();
	
}

</script>
</head>

<%-- <body style="background-color:#e9e9e9;">
<div id="skipnavi">
	<ul>
	<li><a href="#l_field">로그인 바로가기</a></li>
	</ul>
</div>

<div id="l_wrapper">

	<!-- login form start -->
	<div class="login_form">
	<form id="loginForm" name="loginForm" method="post" action="<c:out value="${pageContext.request.contextPath}" />/common/login.do">
		<div class="l_logo">
			<img src="<c:url value='/resources/images/login/img_logo.png'/>" onclick="fnLogin();">
		</div>

		<fieldset><legend>로그인</legend>
		<div class="l_field" id="l_field">
			<ul>
			<li><span><label for="form1">아이디</label></span><input type="text" id="userID" name="userID" class="i_txt" title="아이디 입력" /></li>
			<li><span><label for="form2">비밀번호</label></span><input type="password" id="password" name="password" class="i_txt" title="비밀번호 입력" /></li>
			</ul>

			<div>
				<ul>
				<li><input type="checkbox" id="form3" /><label for="form3">아이디 기억</label></li>
				<li class="li_02"><a href="#" class="btn b_orange medium shadow" onclick="fnLogin();">로그인</a></li>
				</ul>
			</div>

		</div>
		</fieldset>

		<div class="l_footer">Copyright © 2016. Incheon Metropolitan City. All rights reserve. </div>
	</form>
	</div>
	<!--// login form end -->
	
</div>

</body> --%>

<body>
	<div class="login_wrap">
    	<form id="loginForm" name="loginForm" method="post" action="<c:out value="${pageContext.request.contextPath}" />/common/login.do">
        <div class="login_box">
        <div class="logo2" title="DiQuest&Selvas"></div>
        <h1 class="login_logo" title="DiSXi 분석시스템"></h1>
        	<fieldset><legend>로그인</legend>
		<div class="l_field" id="l_field">
        	
			<ul class="login_field">
            <li><label for="id"><span class="id">Username/ID</span></label></li>
			<li><input type="text" id="userID" name="userID" class="gray2" title="아이디 입력" /></li>
            <li class="mt_10"><label for="password"><span class="pw">Password</span></label></li>
			<li><input type="password" id="password" name="password" class="gray2" title="비밀번호 입력" autocomplete="off" /></li>
            <li class="login_btn"><a href="#" class="login_btn" title="로그인"></a></li>
            <li><input type="checkbox" id="form3" /><span style="display:inline-block !important;">아이디 기억</span></li>
			</ul>
            
            <a href="#" class="btn_login" onclick="fnLogin();">Login</a>
            
           
		</div>
		</fieldset>
        </div>     
        </form>
    </div>
    
    <div class="copyright">
    	<ul>
        	<li class="t_l"><a href="<c:url value='/resources/images/diquest.pdf'/>" download>다이퀘스트 회사소개</a><span class="bar">|</span>
        	<a href="<c:url value='/resources/images/셀바스AI_회사소개서_2017_0124.pdf'/>" download>셀바스 AI 회사소개</a></li>
            <li class="t_r">© DiQuest Corp., SELVAS AI Corp.</li>
        </ul>
    </div>
    

</body>
</html>