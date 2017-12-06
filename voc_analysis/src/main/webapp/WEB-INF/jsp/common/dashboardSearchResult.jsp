<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<link rel="stylesheet" type="text/css" href="/voc_analysis/resources/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/voc_analysis/resources/css/style.css">
<link rel="stylesheet" type="text/css" href="/voc_analysis/resources/css/layout.css">
<script type="text/javascript" src="/voc_analysis/resources/js/jquery.min.js"></script>
<script type="text/javascript" src="/voc_analysis/resources/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/voc_analysis/resources/js/jquery.simplemodal.js"></script>
<script type="text/javascript">
function getContextPath() {
    return "<c:out value="${pageContext.request.contextPath}" />";
}
//카테고리 클릭
$(function(){
	 $(document).on('click', '.tab_tit', function(){
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		
		$('#searchForm input[name=needsType]').val($(this).attr("name"));
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
	}); 
	
	$(document).on('change','#pageSize', function(){
		$('#searchForm input[name=pagesize]').val($("select[name=pageSize]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
	});
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		var collection = $("#collection").val();
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/dashBoard/getAlikeSearch.do #alike",
			{"doc_id" : id, "title" : title, "content" : content, "collection"  : collection},
			function(){
				$("#basic-modal-alike").modal({
					persist: false,
					focus: false,
					onClose: function () {
						   $('body').css('overflow','auto');
						   $.modal.close();
						}
				});
				$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
			});
		}
	});
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$("#searchForm").attr('action', getContextPath() + "/dashBoard/excelVocRegionSearchResult.do").submit();
	});
	
	//채창열기
	$(document).on('click', '.newWindow', function(){
		var newWin = $.trim($(this).parent().parent().parent().children('input[name=newWin]').val());
		window.open(newWin, "새창");
	});
	
	
});

//VOC검색결과 리스트 생성(리스트+페이징)
function fnsearchList(){
	$.ajax({
		type : "post",
		url : getContextPath()+"/dashBoard/regionSearchList.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			$('#result_list').html(data);
			$('#share').text($('#result_list input[name=share]').val()+'%');
		},
		error : function(result) {     
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}

function pageNavi(pageNo){
	$('#searchForm input[name=currentPage]').val(pageNo);
	fnsearchList();
}

//상세보기
function detailView(id){
	var collection = $("#collection").val();
	$("#basic-modal-detail").load(getContextPath() + "/dashBoard/detailView.do #detail",
	{"doc_id" : id, "collection" : collection},
	function(){
		$("#basic-modal-detail").modal({
			persist: false,
			focus: false,
			onClose: function () {
				   $('body').css('overflow','auto');
				   $.modal.close();
				}
		});
		$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
	});
}
</script>	

	<form id="searchForm" name="searchForm" method="post">
		<input type="hidden" id="condition" name="condition" value='${searchSc.condition}'>
		<input type="hidden" id="emotion" name="emotion" value='${searchSc.emotion}'>
		<input type="hidden" id="guname" name = "guname" value='${searchSc.guname}'>
		<input type="hidden" id="startDate" name = "startDate" value='${searchSc.startDate}'>
		<input type="hidden" id="endDate" name ="endDate" value='${searchSc.endDate}'>
		<input type="hidden" id="pagesize" name="pagesize" value="10"/> 
		<input type="hidden" id="currentPage" name="currentPage" value="1"/>
		<input type="hidden" id="needsType" name="needsType">
		<input type="hidden" id="collection" name="collection" value='${searchSc.collection}'>
	</form>
	
<div class="cont_body" id="cont_body">
<div class="win win_full" id="search_result">
	<c:choose>
		<c:when test="${ !empty searchResultList.listResult}">
		<!-- VOC 검색결과 start -->
		<div class="win_head clear2">
			<span class="win_tit">검색결과</span>
			<span class="win_date">
				키워드 : 
						<span class="font_yb" id="search_keyword">
							<c:out value="${searchResultList.keyword}" />
						</span> 
				/ 점유율 : 
						<span class="font_yb" id="share">
							<c:out value="${searchResultList.share}" />%
						</span>
			</span>
			<ul class="win_btnset">
				<li><a href="javascript:;" class="btn_xls" title="액셀파일 내려받기"></a></li>
				<li><a href="#" class="btn_sh" title="접기"></a></li>
			</ul>
		</div>
		
		<div class="win_contarea p_0">
			<!-- tab start -->
			<div class="tab_wrap clear2">
				<ul class="tab_win" id="searchCategory">
					<li>
						<a href="javascript:;" class="tab_tit on" id="tab_01" name="">전체<span>(<c:out value="${searchResultList.totalSize}" />)</span></a>
					</li>
					<c:forEach var="result" items="${searchResultList.groupResult}" varStatus="status">
					<li>
						<a href="javascript:;" class="tab_tit" id="tab_01" name="${result.name }"><c:out value="${result.name}" /><span>(<c:out value="${result.count}" />)</span></a>
					</li>
					</c:forEach>
				</ul>
				<div class="tab_right mr_15">
					<fieldset><legend>목록갯수</legend>
						<select id="pageSize" name="pageSize" title="목록갯수 선택" class="">
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
			<form id="searchResultForm" name="searchResultForm" method="post">
				<div class="tab_cont cont_tab_01">
					<div class="result_list" id="result_list">
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
												<c:choose>
													<c:when test="${result.USE_MED_CD eq 'news' or result.USE_MED_CD eq 'blog'}">
														<a href="javascript:;" class="newWindow">
															[<c:out value="${result.USE_MED_CD}" escapeXml="false" />] <c:out value="${result.TITLE}" escapeXml="false" />
														</a>
													</c:when>
													<c:otherwise>												
														<a href="javascript:;" onclick="detailView('<c:out value='${result.ID}' />'); return false;" >
															[<c:out value="${result.USE_MED_CD}" escapeXml="false" />] <c:out value="${result.TITLE}" escapeXml="false" />
														</a>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<a href="javascript:;" onclick="detailView('<c:out value='${result.ID}' />'); return false;" >
													[<c:out value="${result.USE_MED_CD}" escapeXml="false" />] 트위터 멘션
												</a>
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
						<div id="paging" class="paging">
							<ui:pagination paginationInfo = "${searchResultList.paginationInfo}" type="image" jsFunction="pageNavi" />
						</div>
					</div>
				</div>
			</form>
			<!-- list end -->
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
					<div class="graph_in"></div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</div>
</div>
<div class="modal_popup doc_view" id="basic-modal-alike" /></div>
<div class="modal_popup result_view" id="basic-modal-detail" /></div>
<!--// 검색결과 end -->