<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/standardAlarm.js'/>"></script>
<script type="text/javascript">
function getContextPath() {
	return "<c:out value="${pageContext.request.contextPath}" />";
}

function getImgPath(){
	return "<c:url value='/resources/images/common/'/>";
}
</script>
<!-- location start -->
	<div class="loc_wrap clear2">
		<ul>
			<li>홈</li>
			<li>설정</li>
			<li>키워드관리</li>
			<li>알람키워드 관리</li>
			<li>발생기준 관리</li>
		</ul>
	</div>
	<!--// location end -->
	<!-- 본문 start -->
	<div class="cont_body">
		<div class="cont_head">
			<span class="cont_tit">키워드 관리</span><span class="cont_desc">관심 키워드 및 알람 키워드에 대한 설정 기능을 제공합니다.</span>
		</div>
		<!-- main tab start -->
		<div class="main_tab clear2">
			<ul>
			<li><a href='<c:out value="${pageContext.request.contextPath}" />/management/interestKeywordInit.do'>관심키워드 관리</a></li>
			<li class="on"><a href="#">알람키워드 관리</a></li>
			</ul>
		</div>
		<!--// main tab end -->
		<!-- sub tab start -->
		<div class="tab clear2">
			<ul class="tab_list">
				<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/alarmKeywordInit.do">알람키워드 관리</a></li>
				<li><a href="#" class="on">발생기준 관리</a></li>
				<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/mailReceiverInit.do">메일수신자 관리</a></li>
			</ul>
		</div>
		<!--// sub tab end -->
		<!-- 발생기준 관리 start -->
		<div class="win win_full">
			<div class="win_head clear2">
				<span class="win_tit bg_none">발생기준 관리</span>
				<ul class="win_btnset">
				<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			<div class="win_contarea">
				<!-- 폼 start -->
				<div class="tab_cont">
					<fieldset><legend>발생기준 관리 양식</legend>
						<table class="tbl_type03">
						<caption>발생기준 관리 양식</caption>
						<colgroup>
							<col style="width:13%;" />
							<col />
						</colgroup>
						<tbody>
						<input type="hidden" name="no" id="no" />
							<tr>
								<th scope="row"><label for="form_02">발생레벨 - 상</label></th>
								<td>전일대비 발생 건수가<input type="text" name="level1" id="level1" class="gray w30px align_r ml_10" title="발생건수 입력" id="form_02" />% 이상 증가한 경우</td>
							</tr>
							<tr>
								<th scope="row"><label for="form_03">발생레벨 - 중</label></th>
								<td>전일대비 발생 건수가<input type="text" name="level2" id="level2" class="gray w30px align_r ml_10" title="발생건수 입력" id="form_03" />% 이상 증가한 경우</td>
							</tr>
							<tr>
								<th scope="row"><label for="form_04">발생레벨 - 하</label></th>
								<td>전일대비 발생 건수가<input type="text" name="level3" id="level3" class="gray w30px align_r ml_10" title="발생건수 입력" id="form_04" />% 이상 증가한 경우</td>
							</tr>
						</tbody>
						</table>
						<div class="align_r mt_20">
							<a href="#" id="btnSave" class="btn b_orange medium">저장</a>
						</div>
					</fieldset>
				</div>
				<!--// 폼 end -->
			</div>
		</div>
		<!--// 발생기준 관리 end -->
	</div>
	<!--// 본문 end -->