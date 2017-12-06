package com.diquest.voc.management.controller;

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

import com.diquest.voc.management.service.StandardAlarmService;
import com.diquest.voc.management.service.MailReceiverService;
import com.diquest.voc.management.vo.MailReceiverVo;
import com.diquest.voc.management.vo.StandardAlarmVo;

/**  
 * @Class Name : StandardAlarmController.java
 * @Description : StandardAlarmController Class
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
public class StandardAlarmController {

	/** StandardAlarmService */
	@Resource(name = "standardAlarmService")
	private StandardAlarmService standardAlarmService;
	
	
	/** MailReceiverService */
	@Resource(name = "mailReceiverService")
	private MailReceiverService mailReceiverService;
	
	
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	private static String portal_id;	//메트로 포탈 사용자ID
	private static String portal_nm;	//메트로 포탈 사용자 이름
	
	/**
	 * 알람설정 (발생기준 관리) 초기화면 표시
	 * 
	 * @param model
	 * @return "/management/standardAlarm"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/standardAlarmInit.do")
	public String init(Model model , HttpServletRequest request) throws Exception {
		//서울메트로는 VOC에서 별도로 로그인 관리를 하지 않음.
		//태블로 포탈 페이지에서 iFrame으로 사용자 ID와 이름만 넘겨받아 연동.
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/management/standardAlarm";
	}
	
	/**
	 * 알람설정 (발생기준 관리) 신규등록
	 * @param alarmVo
	 * @return 등록결과
	 * @exception Exception
	 */
	@RequestMapping(value="/management/addStandardAlarm.do", method=RequestMethod.POST)
	@ResponseBody
	public String addStandardAlarm(@RequestBody StandardAlarmVo alarmVo, HttpServletRequest request) throws Exception {
		String result = "";
		try {
			/*Map<String, String> map = (Map<String, String>)request.getSession().getAttribute("login");
			alarmVo.setRegId(map.get("userId"));
			*/
			alarmVo.setRegId(portal_id);
			result = standardAlarmService.insertStandardAlarm(alarmVo);
			//발생기준 신규 등록 시 CUT_ALM_MAIL 에 발송할 메일주소가 자동으로 등록되게 프로세스 추가 필요.
			//CUT_ALM_MAIL에 메일주소 등록하는 과정. 서울메트로는 메일수신자 관리 메뉴가 따로 없어서 추가함.
			MailReceiverVo mailReceiverVo = new MailReceiverVo();
			mailReceiverVo.setRegId(alarmVo.getRegId());
			mailReceiverVo.setOrg("");
			mailReceiverVo.setEmail(alarmVo.getEmail());
			mailReceiverVo.setRegYn("Y");
			if(!"".equals(alarmVo.getEmail())){
				mailReceiverService.insertMailUser(mailReceiverVo);
			}
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 알람설정 (발생기준 관리) 수정
	 * @param alarmVo
	 * @return 수정결과
	 * @exception Exception
	 */
	@RequestMapping(value="/management/updateStandardAlarm.do", method=RequestMethod.POST)
	@ResponseBody
	public int updateStandardAlarm(@RequestBody StandardAlarmVo alarmVo, HttpServletRequest request) throws Exception {
		int result = 0;
		
		try {
			/*Map<String, String> map = (Map<String, String>)request.getSession().getAttribute("login");
			alarmVo.setRegId(map.get("userId"));*/
			alarmVo.setRegId(portal_id);
			result = standardAlarmService.updateStandardAlarm(alarmVo);
			//발생기준 수정 시 CUT_ALM_MAIL 에 발송할 메일주소가 자동으로 수정되게 프로세스 추가 필요.
			//CUT_ALM_MAIL에 메일주소 등록하는 과정. 서울메트로는 메일수신자 관리 메뉴가 따로 없어서 추가함.
			MailReceiverVo mailReceiverVo = new MailReceiverVo();
			mailReceiverVo.setRegId(alarmVo.getRegId());
			mailReceiverVo.setOrg("");
			mailReceiverVo.setEmail(alarmVo.getEmail());
			mailReceiverVo.setRegYn("Y");
			if(!"".equals(alarmVo.getEmail())){	//이메일 입력란이 입력되어 있을때
				if(mailReceiverService.selectMailReceiverExist(mailReceiverVo) > 0){	//사용자가 메일정보를 이미 등록한 경우
					mailReceiverService.updateMailUser(mailReceiverVo);
				}
				else{	//신규로 등록하는 경우
					mailReceiverService.insertMailUser(mailReceiverVo);	
				}
			}
			else{	//이메일 입력 란이 비어 있을때
				mailReceiverService.deleteMailReceiver(mailReceiverVo);
			}
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 알람설정 (발생기준 관리) 상세
	 * @param alarmVo
	 * @return 상세
	 * @exception Exception
	 */
	@RequestMapping(value="/management/selectStandardAlarm.do", method=RequestMethod.POST)
	@ResponseBody
	public StandardAlarmVo selectStandardAlarm(@RequestBody StandardAlarmVo alarmVo) throws Exception {
		StandardAlarmVo result = null;
		alarmVo.setRegId(portal_id);
		
		MailReceiverVo mailReceiverVo = new MailReceiverVo();
		mailReceiverVo.setRegId(alarmVo.getRegId());
		mailReceiverVo.setOrg("");
		mailReceiverVo.setRegYn("Y");
		String email = "";
		try {
			result = standardAlarmService.selectStandardAlarm(alarmVo);
			email = mailReceiverService.selectMail(mailReceiverVo);
			if(email != null) result.setEmail(email);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
}
