
$(function(){
	var defaultSelectDate = "2017/01/23";
	//defaultSelectDate = "";
	//확인 버튼 클릭
	$('#searchStart').click(function(){	

		var selectOption = $('#selectOption').val();
		
		if(selectOption=='month_select'){
			$("input[name='condition']").val("MONTH");
		}else if(selectOption=='year_select'){
			$("input[name='condition']").val("YEAR");
		}else{
			if(!validate()){
				return false;
			}
			$("input[name='condition']").val("WEEK");
		}
		$('#searchForm input[name=currentPage]').val('1');
		
		$('input[name="vocChannel"]').val($('select[id="vocChannelList"] option:selected').val());
		$('input[name="vocRecType"]').val($('select[id="vocRecTypeList"] option:selected').val());
		$('input[name="vocKind"]').val($('select[id="vocKindList"] option:selected').val());
		$('input[name="vocPart"]').val($('select[id="vocPartList"] option:selected').val());
		$('input[name="vocItem"]').val($('select[id="vocItemList"] option:selected').val());
		$('input[name="repLevel"]').val($('select[id="repLevelList"] option:selected').val());
		$('input[name="metroDept"]').val($('select[id="metroDeptList"] option:selected').val());	//처리주무부서
		
		//이슈키워드 분석
		$.ajax({
			type : "post",     
			url : getContextPath() + "/keywordRanking/issueRanking.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var temp = $('#total_Rank').html(data).find('#keywordTemp').val();//키워드
				$("input[name='keyword']").val(temp);
				$('#total_Rank').html(data);
				if(!temp==''){
					fnsearchTotalListFirst(temp);
				}else{
					var str = search_result();
					$('#search_result').html(str);
					$('#reportChart').html('');
				}
			},
			error : function(request, status, error) {
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
		
		return false;
	});
	
	//전주대비 일시 아래 맨트 변경
	$('#week_select').change(function(){
		var thisDate = $('#week_select').val();
		thisDate = thisDate.replace(/\//gi,"");
		var thisYear = parseInt(thisDate.substring(0,4));	//달력으로 선택한 년도
		var thisMonth = parseInt(thisDate.substring(4,6)-1);//달력으로 선택한 월
		var thisDay = parseInt(thisDate.substring(6,8));	//달력으로 선택 일
		var thisFd = new Date(thisYear, thisMonth, 1);
		var thisWeek = Math.ceil((parseInt(thisDay)+thisFd.getDay())/7);	//달력으로 선택한 '주'가 몇주차인지 계산
		
		var lastDate = new Date(thisYear, thisMonth, thisDay);
		lastDate = new Date(Date.parse(lastDate) - 7 * 1000 * 60 * 60 * 24);
		
		var lastYear = lastDate.getFullYear();	//달력으로 선택한 날의 전주의 년도
		var lastMonth = lastDate.getMonth();	//달력으로 선택한 날의 전주의 월
		var lastDay = lastDate.getDate();		//달력으로 선택날의 전주 일(-7)
		var lastFd = new Date(lastYear, lastMonth, 1);	
		var lastWeek = Math.ceil((parseInt(lastDay)+lastFd.getDay())/7);	//달력으로 선택한 '주'의 전 주
		
		var text = lastYear+"년 "+(lastMonth+1)+"월 "+lastWeek+"째주 ~ "+thisYear+"년 "+(thisMonth+1)+"월 "+thisWeek+"째주";
		$('#week_select_text').html(text);
		
		
		//시작날짜 끝날짜 설정 - startDate, endDate
		var thisStartDate = new Date(thisYear, thisMonth, thisDay);
		var dateTemp = parseInt(thisStartDate.getDay());
		thisStartDate = new Date(Date.parse(thisStartDate) + (6 - dateTemp) * 1000 * 60 * 60 * 24);
		
		var thisStartDateYear = thisStartDate.getFullYear();
		var thisStartDateMonth = thisStartDate.getMonth()+1;
		var thisStartDateDate = thisStartDate.getDate();
		thisStartDateMonth = (thisStartDateMonth.toString().length > 1 ? thisStartDateMonth : "0"+thisStartDateMonth);
		thisStartDateDate = (thisStartDateDate.toString().length > 1 ? thisStartDateDate : "0"+thisStartDateDate);
		
		var lastStartDate = new Date(lastYear, lastMonth, lastDay);
		var lastDateTemp = parseInt(lastStartDate.getDay());
		lastStartDate = new Date(Date.parse(lastStartDate) - lastDateTemp  * 1000 * 60 * 60 * 24);
		
		var lastStartDateYear = lastStartDate.getFullYear();
		var lastStartDateMonth = lastStartDate.getMonth()+1;
		var lastStartDateDate = lastStartDate.getDate();
		lastStartDateMonth = (lastStartDateMonth.toString().length > 1 ? lastStartDateMonth : "0"+lastStartDateMonth);
		lastStartDateDate = (lastStartDateDate.toString().length > 1 ? lastStartDateDate : "0"+lastStartDateDate);
		
		$("input[name='startDate']").val(lastStartDateYear.toString()+lastStartDateMonth.toString()+lastStartDateDate.toString());
		$("input[name='endDate']").val(thisStartDateYear.toString()+thisStartDateMonth.toString()+thisStartDateDate.toString());
		
	});
	
	//조회구분- 전월대비 메뉴이용이 아래 멘트 변경 (년도)
	$('#month_select01').change(function(){
		var year = $('#month_select01').val();
		var month = $('#month_select02').val();
		year = parseInt(year);
		month = parseInt(month);
		var lastYear;
		var lastMonth;
		
		if(month==1){
			lastYear = year - 1 ;
			lastMonth = 12;
		}else{
			lastYear = year;
			lastMonth = month - 1;
		}
		var text = lastYear+"년 "+lastMonth+"월 ~ "+year+"년 "+month+"월";
		$('#month_select_text').html(text);
		
		//시작날짜 끝날짜 설정 - startDate, endDate
		lastMonth = (lastMonth.toString().length > 1 ? lastMonth : "0"+lastMonth);
		var lastDate = lastYear.toString() + lastMonth.toString() + "01"; 
		
		var thisDate = (new Date(year, month, 0)).getDate();
		thisDate = (thisDate.toString().length > 1 ? thisDate : "0"+thisDate);
		month = (month.toString().length > 1 ? month : "0"+month);
		var endDate = year.toString() + month.toString() + thisDate.toString();
		
		$("input[name='startDate']").val(lastDate);
		$("input[name='endDate']").val(endDate);
	});
	
	//조회구분- 전월대비 메뉴이용이 아래 멘트 변경 (월)
	$('#month_select02').change(function(){
		var year = $('#month_select01').val();
		var month = $('#month_select02').val();
		year = parseInt(year);
		month = parseInt(month);
		var lastYear;
		var lastMonth;
		
		if(month==1){
			lastYear = year - 1 ;
			lastMonth = 12;
		}else{
			lastYear = year;
			lastMonth = month - 1;
		}
		var text = lastYear+"년 "+lastMonth+"월 ~ "+year+"년 "+month+"월";
		$('#month_select_text').html(text);
		
		//시작날짜 끝날짜 설정 - startDate, endDate
		lastMonth = (lastMonth.toString().length > 1 ? lastMonth : "0"+lastMonth);
		var lastDate = lastYear.toString() + lastMonth.toString() + "01"; 
		
		var thisDate = (new Date(year, month, 0)).getDate();
		thisDate = (thisDate.toString().length > 1 ? thisDate : "0"+thisDate);
		month = (month.toString().length > 1 ? month : "0"+month);
		var endDate = year.toString() + month.toString() + thisDate.toString();
		
		$("input[name='startDate']").val(lastDate);
		$("input[name='endDate']").val(endDate);
	});
	
	//조회구분- 전년대비 select 메뉴 이용시 아래 맨트 변경
	$('#year_select').change(function(){
		var year = $('#year_select').val();
		year = parseInt(year);
		var text = (year-1)+"년 ~ "+year+"년";
		$('#year_select_text').html(text);
		
		//시작날짜 끝날짜 설정 - startDate, endDate
		$("input[name='startDate']").val((year-1).toString()+"0101");
		$("input[name='endDate']").val(year.toString()+"1231");
		
	});
	
	//기간 새로고침
	$('.btn_refresh').click(function(){
		/*$('#week_select').val('');
		$('#week_select_text').html('');*/
		$('#week_select').datepicker('setDate', new Date());
		$('#week_select').change();
		return false;
	});
	
	//기간 옵션 밑에 멘트 삭제
	$('#selectOption').change(function(){
		$('#week_select').val('');
		$('#week_select_text').html('');
		$('#month_select_text').html('');
		$('#year_select_text').html('');
		
		var option = $("#selectOption").val();
		var date = new Date();
		var year = date.getFullYear();
		if(option == 'week_select'){
			var thisYear = date.getFullYear();
			var thisMonth = date.getMonth()+1;
			var thisDay = date.getDate();
			var thisFd = new Date(thisYear, thisMonth, 1);
			var thisWeek = Math.ceil((parseInt(thisDay)+thisFd.getDay())/7);	//달력으로 선택한 '주'가 몇주차인지 계산
			thisMonth = (thisMonth.toString().length > 1 ? thisMonth : "0"+thisMonth);
			thisDay = (thisDay.toString().length > 1 ? thisDay : "0"+thisDay);

			var lastDate = new Date(thisYear, thisMonth, thisDay);
			lastDate = new Date(Date.parse(lastDate) - 7 * 1000 * 60 * 60 * 24);
			
			var lastYear = lastDate.getFullYear();	//달력으로 선택한 날의 전주의 년도
			var lastMonth = lastDate.getMonth();	//달력으로 선택한 날의 전주의 월
			var lastDay = lastDate.getDate();		//달력으로 선택날의 전주 일(-7)
			var lastFd = new Date(lastYear, lastMonth, 1);	
			var lastWeek = Math.ceil((parseInt(lastDay)+lastFd.getDay())/7);	//달력으로 선택한 '주'의 전 주
			
			var text = lastYear+"년 "+(lastMonth)+"월 "+lastWeek+"째주 ~ "+thisYear+"년 "+(thisMonth)+"월 "+thisWeek+"째주";
			$('#week_select_text').html(text);
			$('#week_select').val(thisYear+"/"+thisMonth+"/"+thisDay);
			
			//시작날짜 끝날짜 설정 - startDate, endDate
			var thisStartDate = new Date(thisYear, thisMonth, thisDay);
			var dateTemp = parseInt(thisStartDate.getDay());
			thisStartDate = new Date(Date.parse(thisStartDate) + (6 - dateTemp) * 1000 * 60 * 60 * 24);
			
			var thisStartDateYear = thisStartDate.getFullYear();
			var thisStartDateMonth = thisStartDate.getMonth();
			var thisStartDateDate = thisStartDate.getDate();
			thisStartDateMonth = (thisStartDateMonth.toString().length > 1 ? thisStartDateMonth : "0"+thisStartDateMonth);
			thisStartDateDate = (thisStartDateDate.toString().length > 1 ? thisStartDateDate : "0"+thisStartDateDate);
			
			var lastStartDate = new Date(lastYear, lastMonth, lastDay);
			var lastDateTemp = parseInt(lastStartDate.getDay());
			lastStartDate = new Date(Date.parse(lastStartDate) - lastDateTemp  * 1000 * 60 * 60 * 24);
			
			var lastStartDateYear = lastStartDate.getFullYear();
			var lastStartDateMonth = lastStartDate.getMonth();
			var lastStartDateDate = lastStartDate.getDate();
			lastStartDateMonth = (lastStartDateMonth.toString().length > 1 ? lastStartDateMonth : "0"+lastStartDateMonth);
			lastStartDateDate = (lastStartDateDate.toString().length > 1 ? lastStartDateDate : "0"+lastStartDateDate);
			
			$("input[name='startDate']").val(lastStartDateYear.toString()+lastStartDateMonth.toString()+lastStartDateDate.toString());
			$("input[name='endDate']").val(thisStartDateYear.toString()+thisStartDateMonth.toString()+thisStartDateDate.toString());
		}else if(option == 'month_select'){
			var month = date.getMonth()+1;
			month = (month.toString().length > 1? month : "0"+month);
			$("#month_select01 > option[value="+year+"]").attr('selected',true);
			$("#month_select02 > option[value="+month+"]").attr('selected',true);
			
			year = $('#month_select01').val();
			month = $('#month_select02').val();
			
			year = parseInt(year);
			month = parseInt(month);
			var lastYear;
			var lastMonth;
			
			if(month==1){
				lastYear = year - 1 ;
				lastMonth = 12;
			}else{
				lastYear = year;
				lastMonth = month - 1;
			}
			
			var text = lastYear+"년 "+lastMonth+"월 ~ "+year+"년 "+month+"월";
			$('#month_select_text').html(text);
			lastMonth = (lastMonth.toString().length > 1 ? lastMonth : "0"+lastMonth);
			var lastDate = lastYear.toString() + lastMonth.toString() + "01"; 
			var thisDate = (new Date(year, month, 0)).getDate();
			thisDate = (thisDate.toString().length > 1 ? thisDate : "0"+thisDate);
			month = (month.toString().length > 1 ? month : "0"+month);
			var endDate = year.toString() + month.toString() + thisDate.toString();
			
			$("input[name='startDate']").val(lastDate);
			$("input[name='endDate']").val(endDate);
			
		}else if(option == 'year_select'){
			$("#year_select > option[value="+year+"]").attr('selected',true);
			
			year = $('#year_select').val();
			year = parseInt(year);
			var text = (year-1)+"년 ~ "+year+"년";
			$('#year_select_text').html(text);
			$("input[name='startDate']").val((year-1).toString()+"0101");
			$("input[name='endDate']").val(year.toString()+"1231");
		}
			
	});	
	
	
	//초기화 버튼
	$('#searchReset').click(function() {
		$('#selectOption').find('option:first').attr('selected',true);
		$("input[name='week_select']").val('');
		$('#week_select_text').html('');
		$('#month_select_text').html('');
		$('#year_select_text').html('');
		$('.week_select').css("display","block");
		$('.month_select').css("display","");
		$('.year_select').css("display","");
		$(".btn_xls").remove();
		
		$('#vocChannelList').find('option:first').attr('selected',true);
		$('#vocRecTypeList').find('option:first').attr('selected',true);
		$('#metroDeptList').find('option:first').attr('selected',true);
		$('#vocKindList').find('option:first').attr('selected',true);
		$('#vocPartList').find('option:first').attr('selected',true);
		$('#vocItemList').find('option:first').attr('selected',true);
		$('#repLevelList').find('option:first').attr('selected',true);
		$('#metroDeptList').find('option:first').attr('selected',true);
		
		$('#week_select').datepicker('setDate', new Date());
		$('#week_select').change();
	});
	
	//카테고리 클릭
	$(document).on('click', '.tab_tit', function(){
		
		// tab에 선택 클리어
		$('.tab_win li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		
		$('#searchForm input[name=needsType]').val($(this).attr("name"));
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList();
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
		$('#searchForm input[name=needsType]').val('');
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
			     //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	

	function fnsearchTotalListFirst(val){
		$("input[name='keyword'").val(val);
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#vocSearchTeam').remove();
				$('#search_result').html(data);
			},
			error : function(result) {     
			     //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}

	//VOC검색결과 리스트 생성(리스트+페이징)
	function fnsearchList(){
		$('#searchForm input[name=keyword]').val($.trim($('#search_keyword').text()));
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchResultList.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#result_list').html(data);
				$('#share').text($('#result_list input[name=share]').val()+'%');
			},
			error : function(result) {     
			     //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
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
			     //alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	
	function search_result(){
		var str = "<div class=\"win_head clear2\">";
		str += "<span class=\"win_tit bg_none\">VOC 검색결과</span>";
		str += "<ul class=\"win_btnset\">";
		str += "<li><a href=\"javascript:;\" class=\"btn_sh\" title=\"접기\"></a></li>";
		str += "</ul></div>";
		str += "<div id=\"vocSearchTeam\" class=\"win_contarea\"></div></div>";
	
		return str;
	}
	
	function validate(){

		if($('#week_select').val().length <= 0){
			alert("기간을 설정해 주세요.");
			return false;
		}
		
		return true;
	}
	