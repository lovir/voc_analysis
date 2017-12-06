package com.diquest.voc.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.vo.BaseVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

public class BaseService {

	/**
	 * 검색결과 상세 글을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 글 상세
	 */
	public HashMap<String, String> getDetail(Result[] resultList) {
		HashMap<String, String> map = null;
		for(int i = 0; resultList != null && i < 1; i++) {
			Result result = (Result)resultList[i];
			if(result.getRealSize() != 0){
				
				for (int j = 0; j < result.getRealSize(); j++) {
					map = new HashMap<String, String>();
					System.out.println("값 확인 : "+new String(result.getResult(j,2)));
					map.put("ID", new String(result.getResult(j,0)));
					map.put(Globals.FIELD_TITLE, new String(result.getResult(j,1)));
					map.put(Globals.FIELD_CONTENT, new String(result.getResult(j,2)));
					map.put(Globals.FIELD_REGDATE, new String(result.getResult(j,3)));
					map.put("USE_MED_CD", new String(result.getResult(j,5)));
					map.put("M_CATE_NAME", new String(result.getResult(j,6)));
					map.put("S_CATE_NAME", new String(result.getResult(j,7)));
					map.put("DEPART_TRANSFER", new String(result.getResult(j,8)));
					map.put("RES_CONTENT", new String(result.getResult(j,9)));
					
					
					int weight = Integer.parseInt(new String(result.getResult(j,4)));
					if(weight > 0){
						map.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
					}else{
						map.put(Globals.FIELD_WEIGHT, "0");
					}
				}
			}	
		}
		return map;
	}
	
