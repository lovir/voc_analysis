package com.diquest.voc.management.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.MailReceiverVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("mailReceiverDAO")
public class MailReceiverDAO extends EgovAbstractDAO {

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	
	/**
	 * 메일수신자에 등록된 사용자를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MailReceiverVo> selectMailReceiverList(MailReceiverVo vo) throws Exception {
		return list("mailReceiverDAO.selectMailReceiverList", vo);
	}

	
	/**
	 * 메일수신자에 등록된 사용자의 Count를 조회한다.
	 * @param vo
	 * @return
	 */
	public int selectMailReceiverListTotCnt(MailReceiverVo vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("mailReceiverDAO.selectMailReceiverListTotCnt", vo);
	}
	
	/**
	 * 메일수신자에 사용자의 id가 등록되었는지 Count를 조회한다. - 서울메트로용
	 * @param vo
	 * @return
	 */
	public int selectMailReceiverExist(MailReceiverVo vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("mailReceiverDAO.selectMailReceiverExist", vo);
	}
	
	/**
	 * 메일수신자 등록할 대상 사용자 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MailReceiverVo> selectMailReceiverAddUserList(MailReceiverVo vo) throws Exception {
		return list("mailReceiverDAO.selectMailReceiverAddUserList", vo);
	}
	
	/**
	 * 메일수신자에 등록 가능한 전체사용자 조회 - Count
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MailReceiverVo> selectMailReceiverAddUserListCnt(MailReceiverVo vo) throws Exception {
		return list("mailReceiverDAO.selectMailReceiverAddUserListCnt", vo);
	}
	
	/**
	 * 등록된 메일수신자를 삭제한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int deleteMailReceiver(MailReceiverVo vo) throws Exception {
		return delete("mailReceiverDAO.deleteMailReceiver", vo);
	}
	
	/**
	 * 메일수신자에 등록할 사용자 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MailReceiverVo> selectMailAddUserList(MailReceiverVo vo) throws Exception {
		return list("mailReceiverDAO.selectMailAddUserList", vo);
	}
	
	/**
	 * 메일수신자를 등록한다.
	 * @param vo
	 * @throws Exception
	 */
	public void insertMailAddUser(MailReceiverVo vo) throws Exception {
		insert("mailReceiverDAO.insertMailAddUser", vo);
	}
	
	/**
	 * 특정 사용자의 메일 수신 정보를 수정한다.
	 * @param vo
	 * @throws Exception
	 */
	public void updateMailAddUser(MailReceiverVo vo) throws Exception {
		update("mailReceiverDAO.updateMailAddUser", vo);
	}
	/**
	 * 특정 사용자의 수신 메일 정보를 조회한다.
	 * @param vo
	 * @throws Exception
	 */
	public String selectMail(MailReceiverVo vo) throws Exception {
		return (String)getSqlMapClientTemplate().queryForObject("mailReceiverDAO.selectMail", vo);
	}
	
	
}
