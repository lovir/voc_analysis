$(function () {
	
	$( window ).load(function() {
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
		fnSearch();
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
		fnSearch();
		return false;
	});

	// 표시 건수 변경
	$('#page').change(function() {
		$('#pageSize').val($('#page option:selected').val());
		$('#currentPageNo').val(1);
		fnSearch();
	});
	
	// 상세 보기 클릭
	$('[name="title"]').live('click', function(){
		var docId = $(this).prev().attr('id');
		var json = {
			"id" : docId
		};
		
		$("#basic-modal-detail").load(getContextPath() + "/mainSearch/voeDetailView.do #detail",
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
		
		$("#basic-modal-alike").load(getContextPath() + "/mainSearch/voeAlikeView.do #alike",
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
	
	// 초기화 버튼 클릭
	$('#btnClear').on('click', function(){
		$('#condition').find('option:first').attr('selected',true);
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#lcls').find('option:first').attr('selected',true);
		$("#exclusion").val('');
		$(".btn_xls").remove();
		$('#calendar_select').change();
	});

	// 검색 버튼 클릭
	$('#btnSearch').on('click', function() {
		// 검색
		if (validate()) {
			$('#needsType').val('');
			$('#currentPageNo').val(1);
			$('#term').val($('#searchTerm').val());
			$("#searchForm").attr('action', getContextPath() + "/mainSearch/voeSearch.do").submit();
		}
		return false;
	});
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$("#searchForm").attr('action', getContextPath()+"/mainSearch/voeExcelDownload.do").submit();
	});
	
});

// VOC 탭 클릭
function fnMove(){
	if($('#searchTerm').val().length<=0){
		alert("검색어 입력 해 주세요.");
		return false;
	}else{
		$("#headerForm").attr('action', getContextPath()+"/mainSearch/vocSearch.do");
		$("#headerForm").submit();
	}
}

//검색
function fnSearch() {
	
	$('#term').val($('#searchTerm').val());
	
	if (validate()) {
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/mainSearch/voeList.do",
			data: $("#searchForm").serialize(),
			success : function(response) {
				// 탭 제외 목록 업데이트
				$('#searchResult').html(response);
			}
		});
	}

	return false;
}

//페이지 이동
function pageNavi(pageNo, obj){
	$('#searchForm input[name=currentPageNo]').val(pageNo);
	fnSearch();
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
	
	if($('#searchTerm').val().length <= 0){
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