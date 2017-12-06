package com.diquest.voc.trend.controller;

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

import com.diquest.voc.fieldStatus.vo.FieldStatusVo;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.stationStatus.service.StationStatusService;
import com.diquest.voc.trend.service.TrendAnalysisService;
import com.diquest.voc.trend.vo.TrendAnalysisVo;
import com.google.gson.Gson;

/**  
 * @Class Name : TrendAnalysisCompareController.java
 * @Description : TrendAnalysisCompareController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2017.11.22           최초생성
 * 
 * @author 신누리
 * @since 2017.11.22 
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class TrendAnalysisCompareController {
	
	/** CommonSelectBoxService */
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;

	/** TrendAnalysisService */
	@Resource(name = "trendAnalysisService")
	private TrendAnalysisService trendAnalysisService;

	/** KeywordRankingService */
	@Resource(name = "keywordRankingService")
	private KeywordRankingService keywordRankingService;
	
	/** KeywordRankingService */
	@Resource(name = "stationStatusService")
	private StationStatusService stationStatusService;
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 동향정보 비교 분석 초기화면 표시
	 * 
	 * @param model
	 * @return "/trend/trendAnalysisCompare"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value="/trend/trendAnalysisCompareInit.do")
	public String init(Model model, HttpServletRequest request)throws Exception {
		
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명

		try {
			// 대분류(성격유형) 
			/*model.addAttribute("characterTypeList", commonSelectBoxService.characterTypeList());
			// 중분류(업무유형) 
			model.addAttribute("businessTypeList", commonSelectBoxService.businessTypeList());
			// 니즈유형
			model.addAttribute("needsTypeList", commonSelectBoxService.needsTypeList());
			// 이용매체유형
			model.addAttribute("usesMediaTypeList", commonSelectBoxService.usesMediaTypeList());
			// 관심키워드
			model.addAttribute("interestList", trendAnalysisService.selectKeywordList(null));*/
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		
		return "/trend/trendAnalysisCompare";
	}
	/**
	 * 트랜드 분석 검색
	 * @param complainVo
	 * @return 차트
	 * @exception Exception
	 */
	@RequestMapping(value="/trend/trendCompareAnalysisSearch.do", method=RequestMethod.POST)
	public String trendSearch(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		vo.setUserId(portal_id);
		String chart = trendAnalysisService.trendAnalysisReport(vo);	
		// 차트
		model.addAttribute("jsonData", chart);
		return "/common/ajax";
	}
	
	/**
	 * 키워드랭킹분석_종합랭킹분석 사용
	 * 
	 * @param model
	 * @return "/trend/trendTotalRanking"
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/trendCompareTotalRanking.do")
	public String totalRanking(Model model, @ModelAttribute("vo") TrendAnalysisVo vo) throws Exception {
		vo.setUserId(portal_id);
		Gson gson = null;
		try {
		
			LinkedHashMap<String, Object> synthesisRanking = new LinkedHashMap<String, Object>();
			// 종합랭킹일경우
			synthesisRanking = trendAnalysisService.getSynthesisRanking(vo);
			model.addAttribute("pageType", "N");
			model.addAttribute("synthesisRanking", synthesisRanking);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/trend/trendAnalysis_total";
	}
	
	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * 
	 * @param model
	 * @return "/fieldStatus/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/vocSearchResultCompare.do")
	public String vocSearchResult(Model model, @ModelAttribute("vo") TrendAnalysisVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", trendAnalysisService.getSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/trend/vocSearchResultCompare";
	}
	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/detailViewCompare.do")
	public String detailView(Model model, @RequestParam(value = "dq_docid") String id) throws Exception {
		try {
			model.addAttribute("detailViewResult", stationStatusService.getdetailViewResult(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}
	
	/* 검색결과 선택한 문서의 유사문서
	 * 
	 * @param keywordRankingVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/getAlikeSearchCompare.do")
	public String alikeSearch(Model model, @ModelAttribute("searchVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("alikeResult", keywordRankingService.getAlikeSearchResult(keywordRankingVo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}
	
	/**
	 * 키워드랭킹분석 검색결과 Excel-다운로드
	 * 
	 * @param model
	 * @return "/keywordRanking/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/excelVocSearchResultCompare.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("vo") TrendAnalysisVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", trendAnalysisService.getExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
	/**
	 * 트렌드 분석 관심키워드 불러오기
	 * 
	 * @param model
	 * @return "/trend/trendKeyword"
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/trendKeywordCompare.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String reportChart(Model model, @ModelAttribute("vo") FieldStatusVo vo, HttpServletRequest request) throws Exception {
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("loginId", portal_id);
		paramMap.put("limit", "10");
		List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출
		
		Gson gson = new Gson();
		model.addAttribute("jsonData", gson.toJson(keywordList));
		
		return "/common/ajax";
	}
}
	/*
	*//**
	 * 트랜드 분석 검색
	 * @param complainVo
	 * @return 차트
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisCompareSearch.do", method=RequestMethod.POST)
	public String trendAnalysisCompareSearch(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		
		try {
			
			// 차트
			model.addAttribute("searchResultChart", trendAnalysisService.trendAnalysisReport(vo));
			// 랭킹
			model.addAttribute("searchResultRanking", trendAnalysisService.trendAnalysisRanking(vo));
			// 목록
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisList(vo));
			
			// 대분류(성격유형) 
			model.addAttribute("characterTypeList", commonSelectBoxService.characterTypeList());
			// 중분류(업무유형) 
			model.addAttribute("businessTypeList", commonSelectBoxService.businessTypeList());
			// 니즈유형
			model.addAttribute("needsTypeList", commonSelectBoxService.needsTypeList());
			// 이용매체유형
			model.addAttribute("usesMediaTypeList", commonSelectBoxService.usesMediaTypeList());
			// 관심키워드
			model.addAttribute("interestList", trendAnalysisService.selectKeywordList(null));
			// 검색조건
			model.addAttribute("searchVo", vo);
			
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/trend/searchResultCompare";
	}
	
	*//**
	 * 불만지수 리스트(랭킹 선택)
	 * @param complainVo
	 * @return 리스트
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisCompareListAll.do", method=RequestMethod.POST)
	public String trendAnalysisCompareListAll(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		try {
			// 목록
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisList(vo));
			// 검색조건
			model.addAttribute("searchVo", vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/searchResultList";
	}

	*//**
	 * 불만지수 리스트(탭 선택, 페이징 선택)
	 * @param complainVo
	 * @return 리스트
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisCompareList.do", method=RequestMethod.POST)
	public String trendAnalysisCompareList(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		try {
			// 목록
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisList(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/searchResult";
	}
	
	*//**
	 * 동향정보 비교 분석 상세문서
	 * @param model
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisCompareDetailView.do", method=RequestMethod.POST)
	public String trendAnalysisCompareDetailView(Model model, @ModelAttribute("trendVo") TrendAnalysisVo trendVo) throws Exception{
		try {
			model.addAttribute("detailViewResult", trendAnalysisService.trendAnalysisDetailView(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * 동향정보 비교 분석 유사문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisCompareAlikeView.do")
	public String trendAnalysisCompareAlikeView(Model model, @ModelAttribute("trendVo") TrendAnalysisVo trendVo) throws Exception{
		try {
			model.addAttribute("alikeResult", trendAnalysisService.trendAnalysisAlikeView(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
}
*/