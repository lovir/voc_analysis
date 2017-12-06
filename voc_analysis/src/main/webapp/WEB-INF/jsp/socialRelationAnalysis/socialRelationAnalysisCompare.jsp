<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
		<title>서울교통공사 : 연관도 비교 분석</title>
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
		<script type="text/javascript" src="<c:url value='/resources/js/d3.js'/>"></script>
		<script src="<c:url value='/resources/js/attrchange.js'/>"></script>	<!-- iFrame 연동 관련 추가 라이브러리 -->
		<!-- header start -->
		<script type="text/javascript" src="<c:url value='/resources/js/header.js'/>"></script>
		<!--// header end -->
		<script type="text/javascript" src="<c:url value='/resources/js/voc/socialRelationAnalysisCompare.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
		<!-- 초기화면 분석조건 로드 -->
		<script type="text/javascript">
			function getContextPath() {
				return "<c:out value="${pageContext.request.contextPath}" />";
			}
			$(function () {
				$( window ).load(function() {
					$.ajax({
						type : "POST", 
						url : getContextPath() + "/common/selectOptionListSocial.do",
						dataType: 'json', 
						contentType: 'application/json;charset=utf-8',
						mimeType: 'application/json',
						success : function(response) {				
							$('#socialChannelListA').html(selectListSocial(response.socialChannelList));	// 소셜 채널
							$('#socialChannelListB').html(selectListSocial(response.socialChannelList));	// 소셜 채널
						}
					}); 
					
				});
				
			});
		</script>
		<style type="text/css">
			.overlay { 
				fill : none !important;
				pointer-events : all !important; 
			}
				
			.node {
				cursor : pointer !important;
			}
			
			line.link {
				fill : none !important;
				stroke : #9ecae1 !important; 
				stroke-width : 1.5px !important;
			}
			
			.node text.text_normal {
				pointer-events : none !important;
				font-family : "NanumGothicBold" !important;
				font_size : 13px !important;
				text-anchor : middle !important;
				font-weight : bold !important;
			}
			
			.node text.text_center {
				pointer-events : none !important;
				font-family : "NanumGothicBold" !important;
				font_size : 15px !important;
				text-anchor : middle !important;
				font-weight : bold !important;
			}
			
			.color_center {	/* 3depth 노드 안 텍스트 색상 */
				fill : #fff !important;
			}
			
			.color_city {
				fill : #a26eb3 !important;
			}
			
			.color_nature {
				fill : #6ba22c !important;
			}
			
			.color_nature1 {
				fill : #6ba22c !important;
				stroke-linecap : round !important;
			}
			
			.color_specialty {
				fill : #d55a76 !important;
			}
			
			.color_history {
				fill : #4083c6 !important;
			}
			
			.color_cultural {
				fill : #ce9514 !important;
			}
			
			.color_foaktale {
				fill : #d66f0d !important;
			}
			
			.color_festival {
				fill : #867bd4 !important;
			}
			
			.color_infrastructure {
				fill : #3ba5a5 !important;
			}
			
			.color_default {
				fill : #01DFD7 !important;
			}
			
			
			.node text.text_over {
				pointer-events : none !important;
				fill : #9e1ae1 !important;
				font : 12px sans-serif !important;
				font-weight : bold !important;
				text-anchor : middle !important;
			}
			
			.color_center_text {
				fill : black !important;
			}
		</style>
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
							<form id="searchForm" name="searchForm" method="post">
								<input type="hidden" name="pageSize" id="pageSize" value="10"/>
								<input type="hidden" name="pageType" id="pageType" value="compare"/>	<!-- 연관도 분석, 비교분석 구분용 --> 
								<input type="hidden" name="currentPage" id="currentPage" value="1"/> 
								<input type="hidden" name="startDate" id="startDate" />
								<input type="hidden" name="endDate" id="endDate" />
								<input type="hidden" name="exclusion" id="exclusion" />	<!-- 제외 단어 -->
								<input type="hidden" name="keyword" id="keyword" />	<!-- 주제어 -->
								<input type="hidden" name="condition" id="condition"  value="DAY"/> 	<!-- 조회구분 -->
								<input type="hidden" name="socialChannel" value="all"/> 	<!-- 선호채널 -->
							</form>
							<div class="cont_head">
								<span class="cont_tit">연관도 비교 분석</span><span class="cont_desc">주제어에 대한 연관정보 비교 분석결과를 제공합니다.</span>
							</div>
							<!-- div class="clear2" start -->
							<div class="clear2">
								<!--left contents start -->
								<div class="float_l win_half">
									<!-- 분석조건 A start -->
									<div class="win win_full">
									
										<div class="win_contarea no_head">
		
											<p class="nowin_tit">분석조건 A</p>
		
											<fieldset><legend>분석조건 A</legend>
											<div class="box_blue">
		
												<ul class="terms_list type05">
													<li>
														<span><label for="select01">조회구분</label></span>
														<select id="calendar_selectA" title="조회구분 선택">
															<option value="DAY" >일별</option>
															<option value="WEEK" >주별</option>
															<option value="MONTH" >월별</option>
															<option value="QUARTER" >분기별</option>
															<option value="HALF" >반기별</option>
															<option value="YEAR" >년별</option>
														</select>
													</li>
													<li class="terms_w_auto2">
														<span><label for="select02">분석기간</label></span>
														
														<div>
															<input type="text" class="w90px date_time gray" title="시작날짜 입력" id="startDateA" name="startDateA" readonly /> ~
															<input type="text" class="w90px date_time gray" title="마지막날짜 입력" id="endDateA" name="endDateA" readonly />
														</div>
																							
													</li>
													 
														
													<li>
														<span><label for="select04">채널</label></span>												
		                                      			<select id="socialChannelListA" title="채널">                                      
											  			</select>   
													</li>
													
														
												</ul>
												
												<hr class="terms_line">
												<ul class="terms_list type05">
													<li class="terms_w_auto2">
														<span><label for="keywordTempA">주제어 입력</label></span>
														<select title="주제어 선택" id="keywordListA">
															<option>직접입력</option>
															<c:choose>
																<c:when test="${!empty interestKeyword}">
																	<optgroup label="관심키워드">
																		<c:forEach var="result" items="${interestKeyword}">
																			<option>${result}</option>
																		</c:forEach>
																	</optgroup>
																</c:when>
																<c:otherwise>
																</c:otherwise>
															</c:choose>
														</select>
														<input type="text" id="keywordTempA" name="keywordTempA" class="input_gray w200px" title="주제어 입력" />
													</li>
													<li class="terms_w_auto2 mb_5 mt_20" style="margin-right: 0px;">
														<span><label for="select08">제외어 입력</label></span>
														<input type="text" id="exclusionA" name="exclusionA"  class="white w300px mb_5" title="제외어 입력">
														<br>제외키워드는 콤마 ( , )로 구분됩니다.
													</li>
												</ul>
											</div>
		
											<div class="align_r mt_10"><a href="javascript:;" id="searchStartA" class="btn b_blue2 medium">확인</a>
											<a href="javascript:;" id="searchResetA" class="btn b_gray medium">초기화</a></div>
											</fieldset>
		
										</div>
									
									</div>
									<!--// 분석조건 A end -->
		
									<!-- 연관도 분석 A start -->
									<div class="win win_full">
										<div class="win_head clear2">
											<span class="win_tit bg_none"> 연관도 분석결과A</span>
		
											<ul class="win_btnset">
												<li><a href="#" class="btn_sh" title="접기"></a></li>
											</ul>
										</div>
										
										<div class="win_contarea align_c">
											<div id="d3ChartA" class="graph_in" ></div>
										</div>	
									
									</div>
									<!--// 연관도 분석 A end -->
		
		
									<!-- 연관 키워드 트렌드 분석 결과 A start -->
									<div class="win win_full">
										<div class="win_head clear2">
											<span class="win_tit bg_none">연관 키워드 트렌드 분석결과A</span>
		
										<ul class="win_btnset">
											<li><a href="#" class="btn_sh" title="접기"></a></li>
											</ul>
										</div>
										
										<div class="win_contarea align_c">
											<!-- 리포트 차트 start -->
											<div id="reportChartA" class="graph_in" >
											</div>
											<!--//리포트 차트 end -->
										</div>
									</div>
									<!--// 연관 키워드 트렌드 분석 결과 A end -->
		
									<!-- VOC 검색결과 A start -->
									<div class="win win_full" id="search_resultA">
										<div class="win_head clear2">
											<span class="win_tit">검색결과A</span>
											<ul class="win_btnset">
											<li><a href="javascript:;" class="btn_xls" title="액셀파일 내려받기"></a></li>
											<li><a href="javascript:;" class="btn_sh" title="접기"></a></li>
											</ul>
										</div>
										<div id="vocSearchTeam" class="win_contarea align_c">
											<div class="graph_in">해당 검색정보가 없습니다.</div>
										</div>
									</div>
									<!--// VOC 검색결과 A end -->
								</div>
								<!--//left contents end -->
								<!-- right contents start -->
								<div class="float_r win_half">
									<!-- 분석조건 B start -->
									<div class="win win_full">
										<div class="win_contarea no_head">
											<p class="nowin_tit">분석조건 B</p>
											<fieldset><legend>분석조건 B</legend>
											<div class="box_blue">
		
												<ul class="terms_list type05">
													<li>
														<span><label for="select01">조회구분</label></span>
														<select id="calendar_selectB" title="조회구분 선택">
															<option value="DAY" >일별</option>
															<option value="WEEK" >주별</option>
															<option value="MONTH" >월별</option>
															<option value="QUARTER" >분기별</option>
															<option value="HALF" >반기별</option>
															<option value="YEAR" >년별</option>
														</select>
													</li>
													<li class="terms_w_auto2">
														<span><label for="select02">분석기간</label></span>
														
														<div>
															<input type="text" class="w90px date_time gray" title="시작날짜 입력" id="startDateB" name="startDateB" readonly /> ~
															<input type="text" class="w90px date_time gray" title="마지막날짜 입력" id="endDateB" name="endDateB" readonly />
														</div>
																							
													</li>
													<li>
														<span><label for="select04">채널</label></span>												
		                                      			<select id="socialChannelListB" title="채널">                                      
											  			</select>   
													</li>
													
													
												</ul>	
												
												<hr class="terms_line">
												<ul class="terms_list type05">
													<li class="terms_w_auto2">
														<span><label for="keywordTempB">주제어 입력</label></span>
														<select title="주제어 선택" id="keywordListB">
															<option>직접입력</option>
															<c:choose>
																<c:when test="${!empty interestKeyword}">
																	<optgroup label="관심키워드">
																		<c:forEach var="result" items="${interestKeyword}">
																			<option>${result}</option>
																		</c:forEach>
																	</optgroup>
																</c:when>
																<c:otherwise>
																</c:otherwise>
															</c:choose>
														</select>
														<input type="text" id="keywordTempB"  name="keywordTempB" class="input_gray w200px" title="주제어 입력" />
													</li>
													<li class="terms_w_auto2 mb_5 mt_20" style="margin-right: 0px;">
														<span><label for="exclusionB">제외어 입력</label></span>
														<input type="text" id="exclusionB" name="exclusionB"  class="white w300px mb_5" title="제외어 입력">
														<br>제외키워드는 콤마 ( , )로 구분됩니다.
													</li>
												</ul>
											</div>
		
											<div class="align_r mt_10"><a href="javascript:;" id="searchStartB" class="btn b_blue2 medium">확인</a>
											<a href="javascript:;" id="searchResetB" class="btn b_gray medium">초기화</a></div>
											</fieldset>
		
										</div>
									
									</div>
									<!--// 분석조건 B end -->
		
									<!--  연관도 분석 B start -->
									<div class="win win_full">
										<div class="win_head clear2">
											<span class="win_tit bg_none"> 연관도 분석결과B</span>
		
										<ul class="win_btnset">
											<li><a href="#" class="btn_sh" title="접기"></a></li>
											</ul>
										</div>
										
										<div class="win_contarea align_c">
											<div id="d3ChartB" class="graph_in"></div>
										</div>	
									
									</div>
									<!--// 연관도 분석 B end -->
		
		
		
									<!-- 연관 키워드 트렌드 분석 결과 B start -->
									<div class="win win_full">
										<div class="win_head clear2">
											<span class="win_tit bg_none">연관 키워드 트렌드 분석결과B</span>
		
										<ul class="win_btnset">
											<li><a href="#" class="btn_sh" title="접기"></a></li>
											</ul>
										</div>
										
								 		 <div class="win_contarea">
								 		 	<!-- 리포트 차트 start -->
											<div id="reportChartB" class="graph_in">
											</div>
											<!--// 리포트 차트 end -->
										 </div>
									</div>
									<!--// 연관 키워드 트렌드 분석 결과 B end -->
		
									<!-- VOㅊ 검색결과 B start -->
									<div class="win win_full" id="search_resultB">
										<div class="win_head clear2">
											<span class="win_tit">검색결과B</span>
											<ul class="win_btnset">
											<li><a href="javascript:;" class="btn_xls" title="액셀파일 내려받기"></a></li>
											<li><a href="javascript:;" class="btn_sh" title="접기"></a></li>
											</ul>
										</div>
										<div id="vocSearchTeam" class="win_contarea align_c">
											<div class="graph_in">해당 검색정보가 없습니다.</div>
										</div>
									</div>
									<!--// VOㅊ 검색결과 B end -->
								</div>
								<!--//right contents end -->
							</div>
							<!--// div class="clear2" end -->
						</div>
						<!-- 본문 end -->
						<!-- 상세페이지 팝업레이어 -->
						<div class="modal_popup result_view" id="basic-modal-detail" >
						</div>
						<!-- 유사문서 팝업레이어 -->
						<div class="modal_popup doc_view" id="basic-modal-alike" >
						</div>
					</div>
					<!--//right area start -->
				</div>
				<!--//cont end -->
			</div>
			<!--//content end -->
		</div>
		<!--//wrapper end -->
	</body>
</html>