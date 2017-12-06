$(function(){
	
	displayReport();
	// 검색 폼 on/off
	$('.btn_rank_search').click(function(){
		if(!validate()){
			return false;
		}
		displayReport();
		/*var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());
		
		//리포트 차트
		$.ajax({
			type : "post",     
			url : getContextPath() + "/keywordRanking/fieldReportChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				
                 $("#reportChart").html(data);
                 fnsearchTotalListFirst("A");
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});*/
		
	    //검색결과 샘플을 만들기 위한 코드
		/*fnsearchTotalListFirst("버스");*/
		
		return false;
	});
	
	function displayReport(){
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());
		
		//리포트 차트
		$.ajax({
			type : "post",     
			url : getContextPath() + "/keywordRanking/fieldReportChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				
                 $("#reportChart").html(data);
                 fnsearchTotalListFirst("A");
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}
	
	//검색조건 초기화
	$('#searchReset').click(function() {
		if($('input[name=pageType]').val() == 'synthesis'){	
			//종합랭킹
			$("#startDate").val('2016/06/24');
		    $("#endDate").val('2016/06/30');
		    $('#calendar_select').find('option:first').attr('selected',true);
		    $('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		    $('#categoryTypeList > select').find('option:first').attr('selected',true);
		    $('#minwonTypeList > select').find('option:first').attr('selected',true);
		    $('#guTypeList > select').find('option:first').attr('selected',true);
		    $('#r_chTypeList > select').find('option:first').attr('selected',true);
		    
		    $(".btn_xls").remove();
		    $('#calendar_select').change();
		}else{
			//관심키워드랭킹
			$("#startDate").val('2016/06/24');
		    $("#endDate").val('2016/06/30');
		    $('#calendar_select').find('option:first').attr('selected',true);
		    $('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		    $(".btn_xls").remove();
		    $('#calendar_select').change();
		}
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
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath() + "/keywordRanking/getAlikeSearch.do #alike",
			{"doc_id" : id, "title" : title, "content" : content},
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
		$("#searchForm").attr('action', getContextPath() + "/keywordRanking/excelVocFieldSearchResult.do").submit();
	});
	
	
});

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
			url : getContextPath()+"/keywordRanking/vocfieldSearchResult.do",
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
			/*$('#searchForm input[name=keyword]').val($.trim($('#search_keyword').text()));*/
			$('#searchForm input[name=keyword]').val($.trim($('#search_keyword').val()));
		}
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocfieldSearchResultList.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				
				$('#result_list').html(data);
				$('#share').text($('#result_list input[name=share]').val()+'%');
			},
			error : function(result) {
				
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}

	//상세보기
	function detailView(id){
		$("#basic-modal-detail").load(getContextPath() + "/keywordRanking/detailView.do #detail",
		{"doc_id" : id},
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
	