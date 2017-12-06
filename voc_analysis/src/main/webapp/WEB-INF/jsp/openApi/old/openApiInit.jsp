<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%-- <script type="text/javascript" src="<c:url value='/resources/js/voc/keywordRanking.js'/>"></script> --%>
<script type="text/javascript">
function getContextPath() {
    return "<c:out value="${pageContext.request.contextPath}" />";
}
</script>

				<!-- location start -->
				<div class="loc_wrap clear2">
					<ul>
					<li>홈</li>
					<li>설정</li>
					<li>OPEN API</li>
					</ul>
				</div>
				<!--// location end -->

				<!-- 본문 start -->
<div class="cont_body" id="cont_body">
				
					<div class="cont_head">
						<span class="cont_tit">OPEN API</span><span class="cont_desc">VOC분석 API를 제공됩니다.</span>
					</div>
                    
                    <div class="title_info api">
                    <p>소개</p>
                    	<ul class="mt_10">
                        	<li>VOC분석 API는 타 시스템과 연계되도록 API을 제공합니다.</li>
                            <li>제공하는 API는 REST FUL 방식으로 타 시스템과 인터페이스 할 수 있도록 제공 합니다.</li>
                        </ul>
                    </div>
                    
                    


					<div class="clear2">
						<!-- 리소스 모니터링 start -->
						<div class="win win_full float_l mr_10" style="margin-top:10px !important;">
							<div class="win_head clear2" style="text-align:left;">
								<span class="win_tit bg_none">제공되는 OPEN API 목록</span>
								<ul class="win_btnset">
								<li class="right_help">
									<a href="#" class="icon_help"></a>
									<div class="tooltip btn_qty1">OPEN API 목록을 확인하실 수 있습니다.<div class="tip-arrow"></div></div>
								</li>
								
								</ul>
							</div>
							<div class="win_contarea align_c">
							  <div class="graph_in" style="height:100%;">
                              		
                                   
                                          <fieldset><legend>REST FUL 목록</legend>
                                            <table class="tbl_type04 mt_5" summary="API명, 설명, 사용방법 목록">
                                            <caption>REST FUL 목록</caption>
                                            <colgroup>									
                                                <col style="width:12%;">
                                                <col style="width:28%;">
                                                <col>
                                                <col style="width:14%;">
                                            </colgroup>
            
                                            <thead>
                                              <tr>
                                                <th scope="col">구분</th>
                                                <th scope="col">API명</th>
                                                <th scope="col">설명</th>
                                                <th scope="col">사용방법</th>
                                              </tr>
                                            </thead>
            
                                            <tbody>
                                            <tr>
                                                <td>음성민원</td>
                                                <td>일자별 음성인식 목록</td>
                                                <td class="align_l">일자별 인식된 음성 목록 조회(시간/카테고리/제목/처리상태)</td>
                                                <td>JSON</td>
                                                
                                            </tr>
                                            <tr>    
                                                <td>음성민원</td>
                                                <td>음성인식 상세 조회</td>
                                                <td class="align_l">음성 컨텐츠별 상세조회(음성파일명, 추출된TEXT정보)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>종합랭킹 조회</td>
                                                <td class="align_l">분석된 키워드 랭킹 정보 조회(기간/카테고리/지역/부서)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>관심키워드랭킹 조회</td>
                                                <td class="align_l">사용자가 지정한 관심키워드에 대한 랭킹분석(기간/관심키워드목록/버즈량)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>이슈키워드랭킹 조회</td>
                                                <td class="align_l">기준 기간 대비 이슈가 되는 키워드에 대한 랭킹 조회(기간/카테고리/지역/부서)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>분야별현황분석 조회</td>
                                                <td class="align_l">분야별로 분석 정보 조회(기간/유형/분야/버즈량)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>지역별현황분석</td>
                                                <td class="align_l">지역별로 분석 정보 조회(기간/지역/감정분석/버즈량/기타)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>감성분석 조회</td>
                                                <td class="align_l">감성분석 정보 조회(기간/지역/감정키워드/버즈량/기타)</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>연관도분석 조회</td>
                                                <td class="align_l">주제어에 대한 연관정보 분석 조회(조건 : 기간/카테고리/지역/부서/주제어/제외어)에 따른 분석결과(연관도 1Depth~3 Depth정보/원문목록) 버즈량</td>
                                                <td>JSON</td>
                                            </tr>
                                            <tr>
                                                <td>VOC분석</td>
                                                <td>트렌드분석 조회</td>
                                                <td class="align_l">주제어에 대한 트렌드 분석 조회(조건 : 기간/카테고리/지역/부서/주제어/제외어)른 분석결과(기간/버즈량/원문목록)</td>
                                                <td>JSON</td>
                                            </tr>
                                           <!--  <tr>
                                                <td>1</td>
                                                <td>5_Pro_toal_cms</td>
                                                <td class="align_l">http://openapi.map.naver.com/api/geocode.php?key=test&query... com/api/geocode.php?key=8007ea8a8bbccc38e9f5419e3a8c07cf&query...</td>
                                                <td>사용방법</td>
                                            </tr>
                                            <tr>
                                                <td>1</td>
                                                <td>5_Pro_toal_cms</td>
                                                <td class="align_l">http://openapi.map.naver.com/api/geocode.php?key=test&query... com/api/geocode.php?key=8007ea8a8bbccc38e9f5419e3a8c07cf&query...</td>
                                                <td>사용방법</td>
                                            </tr> -->
                                            </tbody>
                                            </table>
                                		</fieldset>
                                    	
                                    
                               	
                                
                                </div>
							</div>
						</div>
			      <!--// 리소스 모니터링 end -->


							</div>
						
						</div>
						
					</div>
				</div>
				<!--// 본문 end -->
			
			

