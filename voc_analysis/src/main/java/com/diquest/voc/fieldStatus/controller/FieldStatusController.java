package com.diquest.voc.fieldStatus.controller;


import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.fieldStatus.service.FieldStatusService;
import com.diquest.voc.fieldStatus.vo.FieldStatusVo;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.newSelectBoxVO;
import com.diquest.voc.stationStatus.service.StationStatusService;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class FieldStatusController {

	/** FieldService */
	@Resource(name = "fieldStatusService")
	private FieldStatusService fieldStatusService;

	/** AlarmKeywordService */
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;
	
	/** keywordRankingService */
	@Resource(name = "keywordRankingService")
	private KeywordRankingService keywordRankingService;
	
	/** keywordRankingService */
	@Resource(name = "stationStatusService")
	private StationStatusService stationStatusService;

	/**
	 * 분야별 현황분석 - 초기화면
	 * 
	 * @param model
	 * @return "/fieldStatus/fieldStatusInit"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/fieldStatus/fieldStatusInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String fieldStatusInit(Model model, HttpServletRequest request) throws Exception {

		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/fieldStatus/fieldStatus";
	}
	
	/**
	 * 분야별 현황분석 - 파이차트
	 * 
	 * @param model
	 * @return "/fieldStatus/fieldStatusChart"
	 * @exception Exception
	 */
	@RequestMapping(value = "/fieldStatus/fieldStatusChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String fieldStatusChart(Model model, @ModelAttribute("vo") FieldStatusVo vo, HttpServletRequest request) throws Exception {
		
		Gson gson = null;
		vo.setpCat1Id(vo.getVocKind());
		vo.setpCat2Id(vo.getVocPart());
		ArrayList<HashMap<String, String>> fieldCodeList = fieldStatusService.getFieldChart(vo);
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		model.addAttribute("jsonData", gson.toJson(fieldCodeList));
		return "/common/ajax";
	}
	
	/**
	 * 분야별 현황분석 - top 10 키워드
	 * 
	 * @param model
	 * @return "/fieldStatus/fieldStatusKeyword"
	 * @exception Exception
	 */
	@RequestMapping(value = "/fieldStatus/fieldStatusKeyword.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String fieldStatusKeyword(Model model, @ModelAttribute("vo") FieldStatusVo vo, HttpServletRequest request) throws Exception {
		vo.setpCat1Id(vo.getVocKind());
		vo.setpCat2Id(vo.getVocPart());
		String data = fieldStatusService.getFieldKeywords(vo);
		
		model.addAttribute("jsonData", data);
		return "/common/ajax";
	}
	
	/**
	 * 분야별 현황분석 - 라인차트
	 * 
	 * @param model
	 * @return "/fieldStatus/fieldStatusKeyword"
	 * @exception Exception
	 */
	@RequestMapping(value = "/fieldStatus/reportChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String reportChart(Model model, @ModelAttribute("vo") FieldStatusVo vo, HttpServletRequest request) throws Exception {
		vo.setpCat1Id(vo.getVocKind());
		vo.setpCat2Id(vo.getVocPart());
		
		Gson gson = new Gson();
		String report = gson.toJson(fieldStatusService.getSynthesisReport(vo)); // 리포트 정보
		model.addAttribute("jsonData", report);

		return "/common/ajax";
	}
	
	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * 
	 * @param model
	 * @return "/fieldStatus/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/fieldStatus/vocSearchResult.do")
	public String vocSearchResult(Model model, @ModelAttribute("vo") FieldStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", fieldStatusService.getSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/fieldStatus/vocSearchResult";
	}
	
	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/fieldStatus/detailView.do")
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
	@RequestMapping(value = "/fieldStatus/getAlikeSearch.do")
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
	@RequestMapping(value = "/fieldStatus/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("vo") FieldStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", fieldStatusService.getExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
	

}