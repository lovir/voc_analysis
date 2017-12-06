<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<c:choose>
	<c:when test="${ !empty synthesisRanking}">
		<%-- <ul>
				<li class="no_1"><span class="rank_no">1</span><a class="rank_key rank_mykey">열차 지연</a><span class="rank_right">
				<img src="<c:url value='/resources/images/common/bar1.png'/>" alt="100%"></span><span class="count_no">45,000</span></li>
				<li class="no_2"><span class="rank_no">2</span><a href="#" class="rank_key">불편신고</a><span class="rank_right"><img src="images/common/bar1.png" alt="90%" style="width:90%; height:100%;"></span><span class="count_no">45,000</span></li>
				<li class="no_3"><span class="rank_no">3</span><a href="#" class="rank_key">더워요</a><span class="rank_right"><img src="images/common/bar1.png" alt="80%" style="width:80%; height:100%;"></span><span class="count_no">41,000</span></li>
				<li class="no"><span class="rank_no">4</span><a class="rank_key rank_mykey">환불</a><span class="rank_right"><img src="images/common/bar1.png" alt="70%" style="width:70%; height:100%;"></span><span class="count_no">40,030</span></li>
				<li class="no"><span class="rank_no">5</span><a href="#" class="rank_key">안내 방송</a><span class="rank_right"><img src="images/common/bar1.png" alt="60%" style="width:60%; height:100%;"></span><span class="count_no">31,000</span></li>
				<li class="no"><span class="rank_no">6</span><a href="#" class="rank_key">유실물</a><span class="rank_right"><img src="images/common/bar1.png" alt="55%" style="width:55%; height:100%;"></span><span class="count_no">25,000</span></li>
				<li class="no"><span class="rank_no">7</span><a href="#" class="rank_key">와이파이</a><span class="rank_right"><img src="images/common/bar1.png" alt="50%" style="width:50%; height:100%;"></span><span class="count_no">21,000</span></li>
				<li class="no"><span class="rank_no">8</span><a href="#" class="rank_key">호객행위</a><span class="rank_right"><img src="images/common/bar1.png" alt="45%" style="width:45%; height:100%;"></span><span class="count_no">18,290</span></li>
				<li class="no"><span class="rank_no">9</span><a href="#" class="rank_key">불친절</a><span class="rank_right"><img src="images/common/bar1.png" alt="20%" style="width:20%; height:100%;"></span><span class="count_no">1,030</span></li>
				<li class="no"><span class="rank_no">10</span><a href="#" class="rank_key">성추행</a><span class="rank_right"><img src="images/common/bar1.png" alt="10%" style="width:10%; height:100%;"></span><span class="count_no">450</span></li>
			</ul> --%>
			<ul>
			<c:forEach var="result" items="${synthesisRanking}" varStatus="status">
					
				<c:if test="${empty result}">
					<li >키워드 없음</li>
				</c:if>
				
				<c:choose>
					<c:when test="${status.count == 1 || status.count == 2 || status.count == 3}"><li class="no_${status.count}"></c:when>
					<c:otherwise><li class="no"></c:otherwise>
				</c:choose>
				<span class="rank_no"><c:out value="${status.count}"/></span>
				<c:choose>
					<c:when test="${fn:length(result.keyword)>7}">
						<c:set var="titleTag" value="${result.keyword}"/>								
					</c:when>
					<c:otherwise>
						<c:set var="titleTag" value=""/>								
					</c:otherwise>
				</c:choose>
				<a class="rank_key <c:if test="${'Y' eq result.interestYn}">rank_mykey</c:if>" title="<c:out value="${result.keyword}"/>" href="javascript:goTrendAnalysis('<c:out value="${result.keyword}"/>');" name="<c:out value="${result.keyword}"/>">
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
					<img src="<c:url value='/resources/images/common/bar1.png'/>"  alt="<c:out value="${result.share}"/>%" style="width:<c:out value="${result.share}"/>%; height:100%;"></span><span class="count_no">${result.count}																		
				</span>
				</li>
				
		</c:forEach>
		</ul>
	
	</c:when>
	<c:otherwise>
		
	</c:otherwise>
</c:choose>

