<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</head>
<body style="background:#fff !important;">

<div id="skipnavi">
	<ul>
	<li><a href="#content">주메뉴 바로가기</a></li>
	<li><a href="#cont_body">본문 바로가기</a></li>
	</ul>
</div>
<hr />
<div id="wrapper">

	<!-- header start -->
  <!--// header end -->

<hr />


	<!-- content start -->
	<div id="content">

		<div class="cont">
        
        <!-- 모달팝업 : 알람키워드 모니터링 start -->
	<div class="modal_popup modal_01" id="basic-modal-util_01" />
		<div class="win win_full" style="margin-top:0">
			<div class="win_head clear2">
				<span class="win_tit bg_none">알람키워드 모니터링</span>
			</div>
			
			<div class="win_contarea clear2">

				<!-- 그래프 start -->
				<div class="alarm_left clear2">
					<fieldset><legend>기간 선택</legend>
						<ul class="radio_term clear2">
						<li><input type="radio" id="alarm_01" /><label for="alarm_01">지난 일주일</label></li>
						<li><input type="radio" id="alarm_02" /><label for="alarm_02">전날</label></li>
						<li><input type="radio" id="alarm_03" /><label for="alarm_03">오늘</label></li>
						</ul>
						<div class="p_10 align_c">그래프 삽입</div>
					</fieldset>
				</div>
				<!--// 그래프 end -->
				
				<!-- 표 start -->
				<div class="alarm_right clear2">
					<fieldset><legend>키워드 목록</legend>
						<table class="tbl_type01" summary="키워드, VOC유형, 평균건수로 구성된 키워드 목록">
						<caption>키워드 목록</caption>
						<colgroup>
							<col style="width:27px;" />
							<col style="width:100px;" />
							<col style="width:100px;" />
							<col />
						</colgroup>
						<thead>
							<tr>
							<th scope="col" class="align_c"><input type="checkbox" title="전체 선택" /></th>
							<th scope="col">키워드</th>
							<th scope="col">VOC점유율</th>
							<th scope="col">평균건수</th>
							</tr>
						</thead>
						<tbody>
							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_01">클레임</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_02">수익률</td>
							<td>불만, 문의</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_03">관리</td>
							<td>불만, 제안, 칭찬</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_04">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_05">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_06">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_07">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_08">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_09">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

							<tr>
							<td class="align_c"><input type="checkbox" /></td>
							<td><span class="key_10">알람 키워드</td>
							<td>불만</td>
							<td class="av_r">1,234</td>
							</tr>

						</tbody>
						</table>
					</fieldset>
                    
                    
                   
				</div>
				<!--// 표 end -->

				<!-- 기본정보 start -->				
				<div class="alarm_bottom">
					<ul>
					<li>
						<span>키워드</span>
						<div>성추행</div>
					</li>
					<li>
						<span>중분류</span>
						<div>열차운영</div>
					</li>
					<li>
						<span>접수채널</span>
						<div>SMS</div>
					</li>
					
					</ul>
				</div>
				<!--// 기본정보 end -->
			</div>
		
		</div>
	</div>
	<hr />
	<!--// 모달팝업 : 알람키워드 모니터링 end -->


			

<!-- right area start -->
<div class="right_area">
				

<!-- 본문 start -->
				
