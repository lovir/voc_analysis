/*package com.diquest.voc.mainSearch.controller;

import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.mainSearch.service.UserSearchService;
import com.diquest.voc.mainSearch.vo.UserVo;
import com.diquest.voc.management.service.CommonSelectBoxService;

*//**  
 * @Class Name : UserSearchController.java
 * @Description : UserSearchController Class
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
public class UserSearchController {

	*//** CommonSelectBoxService *//*
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;
	
	*//** UserSearchService *//*
	@Resource(name = "userSearchService")
	private UserSearchService userSearchService;

	*//** Log Service *//*
	Logger log = Logger.getLogger(this.getClass());
	
	*//**
	 * 고객명 검색
	 * 
	 * @param model
	 * @return "/mainSearch/userSearch"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/userSearch.do", method=RequestMethod.POST)
	public String search(Model model, @ModelAttribute("userVo") UserVo vo) throws Exception{
		// 통합 검색인 경우 부족한 인자값을 기본값으로 세팅
		vo.setTerm(vo.getSearchTerm());
		// strDate = DateUtil.addYearMonthDay("yyyyMMdd", startDate, Calendar.DAY_OF_MONTH, -7);
		if(vo.getEndDate() == null || vo.getEndDate().length()<=0) vo.setEndDate(DateUtil.getCurrentDate("yyyy/MM/dd")); // 종료일
		if(vo.getStartDate() == null || vo.getStartDate().length()<=0) vo.setStartDate(DateUtil.addYearMonthDay("yyyy/MM/dd", vo.getEndDate(), Calendar.DAY_OF_MONTH, -6)); // 시작일	
		
		// 차트
		model.addAttribute("searchResultChart", userSearchService.userReport(vo));
		// 목록
		model.addAttribute("searchResultList", userSearchService.userList(vo));
		// 니즈유형
		model.addAttribute("needsTypeList", commonSelectBoxService.needsTypeList());
		// 고객명 검색
		model.addAttribute("searchCondition", "02");
		// 검색조건
		model.addAttribute("searchVo", vo);
				
		return "/mainSearch/userSearch";
	}
	
	*//**
	 * 고객명 검색_하단 검색 결과 - Excel다운로드
	 * @param model
	 * @return "/relation/vocSearchResult"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/userExcelDownload.do")
	public String userExcelDownload(Model model, @ModelAttribute("userVo") UserVo vo) throws Exception{
		try {
			vo.setTerm(vo.getSearchTerm());
			model.addAttribute("searchResultList", userSearchService.userExcelList(vo));	//VOC 검색결과
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
	@RequestMapping(value="/mainSearch/userList.do", method=RequestMethod.POST)
	public String userList(Model model, @ModelAttribute("userVo") UserVo vo) throws Exception{
		try {
			vo.setTerm(vo.getSearchTerm());
			// 목록
			model.addAttribute("searchResultList", userSearchService.userList(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/searchResult";
	}

	*//**
	 * 고객명 상세문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/userDetailView.do", method=RequestMethod.POST)
	public String userDetailView(Model model, @ModelAttribute("userVo") UserVo vo) throws Exception{
		try {
			model.addAttribute("detailViewResult", userSearchService.userDetailView(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
	
	*//**
	 * 고객명 유사문서
	 * @param model 
	 * @param trendVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 *//*
	@RequestMapping(value="/mainSearch/userAlikeView.do")
	public String userAlikeView(Model model, @ModelAttribute("userVo") UserVo vo) throws Exception{
		try {
			model.addAttribute("alikeResult", userSearchService.userAlikeView(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer";
	}
}
*/