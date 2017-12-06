<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
	<title>KB투자증권 통합VOC 분석시스템 : 대시보드</title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery-ui.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/default2.css'/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/layout2.css'/>" />
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-ui.min.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery.simplemodal.js'/>"></script>
	<script src="<c:url value='/resources/js/highcharts.js'/>"></script>
	<script src="<c:url value='/resources/js/highcharts-more.js'/>"></script>
	<script src="<c:url value='/resources/js/modules/exporting.js'/>"></script>
	<script src="<c:url value='/resources/js/modules/no-data-to-display.js'/>"></script>

	<script type="text/javascript">
		$(function(){
			//툴팁			
			$('.icon_help').mouseover(function(){
				top_mg = $(this).next('.tooltip').height()+2;
				$(this).next('.tooltip').css('top','-' + top_mg + 'px').fadeIn('fast');
			});

			$('.icon_help').mouseout(function(){
				$(this).next('.tooltip').fadeOut('fast');
			});
			
			// 상세 보기 클릭
			$('.complain_list a').on('click', function(){
				var docId = $(this).attr('id');
				var json = {
					"id" : docId
				};
				
				$("#basic-modal-complain_view").load("<c:out value="${pageContext.request.contextPath}" />/dashBoard/portalComplainDetailView.do #complain",
						json,
					function(){
						$("#basic-modal-complain_view").modal({
							persist: false,
							focus: false,
							onClose: function () {
								$('body').css('overflow','auto');
								$.modal.close();
							}
						});
						$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
					});
				return false;
			});
			
			// VOC 니즈유형별 분석 차트
			<c:forEach var="result" items="${needs}" varStatus="status">
				$('#d_keyword${status.index}').html("(당월:<c:out value="${result.current}" escapeXml="false"/>건)");
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
							size: '110%',
							allowPointSelect: true,
							borderWidth: 0,
							cursor: 'pointer',
							dataLabels: {
								enabled: false
							},
							innerSize: '108%'
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
							fontSize: '9px',
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
								html: '<c:out value="${result.count}" escapeXml="false"/>건',
								style: {
									left: '40px',
									top: '60px',
									fontSize: '11px',
									display:'block'
								}
							}
						]
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
								} ,
								{
									html: '(당월:<c:out value="${result.current}" escapeXml="false"/>건)',
									style: {
										left: '28px',
										top: '60px',
										fontSize: '10px',
										display:'block'
									}
								} 
							] */
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
						size: '105%',
						allowPointSelect: true,
						borderWidth: 0,
						cursor: 'pointer',
						dataLabels: {
							enabled: false
						},
						innerSize: '103%'
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
						fontSize: '12px',
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
							html: '(당월:<c:out value="${result.current}" escapeXml="false"/>건)',
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
								html: '<c:out value="${result.count}" escapeXml="false"/>건',
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
			
			// 우수고객 이슈 분석
			$('#vip').highcharts({ 
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

			var vipChart = $('#vip').highcharts();
			
			var series = vipChart.series[0];
			// 첫번째 키워드 한개만 활성화
			if(series != undefined){
				if(series.visible){
					$(vipChart.series).each(function(index){
						if(index != 0) this.setVisible(false, false);
					})
					vipChart.redraw();
				}
			}
			
			// 개선과제
			$('#lclsShare').highcharts({
				chart: {
					plotBackgroundColor: null,
					plotBorderWidth: null,
					plotShadow: false
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
				tooltip: {
					pointFormat: '{point.name}: <b>{point.percentage:.1f}%</b>'
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						dataLabels: {
							enabled: true,
							color: '#000000',
							connectorColor: '#000000',
							format: '<b>{point.name}</b>: {point.percentage:.1f} %'
						}
					}
				},
				series: [{
					type: 'pie',
					data: <c:out value="${lclsShare.series}" escapeXml="false"/>
				}]
			});
			
			// 제안부서 TOP 5 분석
			 $('#dept').highcharts({
				chart: {
					type: 'bar',
					spacingTop: 0,
					spacingBottom: 0,
					spacingLeft: 0,
					spacingRight:0
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
					text: null,
					enabled:false
				},
				xAxis: {
					categories: <c:out value="${dept.categories}" escapeXml="false"/>
				},
				yAxis: {
					style: {
						color: '#000',
						fontSize: '5px',
						fontFamily: 'arial'
					},
					title: {
						text: '빈도수'
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
						return '빈도수: '+this.point.y+'건';
					}
				},
				series: <c:out value="${dept.series}" escapeXml="false"/>
			});
		});

	</script>

</head>

<body>

