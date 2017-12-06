package com.diquest.voc.common.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.CommonService;
import com.diquest.voc.dashBoard.vo.ContryMInwonTotalVO;
import com.diquest.voc.dashBoard.vo.DashBoardVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;

import egovframework.rte.fdl.idgnr.impl.Base64;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * @Class Name : CommonController.java
 * @Description : CommonController Class
 * @Modification Information @ @ 수정일 수정자 수정내용 @ --------- ---------
 *               ------------------------------- @ 2015.09.01 최초생성
 * 
 * @author 박소영
 * @since 2015. 09.01
 * @version 1.0
 * @see Copyright (C) by DIQUEST All right reserved.
 */
@Controller
public class CommonController {

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	/** CommonService */
	@Resource(name = "commonService")
	private CommonService commonService;

	/** CommonSelectBoxService */
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;
	/**
	 * 로그인 초기화면 표시
	 * 
	 * @param model
	 * @return "/common/login"
	 * @exception Exception
	 */
	@RequestMapping(value = "/common/loginInit.do")
	public String loginInit(Model model, HttpServletRequest request) throws Exception {
		return "/common/login";
	}

	/**
	 * SSO 로그인
	 * 
	 * @param model
	 * @return "/common/ssoLogin.do"
	 * @exception Exception
	 */
	@RequestMapping(value = "/common/ssoLogin.do")
	public String ssoLogin(Model model, HttpServletRequest request) throws Exception {
		String uId = request.getParameter("uid") == null ? "" : request.getParameter("uid"); // uid(사번)
		String guId = request.getParameter("guid") == null ? "" : request.getParameter("guid"); // guid(인증키)
		String sId = request.getParameter("sid") == null ? "4" : request.getParameter("sid"); // sid

		String returnMsg = "";

		try {

			if (!"".equals(uId) && !"".equals(guId)) {

				// 앞에서 체크하고 넘어온 uId이므로 별도로 SSO를 태우지 않고 로그인 처리
				// SSO는 불필요해졌으므로 주석처리
				/*
				 * returnMsg = commonService.ssoLogin(uId, guId, sId);
				 * 
				 * if("N".equals(returnMsg)){ model.addAttribute("sso",
				 * "fail_SSO"); return "/common/login"; }
				 */

				EgovMap login = commonService.selectIdLogin(uId);
				if (login != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userIp", getClienIp(request));
					map.put("userId", login.get("userId"));
					map.put("userNm", login.get("userNm"));
					map.put("depCd", login.get("depCd"));
					map.put("depNm", login.get("depNm"));
					map.put("pstNm", login.get("pstNm"));

					HttpSession session = request.getSession(false);

					if (session != null) {
						session.invalidate(); // 초기화
					}

					session = request.getSession(true); // 새로 생성

					session.setAttribute("login", map);
					log.debug("[VOC Analysis(SSO Login) Session Checker] ServletContext :" + session.getServletContext());
					log.debug("[VOC Analysis(SSO Login) Session Checker] MaxInactiveInterval :" + session.getMaxInactiveInterval());
					log.debug("[VOC Analysis(SSO Login) Session Checker] LastAccessedTime :" + session.getLastAccessedTime());
					log.debug("[VOC Analysis(SSO Login) Session Checker] login :" + session.getAttribute("login"));
					log.debug("[VOC Analysis(SSO Login) Session Checker] ID :" + session.getId());

				} else {
					model.addAttribute("sso", "fail_VOC");
					return "/common/login";
				}
			} else {
				model.addAttribute("sso", "fail_VOC");
				return "/common/login";
			}

		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}

		return "redirect:/dashBoard/init.do";
	}

	/**
	 * 로그인 (AD로그인 or 아이디 로그인)
	 * 
	 * @param model
	 * @return "/common/login"
	 * @exception Exception
	 */
	@RequestMapping(value = "/common/login.do")
	public String login(@RequestParam("userID") String userID, @RequestParam("password") String userPass, HttpServletRequest request, Model model) throws Exception {

		System.out.println("-------------------------");

		userID = userID.toUpperCase();

		String returnUrl = "/common/login";
//		System.out.println("userID:" + userID);
		System.out.println("userPW:" + userPass);
		String code = commonService.base64LoginCheker(userID, userPass);
//		System.out.println("code:" + code);
		if (null != code && code.equals("P")) {
			// if (code.equals("200") || userID.equals("TEST")) {
			log.debug("[VOC Analysis(AD Login) code - " + code + "]");
			try {
				EgovMap login = commonService.selectLogin(userID);
				log.debug("[VOC Analysis(AD Login) ] login Info : " + login);
				if (login != null) {
					log.debug("[VOC Analysis(AD Login)] Login Success!!!");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userIp", getClienIp(request));
					map.put("userId", login.get("userId"));
					map.put("userNm", login.get("userNm"));
					map.put("depCd", login.get("depCd"));
					map.put("depNm", login.get("depNm"));
					map.put("pstNm", login.get("pstNm"));

					HttpSession session = request.getSession(false);

					if (session != null) {
						session.invalidate(); // 초기화
					}

					session = request.getSession(true); // 새로 생성

					session.setAttribute("login", map);
					log.debug("[VOC Analysis(AD Login) Session Checker] ServletContext :" + session.getServletContext());
					log.debug("[VOC Analysis(AD Login) Session Checker] MaxInactiveInterval :" + session.getMaxInactiveInterval());
					log.debug("[VOC Analysis(AD Login) Session Checker] LastAccessedTime :" + session.getLastAccessedTime());
					log.debug("[VOC Analysis(AD Login) Session Checker] login :" + session.getAttribute("login"));
					log.debug("[VOC Analysis(AD Login) Session Checker] ID :" + session.getId());

					returnUrl = "redirect:/dashBoard/init.do";
				} else {
					log.debug("[VOC Analysis(AD Login)] Login Fail !!!");
					model.addAttribute("adlogin", "fail_VOC");
				}
			} catch (Exception e) {
				log.error("Exception : " + e);
				throw e;
			}
		} else {
			log.debug("[VOC Analysis(AD Login) code - " + code + "] Login Fail!!!");
			model.addAttribute("adlogin", "fail_AD");
		}
		return returnUrl;
	}

