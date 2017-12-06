package com.diquest.voc.management.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.MailReceiverVo;
import com.diquest.voc.management.vo.UserManagementVo;
import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("commonDAO")
public class CommonDAO extends SqlMapClientDaoSupport {
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 정형분석(KBVOC)DB 접근
	 */
	@Resource(name="sqlMapClient2")
	public void setSuperlMapClient(SqlMapClient sqlMapClient){
		super.setSqlMapClient(sqlMapClient);
	}
	
	/**
	 * KBVOC.CUT_USER 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectKbvocCutUser(MailReceiverVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("mailReceiverDAO.selectKbvocCutUser", vo);
	}
	
	/**
	 * KBVOC.CUT_USER 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectKbvocCutUser(UserManagementVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("userManagementDAO.selectKbvocCutUser", vo);
	}
	
	/**
	 * 등록 할 사용자 리스트를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserManagementVo> selectAddUserManagementList(UserManagementVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("userManagementDAO.selectAddUserManagementList", vo);
	}
	
	/**
	 * 등록 할 사용자 Count를 조회한다.
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectAddUserManagementListTotCnt(UserManagementVo vo) {
		return getSqlMapClientTemplate().queryForList("userManagementDAO.selectAddUserManagementListTotCnt", vo);
	}
	
	/**
	 * 등록 대상 사용자 조회
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UserManagementVo> addUserList(UserManagementVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("userManagementDAO.addUserList", vo);
	}
}