	public HashMap<String, String> getDetail2(Result[] resultList) {
		HashMap<String, String> map = null;
		for(int i = 0; resultList != null && i < 1; i++) {
			Result result = (Result)resultList[i];
			if(result.getRealSize() != 0){
				
				for (int j = 0; j < result.getRealSize(); j++) {
					map = new HashMap<String, String>();
					System.out.println("값 확인 : "+new String(result.getResult(j,2)));
					map.put("ID", new String(result.getResult(j,0)));
					map.put(Globals.FIELD_TITLE, new String(result.getResult(j,1)));
					map.put(Globals.FIELD_CONTENT, new String(result.getResult(j,2)));
					map.put(Globals.FIELD_REGDATE, new String(result.getResult(j,3)));
					map.put("M_CATE_NAME", new String(result.getResult(j,5)));
					map.put("S_CATE_NAME", new String(result.getResult(j,6)));

					
					
					int weight = Integer.parseInt(new String(result.getResult(j,4)));
					if(weight > 0){
						map.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
					}else{
						map.put(Globals.FIELD_WEIGHT, "0");
					}
				}
			}	
		}
		return map;
	}
	/**
	 * 검색결과 유사문서 목록 화면 표시용으로 가공.
	 * @param resultList - 검색 결과 Result[]
	 * @return 유사문서 목록
	 */
	public List<Object> getAlikeList(Result[] resultList) {
		List<Object> list = new ArrayList<Object>();
		HashMap<String, String> map = null;
		
		for(int i = 0; resultList != null && i < 1; i++) {
			Result result = (Result)resultList[i];
			if(result.getRealSize() != 0){
				
				for (int j = 0; j < result.getRealSize(); j++) {
					map = new HashMap<String, String>();
					map.put(Globals.FIELD_TITLE, new String(result.getResult(j,0)));
					map.put(Globals.FIELD_CONTENT, new String(result.getResult(j,1)));
					map.put(Globals.FIELD_REGDATE, new String(result.getResult(j,2)));
					
					int weight = Integer.parseInt(new String(result.getResult(j,3)));
					if(weight > 0){
						map.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
					}else{
						map.put(Globals.FIELD_WEIGHT, "0");
					}
					list.add(map);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 검색결과 글목록을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 글 목록
	 */
	public List<HashMap<String, String>> getList(Result result) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		
		if(result.getRealSize() != 0){
			
			for (int j = 0; j < result.getRealSize(); j++) {
				//System.out.println("타이틀 확인 : "+new String(result.getResult(j,0)));
				map = new HashMap<String, String>();
				map.put(Globals.FIELD_TITLE, new String(result.getResult(j,0)));
				map.put("CALL_META_ID", new String(result.getResult(j,1)));
				map.put(Globals.FIELD_CONTENT, new String(result.getResult(j,2)));
				map.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(j,3)));
				map.put(Globals.FIELD_REGDATE, new String(result.getResult(j,4)));
				map.put(Globals.FIELD_ID, new String(result.getResult(j,5)));
				map.put("USE_MED_CD", new String(result.getResult(j,7)));
				
				if(new String(result.getResult(j,7)).equals("120")){
					map.put("USE_MED_CD", "120미추홀콜센터");
				}else if(new String(result.getResult(j,7)).equals("새올")){
					map.put("USE_MED_CD", "새올민원");
				}else if(new String(result.getResult(j,7)).equals("신문고")){
					map.put("USE_MED_CD", "국민신문고");
				}
				
				int weight = Integer.parseInt(new String(result.getResult(j,6)));
				if(weight > 0){
					map.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
				}else{
					map.put(Globals.FIELD_WEIGHT, "0");
				}
				
				list.add(map);
			}
		}
		
		return list;
	}
	
	/**
	 * 검색결과 카테고리 목록을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 카테고리 목록
	 */
	public List<HashMap<String, String>> getCategoryList(Result result) {
		GroupResult[] groupResults = null;
		List<HashMap<String, String>> categoryList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> categoryMap = null;
		
		if(result.getGroupResultSize() != 0){
			groupResults = result.getGroupResults();
			for (int j = 0; j < groupResults.length; j++) {
				int rSize = groupResults[j].groupResultSize();
				for (int k = 0; k < rSize; k++) {
					if(!new String(groupResults[j].getId(k)).equals("미분류")){												
						//System.out.println("id 확인 : "+new String(groupResults[j].getId(k)));
						categoryMap = new HashMap<String, String>();
						categoryMap.put("id", new String(groupResults[j].getId(k)));
						categoryMap.put("value", String.valueOf(groupResults[j].getIntValue(k)));
						categoryList.add(categoryMap);
					}
				}
			}
		}
		
		return categoryList;
	}
	
	/**
	 * 키워드별 점유율.
	 * @param resultList - 검색 결과 Result[]
	 * @param trendVo - 조회 할 정보가 담긴 TrendAnalysisVo
	 * @return 점유율 목록
	 */
	public HashMap<String, String> getShare(Result result, String term) {
		GroupResult[] groupResults = null;
		HashMap<String, String> shareMap = null;
		float total = 0;
		float value = 0.f;
		float share = 0;

		// 검색결과 전체 문서의 키워드 수
		if(result.getGroupResultSize() != 0){
			groupResults = result.getGroupResults();
			for (int j = 0; j < groupResults.length; j++) {
				int rSize = groupResults[j].groupResultSize();
				for (int k = 0; k < rSize; k++) {
					total += groupResults[j].getIntValue(k);
					String keyword = new String(groupResults[j].getId(k));
					if(term.toUpperCase().equals(keyword.toUpperCase().trim())){
						value = groupResults[j].getIntValue(k);
					}
				}
			}
		}
		
		share = (value / total) * 100;
		
		if(Float.isNaN(share)){
			share = 0;
		}
		
		shareMap = new HashMap<String, String>();
		shareMap.put("id", term);			
		shareMap.put("value", String.format("%.3f", share));
		
		return shareMap;
	}
	
	/**
	 * 페이지 총 사이즈.
	 * @param resultList - 검색 결과 Result[]
	 * @return 페이지 사이즈
	 */
	public PaginationInfo getPaging(Result result, BaseVo vo) {
		/** pageing setting */
		System.out.println("페이지 확인 : "+Integer.valueOf(vo.getCurrentPageNo()));
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.valueOf(vo.getCurrentPageNo()));
		paginationInfo.setRecordCountPerPage(Integer.valueOf(vo.getPageSize()));
		paginationInfo.setPageSize(Integer.valueOf(vo.getPageSize()));
		paginationInfo.setTotalRecordCount(result.getTotalSize());
		
		return paginationInfo;
	}
	
