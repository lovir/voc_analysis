package com.diquest.voc.management.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.diquest.voc.management.service.StopwordDictionaryService;
import com.diquest.voc.management.vo.AlarmKeywordVo;
import com.diquest.voc.management.vo.StopWordDictionaryVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;


@Controller
public class StopWordDictionaryController {

	/** stopwordDictionaryService */
	@Resource(name = "stopwordDictionaryService")
	private StopwordDictionaryService stopwordDictionaryService;

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 불용어 사전관리
	 * @param model, DictionaryVo
	 * @return "/management/dictionaryManagement"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/stopwordDictionaryInit.do")
	public String stopwordInit(
			Model model
			,@ModelAttribute("dictionaryVo") StopWordDictionaryVo vo
			) throws Exception{

		try{
			DecimalFormat df = new DecimalFormat("0.##");
			
			int totalCount = stopwordDictionaryService.getStopwordCnt(vo);
			/** pageing setting */
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(vo.getCurrentPage());
			paginationInfo.setRecordCountPerPage(vo.getPageSize());
			paginationInfo.setPageSize(10);
			paginationInfo.setTotalRecordCount(totalCount);
			
			vo.setFirstRecordIndex(paginationInfo.getFirstRecordIndex());
			List stopwordList = stopwordDictionaryService.getStopwordList(vo);
			
			model.addAttribute("stopwordList", stopwordList);
			model.addAttribute("totalCount", df.format(totalCount));
			model.addAttribute("paginationInfo", paginationInfo);
		
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/management/stopwordDictionary";
	}
	
	/**
	 * 불용어 사전관리_추가시 등록된 키워드인지 체크
	 * @param model, DictionaryVo
	 * @return "/management/dictionaryManagement"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/stopwordExistCheck.do")
	public String stopwordExistCheck(
			Model model
			, HttpServletRequest request
			, @RequestParam(value="keyword",required=true) String keyword
			) throws Exception{
		try{
			//등록 체크
			int selectCount = stopwordDictionaryService.getSelectStopword(keyword.trim());	
			if(selectCount < 1){//1이상이면 존재하는거임.
				Map<String, String> map = (Map<String, String>)request.getSession().getAttribute("login");
				
				StopWordDictionaryVo vo = new StopWordDictionaryVo();
				vo.setKeyword(keyword.trim());
				vo.setLoginId(map.get("userId"));
				stopwordDictionaryService.insertStopword(vo);//인서트
			}
			model.addAttribute("jsonData", selectCount);
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/ajax";
		
	}
	
	/**
	 * 불용어 사전관리_삭제
	 * @param model, DictionaryVo
	 * @return "/management/dictionaryManagement"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/stopwordDelete.do")
	public String stopwordDelete(
		Model model
		, @RequestParam(value="checkKeyword",required=true) String[] delIdList
		, @RequestParam(value="selectedKeyword",required=true) String delKeywordList
		) throws Exception{
		
		try{
		int deleteCount = stopwordDictionaryService.deleteStopword(delIdList, delKeywordList.split(","));
		String message = deleteCount+"개 삭제함";
		model.addAttribute("jsonData", message);
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/ajax";
	
	}
	
	/**
	 * 불용어 사전관리_적용
	 * @param model, DictionaryVo
	 * @return "/management/dictionaryManagement"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/stopwordApply.do")
	public String stopwordApply(
			Model model
			, @RequestParam(value="checkKeyword",required=true) String[] applyIdList
			, @RequestParam(value="selectedKeyword",required=true) String applyKeywordList
			) throws Exception{
		
		try{
			int applyCount = stopwordDictionaryService.applyStopword(applyIdList, applyKeywordList.split(","));
			model.addAttribute("jsonData", applyCount);
		} catch(Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/ajax";
		
	}
}
