package com.diquest.voc.dashBoard.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.CommonService;
import com.diquest.voc.dashBoard.service.DashBoardService;
import com.diquest.voc.dashBoard.vo.DashBoardVo;
import com.diquest.voc.dashBoard.vo.Top10totalVO;
import com.diquest.voc.dashBoard.vo.TopKeywordVO;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.openapi.service.OpenApiService;
import com.diquest.voc.stationStatus.service.StationStatusService;
//import com.diquest.voc.trend.service.TrendAnalysisService;
//import com.diquest.voc.dashBoard.service.DashBoardService;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.diquest.voc.trend.service.TrendAnalysisService;
import com.diquest.voc.trend.vo.TrendAnalysisVo;
import com.google.gson.Gson;

import egovframework.rte.psl.dataaccess.util.EgovMap;

/**  
 * @Class Name : DashBoardController.java
 * @Description : DashBoardController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.11.30         최초생성
 * 
 * @author 
 * @since 
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class DashBoardController {

	/** StationService */
	@Resource(name = "stationStatusService")
	private StationStatusService stationStatusService;
	
	/** SearchService */
	@Resource(name = "keywordRankingService")
	private KeywordRankingService keywordRankingService;

	/** SearchService */
	@Resource(name = "dashBoardService")
	private DashBoardService dashBoardService;
	
	/** SearchService */
	@Resource(name = "trendAnalysisService")
	private TrendAnalysisService trendAnalysisService;
	/**
	 * 대시보드 초기화면 표시
	 * @param model
	 * @return "/dashBoard/analysisSummary"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/dashBoard/dashBoardInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String init(Model model, @ModelAttribute("vo") DashBoardVo vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		
		return "/dashBoard/dashBoard";
	}
	
	/**
	 * 대시보드 역별현황 차트 
	 * @param model
	 * @return "/dashBoard/analysisSummary"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/stationChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String stationChart(Model model, @ModelAttribute("vo") StationStatusVo vo) throws Exception {
		
		vo.setUserId(portal_id);
		
		String jsonStation = stationStatusService.getStationChart(vo);	
		model.addAttribute("jsonData", jsonStation);
		
		return "/common/ajax";
	}
	
	/**
	 * 대시보드 관심키워드 차트 
	 * @param model
	 * @return "/dashBoard/analysisSummary"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/interestChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String interestChart(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		
		keywordRankingVo.setLogin_Id(portal_id);
		
		// 관심키워드 조회시 필요한 값 셋팅
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("loginId", portal_id);
		List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출

		// 관심키워드의 경우 날짜 재연산 (x축 7개)
		String endDate = keywordRankingVo.getEndDate();
		String condition = keywordRankingVo.getCondition();
		
		String startDate = "";
		if(condition.equals("DAY")){
			startDate = DateUtil.addYearMonthDay("yyy/MM/dd", endDate, Calendar.DATE, -6);
		}else if(condition.equals("WEEK")){
			startDate = DateUtil.addYearMonthDay("yyy/MM/dd", endDate, Calendar.DATE, -42);
		}else if(condition.equals("MONTH")){
			startDate = DateUtil.addYearMonthDay("yyy/MM/dd", endDate, Calendar.MONTH, -6);
		}
		keywordRankingVo.setStartDate(startDate);
		
		if (keywordList.size() > 0) {
			keywordRankingVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심 키워드 설정
			if (keywordList.size() > 0) {
				keywordRankingVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심 키워드 설정
			}
		}else{		// 관심키워드가 없을 경우 top 10 키워드 추출
			keywordRankingVo.setKeywordArr(keywordRankingService.getsynthesisTotalKeywordArr(keywordRankingVo)); // 상위 10개 키워드 셋팅
		}
	
		Gson gson = new Gson();
		String report = gson.toJson(keywordRankingService.getSynthesisReport(keywordRankingVo)); // 리포트 정보
		model.addAttribute("jsonData", report);

		
		return "/common/ajax";
	}
	/**
	 * 대시보드 유형별현황 차트 
	 * @param model
	 * @return "/dashBoard/kindChart"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/kindChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String kindChart(Model model, @ModelAttribute("vo") DashBoardVo vo) throws Exception {
		
		vo.setLogin_Id(portal_id);
		
		String jsonStation = dashBoardService.getKindChart(vo);
		model.addAttribute("jsonData", jsonStation);
		
		return "/common/ajax";
	}
	/**
	 * 키워드랭킹분석_종합랭킹분석
	 * 
	 * @param model
	 * @return "/keywordRanking/synthesisRanking_total"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/getTotalRanking.do")
	public String totalRanking(Model model,  @ModelAttribute("vo") DashBoardVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		
		try {
			// 관심키워드 조회시 필요한 값 셋팅
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("loginId", portal_id); 

			ArrayList<HashMap<String, String>> synthesisRanking = new ArrayList<HashMap<String, String>>();			
							
			synthesisRanking = dashBoardService.getTotalRanking(vo);
				
			
			model.addAttribute("synthesisRanking", synthesisRanking);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/dashBoard/dashBoardRanking_total";
	}
	/**
	 * 카테고리 top 10
	 * 
	 * @param model
	 * @return "/keywordRanking/synthesisRanking_total"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/getCategoryRanking.do")
	public String categoryRanking(Model model,  @ModelAttribute("vo") DashBoardVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		
		try {
			// 관심키워드 조회시 필요한 값 셋팅
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("loginId", portal_id); 

			ArrayList<HashMap<String, String>> synthesisRanking = new ArrayList<HashMap<String, String>>();										
			synthesisRanking = dashBoardService.getCategoryRanking(vo);
				
			
			model.addAttribute("synthesisRanking", synthesisRanking);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/dashBoard/dashBoardRanking_total";
	}	
	
	/**
	 * 긍/부정 증감현황
	 * 
	 * @param model
	 * @return "/keywordRanking/synthesisRanking_total"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/getEmotionStatus.do")
	public String emotionStatus(Model model,  @ModelAttribute("vo") DashBoardVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		
		model.addAttribute("jsonData", dashBoardService.getEmotionChart(vo));
		//model.addAttribute("IncreAnDecre",dashBoardService.dashBoardIncreAnDecre(vo)); // 긍/부정 VOC증감현황
		return "/common/ajax";
	}	
	
	/**
	 * 워드 클라우드 차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/wordCloudChart.do")
	public String wordCloudChart(Model model, @ModelAttribute("vo") DashBoardVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		
		try {
			Gson gson = new Gson();
			String wordList = gson.toJson(dashBoardService.getWordCloudChart(vo)); // 리포트 정보
			model.addAttribute("jsonData", wordList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/common/ajax";
	}
	
	/**
	 * 역별현황 클릭시 키워드
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/stationKeyword.do")
	public String stationKeyword(Model model, @ModelAttribute("vo") StationStatusVo vo, HttpServletRequest request) throws Exception {
		
		vo.setUserId(portal_id);
		String keywords = stationStatusService.getKeywordArr(vo);
		model.addAttribute("jsonData", keywords);		
		return "/common/ajax";
	}
	
	/**
	 * 이슈 클라우드의 키워드 클릭시 선그래프/하단 표
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/dashBoard/wordCloudClick.do")
	public String wordCloudClick(Model model, @ModelAttribute("vo") DashBoardVo vo) throws Exception {
		
		vo.setLogin_Id(portal_id);

		// 날짜 재 연산 (x축 7개)
		String endDate = vo.getEndDate();
		String condition = vo.getCondition();
		
		String startDate = "";
		if(condition.equals("DAY")){
			startDate = DateUtil.addYearMonthDay("yyy/MM/dd", endDate, Calendar.DATE, -6);
		}else if(condition.equals("WEEK")){
			startDate = DateUtil.addYearMonthDay("yyy/MM/dd", endDate, Calendar.DATE, -42);
		}else if(condition.equals("MONTH")){
			startDate = DateUtil.addYearMonthDay("yyy/MM/dd", endDate, Calendar.MONTH, -6);
		}
		vo.setStartDate(startDate);
		
		String chart = dashBoardService.issueWordCloudClick(vo);	
		
		model.addAttribute("jsonData", chart);		
		return "/common/ajax";
	}
	
	/*
	*//** DashBoardService *//*
	@Resource(name = "dashBoardService")
	private DashBoardService dashBoardService;
	
	*//** openapiservice *//*
	@Resource(name = "openApiService")
	private OpenApiService openApiService;
	
	*//** TrendAnalysisService *//*
	@Resource(name = "trendAnalysisService")
	private TrendAnalysisService trendAnalysisService;
	
	*//** CommonService *//*
	@Resource(name = "commonService")
	private CommonService commonService;
	
	*//** Log Service *//*
	Logger log = Logger.getLogger(this.getClass());
	
	*//**
	 * 대시보드 초기화면 표시
	 * @param model
	 * @return "/dashBoard/analysisSummary"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/init.do")
	public String init(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo, HttpServletRequest request) throws Exception {
		
		// session 처리 	
		String userID="test";
		String userPass="test";
		userID="";
		userPass="";
		
		String code = commonService.base64LoginCheker(userID, userPass);
		
		EgovMap login = commonService.selectLogin(userID);
		log.debug("[VOC Analysis(AD Login) ] login Info : " + login);
		if(login != null){
			log.debug("[VOC Analysis(AD Login)] Login Success!!!");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userIp", "localhost");
			map.put("userId", login.get("userId"));
			map.put("userNm", login.get("userNm"));
			map.put("depCd", login.get("depCd"));
			map.put("depNm", login.get("depNm"));
			map.put("pstNm", login.get("pstNm"));

			HttpSession session = request.getSession(false);
			
			if(session != null){
				session.invalidate(); // 초기화
			}
			
			session = request.getSession(true); // 새로 생성
			
			session.setAttribute("login", map);
			log.debug("[VOC Analysis(AD Login) Session Checker] ServletContext :"+session.getServletContext());
			log.debug("[VOC Analysis(AD Login) Session Checker] MaxInactiveInterval :"+session.getMaxInactiveInterval());
			log.debug("[VOC Analysis(AD Login) Session Checker] LastAccessedTime :"+session.getLastAccessedTime());
			log.debug("[VOC Analysis(AD Login) Session Checker] login :"+session.getAttribute("login"));
			log.debug("[VOC Analysis(AD Login) Session Checker] ID :"+session.getId());
		}
		////////////////////// 
		
		LinkedHashMap<String, String> periodMap;
		
		vo.setCollection("VOC");
		
		Map<String, Object> map = (Map<String, Object>)request.getSession().getAttribute("login");
		vo.setUserId(map.get("userId").toString());
		vo.setUserIp(map.get("userIp").toString());
		vo.setCondition(Globals.FIELD_WEEK); // 전주
      
		vo.setEndDate(DateUtil.getCurrentDate("yyyyMMdd")); // 종료일
		vo.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), -7)); // 시작일		

		// 전주대비
		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_WEEK);
		}else{
			periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_MONTH);
		}	
		
		String current = "";
		
		for(String period : periodMap.keySet()){
			if(current.isEmpty()) current = period;
			if(period.compareTo(current)>0){
				vo.setCurrent(period);
			}else{
				vo.setPrevious(period);
			}
		}
		 
		try {
			model.addAttribute("interest", dashBoardService.dashBoardInterestReport(vo)); // 관심키워드별 분석차트
			model.addAttribute("issue", dashBoardService.dashBoardIssueReport(vo)); // VOC 이슈 클라우드 분석		
			model.addAttribute("top10Keywordlist",dashBoardService.dashBoardTop10Keyword(vo)); //금주 키워드 Top10 작업
			model.addAttribute("topCate",dashBoardService.dashBoardTopCategory(vo));  //금주 카테고리 Top10 작업
			model.addAttribute("IncreAnDecre",dashBoardService.dashBoardIncreAnDecre(vo)); // 긍/부정 VOC증감현황
			model.addAttribute("voice", dashBoardService.dashBoardVoiceReport(vo)); // 음성분석차트
			//인천광역시 민원발생 현황 작업
			ContryMInwonTotalVO cmtvo = dashBoardService.dashBoardCountryMinwon(vo);
			model.addAttribute("countrys",cmtvo.getCmlist());
			model.addAttribute("totalMinwon",cmtvo.getTotalT());
			model.addAttribute("totalPosi",cmtvo.getTotalPosi());
			model.addAttribute("totalNega",cmtvo.getTotalNega());
			model.addAttribute("totalNetu",cmtvo.getTotalNetu());
			model.addAttribute("nowDate",cmtvo.getNowdate());
			
			model.addAttribute("startDate",vo.getStartDate());
			model.addAttribute("endDate",vo.getEndDate());
			
			if(!vo.getCondition().equals("WEEK")){
				vo.setCondition("MONTH");
			}
			model.addAttribute("condition",vo.getCondition());			
			model.addAttribute("callText",dashBoardService.dashBoardIssueText(vo)); // CALL TEXT
			System.out.println();
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		
		return "/dashBoard/dashBoard";
	} 

	*//**
	 * 대시보드 검색(전주대비/전월대비)
	 * @param model
	 * @return "/dashBoard/search"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/search.do")
	public String search(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception {
		
		LinkedHashMap<String, String> periodMap;
		
		// 종료일
		vo.setEndDate(DateUtil.getCurrentDate("yyyyMMdd"));
		
		// 시작일
		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			vo.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), -7)); // 전주대비 (전주 시작일<일요일>)
		}else{
			vo.setStartDate(DateUtil.addYearMonthDay("yyyyMM", vo.getEndDate().substring(0,6), Calendar.MONTH, -1) + "01"); // 전월대비 (전월 첫째날)
		}
		
		// 전주대비
		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_WEEK);
		}else{
			periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_MONTH);
		}
		
		
		
		String current = "";
		
		for(String period : periodMap.keySet()){
			if(current.isEmpty()) current = period;
			if(period.compareTo(current)>0){
				vo.setCurrent(period);
			}else{
				vo.setPrevious(period);
			}
		}
		
		try {
			
			model.addAttribute("interest", dashBoardService.dashBoardInterestReport(vo)); // 관심키워드별 분석차트
			model.addAttribute("issue", dashBoardService.dashBoardIssueReport(vo)); // VOC 이슈 클라우드 분석
			model.addAttribute("top10Keywordlist",dashBoardService.dashBoardTop10Keyword(vo)); //금주 키워드 Top10 작업
			model.addAttribute("topCate",dashBoardService.dashBoardTopCategory(vo));  //금주 카테고리 Top10 작업
			model.addAttribute("IncreAnDecre",dashBoardService.dashBoardIncreAnDecre(vo)); // 긍/부정 VOC증감현황
			//인천광역시 민원발생 현황 작업
			ContryMInwonTotalVO cmtvo = dashBoardService.dashBoardCountryMinwon(vo);
			model.addAttribute("countrys",cmtvo.getCmlist());
			model.addAttribute("totalMinwon",cmtvo.getTotalT());
			model.addAttribute("totalPosi",cmtvo.getTotalPosi());
			model.addAttribute("totalNega",cmtvo.getTotalNega());
			model.addAttribute("totalNetu",cmtvo.getTotalNetu());
			model.addAttribute("nowDate",cmtvo.getNowdate());
			model.addAttribute("conditionCheck",vo.getCondition());
			model.addAttribute("collectionCkeck",vo.getCollection());
			
			model.addAttribute("startDate",vo.getStartDate());
			model.addAttribute("endDate",vo.getEndDate());
			
			model.addAttribute("condition",vo.getCondition());
			
			// 목록
			
		}catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		
		return "/dashBoard/dashBoard";
	} 
	
	*//**
	 * 포탈 대시보드
	 * @param model
	 * @return "/dashBoard/search"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/portal.do")
	public String portal(Model model, HttpServletRequest request) throws Exception {
		String uId  = request.getParameter("uid") == null ? "" : request.getParameter("uid"); // uid(사번)
		String guId = request.getParameter("guid") == null ? "" : request.getParameter("guid"); // guid(인증키)
		String sId = request.getParameter("sid") == null ? "4" : request.getParameter("sid"); // sid
		LinkedHashMap<String, String> periodMap;
		DashBoardVo vo = new DashBoardVo();
		Map<String, Object> map = null;
		if(request.getSession().getAttribute("login") != null) {
			map = (Map<String, Object>)request.getSession().getAttribute("login");
			vo.setUserIp(map.get("userIp").toString());
		}
		
		vo.setCondition(Globals.FIELD_MONTH); // 전월
		vo.setEndDate(DateUtil.getCurrentDate("yyyyMMdd")); // 종료일
		vo.setStartDate(DateUtil.addYearMonthDay("yyyyMM", vo.getEndDate().substring(0,6), Calendar.MONTH, -1) + "01"); // 전월대비 (전월 첫째날)
		// 전월대비
		periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_MONTH);
		String current = "";
		
		for(String period : periodMap.keySet()){
			if(current.isEmpty()) current = period;
			if(period.compareTo(current)>0){
				vo.setCurrent(period);
			}else{
				vo.setPrevious(period);
			}
		}
		
		try {
			model.addAttribute("needs", dashBoardService.dashBoardNeedsReport(vo)); // VOC 니즈유형별 분석트
			model.addAttribute("lcls", dashBoardService.dashBoardLclsReport(vo)); // VOE 대분류별 분석차트
			model.addAttribute("lclsShare", dashBoardService.dashBoardLclsShareReport(vo)); // 개선과제 유형별 점유율 현황 차트
			model.addAttribute("dept", dashBoardService.dashBoardDeptReport(vo)); // 제안부서 TOP 5 현황 차트
			model.addAttribute("vip", dashBoardService.dashBoardVipIssueReport(vo)); // 우수고객등급 이슈 분석
			model.addAttribute("complainDocumnet", dashBoardService.dashBoardComplainDocument(vo)); // 불만지수 분석
			
			
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		model.addAttribute("uid", uId); // uid(사번)
		model.addAttribute("guid", guId); // guid(인증키)
		model.addAttribute("sid", sId); // sid
		return "/dashBoard/portalDashBoard";
	} 
	
	*//**
	 * 포탈 불만 상세문서
	 * @param model 
	 * @param dashBoardVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/portalComplainDetailView.do", method=RequestMethod.POST)
	public String portalComplainDetailView(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{
		try {
			model.addAttribute("complainViewResult", dashBoardService.dashBoardComplainDetailView(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * 불만 상세문서
	 * @param model 
	 * @param dashBoardVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/dashBoardComplainDetailView.do", method=RequestMethod.POST)
	public String dashBoardComplainDetailView(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{
		try {
			model.addAttribute("complainViewResult", dashBoardService.dashBoardComplainDetailView(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * 이슈키워드 상세보기
	 * @param complainVo
	 * @return 차트
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/dashBoardIssueDetailView.do", method=RequestMethod.POST)
	public String dashBoardIssueDetailView(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{
		try {
			//System.out.println("이슈 클라우드 상세페이지 controller실행");
			//System.out.println("컨디션 확인 : "+vo.getCondition());
			//System.out.println("VO 확인1 : "+vo.getId());
			//System.out.println("VO 확인2 : "+vo.getUserIp());
			
			LinkedHashMap<String, String> periodMap;
			
			// 종료일
			vo.setEndDate(DateUtil.getCurrentDate("yyyyMMdd"));
			
			// 시작일
			if(vo.getCondition().equals(Globals.FIELD_WEEK)){
				//System.out.println("전주대비가 실행");
				vo.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), -7)); // 전주대비 (전주 시작일<일요일>)
			}else{
				 //System.out.println("전월대비가 실행");
				vo.setStartDate(DateUtil.addYearMonthDay("yyyyMM", vo.getEndDate().substring(0,6), Calendar.MONTH, -1) + "01"); // 전월대비 (전월 첫째날)
			}
			
			// 전주대비
			if(vo.getCondition().equals(Globals.FIELD_WEEK)){
				periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_WEEK);
			}else{
				periodMap = DateUtil.getCategories(vo.getStartDate().replace("/", "") + "000000", vo.getEndDate().replace("/", "") + "235959", "", Globals.COM_PERIOD_MONTH);
			}
			
			
			String current = "";
			
			for(String period : periodMap.keySet()){
				if(current.isEmpty()) current = period;
				if(period.compareTo(current)>0){
					vo.setCurrent(period);
				}else{
					vo.setPrevious(period);
				}
			}
			
			model.addAttribute("issueViewResult", dashBoardService.dashBoardIssueDetailView(vo));
			model.addAttribute("condition", vo.getCondition());
			
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer_chart";
	}
	
	*//**
	 * 매뉴얼 표시
	 * @param model
	 * @return "/common/help"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/dashBoard/help.do")
	public String help(Model model) throws Exception {
		return "/common/help";
	} 
	
	@RequestMapping(value="/dashBoard/regionSt.do")
	public String regionSt(Model model) throws Exception{
		
		return "/common/regionSt";
	}
	
	@RequestMapping(value="/dashBoard/emotionNl.do")
	public String emotionNl(){
		
		return "/common/emotionNl";
	}
	
	@RequestMapping(value="/dashBoard/regionSearch.do")
	public String regionSearch(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{		
		try{
			model.addAttribute("searchResultList",dashBoardService.getSearchResult(vo));
			model.addAttribute("searchSc",vo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "/common/dashboardSearchResult";
	}
	
	@RequestMapping(value="/dashBoard/regionSearchList.do")
	public String regionSearchList(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{	
		try{
			model.addAttribute("searchResultList",dashBoardService.getSearchResult(vo));
			model.addAttribute("searchSc",vo);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "/common/dashboardSearchResultList";
	}
	
	@RequestMapping(value="/dashBoard/getAlikeSearch.do")
	public String alikeSearch(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{
		
		try{
			model.addAttribute("alikeResult",dashBoardService.getAlikeSearchResult(vo));
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}
	@RequestMapping(value="/dashBoard/excelVocRegionSearchResult.do")
	public String excelVocRegionSearchResult(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{
		
		try{
			model.addAttribute("searchResultList",dashBoardService.getRegionExcelResult(vo));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "docExcelView";
	}
	
	@RequestMapping(value="/dashBoard/detailView.do")
	public String detailView(Model model, @ModelAttribute("dashBoardVo") DashBoardVo vo) throws Exception{
		
		try{
			model.addAttribute("detailViewResult",dashBoardService.getdetailViewResult(vo));
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}
	*/
}

