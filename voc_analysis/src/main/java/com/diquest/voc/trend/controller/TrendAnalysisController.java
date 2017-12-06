package com.diquest.voc.trend.controller;

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

import com.diquest.voc.fieldStatus.vo.FieldStatusVo;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.stationStatus.service.StationStatusService;
import com.diquest.voc.trend.service.TrendAnalysisService;
import com.diquest.voc.trend.vo.TrendAnalysisVo;
import com.google.gson.Gson;

import egovframework.rte.psl.dataaccess.util.EgovMap;

/**  
 * @Class Name : TrendAnalysisController.java
 * @Description : TrendAnalysisController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.04.30           최초생성
 * 
 * @author 박소영
 * @since 2014. 04.30
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class TrendAnalysisController {
		
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
	 * 트랜드 분석 초기화면 표시
	 * 
	 * @param model
	 * @return "/trend/trendAnalysis"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value="/trend/trendAnalysisInit.do")
	public String init(Model model, HttpServletRequest request) throws Exception {
		
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/trend/trendAnalysis";
	} 
	/**
	 * 트렌드 분석 관심키워드 불러오기
	 * 
	 * @param model
	 * @return "/trend/trendKeyword"
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/trendKeyword.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String reportChart(Model model, @ModelAttribute("vo") FieldStatusVo vo, HttpServletRequest request) throws Exception {
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("loginId", portal_id);
		paramMap.put("limit", "10");
		List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출
		
		Gson gson = new Gson();
		model.addAttribute("jsonData", gson.toJson(keywordList));
		
		return "/common/ajax";
	}
	/**
	 * 트랜드 분석 검색
	 * @param complainVo
	 * @return 차트
	 * @exception Exception
	 */
	@RequestMapping(value="/trend/trendAnalysisSearch.do", method=RequestMethod.POST)
	public String trendSearch(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		
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
	@RequestMapping(value = "/trend/trendTotalRanking.do")
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
	@RequestMapping(value = "/trend/vocSearchResult.do")
	public String vocSearchResult(Model model, @ModelAttribute("vo") TrendAnalysisVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", trendAnalysisService.getSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/trend/vocSearchResult";
	}
	
	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/trend/detailView.do")
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
	@RequestMapping(value = "/trend/getAlikeSearch.do")
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
	@RequestMapping(value = "/trend/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("vo") TrendAnalysisVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", trendAnalysisService.getExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
	
}
	
	/**
	 * 트랜드 분석 검색_하단 검색 결과 - Excel다운로드
	 * @param model
	 * @return "/relation/vocSearchResult"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisExcelDownload.do")
	public String trendAnalysisExcelList(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		try {
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisExcelList(vo));	//VOC 검색결과
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "docExcelView";
	}
	
	*//**
	 * 불만지수 리스트(랭킹 선택)
	 * @param complainVo
	 * @return 리스트
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisListAll.do", method=RequestMethod.POST)
	public String trendAnalysisListAll(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
		
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
	@RequestMapping(value="/trend/trendAnalysisList.do", method=RequestMethod.POST)
	public String trendAnalysisList(Model model, @ModelAttribute("trendAnalysisVo") TrendAnalysisVo vo) throws Exception{
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
	 * 트랜드 분석 상세문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisDetailView.do", method=RequestMethod.POST)
	public String trendAnalysisDetailView(Model model, @ModelAttribute("trendVo") TrendAnalysisVo trendVo) throws Exception{
		try {
			model.addAttribute("detailViewResult", trendAnalysisService.trendAnalysisDetailView(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * 트랜드 분석 유사문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/trend/trendAnalysisAlikeView.do")
	public String trendAnalysisAlikeView(Model model, @ModelAttribute("trendVo") TrendAnalysisVo trendVo) throws Exception{
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