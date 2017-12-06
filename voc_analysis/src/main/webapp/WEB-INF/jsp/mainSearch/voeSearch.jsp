<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/voc/voeSearch.js'/>"></script>
<script type="text/javascript">
function getContextPath() {
	return "<c:out value="${pageContext.request.contextPath}" />";
}

function getImgPath(){
	return "<c:url value='/resources/images/common/'/>";
}

<c:if test="${not empty searchResultChart}">

$(function () {
	 $('#voeTrend').highcharts({
		chart: {
			zoomType: 'xy'
		},
		title: {
			text: null
		},
		subtitle: {
			text: null
		},
		credits:{
			enabled:false
		},
		xAxis: [{
			categories: ${searchResultChart.categories}
		}],
		yAxis: {
			min: 0,
			title: {
				text: '빈도수'
			}
		},
		plotOptions: {
			series: {
				pointWidth: 25
			}
		},
		tooltip: {
			shared: true
		},
		series: ${searchResultChart.series}
	});
});
</c:if>

</script>

<!-- location start -->
<div class="loc_wrap clear2">
	<ul>
		<li>홈</li>
		<li>키워드검색</li>
		<li>VOE</li>
	</ul>
</div>
<!--// location end -->

<!-- 본문 start -->
<div class="cont_body" id="cont_body">

	<div class="cont_head">
		<span class="cont_tit">키워드검색</span><span class="cont_desc">통합 VOC의 등록된 키워드에 대한 검색 기능을 제공합니다.</span>
	</div>

	<!-- main tab start -->
	<div class="main_tab clear2">
		<ul>
		<li><a href="#" onclick="fnMove();">VOC</a></li>
		<li class="on"><a href="#">VOE</a></li>
		</ul>
	</div>
	<!--// main tab end -->

	<!-- 검색조건 start -->
	<form method="post" id="searchForm" name="searchForm" action='<c:out value="${pageContext.request.contextPath}" />/mainSearch/voeSearch.do'>
		<input type="hidden" name="searchTerm" id="term" value="<c:out value="${not empty searchVo.searchTerm ? searchVo.searchTerm : ''}" />" />
		<input type="hidden" name="shareTerm" id="shareTerm" value="<c:out value="${not empty searchVo.shareTerm ? searchVo.shareTerm : ''}" />" />
		<input type="hidden" name="currentPageNo" id="currentPageNo" value="<c:out value="${not empty searchVo.currentPageNo ? searchVo.currentPageNo : ''}" />" />
		<input type="hidden" name="pageSize" id="pageSize" value="<c:out value="${not empty searchVo.pageSize ? searchVo.pageSize : ''}" />" />
		<input type="hidden" name="needsType" id="needsType" value='<c:out value="${not empty searchVo.needsType ? searchVo.needsType : ''}" />' />
	<div class="win win_full">
	
		<div class="win_contarea no_head">

			<p class="nowin_tit">검색조건</p>

			<fieldset><legend>검색조건</legend>
			<div class="box_blue">

				<ul class="terms_list type06">
					<li>
						<span><label for="select01"><font color="red">＊</font>조회구분</label></span>
						<select id="calendar_select" title="조회구분 선택" name="condition">
							<option value="DAY" <c:out value="${not empty searchVo.condition && searchVo.condition eq 'DAY' ? 'selected' : ''}" />>일별</option>
							<option value="WEEK" <c:out value="${not empty searchVo.condition && searchVo.condition eq 'WEEK' ? 'selected' : ''}" />>주별</option>
							<option value="MONTH" <c:out value="${not empty searchVo.condition && searchVo.condition eq 'MONTH' ? 'selected' : ''}" />>월별</option>
							<option value="QUARTER" <c:out value="${not empty searchVo.condition && searchVo.condition eq 'QUARTER' ? 'selected' : ''}" />>분기별</option>
							<option value="HALF" <c:out value="${not empty searchVo.condition && searchVo.condition eq 'HALF' ? 'selected' : ''}" />>반기별</option>
							<option value="YEAR" <c:out value="${not empty searchVo.condition && searchVo.condition eq 'YEAR' ? 'selected' : ''}" />>년별</option>
						</select>
					</li>
					<li class="terms_w_auto">
						<span><label for="select02"><font color="red">＊</font>분석기간</label></span>
						<ul>
						<li>
							<div class="add_date2">
								<input type="text" class="w100px date_time gray" title="시작날짜 입력" id="startDate" name="startDate" value='<c:out value="${not empty searchVo.startDate ? searchVo.startDate : ''}" />' /> 부터&nbsp;
								<input type="text" class="w100px date_time gray" title="마지막날짜 입력" id="endDate" name="endDate" value='<c:out value="${not empty searchVo.endDate ? searchVo.endDate : ''}" />' /> 까지
							</div>
						</li>
						</ul>
					</li>
					<li>
						<span><label for="select03">VOE 대분류</label></span>
						<select id="lcls" title="VOE 대분류 선택" name="lcls">
							<option value='all'>전체</option>
							<c:forEach var="result" items="${userLCLSType}" varStatus="status">
								<option value='<c:out value="${result.lcls}"/>' <c:out value="${not empty searchVo.lcls && searchVo.lcls eq result.lcls ? 'selected' : ''}" />><c:out value="${result.cnslCatNm}"/></option>
							</c:forEach>
						</select>
					</li>
					<li>
						<span><label for="select05">제외어</label></span>
						<input type="text" title="제외어 입력" id="exclusion" class="white w100p" name="exclusion" value='<c:out value="${not empty searchVo.exclusion ? searchVo.exclusion : ''}" />' />
					</li>
				</ul>
				<ul class="terms_list" style="padding-top: 5px">
					<li>
						<span>
							<label id="selectText" for="select01" style="color: black; white-space: nowrap;">
								<font color="red">'일별'</font>은 시작일부터 일주일만 설정 가능합니다.
							</label>
						</span>
					</li>
				</ul>
			</div>

			<div class="align_r mt_10">
				<a href="#" class="btn b_orange medium" id="btnSearch">확인</a>
				<a href="#" class="btn b_gray medium" id="btnClear">초기화</a>
			</div>
			</fieldset>

		</div>
	
	</div>
	</form>
	<!--// 검색조건 end -->

	<!-- 검색키워드 트렌드 분석 start -->
	<div class="win win_full">
		<div class="win_head clear2">
			<span class="win_tit bg_none">검색키워드 트렌드 분석</span>
			<ul class="win_btnset">
			<li><a href="#" class="btn_sh" title="접기"></a></li>
			</ul>
		</div>
		<div class="win_contarea align_c">
			<div class="graph_in" id="voeTrend" style="height:400px;">
			</div>
		</div>
	</div>
	<!--// 검색키워드 트렌드 분석 end -->

	<!-- VOE 검색결과 start -->
	<div class="win win_full" id="searchResultList">
		<jsp:include page='/WEB-INF/jsp/common/searchResultList.jsp' flush="false"></jsp:include>
	</div>
	<!--// VOE 검색결과 end -->

</div>
<!--// 본문 end -->

<!-- 상세페이지 팝업레이어 -->
<div class="modal_popup result_view" id="basic-modal-detail">
</div>
<!-- 유사문서 팝업레이어 -->
<div class="modal_popup doc_view" id="basic-modal-alike">
</div>
