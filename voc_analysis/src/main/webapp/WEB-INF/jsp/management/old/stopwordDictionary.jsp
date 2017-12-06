<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/stopwordDictionary.js'/>"></script>
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
		<li>사전관리</li>
		<li>사불용어사전 관리</li>
		</ul>
	</div>
	<!--// location end -->

	<!-- 본문 start -->
	<div class="cont_body">
	
		<div class="cont_head">
			<span class="cont_tit">사전 관리</span><span class="cont_desc">용어사전을 등록하고 관리하는 기능을 제공합니다.</span>
		</div>

		<!-- main tab start -->
		<div class="main_tab clear2">
			<ul>
			<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/thesaurusDictionaryInit.do">유의어 사전 관리</a></li>
			<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/userDictionaryInit.do">사용자 사전 관리</a></li>
			<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/userCompoundDictionaryInit.do">사용자복합어사전 관리</a></li>
			<li class="on"><a href="#">불용어 사전 관리</a></li>
			</ul>
		</div>
		<!--// main tab end -->


					<!-- 불용어 사전 start -->
					<div class="win win_full">
						<div class="win_head clear2">
							<span class="win_tit bg_none">불용어 사전</span>

							<ul class="win_btnset">
							<!-- <li><a href="#" class="btn_dic_add" value="2"  title="사전 등록"></a></li>
							<li><a href="#" class="btn_dic_down" value="2"  title="사전 다운로드"></a></li> -->
							<li><a href="#" class="btn_sh" title="접기"></a></li>
							</ul>
						</div>
						
						<div class="win_contarea clear2">


							<form id="stopwordListForm" name="stopwordListForm" method="post">
							<input type="hidden" name="currentPage" value="1"/>
							<input type="hidden" name="selectedKeyword" /> 
							<!-- 불용어 사전 목록 start -->
							<div class="win_cont_left clear2">
								<div class="dic_list">

									<!-- 목록 start -->
									<div class="clear2">	
										<fieldset><legend>검색</legend>
										<span class="float_l">
											<select id="condition" name="condition">
											<option value="01">키워드</option>
											<option value="02">등록자</option>
											<!-- <option value="02">부서명</option> -->
											</select>
											<input type="text" name="keyword" value="<c:out value='${dictionaryVo.keyword}'/>" class="w100px gray" title="검색어 입력" /><a href="javascript:;" class="btn ssmall b_gray" onclick="fnSearchAction();return false;">검색</a>
										</span>

										<span class="float_r">
											<select id="pageSize" name="pageSize" title="출력건수 선택">
												<option value="10">10 건</option>
												<option value="20">20 건</option>
											</select>
											<a href="javascript:;" onclick="fnKeywordApply(); return false;" class="btn ssmall b_orange">선택 적용</a><a href="javascript:;" onclick="fnKeywordDelete(); return false;" class="btn ssmall b_orange">선택 삭제</a>
										</span>
										</fieldset>
									</div>

									<fieldset><legend>미사용자 목록</legend>
									<table class="tbl_type04 mt_5">
									<caption>미사용자 목록</caption>
									<colgroup>
										<col style="width:3%;" />
										<col style="width:7%;" />
										<col />
										<col style="width:8%;" />
										<col style="width:18%;" />
										<col style="width:18%;" />
										<col style="width:16%;" />
									</colgroup>

									<thead>
									  <tr>
										<th scope="col"><input type="checkbox" name="totalIdCheck" title="전체선택" /></th>
										<th scope="col">No.</th>
										<th scope="col">불용어</th>
										<th scope="col">적용</th>
										<!-- <th scope="col">부서명</th> -->
										<th scope="col">등록자</th>
										<th scope="col">수정일</th>
									  </tr>
									</thead>

									<tbody>
									<c:forEach var="result" items="${stopwordList}" varStatus="status">
										<tr>
											<td name='<c:out value="${result.keyword}" />'><input type="checkbox" name="checkKeyword" value="<c:out value="${result.no}" />" /></td>
											<td><c:out value="${paginationInfo.totalRecordCount-(paginationInfo.firstRecordIndex+status.index)}" /></td>
											<td><c:out value="${result.keyword}" /></td>
											<td>
											<c:choose>
											<c:when test="${result.applyYn eq 'Y'}">예</c:when>
											<c:otherwise>-</c:otherwise>
											</c:choose>
											</td>
											<!-- <td>부서명</td> -->
											<td><c:out value="${result.regId}" /></td>
											<td><c:out value="${result.regDate}" /></td>
										</tr>
									</c:forEach>

									</tbody>
									</table>
									</fieldset>


									<!-- 목록정보 start -->
									<div class="list_info">
										<span class="float_l">총 <strong><c:out value="${totalCount}" /></strong> 건</span><span class="float_r"><c:out value="${dictionaryVo.currentPage}" />/<c:out value="${paginationInfo.totalPageCount}" /> 페이지</span>
									</div>
									<!--// 목록정보 end -->


									
									<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
									<div id="paging" class="paging">
										<ui:pagination paginationInfo = "${paginationInfo}"
											   type="image"
											   jsFunction="fnPageNavi"
											   />
									</div>
									<!--// 페이징 end -->
				
				
									<!--// 목록 end -->
								</div>
							</div>
							<!--// 불용어 사전 목록 end -->
							</form>
							

							<!-- 불용어 사전 편집 start -->
							<div class="win_cont_right3 clear2">
								<div class="dic_edit">

									<!-- 편집 양식 start -->
									<p class="font_blk_b mb_5">불용어 사전 편집</p>
									<fieldset><legend>불용어 사전 편집 양식</legend>
									<table class="tbl_type06">
									<caption>불용어 사전 편집 양식</caption>
									<colgroup>
										<col style="width:70px;" />
										<col />
									</colgroup>
									<tbody>
										<tr>
											<th scope="row"><label for="form_01">불용어</label></th>
											<td><input type="text" id="addKeyword" name="addKeyword" class="white w150px" title="키워드 입력" /><a href="javascript:;" id=addKeyword" onclick="fnKeywordAdd(); return false;" class="btn b_gray ssmall">추가</a></td>
										</tr>
										<tr>
											<th scope="row"><label for="form_02">필드</label></th>
											<td>
												<select id="form_02">
													<option>전체</option>
												</select>
											

											</td>
										</tr>
									</tbody>
									</table>
									</fieldset>
									<!--// 편집 양식 end -->


								</div>
							</div>
							<!--// 불용어 사전 편집 end -->
						

						</div>
					</div>
					<!--// 불용어 사전 end -->



				</div>
				<!--// 본문 end -->
				
				<form id="stopwordPageForm" name="stopwordPageForm" method="post">
					<input type="hidden" name="currentPage" /> 
					<input type="hidden" name="keyword" value="<c:out value='${dictionaryVo.keyword}'/>" /> 
					<input type="hidden" name="condition" value="<c:out value='${dictionaryVo.condition}'/>" /> 
				</form>
				
<!-- 유의어사전 업/다운로드 팝업레이어 -->
<div class="modal_popup w700px" id="basic-modal-dic_view" ></div>