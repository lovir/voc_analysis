<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
<title>서울교통공사 통합VOC 분석시스템 : 사전 올리기</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script type="text/javascript">
$(function () {
	// 파일 검증
	$('#btnNext').click(function(){
		if($("#fileName").val() != ''){
			$("#uploadForm").attr('action', '<c:out value="${pageContext.request.contextPath}" />/management/uploadFile.do').submit();
		}else{
			alert('파일을 선택 해 주세요.');
		}
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
		<h1>사전 올리기(파일선택) : <c:out value='${dicTitle}' /></h1>
	</div>
	<!--// header end -->

	<!-- content start -->
	<form id="uploadForm" name="uploadForm" method="POST" enctype="multipart/form-data">
		<input name="dicType" type="hidden" value='<c:out value="${dicType}" />'>
		<input name="dicTitle" type="hidden" value='<c:out value="${dicTitle}" />'>
		<div class="pop_contarea" id="pop_contarea">
			<fieldset><legend><c:out value='${dicTitle}' /> 파일 선택</legend>
				<p class="pop_dic_tit"><c:out value='${dicTitle}' /> 파일 선택</p>
				<ul class="pop_dic">
					<li class="pop_file_li"><label for="dic_file">파일선택</label><input type="file" id="fileName" name="fileName" class="input_file"/></li>
					<li>파일의 Encoding은 확장자명을 따릅니다.</li>
				</ul>
				<div class="align_c mt_20">
					<a href="javascript:window.close();" class="btn b_gray medium">취소</a>
					<a href="#" class="btn b_orange medium" id="btnNext">다음</a>
				</div>
			</fieldset>
		</div>
	</form>
	<!--// content end -->

</div>
</body>
</html>