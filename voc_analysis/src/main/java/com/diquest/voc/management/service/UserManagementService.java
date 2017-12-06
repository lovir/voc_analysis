package com.diquest.voc.management.service;

import java.util.HashMap;

import com.diquest.voc.management.vo.UserManagementVo;

/**  
 * @Class Name : UserManagementService.java
 * @Description : UserManagementService Class
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


public interface UserManagementService {

	/**
	 * 사용자 관리를 조회한다.
	 * @param vo - 조회 할 정보가 담긴 UserManagementVo
	 * @return 조회한 글(상세)
	 * @exception Exception
	 */
	public HashMap<String, Object> selectuserManagementList(UserManagementVo vo) throws Exception;
	
	/**
	 *사용자 관리를 수정한다.
	 * @param vo - 수정 할 정보가 담긴 UserManagementVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	public int insertUserManagement(UserManagementVo vo) throws Exception;
	
	/**
	 * 등록된 사용자를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int deleteUserManagement(UserManagementVo vo) throws Exception;
	
}
