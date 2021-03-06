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
		<title>서울교통공사 : 역별 현황분석</title>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
		<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/datepicker_kr.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/modal.basic.js'/>"></script>
		<script src="<c:url value='/resources/js/highcharts.js'/>"></script>
		<script src="<c:url value='/resources/js/highcharts-3d.js'/>"></script>
		<script src="<c:url value='/resources/js/highcharts-more.js'/>"></script>
		<script src="<c:url value='/resources/js/modules/exporting.js'/>"></script>
		<script src="<c:url value='/resources/js/modules/no-data-to-display.js'/>"></script>
		<script src="<c:url value='/resources/js/attrchange.js'/>"></script>	<!-- iFrame 연동 관련 추가 라이브러리 -->
		<!-- header start -->
		<script type="text/javascript" src="<c:url value='/resources/js/header.js'/>"></script>
		<!--// header end -->
		<script type="text/javascript" src="<c:url value='/resources/js/voc/stationStatus.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
	</head>	
	<body>	
		<div id="skipnavi">
			<ul>
			<li><a href="#content">주메뉴 바로가기</a></li>
			<li><a href="#cont_body">본문 바로가기</a></li>
			</ul>
		</div>
		<hr />
		<div id="wrapper">
			
			<hr />
			<!-- content start -->
			<div id="content">
		
				<div class="cont">
					<!-- body resource start -->
					
					<!-- 초기화면 분석조건 로드 -->
					<script type="text/javascript">
					$(document).ready(function(){
						
						var condition = "<c:out value="${param.dashCondition}" />";
						
						if(condition != ""){
							dashInputVal(condition);	// 넘어온 값을 대시보드에 넣는다
						}
					});
					function getContextPath() {
						return "<c:out value="${pageContext.request.contextPath}" />";
					}
					$(function () {
						$( window ).load(function() {
							//$('#statusCont').hide();
							//$('#search_result').hide();
							$.ajax({
								type : "POST", 
								url : getContextPath() + "/common/selectOptionList.do",
								dataType: 'json', 
								contentType: 'application/json;charset=utf-8',
								mimeType: 'application/json',
								success : function(response) {
									$('#vocChannelList').html(selectList(response.vocChannelList));	//민원 접수 채널
									$('#vocRecTypeList').html(selectList(response.vocRecTypeList));	//민원 접수 구분
									$('#vocKindList').html(selectList(response.vocKindList));	//대 카테고리(민원 유형)
									$('#vocPartList').html(selectList(response.vocPartList));	//중 카테고리(민원 분야)
									$('#vocItemList').html(selectList(response.vocItemList));	//소 카테도리(민원 세부항목)
									//$('#vocMetroDeptList').html(selectList(response.vocMetroDeptList));	//처리 주무부서
									$('#vocMetroDeptList').html("<option value=\"all\">전체</option>");
									$('#vocChannelList > select').find('option:first').attr('selected',true);
									$('#vocRecTypeList > select').find('option:first').attr('selected',true);
									$('#vocKindList > select').find('option:first').attr('selected',true);
									$('#vocPartList > select').find('option:first').attr('selected',true);
									$('#vocItemList > select').find('option:first').attr('selected',true);
									// 초기화면 - 대분류 / 중분류 / 소분류 비활성 
									$("#vocKindList").attr("disabled", true);
									$("#vocPartList").attr("disabled", true);
									$("#vocItemList").attr("disabled", true);
								}
							}); 
						});
						
					});
					
					</script>
					<!-- body resource end -->
					
		
					<!-- right area start -->
					<div class="right_area">
						
		
						<!-- 본문 start -->
						<div class="cont_body" id="cont_body">
						
							<form id="searchForm" name="searchForm" method="post">	 
								<input type="hidden" name="pageType" value="stationStatus"/> 
								<input type="hidden" name="pageSize" value="12"/> 
								<input type="hidden" name="currentPage" value="1"/> 
								<input type="hidden" name="totalSize" value="1"/> 
								<input type="hidden" name="stationPageSize" value="12"/> 
								<input type="hidden" name="stationCurrentPageNo" value="1"/> 
								<input type="hidden" name="stationEndPage" value="12"/> 
								<input type="hidden" name="stationTotalSize" value="12"/> 
								<input type="hidden" name="stationName" value="all"/> 
								<input type="hidden" name="condition" value="DAY"/> 	<!-- 조회구분 -->
								<input type="hidden" name="vocChannel" value="all"/> 		<!-- 선호채널 -->
								<input type="hidden" name="vocRecType" value="all"/> 		<!-- VOC종류 -->
								<input type="hidden" name="metroDept" value="all"/>		<!-- 처리부서 -->
								<input type="hidden" name="repLevel" value="all"/>		<!-- 만족도 -->
								<input type="hidden" name="vocKind" value="all"/>		<!-- 대분류 -->
								<input type="hidden" name="vocPart" value="all"/>		<!-- 중분류 -->
								<input type="hidden" name="vocItem" value="all"/>		<!-- 소분류 -->
								<input type="hidden" name="line" value="all"/>			<!-- 지하철 노선 -->
								<input type="hidden" name="keyword" value="all"/>		<!-- 선택한 검색키워드 -->
								<input type="hidden" name="searchType" value="all"/>	<!-- 검색 타입 -->
								
								
							<div class="cont_head">
								<span class="cont_tit">역별 현황분석</span><span class="cont_desc">역별 민원 분석 결과를 제공합니다.</span>
							</div>
		
		
							<!-- 분석조건 start -->
							<div class="opt_box">
								<div class="no_head">
									<p class="nowin_tit">분석조건</p>
									<fieldset><legend>분석조건</legend>
									<div class="box_blue">
		
										<ul class="terms_list">
											<li>
												<span><label for="calendar_select">조회구분</label></span>
												<select id="calendar_select" title="조회구분 선택">
													<option value="DAY" >일별</option>
													<option value="WEEK" >주별</option>
													<option value="MONTH" >월별</option>
													<option value="QUARTER" >분기별</option>
													<option value="HALF" >반기별</option>
													<option value="YEAR" >년별</option>
												</select>
											</li>
											<li style="width:270px;">
												<span><label for="select02">분석기간</label></span>
												
												<div>
													<input type="text" class="w100px date_time gray" title="시작날짜 입력" id="startDate" name="startDate" readonly /> ~
													<input type="text" class="w100px date_time gray" title="마지막날짜 입력" id="endDate" name="endDate" readonly />
												</div>
											</li>
											
											
											<li>
												<span><label for="vocChannelList">접수채널</label></span>
												
												<select id="vocChannelList"></select>
											</li>
											
											<li>
												<span><label for="vocRecTypeList">VOC 종류</label></span>
												
												<select id="vocRecTypeList"></select>
											</li>
											
											<li>
												<span><label for="cdDeptList">처리주무부서</label></span>										
												<select id="vocMetroDeptList" title="처리주무부서">
												</select>									
											</li>
											
											<li class="mt_10">
												<span><label for="repLevelList">만족도 등급</label></span>
												<select id="repLevelList" title="만족도 등급">
													<option value="all" selected>전체</option>
													<option value="1" >매우만족</option>
													<option value="2" >만족</option>
													<option value="3" >보통</option>
													<option value="4" >불만족</option>
													<option value="5" >매우불만족</option>
												</select>
											</li>
											
											<li class="w100pp mt_10">
												<span><label for="vocKindList">접수종류</label></span>
												<select id="vocKindList" ></select>
												<select id="vocPartList" ></select>
												<select id="vocItemList" ></select>
												
												<!-- <span id="vocKindList" ></span>
												<span id="vocPartList" ></span>
												<span id="vocItemList" ></span> -->
											</li>
										</ul>
									</div>
		
									<div class="align_r mt_10">
										<a href="#" class="btn b_blue2 medium" id="searchStart">확인</a>
											<a href="#" class="btn b_gray medium" id="searchReset">초기화</a>
									</div>
									</fieldset>
		
								</div>
							</div>
							<!--// 분석조건 end -->
							</form>
							
		
		
							<!--// 분석조건 end -->
							<div id = "statusCont" class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">역별 현황</span>
		
									<ul class="win_btnset">
									<li style="background: none; padding: 0px 10px;">
										<select title="호선선택" class="mt_5" id="select_line">
													<option selected="selected" value="all">전체</option>
													<option value="18">1~8호선</option>
													<option value="1">1호선</option>
													<option value="2">2호선</option>
													<option value="3">3호선</option>
													<option value="4">4호선</option>
													<option value="5">5호선</option>
													<option value="6">6호선</option>
													<option value="7">7호선</option>
													<option value="8">8호선</option>
													
										</select></li>
								   	<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
								<div class="win_contarea align_c">
										
										<div class="graph_in align_l">
											 <!--노선별 긍부정-->
												<div id="statusArea" class="line_boxw p_15 etc">
												</div>
										</div>
										<div id = "statusPaging">
										
										</div>
								</div>
							
							</div>
							<!--// 역별 현황 end -->
							
							
							
							
							<!--역별 현황 상세보기 모달-->
							
							<div class="modal_popup doc_view" id="basic-modal-doc_03">
								<div class="win win_full" style="margin-top:0">
									<div class="win_head clear2">
										<span id="rank_name" class="win_tit bg_none"></span>
									</div>
									
									
													<div class="win_contarea p_20">
														<div class="rank_keyword">
														<div class="rank_top"></div>
															<div class="rank_list">
															   
															</div>
														</div>
		
													<div class="chart_w01">긍/부정 비율 챠트 삽입 width:100% * height:400px</div>
		
									</div>
																					
								</div>
							</div>
							<hr />
							<!--// 역별 현황 상세보기 모달-->
							
								<!-- VOC 검색결과 영역 -->				  		
								<!-- VOC 검색결과 start -->
								<div class="win win_full" id="search_result">
									<div class="win_head clear2">
										<span class="win_tit">검색결과</span>
										<ul class="win_btnset">
										<li><a href="javascript:;" class="btn_xls" title="액셀파일 내려받기"></a></li>
										<li><a href="javascript:;" class="btn_sh" title="접기"></a></li>
										</ul>
									</div>
									<div id="vocSearchTeam" class="win_contarea align_c">
										<div class="graph_in">해당 검색정보가 없습니다.</div>
									</div>
								</div>
								<!--// VOC 검색결과 end -->
							
						</div>
						<!--// 본문 end -->
						<!-- 상세페이지 팝업레이어 -->
						<div class="modal_popup result_view" id="basic-modal-detail" >
						</div>
						<!-- 유사문서 팝업레이어 -->
						<div class="modal_popup doc_view" id="basic-modal-alike" >
						</div>
					</div>
					<!--// right area end -->
				</div>
				<!--// cont end -->
			</div>
			<!--// content end -->
		</div>
	</body>
</html>
					