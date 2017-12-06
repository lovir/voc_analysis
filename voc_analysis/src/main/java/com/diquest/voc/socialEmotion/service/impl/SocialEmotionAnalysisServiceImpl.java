package com.diquest.voc.socialEmotion.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.diquest.voc.emotion.service.EmotionAnalysisService;
import com.diquest.voc.emotion.vo.EmotionAnalysisVo;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.impl.CommonSelectBoxDAO;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.openapi.service.DqXmlBuilderJ;
import com.diquest.voc.socialEmotion.service.SocialEmotionAnalysisService;
import com.diquest.voc.socialEmotion.vo.SocialEmotionAnalysisVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("socialEmotionAnalysisService")
public class SocialEmotionAnalysisServiceImpl implements SocialEmotionAnalysisService {
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;

	private Gson gson = null;
	private static final boolean MARINER_LOGGABLE = true; // 검색엔진 로그 저장 여부
	private static final boolean MARINER_PRINT_QUERY = false; // 검색엔진 검색쿼리 출력 여부
	private static final boolean MARINER_DEBUG = true; // 검색엔진 디버그 사용 여부
	private static final boolean MARINER_PRITER = false; //개인정보보호 필터(PRITER) 사용 여부

	// SELECTSET 분기
	private static final int SELECTSET_COUNT = 100; // 통계치용 건수 조회용
	private static final int SELECTSET_DETAIL = 101; // 상세페이지 조회용
	private static final int SELECTSET_ALIKE = 102; // 유사문서 조회용
	private static final int SELECTSET_LIST = 103; // 검색결과 리스트

	// WHERESET 분기
	private static final int WHERESET_PERIOD = 200; //기간검색 - 종합랭킹 등에서 날짜별 건수 검색용
	private static final int WHERESET_DETAIL = 203; // 상세보기
	private static final int WHERESET_ALIKE = 204; // 유사문서
	private static final int WHERESET_REPDEPT = 205;	//처리주무부서 검색
	private static final int WHERESET_PNN = 206; // 긍정,부정,중립 선택 검색
	private static final int WHERESET_CLICKSTACK = 207; //감성 추세 차트 막대 클릭 시 검색
	
	// FILTERSET 분기

