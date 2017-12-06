$(function () {
	
	// 검색클릭 A 이벤트
	$(document).on("click","#searchStartA",function(){  		
		
		if($('input[name=searchTermA]').val() == ''){
			alert('검색어를 입력해 주시기 바랍니다.');
			return;
		}
			
		inputVal('A');
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		displayLine('A');
		displayRanking('A');
		fnsearchTotalList('A');
		
	});	
	
	// 검색클릭 B 이벤트
	$(document).on("click","#searchStartB",function(){  		
		
		if($('input[name=searchTermB]').val() == ''){
			alert('검색어를 입력해 주시기 바랍니다.');
			return;
		}
				
		inputVal('B');
		
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		displayLine('B');
		displayRanking('B');
		fnsearchTotalList('B');
		
	});	
	
	// searchForm 적용
	function inputVal(div){
		$('input[name=condition]').val($('#calendar_select'+div+' option:selected').val());
		$('input[name=socialChannel]').val($('#socialChannelList'+div+' option:selected').val());
				
		$('input[name=keyword]').val($('input[name=searchTerm'+div+']').val());
		$('input[name=exclusion]').val($('input[name=excTerm'+div+']').val());
		$('input[name=startDate]').val($('input[name=startDate'+div+']').val());
		$('input[name=endDate]').val($('input[name=endDate'+div+']').val());
		
		$('input[name=currentPage]').val('1');
	
	}
	
	function displayLine(div){
		
		//라인 차트 그리기
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialTrend/trendCompareAnalysisSearch.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);				
				if(returnData.series != undefined){	
					$('#lineChart'+div).highcharts({
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
				
			}		
		});	
	}
	
	function displayRanking(div){
		//종합 랭킹 분석
		
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/socialTrend/trendCompareTotalRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#total_Rank'+div).html(data);	
				$('#total_Rank'+ div +' > .rank_top_area').scrollLeft($('#total_Rank'+ div +' > .rank_top_area').width() * 10);
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}

	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		var div = $.trim($(this).parent().parent().parent().parent().attr('id'));
		if(div == 'search_resultA'){
			
		
			inputVal('A');
		}else if(div == 'search_resultB'){
			
			inputVal('B');
		}
		$("#searchForm").attr('action', getContextPath() + "/socialTrend/excelVocSearchResultCompare.do").submit();
	});
	//상세보기
	/*$(document).on('click', '.result_detail', function(){
		
		var id =  $.trim($(this).attr("name"));
		
		$("#basic-modal-detail").load(getContextPath() + "/socialTrend/detailViewCompare.do #detail",
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
	});*/

	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath()+"/socialTrend/getAlikeSearchCompare.do #alike",
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
	//키워드 클릭시
	$(document).on('click', '.rank_key', function(){
		var div = $.trim($(this).parent().parent().parent().parent().parent().parent().parent().attr('id'));
		//console.log(div);
		var area = '';
		if(div=='total_RankA'){
						
			area = 'A';
			
		}else if(div=='total_RankB'){
			
			area = 'B';
		}
		
		
		inputVal(area);
		$('#searchForm input[name=keyword]').val($.trim($(this).text()));		
		fnsearchTotalList(area);
		
	});
	//키워드 선택 시
	$(document).on('change', '.selectKeyword', function(){
		var div = $.trim($(this).attr('id'));
		if(div == 'selectKeywordA'){
			if($(this).val() == 'all'){
				$('#searchTermA').val('');
				$('#searchTermA').attr('disabled', false);
			}else{
				$('#searchTermA').val($(this).val());
				$('#searchTermA').attr('disabled', true);
			}
		}else  if('selectKeywordB'){
			if($(this).val() == 'all'){
				$('#searchTermB').val('');
				$('#searchTermB').attr('disabled', false);
			}else{
				$('#searchTermB').val($(this).val());
				$('#searchTermB').attr('disabled', true);
			}
		}
		
	});
	// 초기화 - A
	$(document).on('click', '#searchResetA', function(){
		$("#startDateA").val('');
		$("#endDateA").val('');
		$('#calendar_selectA').find('option:first').attr('selected',true);
		
		$('#socialChannelListA ').find('option:first').attr('selected',true);
		
		$("#searchTermA").val('');
		$("#excTermA").val('');
		$('#calendar_selectA').change();
		$('#searchTermA').attr('disabled', false);
		$('#selectKeywordA').find('option:first').attr('selected',true);
	});
	// 초기화 - B
	$(document).on('click', '#searchResetB', function(){
		$("#startDateB").val('');
		$("#endDateB").val('');
		$('#calendar_selectB').find('option:first').attr('selected',true);
		
		$('#socialChannelListB ').find('option:first').attr('selected',true);
		$("#searchTermB").val('');
		$("#excTermB").val('');
		$('#calendar_selectB').change();
		$('#searchTermB').attr('disabled', false);
		$('#selectKeywordB').find('option:first').attr('selected',true);
	});
});
function detailView(id){
	$("#basic-modal-detail").load(getContextPath()+"/socialTrend/detailViewCompare.do #detail",
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
}
/**
* 검색결과를 도출하는 기능을 수행
* @param val
*/
function fnsearchTotalList(div){
	$.ajax({
		type : "post",
		url : getContextPath()+"/socialTrend/vocSearchResultCompare.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			
			$('#search_result'+div).html(data);
		},
		error : function(result) {	 
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}
//페이지 이동
function pageNavi(pageNo, element){
	
	divElement = element.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
	if(divElement.getAttribute('id') == 'search_resultA'){
		
		$('#searchForm input[name=currentPage]').val(pageNo);	
		fnsearchTotalList('A');
	}
	else if(divElement.getAttribute('id') == 'search_resultB'){
		$('#searchForm input[name=currentPage]').val(pageNo);
		fnsearchTotalList('B');
	}
}