$(function(){
	// 검색클릭 이벤트
	$(document).on("click","#searchStart",function(){  		
		
		if($('input[name=searchTerm]').val() == ''){
			alert('검색어를 입력해 주시기 바랍니다.');
			return;
		}
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=socialChannel]').val($('#socialChannelList option:selected').val());
		$('input[name=currentPage]').val('1');
		$('input[name=keyword]').val($('input[name=searchTerm]').val());
		$('input[name=exclusion]').val($('input[name=excTerm]').val());
		
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		displayLine();
		displayRanking();
		fnsearchTotalList();
		
	});	
	function displayLine(){
		//라인 차트 그리기
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialTrend/trendAnalysisSearch.do",
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
				
				
				
			}		
		});	
	}
	function displayRanking(){
		//종합 랭킹 분석
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/socialTrend/trendTotalRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#search_keyword').text($('input[name=keyword]').val());
				//var temp = $('#total_Rank').html(data).find('#keywordTemp').val();//키워드
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
	//키워드 클릭시
	$(document).on('click', '.rank_key', function(){
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=socialChannel]').val($('#socialChannelList option:selected').val());
		
		$('input[name=exclusion]').val($('input[name=excTerm]').val());
		$('input[name=currentPage]').val('1');
		$('#searchForm input[name=keyword]').val($.trim($(this).text()));		
			
		fnsearchTotalList();
	});
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=socialChannel]').val($('#socialChannelList option:selected').val());
		
		$('input[name=keyword]').val($('input[name=searchTerm]').val());
		$('input[name=exclusion]').val($('input[name=excTerm]').val());
		
		$("#searchForm").attr('action', getContextPath() + "/socialTrend/excelVocSearchResult.do").submit();
	});
	//상세보기
	$(document).on('click', '.result_detail', function(){
		
		var id =  $.trim($(this).attr("name"));
		
		$("#basic-modal-detail").load(getContextPath() + "/socialTrend/detailView.do #detail",
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
			$("#basic-modal-alike").load(getContextPath() + "/socialTrend/getAlikeSearch.do #alike",
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
		
		$('#socialChannelList ').find('option:first').attr('selected',true);
		
		$("#searchTerm").val('');
		$("#excTerm").val('');
		$('#calendar_select').change();
		$('#searchTerm').attr('disabled', false);
		$('#selectKeyword').find('option:first').attr('selected',true);
	});
});
/**
* 검색결과를 도출하는 기능을 수행
* @param val
*/
function fnsearchTotalList(){
	$.ajax({
		type : "post",
		url : getContextPath()+"/socialTrend/vocSearchResult.do",
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
