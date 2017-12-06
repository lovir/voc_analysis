<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">

<c:if test="${not empty searchResultChart}">

$(function () {
	 $('#trendAnalysisLine${param.compare}').highcharts({
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

<!-- 트렌드 분석 start -->
<div class="win win_full">
	<div class="win_head clear2">
		<span class="win_tit bg_none">트렌드 분석 <c:out value="${param.compare}" /></span>
		<ul class="win_btnset">
		<li><a href="#" class="btn_sh" title="접기"></a></li>
		</ul>
	</div>
	
	<div class="win_contarealign_c">
		<c:if test="${not empty searchResultChart}">
			<div id="trendAnalysisLine<c:out value="${param.compare}" />" class="graph_in" style="height:400px;">
			</div>
		</c:if>
	</div>

</div>
<!--// 트렌드 분석 end -->

<!-- 트렌드 분석 랭킹 start -->
<div class="win win_full">
	<div class="win_head clear2">
		<span class="win_tit bg_none">트렌드 분석 랭킹 <c:out value="${param.compare}" /></span>
		<ul class="win_btnset">
		<li><a href="#" class="btn_sh" title="접기"></a></li>
		</ul>
	</div>
	<jsp:include page='/WEB-INF/jsp/common/searchResultRanking.jsp' flush="false"></jsp:include>

</div>
<!--// 트렌드 분석 랭킹 end -->

<!-- VOC 검색결과 start -->
<div class="win win_full" id="searchResultList<c:out value="${param.compare}" />">
	<jsp:include page='/WEB-INF/jsp/common/searchResultList.jsp' flush="false"></jsp:include>
</div>
<!--// VOC 검색결과 end -->