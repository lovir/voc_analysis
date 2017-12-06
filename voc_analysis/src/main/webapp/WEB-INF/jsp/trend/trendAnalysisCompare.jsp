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
		<title>서울교통공사 : 트렌드 분석</title>
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
		<script type="text/javascript" src="<c:url value='/resources/js/voc/trendAnalysisCompare.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
	</head>	
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

		<!-- 초기화면 분석조건 로드 -->
		<script type="text/javascript">
		
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
						/////// 비교분석A
						$('#vocChannelListA').html(selectList(response.vocChannelList));	//민원 접수 채널
						$('#vocRecTypeListA').html(selectList(response.vocRecTypeList));	//민원 접수 구분
						$('#vocKindListA').html(selectList(response.vocKindList));	//대 카테고리(민원 유형)
						$('#vocPartListA').html(selectList(response.vocPartList));	//중 카테고리(민원 분야)
						$('#vocItemListA').html(selectList(response.vocItemList));	//소 카테도리(민원 세부항목)
						//$('#vocMetroDeptList').html(selectList(response.vocMetroDeptList));	//처리 주무부서
						
						$('#vocMetroDeptListA').html("<option value=\"all\">전체</option>");
						$('#vocChannelListA > select').find('option:first').attr('selected',true);
						$('#vocRecTypeListA > select').find('option:first').attr('selected',true);
						$('#vocKindListA > select').find('option:first').attr('selected',true);
						$('#vocPartListA > select').find('option:first').attr('selected',true);
						$('#vocItemListA > select').find('option:first').attr('selected',true);
						// 초기화면 - 대분류 / 중분류 / 소분류 비활성 
						$("#vocKindListA").attr("disabled", true);
						$("#vocPartListA").attr("disabled", true);
						$("#vocItemListA").attr("disabled", true);
						
						/////// 비교분석B
						$('#vocChannelListB').html(selectList(response.vocChannelList));	//민원 접수 채널
						$('#vocRecTypeListB').html(selectList(response.vocRecTypeList));	//민원 접수 구분
						$('#vocKindListB').html(selectList(response.vocKindList));	//대 카테고리(민원 유형)
						$('#vocPartListB').html(selectList(response.vocPartList));	//중 카테고리(민원 분야)
						$('#vocItemListB').html(selectList(response.vocItemList));	//소 카테도리(민원 세부항목)
						//$('#vocMetroDeptList').html(selectList(response.vocMetroDeptList));	//처리 주무부서
						
						$('#vocMetroDeptListB').html("<option value=\"all\">전체</option>");
						$('#vocChannelListB > select').find('option:first').attr('selected',true);
						$('#vocRecTypeListB > select').find('option:first').attr('selected',true);
						$('#vocKindListB > select').find('option:first').attr('selected',true);
						$('#vocPartListB > select').find('option:first').attr('selected',true);
						$('#vocItemListB > select').find('option:first').attr('selected',true);
						// 초기화면 - 대분류 / 중분류 / 소분류 비활성 
						$("#vocKindListB").attr("disabled", true);
						$("#vocPartListB").attr("disabled", true);
						$("#vocItemListB").attr("disabled", true);
					}
				});  
				
				$.ajax({
					type : "POST", 
					url : getContextPath() + "/trend/trendKeywordCompare.do",
					dataType: 'json', 
					contentType: 'application/json;charset=utf-8',
					mimeType: 'application/json',
					success : function(data) {			
						var str = "";
						//str += "<select id=\"selectList\">";
						str += "<option value=\"all\">직접입력</option>";
						str += "<optgroup label=\"관심키워드\">";
						if(data != null){
							for ( var i = 0, len = data.length; i < len; i++) {			
								
								str += "<option value='"+ data[i] +"'>"+ data[i] +"</option>";
							}
						}
						str += "</optgroup>";
						$('#selectKeywordA').html(str);	// 관심키워드 
						$('#selectKeywordB').html(str);	// 관심키워드 
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
				
					<div class="cont_head">
						<span class="cont_tit">트렌드 비교 분석</span><span class="cont_desc">주제어에 대한 연관정보 비교 분석결과를 제공합니다.</span>
					</div>

					<div class="clear2">

					<form id="searchForm" name="searchForm" method="post">	 
						<input type="hidden" name="pageType" value="trendAnalysisCompare"/> 
						<input type="hidden" name="pageSize" value="10"/> 
						<input type="hidden" name="currentPage" value="1"/> 
						<input type="hidden" name="totalSize" value="1"/> 
						<input type="hidden" name="condition" value="DAY"/> 	<!-- 조회구분 -->
						<input type="hidden" name="vocChannel" value="all"/> 	<!-- 선호채널 -->
						<input type="hidden" name="vocRecType" value="all"/> 	<!-- VOC종류 -->
						<input type="hidden" name="metroDept" value="all"/>		<!-- 처리부서 -->
						<input type="hidden" name="repLevel" value="all"/>		<!-- 만족도 -->
						<input type="hidden" name="vocKind" value="all"/>		<!-- 대분류 -->
						<input type="hidden" name="vocPart" value="all"/>		<!-- 중분류 -->
						<input type="hidden" name="vocItem" value="all"/>		<!-- 소분류 -->
						<input type="hidden" name="keyword" value=""/>			<!-- 선택한 검색키워드 -->
						<input type="hidden" id="rankingIndex" name="rankingIndex" value="0"/>
						<input type="hidden" id="exclusion" name=exclusion value=""/>	<!-- 제외한 검색키워드 -->
						<input type="hidden" id="startDate" name="startDate" value=""/>			<!-- 조회날짜 -->
						<input type="hidden" id="endDate" name="endDate" value=""/>				<!-- 조회날짜 -->
					</form>

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
											 
												
											<li class="terms_w_auto3 mt_20">
												<span><label for="vocChannelList">접수채널</label></span>
												
												<select id="vocChannelListA"></select>
											</li>
											<li class="mt_20">
												<span><label for="repLevelList">만족도 등급</label></span>
												<select id="repLevelListA" title="만족도 등급">
													<option value="all" selected>전체</option>
													<option value="1" >매우만족</option>
													<option value="2" >만족</option>
													<option value="3" >보통</option>
													<option value="4" >불만족</option>
													<option value="5" >매우불만족</option>
												</select>
											</li>
											<li class="mt_20">
												<span><label for="vocRecTypeList">VOC 종류</label></span>
												
												<select id="vocRecTypeListA"></select>
											</li>
											
											<li class="terms_w_auto2 mt_20">
												<span><label for="vocKindList">접수종류</label></span>
												<select id="vocKindListA" ></select>
												<select id="vocPartListA" ></select>
												<select id="vocItemListA" ></select>
												
												<!-- <span id="vocKindList" ></span>
												<span id="vocPartList" ></span>
												<span id="vocItemList" ></span> -->
											</li>
											
											<li class="terms_w_auto2 mt_20">
												<span><label for="cdDeptList">처리주무부서</label></span>										
												<select id="vocMetroDeptListA" title="처리주무부서">
												</select>									
											</li>		
										</ul>
										
										<hr class="terms_line">
										<ul class="terms_list type05">
											<li class="terms_w_auto2">
												<span><label for="select08">주제어 입력</label></span>
                                                <select id="selectKeywordA" class="selectKeyword" title="주제어 선택">								
												</select>
												<input type="text" id="searchTermA" name = "searchTermA"  class="white w150p" title="주제어 입력">
											</li>
											<li class="terms_w_auto2 mb_5 mt_20" style="margin-right: 0px;">
												<span><label for="select08">제외어 입력</label></span>
												<input type="text" id="excTermA" name="excTermA"  class="white w300px mb_5" title="제외어 입력">
												<br>제외키워드는 콤마 ( , )로 구분됩니다.
											</li>
										</ul>
									</div>

									<div class="align_r mt_10"><a href="#" id="searchStartA" class="btn b_blue2 medium">확인</a>
									<a href="#" id="searchResetA" class="btn b_gray medium">초기화</a></div>
									</fieldset>

								</div>
							
							</div>
							<!--// 분석조건 A end -->

							<!-- 제안키워드 트렌드 분석 A start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none"> 트렌드 분석결과A</span>

								  <ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
						  		<div class="win_contarea align_c">
                                	<div id="lineChartA" class="p_15 clear2"></div>
                            	</div>	
							
							</div>
							<!--// 제안키워드 트렌드 분석 A end -->


							<!-- 제안 키워드 트렌드 분석 랭킹 A start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">트렌드 분석 랭킹 A</span>

								  <ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
								  <div class="win_contarea">
									<div class="rank_top_area compare">
										<div class="win_contarea" id="total_RankA" ></div>
									</div>
								  </div>
							
							</div>
							<!--// 제안 키워드 트렌드 분석 랭킹 A end -->

							<!-- VOC 검색결과 A start -->
							<div class="win win_full" id="search_resultA">
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
							<!--// VOC 검색결과 A end -->

						</div>
						<!--// left contents end -->




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
											<li class="terms_w_auto3 mt_20">
												<span><label for="vocChannelList">접수채널</label></span>
												
												<select id="vocChannelListB"></select>
											</li>	
																						
											<li class="mt_20">
												<span><label for="repLevelList">만족도 등급</label></span>
												<select id="repLevelListB" title="만족도 등급">
													<option value="all" selected>전체</option>
													<option value="1" >매우만족</option>
													<option value="2" >만족</option>
													<option value="3" >보통</option>
													<option value="4" >불만족</option>
													<option value="5" >매우불만족</option>
												</select>
											</li>
											
											<li class="mt_20">
												<span><label for="vocRecTypeList">VOC 종류</label></span>
												
												<select id="vocRecTypeListB"></select>
											</li>
											
											<li class="terms_w_auto2 mt_20">
												<span><label for="vocKindList">접수종류</label></span>
												<select id="vocKindListB" ></select>
												<select id="vocPartListB" ></select>
												<select id="vocItemListB" ></select>
												
												<!-- <span id="vocKindList" ></span>
												<span id="vocPartList" ></span>
												<span id="vocItemList" ></span> -->
											</li>
											
											<li class="terms_w_auto2 mt_20">
												<span><label for="cdDeptList">처리주무부서</label></span>										
												<select id="vocMetroDeptListB" title="처리주무부서">
												</select>									
											</li>
										</ul>	
										
										<hr class="terms_line">
										<ul class="terms_list type05">
											<li class="terms_w_auto2">
												<span><label for="select08">주제어 입력</label></span>
                                                <select id="selectKeywordB" class="selectKeyword" title="주제어 선택">								
												</select>
												<input type="text" id="searchTermB" name = "searchTermB"  class="white w150p" title="주제어 입력">
											</li>
											<li class="terms_w_auto2 mb_5 mt_20" style="margin-right: 0px;">
												<span><label for="select08">제외어 입력</label></span>
												<input type="text" id="excTermB" name="excTermB"  class="white w300px mb_5" title="제외어 입력">
												<br>제외키워드는 콤마 ( , )로 구분됩니다.
											</li>
										</ul>
									</div>

									<div class="align_r mt_10"><a href="#" id="searchStartB" class="btn b_blue2 medium">확인</a>
									<a href="#" id="searchResetB" class="btn b_gray medium">초기화</a></div>
									</fieldset>

								</div>
							
							</div>
							<!--// 분석조건 B end -->

							<!--  제안키워드 트렌드 분석 B start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none"> 트렌드 분석결과B</span>

								  <ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
						  		<div class="win_contarea align_c">
                                	<div id="lineChartB" class="p_15 clear2"></div>
                            	</div>	
							
							</div>
							<!--// 연관도 분석 B end -->



							<!-- 제안 키워드 트렌드 분석 랭킹 B start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">트렌드 분석 랭킹 B</span>

								  <ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
						 		 <div class="win_contarea">
									<div class="rank_top_area compare">
										<div class="win_contarea" id="total_RankB" ></div>
									</div>
								 </div>
							</div>
							<!--// 제안 키워드 트렌드 분석 랭킹 B end -->

							<!-- VOE 검색결과 B start -->
							<div class="win win_full" id="search_resultB">
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
							<!--// VOE 검색결과 B end -->
						</div>
						<!--// right contents end -->


						<!-- 상세페이지 팝업레이어 -->
						<div class="modal_popup result_view" id="basic-modal-detail" >
						</div>
						<!-- 유사문서 팝업레이어 -->
						<div class="modal_popup doc_view" id="basic-modal-alike" >
						</div>

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
		