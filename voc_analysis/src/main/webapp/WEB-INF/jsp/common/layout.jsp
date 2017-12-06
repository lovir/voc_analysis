<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
	<title>음성 VOC 분석 시스템 : <tiles:getAsString name="title"/></title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/datepicker_kr.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.treeview.js'/>"></script>
	<script src="<c:url value='/resources/js/highcharts.js'/>"></script>
	<script src="<c:url value='/resources/js/highcharts-3d.js'/>"></script>
	<script src="<c:url value='/resources/js/highcharts-more.js'/>"></script>
	<script src="<c:url value='/resources/js/modules/exporting.js'/>"></script>
	<script src="<c:url value='/resources/js/modules/no-data-to-display.js'/>"></script>
</head>

<body>

<div id="skipnavi">
	<ul>
	<li><a href="#content">주메뉴 바로가기</a></li>
	<li><a href="#cont_body">본문 바로가기</a></li>
	</ul>
</div>
<hr />

<div id="wrapper">

	<!-- header start -->
	<div class="header">
		<tiles:insertAttribute name="header" />
	</div>
	<!--// header end -->

	<hr />

	<!-- content start -->
	<div id="content">

		<div class="cont">

			<!-- left area start -->
			<div class="left_area">
				<tiles:insertAttribute name="menu" />
			</div>
			<!--// left area end -->

			<!-- right area start -->
			<div class="right_area">
				<tiles:insertAttribute name="body" />
				<!-- footer start -->
				<div id="footer"><tiles:insertAttribute name="footer" /></div>
				<!--// footer end -->
			</div>
			<!--// right area end -->

		</div>

	</div>
	<!--// content end -->

</div>
</body>
</html>