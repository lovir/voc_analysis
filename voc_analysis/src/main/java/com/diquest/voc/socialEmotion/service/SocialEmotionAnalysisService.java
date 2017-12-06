package com.diquest.voc.socialEmotion.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.diquest.voc.emotion.vo.EmotionAnalysisVo;
import com.diquest.voc.socialEmotion.vo.SocialEmotionAnalysisVo;

public interface SocialEmotionAnalysisService {

	/**
	 * 기간별 긍정,부정,중립 카운트 조회.
	 * @return 
	 * @exception Exception
	 */
	public HashMap<String, Object> getEmotionTrendChart(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * 부정 감성 분포 조회.
	 * @return 
	 * @exception Exception
	 */
	public HashMap<String, Object> getEmotionDistributionChart(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception;
	/**
	 * VOC 검색결과 Excel-다운로드
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
	/**
	 * VOC 검색결과 - 감성 추세 차트(막대) 클릭한 결과 리스트
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getClickStackSearchResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception;
	/**
	 * VOC 검색결과 - 감성 추세 차트(막대) 클릭한 결과 리스트 Excel-다운로드
	 * @param emotionAnalysisVo
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getClickStackExcelResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception;
	
}