<div class="cont_body" id="cont_body">


				
					<div class="cont_head">
						<span class="cont_tit">대시보드</span><span class="cont_desc">통합 VOC의 주요 분석항목을 대시보드 형태로 제공합니다.</span>
                        <div class="align_r mb_10 mt_-20">
                         <a href="#" title="알림" name="util_01" class="modal_btn util_01"></a>
							<fieldset><legend>조건선택</legend>
                            
								<select title="조건선택" class="mr_10">
								<option>전체</option>
								<option>VOC</option>
                                <option>소셜</option>
								</select>
                                
                      <select title="조건선택">
								<option>전주대비</option>
								<option>전월대비</option>
							</select>
								<a href="#" class="btn b_orange ssmall">확인</a>
							</fieldset>
						</div>
                       
					</div>

					<div class="clear2">
					<div class="top_graph">
						
                        
						<div class="left_areas">
				  <div class="top_donut h_200 left clear2">
							<p>긍/부정 VOC 증감현황</p>
							<ul>
								<li>
									<span class="d_keyword">긍정</span>
									<div class="donut_back">
										<!-- 그래프 삽입 (106x106) -->
										<img src="images/temp/temp_graph_donut_blue.png" alt="그래프" />
									</div>
									<span class="d_percent"><img src="images/common/arrow_down.png" alt="하락" />49<span>%</span></span>
									<span class="d_number">-1,234건</span>
								</li>

								<li>
									<span class="d_keyword">중립</span>
									<div class="donut_back">
										<!-- 그래프 삽입 (106x106) -->
										<img src="images/temp/temp_graph_donut_green.png" alt="그래프" />
									</div>
									<span class="d_percent"><img src="images/common/arrow_up.png" alt="상승" />32<span>%</span></span>
									<span class="d_number">514건</span>
								</li>

								<li>
									<span class="d_keyword">부정</span>
									<div class="donut_back">
										<!-- 그래프 삽입 (106x106) -->
										<img src="images/temp/temp_graph_donut_red.png" alt="그래프" />
									</div>
									<span class="d_percent"><img src="images/common/arrow_down.png" alt="하락" />17<span>%</span></span>
									<span class="d_number">-1,468건</span>
								</li>

								

							</ul>
						</div>
                        <!-- 관심키워드 트렌드 분석 start -->
						<div class="win win_full" style="margin-top:15px !important;">
						  <div class="win_head clear2 align_l">
								<span class="win_tit bg_none">관심키워드 트렌드 분석</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help"></a>
									<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
						  </div>
							<div class="win_contarea align_c">
								<div class="graph_in" style="height:303px;"><img src="images/temp/temp_graph11.png" alt="관심키워드 트렌드 분석 그래프"></div>
							</div>
						</div>
						<!--// 관심키워드 트렌드 분석 end -->
                      </div>

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
											<th colspan="2" style="border-right: none; ">VOC</th>
										</tr>
										<tr>
											<th style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;"><p>1~4호선</p></th>
											<th style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;"><p>5~8호선</p></th>
											<th style="border-right:1px dotted #dbdbdb; border-bottom:1px solid #dbdbdb;"><p>1~4호선</p></th>
											<th style="border-right: none; border-bottom:1px solid #dbdbdb;"><p>5~8호선</p></th>
										</tr>
									</thead>
									<tbody>
										
										<tr>
											<td class="bg" style="border-top:1px solid #fff;">계</td>
											<td><p>5,360</p></td>
											<td><p>1,660</p></td>
											<td>520</td>
											<td><p>1,660</p></td>
											<td style="border-right: none;">520</td>
										</tr>
										
										<tr>
											<td class="bg"><p>단순문의</p></td>
											<td><p>554</p></td>
											<td>166</td>
											<td>111</td>
											<td>166</td>
											<td style="border-right: none;"><p>111 </p></td>
										</tr>
										
										<tr>
											<td class="bg"><p>불편개선</p></td>
											<td>3794</td>
											<td>1,488</td>
											<td>408</td>
											<td>1,488</td>
											<td style="border-right: none;">408</td>
										</tr>
										
										<tr>
											<td class="bg"><p>칭찬격려</p></td>
											<td><p>14</p></td>
											<td>6</td>
											<td>1</td>
											<td>6</td>
											<td style="border-right: none;">1</td>
										</tr>
									</tbody>
								</table>
                                <!--// 콜센터 유형별 현황-->
                                
                                
                                 <!--노선별 긍부정-->
                                    <div class="line_boxw">
                                        <div class="line_half mr_10">
                                            <p class="t_line2"><a href="#" class="modal_btn" name="doc_03">신도림역</a></p>
                                            <p class="bg_gray mt_5"><span class="num1">1226건</span><span class="keyword_m">더워요</span><span class="smile"></span></p>
                                            <p class="condition mt_5"><span class="bule_bg mr_5">104건</span><span class="orange_bg mr_5">1100건</span><span class="green_bg">2건</span></p>
                                            
                                        </div>
                                        <div class="line_half">
                                            <p class="t_line1"><a href="#" class="modal_btn" name="doc_03">시청역</a></p>
                                            <p class="bg_gray mt_5"><span class="num1">1226건</span><span class="keyword_m">더워요</span><span class="smile"></span></p>
                                            <p class="condition mt_5"><span class="bule_bg mr_5">104건</span><span class="orange_bg mr_5">1100건</span><span class="green_bg">2건</span></p>
                                            
                                        </div>
                                        <div class="line_half mr_10">
                                            <p class="t_line5"><a href="#" class="modal_btn" name="doc_03">여의도역</a></p>
                                            <p class="bg_gray mt_5"><span class="num1">1226건</span><span class="keyword_m">더워요</span><span class="smile"></span></p>
                                            <p class="condition mt_5"><span class="bule_bg mr_5">104건</span><span class="orange_bg mr_5">1100건</span><span class="green_bg">2건</span></p>
                                            
                                        </div>
                                        <div class="line_half">
                                            <p class="t_line2"><a href="#" class="modal_btn" name="doc_03">구로디지털역</a></p>
                                            <p class="bg_gray mt_5"><span class="num1">1226건</span><span class="keyword_m">더워요</span><span class="smile"></span></p>
                                            <p class="condition mt_5"><span class="bule_bg mr_5">104건</span><span class="orange_bg mr_5">1100건</span><span class="green_bg">2건</span></p>
                                            
                                        </div>
                                        <div class="line_half mr_10">
                                            <p class="t_line3"><a href="#" class="modal_btn" name="doc_03">연신내역</a></p>
                                            <p class="bg_gray mt_5"><span class="num1">1226건</span><span class="keyword_m">더워요</span><span class="smile"></span></p>
                                            <p class="condition mt_5"><span class="bule_bg mr_5">104건</span><span class="orange_bg mr_5">1100건</span><span class="green_bg">2건</span></p>
                                            
                                        </div>
                                        <div class="line_half">
                                            <p class="t_line2"><a href="#" class="modal_btn" name="doc_03">강남역</a></p>
                                            <p class="bg_gray mt_5"><span class="num1">1226건</span><span class="keyword_m">더워요</span><span class="smile"></span></p>
                                            <p class="condition mt_5"><span class="bule_bg mr_5">104건</span><span class="orange_bg mr_5">1100건</span><span class="green_bg">2건</span></p>
                                            
                                        </div>
                                    </div>
                                
                                
                                <!--//노선별 긍부정-->
                                
                                
                                <!--역별 현황 상세보기 모달-->
                    
									<div class="modal_popup doc_view" id="basic-modal-doc_03">
										<div class="win win_full" style="margin-top:0">
											<div class="win_head clear2">
												<span class="win_tit bg_none">신도림역 상세보기</span>
											</div>
											
											
                                                <div class="win_contarea p_20">
                                                    <div class="rank_keyword">
                                                    <div class="rank_top">신도림역 키워드 Top 10</div>
                                                        <div class="rank_list">
                                                            <ul>
                                                            <li class="no_1"><span class="rank_no">1</span><a class="rank_key rank_mykey">열차 지연</a><span class="rank_right">500</span></li>
                                                            <li class="no_23"><span class="rank_no">2</span><a href="#" class="rank_key">불편 신고</a><span class="rank_right">300</span></li>
                                                            <li class="no_23"><span class="rank_no">3</span><a href="#" class="rank_key">더워요</a><span class="rank_right">250</span></li>
                                                            <li><span class="rank_no">4</span><a class="rank_key rank_mykey">환불</a><span class="rank_right">140</span></li>
                                                            <li><span class="rank_no">5</span><a href="#" class="rank_key">안내방송</a><span class="rank_right">123</span></li>
                                                            <li><span class="rank_no">6</span><a href="#" class="rank_key">유실물</a><span class="rank_right">93</span></li>
                                                            <li><span class="rank_no">7</span><a href="#" class="rank_key">와이파이</a><span class="rank_right">70</span></li>
                                                            <li><span class="rank_no">8</span><a href="#" class="rank_key">호객행위</a><span class="rank_right">68</span></li>
                                                            <li><span class="rank_no">9</span><a href="#" class="rank_key">불친절</a><span class="rank_right">40</span></li>
                                                            <li><span class="rank_no">10</span><a href="#" class="rank_key">성추행</a><span class="rank_right">3</span></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                <div class="chart_w01">긍/부정 비율 챠트 삽입 width:100% * height:430px</div>
											</div>	
                                            <div class="align_c mb_10 p_10"><a href="#" class="btn b_gray medium simplemodal-close">확인</a></div>								
										</div>
									</div>
									<hr />
					
                   		 <!--// 역별 현황 상세보기 모달-->
                           
                           </div>
                      </div>
					</div>

					<div class="clear2">
						<!-- 관심키워드 트렌드 분석 start -->
						<div class="win win_1 float_l mr_10">
							<div class="win_head clear2">
								<span class="win_tit bg_none">금주 키워드 TOP10</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help"></a>
									<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
							  <div class="graph_in" style="height:280px;">
                               	<div class="keywodrank_list">
										<ul>
										<li class="no_1"><span class="rank_no">1</span><a class="rank_key rank_mykey">열차 지연</a><span class="rank_right"><img src="images/common/bar1.png" alt="100%"></span><span class="count_no">45,000</span></li>
										<li class="no_2"><span class="rank_no">2</span><a href="#" class="rank_key">불편신고</a><span class="rank_right"><img src="images/common/bar1.png" alt="90%" style="width:90%; height:100%;"></span><span class="count_no">45,000</span></li>
										<li class="no_3"><span class="rank_no">3</span><a href="#" class="rank_key">더워요</a><span class="rank_right"><img src="images/common/bar1.png" alt="80%" style="width:80%; height:100%;"></span><span class="count_no">41,000</span></li>
										<li class="no"><span class="rank_no">4</span><a class="rank_key rank_mykey">환불</a><span class="rank_right"><img src="images/common/bar1.png" alt="70%" style="width:70%; height:100%;"></span><span class="count_no">40,030</span></li>
										<li class="no"><span class="rank_no">5</span><a href="#" class="rank_key">안내 방송</a><span class="rank_right"><img src="images/common/bar1.png" alt="60%" style="width:60%; height:100%;"></span><span class="count_no">31,000</span></li>
										<li class="no"><span class="rank_no">6</span><a href="#" class="rank_key">유실물</a><span class="rank_right"><img src="images/common/bar1.png" alt="55%" style="width:55%; height:100%;"></span><span class="count_no">25,000</span></li>
										<li class="no"><span class="rank_no">7</span><a href="#" class="rank_key">와이파이</a><span class="rank_right"><img src="images/common/bar1.png" alt="50%" style="width:50%; height:100%;"></span><span class="count_no">21,000</span></li>
										<li class="no"><span class="rank_no">8</span><a href="#" class="rank_key">호객행위</a><span class="rank_right"><img src="images/common/bar1.png" alt="45%" style="width:45%; height:100%;"></span><span class="count_no">18,290</span></li>
										<li class="no"><span class="rank_no">9</span><a href="#" class="rank_key">불친절</a><span class="rank_right"><img src="images/common/bar1.png" alt="20%" style="width:20%; height:100%;"></span><span class="count_no">1,030</span></li>
										<li class="no"><span class="rank_no">10</span><a href="#" class="rank_key">성추행</a><span class="rank_right"><img src="images/common/bar1.png" alt="10%" style="width:10%; height:100%;"></span><span class="count_no">450</span></li>
										</ul>
								</div>
                                
                                
                                </div>
							</div>
						</div>
			      <!--// 관심키워드 트렌드 분석 end -->

						<!-- 금주 카테고리 Top 10 start -->
						<div class="win win_1 float_l">
							<div class="win_head clear2">
								<span class="win_tit bg_none">금주 카테고리 Top 10</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help"></a>
									<div class="tooltip btn_qty1">금주 카테고리 Top 10을 확인할 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
								<div class="graph_in" style="height:280px;">
                                
                                <div class="keywodrank_list">
										<ul>
										<li class="no_1"><span class="rank_no">1</span><a class="rank_key rank_mykey">객실 냉방</a><span class="rank_right"><img src="images/common/bar1.png" alt="100%"></span><span class="count_no">45,000</span></li>
										<li class="no_2"><span class="rank_no">2</span><a href="#" class="rank_key">유실물 처리</a><span class="rank_right"><img src="images/common/bar1.png" alt="90%" style="width:90%; height:100%;"></span><span class="count_no">45,000</span></li>
										<li class="no_3"><span class="rank_no">3</span><a href="#" class="rank_key">정보통신</a><span class="rank_right"><img src="images/common/bar1.png" alt="80%" style="width:80%; height:100%;"></span><span class="count_no">41,000</span></li>
										<li class="no"><span class="rank_no">4</span><a class="rank_key rank_mykey">역사내 질서</a><span class="rank_right"><img src="images/common/bar1.png" alt="70%" style="width:70%; height:100%;"></span><span class="count_no">40,030</span></li>
										<li class="no"><span class="rank_no">5</span><a href="#" class="rank_key">공사 일반</a><span class="rank_right"><img src="images/common/bar1.png" alt="60%" style="width:60%; height:100%;"></span><span class="count_no">31,000</span></li>
										<li class="no"><span class="rank_no">6</span><a href="#" class="rank_key">열차 운행</a><span class="rank_right"><img src="images/common/bar1.png" alt="55%" style="width:55%; height:100%;"></span><span class="count_no">25,000</span></li>
										<li class="no"><span class="rank_no">7</span><a href="#" class="rank_key">역사 환경</a><span class="rank_right"><img src="images/common/bar1.png" alt="50%" style="width:50%; height:100%;"></span><span class="count_no">21,000</span></li>
										<li class="no"><span class="rank_no">8</span><a href="#" class="rank_key">열차 지연</a><span class="rank_right"><img src="images/common/bar1.png" alt="45%" style="width:45%; height:100%;"></span><span class="count_no">18,290</span></li>
										<li class="no"><span class="rank_no">9</span><a href="#" class="rank_key">안내방송</a><span class="rank_right"><img src="images/common/bar1.png" alt="20%" style="width:20%; height:100%;"></span><span class="count_no">1,030</span></li>
										<li class="no"><span class="rank_no">10</span><a href="#" class="rank_key">상가운영</a><span class="rank_right"><img src="images/common/bar1.png" alt="10%" style="width:10%; height:100%;"></span><span class="count_no">450</span></li>
										</ul>
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
								<li class="right_help">
									<a href="#" class="icon_help"></a>
									<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
								<div class="graph_in" style="height:285px;">
                                    <div class="cloud_opt clear2">
                                        <ul class="cloud_left clear2">
                                            <li><a href="#" class="btn b_gray small">Issue Cloud #1</a></li>
                                            <li><a href="#" class="btn b_gray small">Issue Cloud #2</a></li>
                                        </ul>
                                        <ul class="cloud_right clear2">
                                            <li><a href="#" class="cloud_check type01" id="cloud_01" title="콜센터"></a><label for="cloud_01">콜센터</label></li>
                                            <li><a href="#" class="cloud_check type02" id="cloud_02" title="VOC"></a><label for="cloud_02">VOC</label></li>
                                            <li><a href="#" class="cloud_check type03" id="cloud_03" title="소셜"></a><label for="cloud_03">소셜</label></li>
                                        </ul>
                                    </div>
                                    
                                    <div class="mt_10">클라우드 삽입 (100% x 290px)<br /><br /><br /><br /><a href="#" class="modal_btn" name="cloud_view">모달보기</a></div>


								<!-- 모달팝업 : 이슈키워드 상세보기 start -->
								<div class="modal_popup w700px" id="basic-modal-cloud_view" />
									<div class="win win_full" style="margin-top:0">
										<div class="win_head clear2">
											<span class="win_tit bg_none">이슈키워드 상세보기</span>
										</div>
										
										<div class="win_contarea clear2 cloud_modal">
											<div class="align_c" style="height:350px;">그래프 삽입 (100% x 350px)</div>
											<table class="tbl_type04" summary="키워드, 전주/금주 건수, 점유율로 구성된 이슈키워드 정보">
												<caption>이슈키워드 정보</caption>
												<thead>
													<tr>
														<th scope="col">키워드</th>
														<th scope="col">전주 건수</th>
														<th scope="col">금주 건수</th>
														<th scope="col">해당 유형 내 점유율</th>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td>챠트수정</td>
														<td>87건</td>
														<td>25건</td>
														<td>15.6%</td>
													</tr>
												</tbody>

											</table>
										</div>
									</div>
								</div>
								<hr />
								<!--// 모달팝업 : 이슈키워드 상세보기 end -->

                                </div>
							</div>
						</div>
						<!--/	<!--//이슈 클라우드 end -->


					

					


							</div>
						
						</div>
						<!--// 지점별 주요 키워드 분석 end -->
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