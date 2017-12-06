package com.diquest.voc.socialEmotion.controller;

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

import com.diquest.voc.emotion.service.EmotionAnalysisService;
import com.diquest.voc.emotion.vo.EmotionAnalysisVo;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.socialChannelStatus.service.SocialChannelStatusService;
import com.diquest.voc.socialChannelStatus.vo.SocialChannelStatusVo;
import com.diquest.voc.socialEmotion.service.SocialEmotionAnalysisService;
import com.diquest.voc.socialEmotion.vo.SocialEmotionAnalysisVo;
import com.diquest.voc.stationStatus.service.StationStatusService;
import com.google.gson.Gson;


@Controller
public class SocialEmotionAnalysisController {

	/** SearchService */
	 @Resource(name = "socialEmotionAnalysisService")
	 private SocialEmotionAnalysisService socialEmotionAnalysisService;
 
	/** sosicalChannelStatusService */
	@Resource(name = "socialChannelStatusService")
	private SocialChannelStatusService socialChannelStatusService;
	
	/**
	 * 감성 분석
	 * 
	 * @param model
	 * @return "/emotion/emotionAnalysis"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/socialEmotion/socialEmotionAnalysisInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String synthesisRanking(Model model, HttpServletRequest request) throws Exception {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/socialEmotion/socialEmotionAnalysis";
	}
/*	
	/**
	 * 감성 분석 - 추세 차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/socialEmotionTrendChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String emtionTrendChart(Model model, HttpServletRequest request, SocialEmotionAnalysisVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		Gson gson = new Gson();
		String report = gson.toJson(socialEmotionAnalysisService.getEmotionTrendChart(vo)); //감석 추세 차트 정보
		model.addAttribute("jsonData", report);
		return "/common/ajax";
	}
	
	/**
	 * 감성 분석 - 부정 감성 분포 차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/socialEmotionDistributionChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String emotionDistributionChart(Model model, HttpServletRequest request, SocialEmotionAnalysisVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		Gson gson = new Gson();
		String report = gson.toJson(socialEmotionAnalysisService.getEmotionDistributionChart(vo)); //감석 추세 차트 정보
		model.addAttribute("jsonData", report);
		return "/common/ajax";
	}
	
	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * @param model
	 * @return "/emotion/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value="/socialEmotion/vocSearchResult.do")
	public String vocSearchResult(Model model ,@ModelAttribute("vo") SocialEmotionAnalysisVo vo) throws Exception{
		vo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", socialEmotionAnalysisService.getSearchResult(vo));	//VOC 검색결과
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "/emotion/vocSearchResult";
	}

	/**
	 * 키워드랭킹분석_하단 검색 결과(리스트만)
	 * 
	 * @param model
	 * @return "/emotion/vocSearchResultList";
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/vocSearchResultList.do")
	public String vocSearchResultList(Model model, @ModelAttribute("vo") SocialEmotionAnalysisVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", socialEmotionAnalysisService.getSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/socialEmotion/vocSearchResultList";
	}
	
	/**
	 * 키워드랭킹분석 검색결과 Excel-다운로드
	 * 
	 * @param model
	 * @return "/emotion/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("vo") SocialEmotionAnalysisVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", socialEmotionAnalysisService.getExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}

	
	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/detailView.do")
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
	@RequestMapping(value = "/socialEmotion/getAlikeSearch.do")
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
	 * 키워드랭킹분석 공통_하단 검색 결과 - 감성 추세 차트(막대) 클릭한 결과 (그룹+리스트)
	 * @param model
	 * @return "/emotion/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value="/socialEmotion/vocClickStackSearchResult.do")
	public String vocClickStackSearchResult(Model model ,@ModelAttribute("vo") SocialEmotionAnalysisVo vo) throws Exception{
		vo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", socialEmotionAnalysisService.getClickStackSearchResult(vo));	//VOC 검색결과
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "/socialEmotion/vocSearchResult";
	}

	/**
	 * 키워드랭킹분석_하단 검색 결과 - 감성 추세 차트(막대) 클릭한 결과 (리스트만)
	 * 
	 * @param model
	 * @return "/emotion/vocSearchResultList";
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/vocClickStackSearchResultList.do")
	public String vocClickStackSearchResultList(Model model, @ModelAttribute("vo") SocialEmotionAnalysisVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", socialEmotionAnalysisService.getClickStackSearchResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/socialEmotion/vocSearchResultList";
	}
	
	/**
	 * 키워드랭킹분석 검색결과 - 감성 추세 차트(막대) 클릭한 결과  Excel-다운로드
	 * 
	 * @param model
	 * @return "/emotion/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/socialEmotion/excelClickStackVocSearchResult.do")
	public String excelVocClickStackSearchResult(Model model, @ModelAttribute("vo") SocialEmotionAnalysisVo vo) throws Exception {
		vo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", socialEmotionAnalysisService.getClickStackExcelResult(vo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
	
}
