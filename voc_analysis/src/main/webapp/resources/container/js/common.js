$(function(){

	//알림사항 닫기
	$('.notice_close').click(function(){
		$(this).fadeOut();
		$(this).parent('li').slideUp();
		return false;
	});
	
	//레이어창 닫기
	$('.close_b').click(function(){
		$(this).parent().parent().hide('.layer_pop');
		var obj = $('#file');
		if(typeof obj != undefined) {
			if ($.browser.msie) {
				// ie 일때 input[type=file] init.
				$(obj).replaceWith( $(obj).clone(true) );
			} else {
				// other browser 일때 input[type=file] init.
				$(obj).val("");
			}
		}
	});


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


	//셀렉트 - 직접입력 선택시 달력 노출
	$(".view_datepicker").change(function(){
		 var date_value =  $(this).val();
	     if(date_value == "addDate"){
			$(this).parent().children('.add_date').slideDown('fast');
		 }else{
			$(this).parent().children('.add_date').slideUp('fast');
		 }
	 });

	//이슈키워드랭킹 조회구분 선택

	$('.'+$('.select_last').val()).show();

	$('.select_last').change(function(){
		
		$('.select_date').hide();
		var date_value =  $(this).val();
		$(this).parent().next().children('.'+date_value).fadeIn('fast');
	 });




	//layout style
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


	//help
	$('.icon_help').mouseover(function(){
		top_mg = $(this).next('.tooltip').height()+2;
		$(this).next('.tooltip').css('top','-' + top_mg + 'px').fadeIn('fast');
	});

	$('.icon_help').mouseout(function(){
		$(this).next('.tooltip').fadeOut('fast');
	});
			
	
	// map_info
	
	$('.map_g1').hover(map_show, map_hide);

	function map_show(){
		$('.map_pop1').show();	
	};

	function map_hide(){
		$('.map_pop1').hide();
	}
	
	$('.map_g2').hover(map_show2, map_hide2);

	function map_show2(){
		$('.map_pop2').show();	
	};

	function map_hide2(){
		$('.map_pop2').hide();
	}
	
	$('.map_g3').hover(map_show3, map_hide3);

	function map_show3(){
		$('.map_pop3').show();	
	};

	function map_hide3(){
		$('.map_pop3').hide();
	}
	
	
	$('.map_g4').hover(map_show4, map_hide4);

	function map_show4(){
		$('.map_pop4').show();	
	};

	function map_hide4(){
		$('.map_pop4').hide();
	}
	
	$('.map_g5').hover(map_show5, map_hide5);

	function map_show5(){
		$('.map_pop5').show();	
	};

	function map_hide5(){
		$('.map_pop5').hide();
	}
	
	
	$('.map_g6').hover(map_show6, map_hide6);

	function map_show6(){
		$('.map_pop6').show();	
	};

	function map_hide6(){
		$('.map_pop6').hide();
	}
	
	
	$('.map_g7').hover(map_show7, map_hide7);

	function map_show7(){
		$('.map_pop7').show();	
	};

	function map_hide7(){
		$('.map_pop7').hide();
	}
	
	
	$('.map_g8').hover(map_show8, map_hide8);

	function map_show8(){
		$('.map_pop8').show();	
	};

	function map_hide8(){
		$('.map_pop8').hide();
	}
	
	$('.map_g9').hover(map_show9, map_hide9);

	function map_show9(){
		$('.map_pop9').show();	
	};

	function map_hide9(){
		$('.map_pop9').hide();
	}
	
	$('.map_g10').hover(map_show10, map_hide10);

	function map_show10(){
		$('.map_pop10').show();	
	};

	function map_hide10(){
		$('.map_pop10').hide();
	}		

	$('.map_g11').hover(map_show11, map_hide11);

	function map_show11(){
		$('.map_pop11').show();	
	};

	function map_hide11(){
		$('.map_pop11').hide();
	}	
	
	$('.map_g12').hover(map_show12, map_hide12);

	function map_show12(){
		$('.map_pop12').show();	
	};

	function map_hide12(){
		$('.map_pop12').hide();
	}	
	
	$('.map_g13').hover(map_show13, map_hide13);

	function map_show13(){
		$('.map_pop13').show();	
	};

	function map_hide13(){
		$('.map_pop13').hide();
	}
	
	$('.map_g14').hover(map_show14, map_hide14);

	function map_show14(){
		$('.map_pop14').show();	
	};

	function map_hide14(){
		$('.map_pop14').hide();
	}	
	$('.map_g15').hover(map_show15, map_hide15);

	function map_show15(){
		$('.map_pop15').show();	
	};

	function map_hide15(){
		$('.map_pop15').hide();
	}
	
	$('.map_g16').hover(map_show16, map_hide16);

	function map_show16(){
		$('.map_pop16').show();	
	};

	function map_hide16(){
		$('.map_pop16').hide();
	}
		
	$('.map_g17').hover(map_show17, map_hide17);

	function map_show17(){
		$('.map_pop17').show();	
	};

	function map_hide17(){
		$('.map_pop17').hide();
	}
	
	
	$('.map_g18').hover(map_show18, map_hide18);

	function map_show18(){
		$('.map_pop18').show();	
	};

	function map_hide18(){
		$('.map_pop18').hide();
	}
	
	$('.map_g19').hover(map_show19, map_hide19);

	function map_show19(){
		$('.map_pop19').show();	
	};

	function map_hide19(){
		$('.map_pop19').hide();
	}
	
	$('.map_g20').hover(map_show20, map_hide20);

	function map_show20(){
		$('.map_pop20').show();	
	};

	function map_hide20(){
		$('.map_pop20').hide();
	}
	
		$('.map_g21').hover(map_show21, map_hide21);

	function map_show21(){
		$('.map_pop21').show();	
	};

	function map_hide21(){
		$('.map_pop21').hide();
	}
	
		$('.map_g22').hover(map_show22, map_hide22);

	function map_show22(){
		$('.map_pop22').show();	
	};

	function map_hide22(){
		$('.map_pop22').hide();
	}
	
		$('.map_g23').hover(map_show23, map_hide23);

	function map_show23(){
		$('.map_pop23').show();	
	};

	function map_hide23(){
		$('.map_pop23').hide();
	}
	
	
	$('.map_g24').hover(map_show24, map_hide24);

	function map_show24(){
		$('.map_pop24').show();	
	};

	function map_hide24(){
		$('.map_pop24').hide();
	}
	
	
	$('.map_g25').hover(map_show25, map_hide25);

	function map_show25(){
		$('.map_pop25').show();	
	};

	function map_hide25(){
		$('.map_pop25').hide();
	}
	
	

});