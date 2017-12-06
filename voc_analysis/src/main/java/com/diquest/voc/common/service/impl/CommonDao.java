package com.diquest.voc.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.diquest.voc.common.vo.CommonCodeVo;
import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("commonDao")
public class CommonDao extends SqlMapClientDaoSupport {
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	@Resource(name="sqlMapClient2")
	public void setSuperlMapClient(SqlMapClient sqlMapClient){
		super.setSqlMapClient(sqlMapClient);
	}
	
	/**
	 * 사용자정보 조회
	 * @param userId 아이디
	 * @param userPass 패스워드
	 * @return 사용자정보
	 * @exception Exception
	 */
	public EgovMap selectLogin(String userId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginId", userId);
		return (EgovMap) getSqlMapClientTemplate().queryForObject("commonDAO.selectLogin_S", map);
	}
	
	/**
	 * Base64 로그인 승인 여부 조회
	 * @param userId 아이디
	 * @param userPassWord 패스워드
	 * @return 승인 여부
	 * @exception Exception
	 */
	public String selectLogin(String userId, String userPassWord) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginId", userId);
		map.put("loginPwd", userPassWord);
		return (String) getSqlMapClientTemplate().queryForObject("commonDAO.selectLogin_Checker", map);
	}
	
	/**
	 * 서울메트로 사용자정보 조회
	 * @param portal_id 포탈 아이디
	 * @param portal_nm 포탈 사용자명
	 * @return 사용자명
	 * @exception Exception
	 */
	public String selectLogin_Metro(String portal_id) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("portal_id", portal_id);
		return (String) getSqlMapClientTemplate().queryForObject("commonDAO.selectLogin_Checker_Metro", map);
	}
	
	@SuppressWarnings("unchecked")
	public List<CommonCodeVo> selectVocCommonCode(HashMap<String, Object> paramMap) throws Exception {
		return getSqlMapClientTemplate().queryForList("commonDAO.selectVocCommonCode", paramMap);
	}
	
}
