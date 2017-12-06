package com.diquest.voc.socialChannelStatus.controller;


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
import com.diquest.voc.socialChannelStatus.service.SocialChannelStatusService;
import com.diquest.voc.socialChannelStatus.vo.SocialChannelStatusVo;
import com.diquest.voc.stationStatus.service.StationStatusService;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class SocialChannelStatusController {

	/** sosicalChannelStatusService */
	@Resource(name = "socialChannelStatusService")
	private SocialChannelStatusService socialChannelStatusService;

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
	 * @return "/socialChannelStatus/socialChannelStatusInit"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/socialChannelStatus/socialChannelStatusInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String fieldStatusInit(Model model, HttpServletRequest request) throws Exception {

		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/socialChannelStatus/socialChannelStatus";
	}
	
	/*
	 * 분야별 현황분석 - 파이차트
	 * 
	 * @param model
	 * @return "/socialChannelStatus/socialChannelStatusChart"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/socialChannelStatusChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String socialStatusChart(Model model, @ModelAttribute("vo") SocialChannelStatusVo vo, HttpServletRequest request) throws Exception {
		vo.setUserId(portal_id);
		Gson gson = null;
		ArrayList<HashMap<String, String>> socialList = socialChannelStatusService.getChannelChart(vo);
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		model.addAttribute("jsonData", gson.toJson(socialList));
		return "/common/ajax";
	}
	
	/**
	 * 분야별 현황분석 - top 10 키워드
	 * 
	 * @param model
	 * @return "/fieldStatus/fieldStatusKeyword"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/socialChannelStatusKeyword.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String fieldStatusKeyword(Model model, @ModelAttribute("vo") SocialChannelStatusVo vo, HttpServletRequest request) throws Exception {
		vo.setUserId(portal_id);
		String data = socialChannelStatusService.getChannelKeywords(vo);
		
		model.addAttribute("jsonData", data);
		return "/common/ajax";
	}
	
	/**
	 * 분야별 현황분석 - 라인차트
	 * 
	 * @param model
	 * @return "/socialChannelStatus/reportChart"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/reportChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String reportChart(Model model, @ModelAttribute("vo") SocialChannelStatusVo vo, HttpServletRequest request) throws Exception {
		vo.setUserId(portal_id);
		Gson gson = new Gson();
		String report = gson.toJson(socialChannelStatusService.getSynthesisReport(vo)); // 리포트 정보
		model.addAttribute("jsonData", report);

		return "/common/ajax";
	}
	
	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * 
	 * @param model
	 * @return "/socialChannelStatus/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/vocSearchResult.do")
	public String vocSearchResult(Model model, @ModelAttribute("vo") SocialChannelStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", socialChannelStatusService.getSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/socialChannelStatus/vocSearchResult";
	}
	
	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/detailView.do")
	public String detailView(Model model, @RequestParam(value = "dq_docid") String id) throws Exception {
		try {
			model.addAttribute("detailViewResult", socialChannelStatusService.getdetailViewResult(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layerSocial";
	}
	
	// 검색결과 선택한 문서의 유사문서
	/*
	 * @param keywordRankingVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/getAlikeSearch.do")
	public String alikeSearch(Model model, @ModelAttribute("vo") SocialChannelStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		try {
			model.addAttribute("alikeResult", socialChannelStatusService.getAlikeSearchResult(vo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layerSocial";
	}
	
	
	/**
	 * 검색결과 Excel-다운로드
	 * 
	 * @param model
	 * @return "/socialChannelStatus/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialChannelStatus/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("vo") SocialChannelStatusVo vo) throws Exception {
		vo.setUserId(portal_id);
		
		try {
			model.addAttribute("searchResultList", socialChannelStatusService.getExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
	

}