	/**
	 * 검색결과 랭킹 목록을 화면 표시용으로 가공.
	 * @param resultList - 검색 결과 Result[]
	 * @param categoriesMap - 차트 x축 표시정보
	 * @param keywordMap - 랭킹 키워드 정보
	 * @return 화면표시용 랭킹 정보
	 */
	public HashMap<String, Object> getRanking(Result[] resultList, LinkedHashMap<String, String> categoriesMap, LinkedHashMap<String,String> keywordMap){
		GroupResult[] groupResults = null;
		LinkedHashMap<String, Integer> dataMap = null;
		HashMap<String, Object> rankingMap = new HashMap<String, Object>();
		List<LinkedHashMap<String, Integer>> rankingList = new ArrayList<LinkedHashMap<String, Integer>>();
		List<HashMap<String, Object>> mapList = null;
		
		// 기간별로 랭킹을 집계하기 위해 기간맵 생성
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			Result result = (Result)resultList[i];
			dataMap = new LinkedHashMap<String, Integer>();
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				for (int j = 0; j < groupResults.length; j++) {
					int rSize = groupResults[j].groupResultSize();
					for (int k = 0; k < rSize; k++) {
						dataMap.put(new String(groupResults[j].getId(k)), k+1);
					}
				}
			}
			rankingList.add(dataMap);
		}
		
		// 기간명(화면 표시명) 리스트
		List<String> keys = new ArrayList<String>(categoriesMap.keySet());
		
		// 기간별 랭킹 집계
		for(int i=0; i < keys.size(); i++){
			LinkedHashMap<String, Integer> prevMap = (LinkedHashMap<String, Integer>)rankingList.get(i); // 전 기간
			LinkedHashMap<String, Integer> curMap = (LinkedHashMap<String, Integer>)rankingList.get(i+1); // 당 기간
			mapList = new ArrayList<HashMap<String, Object>>();
			
			for (Entry<String, Integer> entry : curMap.entrySet()) {
				if(mapList.size() >= 10) break;
				HashMap<String, Object> map = new HashMap<String, Object>();
				// 전 기간에 당 기간의 키워드 있음
				if(prevMap.containsKey(entry.getKey())){
					int curVal = entry.getValue();
					int prevVal = prevMap.get(entry.getKey());
					map.put("keyword", entry.getKey());
					map.put("ranking", Math.abs(curVal-prevVal));
					if(keywordMap.containsKey(entry.getKey())){
						map.put("highlight", "rank_key rank_mykey");
					}else{
						map.put("highlight", "rank_key");
					}
					if(curVal-prevVal < 0){
						map.put("order", "up");
					}else if(curVal-prevVal > 0){
						map.put("order", "down");
					}else{
						map.put("order", "equable");
					}
				// 전 기간에 당기간 키워드 없음
				}else{
					map.put("highlight", "rank_key");
					map.put("keyword", entry.getKey());
					map.put("order", "new");
					map.put("ranking", 0);
					
					if(keywordMap.containsKey(entry.getKey())){
						map.put("highlight", "rank_key rank_mykey");
					}else{
						map.put("highlight", "rank_key");
					}
				}
				mapList.add(map);
			}
			// 랭킹 집계
			rankingMap.put(keys.get(i), mapList);
		}
		
		// 기간명
		rankingMap.put("periodKeyword", categoriesMap);
		
		return rankingMap;
	}
	
	/**
	 * column 차트 x축 표시정보.
	 * @param vo - 조회 할 정보가 담긴 BaseVo
	 * @return 차트 x축 표시정보
	 */
	public LinkedHashMap<String, String> getCategories(BaseVo vo){
		
		LinkedHashMap<String, String> categoriesMap = null;
		String strDate = vo.getStartDate().replace("/", "") + "000000";
		String endDate = vo.getEndDate().replace("/", "") + "235959";
		String condition = vo.getCondition();

		if(condition.equals("DAY")){
			categoriesMap = DateUtil.getCategories(strDate, endDate, "MM월dd일", Globals.COM_PERIOD_DAY);
		}else if(condition.equals("WEEK")){
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.COM_PERIOD_WEEK);
		}else if(condition.equals("MONTH")){
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.COM_PERIOD_MONTH);
		}else if(condition.equals("QUARTER")){
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.COM_PERIOD_QUARTER);
		}else if(condition.equals("HALF")){
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.COM_PERIOD_HALF);
		}else{
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.COM_PERIOD_YEAR);
		}
		
		return categoriesMap;	
	}
	
	/**
	 * column 차트 x축 표시정보.
	 * @param vo - 조회 할 정보가 담긴 BaseVo
	 * @return 차트 x축 표시정보
	 */
	public HashMap<String, Object> getExcel(Result[] resultList){
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		List<HashMap<String, String>> listResult = new ArrayList<HashMap<String, String>>();
		
		if(resultList!=null) {
			HashMap<String, String> resultMap = null;
			
			for(int i = 0; resultList != null && i < 1; i++) {
				Result result = (Result)resultList[i];
				if(result.getRealSize() != 0){
					
					for (int j = 0; j < result.getRealSize(); j++) {
						resultMap = new HashMap<String, String>();
						resultMap.put(Globals.FIELD_TITLE, new String(result.getResult(j,0)));
						resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(j,1)));
						resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(j,2)));
						
						String date = "";
						if(result.getResult(j,3)!=null){
							date = new String(result.getResult(j,3));
							if(date.length()>7){
								date = date.substring(0,4)+"."+date.substring(4,6)+"."+date.substring(6,8);
							}
						}
						resultMap.put("REGDATE", date);
						
						resultMap.put(Globals.FIELD_ID, new String(result.getResult(j,4)));
						
						int weight = Integer.parseInt(new String(result.getResult(j,5)));
						if(weight > 0){
							resultMap.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
						}else{
							resultMap.put(Globals.FIELD_WEIGHT, "0");
						}
						listResult.add(resultMap);
					}
				}	
			}
		}
		searchResult.put("listResult", listResult);
		
		return searchResult;	
	}
}
