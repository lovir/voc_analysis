
$(function(){
	//하이차트 최초화면 초기화
	/*$('#reportChart').highcharts({
		title: {
			text: '',
			x: -20 //center
		},
		xAxis: {
			categories: ""
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
		series: "",
		
		chart:{
			height : 340
		}
	});*/
	//displayReport();
	
	
	//확인 버튼 클릭
	$('#searchStart').click(function(){
		if(!validate()){
			return false;
		}
		displayReport();
		return false;
	});
	
	
	
	//검색조건 초기화
	$('#searchReset').click(function() {
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');

		$('#vocChannelList ').find('option:first').attr('selected',true);
		$('#vocRecTypeList ').find('option:first').attr('selected',true);
		$('#vocKindList ').find('option:first').attr('selected',true);
		$('#vocPartList ').find('option:first').attr('selected',true);
		$('#vocItemList ').find('option:first').attr('selected',true);
		$('#repLevelList').find('option:first').attr('selected',true);
		$('#metroDeptList').find('option:first').attr('selected',true);
		
		$(".btn_xls").remove();
		$('#calendar_select').change();
	});
	
	//카테고리 클릭
	/*
	$(document).on('click', '.tab_tit', function(){
		
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		
		//$('#searchForm input[name=needsType]').val($(this).attr("name"));
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
	});
	*/
	
	//페이지 문서수 변경
	$(document).on('change','#pageSize', function(){
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
	});
	
	
	//랭킹키워드에 있는 단어 클릭시
	$(document).on('click', '.rank_key', function(){
		$('#searchForm input[name=keyword]').val($(this).attr("name"));
		//$('#searchForm input[name=needsType]').val('');
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchTotalList();
	});
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/keywordRanking/getAlikeSearch.do #alike",
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
		$("#searchForm").attr('action', getContextPath() + "/keywordRanking/excelVocSearchResult.do").submit();
	});
	
});
	function displayReport(){
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="vocChannel"]').val($('select[id="vocChannelList"] option:selected').val());
		$('input[name="vocRecType"]').val($('select[id="vocRecTypeList"] option:selected').val());
		$('input[name="vocKind"]').val($('select[id="vocKindList"] option:selected').val());
		$('input[name="vocPart"]').val($('select[id="vocPartList"] option:selected').val());
		$('input[name="vocItem"]').val($('select[id="vocItemList"] option:selected').val());
		$('input[name="repLevel"]').val($('select[id="repLevelList"] option:selected').val());
		$('input[name="metroDept"]').val($('select[id="metroDeptList"] option:selected').val());	//처리주무부서
		//리포트 차트
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/reportChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData.keywordPeriodCountList != undefined){
					//$('#reportChart').addClass('p_20');	
					$('#reportChart').highcharts({
						title: {
							text: '',
							x: -20 //center
						},
						xAxis: {
							categories: returnData.periodList
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
						series: returnData.keywordPeriodCountList,
						
						chart:{
							height : 340
						}
					});
				}
				else{
					//$('#reportChart').removeClass('p_20');
					$('#reportChart').empty();
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		//처리주무부서 리스트 조회
		$.ajax({
			type : "POST",
			url : getContextPath() + "/keywordRanking/repDeptList.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				// 처리주무부서 리스트 
				var deptData = returnData.metroDeptList;
				if(returnData.metroDeptList != undefined){
					var selectHtml = "";
					var selectDept = $('input[name="metroDept"]').val();
					selectHtml += "<option value=\"all\">전체</option>";
					for ( var i = 0, len = deptData.length; i < len; i++) {			
						var result = deptData[i];
						code = result.CODE;
						codeName = result.NAME;
						if(code == selectDept){
							selectHtml += "<option value='"+ code +"' selected >"+ codeName +"</option>";
						}
						else{
							selectHtml += "<option value='"+ code +"'>"+ codeName +"</option>";	
						}
						
					}
					$('#metroDeptList').html(selectHtml);
					//$('#searchForm input[name=keyword]').val(keyword);
					//fnsearchTotalListFirst(keyword);	
				}
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		//워드클라우드 차트
		if($('#searchForm input[name=pageType]').val() == 'synthesis'){	//종합랭킹 페이지 에서만 작동
			$.ajax({
				type : "post",
				url : getContextPath()+"/keywordRanking/wordCloudChart.do",
				data : $("#searchForm").serialize(),
				success : function(data) {
					var returnData = $.parseJSON(data);
					if(returnData != undefined){
						var chartTitleList = returnData.chartTitleList;
						var finalWordMapList = returnData.finalWordMapList;
						var htmlStr = "";
						$.each(chartTitleList,function(index, title){
							htmlStr += "<li>\n";
							htmlStr += "	<div class=\"cloud_w50\">\n";
							htmlStr += "		<div id=\"cloud_" + (index < 10 ? "0"+index : index) + "\" class=\"cloud_tag\" ></div>\n";
							htmlStr += "		<p>" + title + "</p>\n";
							htmlStr += "	</div>\n";
							htmlStr += "</li>\n";
						});
						$('#cloud_container').html(htmlStr);
						$.each(finalWordMapList,function(index, wordList){
							$('#cloud_' + (index < 10 ? "0"+index : index)).jQCloud(wordList, {
								width: 406,
								height: 396,
								//center: { x: 0.5, y: 0.5 },
								//steps: 10,
								//delay: null,
								shape: 'elliptic',
								//classPattern: 'w{n}',
								encodeURI: true,
								removeOverflowing: true,
								afterCloudRender: null,
								autoResize: true,
								fontSize: null,
								template: null
							});
						});
						setChartSlideDiv();	//슬라이드 부분 크기 조정
					}else{
						//$('#reportChart').removeClass('p_20');	
						$('#cloud_container').empty();
					}
				},
				error : function(result) {	 
					alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
				}
			});
		}
		
		//종합 랭킹 분석
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/keywordRanking/getTotalRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var temp = $('#total_Rank').html(data).find('#keywordTemp').val();//키워드
				$("input[name='keyword']").val(temp);
				$('#total_Rank').html(data);
				
				$('.rank_top_area').scrollLeft($('.rank_top_area').width() * 10);
				
				fnsearchTotalListFirst(temp);
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		//종합 랭킹 분석 스크롤 우측으로 이동
		//console.log($('#total_rank').scrollSize());
		//console.log($('#total_rank').scrollWidth());
		
		
	}
	
	
	
	//VOC검색결과 리스트 생성(그룹_리스트+페이징)
	function fnsearchTotalList(){
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchResult.do",
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
	* @param val
	*/
	function fnsearchTotalListFirst(val){
		if($('#searchForm input[name=pageType]').val()=='interest'){
			$("input[name='keyword'").val(val);
		}
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchResult.do",
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
	function fnsearchList(){
		
		if($('#searchForm input[name=pageType]').val()=='interest'){
			$('#searchForm input[name=keyword]').val($.trim($('#search_keyword').text()));
		}
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchResultList.do",
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
		$("#basic-modal-detail").load(getContextPath() + "/keywordRanking/detailView.do #detail",
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
		fnsearchList();
	}
	
	//종합랭킹분석 네비게이션
	function rankingNavi(index){
		$("#rankingIndex").val(index);
		$.ajax({
			type : "post", 
			url : getContextPath() + "/keywordRanking/getTotalRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#total_Rank').html(data);
				//종합랭킹 css
				//$('#rankNavi'+index).addClass('rank_b_on');	//이미지 사이즈 변경필요함.
				var selectTag = '#'+$('#rankNavi'+index).text();
				$(selectTag).addClass('rank_on');
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
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
	
	// 대시보드에서 넘겨온 값을 searchForm에 세팅한다
	function dashInputVal(condition){
		
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
		
		//console.log($('#searchForm input[name=condition]').val());
		$("#calendar_select").removeAttr('selected')
		$("#calendar_select option[value=\""+condition+"\"]").prop("selected", true);
		displayReport();
		//fnDashSearchResult();
		fnsearchTotalList()
	}

	/*//VOC검색결과 리스트 생성(그룹_리스트+페이징)
	function fnDashSearchResult(){
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/dashBoardSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#search_result').html(data);
				$('#share').text($('#result_list input[name=share]').val());	//점유율 대신 결과수로 대체
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}*/
	
	