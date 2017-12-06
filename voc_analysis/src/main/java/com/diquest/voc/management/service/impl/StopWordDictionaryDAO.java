package com.diquest.voc.management.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.StopWordDictionaryVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("stopWordDAO")
public class StopWordDictionaryDAO extends EgovAbstractDAO {

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	
	/**
	 * 불용어 총 갯수를 조회한다.
	 * @param DictionaryVo - 조회할 정보가 담긴 vo
	 * @return 글 총 갯수
	 * @exception
	 */
	public List selectStopwordList(StopWordDictionaryVo vo)throws Exception{
		return  list("stopWordDAO.selectStopwordList",vo);
	}
	
	/**
	 * 불용어 총 갯수를 조회한다.
	 * @param DictionaryVo - 조회할 정보가 담긴 vo
	 * @return 글 총 갯수
	 * @exception
	 */

	public int selectStopwordCnt(StopWordDictionaryVo vo)throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stopWordDAO.selectStopwordCnt", vo);
	}
	
	/**
	 * 불용어 추가시 등록 유무 체크
	 * @param keyword
	 * @return 키워드
	 * @exception Exception
	 */
	public int selectStopword(String keyword) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("stopWordDAO.selectStopword", keyword);
	}
	
	/**
	 * 불용어 추가
	 * @param keyword
	 * @return 키워드
	 * @exception Exception
	 */
	public void insertStopword(StopWordDictionaryVo vo) throws Exception{
		update("stopWordDAO.insertStopword", vo);
	}
	
	
	/**
	 * 불용어 사전 삭제.
	 * @param DelIdList
	 * @return 키워드
	 * @exception Exception
	 */
	public int deleteStopword(String[] delKeywordList)throws Exception{
		return delete("stopWordDAO.deleteStopword", delKeywordList);
	}
	
	/**
	 * 불용어 사전 삭제.
	 * @param applyIdList
	 * @return 키워드
	 * @exception Exception
	 */
	public int applyStopword(String[] applyIdList)throws Exception{
		return update("stopWordDAO.applyStopword", applyIdList);
	}
	
}
