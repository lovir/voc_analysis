<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="<c:url value='/resources/js/voc/dashBoard.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jQCloud.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.tagcanvas.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jQCloud.css'/>" />

<link rel="stylesheet" href="<c:url value='/resources/css/player/content.css'/>" />
<link rel="stylesheet" href="<c:url value='/resources/css/player/jquery.selectbox.css'/>" />
<link rel="stylesheet" href="<c:url value='/resources/css/player/jquery.mCustomScrollbar.css'/>" />
<script type="text/javascript" src="<c:url value='/resources/js/player/plugins/jquery.selectbox-0.6.1.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/player/plugins/jquery.mCustomScrollbar.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/Base.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/pager.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/voc/common.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>

<style>
div.rank_kw{
    display: inline-block;
    width: 110px;
    padding: 0 8px;
}
#mouse_edit{
 cursor: default;
}
#mouse_edit2{
 cursor: default;
}
#mouse_edit3{
 cursor: default;
}
#mouse_edit4{
 cursor: default;
}
</style>
<script type="text/javascript">

var words = <c:out value='${issue.word}' escapeXml="false"/>;

function getContextPath() {
	return "<c:out value="${pageContext.request.contextPath}" />";
}

function getImgPath(){
	return "<c:url value='/resources/images/common/'/>";
}

