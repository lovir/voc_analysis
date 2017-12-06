<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">

$(function () {

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

			<!-- <h1><img src="../resources/images/common/logo.png" alt="DiXSi 음성 VOC 분석 시스템" /></h1> -->
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