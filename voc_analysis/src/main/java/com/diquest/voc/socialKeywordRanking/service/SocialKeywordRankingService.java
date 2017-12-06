package com.diquest.voc.socialKeywordRanking.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.diquest.voc.dashBoard.vo.ContryMInwonTotalVO;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.socialKeywordRanking.vo.SocialKeywordRankingVo;

public interface SocialKeywordRankingService {

	/**
	 * 키워드의 기간별카운트 조회.
	 * @return 키워드
	 * @exception Exception
	 */
	public HashMap<String, Object> getSynthesisReport(SocialKeywordRankingVo vo) throws Exception;

	
	/**
	 * 선택한 조건에 따라서 나온 상위 키워드 10개를 가져온다
	 * @param SocialKeywordRankingVo - 조회할 정보가 담긴 VO
	 * @return 상위 키워드 리스트
	 * @exception Exception
	 */
	public String[] getsynthesisTotalKeywordArr(SocialKeywordRankingVo vo) throws Exception;
	
	/**
	 * 종합 랭킹 분석
	 * @param SocialKeywordRankingVo - 조회할 정보가 담긴 VO
	 * @return 상위 키워드 리스트
	 * @exception Exception
	 */
	public LinkedHashMap<String, Object> getSynthesisRanking(SocialKeywordRankingVo keywordRankingVo) throws Exception;
	
	/**
	 * 관심키워드 랭킹 분석
	 * @param RelationAnalysisVo - 조회할 정보가 담긴 VO
	 * @return 상위 키워드 리스트
	 * @exception Exception
	 */
	public LinkedHashMap<String, Object> getInterestRanking(SocialKeywordRankingVo keywordRankingVo) throws Exception;
	/**
	 * 이슈키워드 랭킹 분석
	 * @param RelationAnalysisVo - 조회할 정보가 담긴 VO
	 * @return 상위 키워드 리스트
	 * @exception Exception
	 */
	public LinkedHashMap<String, Object> getIssueRanking(SocialKeywordRankingVo keywordRankingVo) throws Exception;
	
	/**
	 * VOC 검색결과
	 * @param RelationAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(SocialKeywordRankingVo keywordRankingVo) throws Exception;
	/**
	 * VOC 검색결과 Excel-다운로드
	 * @param RelationAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(SocialKeywordRankingVo keywordRankingVo) throws Exception;
	
	// 채널별 조회현황 불러오기
	public String socialSpiderStatus(SocialKeywordRankingVo vo) throws Exception;
	/**
	 * VOC 검색결과 - 클릭한 제목의 상세페이지
	 * @param id
	 * @return
	 * @throws Exception
	 */
//	public HashMap<String,String> getdetailViewResult(String id) throws Exception;

	/** 
	 * VOC 검색결과 - 유사문서 구하기
	 * @param keywordRankingVo
	 * @return
	 * @throws Exception
	 */
//	public ArrayList<HashMap<String,Object>> getAlikeSearchResult(KeywordRankingVo keywordRankingVo) throws Exception;
	
	/**
	 * 관심키워드 10개
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<String> getInterestKeyword(HashMap<String, String> paramMap) throws Exception;
	/**
	 * 종합 랭킹 분석 - 워드 클라우드 차트
	 * @param keywordRankingVo
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getWordCloudChart(SocialKeywordRankingVo keywordRankingVo) throws Exception;
	
	/**
	 * 셀렉트 박스 - 처리주무부서 검색조건 구성
	 * @param keywordRankingVo
	 * @return
	 * @throws Exception
	 */
//	public HashMap<String, Object> getRepDeptList(KeywordRankingVo keywordRankingVo) throws Exception;
	
}
