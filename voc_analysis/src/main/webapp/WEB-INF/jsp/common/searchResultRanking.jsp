<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="win_contarea">
	<c:if test="${not empty searchResultRanking}">
	<!-- 랭크 목록 start -->
	<div class="rank_top_area" id="rankingPriod<c:out value="${param.compare}" />">
		<ul class="rank_group full">
			<c:choose>
				<c:when test="${not empty searchResultRanking.periodKeyword}">
					<c:forEach var="resultMap" items="${searchResultRanking.periodKeyword}" varStatus="mapStatus">
						<li id="<c:out value='${resultMap.key}'/>">
							<div class="rank_top"><c:out value="${resultMap.value}"/></div>
							<div class="rank_list">
								<ul>
									<c:set var="ranking" value="${searchResultRanking[resultMap.key]}" />
									<c:if test="${empty ranking}">
										<li >키워드 없음</li>
									</c:if>
									<c:if test="${not empty ranking}">
										<c:forEach var="result" items="${ranking}" varStatus="status">
											<c:set var="rank" value="${status.count}" />
											<c:if test="${not empty result.trendRanking &&  result.trendRanking > 0}">
												<c:set var="rank" value="${result.trendRanking}" />
											</c:if>											
											<c:choose>
												<c:when test="${rank == 1}"><li class="no_1"></c:when>
												<c:when test="${rank == 2 || rank == 3}"><li class="no_23"></c:when>
												<c:otherwise><li></c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${rank <11}">
													<span class="rank_no"><c:out value="${rank}"/></span>
												<a class='<c:out value="${result.highlight}"/>' href="javascript:;" name="<c:out value="${result.keyword}"/>">
													<c:choose>
														<c:when test="${fn:length(result.keyword)>7}">
															<c:out value="${fn:substring(result.keyword,0,6)}"/>...								
														</c:when>
														<c:otherwise>
															<c:out value="${result.keyword}"/>								
														</c:otherwise>
													</c:choose>
												</a>
												<span class="rank_right">
													<c:if test="${result.ranking != 0}">
														<c:out value="${result.ranking}"/>
													</c:if>
													<c:choose>
														<c:when test="${'up' eq result.order}">
															<c:out value="${result.change}"/>
															<img src="<c:url value='/resources/images/common/rank_arrow_up.gif'/>" alt="순위 상승" />
														</c:when>
														<c:when test="${'down' eq result.order}">
															<c:out value="${result.change}"/>
															<img src="<c:url value='/resources/images/common/rank_arrow_down.gif'/>" alt="순위 하락" />
														</c:when>
														<c:when test="${'new' eq result.order}">
														<img src="<c:url value='/resources/images/common/icon_new.gif'/>" alt="New" class="rank_new" />
														</c:when>
														<c:otherwise>
																-
														</c:otherwise>
													</c:choose>
												</span>
												</c:when>
											</c:choose>
											</li>
										</c:forEach>
									</c:if>
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
	</c:if>
	<!--// 랭크 목록 end -->
</div>