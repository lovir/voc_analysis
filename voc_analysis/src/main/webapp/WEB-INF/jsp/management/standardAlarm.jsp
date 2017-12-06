<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
		<title>서울교통공사 : 종합 키워드 분석</title>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
		<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/datepicker_kr.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/modal.basic.js'/>"></script>
		<script src="<c:url value='/resources/js/attrchange.js'/>"></script>	<!-- iFrame 연동 관련 추가 라이브러리 -->
		<!-- header start -->
		<script type="text/javascript" src="<c:url value='/resources/js/header.js'/>"></script>
		<!--// header end -->
		<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/standardAlarm.js'/>"></script>
		<script type="text/javascript">
		function getContextPath() {
			return "<c:out value="${pageContext.request.contextPath}" />";
		}
		
		function getImgPath(){
			return "<c:url value='/resources/images/common/'/>";
		}
		</script>
	</head>
	<body>
		<div id="skipnavi">
			<ul>
			<li><a href="#content">주메뉴 바로가기</a></li>
			<li><a href="#cont_body">본문 바로가기</a></li>
			</ul>
		</div>
		<hr />
		<!-- wrapper start -->
		<div id="wrapper">
			<hr />
			<!-- content start -->
			<div id="content">
				<div class="cont">
					<!-- right area start -->
					<div class="right_area">
						<!-- 본문 start -->
						<div class="cont_body" id="cont_body">
							<div class="cont_head">
								<span class="cont_tit">키워드 관리</span><span class="cont_desc">관심 키워드 및 알람 키워드에 대한 설정 기능을 제공합니다.</span>
							</div>
							<!-- main tab start -->
							<div class="main_tab clear2">
								<ul>
								<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/interestKeywordInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />">관심키워드 관리</a></li>
								<li class="on"><a href="#">알람키워드 관리</a></li>
								</ul>
							</div>
							<!--// main tab end -->
							<!-- sub tab start -->
							<div class="tab clear2">
								<ul class="tab_list">
									<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/alarmKeywordInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />">알람키워드 관리</a></li>
									<li><a href="#" class="on">발생기준 관리</a></li>
									<%-- <li><a href="<c:out value='${pageContext.request.contextPath}' />/management/mailReceiverInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />">메일수신자 관리</a></li> --%>
								</ul>
							</div>
							<!--// sub tab end -->
							
							<!-- 발생기준 관리 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">발생기준 관리</span>
									<ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								<div class="win_contarea">
									<!-- 폼 start -->
									<div class="p_20">
										<fieldset>
											<legend>발생기준 관리 양식</legend>
											<table class="tbl_type03" summary="기간, 제외날짜, 발셍레벨 등으로 구성된 발생기준 관리">
											<caption>발생기준 관리 양식</caption>
											<colgroup>
												<col style="width:16%;" />
												<col />
											</colgroup>
											<tbody>
											<input type="hidden" name="no" id="no" />
												<tr>
													<th scope="row"><label for="form_02">발생레벨 - 상</label></th>
													<td>전일대비 발생 건수가<input type="text" name="level1" id="level1" class="gray w40px align_r"" title="발생건수 입력" id="form_02" />% 이상 증가한 경우</td>
												</tr>
												<tr>
													<th scope="row"><label for="form_03">발생레벨 - 중</label></th>
													<td>전일대비 발생 건수가<input type="text" name="level2" id="level2" class="gray w40px align_r"" title="발생건수 입력" id="form_03" />% 이상 증가한 경우</td>
												</tr>
												<tr>
													<th scope="row"><label for="form_04">발생레벨 - 하</label></th>
													<td>전일대비 발생 건수가<input type="text" name="level3" id="level3" class="gray w40px align_r"" title="발생건수 입력" id="form_04" />% 이상 증가한 경우</td>
												</tr>
												<tr>
													<th scope="row"><label for="form_05">수신 메일 주소 입력</label></th>
													<td><input name="email" id="email" class="gray w200px" title="메일 주소 입력" /> (메일 주소 입력란을 모두 지운 후 저장 시 삭제 됩니다.)</td>
												</tr>
											</tbody>
											</table>
											
											<div class="align_r mt_10">
												<a href="#" id="btnSave" class="btn b_blue2 medium">저장</a>
											</div>
										</fieldset>
									</div>
									<!--// 폼 end -->
								</div>
							</div>
							<!--// 발생기준 관리 end -->
						</div>
						<!--// 본문 end -->
					</div>
					<!--// right area end -->		
				</div>
			</div>
			<!--// content end -->
		</div>
		<!--// wrapper end -->
	</body>
</html>
