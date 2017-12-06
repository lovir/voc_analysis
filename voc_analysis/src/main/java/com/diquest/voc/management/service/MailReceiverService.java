package com.diquest.voc.management.service;

import java.util.HashMap;

import com.diquest.voc.management.vo.MailReceiverVo;

/**  
 * @Class Name : MailReceiverService.java
 * @Description : MailReceiverService Class
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

public interface MailReceiverService {

	/**
	 * 알람설정 (메일수신자 관리) 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 MailReceiverVo
	 * @return 글 목록
	 * @exception Exception
	 */
	public HashMap<String, Object> selectMailReceiverList(MailReceiverVo receiverVo) throws Exception;
	
	
	/**
	 * 등록된 메일 수신자를 삭제한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	public int deleteMailReceiver(MailReceiverVo receiverVo) throws Exception;
	
	
	/**
	 * 메일 수신자를 등록한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	public int insertMailAddUser(MailReceiverVo receiverVo) throws Exception;
	
	/**
	 * 메일 수신자를 등록한다. - 서울메트로용. 사용자 개개인의 ID값을 받아와 등록한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	public int insertMailUser(MailReceiverVo receiverVo) throws Exception;
	
	/**
	 * 메일 수신정보를 수정한다. - 서울메트로용. 사용자 개개인의 ID값을 받아와 수정한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	public int updateMailUser(MailReceiverVo receiverVo) throws Exception;
	/**
	 * 특정 사용자 메일 주소를 조회한다. - 서울메트로용. 사용자 개개인의 ID값을 가져와 조회한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	public String selectMail(MailReceiverVo receiverVo) throws Exception;
	
	/**
	 * 특정 사용자의 메일 수신정보가 입력되었는지 체크한다. - 서울메트로용. 사용자 개개인의 ID값을 가져와 조회한다.
	 * @param receiverVo
	 * @return
	 * @throws Exception
	 */
	public int selectMailReceiverExist(MailReceiverVo receiverVo) throws Exception;
}
