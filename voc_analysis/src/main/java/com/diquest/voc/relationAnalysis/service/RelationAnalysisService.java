package com.diquest.voc.relationAnalysis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.diquest.voc.relationAnalysis.vo.RelationAnalysisVo;

public interface RelationAnalysisService {

	/**
	 * 키워드의 기간별카운트 조회.
	 * @param RelationAnalysisVo - 조회할 정보가 담긴 VO
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getSynthesisReport(RelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * 선택한 조건에 따라서 나온 연관 키워드 8(10)개를 가져온다
	 * @param RelationAnalysisVo - 조회할 정보가 담긴 VO
	 * @return LinkedHashMap - 연관 키워드
	 * @exception Exception
	 */
	public LinkedHashMap<String, Integer> getRelationKeywordArr(RelationAnalysisVo relationVo, String type) throws Exception;
	
	/**방사형그래프 데이터 생성 - 연관도 데이터 조회
	 * @param RelationAnalysisVo
	 * @param String
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> getRadarGraph(RelationAnalysisVo relationVo, String type) throws Exception;
	
	/**
	 * VOC 검색결과
	 * @param RelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(RelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * VOC 검색결과 - Excel다운로드
	 * @param RelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(RelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * 클릭한 제목의 상세페이지
	 * @param RelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String,String> getdetailViewResult(String id) throws Exception;
	
	/**
	 * 유사문서 구하기
	 * @param RelationAnalysisVo
	 * @return HashMap - 검색 결과 리스트
	 * @exception Exception
	 */
	public ArrayList<HashMap<String,Object>> getAlikeSearchResult(RelationAnalysisVo relationVo) throws Exception;
	
	/**
	 * 셀렉트 박스 - 처리주무부서 검색조건 구성
	 * @param RelationAnalysisVo
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> getRepDeptList(RelationAnalysisVo relationVo) throws Exception;
}
