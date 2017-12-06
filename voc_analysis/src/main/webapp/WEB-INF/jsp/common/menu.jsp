<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(function () {

	$('.btn_dashboard').removeClass('on');
	$('.left_nav li ul li a').removeClass('on');
	$('.left_nav li a').removeClass('on');
	
	if('대시보드' == $('.cont_tit').text()){
		$('.btn_dashboard').addClass('on');
		//dashboard btn (common.js에서 이동)
		if($('.btn_dashboard').hasClass('on')){
			$('.left_area').css('z-index','1001');
		};
	}else{
		$('.left_nav li ul li a').each(function() {
			if($(this).text() == $('.cont_tit').text()){
				$(this).addClass('on');
				$(this).parents("li").find(".nav_1depth").next('span').html('-');	
				$(this).parents("li").find(".nav_1depth").addClass('br_none');
				$(this).parents("li").find(".nav_1depth").next().next('ul').slideDown();	
			}
		});
		
		$('.left_nav li a').each(function() {
			if($(this).text() == $('.cont_tit').text()){
				$(this).addClass('on');
			}
		});
	}
	
});

	
</script>

<!-- left area start -->
	<!-- 활성화시 a태그 class에 on 추가 -->
	<a href='<c:out value="${pageContext.request.contextPath}" />/dashBoard/init.do' class='btn_dashboard'>음성VOC 대시보드</a>
				
	<p>VOC 분석</p>
	<ul class="left_nav">
		<li><a href="#" class="nav_1depth"><span class="nav_icon nav07"></span>키워드랭킹분석</a><span class="sub_more">+</span>
			<ul>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/keywordRanking/SynthesisRankingInit.do'>종합랭킹</a></li>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/keywordRanking/InterestRankingInit.do'>관심키워드랭킹</a></li>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/keywordRanking/issueKeywordRanking.do'>이슈키워드랭킹</a></li>
			</ul>
		</li>
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/dashBoard/regionSt.do'><span class="nav_icon nav14"></span>지역현황분석</a></li>
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/keywordRanking/fieldInit.do'><span class="nav_icon nav15"></span>분야현황분석</a>				
		</li>
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/dashBoard/emotionNl.do'><span class="nav_icon nav08"></span>감성분석</a></li>
		<li><a href="#" class="nav_1depth"><span class="nav_icon nav04"></span>전문분석</a><span class="sub_more">+</span>
			<ul>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/relation/relationAnalysisInit.do'>연관도 분석</a></li>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/trend/trendAnalysisInit.do'>트렌드 분석</a></li>
			</ul>	
		</li>
	</ul>
<%-- 
	<p>소셜 분석</p>
	<ul class="left_nav">
		<li><a href='#' class="nav_1depth"><span class="nav_icon nav07"></span> 키워드종합분석</a><span class="sub_more">+</span>
			<ul>
				<li><a href='<c:out value="${pageContext.request.contextPath}" />/voekeywordRanking/synthesisRankingInit.do'> 종합랭킹</a></li>
				<li><a href='<c:out value="${pageContext.request.contextPath}" />/voekeywordRanking/InterestRankingInit.do'> 관심키워드랭킹</a></li>
				<li><a href='<c:out value="${pageContext.request.contextPath}" />/voekeywordRanking/issueKeywordRanking.do'> 이슈키워드랭킹</a></li>
			</ul>
		</li>
	
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/voekeywordRanking/voeregionSt.do'><span class="nav_icon nav14"></span> 지역현황분석</a></li>
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/voekeywordRanking/fieldInit.do'><span class="nav_icon nav15"></span> 분야현황분석</a></li>
		<li><a href="<c:out value="${pageContext.request.contextPath}" />/voekeywordRanking/emotionNl.do"><span class="nav_icon nav08"></span> 감성분석</a></li>
	
		<li><a href="#" class="nav_1depth"><span class="nav_icon nav04"></span>전문분석</a><span class="sub_more">+</span>
			<ul>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/voerelation/relationAnalysisInit.do'> 연관도 분석</a></li>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/voetrend/trendAnalysisInit.do'> 트렌드 분석</a></li>
			</ul>	
		</li>
	</ul>
 --%>
	<p>설정</p>
	<ul class="left_nav">
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/management/interestKeywordInit.do'><span class="nav_icon nav09"></span>키워드 관리</a></li>
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/management/thesaurusDictionaryInit.do'><span class="nav_icon nav10"></span>사전 관리</a></li>
		<li><a href='<c:out value="${pageContext.request.contextPath}" />/openApi/openApiInit.do'><span class="nav_icon nav10"></span>OPEN API</a></li>
		<%-- <li><a href='<c:out value="${pageContext.request.contextPath}" />/management/userManagementInit.do'><span class="nav_icon nav10"></span>사용자 관리</a></li> --%>
	</ul>

<!--// left area end -->