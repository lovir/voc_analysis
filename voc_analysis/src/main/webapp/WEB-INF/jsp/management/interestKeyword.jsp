<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
		<title>서울교통공사 : 종합 키워드 분석</title>
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
		<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/datepicker_kr.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/modal.basic.js'/>"></script>
		<script src="<c:url value='/resources/js/attrchange.js'/>"></script>	<!-- iFrame 연동 관련 추가 라이브러리 -->
		<!-- header start -->
		<script type="text/javascript" src="<c:url value='/resources/js/header.js'/>"></script>
		<!--// header end -->
		<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/resources/js/voc/interestKeyword.js'/>"></script>
		<script type="text/javascript">
		function getContextPath() {
			return "<c:out value="${pageContext.request.contextPath}" />";
		}
		
		function getImgPath(){
			return "<c:url value='/resources/images/common/'/>";
		}
		</script>
	</head>
	<body>
		<div id="skipnavi">
			<ul>
			<li><a href="#content">주메뉴 바로가기</a></li>
			<li><a href="#cont_body">본문 바로가기</a></li>
			</ul>
		</div>
		<hr />
		<!-- wrapper start -->
		<div id="wrapper">
			<hr />
			<!-- content start -->
			<div id="content">
				<div class="cont">
					<!-- right area start -->
					<div class="right_area">
						<!-- 본문 start -->
						<div class="cont_body" id="cont_body">
						
							<div class="cont_head">
								<span class="cont_tit">키워드 관리</span><span class="cont_desc">관심 키워드 및 알람 키워드에 대한 설정 기능을 제공합니다.</span>
							</div>
					
							<!-- main tab start -->
							<div class="main_tab clear2">
								<ul>
								<li class="on"><a href="#">관심키워드 관리</a></li>
								<li><a href='<c:out value="${pageContext.request.contextPath}" />/management/alarmKeywordInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />'>알람키워드 관리</a></li>
								</ul>
							</div>
							<!--// main tab end -->
					
							<!-- 관심키워드 목록 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">관심키워드 목록</span>
									<ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
								<div class="win_contarea align_c">
					
									<!-- 목록 start -->
									<div class="p_20">
										
										<div class="clear2">	
											<fieldset><legend>검색 및 노출건수 설정</legend>
											<span class="float_l">
												키워드 검색 : 
												<!-- <select id="condition">
													<option value="1">키워드</option>
													<option value="2">비고</option>
													<option value="3">부서명</option>
													<option value="4">등록자</option>
												</select> -->
												<input type="text" id="term" class="w150px gray" title="검색어 입력" /><a href="#" id="btnSearch" class="btn ssmall b_gray">검색</a>
											</span>
					
											<span class="float_r">
												<select id="pageSize">
												<option value="10">10 건</option>
												<option value="20">20 건</option>
												</select>
												<a href="javascript:;" id="btnDelete" class="btn medium b_blue2" style="margin-left:10px !important;">선택삭제</a>
												<a href="javascript:;" id="btnClear" class="btn medium b_blue2">신규등록</a>
											</span>
											</fieldset>
										</div>
					
										<fieldset><legend>관심키워드 목록</legend>
										<table class="tbl_type04 mt_5" summary="키워드, 부서명, 등록자, 상태 등으로 구성된 관심키워드 목록">
										<caption>관심키워드 목록</caption>
										<colgroup>
											<col style="width:3%;" />
											<col style="width:7%;" />
											<col style="width:18%;" />
											<col />
											<col style="width:11%;" />
											<col style="width:10%;" />
										</colgroup>
					
										<thead>
										  <tr>
											<th scope="col"><input type="checkbox" id="selectAll" title="전체선택" /></th>
											<th scope="col">No.</th>
											<th scope="col">키워드</th>
											<th scope="col">비고</th>
											<th scope="col">등록일</th>
											<th scope="col">활성/비활성</th>
										  </tr>
										</thead>
					
										<tbody id="list_contents">
										</tbody>
										</table>
										</fieldset>
					
					
										<!-- 목록정보 start -->
										<div class="list_info">
											<span class="float_l">총 <strong id="totCnt"></strong> 건</span><span class="float_r"><!-- 3/10 페이지 --></span>
										</div>
										<!--// 목록정보 end -->
					
					
										<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
										<div class="paging" id="paging">
										</div>
										<!--// 페이징 end -->
					
									</div>
									<!--// 목록 end -->
								</div>
							</div>
							<!--// 관심키워드 목록 end -->
					
					
					
							<!-- 관심키워드 등록정보 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">관심키워드 등록정보</span>
					
									<ul class="win_btnset">
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
								<div class="win_contarea">
					
					
									<!-- 폼 start -->
									<form:form name="interestKeywordForm" id="interestKeywordForm" commandName="interestKeywordVo"  method="post">
									<div class="tab_cont">
										<p class="font_14b mb_5">등록정보</p>
										<fieldset><legend>등록 정보</legend>
											<table class="tbl_type03">
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
													<th scope="row">등록자</th>
													<td id="contents_regNm">${interestKeywordVo.regNm}(${interestKeywordVo.regId})</td><form:hidden path="regId" id="regId"/><form:hidden path="regNm" id="regNm" />
												</tr>
												<tr>
													<th scope="row">활성여부</th>
													<td>
														<form:radiobutton path="useYn" value="Y" id="detail_02" /><label for="detail_02">활성</label>
														<form:radiobutton path="useYn" value="N" id="detail_03" class="ml_10" /><label for="detail_03">비활성</label>
													</td>
													<th scope="row"></th>
													<td ></td>
												</tr>
												<tr>
													<th scope="row"><label for="detail_04">비고</label></th>
													<td colspan="3">
														<form:textarea path="etc" class="gray w99p" rows="10" id="etc" title="내용 입력"/>
													</td>
												</tr>
					
					
											</tbody>
											</table>
					
											<div class="align_r mt_10">
												<a href="#" id="btnSave" class="btn b_blue2 medium">저장</a>
											</div>
					
										</fieldset>
									</div>
									</form:form>
									<!--// 폼 end -->
								</div>
							</div>
							<!--// 관심키워드 등록정보 end -->
						</div>
						<!--// 본문 end -->
					</div>
					<!--// right area end -->		
				</div>
			</div>
			<!--// content end -->
		</div>
		<!--// wrapper end -->
	</body>
</html>