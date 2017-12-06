package com.diquest.voc.trend.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.diquest.voc.trend.vo.TrendAnalysisVo;

/**  
 * @Class Name : TrendAnalysisService.java
 * @Description : TrendAnalysisService Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2017.11.21           최초생성
 * 
 * @author 신누리
 * @since 2017. 11.21
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */


public interface TrendAnalysisService {

		/**
		 * 동향정보 분석(차트)을 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 동향정보 분석(차트)
		 * @exception Exception
		 */
		public String trendAnalysisReport(TrendAnalysisVo vo) throws Exception;
		/**
		 * 검색결과 조회
		 * @param vo - 조회 할 정보가 담긴 TrendAnalysisVo
		 * @return 검색결과
		 * @exception Exception
		 */
		public HashMap<String, Object> getSearchResult(TrendAnalysisVo vo) throws Exception;
		
		/**
		 * 키워드 종합랭킹 조회
		 * @param vo - 조회 할 정보가 담긴 TrendAnalysisVo
		 * @return 키워드 종합랭킹
		 * @exception Exception
		 */
		public LinkedHashMap<String, Object> getSynthesisRanking(TrendAnalysisVo vo) throws Exception;
		
		/**
		 * 엑셀 조회
		 * @param vo - 조회 할 정보가 담긴 TrendAnalysisVo
		 * @return 키워드 종합랭킹
		 * @exception Exception
		 */
		public HashMap<String, Object> getExcelResult(TrendAnalysisVo vo) throws Exception;
		/**
		 * 동향정보 분석(랭킹)을 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 동향정보 분석(랭킹)
		 * @exception Exception
		 *//*
		public HashMap<String, Object> trendAnalysisRanking(TrendAnalysisVo trendVo) throws Exception;
		
		
		
		*//**
		 * 동향정보 분석 목록을 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 글 목록
		 * @exception Exception
		 *//*
		public HashMap<String, Object> trendAnalysisList(TrendAnalysisVo trendVo) throws Exception;
		
		*//**
		 * 동향정보 분석 엑셀로 다운받을 목록을 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 글 목록
		 * @exception Exception
		 *//*
		public HashMap<String, Object> trendAnalysisExcelList(TrendAnalysisVo trendVo) throws Exception;
		
		*//**
		 * 관심 키워드를 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 관심키워드
		 * @exception Exception
		 *//*
		public LinkedHashMap<String,String> selectKeywordList(TrendAnalysisVo trendVo) throws Exception;
		
		*//**
		 * 동향정보 분석 상세문서를 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 조회한 글(상세)
		 * @exception Exception
		 *//*
		public HashMap<String, String> trendAnalysisDetailView(TrendAnalysisVo trendVo) throws Exception;
		
		*//**
		 * 동향정보 분석 유사문서 조회한다.
		 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
		 * @return 글 목록(유사문서)
		 * @exception Exception
		 *//*
		public List<Object> trendAnalysisAlikeView(TrendAnalysisVo trendVo) throws Exception;
*/
}
