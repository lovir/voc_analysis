package com.diquest.voc.management.service;

import java.util.HashMap;

import com.diquest.voc.management.vo.UserDictionaryVo;

public interface UserDictionaryService {

	/**
	 * 사전 관리 (사용자 사전 관리) 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 UserDictionaryVo
	 * @return 글 목록
	 * @exception Exception
	 */
	public HashMap<String, Object> selectUserDictionaryList(UserDictionaryVo vo) throws Exception;
	
	/**
	 * 사전 관리 (사용자 사전 관리)을 삭제한다.
	 * @param vo - 등록 할 정보가 담긴 UserDictionaryVo
	 * @exception Exception
	 */
	public void deleteUserDictionary(UserDictionaryVo vo) throws Exception;
	
	/**
	 * 사전 관리 (사용자 사전 관리)을 적용한다.
	 * @exception Exception
	 */
	public void applyUserDictionary() throws Exception;
	
	
	/**
	 * 해당 키워드의 형태소 색인어휘를 추출 한다. 
	 * @param vo - 조회 할 정보가 담긴 UserDictionaryVo
	 * @return 형태소 색인어휘
	 * @exception Exception
	 */
	public UserDictionaryVo extractorUserKeyword(UserDictionaryVo vo) throws Exception;
	
	/**
	 * 사전 관리 (사용자 사전 관리)을 등록한다.
	 * @return 등록결과 (1: 성공, 2: 기존에 등록된 결과 있음)
	 * @exception Exception
	 */
	public int addUserKeyword(UserDictionaryVo vo) throws Exception;
	
}
