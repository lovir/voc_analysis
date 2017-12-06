<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/jQCloud.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jQCloud.css'/>" />
<script type="text/javascript" src="<c:url value='/resources/js/voc/userSearch.js'/>"></script>
<script type="text/javascript">

var words = <c:out value='${searchResultChart.word}' escapeXml='false'/>;

function getContextPath() {
	return "<c:out value="${pageContext.request.contextPath}" />";
}

function getImgPath(){
	return "<c:url value='/resources/images/common/'/>";
}

<c:if test="${not empty searchResultChart}">

$(function () {
	 $('#userLine').highcharts({
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
	 
	$('#tags').jQCloud(words, {
		height: 290,
		shape: 'elliptic'
	});
});
</c:if>

</script>

<!-- location start -->
<div class="loc_wrap clear2">
	<ul>
	<li>홈</li>
	<li>고객명검색</li>
	</ul>
</div>
<!--// location end -->

<!-- 본문 start -->
<div class="cont_body" id="cont_body">

	<div class="cont_head">
		<span class="cont_tit">고객명검색</span><span class="cont_desc">고객명에 따른 불만지수분석, 키워드 클라우드, VOC검색결과를 제공합니다.</span>
	</div>

	<!-- 검색조건 start -->
	<form method="post" id="searchForm" name="searchForm" action='<c:out value="${pageContext.request.contextPath}" />/mainSearch/userSearch.do'>
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

				<ul class="terms_list">
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
						<span><label for="select03">VOC 니즈유형</label></span>
						<select id="needs" title="VOC 니즈유형 선택" name="needs">
							<option value='all'>전체</option>
							<c:forEach var="result" items="${needsTypeList}" varStatus="status">
								<option value='<c:out value="${result.cdTp}"/>' <c:out value="${not empty searchVo.needs && searchVo.needs eq result.cdTp ? 'selected' : ''}" />><c:out value="${result.cdKnm}"/></option>
							</c:forEach>
						</select>
					</li>
					<li>
						<span><label for="select03">고객ID</label></span>
						<select id="userId" title="고객ID 선택" name="userId">
							<option value='all'>전체</option>
							<c:forEach var="userId" items="${searchResultList.userIdResult}" varStatus="status">
								<option value='<c:out value="${userId}"/>' <c:out value="${not empty searchVo.userId && searchVo.userId eq userId ? 'selected' : ''}" />><c:out value="${userId}"/></option>
							</c:forEach>
						</select>
					</li>
					<li>
						<span><label for="select05">제외어</label></span>
						<input type="text" title="제외어 입력" id="exclusion" name="exclusion" class="white w100p" value='<c:out value="${not empty searchVo.exclusion ? searchVo.exclusion : ''}" />' />
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

	<div class="clear2">
		<!-- 불만지수 분석 start -->
		<div class="win win_1 float_l">
			<div class="win_head clear2">
				<span class="win_tit bg_none">불만지수 분석</span>
				<ul class="win_btnset">
					<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			<div class="win_contarea p_0 align_c" style="height:320px;">
				<ul class="complain com_search clear2">
					<li class="complain_01">
						<div>불만문서 / 총문서 건수</div>
						<p style="font-size: 26px"><c:out value="${not empty searchResultChart ? searchResultChart.complainSize : '0'}"/> / <c:out value="${not empty searchResultChart ? searchResultChart.totalSize : '0'}"/> 건</p>
					</li>
					<li class="complain_02">
						<div>불만지수</div>
						<p><c:out value="${not empty searchResultChart ? searchResultChart.percentage : '0'}"/>%</p>
					</li>
				</ul>
			</div>
		</div>
		<!--// 불만지수 분석 end -->

		<!-- 불만문서 트렌드 분석 start -->
		<div class="win win_2 float_r">
			<div class="win_head clear2">
				<span class="win_tit bg_none">불만문서 트렌드 분석</span>
				<ul class="win_btnset">
				<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea align_c">
				<div class="graph_in" id="userLine" style="height:300px;">
				</div>
			</div>
		</div>
		<!--// 불만문서 트렌드 분석 end -->

	</div>

	<!-- 키워드 클라우드 start -->
	<div class="win win_full">
		<div class="win_head clear2">
			<span class="win_tit bg_none">키워드 클라우드</span>
			<ul class="win_btnset">
			<li><a href="#" class="btn_sh" title="접기"></a></li>
			</ul>
		</div>
		
		<div class="win_contarea align_c">
			<div class="graph_in" id="tags" style="height:400px;">
			</div>
		</div>
	</div>
	<!--// 키워드 클라우드 end -->

	<!-- VOC 검색결과 start -->
	<div class="win win_full" id="searchResultList">
		<jsp:include page='/WEB-INF/jsp/common/searchResultList.jsp' flush="false"></jsp:include>
	</div>
	<!--// VOC 검색결과 end -->

</div>
<!--// 본문 end -->

<!-- 상세페이지 팝업레이어 -->
<div class="modal_popup result_view" id="basic-modal-detail">
</div>
<!-- 유사문서 팝업레이어 -->
<div class="modal_popup doc_view" id="basic-modal-alike">
</div>
