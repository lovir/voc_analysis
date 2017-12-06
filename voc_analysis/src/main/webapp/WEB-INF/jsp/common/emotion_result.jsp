<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>

<div id="topbox" class="box_2">
	<p>${emotionTitle} Top 10 목록</p>
	<ul class="keyword_list">
	<input type="hidden" id="resultkeyword" value="${emotionList.get(0)}">
		<c:forEach items="${emotionList}" var="emotionList" varStatus="elNum">
			<li><span class="number">${elNum.count}</span><a href="javascript:;"><span class="key_word" name="${emotionList}">${emotionList}</span></a></li>
		</c:forEach>
	</ul>     
</div>