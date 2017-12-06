<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<!-- 모달팝업 : 불만문서 상세보기 start -->
	<%-- <div id="complain" class="win win_full" style="margin-top:0">
		<div class="win_head clear2">
			<span class="win_tit bg_none">불만문서 상세보기</span>
		</div>
		
		<div class="win_contarea clear2 complain_modal">
			<c:out value="${complainViewResult.CONTENT}" />
		</div>
		
		<div class="align_c"><a href="#" class="btn b_gray medium mt_10 simplemodal-close">닫기</a></div>
	</div>
	<hr /> --%>
	<!--// 모달팝업 : 불만문서 상세보기 end -->
		
	<!-- 모달팝업 : 상세보기 start -->
	<div id="detail" class="win win_full" style="margin-top:0">
		<div class="win_head clear2">
			<span class="win_tit bg_none">검색결과 상세보기</span>
		</div>
		
		<div class="win_contarea clear2 pt_10">
			<c:choose>
				<c:when test="${empty detailViewResult.TITLE}">
　　					<p>제목 없음</p>
				</c:when>
				<c:otherwise>
					<p><c:out value="${detailViewResult.TITLE}" /></p>
				</c:otherwise>
			</c:choose>
			<span class="result_date">
				<fmt:parseDate value="${detailViewResult.REGDATE}" var="dateFmt" pattern="yyyyMMddHHmmss"/>
				<fmt:formatDate value="${dateFmt}" pattern="yyyy.MM.dd"/>
			</span>
			<div class="p_15">
				<table	class="tbl_type03 mb_20">
					<tr>
						<th>접수 채널</th>
						<td><c:out value="${detailViewResult.VOCCHANNEL}" /></td>
						<th>종류</th>
						<td><c:out value="${detailViewResult.VOCRECTYPE}" /></td>
					</tr>
					<tr>
						<th>접수 분류</th>
						<td>${detailViewResult.CATEGORY}</td>
						<th>처리주무부서</th>
						<td>${detailViewResult.REPDEPT}</td>
					</tr>
					<tr>
						<th>민원발생역</th>
						<td>${detailViewResult.EXT_STATION}</td>
						<th>답변만족도 등급</th>
						<td>${detailViewResult.REPLEVEL}</td>
					</tr>
				</table>
				<div class="result_view_cont">${detailViewResult.CONTENT}</div>
				<div class="result_view_cont line">${detailViewResult.REPCONT}</div>
				
			</div>
		</div>
		<div class="align_c mb_10 p_10"><a href="#"  class="btn b_gray medium simplemodal-close">확인</a></div>
	</div>
	<hr />
	<!--// 모달팝업 : 상세보기 end -->

	<!-- 모달팝업 : 유사문서 start -->
	<div id="alike" class="win win_full" style="margin-top:0">
		<div class="win_head clear2">
			<span class="win_tit bg_none">유사문서 TOP 10 결과</span>
		</div>
		<div class="win_contarea clear2">
			<div class="result_list">
				<ul class="result_group">
				<c:forEach var="result" items="${alikeResult}" varStatus="status">
					<li>
						<dl>
							<dt>
								<a href="javascript:;" class="modal_btn"><c:out value="${result.TITLE}" /></a>
									<fmt:parseDate value="${result.REGDATE}" var="dateFmt" pattern="yyyyMMddHHmmss"/>
								<span class="result_date"><fmt:formatDate value="${dateFmt}" pattern="yyyy.MM.dd"/></span>
								<span class="doc_similar">유사도 : <c:out value="${result.WEIGHT}" />%</span>
							</dt>
							
							<dd>
								<c:out value="${result.CONTENT}" />
							</dd>
						</dl>
					</li>
				</c:forEach>
				</ul>
				<!-- 목록정보 start -->
				<!-- <div class="list_info">
					<span class="float_l">총 <strong></strong> 건</span>
					<span class="float_r">/ 페이지</span>
				</div> -->
				<!--// 목록정보 end -->
			</div>
		</div>
		<div class="align_c mt_10" id="m_close">
			<a href="#" class="btn b_gray medium simplemodal-close">확인</a>
		</div>
	</div>
	<hr />
	<!--// 모달팝업 : 유사문서 end -->
	
	<!-- 모달팝업 : OpenAPI start -->
	<!--// 모달팝업 : OpenAPI end -->
