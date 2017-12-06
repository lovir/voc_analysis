window.currentTime = 15.5; // 현재시간을 셋팅 전역변수
window.setTime = window.currentTime; // 


//ajax 객체 생성
function getAjaxHttp(){
	var xmlhttp;
	if(window.XMLHttpRequest){
		xmlhttp = new XMLHttpRequest();
	} else {
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xmlhttp;
}

function requestAjaxData(ajax, method, url, callback) {
	ajax.onreadystatechange = function() {
		if(ajax.readyState==4 && ajax.status==200) eval(callback)(ajax);
	};
	ajax.open(method, url, true);
	ajax.send();
}

function callAjax(method, url, callback) {
	var ajax = getAjaxHttp();
	if(ajax==null) {
		alert("ajax 변수 세팅안됨"); 
		return;
	}
	requestAjaxData(ajax, method, url, callback);
}

function callAjaxPost(url, data, callback) {
	$.ajax({
	       type: "POST",
	       url: url, 
	       data: data, 
	       success: function(data) {
	    	   eval(callback)(data); //fn_registEvalCallBack(data);
	       },
	       error: function() {
	          alert("error");
	       }
	});
}

$(document).ready(function(){
	height_size(); 
	on_js(); // 리스트 내 on class
	close_open();
	slide(); // 조건설정 및 조회 접기 열기
	tab_lnb();
	gnb_on();

	menuTree();

	precess_boarder();
	hottopic();
	bar_graph();
	time_playerbtn();

	if ($(".calendar").size() > 0) {
		dayZone(); //타임라인 캘린더
		setTimeLine(); // 타임라인 셋팅
		timeline();
		calendar();
	}

	init();

   if ($(".topicZone").size() == 1) {
	   var current = "total";
	   var topic = $(".hottoopic_order .current");
	   if (topic.size() != 0) current = topic.index();
	   getLineGraph(current);
   }
});

function init() {
	radio(); // 라디오버튼 디자인 효과
	resetBtn(); // 섹렉트박스 초기화 이벤트

	$(".player_select").selectbox();

	$(".datepicker").datepicker({
      showOn: "button"
    });
}

$(window).load(function(){ 
   scrollbar();
   var html = '<div class="slider_bg"></div>';
   html += '<div class="slider_selectbg01"></div>';
   html += '<div class="slider_selectbg02"></div>';
   $(".select_w88").prev().prev().before(html);
   
   var html2 = '<div class="slider_bg2"></div>';
   if ($(".select_g67").size() > 0) $(".select_g67").prev().prev().before(html2);
});

$(window).resize(function(){
   height_size();
   dayZone();
});

// ******************************************************
// 셀렉트박스 초기화 이벤트
// 작성자: 아이콘인터렉티브 신옥섭
// ******************************************************
function resetBtn() {
	$("input[type=reset]").unbind("click");
	$("input[type=reset]").bind("click", function() {
		var _form = $(this).parents("form");
		var _select_box = $(".jquery-selectbox",_form);

		_select_box.each(function(i) {
			var _select = $(this).find("select");
			var _settingsSelect = $(this).find(".jquery-selectbox-currentItem");
			var _settiongOption = $(this).find(".jquery-selectbox-list .jquery-selectbox-item").eq(0);
			var _text = _settiongOption.text();
			
			var _val = _settiongOption.attr("class").split(' ');
			for( k1 in _val ) {
				if(/^value-.+$/.test(_val[k1])) {
					_val = _val[k1].replace('value-','');
					break;
				}
			};
			_settingsSelect.text(_text).attr("class","jquery-selectbox-currentItem item-0");
			_select.val(_val).triggerHandler('change');
		});
	});
}


// ******************************************************
// 대시보드 height size
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function height_size(){

   var winH = $(window).height();
   var headerH = $("header").height();
   var timeH = $(".timeline").height();
   var footH = $("footer").height();

   // if(winH > 760) winH = $(window).height();
   // if(winH < 560) winH = 560;
   // winH = $(window).height();
   if($("section").hasClass("stats_box")){
     if(winH < 560) winH = 560;
   }else{
     if(winH < 350) winH = 350;
   }
   if($("article > section").hasClass("timeline"))var settingH01 = winH - headerH - 34;
   else var settingH01 = winH - headerH - 35;
   var settingH02 = settingH01 - timeH - 42;
   if($("article > section").hasClass("timeline"))var settingH03 = settingH01 - timeH - 60;
   else var settingH03 = settingH01 - timeH - 61;

   var dashboard = winH - $(".timeline").height() - headerH - $(".stats_box").height() - 30;
   var overview = winH - $(".timeline").height() - headerH - 34;
   var newSetting = dashboard - 42;
   if (newSetting < 480) newSetting = 480;

   $(".content_box").height(settingH01);
   $(".dashboardContnet").height(dashboard);
   $(".step01, .step02, .step03").height(newSetting);
   $(".left_area, .right_area").height(settingH03);
   $(".areaLeft, .areaCenter, .areaRight").height(settingH03);
   $(".overviewContent").height(overview);

	if ($(".right_scrollbar").size() > 0) {
	   $(".right_scrollbar").mCustomScrollbar("update");
	   $(".left_scrollbar").mCustomScrollbar("update");
	   $(".center_scrollbar").mCustomScrollbar("update");
	}
	if ($(".overviewContent").size() > 0) {
	   $(".overviewContent").mCustomScrollbar("update");
	}

}



// ******************************************************
// 라디오버튼 디자인 변경 함수
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function radio(){
   var radio_design = $(".designRadio");
   var radio_label = $(".select_label");

   radio_design.unbind("click");
   radio_label.unbind("click");

   radio_design.bind("click",function(){
   	  if($(this).parents("ul").hasClass("userSelect02")) return false;

      $(this).children(".radio").addClass("checked").parents("li").siblings("li").find(".radio").removeClass("checked");
      $(this).next("input").attr("checked","checked").parents("li").siblings("li").find("input").removeAttr("checked");
	  if ($(".hottoopic_order").size() == 1) radoiCheck();
   });

   radio_label.bind("click",function(){
   	  if($(this).parents("ul").hasClass("userSelect02")) return false;
   	  
      $(this).parents("li").find(".radio").addClass("checked").parents("li").find("input").attr("checked","checked");
      $(this).parents("li").siblings("li").find(".radio").removeClass("checked").parents("li").find("input").removeAttr("checked");
	  if ($(".hottoopic_order").size() == 1) radoiCheck();
   }); 

   function radoiCheck() {
		if ($(".radiobtn").eq(0).attr("checked")) {selectGraph("total"); return;}
		if ($(".hottoopic_order .current").size() == 0) $(".hottoopic_order li a").eq(0).click();
   }  
}


// ******************************************************
// li 리스트 중 on 클래스 추가 함수
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function on_js(){
   $(".on_js li").click(function(){
      $(this).addClass("on").siblings("li").removeClass("on");
      return false;
   });
}

// ******************************************************
// 타임라인 열고 닫기 함수
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function close_open(){


    if ($(".timeline").size() < 1){
	  	$("header").css("border-bottom","1px solid #7c7e82").next("article").find(".main_contents").css("border-top","1px solid #c6c9cf");
		return false;
	  } 
   $(".timeline_btn").click(function(){
      if($(".timeline_btnbox").hasClass("btn_on")){
         $(".timeline_inner").slideDown("normal", function(){
            $(".timeline_btnbox").removeClass("btn_on");
         });
      } else{
         $(".timeline_inner").slideUp("normal", function(){
            $(".timeline_btnbox").addClass("btn_on");
         });

         if ($(".stats_btnbox .statsbox_btn").size() > 0) {
         	var statBtn = $(".stats_btnbox .statsbox_btn");
      	   if ($(".stats_btnbox").hasClass("btn_on")) statBtn.click();
		   dashboardContnet();
     	}
      }


	  if ($(".stats_btnbox").size() == 1) return false;

      var height = $(".content_box").height();
      var openboxHeight = $(".openbox").find(".openbox_inner").height();

      $(".content_box, .left_area, .right_area").animate({
         height : height + openboxHeight
      },"normal",function() {height_size();});
       return false;
   });
}


// ******************************************************
// 콘솔 창
// 작성자: 아이콘인터렉티브 신옥섭
// ******************************************************

function log(msg) {
   window.console&&console.log(msg);
}


// ******************************************************
// 타임라인 캘린더 
// 작성자: 아이콘인터렉티브 신옥섭
// ******************************************************

function dayZone() {
   var width = $(window).width();
   var dayZoneWidth = 1920;

   if (width < 1280) width = 1280;

   var dayWidth = $(".days li:first").width();
   var weekWidth = $(".week li:first").width();
   var monthsWidth = $(".months li:first").width();

   var dayLen = ($(".days .first").prevAll().size() - 4) * dayWidth;
   var weekLen = ($(".week .first").prevAll().size() - 4) * weekWidth;
   var monthsLen = ($(".months .first").prevAll().size() - 4) * monthsWidth;
   
   var margin = (width- dayZoneWidth) / 2;
   $(".days").css("margin-left",margin - dayLen);
   $(".week").css("margin-left",margin - weekLen);
   $(".months").css("margin-left",margin - monthsLen);
}


function setTimeLine() {
	var html = '';
	for (var i=0; i< 25; i++) {
		if (i < 10) i = '0' + i;
		html += '<li></li>';
	}
	$(".slider_time").html(html);
	$("#slider").slider({
		range: true,
		values: [0, 100],
		slide: function( event, ui ) {
			selectTime (ui.values[0], ui.values[1])
		 }
	});
	var currentTime = window.setTime; //현재시간 또는 오늘을 제외한 날은 24시로 셋팅
	var sliderWidth = 768 * (currentTime/24);
	$(".ui-slider").width(sliderWidth);

	for (var i=0; i< currentTime+1; i++) {
		$(".slider_time li").eq(i).addClass("current");
	}
	//var hotGraph = window.hotGraph;
	//if (hotGraph) hotGraph(0, parseInt(currentTime));
	selectTime(0,100);
}

function selectTime(first,last) {
	var timeZone = $(".ui-slider").width();
	first = parseInt(timeZone * (first / 100)) / 32;
	last = parseInt(timeZone * (last / 100)) / 32;

	/* 해당시간 값을 가져오기 위한 부분
	var firstUp = first;
	var lastUp = last;
	$(".ui-slider-handle").unbind("mouseup");
	$(".ui-slider-handle").bind("mouseup",function() {
		
	});*/

	first = Math.ceil(first);
	last = parseInt(last) + 1;
	$(".slider_time li").removeClass("on");
	for (var i = first; i < last; i++) {
		$(".slider_time li").eq(i).addClass("on");
	}
}

function time_playerbtn(){
	if($(".stopbtn").size() < 1)return false;
	$(".stopbtn").bind("click",function(){
		$(this).toggleClass("on");
	});
}

// ******************************************************
// scrollbar 
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function scrollbar(){
	if ($(".right_scrollbar").size() == 1) {
	   $(".right_scrollbar").mCustomScrollbar({
		  autoHideScrollbar:true,
		  theme:"light-thin",
		  advanced:{
				updateOnContentResize: true,
				autoScrollOnFocus: false
			},
			callbacks:{
				whileScrolling:function(){ WhileScrolling(); } 
			}
	   });
	   $(".left_scrollbar").mCustomScrollbar({
		  autoHideScrollbar:true,
		  theme:"light-thin",
		  advanced:{
				updateOnContentResize: true,
				autoScrollOnFocus: false
			}
	   });
	   $(".center_scrollbar").mCustomScrollbar({
		  autoHideScrollbar:true,
		  theme:"light-thin",
		  advanced:{
				updateOnContentResize: true,
				autoScrollOnFocus: false
			}
	   });
	}

	if ($(".sub_scrollbar").size() > 0) {

		$(".sub_scrollbar").mCustomScrollbar({
			autoHideScrollbar:true,
			theme:"light-thin",
			advanced:{
				updateOnContentResize: true,
				autoScrollOnFocus: false
			} 
		});
	}

	if ($(".dashboardContnet").size() == 1) {
		$(".dashboardContnet").mCustomScrollbar({
			autoHideScrollbar:true,
			theme:"light-thin",
			advanced:{
				updateOnContentResize: true,
				autoScrollOnFocus: false
			} 
		});
	}
	
	if ($(".overviewContent").size() == 1) {
		$(".overviewContent").mCustomScrollbar({
			autoHideScrollbar:true,
			theme:"light-thin",
			advanced:{
				updateOnContentResize: true,
				autoScrollOnFocus: false
			} 
		});
	}
}
function WhileScrolling(){
	var datepicker = $("#ui-datepicker-div");
	if (datepicker.css("display") == "block") {
		var changeTop = window.pickerTop + mcs.top;
		datepicker.css("top",changeTop+"px");
	}
}

// ******************************************************
// tab slideToggle 
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function slide(){
	$(".search_area_inner").hide().parents(".tab").css("opacity","0.5").find(".search_btn").addClass("btn_on");
	$(".search_area_inner02").hide().parents(".tab").css("opacity","0.5").find(".search_btn").addClass("btn_on");
    $(".search_btn").click(function(){
      $(this).toggleClass("btn_on").prev(".hide_zone").find(".hide_zone_inner").slideToggle();
      if($(this).hasClass("btn_on")) $(this).parents(".tab").css("opacity","0.5");
      else $(this).parents(".tab").css("opacity","1");
      $("select").selectbox();
      return false;
   });
}

// ******************************************************
// 분석 결과 게시판  
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function precess_boarder(){
   $(".ing_Per").each(function(){
      var v = parseInt($(this).text());
      for(var i=1;i<=10;i++){
         if(v==0)v = "per_0"; 
         if(v==i+"0") v = "per_"+i+"0";
         if(v==i+"00") v= "per_"+i+"00";
      }
      $(this).find("span").attr("class",v);
   });   

   $(".ing_Ico").each(function(){
      if($(this).hasClass("error")) $(this).parents("li").addClass("error");
      if($(this).hasClass("done")) $(this).parents("li").addClass("done");
   });
   a_none();

}


// ******************************************************
// 음성처리현황 a링크 넣기
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function a_none(){
   $(".row_list03 a").click(function() {
      var done = $(this).parent().parent();
      if (done.hasClass("done")) return true;
      return false; 
   });
}


// ******************************************************
// 핫토픽 (음성분석결과)  
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function hottopic(){
	$(".hottoopic_order li").removeClass("current");
   	$(".hottoopic_order li a").click(function(){
      $(this).parent().addClass("current").siblings("li").removeClass("current");
      bar_graph();
      var select = $(this).parent().text();
	  var current = $(this).parent("li").index();
      selectGraph(current);

	  $(".select_label").eq(1).click();
      return false;
   });
}

function bar_graph(){
 var totaltxt = $(".hottoopic_order .current").find(".total").text();
   var txtdel01 = totaltxt.substr(2, totaltxt.length); // 슬라이드(/) 빼기
   var txtdel02 = txtdel01.substr(0, txtdel01.length-1); // 건 문자 빼기
   var txtdel03 = txtdel02.replace(/,/g, ""); // 콤마(,) 빼기

   $(".bargraph_inner").each(function(){
      var bar_num = parseInt($(this).find(".bar_num").text());
      var num =  Math.ceil((100/txtdel03)*bar_num);
      $(this).find(".bar").animate({"width": num+"%"},500);
   });
}


// ******************************************************
// 상세페이지 lnb
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function tab_lnb(){
	if($(".lnbList").size() < 1) return false;

	$("section .lnbList").eq(0).show().siblings(".lnbList").hide();
	$(".tab_lnb li").click(function(){
		var i = $(this).index();
		// alert(i);
		$(this).parent().parent().parent("section").find(".lnbList").eq(i).show().siblings(".lnbList").hide();
      	$("select").selectbox();      
		return false;
	})
}

// ******************************************************
// 상세페이지 gnb
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function gnb_on(){
	// var i = $(".gnb > ul > .on").index();
	if($(".gnb li").hasClass("on"))	var v = $(".gnb > ul > .on").siblings("li");
	else var v = $(".gnb").find("ul > li");
	v.mouseover(function(){
      if($(".gnb > ul > .on").find("ul").size() > 0) $(".gnb > ul > .on").find("ul").hide();
      $(this).addClass("over").siblings("li").removeClass("over");
   });
   $(".gnb > ul > li").mouseleave(function(){
      $(".gnb > ul > li").removeClass("over");
      $(".gnb > ul > .on").find("ul").show();
	});
}


// ******************************************************
// 타임라인
// 작성자: 아이콘인터렉티브 신옥섭
// ******************************************************

function timeline(){
	$(".cal_group").eq(0).show().siblings(".cal_group").hide();
	
	$(".calendar_nav li a").click(function(){
		if ($(this).parent().hasClass("on")) return false;
		var index = $(this).parent().index();
		$(this).parent().addClass("on").siblings("li").removeClass("on");

		if (index < 2 ) {
			$(".cal_group").eq(0).show().siblings(".cal_group").hide();
			window.setTime = window.currentTime;
			setTimeLine();
		} else {
			$(".cal_group").eq(index-1).show().siblings(".cal_group").hide();
			window.setTime = 24;
			setTimeLine();
		}
		
		// today 선택시 일간, 주간, 월간 초기화
		var $dayCurrent = $("#"+window.currentDay+"_day");
		var $weekCurrent = $("#20140316_20140322_week");
		var $monthCurrent = $("#201402_month");
		var $current;
		$(".cal_group").each(function(i) {
			switch(i) {
				case 0 : $current = $dayCurrent; break;
				case 1 : $current = $weekCurrent; break;
				default : $current = $monthCurrent; break;
			}

			var dayWidth = $("ul:last li:first", this).width();
			var dayLen = ($current.prevAll().size() - $("ul:last .first", this).prevAll().size() - 3) * dayWidth;
			var margin = parseInt($("ul:last", this).css("margin-left"));
			var margin_left = margin - dayLen;

			//$(".calendar_nav li").eq(0).addClass("on").siblings().removeClass("on");
			$current.siblings().attr("class","");
			$current.attr("class","view next current");
			$current.prev().attr("class","view").prev().attr("class","view").prev().attr("class","view first");
			$current.next().attr("class","next view").next().attr("class","next view").next().attr("class","next view last");
			$("ul:last", this).css("margin-left",margin_left);
		});


		return false;
	});
}

// ******************************************************
// 레이어팝업 테스트
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

// 유틸 레이어팝업 호출 
function showlayer(objId) {
	if(objId == ''){
		$("#layer").removeClass("open_layer");
		$("#layer").removeClass("open_util");
		$("#layer").empty();
	}else{
		$("#layer").addClass("open_layer");
		$("#layer").addClass("open_util");
		var link = "../util/" + objId + ".html"; // 차후 확장자 변경
		$("#layer").load(link,function(){
			init();
			layerPopup();
			layerClose();
		});
	}
}

function layerClose() {
	$("#layer").mouseleave(function() {
		window.showLayer = true;
	}).mouseenter(function() {
		window.showLayer = false;
	});
	$("body").click(function() {
		if (window.showLayer) showlayer('');
	});
}

// 일반 경고팝업 및 설정팝업 호출
function showlayer02(objId) {
	if(objId == ''){
		$("#layer").removeClass("open_layer02");
		$("#layer").empty();
	}else{
		$("#layer").addClass("open_layer02");
		var link = "../layer/" + objId + ".html"; // 차후 확장자 변경
		$("#layer").load(link,function(){
			init();
			layerPopup();
		});
	}
}

// 유틸시 경고팝업 뜨는 이벤트 호출
function showlayer03(objId) {
	if(objId == ''){
		$("#layer").removeClass("open_layer02");
		$("#layer").empty();
	}else{
		$("#layer").removeClass("open_layer").removeClass("opne_util");
		$("#layer").addClass("open_layer02");
		var link = "../layer/" + objId + ".html"; // 차후 확장자 변경
		$("#layer").load(link,function(){
			init();
			layerPopup();
		});
	}
}


// ******************************************************
// 타임라인 달력 움직임
// 작성자: 아이콘인터렉티브 신옥섭
// ******************************************************

function calendar() {
	window.todayHtml = $(".days").html(); // 현재날짜 html 임시저장
	window.currentDay = 20140325; // 현재날짜
	window.changeDay = window.currentDay;
	window.setYear = sliceDay(window.currentDay)[0]; // 현재 년도
	window.setMonth = doubleDigit(sliceDay(window.currentDay)[1]); // 현재 달
	window.setDay = sliceDay(window.currentDay)[2]; // 현재 일

	$(".cal_group .current .day_box").css("cursor","pointer");
	cal_group();

   $(".cal_btn li a").click(function() {
		var index = $(this).parent().index();
		var calendarZone = $(this).parents(".cal_group");
		var marginLeft = parseInt($(this).parents(".cal_btn").next("ul").css("margin-left"));

		if (calendarZone.hasClass("days_group")) { // 일일
			if (index) { // 다음
				if ($(".days .next:last").hasClass("last")) return false;
				 $(".days").animate({"margin-left" : (marginLeft - 175) +"px"},function() {nextDay();});
			} else { // 이전
				 $(".days").animate({"margin-left" : (marginLeft + 175) +"px"},function() {prevDay();});
			}
		} else if (calendarZone.hasClass("week_group")) { // 주간
			if (index) { // 다음
				if ($(".week .next:last").hasClass("last")) return false;
				$(".week").animate({"margin-left" : (marginLeft - 165) +"px"},function() {nextWeek();});
			} else { // 이전
				$(".week").animate({"margin-left" : (marginLeft + 165) +"px"},function() {prevWeek();});
			}
		} else { // 월간
			if (index) { // 다음
				if ($(".months .next:last").hasClass("last")) return false;
				$(".months").animate({"margin-left" :  marginLeft - 175 +"px"},function() {nextMonth();});
			} else { // 이전
				$(".months").animate({"margin-left" : marginLeft + 175 +"px"},function() {prevMonth();});
			}
		}
		return false;
   });

     // 일일 이전
	function prevDay() {
		$(".calendar_nav li").eq(1).addClass("on").siblings().removeClass("on");
		$(".days .first").removeClass("first").prev().addClass("view first"); 
		$(".days .last").removeClass("view last").prev().addClass("last");
		var _firstDay = parseInt($(".days li:first").attr("id"));
		var _prevDay = getAgoDate(0, 0, -1, _firstDay);
		var _day = doubleDigit(sliceDay(_prevDay)[2]);
		var _week = getDay(_prevDay);
		var html = '<li id="'+_prevDay+'_day"><span class="day_box"><span class="day">'+_week+'</span>'+_day+'<span class="successColor colorCircle01">정상</span></span></li>';
		$(".days li:first").before(html);
		dayZone();
		cal_group();
   }

	// 일일 다음
   function nextDay() {
		$(".days .first").removeClass("view first").next().addClass("first"); 
		$(".days .last").removeClass("last").next().addClass("view last"); 
		var $current = $("#"+window.currentDay+"_day");
		if (($current.index() == ($(".days .last").index() - 3)) && $current.hasClass("current")) $(".calendar_nav li").eq(0).addClass("on").siblings().removeClass("on");
		dayZone();
   }

   // 주간 이전
	function prevWeek() {
		$(".week .first").removeClass("first").prev().addClass("view first"); 
		$(".week .last").removeClass("view last").prev().addClass("last");
		var _prevStartDay = parseInt($(".week li:first").attr("id"));
		_prevStartDay = getAgoDate(0, 0, -7, _prevStartDay);
		var startDay = getAgoDate(0, 0, -getDay02(_prevStartDay), _prevStartDay); 
		var endDay = getAgoDate(0, 0, 6-getDay02(_prevStartDay), _prevStartDay); 

		var _weekNum = getSecofWeek(endDay); // 달에 몇번째 주인지...
		var _weekYear = sliceDay(endDay)[0]; 
		var _weekMonth = sliceDay(endDay)[1];
		var _monthStartNum = getDay02(_weekYear +''+ doubleDigit(_weekMonth) + '01');

		var _PweekYear = sliceDay(startDay)[0]; 
		var _PweekMonth = sliceDay(startDay)[1];
		var _PmonthStartNum = getDay02(_PweekYear +''+ doubleDigit(_PweekMonth) + '01');
		if (_monthStartNum > 3) { 
			_weekMonth = sliceDay(startDay)[1];
			_weekNum = getSecofWeek(startDay);
			if (_PmonthStartNum > 3) _weekNum--;
		}
		var html = '<li id="'+startDay +"_"+endDay+'_week"><span class="day_box"><span class="weekmonth">'+doubleDigit(_weekMonth)+'</span>'+_weekNum+'<span class="weektxt">Week</span><span class="successColor colorCircle01">정상</span></span></li>';
		$(".week li:first").before(html);
		dayZone();
		cal_group();
	}

	// 주간 다음
	function nextWeek() {
		$(".week .first").removeClass("view first").next().addClass("first"); 
		$(".week .last").removeClass("last").next().addClass("view last"); 
		dayZone();
	}

	// 월간 이전
   function prevMonth() {
	   $(".months .first").removeClass("first").prev().addClass("view first"); 
		$(".months .last").removeClass("view last").prev().addClass("last");
		var _firstMonth = parseInt($(".months li:first").attr("id"));
		var _prevMonth = getAgoDate(0, -1, 0, _firstMonth + "01");
		var _year = sliceDay(_prevMonth)[0];
		var _month = sliceDay(_prevMonth)[1];
		_month = doubleDigit(_month);
		var html = '<li id="'+_year+_month+'_month"><span class="day_box"><span class="day">'+_year+'</span>'+_month+'<span class="successColor colorCircle01">정상</span></span></li>';
		$(".months li:first").before(html);
		dayZone();
		cal_group();
   }

	// 월간 다음
   function nextMonth() {
	   $(".months .first").removeClass("view first").next().addClass("first"); 
		$(".months .last").removeClass("last").next().addClass("view last"); 
		dayZone();
	}
}

function cal_group() {
	$(".cal_group ul:odd").find("li").unbind("click");
	$(".cal_group ul:odd").find("li").click(function() {
		var parent = $(this).parents(".cal_group");
		if ($(this).hasClass("view") && !$(this).hasClass("next") || $(this).attr("id") == $(".next:first", parent).attr("id")) {
			$(this).addClass("current").siblings().removeClass("current");
		}
		if ($(this).parent(".days").size() > 0) $(".calendar_nav li").eq(1).addClass("on").siblings().removeClass("on");
		var $current = $("#"+window.currentDay+"_day");
		if (($(this).parent().hasClass("days") && $current.index() == ($(".days .last").index() - 3)) && $current.hasClass("current")) {
			$(".calendar_nav li").eq(0).addClass("on").siblings().removeClass("on");
			window.setTime = window.currentTime;
			setTimeLine();
		} else {
			window.setTime = 24;
			setTimeLine();
		}
		return false;
	});
}

function doubleDigit(digit) {
	if (digit < 10) digit = "0" + digit;
	return digit;
}

function getDay(date) {
	var _date = new Date(sliceDay(date)[0], sliceDay(date)[1], sliceDay(date)[2]);
	var _day = _date.getDay()
	var _week = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
	return _week[_day];
}

function getDay02(date) {
	var _date = new Date(sliceDay(date)[0], sliceDay(date)[1] - 1, sliceDay(date)[2]);
	var _day = _date.getDay()
	return _day;
}

function getAgoDate(yyyy, mm, dd, changeDay) {
	var year = sliceDay(changeDay)[0];
	var month = sliceDay(changeDay)[1] - 1;
	var day = sliceDay(changeDay)[2];
	var resultDate = new Date(yyyy+year, month+mm, day+dd);
  
	year = resultDate.getFullYear();
	month = resultDate.getMonth() + 1;
	day = resultDate.getDate();

	if (month < 10)
		month = "0" + month;
	if (day < 10)
		day = "0" + day;

	return year + "" + month + "" + day;
}

function sliceDay(sliceDay) {
	sliceDay = String(sliceDay);
	var year = parseInt(sliceDay.substr(0,4));
	var month = parseInt(sliceDay.substr(4,2));
	var day = parseInt(sliceDay.substr(6,2));
	return [year,month,day];
}

 function getSecofWeek(date){
	var d = new Date( date.substring(0,4), parseInt(date.substring(4,6))-1, date.substring(6,8) );
	var fd = new Date( date.substring(0,4), parseInt(date.substring(4,6))-1, 1 );
	return Math.ceil((parseInt(date.substring(6,8))+fd.getDay())/7);
}

// ******************************************************
// 레이어팝업 radio
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function layerPopup(){
	$(".testList").eq(1).hide();
	$(".userSelect .designRadio, .userSelect label").bind('click',function(){
		var index = $(this).parents(".userSelect li").index();
		// alert(index);
		// $(".testList").eq(index).show().siblings(".testList").hide();
		$(this).parents(".sort").parent().find(".testList").eq(index).show().siblings(".testList").hide();
		$("select").selectbox();
	});
	$(".userSelect02 .designRadio, .userSelect02 label").bind('click',function(){
		var index = $(this).parents(".userSelect02 li").index();
		$(this).parents(".sort").parents(".lnbList").find(".testList").eq(index).show().siblings(".testList").hide();
		$("select").selectbox();
	});

}


// ******************************************************
// 트리구조형 리스트 함수
// 작성자: 아이콘인터렉티브 성재연
// ******************************************************

function menuTree(){
	if($(".group_list").hasClass("group_list02")){ // 핵심어관리 트리
		$(".tree_btn").click(function(){
			$(this).next().next().next(".team_list").slideToggle().parents("li").toggleClass("on");
			return false;
		});
		$(".group_list02 a").click(function(){
			if($(this).parent("div").attr("class")=="groupList"){
				$(this).parents("li").addClass("click").find("li").removeClass("click02").parents("li").siblings("li").removeClass("click").find("li").removeClass("click02");
			}else{
				$(this).parent(".teamList").parent("li").addClass("click02").siblings("li").removeClass("click02").parents("li").addClass("click").siblings("li").removeClass("click").find("li").removeClass("click02"); 
			}
			return false;
		});
	}if($(".group_list").hasClass("group_list01")){ // 통계 (토픽)
		$(".tree_btn").click(function(){
			$(this).next().next().next("ul").slideToggle().parents("li").toggleClass("on");
		});
		$(".group_list01 li a").click(function(){
			if($(this).parent("li").parent("ul").hasClass("group_list")){ //대분류
				$(this).parent("li").addClass("click").siblings("li").removeClass("click");
				return false;
			}else {
				// $(this).parent("li").addClass("click02");
				return false;
			} //소분류
		});
	}if($(".group_list").hasClass("group_list05")){ // 통계(그룹/팀)
		$(".tree_btn").click(function(){
			$(this).next().next().next("ul").slideToggle().parents("li").toggleClass("on");
		});
		$(".group_list05 li a").click(function(){
			if($(this).parent("li").parent("ul").hasClass("group_list")){ //대분류
				$(this).parent("li").addClass("click").siblings("li").removeClass("click");
				return false;
			}else {
				// $(this).parent("li").addClass("click02");
				return false;
			} //소분류
		});
	}if($(".group_list").hasClass("group_list03")){ // 통게 (핵심어)
		$(".group_list li a").click(function(){
			$(this).parent("li").addClass("click").siblings("li").removeClass("click");
			return false;
		});
	}if($(".group_list").hasClass("group_list04")){ //통계 (수집및분석/통화내역평가)
		$(".group_list li a").click(function(){
			$(this).parent("li").addClass("click").siblings("li").removeClass("click");
			if ($(this).parents(".group_list").hasClass("group_listAnalysis")) getAnalysis_Graph($(this).text(), $(this).parent().index()); // 수집 및 분석 클릭시 함수 호출
			return false;
		});
	}else{ // 핵심어관리 트리&통계 를 제외한 트리
		$(".tree_btn").click(function(){
			$(this).next().next("ul").slideToggle().parents("li").toggleClass("on");
			return false;
		});
		$(".group_list li a").click(function(){
			if($(this).parents(".group_list").hasClass("group_list01") || $(this).parents(".group_list").hasClass("group_list02") || $(this).parents(".group_list").hasClass("group_list03")){return false;}
			else{
				if($(this).parent("li").parent("ul").hasClass("group_list")){ //대분류
						$(this).parent("li").addClass("click").find("li").removeClass("click02").parents("li").siblings("li").removeClass("click").find("li").removeClass("click02");
				}else { // 소분류
					$(this).parent("li").addClass("click02").siblings("li").removeClass("click02").parents("li").addClass("click").siblings("li").removeClass("click").find("li").removeClass("click02"); 
				}
				return false;
			}
		});
	}
}