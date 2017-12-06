<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<title>서울교통공사 통합VOC 분석시스템 : 사전 내려받기</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script type="text/javascript">
$(function () {
	
	// 다운로드 버튼 클릭
	$('#btnDown').click(function(){
		$("#downloadForm").attr('action', '<c:out value="${pageContext.request.contextPath}" />/management/downloadFile.do').submit();
		return false;
	});
	
	// 확인(삭제) 버튼 클릭
	$('#btnDel').click(function(){
		$.ajax({
			type : "POST", 
			url : '<c:out value="${pageContext.request.contextPath}" />/management/delFile.do',
			data: $("#downloadForm").serialize(),
			success : function(response) {
				alert("임시 파일이 삭제 되었습니다.");
				window.close();
			}
		});
		return false;
	});
});

</script>
</head>
<body>

<div id="skipnavi">
	<ul>
	<li><a href="#pop_contarea">본문 바로가기</a></li>
	</ul>
</div>
<hr />
<div id="wrapper">

	<!-- header start -->
	<div class="pop_header">
		<h1>사전 내려받기 : <c:out value='${dicTitle}' /></h1>
	</div>
	<!--// header end -->

	<!-- content start -->
	<form id="downloadForm"  name="downloadForm" method="POST">
		<input name="filename" type="hidden" value="<c:out value="${downloadResult.fileName}" escapeXml="false"/>">
		<input name="filepath" type="hidden" value="<c:out value="${downloadResult.filePath}" escapeXml="false"/>">
		<div class="pop_contarea" id="pop_contarea">
			<p class="pop_dic_tit"><c:out value='${dicTitle}' /> 내려받기</p>
			<ul class="pop_dic">
				<li><c:out value='${dicTitle}' /> : 총${downloadResult.counts}건</li>
			</ul>
			<div class="box_blue align_c">
				<a href="#" class="btn b_blue small mb_5" id="btnDown"><c:out value="${downloadResult.fileName}" escapeXml="false"/></a><br />
				<div class="align_l mt_5">
					저장 완료 후 반드시 아래 확인 버튼을 클릭해 주십시오.<br />
					(클릭하지 않을 경우 관리도구에 불필요한 파일이 남을 수 있습니다.)</div>
			</div>
			<div class="align_c mt_20">
				<a href="javascript:window.close();" class="btn b_gray medium">취소</a>
				<a href="#" class="btn b_orange medium" id="btnDel">확인</a>
			</div>
		</div>
	</form>
	<!--// content end -->

</div>
</body>
</html>