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
<script type="text/javascript">

<c:if test="${not empty uploadFlg}" >
	alert("업로드가 완료 되었습니다.");
	window.close();
</c:if>

$(function () {
	// 업로드 버튼 클릭
	$('#btnUp').click(function(){
		$("#uploadForm").attr('action', '<c:out value="${pageContext.request.contextPath}" />/management/uploadDictionary.do').submit();
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
		<h1>사전 올리기(파일검증) : <c:out value='${dicTitle}' /></h1>
	</div>
	<!--// header end -->

	<!-- content start -->
	<form id="uploadForm"  name="uploadForm" method="POST">
		<input name="filepath" type="hidden" value="<c:out value='${tempFilePath}' />">
		<input name="dicType" type="hidden" value="<c:out value='${dicType}' />">
		<input name="filename" type="hidden" value="<c:out value='${fileName}' />">
		<input name="encoding" type="hidden" value="<c:out value='${encoding}' />">
		<div class="pop_contarea" id="pop_contarea">
			<p class="pop_dic_tit"><c:out value='${dicTitle}' /> 파일 검증 결과</p>
			<ul class="pop_dic">
				<li><span class="pop_dic_left">파일명</span>: <c:out value='${fileName}' /></li>
				<li><span class="pop_dic_left">전체</span>: <c:out value='${uploadResult.total}' />개</li>
				<li><span class="pop_dic_left">정상</span>: <c:out value='${uploadResult.normal}' />개</li>
				<li><span class="pop_dic_left">실패</span>: <c:out value='${uploadResult.errors + uploadResult.duplications}' />건
					<span class="pop_dic_sub">(중복키워드: <c:out value='${uploadResult.errors}' />건 / 형식 오류: <c:out value='${uploadResult.duplications}' />건)</span>
					<div class="pop_dic_desc">* 중복키워드는 먼저 출현한 것이 적용됩니다.</div>
				</li>
			</ul>
			<div class="align_c mt_20">
				<a href="javascript:window.close();" class="btn b_gray medium">취소</a>
				<a href="#" class="btn b_orange medium" id="btnUp">확인</a>
			</div>
		</div>
	</form>
	<!--// content end -->

</div>
</body>
</html>