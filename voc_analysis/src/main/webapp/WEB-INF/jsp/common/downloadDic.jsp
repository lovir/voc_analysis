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
	// 확장자 선택
	$('#dic_file').change(function(){
		var select = $('#dic_file option:selected').val();
		if("select" == select){
			$('.input_filename').val('');
			$('.input_filename').attr('readonly', true);
		}else{
			if(select.length>0){
				$('.input_filename').val(select);
				$('.input_filename').attr('readonly', true);
			}else{
				$('.input_filename').val('');
				$('.input_filename').attr('readonly', false);
			} 
		}
	 });
	
	// 다음 버튼 클릭
	$('#btnNext').click(function(){	
		if($('.input_filename').val() != ''){
			$("#downloadForm").attr('action', '<c:out value="${pageContext.request.contextPath}" />/management/downloadDictionary.do').submit();
		}else{
			alert('파일의 확장자를 선택해 주세요.');
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
		<h1>사전 내려받기 : <c:out value='${dicTitle}' /></h1>
	</div>
	<!--// header end -->

	<!-- content start -->
	<form id="downloadForm" name="downloadForm" method="POST" >
		<input name="dicType" type="hidden" value='<c:out value="${dicType}" />'>
		<input name="dicTitle" type="hidden" value='<c:out value="${dicTitle}" />'>
		<div class="pop_contarea" id="pop_contarea">
			<fieldset><legend><c:out value='${dicTitle}' /> 내려받기</legend>
				<p class="pop_dic_tit"><c:out value='${dicTitle}' /> 내려받기</p>
				<ul class="pop_dic">
					<li>
						<label for="dic_file">Encoding 선택</label>
						<select id="dic_file" class="ml_5">
							<option value="select">선택하세요.</option>
							<option value="UTF-8">UTF-8</option>
							<option value="MS949">MS949</option>
							<option value="EUC-KR">EUC-KR</option>
							<option value="KSC5601">KSC5601</option>
							<option value="">직접입력</option>
							</select>
						<input type="text" name="encoding" class="input_filename white w200px" readonly/>
					</li>
					<li>파일의 확장자는 Encoding명으로 결정됩니다.</li>
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