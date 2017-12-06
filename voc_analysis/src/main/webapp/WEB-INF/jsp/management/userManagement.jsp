<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript" src="<c:url value='/resources/js/voc/userManagement.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
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
		<li>환경설정</li>
		<li>사용자 관리</li>
		</ul>
	</div>
	<!--// location end -->

	<!-- 본문 start -->
	<div class="cont_body">
	
		<div class="cont_head">
			<span class="cont_tit">사용자 관리</span><span class="cont_desc">사용자를 등록하고 관리하는 기능을 제공합니다.</span>
		</div>

		<!-- 사용자 목록 start -->
		<div class="win win_full">
			<div class="win_head clear2">
				<span class="win_tit bg_none">사용자 목록</span>

				<ul class="win_btnset">
				<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea align_c">

				<!-- 목록 start -->
				<div class="p_20">
					
					<div class="clear2">	
						<fieldset><legend>검색</legend>
						<span class="float_l">
							<select id="conditionY">
							<option value="1">조직명</option>
							<option value="2">이름</option>
							<option value="3">이메일</option>
							<option value="4">연락처</option>
							</select>
							<input type="text" id="termY" class="w150px gray" title="검색어 입력" /><a href="#" id="btnSearchY" class="btn ssmall b_gray">검색</a>
						</span>

						<span class="float_r">
							<a href="#" id="btnSelectedDelete" class="btn ssmall b_orange">선택 삭제</a>
						</span>
						</fieldset>
					</div>
					
					<fieldset><legend>사용자 목록</legend>

					<table class="tbl_type04 mt_5">
					<caption>사용자 목록</caption>
					<colgroup>
						<col style="width:3%;" />
						<col style="width:7%;" />
						<col />
						<col style="width:18%;" />
						<col style="width:22%;" />
						<col style="width:13%;" />
						<col style="width:15%;" />
					</colgroup>

					<thead>
					  <tr>
						<th scope="col"><input type="checkbox" id="selectAllY" title="전체선택" /></th>
						<th scope="col">No.</th>
						<th scope="col">조직명</th>
						<th scope="col">이름</th>
						<th scope="col">이메일</th>
						<th scope="col">연락처</th>
						<th scope="col">등록일</th>
					  </tr>
					</thead>

					<tbody id="list_contentsY">
					</tbody>
					</table>
					</fieldset>

					<!-- 목록정보 start -->
					<div class="list_info">
						<span class="float_l">총 <strong id="totCntY"></strong> 건</span><!-- <span class="float_r">3/10 페이지</span> -->
					</div>
					<!--// 목록정보 end -->
					
					<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
					<div class="paging" id="pagingY">
					</div>
					<!--// 페이징 end -->
					
				</div>
				<!--// 목록 end -->

			</div>
		</div>
		<!--// 사용자 목록 end -->



		<!-- 사용자 등록 start -->
		<div class="win win_full">
			<div class="win_head clear2">
				<span class="win_tit bg_none">사용자 등록</span>

				<ul class="win_btnset">
				<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea align_c">

				<!-- 목록 start -->
				<div class="p_20">
					
					<div class="clear2">	
						<fieldset><legend>검색</legend>
						<span class="float_l">
							<select id="conditionN">
							<option value="1">조직명</option>
							<option value="2">이름</option>
							<option value="3">이메일</option>
							<option value="4">연락처</option>
							</select>
							<input type="text" id="termN" class="w150px gray" title="검색어 입력" /><a href="#" id="btnSearchN" class="btn ssmall b_gray">검색</a>
						</span>

						<span class="float_r">
							<a href="#" id="btnSelectedAdd" class="btn ssmall b_orange">선택 등록</a>
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
						<col style="width:18%;" />
						<col style="width:22%;" />
						<col style="width:13%;" />
						<col style="width:15%;" />
					</colgroup>

					<thead>
					  <tr>
						<th scope="col"><input type="checkbox" id="selectAllN" title="전체선택" /></th>
						<th scope="col">No.</th>
						<th scope="col">조직명</th>
						<th scope="col">이름</th>
						<th scope="col">이메일</th>
						<th scope="col">연락처</th>
						<th scope="col">등록</th>
					  </tr>
					</thead>

					<tbody id="list_contentsN">
					</tbody>
					</table>
					</fieldset>


					<!-- 목록정보 start -->
					<div class="list_info">
						<span class="float_l">총 <strong id="totCntN"></strong> 건</span><!-- <span class="float_r">3/10 페이지</span> -->
					</div>
					<!--// 목록정보 end -->


					<!-- 페이징 start : 해당 페이지 span에 class="on" 삽입 -->
					<div class="paging" id="pagingN">
					</div>
					<!--// 페이징 end -->

				</div>
				<!--// 목록 end -->

			</div>
		</div>
		<!--// 사용자 등록 end -->

	</div>
	<!--// 본문 end -->