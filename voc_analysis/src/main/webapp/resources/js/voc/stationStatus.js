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
	
	displayReport();
	
	$("#calendar_select").removeAttr('selected')
	$("#calendar_select option[value=\""+condition+"\"]").prop("selected", true);
	
}
$(function(){
	
	$(document).on("click",".line_half a",function(){  	
		
		$('#rank_name').html($(this).text()+" 상세보기");
		$('.rank_top').html($(this).text()+" 키워드 Top 10");
		// 키워드 출력
		var statusHtml = "";
		$('input[name=stationName]').val($(this).text());
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/stationStatus/stationKeyword.do",
			data : $("#searchForm").serialize(),
			success : function(data) {				
				var returnData = $.parseJSON(data);
				statusHtml += "<ul>";
				for (var i = 0; i < returnData.length; i++) {
					statusHtml += "<li><span class=\"rank_no\">"+Number(i+1)+"</span>";
					statusHtml += "<a href=\"#\" class=\"rank_key\">"+returnData[i].NAME+"</a>";
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
	
	$(document).on("click","#statusArea .modal_btn",function(){  		
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
	// 검색클릭 이벤트
	$(document).on("click","#searchStart",function(){  		
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList option:selected').val());
		$('input[name=metroDept]').val($('#vocMetroDeptList option:selected').val());
		
		$('input[name=line]').val('all');
		$("#select_line option[value=\"all\"]").prop("selected", true);
		
		$('input[name=stationCurrentPageNo]').val('1');
		$('input[name=stationEndPage]').val('12');
		$('input[name=stationTotalSize]').val('12');
		$('input[name=stationPageSize]').val('12');
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		
		displayReport();
		
	});			
	// 역명 클릭시  
	/*$(document).on("click","#statusArea",function(){  		
		goModal();
	});*/
	// 노선 change 이벤트 
	$(document).on("change","#select_line",function(){  	
		if($("#select_line option:selected").val() == 'all')
			$('input[name=line]').val('all');
		else if($("#select_line option:selected").val() == '18')
			$('input[name=line]').val($("#select_line option:selected").val());
		else
			$('input[name=line]').val('0'+$("#select_line option:selected").val());
		$('input[name=stationCurrentPageNo]').val('1');
		$('input[name=stationEndPage]').val('12');
		$('input[name=stationTotalSize]').val('12');
		$('input[name=stationPageSize]').val('12');
		displayReport();
	});	

	function goModal(){
		$("#stationModal").show();
	}
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/stationStatus/getAlikeSearch.do #alike",
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
		
		$('input[name=condition]').val($('#calendar_select option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList option:selected').val());
		$('input[name=metroDept]').val($('#vocMetroDeptList option:selected').val());
		
		$('input[name=line]').val('all');
		
		$("#searchForm").attr('action', getContextPath() + "/stationStatus/excelVocSearchResult.do").submit();
	});
	//상세보기
	$(document).on('click', '.result_detail', function(){
		
		var id =  $.trim($(this).attr("name"));
		
		$("#basic-modal-detail").load(getContextPath() + "/stationStatus/detailView.do #detail",
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
	
	//키워드 클릭시
	$(document).on('click', '.rank_key', function(){

		$('#searchForm input[name=keyword]').val($.trim($(this).text()));		
		$('input[name=searchType]').val('keyword');
		
		fnsearchTotalList();
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
		//$(".btn_xls").remove();
		$('#calendar_select').change();
	});
});
//페이지 이동
function pageNavi(pageNo, element){
	
	divElement = element.parentNode.parentNode.parentNode;
	if(divElement.getAttribute('id') == 'statusPaging'){
		
		$('#searchForm input[name=stationCurrentPageNo]').val(pageNo);	
		displayReport();
	}
	else{
		$('#searchForm input[name=currentPage]').val(pageNo);
		fnsearchTotalList();
	}
};

/**
* 검색결과를 도출하는 기능을 수행
* @param val
*/
function fnsearchTotalListFirst(){
	$.ajax({
		type : "post",
		url : getContextPath()+"/stationStatus/vocSearchResult.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
		
			$('#vocSearchTeam').remove();
			$('#search_result').html(data);
			$('#displayKeyword').hide();
		},
		error : function(result) {	 
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}
//VOC검색결과 리스트 생성(그룹_리스트+페이징)
function fnsearchTotalList(){
	
	$.ajax({
		type : "post",
		url : getContextPath()+"/stationStatus/vocSearchResult.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			$('#search_result').html(data);
			$('#displayKeyword').show();
		},
		error : function(result) {	 
			alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		}
	});
}

function displayReport(){
	$('#statusArea').html("");
	$('#statusCont').show();
	$('#search_result').show();
	var totalSize = "";
	
	$.ajax({
		type : "post",
		async : false,
		url : getContextPath()+"/stationStatus/stationChart.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			var allData = $.parseJSON(data);
			var statusHtml = "";
			//console.log(data);
			var returnData = allData.stationChart;
			var deptData = allData.metroDept;
			var keyword = "";
			//console.log(returnData);
			
			// 페이징, 역별 현황 
			endPage = returnData[0].COUNT;
			totalSize = returnData[1].COUNT;
			$('input[name=stationEndPage]').val(endPage);
			$('input[name=stationTotalSize]').val(totalSize);
			for (var i = 2; i < returnData.length; i++) {
				
				keyword = returnData[0].KEYWORD
				
				statusHtml += "<div class=\"line_half mr_10\">";
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
				statusHtml += "<span class=\"orange_bg \">"+returnData[i].NEGATIVE+"개</span></p></div>";
				
				
			}
			$('#statusArea').html(statusHtml);
			//console.log(deptData);
			// 처리주무부서 리스트 
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

			$('input[name=searchType]').val('all');
			fnsearchTotalListFirst();
		}
	});
	
	getPage();
	
}

function getPage(){
	
	$.ajax({
		type : "post",
		async : false,
		url : getContextPath()+"/stationStatus/paging.do",
		data : $("#searchForm").serialize(),
		success : function(data) {
			$('#statusPaging').html(data);		
		}
	});
}