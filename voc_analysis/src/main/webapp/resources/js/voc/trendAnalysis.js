// 대시보드에서 넘겨온 값을 searchForm에 세팅한다
function dashInputVal(keyword, condition){
	
	keyword = decodeURIComponent(keyword).trim();
	
	var sdate = new Date();
	var edate = new Date();
	
	if("DAY" == condition){
		sdate.setDate(edate.getDate()-6);
	}else if("WEEK" == condition){
		sdate.setMonth(edate.getMonth()-2);
	}else if("MONTH" == condition){
		sdate.setMonth(edate.getMonth()-12);
	}
	$('#startDate').datepicker('setDate', sdate);
	$('#endDate').datepicker('setDate', edate);
	
	$("#rankingIndex").val(0);
	$('#searchForm input[name=currentPage]').val('1');
	$('#searchForm input[name=condition]').val(condition);
	$('input[name=keyword]').val(keyword);
	
	displayLine();
	displayRanking();
	fnsearchTotalList();
	$('input[name=searchTerm]').val(keyword);
	$("#calendar_select").removeAttr('selected')
	$("#calendar_select option[value=\""+condition+"\"]").prop("selected", true);
	
}

$(function(){
	// 검색클릭 이벤트
	$(document).on("click","#searchStart",function(){  		
		
		if($('input[name=searchTerm]').val() == ''){
			alert('검색어를 입력해 주시기 바랍니다.');
			return;
		}
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList option:selected').val());
		$('input[name=metroDept]').val($('#vocMetroDeptList option:selected').val());
		
		$('input[name=keyword]').val($('input[name=searchTerm]').val());
		$('input[name=exclusion]').val($('input[name=excTerm]').val());
		
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		displayLine();
		displayRanking();
		fnsearchTotalList();
		
	});	
	
	//키워드 클릭시
	$(document).on('click', '.rank_key', function(){
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList option:selected').val());
		$('input[name=metroDept]').val($('#vocMetroDeptList option:selected').val());
		
		$('input[name=exclusion]').val($('input[name=excTerm]').val());
		
		$('#searchForm input[name=keyword]').val($.trim($(this).text()));		
			
		fnsearchTotalList();
	});
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList option:selected').val());
		$('input[name=metroDept]').val($('#vocMetroDeptList option:selected').val());
		
		$('input[name=keyword]').val($('input[name=searchTerm]').val());
		$('input[name=exclusion]').val($('input[name=excTerm]').val());
		
		$("#searchForm").attr('action', getContextPath() + "/trend/excelVocSearchResult.do").submit();
	});
	//상세보기
	$(document).on('click', '.result_detail', function(){
		
		var id =  $.trim($(this).attr("name"));
		
		$("#basic-modal-detail").load(getContextPath() + "/trend/detailView.do #detail",
		{"dq_docid" : id},
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
	});
	//유사문서
	$(document).on('click', '.result_doc', function(){
		
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/trend/getAlikeSearch.do #alike",
			{"dq_docid" : id, "title" : title, "content" : content},
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
	
	//키워드 선택 시
	$(document).on('change', '#selectKeyword', function(){
		
		if($(this).val() == 'all'){
			$('#searchTerm').val('');
			$('#searchTerm').attr('disabled', false);
		}else{
			$('#searchTerm').val($(this).val());
			$('#searchTerm').attr('disabled', true);
		}
	});
	
	// 초기화 
	$(document).on('click', '#searchReset', function(){
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		
		$('#vocChannelList ').find('option:first').attr('selected',true);
		$('#vocRecTypeList ').find('option:first').attr('selected',true);
		$('#vocKindList ').find('option:first').attr('selected',true);
		$('#vocPartList ').find('option:first').attr('selected',true);
		$('#vocItemList ').find('option:first').attr('selected',true);
		$('#repLevelList').find('option:first').attr('selected',true);
		$('#vocMetroDeptList').find('option:first').attr('selected',true);
		$("#searchTerm").val('');
		$("#excTerm").val('');
		$('#calendar_select').change();
		$('#searchTerm').attr('disabled', false);
		$('#selectKeyword').find('option:first').attr('selected',true);
	});
});

function displayLine(){
	//라인 차트 그리기
	$.ajax({
		type : "post",
		url : getContextPath()+"/trend/trendAnalysisSearch.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			var returnData = $.parseJSON(data);				
			if(returnData.series != undefined){	
				$('#lineChart').highcharts({
					title: {
						text: '',
						x: -20 //center
					},
					xAxis: {
						categories: returnData.categories
					},
					credits:{
						enabled:false
					},
					yAxis: {
						min: 0,
						title: {
							text: '건수'
						},
					},
					legend: {
						layout: 'horizontal',
						align: 'center',
						verticalAlign: 'bottom',
						borderWidth: 0
					},
					series: returnData.series,
					
					chart:{
						height : 340
					}
				});
			}
			// 처리주무부서 리스트 
			var deptData = returnData.metroDept;
			var selectHtml = "";
			selectHtml += "<option value=\"all\">전체</option>";
			for ( var i = 0, len = deptData.length; i < len; i++) {			
				var result = deptData[i];
				code = result.CODE;
				codeName = result.NAME;
				selectHtml += "<option value='"+ code +"'>"+ codeName +"</option>";
			}
			$('#vocMetroDeptList').html(selectHtml);
			$('#vocMetroDeptList option[value='+$('input[name=metroDept]').val()+']').attr('selected', 'selected');
			
		}		
	});	
}
function displayRanking(){
	//종합 랭킹 분석
	$.ajax({
		type : "post",	 
		url : getContextPath() + "/trend/trendTotalRanking.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			//console.log("data:"+data);
			var temp = $('#total_Rank').html(data).find('#keywordTemp').val();//키워드
			//$("input[name='keyword']").val(temp);
			$('#total_Rank').html(data);
			//fnsearchTotalListFirst(temp);
			$('.rank_top_area').scrollLeft($('.rank_top_area').width() * 10);
		},
		error : function(request, status, error) {
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}

/**
* 검색결과를 도출하는 기능을 수행
* @param val
*/
function fnsearchTotalList(){
	$.ajax({
		type : "post",
		url : getContextPath()+"/trend/vocSearchResult.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			//console.log(data);
			$('#search_result').html(data);
		},
		error : function(result) {	 
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}
//페이지 이동
function pageNavi(pageNo){
	
	$('#searchForm input[name=currentPage]').val(pageNo);
	fnsearchTotalList();
	
}
