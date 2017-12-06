<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>


	<c:choose>
		<c:when test="${ !empty searchResultList.listResult}">
		<!-- VOC 검색결과 start -->
			<div class="win_head clear2">
				<span class="win_tit">검색결과 <c:out value="${type}"/></span>
				<span class="win_date">
					키워드 : 
							<span class="font_yb" id="search_keyword">
								<c:out value="${searchResultList.keyword}" />
							</span> 
					/ 점유율 : 
							<span class="font_yb" id="share<c:out value="${type}"/>">
								<c:out value="${searchResultList.share}" />%
							</span>
				</span>
				<ul class="win_btnset">
					<li><a href="#" class="btn_sh" title="접기"></a></li>
				</ul>
			</div>
			
			<div class="win_contarea p_0">
		
			<!-- tab start -->
			<div class="tab_wrap clear2">
				<ul class="tab_win" id="searchCategory<c:out value="${type}"/>">
					<li>
						<a href="javascript:;" class="tab_tit on" id="tab_01<c:out value="${type}"/>" name="">전체<span>(<c:out value="${searchResultList.totalSize}" />)</span></a>
					</li>
					<c:forEach var="result" items="${searchResultList.groupResult}" varStatus="status">
					<li>
						<a href="javascript:;" class="tab_tit" id="tab_01<c:out value="${type}"/>" name="${result.name }"><c:out value="${result.name}" /><span>(<c:out value="${result.count}" />)</span></a>
					</li>
					</c:forEach>
				</ul>
				<div class="tab_right mr_15">
					<fieldset><legend>목록갯수</legend>
						<select id="pageSize<c:out value="${type}"/>" name="pageSize<c:out value="${type}"/>" title="목록갯수 선택" class="">
							<option value="10" <c:if test="${keywordRankingVo.pageSize eq 10}">selected </c:if>>10개</option>
							<option value="20" <c:if test="${keywordRankingVo.pageSize eq 20}">selected </c:if>>20개</option>
							<option value="30" <c:if test="${keywordRankingVo.pageSize eq 30}">selected </c:if>>30개</option>
							<option value="50" <c:if test="${keywordRankingVo.pageSize eq 50}">selected </c:if>>50개</option>
						</select>
					</fieldset>
				</div>
			</div>
			<!-- tab end -->
			
			<!-- list start -->
			<form id="searchResultForm<c:out value="${type}"/>" name="searchResultForm" method="post">
				<div class="tab_cont cont_tab_01">
					<div class="result_list" id="result_list<c:out value="${type}" />">
					<c:forEach var="result" items="${searchResultList.listResult}" varStatus="status">
						<ul class="result_group">
							<li>
								<input type="hidden" name="title" value="<c:out value='${result.TITLE}'/>" />
								<input type="hidden" name="content" value="<c:out value='${result.CONTENT_ORI}'/>" />
								<dl>
									<dt>
										<c:choose>
											<c:when test="${!empty result.TITLE}">
												<a href="javascript:;" onclick="detailView('<c:out value='${result.ID}' />'); return false;" >
													<c:out value="${result.TITLE}" escapeXml="false" />
												</a>						
											</c:when>
											<c:otherwise>
												제목없음
											</c:otherwise>
										</c:choose>
										<span class="result_date">
											<c:out value="${result.REGDATE}"/>
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
								<strong><c:out value="${searchResultList.totalSize}"/></strong> 건
							</span>
							<span class="float_r">
								<c:out value="${searchResultList.currentPage}"/>/<c:out value="${searchResultList.endPage}"/> 페이지
							</span>
						</div>
						<div id="paging<c:out value="${type}" />" class="paging">
							<ui:pagination paginationInfo = "${searchResultList.paginationInfo}" type="image" jsFunction="pageNavi" />
						</div>
					</div>
				</div>
			</form>
			<!-- list end -->
		</div>
		</c:when>
		<c:otherwise>
			<div class="win win_full" id="search_resultA">
				<div class="win_head clear2">
					<span class="win_tit">검색결과 <c:out value="${type}"/></span>
					<ul class="win_btnset">
					<li><a href="#" class="btn_sh" title="접기"></a></li>
					</ul>
				</div>
				<div id="vocSearchTeam<c:out value="${type}"/>" class="win_contarea p_0">
					<div class="graph_in"></div>									
				</div>
			</div>
		</c:otherwise>
	</c:choose>

<!--// 검색결과 end -->