package com.diquest.voc.trend.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.GroupBySet;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QueryParser;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.BaseService;
import com.diquest.voc.fieldStatus.vo.FieldStatusVo;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.InterestKeywordService;
import com.diquest.voc.management.vo.InterestKeywordVo;
import com.diquest.voc.stationStatus.service.impl.StationStatusDAO;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.diquest.voc.trend.service.TrendAnalysisService;
import com.diquest.voc.trend.vo.TrendAnalysisVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

	@Service("trendAnalysisService")
	public class TrendAnalysisServiceImpl extends BaseService implements TrendAnalysisService{

	/** stationStatusDAO */
	@Resource(name = "stationStatusDAO")
	private StationStatusDAO stationStatusDAO;
	
	private Gson gson = null;
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	private static final boolean MARINER_LOGGABLE = true; // 검색엔진 로그 저장 여부
	private static final boolean MARINER_PRINT_QUERY = false; // 검색엔진 검색쿼리 출력 여부
	private static final boolean MARINER_DEBUG = true; // 검색엔진 디버그 사용 여부
	private static final boolean MARINER_PRITER = false; //개인정보보호 필터(PRITER) 사용 여부

	// SELECTSET 분기
	private static final int SELECTSET_TREND = 100; 			// 조회용
	private static final int SELECTSET_LIST = 101; // 검색결과 리스트
	private static final int SELECTSET_DETAIL = 102; // 검색결과 리스트
	private static final int SELECTSET_LIST_COMPARE = 103;
	
	// WHERESET 분기
	private static final int WHERESET_TRENDCHART = 200; 		// 트렌드 차트 조회조건
	private static final int WHERESET_SEARCHKEYWORD = 201; 		// 키워드에 대한 하단 VOC 검색결과
	private static final int WHERESET_DETAIL = 202; 			// 상세보기
	private static final int WHERESET_PERIOD = 203;
	private static final int WHERESET_METRODEPT = 204;	
	
	// FILTERSET 분기
	private static final int FILTERSET_TRENDCHART = 300; 		// 트렌드 차트
	
	// GROUPBYSET 분기
	private static final int GROUPBYSET_TRENDCHART = 400; 		// 트렌드 차트
	private static final int GROUPBYSET_VOC_CATEGORY = 401;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_RANKING = 402; 		// 종합랭킹, 상위키워드 10개 추출
	private static final int GROUPBYSET_METRODEPT = 403;	// 처리주무부서 카운트 
		
	@Override
	public String trendAnalysisReport(TrendAnalysisVo vo) throws Exception {
				
		LinkedHashMap<Result, String> reusltArr = null;		
		HashMap<String, Object> chart = new HashMap<String, Object>();
		ArrayList<Query> queryList = new ArrayList<Query>();
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		LinkedHashMap<String, String> categoriesMap = getCategories(vo); // x축
		String searchTerm = vo.getKeyword();
		String[] searchTermArr = null;			//주제어가 여러개 입력되었을 시에 대비한 배열 변환
		ArrayList<String> keywordList = new ArrayList<String>();
		ArrayList<String> exclusionList = new ArrayList<String>();
		
		if(searchTerm.indexOf(",") > -1) searchTermArr = searchTerm.split(",");
		else {
			searchTermArr = new String[1];
			searchTermArr[0] = searchTerm;
		}
		for(int i=0; i<searchTermArr.length; i++){
			if(!searchTermArr[i].trim().equals(""))
				keywordList.add(searchTermArr[i].trim());
		}
		
		vo.setKeywordList(keywordList);
		
		String[] exclusionArr = null;	//제외에 설정
		if(!vo.getExclusion().equals("")){
			if(vo.getExclusion().indexOf(",") > -1) exclusionArr = vo.getExclusion().split(",");
			else {
				exclusionArr = new String[1];
				exclusionArr[0] = vo.getExclusion();
			}
		}
		if(exclusionArr != null && !exclusionArr.equals("")){
			for(int i=0; i<exclusionArr.length; i++){
				if(!exclusionArr[i].trim().equals(""))
					exclusionList.add(exclusionArr[i].trim());
			}
		}
		vo.setExclusionList(exclusionList);
		// 순위표 맨앞의 이전 순위구하기
		
		QuerySet querySet = new QuerySet(keywordList.size());	
		
		for(int i = 0; i< keywordList.size(); i++) {
			if(!"".equals(keywordList.get(i))) {
							
				Query query = new Query();
				query.setSelect(createSelectSet(SELECTSET_TREND));
				query.setWhere(createWhereSet(WHERESET_TRENDCHART, vo, keywordList.get(i))); // case를 위한 번호, 키워드, VO
				query.setFilter(createFilterSet(vo));
				query.setGroupBy(createGroupBySet(GROUPBYSET_TRENDCHART, vo));
				query.setFrom(Globals.MARINER_COLLECTION);
				query.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
				query.setSearch(true);
				query.setUserName(vo.getUserId()); //로그에 남길 키워드 명시
				query.setDebug(MARINER_DEBUG);
				query.setPrintQuery(MARINER_PRINT_QUERY);
				query.setLoggable(MARINER_LOGGABLE);
				query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
				query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
				if(MARINER_PRITER){
					query.setResultModifier("priter"); 
					query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
				}
				querySet.addQuery(query);

				QueryParser queryParser = new QueryParser();
//				System.out.println("##### 차트 \"" + searchTermArr[i] + "\" : " + queryParser.queryToString(query));
			}
		}

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		Result[] resultList = null;
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
			reusltArr = new LinkedHashMap<Result, String>();
			for(int i=0; i < resultList.length; i++){
				reusltArr.put(resultList[i], "");
			}
		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
			reusltArr.put(resultList[0], "");
		}
		
		
		chart.put("series", getSeries(reusltArr, categoriesMap, vo));
		chart.put("categories", categoriesMap.values());
		chart.put("metroDept", getMetroDept(vo));

		return gson.toJson(chart);
	}
	
	// 처리주무부서 결과 구하기 
	private ArrayList<HashMap<String, String>> getMetroDept(TrendAnalysisVo vo) throws Exception{
		
		ArrayList<HashMap<String, String>> metroDeptList = new ArrayList<HashMap<String, String>>();	// 처리주무부서 리스트
		Result[] resultlist = null;
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_TREND));
		query.setWhere(createWhereSet(WHERESET_METRODEPT, vo, "")); // case를 위한 번호, 키워드, VO
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_METRODEPT, vo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
		query.setSearch(true);
		query.setUserName(vo.getUserId()); //로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);

		QueryParser queryParser = new QueryParser();

		//System.out.println("##### 역별현황분석 query11: " + queryParser.queryToString(query));
		

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		// 쿼리결과에 문제가 없을경우는 1, 있으면 마이너스(-)
		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
		}

		if (resultlist != null) {
			if (resultlist[0] != null && resultlist[0].getTotalSize() != 0) {
				Result result = resultlist[0];								
				GroupResult[] groupResults = result.getGroupResults();
				
				// 처리주무부서 그룹결과
				
				ArrayList<String> cdDeptList = new ArrayList<String>();	//부서코드 정렬순서 저장
				for(int j = 0; j < groupResults[0].groupResultSize(); j++){
					String deptCode = new String(groupResults[0].getId(j));
					if(deptCode != null && !"".equals(deptCode)){
						cdDeptList.add(deptCode);
					}
				}
				
				HashMap<String, Object> paramMap = new HashMap<String,Object>();
				paramMap.put("codeList", cdDeptList);
				List<HashMap<String, Object>> deptFullList = stationStatusDAO.selectMetroList(paramMap);	//부서코드 리스트로 처리주무부서 리스트 조회
				HashMap<String, Object> selectDeptCodeTable = new HashMap<String,Object>(); 
				for(HashMap<String, Object> tempMap : deptFullList){	//조회한 부서정보 MAP에 부서코드를 키값으로 저장
					selectDeptCodeTable.put((String)tempMap.get("CODE"), (String)tempMap.get("NAME"));
				}
				

			    List<String> list = new ArrayList<String>();
		        list.addAll(selectDeptCodeTable.keySet());
		        Collections.sort(list,new Comparator<Object>(){
					@SuppressWarnings("unchecked")
					public int compare(Object o1,Object o2){
		                Object v1 = selectDeptCodeTable.get(o1);
		                Object v2 = selectDeptCodeTable.get(o2);
		                return ((Comparable<Object>) v1).compareTo(v2);
		            }
		        });
		        
		        Iterator<String> it = list.iterator();
		        while(it.hasNext()){
		            String temp = (String) it.next();
		            HashMap<String, String> deptInfoMap = new HashMap<String, String>();
					deptInfoMap.put("CODE", temp);
					deptInfoMap.put("NAME", (String)selectDeptCodeTable.get(temp));
					metroDeptList.add(deptInfoMap);
		        }

			}
		}
		return metroDeptList;
	}
	
	//리포트 차트 x축(기간) 출력
	public LinkedHashMap<String, String> getCategories(TrendAnalysisVo vo){

		LinkedHashMap<String, String> categoriesMap = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if ("".equals(vo.getStartDate()) || "".equals(vo.getEndDate())) {
			vo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			vo.setStartDate(sdf.format(cal.getTime()));
		}

		String startDate = vo.getStartDate().replace("/", "") + "000000";
		String endDate = vo.getEndDate().replace("/", "") + "235959";

		String condition = vo.getCondition();
		if (condition.equals("DAY")) {

			categoriesMap = DateUtil.getCategories(startDate, endDate, "MM월 dd일", Globals.TREND_PERIOD_DAY);
		} else if (condition.equals("WEEK")) {
			categoriesMap = DateUtil.getCategories(startDate, endDate, "", Globals.TREND_PERIOD_WEEK);
		} else if (condition.equals("MONTH")) {
			categoriesMap = DateUtil.getCategories(startDate, endDate, "", Globals.TREND_PERIOD_MONTH);
		} else if (condition.equals("QUARTER")) {
			categoriesMap = DateUtil.getCategories(startDate, endDate, "", Globals.TREND_PERIOD_QUARTER);
		} else if (condition.equals("HALF")) {
			categoriesMap = DateUtil.getCategories(startDate, endDate, "", Globals.TREND_PERIOD_HALF);
		} else {// YEAR
			categoriesMap = DateUtil.getCategories(startDate, endDate, "", Globals.TREND_PERIOD_YEAR);
		}

		return categoriesMap;
	}
	
	// 종합 랭킹 분석 구성
	public LinkedHashMap<String, Object> getSynthesisRanking(TrendAnalysisVo vo) throws Exception {
		
		LinkedHashMap<String, Object> totalResultMap = new LinkedHashMap<String, Object>(); // 최종 리턴 맵
		LinkedHashMap<String, Object> totalPeriodKeyword = new LinkedHashMap<String, Object>(); // 주기별 랭킹의 키워드순위 정보를 담는 맵

		//String[] iKeywordArr = keywordRankingVo.getInterestKeywordArr(); // 관심키워드
		
		LinkedHashMap<String, String> periodMap = getCategories(vo); // 종합리포트 챠트 x축(기간)
		
		ArrayList<String> periodList = new ArrayList<String>(periodMap.keySet()); // 기간 리스트
		ArrayList<HashMap<String, Object>> periodRankingList = null; // 기간별 키워드랭킹정보

		HashMap<String, Integer> pKeywordRanking = null;
		HashMap<String, Integer> nKeywordRanking = new HashMap<String, Integer>();

		int index = vo.getRankingIndex(); // 랭킹표 생성시 주기가 들어 있는 맵의 시작인덱스
		int stateNum = periodList.size();
		
		String searchTerm = vo.getKeyword();
		String[] searchTermArr = null;			//주제어가 여러개 입력되었을 시에 대비한 배열 변환
		ArrayList<String> keywordList = new ArrayList<String>();
		ArrayList<String> exclusionList = new ArrayList<String>();
		
		if(searchTerm.indexOf(",") > -1) searchTermArr = searchTerm.split(",");
		else {
			searchTermArr = new String[1];
			searchTermArr[0] = searchTerm;
		}
		for(int i=0; i<searchTermArr.length; i++){
			if(!searchTermArr[i].trim().equals(""))
				keywordList.add(searchTermArr[i].trim());
		}
		
		vo.setKeywordList(keywordList);
		
		String[] exclusionArr = null;	//제외에 설정
		if(!vo.getExclusion().equals("")){
			if(vo.getExclusion().indexOf(",") > -1) exclusionArr = vo.getExclusion().split(",");
			else {
				exclusionArr = new String[1];
				exclusionArr[0] = vo.getExclusion();
			}
		}
		if(exclusionArr != null && !exclusionArr.equals("")){
			for(int i=0; i<exclusionArr.length; i++){
				if(!exclusionArr[i].trim().equals(""))
					exclusionList.add(exclusionArr[i].trim());
			}
		}
		vo.setExclusionList(exclusionList);
		// 순위표 맨앞의 이전 순위구하기
		String prevPeriod = "";
		try {
			prevPeriod = periodList.get(index - 1);
		} catch (Exception e) { //// periodMap의 맨처음 기간의 이전 기간구하기

			String startDate = vo.getStartDate().replace("/", "");
			String condition = vo.getCondition();

			if (condition.equals("DAY")) {
				prevPeriod = DateUtil.addYearMonthDay("yyyyMMdd", startDate, Calendar.DAY_OF_MONTH, -1);
			} else if (condition.equals("MONTH")) {
				prevPeriod = DateUtil.addYearMonthDay("yyyyMM", startDate.substring(0, 6), Calendar.MONTH, -1);
			} else if (condition.equals("YEAR")) {
				prevPeriod = DateUtil.addYearMonthDay("yyyy", startDate.substring(0, 4), Calendar.YEAR, -1);
			} else if (condition.equals("WEEK")) {
				prevPeriod = DateUtil.addYearMonthDay("yyyyMMdd", startDate, Calendar.DAY_OF_MONTH, -7);
				String week = "0" + DateUtil.getWeek("yyyyMMdd", prevPeriod);// 해당 월의 주 출력
				prevPeriod = prevPeriod.substring(0, 6) + week;// prevPeriod는 startDate따른 전주
			} else if (condition.equals("QUARTER")) {
				prevPeriod = DateUtil.addYearMonthDay("yyyyMM", startDate.substring(0, 6), Calendar.MONTH, -3);
				int month = Integer.parseInt(prevPeriod.substring(4, 6));
				if (month / 3 == 0) {
					prevPeriod = prevPeriod.substring(0, 4) + "01";
				} else if (month / 3 == 1) {
					prevPeriod = prevPeriod.substring(0, 4) + "02";
				} else if (month / 3 == 2) {
					prevPeriod = prevPeriod.substring(0, 4) + "03";
				} else {
					prevPeriod = prevPeriod.substring(0, 4) + "04";
				}
			} else if (condition.equals("HALF")) {
				prevPeriod = DateUtil.addYearMonthDay("yyyyMM", startDate.substring(0, 6), Calendar.MONTH, -6);
				int month = Integer.parseInt(prevPeriod.substring(4, 6));

				if (month / 6 == 0) {
					prevPeriod = prevPeriod.substring(0, 4) + "01";
				} else {
					prevPeriod = prevPeriod.substring(0, 4) + "02";
				}
			}
		}

		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_TREND));
		query.setWhere(createWhereSet(WHERESET_PERIOD, vo, prevPeriod));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, vo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);

		QueryParser queryParser = new QueryParser();
		//System.out.println("##### 종합랭킹 분석 query: " + queryParser.queryToString(query));

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if (resultlist != null) {
				result = resultlist[0];
			}
		}
		if (result != null && result.getGroupResultSize() != 0) {
			GroupResult[] groupResultlist = result.getGroupResults();
			for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
				String tempKeyword = new String(groupResultlist[0].getId(i)).trim();
				// 이전 주의 전체 키워드 값 hasmap에 세팅 - 순위 구하기 위함
				if(tempKeyword != null && !"".equals(tempKeyword)){
					nKeywordRanking.put(tempKeyword, i + 1);
				}
			}
		}
		// 순위표 맨앞의 이전 순위구하기 끝

		int periodCount = 0;
		while (periodCount < stateNum) {

			String period = periodList.get(index + periodCount); // 선택한 index 부터 4개까지
			querySet = new QuerySet(1);
			query = new Query();
			query.setSelect(createSelectSet(SELECTSET_TREND));
			query.setWhere(createWhereSet(WHERESET_PERIOD, vo, period));
			query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, vo));
			query.setFrom(Globals.MARINER_COLLECTION);
			query.setResult(0, 0);
			query.setSearch(true);
			query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
			query.setDebug(MARINER_DEBUG);
			query.setLoggable(MARINER_LOGGABLE);
			query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
			query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			if(MARINER_PRITER){
				query.setResultModifier("priter"); 
				query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
			}
			querySet.addQuery(query);
			//System.out.println("##### 종합랭킹 분석 query2: " + queryParser.queryToString(query));

			// 검색 서버로 검색 정보 전송
			CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
			command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
			int keywordReturnCode = command.request(querySet);

			if (keywordReturnCode >= 0) {
				ResultSet results = command.getResultSet();
				resultlist = results.getResultList();
				if (resultlist != null) {
					result = resultlist[0];
				}
			}

			if (result != null && result.getGroupResultSize() != 0) {

				GroupResult[] groupResultlist = result.getGroupResults();

				periodRankingList = new ArrayList<HashMap<String, Object>>();

				// - pKeywordRanking는 이전키워드들 정보 담고 있는 배열
				pKeywordRanking = nKeywordRanking;
				nKeywordRanking = new HashMap<String, Integer>();

				// - 전체 키워드 값 Map에 세팅 -start-
				// - groupResultlist[0].groupResultSize() - 그룹핑 된 전체 키워드 갯수
				// - pKeywordRanking.size() - 이전 (일/주/월/분기/년) 그룹핑 된 키워드 갯수
				Map<String, String> totalKeyWord = new HashMap<String, String>();
				int count = 0;
				List<String> topRankingKeyWord = new ArrayList<String>();// 종합랭킹 10위 까지만 배열 변수
				for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
					String tempKeyword = new String(groupResultlist[0].getId(i)).trim();
					if (tempKeyword != null && !"".equals(tempKeyword)) {
						if (count < 10) {
							// 제외어가 있는 경우 해당 키워드 건너뛰기
							boolean exeFlag = true;	
							if(vo.getExclusionList()!= null && vo.getExclusionList().size() > 0){
								for(int j=0; j<vo.getExclusionList().size(); j++){
									if(vo.getExclusionList().get(j).equals(tempKeyword)){
										exeFlag = false;
									}
								}
							}
							if(exeFlag == true){
								// - 종합랭킹 10위 까지만 배열에 담기
								topRankingKeyWord.add(tempKeyword);
								count++;
							}
																			
						}
						totalKeyWord.put(tempKeyword, groupResultlist[0].getIntValue(i) + "/" + (i + 1));
						nKeywordRanking.put(tempKeyword, (i + 1));// 현재달의 랭킹 정보를 담는다. 다음달의 랭킹 정보 구할때 필요
					}
					
				}
				// - 전체 키워드 값 Map에 세팅 -end-

				// - 관심키워드 랭킹 설정 -start- 종합랭킹사이즈 만큼 반복 돌아서 종합랭킹키워드를 키값으로 totalKeyWord_Map에 해당 키워드가 있는지 찾아낸다.
				if (groupResultlist[0].groupResultSize() > 0) {
					for (int i = 0; i < topRankingKeyWord.size(); i++) {
						String keyword = topRankingKeyWord.get(i);
						String rank = totalKeyWord.get(keyword);
						if (rank != null) {
							String realRank = rank.split("/")[1];
							rank = rank.split("/")[0];
							int change = 0;
							HashMap<String, Object> keywordMap = new HashMap<String, Object>();
							keywordMap.put("keyword", keyword); // 관심 키워드 값 설정
							keywordMap.put("interestYn", "N");
							if(vo.getKeywordList() != null && !vo.getKeywordList().equals("")){			// 주제어의 경우 하이라이트
								for (int j=0; j<vo.getKeywordList().size(); j++) {
									if (vo.getKeywordList().get(j).equals(keyword)) {
										keywordMap.put("interestYn", "Y");
									}
								}
							}
							// - 이전달에 같은키워드가 있으면 변동랭킹구함
							if (pKeywordRanking.containsKey(keyword)) {
								// - 이전달에 랭킹 구함
								int preRanking = pKeywordRanking.get(keyword);
								change = preRanking - (Integer.parseInt(realRank));
								if (change > 0) {
									keywordMap.put("imagePath", "up");
								} else if (change < 0) {
									keywordMap.put("imagePath", "down");
								} else {
									keywordMap.put("imagePath", "noChange");
								}
							} else {
								keywordMap.put("imagePath", "new");
							}
							keywordMap.put("nowrank", realRank);// 실제 랭킹
							keywordMap.put("change", Math.abs(change));
							periodRankingList.add(keywordMap);
						}
					}
				}
				// - 관심키워드 랭킹 설정 -end-

				// - periodRankingList HashMap List 숫자 오름차순 정렬
				// - 관심키워드의 등록에 따라 순서대 periodRankingList에 담겨 있으나 실질적으론 nowrank 값이 실제 랭킹이다.
				// - 그래서 nowrank 의값에 따라 정렬 해줘야 함.
				Collections.sort(periodRankingList, new Comparator<HashMap<String, Object>>() {
					public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
						int one = Integer.parseInt(o1.get("nowrank").toString());
						int two = Integer.parseInt(o2.get("nowrank").toString());
						return one < two ? -1 : one > two ? 1 : 0;
					}
				});

				// - 관심 키워드 랭킹 구하기 - start-
				// - nowrank에 따라 화면에 보여질 순번 정하기
				int realyRank = 1;
				int realyRankTemp = 0;
				for (int j = 0; j < periodRankingList.size(); j++) {
					HashMap<String, Object> temp = periodRankingList.get(j);
					if (realyRank == 1) {
						periodRankingList.get(j).put("count", realyRank);
						realyRankTemp = Integer.parseInt(temp.get("nowrank").toString());
					} else {
						if (realyRankTemp == Integer.parseInt(temp.get("nowrank").toString())) {
							realyRank--;
							periodRankingList.get(j).put("count", realyRank);
						} else {
							periodRankingList.get(j).put("count", realyRank);
						}
						realyRankTemp = Integer.parseInt(temp.get("nowrank").toString());
					}
					realyRank++;
				}
				// 관심 키워드 랭킹 구하기 - end -
			}

			// - 관심 키워드는 10개까지만 노출이므로 10개 이상은 삭제한다.
			if (periodRankingList.size() > 10) {
				ArrayList<HashMap<String, Object>> resultRankingList = new ArrayList<HashMap<String, Object>>();
				for (int i = 0; i < 10; i++) {
					resultRankingList.add(periodRankingList.get(i));
				}
				periodRankingList = resultRankingList;
			}
			totalPeriodKeyword.put(periodMap.get(period), periodRankingList);
			periodCount++;

		}	//end of while (periodCount < stateNum) {
		totalResultMap.put("periodKeyword", totalPeriodKeyword); // 주기별 키워드랭킹정보
		ArrayList<String> periodTag = new ArrayList<String>();
		for (Object o : periodMap.values()) {
			String period = (String) o;
			periodTag.add(makePeriodTag(period, vo.getCondition()));
		}
		totalResultMap.put("period", periodTag); // 기간선택 버튼

		double btnCount = Math.floor(938 / periodMap.size());	//????? 버튼사이즈???
		totalResultMap.put("btnSize", btnCount); // 버튼 크기 사이즈 임시

		return totalResultMap;
	}
	// - 종합랭킹분석 네비게이션 태그 (임시)
	public String makePeriodTag(String period, String condition) {

		String periodTag = period;
		try {
			if (condition.equals("DAY")) {
				periodTag = "<span>" + period.substring(0, 3) + "</span>" + period.substring(3);
			} else if (condition.equals("WEEK")) {
				periodTag = "<span>" + period.substring(0, 2) + "</span>" + period.substring(2);
			} else if (condition.equals("YEAR")) {
				periodTag = "<span>" + period.substring(0, 4) + "</span>" + period.substring(4);
			} else {// HALF,MONTH,QUARTER
				periodTag = "<span>" + period.substring(0, 5) + "</span>" + period.substring(5);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return periodTag;
	}
	
	/**
	 * VOC 검색결과
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(TrendAnalysisVo vo) throws Exception {
		
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> groupResult = null;
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;
		String share = "0"; // 점유율
		String keywordTemp = null;

		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();

		int startNo = (vo.getCurrentPage() - 1) * vo.getPageSize();
		int endNo = startNo + vo.getPageSize() - 1;

		QuerySet querySet = new QuerySet(2);
		Query query = new Query(startTag, endTag);
		if(vo.getPageType().equals("trendAnalysis"))
			query.setSelect(createSelectSet(SELECTSET_LIST));
		else
			query.setSelect(createSelectSet(SELECTSET_LIST_COMPARE));
		String searchTerm = vo.getKeyword();
		String[] searchTermArr = null;			//주제어가 여러개 입력되었을 시에 대비한 배열 변환
		ArrayList<String> keywordList = new ArrayList<String>();
		ArrayList<String> exclusionList = new ArrayList<String>();
		
		if(searchTerm.indexOf(",") > -1) searchTermArr = searchTerm.split(",");
		else {
			searchTermArr = new String[1];
			searchTermArr[0] = searchTerm;
		}
		for(int i=0; i<searchTermArr.length; i++){
			if(!searchTermArr[i].trim().equals(""))
				keywordList.add(searchTermArr[i].trim());
		}
		
		vo.setKeywordList(keywordList);
		
		String[] exclusionArr = null;	//제외에 설정
		if(!vo.getExclusion().equals("")){
			if(vo.getExclusion().indexOf(",") > -1) exclusionArr = vo.getExclusion().split(",");
			else {
				exclusionArr = new String[1];
				exclusionArr[0] = vo.getExclusion();
			}
		}
		if(exclusionArr != null && !exclusionArr.equals("")){
			for(int i=0; i<exclusionArr.length; i++){
				if(!exclusionArr[i].trim().equals(""))
					exclusionList.add(exclusionArr[i].trim());
			}
		}
		vo.setExclusionList(exclusionList);
		
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, vo.getKeyword()));
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, vo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(startNo, endNo);
		query.setSearch(true);
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();
		//System.out.println("##### 리스트 검색 query1: " + queryParser.queryToString(query));

		// 키워드 없이 전체 건수 구하기(점유율)
		query = new Query();
		query.setSelect(createSelectSet(SELECTSET_TREND));
		/*if (keywordRankingVo.getEmotion() != null && !keywordRankingVo.getEmotion().equals(""))
			query.setWhere(createWhereSet(10, keywordRankingVo));
		else*/
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, vo.getKeyword()));
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, vo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);

