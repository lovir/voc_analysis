<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
	<title>서울메트로 VOC</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/container/css/jquery-ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/container/css/style.css'/>" />
	<script type="text/javascript" src="<c:url value='/resources/container/js/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/container/js/jquery-ui.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/container/js/common.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/container/js/jquery.simplemodal.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/container/js/jquery.treeview.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/container/js/jquery.iframe-auto-height.plugin.1.5.0.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/container/js/jquery.slimscroll.min.js'/>"></script>
	<!-- 태블로 iFrame 크기 자동변경 소스 -->
	<!-- 
	<script type="text/javascript">
		var getParameters = function (paramName) {
		    // 리턴값을 위한 변수 선언
		    var returnValue;
	
		    // 현재 URL 가져오기
		    var url = location.href;
	
		    // get 파라미터 값을 가져올 수 있는 ? 를 기점으로 slice 한 후 split 으로 나눔
		    var parameters = (url.slice(url.indexOf('?') + 1, url.length)).split('&');
	
		    // 나누어진 값의 비교를 통해 paramName 으로 요청된 데이터의 값만 return
		    for (var i = 0; i < parameters.length; i++) {
		        var varName = parameters[i].split('=')[0];
		        if (varName.toUpperCase() == paramName.toUpperCase()) {
		            returnValue = parameters[i].split('=')[1];
		            return decodeURIComponent(returnValue);
		        }
		    }
		};
		
		$(document).ready(function(){
			var publicframe =  getParameters('publicframe');
			$('#menu_nav_step1').html(publicframe.substring(publicframe.lastIndexOf("/") +1,publicframe.lastIndexOf(".")));
			frameResize();
		});
		$(window).resize(function(){
			frameResize();
		})
		function frameResize(){
			var framH = $(window).height()-$('#px-demo-navbar-collapse').height()-80;
			var framW = $('.px-content').width()+16;
			$('#inneriframe').height( framH ) ;
			$('#inneriframe').width( framW ) ;
		}

		try{
			// addEventListener(FF, Webkit, Opera, IE9+) and attachEvent(IE5-8)
			/*
			var myEventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
			var myEventListener = window[myEventMethod];
			var myEventMessage = myEventMethod == "attachEvent" ? "onmessage" : "message";
			myEventListener(myEventMessage, function (e) {
				var data = e.data;
				if(data && data.indexOf("|") != -1){
					var scrolllHW = data.split("|");
					try{
						var framH = parseInt(scrolllHW[0]);
						document.getElementById('inneriframe').height = framH + "px";
					}catch(e){};
					try{
						var framW = parseInt(scrolllHW[1]);
						document.getElementById('inneriframe').width = framW + "px";
					}catch(e){};
				}
			}, false);
			*/
		}catch(e){

		}
	</script>
	 -->
</head>

<body>

<div id="skipnavi">
	<ul>
	<li><a href="#content">주메뉴 바로가기</a></li>
	<li><a href="#cont_body">본문 바로가기</a></li>
	</ul>
</div>
<hr />

