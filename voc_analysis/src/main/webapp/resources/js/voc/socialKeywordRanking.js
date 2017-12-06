
$(function(){
	
	//확인 버튼 클릭
	$('#searchStart').click(function(){
		if(!validate()){
			return false;
		}
		displayReport();
		return false;
	});
	
	function displayReport(){
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="socialChannel"]').val($('select[id="socialChannelList"] option:selected').val());
		
		//리포트 차트
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialKeywordRanking/reportChart.do",
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
		
		//워드클라우드 차트
		if($('#searchForm input[name=pageType]').val() == 'synthesis'){	//종합랭킹 페이지 에서만 작동
			$.ajax({
				type : "post",
				url : getContextPath()+"/socialKeywordRanking/wordCloudChart.do",
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
								width: 396,
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
			url : getContextPath() + "/socialKeywordRanking/getTotalRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				//console.log("data:"+data);
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
	
	//페이지 문서수 변경
	$(document).on('change','#pageSize', function(){
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
	});
	
	
	//랭킹키워드에 있는 단어 클릭시
	$(document).on('click', '.rank_key', function(){
		$('#searchForm input[name=keyword]').val($(this).attr("name"));
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchTotalListFirst($(this).attr("name"));
	});
	
	
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/socialKeywordRanking/getAlikeSearch.do #alike",
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
		$("#searchForm").attr('action', getContextPath() + "/socialKeywordRanking/excelVocSearchResult.do").submit();
	});
	
	//채널별 수집현황 클릭
	$('#spiderBtn').click(function(){
		// 현재 시간 구하기
		var d = new Date();
		var time = '['+d.getFullYear()+'년 '+(d.getMonth() + 1)+'월 '+d.getDate()+'일 '+d.getHours()+':'+d.getMinutes()+' 채널별 수집 현황]';

		$('#simplemodal-container').css({top:"16px"});
		$('#spiderTime').text(time);
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialKeywordRanking/socialSpiderStatus.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				//console.log(data);
				var returnData = $.parseJSON(data);
				var tableStr = '<thead><tr><th>채널</th><th>사이트</th><th>누적 수집 건수</th><th>오늘 수집 건수</th></tr></thead><tbody>';
				var newsList = returnData.newsList;
				if(newsList.length > 0){
					tableStr += '<tr>';
					tableStr += '<td rowspan=\"'+newsList.length+'\">뉴스</td>';
				}
				for(var i=0; i<newsList.length; i++){
					if(i==0){
						tableStr += '<td>'+newsList[i].NAME+'</td>';
						tableStr += '<td>'+newsList[i].All+'</td>';
						tableStr += '<td>'+newsList[i].TODAY+'</td>';
						tableStr += '</tr>';
					}
					else{
						tableStr += '<tr>';
						tableStr += '<td>'+newsList[i].NAME+'</td>';
						tableStr += '<td>'+newsList[i].All+'</td>';
						tableStr += '<td>'+newsList[i].TODAY+'</td>';
						tableStr += '</tr>';
					}
				}
				var facebookList = returnData.facebookList;
				var twitterList = returnData.twitterList;
				tableStr += '<tr>';
				tableStr += '<td rowspan=\"2\">SNS</td>';							
				tableStr += '<td>'+facebookList[0].NAME+'</td>';
				tableStr += '<td>'+facebookList[0].All+'</td>';
				tableStr += '<td>'+facebookList[0].TODAY+'</td>';
				tableStr += '</tr>';
				tableStr += '<tr>';
				tableStr += '<td>'+twitterList[0].NAME+'</td>';
				tableStr += '<td>'+twitterList[0].All+'</td>';
				tableStr += '<td>'+twitterList[0].TODAY+'</td>';
				tableStr += '</tr>';
					
				var siteList = returnData.siteList;
				if(siteList.length > 0){
					tableStr += '<tr>';
					tableStr += '<td rowspan=\"'+siteList.length+'\">관련 사이트</td>';
				}
				for(var i=0; i<siteList.length; i++){
					if(i==0){
						tableStr += '<td>'+siteList[i].NAME+'</td>';
						tableStr += '<td>'+siteList[i].All+'</td>';
						tableStr += '<td>'+siteList[i].TODAY+'</td>';
						tableStr += '</tr>';
					}
					else{
						tableStr += '<tr>';
						tableStr += '<td>'+siteList[i].NAME+'</td>';
						tableStr += '<td>'+siteList[i].All+'</td>';
						tableStr += '<td>'+siteList[i].TODAY+'</td>';
						tableStr += '</tr>';
					}
				}
				var communityList = returnData.communityList;
				if(communityList.length > 0){
					tableStr += '<tr>';
					tableStr += '<td rowspan=\"'+communityList.length+'\">커뮤니티</td>';
				}
				for(var i=0; i<siteList.length; i++){
					if(i==0){
						tableStr += '<td>'+communityList[i].NAME+'</td>';
						tableStr += '<td>'+communityList[i].All+'</td>';
						tableStr += '<td>'+communityList[i].TODAY+'</td>';
						tableStr += '</tr>';
					}
					else{
						tableStr += '<tr>';
						tableStr += '<td>'+communityList[i].NAME+'</td>';
						tableStr += '<td>'+communityList[i].All+'</td>';
						tableStr += '<td>'+communityList[i].TODAY+'</td>';
						tableStr += '</tr>';
					}
				}
			
				$('#spiderTable').html(tableStr);
			}
		});
	
	
	});
});

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
			url : getContextPath()+"/socialKeywordRanking/vocSearchResult.do",
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
			url : getContextPath()+"/socialKeywordRanking/vocSearchResult.do",
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
		$("#basic-modal-detail").load(getContextPath() + "/socialKeywordRanking/detailView.do #detail",
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
		fnsearchTotalListFirst($('#searchForm input[name=keyword]').val());
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
	