<div id="wrapper">
	
	<div class="pop_head">
		<img src="<c:url value="/resources/images/common/portal_logo.gif"/>" alt="KB투자증권 : 통합VOC 현황판" />
		<!-- 날짜 start -->
		<div class="util_area">
			<span class="datetime_info">
				<strong>
					<fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd" var="thisYmd" />
					<fmt:formatDate value="<%= new java.util.Date() %>" type="time" var="thisTime" />
					${thisYmd} ${thisTime} 기준</strong>
			</span>
		</div>
		<!--// 날짜 end -->
	</div>

	<!-- 본문 start -->
	<div class="cont_body">
	
		<div class="top_graph">

			<div class="top_donut left clear2">
				<p>전월대비 VOC 증감현황</p>
				<ul>
					<li>
						<span class="d_keyword">업무상담</span>
						<div class="donut_back" id="needs0"></div>
						<div class="d_keyword2" id="d_keyword0"></div>
					</li>
					<li>
						<span class="d_keyword">건의·제안</span>
						<div class="donut_back" id="needs1"></div>
						<div class="d_keyword2" id="d_keyword1"></div>
					</li>
					<li>
						<span class="d_keyword">칭찬</span>
						<div class="donut_back" id="needs2"></div>
						<div class="d_keyword2" id="d_keyword2"></div>
					</li>
					<li>
						<span class="d_keyword">불편·불만</span>
						<div class="donut_back" id="needs3"></div>
						<div class="d_keyword2" id="d_keyword3"></div>
					</li>
				</ul>
			</div>

			<div class="top_donut right clear2">
				<p>전월대비 개선과제 증감현황</p>
				<ul>
					<li>
						<span class="d_keyword">업무제안</span>
						<div class="donut_back" id="lcls0"></div>
						<div class="d_keyword2" id="l_keyword0"></div>
					</li>
					<li>
						<span class="d_keyword">매체제안</span>
						<div class="donut_back" id="lcls1"></div>
						<div class="d_keyword2" id="l_keyword1"></div>
					</li>
					<li>
						<span class="d_keyword">상품제안</span>
						<div class="donut_back" id="lcls2"></div>
						<div class="d_keyword2" id="l_keyword2"></div>
					</li>
					<li>
						<span class="d_keyword">영업제안</span>
						<div class="donut_back" id="lcls3"></div>
						<div class="d_keyword2" id="l_keyword3"></div>
					</li>
					<li>
						<span class="d_keyword">경영제도</span>
						<div class="donut_back" id="lcls4"></div>
						<div class="d_keyword2" id="l_keyword4"></div>
					</li>
				</ul>
			</div>
		</div>

		<div class="clear2">
			<!-- 개선과제 유형별 점유율 현황 start -->
			<div class="win win_left float_l">
				<div class="win_head clear2">
					<span class="win_tit bg_none">개선과제 유형별 점유율 현황</span>
					<a href="#" class="icon_help"></a>
					<div class="tooltip">개선과제 대분류 유형에 대한 최근 한달간의 점유율을 파이차트 형태로 확인할 수 있습니다.
						<div class="tip-arrow"></div>
					</div>
				</div>
				<div class="win_contarea align_c">
					<div class="graph_in" style="height:154px;" id="lclsShare">
					</div>
				</div>
			</div>
			<!--// 개선과제 유형별 점유율 현황 end -->

			<!-- 제안부서 TOP 5 현황 start -->
			<div class="win win_right float_r">
				<div class="win_head clear2">
					<span class="win_tit bg_none">제안부서 TOP 5 현황</span>
					<a href="#" class="icon_help"></a>
					<div class="tooltip">최근 한달간 개선과제 등록 건수가 많은 상위 5개 부서에 대한 현황을 그래프 형태로 제공합니다.
						<div class="tip-arrow"></div>
					</div>
				</div>
				<div class="win_contarea align_c">
					<div class="graph_in" style="height:154px;" id="dept">
					</div>
				</div>
			</div>
			<!--// 제안부서 TOP 5 현황 end -->
		</div>


		<div class="clear2">
			<!-- 우수고객 이슈분석 start -->
			<div class="win win_left float_l">
				<div class="win_head clear2">
					<span class="win_tit bg_none">우수고객 이슈분석</span>
					<a href="#" class="icon_help"></a>
					<div class="tooltip">우수고객(MVP 이상) 등급에서 최근 한달간 발생된 VOC 문서 중 주요 이슈키워드를 추출하여 그래프 형태로 제공합니다.
						<div class="tip-arrow"></div>
					</div>
				</div>
				
				<div class="win_contarea clear2 align_c" style="height:164px;" id="vip">
				</div>
			</div>
			<!--// 우수고객 이슈분석 end -->

			<!-- 불만지수 분석 start -->
			<div class="win win_right float_r">
				<div class="win_head clear2">
					<span class="win_tit bg_none">VOC 불만지수 분석</span>
					<a href="#" class="icon_help"></a>
					<div class="tooltip">최근 한달간 등록된 VOC 문서 중 전체 문서 대비 불만문서의 건수를 산출하여 불만지수를 제공합니다.
						<div class="tip-arrow"></div>
					</div>
				</div>
				<div class="win_contarea p_0 align_c" style="height:184px;">
					<ul class="complain clear2">
						<li class="complain_01"><span>총 문서수</span><p><c:out value="${complainDocumnet.totalSize}" escapeXml="false"/></p></li>
						<li class="complain_02"><span>불만문서 건수</span><p><c:out value="${complainDocumnet.complainSize}" escapeXml="false"/></p></li>
						<li class="complain_03"><span>불만지수</span><p><c:out value="${complainDocumnet.percentage}" escapeXml="false"/>%</p></li>
					</ul>

					<div class="complain_list">
						<ul>
						<c:forEach var="result" items="${complainDocumnet.list}" varStatus="status">
							<li>
								<a href="#" name="content" class="modal_btn" id='<c:out value="${result.ID}" escapeXml="false"/>'>
									<c:out value="${result.CONTENT}" escapeXml="false"/>
								</a>
							</li>
						</c:forEach>
						</ul>
					</div>

				</div>
			</div>
			<!--// 불만지수 분석 end -->		

		</div>

	</div>
	<!--// 본문 end -->

	<!-- footer start -->
	<div id="footer">
		<img src="<c:url value="/resources/images/common/footer_logo.gif"/>" />
	</div>
	<!--// footer end -->
			
<!-- 불만문서 상세페이지 팝업레이어 -->
<div class="modal_popup w700px" id="basic-modal-complain_view" ></div>

</div>
</body>
</html>