
$(function () {
	
	startDisplay();
	
	function startDisplay(){
		
		//////////현황부분
		// 날짜 구하기
		getDate();
		// 역별현황
		displayStation();
		// 관심키워드
		displayInterestReport();
		// 유형별 총괄 현황
		displayKindChart();
		// 키워드 top 10
		displayKeywordRank();
		// 키워드 top 10
		displayCategoryRank();
		
		/////////// 대비부분 
		getCompareDate(); 
		// 긍부정 증감 현황
		displayEmotion();
	//	getCompareDate();
		displayIssueCloud();
	}
	
	// 확인 버튼 클릭시
	$("#searchBtn").click(function(){	
		startDisplay();
	});
	
	// 전일대비/전주대비/전월대비 시작날짜 구하기(현황일 경우만)
	function getDate(){
		
		var dateType = $('select[id="dateType"] option:selected').val();
		$('input[name="condition"]').val(dateType);
		
		var sdate = new Date();
		var edate = new Date();	
		
		//edate = new Date(Date.parse(edate) - 50 * 1000 * 60 * 60 * 24);		// 삭제예정 (로컬테스트)
		
		if("DAY" == dateType){
			sdate = new Date(edate);	
		}else if("WEEK" == dateType){
			sdate = new Date(Date.parse(edate) - 6 * 1000 * 60 * 60 * 24);	
		}else if("MONTH" == dateType){
			sdate = new Date(Date.parse(edate) - 29 * 1000 * 60 * 60 * 24);
		}
		
		var startDate = formatDate(sdate);
		var endDate = formatDate(edate);
		
		$('input[name="startDate"]').val(startDate);
		$('input[name="endDate"]').val(endDate);

	}
	// 전일대비/전주대비/전월대비 시작날짜 구하기(대비일 경우)
	function getCompareDate(){
		
		var dateType = $('select[id="dateType"] option:selected').val();
		$('input[name="condition"]').val(dateType);
		
		var sdate = new Date();
		var edate = new Date();	
		
		var prevSdate = new Date();
		var prevEdate = new Date();
		
		//edate = new Date(Date.parse(edate) - 50 * 1000 * 60 * 60 * 24);		// 삭제예정 (로컬테스트)
		
		if("DAY" == dateType){
			prevSdate = new Date(Date.parse(edate) - 1000 * 60 * 60 * 24);	
			prevEdate = new Date(Date.parse(edate) - 1000 * 60 * 60 * 24);	
		}else if("WEEK" == dateType){
			prevSdate = new Date(Date.parse(edate) - 13 * 1000 * 60 * 60 * 24);	
			prevEdate = new Date(Date.parse(edate) - 7 * 1000 * 60 * 60 * 24);	
		}else if("MONTH" == dateType){
			prevSdate = new Date(Date.parse(edate) - 59 * 1000 * 60 * 60 * 24);
			prevEdate = new Date(Date.parse(edate) - 30 * 1000 * 60 * 60 * 24);	
		}
		
		if("DAY" == dateType){
			sdate = new Date(Date.parse(edate));	
		}else if("WEEK" == dateType){
			sdate = new Date(Date.parse(edate) - 6 * 1000 * 60 * 60 * 24);	
		}else if("MONTH" == dateType){
			sdate = new Date(Date.parse(edate) - 29 * 1000 * 60 * 60 * 24);
		}
		
		var startDate = formatDate(sdate);
		var endDate = formatDate(edate);
		var prevStartDate = formatDate(prevSdate);
		var prevEndDate = formatDate(prevEdate);
		
		$('input[name="prevStartDate"]').val(prevStartDate);
		$('input[name="prevEndDate"]').val(prevEndDate);
		
		$('input[name="startDate"]').val(startDate);
		$('input[name="endDate"]').val(endDate);

	}
	function formatDate(date) {
	  var year = date.getFullYear();

	  var month = (1 + date.getMonth()).toString();
	  month = month.length > 1 ? month : '0' + month;

	  var day = date.getDate().toString();
	  day = day.length > 1 ? day : '0' + day;
	  
	  return year + '/' + month + '/' + day;
	}
	
	// 역별 현황 
	function displayStation(){

		var totalSize = "";		
		$.ajax({
			type : "post",
			async : false,
			url : getContextPath()+"/dashBoard/stationChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				//console.log(data);
				var allData = $.parseJSON(data);
				var statusHtml = "";
				
				var returnData = allData.stationChart;
				
				for (var i = 2; i < returnData.length; i++) {

					if(i%2 == 0){
						statusHtml += "<div class=\"line_half mr_10\">";
					}else{
						statusHtml += "<div class=\"line_half\">";
					}
					statusHtml += "<p class=\"t_line"+returnData[i].LINE+"\"><a href=\"#\" class=\"modal_btn\" name=\"doc_03\" >"+returnData[i].NAME+"</a></p>";
					statusHtml += "<p class=\"bg_gray mt_5\"><span class=\"num1\">"+returnData[i].COUNT+"개</span>";
					statusHtml += "<span class=\"keyword_m\">"+returnData[i].KEYWORD+"</span>"
										
					var emotion = "neu";
					if(Number(returnData[i].POSITIVE) > Number(returnData[i].NEGATIVE) && Number(returnData[i].POSITIVE) > Number(returnData[i].NEUTRAL))
						emotion = "smile";
					else if(Number(returnData[i].NEGATIVE) > Number(returnData[i].POSITIVE) && Number(returnData[i].NEGATIVE) > Number(returnData[i].NEUTRAL))
						emotion = "sad";
					statusHtml += "<span class=\""+emotion+"\"></span></p>";
					statusHtml += "<p class=\"condition mt_5\">";
					statusHtml += "<span class=\"bule_bg mr_5\">"+returnData[i].POSITIVE+"개</span>";		
					statusHtml += "<span class=\"green_bg mr_5\">"+returnData[i].NEUTRAL+"개</span>";
					statusHtml += "<span class=\"orange_bg\">"+returnData[i].NEGATIVE+"개</span></p></div>";
				}
				
				$('#stationChart').html(statusHtml);
			
			}
		});
	}
	$(document).on("click","#stationChart .modal_btn",function(){  		
		// 화면 출력용	
		$('#basic-modal-doc_03').modal({
			persist: false,  //이전 변경내용 유지 안함
			focus: false,	//포커스제거
			onClose: function () {
			   $('body').css('overflow','auto');
			   $.modal.close();
			}
		});

		$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
		return false;

	});
	// 역별 현황 모달 출력
	$(document).on("click",".line_half a",function(){  	
		
		$('#rank_name').html($(this).text()+" 상세보기");
		$('.rank_top').html($(this).text()+" 키워드 Top 10");
		// 키워드 출력
		var statusHtml = "";
		$('input[name=stationName]').val($(this).text());
		//console.log($(this).text());
		$.ajax({
			type : "post",
			url : getContextPath()+"/dashBoard/stationKeyword.do",
			data : $("#searchForm").serialize(),
			success : function(data) {				
				//console.log(data);
				var returnData = $.parseJSON(data);
				statusHtml += "<ul>";
				for (var i = 0; i < returnData.length; i++) {
					statusHtml += "<li><span class=\"rank_no\">"+Number(i+1)+"</span>";
					statusHtml += "<p class=\"rank_key\">"+returnData[i].NAME+"</p>";
					statusHtml += "<span class=\"rank_right\">"+returnData[i].COUNT+"</span></li>";
				}
				statusHtml += "</ul>";
		
				$('.rank_list').html(statusHtml);
			}
		});
		
		//console.log($(this).parent().siblings().find( ".orange_bg" ).text());
		var pos = $(this).parent().siblings().find( ".orange_bg" ).text();
		var neg = $(this).parent().siblings().find( ".bule_bg" ).text();
		var neu = $(this).parent().siblings().find( ".green_bg" ).text();
		pos = pos.substring(0, pos.length-1);
		neg = neg.substring(0, neg.length-1);
		neu = neu.substring(0, neu.length-1);
		
		var pieArr = new Array();
		var pieObjPos = new Object();
		pieObjPos.name = "긍정";
		pieObjPos.y = Number(pos);
		var pieObjNeg = new Object();
		pieObjNeg.name = "부정";
		pieObjNeg.y = Number(neg);
		var pieObjNeu = new Object();
		pieObjNeu.name = "중립";
		pieObjNeu.y = Number(neu);
		
		pieArr.push(pieObjPos);
		pieArr.push(pieObjNeg);
		pieArr.push(pieObjNeu);
				
		// 파이차트
		$('.chart_w01').highcharts({
		    chart: {
		        plotBackgroundColor: null,
		        plotBorderWidth: null,
		        plotShadow: false,
		        type: 'pie'
		    },
		    title: {
		        text: '긍/부정 비율'
		    },
		    tooltip: {
		        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		    },
		    plotOptions: {
		        pie: {
		            allowPointSelect: true,
		            cursor: 'pointer',
		            dataLabels: {
		                enabled: true,
		                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                style: {
		                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                }
		            }
		        }
		    },
		    series: [{
		    	name:'긍/부정 비율', 
		    	data: pieArr
		    	
		    }]
		});
	});
	

	// 관심키워드
	function displayInterestReport(){
		$('input[name="pageType"]').val('interest');
		
		//리포트 차트
		$.ajax({
			type : "post",
			url : getContextPath()+"/dashBoard/interestChart.do",
			data : $("#searchForm").serialize(),
			async : false,
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData.keywordPeriodCountList != undefined){
					//$('#reportChart').addClass('p_20');	
					$('#interestChart').highcharts({
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
							height : 310
						}
					});
				}
				else{
					$('#interestChart').empty();
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});	
	}
	// 유형별 총괄 현황
	function displayKindChart(){
		
		var kindHtml = "";

		$.ajax({
			type : "post",
			async : false,
			url : getContextPath()+"/dashBoard/kindChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				//console.log(data);
				var allData = $.parseJSON(data);
				var returnData = allData.countList
				//console.log(returnData.countList);
				/// 총계
				kindHtml += "<tr>";
				kindHtml += "<td class=\"bg\" style=\"border-top:1px solid #fff;\">계</td>";
				for(var i=0; i<returnData.length; i++){
					
					if(i == returnData.length-1){					
						kindHtml += "<td style=\"border-right: none;\">"+returnData[i].TOTAL+"</td>";
					}
					else{
						kindHtml += "<td><p>"+returnData[i].TOTAL+"</p></td>";
					}
				}
				kindHtml += "</tr>";
				/// 단순문의
				kindHtml += "<tr>";
				kindHtml += "<td class=\"bg\">단순문의</td>";
				for(var i=0; i<returnData.length; i++){
					
					if(i == returnData.length-1){					
						kindHtml += "<td style=\"border-right: none;\">"+returnData[i].COUNSEL+"</td>";
					}else{
						kindHtml += "<td><p>"+returnData[i].COUNSEL+"</p></td>";
					}
				}
				kindHtml += "</tr>";
				/// 불편개선
				kindHtml += "<tr>";
				kindHtml += "<td class=\"bg\">불편개선</td>";
				for(var i=0; i<returnData.length; i++){
					
					if(i == returnData.length-1){					
						kindHtml += "<td style=\"border-right: none;\">"+returnData[i].CLAIM+"</td>";
					}else{
						kindHtml += "<td><p>"+returnData[i].CLAIM+"</p></td>";
					}
				}
				kindHtml += "</tr>";
				/// 칭찬격려
				kindHtml += "<tr>";
				kindHtml += "<td class=\"bg\">칭찬격려</td>";
				for(var i=0; i<returnData.length; i++){
					
					if(i == returnData.length-1){					
						kindHtml += "<td style=\"border-right: none;\">"+returnData[i].GOOD+"</td>";
					}else{
						kindHtml += "<td><p>"+returnData[i].GOOD+"</p></td>";
					}
				}
				kindHtml += "</tr>";
				//console.log(kindHtml);
			}
		});	
		$('#kindChart').html(kindHtml);
	}
	// 키워드 top 10
	function displayKeywordRank(){
		
		var dateType = $('input[name="condition"]').val();
		
		if(dateType == 'DAY'){
			$('#keywordrankSpan').text('금일 키워드 TOP10');
		}
		else if(dateType == 'WEEK'){
			$('#keywordrankSpan').text('금주 키워드 TOP10');
		}else if(dateType == 'MONTH'){
			$('#keywordrankSpan').text('금월 키워드 TOP10');
		}
		
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/dashBoard/getTotalRanking.do",
			async : false,
			data : $("#searchForm").serialize(),
			success : function(data) {
				
				//console.log(data);
				$('#total_Rank').html(data);
				
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요."+error);
			}
		});
	}
	/*// 금주 키워드 탑 top 10 키워드 클릭
	$('.rank_key').on('click',function(){
		goTrendAnalysis($(this).text());
	});*/
	
	// 카테고리 top 10
	function displayCategoryRank(){		
		var dateType = $('input[name="condition"]').val();
		
		if(dateType == 'DAY'){
			$('#categoryrankSpan').text('금일 분야별 TOP10');
		}
		else if(dateType == 'WEEK'){
			$('#categoryrankSpan').text('금주 분야별 TOP10');
		}else if(dateType == 'MONTH'){
			$('#categoryrankSpan').text('금월 분야별 TOP10');
		}
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/dashBoard/getCategoryRanking.do",
			async : false,
			data : $("#searchForm").serialize(),
			success : function(data) {

				$('#category_Rank').html(data);
				
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요."+error);
			}
		});
	}
	
	function displayEmotion(){
		
		var condition = $('input[name="condition"]').val();
		var label = "DAY";
		if(condition == 'DAY'){
			label = '금일';
		}else if(condition == "WEEK"){
			label = "금주";
		}else if(condition == "MONTH"){
			label = "금월";
		}
		
		$.ajax({
			type : "post",	 
			url : getContextPath() + "/dashBoard/getEmotionStatus.do",
			async: false,
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				
				for(var i=0; i<returnData.length; i++){
					
					
					$('#d_keyword'+i).html("("+label+":"+returnData[i].current+"건)");
					$('#needs'+i).highcharts({
						chart: {
							type: 'pie',
							plotBackgroundColor: null,
							plotBorderWidth: null,
							plotShadow: false,
							margin:[0, 0, 0, 0],
							spacingTop: 0,
							spacingBottom: 0,
							spacingLeft: 0,
							spacingRight:0,
							events: {
								load: function (){
									var ren = this.renderer;
									if(returnData[i].count > 0){
										ren.image('../resources/images/common/arrow_up.png', 23, 43, 16, 17).add();
									}else if(returnData[i].count == 0){
										
									}else{
										ren.image('../resources/images/common/arrow_down.png', 23, 43, 16, 17).add();
									}
								}
							}
						},
						title: {
							text: null
						},
						subtitle: {
							text: null
						},
						credits:{
							enabled:false
						},
						legend:{
							enabled:false
						},
						exporting: {
							enabled:false
						},
						plotOptions : {
							pie: {
								shadow: false,
								size: '105%',
								allowPointSelect: true,
								borderWidth: 0,
								cursor: 'pointer',
								dataLabels: {
									enabled: false
								},
								innerSize: '75%'
							},
							series: {
								states: {
									hover: {
										enabled: false
									}
								}
							}
						},
						labels: {
							style: {
								color: '#000',
								fontSize: '14px',
								fontFamily: 'arial'
							},
							items: [
									{
										html: returnData[i].percentage+'%',
										style: {
											left: '40px',
											fontSize: '13px',
											top: '45px'
										}
									},
									{
										html: returnData[i].count+'건',
										style: {
											left: '40px',
											top: '60px',
											fontSize: '11px',
											display:'block'
										}
									}
								]
						},
						lang: {
							noData: ''
						},
						tooltip: {
							style: {
								fontSize: '10px'
							},
							formatter : function(){
								if(this.point.y==99999999){
									return '당월/전월:0건';
								}else{
									
									return this.point.name+':'+this.point.y+'건';
								}
							}
						},
						series: [{
							name: '발생건수',
							events: {
					    		click: function(event) {
					    						
					    			//displayKeyword(event.point.code);
					    			// 클릭시 해당 분야 결과 출력
					    			//$('input[name=searchType]').val("rep");	
					    			//$('input[name=keyword]').val(event.point.code);	
					    			//fnsearchTotalList();
					    			//console.log(event.point.gubun);
					    			goEmotionPage(event.point.gubun);
					    		}
					         },
							data: returnData[i].data,
							
							dataLabels: {
								enabled: false
							}
						}]
					});
				}
			}
		});
		
	}
	
	// 긍부정 원 클릭시 감성분석 페이지로 워프됨 
	function goEmotionPage(gubun){
		var portal_id = $('input[name="portal_id"]').val();
		var portal_nm = $('input[name="portal_nm"]').val();
		
		var form = document.createElement("form");     
		form.setAttribute("method","post");
		form.setAttribute("action","http://" + location.host+ "/voc_analysis/emotion/emotionAnalysisInit.do?portal_id="+ encodeURIComponent(portal_id) + "&portal_nm=" + encodeURIComponent(portal_nm));
		document.body.appendChild(form);
		//input
		var inputKeyword = document.createElement("input");  
		inputKeyword.setAttribute("type", "hidden");
		inputKeyword.setAttribute("name", "dashKeyword");
		inputKeyword.setAttribute("value", gubun);
		form.appendChild(inputKeyword);
		var inputCond = document.createElement("input");  
		inputCond.setAttribute("type", "hidden");
		inputCond.setAttribute("name", "dashCondition");
		inputCond.setAttribute("value", $('input[name="condition"]').val());
		form.appendChild(inputCond);
		 
		
		//폼전송
		form.submit();  
	}
	
	function displayIssueCloud(){
		
		//워드클라우드 차트
		$('#cloudChart').empty();
		$.ajax({
			type : "post",
			url : getContextPath()+"/dashBoard/wordCloudChart.do",
			data : $("#searchForm").serialize(),
			async : false,
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData != undefined){
					
					var finalWordMapList = returnData.finalWordMapList;
					$('#cloudChart').jQCloud('destroy');
					//console.log(finalWordMapList);
					$('#cloudChart').jQCloud(finalWordMapList, {
						width: 409,
						height: 250,
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
					
				}else{
				
					$('#cloudChart').empty();
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	//유형별 현황 테이블 클릭 시 "종합 키워드 분석" 이동
	$('.tbl_type05').on('click',function(){
		goSynthesisRankingPage();
	});
});

$(document).on("click","#cloudChart a",function(){  		
	var color = $(this).parent().css('color');
	if(color == "rgb(244, 148, 24)"){
		issueCloudClick($(this).text(), 'CALL');
	}else if(color == "rgb(221, 34, 34)"){
		issueCloudClick($(this).text(), 'MINWON');
	}
	else if(color == "rgb(34, 103, 221)"){
		issueCloudClick($(this).text(), 'SOCIAL');
	}
	
	
});

// 이슈 클라우드 클릭시 모달 생성 
function issueCloudClick(keyword, channelGubun){
	
	$('input[name="keyword"]').val(keyword);
	$('input[name="channel"]').val(channelGubun);
	//라인 차트 그리기
	$.ajax({
		type : "post",
		url : getContextPath()+"/dashBoard/wordCloudClick.do",
		data : $("#searchForm").serialize(),
	
		success : function(data) {
			//console.log(data);
			var returnData = $.parseJSON(data);				
			if(returnData.series != undefined){	

				$('#issueLineChart').highcharts({
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
			
			var condition = $('input[name="condition"]').val();
			var tableHeadStr = "";
			tableHeadStr += "<tr>";
			tableHeadStr += "<th scope=\"col\">키워드</th>";
			if(condition == 'DAY'){
				tableHeadStr += "<th scope=\"col\">전일 건수</th>";
				tableHeadStr += "<th scope=\"col\">금일 건수</th>";
			}else if(condition == 'WEEK'){
				tableHeadStr += "<th scope=\"col\">전주 건수</th>";
				tableHeadStr += "<th scope=\"col\">금주 건수</th>";
			}else if(condition == 'MONTH'){
				tableHeadStr += "<th scope=\"col\">전월 건수</th>";
				tableHeadStr += "<th scope=\"col\">금월 건수</th>";
			}
			/*tableHeadStr += "<th scope=\"col\">해당 유형 내 점유율</th>";*/
			tableHeadStr += "</tr>";

			$('#issueTableHead').html(tableHeadStr);
			var tableStr = "";
			tableStr += "<tr>";
			tableStr += "<td>"+returnData.keyword+"</td>";
			tableStr += "<td>"+returnData.prevCnt+"</td>";
			tableStr += "<td>"+returnData.currCnt+"</td>";
			//tableStr += "<td>"+returnData.share+"%</td>";
			tableStr += "</tr>";
			$('#issueTable').html(tableStr);
		}
	});
	// 화면 출력용	
	$('#basic-modal-cloud_view').modal({
		persist: false,  //이전 변경내용 유지 안함
		focus: false,	//포커스제거
		onClose: function () {
		   $('body').css('overflow','auto');
		   $.modal.close();
		}
	});

	$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
	
	
}

// 역별 현황 페이지로 이동 
function goStation(){
	var portal_id = $('input[name="portal_id"]').val();
	var portal_nm = $('input[name="portal_nm"]').val();
	
	var form = document.createElement("form");     
	form.setAttribute("method","post");
	form.setAttribute("action","http://" + location.host+ "/voc_analysis/stationStatus/stationStatusInit.do?portal_id="+ encodeURIComponent(portal_id) + "&portal_nm=" + encodeURIComponent(portal_nm));
	document.body.appendChild(form);
	//input
	/*var inputKeyword = document.createElement("input");  
	inputKeyword.setAttribute("type", "hidden");
	inputKeyword.setAttribute("name", "dashKeyword");
	inputKeyword.setAttribute("value", gubun);
	form.appendChild(inputKeyword);*/
	var inputCond = document.createElement("input");  
	inputCond.setAttribute("type", "hidden");
	inputCond.setAttribute("name", "dashCondition");
	inputCond.setAttribute("value", $('input[name="condition"]').val());
	form.appendChild(inputCond);
	 
	
	//폼전송
	form.submit();  	
}

// 카테고리 Top10 클릭시 분야별 현황 페이지로 이동
function goFieldStatus(){
	var portal_id = $('input[name="portal_id"]').val();
	var portal_nm = $('input[name="portal_nm"]').val();
	
	var form = document.createElement("form");     
	form.setAttribute("method","post");
	form.setAttribute("action","http://" + location.host+ "/voc_analysis/fieldStatus/fieldStatusInit.do?portal_id="+ encodeURIComponent(portal_id) + "&portal_nm=" + encodeURIComponent(portal_nm));
	document.body.appendChild(form);
	//input
	/*var inputKeyword = document.createElement("input");  
	inputKeyword.setAttribute("type", "hidden");
	inputKeyword.setAttribute("name", "dashKeyword");
	inputKeyword.setAttribute("value", gubun);
	form.appendChild(inputKeyword);*/
	var inputCond = document.createElement("input");  
	inputCond.setAttribute("type", "hidden");
	inputCond.setAttribute("name", "dashCondition");
	inputCond.setAttribute("value", $('input[name="condition"]').val());
	form.appendChild(inputCond);
	 
	
	//폼전송
	form.submit();  	
}

//"키워드 분석 > 관심키워드 랭킹" 페이지로 워프됨 
function goInterestKeywordRankingPage(){
	var portal_id = $('input[name="portal_id"]').val();
	var portal_nm = $('input[name="portal_nm"]').val();
	
	var form = document.createElement("form");     
	form.setAttribute("method","post");
	form.setAttribute("action","http://" + location.host+ "/voc_analysis/keywordRanking/interestRankingInit.do?portal_id="+ encodeURIComponent(portal_id) + "&portal_nm=" + encodeURIComponent(portal_nm));
	document.body.appendChild(form);
	//input
	var inputKeyword = document.createElement("input");  
	var inputCond = document.createElement("input");  
	inputCond.setAttribute("type", "hidden");
	inputCond.setAttribute("name", "dashCondition");
	inputCond.setAttribute("value", $('input[name="condition"]').val());
	form.appendChild(inputCond);
	
	//폼전송
	form.submit();  
}

//"키워드 분석 > 종합 키워드 랭킹" 페이지로 워프됨 
function goSynthesisRankingPage(){
	var portal_id = $('input[name="portal_id"]').val();
	var portal_nm = $('input[name="portal_nm"]').val();
	
	var form = document.createElement("form");     
	form.setAttribute("method","post");
	form.setAttribute("action","http://" + location.host+ "/voc_analysis/keywordRanking/synthesisRankingInit.do?portal_id="+ encodeURIComponent(portal_id) + "&portal_nm=" + encodeURIComponent(portal_nm));
	document.body.appendChild(form);
	//input
	var inputKeyword = document.createElement("input");  
	var inputCond = document.createElement("input");  
	inputCond.setAttribute("type", "hidden");
	inputCond.setAttribute("name", "dashCondition");
	inputCond.setAttribute("value", $('input[name="condition"]').val());
	form.appendChild(inputCond);
	
	//폼전송
	form.submit();  
}
//"트렌드 분석" 페이지로 워프됨 
function goTrendAnalysis(keyword){
	var portal_id = $('input[name="portal_id"]').val();
	var portal_nm = $('input[name="portal_nm"]').val();
	
	var form = document.createElement("form");     
	form.setAttribute("method","post");
	form.setAttribute("action","http://" + location.host+ "/voc_analysis/trend/trendAnalysisInit.do?portal_id="+ encodeURIComponent(portal_id) + "&portal_nm=" + encodeURIComponent(portal_nm));
	document.body.appendChild(form);
	//input
	var inputKeyword = document.createElement("input");  
	inputKeyword.setAttribute("type", "hidden");
	inputKeyword.setAttribute("name", "dashKeyword");
	inputKeyword.setAttribute("value", encodeURIComponent(keyword));
	form.appendChild(inputKeyword);
	var inputCond = document.createElement("input");  
	inputCond.setAttribute("type", "hidden");
	inputCond.setAttribute("name", "dashCondition");
	inputCond.setAttribute("value", $('input[name="condition"]').val());
	form.appendChild(inputCond);
	
	//폼전송
	form.submit();  
}	
	/////////////////////////////////////////////////////////////////////////////////// 아래는 이전 소스
/*	if($("#conditionCheck").val() == "MONTH"){
		
		$("#month").prop("selected",true);
	}
	if($("#conditionCheck").val() == "WEEK"){
		
		$("#week").prop("selected",true);
	}
	
	if($("#collectionCkeck").val() == "VOC"){
		
		$("#voc").prop("selected",true);
	}
	if($("#collectionCkeck").val() == "SNS"){
		
		$("#sns").prop("selected",true);
	}
	

	$('#btnSearch').on('click', function(){
		$("#searchForm").removeAttr("target");
		$("#searchForm").attr('action', getContextPath() + "/dashBoard/search.do").submit();
	});
	
	// 상세 보기 클릭
	$('.complain_list a').on('click', function(){
		var docId = $(this).attr('id');
		var json = {
			"id" : docId
		};
		
		$("#basic-modal-complain_view").load(getContextPath() + "/dashBoard/dashBoardComplainDetailView.do #complain",
				json,
			function(){
				$("#basic-modal-complain_view").modal({
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
	
	// Issue Cloud #1 클릭
	$('#canvasBtn').on('click', function(){
		
		$(this).addClass('on');
		$('#cloudBtn').removeClass('on');

		$('#tags').empty();
		
		$('#tags').append('<canvas id="issueCanvas" width="628px" height="290px" ></canvas>');
		
		TagCanvas.Start('issueCanvas', 'jcloud-tags',{
			textFont : "NanumGothicBold",
			textColour: null,
			textHeight: 10,
			outlineThickness: 1,
			weight: true,
			weightFrom: "data-weight",
			weightMode: "both",
			reverse: true,
			depth: 0.8,
			maxSpeed: 0.05,
			minBrightness: 0.1,
			shadowBlur: 0,
			shape: "vcylinder",
			lock: "y",
			wheelZoom: false,
			active: true,
			freezeActive: true
		});
		
		return false;
	});
	
	// Issue Cloud #2 클릭
	$('#cloudBtn').on('click', function(){
		$(this).addClass('on');
		$('#canvasBtn').removeClass('on');

		$('#tags').empty();
		
		$('#tags').jQCloud(words, {
			height: 290,
			shape: 'elliptic'
		});
		
		return false;
	});
	
	$('#tags a').live('click', function(){
		fnDetailItem($(this).text());
		return false;
	});
	
});

// 이슈 클라우드 상세 모달
function fnDetailItem(term){
	
	var condition = $('#condition option:selected').val();
	var collectionCheck = $("#collectionCkeck").val();
	
	var json = {
		"term" : term,
		"condition" : "MONTH",
		"collection" : collectionCheck
	};
	
	$("#basic-modal-cloud_view").load(getContextPath() + "/dashBoard/dashBoardIssueDetailView.do",
			json,
		function(){
			$("#basic-modal-cloud_view").modal({
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
}

function openBlankFrame( frameName, width, height ) {
	 var winprops = "";
	 
	 winprops    += "toolbar=no,menubar=no,scrollbars=yes,statusbar=no,resizable=yes";
	 winprops    += ",width="+width+",height="+height;
	 
	 window.open( "", frameName, winprops );
}

function regionSearch(emotion, guname, startDate, endDate,currentPage,pagesize){

	var emotion = emotion;
	var guname = guname;
	var startDate = startDate;
	var endDate = endDate;
	var currentPage = currentPage;
	var pagesize = pagesize;
	$("#emotion").val(emotion);
	$("#guname").val(guname);
	$("#startDate").val(startDate);
	$("#endDate").val(endDate);
	$("#pagesize").val(pagesize);
	
	var myForm = document.searchForm;
	var url = getContextPath()+"/dashBoard/regionSearch.do";
	window.open("" ,"popForm", "toolbar=no, width=1500, height=1227, directories=no, status=no, resizable=no"); 
	myForm.action =url; 
	myForm.method="post";
	myForm.target="popForm";
	myForm.submit();
}*/
