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
import com.diquest.voc.mainSearch.vo.VoeVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.socialTrend.service.SocialTrendAnalysisService;
import com.diquest.voc.socialTrend.vo.SocialTrendAnalysisVo;


*//**  
 * @Class Name : VoeSearchController.java
 * @Description : VoeSearchController Class
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
public class VoeSearchController {
	
	*//** CommonSelectBoxService *//*
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;
	
	*//** VoeTrendAnalysisService *//*
	@Resource(name = "voeTrendAnalysisService")
	private SocialTrendAnalysisService voeTrendAnalysisService;
	
	*//** Log Service *//*
	Logger log = Logger.getLogger(this.getClass());
	
	*//**
	 * VOE 키워드 검색
	 * 
	 * @param model
	 * @return "/mainSearch/userSearch"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/voeSearch.do", method=RequestMethod.POST)
	public String search(Model model, @ModelAttribute("VoeTrendAnalysisServiceVo") SocialTrendAnalysisVo vo) throws Exception {
		
		try {
			// 통합 검색인 경우 부족한 인자값을 기본값으로 세팅
			vo.setTerm(vo.getSearchTerm());
			if(vo.getEndDate() == null || vo.getEndDate().length()<=0) vo.setEndDate(DateUtil.getCurrentDate("yyyy/MM/dd")); // 종료일
			if(vo.getStartDate() == null || vo.getStartDate().length()<=0) vo.setStartDate(DateUtil.addYearMonthDay("yyyy/MM/dd", vo.getEndDate(), Calendar.DAY_OF_MONTH, -6)); // 시작일
			
			// 트렌드
			SocialTrendAnalysisVo trendVo = new SocialTrendAnalysisVo();
			trendVo.setStartDate(vo.getStartDate());
			trendVo.setEndDate(vo.getEndDate());
			trendVo.setExclusion(vo.getExclusion());
			trendVo.setTerm(vo.getSearchTerm());
			trendVo.setCondition(vo.getCondition());
			trendVo.setPageSize(vo.getPageSize());
			trendVo.setCurrentPageNo(vo.getCurrentPageNo());
			trendVo.setLcls(vo.getLcls() == null || vo.getLcls().length()<=0 ? Globals.COM_SELECT_ALL : vo.getLcls());
			trendVo.setNeedsType(vo.getNeedsType());

			// 차트
			model.addAttribute("searchResultChart", voeTrendAnalysisService.trendKeywordReport(trendVo));	
			// 목록
			model.addAttribute("searchResultList", voeTrendAnalysisService.trendKeywordList(trendVo));
			// VOE 대분류 조회 
			model.addAttribute("userLCLSType", commonSelectBoxService.userLCLSTypeList());
			// 검색조건
			model.addAttribute("searchVo", vo);
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}

		return "/mainSearch/voeSearch";
	} 
	
	*//**
	 * voe 키워드 검색_하단 검색 결과 - Excel다운로드
	 * @param model
	 * @return "/relation/voeSearchResult"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/voeExcelDownload.do")
	public String voeExcelDownload(Model model, @ModelAttribute("voeVo") VoeVo vo) throws Exception{
		try {
			vo.setTerm(vo.getSearchTerm());
			// 트렌드
			SocialTrendAnalysisVo trendVo = new SocialTrendAnalysisVo();
			trendVo.setStartDate(vo.getStartDate());
			trendVo.setEndDate(vo.getEndDate());
			trendVo.setExclusion(vo.getExclusion());
			trendVo.setTerm(vo.getSearchTerm());
			trendVo.setCondition(vo.getCondition());
			trendVo.setPageSize(vo.getPageSize());
			trendVo.setCurrentPageNo(vo.getCurrentPageNo());
			trendVo.setLcls(Globals.COM_SELECT_ALL);
			trendVo.setNeedsType(vo.getNeedsType());
			
			model.addAttribute("searchResultList", voeTrendAnalysisService.trendKeywordAnalysisExcelList(trendVo));	//voe 검색결과
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
	@RequestMapping(value="/mainSearch/voeList.do", method=RequestMethod.POST)
	public String voeList(Model model, @ModelAttribute("voeVo") VoeVo vo) throws Exception{
		try {
			vo.setTerm(vo.getSearchTerm());
			// 트렌드
			SocialTrendAnalysisVo trendVo = new SocialTrendAnalysisVo();
			trendVo.setStartDate(vo.getStartDate());
			trendVo.setEndDate(vo.getEndDate());
			trendVo.setExclusion(vo.getExclusion());
			trendVo.setTerm(vo.getSearchTerm());
			trendVo.setCondition(vo.getCondition());
			trendVo.setPageSize(vo.getPageSize());
			trendVo.setCurrentPageNo(vo.getCurrentPageNo());
			trendVo.setLcls(vo.getLcls() == null || vo.getLcls().length()<=0 ? Globals.COM_SELECT_ALL : vo.getLcls());
			trendVo.setNeedsType(vo.getNeedsType());
			
			// 목록
			model.addAttribute("searchResultList", voeTrendAnalysisService.trendKeywordList(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/searchResult";
	}

	*//**
	 * voe 키워드 상세문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/voeDetailView.do", method=RequestMethod.POST)
	public String voeDetailView(Model model, @ModelAttribute("voeVo") VoeVo vo) throws Exception{
		try {
			// 트렌드
			SocialTrendAnalysisVo trendVo = new SocialTrendAnalysisVo();
			trendVo.setId(vo.getId());
			
			model.addAttribute("detailViewResult", voeTrendAnalysisService.trendKeywordDetailView(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * voe 키워드 유사문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/voeAlikeView.do")
	public String voeAlikeView(Model model, @ModelAttribute("voeVo") VoeVo vo) throws Exception{
		try {
			// 트렌드
			SocialTrendAnalysisVo trendVo = new SocialTrendAnalysisVo();
			trendVo.setTitle(vo.getTitle());
			trendVo.setContent(vo.getContent());
			trendVo.setId(vo.getId());
			
			model.addAttribute("alikeResult", voeTrendAnalysisService.trendKeywordAlikeView(trendVo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
}
*/