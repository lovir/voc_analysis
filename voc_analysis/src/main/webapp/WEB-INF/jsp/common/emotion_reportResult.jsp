<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<p class="title mb_10" id="reportTitle">카테고리 분포도</p>
	<div id="reportChart"></div>



<c:if test="${not empty issueRanking.categories}">
	
	<script type="text/javascript">
	$(function () {
		$('#reportChart').highcharts({
			 chart: {
					zoomType: 'xy',
					height: '520'
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
					categories: ${issueRanking.categories}
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
				series: ${issueRanking.series}
		});
		
	});
</script>

</c:if>