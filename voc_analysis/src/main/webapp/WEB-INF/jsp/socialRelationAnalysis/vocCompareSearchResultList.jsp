<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>


				<input type="hidden" name="share<c:out value="${type}"/>" value="<c:out value='${searchResultList.share}' />" />
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
									</c:choose>
									<c:otherwise>
										제목없음
									</c:otherwise>
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
				<div id="paging<c:out value='${type}'/>" class="paging">
					<ui:pagination paginationInfo = "${searchResultList.paginationInfo}" type="image" jsFunction="pageNavi" />
				</div>
                
