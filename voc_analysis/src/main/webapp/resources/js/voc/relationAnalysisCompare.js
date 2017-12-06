$(function(){
	
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
		if(!validate('A')){
			return false;
		}
		
		inputVal('A');
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		displayRelation('A');	//연관도 차트 출력
		displayLine('A');	//트렌드 차트 출력
		fnsearchTotalList('A');	//검색결과 리스트 출력
		selectDeptList('A');	//처리주무부서 셀렉트 박스 변경
	});	
	
	// 검색클릭 B 이벤트
	$(document).on("click","#searchStartB",function(){  		
		if(!validate('B')){
			return false;
		}
		
		inputVal('B');
		
		//////// 각 페이지에 선언된 결과를 ajax로 불러오도록 함
		displayRelation('B');	//연관도 차트 출력
		displayLine('B');	//트렌드 차트 출력
		fnsearchTotalList('B');	//검색결과 리스트 출력
		selectDeptList('B');	//처리주무부서 셀렉트 박스 변경
	});	
	
	// 초기화 >> A
	$('#searchResetA').click(function() {
		$("#startDateA").val('');
		$("#endDateA").val('');
		$('#calendar_selectA').find('option:first').attr('selected',true);
		$('#calendar_selectA').change();
		
		$('#keywordListA option:eq(0)').attr('selected','selected');
		$('#keywordTempA').val('');
		$('#keywordTempA').attr("disabled", false);
		$('#exclusionA').val('');
		
		/////// 비교분석A
		$('#metroDeptListA').html("<option value=\"all\">전체</option>");
		$('#repLevelListA').find('option:first').attr('selected',true);
		$('#vocChannelListA').find('option:first').attr('selected',true);
		$('#vocRecTypeListA').find('option:first').attr('selected',true);
		$('#vocKindListA').find('option:first').attr('selected',true);
		$('#vocPartListA').find('option:first').attr('selected',true);
		$('#vocItemListA').find('option:first').attr('selected',true);
		
		// 초기화면 - 대분류 / 중분류 / 소분류 비활성 
		$("#vocKindListA").attr("disabled", true);
		$("#vocPartListA").attr("disabled", true);
		$("#vocItemListA").attr("disabled", true);
	});
	
	// 초기화 >> B
	$('#searchResetB').click(function() {
		$("#startDateB").val('');
		$("#endDateB").val('');
		$('#calendar_selectB').find('option:first').attr('selected',true);
		$('#calendar_selectB').change();
		
		$('#keywordListB option:eq(0)').attr('selected','selected');
		$('#keywordTempB').val('');
		$('#keywordTempB').attr("disabled", false);
		$('#exclusionB').val('');
		
		/////// 비교분석B
		$('#metroDeptListA').html("<option value=\"all\">전체</option>");
		$('#repLevelListA').find('option:first').attr('selected',true);
		$('#vocChannelListA').find('option:first').attr('selected',true);
		$('#vocRecTypeListA').find('option:first').attr('selected',true);
		$('#vocKindListA').find('option:first').attr('selected',true);
		$('#vocPartListA').find('option:first').attr('selected',true);
		$('#vocItemListA').find('option:first').attr('selected',true);
		
		// 초기화면 - 대분류 / 중분류 / 소분류 비활성 
		$("#vocKindListB").attr("disabled", true);
		$("#vocPartListB").attr("disabled", true);
		$("#vocItemListB").attr("disabled", true);
	});
	
	//카테고리 클릭시
	$(document).on('click', '#tab_01A, #tab_01B', function(i){
		var type ='A';
		if($(this).attr("id")=='tab_01B'){
			type ='B';
		}
		// tab에 선택 클리어
		$('#searchCategory'+type+' li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		$('#searchForm input[name=needsType]').val($(this).attr("name"));
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize"+type+"]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList(type);
	});
	
	//페이지 문서수 변경
	$(document).on('change','#pageSizeA, #pageSizeB', function(){
		var type = "A";
		if($(this).attr("id")=='pageSizeB'){
			type = "B";
		}
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize"+type+"]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList(type);
	});
	
	//키워드 선택시
	$('#keywordListA, #keywordListB').change(function() {
		var id = $(this).attr("id");
		var temp = $('#'+id+' option:selected').text();
		if(id=='keywordListA'){
			if(temp=='직접입력'){
				$('#keywordTempA').val('');
				$('#keywordTempA').attr("disabled", false);
			}else{
				$('#keywordTempA').attr("disabled", true);
				$('#keywordTempA').val(temp);
			}
		}else{
			if(temp=='직접입력'){
				$('#keywordTempB').val('');
				$('#keywordTempB').attr("disabled", false);
			}else{
				$('#keywordTempB').attr("disabled", true);
				$('#keywordTempB').val(temp);
			}
		}
	});
	
	
});
// end of $(function(){ line 1
	//연관도 차트
	function displayRelation(div){
		$.ajax({//d3 차트 - 연관도 분석
			type : "post",
			url : getContextPath()+"/relationAnalysis/compareRadarChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				if(data.length > 4){
					var returnData = $.parseJSON(data);
					var path = "d3Chart" + div;
					var id = "chart" + div;
					$('#d3Chart' + div).html('');
					d3Chart(returnData, path, id);
				}else{
					$('#d3Chart' + div).html('');
					$('#d3Chart' + div).html('차트결과가 없습니다.');
				}
			},
			error : function(result) {     
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	//연관어 트렌드 차트
	function displayLine(div){
		$.ajax({//하이차트 - 연관키워드 트렌드 분석
			type : "post",
			url : getContextPath()+"/relationAnalysis/compareKeywordTrend.do",
			async : false,
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData.keywordPeriodCountList != undefined){
					//$('#reportChart').addClass('p_20');	
					$('#reportChart'+div).highcharts({
						chart: {
							height: '340px'
						},
						title: {
							text: '',
							x: -20 //center
						},
						credits:{
							enabled:false
						},
						xAxis: {
							categories: returnData.periodList
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
						series: returnData.keywordPeriodCountList
					});
				}else{
					//$('#reportChart').removeClass('p_20');	
					$('#reportChart'+div).empty();
					$('#reportChart'+div).html('차트 결과가 없습니다.');
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	function selectDeptList(div){
		//처리주무부서 리스트 조회
		$.ajax({
			type : "POST",
			url : getContextPath() + "/relationAnalysis/repDeptListCompare.do",
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
					$('#metroDeptList' + div).html(selectHtml);
					//$('#searchForm input[name=keyword]').val(keyword);
					//fnsearchTotalListFirst(keyword);	
				}
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	/**
	* 검색결과를 도출하는 기능을 수행
	* @param val
	*/
	function fnsearchTotalList(div){
		$.ajax({
			type : "post",
			url : getContextPath()+"/relationAnalysis/vocSearchResultCompare.do",
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

	//searchForm 적용
	function inputVal(div){
		$('input[name=condition]').val($('#calendar_select'+div+' option:selected').val());
		$('input[name=vocChannel]').val($('#vocChannelList'+div+' option:selected').val());
		
		$('input[name=vocRecType]').val($('#vocRecTypeList'+div+' option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList'+div+' option:selected').val());
		
		$('input[name=repLevel]').val($('#repLevelList'+div+' option:selected').val());
		$('input[name=vocKind]').val($('#vocKindList'+div+' option:selected').val());
		$('input[name=vocPart]').val($('#vocPartList'+div+' option:selected').val());
		$('input[name=vocItem]').val($('#vocItemList'+div+' option:selected').val());
		$('input[name=metroDept]').val($('#metroDeptList'+div+' option:selected').val());
		
		$('input[name=keyword]').val($('input[name=keywordTemp'+div+']').val());
		$('input[name=exclusion]').val($('input[name=exclusion'+div+']').val());
		$('input[name=startDate]').val($('input[name=startDate'+div+']').val());
		$('input[name=endDate]').val($('input[name=endDate'+div+']').val());
	
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

	//상세보기
	function detailView(id){
		$("#basic-modal-detail").load(getContextPath()+"/relationAnalysis/detailView.do #detail",
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
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath()+"/relationAnalysis/getAlikeSearchCompare.do #alike",
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
	

	function validate(div){

		if($('input[name="startDate' + div + '"]').val().length <= 0){
			alert("시작일을 설정해 주세요.");
			return false;
		}
		
		if($('input[name="endDate' + div + '"]').val().length <= 0){
			alert("종료일을 설정해 주세요.");
			return false;
		}
		
		if($('input[name="keywordTemp' + div + '"]').val().length <= 0){
			alert('키워드를 입력하세요');
			return false;
		}
		
		if(!$('input[name="exclusion' + div + '"]').val() == ''){
			var exceptKeyword = $('input[name="exclusion' + div + '"]').val();
			var exceptKeywordArr = exceptKeyword.split(",");
			if(exceptKeywordArr.length > 5){
				alert("제외키워드는 5개까지만 입력가능합니다.");
				return false;
			}
		}
		return true;
	}
	
	/* 연관도 차트 - D3 시작 */
	function d3Chart(data, path, id){
		d3.select(id).remove();
		var chartWidth = 441;//표 크기
		var chartHeight = 400;//표 크기
		var node, link, root, nodes, links, overLinks;
		
		var force = d3.layout.force()
		.on("tick", tick)
		/*.gravity(.4)
		.friction(0.9)
		.charge(-1000)
		.linkDistance(50)*/
		.gravity(0.5) // 원들 가운데로 뭉침
		.friction(0.9)
		.charge(-1000) // 원들 뭉쳐짐
		.linkDistance(50) // 원들 사이의 거리
		.size([chartWidth, chartHeight]);
	
		var zoom = d3.behavior.zoom()
		.center([chartWidth / 2, chartHeight / 2])
		.scaleExtent([1, 8])
		.on("zoom", zoomed);
					
		var zoomScale = 1;
	
		var svg = d3.select("#"+path).append("svg")
		.attr("id", id)
		.attr("width", chartWidth)
		.attr("height", chartHeight)
		.attr("overflow", "hidden")
		.append("g")
		.call(zoom)
		.append("g");
	  
		svg.append("rect")
		.attr("class", "overlay")
		.attr("width", chartWidth)
		.attr("height", chartHeight);
		
		
		var pos_x = [0  	,0 		, 45 	, 45	, -45 	, -45	, 50	, -50];
		var pos_y = [80 	,-80 	, 60 	, -60 	, 60 	, -60	, 0	, -0];
		
		var pos = 0;
		root = data;
		root.fixed = true;
		root.x = chartWidth / 2;
		root.y = chartHeight / 2;
		init();
		
		function init() {
			nodes = flatten(root);
			if(nodes != null) {
				for(var i = 0; i < nodes.length; i++) {
					/* if(nodes[i].depth == 2) {	//pos_x, pos_y 값으로 10개 노드 위치 강제 지정.
						nodes[i].x = root.x + pos_x[pos];
						nodes[i].y = root.y + pos_y[pos];
						nodes[i].fixed = true;
						pos++;
					} */
					if(nodes[i].depth == 4) {
						if(nodes[i].children) {
							nodes[i]._children = nodes[i].children;
							nodes[i].children = null;
						}
					}
				}
			}
			update();
		}
	
		function update() {
			nodes = flatten(root),
			links = d3.layout.tree().links(nodes);
			
			force
			.nodes(nodes)
			.links(links)
			.start();
		
			link = svg.selectAll("line")
			.data(links, function(d) { return d.target.id; });
			
			link.enter().insert("line", ".node")
			.attr("id", function(d) { return "link_" + d.source.id + "_" + d.target.id; })
			.attr("x1", function(d) { return d.source.x; })
			.attr("y1", function(d) { return d.source.y; })
			.attr("x2", function(d) { return d.target.x; })
			.attr("y2", function(d) { return d.target.y; });
			
			if(nodes.length < 10) {
				force.gravity(.0);	
			} else if(nodes.length >= 10 && nodes.length < 20) {
				force.gravity(.1);
			}
			
			link.exit().remove();
		  
			svg.selectAll("g.node").remove();
		  
			node = svg.selectAll("g.node")
			.data(nodes, function(d) {
				return d.id; 
			});
		
			node.enter().append("g")
			.attr("class", "node")
			.on("click", click)	  
			//.on("mouseover", mouseOverData)	//d3차트가 2개 이상 존재할때 한쪽에만 적용되는 문제 발생.
			//.on("mouseout", mouseOutData)
			//.on("click", nodeMouseClickData)
			.call(force.drag);
		  	
			node.append("rect")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("x", function(d) { 
				var size = -25;
				var nameSize;
				if(d.group.indexOf("depth_etc") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * -7;
				}
				if(nameSize > size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("y", function(d) {
				var size;
				if(d.group.indexOf("depth_etc") > -1) {
					size = -7;
				}
				return size; 
			})
			.attr("width", function(d) {
				var size = 50;
				var nameSize;
				if(d.group.indexOf("depth_etc") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * 14;
				}
				if(nameSize < size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("height", function(d) { 
				var size;
				if(d.group.indexOf("depth_etc") > -1) {
					size = 22;
				}
				return size; 
			})
			.attr("fill", function(d) {
				if(d.group.indexOf("keyword1") > -1) {
					return "#a26eb3";				
				} else if(d.group.indexOf("keyword2") > -1) {
					return "#6ba22c";
				} else if(d.group.indexOf("keyword3") > -1) {
					return "#d55a76";
				} else if(d.group.indexOf("keyword4") > -1) {
					return "#4083c6";
				} else if(d.group.indexOf("keyword5") > -1) {
					return "#ce9514";
				} else if(d.group.indexOf("keyword6") > -1) {
					return "#d66f0d";
				} else if(d.group.indexOf("keyword7") > -1) {
					return "#867bd4";
				} else if(d.group.indexOf("keyword8") > -1) {
					return "#3ba5a5";
				} else if(d.group.indexOf("keyword9") > -1) {
					return "#01DFD7";				
				} else if(d.group.indexOf("center") > -1) {
					return "#000000";	
				}
			})
			.attr('rx', 10)
			.attr('ry', 10);
			
			node.append("circle")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("x", function(d) { 
				var size = -30;
				var nameSize;
				if(d.group.indexOf("depth2") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * -7;
				}
				if(nameSize > size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("y", function(d) {
				var size;
				if(d.group.indexOf("depth2") > -1) {
					size = -30;
				}
				return size; 
			})
			.attr("width", function(d) {
				var size = 60;
				var nameSize;
				if(d.group.indexOf("depth2") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * 14;
				}
				if(nameSize < size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("height", function(d) { 
				var size;
				if(d.group.indexOf("depth2") > -1) {
					size = 60;
				}
				return size; 
			})
			.attr("fill", function(d,i) {
				if(d.group.indexOf("depth2") > -1) {
					return "#ffffff";
				}
					return "#ffffff";
			})
			.attr("style",function(d) {
				if(d.group.indexOf("keyword1") > -1) {
					return "fill : #ffffff; stroke :#a26eb3;";				
				} else if(d.group.indexOf("keyword2") > -1) {
					return "fill : #ffffff; stroke :#6ba22c;";
				} else if(d.group.indexOf("keyword3") > -1) {
					return "fill : #ffffff; stroke :#d55a76;";
				} else if(d.group.indexOf("keyword4") > -1) {
					return "fill : #ffffff; stroke :#4083c6;";
				} else if(d.group.indexOf("keyword5") > -1) {
					return "fill : #ffffff; stroke :#ce9514;";
				} else if(d.group.indexOf("keyword6") > -1) {
					return "fill : #ffffff; stroke :#d66f0d;";
				} else if(d.group.indexOf("keyword7") > -1) {
					return "fill : #ffffff; stroke :#867bd4;";
				} else if(d.group.indexOf("keyword8") > -1) {
					return "fill : #ffffff; stroke :#3ba5a5;";
				} else if(d.group.indexOf("keyword9") > -1) {
					return "fill : #ffffff; stroke :#01DFD7;";				
				} else if(d.group.indexOf("center") > -1) {
					return "fill : #ffffff; stroke :#000000;";	
				}
			})
			.attr("stroke", function(d) {
				if(d.group.indexOf("keyword1") > -1) {
					return "#a26eb3";				
				} else if(d.group.indexOf("keyword2") > -1) {
					return "#6ba22c";
				} else if(d.group.indexOf("keyword3") > -1) {
					return "#d55a76";
				} else if(d.group.indexOf("keyword4") > -1) {
					return "#4083c6";
				} else if(d.group.indexOf("keyword5") > -1) {
					return "#ce9514";
				} else if(d.group.indexOf("keyword6") > -1) {
					return "#d66f0d";
				} else if(d.group.indexOf("keyword7") > -1) {
					return "#867bd4";
				} else if(d.group.indexOf("keyword8") > -1) {
					return "#3ba5a5";
				} else if(d.group.indexOf("keyword9") > -1) {
					return "#01DFD7";				
				} else if(d.group.indexOf("center") > -1) {
					return "#000000";	
				}
			})
			.attr('stroke-width', 3)
			.attr('rx', 10)
			.attr('ry', 10)
			.attr("r", function(d) {
				var size;
				if(d.group.indexOf("depth2") > -1) {
					size = 17;
				}
				if(d.group.indexOf("center") > -1) {
					size = 25;
				}
				return size;
			});
			
			node.append("image")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("group", function(d) {
				return d.id;
			}) 
			.attr("xlink:href", function(d) {
				if(d.group.indexOf("depth_etc") < 0) {
				}
				
			}) 
			.attr("x", function(d) { 
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size = -30;
				} else if(d.group.indexOf("depth2") > -1) {
					size = -15;
				} 
				return size;
			})
			.attr("y", function(d) {
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size = -30;
				} else if(d.group.indexOf("depth2") > -1) {
					size = -15
				} 
				return size; 
			})
			.attr("width", function(d) {
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size=0;
				} else if(d.group.indexOf("depth2") > -1) {
					size=0;
				} 
				return size;
			})
			.attr("height", function(d) { 
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size=0;
				} else if(d.group.indexOf("depth2") > -1) {
					size=0;
				} 
				return size; 
			});
			
			node.append("text")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("class", function(d) {
				if(d.group.indexOf("center") > -1) {
					return "text_center color_center_text";	
				} else if(d.group.indexOf("keyword1") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#a26eb3")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_city";
					}				
				} else if(d.group.indexOf("keyword2") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#6ba22c")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_nature";
					}
				} else if(d.group.indexOf("keyword3") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#d55a76")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_specialty";
					}				
				} else if(d.group.indexOf("keyword4") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#4083c6")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_history";
					}
				} else if(d.group.indexOf("keyword5") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#ce9514")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_cultural";
					}				
				} else if(d.group.indexOf("keyword6") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#d66f0d")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_foaktale";
					}				
				} else if(d.group.indexOf("keyword7") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#867bd4")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_festival";
					}				
				} else if(d.group.indexOf("keyword8") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#3ba5a5")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_infrastructure";
					}				
				} else if(d.group.indexOf("keyword9") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#01DFD7")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_default";
					}				
				}
			})
			.attr("style", "font-size: 13px;")
			.attr("dx", 0)
			.attr("dy", function(d) {
				if(d.group.indexOf("center") > -1) {
					return 5;
				} else if(d.group.indexOf("depth_etc") > -1) {
					return 8;
				} else {
					return 5;
				}
			})
			.text(function(d) {
				if(d.group.indexOf("depth2") > -1) {
					return d.name;
				} else {
					return d.name;
				}
			});
			
			node.append("title").text(function(d) { return d.name; });
			node.exit().remove();
		}
	
		function mouseOverData(d) {
			
			if(d3.selectAll("text#" + d.group.substring(0, d.group.indexOf("_"))).style("opacity") == "1") {
				d3.select(this).select("rect").transition()
				.attr("x", function(d) { 
					var size = -35;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * -8;
					}
					if(nameSize > size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = -7;
					}
					return size; 
				})
				.attr("width", function(d) {
					var size = 70;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * 14 + 20;
					}
					if(nameSize < size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = 30;
					}
					return size; 
				})
				.attr('rx', 10)
				.attr('ry', 10);
				
					  
				d3.select(this).select("text").transition()
				.attr("class", function(d) {
					if(d.group.indexOf("center") > -1) {
						return "text_center color_center_text";
					} else if(d.group.indexOf("keyword1") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#a26eb3")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_city";
						}
					} else if(d.group.indexOf("keyword2") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#6ba22c")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_nature";
						}
					} else if(d.group.indexOf("keyword3") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d55a76")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_specialty";
						}
					} else if(d.group.indexOf("keyword4") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#4083c6")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_history";
						}
					} else if(d.group.indexOf("keyword5") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#ce9514")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_cultural";
						}
					} else if(d.group.indexOf("keyword6") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d66f0d")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_foaktale";
						}
					} else if(d.group.indexOf("keyword7") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#867bd4")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_festival";
						}
					} else if(d.group.indexOf("keyword8") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#3ba5a5")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_infrastructure";
						}
					} else if(d.group.indexOf("keyword9") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#01DFD7")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_default";
						}
					}
				})
				.attr("style", "font-size: 15px;")
				.attr("dx", function(d) {
					if(d.group.indexOf("depth_etc") > -1) {
						return 0;
					} else {
						return 0;
					}
				})
				//마우스 오버시 폰트 위치 변경 - 위/아래
				.attr("dy", function(d) {
					if(d.group.indexOf("center") > -1) {
						return 8;
					} else if(d.group.indexOf("depth_etc") > -1) {
						return 13;
					} else {
						return 8;
					}
				})
				.text(function(d) { 
					if(d.group.indexOf("depth2") > -1) {
						return d.name;
					} else {
						return d.name;
					}
				});
				
				d3.selectAll("g.node").sort(function (a) {
					if(a.id == d.id) {
						return 1;				
					}
				});
			}
		}
	
		function findPath(node) {
			var temp_links = [];
				
			var p_id = node.id;
			var p_depth = node.depth;
			while(p_depth > 1) {
				for(var i = 0; i < links.length; i++) {
					if(links[i].target.id == p_id) {
						temp_links.push(links[i]);
						p_id = links[i].source.id;
						p_depth--;
						break;
					}
				}
			}
			return temp_links;
		}
	
		function mouseOutData() {
			if(d3.selectAll("text#" + d.group.substring(0, d.group.indexOf("_"))).style("opacity") == "1") {
				d3.select(this).select("rect").transition()
				.attr("x", function(d) { 
					var size = -25;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * -7;
					}
					if(nameSize > size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = -7;
					}
					return size; 
				})
				.attr("width", function(d) {
					var size = 50;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * 14;
					}
					if(nameSize < size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = 22;
					}
					return size; 
				});
				
				d3.select(this).select("image").transition()
				.attr("x", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						//size = -30;
						size = -15;
					} 
					return size;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						size = -15;
					} 
					return size; 
				})
				.attr("width", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = 60;
					} else if(d.group.indexOf("depth2") > -1) {
						size = 30;
					} 
					return size;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = 60;
					} else if(d.group.indexOf("depth2") > -1) {
						size = 30;
					} 
					return size; 
				})
				.attr('rx', 10)
				.attr('ry', 10);
				
				
				d3.select(this).select("image").transition()
				.attr("x", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						size = -20;
					} 
					return size;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						size = -20;
					} 
					return size; 
				})
				.attr("width", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						//size = 60;
						size=0;
					} else if(d.group.indexOf("depth2") > -1) {
						//size = 40;
						size=0;
					} 
					return size;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						//size = 60;
						size=0;
					} else if(d.group.indexOf("depth2") > -1) {
						//size = 40;
						size=0;
					} 
					return size; 
				});
				
				
				d3.select(this).select("text").transition()
				.attr("class", function(d) {
					if(d.group.indexOf("center") > -1) {
						return "text_center color_center_text";
						
					} else if(d.group.indexOf("keyword1") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#a26eb3")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_city";
						}
					} else if(d.group.indexOf("keyword2") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#6ba22c")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_nature";
						}
					} else if(d.group.indexOf("keyword3") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d55a76")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_specialty";
						}
					} else if(d.group.indexOf("keyword4") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#4083c6")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_history";
						}
					} else if(d.group.indexOf("keyword5") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#ce9514")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_cultural";
						}
					} else if(d.group.indexOf("keyword6") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d66f0d")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_foaktale";
						}
					} else if(d.group.indexOf("keyword7") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#867bd4")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_festival";
						}
					} else if(d.group.indexOf("keyword8") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#3ba5a5")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_infrastructure";
						}
					} else if(d.group.indexOf("keyword9") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#01DFD7")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_default";
						}
					}
				})
				.attr("style", "font-size: 13px;")
				.attr("dx", 0)
				.attr("dy", function(d) {
					if(d.group.indexOf("center") > -1) {
						return 5;
					} else if(d.group.indexOf("depth_etc") > -1) {
						return 8;
					} else {
						return 5;
					}
				})
				.text(function(d) {
					return d.name;
				});	
			}		
		}
	
		function tick() {
			link
			.attr("x1", function(d) { return d.source.x; })
			.attr("y1", function(d) { return d.source.y; })
			.attr("x2", function(d) { return d.target.x; })
			.attr("y2", function(d) { return d.target.y; });
		
			node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
		}
	
		function click(d) {
			if(d.depth > 2) {
				if(d.children) {
					d._children = d.children;
					d.children = null;
				} else {
					d.children = d._children;
					d._children = null;
				}
				update();
			}
		}
		
		function zoomed() {
			zoomScale = zoom.scale();
			svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
		}
		
		d3.select("#btn_zoomin").on("click", function() {
			zoomScale += 0.2;
			svg.transition()
			.duration(750)
			.call(zoom.center([chartWidth / 2, chartHeight / 2]).scale(zoomScale).event);
		});
	
		d3.select("#btn_zoomout").on("click", function() {	
			if(zoomScale == 1) {
				return;
			}
			zoomScale -= 0.2;
			if(zoomScale < 1) {
				zoomScale = 1;
			}
			svg.transition()
			.duration(750)
			.call(zoom.center([chartWidth / 2, chartHeight / 2]).scale(zoomScale).event);
		});
	
		d3.select("#btn_expand").on("click", function() {
			for(var i = 0; i < nodes.length; i++) {
				if(nodes[i]._children) {
					nodes[i].children = nodes[i]._children;
					nodes[i]._children = null;
				}
			}
			update();
		});
	
		d3.select("#btn_collapse").on("click", function() {
			for(var i = 0; i < nodes.length; i++) {
				if(nodes[i].depth > 2) {
					if(nodes[i].children) {
						nodes[i]._children = nodes[i].children;
						nodes[i].children = null;
					}
				}
			}
			update();
		});
	
		function flatten(root) {
			var nodes = [], i = 0, depth = 1;
		
			function recurse(node, depth) {
				if(node.children) {
					node.size = node.children.reduce(function(p, v) { return p + recurse(v, depth+1); }, 0);
				}
				if(!node.id) {
					node.id = ++i;
					node.depth = depth;
				}
				nodes.push(node);
				return node.size;
			}
		
			root.size = recurse(root, depth);
			root.depth = 1;
			return nodes;
		}
		
		function findGroupPath(node, group) {
			var temp_links = [];
				
			var p_depth = node.depth;
			while(p_depth > 1) {
				for(var i = 0; i < links.length; i++) {
					d3.selectAll("image#" + group).each(function(l){
						if(links[i].source.id == l.id || links[i].target.id == l.id) {
							temp_links.push(links[i]);
							p_id = links[i].source.id;
							p_depth--;
						}
					});
				}
			}
			return temp_links;
		}
		
		function nodeMouseClickData(d) {
			if(!(d.depth == 1 || d.children == undefined)) {
				d3.selectAll("line").transition().style("opacity", ".2");
				overLinks = findGroupPath(d, d.group.substring(0, d.group.indexOf("_")));
				for(var i = 0; i < overLinks.length; i++) {
					var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
					d3.selectAll("line#link_" + id).transition().style("opacity", "1");
				}
				
				d3.selectAll("image").style("opacity", ".2");
				d3.selectAll("image#center").transition().style("opacity", "1");
				d3.selectAll("image#" + d.group.substring(0, d.group.indexOf("_"))).transition().style("opacity", "1");
				
				d3.selectAll("rect").style("opacity", ".2");
				d3.selectAll("rect#center").transition().style("opacity", "1");
				d3.selectAll("rect#" + d.group.substring(0, d.group.indexOf("_"))).transition().style("opacity", "1");
				
				d3.selectAll("text").style("opacity", ".2");
				d3.selectAll("text#center").transition().style("opacity", "1");
				d3.selectAll("text#" + d.group.substring(0, d.group.indexOf("_"))).transition().style("opacity", "1");
			}
			if(d.contentsGb == "CI") {
				if(d.contentsId == "CI00000001") {
					$('#cityGunValue').val("30");
				} else if(d.contentsId == "CI00000002") {
					$('#cityGunValue').val("31");
				} else if(d.contentsId == "CI00000003") {
					$('#cityGunValue').val("1");
				} else if(d.contentsId == "CI00000004") {
					$('#cityGunValue').val("17");
				} else if(d.contentsId == "CI00000005") {
					$('#cityGunValue').val("29");
				}
			} else {
				if(d.children == undefined) {
					$('#contentsId').val(d.contentsId);
					$('#contentsGb').val(d.contentsGb);			
					$('#subContentsGb').val(d.subContentsGb);
					$('#contentsName').val(d.name);
				}
			}
		}
	}
	/* D3 관련 차트 끝*/