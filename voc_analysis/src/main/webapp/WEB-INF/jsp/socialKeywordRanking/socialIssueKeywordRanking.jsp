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
		<title>서울교통공사 : 이슈 키워드 분석</title>
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
		<script type="text/javascript">
		function getContextPath() {
			return "<c:out value="${pageContext.request.contextPath}" />";
		}
		$(function () {
			var selectDate = "2017/01/23";
			selectDate = "";
			//주단위 선택
			$('.ui-datepicker-calendar tr').live('mousemove', function() { $(this).find('td a').addClass('ui-state-hover'); });
			$('.ui-datepicker-calendar tr').live('mouseleave', function() { $(this).find('td a').removeClass('ui-state-hover'); });
			
			$( window ).load(function() {
				$.ajax({
					type : "POST", 
					url : getContextPath() + "/common/selectOptionListSocial.do",
					dataType: 'json', 
					contentType: 'application/json;charset=utf-8',
					mimeType: 'application/json',
					success : function(response) {				
						$('#socialChannelList').html(selectListSocial(response.socialChannelList));	// 소셜 채널
						
					}
				}); 
				
				// ------- 최초 출력화면 start 
				var selectOption = $('#selectOption').val();
				
				if(selectOption=='month_select'){
					$("input[name='condition']").val("MONTH");
				}else if(selectOption=='year_select'){
					$("input[name='condition']").val("YEAR");
				}else{
					$("input[name='condition']").val("WEEK");
				}
				$('#week_select').datepicker('setDate', new Date(selectDate));
				$('#week_select').change();
				$('#searchForm input[name=currentPage]').val('1');
				//$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());
				
				// ------- 최초 출력화면 end 
			});
		});
		</script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/socialIssueKeywordRanking.js'/>"></script>
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
		<!-- wrapper start -->
		<div id="wrapper">
			<hr />
			<!-- content start -->
			<div id="content">
				<!-- cont start -->
				<div id="content">
					<!-- right area start -->
					<div class="right_area">
						<!-- 본문 start -->
						<div class="cont_body" id="cont_body">
							<div class="cont_head">
								<span class="cont_tit">이슈 키워드 분석</span><span class="cont_desc">출현이 급증한 이슈키워드에 대한 랭킹분석 기능을 제공합니다. </span>
							</div>
							<form id="searchForm" name="searchForm" method="post">	 
								<input type="hidden" name="pageType" value="issueKeyword"/> 
								<input type="hidden" name="keyword"/> 
								<input type="hidden" name="pageSize" value="10"/> 
								<input type="hidden" name="currentPage" value="1"/> 
								<input type="hidden" name="startDate"/>
								<input type="hidden" name="endDate"/>
								<input type="hidden" name="condition" value="DAY"/> 		<!-- 조회구분 -->
								<input type="hidden" name="socialChannel" value="all"/> 	<!-- 선호채널 -->
								
							<!-- 분석조건 start -->
							<div class="opt_box">
								<div class="no_head">
									<p class="nowin_tit">분석조건</p>
									<fieldset><legend>분석조건</legend>
									<div class="box_blue">
		
										<ul class="terms_list">
											<li>
												<span><label for="selectOption">조회구분</label></span>
												<select id="selectOption" title="조회구분 선택" class="select_last">
													<!-- <option value="dayofweek_select">전요일대비</option> -->
													<option value="week_select">전주대비</option>
													<option value="month_select">전월대비</option>
													<option value="year_select">전년대비</option>
												</select>
											</li>
											<li class="wlong">
												<span><label for="select02">분석기간</label></span>
												
												<div class="select_date week_select">
													<span class="gray">기준 주 선택</span>
													<input type="text" name="week_select" id="week_select" class="w100px date_time gray" title="기준주간 입력" />
													<span class="gray ml_10">선택기간:</span>
													<span id="week_select_text" class="orange"></span>
												</div>
												<div class="select_date month_select">
													<span class="gray">기준 월 선택</span>
													<select id="month_select01" title="년도 선택">
														<option value="2020">2020년</option>
														<option value="2019">2019년</option>
														<option value="2018">2018년</option>
														<option value="2017">2017년</option>
														<option value="2016">2016년</option>
														<option value="2015">2015년</option>
														<option value="2014">2014년</option>
														<option value="2013">2013년</option>
														<option value="2012">2012년</option>
														<option value="2011">2011년</option>
														<option value="2010">2010년</option>
														<option value="2009">2009년</option>
														<option value="2008">2008년</option>
														<option value="2007">2007년</option>
														<option value="2006">2006년</option>
														<option value="2005">2005년</option>
													</select>
													<select title="월 선택" id="month_select02">
														<option value="01">1월</option>
														<option value="02">2월</option>
														<option value="03">3월</option>
														<option value="04">4월</option>
														<option value="05">5월</option>
														<option value="06">6월</option>
														<option value="07">7월</option>
														<option value="08">8월</option>
														<option value="09">9월</option>
														<option value="10">10월</option>
														<option value="11">11월</option>
														<option value="12">12월</option>
													</select>
													<span class="gray ml_10">선택기간:</span><span id="month_select_text" class="orange"></span>
												</div>
												<div class="select_date year_select">
													<span class="gray">기준 년 선택:</span>
													<select id="year_select" title="연도 선택">
														<option value="2020">2020년</option>
														<option value="2019">2019년</option>
														<option value="2018">2018년</option>
														<option value="2017">2017년</option>
														<option value="2016">2016년</option>
														<option value="2015">2015년</option>
														<option value="2014">2014년</option>
														<option value="2013">2013년</option>
														<option value="2012">2012년</option>
														<option value="2011">2011년</option>
														<option value="2010">2010년</option>
														<option value="2009">2009년</option>
														<option value="2008">2008년</option>
														<option value="2007">2007년</option>
														<option value="2006">2006년</option>
														<option value="2005">2005년</option>
													</select>
													<span class="gray ml_10">선택기간:</span><span id="year_select_text" class="orange">2014년 ~ 2015년</span>
												</div>
											</li>
											
											<!-- 
											<li class="mt_10">
												<span><label for="vocChannelList">접수채널</label></span>
												<select id="vocChannelList"></select>
											</li>
											
											<li class="mt_10">
												<span><label for="vocRecTypeList">VOC 종류</label></span>
												<select id="vocRecTypeList"></select>
											</li>
											
											<li class="mt_10">
												<span><label for="metroDeptList">처리주무부서</label></span>
												<select id="metroDeptList" title="처리주무부서">
													<option value="all" >전체</option>
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
												
												<span id="vocKindList" ></span>
												<span id="vocPartList" ></span>
												<span id="vocItemList" ></span>
											</li> -->
											<li>
												<span><label for="select04">채널</label></span>
												
		                                      	<select id="socialChannelList" title="채널">                                      
											  	</select>   
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
							
							<!-- 관심키워드랭킹 TOP10 리포트 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">이슈 키워드 리포트</span>
									<ul class="win_btnset">
										<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
								<div class="win_contarea align_c">
									<div class="graph_in">
										<div id="reportChart" ></div>
									</div>
								</div>
							</div>
							<!--// 관심키워드랭킹 TOP10 리포트 end -->
							
							<!-- 관심키워드랭킹 분석 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">이슈 급증 키워드 랭킹</span>
		
									<ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								<div class="win_contarea" id="total_Rank" >
								</div>
							</div>
							<!--// 관심키워드랭킹 분석 end -->
							
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
									<div class="graph_in"></div>
								</div>
							</div>
							<!-- VOC 검색결과 end -->
						</div>
						<!-- 본문 end -->
						<!-- 상세페이지 팝업레이어 -->
						<div class="modal_popup result_view" id="basic-modal-detail" ></div>
						<!-- 유사문서 팝업레이어 -->
						<div class="modal_popup doc_view" id="basic-modal-alike" ></div>
					</div>
					<!-- right area end -->
				</div>
				<!-- cont end -->
			</div>
			<!-- content end -->
		</div>
		<!-- wrapper end -->
	</body>
</html>