	// GROUPBYSET 분기
	private static final int GROUPBYSET_KEYWORD_NAME = 400; // 키워드 이름 순으로 정렬
	private static final int GROUPBYSET_RANKING = 401; // 종합랭킹, 상위키워드 10개 추출
	private static final int GROUPBYSET_PERIOD = 402;	//기간별 그룹카운트 조회
	private static final int GROUPBYSET_VOC_CATEGORY = 403;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_DEPT = 404;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_PNN = 405; // 긍부정 그룹핑
	private static final int GROUPBYSET_SENSE_KIND = 406; //감성 종류 그룹핑
	
	
	//조회조건, 기간에 해당하는 긍부정,중립 분포도 데이터 추출 
	public HashMap<String, Object> getEmotionTrendChart(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		LinkedHashMap<String, String> periodMap = getCategories(emotionAnalysisVo); // 종합리포트 챠트 x축(기간)
		ArrayList<String> periodList = new ArrayList<String>(periodMap.keySet()); // 기간 리스트
		Result[] resultlist = null;
		Result result = null;
		LinkedList<LinkedList<HashMap<String,Object>>> periodPnnMapList = new LinkedList<LinkedList<HashMap<String,Object>>>();	//각 x축 기간별 키워드 빈도수 저장
		//LinkedList<HashMap<String, Object>> periodPnnMapList = new LinkedList<HashMap<String, Object>>();
		
		int maxKeywordCount = 100;	//클라우드 차트 하나에 출력한 최대 키워드 수
		QueryParser queryParser = new QueryParser();
		LinkedList<Query> queryList = new LinkedList<Query>();
		//System.out.println("----- 감성 추세 차트 쿼리 -------");
		//x축 한 기간 동안의 키워드 랭킹 조회 쿼리
		for(String indexPeriod : periodList){
			Query query = new Query();
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(WHERESET_PERIOD, indexPeriod, emotionAnalysisVo));
			query.setGroupBy(createGroupBySet(GROUPBYSET_PNN, "ASC"));
			query.setResult(0, 0);
			query.setFrom(Globals.MARINER_COLLECTION2);
			query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | (byte) Protocol.ThesaurusOption.QUASI_SYNONYM);
			query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			query.setSearch(true);
			query.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시v
			query.setDebug(MARINER_DEBUG);
			query.setPrintQuery(MARINER_PRINT_QUERY);
			query.setLoggable(MARINER_LOGGABLE);
			queryList.add(query);
			String indexQr = queryParser.queryToString(query);
			//System.out.println(indexPeriod + " Query : " + indexQr);
		}
		//검색쿼리 리스트 조회
		QuerySet querySet = new QuerySet(queryList.size());
		for(Query indexQuery: queryList){
			querySet.addQuery(indexQuery);
		}
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
		if (resultlist != null) {
			//x축 기간 별 키워드 빈도수 저장
			for(int i=0;i < periodList.size(); i++){
				Result indexPeriodResult = resultlist[i];
				GroupResult[] indexPeriodGroupResult = indexPeriodResult.getGroupResults();
				LinkedList<HashMap<String, Object>> tempMapList = new LinkedList<HashMap<String, Object>>();
				int totalCount = 0;
				HashMap<String, Object>indexMap = new HashMap<String, Object>();
				for(int j=0; j < indexPeriodGroupResult[0].groupResultSize() && j < maxKeywordCount; j++){
					String tempKeyword = new String(indexPeriodGroupResult[0].getId(j));
					int count = indexPeriodGroupResult[0].getIntValue(j);
					if(tempKeyword != null && !"".equals(tempKeyword.trim())){
						indexMap.put(tempKeyword, count);
						totalCount += count;
					}
				}
				
				LinkedList<HashMap<String, Object>> tempList = new LinkedList<HashMap<String, Object>>();
				if(indexMap.get("긍정") != null) {
					HashMap<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("name", "긍정");
					tempMap.put("count", indexMap.get("긍정"));
					tempMap.put("percent", getPercent(totalCount, (int)indexMap.get("긍정")));
					tempList.add(tempMap);
				}
				else{
					HashMap<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("name", "긍정");
					tempMap.put("count", 0);
					tempMap.put("percent", 0);
					tempList.add(tempMap);
				}
				
				if(indexMap.get("중립") != null) {
					HashMap<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("name", "중립");
					tempMap.put("count", indexMap.get("중립"));
					tempMap.put("percent", getPercent(totalCount, (int)indexMap.get("중립")));
					tempList.add(tempMap);
				}
				else{
					HashMap<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("name", "중립");
					tempMap.put("count", 0);
					tempMap.put("percent", 0);
					tempList.add(tempMap);
				}
				
				if(indexMap.get("부정") != null) {
					HashMap<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("name", "부정");
					tempMap.put("count", indexMap.get("부정"));
					tempMap.put("percent", getPercent(totalCount, (int)indexMap.get("부정")));
					tempList.add(tempMap);
				}
				else{
					HashMap<String,Object> tempMap = new HashMap<String,Object>();
					tempMap.put("name", "부정");
					tempMap.put("count", 0);
					tempMap.put("percent", 0);
					tempList.add(tempMap);
				}
				periodPnnMapList.add(tempList);
			}
		}
		returnMap.put("periodMap", periodMap);
		returnMap.put("periodPnnMapList", periodPnnMapList);
		
		return returnMap;
	}
	
	/**
	 * 부정 감성 분포 조회.
	 * @return 
	 * @exception Exception
	 */
	public HashMap<String, Object> getEmotionDistributionChart(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		LinkedHashMap<String, String> periodMap = getCategories(emotionAnalysisVo); // 종합리포트 챠트 x축(기간)
		ArrayList<String> periodList = new ArrayList<String>(periodMap.keySet()); // 기간 리스트
		Result[] resultlist = null;
		Result result = null;
		LinkedList<Integer> totalPeriodSenseMapList = new LinkedList<Integer>();	//전체 기간 동안의 감성별 빈도수 저장 리스트
		LinkedList<LinkedList<Integer>> periodSenseMapList = new LinkedList<LinkedList<Integer>>();	//각 x축 기간별 감성별 빈도수 저장
		String[] senseKindArr = new String[]{"공포","분노","슬픔","통증","혐오"};	//부정 감성 셋팅
		emotionAnalysisVo.setSenseKindArr(senseKindArr);
		
		String groupBySetOption = "DESC";
		QueryParser queryParser = new QueryParser();
		LinkedList<Query> queryList = new LinkedList<Query>();
		//검색기간 전체 동안의 키워드 랭킹 조회 쿼리
		Query totalPeriodQuery = new Query();
		totalPeriodQuery.setSelect(createSelectSet(SELECTSET_COUNT));
		totalPeriodQuery.setWhere(createWhereSet(WHERESET_PNN, "", emotionAnalysisVo));
		totalPeriodQuery.setFilter(createFilterSet(emotionAnalysisVo));
		totalPeriodQuery.setGroupBy(createGroupBySet(GROUPBYSET_SENSE_KIND, groupBySetOption));
		totalPeriodQuery.setResult(0, 0);
		totalPeriodQuery.setFrom(Globals.MARINER_COLLECTION2);
		totalPeriodQuery.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | (byte) Protocol.ThesaurusOption.QUASI_SYNONYM);
		totalPeriodQuery.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		totalPeriodQuery.setSearch(true);
		totalPeriodQuery.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시v
		totalPeriodQuery.setDebug(MARINER_DEBUG);
		totalPeriodQuery.setPrintQuery(MARINER_PRINT_QUERY);
		totalPeriodQuery.setLoggable(MARINER_LOGGABLE);
		queryList.add(totalPeriodQuery);
		//x축 한 기간 동안의 키워드 랭킹 조회 쿼리
		for(String indexPeriod : periodList){
			Query query = new Query();
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(WHERESET_PERIOD, indexPeriod, emotionAnalysisVo));
			query.setGroupBy(createGroupBySet(GROUPBYSET_SENSE_KIND, groupBySetOption));
			query.setResult(0, 0);
			query.setFrom(Globals.MARINER_COLLECTION2);
			query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | (byte) Protocol.ThesaurusOption.QUASI_SYNONYM);
			query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			query.setSearch(true);
			query.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시v
			query.setDebug(MARINER_DEBUG);
			query.setPrintQuery(MARINER_PRINT_QUERY);
			query.setLoggable(MARINER_LOGGABLE);
			queryList.add(query);
		}
		//검색쿼리 리스트 조회
		QuerySet querySet = new QuerySet(queryList.size());
		for(Query indexQuery: queryList){
			querySet.addQuery(indexQuery);
		}
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
		if (resultlist != null) {
			//전체 기간 동안의 키워드 빈도수 저장
			Result totalPeriodResult = resultlist[0];
			GroupResult[] totalPeriodGroupResult = totalPeriodResult.getGroupResults();
			HashMap<String, Object> totalPeriodSenseMap = new HashMap<String, Object>();
			for(String sense : senseKindArr){	//각 부정 감성별 카운트 초기화
				totalPeriodSenseMap.put(sense, 0);
			}
			for(int j=0; j < totalPeriodGroupResult[0].groupResultSize() ; j++){
				String tempKeyword = new String(totalPeriodGroupResult[0].getId(j));
				int count = totalPeriodGroupResult[0].getIntValue(j);
				if(tempKeyword != null && !"".equals(tempKeyword.trim())){
					totalPeriodSenseMap.put(tempKeyword, count);
				}
			}
			for(String sense : senseKindArr){	//감성 순서에 맞춰 빈도수 저장
				totalPeriodSenseMapList.add((Integer) totalPeriodSenseMap.get(sense));
			}
			
			//x축 기간 별 키워드 빈도수 저장
			for(int i=0;i < periodList.size(); i++){
				Result indexPeriodResult = resultlist[i + 1];
				GroupResult[] indexPeriodGroupResult = indexPeriodResult.getGroupResults();
				LinkedList<Integer> indexPeriodMapList = new LinkedList<Integer>();
				HashMap<String, Object> tempMap = new HashMap<String, Object>();
				for(String sense : senseKindArr){	//각 부정 감성별 카운트 초기화
					tempMap.put(sense, 0);
				}
				for(int j=0; j < indexPeriodGroupResult[0].groupResultSize(); j++){
					String tempKeyword = new String(indexPeriodGroupResult[0].getId(j));
					int count = indexPeriodGroupResult[0].getIntValue(j);
					if(tempKeyword != null && !"".equals(tempKeyword.trim())){
						tempMap.put(tempKeyword, count);
					}
				}
				for(String sense : senseKindArr){	//감성 순서에 맞춰 빈도수 저장
					indexPeriodMapList.add((Integer) tempMap.get(sense));
				}
				periodSenseMapList.add(indexPeriodMapList);
			}
		}
		LinkedList<String> chartTitleList = new LinkedList<String>();
		chartTitleList.add("전체(" + periodMap.get(periodList.get(0)) + "~" + periodMap.get(periodList.get(periodList.size() - 1))+ ")");
		for(String periodStr: periodList){
			chartTitleList.add(periodMap.get(periodStr));
		}
		LinkedList<LinkedList<Integer>> finalSenseMapList = new LinkedList<LinkedList<Integer>>();	//최종 감성별 빈도수 맵 리스트( 전체기간, x축 각 기간별 키워드+빈도수)
		finalSenseMapList.add(totalPeriodSenseMapList);
		for(LinkedList<Integer> tempList : periodSenseMapList){
			finalSenseMapList.add(tempList);
		}
		returnMap.put("chartTitleList", chartTitleList);
		returnMap.put("senseKindList", Arrays.asList(senseKindArr));
		returnMap.put("finalSenseMapList", finalSenseMapList);
		
		return returnMap;
	}
	private double getPercent(int totalCount, int select){
		double percentFirst = (double)select / (double)totalCount;
		double percentRound = Math.round(percentFirst * 10000d) / 100d;
		return percentRound;
	}
	//리포트 차트 x축(기간) 출력
	private LinkedHashMap<String, String> getCategories(SocialEmotionAnalysisVo emotionAnalysisVo) {

		LinkedHashMap<String, String> categoriesMap = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if ("".equals(emotionAnalysisVo.getStartDate()) || "".equals(emotionAnalysisVo.getEndDate())) {
			emotionAnalysisVo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			emotionAnalysisVo.setStartDate(sdf.format(cal.getTime()));
		}

		String startDate = emotionAnalysisVo.getStartDate().replace("/", "") + "000000";
		String endDate = emotionAnalysisVo.getEndDate().replace("/", "") + "235959";

		String condition = emotionAnalysisVo.getCondition();
		if (condition.equals("DAY")) {

			categoriesMap = DateUtil.getCategories(startDate, endDate, "MM월dd일", Globals.TREND_PERIOD_DAY);
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
	
	/**
	 * VOC 검색결과
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> groupResult = null;
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;
		String share = "0"; // 점유율
		String keywordTemp = null;

		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();

		int startNo = (emotionAnalysisVo.getCurrentPage() - 1) * emotionAnalysisVo.getPageSize();
		int endNo = startNo + emotionAnalysisVo.getPageSize() - 1;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(SELECTSET_LIST));
		
		query.setWhere(createWhereSet(WHERESET_PNN, emotionAnalysisVo.getClickPnn(), emotionAnalysisVo));
		query.setFilter(createFilterSet(emotionAnalysisVo));
		//query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, "VOC_CATEGORY"));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(startNo, endNo);
		query.setSearch(true);
		query.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시
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

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		// resultlist[0]이 해당 기간에 속한 키워드에 관한 문서 검색결과
		// resultlist[1]이 해당 기간에 추출된 keyword 전체 결과 -

		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			result = resultlist[0];
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
				String title = new String(result.getResult(i, 1));
				if(title == null || "".equals(title)) title = "제목없음";
				resultMap.put(Globals.FIELD_TITLE, new String(title));
				resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)));
				resultMap.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(i, 3)));
				String date = "";
				if (result.getResult(i, 4) != null) {
					date = new String(result.getResult(i, 4));
					if (date.length() > 7) {
						date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
					}
				}
				resultMap.put(Globals.FIELD_REGDATE, date);
				resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 5)));
				listResult.add(resultMap);
			}
			searchResult.put("listResult", listResult); // 리스트
			searchResult.put("totalSize", nf.format(result.getTotalSize())); // 전체 건수
			int endPage = result.getTotalSize() / emotionAnalysisVo.getPageSize();
			if (endPage % emotionAnalysisVo.getPageSize() > 0) {
				endPage++;
			}
			if (endPage == 0) {
				endPage = 1;
			}
			searchResult.put("endPage", endPage); // 끝 페이지
			searchResult.put("currentPage", emotionAnalysisVo.getCurrentPage()); // 현재 페이지

			/** pageing setting */
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(emotionAnalysisVo.getCurrentPage());
			paginationInfo.setRecordCountPerPage(emotionAnalysisVo.getPageSize());
			paginationInfo.setPageSize(10);
			paginationInfo.setTotalRecordCount(result.getTotalSize());

			searchResult.put("paginationInfo", paginationInfo);

		}
		return searchResult;
	}

	/**
	 * 막대차트 클릭시 (선택기간, 선택 감성) VOC 검색결과
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getClickStackSearchResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> groupResult = null;
		ArrayList<HashMap<String, Object>> listResult = null;
		String printDateStr = emotionAnalysisVo.getClickCategory();
		
		Result[] resultlist = null;
		Result result = null;
		String share = "0"; // 점유율
		String keywordTemp = null;

		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();

		int startNo = (emotionAnalysisVo.getCurrentPage() - 1) * emotionAnalysisVo.getPageSize();
		int endNo = startNo + emotionAnalysisVo.getPageSize() - 1;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(SELECTSET_LIST));
		
		query.setWhere(createWhereSet(WHERESET_CLICKSTACK, "", emotionAnalysisVo));
		//query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, "VOC_CATEGORY"));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(startNo, endNo);
		query.setSearch(true);
		query.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
		//System.out.println("##### 막대 클릭 리스트 검색 query : " + queryParser.queryToString(query));

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		// resultlist[0]이 해당 기간에 속한 키워드에 관한 문서 검색결과
		// resultlist[1]이 해당 기간에 추출된 keyword 전체 결과 -

		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			result = resultlist[0];
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
				String title = new String(result.getResult(i, 1));
				if(title == null || "".equals(title)) title = "제목없음";
				resultMap.put(Globals.FIELD_TITLE, new String(title));
				resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)));
				resultMap.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(i, 3)));
				String date = "";
				if (result.getResult(i, 4) != null) {
					date = new String(result.getResult(i, 4));
					if (date.length() > 7) {
						date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
					}
				}
				resultMap.put(Globals.FIELD_REGDATE, date);
				resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 5)));
				listResult.add(resultMap);
			}
			searchResult.put("listResult", listResult); // 리스트
			searchResult.put("totalSize", nf.format(result.getTotalSize())); // 전체 건수
			searchResult.put("clickStack", "Y");	//막대 차트 클릭 여부 구분용
			searchResult.put("printDateStr", printDateStr);	//막대 차트에서 선택한 기간
			searchResult.put("pnn", emotionAnalysisVo.getClickPnn());	//막대차트에서 선택한 긍부정
			
			int endPage = result.getTotalSize() / emotionAnalysisVo.getPageSize();
			if (endPage % emotionAnalysisVo.getPageSize() > 0) {
				endPage++;
			}
			if (endPage == 0) {
				endPage = 1;
			}
			searchResult.put("endPage", endPage); // 끝 페이지
			searchResult.put("currentPage", emotionAnalysisVo.getCurrentPage()); // 현재 페이지

			/** pageing setting */
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(emotionAnalysisVo.getCurrentPage());
			paginationInfo.setRecordCountPerPage(emotionAnalysisVo.getPageSize());
			paginationInfo.setPageSize(10);
			paginationInfo.setTotalRecordCount(result.getTotalSize());

			searchResult.put("paginationInfo", paginationInfo);

		}
		return searchResult;
	}
	/**
	 * VOC 검색결과 엑셀 저장
	 * 
	 * @param EmotionVo
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_LIST));
		query.setWhere(createWhereSet(WHERESET_PNN, emotionAnalysisVo.getClickPnn(), emotionAnalysisVo));
		query.setFilter(createFilterSet(emotionAnalysisVo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 999);
		query.setSearch(true);
		query.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
						resultMap.put(Globals.FIELD_TITLE, new String(result.getResult(i, 1)));
						resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)));
						resultMap.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(i, 3)));
						String date = "";
						if (result.getResult(i, 4) != null) {
							date = new String(result.getResult(i, 4));
							if (date.length() > 7) {
								date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
							}
						}
						resultMap.put(Globals.FIELD_REGDATE, date);
						resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 5)));
						
						listResult.add(resultMap);
					}
					searchResult.put("listResult", listResult); // 리스트
				}

			}
		}

		return searchResult;
	}

	/**
	 * VOC 검색결과 엑셀 저장 - 막대 차트 클릭 한 후 리스트에서 엑셀저장 
	 * 
	 * @param EmotionVo
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getClickStackExcelResult(SocialEmotionAnalysisVo emotionAnalysisVo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_LIST));
		query.setWhere(createWhereSet(WHERESET_CLICKSTACK, "", emotionAnalysisVo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 999);
		query.setSearch(true);
		query.setUserName(emotionAnalysisVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
		//System.out.println("##### 막대 클릭 - 엑셀 결과 리스트 검색 query : " + queryParser.queryToString(query));

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
						resultMap.put(Globals.FIELD_TITLE, new String(result.getResult(i, 1)));
						resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)));
						resultMap.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(i, 3)));
						String date = "";
						if (result.getResult(i, 4) != null) {
							date = new String(result.getResult(i, 4));
							if (date.length() > 7) {
								date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
							}
						}
						resultMap.put(Globals.FIELD_REGDATE, date);
						resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 5)));
						
						listResult.add(resultMap);
					}
					searchResult.put("listResult", listResult); // 리스트
				}

			}
		}

		return searchResult;
	}

	/**
	 * SelectSet 설정.
	 * 
	 * @return SelectSet[]
	 */
	private SelectSet[] createSelectSet(int flag) {

		SelectSet[] selectSet = null;
		
		switch (flag) {
		case SELECTSET_COUNT:
			selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE) };
			break;
		case SELECTSET_LIST:	// VOC 검색 결과
			selectSet = new SelectSet[] { 
					new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE),	//수집문서ID
					new SelectSet(Globals.FIELD_TITLE, (byte)(Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 120), // HIGHLIGHT
					new SelectSet(Globals.FIELD_CONTENT, (byte) (Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 400),
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
	 * 
	 * @return WhereSet[]
	 */
	private WhereSet[] createWhereSet(int flag, SocialEmotionAnalysisVo emotionAnalysisVo) {
		return createWhereSet(flag, "", emotionAnalysisVo);
	}

	private WhereSet[] createWhereSet(int flag, String keyword, SocialEmotionAnalysisVo emotionAnalysisVo) {

		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();

		switch (flag) {
		case WHERESET_PERIOD : //기간검색 - 종합랭킹 등에서 날짜별 건수 검색용
			whereSetList.add(new WhereSet(emotionAnalysisVo.getCondition(), Protocol.WhereSet.OP_HASALL, keyword, 0));	//종합랭킹분석 시 지정한 주기(일,월,분기,반기,연도) 별로 해당 주기 필드에 날짜값 지정.
			if(!emotionAnalysisVo.getSocialChannel().isEmpty() && !emotionAnalysisVo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getSocialChannel(), 0));
			}		
			if (!emotionAnalysisVo.getRepLevel().isEmpty() && !"".equals(emotionAnalysisVo.getClickPnn()) && !emotionAnalysisVo.getClickPnn().equals(Globals.COM_SELECT_ALL)) {	//긍정,부정,중립 선택 여부
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_PNN, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getClickPnn(), 0));
			}
			break;
		case WHERESET_DETAIL :	// 상세보기
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, keyword, 0));	//keyword로 수집문서ID 검색
			break;
		case WHERESET_ALIKE :	// 유사문서
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, emotionAnalysisVo.getTitle(), 1000));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, emotionAnalysisVo.getContent(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getDq_docid(), 0)); // 같은id제거하기
			break;
		case WHERESET_REPDEPT :	//처리주무부서 조회용 검색식
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(keyword != null && !"".equals(keyword)){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_PNN, Protocol.WhereSet.OP_HASALL, keyword, 0));	//긍정,부정,중립 선택 검색
			}
			if(!emotionAnalysisVo.getSocialChannel().isEmpty() && !emotionAnalysisVo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getSocialChannel(), 0));
			}
			break;
		case WHERESET_PNN :	//긍정, 부정, 중립 선택 검색
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(keyword != null && !"".equals(keyword) && !Globals.COM_SELECT_ALL.equals(keyword)){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_PNN, Protocol.WhereSet.OP_HASALL, keyword, 1000));	//긍정,부정,중립 선택 검색
			}
			if(!emotionAnalysisVo.getSocialChannel().isEmpty() && !emotionAnalysisVo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getSocialChannel(), 0));
			}
			break;
		case WHERESET_CLICKSTACK :	//감성 종류에 따른 검색 - 감성 추세 차트 내 막대 클릭 시
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(!emotionAnalysisVo.getClickPnn().isEmpty() && !"".equals(emotionAnalysisVo.getClickPnn()) && !Globals.COM_SELECT_ALL.equals(emotionAnalysisVo.getClickPnn())){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_PNN, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getClickPnn(), 0));	//긍정,부정,중립 선택 검색
			}
			if(!emotionAnalysisVo.getClickCondition().isEmpty() && !"".equals(emotionAnalysisVo.getClickCondition())
					&& !emotionAnalysisVo.getClickDateStr().isEmpty() && !"".equals(emotionAnalysisVo.getClickDateStr())){	//클릭한 막대 그래프의 검색기간 조건
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(emotionAnalysisVo.getClickCondition(), Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getClickDateStr(), 0));
			}
			if(!emotionAnalysisVo.getSocialChannel().isEmpty() && !emotionAnalysisVo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getSocialChannel(), 0));
			}
			if (emotionAnalysisVo.getSenseKindArr() != null) {	//감성 종류에 따른 검색
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_SENSE_KIND, Protocol.WhereSet.OP_HASALL, emotionAnalysisVo.getSenseKindArr()));
			}
			break;
		default:
			break;
		}

		whereSet = new WhereSet[whereSetList.size()];
		for (int j = 0; j < whereSetList.size(); j++) {
			whereSet[j] = whereSetList.get(j);
		}
		return whereSet;
	}

	/**
	 * FilterSet 설정.
	 * 
	 * @return FilterSet[]
	 */
	private FilterSet[] createFilterSet(SocialEmotionAnalysisVo emotionAnalysisVo) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if ("".equals(emotionAnalysisVo.getStartDate()) || "".equals(emotionAnalysisVo.getEndDate())) {
			emotionAnalysisVo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			emotionAnalysisVo.setStartDate(sdf.format(cal.getTime()));
		}

		String startDate = emotionAnalysisVo.getStartDate().replace("/", "") + "000000";
		String endDate = emotionAnalysisVo.getEndDate().replace("/", "") + "235959";

		FilterSet[] filterSet = null;
		ArrayList<FilterSet> filterlist = new ArrayList<FilterSet>();
		filterlist.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "REGDATE", new String[] { startDate, endDate }));
		
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
	 * 
	 * @return GroupBySet[]
	 */
	private GroupBySet[] createGroupBySet(int flag, String option) { //flag=유형, option=그룹바이셋 옵션

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupbysetList = new ArrayList<GroupBySet>();
		//현재는 차이가 없음. 작업 완료 후에도 그룹바이셋 하나만 쓴다면 수정 필요.
		switch (flag) {
		case GROUPBYSET_PERIOD:	//기간별 그룹카운트 조회
			groupbysetList.add(new GroupBySet(option, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", ""));
			break;
		case GROUPBYSET_VOC_CATEGORY:	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
			groupbysetList.add(new GroupBySet(option, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
			break;
		case GROUPBYSET_PNN:	//긍부정, 중립 구분필드
			groupbysetList.add(new GroupBySet(Globals.GROUP_PNN, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", ""));
			break;
		case GROUPBYSET_SENSE_KIND:	//긍부정, 중립 구분필드
			groupbysetList.add(new GroupBySet(Globals.GROUP_SENSE_KIND, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", ""));
			break;
		default:
			break;
		}
		if(groupbysetList.size() > 0){
			groupBys = new GroupBySet[groupbysetList.size()];
			for(int i=0; i < groupbysetList.size(); i++){
				groupBys[i] = groupbysetList.get(i);
			}
		}
		return groupBys;
	}

	private OrderBySet[] createOrderBySet() {
		OrderBySet[] orderbySet = null;
		//orderbySet = new OrderBySet[] { new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE) };
		orderbySet = new OrderBySet[] { new OrderBySet(true, "REGDATE", Protocol.OrderBySet.OP_PREWEIGHT) };
		return orderbySet;
	}

}