$(function() {
	$(".date_time").datepicker({
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		weekHeader: 'Wk',
		//dateFormat: 'yy년 mm월 dd일', //형식(2012-03-03)
		dateFormat: 'yy/mm/dd', //형식(2012-03-03)
		autoSize: false, //오토리사이즈(body등 상위태그의 설정에 따른다)
		changeMonth: true, //월변경가능
		changeYear: true, //년변경가능
		showMonthAfterYear: true, //년 뒤에 월 표시
		buttonImageOnly: true, //이미지표시
		buttonImage: '../resources/images/common/btn_calendar.gif', //이미지주소
		buttonText: '달력',
		showOn: "both", //엘리먼트와 이미지 동시 사용
		yearRange: '2005:2020' //2005년부터 2020년까지
	});
	var testStartDate = "2017/08/07";
	var testEndDate = "2017/08/13";
	isTest = false;
	
	if(($('#startDate').length && $('#endDate').length) && ($('#startDate').val().length<=0 && $('#endDate').val().length<=0)){
		var sdate = new Date();
		var edate = new Date();
		if(isTest){
			sdate = new Date(testStartDate);
			edate = new Date(testEndDate);	
		}
		
		var check = $('#calendar_select option:selected').val();
		
		if("DAY" == check){
			sdate.setDate(edate.getDate()-6);
		}else if("WEEK" == check){
			sdate.setMonth(edate.getMonth()-2);
		}else if("MONTH" == check){
			sdate.setMonth(edate.getMonth()-12);
		}else if("QUARTER" == check){
			sdate.setMonth(edate.getMonth()-24);
		}else if("HALF" == check){
			sdate.setMonth(edate.getMonth()-48);
		}else if("YEAR" == check){
			sdate.setMonth(edate.getMonth()-96);
		}else{								//조회구분을 삭제한 페이지의 디폴트 분석기간이 일주일이 될 수 있도록 설정 
			sdate.setDate(edate.getDate()-6);
			
		}
		
		$('#startDate').datepicker('setDate', sdate);
		$('#endDate').datepicker('setDate', edate);
	}
	
	// 시작일 종료일 설정
	$('#startDate').datepicker();
	$('#startDate').datepicker("option", "maxDate", $("#endDate").val());
	$('#startDate').datepicker("option", "onClose", function ( selectedDate ) {
		
		var check = $('#calendar_select option:selected').val();
		var stxt = selectedDate.split("/");
		
		
		stxt[1] = stxt[1] - 1;
		
		var sdate = new Date(stxt[0], stxt[1], stxt[2]);
		var mdate = new Date(stxt[0], stxt[1], stxt[2]);
		
		if("DAY" == check){
			mdate.setDate(sdate.getDate()+6);
		}else if("WEEK" == check){
			mdate.setMonth(sdate.getMonth()+3);
		}else if("MONTH" == check){
			mdate.setMonth(sdate.getMonth()+12);
		}else if("QUARTER" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("HALF" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("YEAR" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else{								 // 조회구분을 삭제한 페이지의 달력에서 특정 날짜 클릭 시 특정 날짜로부터 1년 뒤날짜가 종료 날짜로 설절될 수 있도록 설정
			/*mdate.setMonth(sdate.getMonth()+12);*/
		}
		
		$("#endDate").datepicker( "option", "minDate", sdate );
		$("#endDate").datepicker( "option", "maxDate", mdate ); //분석기간의 시작일을 누를 때 종료일이 변경되는 것을 막기 위해 주석 처리함
		$('#endDate').datepicker('setDate', mdate);
	});
 
	$('#endDate').datepicker();
	$('#endDate').datepicker("option", "minDate", $("#startDate").val());
	// 일별 : 최근 1주일, 주별 : 최근3개월, 월별 : 최근1년, 분기별 : 최근3년, 반기별 : 최근3년, 년별 : 최근3년
	$('#endDate').datepicker("option", "beforeShow", function () {
		
		var stxt = $("#startDate").val().split("/");
		stxt[1] = stxt[1] - 1;
		var sdate = new Date(stxt[0], stxt[1], stxt[2]);
		var mdate = new Date(stxt[0], stxt[1], stxt[2]);
		
		var check = $('#calendar_select option:selected').val();
		
		if("DAY" == check){
			mdate.setDate(sdate.getDate()+6);
		}else if("WEEK" == check){
			mdate.setMonth(sdate.getMonth()+3);
		}else if("MONTH" == check){
			mdate.setMonth(sdate.getMonth()+12);
		}else if("QUARTER" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("HALF" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("YEAR" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else{		// 조회구분을 삭제한 페이지의 달력에서 특정날짜를 선택 시 종료일을 출력하는 달력에 36개월 치 날짜를 선택할 수 있도록 설정
			mdate.setMonth(sdate.getMonth()+36);
		}
		
		$("#endDate").datepicker( "option", "maxDate", mdate );
		
	});
	
	$('#endDate').datepicker("option", "onClose", function ( selectedDate ) {
		$("#startDate").datepicker( "option", "maxDate", selectedDate );
	});
	
	// 조회구분 변경
	$('#calendar_select').change(function() {
		var check = $('#calendar_select option:selected').val();
		var sdate = new Date();
		var edate = new Date();
		if(isTest){
			sdate = new Date(testStartDate);
			edate = new Date(testEndDate);
		}
		if("DAY" == check){
			$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
			sdate.setDate(edate.getDate()-6);
		}else if("WEEK" == check){
			$('#selectText').html('<font color="red">\'주별\'</font>은 시작일부터 3개월 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-3);
		}else if("MONTH" == check){
			$('#selectText').html('<font color="red">\'월별\'</font>은 시작일부터 1년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-12);
		}else if("QUARTER" == check){
			$('#selectText').html('<font color="red">\'분기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}else if("HALF" == check){
			$('#selectText').html('<font color="red">\'반기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}else if("YEAR" == check){
			$('#selectText').html('<font color="red">\'년별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}
		
		$('#startDate').datepicker("option", "maxDate", edate);
		$('#startDate').datepicker('setDate', sdate);
		
		$("#endDate").datepicker( "option", "minDate", sdate );
		$("#endDate").datepicker( "option", "maxDate", edate );
		$('#endDate').datepicker('setDate', edate);
		
	});
	
	// 비교분석 화면 A
	if(($('#startDateA').length && $('#endDateA').length) && ($('#startDateA').val().length<=0 && $('#endDateA').val().length<=0)){
		var sdate = new Date();
		var edate = new Date();
		if(isTest){
			sdate = new Date(testStartDate);
			edate = new Date(testEndDate);
		}
		var check = $('#calendar_selectA option:selected').val();
		
		if("DAY" == check){
			sdate.setDate(edate.getDate()-6);
		}else if("WEEK" == check){
			sdate.setMonth(edate.getMonth()-2);
		}else if("MONTH" == check){
			sdate.setMonth(edate.getMonth()-12);
		}else if("QUARTER" == check){
			sdate.setMonth(edate.getMonth()-24);
		}else if("HALF" == check){
			sdate.setMonth(edate.getMonth()-48);
		}else if("YEAR" == check){
			sdate.setMonth(edate.getMonth()-96);
		}
		
		$('#startDateA').datepicker('setDate', sdate);
		$('#endDateA').datepicker('setDate', edate);
	}
	
	// 시작일 종료일 설정
	$('#startDateA').datepicker();
	$('#startDateA').datepicker("option", "maxDate", $("#endDateA").val());
	$('#startDateA').datepicker("option", "onClose", function ( selectedDate ) {
		
		var check = $('#calendar_selectA option:selected').val();
		var stxt = selectedDate.split("/");
		stxt[1] = stxt[1] - 1;
		var sdate = new Date(stxt[0], stxt[1], stxt[2]);
		var mdate = new Date(stxt[0], stxt[1], stxt[2]);
		
		if("DAY" == check){
			mdate.setDate(sdate.getDate()+6);
		}else if("WEEK" == check){
			mdate.setMonth(sdate.getMonth()+3);
		}else if("MONTH" == check){
			mdate.setMonth(sdate.getMonth()+12);
		}else if("QUARTER" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("HALF" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("YEAR" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}
		
		$("#endDateA").datepicker( "option", "minDate", sdate );
		$("#endDateA").datepicker( "option", "maxDate", mdate );
		$('#endDateA').datepicker('setDate', mdate);
	});
 
	$('#endDateA').datepicker();
	$('#endDateA').datepicker("option", "minDate", $("#startDateA").val());
	// 일별 : 최근 1주일, 주별 : 최근3개월, 월별 : 최근1년, 분기별 : 최근3년, 반기별 : 최근3년, 년별 : 최근3년
	$('#endDateA').datepicker("option", "beforeShow", function () {
		
		var stxt = $("#startDateA").val().split("/");
		stxt[1] = stxt[1] - 1;
		var sdate = new Date(stxt[0], stxt[1], stxt[2]);
		var mdate = new Date(stxt[0], stxt[1], stxt[2]);
		
		var check = $('#calendar_selectA option:selected').val();
		
		if("DAY" == check){
			mdate.setDate(sdate.getDate()+6);
		}else if("WEEK" == check){
			mdate.setMonth(sdate.getMonth()+3);
		}else if("MONTH" == check){
			mdate.setMonth(sdate.getMonth()+12);
		}else if("QUARTER" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("HALF" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("YEAR" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}
		
		$("#endDateA").datepicker( "option", "maxDate", mdate );
		
	});
	
	$('#endDateA').datepicker("option", "onClose", function ( selectedDate ) {
		$("#startDateA").datepicker( "option", "maxDate", selectedDate );
	});
	
	var sdateA = new Date();
	var edateA = new Date();
	if(isTest){
		sdateA = new Date(testStartDate);
		edateA = new Date(testEndDate);
	}
	var checkA = $('#calendar_selectA option:selected').val();
	
	if("DAY" == checkA){
		sdateA.setDate(edateA.getDate()-6);
	}else if("WEEK" == checkA){
		sdateA.setMonth(edateA.getMonth()-2);
	}else if("MONTH" == checkA){
		sdateA.setMonth(edateA.getMonth()-12);
	}else if("QUARTER" == checkA){
		sdateA.setMonth(edateA.getMonth()-24);
	}else if("HALF" == checkA){
		sdateA.setMonth(edateA.getMonth()-48);
	}else if("YEAR" == checkA){
		sdateA.setMonth(edateA.getMonth()-96);
	}
	
	$('#startDateA').datepicker('setDate', sdateA);
	$('#endDateA').datepicker('setDate', edateA);
	
	// 비교분석 화면 B
	if(($('#startDateB').length && $('#endDateB').length) && ($('#startDateB').val().length<=0 && $('#endDateB').val().length<=0)){
		var sdate = new Date();
		var edate = new Date();
		if(isTest){
			sdate = new Date(testStartDate);
			edate = new Date(testEndDate);
		}
		var check = $('#calendar_selectB option:selected').val();
		
		if("DAY" == check){
			sdate.setDate(edate.getDate()-6);
		}else if("WEEK" == check){
			sdate.setMonth(edate.getMonth()-2);
		}else if("MONTH" == check){
			sdate.setMonth(edate.getMonth()-12);
		}else if("QUARTER" == check){
			sdate.setMonth(edate.getMonth()-24);
		}else if("HALF" == check){
			sdate.setMonth(edate.getMonth()-48);
		}else if("YEAR" == check){
			sdate.setMonth(edate.getMonth()-96);
		}
		
		$('#startDateB').datepicker('setDate', sdate);
		$('#endDateB').datepicker('setDate', edate);
	}
	
	// 시작일 종료일 설정
	$('#startDateB').datepicker();
	$('#startDateB').datepicker("option", "maxDate", $("#endDateB").val());
	$('#startDateB').datepicker("option", "onClose", function ( selectedDate ) {
		
		var check = $('#calendar_selectB option:selected').val();
		var stxt = selectedDate.split("/");
		stxt[1] = stxt[1] - 1;
		var sdate = new Date(stxt[0], stxt[1], stxt[2]);
		var mdate = new Date(stxt[0], stxt[1], stxt[2]);
		
		if("DAY" == check){
			mdate.setDate(sdate.getDate()+6);
		}else if("WEEK" == check){
			mdate.setMonth(sdate.getMonth()+3);
		}else if("MONTH" == check){
			mdate.setMonth(sdate.getMonth()+12);
		}else if("QUARTER" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("HALF" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("YEAR" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}
		
		$("#endDateB").datepicker( "option", "minDate", sdate );
		$("#endDateB").datepicker( "option", "maxDate", mdate );
		$('#endDateB').datepicker('setDate', mdate);
	});
 
	$('#endDateB').datepicker();
	$('#endDateB').datepicker("option", "minDate", $("#startDateB").val());
	// 일별 : 최근 1주일, 주별 : 최근3개월, 월별 : 최근1년, 분기별 : 최근3년, 반기별 : 최근3년, 년별 : 최근3년
	$('#endDateB').datepicker("option", "beforeShow", function () {
		
		var stxt = $("#startDateB").val().split("/");
		stxt[1] = stxt[1] - 1;
		var sdate = new Date(stxt[0], stxt[1], stxt[2]);
		var mdate = new Date(stxt[0], stxt[1], stxt[2]);
		
		var check = $('#calendar_selectB option:selected').val();
		
		if("DAY" == check){
			mdate.setDate(sdate.getDate()+6);
		}else if("WEEK" == check){
			mdate.setMonth(sdate.getMonth()+3);
		}else if("MONTH" == check){
			mdate.setMonth(sdate.getMonth()+12);
		}else if("QUARTER" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("HALF" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}else if("YEAR" == check){
			mdate.setMonth(sdate.getMonth()+36);
		}
		
		$("#endDateB").datepicker( "option", "maxDate", mdate );
		
	});
	
	$('#endDateB').datepicker("option", "onClose", function ( selectedDate ) {
		$("#startDateB").datepicker( "option", "maxDate", selectedDate );
	});
	
	var sdateA = new Date();
	var edateA = new Date();
	if(isTest){
		sdateA = new Date(testStartDate);
		edateA = new Date(testEndDate);
	}
	var checkA = $('#calendar_selectB option:selected').val();
	
	if("DAY" == checkA){
		sdateA.setDate(edateA.getDate()-6);
	}else if("WEEK" == checkA){
		sdateA.setMonth(edateA.getMonth()-2);
	}else if("MONTH" == checkA){
		sdateA.setMonth(edateA.getMonth()-12);
	}else if("QUARTER" == checkA){
		sdateA.setMonth(edateA.getMonth()-24);
	}else if("HALF" == checkA){
		sdateA.setMonth(edateA.getMonth()-48);
	}else if("YEAR" == checkA){
		sdateA.setMonth(edateA.getMonth()-96);
	}
	
	$('#startDateB').datepicker('setDate', sdateA);
	$('#endDateB').datepicker('setDate', edateA);
	
	// 조회조건 선택시 기간 변경 비교분석 화면 A
	// 조회구분 변경
	$('#calendar_selectA').change(function() {
		var check = $('#calendar_selectA option:selected').val();
		var sdate = new Date();
		var edate = new Date();
		if(isTest){
			sdate = new Date(testStartDate);
			edate = new Date(testEndDate);
		}
		if("DAY" == check){
			$('#selectTextA').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
			sdate.setDate(edate.getDate()-6);
		}else if("WEEK" == check){
			$('#selectTextA').html('<font color="red">\'주별\'</font>은 시작일부터 3개월 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-3);
		}else if("MONTH" == check){
			$('#selectTextA').html('<font color="red">\'월별\'</font>은 시작일부터 1년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-12);
		}else if("QUARTER" == check){
			$('#selectTextA').html('<font color="red">\'분기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}else if("HALF" == check){
			$('#selectTextA').html('<font color="red">\'반기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}else if("YEAR" == check){
			$('#selectTextA').html('<font color="red">\'년별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}
		
		$('#startDateA').datepicker("option", "maxDate", edate);
		$('#startDateA').datepicker('setDate', sdate);
		
		$("#endDateA").datepicker( "option", "minDate", sdate );
		$("#endDateA").datepicker( "option", "maxDate", edate );
		$('#endDateA').datepicker('setDate', edate);
	});
	
	// 조회조건 선택시 기간 변경 비교분석 화면 B
	// 조회구분 변경
	$('#calendar_selectB').change(function() {
		var check = $('#calendar_selectB option:selected').val();
		var sdate = new Date();
		var edate = new Date();
		if(isTest){
			sdate = new Date(testStartDate);
			edate = new Date(testEndDate);
		}
		if("DAY" == check){
			$('#selectTextB').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
			sdate.setDate(edate.getDate()-6);
		}else if("WEEK" == check){
			$('#selectTextB').html('<font color="red">\'주별\'</font>은 시작일부터 3개월 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-3);
		}else if("MONTH" == check){
			$('#selectTextB').html('<font color="red">\'월별\'</font>은 시작일부터 1년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-12);
		}else if("QUARTER" == check){
			$('#selectTextB').html('<font color="red">\'분기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}else if("HALF" == check){
			$('#selectTextB').html('<font color="red">\'반기별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}else if("YEAR" == check){
			$('#selectTextB').html('<font color="red">\'년별\'</font>은 시작일부터 3년 설정만 가능합니다.');
			sdate.setMonth(edate.getMonth()-36);
		}
		
		$('#startDateB').datepicker("option", "maxDate", edate);
		$('#startDateB').datepicker('setDate', sdate);
		
		$("#endDateB").datepicker( "option", "minDate", sdate );
		$("#endDateB").datepicker( "option", "maxDate", edate );
		$('#endDateB').datepicker('setDate', edate);
	});
});