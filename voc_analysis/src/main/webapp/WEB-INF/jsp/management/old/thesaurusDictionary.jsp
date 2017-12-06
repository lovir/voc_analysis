<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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

	<!-- location start -->
	<div class="loc_wrap clear2">
		<ul>
			<li>홈</li>
			<li>설정</li>
			<li>사전관리</li>
			<li>유의어사전 관리</li>
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
			<li class="on"><a href="#">유의어 사전 관리</a></li>
			<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/userDictionaryInit.do">사용자 사전 관리</a></li>
			<%-- <li><a href="<c:out value='${pageContext.request.contextPath}' />/management/userCompoundDictionaryInit.do">사용자복합어사전 관리</a></li>
			<li><a href="<c:out value='${pageContext.request.contextPath}' />/management/stopwordDictionaryInit.do">불용어 사전 관리</a></li> --%>
			</ul>
		</div>
		<!--// main tab end -->


		<!-- 유의어 사전 start -->
		<div class="win win_full">
			<div class="win_head clear2">
				<span class="win_tit bg_none">유의어 사전</span>

				<ul class="win_btnset">
				<li><a href="#" class="btn_dic_add" value="8" title="사전 등록"></a></li>
				<li><a href="#" class="btn_dic_down" value="8" title="사전 다운로드"></a></li>
				<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea clear2">

				<!-- 유의어 사전 목록 start -->
				<div class="win_cont_left clear2">
					<div class="dic_list">

						<!-- 목록 start -->
						<div class="clear2">	
							<fieldset><legend>검색</legend>
							<span class="float_l">
								<input type="text" id="searchKeyword" class="w100px gray" title="검색어 입력" maxlength="50" />
								<a href="#" id="btnSearch" class="btn ssmall b_gray">검색</a>
								<a href="#" id="btnAddKeyword" class="btn ssmall b_gray">추가</a>
							</span>

							<span class="float_r">
								<select id="searchSort" title="정렬조건 선택">
									<option value="KEYWORD ASC">가나다 순</option>
									<option value="REG_DATE DESC">수정일 순</option>
									<option value="APPLY ASC">미적용 순</option>
								</select>
								<select id="pageSize" name="pageSize" title="출력건수 선택">
									<option value="10">10 건</option>
									<option value="20">20 건</option>
								</select>
								<a href="#" id="btnApply" class="btn ssmall b_orange">전체 적용</a>
								<a href="#" id="btnSelectedDelete" class="btn ssmall b_orange">선택 삭제</a>
							</span>
							</fieldset>
						</div>

						<fieldset><legend>미사용자 목록</legend>
						<table class="tbl_type04 mt_5">
						<caption>미사용자 목록</caption>
						<colgroup>
							<col style="width:3%;" />
							<col style="width:28%;" />
							<col />
							<col style="width:13%;" />
							<%-- <col style="width:18%;" />
							<col style="width:18%;" /> --%>
							<col style="width:26%;" />
						</colgroup>

						<thead>
						  <tr>
							<th scope="col"><input type="checkbox" title="전체선택" /></th>
							<th scope="col">키워드</th>
							<th scope="col">유의어수</th>
							<th scope="col">적용</th>
<!-- 							<th scope="col">부서명</th>
							<th scope="col">등록자</th> -->
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
						<!--// 목록 end -->
					</div>
				</div>
				<!--// 유의어 사전 목록 end -->

				<!-- 유의어 사전 편집 start -->
				<div class="win_cont_right3 clear2">
					<div class="dic_edit">

						<!-- 편집 양식 start -->
						<p class="font_blk_b mb_5">유의어 사전 편집</p>
						<fieldset><legend>유의어 사전 편집 양식</legend>
						<table class="tbl_type06">
						<caption>유의어 사전 편집 양식</caption>
						<colgroup>
							<col style="width:120px;" />
							<col />
						</colgroup>
						<tbody>
							<tr>
								<th scope="row"><label for="form_01">키워드</label></th>
								<td><input type="text" class="white w120px" id="keyword" title="키워드 입력" readonly="readonly" /><input type="hidden" id="chAddFlag" value="0" /><input type="hidden" id="regDate" value="0" /><!-- <a href="#" class="btn b_gray ssmall">추가</a> --></td>
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
								<td colspan="2"><input type="text" class="white w120px" id="addKeyword" title="키워드 입력" maxlength="50" /><a href="#" id="btnAddEditKeyword" class="btn b_gray ssmall">추가</a></td>
							</tr>
						</tbody>
						</table>
						</fieldset>
						<!--// 편집 양식 end -->

						<!-- 편집 양식 start -->
						<p class="font_blk_b mb_5"></p>
						<fieldset><legend></legend>
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
							<tr>
								<td colspan="2" width="100px">
									<div style="height:100px;overflow-y:auto;overflow-x:hidden;position:relative;border: 1 solid;">
										<ul id="thesaurusKeywordList">
										</ul>
									</div>
									<a href="#" id="btnAllDel" class="btn b_gray ssmall">전체삭제</a>
									<a href="#" id="btnAdd" style="color:#d2d2d2 !important;" class="btn b_gray ssmall">등록</a>
								</td>
							</tr>
						</tbody>
						</table>
						</fieldset>




					</div>
				</div>
				<!--// 유의어 사전 편집 end -->
			

			</div>
		</div>
		<!--// 유의어 사전 end -->



	</div>
	<!--// 본문 end -->
	
<!-- 유의어사전 업/다운로드 팝업레이어 -->
<div class="modal_popup w700px" id="basic-modal-dic_view" ></div>