<div id="wrapper">

	<!-- header start -->
	<div class="header">
		<script type="text/javascript">
		
		$(function () {
			//최초 접속시 대시보드 출력
			iFrameMove("<c:out value="${pageContext.request.contextPath}" />/dashBoard/dashBoardInit.do");
			
			//상세 보기 클릭
			$('[name="util_01"]').click(function(idx){ 
				var	period = '1'; // 지난 일주일(기본)
				var json = {"period":period};
				
				$("#basic-modal-util_01").load(getContextPath() + "/management/selectAlarmKeywordMonitoringList.do",
						json,
					function(){
						$("#basic-modal-util_01").modal({
							persist: false,
							focus: false,
							onClose: function () {
								$('body').css('overflow','auto');
								$.modal.close();
							}
						});
						$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
					});
				return false;
			});
			
			// 인쇄버튼 클릭
			$('.util_04').on('click', function(){
				window.print();
			});
		
			// 매뉴얼 클릭
			$('.util_05').on('click', function(){
				var win = window.open(getContextPath() + '/dashBoard/help.do', '_blank');
				win.focus();
			});
			
			// 검색 버튼 클릭
			$('#mainSearchBtn').on('click', function(){
				if($('#searchTerm').val().length<=0){
					alert("검색어 입력 해 주세요.");
					return false;
				}else{
					if($('#searchCondition option:selected').val() == '01'){
						$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/vocSearch.do");
					}else{
						$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/userSearch.do");
					}
					
					$("#headerForm").submit();
				}
			});
			
			// 검색어 입력 후 엔터
			$('#searchTerm').keypress(function(e){
				if(e.which==13){
					if($('#searchTerm').val().length<=0){
						alert("검색어 입력 해 주세요.");
						return false;
					}else{
						if($('#searchCondition option:selected').val() == '01'){
							$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/vocSearch.do");
						}else{
							$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/userSearch.do");
						}
						
						$("#headerForm").submit();
					}
				}
			});
		});
		</script>
		<!-- header start -->
		<div class="header">
	
			<div class="head_wrap clear2">
	
				<!-- <h1><img src="../resources/container/images/common/logo.png" alt="DiXSi 음성 VOC 분석 시스템" /></h1> -->
				<h1>음성 VOC 분석 시스템</h1>
				<!-- 로그아웃 / 보조메뉴 start -->
				<div class="util_area">
					<span class="person_info">
						<strong><c:out value="${sessionScope.login.userNm}" />(${sessionScope.login.userId})</strong>님 환영합니다!
						<a href="<c:out value='${pageContext.request.contextPath}' />/common/logout.do" >로그아웃</a>
					</span>
					<ul class="util_nav">
						<!-- <li><a href="#" title="인쇄" class="util_04"></a></li> -->
						<!-- <li><a href="#" title="도움말" class="util_05"></a></li> -->
	
					</ul>
				</div>
				<!--// 로그아웃 / 보조메뉴 end -->
	
			</div>
	
		</div>
		<!--// header end -->
	
	
		<!-- 모달팝업 : 알림 팝업레이어 -->
		<div class="modal_popup modal_01" id="basic-modal-util_01" ></div>
	</div>
	<!--// header end -->

	<hr />

	<!-- content start -->
	<div id="content">

		<div class="cont">

			<!-- left area start -->
			<div class="left_area">
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
				
				function iFrameMove(url){
					var userID = 'test';
					var userNm = '테스트';
					url = url + '?portal_id=' + encodeURIComponent(userID) + '&portal_nm=' + encodeURIComponent(userNm);
					$('#contFrame').attr('src',url);
				}	
				</script>
				<!-- 활성화시 a태그 class에 on 추가 -->
				
				<a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/dashBoard/dashBoardInit.do");' class='btn_dashboard'>대시보드</a>
							
				<p>VOC 분석</p>
				<ul class="left_nav">
					<li><a href="#" class="nav_1depth"><span class="nav_icon nav07"></span>키워드랭킹분석</a><span class="sub_more">+</span>
						<ul>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/keywordRanking/synthesisRankingInit.do");'>종합랭킹</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/keywordRanking/interestRankingInit.do");' >관심키워드랭킹</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/keywordRanking/issueKeywordRankingInit.do");'>이슈키워드랭킹</a></li>
						</ul>
					</li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/stationStatus/stationStatusInit.do");'>역별 현황분석</a></li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/fieldStatus/fieldStatusInit.do");'>분야별 현황분석</a></li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/emotion/emotionAnalysisInit.do");'>감성 분석</a></li>
					<li><a href="#" class="nav_1depth"><span class="nav_icon nav04"></span>전문분석</a><span class="sub_more">+</span>
						<ul>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/relationAnalysis/relationAnalysisInit.do");'>연관도 분석</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/relationAnalysis/relationAnalysisCompareInit.do");'>연관도 비교 분석</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/trend/trendAnalysisInit.do");'>트렌드 분석</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/trend/trendAnalysisCompareInit.do");'>트렌드 비교 분석</a></li>
						</ul>	
					</li>
				</ul>
				
				<p>소셜 분석</p>
				<ul class="left_nav">
					<li><a href="#" class="nav_1depth"><span class="nav_icon nav07"></span>키워드랭킹분석</a><span class="sub_more">+</span>
						<ul>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialKeywordRanking/socialSynthesisRankingInit.do");'>종합랭킹</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialKeywordRanking/socialInterestRankingInit.do");' >관심키워드랭킹</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialKeywordRanking/socialIssueKeywordRankingInit.do");'>이슈키워드랭킹</a></li>
						</ul>
					</li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialChannelStatus/socialChannelStatusInit.do");'>채널별 현황분석</a></li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialEmotion/socialEmotionAnalysisInit.do");'>감성 분석</a></li>
					<li><a href="#" class="nav_1depth"><span class="nav_icon nav04"></span>전문분석</a><span class="sub_more">+</span>
						<ul>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialRelationAnalysis/socialRelationAnalysisInit.do");'>연관도 분석</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialRelationAnalysis/socialRelationAnalysisCompareInit.do");'>연관도 비교 분석</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialTrend/socialTrendAnalysisInit.do");'>트렌드 분석</a></li>
						<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/socialTrend/socialTrendAnalysisCompareInit.do");'>트렌드 비교 분석</a></li>
						</ul>	
					</li>
				</ul>
				
				<p>설정</p>
				<ul class="left_nav">
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/management/interestKeywordInit.do");'><span class="nav_icon nav09"></span>키워드 관리</a></li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/management/thesaurusDictionaryInit.do");'><span class="nav_icon nav10"></span>사전 관리</a></li>
					<li><a href='javascript:iFrameMove("<c:out value="${pageContext.request.contextPath}" />/openApi/openApiInit.do");'><span class="nav_icon nav10"></span>OPEN API 관리</a></li>
					<%-- <li><a href='<c:out value="${pageContext.request.contextPath}" />/management/userManagementInit.do'><span class="nav_icon nav10"></span>사용자 관리</a></li> --%>
				</ul>
				
			</div>
			<!--// left area end -->
			<script type="text/javascript">
			/*
			//jquery.iframe-auto-height.plugin.1.5.0.min.js 사용 소스
			$(document).ready(function() {
				//빨간색 URL 부분에 iframe에 출력 원하는 url을 적는다
				var url = "http://localhost:8080/voc_analysis/";
				$("<iframe scrolling='no' />").attr("src", url).attr("frameborder", 0).attr("width", "100%").attr("height", "0px").appendTo("#cont_body");
				//.attr("scrolling", "no").attr("overflow-x", "auto").attr("overflow-y", "hidden")
				$('iframe').iframeAutoHeight({heightOffset: 0});
			}); */
			$(function(){
				$("iframe.contFrame").load(function(){ //iframe 컨텐츠가 로드 된 후에 호출됩니다.
					var frame = $(this).get(0);
					var doc = (frame.contentDocument) ? frame.contentDocument : frame.contentWindow.document;
					$(this).height(doc.body.scrollHeight);
					$(this).width(doc.body.scrollWidth);
				});
			});
			</script>
			<!-- right area start -->
			<div class="right_area">
				<!-- tiles body 시작 -->
				<!-- location start -->
				<div class="loc_wrap clear2">
					<!-- <ul>
					<li>홈</li>
					<li>설정</li>
					<li>OPEN API</li>
					</ul> -->
				</div>
				<!--// location end -->
				<!-- 본문 start -->
				<div class="cont_body" id="cont_body">
					<iframe id="contFrame" class="contFrame" frameborder="0" src="" ></iframe>
				</div>
				<!-- tiles body 종료 -->
				<!-- footer start -->
				<div id="footer">ⓒ DiQuest Corp.
				</div>
				<!--// footer end -->
			</div>
			<!--// right area end -->

		</div>

	</div>
	<!--// content end -->

</div>
</body>
</html>