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
		<title>서울교통공사 : 대시보드</title>
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
		<script src="<c:url value='/resources/js/jQCloud/jqcloud.min.js'/>"></script>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jQCloud/jqcloud.min.css'/>" />
		<script src="<c:url value='/resources/js/attrchange.js'/>"></script>	<!-- iFrame 연동 관련 추가 라이브러리 -->
		<!-- header start -->
		<script type="text/javascript" src="<c:url value='/resources/js/header.js'/>"></script>
		
		<!--// header end -->
		<script type="text/javascript">
			function getContextPath() {
				return "<c:out value="${pageContext.request.contextPath}" />";
			}
		</script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/dashBoard.js'/>"></script>
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
					<!-- 모달팝업 : 알람키워드 모니터링 start -->
					<div class="modal_popup modal_01" id="basic-modal-util_01" >
					</div>
					<hr />
					<!--// 모달팝업 : 알람키워드 모니터링 end -->
					
					
					<!-- body resource end -->
					
					<!-- right area start -->
					<div class="right_area">
						<!-- 본문 start -->
						<div class="cont_body" id="cont_body">
							<form id="searchForm" name="searchForm" method="post">	 
								<input type="hidden" name="pageType" value="dashboard"/> 
								<input type="hidden" name="currentPage" value="1"/> 
								<input type="hidden" name="startDate" value=""/> 
								<input type="hidden" name="endDate" value=""/> 
								<input type="hidden" name="condition" value="DAY"/> 
								<input type="hidden" name="stationCurrentPageNo" value="1"/> 	<!-- 역별현황사용 현재페이지 -->
								<input type="hidden" name="stationEndPage" value="6"/> 			<!-- 역별현황사용 -->
								<input type="hidden" name="stationTotalSize" value="6"/> 		<!-- 역별현황사용 토탈사이즈 -->
								<input type="hidden" name="stationPageSize" value="6"/> 		<!-- 역별현황사용 토탈사이즈 -->
								<input type="hidden" name="portal_id" value="<c:out value="${portal_id}" />"/>
								<input type="hidden" name="portal_nm" value="<c:out value="${portal_nm}" />"/> 
								<input type="hidden" name="prevStartDate" value=""/> 
								<input type="hidden" name="prevEndDate" value=""/>
								<input type="hidden" name="stationName" value=""/>	<!-- 역별현황사용 역명 -->
								<input type="hidden" name="keyword" value=""/>		<!-- 이슈클라우드 사용 키워드 -->
								<input type="hidden" name="channel" value=""/>		<!-- 이슈클라우드 사용 채널(민원/콜센터/소셜) -->
							</form>
								
							<div class="cont_head">
								<span class="cont_tit">대시보드</span><span class="cont_desc">통합 VOC의 주요 분석항목을 대시보드 형태로 제공합니다.</span>
		                        <div class="align_r mb_10 mt_-20">
		                         <a href="#" title="알림" name="util_01" class="modal_btn util_01"></a>
									<fieldset><legend>조건선택</legend>	
		                     		 <select id="dateType" title="조건선택">
		                     		 	<option value="DAY" selected>전일대비</option>
										<option value="WEEK">전주대비</option>
										<option value="MONTH">전월대비</option>
									</select>
									<a href="#" id="searchBtn" class="btn b_orange ssmall">확인</a>
									</fieldset>
								</div>
							</div>

							<div class="clear2">
								<div class="top_graph">			
									<!-- <div class="left_areas"> -->
									<div class="left_areas">
				 						<div class="top_donut h_200 left clear2">
											<p>긍/부정 VOC 증감현황</p>
											<ul>		
												<li>
													<span class="d_keyword">긍정</span>
													<div class="donut_back" id="needs0"></div>
													<div class="d_keyword2" id="d_keyword0"></div>
												</li>
				
												<li>
													<span class="d_keyword">중립</span>
													<div class="donut_back" id="needs1"></div>
													<div class="d_keyword2" id="d_keyword1"></div>
												</li>
				
												<li>
													<span class="d_keyword">부정</span>
													<div class="donut_back" id="needs2"></div>
													<div class="d_keyword2" id="d_keyword2"></div>
												</li>
				
												
				
											</ul>
										</div>
				                        <!-- 관심키워드 트렌드 분석 start -->
										<div class="win win_full" style="margin-top:15px !important;">
										  <div class="win_head clear2 align_l">
												<a href="javascript:goInterestKeywordRankingPage();"><span class="win_tit bg_none">관심키워드 트렌드 분석</span></a>
												<ul class="win_btnset">
												<!-- <li class="right_help">
													<a href="#" class="icon_help"></a>
													<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
												</li> -->
												
												</ul>
										  </div>
											<div class="win_contarea align_c">
												<div id="interestChart" class="graph_in" style="height:303px;">
												</div>
											</div>
										</div>
										<!--// 관심키워드 트렌드 분석 end -->
                      				</div>
                      				<!--// <div class="left_areas"> -->
									<!-- <div class="right_areas"> -->
                           			<div class="right_areas">
                           				<!--콜센터 유형별 현황-->
		                                <table class="tbl_type05" summary="지하철 노선 별 콜센터 VOC현황">
											<caption>지하철 노선별 VOC</caption>
											<colgroup>	                        
												<col width="20%">
									            <col>
									            <col>                    
									            <col>
											</colgroup>
											
									        <thead>
									            <tr class="last_line">
													<th rowspan="2" style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;">유형별</th>
													<th rowspan="2" style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;">계 (A)</th>
													<th colspan="2" style="border-right:1px dotted #dbdbdb;">콜센터</th>
													<th colspan="2" style="border-right: none; ">민원</th>
												</tr>
												<tr>
													<th style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;"><p>1~4호선</p></th>
													<th style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;"><p>5~8호선</p></th> 
													<th  style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;"><p>1~4호선</p></th>
													<th style="border-right: none; border-bottom:1px solid #dbdbdb;"><p>5~8호선</p></th> 
												</tr>
											</thead>
											
											<tbody id="kindChart">
												
											</tbody>
										</table>
		                                <!--// 콜센터 유형별 현황-->                               
                                
	                                	<!--노선별 긍부정-->
	                                	<div class="win win_full" style="margin-top:0px !important;" >
		                                	<div class="win_head clear2 align_l">
			                                	<a href="javascript:goStation();"><span id="stationSpan" class="win_tit bg_none">주요 역별 현황 민원 분석</span></a>
			                                    <div id="stationChart" class="line_boxw">
			                                        
			                                    </div> 
		                                    </div>
	                                    </div> 
                                		<!--//노선별 긍부정-->        
                                		                       
                                
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
                           			</div>
                           			<!--// <div class="right_areas"> -->
	                     		</div>
							</div>
						</div>
						<!-- <div class="clear2"> -->
						<div class="clear2">
							<!-- 관심키워드 트렌드 분석 start -->
							<div class="win win_1 float_l mr_10">
								<div class="win_head clear2">
									<a href="javascript:goSynthesisRankingPage();"><span id="keywordrankSpan" class="win_tit bg_none">금일 키워드 TOP10</span></a>
									<ul class="win_btnset">
									<!-- <li class="right_help">
										<a href="#" class="icon_help"></a>
										<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
									</li> -->
									
									</ul>
								</div>
								<div class="win_contarea align_c">
								  <div class="graph_in" style="height:280px;">
	                               	<div id="total_Rank" class="keywodrank_list">
											
									</div>
	                                
	                                
	                                </div>
								</div>
							</div>
				      		<!--// 관심키워드 트렌드 분석 end -->
	
							<!-- 금주 카테고리 Top 10 start -->
							<div class="win win_1 float_l">
								<div class="win_head clear2">
									<a href="javascript:goFieldStatus();"><span id="categoryrankSpan" class="win_tit bg_none" >금일 분야별 Top 10</span></a>
									<ul class="win_btnset">
									<!-- <li class="right_help">
										<a href="#" class="icon_help"></a>
										<div class="tooltip btn_qty1">금주 분야별 Top 10을 확인할 수 있습니다.<div class="tip-arrow"></div></div>
									</li> -->
									
									</ul>
								</div>
								<div class="win_contarea align_c">
									<div class="graph_in" style="height:280px;">
	                                
	                                <div id="category_Rank" class="keywodrank_list">
											
									  </div>
	                                
	                              </div>
								</div>
							</div>
							<!--// 금주 카테고리 Top 10 end -->
	
							<!--  이슈 클라우드 start -->
							<div class="win win_3 float_l ml_10">
								<div class="win_head clear2">
									<span class="win_tit bg_none">이슈 클라우드</span>
									<ul class="win_btnset">
									<!-- <li class="right_help">
										<a href="#" class="icon_help"></a>
										<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
									</li> -->
									
									</ul>
								</div>
								<!-- <div class="graph_in" style="height:285px;"> -->
								<div class="win_contarea align_c">
									<div class="graph_in" style="height:285px;">
	                                    <div class="cloud_opt clear2">
	                                        <ul class="cloud_left clear2">
	                                           
	                                        </ul>
	                                        <ul class="cloud_right clear2">
	                                            <li><a href="#" class="cloud_check type01" id="cloud_01" title="콜센터"></a><label for="cloud_01">콜센터</label></li>
	                                            <li><a href="#" class="cloud_check type02" id="cloud_02" title="VOC"></a><label for="cloud_02">민원</label></li>
	                                            <li><a href="#" class="cloud_check type03" id="cloud_03" title="소셜"></a><label for="cloud_03">소셜</label></li>
	                                        </ul>
	                                    </div>
	                                    
	                                    <div id="cloudChart" class="mt_10"><a href="#" class="modal_btn" name="cloud_view"></a></div>
	
	
										<!-- 모달팝업 : 이슈키워드 상세보기 start -->
										<div class="modal_popup w700px" id="basic-modal-cloud_view" >
											<div class="win win_full" style="margin-top:0">
												<div class="win_head clear2">
													<span class="win_tit bg_none">이슈키워드 상세보기</span>
												</div>
												
												<div class="win_contarea clear2 cloud_modal">
													<div id="issueLineChart" class="align_c" style="height:350px;">그래프 삽입 (100% x 350px)</div>
													<table class="tbl_type04" summary="키워드, 전주/금주 건수, 점유율로 구성된 이슈키워드 정보">
														<caption>이슈키워드 정보</caption>
														<thead id="issueTableHead">
															<!-- <tr>
																<th scope="col">키워드</th>
																<th scope="col">전주 건수</th>
																<th scope="col">금주 건수</th>
																<th scope="col">해당 유형 내 점유율</th>
															</tr> -->
														</thead>
														<tbody id="issueTable">
															<!-- <tr>
																<td>챠트수정</td>
																<td>87건</td>
																<td>25건</td>
																<td>15.6%</td>
															</tr> -->
														</tbody>
		
													</table>
												</div>
											</div>
										</div>
										<hr />
										<!--// 모달팝업 : 이슈키워드 상세보기 end -->
	
									</div>
									<!--// <div class="graph_in" style="height:285px;">  -->
								</div>
							</div>
							<!--//이슈 클라우드 end -->
						</div>
						<!--// <div class="clear2"> -->
					</div>
					<!--// 지점별 주요 키워드 분석 end -->
				</div>
	  		</div>							
		<!-- 본문 END -->	
		</div>
	</body>
</html>