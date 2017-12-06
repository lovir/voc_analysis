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
		<title>서울교통공사 : 사용자 사전 관리</title>
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
		<script type="text/javascript" src="<c:url value='/resources/js/voc/userDictionary.js'/>"></script>
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
				<!-- content start -->
				<div class="cont">
					<!-- right area start -->
					<div class="right_area">
						<!-- 본문 start -->
						<div class="cont_body" id="cont_body">
							<div class="cont_head">
								<span class="cont_tit">사전 관리</span><span class="cont_desc">용어사전을 등록하고 관리하는 기능을 제공합니다.</span>
							</div>
							<!-- sub tab start -->
							<div class="tab clear2">
								<ul class="tab_list">
									<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/thesaurusDictionaryInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />">유의어 사전 관리</a></li>
									<li ><a href="#" class="on">사용자 사전 관리</a></li>
								</ul>
							</div>
							<!--// sub tab end -->
							
							<!-- 사용자사전 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">사용자 사전</span>
		
									<ul class="win_btnset">
									<li><a href="#" class="btn_dic_add" value="3"  title="사전 등록"></a></li>
									<li><a href="#" class="btn_dic_down" value="3" title="사전 다운로드"></a></li>
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								<!-- win_contarea start -->
								<div class="win_contarea clear2">
									<!-- 사용자사전 목록 start -->
									<div class="win_cont_left clear2">
										<!-- dic_list start -->
										<div class="dic_list">
											<!-- 목록 start -->
											<div class="clear2">	
												<fieldset>
													<legend>검색</legend>
													<span class="float_l">
														키워드 검색 : 
														<input type="text" id="searchKeyword" class="w100px gray" title="검색어 입력" />
														<a href="#" id="btnSearch"class="btn ssmall b_gray">검색</a>
													</span>
													<span class="float_r">
														<select id="searchSort">
															<option value="KEYWORD ASC">가나다 순</option>
															<option value="REG_DATE DESC">수정일 순</option>
															<option value="APPLY ASC">미적용 순</option>
														</select>
														<select id="pageSize" name="pageSize" title="출력건수 선택">
															<option value="10">10 건</option>
															<option value="20">20 건</option>
														</select>
														<a href="#" id="btnApply" class="btn ssmall b_blue2">전체 적용</a>
														<a href="#" id="btnSelectedDelete" class="btn ssmall b_blue2">선택 삭제</a>
													</span>
												</fieldset>
											</div>
											
											<fieldset>
												<legend>사용자사전 목록</legend>
												<table class="tbl_type04 mt_5" summary="키워드, 적용 등으로 구성된 사용자사전 목록">
												<caption>사용자사전 목록</caption>
												<colgroup>
													<col style="width:2%;" />
													<col />
													<col style="width:14%;" />
													<col style="width:18%;" />
												</colgroup>
						
												<thead>
												  <tr>
													<th scope="col"><input type="checkbox" title="전체선택" /></th>
													<th scope="col">키워드</th>
													<th scope="col">적용</th>
													<th scope="col">수정일</th>
												  </tr>
												</thead>
						
												<tbody id="userList">
												</tbody>
												</table>
											</fieldset>
											
											<!-- 목록정보 start -->
											<div class="list_info">
												<span class="float_l">총 <strong id="userCount"></strong> 건</span><span class="float_r" id="userCurrent"></span>
											</div>
											<!--// 목록정보 end -->
											<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
											<div class="paging" id="paging">
											</div>
											<!--// 페이징 end -->
											<!--// 목록 end -->
										</div>
										<!--// dic_list end -->
									</div>
									<!--// 사용자사전 목록 end -->
									
									<!-- 사용자 사전 편집 start -->
									<div class="win_cont_right3 clear2">
										<div class="dic_edit">
											<!-- 편집 양식 start -->
											<p class="mb_5">사용자 사전 편집</p>
											<fieldset>
												<legend>사용자 사전 편집 양식</legend>
												<table class="tbl_type06">
												<caption>사용자 사전 편집 양식</caption>
												<colgroup>
													<col style="width:120px;" />
													<col />
												</colgroup>
												<tbody>
													<tr>
														<th scope="row"><label for="form_01">키워드</label></th>
														<td><input type="text" class="white w190px" id="keyword" title="키워드 입력" /><a href="#" id="btnAdd" class="btn b_gray ssmall">등록</a></td>
													</tr>
													<tr>
														<th scope="row">형태소 분석결과</th>
														<td><label id="morpheme"></label></td>
													</tr>
													<tr>
														<th scope="row">색인어휘 추출결과</th>
														<td><label id="extractor"></label></td>
													</tr>
												</tbody>
												</table>
											</fieldset>
											<!--// 편집 양식 end -->
											
											<!-- 키워드 검색 start -->
											<div class="clear2 mt_20">	
												<fieldset>
													<legend>검색</legend>
													<span class="float_l">키워드 검색</span>
						
													<span class="float_r">
														<input type="text" id="viewKeyword" class="w100px white" title="키워드 입력" maxlength="50" />
														<a href="#" id="btnSearchKeyword" class="btn ssmall b_gray">보기</a>
													</span>
												</fieldset>
											</div>
					
											<fieldset>
												<legend>키워드 목록</legend>
												<table class="tbl_type07 mt_3">
													<caption>키워드 목록</caption>
													<colgroup>
														<col style="width:25%;" />
														<col style="width:30%;" />
														<col />
													</colgroup>
							
													<thead>
													  <tr>
														<th scope="col">키워드</th>
														<th scope="col">색인어휘 추출결과</th>
														<th scope="col">형태소 분석결과</th>
													  </tr>
													</thead>
							
													<tbody id="keywordList">
													</tbody>
												</table>
											</fieldset>
											<!--// 키워드 검색 end -->
										</div>
									</div>
									<!--//사용자 사전 편집 end -->
								</div>
								<!--// win_contarea end -->
							</div>
							<!--// 사용자사전 end -->
							
						</div>
						<!--// 본문 end -->
						<!-- 사용자사전 업/다운로드 팝업레이어 -->
						<div class="modal_popup w700px" id="basic-modal-dic_view" ></div>
					</div>
					<!--// right area end -->
				</div>
				<!--// content end -->
			</div>
			<!--// content end -->
		</div>
		<!--// wrapper end -->
	</body>
</html>
