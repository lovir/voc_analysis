package com.diquest.voc.openapi.controller;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.openapi.service.OpenApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class OpenApiController {

	/** SearchService */
	@Resource(name = "openApiService")
	private OpenApiService openApiService;
	 
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	private static String portal_id;
	private static String portal_nm;

	//Open API 관리 화면
	@RequestMapping(value="/openApi/openApiInit.do")
	public String openApiInit(Model model, HttpServletRequest request) throws Exception{
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명\
		String totalAcmSize = null;
		try {
			totalAcmSize = openApiService.selectTotalCategorizeCount();
			NumberFormat nf = NumberFormat.getNumberInstance();
			totalAcmSize = nf.format(Integer.parseInt(totalAcmSize));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		model.addAttribute("totalAcmSize", totalAcmSize);
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/openApi/openApiInit";
	}
	
	//Open API 관리 화면
	@RequestMapping(value="/openApi/selectDayCategorizeList.do")
	public String selectDayCategorizeList(Model model, HttpServletRequest request) throws Exception{
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명\
		String pageSize = request.getParameter("pageSize") == null ? "10" : request.getParameter("pageSize"); //페이지 크기
		String currentPageNo = request.getParameter("currentPageNo") == null ? "1" : request.getParameter("currentPageNo"); //현재 페이지
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("pageSize", pageSize);
		paramMap.put("currentPageNo", currentPageNo);
		
		String totalListSizeStr = null;
		int totalSize = 0;
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String,String>>();
		try {
			totalListSizeStr = openApiService.selectDayCategorizeCount();
			totalSize = Integer.parseInt(totalListSizeStr);
			resultList = openApiService.selectDayCategorizeList(paramMap);
			NumberFormat nf = NumberFormat.getNumberInstance();
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		Gson gson = null;
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("resultList", resultList);
		jsonMap.put("totalSize", totalSize);
		model.addAttribute("jsonData", gson.toJson(jsonMap));
		return "/common/ajax";
	}
	
	//모달 팝업 내 페이징 처리
	@RequestMapping(value="/openApi/modalPaging.do")
	public String modaPaging(Model model, HttpServletRequest request) throws Exception{
		String pageSize = request.getParameter("pageSize") == null ? "10" : request.getParameter("pageSize"); //페이지 크기
		String currentPageNo = request.getParameter("currentPageNo") == null ? "1" : request.getParameter("currentPageNo"); //현재 페이지
		String totalSize = request.getParameter("totalSize") == null ? "1" : request.getParameter("totalSize"); //현재 페이지
		String endPageNo = "";
		
		int endPage = Integer.parseInt(totalSize) / Integer.parseInt(pageSize);
		if (endPage % Integer.parseInt(pageSize) > 0) {
			endPage++;
		}
		if (endPage == 0) {
			endPage = 1;
		}
		endPageNo = String.valueOf(endPage);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.parseInt(currentPageNo));
		paginationInfo.setRecordCountPerPage(Integer.parseInt(pageSize));
		paginationInfo.setPageSize(10);
		paginationInfo.setTotalRecordCount(Integer.parseInt(totalSize));
		  
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("currentPageNo", currentPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("totalSize", totalSize);
		return "/openApi/modalPaging";
	}
	
	//외부 Open API 관리 화면
	@RequestMapping(value="/openApi/externalOpenApiInit.do")
	public String externalOpenApiInit(Model model, HttpServletRequest request) throws Exception{
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		String totalObserveAcmSize = null;
		String totalForecastAcmSize = null;
		String todayObserveSize = null;
		String todayForecastSize = null;
		
		try {
			totalObserveAcmSize = openApiService.selectTotalWeatherObserveCount();
			totalForecastAcmSize = openApiService.selectTotalWeatherForecastCount();
			todayObserveSize = openApiService.selectTodayWeatherObserveCount();
			todayForecastSize = openApiService.selectTodayWeatherForecastCount();
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			totalObserveAcmSize = nf.format(Integer.parseInt(totalObserveAcmSize));
			totalForecastAcmSize = nf.format(Integer.parseInt(totalForecastAcmSize));
			todayObserveSize = nf.format(Integer.parseInt(todayObserveSize));
			todayForecastSize = nf.format(Integer.parseInt(todayForecastSize));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		model.addAttribute("totalObserveAcmSize", totalObserveAcmSize);
		model.addAttribute("totalForecastAcmSize", totalForecastAcmSize);
		model.addAttribute("todayObserveSize", todayObserveSize);
		model.addAttribute("todayForecastSize", todayForecastSize);
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/openApi/externalOpenApiInit";
	}
	
	//외부 Open API 모달 팝업 리스트 출력
	@RequestMapping(value="/openApi/selectDayExternalOpenApiList.do")
	public String selectDayExternalOpenApiList(Model model, HttpServletRequest request) throws Exception{
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id");	// 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm");	// 포탈 사용자명\
		String pageSize = request.getParameter("pageSize") == null ? "10" : request.getParameter("pageSize");	//페이지 크기
		String currentPageNo = request.getParameter("currentPageNo") == null ? "1" : request.getParameter("currentPageNo");	//현재 페이지
		String apiType = request.getParameter("apiType") == null ? "observe" : request.getParameter("apiType");	//API 구분용 인자(observe:기상 관측 정보, forecast: 기상 예측 정보)
		HashMap<String, String> paramMap = new HashMap<>();
		paramMap.put("pageSize", pageSize);
		paramMap.put("currentPageNo", currentPageNo);
		
		String totalListSizeStr = null;
		int totalSize = 0;
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String,String>>();
		try {
			if(apiType.equals("observe")){
				totalListSizeStr = openApiService.selectDayWeatherObserveCount();
				totalSize = Integer.parseInt(totalListSizeStr);
				resultList = openApiService.selectDayWeatherObserveList(paramMap);
			}
			else{
				totalListSizeStr = openApiService.selectDayWeatherForecastCount();
				totalSize = Integer.parseInt(totalListSizeStr);
				resultList = openApiService.selectDayCWeatherForecastList(paramMap);
			}
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		Gson gson = null;
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("resultList", resultList);
		jsonMap.put("totalSize", totalSize);
		model.addAttribute("jsonData", gson.toJson(jsonMap));
		return "/common/ajax";
	}
	
	
	//이전 VOC 소스들. 서울메트로에서 사용 안함.
	/*
	@RequestMapping(value="/openapi/searchinit.do")
	public String openapitestinit(Model model){
	
		return "/common/openapitest2";
	}
	
	@RequestMapping(value="/openapi/search.do")
	public String openapitest(Model model, HttpServletRequest request){

		// OpenApi의 파라미터 값을 받아 빈값이면 ""값을 넣어주는 구역
		String reqType = SearchUtil.getParamStr(request.getParameter("reqType"), ""); // 요청하는 데이터의 형태 (stat - 통계형 데이터, search - 검색형 데이터)
		String statFiled = SearchUtil.getParamStr(request.getParameter("statFiled"), ""); // 톧계형 데이터인 경우 출력을 원하는 필드
		String searchTarget = SearchUtil.getParamStr(request.getParameter("searchTarget"), ""); // 검색대상  (minwon, 120, sinmun, saeo, sns, news, blog, tweet)
		String pageno = SearchUtil.getParamStr(request.getParameter("pageno"), "1"); // 요청하는 페이지 수
		String resultSize = SearchUtil.getParamStr(request.getParameter("resultSize"), "30");
		String searchTerm = SearchUtil.getParamStr(request.getParameter("query"), ""); //검색어
		String startDate = SearchUtil.getParamStr(request.getParameter("startDate"), ""); //검색을 원하는 시작일
		String endDate = SearchUtil.getParamStr(request.getParameter("endDate"), ""); // 검색을 원하는 종료일
		String guName = SearchUtil.getParamStr(request.getParameter("guName"), ""); // 행정구역명 (예 : 강화군, 서구, 중구 등)
		String cateName = SearchUtil.getParamStr(request.getParameter("cateName"), ""); // 카테고리명  (예 : 상수도, 교통, 행정 등)
		String departName = SearchUtil.getParamStr(request.getParameter("departName"), ""); // 부서명 (본부 혹은 국 단위의 부서명)
		String sensitivity = SearchUtil.getParamStr(request.getParameter("sensitivity"), ""); // 감정값 (N(중립), P(긍정), D(부정))
		
		
		// 파라미터값으로 받은 정보를 Map에 담는 구역
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("reqType", reqType);
		paramMap.put("statFiled", statFiled);
		paramMap.put("searchTarget", searchTarget);
		paramMap.put("pageno", pageno);		
		paramMap.put("resultSize", resultSize);
		paramMap.put("searchTerm", searchTerm);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("guName", guName);
		paramMap.put("cateName", cateName);
		paramMap.put("departName", departName);
		paramMap.put("sensitivity", sensitivity);

		
		
		try {
			
			model.addAttribute("xml", openApiService.openapitest(paramMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/common/openapitest2";
	}
	*/
	
}
