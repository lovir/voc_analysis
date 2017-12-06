package com.diquest.voc.management.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.MailReceiverVo;
import com.diquest.voc.management.vo.UserManagementVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("userManagementDAO")
public class UserManagementDAO extends EgovAbstractDAO{

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 등록 된 사용자 리스트를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserManagementVo> selectUserManagementList(UserManagementVo vo) throws Exception {
		return list("userManagementDAO.selectUserManagementList_D", vo);
	}
	
	/**
	 * 등록 된 사용자 Count 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserManagementVo> selectUserManagementListCnt(UserManagementVo vo) throws Exception {
		return list("userManagementDAO.selectUserManagementListCnt_D", vo);
	}
	
	/**
	 * KBACT.CUT_USER 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectKbactCutUser(UserManagementVo vo) throws Exception {
		return list("userManagementDAO.selectKbactCutUser", vo);
	}
	
	/**
	 * 사용자를 등록한다.
	 * @param vo
	 * @throws Exception
	 */
	public void insertUserManagement(UserManagementVo vo) throws Exception {
		insert("userManagementDAO.insertUserManagement", vo);
	}
	
	/**
	 * 사용자를 삭제한다.
	 * @param vo
	 * @throws Exception
	 */
	public int deleteUserManagement(UserManagementVo vo) throws Exception {
		return (Integer)delete("userManagementDAO.deleteUserManagement", vo);
	}
	
}
