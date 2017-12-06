<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<script type="text/javascript" src="<c:url value='/resources/js/common.js'/>"></script>
<script type="text/javascript">
$(function(){	
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
});
</script>
<div class="graph_in" style="height:620px;">
	 <!--서울시 지역별 현황-->
                            <div class="map_area b2">
                            
                              <div class="map_info" style="position:absolute; left:1000px; top:80px;">
                                <p>서울시 민원발생 현황</p>
                                <ul>
                                    <li><img src="../resources/images/common/s_gray.png" alt="총계"> 민원총계: ${totalMinwon}건</li>
                                    <li><img src="../resources/images/common/s_blue.png" alt="긍정"> 긍정민원: ${totalPosi}건</li>
                                    <li><img src="../resources/images/common/s_orange.png" alt="부정"> 부정민원: ${totalNega}건</li>
                                    <li><img src="../resources/images/common/s_green.png" alt="중립"> 중립민원: ${totalNetu}건</li>
                                    
                                </ul>
                                
                              </div>
                    			
                             <div class="map_wrap">  
                             <c:forEach var="countrys" items="${countrys}" varStatus="countrysNum">
									<c:choose>
										<c:when test="${countrysNum.count == 1}">
										
                              <!--은평구 -->
                              <div class="map_a1"> 
                                   
                                    <a id="search" href="javascript:;" class="map_g1" name="${countrys.countryName}">
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
                                   <a id="search" href="javascript:;" class="map_g2" name="${countrys.countryName}">
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
                                        <a id="search" href="javascript:;" class="map_g3" name="${countrys.countryName}">
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
                                        <a id="search" href="javascript:;" class="map_g4" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g5" name="${countrys.countryName}">
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
                                        <a id="search" href="javascript:;" class="map_g6" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g7" name="${countrys.countryName}">
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
                                        <a id="search" href="javascript:;" class="map_g8" name="${countrys.countryName}">
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
                                        <a id="search" href="javascript:;" class="map_g9" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g10" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g11" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g12" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g13" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g14" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g15" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g16" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g17" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g18" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g19" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g20" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g21" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g22" name="${countrys.countryName}">
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
                                       
                                       <a id="search" href="javascript:;" class="map_g23" name="${countrys.countryName}">
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
                                       <a id="search" href="javascript:;" class="map_g25" name="${countrys.countryName}">
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
                 
                            <%-- <div class="map_area" id="map_area" style="margin-left:190px; margin-top:40px; width: 980px; ">
                              <div class="map_info ml_50">
                                <p>서울특별시 민원발생 현황</p>
                                <ul>
                                    <li><img src="../resources/images/common/s_gray.png" alt="총계"> 민원총계: ${totalMinwon}건</li>
                                    <li><img src="../resources/images/common/s_blue.png" alt="긍정"> 긍정민원: ${totalPosi}건</li>
                                    <li><img src="../resources/images/common/s_orange.png" alt="부정"> 부정민원: ${totalNega}건</li>
                                    <li><img src="../resources/images/common/s_green.png" alt="중립"> 중립민원: ${totalNetu}건</li>
                                    
                                </ul>
                                
                              </div>
								<c:forEach var="countrys" items="${countrys}" varStatus="countrysNum">
									<c:choose>
										<c:when test="${countrysNum.count == 1}">
                    			            <!--강화군 -->
											<div class="map_a1" style="margin-left:-260px;"> 
			                                  <p style="margin-top:70px; border-bottom:none !important; margin-left:-80px;">                      
			                                     <span class="map_t">${countrys.countryName}</span>
			                                     <span class="map_t_gray">${countrys.countryEgName}</span>
			                                 </p>
			                                 <a id="search" href="javascript:;" class="map_g1" name='${countrys.countryName}'>
			                                 <ul class="map_graph" style="margin-top:0px;margin-left:30px;">
			                                      <span class="num">${countrys.total}</span>
			                                      <li class="ml_10 gray_100"></li>
			                                      <li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      <li class="${countrys.netative}"></li>
			                                      <li class="${countrys.neutral}"></li>   
			                                 </ul>
			                                  </a>
		                                	</div>
		                                	<!--//강화군 -->
		                                	<!--강화군 popup-->
		                                	<div class="map_pop1" style="display: none; margin-left: -240px;" >
			                                	<p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                	<a href="javascript:;" class="btn_pclose" id="close1" title="닫기">X</a>
			                                    <div class="gray_box">
			                                    	<ul>
			                                        	<li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                            <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                            <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                        </ul>
			                                    </div>
			                                    
			                                     <div class="box_num">
			                                    	<ul>
			                                        	<li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                            <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                            <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                        </ul>
			                                    </div>                                
			                                </div>
			                                <!--// 강화군 popup-->											
										</c:when>
										<c:when test="${countrysNum.count == 2}">
										    <!--서구 -->
			                                <div class="map_a2" style="margin-left:-298px;margin-top:50px;"> 
			                                  <p style="margin-top:20px; margin-left:20px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                  </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g2">
			                                   <ul class="map_graph" style="margin-top:10px; margin-left:40px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                   </ul>
			                                    </a>
			                                </div>
			                                <!--//서구 -->
			                                <!--서구 popup-->
			                                <div class="map_pop2" style="display: none; margin-left: 160px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close2" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 서구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 3}">
										    <!--계양구 -->
											<div class="map_a3"> 
			                                    <p style="margin-top:120px; margin-left:-220px; width:110px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g3 style="margin-left:-380px;">
			                                    <ul class="map_graph" style="margin-top:-40px; margin-left:-130px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                     	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                     	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//계양구 -->
			                                <!--계양구 popup-->
			                                <div class="map_pop3" style="display: none; margin-left: 110px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close3" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
										    <!--// 계양구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 4}">
											<!--부평구 -->
											<div class="map_a3" style="margin-left:-320px;"> 
			                                    <p style="margin-top:210px; margin-left:70px; width:150px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g4">
			                                    <ul class="map_graph" style="margin-top:-60px; margin-left:180px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//부평구 -->
			                                <!--부평 popup-->
			                                <div class="map_pop4" style="display: none; margin-left: 90px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close4" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 부평구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 5}">
											<!--남동구 -->
											<div class="map_a3" style="margin-left:-310px;"> 
			                                    <p style="margin-top:280px; margin-left:90px; width:110px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g5" style="display:block; margin-top:0px; margin-left:120px;">
			                                    <ul class="map_graph">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//남동구 -->
			                                <!--남동구 popup-->
			                                <div class="map_pop5" style="display: none; margin-left: 90px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close5" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 남동구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 6}">
											<!--남구 -->
											<div class="map_a3" style="margin-left:-300px;"> 
			                                    <p style="margin-top:250px; margin-left:0px; width:150px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g6">
			                                    <ul class="map_graph" style="margin-top:-40px; margin-left:30px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//남구 -->
			                                <!--남구 popup-->
			                                <div class="map_pop6" style="margin-left:20px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close6" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 남구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 7}">
											<!--연수구 -->
											<div class="map_a3" style="margin-left:-330px;"> 
			                                    <p style="margin-top:330px; margin-left:0px; width:150px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g7">
			                                    <ul class="map_graph" style="margin-top:-30px; margin-left:0px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//연수구 -->
			                                <!--연수구 popup-->
			                                <div class="map_pop7">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close7" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 연수구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 8}">
											<!--중구 -->
											<div class="map_a4" style="margin-left:0px;"> 
			                                    <p style="margin-top:415px; margin-left:80px; width:90px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g8" style="display:block;position:absolute;left:520px; width:70px; top:520px; ">
			                                    <ul class="map_graph" style="margin-top:-40px; margin-left:0px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//중구 -->
			                                <!--중구 popup-->
			                                <div class="map_pop8" style="display: none;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close8" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 중구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 9}">
											<!--동구 -->
											<div class="map_a3" style="margin-left:-290px;"> 
			                                    <p style="margin-top:220px; margin-left:-30px; width:100px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g9">
			                                    <ul class="map_graph" style="margin-top:-90px; margin-left:-70px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//동구 -->
			                                <!--동구 popup-->
			                                <div class="map_pop9" style="display: none; margin-left: 110px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close9" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_angry_w.png" alt="angry"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            </div>
			                            <!--// 동구 popup-->
										</c:when>
										<c:when test="${countrysNum.count == 10}">
											<!--옹진군 -->
											<div class="map_a3" style="margin-left:-320px;"> 
			                                    <p style="margin-top:325px; margin-left:-210px; width:100px; border-bottom:none !important;">                      
			                                       <span class="map_t">${countrys.countryName}</span>
			                                       <span class="map_t_gray">${countrys.countryEgName}</span>
			                                   </p>
			                                   <a id="search" href="javascript:;" name='${countrys.countryName}' class="map_g10">
			                                    <ul class="map_graph" style="margin-top:-90px; position:absolute; right:300px;">
			                                        <span class="num">${countrys.total}</span>
			                                        <li class="ml_10 gray_100"></li>
			                                      	<li class="${countrys.positive}"></li>        <!-- 클래스의 숫자를 통해 막대그래프의 크기를 조절가능하다. -->   
			                                      	<li class="${countrys.netative}"></li>
			                                      	<li class="${countrys.neutral}"></li>   
			                                    </ul>
			                                  </a>
			                                </div>
			                                <!--//옹진군 -->
			                                <!--옹진군 popup-->
			                                <div class="map_pop10" style="display: none; margin-left:-280px;">
			                                <p class="map_title clear2"><span>${countrys.countryName}</span><span class="date">${nowDate}</span></p>
			                                <a href="javascript:;" class="btn_pclose" id="close10" title="닫기">X</a>
			                                <div class="gray_box">
			                                    <ul>
			                                        <li> <span class="t_24">${countrys.total}</span><span style="font-size:18px;">건</span><br><span>민원발생량</span></li>
			                                        <li><span class="t_key">클레임</span><br><span>이슈키워드</span></li>
			                                        <li><img src="../resources/images/common/icon_smile_w.png" alt="smile"></li>
			                                    </ul>
			                                </div>
			                                
			                                 <div class="box_num">
			                                    <ul>
			                                        <li class="blue_box"><span class="font_24">${countrys.positiveNum}<span class="font_16">건</span></span>긍정민원</li>
			                                        <li class="orange_box"><span class="font_24">${countrys.netativeNum}<span class="font_16">건</span></span>부정민원</li>
			                                        <li class="green_box"><span class="font_24">${countrys.neutralNum}<span class="font_16">건</span></span>중립민원</li>
			                                    </ul>
			                                </div>                                
			                            	</div>
			                            	<!--// 옹진군 popup-->
										</c:when>
									</c:choose>
								</c:forEach>
								                                                                                            
                    </div>--%>
                    <!--인천광역시 지역별 현황--> 