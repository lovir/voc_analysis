package com.diquest.voc.socialChannelStatus.service.impl;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

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
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.impl.CommonSelectBoxDAO;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.socialChannelStatus.service.SocialChannelStatusService;
import com.diquest.voc.socialChannelStatus.vo.SocialChannelStatusVo;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("socialChannelStatusService")


public class SocialChannelStatusServiceImpl implements SocialChannelStatusService{
		
	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;
	
	private Gson gson = null;
	private static final boolean MARINER_LOGGABLE = true; // 검색엔진 로그 저장 여부
	private static final boolean MARINER_PRINT_QUERY = false; // 검색엔진 검색쿼리 출력 여부
	private static final boolean MARINER_DEBUG = true; // 검색엔진 디버그 사용 여부
	private static final boolean MARINER_PRITER = false; //개인정보보호 필터(PRITER) 사용 여부

	// SELECTSET 분기
	private static final int SELECTSET_STATUS = 100; // 분야별현황 조회용
	private static final int SELECTSET_DETAIL = 101; // 상세페이지 조회용
	private static final int SELECTSET_LIST = 103; // 검색결과 리스트
	private static final int SELECTSET_COUNT = 104; // 통계치용 건수 조회용
	private static final int SELECTSET_ALIKE = 105;
	
	// WHERESET 분기
	private static final int WHERESET_STATUS = 200; 		// 분야별현황 분류별 조회조건	
	private static final int WHERESET_KEYWORDS = 201; 		// 분야별현황 분류별 키워드리스트
	private static final int WHERESET_PERIOD = 202; 		//기간검색 - 종합랭킹 등에서 날짜별 건수 검색용
	private static final int WHERESET_PERIOD_REP = 202; 	//기간검색 - 종합랭킹 등에서 날짜별 건수 검색용
	private static final int WHERESET_SEARCHKEYWORD = 203; // 키워드에 대한 하단 VOC 검색결과
	private static final int WHERESET_DETAIL = 204; 		// 상세보기
	private static final int WHERESET_ALIKE = 205;
	
	// FILTERSET 분기
	private static final int FILTERSET_STATUS = 300; 		// 분야별 분류별 필터 
	//private static final int FILTERSET_STATUSDETAIL = 301; // 각 역별 키워드/긍부정 조회조건	

	// GROUPBYSET 분기
	private static final int GROUPBYSET_STATUS = 400; 		// 분야별 분류별 그룹
	private static final int GROUPBYSET_KEYWORDS = 401; 	// 분야별현황 분류별 키워드리스트
	private static final int GROUPBYSET_PERIOD = 402;	//기간별 그룹카운트 조회
	private static final int GROUPBYSET_VOC_CATEGORY = 403;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_RANKING = 404; // 종합랭킹, 상위키워드 10개 추출
	
	/**
	 * get pie chart
	 * 
	 * @return json String
	 */
	public ArrayList<HashMap<String, String>> getChannelChart(SocialChannelStatusVo vo) throws Exception{
		
		ArrayList<HashMap<String, String>> siteList = new ArrayList<HashMap<String, String>>();
		Result[] resultlist = null;
		String channelName = "";
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		
		if(vo.getSocialChannel() !=null && !vo.getSocialChannel().equals("")){
			CommonSelectBoxVo commonVo = new CommonSelectBoxVo();
			commonVo.setCode(vo.getSocialChannel());
			commonVo = commonSelectBoxDAO.socialChannel(commonVo);
			channelName = commonVo.getName();
		}
		
		
		
		query.setSelect(createSelectSet(SELECTSET_STATUS));
		query.setWhere(createWhereSet(WHERESET_STATUS, vo, channelName)); // case를 위한 번호, 키워드, VO
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_STATUS, vo));
		query.setFrom(Globals.MARINER_COLLECTION2);
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

