package com.diquest.voc.socialKeywordRanking.service.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
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
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.service.impl.KeywordRankingDAO;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.impl.CommonSelectBoxDAO;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.openapi.service.DqXmlBuilderJ;
import com.diquest.voc.socialKeywordRanking.service.SocialKeywordRankingService;
import com.diquest.voc.socialKeywordRanking.vo.SocialKeywordRankingVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("socialKeywordRankingService")
public class SocialKeywordRankingServiceImpl implements SocialKeywordRankingService {
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;
	
	/** keywordRankingDAO */
	@Resource(name = "keywordRankingDAO")
	private KeywordRankingDAO keywordRankingDAO;
	
	/** socialKeywordRankingDAO */
	@Resource(name = "socialKeywordRankingDAO")
	private SocialKeywordRankingDAO socialKeywordRankingDAO;

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
	private static final int WHERESET_KEYWORD_REPORT = 201; // 상위키워드 or 종합리포트 차트 생성조건 or 하단 voc검색결과 전체 건수
	private static final int WHERESET_KEYWORD = 202; // 키워드에 대한 하단 VOC 검색결과
	private static final int WHERESET_DETAIL = 203; // 상세보기
	private static final int WHERESET_ALIKE = 204; // 유사문서
	private static final int WHERESET_REPDEPT = 205;	//처리주무부서 검색
	// 전체 키워드 추출
	// 감성검색??? 주석처리되어있음
	// 감성분석결과

	// FILTERSET 분기
	private static final int FILTERSET_LIST = 300; // VOC 리스트 검색
	private static final int FILTERSET_RANKING = 301; // 종합랭킹
	private static final int FILTERSET_SENSE = 302; // 감성분석???

