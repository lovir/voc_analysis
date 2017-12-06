package com.diquest.voc.dashBoard.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.diquest.voc.dashBoard.vo.ContryMInwonTotalVO;
import com.diquest.voc.dashBoard.vo.CountryMinwonVO;
import com.diquest.voc.dashBoard.vo.DashBoardVo;
import com.diquest.voc.dashBoard.vo.IncreAndDecreVO;
import com.diquest.voc.dashBoard.vo.Top10totalVO;
import com.diquest.voc.dashBoard.vo.TopCategoryVO;
import com.diquest.voc.dashBoard.vo.TopKeywordVO;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;

/**  
 * @Class Name : DashBoardService.java
 * @Description : DashBoardService Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.04.30           최초생성
 * 
 * @author 박소영
 * @since 2014. 04.30
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */
public interface DashBoardService {
	/**
	 * VOC 유형별 총괄 현황 
	 * @return json
	 * @exception Exception
	 */
	public String getKindChart(DashBoardVo vo) throws Exception;
	/**
	 * VOC 키워드 탑 100
	 * @return json
	 * @exception Exception
	 */
	public ArrayList<HashMap<String, String>> getTotalRanking(DashBoardVo vo) throws Exception;
	/**
	 * VOC 카테고리 탑 100
	 * @return json
	 * @exception Exception
	 */
	public ArrayList<HashMap<String, String>> getCategoryRanking(DashBoardVo vo) throws Exception;
	/**
	 * VOC 긍부정 증감현황
	 * @return json
	 * @exception Exception
	 */
	public String getEmotionChart(DashBoardVo vo) throws Exception;
	
	public HashMap<String, Object> getWordCloudChart(DashBoardVo vo) throws Exception;
	
	public String issueWordCloudClick(DashBoardVo vo) throws Exception;
}
	/**
	 * VOC 니즈유형별 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public List<Object> dashBoardNeedsReport(DashBoardVo vo) throws Exception;

	*//**
	 * VOE 상담유형(대분류)별 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public List<Object> dashBoardLclsReport(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOC 관심키워드 트렌드 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardInterestReport(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOC 불만 키워드 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardComplainKeywordReport(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOC 불만지수 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardComplainDocument(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOC 불만지수 상세를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardComplainDetailView(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOE 주요 키워드 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardKeywordReport(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOC 이슈 클라우드 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardIssueReport(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOC 우수고객 등급 이슈 분석 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardVipIssueReport(DashBoardVo vo) throws Exception;

	*//**
	 * VOC 이슈키워드 상세를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardIssueDetailView(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOE 개선과제 유형별 점유율 현황 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardLclsShareReport(DashBoardVo vo) throws Exception;
	
	*//**
	 * VOE 제안부서 TOP 10 현황 차트를 조회한다.
	 * @return 통계결과(차트) 목록
	 * @exception Exception
	 *//*
	public HashMap<String, Object> dashBoardDeptReport(DashBoardVo vo) throws Exception;
	
	public List<TopKeywordVO> dashBoardTop10Keyword(DashBoardVo vo) throws Exception;
	
	public List<TopCategoryVO> dashBoardTopCategory(DashBoardVo vo) throws Exception;
	
	public List<Object> dashBoardIncreAnDecre(DashBoardVo vo) throws Exception;
	
	public ContryMInwonTotalVO dashBoardCountryMinwon(DashBoardVo vo) throws Exception;
	
	public ArrayList<HashMap<String, Object>> getAlikeSearchResult(DashBoardVo vo) throws Exception;
	
	public HashMap<String, Object> getSearchResult(DashBoardVo vo) throws Exception;
	
	public HashMap<String, Object> getRegionExcelResult(DashBoardVo vo) throws Exception;
	
	public HashMap<String,String> getdetailViewResult(DashBoardVo vo) throws Exception;

	ArrayList<HashMap<String, String>> dashBoardIssueText(DashBoardVo vo) throws Exception;

	HashMap<String, Object> dashBoardVoiceReport(DashBoardVo vo)
			throws Exception;
}
*/