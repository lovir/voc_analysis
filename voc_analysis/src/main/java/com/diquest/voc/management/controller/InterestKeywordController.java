package com.diquest.voc.management.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.management.service.InterestKeywordService;
import com.diquest.voc.management.vo.InterestKeywordVo;

/**  
 * @Class Name : InterestKeywordController.java
 * @Description : InterestKeywordController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.04.30           최초생성
 * 
 * @author 박소영
 * @since 2014. 04.30
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class InterestKeywordController {

	/** InterestKeywordService */
	@Resource(name = "interestService")
	private InterestKeywordService interestService;

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	private static String portal_id;	//메트로 포탈 사용자ID
	private static String portal_nm;	//메트로 포탈 사용자 이름
	
	/**
	 * 관심키워드 관리
	 * 
	 * @param model
	 * @return "/management/interestKeyword.do"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/interestKeywordInit.do")
	public String init(Model model, HttpServletRequest request) {

		/*Map<String, String> map = (Map<String, String>)request.getSession().getAttribute("login");
		InterestKeywordVo interestVo = new InterestKeywordVo();
		interestVo.setRegId(map.get("userId"));
		interestVo.setRegNm(map.get("userNm"));
		interestVo.setOrgNm(map.get("depNm"));*/
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		InterestKeywordVo interestVo = new InterestKeywordVo();
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		
		model.addAttribute("interestKeywordVo", interestVo);
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/management/interestKeyword";
	}

	/**
	 * 관심키워드 관리 신규등록
	 * 
	 * @param interestVo
	 * @return 등록결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/addInterestKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public int addInterestKeyword(@RequestBody InterestKeywordVo interestVo) throws Exception {
		int result = 0;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.insertInterestKeyword(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 관심키워드 관리 수정
	 * 
	 * @param interestVo
	 * @return 수정결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/updateInterestKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public int updateInterestKeyword(@RequestBody InterestKeywordVo interestVo) throws Exception {
		int result = 0;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.updateInterestKeyword(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 관심키워드 관리 삭제
	 * 
	 * @param interestVo
	 * @return 삭제결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/deleteInterestKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public int deleteInterestKeyword(@RequestBody InterestKeywordVo interestVo) throws Exception {
		int result = 0;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.deleteInterestKeyword(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 관심키워드 관리 상세
	 * 
	 * @param interestVo
	 * @return 상세
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectInterestKeyword.do", method = RequestMethod.POST)
	@ResponseBody
	public InterestKeywordVo selectInterestKeyword(@RequestBody InterestKeywordVo interestVo) throws Exception {
		InterestKeywordVo result = null;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.selectInterestKeyword(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 관심키워드 관리 리스트
	 * 
	 * @param interestVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectInterestKeywordList.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectInterestKeywordList(@RequestBody InterestKeywordVo interestVo) throws Exception {
		HashMap<String, Object> result = null;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.selectInterestKeywordList(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 관심키워드 대시보드 활성/비활성 수정
	 * 
	 * @param interestVo
	 * @return 수정결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/updateInterestKeywordDashYn.do", method = RequestMethod.POST)
	@ResponseBody
	public int updateInterestKeywordDashYn(@RequestBody InterestKeywordVo interestVo) throws Exception {
		int result = 0;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.updateInterestKeywordDashYn(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 관심키워드 활성/비활성 수정
	 * 
	 * @param interestVo
	 * @return 수정결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/updateInterestKeywordUseYn.do", method = RequestMethod.POST)
	@ResponseBody
	public int updateInterestKeywordUseYn(@RequestBody InterestKeywordVo interestVo) throws Exception {
		int result = 0;
		interestVo.setRegId(portal_id);
		interestVo.setRegNm(portal_nm);
		try {
			result = interestService.updateInterestKeywordUseYn(interestVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
}