	// GROUPBYSET 분기
	private static final int GROUPBYSET_KEYWORD_NAME = 400; // 키워드 이름 순으로 정렬
	private static final int GROUPBYSET_RANKING = 401; // 종합랭킹, 상위키워드 10개 추출
	private static final int GROUPBYSET_PERIOD = 402;	//기간별 그룹카운트 조회
	private static final int GROUPBYSET_VOC_CATEGORY = 403;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_DEPT = 404;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	
	// 조회조건과 기간선택 으로 리포트 챠트에 필요한 json 데이타 리스트 생성 
	public HashMap<String, Object> getSynthesisReport(SocialKeywordRankingVo vo) throws Exception {

		LinkedHashMap<String, String> periodMap = getCategories(vo); // 종합리포트 챠트 x축(기간)
		String[] keywordArr = vo.getKeywordArr(); // 상위 키워드10개 -- 나중에 로직 바꾸기
		String[] interestKeywordArr = vo.getKeywordArr(); //관심 키워드 10개
		ArrayList<HashMap<String, Object>> keywordPeriodCountList = null; // json으로 변환할 리스트
		ArrayList<HashMap<String, String>> metroDeptList = new ArrayList<HashMap<String, String>>();	// 처리주무부서 리스트, 셀렉트 박스 생성용
		
		if (keywordArr != null) {

			Result[] resultlist = null;
			keywordPeriodCountList = new ArrayList<HashMap<String, Object>>();
			//QuerySet querySet = new QuerySet(keywordArr.length);
			QuerySet querySet = new QuerySet(keywordArr.length);	//처리주무부서 그룹핑용 쿼리 추가
			for (int i = 0; i < keywordArr.length; i++) { // 상위 키워드 갯수만큼 쿼리 생성

				Query query = new Query();
				query.setSelect(createSelectSet(SELECTSET_COUNT));
				query.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, keywordArr[i], vo)); // case를 위한 번호, 키워드, VO
				query.setFilter(createFilterSet(vo));
				query.setGroupBy(createGroupBySet(GROUPBYSET_PERIOD, vo.getCondition()));
				query.setFrom(Globals.MARINER_COLLECTION2);
				query.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
				query.setSearch(true);
				query.setUserName(vo.getLogin_Id()); //로그에 남길 키워드 명시
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
				//System.out.println("##### 라인 차트 쿼리 키워드\"" + keywordArr[i] + "\" : " + queryParser.queryToString(query));
			}
			
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
				for (int i = 0; i < keywordArr.length; i++) {

					if (resultlist[i] != null && resultlist[i].getGroupResultSize() != 0) {
						GroupResult[] groupResultlist = resultlist[i].getGroupResults();

						LinkedHashMap<String, Integer> lhm = new LinkedHashMap<String, Integer>(); // x축 주기 순서대로 0으로 초기화
						for (Object key : periodMap.keySet()) {
							lhm.put((String) key, 0);
						}

						for (int k = 0; k < groupResultlist[0].groupResultSize(); k++) {
							lhm.put(new String(groupResultlist[0].getId(k)).trim(), groupResultlist[0].getIntValue(k));
						}

						HashMap<String, Object> periodCount = new HashMap<String, Object>(); // 챠트에 들어갈 키워드와 카운트 맵
						periodCount.put("name", keywordArr[i]);
						periodCount.put("data", new ArrayList<Integer>(lhm.values()));
						keywordPeriodCountList.add(periodCount);
					}
				}
				
			}
		}

		HashMap<String, Object> synthesisReportMap = new HashMap<String, Object>();
		synthesisReportMap.put("periodList", periodMap.values());
		//synthesisReportMap.put("metroDeptList", metroDeptList);
		if (keywordPeriodCountList != null) {
			synthesisReportMap.put("keywordPeriodCountList", keywordPeriodCountList);
		}
		return synthesisReportMap;
	}
	
	//리포트 차트 x축(기간) 출력
	private LinkedHashMap<String, String> getCategories(SocialKeywordRankingVo keywordRankingVo) {

		LinkedHashMap<String, String> categoriesMap = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

		if ("".equals(keywordRankingVo.getStartDate()) || "".equals(keywordRankingVo.getEndDate())) {
			keywordRankingVo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			keywordRankingVo.setStartDate(sdf.format(cal.getTime()));
		}

		String startDate = keywordRankingVo.getStartDate().replace("/", "") + "000000";
		String endDate = keywordRankingVo.getEndDate().replace("/", "") + "235959";

		String condition = keywordRankingVo.getCondition();
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

	// 종합랭킹 분석 호출 시 상위 10개 키워드 추출
	public String[] getsynthesisTotalKeywordArr(SocialKeywordRankingVo vo) throws Exception {
		String[] keywordArr = null; // 상위 10개 까지 담음
		Result[] resultlist = null;
		Result result = null;
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, "", vo));
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, "DESC 0 9"));
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(vo.getLogin_Id()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		query.setPrintQuery(MARINER_PRINT_QUERY);
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);

		QueryParser queryParser = new QueryParser();
		//System.out.println("##### 종합랭킹 분석을 위한 탑10키워드 뽑는 query: " + queryParser.queryToString(query));

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
			if (result.getTotalSize() > 0) {
				ArrayList<String> keywordList = new ArrayList<>();
				if (groupResultlist[0].groupResultSize() > 0) {
					for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
						String tempGroupResult = new String(groupResultlist[0].getId(i)).trim();
						if(tempGroupResult != null && !"".equals(tempGroupResult)){
							keywordList.add(tempGroupResult);
						}
					}
				}
				keywordArr = new String[keywordList.size()];
				for(int i=0;i < keywordList.size(); i++){
					keywordArr[i] = keywordList.get(i);
				}
				
			} else {
				keywordArr = new String[0];
			}
		}
		return keywordArr;
	}

	//사용자의 관심 키워드 10개를 가져온다
	public List<String> getInterestKeyword(HashMap<String, String> paramMap) throws Exception {
		List<String> keywordList = keywordRankingDAO.selectInterest(paramMap);
		return keywordList;
	}

	// 종합 랭킹 분석 차트 구성
	public LinkedHashMap<String, Object> getSynthesisRanking(SocialKeywordRankingVo keywordRankingVo) throws Exception {
		LinkedHashMap<String, Object> totalResultMap = new LinkedHashMap<String, Object>(); // 최종 리턴 맵
		LinkedHashMap<String, Object> totalPeriodKeyword = new LinkedHashMap<String, Object>(); // 주기별 랭킹의 키워드순위 정보를 담는 맵

		String[] iKeywordArr = keywordRankingVo.getInterestKeywordArr(); // 관심키워드

		LinkedHashMap<String, String> periodMap = getCategories(keywordRankingVo); // 종합리포트 챠트 x축(기간)
		
		ArrayList<String> periodList = new ArrayList<String>(periodMap.keySet()); // 기간 리스트
		ArrayList<HashMap<String, Object>> periodRankingList = null; // 기간별 키워드랭킹정보

		HashMap<String, Integer> pKeywordRanking = null;
		HashMap<String, Integer> nKeywordRanking = new HashMap<String, Integer>();

		int index = keywordRankingVo.getRankingIndex(); // 랭킹표 생성시 주기가 들어 있는 맵의 시작인덱스
		int stateNum = periodList.size();

		// 순위표 맨앞의 이전 순위구하기
		String prevPeriod = "";
		try {
			prevPeriod = periodList.get(index - 1);
		} catch (Exception e) { //// periodMap의 맨처음 기간의 이전 기간구하기

			String startDate = keywordRankingVo.getStartDate().replace("/", "");
			String condition = keywordRankingVo.getCondition();

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
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_PERIOD, prevPeriod, keywordRankingVo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, "DESC"));
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(WHERESET_PERIOD, period, keywordRankingVo));
			query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, "DESC"));
			query.setFrom(Globals.MARINER_COLLECTION2);
			query.setResult(0, 0);
			query.setSearch(true);
			query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
							// - 종합랭킹 10위 까지만 배열에 담기
							topRankingKeyWord.add(tempKeyword);
							count++;
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
							// - 관심 키워드면 하이라이팅 처리
							if (iKeywordArr != null) {
								for (String ikeyword : iKeywordArr) {
									if (ikeyword.equals(keyword)) {
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
			periodTag.add(makePeriodTag(period, keywordRankingVo.getCondition()));
		}
		totalResultMap.put("period", periodTag); // 기간선택 버튼

		double btnCount = Math.floor(938 / periodMap.size());	//????? 버튼사이즈???
		totalResultMap.put("btnSize", btnCount); // 버튼 크기 사이즈 임시

		return totalResultMap;
	}
	
	public String socialSpiderStatus(SocialKeywordRankingVo vo) throws Exception{
		
		HashMap<String, Object> socialList = new HashMap<String, Object>();
		List<CommonSelectBoxVo> newsAll = socialKeywordRankingDAO.selectNewsAll();
		List<CommonSelectBoxVo> newsToday = socialKeywordRankingDAO.selectNewsToday();
		List<CommonSelectBoxVo> siteAll = socialKeywordRankingDAO.selectSiteAll();
		List<CommonSelectBoxVo> siteToday = socialKeywordRankingDAO.selectSiteToday();
		List<CommonSelectBoxVo> facebookAll = socialKeywordRankingDAO.selectFacebookAll();
		List<CommonSelectBoxVo> facebookToday = socialKeywordRankingDAO.selectFacebookToday();
		List<CommonSelectBoxVo> twitterAll = socialKeywordRankingDAO.selectTwitterAll();
		List<CommonSelectBoxVo> twitterToday = socialKeywordRankingDAO.selectTwitterToday();
		List<CommonSelectBoxVo> communityAll = socialKeywordRankingDAO.selectCommunityAll();
		List<CommonSelectBoxVo> communityToday = socialKeywordRankingDAO.selectCommunityToday();
		
		/*System.out.println("---------------------------------");
		for(int i=0; i<newsAll.size(); i++){
			System.out.println(newsAll.get(i).getCode());
			System.out.println(newsAll.get(i).getName());
		}
		System.out.println("---------------------------------");*/
		ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> siteList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> facebookList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> twitterList = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> communityList = new ArrayList<HashMap<String, String>>();
		
		newsList = makeList(newsAll, newsToday);
		siteList = makeList(siteAll, siteToday);
		facebookList = makeList(facebookAll, facebookToday);
		twitterList = makeList(twitterAll, twitterToday);
		communityList = makeList(communityAll, communityToday);
		
		socialList.put("newsList", newsList);
		socialList.put("siteList", siteList);
		socialList.put("facebookList", facebookList);
		socialList.put("twitterList", twitterList);
		socialList.put("communityList", communityList);
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.toJson(socialList);
	}
	// 수집현황 리스트 파싱
	public ArrayList<HashMap<String, String>> makeList(List<CommonSelectBoxVo> all, List<CommonSelectBoxVo> today){
		
		ArrayList<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>();
		
		if(all != null && all.size()>0){	// 전체 수집 항목이 있는경우		
			// 전체 수집 갯수를 구함
			for(int i=0; i<all.size(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("NAME", all.get(i).getName());	// 항목이름
				map.put("All", all.get(i).getCode());	// 항목갯수
				// 이에 해당하는 현재 갯수를 구하여 맵에 넣는다
				if(today != null && today.size()>0){	
					boolean flag = true;					// 전체 수집항목에 해당하는 필드가 오늘 수집항목에 있는지 확인
					for(int j=0; j<today.size(); j++){
						if(all.get(i).getName().equals(today.get(j).getName())){	// 오늘 수집 항목에 있다면 해당 갯수를 넣고
							map.put("TODAY", today.get(j).getCode());
							flag = false;
						}							
					}
					if(flag == true){		// 오늘 수집 항목에 없다면 0을 넣음
						map.put("TODAY", "0");
					}
				}else{
					map.put("TODAY", "0");
				}
				returnList.add(map);
			}
		}
		return returnList;
	}
	
	
	// 관심키워드 랭킹 분석
	public LinkedHashMap<String, Object> getInterestRanking(SocialKeywordRankingVo keywordRankingVo) throws Exception {
		LinkedHashMap<String, Object> totalResultMap = new LinkedHashMap<String, Object>(); // 최종 리턴 맵
		LinkedHashMap<String, Object> totalPeriodKeyword = new LinkedHashMap<String, Object>(); // 주기별 랭킹의 키워드순위 정보를 담는 맵

		String[] iKeywordArr = keywordRankingVo.getInterestKeywordArr(); // 관심키워드
		LinkedHashMap<String, String> periodMap = getCategories(keywordRankingVo); // 종합리포트 챠트 x축(기간)
		ArrayList<String> periodList = new ArrayList<String>(periodMap.keySet()); // 기간 리스트
		ArrayList<HashMap<String, Object>> periodRankingList = null; // 기간별 키워드랭킹정보

		HashMap<String, Integer> pKeywordRanking = null;
		HashMap<String, Integer> nKeywordRanking = new HashMap<String, Integer>();

		int index = keywordRankingVo.getRankingIndex(); // 랭킹표 생성시 주기가 들어 있는 맵의 시작인덱스
		int stateNum = periodList.size();

		// 순위표 맨앞의 이전 순위구하기
		String prevPeriod = "";
		try {
			prevPeriod = periodList.get(index - 1);
		} catch (Exception e) { //// periodMap의 맨처음 기간의 이전 기간구하기

			String startDate = keywordRankingVo.getStartDate().replace("/", "");
			String condition = keywordRankingVo.getCondition();

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
		//선택한 기간 한단계 이전 기간의 키워드 통계량 조회(비교를 위해)
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_PERIOD, prevPeriod, keywordRankingVo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, "DESC"));
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
		//System.out.println("##### 관심키워드랭킹 시작기간 이전기간 조회 query: " + queryParser.queryToString(query));

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
			for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) { // - 이전 주의 전체 키워드 값 hasmap에 세팅 - 순위 구하기 위함
				nKeywordRanking.put(new String(groupResultlist[0].getId(i)).trim(), i + 1);
			}
		}
		// - 순위표 맨앞의 이전 순위구하기 끝

		int periodCount = 0;
		while (periodCount < stateNum) {

			String period = periodList.get(index + periodCount);
			querySet = new QuerySet(1);
			query = new Query();
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(WHERESET_PERIOD, period, keywordRankingVo));
			// query.setFilter(createFilterSet(keywordRankingVo));
			query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, "DESC")); // 관심키워드일 경우 전체 키워드 조회
			query.setFrom(Globals.MARINER_COLLECTION2);
			query.setResult(0, 0);
			query.setSearch(true);
			query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시
			query.setDebug(MARINER_DEBUG);
			query.setLoggable(MARINER_LOGGABLE);
			query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
			query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			if(MARINER_PRITER){
				query.setResultModifier("priter"); 
				query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
			}
			querySet.addQuery(query);

			//System.out.println("##### 관심키워드랭킹 기간 " + keywordRankingVo.getCondition()+ " \"" + period + "\" query2: " + queryParser.queryToString(query));

			// - 검색 서버로 검색 정보 전송
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
				pKeywordRanking = nKeywordRanking; // pKeywordRanking 이전 키워드 정보 담은 배열
				nKeywordRanking = new HashMap<String, Integer>();

				// - 전체 키워드 값 Map에 세팅 -start-
				Map<String, String> totalKeyWord = new HashMap<String, String>();

				for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
					String tempKeyword = new String(groupResultlist[0].getId(i)).trim();
					if(tempKeyword != null && !"".equals(tempKeyword)){
						totalKeyWord.put(new String(groupResultlist[0].getId(i)).trim(), groupResultlist[0].getIntValue(i) + "/" + (i + 1));	
					}
				}
				// - 전체 키워드 값 Map에 세팅 -end-

				// - 관심키워드 랭킹 설정 -start- 관심키워드 만큼 반복 돌아서 관심키워드를 키값으로 totalKeyWord_Map에 해당 키워드가 있는지 찾아낸다.
				if (groupResultlist[0].groupResultSize() > 0) {
					for (int i = 0; i < iKeywordArr.length; i++) {
						String keyword = iKeywordArr[i];

						String rank = totalKeyWord.get(keyword);

						if (rank != null) {
							String realRank = rank.split("/")[1];
							rank = rank.split("/")[0];
							int change = 0;
							HashMap<String, Object> keywordMap = new HashMap<String, Object>();
							keywordMap.put("keyword", keyword); // 관심 키워드 값 설정
							keywordMap.put("interestYn", "N");
							if (pKeywordRanking.containsKey(keyword)) { // 이전달에 같은키워드가 있으면 변동랜킹구함
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
							nKeywordRanking.put(keyword, Integer.parseInt(realRank));
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
					} 
					else {
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
				// - 관심 키워드 랭킹 구하기 - end -
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
		}
		
		totalResultMap.put("periodKeyword", totalPeriodKeyword); // 주기별 키워드랭킹정보

		ArrayList<String> periodTag = new ArrayList<String>();
		for (Object o : periodMap.values()) {
			String period = (String) o;
			periodTag.add(makePeriodTag(period, keywordRankingVo.getCondition()));
		}
		totalResultMap.put("period", periodTag); // 기간선택 버튼
		double btnCount = Math.floor(938 / periodMap.size());	//???????????? 버튼크기 조절을 왜 여기서 ??????
		totalResultMap.put("btnSize", btnCount); // 버튼 크기 사이즈 임시

		return totalResultMap;
	}

	/**
	 * 이슈키워드 분석
	 * 
	 * @param EmotionVo
	 *            - 조회할 정보가 담긴 VO
	 * @return HashMap
	 * @exception Exception
	 */
	public LinkedHashMap<String, Object> getIssueRanking(SocialKeywordRankingVo keywordRankingVo) throws Exception {
		//???이슈키워드 랭킹에서 리포트 차트는 금주의 빈도수 순으로 정렬
		//하단 이슈키워드랭킹은 순위 많이 오른 순서로 정렬 해야 된다.
		//소스 로직 검토 필요.
		LinkedHashMap<String, Object> totalResultMap = new LinkedHashMap<String, Object>(); // 최종 리턴 맵
		String[] iKeywordArr = keywordRankingVo.getInterestKeywordArr(); // 관심키워드

		ArrayList<HashMap<String, Object>> periodRankingList = new ArrayList<HashMap<String, Object>>();
		
		String searchKeyword = "";
		String lastDay = null; // 전주, 전월, 전년
		String thisDay = null; // 금주, 금월, 금년

		LinkedHashMap<String, String> categoriesMap = null;

		if (keywordRankingVo.getCondition().equals("WEEK")) {
			String strDate = keywordRankingVo.getStartDate() + "000000";
			String endDate = keywordRankingVo.getEndDate() + "235959";
			categoriesMap = DateUtil.getCategories(strDate, endDate, "yyyymmdd", Globals.TREND_PERIOD_WEEK);
		} else if (keywordRankingVo.getCondition().equals("MONTH")) {
			String strDate = keywordRankingVo.getStartDate() + "000000";
			String endDate = keywordRankingVo.getEndDate() + "235959";
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.TREND_PERIOD_MONTH);
		} else {
			String strDate = keywordRankingVo.getStartDate() + "000000";
			String endDate = keywordRankingVo.getEndDate() + "235959";
			categoriesMap = DateUtil.getCategories(strDate, endDate, "", Globals.TREND_PERIOD_YEAR);
		}

		if (categoriesMap.keySet().size() == 2) {
			lastDay = categoriesMap.keySet().toArray()[0].toString();
			thisDay = categoriesMap.keySet().toArray()[1].toString();
		} else if (categoriesMap.keySet().size() == 3) {
			lastDay = categoriesMap.keySet().toArray()[1].toString();
			thisDay = categoriesMap.keySet().toArray()[2].toString();
		}
		if (lastDay != null & thisDay != null) {
			if (Integer.parseInt(thisDay) > Integer.parseInt(lastDay)) {
				Result[] resultlist = null;
				Result lastResult = null;
				Result thisResult = null;
				QuerySet querySet = new QuerySet(2);

				// - 전주(전월,전년) , 금주(금월,금년) 만 비교하므로 쿼리 2개
				for (int i = 0; i < 2; i++) {
					String temp = lastDay;
					if (i == 1) {
						temp = thisDay;
					}
					Query query = new Query();
					query.setSelect(createSelectSet(SELECTSET_COUNT));
					query.setWhere(createWhereSet(WHERESET_PERIOD, temp, keywordRankingVo));
					query.setFilter(createFilterSet(keywordRankingVo));
					query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, "DESC"));
					query.setFrom(Globals.MARINER_COLLECTION2);
					query.setResult(0, 0);
					query.setSearch(true);
					query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시
					query.setDebug(MARINER_DEBUG);
					query.setLoggable(MARINER_LOGGABLE);
					query.setPrintQuery(MARINER_PRINT_QUERY);
					query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
					query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
					if(MARINER_PRITER){
						query.setResultModifier("priter"); 
						query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
					}
					querySet.addQuery(query);
					QueryParser queryParser = new QueryParser();
					//System.out.println("##### 이슈키워드query: " + queryParser.queryToString(query));
				}
				// - 검색 서버로 검색 정보 전송
				CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
				CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
				int returnCode = command.request(querySet);
				if (returnCode >= 0) {
					ResultSet results = command.getResultSet();
					resultlist = results.getResultList();
					if (resultlist != null) {
						lastResult = resultlist[0];
						thisResult = resultlist[1];
					}
				}

				HashMap<String, Integer> lastKeywordRanking = new HashMap<String, Integer>(); // 이전 키워드 값

				// - 이전주 값 세팅
				if (lastResult != null && lastResult.getGroupResultSize() != 0) {
					GroupResult[] groupResultlist = lastResult.getGroupResults();
					for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
						lastKeywordRanking.put(new String(groupResultlist[0].getId(i)).trim(), groupResultlist[0].getIntValue(i));
					}
				}

				ArrayList<String> categories = new ArrayList<String>(); // 하이차트 x값
				ArrayList<Integer> series = new ArrayList<Integer>(); // 하이차트 y값
				int chartCount = 0; // - 하이차트는 상위 10개만 노출 - 10개 카운터세는 변수

				// - 금주 키워드 갯수만큼 돌면서 이슈키워드 추출
				if (thisResult != null && thisResult.getGroupResultSize() != 0) {
					GroupResult[] groupResultlist = thisResult.getGroupResults();
					for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
						if (lastKeywordRanking.get(new String(groupResultlist[0].getId(i)).trim()) != null) {
							int lastWeight = lastKeywordRanking.get(new String(groupResultlist[0].getId(i)).trim());
							int thisWeight = groupResultlist[0].getIntValue(i);
							if (thisWeight > lastWeight) {
								int change = thisWeight - lastWeight; // - 금주데이터 가중치 (-) 지난데이터 가중치
								String keyword = new String(groupResultlist[0].getId(i)).trim();
								HashMap<String, Object> keywordMap = new HashMap<String, Object>();
								keywordMap.put("keyword", keyword); // - 이슈키워드 값
								keywordMap.put("interestYn", "N");
								// - 관심 키워드면 하이라이팅 처리
								if (iKeywordArr != null) {
									for (String ikeyword : iKeywordArr) {
										if (ikeyword.equals(keyword)) {
											keywordMap.put("interestYn", "Y");
										}
									}
								}
								keywordMap.put("imagePath", "up");
								keywordMap.put("nowrank", change);
								keywordMap.put("change", change);
								periodRankingList.add(keywordMap);

								if (chartCount < 10) {
									// - 이슈키워드 하이차트에 관련한 데이터 설정
									categories.add(keyword);
									series.add(thisWeight);
								}
								chartCount++;
							}
						}
					}
				}

				// - 이슈키워드 하이차트에 필요한 값 세팅
				gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
				List<Object> list = new ArrayList<Object>();
				HashMap<String, Object> seriesData = new HashMap<String, Object>();
				seriesData.put("name", "빈도수");
				seriesData.put("data", series);
				seriesData.put("type", "column");
				list.add(0, seriesData);
				totalResultMap.put("series", gson.toJson(list));
				totalResultMap.put("categories", gson.toJson(categories));
			}
		}

		if (periodRankingList.size() == 0) {
			// - 값이 없으면 끝낸다
			totalResultMap = new LinkedHashMap<String, Object>();
			return totalResultMap;
		}

		// - periodRankingList HashMap List 숫자 내림차순 정렬
		// - 관심키워드의 등록에 따라 순서대 periodRankingList에 담겨 있으나 실질적으론 nowrank 값이 실제 랭킹이다.
		// - 그래서 nowrank 의값에 따라 정렬 해줘야 함.
		Collections.sort(periodRankingList, new Comparator<HashMap<String, Object>>() {
			public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
				int one = Integer.parseInt(o1.get("nowrank").toString());
				int two = Integer.parseInt(o2.get("nowrank").toString());
				return one > two ? -1 : one < two ? 1 : 0;
			}
		});

		// - 이슈 키워드 랭킹 구하기 - start-
		// - nowrank에 따라 화면에 보여질 순번 정하기
		int realyRank = 1;
		int realyRankTemp = 0;
		for (int j = 0; j < periodRankingList.size(); j++) {
			HashMap<String, Object> temp = periodRankingList.get(j);
			if (j == 0) {
				// - 첫 번째 검색화면에 뿌려질 키워드 세팅
				searchKeyword = temp.get("keyword").toString();
			}
			if (realyRank == 1) {
				periodRankingList.get(j).put("count", realyRank);
				realyRankTemp = Integer.parseInt(temp.get("nowrank").toString());
			} else {
				if (realyRankTemp == Integer.parseInt(temp.get("nowrank").toString())) {
					// realyRank--;
					periodRankingList.get(j).put("count", realyRank);
				} else {
					periodRankingList.get(j).put("count", realyRank);
				}
				realyRankTemp = Integer.parseInt(temp.get("nowrank").toString());
			}
			realyRank++;
		}
		// - 이슈 키워드 랭킹 구하기 - end -

		// - 이슈키워드 랭킹 값 설정 1위 ~ 30위
		if (periodRankingList.size() > 0) {
			LinkedHashMap<String, Object> totalPeriodKeyword = new LinkedHashMap<String, Object>(); // 주기별 랭킹의 키워드순위 정보를 담는 맵

			int pageSize = periodRankingList.size() / 10;
			if (pageSize == 0) {
				totalPeriodKeyword.put("1위 ~ 10위", periodRankingList);
			} else {

				for (int i = 0; i <= pageSize; i++) {

					ArrayList<HashMap<String, Object>> resultRankingList = new ArrayList<HashMap<String, Object>>();
					int startNum = 10 * i;
					int endNum = 10 * (i + 1);
					if (endNum > periodRankingList.size()) {
						endNum = periodRankingList.size();
					}
					if (endNum > 100) {
						break;
					}
					for (int j = startNum; j < endNum; j++) {
						resultRankingList.add(periodRankingList.get(j));
					}
					if (i == 0) {
						totalPeriodKeyword.put("1위 ~ 10위", resultRankingList);
					} else if (i == 1) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("11위 ~ 20위", resultRankingList);
						}
					} else if (i == 2) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("21위 ~ 30위", resultRankingList);
						}
					} else if (i == 3) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("31위 ~ 40위", resultRankingList);
						}
					} else if (i == 4) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("41위 ~ 50위", resultRankingList);
						}
					} else if (i == 5) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("51위 ~ 60위", resultRankingList);
						}
					} else if (i == 6) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("61위 ~ 70위", resultRankingList);
						}
					} else if (i == 7) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("71위 ~ 80위", resultRankingList);
						}
					} else if (i == 8) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("81위 ~ 90위", resultRankingList);
						}
					} else if (i == 9) {
						if (resultRankingList.size() > 0) {
							totalPeriodKeyword.put("91위 ~ 100위", resultRankingList);
						}
					}

				}
			}
			totalResultMap.put("periodKeyword", totalPeriodKeyword);
		}

		totalResultMap.put("searchKeyword", searchKeyword);
		double btnCount = Math.floor(938 / 3);	//??? 버튼크기???
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
	public HashMap<String, Object> getSearchResult(SocialKeywordRankingVo vo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> groupResult = null;
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;
		//String share = "0"; // 점유율
		String keywordTemp = null;
		String channelName = "";
		
		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();

		int startNo = (vo.getCurrentPage() - 1) * vo.getPageSize();
		int endNo = startNo + vo.getPageSize() - 1;
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(SELECTSET_LIST));		
		query.setWhere(createWhereSet(WHERESET_KEYWORD, vo.getKeyword(), vo));
		query.setFilter(createFilterSet(vo));
		//query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, vo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(startNo, endNo);
		query.setSearch(true);
		query.setUserName(vo.getLogin_Id()); // 로그에 남길 키워드 명시
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

		//선택한 키워드에 대한 문서 검색 결과
		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
		
			if (resultlist != null) {
				
				result = resultlist[0];
				if (result != null ) {
					GroupResult[] groupResultlist = result.getGroupResults();
					NumberFormat nf = NumberFormat.getNumberInstance();
		
					// 리스트 결과 담기
					listResult = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < result.getRealSize(); i++) {
						HashMap<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put(Globals.FIELD_DQ_DOCID, new String(result.getResult(i, 0)));
						//resultMap.put(Globals.FIELD_VOCNO, new String(result.getResult(i, 1)));
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
					
					searchResult.put("keyword", vo.getKeyword()); // 검색 키워드
					searchResult.put("listResult", listResult); // 리스트
					searchResult.put("totalSize", nf.format(result.getTotalSize())); // 전체 건수
					//searchResult.put("share", share); // 점유율
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
			}
		}
		return searchResult;
	}

	/**
	 * VOC 검색결과
	 * 
	 * @param EmotionVo
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(SocialKeywordRankingVo keywordRankingVo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_LIST));
		query.setWhere(createWhereSet(WHERESET_KEYWORD, keywordRankingVo.getKeyword(), keywordRankingVo));
		query.setFilter(createFilterSet(keywordRankingVo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 999);
		query.setSearch(true);
		query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시
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
	 * 종합 랭킹 분석 - 워드 클라우드 차트
	 * @param keywordRankingVo
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getWordCloudChart(SocialKeywordRankingVo keywordRankingVo) throws Exception{
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		LinkedHashMap<String, String> periodMap = getCategories(keywordRankingVo); // 종합리포트 챠트 x축(기간)
		ArrayList<String> periodList = new ArrayList<String>(periodMap.keySet()); // 기간 리스트
		Result[] resultlist = null;
		Result result = null;
		LinkedList<HashMap<String,Object>> totalPeriodKeywordMapList = new LinkedList<HashMap<String,Object>>();	//전체 기간 동안의 키워드 빈도수 저장 리스트
		LinkedList<LinkedList<HashMap<String,Object>>> periodKeywordMapList = new LinkedList<LinkedList<HashMap<String,Object>>>();	//각 x축 기간별 키워드 빈도수 저장
		int maxKeywordCount = 100;	//클라우드 차트 하나에 출력한 최대 키워드 수
		String groupBySetOption = "DESC 0 " + String.valueOf(maxKeywordCount - 1);
		QueryParser queryParser = new QueryParser();
		LinkedList<Query> queryList = new LinkedList<Query>();
		//검색기간 전체 동안의 키워드 랭킹 조회 쿼리
		Query totalPeriodQuery = new Query();
		totalPeriodQuery.setSelect(createSelectSet(SELECTSET_COUNT));
		totalPeriodQuery.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, "", keywordRankingVo));
		totalPeriodQuery.setFilter(createFilterSet(keywordRankingVo));
		totalPeriodQuery.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, groupBySetOption));
		totalPeriodQuery.setResult(0, 0);
		totalPeriodQuery.setFrom(Globals.MARINER_COLLECTION2);
		totalPeriodQuery.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | (byte) Protocol.ThesaurusOption.QUASI_SYNONYM);
		totalPeriodQuery.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		totalPeriodQuery.setSearch(true);
		totalPeriodQuery.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시v
		totalPeriodQuery.setDebug(MARINER_DEBUG);
		totalPeriodQuery.setPrintQuery(MARINER_PRINT_QUERY);
		totalPeriodQuery.setLoggable(MARINER_LOGGABLE);
		queryList.add(totalPeriodQuery);
		//x축 한 기간 동안의 키워드 랭킹 조회 쿼리
		for(String indexPeriod : periodList){
			Query query = new Query();
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(WHERESET_PERIOD, indexPeriod, keywordRankingVo));
			query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, groupBySetOption));
			query.setResult(0, 0);
			query.setFrom(Globals.MARINER_COLLECTION2);
			query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | (byte) Protocol.ThesaurusOption.QUASI_SYNONYM);
			query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
			query.setSearch(true);
			query.setUserName(keywordRankingVo.getLogin_Id()); // 로그에 남길 키워드 명시v
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
			String[] colorArr = Globals.COM_WORD_CLOUD_COLOR;
			//전체 기간 동안의 키워드 빈도수 저장
			Result totalPeriodResult = resultlist[0];
			GroupResult[] totalPeriodGroupResult = totalPeriodResult.getGroupResults();
			for(int j=0; j < totalPeriodGroupResult[0].groupResultSize() && j < maxKeywordCount; j++){
				String tempKeyword = new String(totalPeriodGroupResult[0].getId(j));
				int count = totalPeriodGroupResult[0].getIntValue(j);
				HashMap<String, Object> totalPeriodKeywordMap = new HashMap<String, Object>();
				if(tempKeyword != null && !"".equals(tempKeyword.trim())){
					totalPeriodKeywordMap.put("text", tempKeyword);
					totalPeriodKeywordMap.put("weight", count);
					totalPeriodKeywordMap.put("color", colorArr[j % colorArr.length]);
				}
				totalPeriodKeywordMapList.add(totalPeriodKeywordMap);
			}
			//x축 기간 별 키워드 빈도수 저장
			for(int i=0;i < periodList.size(); i++){
				Result indexPeriodResult = resultlist[i + 1];
				GroupResult[] indexPeriodGroupResult = indexPeriodResult.getGroupResults();
				LinkedList<HashMap<String, Object>> indexPeriodMapList = new LinkedList<HashMap<String, Object>>();
				for(int j=0; j < indexPeriodGroupResult[0].groupResultSize() && j < maxKeywordCount; j++){
					String tempKeyword = new String(indexPeriodGroupResult[0].getId(j));
					int count = indexPeriodGroupResult[0].getIntValue(j);
					HashMap<String, Object>tempMap = new HashMap<String, Object>();
					if(tempKeyword != null && !"".equals(tempKeyword.trim())){
						tempMap.put("text", tempKeyword);
						tempMap.put("weight", count);
						tempMap.put("color", colorArr[j % colorArr.length]);
					}
					indexPeriodMapList.add(tempMap);
				}
				periodKeywordMapList.add(indexPeriodMapList);
			}
		}
		LinkedList<String> chartTitleList = new LinkedList<String>();
		chartTitleList.add("전체(" + periodMap.get(periodList.get(0)) + "~" + periodMap.get(periodList.get(periodList.size() - 1))+ ")");
		for(String periodStr: periodList){
			chartTitleList.add(periodMap.get(periodStr));
		}
		LinkedList<LinkedList<HashMap<String,Object>>> finalWordMapList = new LinkedList<LinkedList<HashMap<String,Object>>>();	//최종 워드맵 리스트( 전체기간, x축 각 기간별 키워드+빈도수)
		finalWordMapList.add(totalPeriodKeywordMapList);
		finalWordMapList.addAll(periodKeywordMapList);
		returnMap.put("chartTitleList", chartTitleList);
		returnMap.put("finalWordMapList", finalWordMapList);
		
		return returnMap;
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
		case SELECTSET_ALIKE:	// 유사문서
			selectSet = new SelectSet[]{
					new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE),		//수집문서ID
					new SelectSet(Globals.FIELD_TITLE, (byte)(Protocol.SelectSet.NONE)),	//제목
					new SelectSet(Globals.FIELD_CONTENT,  (byte)(Protocol.SelectSet.NONE)),		//본문
					new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE),		//등록일
					new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE),		//가중치
					new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE),		//VOC문서 번호
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
	private WhereSet[] createWhereSet(int flag, String keyword, SocialKeywordRankingVo vo) {

		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();

		switch (flag) {
		case WHERESET_PERIOD : //기간검색 - 종합랭킹 등에서 날짜별 건수 검색용
			whereSetList.add(new WhereSet(vo.getCondition(), Protocol.WhereSet.OP_HASALL, keyword, 0));	//종합랭킹분석 시 지정한 주기(일,월,분기,반기,연도) 별로 해당 주기 필드에 날짜값 지정.
			if(!vo.getSocialChannel().isEmpty() && !vo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, vo.getSocialChannel(), 0));
			}			
			break;
		case WHERESET_KEYWORD_REPORT :	//상위키워드 or 종합 리포트차트생성 조건 or 워드 클라우드 에서 사용
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(keyword != null && !"".equals(keyword)){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, keyword, 0));	//추출 키워드 검색
			}
			if(!vo.getSocialChannel().isEmpty() && !vo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, vo.getSocialChannel(), 0));
			}
			break;
		case WHERESET_KEYWORD :	// 키워드에 대한 하단 VOC 검색결과
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			if(keyword != null && !"".equals(keyword)){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword, 500));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, keyword, 100));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, keyword, 1000)); // 추출 키워드 검색
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}
			if(!vo.getSocialChannel().isEmpty() && !vo.getSocialChannel().equals(Globals.COM_SELECT_ALL)) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, vo.getSocialChannel(), 0));
			}
			break;
		case WHERESET_DETAIL :	// 상세보기
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, keyword, 0));	//keyword로 수집문서ID 검색
			break;
		case WHERESET_ALIKE :	// 유사문서
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, vo.getTitle(), 1000));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, vo.getContent(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, vo.getDq_docid(), 0)); // 같은id제거하기
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
	private FilterSet[] createFilterSet(int flag, SocialKeywordRankingVo vo) {

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
		switch (flag) {
		case FILTERSET_LIST:	//VOC 검색결과
			break;
		case FILTERSET_RANKING:	//종합랭킹
			
			break;
		case FILTERSET_SENSE:	//감성분
			break;

		default:
			break;
		}
		if (filterlist.size() > 0) {
			filterSet = new FilterSet[filterlist.size()];
			for (int i = 0; i < filterlist.size(); i++) {
				filterSet[i] = filterlist.get(i);
			}
		}
		return filterSet;
	}
	//2017.11.02 필터 적용할만한 것이 날짜 외에는 보이지 않아 별도로 생성.
	private FilterSet[] createFilterSet(SocialKeywordRankingVo vo) {

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
		case GROUPBYSET_RANKING:	//종합랭킹 분석용
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), option, ""));
			break;
		case GROUPBYSET_KEYWORD_NAME:	//종합 리포트 차트
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), option, ""));
			break;
		case GROUPBYSET_PERIOD:	//기간별 그룹카운트 조회
			groupbysetList.add(new GroupBySet(option, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", ""));
			break;
		/*case GROUPBYSET_VOC_CATEGORY:	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
			groupbysetList.add(new GroupBySet(option, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
			break;*/
		case GROUPBYSET_DEPT:	//처리주무부서 셀렉트박스 생성용
			groupbysetList.add(new GroupBySet(Globals.GROUP_CDREPDEPT, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", option, ""));	
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