package com.diquest.voc.socialRelationAnalysis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.diquest.voc.relationAnalysis.vo.RelationAnalysisVo;
import com.diquest.voc.socialRelationAnalysis.vo.SocialRelationAnalysisVo;

public interface SocialRelationAnalysisService {

	/**
	 * 키워드의 기간별카운트 조회.
	 * @param SocialRelationAnalysisVo - 조회할 정보가 담긴 VO
	 * @return HashMap
	 * @exception Exception
	 */	
	public HashMap<String, Object> getSynthesisReport(SocialRelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * 선택한 조건에 따라서 나온 연관 키워드 8(10)개를 가져온다
	 * @param SocialRelationAnalysisVo - 조회할 정보가 담긴 VO
	 * @return LinkedHashMap - 연관 키워드
	 * @exception Exception
	 */
	public LinkedHashMap<String, Integer> getRelationKeywordArr(SocialRelationAnalysisVo relationVo, String type) throws Exception;
	
	/**방사형그래프 데이터 생성 - 연관도 데이터 조회
	 * @param SocialRelationAnalysisVo
	 * @param String
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> getRadarGraph(SocialRelationAnalysisVo relationVo, String type) throws Exception;
	
	/**
	 * VOC 검색결과
	 * @param SocialRelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(SocialRelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * VOC 검색결과 - Excel다운로드
	 * @param SocialRelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(SocialRelationAnalysisVo relationVo) throws Exception;
	/**
	 * 클릭한 제목의 상세페이지
	 * @param SocialRelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	//public HashMap<String,String> getdetailViewResult(String id) throws Exception;
	
	/**
	 * 유사문서 구하기
	 * @param SocialRelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	//public ArrayList<HashMap<String,Object>> getAlikeSearchResult(RelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * 셀렉트 박스 - 처리주무부서 검색조건 구성
	 * @param SocialRelationAnalysisVo
	 * @return HashMap
	 * @throws Exception
	 */
	//public HashMap<String, Object> getRepDeptList(RelationAnalysisVo relationVo) throws Exception;
}
