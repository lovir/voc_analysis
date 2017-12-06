<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript" src="<c:url value='/resources/js/voc/fieldNl.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>

<link rel="stylesheet" href="<c:url value='/resources/css/player/content.css'/>" />
<link rel="stylesheet" href="<c:url value='/resources/css/player/jquery.selectbox.css'/>" />
<link rel="stylesheet" href="<c:url value='/resources/css/player/jquery.mCustomScrollbar.css'/>" />
<script type="text/javascript" src="<c:url value='/resources/js/player/plugins/jquery.selectbox-0.6.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/player/plugins/jquery.mCustomScrollbar.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/Base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/pager.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>

<script type="text/javascript">
function getContextPath() {
    return "<c:out value="${pageContext.request.contextPath}" />";
}

$(function () {
	$( window ).load(function() {
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/keywordRanking/selectOptionList.do",
			dataType: 'json', 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#minwonTypeList').html(selectList(response.minwonTypeList));//성격유형
				
				$('#minwonTypeList > select').attr("name","minwonTypeList");

			}
		});
	});
});
</script>

				<!-- location start -->
				<div class="loc_wrap clear2">
					<ul>
					<li>홈</li>
					<li>VOC 분석</li>
					<li>분야현황분석</li>
					</ul>
				</div>
				<!--// location end -->

				<!-- 본문 start -->
				<!-- 본문 start -->
				<div class="cont_body" id="cont_body">
					<div class="cont_head">
						<span class="cont_tit">분야현황분석</span><span class="cont_desc">민원정보를 분야별로 분석할 수 있는 기능을 제공합니다. </span>
					</div>
					<!-- 분석조건 start -->
					
					<form id="searchForm" name="searchForm" method="post">    
						<input type="hidden" id="rankingIndex" name="rankingIndex" value="0"/>
						<input type="hidden" name="pageType" value="interest"/>
						<input type="hidden" name="keyword" value="A" /> 
						<input type="hidden" name="needsType" /> 
						<input type="hidden" name="pageSize" value="10"/> 
						<input type="hidden" name="currentPage" value="1"/>
						<input type="hidden" name="condition"/>  
					
						<div class="win win_full">
						
							<div class="win_contarea no_head">
	
								<p class="nowin_tit">분석조건</p>
	
								<fieldset><legend>분석조건</legend>
									<div class="box_blue">
		
										<ul class="terms_list">
											<!-- <li>
												<span><label for="select01"><font color="red">＊</font>조회구분</label></span>
												<select id="calendar_select" title="조회구분 선택">
													<option value="DAY" >일별</option>
													<option value="WEEK" >주별</option>
													<option value="MONTH" >월별</option>
													<option value="QUARTER" >분기별</option>
													<option value="HALF" >반기별</option>
													<option value="YEAR" >년별</option>
												</select>
											</li> -->
											<li class="terms_w_auto">
												<span><label for="select02"><font color="red">＊</font>분석기간</label></span>
												<ul>
													<div class="add_date2">
														<input type="text" class="w100px date_time gray" title="시작날짜 입력" id="startDate" name="startDate">
														&nbsp;&nbsp;
														<input type="text" class="w100px date_time gray" title="마지막날짜 입력" id="endDate" name="endDate">
													</div>
												</ul>
											</li>
											<li>
												<span><label for="select03">VOC 민원유형</label></span>
												<span id="minwonTypeList"></span>
											</li>
										</ul>
										<!-- <ul class="terms_list" style="padding-top: 5px">
											<li>
												<span>
													<label id="selectText" for="select01" style="color: black; white-space: nowrap;">
														<font color="red">'일별'</font>은 시작일부터 일주일 설정만 가능합니다.
													</label>
												</span>
											</li>
										</ul> -->
									</div>

									<div class="align_r mt_10">
										<a href="#" class="btn b_orange medium btn_rank_search">확인</a>
										<a id="searchReset" href="#" class="btn b_gray medium">초기화</a>
									</div>
								</fieldset>
							</div>
						</div>
						<!--// 분석조건 end -->
					</form>

					<!-- 관심키워드 리포트 start -->
					<div class="win win_full">
						<div class="win_head clear2">
							<span class="win_tit bg_none">분야별 현황</span>
							<ul class="win_btnset">
							<li><a href="#" class="btn_sh" title="접기"></a></li>
							</ul>
						</div>
						<div class="win_contarea align_c">
							<div class="graph_in">
								<div id="reportChart"></div>
							</div>
						</div>
					</div>
					<!--// 관심키워드 리포트 end -->
					<!-- VOC 검색결과 start -->
					<div class="win win_full" id="search_result">
						<div class="win_head clear2">
							<span class="win_tit bg_none">검색결과</span>
							<ul class="win_btnset">
							<li><a href="javascript:;" class="btn_sh" title="접기"></a></li>
							</ul>
						</div>
						<div id="vocSearchTeam" class="win_contarea align_c">
							<div class="graph_in"></div>
						</div>
					</div>
					<!-- VOC 검색결과 끝 -->
				</div>
				<!--// 본문 end -->
			
				
				<!-- 상세페이지 팝업레이어 -->
			    <div class="modal_popup result_view" id="basic-modal-detail" />
			    </div>
			    <!-- 유사문서 팝업레이어 -->
			    <div class="modal_popup doc_view" id="basic-modal-alike" />
			    </div>
<!-- 음성민원 상세보기 modalpopup start-->
<div class="modal_popup w980px" id="basic-modal-voice_info"></div>
