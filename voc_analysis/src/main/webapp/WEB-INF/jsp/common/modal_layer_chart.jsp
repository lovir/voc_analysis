<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">

$(function () {
	
	<c:if test="${not empty issueViewResult}" >
		// 이슈 키워드 동향 분석
		$('#issueChart').highcharts({ 
			chart: {
				type: 'line'
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
			xAxis: {
				categories: <c:out value="${issueViewResult.categories}" escapeXml="false"/>
			},
			yAxis: {
				title: {
					text: '빈도수'
				},
				min:0
			},
			series: <c:out value="${issueViewResult.series}" escapeXml="false"/>
		});
	</c:if>
	
	<c:if test="${not empty alarmViewResult}" >
	// 알람 모니터링 키워드 동향 분석
	$('#alarmChart').highcharts({ 
		chart: {
			type: 'line'
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
		xAxis: {
			categories: <c:out value="${alarmViewResult.categories}" escapeXml="false"/>
		},
		yAxis: {
			title: {
				text: '빈도수'
			},
			min:0
		},
		series: <c:out value="${alarmViewResult.series}" escapeXml="false"/>
	});
	
	var chart = $('#alarmChart').highcharts();
	
	for ( var i = 0, len = chart.series.length; i < len; i++) {		
		var series = chart.series[i];
		//series.hide();
	}
	
	// 기간 선택
	$('input[name=period]:radio').change(function () {
		var	period = $('input:radio[name=period]:checked').val();
		var portal_id = $('input[name="portal_id"]').val();
		var portal_nm = $('input[name="portal_nm"]').val();
		var json = {"period":period, "regId":portal_id , "regNm":portal_nm};
		
		$("#basic-modal-util_01").load(getContextPath() + "/management/selectAlarmKeywordMonitoringList.do",
				json);
		return false;
	});
	
	// 키워드 선택
	$('input[name="keyword"]:checkbox').live('change', function(){
		var chart = $('#alarmChart').highcharts();
		
		for ( var i = 0, len = chart.series.length; i < len; i++) {		
			var series = chart.series[i];
			if($(this).attr('value') == series.name){
				if($(this).attr("checked")) {
					series.show();
				}else{
					series.hide();
				}
			}
		}
	});
	
	// 키워드 마우스 이벤트
	$('.alarm_list').on('click', function(){
		/* if (event.type == 'mouseover') {			  
			$("[name='" + $(this).find("input[name='keyword']").val() + "']").css("display", "block");
		}else{
			  $("[name='" + $(this).find("input[name='keyword']").val() + "']").css("display", "none");
		}	 */
		$('.alarm_bottom ul').css("display", "none");
		$("[name='" + $(this).find("input[name='keyword']").val() + "']").css("display", "block");
	});
	
	// 키워드 전체 선택
	$('#allKeyword:checkbox').change(function () {
		if($(this).attr("checked")){
			$("input[name='keyword']").prop("checked", true);
		}else{
			$("input[name='keyword']").prop("checked", false);
		}
		
		var chart = $('#alarmChart').highcharts();
		
		for ( var i = 0, len = chart.series.length; i < len; i++) {		
			var series = chart.series[i];
			if($(this).attr("checked")) {
				series.show();
			}else{
				series.hide();
			}
		}
	});
	</c:if>
});

</script>
	
	<c:if test="${not empty issueViewResult}" >
	<!-- 모달팝업 : 이슈키워드 상세보기 start -->
	<div id="issue" class="win win_full" style="margin-top:0">
		<div class="win_head clear2">
			<span class="win_tit bg_none">이슈키워드 상세보기</span>
		</div>
		
		<div class="win_contarea clear2 cloud_modal">
			<div class="align_c" style="height:350px;width:680px;" id="issueChart">
			</div>
			<table class="tbl_type04" summary="키워드, 전주/금주 건수, 점유율로 구성된 이슈키워드 정보">
				<caption>이슈키워드 정보</caption>
				<thead>
					<tr>
						<th scope="col">키워드</th>
						<th scope="col">${condition == 'WEEK' ? '전주' : '전월'} 건수</th>
						<th scope="col">${condition == 'WEEK' ? '금주' : '당월'} 건수</th>
						<th scope="col">점유율</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><c:out value="${issueViewResult.name}" /></td>
						<td><c:out value="${issueViewResult.prev}" />건</td>
						<td><c:out value="${issueViewResult.current}" />건</td>
						<td><c:out value="${issueViewResult.share}" />%</td>
					</tr>
				</tbody>

			</table>
		</div>
	</div>
	<!--// 모달팝업 : 이슈키워드 상세보기 end -->
	</c:if>

	<c:if test="${not empty alarmViewResult}" >
	<!-- 모달팝업 : 알람키워드 모니터링 start -->
	<div id="alarm" class="win win_full" style="margin-top:0">
		<div class="win_head clear2">
			<span class="win_tit bg_none">알람키워드 모니터링</span>
		</div>
		
		<div class="win_contarea clear2">

			<!-- 그래프 start -->
			<div class="alarm_left clear2">
				<fieldset><legend>기간 선택</legend>
					<ul class="radio_term clear2">
						<li><input type="radio" name="period" value="1" id="alarm_01" ${alarmViewResult.period == '1' ? 'checked="checked"' : ''} /><label for="alarm_01">지난 일주일</label></li>
						<li><input type="radio" name="period" value="2" id="alarm_02" ${alarmViewResult.period == '2' ? 'checked="checked"' : ''} /><label for="alarm_02">전날</label></li>
						<li><input type="radio" name="period" value="3" id="alarm_03" ${alarmViewResult.period == '3' ? 'checked="checked"' : ''} /><label for="alarm_03">오늘</label></li>
					</ul>
					<div class="p_10 align_c" style="width:450px; height:280px;" id="alarmChart"></div>
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
						<!-- <th scope="col" class="align_c"><input type="checkbox" id="allKeyword" title="전체 선택" /></th> -->
						<th scope="col" class="align_c"></th>
						<th scope="col">키워드</th>
						<th scope="col">VOC점유율</th>
						<th scope="col">평균건수</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="result" items="${alarmViewResult.resultList}" varStatus="status">
							<tr class="alarm_list">
								<%-- <td class="align_c"><input type="checkbox" name="keyword" value='<c:out value="${result.keyword}" />' /></td> --%>
								<td class="align_c"><input type="hidden" name="keyword" value='<c:out value="${result.keyword}" />' /></td>
								<%-- <td><span class="key_${status.count<10 ? '0' : ''}${status.count}"><c:out value="${result.keyword}" /></span></td> --%>
								<td><span class="key_03"><c:out value="${result.keyword}" /></span></td>
								<td><c:out value="${result.share}" /> %</td>
								<td class="av_r"><c:out value="${result.count}" /></td>
							</tr>
						</c:forEach>
					</tbody>
					</table>
				</fieldset>
			</div>
			<!--// 표 end -->

			<!-- 기본정보 start -->
			<div class="alarm_bottom">
				<c:set var="lv1" value="${alarm.level1}" />
				<c:set var="lv2" value="${alarm.level2}" />
				<c:set var="lv3" value="${alarm.level3}" />
				<c:forEach var="result" items="${alarmViewResult.alarmStateList}" varStatus="status">
					<ul name='<c:out value="${result.keyword}" />' style='display:none;'>
						<li>
							<span>키워드</span>
							<div><c:out value="${result.keyword}" /></div>
						</li>
						<li>
							<span>소분류</span>
							<div <c:if test="${fn:length(result.vocItemName) > 6 }"> style="font-size:15px" </c:if> ><c:out value="${result.vocItemName}"></c:out></div>
						</li>
						<li>
							<span>접수채널</span>
							<div><c:out value="${result.channelName}" /></div>
						</li>
					</ul>
				</c:forEach>
			</div>
			<!--// 기본정보 end -->
		</div>
	
	</div>
	<!--// 모달팝업 : 알람키워드 모니터링 end -->
	</c:if>