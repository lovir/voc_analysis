$(function() {
	
	$( window ).load(function() {
		//트리
		$("#tree_list").treeview({
			animated:"fast",
			collapsed: true
		});
		
		var check = $('#calendar_select option:selected').val();
		if("DAY" == check){
			$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		}else if("WEEK" == check){
			$('#selectText').html('<font color="red">\'주별\'</font>은 시작일부터 3개월 설정만 가능합니다.');
		}else if("MONTH" == check){
			$('#selectText').html('<font color="red">\'월별\'</font>은 시작일부터 1년 설정만 가능합니다.');
		}else if("QUARTER" == check){
			$('#selectText').html('<font color="red">\'분기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
		}else if("HALF" == check){
			$('#selectText').html('<font color="red">\'반기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
		}else if("YEAR" == check){
			$('#selectText').html('<font color="red">\'년별\'</font>은 시작일부터 3년 설정만 가능합니다.');
		}
	});
	
	// 대분류 선택 변경
	$('#lcls').change(function() {
		var lcls = $('#lcls option:selected').val();
		
		var json = {"lcls":lcls};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + '/voeKeyword/searchMclsList.do',
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				var str = "";
				var mclsList = response.userMCLSType;
				
				str += '<option value="all">전체</option>';
				for ( var i = 0, len = mclsList.length; i < len; i++) {
					var result = mclsList[i];
					str += '<option value="' + result.mcls + '" >' + result.cnslCatNm + '</option>';
				}
				str += '</select>';
				
				$('#mcls').html(str);
			}
		});
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
	$('[name="title"]').live('click',function() {
		var docId = $(this).prev().attr('id');
		var json = {
			"id" : docId
		};

		$("#basic-modal-detail").load(getContextPath() + "/voeKeyword/keywordDetailView.do #detail", json,
			function() {
				$("#basic-modal-detail").modal(
					{
						persist : false,
						focus : false,
						onClose : function() {
							$('body').css(
									'overflow',
									'auto');
							$.modal.close();
						}
					});
				$('body').css('overflow', 'hidden'); // 백그라운다
														// 마우스휠
														// off
			});
		return false;
	});

	// 유사문서 보기 클릭
	$('.result_doc').live('click',function() {

		var docId = $(this).attr('name');
		var title = $(this).prev().prev().text();
		var content = $('#' + docId).val();
		var json = {
			"id" : docId,
			"title" : title,
			"content" : content
		};

		$("#basic-modal-alike")
				.load(
						getContextPath()
								+ "/voeKeyword/keywordAlikeView.do #alike",
						json,
						function() {
							$("#basic-modal-alike").modal(
									{
										persist : false,
										focus : false,
										onClose : function() {
											$('body').css(
													'overflow',
													'auto');
											$.modal.close();
										}
									});
							$('body').css('overflow', 'hidden'); // 백그라운다
																	// 마우스휠
																	// off
						});
		return false;
	});
	
	// 부서 선택 클릭
	$('[name="select_div"]').on('click', function() {
		
		$('#basic-modal-select_div').modal({
			persist: false,  //이전 변경내용 유지 안함
			focus: false,	//포커스제거
			onClose: function () {
				$('body').css('overflow','auto');
				$('#voeDept').val($('input:radio[name=deptId]:checked').attr('id'));
				$('#voeDeptNm').val($('input:radio[name=deptId]:checked').val());
				$.modal.close();
			}
		});
	});
	
	// 초기화
	$('#btnClear').on('click', function() {
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#lcls').find('option:first').attr('selected',true);
		var str = "";
		str += '<option value="all">전체</option>';
		str += '</select>';
		$('#mcls').html(str);
		$(".btn_xls").remove();
		
		$('#term').val('');
		$('#shareTerm').val('');
		
		$('#voeDept').val('');
		$('#voeDeptNm').val('');
		$('#calendar_select').change();
	});
	
	$('#btnSearch').on('click', function() {
		// 검색
		if (validate()) {
			$('#term').val('');
			$('#needsType').val('');
			$('#currentPageNo').val(1);
			$("#searchForm").submit();
		}
		return false;
	});

	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$("#searchForm").attr('action', getContextPath() + "/voeKeyword/voeKeywordExcelDownload.do").submit();
	});
	
});

// 검색
function fnSearch(listFlg) {
	var url = "/voeKeyword/voeKeywordAnalysisList.do";
	if(listFlg){
		url = "/voeKeyword/voeKeywordAnalysisListAll.do";
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
function validate() {

	if ($('#startDate').val().length <= 0) {
		alert("시작일을 입력 해 주세요.");
		return false;
	}

	if ($('#endDate').val().length <= 0) {
		alert("종료일을 입력 해 주세요.");
		return false;
	}
	
	return true;
}