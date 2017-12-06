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
		<title>서울교통공사 : Open API 관리</title>
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
		<script type="text/javascript" src="<c:url value='/resources/js/voc/openApi.js'/>"></script>
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
					<!-- cont start -->
					<div class="cont">
						<!-- right area start -->
						<div class="right_area">
							<!-- 본문 start -->
							<div class="cont_body" id="cont_body">
								<div class="cont_head">
									<span class="cont_tit">Open API 관리</span><span class="cont_desc">Open API를 관리하는 기능을 제공합니다.</span>
								</div>
								<!-- main tab start -->
								<div class="main_tab clear2">
									<ul>
									<li class="on"><a href="#">Open API  제공</a></li>
									<li><a href="<c:out value='${pageContext.request.contextPath}' />/openApi/externalOpenApiInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />">외부 Open API</a></li>
									</ul>
								</div>
								<!--// main tab end -->
								
								<!-- 현재 제공되고 있는 Open API 목록 start -->
								<div class="win win_full">
									<div class="win_head clear2">
										<span class="win_tit bg_none">현재 제공되고 있는 Open API</span>
			
										<ul class="win_btnset">
										<li><a href="#" class="btn_sh" title="접기"></a></li>
										</ul>
									</div>
									
									<!-- win_contarea align_c start -->
									<div class="win_contarea align_c">
										<!-- 목록 start -->
										<div class="p_20">
											<fieldset>
												<legend>현재 제공되고 있는 Open API 목록</legend>
												<table class="tbl_type04 mt_5" summary="현재 제공되고 있는 Open API 목록">
													<caption>Open API 목록</caption>
													<colgroup>
														<col style="width:3%;" />
														<col/>
														<col />
														<col style="width:15%;" />
													</colgroup>
													<thead>
														<tr>
															<th scope="col">No.</th>
															<th scope="col">Open API 목록</th>
															<th scope="col">누적 로그</th>
															<th scope="col">등록일</th>
														</tr>
													</thead>
													<tbody>
														<tr>
															<td>1</td>
															<td class="align_l"><a href="#" class="t_blue modal_btn" name="channel_01" >자동 분류 추천 Open API</a></td>
															<td><c:out value="${totalAcmSize }"/></td>
															<td>2017-11-08</td>
														</tr>
													</tbody>
												</table>
											</fieldset>
											
											<!-- 목록정보 start -->
											<%-- <div class="list_info">
												<span class="float_l">총 <strong id="userCount"><c:out value="${totalAcmSize }"/></strong> 건</span><span class="float_r" id="userCurrent"></span>
											</div> --%>
											<!--// 목록정보 end -->
											<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
											<!-- <div class="paging" id="paging">
											</div> -->
											<!--// 페이징 end -->
										</div>
										<!--//목록 end -->
									</div>
									<!--//win_contarea align_c end -->
								</div>
								<!--//현재 제공되고 있는 Open API 목록 end -->
								
								<!-- 모달팝업 : 채널별 수집현황 보기 start -->
								<div class="modal_popup channel_view" id="basic-modal-channel_01">
									<form name="openApiModalForm" id="openApiModalForm" method="post">
										<input type="hidden" id="pageSize" name="pageSize" value="10"/> 
										<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>
										<input type="hidden" id="totalSize" name="totalSize" value="1"/> 
									</form>
									<div class="win win_full" style="margin-top:0">
										<div class="win_head clear2">
											<span class="win_tit bg_none" id="modal_title">내부 데이터 외부 제공 Open API 일별 현황 보기</span>
										</div>
										
										<div class="win_contarea clear2 pt_10" >
											<p id="currentTime" class="t_blue01_r"></p>
											<table class="tbl_type05" id="list_contents" summary="채널별 누적 수집건수 목록">
												<thead>
													<tr>
													<th>날짜</th>
													<th>누적 로그</th>
													<th>당일 로그</th>
													</tr>
												</thead>
												<tbody >
												</tbody>
											</table>
											<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
											<div id="paging_modal">
											</div>
											<!--// 페이징 end -->
											<div class="align_c p_10">
												<a href="#" class="btn b_gray medium simplemodal-close">닫기</a>
											</div> 
										</div>
									</div>
								</div>
								<hr />
								<!--// 모달팝업 : 채널별 수집현황 보기 end -->
							</div>
							<!-- 본문 end -->
						</div>
						<!--//right area end -->
					</div>
					<!--//cont end -->
				</div>
				<!--//content end -->
		</div>
		<!--//wrapper end -->
	</body>
</html>