$(function () {
	
	$("#close1").click(function(){
		$('.map_pop1').hide();
	});
	
	$("#close2").click(function(){
		$('.map_pop2').hide();
	});
	
	$("#close3").click(function(){
		$('.map_pop3').hide();
	});
	
	$("#close4").click(function(){
		$('.map_pop4').hide();
	});
	
	$("#close5").click(function(){
		$('.map_pop5').hide();
	});
	
	$("#close6").click(function(){
		$('.map_pop6').hide();
	});
	
	$("#close7").click(function(){
		$('.map_pop7').hide();
	});
	
	$("#close8").click(function(){
		$('.map_pop8').hide();
	});
	
	$("#close9").click(function(){
		$('.map_pop9').hide();
	});
	
	$("#close10").click(function(){
		$('.map_pop10').hide();
	});
	
	$("#close11").click(function(){
		$('.map_pop11').hide();
	});

	$("#close12").click(function(){
		$('.map_pop12').hide();
	});

	$("#close13").click(function(){
		$('.map_pop13').hide();
	});

	$("#close14").click(function(){
		$('.map_pop14').hide();
	});

	$("#close15").click(function(){
		$('.map_pop15').hide();
	});

	$("#close16").click(function(){
		$('.map_pop16').hide();
	});

	$("#close17").click(function(){
		$('.map_pop17').hide();
	});

	$("#close18").click(function(){
		$('.map_pop18').hide();
	});

	$("#close19").click(function(){
		$('.map_pop19').hide();
	});

	$("#close20").click(function(){
		$('.map_pop20').hide();
	});

	$("#close21").click(function(){
		$('.map_pop21').hide();
	});

	$("#close22").click(function(){
		$('.map_pop22').hide();
	});

	$("#close23").click(function(){
		$('.map_pop23').hide();
	});

	$("#close24").click(function(){
		$('.map_pop24').hide();
	});

	$("#close25").click(function(){
		$('.map_pop25').hide();
	});
	
	var condition = '${condition}';
	if(condition == "" || condition == "WEEK") condition = "당주";
	else condition = "당월";
//긍/부정 VOC 증감현황 차트
<c:forEach var="result" items="${IncreAnDecre}" varStatus="status">
	$('#d_keyword${status.index}').html("("+condition+":<c:out value="${result.current}" escapeXml="false"/>건)");
	$('#needs${status.index}').highcharts({
		chart: {
			type: 'pie',
			plotBackgroundColor: null,
			plotBorderWidth: null,
			plotShadow: false,
			margin:[0, 0, 0, 0],
			spacingTop: 0,
			spacingBottom: 0,
			spacingLeft: 0,
			spacingRight:0,
			events: {
				load: function (){
					var ren = this.renderer;
					<c:choose>
						<c:when test="${result.count > 0}">
							ren.image('<c:url value="/resources/images/common/arrow_up.png"/>', 23, 43, 16, 17).add();
						</c:when>
						<c:when test="${result.count == 0}">
						</c:when>
						<c:otherwise>
						ren.image('<c:url value="/resources/images/common/arrow_down.png"/>', 23, 43, 16, 17).add();
						</c:otherwise>
					</c:choose>
				}
			}
		},
		title: {
			text: null
		},
		subtitle: {
			text: null
		},
		credits:{
			enabled:false
		},
		legend:{
			enabled:false
		},
		exporting: {
			enabled:false
		},
		plotOptions : {
			pie: {
				shadow: false,
				size: '105%',
				allowPointSelect: true,
				borderWidth: 0,
				cursor: 'pointer',
				dataLabels: {
					enabled: false
				},
				innerSize: '75%'
			},
			series: {
				states: {
					hover: {
						enabled: false
					}
				}
			}
		},
		labels: {
			style: {
				color: '#000',
				fontSize: '14px',
				fontFamily: 'arial'
			},
			items: [
					{
						html: '<c:out value="${result.percentage}" escapeXml="false"/>%',
						style: {
							left: '40px',
							fontSize: '13px',
							top: '45px'
						}
					},
					{
						html: '<fmt:formatNumber><c:out value="${result.count}" escapeXml="false"/></fmt:formatNumber>건',
						style: {
							left: '40px',
							top: '60px',
							fontSize: '11px',
							display:'block'
						}
					}
				]
		},
		lang: {
			noData: ''
		},
		tooltip: {
			style: {
				fontSize: '10px'
			},
			formatter : function(){
				if(this.point.y==99999999){
					return '당월/전월:0건';
				}else{
					
					return this.point.name+':'+AddComma(this.point.y)+'건';
				}
			}
		},
		series: [{
			name: '발생건수',
			data: <c:out value="${result.data}" escapeXml="false"/>,
			dataLabels: {
				enabled: false
			}
		}]
	});
</c:forEach>

// VOE는 패스
<c:forEach var="result" items="${lcls}" varStatus="status">
$('#l_keyword${status.index}').html("(당월:<c:out value="${result.current}" escapeXml="false"/>건)");
$('#lcls${status.index}').highcharts({
	chart: {
		type: 'pie',
		plotBackgroundColor: null,
		plotBorderWidth: null,
		plotShadow: false,
		margin:[0, 0, 0, 0],
		spacingTop: 0,
		spacingBottom: 0,
		spacingLeft: 0,
		spacingRight:0,
		events: {
			load: function (){
				var ren = this.renderer;
				<c:choose>
					<c:when test="${result.count > 0}">
						ren.image('<c:url value="/resources/images/common/arrow_up.png"/>', 23, 43, 16, 17).add();
					</c:when>
					<c:when test="${result.count == 0}">
					</c:when>
					<c:otherwise>
					ren.image('<c:url value="/resources/images/common/arrow_down.png"/>', 23, 43, 16, 17).add();
					</c:otherwise>
				</c:choose>
			}
		}
	},
	title: {
		text: null
	},
	subtitle: {
		text: null
	},
	credits:{
		enabled:false
	},
	legend:{
		enabled:false
	},
	exporting: {
		enabled:false
	},
	plotOptions : {
		pie: {
			shadow: false,
			size: '120%',
			allowPointSelect: true,
			borderWidth: 0,
			cursor: 'pointer',
			dataLabels: {
				enabled: false
			},
			innerSize: '118%'
		},
		series: {
			states: {
				hover: {
					enabled: false
				}
			}
		}
	},
	labels: {
		style: {
			color: '#000',
			fontSize: '14px',
			fontFamily: 'arial'
		},
		/* items: [
			{
				html: '<c:out value="${result.percentage}" escapeXml="false"/>%',
				style: {
					left: '42px',
					fontSize: '13px',
					top: '35px'
				}
			},
			{
				html: '<c:out value="${result.count}" escapeXml="false"/>건',
				style: {
					left: '45px',
					top: '49px',
					fontSize: '11px',
					display:'block'
				}
			},
			{
				html: '('+condition+':<c:out value="${result.current}" escapeXml="false"/>건)',
				style: {
					left: '28px',
					top: '60px',
					fontSize: '10px',
					display:'block'
				}
			}
		] */
		items: [
				{
					html: '<c:out value="${result.percentage}" escapeXml="false"/>%',
					style: {
						left: '40px',
						fontSize: '13px',
						top: '45px'
					}
				},
				{
					html: '<fmt:formatNumber><c:out value="${result.count}" escapeXml="false"/> </fmt:formatNumber>건',
					style: {
						left: '40px',
						top: '60px',
						fontSize: '11px',
						display:'block'
					}
				}
			]
	},
	lang: {
		noData: ''
	},
	tooltip: {
		style: {
			fontSize: '10px'
		},
		formatter : function(){
			if(this.point.y==99999999){
				return '당월/전월:0건';
			}else{
				return this.point.name+':'+this.point.y+'건';
			}
		}
	},
	series: [{
		name: '발생건수',
		data: <c:out value="${result.data}" escapeXml="false"/>,
		dataLabels: {
			enabled: false
		}
	}]
});
</c:forEach>

//VOC 관심 키워드 동향 분석
 $('#interest').highcharts({ 
	chart: {
		type: 'line'
	},
	title: {
		text: null
	},
	subtitle: {
		text: null
	},
	credits:{
		enabled:false
	},
	xAxis: {
		categories: <c:out value="${interest.categories}" escapeXml="false"/>,
		labels: {
            style: {
                fontSize:'9px'
            }
        }
	},
	yAxis: {
		title: {
			text: '빈도수'
		},
		min:0
	},
	series: <c:out value="${interest.series}" escapeXml="false"/>
}); 

//VOC 음성분석 추이
 $('#voice').highcharts({ 
	chart: {
		type: 'line'
	},
	title: {
		text: null
	},
	subtitle: {
		text: null
	},
	credits:{
		enabled:false
	},
	xAxis: {
		categories: <c:out value="${voice.categories}" escapeXml="false"/>,
		labels: {
            style: {
                fontSize:'9px'
            }
        }
	},
	yAxis: {
		title: {
			text: '빈도수'
		},
		min:0
	},
	series: <c:out value="${voice.series}" escapeXml="false"/>
}); 
//VOC 불만 키워드 동향 분석
/*  $('#top10Keyword').highcharts({ 
	chart: {
		type: 'bar'
	},
	title: {
		text: null
	},
	subtitle: {
		text: null
	},
	credits:{
		enabled:false
	},
	legend:{
		enabled:false
	},
 	xAxis: {
		categories: <c:out value="${complainKeyword.categories}" escapeXml="false"/>
	}, 
	yAxis: {
		title: {
			text: null
		},
		min:0
	},
	plotOptions: {
		series: {
			pointWidth: 10
		}
	},
	tooltip: {
		style: {
			fontSize: '12px'
		},
		formatter : function(){
			return '금주 키워드: '+this.point.y+'건';
		}
	},
	series: <c:out value="${complainKeyword.series}" escapeXml="false"/>
}); */

//VOC 우수고객 이슈 분석
/* $('#vip').highcharts({ 
	chart: {
		type: 'spline'
	},
	title: {
		text: null
	},
	subtitle: {
		text: null
	},
	credits:{
		enabled:false
	},
	xAxis: {
		type: 'datetime',
		labels: {
			formatter: function() {
				var month = parseInt(Highcharts.dateFormat('%m', this.value));
				var day = Highcharts.dateFormat('%e', this.value);
				return month + '월' + day + '일';
			}
		}
	},
	yAxis: {
		title: {
			text: null
		},
		min:0
	},
	plotOptions: {
		spline: {
			lineWidth: 4,
			states: {
				hover: {
					lineWidth: 5
				}
			},
			marker: {
				enabled: false
			},
			pointInterval: 24 * 3600 * 1000, // one day
			pointStart: Date.UTC(<c:out value="${vip.pointStartYear}" escapeXml="false"/>, 
					<c:out value="${vip.pointStartMonth-1}" escapeXml="false"/>, 
					<c:out value="${vip.pointStartDay}" escapeXml="false"/>, 0, 0, 0)
		}
	},
	navigation: {
		menuItemStyle: {
			fontSize: '10px'
		}
	},
	series: <c:out value="${vip.series}" escapeXml="false"/>
});

var vipChart = $('#vip').highcharts(); */
/* var series = vipChart.series[0];

if(series != undefined){
	if(series.visible){
		$(vipChart.series).each(function(index){
			if(index != 0) this.setVisible(false, false);
		})
		vipChart.redraw();
	}
} */

//이슈 클라우드
if($('#canvasBtn').hasClass("on")){
	$('#tags').append('<canvas id="issueCanvas" style="width:100%;height:100%;"></canvas>');
	
	TagCanvas.Start('issueCanvas', 'jcloud-tags',{
		textFont : "NanumGothicBold",
		textColour: null,
		textHeight: 10,
		outlineThickness: 1,
		weight: true,
		weightFrom: "data-weight",
		weightMode: "both",
		reverse: true,
		depth: 0.8,
		maxSpeed: 0.05,
		minBrightness: 0.1,
		shadowBlur: 0,
		shape: "vcylinder",
		lock: "y",
		wheelZoom: false,
		active: true,
		freezeActive: true
	});
	
}else{
	$('#tags').jQCloud(words, {
		height: 240,
		shape: 'elliptic'
	});
}

});

	function AddComma(data_value) {

		 

	    var txtNumber = '' + data_value;    // 입력된 값을 문자열 변수에 저장합니다.

	 

	    if (isNaN(txtNumber) || txtNumber == "") {    // 숫자 형태의 값이 정상적으로 입력되었는지 확인합니다.
	        alert("숫자만 입력 하세요");
	        return;
	    }


	    else {
	        var rxSplit = new RegExp('([0-9])([0-9][0-9][0-9][,.])');    // 정규식 형태 생성
	        var arrNumber = txtNumber.split('.');    // 입력받은 숫자를 . 기준으로 나눔. (정수부와 소수부분으로 분리)
	        arrNumber[0] += '.'; // 정수부 끝에 소수점 추가

	 

	        do {
	            arrNumber[0] = arrNumber[0].replace(rxSplit, '$1,$2'); // 정수부에서 rxSplit 패턴과 일치하는 부분을 찾아 replace 처리
	        } while (rxSplit.test(arrNumber[0])); // 정규식 패턴 rxSplit 가 정수부 내에 있는지 확인하고 있다면 true 반환. 루프 반복.

	 

	        if (arrNumber.length > 1) { // txtNumber를 마침표(.)로 분리한 부분이 2개 이상이라면 (즉 소수점 부분도 있다면)
	            return arrNumber.join(''); // 배열을 그대로 합칩. (join 함수에 인자가 있으면 인자를 구분값으로 두고 합침)
	        }
	        else { // txtNumber 길이가 1이라면 정수부만 있다는 의미.
	            return arrNumber[0].split('.')[0]; // 위에서 정수부 끝에 붙여준 마침표(.)를 그대로 제거함.
	        }
	    }
	}

	
	
	// 이슈 call text 상세보기
