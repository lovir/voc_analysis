package com.diquest.voc.keywordRanking.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.diquest.voc.cmmn.service.Globals;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("keywordRankingDAO")
public class KeywordRankingDAO extends EgovAbstractDAO {

	/**
	 * 관심키워드 상위10개 가져오기
	 */
	@SuppressWarnings("unchecked")
	public List<String> selectInterest(HashMap<String, String> paramMap) throws Exception {
		ArrayList<String> interestKeywordList = (ArrayList<String>) list("keywordRankingDAO.selectInterestKeyword", paramMap);
		if(interestKeywordList == null || interestKeywordList.size() == 0){
			return Arrays.asList(Globals.DEFAULT_INTEREST_KEYWORD);	//저장한 관심키워드가 없다면 기본 관심 키워드 반환
		}
		return list("keywordRankingDAO.selectInterestKeyword", paramMap);
	}
}
