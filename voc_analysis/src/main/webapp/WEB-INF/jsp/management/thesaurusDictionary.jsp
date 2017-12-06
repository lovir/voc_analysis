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
		<title>서울교통공사 : 유의어 사전 관리</title>
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
		<script type="text/javascript" src="<c:url value='/resources/js/voc/thesaurusDictionary.js'/>"></script>
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
				<!-- cont start -->
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
									<li ><a href="#" class="on">유의어 사전 관리</a></li>
									<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/userDictionaryInit.do?portal_id=<c:out value='${portal_id}' />&portal_nm=<c:out value='${portal_nm}' />">사용자 사전 관리</a></li>
									<%-- <li><a href="<c:out value='${pageContext.request.contextPath}' />/management/userCompoundDictionaryInit.do">사용자복합어사전 관리</a></li>
									<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/stopwordDictionaryInit.do">불용어 사전 관리</a></li> --%>
								</ul>
							</div>
							<!--// sub tab end -->
							
							<!-- 유의어사전 start -->
							<div class="win win_full">
								<div class="win_head clear2">
									<span class="win_tit bg_none">유의어사전</span>
		
									<ul class="win_btnset">
									<li><a href="#" class="btn_dic_add" value="8" title="사전 등록"></a></li>
									<li><a href="#" class="btn_dic_down" value="8" title="사전 다운로드"></a></li>
									<li><a href="#" class="btn_sh" title="접기"></a></li>
									</ul>
								</div>
								
								<!-- win_contarea start -->
								<div class="win_contarea clear2">
									<!-- 유의어사전 목록 start -->
									<div class="win_cont_left clear2">
										<!-- dic_list start -->
										<div class="dic_list">
											<!-- 목록 start -->
											<div class="clear2">	
												<fieldset>
													<legend>검색</legend>
													<span class="float_l">
														키워드 : 
														<input type="text" id="searchKeyword" class="w100px gray" title="검색어 입력" />
														<a href="#" id="btnSearch"class="btn ssmall b_gray">검색</a>
														<a href="#" id="btnAddKeyword" class="btn ssmall b_gray">추가</a>
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
												<legend>유의어사전 목록</legend>
												<table class="tbl_type04 mt_5" summary="키워드, 적용 등으로 구성된 사용자사전 목록">
													<caption>유의어사전 목록</caption>
													<colgroup>
														<col style="width:2%;" />
														<col />
														<col style="width:14%;" />
														<col style="width:14%;" />
														<col style="width:18%;" />
													</colgroup>
							
													<thead>
													<tr>
														<th scope="col"><input type="checkbox" title="전체선택" /></th>
														<th scope="col">키워드</th>
														<th scope="col">유의어 수</th>
														<th scope="col">적용</th>
														<th scope="col">수정일</th>
													</tr>
													</thead>
							
													<tbody id="thesaurusList">
													</tbody>
												</table>
											</fieldset>
											
											<!-- 목록정보 start -->
											<div class="list_info">
												<span class="float_l">총 <strong id="thesaurusCount"></strong> 건</span><span class="float_r" id="thesaurusCurrent"></span>
											</div>
											<!--// 목록정보 end -->
											<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
											<div class="paging" id="paging">
											</div>
											<!--// 페이징 end -->
											<!--//목록 end -->
										</div>
										<!--// dic_list end -->
									</div>
									<!-- 유의어사전 목록 end -->
									
									<!-- 유의어사전 편집 start -->
									<div class="win_cont_right3 clear2">
										<!-- dic_edit start -->
										<div class="dic_edit">
											<!-- 편집 양식 start -->
											<p class="mb_5">유의어 사전 편집</p>
											<fieldset>
												<legend>유의어 사전 편집 양식</legend>
												<table class="tbl_type06">
												<caption>유의어 사전 편집 양식</caption>
												<colgroup>
													<col style="width:120px;" />
													<col />
												</colgroup>
												<tbody>
													<tr>
														<th scope="row"><label for="form_01">키워드</label></th>
														<td>
															<input type="text" class="white w190px" id="keyword" title="키워드 입력" readonly="readonly" />
															<input type="hidden" id="chAddFlag" value="0" />
															<input type="hidden" id="regDate" value="0" />
															<!-- <a href="#" class="btn b_gray ssmall">추가</a> -->
														</td>
													</tr>
													<tr>
														<th scope="row">형태소 분석결과</th>
														<td><label id="chKeyword"></label></td>
													</tr>
													<tr>
														<td colspan="2">
															<input type="radio" name="way" value="1" checked="checked" /><label for="form_a_01_1">단방향</label>
															<input type="radio" name="way" value="2" /><label for="form_a_01_1">양방향</label> 
														</td>
													</tr>
													<tr>
														<td colspan="2">
															<input type="text" class="white w120px" id="addKeyword" title="키워드 입력" maxlength="50" />
															<a href="#" id="btnAddEditKeyword" class="btn b_gray ssmall">추가</a>
														</td>
													</tr>
												</tbody>
												</table>
											</fieldset>
											<!--// 편집 양식 end -->
					
											<!-- 편집 양식 start -->
											<p class="font_blk_b mb_5"></p>
											<fieldset>
												<legend></legend>
												<table class="tbl_type06">
												<caption></caption>
												<colgroup>
													<col style="width:120px;" />
													<col />
												</colgroup>
												<tbody>
													<tr>
														<th scope="row"><label for="form_01">형태소 분석 결과</label></th>
														<td><label id="morpheme"></label><input type="hidden" id="chFlag" value="0" /></td>
													</tr>
													<tr>
														<th scope="row">색인어휘 추출 결과</th>
														<td><label id="extractor"></label></td>
													</tr>
												</tbody>
												</table>
											</fieldset>
											<div class="clear2 mt_20">	
												<fieldset><legend>검색</legend>
												<div class="wbox float_l" id="thesaurusKeywordList" >
													<!-- <p>국민은행<a href="#" class="btn ssmall b_gray">삭제</a></p>
													<p>국민은행<a href="#" class="btn ssmall b_gray">삭제</a></p>
													<p>국민은행<a href="#" class="btn ssmall b_gray">삭제</a></p>
													<p>국민은행<a href="#" class="btn ssmall b_gray">삭제</a></p> -->
												</div>
		
												<div class="wbtn float_r">
													<a href="#" id="btnAllDel" class="btn ssmall b_blue2">전체 삭제</a>
													<a href="#" id="btnAdd" class="btn ssmall b_blue2 mt_20" style="padding:0 17px;">등록</a>
												</div>
												</fieldset>
											</div>
										</div>
										<!--//dic_edit end -->
									</div>
									<!--// 유의어사전 편집 end -->
								</div>
								<!--// win_contarea end -->
							</div>
							<!--//유의어사전 end -->
						</div>
						<!--//본문 end -->
						<!-- 유의어사전 업/다운로드 팝업레이어 -->
						<div class="modal_popup w700px" id="basic-modal-dic_view" ></div>
					</div>
					<!--//right area end -->
				</div>
				<!--//cont end -->
			</div>
			<!--//content end -->
		</div>
		<!--//wrapper end -->
	</body>
</html>