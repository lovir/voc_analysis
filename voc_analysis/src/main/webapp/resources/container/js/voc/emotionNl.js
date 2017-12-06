$(function(){
	
	rankSearch();
	$('.btn_rank_search').click(function(){
		
		rankSearch();
	});
	
	function rankSearch(){
		$("#rnStart").attr('class','win_contarea align_c clear2');
		if($("#emotion").val() == "" | $("#emotion").val() == null){
			
			$("#emotion").val("P");
		}
			
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());

		// 긍/부정 퍼센테이지
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/emotionPercentage.do",
			data : $("#searchForm").serialize(),
			success : function(data){
				$("#percentage").html(data);
				$("#posiBox").attr('class','gray_box1 smile_on mr_5');
				var posiImgsc = document.getElementById('posiImg');
				posiImg.src="../resources/images/common/icon_smile.png";
				
				// 긍/부정 Top10 키워드
				$.ajax({
					type : "post",
					url : getContextPath()+"/keywordRanking/emotionList.do",
					data : $("#searchForm").serialize(),
					success : function(data){
		                var temp = $("#topList").html(data).find("#resultkeyword").val();
		                $("input[name='keyword']").val("");
		                $("#topList").html(data);

		                //fnsearchTotalListFirst(temp);
		                fnsearchTotalListFirst($("#emotion").val());
		                
		                // 리포트차트 분포도 
		                $.ajax({
		        			type : "post",     
		        			url : getContextPath() + "/keywordRanking/emotionCategory.do",
		        			data : $("#searchForm").serialize(),
		        			success : function(data) {
		        				/*alert("리포트 완성");*/
		        				 $("#reportTitle").html("긍정 카테고리 분포도");
		        				 $("#report_start").attr('class','graph_in_r');
		        				 $("#reportTitle").attr('class','title mb_10');
		                         $("#reportChart").html(data);
		        				
		        			},
		        			error : function(request, status, error) {
		        				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		        			}
		                });
		                
					},
					error : function(result){
						$("#topbox").empty();
						$("#searchCategory").empty();
						$('.tab_cont').empty();
						alert("해당 분석기간에 긍정 혹은 부정 키워드가 존재하지 않습니다.");
					}
				});
				
			},
			error : function(result){
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		// 긍/부정 Top10 키워드
		/*$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/emotionList.do",
			data : $("#searchForm").serialize(),
			success : function(data){
                var temp = $("#topList").html(data).find("#resultkeyword").val();
                $("input[name='keyword']").val(temp);
                $("#topList").html(data);
                fnsearchTotalListFirst(temp);
			},
			error : function(result){
				$("#topbox").empty();
				$("#searchCategory").empty();
				$('.tab_cont').empty();
				alert("해당 분석기간에 긍정 혹은 부정 키워드가 존재하지 않습니다.");
			}
		});*/
		
		// 리포트차트 분포도 
		/*$.ajax({
			type : "post",     
			url : getContextPath() + "/keywordRanking/emotionCategory.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				alert("리포트 완성");
				 $("#reportTitle").html("긍정 카테고리 분포도");
				 $("#report_start").attr('class','graph_in_r');
				 $("#reportTitle").attr('class','title mb_10');
                 $("#reportChart").html(data);
				
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});*/
	}

	
	
	 //초기화 버튼
	$('#searchReset').click(function() {
		$("#startDate").val('2016/06/24');
	    $("#endDate").val('2016/06/30');
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
	
	//랭킹키워드에 있는 단어 클릭시
	$(document).on('click', '.key_word', function(){
		$('#searchForm input[name=keyword]').val($(this).attr("name"));
		$('#searchForm input[name=needsType]').val('');
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchTotalList();
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
		$("#searchForm").attr('action', getContextPath() + "/keywordRanking/excelVocSearchResult.do").submit();
	});
	
	
	//긍정버튼 클릭
	$(document).on('click', '.positiveBtn', function(){
		$("#emotion").val("P");
		$("#reportTitle").html("긍정 카테고리 분포도");
		
		
		if($("#emotion").val() == "" | $("#emotion").val() == null){
			$("#reportTitle").html("긍정 카테고리 분포도");
			$("#emotion").val("P");
		}
			
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());

		// 긍/부정 퍼센테이지
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/emotionPercentage.do",
			data : $("#searchForm").serialize(),
			success : function(data){
				$("#percentage").html(data);
				$("#posiBox").attr('class','gray_box1 smile_on mr_5');
				var posiImgsc = document.getElementById('posiImg');
				posiImg.src="../resources/images/common/icon_smile.png";
				
				// 긍/부정 Top10 키워드
				$.ajax({
					type : "post",
					url : getContextPath()+"/keywordRanking/emotionList.do",
					data : $("#searchForm").serialize(),
					success : function(data){
		                var temp = $("#topList").html(data).find("#resultkeyword").val();
		                $("input[name='keyword']").val("");
		                $("#topList").html(data);
		               // fnsearchTotalListFirst(temp);
		                fnsearchTotalListFirst($("#emotion").val());
		                
		        		// 카테고리 분포도 
		        		$.ajax({
		        			type : "post",     
		        			url : getContextPath() + "/keywordRanking/emotionCategory.do",
		        			data : $("#searchForm").serialize(),
		        			success : function(data) {
		        				
		        				 /*$("#reportcart_make").html();*/
		                         $("#reportChart").html(data);
		        				
		        			},
		        			error : function(request, status, error) {
		        				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		        			}
		        		});
		        		
					},
					error : function(result){
						alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
					}
				});
			},
			error : function(result){
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
	});
	
	//부정버튼 클릭
	$(document).on('click', '.DenyBtn', function(){
		$("#emotion").val("N");
		$("#reportTitle").html("부정 카테고리 분포도");
		
		
		if($("#emotion").val() == "" | $("#emotion").val() == null){
			$("#reportTitle").html("긍정 카테고리 분포도");
			$("#emotion").val("P");
		}
			
		var selectOption = $('#calendar_select option:selected').val();
		$('#searchForm input[name=currentPage]').val('1');
		$("#rankingIndex").val(0);
		$('#searchForm input[name=condition]').val(selectOption);
		$('input[name="needsType"]').val($('select[name="selectNeedsType"] option:selected').text());

		// 긍/부정 퍼센테이지
		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/emotionPercentage.do",
			data : $("#searchForm").serialize(),
			success : function(data){
				$("#percentage").html(data);
				$("#denBox").attr('class','gray_box1 angry_on');
				var denImgsc = document.getElementById('denImg');
				denImg.src="../resources/images/common/icon_angry.png";
				
				// 긍/부정 Top10 키워드
				$.ajax({
					type : "post",
					url : getContextPath()+"/keywordRanking/emotionList.do",
					data : $("#searchForm").serialize(),
					success : function(data){
		                var temp = $("#topList").html(data).find("#resultkeyword").val();
		                $("input[name='keyword']").val("");
		                $("#topList").html(data);
		                fnsearchTotalListFirst($("#emotion").val());
		                
		        		// 카테고리 분포도 
		        		$.ajax({
		        			type : "post",     
		        			url : getContextPath() + "/keywordRanking/emotionCategory.do",
		        			data : $("#searchForm").serialize(),
		        			success : function(data) {
		        				
		        				 /*$("#reportcart_make").html();*/
		                         $("#reportChart").html(data);
		        				
		        			},
		        			error : function(request, status, error) {
		        				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
		        			}
		        		});
		        		
					},
					error : function(result){
						alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
					}
				});
				
			},
			error : function(result){
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
	});
});


/**
 * 검색결과를 도출하는 기능을 수행
 * @param val
 */
	function fnsearchTotalListFirst(val){
		
		$("input[name='emotion'").val(val);

		$.ajax({
			type : "post",
			url : getContextPath()+"/keywordRanking/vocSearchResult.do",
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
