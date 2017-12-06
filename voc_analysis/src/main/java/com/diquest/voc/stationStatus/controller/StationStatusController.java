package com.diquest.voc.stationStatus.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.stationStatus.service.StationStatusService;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.newSelectBoxVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class StationStatusController {

	/** StationService */
	@Resource(name = "stationStatusService")
	private StationStatusService stationStatusService;

	/** AlarmKeywordService */
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;
	
	/** keywordRankingService */
	@Resource(name = "keywordRankingService")
	private KeywordRankingService keywordRankingService;

	/**
	 * 역별 현황분석 - 초기화면
	 * 
	 * @param model
	 * @return "/stationStatus/stationStatus"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/stationStatus/stationStatusInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String synthesisRanking(Model model, HttpServletRequest request) throws Exception {

		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/stationStatus/stationStatus";
	}

	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * 
	 * @param model
	 * @return "/keywordRanking/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/stationStatus/vocSearchResult.do")
	public String vocSearchResult(Model model, @ModelAttribute("stationStatusVo") StationStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", stationStatusService.getSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/stationStatus/vocSearchResult";
	}
	
	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/stationStatus/detailView.do")
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
	@RequestMapping(value = "/stationStatus/getAlikeSearch.do")
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
	 * 검색결과 Excel-다운로드
	 * 
	 * @param model
	 * @return "/stationStatus/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/stationStatus/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("vo") StationStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", stationStatusService.getExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
	
	/**
	 * 키워드랭킹분석_하단 검색 결과(리스트만)
	 * 
	 * @param model
	 * @return "/keywordRanking/vocSearchResultList";
	 * @exception Exception
	 */
	/*@RequestMapping(value = "/stationStatus/vocSearchResultList.do")
	public String vocSearchResultList(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			model.addAttribute("searchResultList", keywordRankingService.getSearchResult(keywordRankingVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/stationStatus/vocSearchResultList";
	}*/
	
	/**
	 * 역별 현황분석 - 차트
	 * 
	 * @param model
	 * @return "/stationStatus/stationStatus"
	 * @exception Exception
	 */
	@RequestMapping(value = "/stationStatus/stationChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String stationChart(Model model, @ModelAttribute("vo") StationStatusVo vo, HttpServletRequest request) throws Exception {
		
		String jsonStation = stationStatusService.getStationChart(vo);	
		model.addAttribute("jsonData", jsonStation);
		
		return "/common/ajax";
	}
	
	/**
	 * 역별 현황분석 - 페이징
	 * 
	 * @param model
	 * @return "/stationStatus/paging"
	 * @exception Exception
	 */
	@RequestMapping(value = "/stationStatus/paging.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String paging(Model model, @ModelAttribute("vo") StationStatusVo vo, HttpServletRequest request) throws Exception {
		
	//	HashMap<String, Object> map = stationStatusService.getPaging(vo);
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.parseInt(vo.getStationCurrentPageNo()));
		paginationInfo.setRecordCountPerPage(Integer.parseInt(vo.getStationPageSize()));
		paginationInfo.setPageSize(10);
		paginationInfo.setTotalRecordCount(Integer.parseInt(vo.getStationTotalSize()));
		  
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "/stationStatus/paging";		
	}
	
	/**
	 * 역별 현황분석 - 키워드
	 * 
	 * @param model
	 * @return "/stationStatus/paging"
	 * @exception Exception
	 */
	@RequestMapping(value = "/stationStatus/stationKeyword.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String stationKeyword(Model model, @ModelAttribute("vo") StationStatusVo vo, HttpServletRequest request) throws Exception {
		
		vo.setUserId(portal_id);
		String keywords = stationStatusService.getKeywordArr(vo);
		model.addAttribute("jsonData", keywords);		
		return "/common/ajax";
	}
}
