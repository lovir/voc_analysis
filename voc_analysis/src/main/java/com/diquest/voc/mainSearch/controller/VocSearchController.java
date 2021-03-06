/*package com.diquest.voc.mainSearch.controller;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.mainSearch.vo.VocVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.relationAnalysis.service.RelationAnalysisService;
import com.diquest.voc.relationAnalysis.vo.RelationAnalysisVo;
import com.diquest.voc.trend.service.TrendAnalysisService;
import com.diquest.voc.trend.vo.TrendAnalysisVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

*//**  
 * @Class Name : VocSearchController.java
 * @Description : VocSearchController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2015.09.08           최초생성
 * 
 * @author 박소영
 * @since 2015. 09.08
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 *//*

@Controller
public class VocSearchController {
	
	*//** CommonSelectBoxService *//*
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;
	
	*//** RelationAnalysisService *//*
	@Resource(name = "relationAnalysisService")
	private RelationAnalysisService relationAnalysisService;
	
	*//** TrendAnalysisService *//*
	@Resource(name = "trendAnalysisService")
	private TrendAnalysisService trendAnalysisService;
	
	*//** Log Service *//*
	Logger log = Logger.getLogger(this.getClass());
	
	*//**
	 * VOC 키워드 검색
	 * 
	 * @param model
	 * @return "/mainSearch/vocSearch"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/vocSearch.do", method=RequestMethod.POST)
	public String search(Model model, @ModelAttribute("vocVo") VocVo vo) throws Exception{
				
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			// 통합 검색인 경우 부족한 인자값을 기본값으로 세팅
			vo.setTerm(vo.getSearchTerm());
			if(vo.getEndDate() == null || vo.getEndDate().length()<=0) vo.setEndDate(DateUtil.getCurrentDate("yyyy/MM/dd")); // 종료일
			if(vo.getStartDate() == null || vo.getStartDate().length()<=0) vo.setStartDate(DateUtil.addYearMonthDay("yyyy/MM/dd", vo.getEndDate(), Calendar.DAY_OF_MONTH, -6)); // 시작일	
			
			// 트렌드
			TrendAnalysisVo trendVo = new TrendAnalysisVo();
			trendVo.setStartDate(vo.getStartDate());
			trendVo.setEndDate(vo.getEndDate());
			trendVo.setExclusion(vo.getExclusion());
			trendVo.setTerm(vo.getSearchTerm());
			trendVo.setCondition(vo.getCondition());
			trendVo.setPageSize(vo.getPageSize());
			trendVo.setCurrentPageNo(vo.getCurrentPageNo());
			trendVo.setNeeds(vo.getNeeds() == null || vo.getNeeds().length()<=0 ? Globals.COM_SELECT_ALL : vo.getNeeds());
			trendVo.setNeedsType(vo.getNeedsType());
			
			// 연관도
			RelationAnalysisVo relationVo = new RelationAnalysisVo();
			relationVo.setStartDate(vo.getStartDate());
			relationVo.setEndDate(vo.getEndDate());
			relationVo.setExclusion(vo.getExclusion());
			relationVo.setKeyword(vo.getSearchTerm());
			relationVo.setCondition(vo.getCondition());
			relationVo.setPageSize(Integer.parseInt(vo.getPageSize()));
			relationVo.setCurrentPage(Integer.parseInt(vo.getCurrentPageNo()));
			relationVo.setSelectNeedsType(vo.getNeeds());
			relationVo.setNeedsType(vo.getNeedsType());
			
			// 연관도 그래프
			
			// 차트 (트렌드)
			model.addAttribute("searchResultChart", trendAnalysisService.trendAnalysisReport(trendVo));
			// 차트 (연관도)
			model.addAttribute("jsonData", gson.toJson(relationAnalysisService.getRadarGraph(relationVo,  "compare")));
			
			// 목록
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisList(trendVo));
			// 니즈유형
			model.addAttribute("needsTypeList", commonSelectBoxService.needsTypeList());
			// VOC 키워드 검색
			model.addAttribute("searchCondition", "01");
			// 검색조건
			model.addAttribute("searchVo", vo);
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
				
		return "/mainSearch/vocSearch";
	}
	
	*//**
	 * VOC 키워드 검색_하단 검색 결과 - Excel다운로드
	 * @param model
	 * @return "/relation/vocSearchResult"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/vocExcelDownload.do")
	public String vocExcelDownload(Model model, @ModelAttribute("vocVo") VocVo vo) throws Exception{
		try {
			vo.setTerm(vo.getSearchTerm());
			// 트렌드
			TrendAnalysisVo trendVo = new TrendAnalysisVo();
			trendVo.setStartDate(vo.getStartDate());
			trendVo.setEndDate(vo.getEndDate());
			trendVo.setExclusion(vo.getExclusion());
			trendVo.setTerm(vo.getSearchTerm());
			trendVo.setCondition(vo.getCondition());
			trendVo.setPageSize(vo.getPageSize());
			trendVo.setCurrentPageNo(vo.getCurrentPageNo());
			trendVo.setNeeds(Globals.COM_SELECT_ALL);
			trendVo.setNeedsType(vo.getNeedsType());
			
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisExcelList(trendVo));	//VOC 검색결과
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "docExcelView";
	}

	*//**
	 * 불만지수 리스트(탭 선택, 페이징 선택)
	 * @param complainVo
	 * @return 리스트
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/vocList.do", method=RequestMethod.POST)
	public String vocList(Model model, @ModelAttribute("vocVo") VocVo vo) throws Exception{
		try {
			vo.setTerm(vo.getSearchTerm());
			// 트렌드
			TrendAnalysisVo trendVo = new TrendAnalysisVo();
			trendVo.setStartDate(vo.getStartDate());
			trendVo.setEndDate(vo.getEndDate());
			trendVo.setExclusion(vo.getExclusion());
			trendVo.setTerm(vo.getSearchTerm());
			trendVo.setCondition(vo.getCondition());
			trendVo.setPageSize(vo.getPageSize());
			trendVo.setCurrentPageNo(vo.getCurrentPageNo());
			trendVo.setNeeds(vo.getNeeds() == null || vo.getNeeds().length()<=0 ? Globals.COM_SELECT_ALL : vo.getNeeds());
			trendVo.setNeedsType(vo.getNeedsType());
			
			// 목록
			model.addAttribute("searchResultList", trendAnalysisService.trendAnalysisList(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/searchResult";
	}

	*//**
	 * VOC 키워드 상세문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/vocDetailView.do", method=RequestMethod.POST)
	public String vocDetailView(Model model, @ModelAttribute("vocVo") VocVo vo) throws Exception{
		try {
			// 트렌드
			TrendAnalysisVo trendVo = new TrendAnalysisVo();
			trendVo.setId(vo.getId());
			
			model.addAttribute("detailViewResult", trendAnalysisService.trendAnalysisDetailView(trendVo));
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * VOC 키워드 유사문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/vocAlikeView.do")
	public String vocAlikeView(Model model, @ModelAttribute("vocVo") VocVo vo) throws Exception{
		try {
			// 트렌드
			TrendAnalysisVo trendVo = new TrendAnalysisVo();
			trendVo.setTitle(vo.getTitle());
			trendVo.setContent(vo.getContent());
			trendVo.setId(vo.getId());
			
			model.addAttribute("alikeResult", trendAnalysisService.trendAnalysisAlikeView(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
}
*/