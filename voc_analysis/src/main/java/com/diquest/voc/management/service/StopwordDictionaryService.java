package com.diquest.voc.management.service;

import java.util.List;

import com.diquest.voc.management.vo.StopWordDictionaryVo;

public interface StopwordDictionaryService {

	/**
	 * 불용어 사전 조회.
	 * @param vo - 조회할 정보가 담긴 VO
	 * @return 키워드
	 * @exception Exception
	 */
	public List getStopwordList(StopWordDictionaryVo vo) throws Exception;
	
	
	/**
	 * 불용어 사전 총갯수 조회.
	 * @param vo - 조회할 정보가 담긴 VO
	 * @return
	 * @exception Exception
	 */
	public int getStopwordCnt(StopWordDictionaryVo vo) throws Exception;
	
	/**
	 * 불용어 추가시 등록 유무 체크
	 * @param keyword
	 * @return
	 * @exception Exception
	 */
	public int getSelectStopword(String keyword) throws Exception;
	
	/**
	 * 불용어 추가
	 * @param keyword
	 * @return
	 * @exception Exception
	 */
	public void insertStopword(StopWordDictionaryVo vo) throws Exception;
	
	/**
	 * 불용어 사전 삭제.
	 * @param DelIdList
	 * @return
	 * @exception Exception
	 */
	public int deleteStopword(String[] DelIdList, String[] DelkeywordList) throws Exception;
	
	/**
	 * 불용어 사전 적용.
	 * @param applyIdList
	 * @return
	 * @exception Exception
	 */
	public int applyStopword(String[] applyIdList, String[] applyKeywordList) throws Exception;

	
}
