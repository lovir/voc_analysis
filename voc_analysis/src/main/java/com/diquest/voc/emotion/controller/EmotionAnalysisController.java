package com.diquest.voc.emotion.controller;

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
import com.google.gson.Gson;


@Controller
public class EmotionAnalysisController {

	/** SearchService */
	 @Resource(name = "emotionAnalysisService")
	 private EmotionAnalysisService emotionAnalysisService;
	 
	/**
	 * 감성 분석
	 * 
	 * @param model
	 * @return "/emotion/emotionAnalysis"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/emotion/emotionAnalysisInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String synthesisRanking(Model model, HttpServletRequest request) throws Exception {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/emotion/emotionAnalysis";
	}
	
	/**
	 * 감성 분석 - 추세 차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/emotionTrendChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String emtionTrendChart(Model model, HttpServletRequest request, EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		Gson gson = new Gson();
		String report = gson.toJson(emotionAnalysisService.getEmotionTrendChart(emotionAnalysisVo)); //감석 추세 차트 정보
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
	@RequestMapping(value = "/emotion/emotionDistributionChart.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String emotionDistributionChart(Model model, HttpServletRequest request, EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		Gson gson = new Gson();
		String report = gson.toJson(emotionAnalysisService.getEmotionDistributionChart(emotionAnalysisVo)); //감석 추세 차트 정보
		model.addAttribute("jsonData", report);
		return "/common/ajax";
	}
	
	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * @param model
	 * @return "/emotion/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value="/emotion/vocSearchResult.do")
	public String vocSearchResult(Model model ,@ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception{
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getSearchResult(emotionAnalysisVo));	//VOC 검색결과
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
	@RequestMapping(value = "/emotion/vocSearchResultList.do")
	public String vocSearchResultList(Model model, @ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getSearchResult(emotionAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/emotion/vocSearchResultList";
	}
	
	/**
	 * 키워드랭킹분석 검색결과 Excel-다운로드
	 * 
	 * @param model
	 * @return "/emotion/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getExcelResult(emotionAnalysisVo)); // VOC 검색결과
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
	@RequestMapping(value = "/emotion/detailView.do")
	public String detailView(Model model, @RequestParam(value = "dq_docid") String id) throws Exception { 
		try {
			model.addAttribute("detailViewResult", emotionAnalysisService.getdetailViewResult(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}

	/**
	 * 검색결과 선택한 문서의 유사문서
	 * 
	 * @param emotionAnalysisVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/getAlikeSearch.do")
	public String alikeSearch(Model model, @ModelAttribute("searchVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("alikeResult", emotionAnalysisService.getAlikeSearchResult(emotionAnalysisVo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}
	
	/**
	 * 키워드랭킹분석_처리주무부서 리스트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/repDeptList.do")
	public String getRepDeptList(Model model,  @ModelAttribute("keywordRankingVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		Gson gson = new Gson();
		String repDeptList = gson.toJson(emotionAnalysisService.getRepDeptList(emotionAnalysisVo));
		model.addAttribute("jsonData", repDeptList);
		return "/common/ajax";
	}
	
	/**
	 * 키워드랭킹분석 공통_하단 검색 결과 - 감성 추세 차트(막대) 클릭한 결과 (그룹+리스트)
	 * @param model
	 * @return "/emotion/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value="/emotion/vocClickStackSearchResult.do")
	public String vocClickStackSearchResult(Model model ,@ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception{
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getClickStackSearchResult(emotionAnalysisVo));	//VOC 검색결과
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "/emotion/vocSearchResult";
	}
	/**
	 * 키워드랭킹분석_하단 검색 결과 - 감성 추세 차트(막대) 클릭한 결과 (리스트만)
	 * 
	 * @param model
	 * @return "/emotion/vocSearchResultList";
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/dashBoardSearchResult.do")
	public String dashBoardSearchResult(Model model, @ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getDashBoardSearchResult(emotionAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/emotion/vocSearchResult";
	}
	/**
	 * 키워드랭킹분석_하단 검색 결과 - 감성 추세 차트(막대) 클릭한 결과 (리스트만)
	 * 
	 * @param model
	 * @return "/emotion/vocSearchResultList";
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/vocClickStackSearchResultList.do")
	public String vocClickStackSearchResultList(Model model, @ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getClickStackSearchResult(emotionAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/emotion/vocSearchResultList";
	}
	
	/**
	 * 키워드랭킹분석 검색결과 - 감성 추세 차트(막대) 클릭한 결과  Excel-다운로드
	 * 
	 * @param model
	 * @return "/emotion/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/emotion/excelClickStackVocSearchResult.do")
	public String excelVocClickStackSearchResult(Model model, @ModelAttribute("emotionAnalysisVo") EmotionAnalysisVo emotionAnalysisVo) throws Exception {
		emotionAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", emotionAnalysisService.getClickStackExcelResult(emotionAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}
}
