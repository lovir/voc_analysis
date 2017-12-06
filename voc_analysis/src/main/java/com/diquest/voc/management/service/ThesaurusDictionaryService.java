package com.diquest.voc.management.service;

import java.util.HashMap;

import com.diquest.voc.management.vo.ThesaurusDictionaryVo;

public interface ThesaurusDictionaryService {

	/**
	 * 사전 관리 (유의어 사전 관리) 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 ThesaurusDictionaryVo
	 * @return 글 목록
	 * @exception Exception
	 */
	public HashMap<String, Object> selectThesaurusDictionaryList(ThesaurusDictionaryVo vo) throws Exception;
	
	/**
	 * 유의어 사전 편집을 조회 한다. 
	 * @exception Exception
	 */
	public ThesaurusDictionaryVo selectThesaurusDictionary(ThesaurusDictionaryVo vo) throws Exception;
	
	/**
	 * 사전 관리 (유의어 사전 관리)을 수정한다.
	 * @param vo - 등록 할 정보가 담긴 ThesaurusDictionaryVo
	 * @exception Exception
	 */
	public void deleteThesaurusDictionary(ThesaurusDictionaryVo vo) throws Exception;
	
	/**
	 * 사전 관리 (유의어 사전 관리)을 적용한다.
	 * @exception Exception
	 */
	public void applyThesaurusDictionary() throws Exception;
	
	/**
	 * ##사전 관리 (유의어 사전 관리) 추가 편집을 조회 한다. 
	 * @exception Exception
	 */
	public ThesaurusDictionaryVo selectThesaurusAddKeyword(ThesaurusDictionaryVo vo) throws Exception;
	
	/**
	 * 해당 키워드의 형태소 분석을 조회 한다. 
	 * @exception Exception
	 */
	public ThesaurusDictionaryVo selectThesaurusChKeyword(ThesaurusDictionaryVo vo) throws Exception;
	
	/**
	 * 해당 키워드의 형태소 색인어휘를 추출 한다. 
	 * @exception Exception
	 */
	public ThesaurusDictionaryVo extractorThesaurusKeyword(ThesaurusDictionaryVo vo) throws Exception;
	
	/**
	 * 사전 관리 (유의어 사전 관리)을 등록한다.
	 * @exception Exception
	 */
	public int addThesaurusKeyword(ThesaurusDictionaryVo vo) throws Exception;
	
}