//		System.out.println("##### 채널별현황분석 query1: " + queryParser.queryToString(query));
		

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
				
				// 분야별 결과 리스트에 담기		
				for(int i = 0; i < groupResults[0].groupResultSize(); i++){
					
					HashMap<String, String> map = new HashMap<String, String>();
					List<String> name = null;
					map.put("NAME", new String(groupResults[0].getId(i)));
					map.put("COUNT", Integer.toString(groupResults[0].getIntValue(i)));
					siteList.add(map);
				}
			}
		}
		//gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		//return gson.toJson(fieldCodeList);
		return siteList;
	}
	/**
	 * get keywords
	 * 
	 * @return json String
	 */
	public String getChannelKeywords(SocialChannelStatusVo vo) throws Exception{
		
		ArrayList<HashMap<String, String>> fieldKeywordList = new ArrayList<HashMap<String, String>>();
		Result[] resultlist = null;
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_STATUS));
		query.setWhere(createWhereSet(WHERESET_KEYWORDS, vo, "")); // case를 위한 번호, 키워드, VO
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_KEYWORDS, vo));
		query.setFrom(Globals.MARINER_COLLECTION2);
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

//		System.out.println("##### 분야별키워드분석 query1: " + queryParser.queryToString(query));
		

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
				
				// 분야별 키워드 결과 리스트에 담기		
				int len = groupResults[0].groupResultSize();
				if(groupResults[0].groupResultSize() > 10)
					len = 10;
				for(int i = 0; i < len; i++){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("KEYWORD", new String(groupResults[0].getId(i)));
					map.put("COUNT", Integer.toString(groupResults[0].getIntValue(i)));
					fieldKeywordList.add(map);
				}
			}
		}
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		return gson.toJson(fieldKeywordList);
	}
	
	// 조회조건과 기간선택 으로 리포트 챠트에 필요한 json 데이타 리스트 생성 
	public HashMap<String, Object> getSynthesisReport(SocialChannelStatusVo vo) throws Exception {

		LinkedHashMap<String, String> periodMap = getCategories(vo); // 종합리포트 챠트 x축(기간)
		ArrayList<HashMap<String, String>> siteList = getChannelChart(vo);
		ArrayList<String> channelNameList = new ArrayList<String>();
		
		ArrayList<HashMap<String, Object>> channelPeriodCountList = null; // json으로 변환할 리스트
			
		if (siteList != null) {

			Result[] resultlist = null;
			channelPeriodCountList = new ArrayList<HashMap<String, Object>>();
			//QuerySet querySet = new QuerySet(keywordArr.length);
			
			// 분야별 현황 코드 리스트 세팅
			for(int i=0; i<siteList.size(); i++){
				
				channelNameList.add(siteList.get(i).get("NAME"));
				vo.setCodeList(channelNameList);
			}
			QuerySet querySet = new QuerySet(channelNameList.size());	
			for (int i = 0; i < siteList.size(); i++) { // 분야 갯수만큼 쿼리 생성
				vo.setSocialSite(channelNameList.get(i));				
				
				Query query = new Query();
				query.setSelect(createSelectSet(SELECTSET_STATUS));
				query.setWhere(createWhereSet(WHERESET_PERIOD, vo, "")); // case를 위한 번호, 키워드, VO
				query.setFilter(createFilterSet(vo));
				query.setGroupBy(createGroupBySet(GROUPBYSET_PERIOD, vo));
				query.setFrom(Globals.MARINER_COLLECTION2);
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
//				System.out.println("##### 라인 차트 쿼리 키워드\"" + channelNameList.get(i) + "\" : " + queryParser.queryToString(query));
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
				for (int i = 0; i < channelNameList.size(); i++) {

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
						periodCount.put("name", siteList.get(i).get("NAME"));
						periodCount.put("data", new ArrayList<Integer>(lhm.values()));
						channelPeriodCountList.add(periodCount);
					}
				}
			}
		}

		HashMap<String, Object> synthesisReportMap = new HashMap<String, Object>();
		synthesisReportMap.put("periodList", periodMap.values());

		if (channelPeriodCountList != null) {
			synthesisReportMap.put("channelPeriodCountList", channelPeriodCountList);
		}
		return synthesisReportMap;

	}
	
	//리포트 차트 x축(기간) 출력
	private LinkedHashMap<String, String> getCategories(SocialChannelStatusVo vo){

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
	
	/**
	 * VOC 검색결과
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(SocialChannelStatusVo vo) throws Exception {
		
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

		if(vo.getSocialChannel() !=null && !vo.getSocialChannel().equals("")){
			CommonSelectBoxVo commonVo = new CommonSelectBoxVo();
			commonVo.setCode(vo.getSocialChannel());
			commonVo = commonSelectBoxDAO.socialChannel(commonVo);
			channelName = commonVo.getName();
		}
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(SELECTSET_LIST));		
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, channelName));
		query.setFilter(createFilterSet(vo));
		//query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, vo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
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
//		System.out.println("##### 리스트 검색 query1: " + queryParser.queryToString(query));

		// 키워드 없이 전체 건수 구하기(점유율)
		/*query = new Query();
		query.setSelect(createSelectSet(SELECTSET_STATUS));
		if (keywordRankingVo.getEmotion() != null && !keywordRankingVo.getEmotion().equals(""))
			query.setWhere(createWhereSet(10, keywordRankingVo));
		else
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, ""));
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RANKING, vo));
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		querySet.addQuery(query);

//		System.out.println("##### 리스트 검색 query2: " + queryParser.queryToString(query));
		*/

		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		// resultlist[0]이 해당 기간에 속한 키워드에 관한 문서 검색결과
		// resultlist[1]이 해당 기간에 추출된 keyword 전체 결과 -
		/*
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
		}*/
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
					/*if ((keywordRankingVo.getEmotion() != null && !keywordRankingVo.getEmotion().equals("")) && keywordRankingVo.getKeyword().equals(""))
						searchResult.put("keyword", ""); // 검색 키워드
					else*/
					searchResult.put("keyword", keywordTemp); // 검색 키워드
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
	 * VOC 엑셀결과
	 * 
	 * @param EmotionVo
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(SocialChannelStatusVo vo) throws Exception {
		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_LIST));
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, ""));
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, vo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
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
	 * VOC 검색_상세페이지
	 * 
	 * @param id
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, String> getdetailViewResult(String id) throws Exception {
		HashMap<String, String> resultMap = null;

		char[] startTag = "<span class=\"result_keyword\">".toCharArray();
		char[] endTag = "</span>".toCharArray();

		Result[] resultlist = null;
		Result result = null;
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(SELECTSET_DETAIL));
		SocialChannelStatusVo vo = new SocialChannelStatusVo();
		vo.setDq_docid(id);
		query.setWhere(createWhereSet(WHERESET_DETAIL, vo, ""));
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE));
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();
//		System.out.println("##### 상세검색 query: " + queryParser.queryToString(query));
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
		if (result != null) {
			// 결과 담기
			resultMap = new HashMap<String, String>();
			for (int i = 0; i < result.getRealSize(); i++) {
				resultMap.put(Globals.FIELD_DQ_DOCID, new String(result.getResult(i, 0)));
				resultMap.put(Globals.FIELD_TITLE, new String(result.getResult(i, 1)));
				resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)).replaceAll("\n", "<br/>"));
				resultMap.put(Globals.FIELD_REGDATE, new String(result.getResult(i, 3)));
				resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 4)));
				resultMap.put(Globals.FIELD_CHANNEL, new String(result.getResult(i, 5)));
				resultMap.put(Globals.FIELD_SITE, new String(result.getResult(i, 6)));
				resultMap.put(Globals.FIELD_URL, new String(result.getResult(i, 7)));
			}
		}
		return resultMap;
	}
	/**
	 * VOC 검색_유사문서
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public ArrayList<HashMap<String, Object>> getAlikeSearchResult(SocialChannelStatusVo vo) throws Exception {
		ArrayList<HashMap<String, Object>> searchResult = null;

		Result[] resultlist = null;
		Result result = null;
		Query query = new Query();

		query.setSelect(createSelectSet(SELECTSET_ALIKE));
		query.setWhere(createWhereSet(WHERESET_ALIKE, vo, ""));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION2);
		query.setResult(0, 9);
		query.setThesaurusOption((byte) (Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));
		query.setSearchOption((byte) (Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED | Protocol.SearchOption.PERCENT));
		query.setSearch(true);
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시v
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		if(MARINER_PRITER){
			query.setResultModifier("priter"); 
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();
//		System.out.println("유사문서 보기 : " + queryParser.queryToString(query));
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

		if (result != null) {
			// 리스트 결과 담기
			searchResult = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < result.getRealSize(); i++) {
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(Globals.FIELD_DQ_DOCID, new String(result.getResult(i, 0)));
				String title = new String(result.getResult(i, 1));
				if(title == null || "".equals(title)) title = "제목없음";
				resultMap.put(Globals.FIELD_TITLE, title);
				resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)));
				resultMap.put(Globals.FIELD_REGDATE, new String(result.getResult(i, 3)));
				int weight = Integer.parseInt(new String(result.getResult(i, 4)));
				if (weight > 0) {
					resultMap.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
				} else {
					resultMap.put(Globals.FIELD_WEIGHT, "0");
				}
				searchResult.add(resultMap);
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
		case SELECTSET_STATUS:
			selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE) };
			break;
		case SELECTSET_LIST:	// VOC 검색 결과
			selectSet = new SelectSet[] { 
					new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE),	//수집문서ID
					//new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE),	//제목
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
					new SelectSet(Globals.FIELD_CHANNEL),
					new SelectSet(Globals.FIELD_SITE),
					new SelectSet(Globals.FIELD_URL)
			};
			break;
		case SELECTSET_ALIKE:	// 유사문서
			selectSet = new SelectSet[]{
					new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE),		//수집문서ID
					new SelectSet(Globals.FIELD_TITLE, (byte)(Protocol.SelectSet.NONE)),	//제목
					new SelectSet(Globals.FIELD_CONTENT,  (byte)(Protocol.SelectSet.NONE)),		//본문
					new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE),		//등록일
					new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE),		//가중치
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
	private WhereSet[] createWhereSet(int flag, SocialChannelStatusVo vo, String channelName) {

		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();
		
		whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
		
		switch (flag) {
		case WHERESET_STATUS : // 채널별 현황 검색 조건 		
			if(!channelName.equals("")) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, channelName, 0));
			}
			break;
		case WHERESET_KEYWORDS:
		case WHERESET_PERIOD:
			if (!vo.getSocialSite().isEmpty() && !vo.getSocialSite().equals(Globals.COM_SELECT_ALL)) {	// 사이트명 
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_SITE, Protocol.WhereSet.OP_HASALL, vo.getSocialSite(), 0));
			}
			break;
		
		case WHERESET_SEARCHKEYWORD:	
			if(!channelName.equals("")) {	//소셜채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CHANNEL, Protocol.WhereSet.OP_HASALL, channelName, 0));
			}
			if(!vo.getSearchType().isEmpty() && vo.getSearchType().equals("rep")){	// 특정 사이트 검색
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_SITE, Protocol.WhereSet.OP_HASALL, vo.getSocialSite(), 1000));
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}else if(!vo.getSearchType().isEmpty() && vo.getSearchType().equals("keyword")){	// 특정 키워드 검색
				if (!vo.getSocialSite().isEmpty() && !vo.getSocialSite().equals(Globals.COM_SELECT_ALL)) {	// 사이트명 
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Globals.IDX_SITE, Protocol.WhereSet.OP_HASALL, vo.getSocialSite(), 0));
				}
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 1000));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 100));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 100));
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}
			break;
		case WHERESET_DETAIL :	// 상세보기
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, vo.getDq_docid(), 0));	//keyword로 수집문서ID 검색
			break;
		case WHERESET_ALIKE :	// 유사문서
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
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
	private FilterSet[] createFilterSet(SocialChannelStatusVo vo) {

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
	private GroupBySet[] createGroupBySet(int flag, SocialChannelStatusVo vo) { 

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupbysetList = new ArrayList<GroupBySet>();
		
		switch (flag) {
		case GROUPBYSET_STATUS:	//분야별 현황 - 사이트값
			groupbysetList.add(new GroupBySet(Globals.GROUP_SITE, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
			break;
		case GROUPBYSET_KEYWORDS:
			groupbysetList.add(new GroupBySet("KEYWORD", (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
			break;
		case GROUPBYSET_PERIOD:
			groupbysetList.add(new GroupBySet(vo.getCondition(), (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
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