<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<c:if test="${not empty issueRanking.categories}">
	
	<script type="text/javascript">
	$(function () {
		$('#reportChart').highcharts({
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
				legend:{
					enabled:false
				},
				xAxis: [{
					categories: ${issueRanking.categories}
				}],
				yAxis: {
					min: 0,
					title: {
						text: '민원빈도'
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

<input type="hidden" name="keywordTemp" id="keywordTemp" value="<c:out value="${keyword}"/>" />
	<div class="rank_top_area" id="rank_top_area">
		<ul class="rank_group full">
			<c:choose>
				<c:when test="${not empty issueRanking.periodKeyword}">
					<c:forEach var="resultMap" items="${issueRanking.periodKeyword}" varStatus="status">
						<li id="<c:out value='${resultMap.key}'/>">
							<div class="rank_top"><c:out value="${resultMap.key}"/></div>
							<div class="rank_list">
								<ul>
									<c:if test="${empty resultMap.value}">
										<li >키워드 없음</li>
									</c:if>
									<c:forEach var="result" items="${resultMap.value}" varStatus="status">
										<c:choose>
											<c:when test="${result.count == 1}"><li class="no_1"></c:when>
											<c:when test="${result.count == 2 || result.count == 3}"><li class="no_23"></c:when>
											<c:otherwise><li></c:otherwise>
										</c:choose>
										<span class="rank_no"><c:out value="${result.count}"/></span>
										<a class="rank_key <c:if test="${'Y' eq result.interestYn}">rank_mykey</c:if>" href="javascript:;" name="<c:out value="${result.keyword}"/>" >
											<c:choose>
												<c:when test="${fn:length(result.keyword)>7}">
													<c:out value="${fn:substring(result.keyword,0,6)}"/>...								
												</c:when>
												<c:otherwise>
													<c:out value="${result.keyword}"/>								
												</c:otherwise>
											</c:choose>
											<c:if test="${'Y' eq pageType}">
												<c:out value="(${result.nowrank})"/>
											</c:if>
										</a>
										<span class="rank_right">
											<c:choose>
												<c:when test="${'up' eq result.imagePath}">
													<c:out value="${result.change}"/>
													<img src="<c:url value='/resources/images/common/rank_arrow_up.gif'/>" alt="순위 상승" />
												</c:when>
												<c:when test="${'down' eq result.imagePath}">
													<c:out value="${result.change}"/>
													<img src="<c:url value='/resources/images/common/rank_arrow_down.gif'/>" alt="순위 하락" />
												</c:when>
												<c:when test="${'new' eq result.imagePath}">
													<img src="<c:url value='/resources/images/common/icon_new.gif'/>" alt="New" class="rank_new" />
												</c:when>
												<c:otherwise>
														-
													<%-- <img src="<c:url value='/resources/images/common/icon_new.gif'/>" alt="변동없음" /> --%>
												</c:otherwise>
											</c:choose>
										</span>
									</c:forEach>
								</ul>
							</div>
							<div class="rank_bottom"></div>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li>
						<div class="rank_top">1위 ~ 10위</div>
						<div class="rank_list">
							<ul>
								<li>키워드 없음.</li>
							</ul>
						</div>
						<div class="rank_bottom"></div>
				 	</li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
