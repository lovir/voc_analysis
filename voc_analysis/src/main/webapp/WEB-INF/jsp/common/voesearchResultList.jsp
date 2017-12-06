<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${not empty searchResultList}">
	<div class="win_head clear2">
		<span class="win_tit">검색결과</span>
		<span class="win_date" id="voc_percentage<c:out value="${param.compare}" />">
			<c:if test="${not empty searchResultList.share}">
				키워드 : <span class=''font_yb'><c:out value="${searchResultList.share.id}"/></span> / 점유율 : <span class='font_yb'><c:out value="${searchResultList.share.value}"/>%</span>&nbsp;
			</c:if>
		</span> <!-- 쉐어 쓰기 -->
		<ul class="win_btnset">
		<c:if test="${empty searchVo.compare}"> <!-- 비교분석에는 엑셀 다운로드 안쓰이므로, compare로 null일 경우만 엑셀다운로드 아이콘 띄움 -->
			<c:if test="${fn:length(searchResultList.listResult) > 0}"><!-- 검색결과 리스트 size가 0일경우 엑셀다운로드 아이콘 안 띄움 -->
				<li><a href="javascript:;" class="btn_xls" title="액셀파일 내려받기" id="<c:out value="${param.compare}" />"></a></li>
			</c:if>
		</c:if>
		<li><a href="#" class="btn_sh" title="접기"></a></li>
		</ul>
	</div>
	<div class="win_contarea p_0">
		<!-- tab start -->
		<div class="tab_wrap clear2">
			<ul class="tab_win" id="searchListGrp<c:out value="${param.compare}" />">
				<li>
					<a href='#' class='tab_tit<c:out value="${searchVo.needsType eq '' ? ' on' : ''}" />' id='tab_1' name=''>
						전체<span>(<fmt:formatNumber value="${searchResultList.totSize}" pattern="#,###" />)</span>
					</a>
				</li>
				<c:forEach var="result" items="${searchResultList.groupResult}" varStatus="status">
					<li>
						<a href='#' class='tab_tit<c:out value="${searchVo.needsType eq result.id ? ' on' : ''}" />' id='tab_1' name='${result.id}'>
							${result.id}<span>(<fmt:formatNumber value="${result.value}" pattern="#,###" />)</span>
						</a>
					</li>
				</c:forEach>
			</ul>
			<div class="tab_right mr_15">
				<fieldset><legend>목록갯수</legend>
					<select title="목록갯수 선택" class="" id="page<c:out value="${param.compare}" />" name="page">
						<option value="10" <c:out value="${not empty searchVo.pageSize && searchVo.pageSize eq 10 ? 'selected' : ''}" />>10개</option>
						<option value="20" <c:out value="${not empty searchVo.pageSize && searchVo.pageSize eq 20 ? 'selected' : ''}" />>20개</option>
						<option value="30" <c:out value="${not empty searchVo.pageSize && searchVo.pageSize eq 30 ? 'selected' : ''}" />>30개</option>
						<option value="50" <c:out value="${not empty searchVo.pageSize && searchVo.pageSize eq 50 ? 'selected' : ''}" />>50개</option>
					</select>
				</fieldset>
			</div>
		</div>
		<!--// tab end -->
	
		<!-- Tab 01 : 전체 start -->
		<div class="tab_cont cont_tab_01" id="searchResult<c:out value="${param.compare}" />">
	
			<div class="result_list">
				<%-- <input type="hidden" name="share" value="<c:out value='${searchResultList.share}' />" /> --%>
				<c:forEach var="result" items="${searchResultList.listResult}" varStatus="status">
					<ul class="result_group">
						<li>
							<input type="hidden" name="title" value="<c:out value='${result.TITLE}'/>" />
							<input type="hidden" name="content" value="<c:out value='${result.CONTENT_ORI}'/>" />
							<input type="hidden" name="newWin" value="${result.ID}"/>
							<dl>
								<dt>
									<c:choose>
										<c:when test="${!empty result.TITLE}">										
											<a href="javascript:;" class="newWindow">
												<%-- <a href="javascript:;" onclick="detailView('<c:out value='${result.ID}' />'); return false;" > --%>
												[<c:out value="${result.USE_MED_CD}" escapeXml="false" />] <c:out value="${result.TITLE}" escapeXml="false" />
											</a>
										</c:when>
										<c:otherwise>
											<input type="hidden" id="${result.ID}" name="content_ori" value="${result.TITLE}" />
											<a href="javascript:;" name='title' class='modal_btn' >
												[<c:out value="${result.USE_MED_CD}" escapeXml="false" />] 트위터 멘션
											</a>
										</c:otherwise>
									</c:choose>
									<span class="result_date">
										<c:out value="${fn:substring(result.REGDATE, 0, 4)}"/>/<c:out value="${fn:substring(result.REGDATE, 4, 6)}"/>/<c:out value="${fn:substring(result.REGDATE, 6, 8)}"/>
									</span>
									<c:choose>
										<c:when test="${!empty result.TITLE}">										
											<a href="javascript:;"  class='result_doc' name="<c:out value='${result.ID}' />">
												유사문서 보기
											</a>
										</c:when>
									</c:choose>
								</dt>
								<dd>
									<c:out value="${result.CONTENT}" escapeXml="false" />
								</dd>
							</dl>
						</li>
					</ul>
				</c:forEach>
				<div class="list_info">
					<span class="float_l">총 
						<strong><c:out value="${searchResultList.paginationInfo.totalRecordCount}"/></strong> 건
					</span>
					<span class="float_r">
						<c:out value="${searchResultList.paginationInfo.currentPageNo}"/>/<c:out value="${searchResultList.paginationInfo.lastPageNo}"/> 페이지
					</span>
				</div>
				<div id="paging<c:out value="${param.compare}" />" class="paging">
					<ui:pagination paginationInfo = "${searchResultList.paginationInfo}" type="image" jsFunction="pageNavi" />
				</div>
			</div>
		</div>
		<!-- Tab 01 : 전체 end -->
	
	</div>
</c:if>
<c:if test="${empty searchResultList}">
	<div class="win_head clear2">
		<span class="win_tit">검색결과</span>
		<span class="win_date" id="voc_percentage<c:out value="${param.compare}" />">
			<c:if test="${not empty searchResultList.share}">
				키워드 : <span class=''font_yb'><c:out value="${searchResultList.share.id}"/></span> / 점유율 : <span class='font_yb'><c:out value="${searchResultList.share.value}"/>%</span>&nbsp;
			</c:if>
		</span> <!-- 쉐어 쓰기 -->
		<ul class="win_btnset">
		<li><a href="#" class="btn_sh" title="접기"></a></li>
		</ul>
	</div>
	<div class="win_contarea align_c">
		<div class="graph_in">해당하는 검색정보가 없습니다.</div>
	</div>
</c:if>