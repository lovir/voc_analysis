
$(function(){
	//확인 버튼 클릭
	$('#searchStart').click(function(){
		if(!validate()){
			return false;
		}
		displayReport();
		return false;
	});
	
	Highcharts.setOptions({
		lang: {
			thousandsSep: ','
		}
	});
	function displayReport(){
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name=socialChannel]').val($('#socialChannelList option:selected').val());
		//막대차트 클릭 관련 부분 초기화
		$('input[name="clickColumn"]').val('N');
		$('input[name="clickPnn"]').val('');
		
		//감성 추세 차트
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialEmotion/socialEmotionTrendChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData != undefined){
					var categoriSize = Array.isArray(returnData.periodMap) ? returnData.periodMap.length : Object.keys(returnData.periodMap).length;
					var categories = new Array();
					var dateValue = new Array();
					$.each(returnData.periodMap, function(index, item){
						categories.push(item);
						dateValue.push(index);
					});
					var positiveCountData = new Array();
					var neutralCountData = new Array();
					var negativeCountData = new Array();
					
					$.each(returnData.periodPnnMapList, function(index, item){
						$.each(item, function(sIndex, pnn){
							if(pnn.name == '긍정'){
								positiveCountData.push(pnn.count);
							}
							else if(pnn.name == '중립'){
								neutralCountData.push(pnn.count);
							}
							else if(pnn.name == '부정'){
								negativeCountData.push(pnn.count);
							}
						});
					});
					
					$('#trendChart').highcharts({
						chart: {
							type: 'column',
							width : 924,
							height : 400
						},
						title: {
							text: ''
						},
						xAxis: {
							categories: categories,
							labels:{
								style:{
									fontWeight: 'bold'
								}
							}
						},
						yAxis: {
							min: 0,
							title: {
								text: '백분율(%)'
							}
						},
						tooltip: {
							headerFormat: '<span >전체: {point.total:,.0f} 건</span><br/>',
							pointFormat: '{series.name}: <b>{point.y:,.0f} 건</b> ({point.percentage:.2f}%)<br/>',
							shared: false
						},
						plotOptions: {
							column: {
								stacking: 'percent'
							},
							series: {
								cursor: 'pointer',
								events:{
									click: function(){
										$('input[name="clickPnn"]').val(this.name);
										$('input[name="clickDateStr"]').val(this.options.count[event.point.index]);
										$('input[name="clickCategory"]').val(event.point.category);
										$('#searchForm input[name=currentPage]').val('1');
										fnSearchClickStackTotalList();
									}
								}
							}
						},	//클릭이벤트 테스트 중
						series: [{
							name : "긍정",
							data : positiveCountData,
							count : dateValue,
							color : "#2F73C8"
						}, {
							name : "중립",
							data : neutralCountData,
							count : dateValue,
							color : "#8EB252"
						}, {
							name : "부정",
							data : negativeCountData,
							count : dateValue,
							color : "#FC8A29"
						}],
						legend: {
							align : "center",
							verticalAlign: 'top',
						},
						credits: {
							enabled: false
						}
					});
				}else{
					//$('#reportChart').removeClass('p_20');
					console.log("error : " + returnData);
					$('#trendChart').empty();
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		//부정 감성 분포 차트
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialEmotion/socialEmotionDistributionChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData != undefined){
					var chartTitleList = returnData.chartTitleList;
					var finalSenseMapList = returnData.finalSenseMapList;
					var senseKindList = returnData.senseKindList;
					var htmlStr = "";
					//스파이더 차트 출력영역 동적 생성
					$.each(chartTitleList,function(index, title){
						htmlStr += "<li>\n";
						htmlStr += "	<div class=\"cloud_w50\">\n";
						htmlStr += "		<div id=\"spider_" + (index < 10 ? "0"+index : index) + "\" class=\"cloud_tag\" ></div>\n";
						htmlStr += "		<p>" + title + "</p>\n";
						htmlStr += "	</div>\n";
						htmlStr += "</li>\n";
					});
					$('#spider_list').html(htmlStr);
					//스파이더 차트 각각 출력
					$.each(finalSenseMapList,function(index, senseDataList){
						$('#spider_' + (index < 10 ? "0"+index : index)).highcharts({
							chart: {
								polar: true,
								type: 'line',
								widht: '396',
								height: '396'
							},
							title: {
								text: ''
							},
							pane: {
								size: '90%'
							},
							xAxis: {
								categories: senseKindList,
								tickmarkPlacement: 'on',
								lineWidth: 0,
								labels:{
									style:{
										fontWeight: 'bold',
										fontSize: '12px'
									}
								}
							},
							yAxis: {
								gridLineInterpolation: 'polygon',
								lineWidth: 0,
								min: 0
							},
							tooltip: {
								shared: true,
								headerFormat: '<b>{point.x}</b>: {point.y:,.0f} 건',
								pointFormat: ''
							},
							legend: {
								enabled : false
							},
								exporting: {
								enabled : false
							},
							credits: {
								enabled: false
							},
							series: [{
								name: '감성분포',
								data: senseDataList,
								pointPlacement: 'on',
								color: '#5B9BD5'
							}]

						});
					});
					setChartSlideDiv();	//슬라이드 부분 크기 조정
				}else{
					console.log("error : " + returnData);
					$('#spiderChart').empty();
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		fnSearchTotalListFirst();	//하단 검색결과 리스트 차트
	}
	
	//검색조건 초기화
	$('#searchReset').click(function() {
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#socialChannelList ').find('option:first').attr('selected',true);
		
		$(".btn_xls").remove();
		$('#calendar_select').change();
	});
	
	//카테고리 클릭
	$(document).on('click', '.tab_tit', function(){
		
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		
		//$('#searchForm input[name=needsType]').val($(this).attr("name"));
		$('#searchForm input[name=currentPage]').val('1');
		fnSearchList();
	});
	
	//페이지 문서수 변경
	$(document).on('change','#pageSize', function(){
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnSearchList();
	});
	
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/socialEmotion/getAlikeSearch.do #alike",
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
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		var clickColumn = $('input[name="clickColumn"]').val();
		if(clickColumn == 'Y'){
			$("#searchForm").attr('action', getContextPath() + "/socialEmotion/excelClickStackVocSearchResult.do").submit();
		}
		else{
			$("#searchForm").attr('action', getContextPath() + "/socialEmotion/excelVocSearchResult.do").submit();
		}
	});
	
});

	//VOC검색결과 리스트 생성(그룹_리스트+페이징)
	function fnSearchTotalList(){
		$.ajax({
			type : "post",
			url : getContextPath()+"/emotion/vocSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#search_result').html(data);
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
   /**
	* 검색결과를 도출하는 기능을 수행
	*/
	function fnSearchTotalListFirst(){
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialEmotion/vocSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#vocSearchTeam').remove();
				$('#search_result').html(data);
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}

	//VOC검색결과 리스트 생성(리스트+페이징)
	function fnSearchList(){
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialEmotion/vocSearchResultList.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				
				$('#result_list').html(data);
				//$('#share').text($('#result_list input[name=share]').val()+'%');	//점유율 출력
				$('#share').text($('#result_list input[name=share]').val());	//점유율 대신 결과수로 대체
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}

	//감성 추세 차트 막대 클릭 시 검색결과 리스트(그룹핑+리스트+페이징) - 클릭한 조건에 맞게 검색조건을 셋팅하고 검색.
	function fnSearchClickStackTotalList(){
		var condition = $('#searchForm input[name=condition]').val();
		$('input[name="clickColumn"]').val('Y');
		$('input[name="clickCondition"]').val(condition);
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialEmotion/vocClickStackSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#vocSearchTeam').remove();
				$('#search_result').html(data);
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	//감성 추세 차트 막대 클릭 시 검색결과 리스트(리스트+페이징) - 클릭한 조건에 맞게 검색조건을 셋팅하고 검색.
	function fnSearchClickStackList(){
		var condition = $('#searchForm input[name=condition]').val();
		$('input[name="clickColumn"]').val('Y');
		$('input[name="clickCondition"]').val(condition);
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialEmotion/vocClickStackSearchResultList.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#result_list').html(data);
				//$('#share').text($('#result_list input[name=share]').val()+'%');	//점유율 출력
				$('#share').text($('#result_list input[name=share]').val());	//점유율 대신 결과수로 대체
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	//상세보기
	function detailView(id){
		$("#basic-modal-detail").load(getContextPath() + "/socialEmotion/detailView.do #detail",
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


	//페이지 이동
	function pageNavi(pageNo){
		$('#searchForm input[name=currentPage]').val(pageNo);
		var clickColumn = $('input[name="clickColumn"]').val();
		if(clickColumn == 'Y'){
			fnSearchClickStackList();
		}
		else{
			fnSearchList();	
		}
	}
	
	function validate(){

		if($('#startDate').val().length <= 0){
			alert("시작일을 설정해 주세요.");
			return false;
		}
		
		if($('#endDate').val().length <= 0){
			alert("종료일을 설정해 주세요.");
			return false;
		}
		
		return true;
	}
	