/* 	function detailView(id){
		$.ajax({
			type : "post",     
			url : getContextPath() + "/audio/detailView.do",
			data : {
				audio_id: id
			},
			success : function(data) {
	             $("#basic-modal-voice_info").html(data);
	             $("#basic-modal-voice_info").modal({
	 				persist: false,
	 				focus: false,
	 				onClose: function () {
	 					   $('body').css('overflow','auto');
	 					   $.modal.close();
	 					}
	 			});
	 			$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
	             // loadPlayer();
			},
			error : function(request, status, error) {
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	} */
</script>

<!-- location start -->
<div class="loc_wrap clear2">
	<ul>
	<li>홈</li>
	<li>대시보드</li>
	</ul>
</div>
<!--// location end -->

<!-- 본문 start -->
	<div class="cont_body" id="cont_body">
		
			<div class="cont_head">
				<span class="cont_tit">대시보드</span><span class="cont_desc">음성인식 및 VOC분석 형태를 제공 합니다.</span>
			</div>


			<div class="top_graph">
				<div class="align_r mb_10 mt_-30">
					<form id="searchForm" name="searchForm" method="post">
						<input type="hidden" id="emotion" name="emotion">
						<input type="hidden" id="guname" name = "guname">
						<input type="hidden" id="startDate" name = "startDate">
						<input type="hidden" id="endDate" name ="endDate">
						<input type="hidden" id="pagesize" name="pagesize" value="10"/> 
						<input type="hidden" id="currentPage" name="currentPage" value="1"/>
						<input type="hidden" id="needsType" name="needsType">
						<input type="hidden">
						<fieldset><legend>조건선택</legend>
                           
                           	<input type="hidden" id="collectionCkeck" value='${collectionCkeck}'>
							<!-- <select title="조건선택" class="mr_10" id="collection" name="collection"> -->
							<!-- <option>전체</option> -->
							<%-- <option id = "vocsc" name = "voc" value="VOC_SEOUL" ${collection == 'VOC' ? 'selected' : ''}>VOC</option>
                               <option id = "sns" name = "sns" value="SNS_SEOUL" ${collection == 'SNS' ? 'selected' : ''}>소셜</option>
							</select> --%>
                               
                            <input type="hidden" id="conditionCheck" value='${conditionCheck}'>
							<!-- <input type="hidden" id="condition" value="MONTH"> -->
                    		<%-- <select title="조건선택" id="condition" name="condition">
								<option id="month" name="month" value="MONTH" ${condition == 'MONTH' ? 'selected' : ''}>전월대비</option>
				  			</select>
							<a href="#" class="btn b_orange ssmall" id="btnSearch">확인</a>   --%>
						</fieldset>
					</form>
				</div>
                <!--음성인식 추이 start-->
                <div class="win win_half left" style="margin-top:0; margin-bottom:15px !important;">
                      <div class="win_head clear2 align_l">
                            <span class="win_tit bg_none">음성인식</span>
                            <ul class="win_btnset">
                            <li class="right_help">
                              <!--   <span class="t_white">음성인식 현황 : 00/00</span> -->

                            </li>
                            
                            </ul>
                      </div>
                        <div class="win_contarea align_c">
                            <div class="graph_in" style="height:255px;" id="voice"></div>
                        </div>
                </div>                  
                <!--//음성인식 추이 end-->
                <!-- 관심 키워드 추이 start -->
				<div class="win win_half right" style="margin-top:0; margin-bottom:15px !important;">
				  <div class="win_head clear2 align_l">
						<span class="win_tit bg_none">관심 키워드 추이</span>
						<ul class="win_btnset">
						<li class="right_help">
							<a href="#" class="icon_help" id="mouse_edit2"></a>
							<div class="tooltip btn_qty1">개선과제 대분류 유형에 대한 최근1주일간의 점유율을 파이차트 형태로 확인할 수 있습니다.<div class="tip-arrow"></div></div>
						</li>
						
						</ul>
				  </div>
					<div class="win_contarea align_c">
					    <div class="graph_in" style="height:265px;" id="interest"></div>
					</div>
				</div>
				<!--// 관심 키워드 추이 end -->        
                
		<div class="clear2">
		<div class="left_areas">
		<!--감성분석 start-->	
		<div class="top_donut h_200 left clear2">
			<p class="head_bg">감성 분석(긍&middot;부정)</p>
			<ul>
				<li>
					<span class="d_keyword">긍정</span>
					<div class="donut_back" id="needs0"></div>
					<div class="d_keyword2" id="d_keyword0"></div>
				</li>
				<li>
					<span class="d_keyword">중립</span>
					<div class="donut_back" id="needs1"></div>
					<div class="d_keyword2" id="d_keyword1"></div>
				</li>
				<li>
					<span class="d_keyword">부정</span>
					<div class="donut_back" id="needs2"></div>
					<div class="d_keyword2" id="d_keyword2"></div>
				</li>
			</ul>
		</div>
		<!--// 감성분석 end-->
		<!--  이슈 클라우드 start -->
		<div class="win win_full" style="margin-top:15px !important;">
			<div class="win_head clear2 align_l">
				<span class="win_tit bg_none">이슈 클라우드</span>
				<ul class="win_btnset">
				<li class="right_help">
					<a href="#" class="icon_help" id="mouse_edit4"></a>
					<div class="tooltip btn_qty1">해당 기간 동안 민원요청의 키워드를 이슈 클라우드 형식으로 확인 할 수 있다.<div class="tip-arrow"></div></div>
				</li>
				
				</ul>
			</div>
			<div class="win_contarea align_c">
				<div class="graph_in" style="height:285px;">
                                <div class="cloud_opt clear2">
                                    <ul class="cloud_left clear2">
                                        <li><a href="#" class="btn b_gray small on" id="cloudBtn">텍스트 타입</a></li>
                                        <li><a href="#" class="btn b_gray small" id="canvasBtn">3D 타입</a></li>
                                    </ul>
                                    <ul class="cloud_right clear2">
							<c:forEach var="result" items="${issue.characterTypeList}" varStatus="status"> <!-- 성격유형 -->
								<li>
									<a href="#" class="cloud_check type0<c:out value='${status.count}'/>" title='<c:out value="${result.cdKnm}"/> 선택'></a>
									<label for="cloud_01"><c:out value="${result.cdTp}"/></label>
								</li>
							</c:forEach>
                                    </ul>
                                </div>
                                <ul id="jcloud-tags" style="display: none">
						<c:forEach var="result" items="${issue.characterTypeList}" varStatus="status">
							<c:set var="lcls" value="${result.cdKnm}" />
								<c:forEach var="issue" items="${issue[lcls]}" varStatus="status">
									<li style="color: <c:out value='${issue.style}'/>; font-family: 'NanumGothicBold';">
										<a style="color: <c:out value='${issue.style}'/>; font-family: 'NanumGothicBold';" href="javascript:;" onclick="fnDetailItem('<c:out value="${issue.text}"/>');"><c:out value="${issue.text}"/></a>
									</li>
								</c:forEach>
						</c:forEach>
					</ul>
                                
                    <div class="mt_10" style="height: 560px" id="tags">
                    </div>
                </div>
			</div>
		</div>
		<!--//  이슈 클라우드 end -->
		</div>

						<div class="right_areas">
                          <div class="top_donut right clear2">
                          	<!--서울시 지역별 현황-->
                            <div class="map_area">
                              <div class="map_info" style="margin-left:80px;">
                                <p>서울시 민원발생 현황</p>
                                <ul style="margin-left:30px;">
                                    <li><img src="../resources/images/common/s_gray.png" alt="총계"> 민원총계: ${totalMinwon}건</li>
                                    <li><img src="../resources/images/common/s_blue.png" alt="긍정"> 긍정민원: ${totalPosi}건</li>
                                    <li><img src="../resources/images/common/s_orange.png" alt="부정"> 부정민원: ${totalNega}건</li>
                                    <li><img src="../resources/images/common/s_green.png" alt="중립"> 중립민원: ${totalNetu}건</li>
                                </ul>
                              </div>
                                
                              <c:forEach var="countrys" items="${countrys}" varStatus="countrysNum">
									<c:choose>
										<c:when test="${countrysNum.count == 1}">
										
                              <!--은평구 -->
                              <div class="map_a1"> 
                                   <a href="#" class="map_g1">
                                   <ul class="map_graph" >
                                        <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>   
                                   </ul>
                                   </a>
                              </div>
                              <!--//은평구 -->
                                	<!--은평구 popup-->
                                    <div class="map_pop1">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close1" title="닫기">X</a></span> </p>
                                   
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 은평구 popup-->
                                </c:when>
								<c:when test="${countrysNum.count == 2}">
                                <!--서대문구 -->
                                <div class="map_a2"> 
                                   <a href="#" class="map_g2">
                                   <ul class="map_graph" >
                                        <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>   
                                   </ul>
                                   </a>
                              	</div>
                                <!--//서대문구 -->
                                <!--서대문 popup-->
                               <div class="map_pop2">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close2" title="닫기">X</a></span> </p>
                                   
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                             
                                </div>
                                <!--// 서대문 popup-->
                                </c:when>
								<c:when test="${countrysNum.count == 3}">
                                <!--종로구 -->
                                    <div class="map_a3"> 
                                       <a href="#" class="map_g3">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//종로구 -->
                               	<!--종로구 popup-->
                                   <div class="map_pop3">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close3" title="닫기">X</a></span> </p>
                                   
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                             
                                </div>
                                <!--// 종로구 popup-->
                                 </c:when>
								<c:when test="${countrysNum.count == 4}">
                                <!--성북구 -->
                                <div class="map_a4"> 
                                       <a href="#" class="map_g4">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//성북구 -->
                                	<!--성북구 popup-->
                                    <div class="map_pop4">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close4" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                          
                                </div>
                                <!--// 성북구 popup-->    
                                 </c:when>                                
                            	<c:when test="${countrysNum.count == 5}">
                                 <!--강북구 -->
                                    <div class="map_a5"> 
                                       <a href="#" class="map_g5">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//강북구 -->
                                	<!--강북구 popup-->
                                    <div class="map_pop5">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close5" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 강북구 popup-->    
                                 </c:when>
                                <c:when test="${countrysNum.count == 6}">
                                <!--도봉구 -->
                                    <div class="map_a6"> 
                                       <a href="#" class="map_g6">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//도봉구 -->
                                	<!--동봉구 popup-->
                                    <div class="map_pop6">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close6" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 도봉구 popup--> 
                                 </c:when>   
                                <c:when test="${countrysNum.count == 7}">
                                <!--노원구 -->
                                   <div class="map_a7"> 
                                       <a href="#" class="map_g7">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//노원구 -->
                                	<!--노원구 popup-->
                                    <div class="map_pop7">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close7" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 노원구 popup-->
                                 </c:when>
                                <c:when test="${countrysNum.count == 8}">
                                <!--중랑구 -->
                                    <div class="map_a8"> 
                                       <a href="#" class="map_g8">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//중랑구 -->
                                	<!--중랑구 popup-->
                                    <div class="map_pop8">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close8" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                             
                                </div>
                                <!--// 중랑구 popup--> 
                                 </c:when>
                                <c:when test="${countrysNum.count == 9}">
                                <!--동대문구 -->
                                    <div class="map_a9"> 
                                       <a href="#" class="map_g9">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//동대문구 -->
                                	<!--동대문구 popup-->
                                    <div class="map_pop9">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close9" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                              
                                </div>
                                <!--// 동대문구 popup-->
                                 </c:when>
                                <c:when test="${countrysNum.count == 10}">
                                <!--중구 -->
                                    <div class="map_a10"> 
                                       <a href="#" class="map_g10">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//중구 -->
                                	<!--중구 popup-->
                                    <div class="map_pop10">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close10" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 중구 popup--> 
                                 </c:when>
                                <c:when test="${countrysNum.count == 11}">
                                <!--성동구 -->
                                    <div class="map_a11"> 
                                       <a href="#" class="map_g11">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//성동구 -->
                                	<!--성동구 popup-->
                                    <div class="map_pop11">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close11" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 성동구 popup-->       
                                 </c:when>                         
                                <c:when test="${countrysNum.count == 12}">
                                <!--광진구 -->
                                    <div class="map_a12"> 
                                       <a href="#" class="map_g12">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//광진구 -->
                                	<!--광진구 popup-->
                                    <div class="map_pop12">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close12" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                  
                                </div>
                                <!--// 광진구 popup--> 
                                 </c:when>    
                                <c:when test="${countrysNum.count == 13}">
                                <!--강동구 -->
                                    <div class="map_a13"> 
                                       <a href="#" class="map_g13">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//강동구 -->
                                	<!--강동구 popup-->
                                    <div class="map_pop13">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close13" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 강동구 popup-->     
                                 </c:when>
                                <c:when test="${countrysNum.count == 14}">
                                <!--마포구 -->
                                    <div class="map_a14"> 
                                       <a href="#" class="map_g14">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//마포구 -->
                                	<!--마포구 popup-->
                                    <div class="map_pop14">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close14" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 마포구 popup-->
                                 </c:when>
                                <c:when test="${countrysNum.count == 15}">
								<!--용산구 -->
                                    <div class="map_a15"> 
                                       <a href="#" class="map_g15">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//용산구 -->
                                	<!--용산구 popup-->
                                    <div class="map_pop15">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close15" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 용산구 popup-->   
                                 </c:when>
                                <c:when test="${countrysNum.count == 16}">
								<!--강서구 -->
                                    <div class="map_a16"> 
                                       <a href="#" class="map_g16">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//강서구 -->
                                	<!--강서구 popup-->
                                    <div class="map_pop16">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close16" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 강서구 popup--> 
                                 </c:when>
                                <c:when test="${countrysNum.count == 17}">
								<!--양천구 -->
                                    <div class="map_a17"> 
                                       <a href="#" class="map_g17">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//양천구 -->
                                	<!--양천구 popup-->
                                    <div class="map_pop17">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close17" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                               
                                </div>
                                <!--// 양천구 popup--> 
                                 </c:when>
                                <c:when test="${countrysNum.count == 18}">
                                <!--구로구 -->
                                    <div class="map_a18"> 
                                       <a href="#" class="map_g18">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//구로구 -->
                                	<!--구로구 popup-->
                                    <div class="map_pop18">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close18" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                 
                                </div>
                                <!--// 구로구 popup-->
                                 </c:when>
                                <c:when test="${countrysNum.count == 19}">
                                <!--금천구 -->
                                    <div class="map_a19"> 
                                       <a href="#" class="map_g19">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//금천구 -->
                                	<!--금천구 popup-->
                                    <div class="map_pop19">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close19" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                               
                                </div>
                                <!--// 금천구 popup-->   
                                 </c:when>                             
                                <c:when test="${countrysNum.count == 20}">
                                <!--영등포구 -->
                                    <div class="map_a20"> 
                                       <a href="#" class="map_g20">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//영등포구 -->
                                	<!--영등포구 popup-->
                                    <div class="map_pop20">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close20" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 영등포구 popup-->   
                                 </c:when>
                                <c:when test="${countrysNum.count == 21}">
                                <!--관악구 -->
                                    <div class="map_a21"> 
                                       <a href="#" class="map_g21">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//관악구 -->
                                	<!--관악구 popup-->
                                    <div class="map_pop21">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close21" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                            
                                </div>
                                <!--// 관악구 popup-->  
                                 </c:when>
                                <c:when test="${countrysNum.count == 22}">
                                <!--동작구 -->
                                    <div class="map_a22"> 
                                       <a href="#" class="map_g22">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//동작구 -->
                                	<!--동작구 popup-->
                                    <div class="map_pop22">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close22" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 동작구 popup-->    
                                 </c:when>
                                 
                                <c:when test="${countrysNum.count == 23}">
                                <!--서초구 -->
                                    <div class="map_a23"> 
                                       
                                       <a href="#" class="map_g23">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//서초구 -->
                                	<!--서초구 popup-->
                                    <div class="map_pop23">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close23" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                               
                                <!--// 서초구 popup-->  
                                 </c:when>
                               	 <c:when test="${countrysNum.count == 24}">
                                    <div class="map_a24"> 
                                       <a href="#" class="map_g24">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//강남구 -->
                                	<!--강남구 popup-->
                                    <div class="map_pop24">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close24" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                              
                                </div>
                                <!--// 강남구 popup-->   
                                 </c:when>
                                <c:when test="${countrysNum.count == 25}">
                                <!--송파구 -->
                                    <div class="map_a25"> 
                                       <a href="#" class="map_g25">
                                       <ul class="map_graph" >
                                            <span class="num">${countrys.total}</span>
                                        <li class="ml_10 gray_100"></li>
                                        <li class="${countrys.positive}"></li>
                                        <li class="${countrys.netative}"></li>
                                        <li class="${countrys.neutral}"></li>    
                                       </ul>
                                        </a>
                                    </div>
                                <!--//송파구 -->
                                	<!--송파구 popup-->
                                    <div class="map_pop25">
                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}&nbsp;<a href="javascript:;" class="btn_pclose" id="close25" title="닫기">X</a></span> </p>
                                    <a href="javascript:;" onclick="regionSearch('','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                    <div class="gray_box">
                                    	<ul>
                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
                                        </ul>
                                    </div>
                                    </a>
                                     <div class="box_num">
                                    	<ul>
                                    		<a href="javascript:;" onclick="regionSearch('P','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
                                        	</a>
                                        	<a href="javascript:;" onclick="regionSearch('D','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
                                            </a>
			                                <a href="javascript:;" onclick="regionSearch('N','<c:out value="${countrys.countryName}"/>','<c:out value="${startDate}"/>','<c:out value="${endDate}"/>',1,10)">
                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
                                            </a>
                                        </ul>
                                    </div>                                
                                </div>
                                <!--// 송파구 popup-->   
								</c:when>
									</c:choose>
								</c:forEach>                                                                                                                                         
		                    </div>
		                    <!--서울시 지역별 현황-->
                 
                  </div>
                </div>
            </div>
			</div>


					<div class="clear2">
						<!-- 금주 키워드 TOP10 start -->
						<div class="win win_1 float_l mr_10" style="margin-top:10px !important;">
							<div class="win_head clear2">
								<span class="win_tit bg_none">금주 키워드 TOP10</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help" id="mouse_edit"></a>
									<div class="tooltip btn_qty1">금주 키워드 Top 10을 확인할 수 있스니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
							  <div class="graph_in" style="height:280px;"> <!-- 기존의 높이 : 280px 325-->
                               	<div class="keywodrank_list">
										<ul>
										  <c:forEach var="top10Keyword" items="${top10Keywordlist}" varStatus="topnum">
										    <c:choose>
										      <c:when test="${topnum.count == 1}">
										      	<c:choose>
										  			<c:when test="${fn:length(top10Keyword.keyword)>7}">
										  				<li class="no_1"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')"><c:out value="${fn:substring(top10Keyword.keyword,0,6)}"/>...</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>
										  			</c:when>
										  			<c:otherwise>										  		
										      			<li class="no_1"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')">${top10Keyword.keyword}</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>
										  			</c:otherwise>
										  		</c:choose>
										      </c:when>
										      <c:when test="${topnum.count == 2}">
										      	<c:choose>
										      		<c:when test="${fn:length(top10Keyword.keyword)>7}">
										      			<li class="no_2"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')"><c:out value="${fn:substring(top10Keyword.keyword,0,6)}"/>...</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>	
										      		</c:when>
										      		<c:otherwise>
										      			<li class="no_2"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')">${top10Keyword.keyword}</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>										      		
										      		</c:otherwise>
										      	</c:choose>
										      </c:when>
										      <c:when test="${topnum.count == 3}">
										      	<c:choose>
										      		<c:when test="${fn:length(top10Keyword.keyword)>7}">
										      			<li class="no_3"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')"><c:out value="${fn:substring(top10Keyword.keyword,0,6)}"/>...</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>	
										      		</c:when>
										      		<c:otherwise>										      		
										      			<li class="no_3"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')">${top10Keyword.keyword}</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>
										      		</c:otherwise>
										      	</c:choose>
										      </c:when>
										      <c:otherwise>
										      	<c:choose>
										      		<c:when test="${fn:length(top10Keyword.keyword)>7}">
										      			<li class="no"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')"><c:out value="${fn:substring(top10Keyword.keyword,0,6)}"/>...</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>
										      		</c:when>
										      		<c:otherwise>										      		
														<li class="no"><span class="rank_no">${topnum.count}</span><div class="rank_kw"><a href="javascript:fnDetailItem('<c:out value="${top10Keyword.keyword}"/>')">${top10Keyword.keyword}</a></div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${top10Keyword.percentage} style="width:${top10Keyword.percentage}%; height:100%;"></span><span class="count_no">${top10Keyword.keywordNum}</span></li>										    										      
										      		</c:otherwise>
										      	</c:choose>
										      </c:otherwise>
										    </c:choose>
										  </c:forEach>							
										</ul>
								</div>
                                
                                
                                </div>
							</div>
						</div>
			      <!--// 금주 키워드 TOP10 end -->

						<!-- 금주 카테고리 Top 10 start -->
						<div class="win win_1 float_l" style="margin-top:10px !important;">
							<div class="win_head clear2">
								<span class="win_tit bg_none">금주 카테고리 TOP10</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help" id="mouse_edit3"></a>
									<div class="tooltip btn_qty1">금주 카테고리 Top 10을 확인할 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
								<div class="graph_in" style="height:280px;">
                                
                                <div class="keywodrank_list">
										<ul>
										  <c:forEach var="topCate" items="${topCate}" varStatus="topCatenum">
										    <c:choose>
										      <c:when test="${topCatenum.count == 1}">
										      	<c:choose>
										      		<c:when test="${fn:length(topCate.keyword)>10}">
										      			<li class="no_1"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw"><c:out value="${fn:substring(topCate.keyword,0,6)}"/>...</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:when>
										      		<c:otherwise>										      		
										      			<li class="no_1"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw">${topCate.keyword}</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:otherwise>
										      	</c:choose>
										      </c:when>
										      <c:when test="${topCatenum.count == 2}">
										      	<c:choose>
										      		<c:when test="${fn:length(topCate.keyword)>10}">
										      			<li class="no_2"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw"><c:out value="${fn:substring(topCate.keyword,0,6)}"/>...</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:when>
										      		<c:otherwise>										      		
										      			<li class="no_2"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw">${topCate.keyword}</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:otherwise>
										      	</c:choose>
										      </c:when>
										      <c:when test="${topCatenum.count == 3}">
										      	<c:choose>
										      		<c:when test="${fn:length(topCate.keyword)>10}">
										      			<li class="no_3"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw"><c:out value="${fn:substring(topCate.keyword,0,6)}"/>...</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:when>
										      		<c:otherwise>										      		
										      			<li class="no_3"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw">${topCate.keyword}</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:otherwise>
										      	</c:choose>
										      </c:when>
										      <c:otherwise>
										      	<c:choose>
										      		<c:when test="${fn:length(topCate.keyword)>10}">
										      			<li class="no"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw"><c:out value="${fn:substring(topCate.keyword,0,6)}"/>...</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>
										      		</c:when>
										      		<c:otherwise>										      		
														<li class="no"><span class="rank_no">${topCatenum.count}</span><div class="rank_kw">${topCate.keyword}</div><span class="rank_right"><img src="../resources/images/common/bar1.png" alt=${topCate.percentage} style="width:${topCate.percentage}%; height:100%;"></span><span class="count_no">${topCate.keywordNum}</span></li>										    										      
										      		</c:otherwise>
										      	</c:choose>
										      </c:otherwise>
										    </c:choose>
										  </c:forEach>							
										</ul>

								</div>
                                
                              </div>
							</div>
						</div>
						<!--// 금주 카테고리 Top 10 end -->
						<!--  금일 이슈 Call TEXT start -->
						<div class="win win_3 float_r" style="margin-top:10px !important;">
							<div class="win_head clear2">
								<span class="win_tit bg_none">금일 이슈 Call TEXT</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help"></a>
									<div class="tooltip btn_qty1">금일 이슈 Call TEXT를 확인할 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
								<div class="graph_in" style="height:285px;">
                                	<ul class="calltext">
                                 
 										 <c:forEach var="callTexts" items="${callText}" >
 										 	<li><a href="javascript:detailView('${callTexts.CALL_META_ID }')"><span class="date">${fn:substring(callTexts.REGDATE, 0, 4)}.${fn:substring(callTexts.REGDATE, 4, 6)}.${fn:substring(callTexts.REGDATE, 6, 8)}</span><span class="t_theme">${callTexts.CONTENT }</span><span class="wirter">${callTexts.CUSTOMER_NAME }</span></a></li>
 										 </c:forEach>
 
 									</ul>
					           </div>
							</div>
						</div>
						<!--//금일 이슈 Call TEXT end -->

						<!--// 지점별 주요 키워드 분석 end -->
					</div>
				</div>
				<!--// 본문 end -->

<!-- 불만문서 상세페이지 팝업레이어 -->
<div class="modal_popup w700px" id="basic-modal-complain_view" ></div>
<!-- 이슈키워드 상세페이지 팝업레이어 -->
<div class="modal_popup w700px" id="basic-modal-cloud_view" ></div>
<!-- 지도 상세페이지 클릭시 다시 팝업 -->
<div class="modal_popup doc_view" id="basic-modal-alike" /></div>
<div class="modal_popup result_view" id="basic-modal-detail" /></div>
<!-- 음성민원 상세보기 modalpopup start-->
<div class="modal_popup w980px" id="basic-modal-voice_info"></div>