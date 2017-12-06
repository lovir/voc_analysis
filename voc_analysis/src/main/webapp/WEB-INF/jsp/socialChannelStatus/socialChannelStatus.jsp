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
		<title>서울교통공사 : 채널별 현황분석</title>
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
	 	<script type="text/javascript" src="<c:url value='/resources/js/header.js'/>"></script>
	 	<script type="text/javascript" src="<c:url value='/resources/js/voc/socialChannelStatus.js'/>"></script> 
		<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
	</head>	
		<script type="text/javascript">
			//태블로 연동용 스크립트
			window.onload = function() {
				window.parent.postMessage(
					document.body.scrollHeight+"|"+document.body.scrollWidth
					,"*"
				)
			};
		</script>
		
		
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
			
			function getContextPath() {
				return "<c:out value="${pageContext.request.contextPath}" />";
			}
			$(function () {
				$( window ).load(function() {
					$('.rank_keyword').hide();
					$('#socialChannelCont').hide();
					$('#search_result').hide();
					$.ajax({
						type : "POST", 
						url : getContextPath() + "/common/selectOptionListSocial.do",
						dataType: 'json', 
						contentType: 'application/json;charset=utf-8',
						mimeType: 'application/json',
						success : function(response) {				
							$('#socialChannelList').html(selectList(response.socialChannelList));	// 소셜 채널
							
						}
					}); 
				});
				
			});
			</script>

			<!-- right area start -->
			<div class="right_area">
				

				<!-- 본문 start -->
				<div class="cont_body" id="cont_body">
					<form id="searchForm" name="searchForm" method="post">	 
						<input type="hidden" name="pageType" value="socialChannelStatus"/> 
						<input type="hidden" name="pageSize" value="10"/> 
						<input type="hidden" name="currentPage" value="1"/> 						
						<input type="hidden" name="searchType" value="all"/>	<!-- 검색타입 -->
						<input type="hidden" name="keyword" value=""/>			<!-- 키워드 -->
						<input type="hidden" name="title" value=""/>
						<input type="hidden" name="content" value=""/>
						<input type="hidden" name="socialChannel" value="all"/>
						<input type="hidden" name="socialSite" value="all"/>
						<input type="hidden" name="condition" value="DAY"/>
		
					<div class="cont_head">
						<span class="cont_tit">채널별 현황분석</span><span class="cont_desc">사용자가 지정한 채널에 대한 랭킹분석 기능을 제공합니다. </span>
					</div>


					<!-- 분석조건 start -->
					<div class="opt_box">
					
						<div class="no_head">

							<p class="nowin_tit">분석조건</p>

							<fieldset><legend>분석조건</legend>
							<div class="box_blue">

								<ul class="terms_list">
									<li>
										<span><label for="select01">조회구분</label></span>
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

                                    <li class="w100pp">
										<span><label for="select04">채널</label></span>
										
                                      <select id="socialChannelList" title="채널">                                      
									  </select>
                                     
									</li>
								</ul>
							</div>

							<div class="align_r mt_10"><a href="#" id="searchStart" class="btn b_blue2 medium">확인</a><a href="#" id="searchReset" class="btn b_gray medium">초기화</a></div>
							</fieldset>

						</div>
					</div>
					</form>
					<!--// 분석조건 end -->


					<!-- 분야별 현황 start -->
					<div id = "socialChannelCont" class="win win_full">
						<div class="win_head clear2">
							<span class="win_tit bg_none">채널별 현황</span>

							<ul class="win_btnset">
							<li><a href="#" class="btn_sh" title="접기"></a></li>
							</ul>
						</div>
						
						<div class="win_contarea align_c">
                        	<div class="p_15 clear2">
                            	<p class="float_l">
                            	<label for="chart01"  class="mr_10"><input type="radio" id="pieChart" name="radio" checked>채널별 비율</label>
                            	<label for="chart02"><input type="radio" id="lineChart" name="radio"/>채널별 현황</label></p>
                               
                            </div>
							<div id="chart01" class="graph_in chart01 clear2">
                            	 <div class="clear">
                                 			<div class="chart_w02 mr_20">                
                                 			</div>
                                             <div class="rank_keyword">
                                                    <div class="rank_top">채널별 키워드 Top 10</div>
                                                        <div class="rank_list">
                                                           
                                                        </div>
                                                    </div>

                                                

											</div>
                            
                            
                            </div>
                            
                            <div id="chart02" class="graph_in chart02 on"  style="height:400px;">
                            <div id="reportChart">채널별 현황</div>
                      		
                      		</div>
					  </div>
					
					</div>
					<!--// 분야별 현황 end -->


				


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
					<!-- 상세페이지 팝업레이어 -->
					<div class="modal_popup result_view" id="basic-modal-detail" >
					</div>
					<!-- 유사문서 팝업레이어 -->
					<div class="modal_popup doc_view" id="basic-modal-alike" >
					</div>

				
				</div>
				<!--// 본문 end -->

				
			</div>
			<!--// right area end -->

		</div>

</div>
	<!--// content end -->
</div>
</body>
</html>