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

import com.diquest.voc.management.service.MailReceiverService;
import com.diquest.voc.management.vo.MailReceiverVo;

/**  
 * @Class Name : MailReceiverController.java
 * @Description : MailReceiverController Class
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
public class MailReceiverController {

	/** MailReceiverService */
	@Resource(name = "mailReceiverService")
	private MailReceiverService mailReceiverService;

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 알람 설정(메일수신자 관리) 초기화면 표시
	 * 
	 * @param model
	 * @return "/management/mailReceiver"
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/mailReceiverInit.do")
	public String init(Model model) throws Exception {
		return "/management/mailReceiver";
	}

	/**
	 * 알람 설정(메일수신자 관리) 리스트
	 * @param receiverVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/selectMailReceiverList.do", method = RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectMailReceiverList(@RequestBody MailReceiverVo receiverVo)
			throws Exception {
		HashMap<String, Object> result = null;
		try {
			result = mailReceiverService.selectMailReceiverList(receiverVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 알람 설정(메일수신자 관리) 삭제
	 * @param receiverVo
	 * @return 삭제결과
	 * @exception Exception
	 */
	@RequestMapping(value = "/management/deleteMailReceiver.do", method = RequestMethod.POST)
	@ResponseBody
	public int deleteMailReceiver(@RequestBody MailReceiverVo receiverVo)
			throws Exception {
		int result = 0;
		try {
			result = mailReceiverService.deleteMailReceiver(receiverVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	
	/**
	 * 등록된 메일 수신자를 등록한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/management/insertMailAddUser.do", method = RequestMethod.POST)
	@ResponseBody
	public int insertMailAddUser(@RequestBody MailReceiverVo receiverVo)
			throws Exception {
		int result = 0;
		try {
			//result = mailReceiverService.insertMailAddUser(receiverVo);
			//서울메트로용 메일수신자 등록
			result = mailReceiverService.insertMailUser(receiverVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 등록된 메일 수신자를 등록한다. - 서울메트로용 메일수신자 등록. 사용자 개개인의 ID값을 받아와 등록한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/management/insertMailUser.do", method = RequestMethod.POST)
	@ResponseBody
	public int insertMailUser(@RequestBody MailReceiverVo receiverVo)
			throws Exception {
		int result = 0;
		try {
			result = mailReceiverService.insertMailUser(receiverVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
}
