<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Script-Type" content="text/javascript">
<meta http-equiv="Content-Style-Type" content="text/css">
<meta http-equiv="imagetoolbar" content="no">
<title>통합VOC 분석시스템 도움말</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/style.css'/>" />
<style type="text/css">

/* 도움말 */
/*11.02 수정 */
html,body {background-color:#d3d3d3;}

#wrapper_help {width:830px;padding:20px;}
.help_list > li {margin-bottom:40px;}
/* .help_list li dl dt {font-family:"NanumGothicBold";color:#d19900;font-size:16px;margin-bottom:10px;} #ffbc06 <- #d19900보다 밝은색 */
.help_list li dl dt {font-family:"NanumGothicBold";color:#000000;font-size:16px;margin-bottom:10px;}
.help_list li dl dd {margin-top:13px;font-size:14px;padding-left:12px;margin-left:10px;font-weight:lighter;color:#505050;} 
.help_list li dl dd.bullet_none {background:none;}
.help_list li dl dd > span {font-family:"NanumGothicBold";color:#000;padding-right:5px;}
.help_list li dl dd img {border:2px solid #cdcdcd;}

.tbl_type03 {width:804px;font-size:14px;border-left:1px solid #cdcdcd;border-top:1px solid #cdcdcd;border-right:2px solid #cdcdcd;border-bottom:2px solid #cdcdcd;}
.tbl_type03 th {background:#f5f5f5;padding:7px 5px;border:1px solid #ffffff;font-family:"NanumGothicBold";font-weight:normal;text-align:center;}
.tbl_type03 td {border:1px solid #ffffff;padding:7px;text-align:center;}
.tbl_type03 td.align_l {text-align:left;padding:10px 10px 5px 10px;}
.tbl_type03 td ul li {margin-bottom:5px;font-size:12px;}
.tbl_type03 td ul li span {font-family:"NanumGothicBold";color:#000;padding-right:3px;}

</style>
</head>

<body>
<div id="wrapper_help">
	<ol class="help_list">
	<li>
		<dl>
		<dt>1. DiXSi | 음성 VOC 분석 시스템 이란?</dt>
		<dd class="bullet_none"><img src="<c:url value='/resources/images/help/help_dixsi_01.png' />" width="800" alt="" /></dd>
		<dd><b>DiXSi 음성VOC 분석시스템</b>은 국내 최고에 음성인식 기술을 가지고 있는 셀바스AI와 비정형데이터 분석 분야에 최고에 기술을 보유한 다이퀘스트가 컨소시엄을 통해 개발한 음성 인식 및 체계적 분석 결과를 확인할 수 있는 음성 데이터 분석 시스템 입니다.</dd>
		</dl>
	</li>

	<li>
		<dl>
		<dt>2. DiXSi | 음성 VOC분석 시스템 아키텍처</dt>
		<dd class="bullet_none"><img src="<c:url value='/resources/images/help/help_dixsi_02.png' />" width="800" alt="" /></dd>
		<dd>DiXSi 음성 VOC분석 시스템에 아키텍처. 녹취 시스템으로부터 실시간으로 음성 텍스트 추출(ASR)하여 음성인식 처리 프로세스를 통해 텍스트를 추출하게 되고, 추출된 텍스트 정보 VOC분석 수행 후 시각화를 통해 분석리포트를 제공 합니다. 또한 분석 정보를 연계시스템으로 연계할 수 있도록 API을 제공 합니다. </dd>
		</dl>
	</li>

	<li>
		<dl>
		<dt>3. 음성VOC 대시보드</dt>
		<dd class="bullet_none"><img src="<c:url value='/resources/images/help/help_dixsi_03.png' />" width="800" alt="" /></dd>
		<dd>음성VOC 대시보드는 준 실시간 음성인식 처리 현황과 VOC비정형분석 결과에 대한 관심 키워드 추이, 감성 분석, 지역별 민원발생 현황, 이슈키워드에 대한 랭킹 및 클라우드 등 기능을 제공하고 고객에 맞춤 화면 커스터마이징을 지원 합니다.</dd>
		</dl>
	</li>

	<li>
		<dl>
		<dt>4. 음성인식 소개</dt>
		<dd class="bullet_none"><img src="<c:url value='/resources/images/help/help_dixsi_04.png' />" width="800" alt="" /></dd>
		<dd><span>음성민원 인식: </span>음성 민원 인식결과를 조회할 수 있습니다. 통화내용, 상담원 및 고객 전화번호, 통화길이, 카테고리, 기간별 설정을 통해 통화내역 검색기능을 제공합니다. 또한 검색된 통화내역을 재생하면서 인식결과를 함께 확인 가능합니다. </dd>
		<dd><span>음성인식 모니터링: </span>음성 민원 수집과 인식 현황을 조회할 수 있습니다. 민원 수집 통계와 음성 인식 상태-준비, 진행, 완료, 에러-에 따른 건수를 확인할 수 있습니다.</dd>
		</dl>
	</li>
	
	<li>
		<dl>
		<dt>5. VOC분석 소개</dt>
		<dd class="bullet_none"><img src="<c:url value='/resources/images/help/help_dixsi_05.png' />" width="800" alt="" /></dd>
		<dd><span>키워드랭킹분석 :</span>화면별로 랭킹분석을 조회합니다.</dd>
		<dd><span> - 종합랭킹 :</span>통합VOC 분석시스템에 등록된 키워드에 대한 랭킹을 조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		<dd><span> - 관심키워드랭킹 :</span>사용자가 등록한 관심키워드에 대한 랭킹을 조회합니다. 제목, 본문을 검색할 수 있습니다. ※설정 > 키워드관리 > 관심키워드 관리에 활성여부가 [활성]인 관심키워드를 표시합니다. MAX 10개 표시 가능.</dd>
		<dd><span> - 이슈키워드랭킹 :</span>분석기간에 TOP10에 속하는 키워드(이슈키워드) 대한 랭킹을 조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		<dd><span>지역별 현황분석 :</span>인천 지역별 민원현황을 조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		<dd><span>분야별 현황분석 :</span>민원정보를 분야별로 현황조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		<dd><span>감성분석 :</span>민원정보를 감성 별로 조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		<dd><span>전문분석 :</span>검색어에 대한 연관도정보, 트랜드정보를 조회합니다.</dd>
		<dd><span> - 연관도 분석 :</span>주제어에 대한 연관정보 분석결과를 조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		<dd><span> - 트렌드 분석 :</span>주제어에 대한 트렌드 분석결과를 조회합니다. 제목, 본문을 검색할 수 있습니다.</dd>
		</dl>
	</li>

	<li>
		<dl>
		<dt>6. 설정 기능 설명</dt>
		<dd class="bullet_none"><img src="<c:url value='/resources/images/help/help_dixsi_06.png' />" width="800" alt="" /></dd>
		<dd><span>관심키워드관리: </span>통합VOC 대시보드와 각 화면의 분석조건에서 사용하는 관심키워드를 등록하고 관리합니다. 사용자가 관심 있는 키워드를 등록해 놓으면 대시보드에 및 조건 검색시 바로 확인 가능합니다. 로그인 → 키워드관리 → 관심키워드 관리 → 신규등록을 클릭해서 관심키워드를 등록하거나 목록에서 선택하여 수정합니다. ※대시보드에 [관심키워드 트렌드 분석]에 표시하고 싶으면, 적용하기 버튼을 클릭하여 적용합니다. MAX 10개 표시 가능.</dd>
		<dd><span>유의어 사전: </span>특정 색인어에 대한 유의어들을 유의어 사전에 등록하여 검색 시에 확장하여 검색할 수 있도록 관리합니다. 로그인 → 사전관리 → 유의어 사전 관리 → 좌측 창에 키워드 입력 후 [추가] 버튼을 클릭해서 유의어 추가가 가능해 집니다. 유의어를 모두 입력 하였으면 [등록]버튼을 눌러 사전에 등록합니다. 실제 서비스에 적용을 하기 위해서는 유의어 사전 브라우징 창에 [전체 적용] 버튼을 누르면 적용됩니다.</dd>
		<dd><span>사용자 사전: </span>특정 색인어에 대해 사용자 사전에 등록하여 검색 시에 확장하여 검색할 수 있도록 관리합니다. 로그인 → 사전관리 → 사용자 사전 관리 → 우측 창에 키워드 입력 후 [등록] 버튼을 클릭해서 사용자 추가가 가능해 집니다. 실제 서비스에 적용을 하기 위해서는 사용자 사전 브라우징 창에 [전체 적용] 버튼을 누르면 적용됩니다.</dd>
		<dd><span>OPEN API: </span>음성VOC분석시스템을 통해 분석된 데이터는 타 시스템과 연계 할 수 있도록 다양한 API을 통해 조회 할 수 있도록 제공 합니다.</dd>
		</dl>
	</li>
	
	</ol>

</div>

<div class="footer">© DiQuest Corp., SELVAS AI Corp.</div>

</body>
</html>
