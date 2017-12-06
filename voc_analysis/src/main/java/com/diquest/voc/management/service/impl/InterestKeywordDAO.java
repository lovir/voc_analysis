package com.diquest.voc.management.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.InterestKeywordVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("interestDAO")
public class InterestKeywordDAO extends EgovAbstractDAO {

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 InterestKeywordVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public int insertInterestKeyword(InterestKeywordVo vo) throws Exception {
		return update("interestDAO.insertInterestKeyword", vo);
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 InterestKeywordVO
	 * @return void형
	 * @exception Exception
	 */
	public int updateInterestKeyword(InterestKeywordVo vo) throws Exception {
		return update("interestDAO.updateInterestKeyword", vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 InterestKeywordVO
	 * @return void형 
	 * @exception Exception
	 */
	public int deleteInterestKeyword(InterestKeywordVo vo) throws Exception {
		return delete("interestDAO.deleteInterestKeyword", vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 InterestKeywordVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public InterestKeywordVo selectInterestKeyword(InterestKeywordVo vo) throws Exception {
		return (InterestKeywordVo) selectByPk("interestDAO.selectInterestKeyword", vo);
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<InterestKeywordVo> selectInterestKeywordList(InterestKeywordVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("interestDAO.selectInterestKeywordList", vo);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 총 갯수
	 * @exception
	 */
	public int selectInterestKeywordListTotCnt(InterestKeywordVo vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("interestDAO.selectInterestKeywordListTotCnt", vo);
	}

	/**
	 * 대시보드 활성/비활성을 반영 한다.
	 * @param vo - 수정할 정보가 담긴 InterestKeywordVO
	 * @return int
	 * @exception Exception
	 */
	public int updateInterestKeywordDashYn(InterestKeywordVo vo) throws Exception {
		return update("interestDAO.updateInterestKeywordDashYn", vo);
	}
	
	/**
	 * 대시보드 활성 가능한 관심키워드 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public int selectInterestKeywordListDashYnTotCnt(InterestKeywordVo vo) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("interestDAO.selectInterestKeywordListDashYnTotCnt", vo);
	}
	/**
	 * 대시보드 활성화 된 관심키워드 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EgovMap> selectInterestKeywordListDashYn(InterestKeywordVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("interestDAO.selectInterestKeywordListDashYn", vo);
	}
	
	/**
	 * 키워드 활성/비활성을 반영 한다.
	 * @param vo - 수정할 정보가 담긴 InterestKeywordVO
	 * @return int
	 * @exception Exception
	 */
	public int updateInterestKeywordUseYn(InterestKeywordVo vo) throws Exception {
		return update("interestDAO.updateInterestKeywordUseYn", vo);
	}
	/**
	 * 활성 가능한 관심키워드 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	public int selectInterestKeywordListUseYnTotCnt(InterestKeywordVo vo) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("interestDAO.selectInterestKeywordListUseYnTotCnt", vo);
	}
	
	/**
	 * 활성화 된 관심키워드 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EgovMap> selectInterestKeywordListTop10(InterestKeywordVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("interestDAO.selectInterestKeywordListTop10", vo);
	}
	
	
}
