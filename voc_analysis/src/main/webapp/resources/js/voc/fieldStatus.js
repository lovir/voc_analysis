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
	
	$('#searchForm input[name=currentPage]').val('1');
	$('#searchForm input[name=condition]').val(condition);
	
	//displayPie();
	
	$("#calendar_select").removeAttr('selected')
	$("#calendar_select option[value=\""+condition+"\"]").prop("selected", true);
	
}
$(function(){
	// 검색클릭 이벤트
	$(document).on("click","#searchStart",function(){  		
		
		if($('#vocChannelList option:selected').val() == 'all' || $('#vocKindList option:selected').val() == 'all' 
			|| $('#vocPartList option:selected').val() == 'all'){
			alert('접수채널, 대분류, 중분류를 모두 선택해 주시기 바랍니다.');
			return ;
		}
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		$('input[name=searchType]').val("all");	
		//$('input[name=keyword]').val(event.point.code);	
		
		$("#fieldCont").show();
		$("#search_result").show();
		
		$(".rank_keyword").hide();
		displayPie();
		fnsearchTotalList();

	});		
	
	
	
	function displayKeyword(code){

		$('input[name=vocItem]').val(code);	
		var keywordHtml = "";
		keywordHtml += "<ul>";
		$.ajax({
			type : "post",
			url : getContextPath()+"/fieldStatus/fieldStatusKeyword.do",
			data : $("#searchForm").serialize(),
			async: false,
			success : function(data) {
				
				var returnData = $.parseJSON(data);	
				
				for (var i = 0; i < returnData.length; i++) {	
					
					keywordHtml += "<li><span class=\"rank_no\">"+Number(i+1)+"</span>";
					keywordHtml += "<a href=\"#\" class=\"rank_key\">"+returnData[i].KEYWORD+"</a>";
					keywordHtml += "<span class=\"rank_right\">"+returnData[i].COUNT+"</span></li>";
					console.log(keywordHtml);
				}
			}
		});
		keywordHtml += "</ul>";
		$('.rank_list').html(keywordHtml);
	}
	
	$(document).on("click","#lineChart",function(){  		
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		
		displayLine();	
	});		
	
	$(document).on("click","#pieChart",function(){  		
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		
		displayPie();	
	});		
	
	function displayLine(){
		
		$("#chart01").hide();
		$("#chart02").show();
		console.log(1);
		//리포트 차트
		$.ajax({
			type : "post",
			url : getContextPath()+"/fieldStatus/reportChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData.fieldPeriodCountList != undefined){	
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
						series: returnData.fieldPeriodCountList,
						
						chart:{
							height : 340
						}
					});
				}
			}
		});
	}
	
	//키워드 클릭시
	$(document).on('click', '.rank_key', function(){

		$('#searchForm input[name=keyword]').val($.trim($(this).text()));		
		$('input[name=searchType]').val('keyword');
		
		fnsearchTotalList();
	});
	
	//엑셀 다운 클릭
	$(document).on('click', '.btn_xls', function(){
		$("#searchForm").attr('action', getContextPath() + "/fieldStatus/excelVocSearchResult.do").submit();
	});
	//상세보기
	$(document).on('click', '.result_detail', function(){
		
		var id =  $.trim($(this).attr("name"));
		
		$("#basic-modal-detail").load(getContextPath() + "/fieldStatus/detailView.do #detail",
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
			$("#basic-modal-alike").load(getContextPath() + "/fieldStatus/getAlikeSearch.do #alike",
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
	
	// 초기화 
	$(document).on('click', '#searchReset', function(){
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		
		$('#vocChannelList ').find('option:first').attr('selected',true);
		$('#vocRecTypeList ').find('option:first').attr('selected',true);
		$('#vocKindList ').find('option:first').attr('selected',true);
		$('#vocPartList ').find('option:first').attr('selected',true);
		//$('#vocItemList ').find('option:first').attr('selected',true);
		//$('#repLevelList').find('option:first').attr('selected',true);
		
		//$(".btn_xls").remove();
		$('#calendar_select').change();
	});
});
function displayPie(){
	
	$("#chart02").hide();
	$("#chart01").show();
	
	var pieArr = new Array();
	var pieArr2 = new Array();
	
	$.ajax({
		type : "post",
		url : getContextPath()+"/fieldStatus/fieldStatusChart.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			
			var returnData = $.parseJSON(data);
			//console.log(returnData);
			// 파이차트 실 데이터 
			for (var i = 0; i < returnData.length; i++) {										
				var obj = new Object();
				obj.name = returnData[i].NAME;
				obj.y = Number(returnData[i].COUNT);		
				obj.code = returnData[i].CODE;	
				pieArr.push(obj);	
				
			}
			// 파이차트
			$('.chart_w02').highcharts({
			    chart: {
			        plotBackgroundColor: null,
			        plotBorderWidth: null,
			        plotShadow: false,
			        type: 'pie'
			    },
			    title: {
			        text: '소분류 비율'
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
			            },
			            showInLegend: true
			        }
			    },
			    series: [{
			    	name:'소분류 비율', 
			    	events: {
			    		click: function(event) {
			    			$(".rank_keyword").show();
			    			// 클릭시 키워드 출력				    			
			    			displayKeyword(event.point.code);
			    			// 클릭시 해당 분야 결과 출력
			    			$('input[name=searchType]').val("rep");	
			    			$('input[name=keyword]').val(event.point.code);	
			    			fnsearchTotalList();
			    		}
			         },
			    	data: pieArr
			    	
			    }]
			});
		}
	});
}
//페이지 이동
function pageNavi(pageNo){
	
	$('#searchForm input[name=currentPage]').val(pageNo);
	fnsearchTotalList();
	
}
/**
* 검색결과를 도출하는 기능을 수행
* @param val
*/
function fnsearchTotalList(){
	$.ajax({
		type : "post",
		url : getContextPath()+"/fieldStatus/vocSearchResult.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			console.log(data);
			$('#vocSearchTeam').remove();
			$('#search_result').html(data);
			$('#displayKeyword').hide();
		},
		error : function(result) {	 
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}
