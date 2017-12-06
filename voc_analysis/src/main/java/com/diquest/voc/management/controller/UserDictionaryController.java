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

import com.diquest.voc.management.service.UserDictionaryService;
import com.diquest.voc.management.vo.UserDictionaryVo;

/**
 * @Class Name : UserDictionaryController.java
 * @Description : UserDictionaryController Class
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
public class UserDictionaryController {

	/** UserDictionaryService */
	@Resource(name = "userDictionaryService")
	private UserDictionaryService userDictionaryService;

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	private static String portal_id;
	private static String portal_nm;

	/**
	 * 사전 관리 (사용자 사전 관리) 초기화면 표시
	 * 
	 * @param model
	 * @return "/management/userDictionaryMgmt"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/userDictionaryInit.do")
	public String init(Model model, HttpServletRequest request) {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/management/userDictionary";
	}

	/**
	 * 사전 관리 (사용자 사전 관리) 리스트
	 * 
	 * @param vo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectUserDictionaryList.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectUserDictionaryList(@RequestBody UserDictionaryVo vo) throws Exception {
		HashMap<String, Object> result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			result = userDictionaryService.selectUserDictionaryList(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (사용자 사전 관리) 삭제
	 * 
	 * @param vo
	 * @return 삭제결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/deleteUserDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public int deleteUserDictionary(@RequestBody UserDictionaryVo vo) throws Exception {
		int result = 1;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			userDictionaryService.deleteUserDictionary(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (사용자 사전 관리) 적용
	 * 
	 * @param vo
	 * @return 적용결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/applyUserDictionary.do", method = RequestMethod.POST)
	@ResponseBody
	public int applyUserDictionary(@RequestBody UserDictionaryVo vo) throws Exception {
		int result = 1;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			userDictionaryService.applyUserDictionary();
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (사용자 사전 관리) 형태소 색인어휘 추출 조회
	 * 
	 * @param vo
	 * @return 형태소 색인어휘 추출 결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/extractorUserKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public UserDictionaryVo extractorUserKeyword(@RequestBody UserDictionaryVo vo) throws Exception {
		UserDictionaryVo result = null;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			result = userDictionaryService.extractorUserKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 사전 관리 (사용자 사전 관리) 등록
	 * 
	 * @param vo
	 * @return 등록결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/addUserKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public int addUserKeyword(@RequestBody UserDictionaryVo vo) throws Exception {
		int result = 0;
		vo.setRegId(portal_id);
		vo.setRegNm(portal_nm);
		try {
			result = userDictionaryService.addUserKeyword(vo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
}
