package com.diquest.voc.management.controller;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.management.service.UserCompoundDictionaryService;
import com.diquest.voc.management.vo.UserCompoundDictionaryVo;

/**  
 * @Class Name : UserCompoundCompoundDictionaryController.java
 * @Description : UserCompoundCompoundDictionaryController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2015.08.10           최초생성
 * 
 * @author 박소영
 * @since 2015. 08.10
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class UserCompoundDictionaryController {

	/** UserCompoundDictionaryService */
	@Resource(name = "userCompoundDictionaryService")
	private UserCompoundDictionaryService userCompoundDictionaryService;
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 사전 관리 (사용자 복합어 사전 관리) 초기화면 표시
	 * @param model
	 * @return "/management/userDictionaryMgmt"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/userCompoundDictionaryInit.do")
	public String init(Model model) {
		return "/management/userCompoundDictionary";
	}
	
	/**
	 * 사전 관리 (사용자 복합어 사전 관리) 리스트
	 * @param vo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectUserCompoundDictionaryList.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectUserCompoundDictionaryList(@RequestBody UserCompoundDictionaryVo vo)
			throws Exception {
		HashMap<String, Object> result = null;
		
		try {
			result = userCompoundDictionaryService.selectUserCompoundDictionaryList(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 사전 관리 (사용자 복합어 사전 관리) 삭제
	 * @param vo
	 * @return 삭제결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/deleteUserCompoundDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public int deleteUserCompoundDictionary(@RequestBody UserCompoundDictionaryVo vo)
			throws Exception {
		int result = 1;
		
		try {
			userCompoundDictionaryService.deleteUserCompoundDictionary(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 사전 관리 (사용자 복합어 사전 관리) 적용
	 * @param vo
	 * @return 적용결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/applyUserCompoundDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public int applyUserCompoundDictionary(@RequestBody UserCompoundDictionaryVo vo)
			throws Exception {
		int result = 1;
		
		try {
			userCompoundDictionaryService.applyUserCompoundDictionary();
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 사전 관리 (사용자 복합어 사전 관리) 형태소 색인어휘 추출 조회
	 * @param vo
	 * @return 형태소 색인어휘 추출 결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/extractorUserCompoundKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public UserCompoundDictionaryVo extractorUserCompoundKeyword(@RequestBody UserCompoundDictionaryVo vo)
			throws Exception {
		UserCompoundDictionaryVo result = null;
		
		try {
			result = userCompoundDictionaryService.extractorUserCompoundKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 사전 관리 (사용자 복합어 사전 관리) 등록
	 * @param vo
	 * @return 등록결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/addUserCompoundKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public int addUserCompoundKeyword(@RequestBody UserCompoundDictionaryVo vo)
			throws Exception {
		int result = 0;
		
		try {
			result = userCompoundDictionaryService.addUserCompoundKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
}
