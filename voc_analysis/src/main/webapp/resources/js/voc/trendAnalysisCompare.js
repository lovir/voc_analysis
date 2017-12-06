$(function () {
	
	/******* 콤보박스 A 이벤트 START ******/
	//selectBox 접수채널 change 이벤트 
	$(document).on("change","#vocChannelListA",function(){  
		var channel = $("#vocChannelListA option:selected").val();	// 접수채널 값
		var type = "";		// 셀렉트박스 구분코드
		var code = "";		// 셀렉트박스 선택코드
		
		// 중분류, 소분류 disabled
		$("#vocRecTypeListA").attr("disabled", true);
		$('#vocRecTypeListA').find('option:first').attr('selected',true);
		$("#vocPartListA").attr("disabled", true);
		$('#vocPartListA').find('option:first').attr('selected',true);
		$("#vocItemListA").attr("disabled", true);
		$('#vocItemListA').find('option:first').attr('selected',true);
		
		// 접수 채널이 콜센터 : 만족도 비활성, voc 종류 비활성, 대분류 리로드
		if(channel == '101'){
			$('#repLevelListA').find('option:first').attr('selected',true);
			$('#vocRecTypeListA').find('option:first').attr('selected',true);
			$("#repLevelListA").attr("disabled", true);
			$("#vocRecTypeListA ").attr("disabled", true);
			type = "CHANNEL";
			code = "CALL_TYPE";
		}else{
			if(channel == 'all'){	//전체 선택시 "VOC종류", "만족도등급" 비활성화
				$('#vocRecTypeListA').find('option:first').attr('selected',true);
				$("#vocRecTypeListA ").attr("disabled", true);
				$('#repLevelListA').find('option:first').attr('selected',true);
				$("#repLevelListA ").attr("disabled", true);
			}
			else{
				$("#vocRecTypeListA ").attr("disabled", false);
				$("#repLevelListA").attr("disabled", false);
			}
			$("#vocKindListA").attr("disabled", false);
			type = "CHANNEL";
			code = "CDVOCCHANNEL";
		}
	
		var formData = "type="+type+"&code="+code;
		// 대분류 load
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/common/selectOptionList.do",
			data : formData,
			//contentType: 'application/json;charset=utf-8',
			//mimeType: 'application/json',
			success : function(response) {				
				$('#vocKindListA').html(selectList(response.vocKindList));	// 대분류
			    $('#vocKindListA').find('option:first').attr('selected',true);
			}
		}); 
	
		
	}); 
	//selectBox 접수 대분류 change 이벤트 
	$(document).on("change","#vocKindListA",function(){  	
		var channel = $("#vocChannelListA  option:selected").val();	// 접수채널 값
		var type = "";															// 셀렉트 박스 구분값
		if(channel.length == 3) type = "CALL_KIND";
		else type = "MINWON_KIND";
		var code = $("#vocKindListA option:selected").val(); 					// 대분류 코드(부모1차코드로 사용)

		$('#vocItemListA').find('option:first').attr('selected',true);
		$("#vocItemListA").attr("disabled", true);
		$("#vocPartListA").attr("disabled", false);
		
		var formData = "type="+type+"&code="+code;
		// 중분류 load
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/common/selectOptionList.do",
			data : formData,
			//contentType: 'application/json;charset=utf-8',
			//mimeType: 'application/json',
			success : function(response) {		
				
				$('#vocPartListA').html(selectList(response.vocPartList));	// 중분류
			    $('#vocPartListA').find('option:first').attr('selected',true);
			}
		}); 
	}); 
	//selectBox 접수 중분류 change 이벤트 
	$(document).on("change","#vocPartListA",function(){  		
		var channel = $("#vocChannelListA  option:selected").val();	// 접수채널 값
		var type = "";															// 셀렉트 박스 구분값
		if(channel.length == 3) type = "CALL_PART";
		else type = "MINWON_PART";
		var code = $("#vocKindListA option:selected").val(); 	// 대분류 코드(부모1차코드로 사용)
		var codePart = $("#vocPartListA option:selected").val(); 	// 중분류 코드(부모2차코드로 사용)
		$("#vocItemListA").attr("disabled", false);
		var formData = "type="+type+"&code="+code+"&codePart="+codePart;
		// 중분류 load
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/common/selectOptionList.do",
			data: formData,
			//contentType: 'application/json;charset=utf-8',
			//mimeType: 'application/json',
			success : function(response) {		
				
				$('#vocItemListA').html(selectList(response.vocItemList));	// 소분류
			    $('#vocItemListA').find('option:first').attr('selected',true);
			}
		}); 
	});
	/******* 콤보박스 B 이벤트 START ******/
	//selectBox 접수채널 change 이벤트 
	$(document).on("change","#vocChannelListB",function(){  
		var channel = $("#vocChannelListB option:selected").val();	// 접수채널 값
		var type = "";		// 셀렉트박스 구분코드
		var code = "";		// 셀렉트박스 선택코드
		
		// 중분류, 소분류 disabled
		$("#vocRecTypeListB").attr("disabled", true);
		$('#vocRecTypeListB').find('option:first').attr('selected',true);
		$("#vocPartListB").attr("disabled", true);
		$('#vocPartListB').find('option:first').attr('selected',true);
		$("#vocItemListB").attr("disabled", true);
		$('#vocItemListB').find('option:first').attr('selected',true);
		
		// 접수 채널이 콜센터 : 만족도 비활성, voc 종류 비활성, 대분류 리로드
		if(channel == '101'){
			$('#repLevelListB').find('option:first').attr('selected',true);
			$('#vocRecTypeListB').find('option:first').attr('selected',true);
			$("#repLevelListB").attr("disabled", true);
			$("#vocRecTypeListB ").attr("disabled", true);
			type = "CHANNEL";
			code = "CALL_TYPE";
		}else{
			if(channel == 'all'){	//전체 선택시 "VOC종류", "만족도등급" 비활성화
				$('#vocRecTypeListB').find('option:first').attr('selected',true);
				$("#vocRecTypeListB ").attr("disabled", true);
				$('#repLevelListB').find('option:first').attr('selected',true);
				$("#repLevelListB ").attr("disabled", true);
			}
			else{
				$("#vocRecTypeListB ").attr("disabled", false);
				$("#repLevelListB").attr("disabled", false);
			}
			$("#vocKindListB").attr("disabled", false);
			type = "CHANNEL";
			code = "CDVOCCHANNEL";
		}
	
		var formData = "type="+type+"&code="+code;
		// 대분류 load
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/common/selectOptionList.do",
			data : formData,
			//contentType: 'application/json;charset=utf-8',
			//mimeType: 'application/json',
			success : function(response) {				
				$('#vocKindListB').html(selectList(response.vocKindList));	// 대분류
			    $('#vocKindListB').find('option:first').attr('selected',true);
			}
		}); 
	
		
	}); 
	//selectBox 접수 대분류 change 이벤트 
	$(document).on("change","#vocKindListB",function(){  	
		var channel = $("#vocChannelListB  option:selected").val();	// 접수채널 값
		var type = "";															// 셀렉트 박스 구분값
		if(channel.length == 3) type = "CALL_KIND";
		else type = "MINWON_KIND";
		var code = $("#vocKindListB option:selected").val(); 					// 대분류 코드(부모1차코드로 사용)

		$('#vocItemListB').find('option:first').attr('selected',true);
		$("#vocItemListB").attr("disabled", true);
		$("#vocPartListB").attr("disabled", false);
		
		var formData = "type="+type+"&code="+code;
		// 중분류 load
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/common/selectOptionList.do",
			data : formData,
			//contentType: 'application/json;charset=utf-8',
			//mimeType: 'application/json',
			success : function(response) {		
				
				$('#vocPartListB').html(selectList(response.vocPartList));	// 중분류
			    $('#vocPartListB').find('option:first').attr('selected',true);
			}
		}); 
	}); 
	//selectBox 접수 중분류 change 이벤트 
	$(document).on("change","#vocPartListB",function(){  		
		var channel = $("#vocChannelListB  option:selected").val();	// 접수채널 값
		var type = "";															// 셀렉트 박스 구분값
		if(channel.length == 3) type = "CALL_PART";
		else type = "MINWON_PART";
		var code = $("#vocKindListB option:selected").val(); 	// 대분류 코드(부모1차코드로 사용)
		var codePart = $("#vocPartListB option:selected").val(); 	// 중분류 코드(부모2차코드로 사용)
		$("#vocItemListB").attr("disabled", false);
		var formData = "type="+type+"&code="+code+"&codePart="+codePart;
		// 중분류 load
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/common/selectOptionList.do",
			data: formData,
			//contentType: 'application/json;charset=utf-8',
			//mimeType: 'application/json',
			success : function(response) {		
				
				$('#vocItemListB').html(selectList(response.vocItemList));	// 소분류
			    $('#vocItemListB').find('option:first').attr('selected',true);
			}
		}); 
	});
	/******* 콤보박스 이벤트 END ******/
	
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
		$('input[name=vocChannel]').val($('#vocChannelList'+div+' option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList'+div+' option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList'+div+' option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList'+div+' option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList'+div+' option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList'+div+' option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList'+div+' option:selected').val());
		$('input[name=metroDept]').val($('#vocMetroDeptList'+div+' option:selected').val());
		
		$('input[name=keyword]').val($('input[name=searchTerm'+div+']').val());
		$('input[name=exclusion]').val($('input[name=excTerm'+div+']').val());
		$('input[name=startDate]').val($('input[name=startDate'+div+']').val());
		$('input[name=endDate]').val($('input[name=endDate'+div+']').val());
	
	}
	
	function displayLine(div){
		
		//라인 차트 그리기
		$.ajax({
			type : "post",
			url : getContextPath()+"/trend/trendCompareAnalysisSearch.do",
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
				$('#vocMetroDeptList'+div).html(selectHtml);
				$('#vocMetroDeptList'+div+' option[value='+$('input[name=metroDept]').val()+']').attr('selected', 'selected');
				
			}		
		});	
	}
	
	function displayRanking(div){
		//종합 랭킹 분석
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/trend/trendCompareTotalRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				//console.log("data:"+data);
				//var temp = $('#total_Rank').html(data).find('#keywordTemp').val();//키워드
				//$("input[name='keyword']").val(temp);
				$('#total_Rank'+div).html(data);
				//fnsearchTotalListFirst(temp);
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
		$("#searchForm").attr('action', getContextPath() + "/trend/excelVocSearchResultCompare.do").submit();
	});
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath()+"/trend/getAlikeSearchCompare.do #alike",
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
		
		$('#vocChannelListA ').find('option:first').attr('selected',true);
		$('#vocRecTypeListA ').find('option:first').attr('selected',true);
		$('#vocKindListA ').find('option:first').attr('selected',true);
		$('#vocPartListA ').find('option:first').attr('selected',true);
		$('#vocItemListA ').find('option:first').attr('selected',true);
		$('#repLevelListA').find('option:first').attr('selected',true);
		$('#vocMetroDeptListA').find('option:first').attr('selected',true);
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
		
		$('#vocChannelListB ').find('option:first').attr('selected',true);
		$('#vocRecTypeListB ').find('option:first').attr('selected',true);
		$('#vocKindListB ').find('option:first').attr('selected',true);
		$('#vocPartListB ').find('option:first').attr('selected',true);
		$('#vocItemListB ').find('option:first').attr('selected',true);
		$('#repLevelListB').find('option:first').attr('selected',true);
		$('#vocMetroDeptListB').find('option:first').attr('selected',true);
		$("#searchTermB").val('');
		$("#excTermB").val('');
		$('#calendar_selectB').change();
		$('#searchTermB').attr('disabled', false);
		$('#selectKeywordB').find('option:first').attr('selected',true);
	});
});
//상세보기
/*$(document).on('click', '.result_detail', function(){
	
	var id =  $.trim($(this).attr("name"));
	
	$("#basic-modal-detail").load(getContextPath() + "/trend/detailViewCompare.do #detail",
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
*/
function detailView(id){
	$("#basic-modal-detail").load(getContextPath()+"/trend/detailViewCompare.do #detail",
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
		url : getContextPath()+"/trend/vocSearchResultCompare.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			//console.log("data"+data);
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