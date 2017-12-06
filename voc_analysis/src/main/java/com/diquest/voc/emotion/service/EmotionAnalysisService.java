package com.diquest.voc.emotion.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.diquest.voc.emotion.vo.EmotionAnalysisVo;

public interface EmotionAnalysisService {

	/**
	 * 기간별 긍정,부정,중립 카운트 조회.
	 * @return 
	 * @exception Exception
	 */
	public HashMap<String, Object> getEmotionTrendChart(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * 부정 감성 분포 조회.
	 * @return 
	 * @exception Exception
	 */
	public HashMap<String, Object> getEmotionDistributionChart(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과 Excel-다운로드
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과 - 클릭한 제목의 상세페이지
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,String> getdetailViewResult(String id) throws Exception;

	/** 
	 * VOC 검색결과 - 유사문서 구하기
	 * @param emotionAnalysisVo
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String,Object>> getAlikeSearchResult(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * 셀렉트 박스 - 처리주무부서 검색조건 구성
	 * @param emotionAnalysisVo
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getRepDeptList(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과 - 감성 추세 차트(막대) 클릭한 결과 리스트
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getClickStackSearchResult(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과 - 감성 추세 차트(막대) 클릭한 결과 리스트 Excel-다운로드
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getClickStackExcelResult(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	public HashMap<String, Object> getDashBoardSearchResult(EmotionAnalysisVo emotionAnalysisVo) throws Exception;
}
