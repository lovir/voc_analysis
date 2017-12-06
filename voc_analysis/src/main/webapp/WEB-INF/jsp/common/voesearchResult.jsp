<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<c:choose>
<c:when test="${not empty searchResultList }">
	
	<script type="text/javascript">

	$(function () {
		// 점유율 표시
		<c:if test="${not empty searchResultList.share}">
			var percent = "<c:out value='${searchResultList.share.value}' escapeXml='false' />"; 
			var share = "키워드 : <span class=''font_yb'><c:out value='${searchResultList.share.id}'/></span> / ";
			share += "점유율 : <span class='font_yb'>" + percent + "%</span>&nbsp;";
			$('#voc_percentage<c:out value="${param.compare}" />').html(share);
		</c:if>
	});
	
	</script>
	
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


</c:when>
<c:otherwise>
			<div class="win win_full" id="search_result">
				<div class="win_head clear2">
					<span class="win_tit bg_none">검색결과</span>
					<ul class="win_btnset">
					<li><a href="javascript:;" class="btn_sh" title="접기"></a></li>
					</ul>
				</div>
				<div id="vocSearchTeam" class="win_contarea align_c">
					<div class="graph_in">해당 검색정보가 없습니다.</div>
				</div>
			</div>
</c:otherwise>
</c:choose>