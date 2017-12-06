<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<div class="box_1">
<%-- 	<a href="#" class="positiveBtn">
		<div class="gray_box1 mr_5">
			<p><img src="../resources/images/common/icon_smile.png" alt="긍정"></p>
			<p><span class="t_16">긍정</span></p>
			<p><span class="t_26">${percentage.get(0)}</span><span class="t_16">%</span></p>
		</div>
	</a>
 --%>
 	<a href="javascript:;" class="positiveBtn">
 	<div id="posiBox"class="gray_box1 off mr_5">
			<p><img src="../resources/images/common/icon_smile_off.png" alt="긍정" id="posiImg"></p>
			<p><span class="t_16">긍정</span></p>
			<c:choose>
				<c:when test="${percentage.get(0) eq 'NaN'}">
					<p><span class="t_26">0.0</span><span class="t_16">%</span></p>
				</c:when>
				<c:otherwise>			
					<p><span class="t_26">${percentage.get(0)}</span><span class="t_16">%</span></p>
				</c:otherwise>
			</c:choose>
	</div>
	</a> 
   <a href="javascript:;" class="DenyBtn">
	<div id="denBox" class="gray_box1 off">
			<p><img src="../resources/images/common/icon_angry_off.png" alt="부정" id="denImg"></p>
			<p><span class="t_16">부정</span></p>
			<c:choose>
				<c:when test="${percentage.get(1) eq 'NaN'}">
					<p><span class="t_26">0.0</span><span class="t_16">%</span></p>
				</c:when>
				<c:otherwise>
					<p><span class="t_26">${percentage.get(1)}</span><span class="t_16">%</span></p>
				</c:otherwise>
		 	</c:choose>
	</div>
	</a> 
</div>
