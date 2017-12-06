
$(function(){
	
	// 검색 폼 on/off
	$('#searchStart').click(function(){
		
		if(!validate()){
			return false;
		}
		
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="pageSize"]').val(10);
		$('input[name="keyword"]').val($('#keywordTemp').val());
		
		$('input[name="socialChannel"]').val($('select[id="socialChannelList"] option:selected').val());
		
		$.ajax({//d3 차트 - 연관도 분석
			type : "post",
			url : getContextPath()+"/socialRelationAnalysis/radarChart.do",
			async : false,
			data : $("#searchForm").serialize(),
			success : function(data) {
				if(data.length > 4){
					$('#d3Chart').html(data);
				}else{
					$('#d3Chart').html('차트 결과가 없습니다.');
				}
				
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
						
		$.ajax({//하이차트 - 연관키워드 트렌드 분석
			type : "post",
			url : getContextPath()+"/socialRelationAnalysis/keywordTrend.do",
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
					$('#reportChart').empty();
					$('#reportChart').html('차트 결과가 없습니다.');
				}
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		//하단 VOC검색결과
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialRelationAnalysis/vocSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#search_result').html(data);
			},
			error : function(result) {	 
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		return false;
	});
	
	$('#keywordList').change(function() {
		var temp = $('#keywordList option:selected').text();
		if(temp=='직접입력'){
			$('#keywordTemp').val('');
			$('#keywordTemp').attr("disabled", false);
		}else{
			$('#keywordTemp').attr("disabled", true);
			$('#keywordTemp').val(temp);
		}
	});
	
	//연관도종합분석 > 초기화
	$('#searchReset').click(function() {
		
		$("#startDate").val('');
		$("#endDate").val('');
		$('#calendar_select').find('option:first').attr('selected',true);
		$('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		
		$('#socialChannelList ').find('option:first').attr('selected',true);
		
		$("#exceptKeyword").val('');
		
		$(".btn_xls").remove();
		$('#keywordList option:eq(0)').attr('selected','selected');
		$('#keywordTemp').val('');
		$('#keywordTemp').attr("disabled", false);
		$('#exclusion').val('');
		$('#calendar_select').change();
	});
	
	//페이지 문서수 변경
	$(document).on('change','#pageSize', function(){
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
	});

	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath()+"/socialRelationAnalysis/getAlikeSearch.do #alike",
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
		$("#searchForm").attr('action', getContextPath()+"/socialRelationAnalysis/excelVocSearchResult.do").submit();
	});
	
	
	// 키워드 삭제
	$('#del').live('click', function(){
		$(this).parent().remove();
		$('#keyword').val($('#keywordLabel').text());
	});
	
	
});

	//VOC검색결과 리스트 생성(리스트+페이징)
	function fnsearchList(){
		//$('#searchForm input[name=keyword]').val($.trim($('#search_keyword').text()));
		$.ajax({
			type : "post",
			url : getContextPath()+"/socialRelationAnalysis/vocSearchResultList.do",
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
		$("#basic-modal-detail").load(getContextPath()+"/socialRelationAnalysis/detailView.do #detail",
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
	
	function validate(){

		if($('#startDate').val().length <= 0){
			alert("시작일을 설정해 주세요.");
			return false;
		}
		
		if($('#endDate').val().length <= 0){
			alert("종료일을 설정해 주세요.");
			return false;
		}
		
		if($('#keywordTemp').val().length <= 0){
			alert('키워드를 입력하세요');
			return false;
		}
		
		if(!$('#exclusion').val() == ''){
			var exceptKeyword = $('#exclusion').val();
			var exceptKeywordArr = exceptKeyword.split(",");
			if(exceptKeywordArr.length > 5){
				alert("제외키워드는 5개까지만 입력가능합니다.");
				return false;
			}
		}
		
		return true;
	}
	
	// - IE 및 브라우저 버젼 체크할 때 사용. 현재는 사용 안함. - 2015-09-04-
	function ie_VersionCheck(){
		
		//IE 8이하는 d3적용 안됨.
		var _ua = navigator.userAgent;	
		var rv = -1;
		
		//IE 11,10,9,8
		var trident = _ua.match(/Trident\/(\d.\d)/i);
		if (trident != null){
			if(trident[1] == "7.0") return true; //return rv = "IE" + 11;
			if(trident[1] == "6.0") return true; //return rv = "IE" + 10;
			if(trident[1] == "5.0") return true; //return rv = "IE" + 9;
			if(trident[1] == "4.0") return false; //return rv = "IE" + 8;
		}
		
		var re = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
		if(re.exec(_ua) != null) rv = parseFloat(RegExp.$1);
		if(rv == 7) return false; //return rv = "IE"+7;
		
		var agt = _ua.toLowerCase();
		if (agt.indexOf("chrome") != -1) return true; //return 'chrome';
		if (agt.indexOf("opera") != -1) return true; //return 'opera';
		if (agt.indexOf("staroffice") != -1) return true; //return 'star office';
		if (agt.indexOf("webtv") != -1) return true; //return 'webtv';
		if (agt.indexOf("beonex") != -1) return true; //return 'beonex';
		if (agt.indexOf("chimera") != -1) return true; //return 'chimera';
		if (agt.indexOf("netpositive") != -1) return true; //return 'netpositive';
		if (agt.indexOf("phoenix") != -1) return true; //return 'phoenix';
		if (agt.indexOf("firefox") != -1) return true; //return 'firefox';
		if (agt.indexOf("safari") != -1) return true; //return 'safari';
		if (agt.indexOf("skipstone") != -1) return true; //return 'skipstone';
		if (agt.indexOf("netscape") != -1) return true; //return 'netscape';
		if (agt.indexOf("mozilla/5.0") != -1) return true; //return 'mozilla';
		
	}
