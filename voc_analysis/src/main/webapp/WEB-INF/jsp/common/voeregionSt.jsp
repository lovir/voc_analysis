<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/voeregionSt.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
<script type="text/javascript">
function getContextPath() {
    return "<c:out value="${pageContext.request.contextPath}" />";
}

$(function () {
	$( window ).load(function() {
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/voekeywordRanking/selectOptionList.do",
			dataType: 'json', 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#categoryTypeList').html(selectList(response.categoryTypeList));//니즈유형
				//$('#minwonTypeList').html(selectList(response.minwonTypeList));//성격유형
				$('#socialTypeList').html(selectList(response.socialTypeList));
				//$('#deptTypeList').html(selectList(response.deptTypeList));
				/* $('#r_chTypeList').html(selectList(response.r_chTypeList)); *///접수채널유형
				
				$('#categoryTypeList > select').attr("name","categoryTypeList");
				//$('#minwonTypeList > select').attr("name","minwonTypeList");
				$('#socialTypeList > select').attr("name","socialTypeList");
				//$('#deptTypeList > select').attr("name","deptTypeList");
				/* $('#r_chTypeList > select').attr("name","r_chTypeList"); */
				
			}
		});
	});
});

</script>

<!-- location start -->
<div class="loc_wrap clear2">
	<ul>
	<li>홈</li>
	<li>소셜 분석</li>
	<li>지역현황분석</li>
	</ul>
</div>
<!--// location end -->
				<!-- 본문 start -->
				<div class="cont_body" id="cont_body">
				
					<div class="cont_head">
						<span class="cont_tit"> 지역현황분석</span><span class="cont_desc">소셜정보를 지역별로 분석할 수 있는 기능을 제공합니다. </span>
					</div>


					<!-- 분석조건 start -->
					<div class="opt_box">
					
						<div class="no_head">
							<form id="searchForm" name="searchForm" method="post">
								<input type="hidden" id="rankingIndex" name="rankingIndex" value="0"/>
								<input type="hidden" name="pageType" value="region"/> 
								<input type="hidden" name="keyword" /> 
								<input type="hidden" name="pageSize" value="10"/> 
								<input type="hidden" name="currentPage" value="1"/> 
								<input type="hidden" name="condition"/>
								<input type="hidden" name="needsType"/>
							<p class="nowin_tit">분석조건</p>

							<fieldset><legend>분석조건</legend>
							<div class="box_blue">

								<ul class="terms_list type06">
<!-- 									<li>
										<span><label for="select01">조회구분</label></span>
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
													<input type="text" class="w100px date_time gray" title="시작날짜 입력" name="startDate" id="startDate">
													&nbsp;&nbsp;
													<input type="text" class="w100px date_time gray" title="마지막날짜 입력" name="endDate" id="endDate">
											</ul>
									</li>
									
									
									<li>
										<span>
										<label for="select03">소셜 카테고리유형</label>
										</span>
										<span id="categoryTypeList"></span>
									</li>
                                    
									<li>
										<span><label for="select04">소셜 유형</label></span>
										<span id="socialTypeList"></span>
									</li>
                                     <!-- <li>
										<span><label for="select06">부서</label></span>
										<span id="deptTypeList"></span>
									</li> -->
								</ul>
								<ul class="terms_list" style="padding-top: 5px">
									<li>
										<span>
											<!-- <label id="selectText" for="select01" style="color: black; white-space: nowrap;">
												<font color="red">'일별'</font>은 시작일부터 일주일 설정만 가능합니다.
											</label> -->
										</span>
									</li>
								</ul>
							</div>

							<div class="align_r mt_10">
							<a href="#" class="btn b_blue2 medium btn_rank_search">확인</a>
							<a href="#" class="btn b_gray medium" id="searchReset">초기화</a>
							</div>
							</fieldset>

							</form>
						</div>
					</div>
					<!--// 분석조건 end -->


					<!-- 종합랭킹 TOP10 리포트 start -->
					<div class="win win_full">
						<div class="win_head clear2">
							<span class="win_tit bg_none">지역별 현황</span>

							<ul class="win_btnset">
							<li><a href="#" class="btn_sh" title="접기"></a></li>
							</ul>
						</div>
						<div id="regionStart" class="win_contarea align_c">
                            <!-- <div class="graph_in" style="height:620px;"> -->
                            <!--인천광역시 지역별 현황-->
                             <!-- <div id="map_area"></div> -->
                            <!--인천광역시 지역별 현황-->
                            </div>
					  	</div>
					
					<!--// 종합랭킹 TOP10 리포트 end -->
					<!-- VOC 검색결과 start -->
					<div class="win win_full" id="search_result">
						<div class="win_head clear2">
							<span class="win_tit bg_none">검색결과</span>
							<ul class="win_btnset">
							<li><a href="javascript:;" class="btn_sh" title="접기"></a></li>
							</ul>
						</div>
						<div id="vocSearchTeam" class="win_contarea align_c">
						<div class="graph_in">
						</div>
					</div>
					</div>
					<!-- VOC 검색결과 끝 -->
					</div>
	
					
				<!-- 상세페이지 팝업레이어 -->
			    <div class="modal_popup result_view" id="basic-modal-detail" /></div>

			    <!-- 유사문서 팝업레이어 -->
			    <div class="modal_popup doc_view" id="basic-modal-alike" /></div>