//		System.out.println("##### 리스트 검색 query2: " + queryParser.queryToString(query));

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		// resultlist[0]이 해당 기간에 속한 키워드에 관한 문서 검색결과
		// resultlist[1]이 해당 기간에 추출된 keyword 전체 결과 -

		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();

			if (resultlist != null) {
				result = resultlist[0];
				try {

					if (result.getTotalSize() > 0) {

						if (resultlist[1].getTotalSize() > 0) {
							// 키워드에 해당하는 점유율 구하기 - 변수 : share
							Result TotalKeyWordResult = resultlist[1];
							GroupResult[] groupResultlist = TotalKeyWordResult.getGroupResults();

							keywordTemp = vo.getKeyword();
							// - 키워드가 없으면 키워드 점유율이 제일 높은게 키워드가 된다.

							if ((keywordTemp == null || "".equals(keywordTemp)) && groupResultlist[0].groupResultSize() > 0) {
								keywordTemp = new String(groupResultlist[0].getId(0)).trim();
							}

							int totalKeywordCount = 0; // 해당 기간동안의 전체 키워드 갯수
							int selectKeywordCount = 0; // 선택 키워드에 대한 갯수
							for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
								if (keywordTemp.equals(new String(groupResultlist[0].getId(i)).trim())) {
									selectKeywordCount = groupResultlist[0].getIntValue(i); // 해당 키워드의 갯수 구하기
								}
								totalKeywordCount += groupResultlist[0].getIntValue(i); // 전체 키워드 대한 갯수 (누적) 구하기

							}
							if (selectKeywordCount > 0) {
								DecimalFormat df = new DecimalFormat("0.###");
								share = df.format((selectKeywordCount / (double) totalKeywordCount) * 100);
							}
							// 점유율 구하기 끝
						}
					}
				} catch (ArithmeticException e) {
				}
			}
		}
		//선택한 키워드에 대한 문서 검색 결과
		if (result != null ) {
			GroupResult[] groupResultlist = result.getGroupResults();
			NumberFormat nf = NumberFormat.getNumberInstance();

			// 리스트 결과 담기
			listResult = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < result.getRealSize(); i++) {
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(Globals.FIELD_DQ_DOCID, new String(result.getResult(i, 0)));
				resultMap.put(Globals.FIELD_VOCNO, new String(result.getResult(i, 1)));
				String title = new String(result.getResult(i, 2));
				if(title == null || "".equals(title)) title = "제목없음";
				resultMap.put(Globals.FIELD_TITLE, new String(title));
				resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 3)));
				resultMap.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(i, 4)));
				String date = "";
				if (result.getResult(i, 5) != null) {
					date = new String(result.getResult(i, 5));
					if (date.length() > 7) {
						date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
					}
				}
				resultMap.put(Globals.FIELD_REGDATE, date);
				resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 6)));
				listResult.add(resultMap);
			}
			/*if ((keywordRankingVo.getEmotion() != null && !keywordRankingVo.getEmotion().equals("")) && keywordRankingVo.getKeyword().equals(""))
				searchResult.put("keyword", ""); // 검색 키워드
			else*/
			searchResult.put("keyword", keywordTemp); // 검색 키워드
			searchResult.put("listResult", listResult); // 리스트
			searchResult.put("totalSize", nf.format(result.getTotalSize())); // 전체 건수
			searchResult.put("share", share); // 점유율
			int endPage = result.getTotalSize() / vo.getPageSize();
			if (endPage % vo.getPageSize() > 0) {
				endPage++;
			}
			if (endPage == 0) {
				endPage = 1;
			}
			searchResult.put("endPage", endPage); // 끝 페이지
			searchResult.put("currentPage", vo.getCurrentPage()); // 현재 페이지

			/** pageing setting */
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(vo.getCurrentPage());
			paginationInfo.setRecordCountPerPage(vo.getPageSize());
			paginationInfo.setPageSize(10);
			paginationInfo.setTotalRecordCount(result.getTotalSize());

			searchResult.put("paginationInfo", paginationInfo);

		}
		return searchResult;
	}
	
	/**
	 * excel 조회
	 * 
	 * @param EmotionVo
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(TrendAnalysisVo vo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;
		
		String searchTerm = vo.getKeyword();
		String[] searchTermArr = null;			//주제어가 여러개 입력되었을 시에 대비한 배열 변환
		ArrayList<String> keywordList = new ArrayList<String>();
		ArrayList<String> exclusionList = new ArrayList<String>();
		
		if(searchTerm.indexOf(",") > -1) searchTermArr = searchTerm.split(",");
		else {
			searchTermArr = new String[1];
			searchTermArr[0] = searchTerm;
		}
		for(int i=0; i<searchTermArr.length; i++){
			if(!searchTermArr[i].trim().equals(""))
				keywordList.add(searchTermArr[i].trim());
		}
		
		vo.setKeywordList(keywordList);
		
		String[] exclusionArr = null;	//제외에 설정
		if(!vo.getExclusion().equals("")){
			if(vo.getExclusion().indexOf(",") > -1) exclusionArr = vo.getExclusion().split(",");
			else {
				exclusionArr = new String[1];
				exclusionArr[0] = vo.getExclusion();
			}
		}
		if(exclusionArr != null && !exclusionArr.equals("")){
			for(int i=0; i<exclusionArr.length; i++){
				if(!exclusionArr[i].trim().equals(""))
					exclusionList.add(exclusionArr[i].trim());
			}
		}
		vo.setExclusionList(exclusionList);

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_LIST));
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, vo.getKeyword()));
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, vo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 999);
		query.setSearch(true);
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);

		QueryParser queryParser = new QueryParser();
		//System.out.println("##### 엑셀결과를 얻기위한 쿼리: " + queryParser.queryToString(query));

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if (resultlist != null) {
				result = resultlist[0];
				if (result != null ) {
					// 리스트 결과 담기
					listResult = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < result.getRealSize(); i++) {
						HashMap<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put(Globals.FIELD_DQ_DOCID, new String(result.getResult(i, 0)));
						resultMap.put(Globals.FIELD_VOCNO, new String(result.getResult(i, 1)));
						resultMap.put(Globals.FIELD_TITLE, new String(result.getResult(i, 2)));
						resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 3)));
						resultMap.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(i, 4)));
						String date = "";
						if (result.getResult(i, 5) != null) {
							date = new String(result.getResult(i, 5));
							if (date.length() > 7) {
								date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
							}
						}
						resultMap.put(Globals.FIELD_REGDATE, date);
						resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 6)));
						
						listResult.add(resultMap);
					}
					searchResult.put("listResult", listResult); // 리스트
				}

			}
		}

		return searchResult;
	}
	
	/*
	@Override
	public HashMap<String, Object> trendAnalysisRanking(TrendAnalysisVo vo) throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Query> queryList = new ArrayList<Query>();
		LinkedHashMap<String,String> keywordMap = new LinkedHashMap<String,String>();
		
		List<EgovMap> interestList = interestService.selectInterestKeywordListTop10(new InterestKeywordVo());
		
		for(EgovMap interest : interestList){
			keywordMap.put((String)interest.get("keyword"), (String)interest.get("keyword"));
		}
		
		LinkedHashMap<String, String> categoriesMap = getCategories(vo); // x축
		
		queryList.add(createQuery(Globals.TREND_AL_RANKING_PERIOD_OLD, vo, "")); // 이전 랭킹
		
		for (Entry<String, String> entry : categoriesMap.entrySet()) { // 전회분
			queryList.add(createQuery(Globals.TREND_AL_RANKING_PERIOD, vo, entry.getKey()));
		}
		
		// QuerySet 인스턴스화
		querySet = new QuerySet(queryList.size());
		
		for(int i=0; i < queryList.size(); i++){
			querySet.addQuery((Query) queryList.get(i));
		}

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
		}

		map = this.getRanking(resultList, categoriesMap, keywordMap, vo);

		return map;
	}
	
	@Override
	public HashMap<String, Object> trendAnalysisList(TrendAnalysisVo vo) throws Exception {
		
		HashMap<String, Object> chart = new HashMap<String, Object>();
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 본문 리스트 검색
		queryList.add(createQuery(Globals.TREND_AL_LIST, vo, ""));
		
		String[] searchTermArr = null;
		if(vo.getTerm().indexOf(",") > -1) {
			searchTermArr = vo.getTerm().split(",");
			// 유형별 점유율
			queryList.add(createQuery(Globals.TREND_AL_KEYWORD_PERCENTAGE, vo, searchTermArr[0]));

		}else{
			queryList.add(createQuery(Globals.TREND_AL_KEYWORD_PERCENTAGE, vo, vo.getTerm()));
		}
		
		
		// QuerySet 인스턴스화
		querySet = new QuerySet(queryList.size());
		
		for(int i=0; i < queryList.size(); i++){
			querySet.addQuery((Query) queryList.get(i));
		}

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
		}else{
			resultList = new Result[2];
			resultList[0]= new Result();
			resultList[1]= new Result();
		}
		
		// 검색결과 가공
		//System.out.println("페이징 확인 : "+resultList[0].getTotalSize());
		chart.put("listResult", getList(resultList[0])); // 리스트
		chart.put("groupResult", getCategoryList(resultList[0])); // 카테고리 리스트
		chart.put("paginationInfo", getPaging(resultList[0], vo)); // 페이징
		chart.put("totSize", resultList[0].getTotalSize()); // 페이징
		if(vo.getTerm().indexOf(",") > -1) {
			chart.put("share", getShare(resultList[1], searchTermArr[0])); // 점유율
		}else{
			chart.put("share", getShare(resultList[1], vo.getTerm())); // 점유율
		}
		

		return chart;
	}
	
	@Override
	public HashMap<String, Object> trendAnalysisExcelList(TrendAnalysisVo vo) throws Exception {
		
		Result[] resultList = null;
		CommandSearchRequest command = null;
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(createQuery(Globals.TREND_AL_EXCEL, vo, ""));
		
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
		}
		
		return getExcel(resultList);
	}
	
	@Override
	public HashMap<String, String> trendAnalysisDetailView(TrendAnalysisVo vo) throws Exception {
		
		Result[] resultList = null;
		CommandSearchRequest command = null;
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(createQuery(Globals.TREND_AL_DETAIL, vo, ""));
		
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
		}
		
		return getDetail(resultList);
	}
	
	@Override
	public List<Object> trendAnalysisAlikeView(TrendAnalysisVo vo) throws Exception {
		
		Result[] resultList = null;
		CommandSearchRequest command = null;
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(createQuery(Globals.TREND_AL_ALIKE, vo, ""));

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
		}
		
		return getAlikeList(resultList);
	}
	*/
	/**
	 * 검색결과 랭킹 목록을 화면 표시용으로 가공.
	 * @param resultList - 검색 결과 Result[]
	 * @param categoriesMap - 차트 x축 표시정보
	 * @param keywordMap - 랭킹 키워드 정보
	 * @return 화면표시용 랭킹 정보
	 *//*
	public HashMap<String, Object> getRanking(Result[] resultList, LinkedHashMap<String, String> categoriesMap, LinkedHashMap<String,String> keywordMap, TrendAnalysisVo vo){
		GroupResult[] groupResults = null;
		LinkedHashMap<String, Integer> dataMap = null;
		HashMap<String, Object> rankingMap = new HashMap<String, Object>();
		List<LinkedHashMap<String, Integer>> rankingList = new ArrayList<LinkedHashMap<String, Integer>>();
		List<HashMap<String, Object>> mapList = null;
		String trendKeyword = vo.getTerm();
		
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
			
			// 랭킹에 트렌드 키워드 존재
			boolean exist = true;
			
			for (Entry<String, Integer> entry : curMap.entrySet()) {
				if(mapList.size() >= 10) {
					
					// 트렌드 키워드가 랭킹에 포함되지 않은 경우 가장 마지막 랭킹을 트렌드 키워드로 변경
					if(exist){
						
						HashMap<String, Object> map = new HashMap<String, Object>();
						
						// 전 기간에 당 기간의 키워드 있음
						if(prevMap.containsKey(trendKeyword) && curMap.containsKey(trendKeyword)){
							int curVal = curMap.get(trendKeyword);
							int prevVal = prevMap.get(trendKeyword);
							map.put("trendRanking", curVal);
							map.put("keyword", trendKeyword);
							map.put("ranking", Math.abs(curVal-prevVal));
							if(keywordMap.containsKey(trendKeyword)){
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
						}else if(curMap.containsKey(trendKeyword)){
							map.put("trendRanking", curMap.get(trendKeyword));
							map.put("highlight", "rank_key");
							map.put("keyword", trendKeyword);
							map.put("order", "new");
							map.put("ranking", 0);
							
							if(keywordMap.containsKey(trendKeyword)){
								map.put("highlight", "rank_key rank_mykey");
							}else{
								map.put("highlight", "rank_key");
							}
						}
						if(map.size()>0){
							mapList.set(mapList.size()-1, map);
						}
					}
					break;
				}
				
				// 랭킹TOP10에 트렌드 키워드가 존재하지 않음
				if(trendKeyword.equals(entry.getKey())){
					exist = false;
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				// 전 기간에 당 기간의 키워드 있음
				if(prevMap.containsKey(entry.getKey())){
					int curVal = entry.getValue();
					int prevVal = prevMap.get(entry.getKey());
					map.put("trendRanking", "0");
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
					map.put("trendRanking", "0");
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
	 * 검색결과를 차트 표시용 Series로 가공.
	 * @param resultMap - 검색 결과 Result
	 * @param categoriesMap - 차트 x축 표시정보
	 * @param items - 검색 키워드 리스트
	 * @return Series 목록
	 */
	private List<Object> getSeries(LinkedHashMap<Result, String> reusltArr, LinkedHashMap<String, String> categoriesMap, TrendAnalysisVo vo){
		GroupResult[] groupResults = null;
		LinkedHashMap<String, Integer> dataMap = null;
		List<Object> list = new ArrayList<Object>();
			dataMap = new LinkedHashMap<String, Integer>();
					
			// y축 표시 정보
			int i = 0;
			int keywordCnt = 0;
			for(Result result : reusltArr.keySet()){
				// x축 표시 정보
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					
					int rSize = groupResults[0].groupResultSize();
					for (int k = 0; k < rSize; k++) {
						dataMap.put(new String(groupResults[0].getId(k)), groupResults[0].getIntValue(k));
					}
					
					boolean flag = true;
					for(int j=0; j < vo.getExclusionList().size(); j++){		// 제외어 처리
						if(vo.getKeywordList().get(keywordCnt).equals(vo.getExclusionList().get(j))){
							flag = false;
						}
					}
					
					if(flag == true){
						HashMap<String, Object> seriesMap = new HashMap<String, Object>();
						seriesMap.put("name", vo.getKeywordList().get(keywordCnt));
						seriesMap.put("type", "line");
						seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
						list.add(i, seriesMap);
						//}
						i++;
					}
					keywordCnt++;
/*
					HashMap<String, Object> seriesMap2 = new HashMap<String, Object>();
					seriesMap2.put("name", term[0]);
					seriesMap2.put("type", "column");
					seriesMap2.put("data", new ArrayList<Integer>(dataMap.values()));
					
					list.add(1, seriesMap2);*/
				}
			}
			
		return list;
	}
	
	/**
	 * SelectSet 설정.
	 * @return SelectSet[]
	 */
	private SelectSet[] createSelectSet(int flag){
		
		SelectSet[] selectSet = null;		
		switch (flag) {
			case SELECTSET_TREND:
				selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE) };
				break;
			case SELECTSET_LIST:	// VOC 검색 결과
				selectSet = new SelectSet[] { 
						new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE),	//수집문서ID
						new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE),	//제목
						new SelectSet(Globals.FIELD_TITLE, (byte)(Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 120), // HIGHLIGHT
						new SelectSet(Globals.FIELD_CONTENT, (byte) (Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 400),
						new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE),
						new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE),
						new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE),
				};
				break;
			case SELECTSET_DETAIL:	// 상세페이지
				selectSet = new SelectSet[]{
						new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE),		//수집문서ID
						new SelectSet(Globals.FIELD_TITLE, Protocol.SelectSet.NONE),	//제목
						new SelectSet(Globals.FIELD_CONTENT,  Protocol.SelectSet.NONE),		//본문
						new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE),		//등록일
						new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE),		//가중치
						new SelectSet(Globals.FIELD_CDVOCCHANNEL, Protocol.SelectSet.NONE),	//접수채널
						new SelectSet(Globals.FIELD_CDVOCRECTYPE, Protocol.SelectSet.NONE),	//접수종류
						new SelectSet(Globals.FIELD_CDVOCKIND, Protocol.SelectSet.NONE),	//접수분류 - 대
						new SelectSet(Globals.FIELD_CDVOCPART, Protocol.SelectSet.NONE),	//접수분류 - 중
						new SelectSet(Globals.FIELD_CDVOCITEM, Protocol.SelectSet.NONE),	//접수분류 - 소
						new SelectSet(Globals.FIELD_CDREPDEPT, Protocol.SelectSet.NONE),	//답변 부서 코드
						new SelectSet(Globals.FIELD_EXT_STATION, Protocol.SelectSet.NONE),		//민원발생역
						new SelectSet(Globals.FIELD_CDVOCREPLEVEL, Protocol.SelectSet.NONE),	//답변만족도 등급
						new SelectSet(Globals.FIELD_REPCONT, Protocol.SelectSet.NONE),	//답변만족도 등급
						new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE),	//VOC문서 번호
						new SelectSet(Globals.FIELD_VOC_CATEGORY, Protocol.SelectSet.NONE),	//내부 VOC 민원(MINWON), 콜센터(CALL), SMS(SMS) 구분용
				};
				break;
			case SELECTSET_LIST_COMPARE: // VOC 검색 결과 - 비교분석
				selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE), // 수집문서ID
						new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE), // 제목
						new SelectSet(Globals.FIELD_TITLE, (byte) (Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 40), // HIGHLIGHT
						new SelectSet(Globals.FIELD_CONTENT, (byte) (Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 100),
						new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE),
						new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE), 
						new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE), 
				};
				break;
			default:
				break;
		}
		return selectSet;
	}
	
	/**
	 * WhereSet 설정.
	 * @return WhereSet[]
	 */
	private WhereSet[] createWhereSet(int con, TrendAnalysisVo vo, String keyword) throws Exception{
		
		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();
		

		
		switch (con) {
		case WHERESET_TRENDCHART:	
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(!vo.getVocChannel().isEmpty() && !vo.getVocChannel().equals(Globals.COM_SELECT_ALL)) {	//접수채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCCHANNEL, Protocol.WhereSet.OP_HASALL, vo.getVocChannel(), 0));
			}
			if (!vo.getVocRecType().isEmpty() && !vo.getVocRecType().equals(Globals.COM_SELECT_ALL)) {	//VOC종류
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCRECTYPE, Protocol.WhereSet.OP_HASALL, vo.getVocRecType(), 0));
			}
			if (!vo.getVocKind().isEmpty() && !vo.getVocKind().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 대
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, vo.getVocKind(), 0));
			}
			if (!vo.getVocPart().isEmpty() && !vo.getVocPart().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 중
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCPART, Protocol.WhereSet.OP_HASALL, vo.getVocPart(), 0));
			}
			if (!vo.getVocItem().isEmpty() && !vo.getVocItem().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 소
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCITEM, Protocol.WhereSet.OP_HASALL, vo.getVocItem(), 0));
			}
			
			if (!vo.getRepLevel().isEmpty() && !vo.getRepLevel().equals(Globals.COM_SELECT_ALL)) {	//만족도 등급
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCREPLEVEL, Protocol.WhereSet.OP_HASALL, vo.getRepLevel(), 0));
			}
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			if(!keyword.equals("")){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, keyword.trim(), 1000));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, keyword.trim(), 100));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword.trim(), 100));
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}			
		break;
		case WHERESET_SEARCHKEYWORD:
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(!vo.getVocChannel().isEmpty() && !vo.getVocChannel().equals(Globals.COM_SELECT_ALL)) {	//접수채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCCHANNEL, Protocol.WhereSet.OP_HASALL, vo.getVocChannel(), 0));
			}
			if (!vo.getVocRecType().isEmpty() && !vo.getVocRecType().equals(Globals.COM_SELECT_ALL)) {	//VOC종류
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCRECTYPE, Protocol.WhereSet.OP_HASALL, vo.getVocRecType(), 0));
			}
			if (!vo.getVocKind().isEmpty() && !vo.getVocKind().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 대
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, vo.getVocKind(), 0));
			}
			if (!vo.getVocPart().isEmpty() && !vo.getVocPart().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 중
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCPART, Protocol.WhereSet.OP_HASALL, vo.getVocPart(), 0));
			}
			if (!vo.getVocItem().isEmpty() && !vo.getVocItem().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 소
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCITEM, Protocol.WhereSet.OP_HASALL, vo.getVocItem(), 0));
			}
			
			if (!vo.getRepLevel().isEmpty() && !vo.getRepLevel().equals(Globals.COM_SELECT_ALL)) {	//만족도 등급
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCREPLEVEL, Protocol.WhereSet.OP_HASALL, vo.getRepLevel(), 0));
			}
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			if(vo.getKeyword() != null && !vo.getKeyword().equals("")){
				
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));				
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				for(int i=0; i<vo.getKeywordList().size(); i++){
					if(i > 0){
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, vo.getKeywordList().get(i), 1000));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, vo.getKeywordList().get(i), 100));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, vo.getKeywordList().get(i), 100));
					whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				}
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			if(vo.getExclusion() != null && !vo.getExclusion().equals("")){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				for(int i=0; i<vo.getExclusionList().size(); i++){
					if(i > 0){
						whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					}
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, vo.getExclusionList().get(i)));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, vo.getExclusionList().get(i)));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, vo.getExclusionList().get(i)));
					whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				}
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}
		break;
		case WHERESET_PERIOD :
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(!vo.getVocChannel().isEmpty() && !vo.getVocChannel().equals(Globals.COM_SELECT_ALL)) {	//접수채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCCHANNEL, Protocol.WhereSet.OP_HASALL, vo.getVocChannel(), 0));
			}
			if (!vo.getVocRecType().isEmpty() && !vo.getVocRecType().equals(Globals.COM_SELECT_ALL)) {	//VOC종류
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCRECTYPE, Protocol.WhereSet.OP_HASALL, vo.getVocRecType(), 0));
			}
			if (!vo.getVocKind().isEmpty() && !vo.getVocKind().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 대
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, vo.getVocKind(), 0));
			}
			if (!vo.getVocPart().isEmpty() && !vo.getVocPart().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 중
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCPART, Protocol.WhereSet.OP_HASALL, vo.getVocPart(), 0));
			}
			if (!vo.getVocItem().isEmpty() && !vo.getVocItem().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 소
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCITEM, Protocol.WhereSet.OP_HASALL, vo.getVocItem(), 0));
			}
			
			if (!vo.getRepLevel().isEmpty() && !vo.getRepLevel().equals(Globals.COM_SELECT_ALL)) {	//만족도 등급
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCREPLEVEL, Protocol.WhereSet.OP_HASALL, vo.getRepLevel(), 0));
			}
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));		
			whereSetList.add(new WhereSet(vo.getCondition(), Protocol.WhereSet.OP_HASALL, keyword, 0));	//종합랭킹분석 시 지정한 주기(일,월,분기,반기,연도) 별로 해당 주기 필드에 날짜값 지정.
			break;
		case WHERESET_METRODEPT:	
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(!vo.getVocChannel().isEmpty() && !vo.getVocChannel().equals(Globals.COM_SELECT_ALL)) {	//접수채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCCHANNEL, Protocol.WhereSet.OP_HASALL, vo.getVocChannel(), 0));
			}
			if (!vo.getVocRecType().isEmpty() && !vo.getVocRecType().equals(Globals.COM_SELECT_ALL)) {	//VOC종류
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCRECTYPE, Protocol.WhereSet.OP_HASALL, vo.getVocRecType(), 0));
			}
			if (!vo.getVocKind().isEmpty() && !vo.getVocKind().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 대
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, vo.getVocKind(), 0));
			}
			if (!vo.getVocPart().isEmpty() && !vo.getVocPart().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 중
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCPART, Protocol.WhereSet.OP_HASALL, vo.getVocPart(), 0));
			}
			if (!vo.getVocItem().isEmpty() && !vo.getVocItem().equals(Globals.COM_SELECT_ALL)) {	//접수종류 - 소
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCITEM, Protocol.WhereSet.OP_HASALL, vo.getVocItem(), 0));
			}
			
			if (!vo.getRepLevel().isEmpty() && !vo.getRepLevel().equals(Globals.COM_SELECT_ALL)) {	//만족도 등급
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCREPLEVEL, Protocol.WhereSet.OP_HASALL, vo.getRepLevel(), 0));
			}
			break;
		default:
		break;
		}
		
		whereSet = new WhereSet[whereSetList.size()];
		
		for(int i=0; i < whereSetList.size(); i++){
			whereSet[i] = (WhereSet) whereSetList.get(i);
		}
		
		return whereSet;
	}
	
	/**
	 * FilterSet 설정.
	 * @return FilterSet[]
	 * @throws ParseException 
	 */
	private FilterSet[] createFilterSet(TrendAnalysisVo vo) throws ParseException{
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if ("".equals(vo.getStartDate()) || "".equals(vo.getEndDate())) {
			vo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			vo.setStartDate(sdf.format(cal.getTime()));
		}

		String startDate = vo.getStartDate().replace("/", "") + "000000";
		String endDate = vo.getEndDate().replace("/", "") + "235959";

		FilterSet[] filterSet = null;
		ArrayList<FilterSet> filterlist = new ArrayList<FilterSet>();
		filterlist.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "REGDATE", new String[] { startDate, endDate }));	//날짜 필터링
		
		///// 공백테스트
		//filterlist.add(new FilterSet(Protocol.FilterSet.OP_NOT, "EXT_LINE", ""));
		
		if (filterlist.size() > 0) {
			filterSet = new FilterSet[filterlist.size()];
			for (int i = 0; i < filterlist.size(); i++) {
				filterSet[i] = filterlist.get(i);
			}
		}
		return filterSet;
	}
	
	/**
	 * GroupBySet 설정.
	 * @return GroupBySet[]
	 */
	private GroupBySet[] createGroupBySet(int flag, TrendAnalysisVo vo){

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupbysetList = new ArrayList<GroupBySet>();
		
		switch (flag) {
		case GROUPBYSET_TRENDCHART:	// 트랜드 차트 
			groupbysetList.add(new GroupBySet(vo.getCondition(), (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
			groupbysetList.add(new GroupBySet(Globals.GROUP_CDREPDEPT, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));
			break;
		case GROUPBYSET_VOC_CATEGORY:	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
			groupbysetList.add(new GroupBySet("VOC_CATEGORY", (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
			break;
		case GROUPBYSET_RANKING:	//종합랭킹 분석용
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
			break;
		case GROUPBYSET_METRODEPT:
			groupbysetList.add(new GroupBySet(Globals.GROUP_CDREPDEPT, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));
			break;
		default:
			break;
		}
		
		groupBys = new GroupBySet[groupbysetList.size()];
		
		for(int i=0; i < groupbysetList.size(); i++){
			groupBys[i] = (GroupBySet) groupbysetList.get(i);
		}
		
		return groupBys;
	}
	
	/**
	 * OrderBySet 설정.
	 * @return OrderBySet[]
	 */
	private OrderBySet[] createOrderBySet(){
		
		OrderBySet[] orderbySet = null;
		orderbySet = new OrderBySet[]{new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE)};
		return orderbySet;
	}

}
