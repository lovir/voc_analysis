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
import com.diquest.voc.management.service.UserManagementService;
import com.diquest.voc.management.vo.MailReceiverVo;
import com.diquest.voc.management.vo.UserManagementVo;

/**  
 * @Class Name : UserManagementController.java
 * @Description : UserManagementController Class
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
public class UserManagementController {

	/** UserManagementService */
	@Resource(name = "userManagementService")
	private UserManagementService userManagementService;
	
	/** MailReceiverService */
	@Resource(name = "mailReceiverService")
	private MailReceiverService mailReceiverService;
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 사용자 관리 초기화면 표시
	 * 
	 * @param model
	 * @return "/management/userManagement"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/userManagementInit.do")
	public String init(Model model) throws Exception {
		return "/management/userManagement";
	}
	
	/**
	 * 사용자 관리 리스트
	 * @param userVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value="/management/selectUserManagementList.do", method=RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectuserManagementList(@RequestBody UserManagementVo userVo) throws Exception {
		HashMap<String, Object> result = null;
		try {
			result = userManagementService.selectuserManagementList(userVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 등록된 사용자 삭제
	 * @param userVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value="/management/deleteUserManagement.do", method=RequestMethod.POST)
	@ResponseBody
	public int deleteuserManagement(@RequestBody UserManagementVo userVo) throws Exception {
		int result = 0;
		try {
			//메일수신자에 등록된 사용자 삭제
			MailReceiverVo receiverVo = new MailReceiverVo();
			receiverVo.setSelectedNo(userVo.getSelectedNo());
			receiverVo.setUserDeleteFlag("1");
			mailReceiverService.deleteMailReceiver(receiverVo);
			result = userManagementService.deleteUserManagement(userVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
	
	/**
	 * 새로운 사용자 등록
	 * @param userVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value="/management/insertUserManagement.do", method=RequestMethod.POST)
	@ResponseBody
	public int updateuserManagement(@RequestBody UserManagementVo userVo) throws Exception {
		int result = 0;
		try {
			result = userManagementService.insertUserManagement(userVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}
}
