<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<div class="list_info">
	<span class="float_l">총 
		<strong><c:out value="${totalSize}"/></strong> 건
	</span>
	<span class="float_r">
		<c:out value="${currentPageNo}"/>/<c:out value="${endPageNo}"/> 페이지
	</span>
</div>
<div id="paging" class="paging">
	<ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="pageNavi" />
</div>