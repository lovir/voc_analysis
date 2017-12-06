package com.diquest.voc.socialKeywordRanking.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("socialKeywordRankingDAO")
public class SocialKeywordRankingDAO extends EgovAbstractDAO {

    /**
	 * 관심키워드 상위10개 가져오기
	 */
	@SuppressWarnings("unchecked")
    public List<String> selectInterest(HashMap<String, String> paramMap) throws Exception {
        return list("socialKeywordRankingDAO.selectInterestKeyword", paramMap);
    }
	// 뉴스 전체 누적건수
	@SuppressWarnings("unchecked")
    public List<CommonSelectBoxVo> selectNewsAll() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectNewsAll");
    }
	// 뉴스 현재 누적건수
	@SuppressWarnings("unchecked")
    public List<CommonSelectBoxVo> selectNewsToday() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectNewsToday");
    }
	// 관련사이트 전체 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectSiteAll() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectSiteAll");
	}
	// 관련사이트 현재 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectSiteToday() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectSiteToday");
	}
	// SNS facebook 전체 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectFacebookAll() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectFacebookAll");
	}
	// SNS facebook 현재 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectFacebookToday() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectFacebookToday");
	}
	// SNS twitter 전체 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectTwitterAll() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectTwitterAll");
	}
	// SNS twitter 현재 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectTwitterToday() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectTwitterToday");
	}
	// 커뮤니티 전체 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectCommunityAll() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectCommunityAll");
	}
	// 커뮤니티 현재 누적건수
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> selectCommunityToday() throws Exception {
		return getSqlMapClientTemplate().queryForList("socialKeywordRankingDAO.selectCommunityToday");
	}
}
