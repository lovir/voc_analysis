package com.diquest.voc.socialChannelStatus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.diquest.voc.socialChannelStatus.vo.SocialChannelStatusVo;


public interface SocialChannelStatusService {

	// 파이 차트 
	public ArrayList<HashMap<String, String>> getChannelChart(SocialChannelStatusVo vo) throws Exception;
	// 탑 10 키워드
	public String getChannelKeywords(SocialChannelStatusVo vo) throws Exception;
	// 선 그래프
	public HashMap<String, Object> getSynthesisReport(SocialChannelStatusVo vo) throws Exception;
	// 검색 결과
	public HashMap<String, Object> getSearchResult(SocialChannelStatusVo vo) throws Exception;
	// 엑셀
	public HashMap<String, Object> getExcelResult(SocialChannelStatusVo vo) throws Exception;
	// 상세검색
	public HashMap<String, String> getdetailViewResult(String id) throws Exception;
	// 유사검색
	public ArrayList<HashMap<String, Object>> getAlikeSearchResult(SocialChannelStatusVo vo) throws Exception;
}
