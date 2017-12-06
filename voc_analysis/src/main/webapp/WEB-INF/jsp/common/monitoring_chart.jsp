<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<c:if test="${not empty chartData.categories}">
	
	<script type="text/javascript">
	$(function () {
		var zoneId = <c:out value="${targetDiv}"/>;
		var chartZone = $(document).find(zoneId);
		// 1000자리마다 콤마
	    Highcharts.setOptions({
			lang: {
				thousandsSep: ','
			}
		});
	    
	    $(chartZone).highcharts({
	        chart: {
	        	type: 'pie',
	            options3d: {
	                enabled: true,
	                alpha: 45,
	                beta: 0
	            }
	        },
	        title: {
	            text: '<c:out value="${title}"/>'
	        },
	        // highchart 로고 안 보이게
			credits:{
				enabled:false
			},
	        tooltip: {
	            pointFormat: '<b>{point.y}</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                depth: 35,
	                dataLabels: {
	                    enabled: true,
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                    style: {
	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                    }
	                },
                    showInLegend: true
	            }
	        },
	        series: ${chartData.series}
	    });
	});
</script>

</c:if>