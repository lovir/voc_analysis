$(function(){	
	$('.btn_rank_search').click(function(){
		
		
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());

		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/regionDt.do",
			data : $("#searchForm").serialize(),
			success : function(data){
				$("#regionStart").html(data);
				fnsearchTotalListFirst('종로구');
			},
			error : function(result){
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	});
	
  //초기화 버튼
	$('#searchReset').click(function() {
		$("#startDate").val('2017/01/15');
	    $("#endDate").val('2017/01/21');
	    $('#calendar_select').find('option:first').attr('selected',true);
	    $('#selectText').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
		$('#selectOption').find('option:first').attr('selected',true);
		$("input[name='week_select']").val('');
		$('#week_select_text').html('');
		$('#month_select_text').html('');
		$('#year_select_text').html('');
		$('.week_select').css("display","block");
		$('.month_select').css("display","");
		$('.year_select').css("display","");
		$(".btn_xls").remove();
		$('#calendar_select').change();
		
	    $('#categoryTypeList > select').find('option:first').attr('selected',true);
	    $('#minwonTypeList > select').find('option:first').attr('selected',true);
	    $('#guTypeList > select').find('option:first').attr('selected',true);
	    $('#r_chTypeList > select').find('option:first').attr('selected',true);
	    $('#deptTypeList > select').find('option:first').attr('selected',true);
	    
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
		$("#searchForm").attr('action', getContextPath() + "/keywordRanking/excelVocRegionSearchResult.do").submit();
	});
	
	//지도 클릭 시 검색 기동
	$(document).on('click', '#search', function(){
		$('#searchForm input[name=keyword]').val($(this).attr("name"));
		$('#searchForm input[name=needsType]').val('');
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchTotalList();
	});
});

/**
 * 검색결과를 도출하는 기능을 수행
 * @param val
 */
	function fnsearchTotalListFirst(val){
	
		$("input[name='keyword'").val(val);

		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocRegionSearchResult.do",
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
	
	//VOC검색결과 리스트 생성(그룹_리스트+페이징)
	function fnsearchTotalList(){
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocRegionSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#search_result').html(data);
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
	
	//VOC검색결과 리스트 생성(리스트+페이징)
	function fnsearchList(){
		$('#searchForm input[name=keyword]').val($.trim($('#search_keyword').text()));
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchRegionResultList.do",
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
