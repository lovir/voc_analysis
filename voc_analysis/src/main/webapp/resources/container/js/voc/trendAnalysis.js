$(function () {
		
	$( window ).load(function() {
		var check = $('#keywordList option').index($('#keywordList option:selected'));
		if(check != '0'){
			$('#keywordTemp').attr("disabled", true);
		}else{
			$('#keywordTemp').attr("disabled", false);
		}
		
		var calendar_select = $('#calendar_select option:selected').val();
		if("DAY" == calendar_select){
			$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		}else if("WEEK" == calendar_select){
			$('#selectText').html('<font color="red">\'주별\'</font>은 시작일부터 3개월 설정만 가능합니다.');
		}else if("MONTH" == calendar_select){
			$('#selectText').html('<font color="red">\'월별\'</font>은 시작일부터 1년 설정만 가능합니다.');
		}else if("QUARTER" == calendar_select){
			$('#selectText').html('<font color="red">\'분기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
		}else if("HALF" == calendar_select){
			$('#selectText').html('<font color="red">\'반기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
		}else if("YEAR" == calendar_select){
			$('#selectText').html('<font color="red">\'년별\'</font>은 시작일부터 3년 설정만 가능합니다.');
		}
	});
	
	// tab 버튼 클릭(동적 생성)
	$('.tab_tit').live('click', function() {
		$('#currentPageNo').val(1);
		$('#needsType').val($(this).attr("name"));
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		fnSearch(false);
		return false;
	});
	
	// tab 버튼 클릭
	$('.tab_tit').on('click', function() {
		$('#currentPageNo').val(1);
		$('#needsType').val($(this).attr("name"));
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		fnSearch(false);
		return false;
	});

	// 표시 건수 변경
	$('#page').change(function() {
		$('#pageSize').val($('#page option:selected').val());
		$('#currentPageNo').val(1);
		fnSearch(false);
	});

	// 랭킹 키워드
	$('.rank_list ul li a').live('click', function() {
		$('#term').val($(this).attr('name'));
		$('#needsType').val('');
		$('#currentPageNo').val(1);
		fnSearch(true);

		return false;
	});

	// 상세 보기 클릭
	$('[name="title"]').live('click', function(){
		var docId = $(this).prev().attr('id');
		var json = {
			"id" : docId
		};
		
		$("#basic-modal-detail").load(getContextPath() + "/trend/trendAnalysisDetailView.do #detail",
				json,
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
		return false;
	});

	// 유사문서 보기 클릭
	$('.result_doc').live('click', function(){
		
		var docId = $(this).attr('name');
		var title = $(this).prev().prev().text();
		var content = $('#' + docId).val();
		var json = {"id":docId, "title":title, "content":content};
		
		$("#basic-modal-alike").load(getContextPath() + "/trend/trendAnalysisAlikeView.do #alike",
				json,
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
		return false;
	});
	
	// 주제어입력 변경
	$('#keywordList').change(function() {
		var temp = $('#keywordList option:selected').text();
		if(temp=='직접입력'){
			$('#keywordTemp').val('');
			$('#keywordTemp').attr("disabled", false);
			$('#term').val('');
		}else{
			$('#keywordTemp').attr("disabled", true);
			$('#keywordTemp').val(temp);
			$('#term').val(temp);
		}
	});
	
	// 초기화 버튼 클릭
	$('#btnClear').on('click', function(){
		$('#condition').find('option:first').attr('selected',true);
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#category').find('option:first').attr('selected',true);
		$('#minwonType').find('option:first').attr('selected',true);
		$('#guList').find('option:first').attr('selected',true);
		$('#r_chType').find('option:first').attr('selected',true);
		$('#deptTypeList > select').find('option:first').attr('selected',true);
		
		$("#exclusion").val('');
		$(".btn_xls").remove();
		
		$('#keywordList option:eq(0)').attr('selected','selected');
		$('#keywordTemp').val('');
		$('#keywordTemp').attr("disabled", false);
		$('#term').val('');
		$('#shareTerm').val('');
		$('#calendar_select').change();
	});

	// 검색 버튼 클릭
	$('#btnSearch').on('click', function() {
		// 검색
		if (validate()) {
			$('#needsType').val('');
			$('#currentPageNo').val(1);
			$('#term').val($('#keywordTemp').val());
			$("#searchForm").attr('action', getContextPath() + "/trend/trendAnalysisSearch.do").submit();
		}
		return false;
	});
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$("#searchForm").attr('action', getContextPath()+"/trend/trendAnalysisExcelDownload.do").submit();
	});
	
});

//검색
function fnSearch(listFlg) {
	var url = "/trend/trendAnalysisList.do";
	if(listFlg){
		url = "/trend/trendAnalysisListAll.do";
	}
	
	if (validate()) {
		$.ajax({
			type : "POST", 
			url : getContextPath() + url,
			data: $("#searchForm").serialize(),
			success : function(response) {
				if(listFlg){
					// 목록 전체 업데이트
					$('#searchResultList').html(response);
				}else{
					// 탭 제외 목록 업데이트
					$('#searchResult').html(response);
				}
				
			}
		});
	}

	return false;
}

//페이지 이동
function pageNavi(pageNo, obj){
	$('#searchForm input[name=currentPageNo]').val(pageNo);
	fnSearch(false);
}

// 유효성 체크
function validate(){
	
	if($('#startDate').val().length <= 0){
		alert("시작일을 입력 해 주세요.");
		return false;
	}
	
	if($('#endDate').val().length <= 0){
		alert("종료일을 입력 해 주세요.");
		return false;
	}
	
	if($('#keywordTemp').val().length <= 0){
		alert("키워드를 입력 해 주세요.");
		return false;
	}
	
	if(!$('#exclusion').val() == ''){
		var exceptKeyword = $('#exclusion').val();
		var exceptKeywordArr = exceptKeyword.split(",");
		if(exceptKeywordArr.length > 3){
			alert("제외키워드는 3개까지만 입력가능합니다.");
			return false;
		}
	}
	return true;
}