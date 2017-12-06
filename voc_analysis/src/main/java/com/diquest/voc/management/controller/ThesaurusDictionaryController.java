package com.diquest.voc.management.controller;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.management.service.ThesaurusDictionaryService;
import com.diquest.voc.management.vo.ThesaurusDictionaryVo;

/**
 * @Class Name : ThesaurusDictionaryController.java
 * @Description : ThesaurusDictionaryController Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.06.09           최초생성
 * 
 * @author 박소영
 * @since 2014. 06.09
 * @version 1.0
 * @see
 * 
 * 		Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class ThesaurusDictionaryController {

	/** ThesaurusDictionaryService */
	@Resource(name = "thesaurusDictionaryService")
	private ThesaurusDictionaryService thesaurusDictionaryService;

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	private static String portal_id;
	private static String portal_nm;

	/**
	 * 사전 관리 (유의어 사전 관리) 초기화면 표시
	 * 
	 * @param model
	 * @return "/management/thesaurusDictionary"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/thesaurusDictionaryInit.do")
	public String init(Model model, HttpServletRequest request) {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/management/thesaurusDictionary";
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 리스트
	 * 
	 * @param vo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectThesaurusDictionaryList.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectThesaurusDictionaryList(@RequestBody ThesaurusDictionaryVo vo, HttpServletRequest request) throws Exception {
		HashMap<String, Object> result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			result = thesaurusDictionaryService.selectThesaurusDictionaryList(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 조회
	 * 
	 * @param vo
	 * @return 유의어 사전 편집
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectThesaurusDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public ThesaurusDictionaryVo selectThesaurusDictionary(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		ThesaurusDictionaryVo result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			result = thesaurusDictionaryService.selectThesaurusDictionary(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 삭제
	 * 
	 * @param vo
	 * @return 삭제결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/deleteThesaurusDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public int deleteThesaurusDictionary(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		int result = 1;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		
		try {
			thesaurusDictionaryService.deleteThesaurusDictionary(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 적용
	 * 
	 * @param vo
	 * @return 적용결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/applyThesaurusDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public int applyThesaurusDictionary(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		int result = 1;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		
		try {
			thesaurusDictionaryService.applyThesaurusDictionary();
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 추가 편집
	 * 
	 * @param vo
	 * @return 형태소 분석 결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectThesaurusAddKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public ThesaurusDictionaryVo selectThesaurusAddKeyword(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		ThesaurusDictionaryVo result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		
		try {
			result = thesaurusDictionaryService.selectThesaurusAddKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 형태소 분석 조회
	 * 
	 * @param vo
	 * @return 형태소 분석 결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectThesaurusChKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public ThesaurusDictionaryVo selectThesaurusChKeyword(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		ThesaurusDictionaryVo result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		
		try {
			result = thesaurusDictionaryService.selectThesaurusChKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 형태소 색인어휘 추출 조회
	 * 
	 * @param vo
	 * @return 형태소 색인어휘 추출 결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/extractorThesaurusKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public ThesaurusDictionaryVo extractorThesaurusKeyword(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		ThesaurusDictionaryVo result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		
		try {
			result = thesaurusDictionaryService.extractorThesaurusKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (유의어 사전 관리) 등록
	 * 
	 * @param vo
	 * @return 등록결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/addThesaurusKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public int addThesaurusKeyword(@RequestBody ThesaurusDictionaryVo vo) throws Exception {
		int result = 0;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		
		try {
			result = thesaurusDictionaryService.addThesaurusKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

}
