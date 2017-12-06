$(function () {
	
	// tab 버튼 클릭(동적 생성)
	$('.tab_tit').live('click', function() {
		$('#currentPageNo').val(1);
		$('#needsType').val($(this).attr("name"));
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		
		if($(this).parent().parent().attr('id') == 'searchListGrpA'){
			compare = 'A';
		}else{
			compare = 'B';
		}
		
		$('#pageSize').val($('#page' + compare + ' option:selected').val());
		
		fnSearch(false, compare);
		return false;
	});

	// 표시 건수 변경
	$('#pageA, #pageB').live('change', function() {
		if($(this).attr('id') == 'pageA'){
			compare = 'A';
		}else{
			compare = 'B';
		}
		
		$('#pageSize').val($('#page' + compare + ' option:selected').val());
		$('#currentPageNo').val(1);
		
		fnSearch(false, compare);
		return false;
	});
	
	// 랭킹 키워드
	$('.rank_list ul li a').live('click', function() {
		if($(this).parents('.rank_top_area').attr('id') == 'rankingPriodA'){
			compare = 'A';
		}else{
			compare = 'B';
		}
		$('#term'+compare).val($(this).attr('name'));
		$('#needsType').val('');
		$('#currentPageNo').val(1);
		fnSearch(true, compare);
		return false;
	});
	
	// 검색
	$('#btnSearchA, #btnSearchB').on('click', function() {
		
		var compare = "";
		if($(this).attr('id') == 'btnSearchA'){
			compare = 'A';
		}else{
			compare = 'B';
		}
		
		$('#term').val($('#term' + compare).val());
		$('#exclusion').val($('#exclusion' + compare).val());
		if(validate(compare)){
			$('#condition').val($('#calendar_select' + compare + ' option:selected').val());
			$('#needs').val($('#needs' + compare + ' option:selected').val());
			$('#character').val($('#character' + compare + ' option:selected').val());
			$('#business').val($('#business' + compare + ' option:selected').val());
			$('#usesMedia').val($('#usesMedia' + compare + ' option:selected').val());
			
			$('#startDate').val($('#startDate' + compare).val());
			$('#endDate').val($('#endDate' + compare).val());
			$('#compare').val(compare);
			$('#needsType').val('');
			$('#currentPageNo').val(1);
			
			$.ajax({
				type : "POST", 
				url : getContextPath() + '/trend/trendAnalysisCompareSearch.do',
				data: $("#searchForm").serialize(),
				success : function(response) {
					// 차트, 랭킹, 목록
					$('#searchResultCompare'+compare).html(response);
				}
			});
		}

		return false;
	});

	// 상세 보기 클릭
	$('[name="title"]').live('click', function(){
		var docId = $(this).prev().attr('id');
		var json = {
			"id" : docId
		};
		
		$("#basic-modal-detail").load(getContextPath() + "/trend/trendAnalysisCompareDetailView.do #detail",
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
		
		$("#basic-modal-alike").load(getContextPath() + "/trend/trendAnalysisCompareAlikeView.do #alike",
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

	// 초기화
	$('#btnClearA').on('click', function(){
		
		$("#startDateA").val('');
		$("#endDateA").val('');
		$('#calendar_selectA').find('option:first').attr('selected',true);
		$('#selectTextA').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#needsA').find('option:first').attr('selected',true);
		$('#characterA').find('option:first').attr('selected',true);
		$('#businessA').find('option:first').attr('selected',true);
		$('#usesMediaA').find('option:first').attr('selected',true);
		$("#exclusionA").val('');
		
		$('#keywordListA option:eq(0)').attr('selected','selected');
		$('#termA').val('');
		$('#termA').attr("disabled", false);
		$('#calendar_selectA').change();
	});
	
	$('#btnClearB').on('click', function(){
		
		$("#startDateB").val('');
		$("#endDateB").val('');
		$('#calendar_selectB').find('option:first').attr('selected',true);
		$('#selectTextB').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#needsB').find('option:first').attr('selected',true);
		$('#characterB').find('option:first').attr('selected',true);
		$('#businessB').find('option:first').attr('selected',true);
		$('#usesMediaB').find('option:first').attr('selected',true);
		$("#exclusionB").val('');
		
		$('#keywordListB option:eq(0)').attr('selected','selected');
		$('#termB').val('');
		$('#termB').attr("disabled", false);
		$('#calendar_selectB').change();
	});
	
	$('#keywordListA, #keywordListB').change(function() {
		var id = $(this).attr("id");
		var temp = $('#'+id+' option:selected').text();
		if(id=='keywordListA'){
			if(temp=='직접입력'){
				$('#termA').val('');
				$('#termA').attr("disabled", false);
			}else{
				$('#termA').attr("disabled", true);
				$('#termA').val(temp);
			}
		}else{
			if(temp=='직접입력'){
				$('#termB').val('');
				$('#termB').attr("disabled", false);
			}else{
				$('#termB').attr("disabled", true);
				$('#termB').val(temp);
			}
		}
	});
	
});

//검색
function fnSearch(listFlg, compare) {
	var url = "/trend/trendAnalysisCompareList.do";
	if(listFlg){
		url = "/trend/trendAnalysisCompareListAll.do";
	}
	$('#condition').val($('#calendar_select' + compare + ' option:selected').val());
	$('#needs').val($('#needs' + compare + ' option:selected').val());
	$('#character').val($('#character' + compare + ' option:selected').val());
	$('#business').val($('#business' + compare + ' option:selected').val());
	$('#usesMedia').val($('#usesMedia' + compare + ' option:selected').val());
	
	$('#startDate').val($('#startDate' + compare).val());
	$('#endDate').val($('#endDate' + compare).val());
	$('#term').val($('#term' + compare).val());
	$('#exclusion').val($('#exclusion' + compare).val());
	$('#compare').val(compare);
	
	if (validate(compare)) {
		$.ajax({
			type : "POST", 
			url : getContextPath() + url,
			data: $("#searchForm").serialize(),
			success : function(response) {
				if(listFlg){
					// 목록 전체 업데이트
					$('#searchResultList'+compare).html(response);
				}else{
					// 탭 제외 목록 업데이트
					$('#searchResult'+compare).html(response);
				}
				
			}
		});
	}
	
	return false;
}

//페이지 이동
function pageNavi(pageNo, obj){
	if($(obj).parents('.paging').attr('id') == 'pagingA'){
		compare = 'A';
	}else{
		compare = 'B';
	}
	$('#searchForm input[name=currentPageNo]').val(pageNo);
	fnSearch(false, compare);
}

//유효성체크
function validate(compare){
		
	if($('#startDate' + compare).val().length <= 0){
		alert("시작일을 입력 해 주세요.");
		return false;
	}
	
	if($('#endDate' + compare).val().length <= 0){
		alert("종료일을 입력 해 주세요.");
		return false;
	}
	
	if($('#term' + compare).val().length <= 0){
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