	/**
	 * 로그아웃
	 * 
	 * @param model
	 * @return "/common/logout"
	 * @exception Exception
	 */
	@RequestMapping(value = "/common/logout.do")
	public String logout(Model model, HttpServletRequest request) throws Exception {
		request.getSession().removeAttribute("login");
		request.getSession().invalidate(); // 초기화
		return "redirect:/common/loginInit.do";
	}

	public String getClienIp(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}

		if ("0:0:0:0:0:0:0:1".equals(ip))
			ip = "127.0.0.1";

		return ip;

	}
	
	/**
	 * 서울메트로 초기화면 표시
	 * @param model
	 * @return "/common/metro_main"
	 * @exception Exception
	 */
	@RequestMapping(value="/common/metro_main.do")
	public String init(Model model, HttpServletRequest request) throws Exception {
		return "/common/metro_main";
	} 
	
	/*
	 * select 옵션 리스트
	 * 
	 * @param request
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/common/selectOptionList.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectOptionList(Model model, HttpServletRequest request) throws Exception {
		
		String type = request.getParameter("type") == null ? "" : request.getParameter("type"); // 셀렉트박스 구분
		String code = request.getParameter("code") == null ? "" : request.getParameter("code"); // 셀렉트박스 값
		String codePart = request.getParameter("codePart") == null ? "" : request.getParameter("codePart"); // 셀렉트박스2 값
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<CommonSelectBoxVo> vocChannelList = commonSelectBoxService.vocComboChannel();
		List<CommonSelectBoxVo> vocRecTypeList = commonSelectBoxService.vocComboRect();
		List<CommonSelectBoxVo> vocKindList = null;
		List<CommonSelectBoxVo> vocPartList = null;
		List<CommonSelectBoxVo> vocItemList = null;
		
		if(type != null){
			// 접수채널 선택 시 대분류 세팅
			if(type.equals("CHANNEL")){		
				if(code.equals("CDVOCCHANNEL")){
					vocKindList = commonSelectBoxService.vocKindList();					
				}else if(code.equals("CALL_TYPE")){
					vocKindList = commonSelectBoxService.vocCallKindList();
				}
			}
			// 대분류 선택 시 중분류 세팅 - 대분류 민원
			else if(type.equals("MINWON_KIND")){	
				CommonSelectBoxVo vo = new CommonSelectBoxVo();
				vo.setpCat1Id(code);
				vocPartList= commonSelectBoxService.vocComboPart(vo);
			}
			// 대분류 선택 시 중분류 세팅 - 대분류 콜센터
			else if(type.equals("CALL_KIND")){
				CommonSelectBoxVo vo = new CommonSelectBoxVo();
				vo.setpCat1Id(code);
				vocPartList= commonSelectBoxService.vocComboCallPart(vo);
			}
			// 중분류 선택시 소분류 세팅 - 대분류 민원
			else if(type.equals("MINWON_PART")){
				CommonSelectBoxVo vo = new CommonSelectBoxVo();
				vo.setpCat1Id(code);
				vo.setpCat2Id(codePart);
				vocItemList= commonSelectBoxService.vocComboItem(vo);
			}
			// 중분류 선택시 소분류 세팅 - 대분류 콜센터
			else if(type.equals("CALL_PART")){
				CommonSelectBoxVo vo = new CommonSelectBoxVo();
				vo.setpCat1Id(code);
				vo.setpCat2Id(codePart);
				vocItemList= commonSelectBoxService.vocComboCallItem(vo);
			}
		}
		
		result.put("vocChannelList", vocChannelList);
		result.put("vocRecTypeList", vocRecTypeList);
		result.put("vocKindList", vocKindList);
		result.put("vocPartList", vocPartList);
		result.put("vocItemList", vocItemList);

		return result;
	}
	/*
	 * select 옵션 리스트 - 소셜
	 * 
	 * @param request
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/common/selectOptionListSocial.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectOptionListSocial(Model model, HttpServletRequest request) throws Exception {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		List<CommonSelectBoxVo> socialChannelList = null;
		
		socialChannelList = commonSelectBoxService.socialChannelList();
		result.put("socialChannelList", socialChannelList);


		return result;
	}
}
