$(function(){

	// 알림사항 닫기
	$('.notice_close').click(function(){
		$(this).fadeOut();
		$(this).parent('li').slideUp();
		return false;
	});
	
	
	
	// 역별현황 더보기

	
	$('.more_list').toggle(hide_list, show_list);

	function hide_list(){
		$(this).addClass('off');
		$(this).text('- 접기');	
		$('.etc').addClass('on');
	}
	
	function show_list(){
		$(this).removeClass('off');
		$(this).text('+ 더보기');	
		$('.etc').removeClass('on');
	};
	
	


	
	




	// 왼쪽 서브메뉴 on/off
	 $('.left_nav li a.nav_1depth').toggle(show_sub, hide_sub);
	
	function show_sub(){
		$(this).next('span').html('-');	
		$(this).addClass('br_none');
		$(this).next().next('ul').slideDown();	
	};
	
	function hide_sub(){
		$(this).next('span').html('+');	
		$(this).removeClass('br_none');
		$(this).next().next('ul').slideUp();
	}

	$sub_list = $('.left_nav').find('.on');
	$sub_list.eq(0).click();


	// 유사문서 on/off
	 $('.btn_viewdoc').toggle(show_doc, hide_doc);
	
	function show_doc(){
		$(this).parent().parent().children('.doc_cont').slideDown();	
	};
	
	function hide_doc(){
		$(this).parent().parent().children('.doc_cont').slideUp();	
	}


	// 창 on/off
	
	$('.btn_sh').toggle(hide_win, show_win);

	function hide_win(){
		$(this).addClass('off');
		$(this).parents('.win_head').addClass('off');
		$(this).attr('title','펼치기');	
		$(this).parents('.win').children('.win_contarea').slideUp();
	}
	
	function show_win(){
		$(this).removeClass('off');
		$(this).parents('.win_head').removeClass('off');
		$(this).attr('title','접기');	
		$(this).parents('.win').children('.win_contarea').slideDown();
	};

	// 말풍선 on/off
	$('.btn_key').hover(key_show, key_hide);

	function key_show(){
		$(this).next('.key_graph').show();
//		btn_hide = $(this);
//		$(this).next('.key_graph').click(function(){
//			$(btn_hide).click();
//		});
	};

	function key_hide(){
		$(this).next('.key_graph').hide();
	}


	// 탭 on/off
	$('.tab_tit').click(function(){

		$(this).closest('.tab_wrap').next('.tab_cont').not($('.cont_'+$(this).attr("id"))).hide();
		$('.cont_'+$(this).attr("id")).show();

		$(this).closest('.tab_win').find('.tab_tit').not($(this)).removeClass('on');
		$(this).addClass('on');

		return false;
	});



	// 검색 폼 on/off
	$('.btn_rank_search').click(function(){
		$('.search_form').slideUp();
		$('.search_value').slideDown();
		return false;
	});

	$('.btn_rank_reset').click(function(){
		$('.search_form').slideDown();
		$('.search_value').slideUp();
		return false;
	});


	//dashboard btn
	if($('.btn_dashboard').is('.on')){
		$('.left_area').css('z-index','1001');
	};


	// 셀렉트 - 직접입력 선택시 달력 노출
	$(".view_datepicker").change(function(){
		 var date_value =  $(this).val();
	     if(date_value == "addDate"){
			$(this).parent().children('.add_date').slideDown('fast');
		 }else{
			$(this).parent().children('.add_date').slideUp('fast');
		 }
	 });

	// 이슈키워드랭킹 조회구분 선택

	$('.'+$('.select_last').val()).show();

	$('.select_last').change(function(){
		
		$('.select_date').hide();
		var date_value =  $(this).val();
		$(this).parent().next().children('.'+date_value).fadeIn('fast');
	 });




	// layout style
	$('.rank_group > li:last-child').css('margin-right','0');
	$('.terms_list li:last-child').css('margin-right','0');

	$('.notice_list li:odd').addClass('bg_gray');
	$('.tbl_type03 tr:last-child').addClass('last_line');
	$('.rank_bottom_area ul li:odd').css('background','#ebf1f5');
	$('.result_group dd ul li:last-child').css('background','none');
	$('.tbl_type04 tr:odd').addClass('tr_bg');
	$('.tbl_type07 tr:last-child td').css('background','none');
	$('.tbl_type07 tr:last-child td').css('background','#fff');
	$('.result_group2 dd ul li:last-child').css('background','none');
	$('.doc_view dd ul li:last-child').css('background','none');


	// help
	$('.icon_help').mouseover(function(){
		top_mg = $(this).next('.tooltip').height()+2;
		$(this).next('.tooltip').css('top','-' + top_mg + 'px').fadeIn('fast');
	});

	$('.icon_help').mouseout(function(){
		$(this).next('.tooltip').fadeOut('fast');
	});
			
	
	
	

});