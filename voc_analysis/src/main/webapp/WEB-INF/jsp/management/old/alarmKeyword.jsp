<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/alarmKeyword.js'/>"></script>
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
			<li>알람키워드 관리</li>
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
				<li><a href="#" class="on">알람키워드 관리</a></li>
				<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/standardAlarmInit.do">발생기준 관리</a></li>
				<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/mailReceiverInit.do">메일수신자 관리</a></li>
			</ul>
		</div>
		<!--// sub tab end -->

		<!-- 알람키워드 목록 start -->
		<div class="win win_full">
			<div class="win_head clear2">
				<span class="win_tit bg_none">알람키워드 목록</span>
				<ul class="win_btnset">
					<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea align_c">

				<!-- 목록 start -->
				<div class="p_20">
					
					<div class="clear2">
						<div class="float_l">* 최대 10개까지 키워드 등록이 가능합니다.</div>
						<div class="float_r">
							<a href="#" id="btnDelete" class="btn b_orange medium">선택 삭제</a>
							<a href="#" id="btnClear" class="btn b_orange medium">신규등록</a>
						</div>
					</div>

					<fieldset><legend>대상알람키워드워드 목록</legend>
					<table class="tbl_type04 mt_5" summary="키워드, 부서명, 등록자, 상태 등으로 구성된 알람키워드 목록">
					<caption>알람키워드 목록</caption>
					<colgroup>
						<col style="width:3%;" />
						<%-- <col style="width:7%;" /> --%>
						<col style="width:18%;" />
						<col />
						<col style="width:18%;" />
						<col style="width:14%;" />
						<col style="width:11%;" />
						<col style="width:8%;" />
					</colgroup>

					<thead>
					  <tr>
						<th scope="col"><input type="checkbox" id="selectAll" title="전체선택" /></th>
						<th scope="col">No.</th>
						<!-- <th scope="col">VOC유형</th> -->
						<th scope="col">키워드</th>
						<th scope="col">비고</th>
						<th scope="col">부서명</th>
						<th scope="col">등록자</th>
						<th scope="col">등록일</th>
						<th scope="col">상태</th>
					  </tr>
					</thead>

					<tbody id="list_contents">
					</tbody>
					</table>
					</fieldset>
					
					<div class="list_info">
						<span class="float_l">총 <strong id="totCnt"></strong> 건</span>
					</div>
					
					<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
					<div class="paging" id="paging">
					</div>
					<!--// 페이징 end -->
						
				</div>
				<!--// 목록 end -->

			</div>
		
		</div>
		<!--// 알람키워드 목록 end -->

		<!-- 알람키워드 등록정보 start -->
		<div class="win win_full">
			<div class="win_head clear2">
				<span class="win_tit bg_none">알람키워드 등록정보</span>

				<ul class="win_btnset">
				<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea">


				<!-- 폼 start -->
				<form:form name="alarmKeywordForm" id="alarmKeywordForm" commandName="alarmKeywordVo"  method="post">
				<div class="tab_cont">
					<p class="font_14b mb_5">등록정보</p>
					<fieldset><legend>등록 정보</legend>
						<table class="tbl_type03" summary="키워드, 부서명, 등록자 등으로 구성된 알람키워드 등록정보">
							<caption>등록 정보</caption>
							<colgroup>
								<col style="width:13%;" />
								<col style="width:40%;" />
								<col style="width:13%;" />
								<col />
							</colgroup>
							<tbody>
								<tr>
									<th scope="row"><label for="detail_01">키워드</label></th>
									<td><form:input path="keyword" class="gray w200px" title="키워드 입력" id="keyword" /><form:hidden path="no" id="no" /></td>
									<th scope="row">부서명</th>
									<td id="contents_orgNm">${alarmKeywordVo.orgNm}</td><form:hidden path="orgNm" id="orgNm" />
								</tr>
								<tr>
									<th scope="row">활성여부</th>
									<td>
										<form:radiobutton path="useYn" value="Y" id="detail_02" /><label for="detail_02">활성</label>
										<form:radiobutton path="useYn" value="N" id="detail_03" class="ml_10" /><label for="detail_03">비활성</label>
									</td>
									<th scope="row">등록자</th>
									<td id="contents_regNm">${alarmKeywordVo.regNm}(${alarmKeywordVo.regId})</td><form:hidden path="regId" id="regId"/><form:hidden path="regNm" id="regNm" />
								</tr>
								<!-- <tr>
									<th scope="row">VOC유형 선택</th>
									<td id="needsTypeList">
									</td>
								</tr> -->
								<tr>
									<th scope="row"><label for="detail_04">비고</label></th>
									<td colspan="3">
										<form:textarea path="etc" class="gray w99p" rows="10" id="etc" title="내용 입력"/>
									</td>
								</tr>
							</tbody>
						</table>
						<input type="hidden" id="useYnTemp" />
						<div class="align_r mt_10">
							<a href="#" id="btnSave" class="btn b_orange medium">저장</a>
						</div>

					</fieldset>
				</div>
				</form:form>
				<!--// 폼 end -->

			</div>
		
		</div>
		<!--// 알람키워드 등록정보 end -->

	</div>
	<!--// 본문 end -->
