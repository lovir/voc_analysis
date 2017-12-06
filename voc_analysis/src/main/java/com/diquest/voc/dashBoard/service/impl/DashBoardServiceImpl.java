package com.diquest.voc.dashBoard.service.impl;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.exception.IRException;
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
import com.diquest.ir.core.index.filter.FilterInfoService;
import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.BaseService;
import com.diquest.voc.common.vo.BaseVo;
import com.diquest.voc.dashBoard.service.DashBoardService;
import com.diquest.voc.dashBoard.vo.ContryMInwonTotalVO;
import com.diquest.voc.dashBoard.vo.CountryMinwonVO;
import com.diquest.voc.dashBoard.vo.DashBoardVo;
import com.diquest.voc.dashBoard.vo.IncreAndDecreVO;
import com.diquest.voc.dashBoard.vo.Top10totalVO;
import com.diquest.voc.dashBoard.vo.TopCategoryVO;
import com.diquest.voc.dashBoard.vo.TopKeywordVO;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.service.InterestKeywordService;
import com.diquest.voc.management.service.impl.CommonSelectBoxDAO;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.InterestKeywordVo;
import com.diquest.voc.trend.vo.TrendAnalysisVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("dashBoardService")
public class DashBoardServiceImpl extends BaseService implements DashBoardService{

	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;
	
	/** commonSelectBoxDAO */
	@Resource(name = "dashBoardDAO")
	private DashBoardDAO dashBoardDAO;
	
	private Gson gson = null;
	
	private static final boolean MARINER_LOGGABLE = true; // 검색엔진 로그 저장 여부
	private static final boolean MARINER_PRINT_QUERY = false; // 검색엔진 검색쿼리 출력 여부
	private static final boolean MARINER_DEBUG = true; // 검색엔진 디버그 사용 여부
	private static final boolean MARINER_PRITER = false; //개인정보보호 필터(PRITER) 사용 여부

	// SELECTSET 분기
	private static final int SELECTSET_COUNT = 100; // 통계치용 건수 조회용

	// WHERESET 분기
	private static final int WHERESET_ALL = 200; 	 	 // 총 통계 건수 조회
	//private static final int WHERESET_CALL_18 = 201; 	 // 콜센터 1~4 건수 조회
	//private static final int WHERESET_VOC_18 = 203;  	 // VOC 1~4 건수 조회
	private static final int WHERESET_CALL_14 = 201; 	 // 콜센터 1~4 건수 조회
	private static final int WHERESET_CALL_58 = 202; 	 // 콜센터 5~8 건수 건수 조회
	private static final int WHERESET_VOC_14 = 203;  	 // VOC 1~4 건수 조회
	private static final int WHERESET_VOC_58 = 204;  	 // VOC 5~8 건수 조회
	private static final int WHERESET_KEYWORD = 205;	 // 키워드 랭킹 조회
	private static final int WHERESET_CATEGORY = 206;	 // 카테고리 랭킹 조회
	private static final int WHERESET_INCREANDDECRE = 207;	// 긍부정
	private static final int WHERESET_ISSUE_MINWON = 208;	// 이슈키워드 민원 조회
	private static final int WHERESET_ISSUE_CALL = 209;		// 이슈키워드 콜 조회
	private static final int WHERESET_ISSUE_SOCIAL = 210;	// 이슈키워드 소셜 조회
	private static final int WHERESET_ISSUE_CLICK = 211;	// 이슈클라우드 클릭시 모달 조회
	
	// FILTERSET 분기
	private static final int FILTERSET_KIND = 300;  	// 유형별 총괄 현황
	private static final int FILTERSET_KEYWORD = 301;	// 키워드 랭킹 조회
	private static final int FILTERSET_CATEGORY = 302;	// 카테고리 랭킹 조회
	private static final int FILTERSET_INCREANDDECRE_PREV = 303;	// 긍부정 이전 조회
	private static final int FILTERSET_INCREANDDECRE_CURR = 304;	// 긍부정 현재 조회
	private static final int FILTERSET_ISSUE_PREV = 305;	// 이슈키워드 이전 조회
	private static final int FILTERSET_ISSUE_CURR = 306;	// 이슈키워드 현재 조회
	private static final int FILTERSET_ISSUE_CLICK = 307;	// 이슈클라우드 클릭시 모달 조회
	
	// GROUPBYSET 분기
	private static final int GROUPBYSET_KIND = 400; 	// 유형별 총괄 현황 CALL
	private static final int GROUPBYSET_KEYWORD = 401; 	// 키워드 랭킹 조회
	private static final int GROUPBYSET_CATEGORY = 402;	// 카테고리 랭킹 조회
	private static final int GROUPBYSET_INCREANDDECRE = 403;	// 긍부정
	private static final int GROUPBYSET_ISSUE = 404;
	private static final int GROUPBYSET_ISSUE_CLICK = 405;	// 이슈클라우드 클릭시 모달 조회
	
	@Override
	public String getKindChart(DashBoardVo vo) throws Exception{
		
		//int[] whereGubun = {WHERESET_ALL, WHERESET_CALL_14, WHERESET_VOC_18};
		int[] whereGubun = {WHERESET_ALL, WHERESET_CALL_14, WHERESET_CALL_58, WHERESET_VOC_14, WHERESET_VOC_58};
		// 콜센터, 민원 대분류 이름값 미리 구하기

		HashMap<String, Object>  kindChart = new HashMap<String, Object>();
		//List<HashMap<String, String>> nameList = dashBoardDAO.selectKind();
		ArrayList<HashMap<String, String>> allList = new ArrayList<HashMap<String, String>>();
		//ArrayList<HashMap<String, String>> call18List = new ArrayList<HashMap<String, String>>();
		//ArrayList<HashMap<String, String>> voc18List = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> call14List = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> call58List = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> voc14List = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> voc58List = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> countList = new ArrayList<HashMap<String, String>>();
		
		Result[] resultlist = null;
		
		QuerySet querySet = new QuerySet(5);	
		for(int i=0; i<5; i++){
			Query query = new Query();
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(whereGubun[i], vo)); // case를 위한 번호, 키워드, VO
			query.setFilter(createFilterSet(FILTERSET_KIND, vo));
			query.setGroupBy(createGroupBySet(GROUPBYSET_KIND, vo));
			query.setFrom(Globals.MARINER_COLLECTION);
			query.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
			query.setSearch(true);
			query.setUserName(vo.getLogin_Id()); //로그에 남길 키워드 명시
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
			//System.out.println("##### 유형별 차트 쿼리 \"" + queryParser.queryToString(query));
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
			for (int i = 0; i < resultlist.length; i++) {
				
				if (resultlist[i] != null && resultlist[i].getGroupResultSize() != 0) {
					GroupResult[] groupResultlist = resultlist[i].getGroupResults();
					if(i == 0){
						allList = parseList(groupResultlist);
					/*}else if(i == 1){
						call18List = parseList(groupResultlist);
					}else if(i == 2){
						voc18List = parseList(groupResultlist);
					}*/
					}else if(i == 1){
						call14List = parseList(groupResultlist);
					}else if(i == 2){
						call58List = parseList(groupResultlist);
					}else if(i == 3){
						voc14List = parseList(groupResultlist);
					}else if(i == 4){
						voc58List = parseList(groupResultlist);
					}	
				} 
			}
		}
		
		
		countList.add(makeResultList(allList));
		/*countList.add(makeResultList(call18List));
		countList.add(makeResultList(voc18List));
*/		
		countList.add(makeResultList(call14List));
		countList.add(makeResultList(call58List));
		countList.add(makeResultList(voc14List));
		countList.add(makeResultList(voc58List));
		
		//kindChart.put("nameList", nameList);
		kindChart.put("countList", countList);
		/*kindChart.put("call14List", call14List);
		kindChart.put("call58List", call58List);
		kindChart.put("voc14List", voc14List);
		kindChart.put("voc58List", voc58List);*/
		
		
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		/*System.out.println("-----------------------------------");
		System.out.println(gson.toJson(stationChartList));
		System.out.println("-----------------------------------");*/
		return gson.toJson(kindChart);
	}
	// 결과값 파싱 함수
	public HashMap<String, String> makeResultList(ArrayList<HashMap<String, String>> kindList){
		HashMap<String, String> totalMap = new HashMap<String, String>();
		// 단순문의: 문의상담 10, 문의 INQ
		// 불편개선: 불편건의 20, 불편 INC
		// 칭찬격려: 칭찬격려 30, 칭찬 PRA
		int counselTotal = 0;
		int claimTotal = 0;
		int goodTotal = 0;
		for(int i=0; i<kindList.size(); i++){
			if(kindList.get(i).get("CODE").equals("10") || kindList.get(i).get("CODE").equals("INQ")){			// 단순문의
				counselTotal += Integer.parseInt(kindList.get(i).get("COUNT"));
			}else if(kindList.get(i).get("CODE").equals("20") || kindList.get(i).get("CODE").equals("INC")){
				claimTotal += Integer.parseInt(kindList.get(i).get("COUNT"));
			}else if(kindList.get(i).get("CODE").equals("30") || kindList.get(i).get("CODE").equals("PRA")){
				goodTotal += Integer.parseInt(kindList.get(i).get("COUNT"));
			}
		}
		
		totalMap.put("TOTAL", Integer.toString(counselTotal+claimTotal+goodTotal));
		totalMap.put("COUNSEL", Integer.toString(counselTotal));
		totalMap.put("CLAIM", Integer.toString(claimTotal));
		totalMap.put("GOOD", Integer.toString(goodTotal));
		return totalMap;
	}
	// 그룹결과 파싱함수  (각각 나눠담아야 하기 때문)
	private ArrayList<HashMap<String, String>> parseList(GroupResult[] groupResultlist){
		
		ArrayList<HashMap<String, String>> returnList = new ArrayList<HashMap<String, String>>();
		if(groupResultlist[0] != null && groupResultlist[0].groupResultSize()>0){
			for(int j = 0; j < groupResultlist[0].groupResultSize(); j++){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("CODE", new String(groupResultlist[0].getId(j)).trim());
				map.put("COUNT", Integer.toString(groupResultlist[0].getIntValue(j)));
				returnList.add(map);
			}
			
		}
		return returnList;
	}

	// 종합 랭킹 분석 차트 구성
	public ArrayList<HashMap<String, String>> getTotalRanking(DashBoardVo vo) throws Exception {
		
		ArrayList<HashMap<String, String>> keywordList = new ArrayList<HashMap<String, String>>();
	
		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_KEYWORD, vo));
		query.setFilter(createFilterSet(FILTERSET_KEYWORD , vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_KEYWORD, vo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(vo.getLogin_Id()); // 로그에 남길 키워드 명시
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
			
			int size = 10;
			if(size > groupResultlist[0].groupResultSize())
				size = groupResultlist[0].groupResultSize();
			
			float max = 0;
			for (int i = 0; i < size; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				// 점유율 계산을 위해 1위를 기준으로 함
				if(i==0){
					map.put("share", Integer.toString(100));
					max = groupResultlist[0].getIntValue(i);
				}else{
					float value = groupResultlist[0].getIntValue(i);
					float share = (value / max) * 100;
					map.put("share", Integer.toString((int)share));
				}
				
				map.put("keyword", new String(groupResultlist[0].getId(i)));
				map.put("count", Integer.toString(groupResultlist[0].getIntValue(i)));
				
				keywordList.add(map);
			}
			
		}
		
		return keywordList;
	}
	// 종합 랭킹 분석 차트 구성
	public ArrayList<HashMap<String, String>> getCategoryRanking(DashBoardVo vo) throws Exception {
		
		ArrayList<HashMap<String, String>> keywordList = new ArrayList<HashMap<String, String>>();
	
		Result[] resultlist = null;
		Result result = null;

		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_CATEGORY, vo));
		query.setFilter(createFilterSet(FILTERSET_CATEGORY , vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_CATEGORY, vo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(vo.getLogin_Id()); // 로그에 남길 키워드 명시
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
			
			int size = 10;
			if(size > groupResultlist[0].groupResultSize())
				size = groupResultlist[0].groupResultSize();
			
			float max = 0;
			for (int i = 0; i < size; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				String keywordCode = new String(groupResultlist[0].getId(i));			
				if(keywordCode != null && !keywordCode.equals("")){
					// 점유율 계산을 위해 1위를 기준으로 함
					if(i==0){
						map.put("share", Integer.toString(100));
						max = groupResultlist[0].getIntValue(i);
					}else{
						float value = groupResultlist[0].getIntValue(i);
						float share = (value / max) * 100;
						map.put("share", Integer.toString((int)share));
					}
					
					HashMap<String, Object> itemMap = dashBoardDAO.selectItem(new String(groupResultlist[0].getId(i)));
					
					map.put("keyword", (String)itemMap.get("name"));				
					map.put("count", Integer.toString(groupResultlist[0].getIntValue(i)));
					
					keywordList.add(map);
				}
			}
			
		}
		
		return keywordList;
	}
	// 긍부정 현황
	public String getEmotionChart(DashBoardVo vo) throws Exception{
		
		QuerySet querySet = null;
		Result[] resultList = null;
		Result lastResult = null;
		Result thisResult = null;
		CommandSearchRequest command = null;
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			
		// 감정별로 쿼리 추가 
		querySet = new QuerySet(2);
		for(int j=0;  j < 2; j++){	// 날짜 쿼리
		
			Query query = new Query();
			query.setSelect(createSelectSet(SELECTSET_COUNT));
			query.setWhere(createWhereSet(WHERESET_INCREANDDECRE, vo));
			if(j == 0)
				query.setFilter(createFilterSet(FILTERSET_INCREANDDECRE_PREV , vo));
			else if(j == 1)
				query.setFilter(createFilterSet(FILTERSET_INCREANDDECRE_CURR , vo));
			
			query.setGroupBy(createGroupBySet(GROUPBYSET_INCREANDDECRE, vo));
			query.setFrom(Globals.MARINER_COLLECTION);
			query.setResult(0, 0);
			query.setSearch(true);
			query.setUserName(vo.getLogin_Id()); // 로그에 남길 키워드 명시
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
			//System.out.println("##### 긍부정 분석 query: " + queryParser.queryToString(query));
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
		}
		
		List<Object> needsList = new ArrayList<Object>();
				
		// 그룹결과에 아무것도 나오지 않을 경우를 대비해 맵형태로 일단 넣어둔다
		//ArrayList<HashMap<String, Integer>> emotionList = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> lastMap = new HashMap<String, Integer>();
		// 이전
		lastMap.put("긍정", 0);
		lastMap.put("부정", 0);
		lastMap.put("중립", 0);		
		//emotionList.add(map);
		HashMap<String, Integer> thisMap = new HashMap<String, Integer>();
		// 현재
		thisMap.put("긍정", 0);
		thisMap.put("부정", 0);
		thisMap.put("중립", 0);
		//emotionList.add(map2);
		
		if(resultList != null){
			for(int i=0; i<resultList.length; i++){
				// 0 : 이전 / 1: 현재
				GroupResult[] groupResults = resultList[i].getGroupResults();
									
				for(int j=0; j<groupResults[0].groupResultSize(); j++){					
					String name = new String(groupResults[0].getId(j));
					int count = groupResults[0].getIntValue(j);
					if(i == 0){
						// 긍정
						if(name.equals("긍정")){
							lastMap.put("긍정", count);
						}
						// 부정
						else if(name.equals("부정")){
							lastMap.put("부정", count);
						}
						// 중립
						else if(name.equals("중립")){
							lastMap.put("중립", count);
						}
					}else if(i == 1){
						// 긍정
						if(name.equals("긍정")){
							thisMap.put("긍정", count);
						}
						// 부정
						else if(name.equals("부정")){
							thisMap.put("부정", count);
						}
						// 중립
						else if(name.equals("중립")){
							thisMap.put("중립", count);
						}
					}
				}
			}
		}
		
		
		String[] nameArr = {"긍정", "중립", "부정"};
		String[] colorArr = {"#2f73c8", "#8eb252", "#fc8a29"};
		for(int i=0; i<3; i++){			
			int total = 0;
			float prev = 0.f;
			float current = 0.f;
			float percentage= 0.f;
			List<Object> list = new ArrayList<Object>(); // 3개 필요 : 긍정,중립,부정
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			for(int j=0; j<2; j++){	// 0  : 전주 / 1 : 금주
				
				HashMap<String, Object> obj = new HashMap<String, Object>();
				if(vo.getCondition().equals("DAY")){
					if(j == 0){
						obj.put("name", Globals.COM_DASH_PREV_DAY);
						obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
						obj.put("y", lastMap.get(nameArr[i])); 
						prev = lastMap.get(nameArr[i]);
					}else if(j == 1){						
						obj.put("name", Globals.COM_DASH_CURRENT_DAY);
						obj.put("color", colorArr[i]); // 금주 or 당월 차트색
						obj.put("y", thisMap.get(nameArr[i])); 
						current = thisMap.get(nameArr[i]);
					}
				
				}else if(vo.getCondition().equals("WEEK")){
					if(j == 0){
						obj.put("name", Globals.COM_DASH_PREV_WEEK);		
						obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
						obj.put("y", lastMap.get(nameArr[i])); 
						prev = lastMap.get(nameArr[i]);
					}else if(j == 1){
						obj.put("name", Globals.COM_DASH_CURRENT_WEEK);	
						obj.put("color", colorArr[i]); // 금주 or 당월 차트색
						obj.put("y", thisMap.get(nameArr[i])); 
						current = thisMap.get(nameArr[i]);
					}
				}else{
					if(j == 0){
						obj.put("name", Globals.COM_DASH_PREV_MONTHS);		
						obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
						obj.put("y", lastMap.get(nameArr[i])); 
						prev = lastMap.get(nameArr[i]);
					}else if(j == 1){
						obj.put("name", Globals.COM_DASH_CURRENT_MONTHS);	
						obj.put("color", colorArr[i]); // 금주 or 당월 차트색
						obj.put("y", thisMap.get(nameArr[i])); 
						current = thisMap.get(nameArr[i]);
					}
				}
				obj.put("gubun", nameArr[i]);
				list.add(obj);
				
			}
			total = (int)(prev);
			if(total>0){
				percentage = 100-((current/total)*100);
			}			
			if(Float.isNaN(percentage)){
				percentage = 0;
			}
		
			map.put("percentage", String.format("%.1f", Math.abs(percentage))); // 점유율
			map.put("count", Math.round(current-prev)); //건수
			map.put("current", comma_add(Integer.toString(Math.round(current)))); //금주 or 당월
			map.put("prev", comma_add(Integer.toString(Math.round(prev)))); //전주 or 전월
			map.put("data", list); // 차트 표시용 데이터(json형식)
			needsList.add(map);
		}

		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		return gson.toJson(needsList);
		//return null;
	}
	public String comma_add(String num){   //콤마 만들기 위한 메소드
	  String original = num;
	  String convert = "";
	  int count = 1;
	  for(int k=original.length()-1; k>-1; k--){   
	   if( (count%3) == 0 && k < original.length()-1 && k > 0){
	    convert = "," +original.charAt(k)  + convert;
	   }
	   else{
	    convert = original.charAt(k) + convert;
	   }
	   count++;
	  }
	  return convert;
	 }
	
	/**
	 * 이슈키워드 분석
	 * 
	 * @param EmotionVo
	 *            - 조회할 정보가 담긴 VO
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getWordCloudChart(DashBoardVo vo) throws Exception {
		//???이슈키워드 랭킹에서 리포트 차트는 금주의 빈도수 순으로 정렬
		//하단 이슈키워드랭킹은 순위 많이 오른 순서로 정렬 해야 된다.
		//소스 로직 검토 필요.
		LinkedHashMap<String, Object> totalResultMap = new LinkedHashMap<String, Object>(); // 최종 리턴 맵
		//String[] iKeywordArr = keywordRankingVo.getInterestKeywordArr(); // 관심키워드
		
		
		LinkedList<HashMap<String,Object>> totalPeriodKeywordMapList = new LinkedList<HashMap<String,Object>>();	//전체 기간 동안의 키워드 빈도수 저장 리스트
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
			
		Result[] resultlist = null;
		Result lastResult = null;
		Result thisResult = null;
			
		int[] gubunArr = {WHERESET_ISSUE_MINWON, WHERESET_ISSUE_CALL, WHERESET_ISSUE_SOCIAL};
		int[] dateGubunArr = {FILTERSET_ISSUE_PREV, FILTERSET_ISSUE_CURR};
		
		//VOC 민원(MINWON), 콜센터(CALL) + SMS(SMS), 소셜
		for(int i = 0 ; i < 3 ; i++){
			QuerySet querySet = new QuerySet(2);
			// - 전주(전월,전년) , 금주(금월,금년) 만 비교하므로 쿼리 2개
			for (int j = 0; j < 2; j++) {
				
				Query query = new Query();
				query.setSelect(createSelectSet(SELECTSET_COUNT));	
				query.setWhere(createWhereSet(gubunArr[i], vo));					// 민원, 콜센터, 소셜			
				query.setFilter(createFilterSet(dateGubunArr[j], vo));				// 이전, 현재
				query.setGroupBy(createGroupBySet(GROUPBYSET_ISSUE, vo));
				if(i == 2)
					query.setFrom(Globals.MARINER_COLLECTION2);
				else 
					query.setFrom(Globals.MARINER_COLLECTION);
				query.setResult(0, 0);
				query.setSearch(true);
				query.setUserName(vo.getLogin_Id()); // 로그에 남길 키워드 명시
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
			// - 검색 서버로 검색 정보 전송
			HashMap<String, Integer> lastKeywordRanking = new HashMap<String, Integer>(); // 이전 키워드 값

			// - 이전주 값 세팅
			if (lastResult != null && lastResult.getGroupResultSize() != 0) {
				GroupResult[] groupResultlist = lastResult.getGroupResults();
				for (int j = 0; j < groupResultlist[0].groupResultSize(); j++) {
					lastKeywordRanking.put(new String(groupResultlist[0].getId(j)).trim(), groupResultlist[0].getIntValue(j));
				}
			}
			ArrayList<HashMap<String, Object>> periodRankingList = new ArrayList<HashMap<String, Object>>();
			
			// - 금주 키워드 갯수만큼 돌면서 이슈키워드 추출
			if (thisResult != null && thisResult.getGroupResultSize() != 0) {
				GroupResult[] groupResultlist = thisResult.getGroupResults();
				for (int j = 0; j < groupResultlist[0].groupResultSize(); j++) {
					if (lastKeywordRanking.get(new String(groupResultlist[0].getId(j)).trim()) != null) {
						int lastWeight = lastKeywordRanking.get(new String(groupResultlist[0].getId(j)).trim());
						int thisWeight = groupResultlist[0].getIntValue(j);
						if (thisWeight > lastWeight) {
							int change = thisWeight - lastWeight; // - 금주데이터 가중치 (-) 지난데이터 가중치
							String keyword = new String(groupResultlist[0].getId(j)).trim();
							HashMap<String, Object> keywordMap = new HashMap<String, Object>();
							keywordMap.put("keyword", keyword); // - 이슈키워드 값
							keywordMap.put("count", change);
							periodRankingList.add(keywordMap);

						}
					}
				}
			}
			
			if (periodRankingList.size() > 0) {
				// - 값이 없으면 끝낸다
			/*	totalResultMap = new LinkedHashMap<String, Object>();
				return totalResultMap;
			}*/

				// - periodRankingList HashMap List 숫자 내림차순 정렬
				// - 관심키워드의 등록에 따라 순서대 periodRankingList에 담겨 있으나 실질적으론 nowrank 값이 실제 랭킹이다.
				// - 그래서 nowrank 의값에 따라 정렬 해줘야 함.
				Collections.sort(periodRankingList, new Comparator<HashMap<String, Object>>() {
					public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
						int one = Integer.parseInt(o1.get("count").toString());
						int two = Integer.parseInt(o2.get("count").toString());
						return one > two ? -1 : one < two ? 1 : 0;
					}
				});
				
				//// 각각 색깔 맞추기 
				int len = 10;
				if(len > periodRankingList.size()){
					len  = periodRankingList.size();
				}
						
				
				for(int j=0; j<len; j++){
					String tempKeyword = (String) periodRankingList.get(j).get("keyword");
					HashMap<String, Object> totalPeriodKeywordMap = new HashMap<String, Object>();
					////////////// 민원 
					if( i==0 ){				
						if(tempKeyword != null && !"".equals(tempKeyword.trim())){
							totalPeriodKeywordMap.put("text", tempKeyword);
							totalPeriodKeywordMap.put("weight", periodRankingList.get(j).get("count"));
							totalPeriodKeywordMap.put("color", "#DD2222");
						}
						//totalPeriodKeywordMap.put("link", "javascript:issueCloudClick('"+tempKeyword+"', 'MINWON')");
						totalPeriodKeywordMap.put("link", "#");
						totalPeriodKeywordMapList.add(totalPeriodKeywordMap);
					}
					////////////// 콜
					else if( i==1 ){
						if(tempKeyword != null && !"".equals(tempKeyword.trim())){
							totalPeriodKeywordMap.put("text", tempKeyword);
							totalPeriodKeywordMap.put("weight", periodRankingList.get(j).get("count"));
							totalPeriodKeywordMap.put("color", "#f49418");
						}
						totalPeriodKeywordMap.put("link", "#");
						//totalPeriodKeywordMap.put("link", "javascript:issueCloudClick('"+tempKeyword+"', 'CALL')");
						totalPeriodKeywordMapList.add(totalPeriodKeywordMap);
					}
					////////////// 소셜
					else if( i==2 ){
						if(tempKeyword != null && !"".equals(tempKeyword.trim())){
							totalPeriodKeywordMap.put("text", tempKeyword);
							//소셜 쪽 키워드 빈도수 조작 - 소셜의 키워드 수가 절대 수치가 너무 높아 1/100 한 값으로 전달되게 수정
							int socialWeight = (int) periodRankingList.get(j).get("count");
							double finalSocialWeight = (double)((float)socialWeight / 100);
							totalPeriodKeywordMap.put("weight", finalSocialWeight);
							totalPeriodKeywordMap.put("color", "#2267DD");
						}
						totalPeriodKeywordMap.put("link", "#");
						//totalPeriodKeywordMap.put("link", "javascript:issueCloudClick('"+tempKeyword+"', 'SOCIAL')");
						totalPeriodKeywordMapList.add(totalPeriodKeywordMap);
					}
				}
			}
		}
		returnMap.put("finalWordMapList", totalPeriodKeywordMapList);

		return returnMap;
	}
	// 이슈 클라우드 클릭시 리포트와 건수/점유율
	@Override
	public String issueWordCloudClick(DashBoardVo vo) throws Exception {
				
		HashMap<String, Object> chart = new HashMap<String, Object>();		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();		
		LinkedHashMap<String, String> categoriesMap = getCategories(vo); // x축		
		QuerySet querySet = new QuerySet(1);	
								
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_ISSUE_CLICK, vo)); // case를 위한 번호, 키워드, VO
		query.setFilter(createFilterSet(FILTERSET_ISSUE_CLICK, vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_ISSUE_CLICK, vo));
		if(vo.getChannel().equals("SOCIAL")){
			query.setFrom(Globals.MARINER_COLLECTION2);
		}else{
			query.setFrom(Globals.MARINER_COLLECTION);
		}
		query.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
		query.setSearch(true);
		query.setUserName(vo.getLogin_Id()); //로그에 남길 키워드 명시
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
		//System.out.println("##### 차트 : " + queryParser.queryToString(query));
			
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		Result[] resultList = null;
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();

		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
		}
		
		LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
		HashMap<String, Object> seriesMap = new HashMap<String, Object>();
		
		
		// x축 표시 정보
		for(String categories : categoriesMap.keySet()){
			dataMap.put(categories, 0);
		}
		
		Object prevDateStr = "";
		Object currDateStr = "";
		
		
		Iterator iterator = dataMap.entrySet().iterator();
		int len = categoriesMap.keySet().size()-2;
		int i = 0;
		while (iterator.hasNext()) {
			Entry entry = (Entry)iterator.next();
			if(i == len){
				prevDateStr = entry.getKey();
			}else if(i == len+1){
				currDateStr = entry.getKey();
			}
			
			i++;
		}
			
		float prevCnt = 0;
		float currCnt = 0;
		int totalCnt = 0;
		float share = 0;
		if(resultList[0].getGroupResultSize() != 0){
			GroupResult[] groupResults = resultList[0].getGroupResults();
			
			int rSize = groupResults[0].groupResultSize();
			for (int k = 0; k < rSize; k++) {
				dataMap.put(new String(groupResults[0].getId(k)), groupResults[0].getIntValue(k));
				
				if(new String(groupResults[0].getId(k)).equals(prevDateStr)){
					prevCnt = groupResults[0].getIntValue(k);
					
				}else if(new String(groupResults[0].getId(k)).equals(currDateStr)){
					currCnt = groupResults[0].getIntValue(k);
				}
				
				totalCnt += groupResults[0].getIntValue(k);
			}
			
			share = (currCnt/totalCnt)*100;
			
			if(Float.isNaN(share)){
				share = 0;
			}
			seriesMap.put("name", vo.getKeyword());
			seriesMap.put("type", "line");
			seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
			
		}
		List<Object> list = new ArrayList<Object>();
		list.add(seriesMap);
		chart.put("currCnt", currCnt);
		chart.put("prevCnt", prevCnt);
		chart.put("share", (int)share);
		chart.put("keyword", vo.getKeyword());
		chart.put("series", list);
		chart.put("categories", categoriesMap.values());
		
		return gson.toJson(chart);
		
	}
	//리포트 차트 x축(기간) 출력
	public LinkedHashMap<String, String> getCategories(DashBoardVo vo){

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
	private WhereSet[] createWhereSet(int flag, DashBoardVo vo) {

		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();
		// WHERESET_ALL, WHERESET_CALL_14, WHERESET_CALL_58, WHERESET_VOC_14, WHERESET_VOC_58
		/* 콜센터 
		문의 - INQ
		불편 - INC
		칭찬 - PRA
		* 민원
		문의상담 - 10
		불편건의 - 20
		칭찬격려 = 30
		*/
		switch (flag) {
		case WHERESET_ALL : 	// 유형별 총괄 현황 전체 
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "10", 0));	// 대분류 
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "20", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "30", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "INQ", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "INC", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "PRA", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "01", 0));			// 노선 1~8호선
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "02", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "03", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "04", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "05", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "06", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "07", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "08", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			break;
		case WHERESET_CALL_14 : // 유형별 총괄 현황 콜센터 1~4호선 
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "INQ", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "INC", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "PRA", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "01", 0));			// 노선 1~4호선
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "02", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "03", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "04", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			break;	
		case WHERESET_CALL_58 : // 유형별 총괄 현황 콜센터 5~8호선 
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "INQ", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "INC", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "PRA", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "05", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "06", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "07", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "08", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			break;	
		case WHERESET_VOC_14 : // 유형별 총괄 현황 콜센터 1~4호선 
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "10", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "20", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "30", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "01", 0));			// 노선 1~4호선
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "02", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "03", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "04", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			break;	
		case WHERESET_VOC_58 : // 유형별 총괄 현황 콜센터 5~8호선 
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "10", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "20", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, "30", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "05", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "06", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "07", 0));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "08", 0));
			whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			break;	
		case WHERESET_KEYWORD:
		case WHERESET_CATEGORY:
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			break;
		case WHERESET_INCREANDDECRE:
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	//전체검색용
			break;
		case WHERESET_ISSUE_MINWON:		// 이슈키워드 민원 검색
			whereSetList.add(new WhereSet(Globals.IDX_VOC_CATEGORY, Protocol.WhereSet.OP_HASALL, "MINWON", 100));
			break;
		case WHERESET_ISSUE_CALL:		// 이슈키워드 CALL 검색 = call+sms
			whereSetList.add(new WhereSet(Globals.IDX_VOC_CATEGORY, Protocol.WhereSet.OP_HASALL, "CALL", 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_VOC_CATEGORY, Protocol.WhereSet.OP_HASALL, "SMS", 100));
			break;
		case WHERESET_ISSUE_SOCIAL:		// 소셜 키워드 검색 (전체)
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	
			break;
		case WHERESET_ISSUE_CLICK:
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0));	
			if(vo.getKeyword() != null && !vo.getKeyword().equals("")){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 1000));
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}
			if(vo.getChannel() != null && !vo.getChannel().equals("")){
				if(vo.getChannel().equals("MINWON")){
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Globals.IDX_VOC_CATEGORY, Protocol.WhereSet.OP_HASALL, "MINWON", 100));
				}else if(vo.getChannel().equals("CALL")){
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereSetList.add(new WhereSet(Globals.IDX_VOC_CATEGORY, Protocol.WhereSet.OP_HASALL, "CALL", 100));
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereSetList.add(new WhereSet(Globals.IDX_VOC_CATEGORY, Protocol.WhereSet.OP_HASALL, "SMS", 100));
					whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				}
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
	private FilterSet[] createFilterSet(int flag, DashBoardVo vo) {

		String startDate = vo.getStartDate().replace("/", "") + "000000";
		String endDate = vo.getEndDate().replace("/", "") + "235959";

		FilterSet[] filterSet = null;
		ArrayList<FilterSet> filterlist = new ArrayList<FilterSet>();
		
		
		switch (flag) {
		case FILTERSET_KIND:	//유형별 현황 분석용
		case FILTERSET_KEYWORD:		//키워드랭킹 분석용
		case FILTERSET_CATEGORY:	//카테고리 랭킹 분석용
		case FILTERSET_INCREANDDECRE_CURR:
		case FILTERSET_ISSUE_CURR:	// 이슈키워드 현재
		case FILTERSET_ISSUE_CLICK: // 이슈키워드 클릭 시
			filterlist.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "REGDATE", new String[] { startDate, endDate }));	//날짜 필터링
			break;
		case FILTERSET_ISSUE_PREV:	// 이슈키워드 이전
		case FILTERSET_INCREANDDECRE_PREV:
			startDate = vo.getPrevStartDate().replace("/", "") + "000000";
			endDate = vo.getPrevEndDate().replace("/", "") + "235959";
			filterlist.add(new FilterSet(Protocol.FilterSet.OP_RANGE, "REGDATE", new String[] { startDate, endDate }));	//날짜 필터링
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
	/**
	 * GroupBySet 설정.
	 * 
	 * @return GroupBySet[]
	 */
	private GroupBySet[] createGroupBySet(int flag, DashBoardVo vo) { //flag=유형, option=그룹바이셋 옵션

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupbysetList = new ArrayList<GroupBySet>();
		
		switch (flag) {
		case GROUPBYSET_KIND:	
			groupbysetList.add(new GroupBySet(Globals.GROUP_CDVOCKIND, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
			break;
		case GROUPBYSET_KEYWORD:	//종합키워드랭킹 분석용
		case GROUPBYSET_ISSUE:
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
			break;
		case GROUPBYSET_CATEGORY:	//종합카테고리랭킹 분석용
			groupbysetList.add(new GroupBySet(Globals.GROUP_CDVOCITEM, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
			break;
		case GROUPBYSET_INCREANDDECRE:
			groupbysetList.add(new GroupBySet(Globals.GROUP_PNN, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
			break;
		case GROUPBYSET_ISSUE_CLICK:	// 트랜드 차트 
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
	
}
	/*
	@Override
	public ArrayList<HashMap<String, String>> dashBoardIssueText(DashBoardVo vo) throws Exception{
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		ArrayList<HashMap<String, String>> textList= new ArrayList<HashMap<String, String>>();
		
		queryList.add(createQuery(Globals.DASH_VOC_CALLTEXT, vo, ""));
		
		// QuerySet 인스턴스화
		querySet = new QuerySet(1);
		querySet.addQuery((Query) queryList.get(0));
	

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
		
		if(resultList[0] != null){
			for(int i=0; i<resultList[0].getRealSize(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ID", new String(resultList[0].getResult(i, 0)));
				map.put("TITLE", new String(resultList[0].getResult(i, 1)));
				map.put("CONTENT", new String(resultList[0].getResult(i, 2)));
				map.put("CUSTOMER_NAME", new String(resultList[0].getResult(i, 3)));
				map.put("REGDATE", new String(resultList[0].getResult(i, 4)));
				map.put("CALL_META_ID", new String(resultList[0].getResult(i, 5)));
				textList.add(map);
			}
		}
		
		return textList;
	}
	
	@Override
	public List<Object> dashBoardNeedsReport(DashBoardVo vo) throws Exception{
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		List<CommonSelectBoxVo> needsTypeList = commonSelectBoxService.needsTypeList();//01 고객문의, 02 고객제안,03 친절칭찬, 04 불편불만

		for(CommonSelectBoxVo needs : needsTypeList){
			queryList.add(createQuery(Globals.DASH_VOC_NEEDS, vo, needs.getCdTp()));
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
		
		List<Object> needsList = new ArrayList<Object>();
		
		for (Result result : resultList) {
			GroupResult[] groupResults = null;
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			if(result.getGroupResultSize() != 0){
				List<Object> list = new ArrayList<Object>();
				groupResults = result.getGroupResults();
				
				int total = 0;
				float prev = 0.f;
				float current = 0.f;
				float percentage= 0.f;
				
				for (int i = 0; i < groupResults.length; i++) {
					int rSize = groupResults[i].groupResultSize();
					if(rSize == 0) {
						HashMap<String, Object> obj = new HashMap<String, Object>();
						obj.put("name", "전월/당월");
						obj.put("y", 99999999);
						obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
						list.add(obj);
					} else {	
						for (int j = 0; j < rSize; j++) {
							HashMap<String, Object> obj = new HashMap<String, Object>();
							String id = new String(groupResults[i].getId(j));
							if(vo.getCondition().equals(Globals.FIELD_WEEK)){
								if(vo.getCurrent().equals(id)){
									obj.put("name", Globals.COM_DASH_CURRENT_WEEK);
								}else{
									obj.put("name", Globals.COM_DASH_PREV_WEEK);
								}
							}else{
								if(vo.getCurrent().equals(id)){
									obj.put("name", Globals.COM_DASH_CURRENT_MONTHS);
								}else{
									obj.put("name", Globals.COM_DASH_PREV_MONTHS);
								}
							}
							
							obj.put("y", groupResults[i].getIntValue(j));
							if(vo.getCurrent().equals(new String(groupResults[i].getId(j)))){
								current = groupResults[i].getIntValue(j); // 금주 or 당월
								obj.put("color", Globals.COM_DASH_VOC_COLOR[needsList.size()]); // 금주 or 당월 차트색
								
							}else{
								prev = groupResults[i].getIntValue(j); // 전주 or 전월
								obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
							}
							//total += groupResults[i].getIntValue(j);
							total = (int)prev;
							list.add(obj);
						}
					}
					
					if(total>0){
						percentage = 100-((current/total)*100);
					}
					
					if(Float.isNaN(percentage)){
						percentage = 0;
					}
				}

				map.put("percentage", String.format("%.1f", percentage)); // 점유율
				map.put("count", Math.round(current-prev)); //건수
				map.put("current", Math.round(current)); //금주 or 당월
				map.put("prev", Math.round(prev)); //전주 or 전월
				map.put("data", gson.toJson(list)); // 차트 표시용 데이터(json형식)
			}else{
				map.put("percentage", "0"); // 점유율
				map.put("count", 0); //건수
				map.put("current", 0); //금주 or 당월
				map.put("prev", 0); //전주 or 전월
				map.put("data", gson.toJson("")); // 차트 표시용 데이터(json형식)
			}
			needsList.add(map);
		}
		
		//needsMap.put("needs", needsList);
		
		return needsList;  
	}
	
	@Override
	public List<Object> dashBoardIncreAnDecre(DashBoardVo vo) throws Exception{
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		String [] emotion = {"P", "N", "D"}; // P:긍정, N:중립, D:부정
		for(int i=0;i<emotion.length;i++){
			queryList.add(createQuery(Globals.DASH_VOC_INCREANDDECRE, vo, emotion[i]));
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
		
		List<Object> needsList = new ArrayList<Object>();
		
		for (Result result : resultList) {
			
			GroupResult[] groupResults = null;
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			if(result.getGroupResultSize() != 0){
				List<Object> list = new ArrayList<Object>();
				groupResults = result.getGroupResults();
				
				int total = 0;
				float prev = 0.f;
				float current = 0.f;
				float percentage= 0.f;
				for (int i = 0; i < groupResults.length; i++) {
					
					int rSize = groupResults[i].groupResultSize();
					if(rSize == 0) {
						
						HashMap<String, Object> obj = new HashMap<String, Object>();
						obj.put("name", "전월/당월");
						obj.put("y", 99999999);
						obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
						list.add(obj);
					} else {
						
						for (int j = 0; j < rSize; j++) {
							
							HashMap<String, Object> obj = new HashMap<String, Object>();
							String id = new String(groupResults[i].getId(j));
							
							if(vo.getCondition().equals(Globals.FIELD_WEEK)){
								if(vo.getCurrent().equals(id)){
									obj.put("name", Globals.COM_DASH_CURRENT_WEEK);
								}else{
									obj.put("name", Globals.COM_DASH_PREV_WEEK);
								}
							}else{
								if(vo.getCurrent().equals(id)){
									obj.put("name", Globals.COM_DASH_CURRENT_MONTHS);
								}else{
									obj.put("name", Globals.COM_DASH_PREV_MONTHS);
								}
							}
							
							obj.put("y", groupResults[i].getIntValue(j));
							//obj.put("y", comma_add(Integer.toString(groupResults[i].getIntValue(j))));
							if(vo.getCurrent().equals(new String(groupResults[i].getId(j)))){
								current = groupResults[i].getIntValue(j); // 금주 or 당월
								obj.put("color", Globals.COM_DASH_VOC_COLOR[needsList.size()]); // 금주 or 당월 차트색
								
							}else{
								prev = groupResults[i].getIntValue(j); // 전주 or 전월
								obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
							}
							//total += groupResults[i].getIntValue(j);
							total = (int)prev;
							list.add(obj);
						}
					}
					
					if(total>0){
						percentage = 100-((current/total)*100);
					}
					
					if(Float.isNaN(percentage)){
						percentage = 0;
					}
				}

				map.put("percentage", String.format("%.1f", Math.abs(percentage))); // 점유율
				map.put("count", Math.round(current-prev)); //건수
				map.put("current", comma_add(Integer.toString(Math.round(current)))); //금주 or 당월
				map.put("prev", comma_add(Integer.toString(Math.round(prev)))); //전주 or 전월
				map.put("data", gson.toJson(list)); // 차트 표시용 데이터(json형식)
			}else{
				map.put("percentage", "0"); // 점유율
				map.put("count", 0); //건수
				map.put("current", 0); //금주 or 당월
				map.put("prev", 0); //전주 or 전월
				map.put("data", gson.toJson("")); // 차트 표시용 데이터(json형식)
			}
			needsList.add(map);
		}
		
		//needsMap.put("needs", needsList);
		
		return needsList;
	}
	
	@Override
	public ContryMInwonTotalVO dashBoardCountryMinwon(DashBoardVo vo) throws Exception{
		List<CountryMinwonVO> cmlist = new ArrayList<CountryMinwonVO>();
		ContryMInwonTotalVO cmtvo = new ContryMInwonTotalVO();
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		//gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		//String [] countrySc = {"강화군","서구","계양구","부평구","남동구","남구","연수구","중구","동구","옹진군"};
		//String [] contryEgSc = {"Ganghwa-gun","Seo-gu","Gyeyang-gu","Bupyeong-gu","Namdong-gu","Nam-gu","Yeongsu-gu","Jung-gu","Dong-gu","Ongjin-gun"};
		String [] countrySc = {"은평구","서대문구","종로구","성북구","강북구"
				,"도봉구","노원구","중랑구","동대문구","중구"
				,"성동구","광진구","강동구","마포구","용산구"
				,"강서구","양천구","구로구","금천구","영등포구"
				,"관악구","동작구","서초구","강남구","송파구" };
		String [] contryEgSc = {"Ganghwa-gun","Seo-gu","Gyeyang-gu","Bupyeong-gu","Namdong-gu"
				,"Nam-gu","Yeongsu-gu","Jung-gu","Dong-gu","Ongjin-gun"
				,"Nam-gu","Yeongsu-gu","Jung-gu","Dong-gu","Ongjin-gun"
				,"Nam-gu","Yeongsu-gu","Jung-gu","Dong-gu","Ongjin-gun"
				,"Nam-gu","Yeongsu-gu","Jung-gu","Dong-gu"};
		for(int j = 0;j<countrySc.length;j++){
			queryList.add(createQuery(Globals.DASH_VOC_COUNTRYMINWON, vo, countrySc[j]));			
		}
		
		// QuerySet 인스턴스화
		querySet = new QuerySet(queryList.size());
		
		int p = 0; // 각 지역의 민원긍정평가의 값이 들어갈 int
		int n = 0; // 각 지역의 민원중립평가의 값이 들어갈 int
		int d = 0; // 각 지역의 민원부정평가의 값이 들어갈 int
		int t = 0; // 각 지역의 총민원개수가 들어갈 int
		
		int totalT=0; // 모든 지역의 총민원개수를 합한 값이 들어갈 int
		int totalP=0; // 모든 지역의 총 민원긍정평가의 값이 들어갈 int
		int totalN=0; // 모든 지역의 총 민원중립평가의 값이 들어갈 int
		int totalD=0; // 모든 지역의 총 민원부정평가의 값이 들어갈 int

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
		int ap = 0;
		try{

			for (Result result : resultList) {

				GroupResult[] groupResults = null;
							
				if(result.getGroupResultSize() != 0){
					
					groupResults = result.getGroupResults();

					for (int i = 0; i < groupResults.length; i++) {

						int rSize = groupResults[i].groupResultSize();

						//if(rSize>0){
							
							CountryMinwonVO cmvo = new CountryMinwonVO();
                           for(int j = 0;j<rSize;j++){
                        	   
                        	  
                        	   if(new String(groupResults[i].getId(j)).equals("N")){
                        		   n = groupResults[i].getIntValue(j);
                        	   }
                        	   if(new String(groupResults[i].getId(j)).equals("D")){
                        		   d = groupResults[i].getIntValue(j);								
                        	   }
                        	   if(new String(groupResults[i].getId(j)).equals("P")){
                        		   p = groupResults[i].getIntValue(j);								
                        	   }
                        	   
                        	   t = p+n+d;
						}
                           cmvo.setTotal(comma_add(Integer.toString(t)));
                           double positibleRate = (double)p/t*100; // 퍼센트화
                           int positibleRateBan = ((int)positibleRate+5)/10*10; // 반올림
                           String positibleRate3 = Integer.toString(positibleRateBan);
                           cmvo.setPositive("blue_"+positibleRate3);
                           
                           double negativeRate = (double)d/t*100;
                           int negativeRateBan = ((int)negativeRate+5)/10*10;
                           String negativeRate3 = Integer.toString(negativeRateBan);
                           cmvo.setNetative("orange_"+negativeRate3);
                           
                           double netralRate = (double)n/t*100;
                           int netralRateBan = ((int)netralRate+5)/10*10;
                           String netralRate3 = Integer.toString(netralRateBan);
                           cmvo.setNeutral("green_"+netralRate3);
                           
                           cmvo.setPositiveNum(comma_add(Integer.toString(p)));
                           cmvo.setNeutralNum(comma_add(Integer.toString(n)));
                           cmvo.setNetativeNum(comma_add(Integer.toString(d)));
                           cmvo.setCountryName(countrySc[ap]);
                          // cmvo.setCountryEgName(contryEgSc[ap]);
                           totalP=totalP+p;
                           totalD=totalD+d;
                           totalN=totalN+n;
                           totalT=totalT+t;
                           
                           
                           
                           cmlist.add(cmvo);
                          // if(ap<10){     	                         
                        	   ap=ap+1;  
                          // }
					//}// j의 for문		
					}
				}
				p=0; // 한번 회전 후 변수를 초기화
				d=0;
				n=0;
			}
			
		}catch(Exception e){
			//System.out.println("오류 확인 : "+e);
		}

		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");
		java.util.Date date = format.parse(vo.getEndDate());
		java.text.SimpleDateFormat format2 = new java.text.SimpleDateFormat("yyyy년M월d일");
		String date2 = format2.format(date);
		cmtvo.setNowdate(date2);
		cmtvo.setCmlist(cmlist);
		cmtvo.setTotalNega(comma_add(Integer.toString(totalD)));
		cmtvo.setTotalNetu(comma_add(Integer.toString(totalN)));
		cmtvo.setTotalPosi(comma_add(Integer.toString(totalP)));
		cmtvo.setTotalT(comma_add(Integer.toString(totalT)));
		return cmtvo;
	}
	
	@Override
	public List<Object> dashBoardLclsReport(DashBoardVo vo) throws Exception{
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		List<CommonSelectBoxVo> characterTypeList = commonSelectBoxService.characterTypeList();
		for(CommonSelectBoxVo character : characterTypeList){
			queryList.add(createQuery(Globals.DASH_VOE_LCLS, vo, character.getLcls()));
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
		List<Object> lclsList = new ArrayList<Object>();
		
		for (Result result : resultList) {
			GroupResult[] groupResults = null;
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			if(result.getGroupResultSize() != 0){
				List<Object> list = new ArrayList<Object>();
				groupResults = result.getGroupResults();
				
				int total = 0;
				float prev = 0.f;
				float current = 0.f;
				float percentage= 0.f;
				
				for (int i = 0; i < groupResults.length; i++) {
					
					int rSize = groupResults[i].groupResultSize();
					if(rSize == 0) {
						HashMap<String, Object> obj = new HashMap<String, Object>();
						obj.put("name", "전월/당월");
						obj.put("y", 99999999);
						obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
						list.add(obj);
					} else {	
						for (int j = 0; j < rSize; j++) {
							HashMap<String, Object> obj = new HashMap<String, Object>();
							String id = new String(groupResults[i].getId(j));
							if(vo.getCondition().equals(Globals.FIELD_WEEK)){
								if(vo.getCurrent().equals(id)){
									obj.put("name", Globals.COM_DASH_CURRENT_WEEK);
								}else{
									obj.put("name", Globals.COM_DASH_PREV_WEEK);
								}
							}else{
								if(vo.getCurrent().equals(id)){
									obj.put("name", Globals.COM_DASH_CURRENT_MONTHS);
								}else{
									obj.put("name", Globals.COM_DASH_PREV_MONTHS);
								}
							}
							
							obj.put("y", groupResults[i].getIntValue(j));
							if(vo.getCurrent().equals(new String(groupResults[i].getId(j)))){
								current = groupResults[i].getIntValue(j); // 금주 or 당월
								obj.put("color", Globals.COM_DASH_VOE_COLOR[lclsList.size()]); // 금주 or 당월 차트색
							}else{
								prev = groupResults[i].getIntValue(j); // 전주 or 전월
								obj.put("color", "#c3c3c3"); // 전주 or 전월 차트색
							}
							//total += groupResults[i].getIntValue(j);
							total = (int)prev;
							list.add(obj);
						}
					}
					
					if(total>0){
						percentage = 100-((current/total)*100);
					}
					
					if(Float.isNaN(percentage)){
						percentage = 0;
					}
				}
				
				map.put("count", Math.round(current-prev)); //건수
				map.put("current", Math.round(current)); //금주 or 당월
				map.put("percentage", String.format("%.1f", percentage)); // 점유율
				map.put("prev", Math.round(prev)); //전주 or 전월
				map.put("data", gson.toJson(list)); // 차트 표시용 데이터(json형식)
			}else{
				map.put("percentage", "0"); // 점유율
				map.put("count", 0); //건수
				map.put("current", 0); //금주 or 당월
				map.put("prev", 0); //전주 or 전월
				map.put("data", gson.toJson("")); // 차트 표시용 데이터(json형식)
			}
			lclsList.add(map);
		}
		return lclsList;
	}
	
	@Override
	public HashMap<String, Object> dashBoardInterestReport(DashBoardVo vo) throws Exception{
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 대시보드에 표시 가능한 관심키워드 목록
		List<EgovMap> interestList = interestService.selectInterestKeywordListDashYn(new InterestKeywordVo());

		// 리스트에 담긴 관심키워드 목록을 하나씩 createQuery메소드에 대입한다.
		for(EgovMap interest : interestList){
			String keyword = (String)interest.get("keyword");
			queryList.add(createQuery(Globals.DASH_VOC_INTEREST, vo, keyword));
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
		
		//vo.setCondition(Globals.FIELD_DAY); // 표시 기간
		BaseVo base = new BaseVo();

		// controller에서 가져온 날짜를 기준으로 그 해당 주의 앞의 일요일을 기준으로해서 날짜를 새로 결정한다.
		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			base.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), 0));
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_DAY);
		}else{
			base.setStartDate(vo.getEndDate().substring(0, 6) + "01");
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_WEEK);
		}
		
		LinkedHashMap<String, String> categoriesMap = getCategories(base); // x축
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
				
				// x축 표시 정보
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
				
				// y축 표시 정보
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							EgovMap keywordtt = interestList.get(i);
							dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					} 
					HashMap<String, Object> seriesMap = new HashMap<String, Object>();
					
					EgovMap keyword = interestList.get(i);
					seriesMap.put("name", (String)keyword.get("keyword"));
					seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
					list.add(0, seriesMap);
				}
			}
		}
		
		chart.put("series", gson.toJson(list));
		chart.put("categories", gson.toJson(categoriesMap.values()));
		
		return chart;
	}
	
	@Override
	public HashMap<String, Object> dashBoardVoiceReport(DashBoardVo vo) throws Exception{
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 리스트에 담긴 관심키워드 목록을 하나씩 createQuery메소드에 대입한다.
		queryList.add(createQuery(Globals.DASH_VOC_VOICE, vo, vo.getEndDate()));
		
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
		
		
		LinkedHashMap<String, String> categoriesMap = new LinkedHashMap<String, String>(); // x축
		for(int i=0; i<24; i++){
			if(i % 2 == 0){	// 2시간 텀
				String hour = "";
				if(i < 10)	hour = "0"+Integer.toString(i);
				else hour = Integer.toString(i);
				categoriesMap.put(vo.getEndDate()+hour, hour+"시");
			}
		}
		
		
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
				
				// x축 표시 정보
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
				
				// y축 표시 정보
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							
							if(k % 2 == 0)	// 2시간 텀
								dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					} 
					HashMap<String, Object> seriesMap = new HashMap<String, Object>();
					
					seriesMap.put("name", "음성");
					seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
					list.add(0, seriesMap);
				}
			}
		}
		
		chart.put("series", gson.toJson(list));
		chart.put("categories", gson.toJson(categoriesMap.values()));
		
		return chart;
	}
	
	
	@Override
	public HashMap<String, Object> dashBoardVipIssueReport(DashBoardVo vo) throws Exception{
		
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String[] keyword = new String[10];
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		
		querySet.addQuery(createQuery(Globals.DASH_VOC_VIP_ISSUE, vo, ""));

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
		
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		
		// 상위 10개 우수고객 키워드 쿼리 생성
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							if(k>9) break;
							keyword[k] = new String(groupResults[j].getId(k));
							queryList.add(createQuery(Globals.DASH_VOC_VIP_ISSUE_KEYWORD, vo, new String(groupResults[j].getId(k))));
						}
					}

				}
			}
		}
		
		// QuerySet 인스턴스화
		querySet = new QuerySet(queryList.size());
		
		for(int i=0; i < queryList.size(); i++){
			querySet.addQuery((Query) queryList.get(i));
		}
		
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultList = results.getResultList();
		}else{
			resultList = new Result[1];
			resultList[0]= new Result();
		}
		
		// 10건에 대해 차트 생성
		//vo.setCondition(Globals.FIELD_DAY); // 표시 기간
		BaseVo base = new BaseVo();

		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			base.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), 0));
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_DAY);
		}else{
			base.setStartDate(vo.getEndDate().substring(0, 6) + "01");
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_DAY);
		}
		
		LinkedHashMap<String, String> categoriesMap = getCategories(base); // x축
		List<Object> list = new ArrayList<Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
				
				// x축 표시 정보
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
				
				// y축 표시 정보
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					}
					HashMap<String, Object> seriesMap = new HashMap<String, Object>();
					
					seriesMap.put("name", keyword[i]);
					seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
					list.add(0, seriesMap);
				}
			}
		}
		
		chart.put("series", gson.toJson(list));
		chart.put("pointStartYear", base.getStartDate().substring(0, 4));
		chart.put("pointStartMonth", base.getStartDate().substring(4, 6));
		chart.put("pointStartDay", base.getStartDate().substring(6, 8));
		
		return chart;
	}
	
	@Override
	public HashMap<String, Object> dashBoardComplainKeywordReport(DashBoardVo vo) throws Exception{
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		queryList.add(createQuery(Globals.DASH_VOC_COMPLAIN_KEYWORD, vo, ""));
		
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
		
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		
		// 상위 10개 불만 키워드 차트 생성
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
				
				// y축 표시 정보
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							if(k>9) break;
							dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					}
					List<Object> list = new ArrayList<Object>();
					HashMap<String, Object> seriesMap = new HashMap<String, Object>();
					
					seriesMap.put("name", "불만 키워드");
					seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
					list.add(seriesMap);
					
					chart.put("series", gson.toJson(list));
					chart.put("categories", gson.toJson(dataMap.keySet()));
				}
			}
		}
		
		return chart;
	}
	
	@Override
	public List<TopKeywordVO> dashBoardTop10Keyword(DashBoardVo vo) throws Exception{
		
		List<TopKeywordVO> list = new ArrayList<TopKeywordVO>();
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		//gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		queryList.add(createQuery(Globals.DASH_VOC_Top10_KEYWORD, vo, "A"));
		
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
		
		// 상위 10개 키워드 차트 생성
		int Intsum = 0;
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();

						int rSize = groupResults[0].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							if(k>9) break;
							TopKeywordVO tvo = new TopKeywordVO();
							if(k==0){
								Intsum=groupResults[0].getIntValue(k);									
							}
								double TopKeywordpercentageWork = (double)groupResults[0].getIntValue(k)/Intsum*100;
								int Topkeywordpcworck = (int)TopKeywordpercentageWork;
								String Topkeywordpcworck2 = Integer.toString(Topkeywordpcworck);
								tvo.setKeyword(new String(groupResults[0].getId(k)));
								tvo.setKeywordNum(comma_add(Integer.toString(groupResults[0].getIntValue(k))));
								tvo.setPercentage(Topkeywordpcworck2);
								list.add(tvo);
						}

				}
			}
		}
		
		return list;
	}
	
	@Override
	public List<TopCategoryVO> dashBoardTopCategory(DashBoardVo vo) throws Exception{
		List<TopCategoryVO> TopCatelist = new ArrayList<TopCategoryVO>();
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		queryList.add(createQuery(Globals.DASH_VOC_TopCategory_KEYWORD, vo, "A"));
		
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
		
		// 상위 10개 키워드 차트 생성
		int Intsum2 = 0;
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();

						int rSize = groupResults[0].groupResultSize();
						for (int k = 0; k < rSize; k++) {
								if(k>9) break;
								if(k==0){
									Intsum2=groupResults[0].getIntValue(k);									
								}
								TopCategoryVO tcvo = new TopCategoryVO();
								double TopCatePercentageWork = (double)groupResults[0].getIntValue(k)/Intsum2*100;
								int TopCateptWork = (int)TopCatePercentageWork;
								String TopCateptWork2 = Integer.toString(TopCateptWork);
								tcvo.setKeyword(new String(groupResults[0].getId(k)));
								tcvo.setKeywordNum(comma_add(Integer.toString(groupResults[0].getIntValue(k))));
								tcvo.setPercentage(TopCateptWork2);
								TopCatelist.add(tcvo);
						}

				}
			}
		}
		
		return TopCatelist;
	}
	
	@Override
	public HashMap<String, Object> dashBoardComplainDocument(DashBoardVo vo) throws Exception{
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 불만 목록
		queryList.add(createQuery(Globals.DASH_VOC_COMPLAIN_DOCUMENT, vo, ""));
		// 불만 지수
		queryList.add(createQuery(Globals.DASH_VOC_COMPLAIN_DOCUMENT_REPORT, vo, ""));
		
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
		
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			// 불만 목록
			if(i == 0){
				List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = null;
				
				if(result.getRealSize() != 0){
					
					for (int j = 0; j < result.getRealSize(); j++) {
						map = new HashMap<String, String>();
						map.put(Globals.FIELD_ID, new String(result.getResult(j,0)));
						map.put(Globals.FIELD_CONTENT, new String(result.getResult(j,1)));
						
						list.add(map);
					}
				}
				// 불만 목록
				chart.put("list", list);
				chart.put("complainSize", result.getTotalSize());
				
			//불만 지수
			}else{
				
				float complainY = 0.f;
				float complainN = 0.f;
				float percentage= 0.f;
				
				if(result.getGroupResultSize() != 0){
					
					groupResults = result.getGroupResults();
					
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						
						for (int k = 0; k < rSize; k++) {
							String id = new String(groupResults[j].getId(k));
							// 불만 문서
							if(id.equals(Globals.COM_COMPLAIN_Y)){
								complainY = groupResults[j].getIntValue(k);
							// 비불만 문서
							}else{
								complainN = groupResults[j].getIntValue(k);
							}
						}
					}
				}

				percentage = (complainY/(complainY+complainN))*100;
				if(Float.isNaN(percentage)){
					percentage = 0;
				}
				
				chart.put("totalSize", String.format("%.0f", complainY+complainN));
				// 불만지수
				chart.put("percentage", String.format("%.1f", percentage));
			}
		}
		
		return chart;
	}
	
	@Override
	public HashMap<String, Object> dashBoardComplainDetailView(DashBoardVo vo)
			throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 불만 상세
		queryList.add(createQuery(Globals.DASH_VOC_COMPLAIN_DOCUMENT_DETAIL, vo, ""));
		
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
		
		HashMap<String, Object> detail =  new HashMap<String, Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			
			// 불만 상세
			if(result.getRealSize() != 0){
				for (int j = 0; j < result.getRealSize(); j++) {
					detail.put(Globals.FIELD_ID, new String(result.getResult(j,0)));
					detail.put(Globals.FIELD_CONTENT, new String(result.getResult(j,1)));
				}
			}
		}
		
		return detail;
	}
	
	@Override
	public HashMap<String, Object> dashBoardIssueReport(DashBoardVo vo) throws Exception{
		
		// TOP 10개 갯수
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		
		//DB에서 민원종류를 가져온다.
		List<CommonSelectBoxVo> characterTypeList = new ArrayList<CommonSelectBoxVo>();
		if(vo.getCollection().equals("VOC")){
			characterTypeList = commonSelectBoxService.minwonTypeList();			
		}else{
			characterTypeList = commonSelectBoxService.snsTypeList();
		}
		
		
		// 화면표시용 성격유형 명칭
		chart.put("characterTypeList", characterTypeList);

		// DB에서 가져온 민원종류를 하나씩 createQuery메소드에 적용한다.
		for(CommonSelectBoxVo character : characterTypeList){
			queryList.add(createQuery(Globals.DASH_VOC_ISSUE, vo, character.getCdKnm()));
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
		
		String[] colorArr = Globals.COM_DASH_ISSUE_COLOR; // 이슈클라우드에서 쓰일 색 종류를 가져온다.
		
		List<Object> word = new ArrayList<Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			if(result.getGroupResultSize() != 0){
				List<Object> list = new ArrayList<Object>();
				groupResults = result.getGroupResults();
				
				String color = "#000000";
				
				if(i<=colorArr.length){
					color = colorArr[i];
				}
				
				
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("style", "color: " + color +"; font-family: 'NanumGothicBold'; ");
				
				for (int j = 0; j < groupResults.length; j++) {
					int rSize = groupResults[j].groupResultSize();
					float total = 0.f;
					
					for (int k = 0; k < rSize; k++) {
						if(k>=16) break;
						total += groupResults[j].getIntValue(k);
					}
					
					for (int k = 0; k < rSize; k++) {
						if(k>=15) break;
						HashMap<String, Object> obj = new HashMap<String, Object>();
						int value = groupResults[j].getIntValue(k);
						obj.put("text", new String(groupResults[j].getId(k)));
						if(value == total){
							obj.put("weight", 16);
						}else{
							if(k>=5){
								obj.put("weight", 5-k);
							}else{
								obj.put("weight", 16-k);
							}
							
						}
						obj.put("style", color);
						obj.put("html", data);
						obj.put("link", "#");
						list.add(obj);
						word.add(obj);
					}
				}
				
				//chart.put(characterTypeList.get(i).getLcls(), list); // 성격유형 코드(key)로 등록
				chart.put(characterTypeList.get(i).getCdKnm(), list);
			}
		}
		
		chart.put("word", gson.toJson(word));
		return chart;
	}

	@Override
	public HashMap<String, Object> dashBoardKeywordReport(DashBoardVo vo) throws Exception{
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		LinkedHashMap<String,String> keywordMap = new LinkedHashMap<String,String>();
		
		List<EgovMap> interestList = interestService.selectInterestKeywordListTop10(new InterestKeywordVo());
		
		for(EgovMap interest : interestList){
			keywordMap.put((String)interest.get("keyword"), (String)interest.get("keyword"));
		}
		
		BaseVo base = new BaseVo();

		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			base.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), 0));
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_DAY);
		}else{
			base.setStartDate(vo.getEndDate().substring(0, 6) + "01");
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_WEEK);
		}
		
		LinkedHashMap<String, String> categoriesMap = getCategories(base); // x축
		
		// 이전 랭킹
		queryList.add(createQuery(Globals.DASH_VOE_KEYWORD_OLD, vo, ""));
		
		for (Entry<String, String> entry : categoriesMap.entrySet()) { // 전회분
			// 키워드 랭킹
			queryList.add(createQuery(Globals.DASH_VOE_KEYWORD, vo, entry.getKey()));
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

		return getRanking(resultList, categoriesMap, keywordMap);
	}
	
	@Override
	public HashMap<String, Object> dashBoardIssueDetailView(DashBoardVo vo)
			throws Exception {
		
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		//System.out.println("이슈 클라우드 상세페이지 term 확인 : "+vo.getTerm());
		// 이슈키워드 상세
		queryList.add(createQuery(Globals.DASH_VOC_ISSUE_DETAIL, vo, vo.getTerm()));
		
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
		
		HashMap<String, Object> detail =  new HashMap<String, Object>();
		
		//vo.setCondition(Globals.FIELD_DAY); // 표시 기간
		BaseVo base = new BaseVo();

		if(vo.getCondition().equals(Globals.FIELD_WEEK)){
			base.setStartDate(DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), 0));
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_DAY);
		}else{
			base.setStartDate(vo.getEndDate().substring(0, 6) + "01");
			base.setEndDate(vo.getEndDate());
			base.setCondition(Globals.FIELD_WEEK);
		}
		
		LinkedHashMap<String, String> categoriesMap = getCategories(base); // x축
		List<Object> list = new ArrayList<Object>();
		
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
				
				// x축 표시 정보
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
				
				// y축 표시 정보
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						if(j == 0){
							// 키워드 추이
							for (int k = 0; k < rSize; k++) {
								dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
							}
							
							HashMap<String, Object> seriesMap = new HashMap<String, Object>();
							
							seriesMap.put("name", vo.getTerm());
							seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
							list.add(seriesMap);
							
							detail.put("name", vo.getTerm());
							detail.put("series", gson.toJson(list));
							detail.put("categories", gson.toJson(categoriesMap.values()));
						}else if(j == 1){
							// 전주 금주
							int prev = 0;
							int current = 0;
							for (int k = 0; k < rSize; k++) {
								HashMap<String, Object> obj = new HashMap<String, Object>();
								if(vo.getCurrent().equals(new String(groupResults[j].getId(k)))){
									current = groupResults[j].getIntValue(k); // 금주 or 당월
									
								}else{
									prev = groupResults[j].getIntValue(k); // 전주 or 전월
								}
								list.add(obj);
							}
							detail.put("prev", prev); //건수
							detail.put("current", current); // 차트 표시용 데이터(json형식)
						
						}else{
							float total = 0;
							float value = 0.f;
							float share = 0;
							
							// 점유율
							for (int k = 0; k < rSize; k++) {
								total += groupResults[j].getIntValue(k);
								String keyword = new String(groupResults[j].getId(k));
								if(vo.getTerm().equals(keyword)){
									value = groupResults[j].getIntValue(k);
								}
							}
							
							share = (value / total) * 100;
							
							if(Float.isNaN(share)){
								share = 0;
							}
							detail.put("share", String.format("%.3f", share)); //건수
						}
					}

				}
			}
		}

		return detail;
	}

	@Override
	public HashMap<String, Object> dashBoardLclsShareReport(DashBoardVo vo)
			throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		queryList.add(createQuery(Globals.DASH_VOE_LCLS_SHARE, vo, ""));
		
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
		
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		List<HashMap<String, Object>> share = new ArrayList<HashMap<String, Object>>();
		String name = "";
		for(int i = 0; resultList != null && i < resultList.length; i++) {
					
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				for (int j = 0; j < groupResults.length; j++) {
					int rSize = groupResults[j].groupResultSize();
					for (int k = 0; k < rSize; k++) {
						if(groupResults[j].getId(k).length>0){
							HashMap<String, Object> data = new HashMap<String, Object>();
							name = new String(groupResults[j].getId(k));
							name = name.replace("관련", "");
							data.put("name",name);
							data.put("y", groupResults[j].getIntValue(k));
							data.put("color", Globals.COM_DASH_VOE_SHARE_COLOR[share.size()]);
							share.add(data);
						}
					}
				}
			}
		}
		
		chart.put("series", gson.toJson(share));
		
		return chart;
	}

	@Override
	public HashMap<String, Object> dashBoardDeptReport(DashBoardVo vo)
			throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		queryList.add(createQuery(Globals.DASH_VOE_DEPT, vo, ""));
		
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
		
		HashMap<String, Object> chart =  new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		
		// 상위 5개 제안부서 차트 생성
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			
			Result result = (Result)resultList[i];
			GroupResult[] groupResults = null;
			
			if(result.getGroupResultSize() != 0){
				groupResults = result.getGroupResults();
				
				LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<String, Integer>();
				
				// y축 표시 정보
				if(result.getGroupResultSize() != 0){
					groupResults = result.getGroupResults();
					for (int j = 0; j < groupResults.length; j++) {
						int rSize = groupResults[j].groupResultSize();
						for (int k = 0; k < rSize; k++) {
							if(k>4) break;
							dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					}
					
					HashMap<String, Object> seriesMap = new HashMap<String, Object>();
					
					seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
					list.add(seriesMap);
					
				}
				chart.put("categories", gson.toJson(dataMap.keySet()));
			}
		}
		chart.put("series", gson.toJson(list));
		
		
		return chart;
	}
	
public ArrayList<HashMap<String,Object>> getAlikeSearchResult(DashBoardVo vo) throws Exception{
		
		ArrayList<HashMap<String,Object>> searchResult = null;
		
		Result [] resultlist = null;
		Result result = null;
		Query query = new Query();
		
		query.setSelect(createSelectSet(97, vo));
		query.setWhere(createWhereSet(97, vo, vo.getGuname()));
		query.setOrderby(createOrderBySet());
		query.setFrom(vo.getCollection());
		query.setResult(0, 9);
		query.setSearchOption((byte)Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED|Protocol.SearchOption.PERCENT);
		query.setThesaurusOption((byte)Protocol.ThesaurusOption.EQUIV_SYNONYM|(byte)Protocol.ThesaurusOption.QUASI_SYNONYM);	
		query.setSearch(true);
		//query.setExtData(keywordRankingVo.getUserIp()); // 로그에 남길 키워드 명시
		//query.setUserName(keywordRankingVo.getUserId()); // 로그에 남길 키워드 명시v
		query.setDebug(true);
		query.setPrintQuery(true);
		query.setLoggable(true);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
	
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if(resultlist!=null) {
				result = resultlist[0];
			}
		}
		
		if(result != null ){
			
			//리스트 결과 담기
			searchResult = new ArrayList<HashMap<String,Object>>();
			for(int i = 0; i < result.getRealSize(); i++){
				HashMap<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("ID", new String(result.getResult(i, 0)));
				resultMap.put("TITLE", new String(result.getResult(i, 1)));
				resultMap.put("CONTENT", new String(result.getResult(i, 2)));
				resultMap.put("REGDATE", new String(result.getResult(i, 3)));
				int weight = Integer.parseInt(new String(result.getResult(i,4)));
				if(weight > 0){
					resultMap.put("WEIGHT", String.valueOf(weight / 100));
				}else{
					resultMap.put("WEIGHT", "0");
				}
				searchResult.add(resultMap);
			}
		}
		return searchResult;
	}


   public HashMap<String, Object> getSearchResult(DashBoardVo vo) throws Exception{
	   
	   HashMap<String,Object> searchResult = new HashMap<String,Object>();
		ArrayList<HashMap<String,Object>> groupResult = null;
		ArrayList<HashMap<String,Object>> listResult = null;
		Result [] resultlist = null;
		Result result = null;
		String share = "0";	//점유율
		String keywordTemp = null;
		
		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();
		
		int startNo = (vo.getCurrentPage() - 1) * vo.getPagesize();
		int endNo = startNo + vo.getPagesize()-1;
			
		QuerySet querySet = new QuerySet(2);
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(99,vo));
		query.setWhere(createWhereSet(98,vo,vo.getGuname()));
		//System.out.println("keywordRankingVo.getNeedsType() 확인 : "+keywordRankingVo.getNeedsType());
		GroupBySet[] groupBys = new GroupBySet[] {
				new GroupBySet("L_CATE_NAME", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", vo.getNeedsType())
				
		};
		query.setGroupBy(groupBys);
		query.setFilter(createFilterSet(98,vo,vo.getNeedsType()));
		query.setOrderby(createOrderBySet());
		query.setFrom(vo.getCollection());
		query.setResult(startNo, endNo);
		query.setSearch(true);
		//query.setExtData(keywordRankingVo.getUserIp()); // 로그에 남길 키워드 명시
		//query.setUserName(keywordRankingVo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(true);
		query.setPrintQuery(true);
		query.setLoggable(true);
		query.setThesaurusOption((byte)Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();	
		System.out.println("##### 결과query1: " + queryParser.queryToString(query));
		
		//키워드 없이 전체 건수 구하기(점유율)
		query = new Query();
		query.setSelect(createSelectSet(10,vo));
		query.setWhere(createWhereSet(98, vo, vo.getGuname()));
		query.setFilter(createFilterSet(98,vo,vo.getNeedsType()));
		query.setGroupBy(createGroupBySet(99,vo, "DESC"));		
		query.setFrom(vo.getCollection());
		query.setResult(0, 0);
		query.setSearch(true);
		//query.setExtData(keywordRankingVo.getUserIp()); // 로그에 남길 키워드 명시
		//query.setUserName(keywordRankingVo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(true);
		query.setLoggable(true);
		query.setThesaurusOption((byte)Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		querySet.addQuery(query);
		
		System.out.println("##### 결과query2: " + queryParser.queryToString(query));

		
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		//resultlist[0]이 해당 기간에 속한 키워드에 관한 문서 검색결과
		//resultlist[1]이 해당 기간에 추출된 keyword 전체 결과 - 
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			
			if(resultlist!=null) {
				result = resultlist[0];
				try{
					
					if(result.getTotalSize()>0){
						
						if(resultlist[1].getTotalSize() >0){
							//키워드에 해당하는 점유율 구하기 - 변수 : share
							Result TotalKeyWordResult = resultlist[1];
							GroupResult[] groupResultlist = TotalKeyWordResult.getGroupResults();	
							System.out.println("통과");
							keywordTemp = vo.getGuname();
							System.out.println("통과2"+keywordTemp);
							// - 키워드가 없으면 키워드 점유율이 제일 높은게 키워드가 된다.
							
							if((keywordTemp == null || "".equals(keywordTemp)) && groupResultlist[0].groupResultSize() > 0){
								keywordTemp = new String(groupResultlist[0].getId(0)).trim();
							}
							
							int totalKeywordCount = 0;	//해당 기간동안의 전체 키워드 갯수
							int selectKeywordCount = 0;	//선택 키워드에 대한 갯수
							for (int i = 0; i < groupResultlist[0].groupResultSize(); i++){
								if(keywordTemp.equals(new String(groupResultlist[0].getId(i)).trim())){
									selectKeywordCount = groupResultlist[0].getIntValue(i);	//해당 키워드의 갯수 구하기
								}
								totalKeywordCount += groupResultlist[0].getIntValue(i);	//전체 키워드 대한 갯수 (누적) 구하기
							
							}
							if(selectKeywordCount>0){
								DecimalFormat df = new DecimalFormat("0.###");
								share = df.format((selectKeywordCount/(double)totalKeywordCount)*100);
							}
							//점유율 구하기 끝
						}
					}
				}catch(ArithmeticException e){}
			}
		}
		
		if(result != null && result.getGroupResultSize() != 0){
			GroupResult[] groupResultlist = result.getGroupResults();	
			//그룹 결과 담기
			NumberFormat nf = NumberFormat.getNumberInstance();
			groupResult = new ArrayList<HashMap<String,Object>>();
			for (int k = 0; k < groupResultlist[0].groupResultSize(); k++){
				//need유형 그룹 별 설정
				HashMap<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("name", new String(groupResultlist[0].getId(k)).trim());
				resultMap.put("count", nf.format(groupResultlist[0].getIntValue(k)));
				groupResult.add(resultMap);
			}
			
			//리스트 결과 담기
			
			listResult = new ArrayList<HashMap<String,Object>>();
			for(int i = 0; i < result.getRealSize(); i++){
				HashMap<String,Object> resultMap = new HashMap<String,Object>();
				resultMap.put("ID", new String(result.getResult(i, 0)));
				resultMap.put("TITLE", new String(result.getResult(i, 1)));
				resultMap.put("CONTENT", new String(result.getResult(i, 2)));
				resultMap.put("CONTENT_ORI", new String(result.getResult(i, 3)));
				if(new String(result.getResult(i, 6)).equals("120")){					
					resultMap.put("USE_MED_CD", "120미추홀콜센터");
				}else if(new String(result.getResult(i, 6)).equals("새올")){
					resultMap.put("USE_MED_CD", "새올민원");
				}else if(new String(result.getResult(i, 6)).equals("신문고")){
					resultMap.put("USE_MED_CD", "국민신문고");
				}else{
					resultMap.put("USE_MED_CD", new String(result.getResult(i, 6)));
				}
				
				String date = "";
				if(result.getResult(i, 4)!=null){
					date = new String(result.getResult(i, 4));
					if(date.length()>7){
						date = date.substring(0,4)+"."+date.substring(4,6)+"."+date.substring(6,8);
					}
				}
				resultMap.put("REGDATE", date);
				resultMap.put("WEIGHT", new String(result.getResult(i, 5)));
				listResult.add(resultMap);
			}
			
			searchResult.put("keyword", vo.getGuname());	//검색 키워드
			searchResult.put("groupResult", groupResult);	//카테고리
			searchResult.put("listResult", listResult);		//리스트
			searchResult.put("totalSize", nf.format(result.getTotalSize()));	//전체 건수
			searchResult.put("share", share);	//점유율
			int endPage = result.getTotalSize()/vo.getPagesize();
			if(endPage % vo.getPagesize() > 0){
				endPage++;
			}
			if(endPage == 0){
				endPage = 1;
			}
			searchResult.put("endPage", endPage);	//끝 페이지
			searchResult.put("currentPage", vo.getCurrentPage());	//현재 페이지
			
	    	*//** pageing setting *//*
	    	PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(vo.getCurrentPage());
			paginationInfo.setRecordCountPerPage(vo.getPagesize());
			paginationInfo.setPageSize(10);
			paginationInfo.setTotalRecordCount(result.getTotalSize());
			
			searchResult.put("paginationInfo", paginationInfo);
	        
		}
		System.out.println("서비스 통과1");
		return searchResult;
   }


	public HashMap<String, Object> getRegionExcelResult(DashBoardVo vo) throws Exception{
		
		HashMap<String,Object> searchResult = new HashMap<String,Object>();
		ArrayList<HashMap<String,Object>> listResult = null;
		Result [] resultlist = null;
		Result result = null;
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(99,vo));
		query.setWhere(createWhereSet(98,vo,vo.getGuname()));
		GroupBySet[] groupBys = new GroupBySet[] {
				new GroupBySet("L_CATE_NAME", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", vo.getNeedsType())
		};
		query.setGroupBy(groupBys);
		query.setFilter(createFilterSet(98,vo,""));
		query.setOrderby(createOrderBySet());
		query.setFrom(vo.getCollection());
		query.setResult(0, 999);
		query.setSearch(true);
		query.setExtData(vo.getUserIp()); // 로그에 남길 키워드 명시
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setDebug(true);
		query.setPrintQuery(true);
		query.setLoggable(true);
		query.setThesaurusOption((byte)Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		querySet.addQuery(query);
				
		QueryParser queryParser = new QueryParser();	
		System.out.println("##### 엑셀결과를 얻기위한 쿼리: " + queryParser.queryToString(query));
		
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if(resultlist!=null) {
				result = resultlist[0];
				if(result != null && result.getGroupResultSize() != 0){
					//리스트 결과 담기
					listResult = new ArrayList<HashMap<String,Object>>();
					for(int i = 0; i < result.getRealSize(); i++){
						HashMap<String,Object> resultMap = new HashMap<String,Object>();
						resultMap.put("ID", new String(result.getResult(i, 0)));
						resultMap.put("TITLE", new String(result.getResult(i, 1)));
						resultMap.put("CONTENT", new String(result.getResult(i, 2)));
						resultMap.put("CONTENT_ORI", new String(result.getResult(i, 3)));
						String date = "";
						if(result.getResult(i, 4)!=null){
							date = new String(result.getResult(i, 4));
							if(date.length()>7){
								date = date.substring(0,4)+"."+date.substring(4,6)+"."+date.substring(6,8);
							}
						}
						resultMap.put("REGDATE", date);
						resultMap.put("WEIGHT", new String(result.getResult(i, 5)));
						listResult.add(resultMap);
					}
					searchResult.put("listResult", listResult);		//리스트
				}
				
			}
		}
		
		return searchResult;
	}
   
   
	public HashMap<String,String> getdetailViewResult(DashBoardVo vo) throws Exception{
		
		HashMap<String, String> resultMap = null;
		
		char[] startTag = "<span class=\"result_keyword\">".toCharArray();
		char[] endTag = "</span>".toCharArray();
		
		Result [] resultlist = null;
		Result result = null;
		Query query = new Query(startTag, endTag);
		query.setSelect(createSelectSet(96,vo));
		query.setWhere(createWhereSet(96, vo, vo.getDoc_id()));
		query.setFrom(vo.getCollection());
		query.setResult(0, 0);
		query.setSearch(true);
		query.setDebug(true);
		query.setPrintQuery(true);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();	
		System.out.println("##### 상세검색 query: " + queryParser.queryToString(query));
		// 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if(resultlist!=null) {
				result = resultlist[0];
			}
		}
		if(result != null ){
			//결과 담기
			resultMap = new HashMap<String, String>();
			for(int i = 0; i < result.getRealSize(); i++){
				
				resultMap.put("ID", new String(result.getResult(i, 0)));
				resultMap.put("TITLE", new String(result.getResult(i, 1)));
				resultMap.put("CONTENT", new String(result.getResult(i, 2)));
				resultMap.put("REGDATE", new String(result.getResult(i, 3)));
				resultMap.put("WEIGHT", new String(result.getResult(i, 4)));
				resultMap.put("USE_MED_CD", new String(result.getResult(i, 5)));
				resultMap.put("M_CATE_NAME", new String(result.getResult(i, 6)));
				resultMap.put("S_CATE_NAME", new String(result.getResult(i, 7)));
				resultMap.put("DEPART_TRANSFER", new String(result.getResult(i, 8)));
				resultMap.put("RES_CONTENT", new String(result.getResult(i, 9)));
			}
		}
		return resultMap;
	}
	
	public String comma_add(String num){   //콤마 만들기 위한 메소드
		  String original = num;
		  String convert = "";
		  int count = 1;
		  for(int k=original.length()-1; k>-1; k--){   
		   if( (count%3) == 0 && k < original.length()-1 && k > 0){
		    convert = "," +original.charAt(k)  + convert;
		   }
		   else{
		    convert = original.charAt(k) + convert;
		   }
		   count++;
		  }
		  return convert;
		 }
	
	*//**
	 * Query 설정.
	 * @return Query
	 *//*
	private Query createQuery(int con, DashBoardVo vo, String keyword) throws Exception{
		
		// Query 설정
		Query query = new Query("", "");
		query.setSearch(true); // 검색 여부 설정DASH_VOC_NEEDS
		query.setExtData(vo.getUserIp()); // 로그에 남길 키워드 명시
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setLogKeyword("".toCharArray()); // 로그에 남길 키워드 명시
		query.setLoggable(true); // queryLog를 남길지 여부
		query.setLogExtractor("KOREAN".toCharArray()); // 분석결과 출력 위한 분석기 지정
		query.setLogExtractorOption("".toCharArray()); // 분석기의 옵션 지정
		query.setDebug(true); // 에러 리포팅 여부
		query.setFaultless(false); // true로 설정하면 검색 중 에러 발생시 검색 중단하고 ErrorMessage 객체를 반환
		if(con==Globals.DASH_VOC_INTEREST) query.setPrintQuery(true); // 쿼리 문자열 출력 여부
		else query.setPrintQuery(true);
		query.setThesaurusOption((byte)Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		
		switch (con) {
		case Globals.DASH_VOC_COMPLAIN_DOCUMENT:
			query.setResult(0, 99);
			break;
		case Globals.DASH_VOC_NEEDS:
		case Globals.DASH_VOE_LCLS:
		case Globals.DASH_VOC_INTEREST:
		case Globals.DASH_VOC_COMPLAIN_KEYWORD:
		case Globals.DASH_VOC_Top10_KEYWORD:
		case Globals.DASH_VOC_TopCategory_KEYWORD:
		case Globals.DASH_VOC_COMPLAIN_DOCUMENT_REPORT:
		case Globals.DASH_VOC_VIP_ISSUE:
		case Globals.DASH_VOC_VIP_ISSUE_KEYWORD:
		case Globals.DASH_VOC_ISSUE_DETAIL:
		case Globals.DASH_VOE_LCLS_SHARE:
		case Globals.DASH_VOC_COUNTRYMINWON:	
		case Globals.DASH_VOE_DEPT:
		case Globals.DASH_VOC_VOICE:
			query.setResult(0, 0);
			break;
		case Globals.DASH_VOC_CALLTEXT:
			query.setResult(0, 8);
			break;
		default:
			query.setResult(0, 10);
			break;
		}
		
		// From 설정, 검색할 컬렉션을 선택
		if(con == Globals.DASH_VOE_LCLS || con == Globals.DASH_VOE_KEYWORD || con == Globals.DASH_VOE_KEYWORD_OLD 
				|| con == Globals.DASH_VOE_LCLS_SHARE || con == Globals.DASH_VOE_DEPT){
			query.setFrom(Globals.MARINER_COLLECTION2);
		}else{
			query.setFrom(Globals.MARINER_COLLECTION);
		}
		
		
		if(vo.getCollection()==null){
			//System.out.println("컬렉션값이 null확인");
			vo.setCollection("");
		}
		
		
		query.setFrom(Globals.MARINER_COLLECTION);
		
		if(vo.getCollection().equals("SNS")){
			query.setFrom("SNS");
		}
		
		keyword = "버스";

		// SelectSet 설정
		query.setSelect(createSelectSet(con, vo));
		
		// WhereSet 설정
		query.setWhere(createWhereSet(con, vo, keyword));
		
		// FilterSet 설정
		query.setFilter(createFilterSet(con, vo, keyword));
		
		// GroupBySet 설정
		query.setGroupBy(createGroupBySet(con, vo,keyword));
		
		QueryParser queryParser = new QueryParser();	
		if(con==Globals.DASH_VOC_COUNTRYMINWON) System.out.println("##### 지역 query: " + queryParser.queryToString(query));
		
		if(con==Globals.DASH_VOC_INTEREST) System.out.println("##### 관심키워드 query: " + queryParser.queryToString(query));
		
		if(con==Globals.DASH_VOC_ISSUE) System.out.println("##### 이슈클라우드 query: " + queryParser.queryToString(query));
		
		if(con==Globals.DASH_VOC_VOICE) System.out.println("##### 음성분석 query: " + queryParser.queryToString(query));
		if(con==Globals.DASH_VOC_CALLTEXT) System.out.println("##### call test query: " + queryParser.queryToString(query));
		return query;
		
	}
	
	*//**
	 * SelectSet 설정.
	 * @return SelectSet[]
	 *//*
	private SelectSet[] createSelectSet(int con, DashBoardVo vo){
		SelectSet[] selectSet = null;
		ArrayList<SelectSet> selectList = new ArrayList<SelectSet>();
		
		switch (con) {
		case Globals.DASH_VOC_NEEDS:
		case Globals.DASH_VOC_INCREANDDECRE:
		case Globals.DASH_VOE_LCLS:
		case Globals.DASH_VOC_INTEREST:
		case Globals.DASH_VOC_COMPLAIN_KEYWORD:
		case Globals.DASH_VOC_Top10_KEYWORD:
		case Globals.DASH_VOC_TopCategory_KEYWORD:
		case Globals.DASH_VOC_COMPLAIN_DOCUMENT_REPORT:
		case Globals.DASH_VOE_KEYWORD_OLD:
		case Globals.DASH_VOE_KEYWORD:
		case Globals.DASH_VOC_ISSUE:
		case Globals.DASH_VOC_VIP_ISSUE:
		case Globals.DASH_VOC_VIP_ISSUE_KEYWORD:
		case Globals.DASH_VOC_ISSUE_DETAIL:
		case Globals.DASH_VOE_LCLS_SHARE:
		case Globals.DASH_VOC_COUNTRYMINWON:	
		case Globals.DASH_VOE_DEPT:
		case Globals.DASH_VOC_VOICE:
			selectList.add(new SelectSet(Globals.FIELD_ID, Protocol.SelectSet.NONE));
			break;
		case Globals.DASH_VOC_COMPLAIN_DOCUMENT:
			selectList.add(new SelectSet(Globals.FIELD_ID, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.SUMMARIZE, 100));
			break;
		case Globals.DASH_VOC_COMPLAIN_DOCUMENT_DETAIL:
			selectList.add(new SelectSet(Globals.FIELD_ID, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE));
			break;
		case 96 : 
			selectList.add(new SelectSet("ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE", Protocol.SelectSet.HIGHLIGHT));
			selectList.add(new SelectSet("CONTENT",  Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("REGDATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("USE_MED_CD", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("M_CATE_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("S_CATE_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("DEPART_TRANSFER", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("RES_CONTENT", Protocol.SelectSet.NONE));
			break;
		case 97 : 
			selectList.add(new SelectSet("ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CONTENT",  Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("REGDATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
			break;
		case 99 :
			selectList.add(new SelectSet("ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CONTENT",  (byte)(Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT),250));
			selectList.add(new SelectSet("CONTENT",  Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("REGDATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("USE_MED_CD", Protocol.SelectSet.NONE));
			break;
		case Globals.DASH_VOC_CALLTEXT:
			selectList.add(new SelectSet("ID", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("TITLE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CONTENT",  (byte)(Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT),100));
			selectList.add(new SelectSet("CUSTOMER_NAME", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("REGDATE", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("CALL_META_ID", Protocol.SelectSet.NONE));
			break;
		default:
			break;
		}
		
		selectSet = new SelectSet[selectList.size()];
		
		for(int i=0; i < selectList.size(); i++){
			selectSet[i] = (SelectSet) selectList.get(i);
		}
		
		return selectSet;
	}
	
	*//**
	 * WhereSet 설정.
	 * @return WhereSet[]
	 *//*
	private WhereSet[] createWhereSet(int con, DashBoardVo vo, String keyword) throws Exception{
		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereList = new ArrayList<WhereSet>();
		
		switch (con) {
			case Globals.DASH_VOC_NEEDS:	
			case Globals.DASH_VOC_COMPLAIN_DOCUMENT_REPORT:	
				whereList.add(new WhereSet(Globals.IDX_COMPLAIN, Protocol.WhereSet.OP_HASANY, 
						new String[]{Globals.COM_COMPLAIN_Y, Globals.COM_COMPLAIN_N}));
				//기존의 모든 데이터를 가지고 오기위에 위와같은 코드를 이용하였으나 현재는 데이터를 가져오는 것이 불가능 하여 하래와 같은 ALLVIEW를 이용한다.
				whereList.add(new WhereSet(Globals.IDX_ALLVIEW, Protocol.WhereSet.OP_HASALL, "A", 100));
				break;
			case Globals.DASH_VOC_ISSUE:
			case Globals.DASH_VOC_CALLTEXT:
				whereList.add(new WhereSet(Globals.IDX_COMPLAIN, Protocol.WhereSet.OP_HASANY, 
						new String[]{Globals.COM_COMPLAIN_Y, Globals.COM_COMPLAIN_N}));
				// Allview를 이용했을 시 모든 데이터를 가져올 수 있다. 해당 모든 데이터를 가지고 오기 위해서는 색인할 Keyword를 'a'를 이용하면 그것이 가능하다.
				whereList.add(new WhereSet(Globals.IDX_ALLVIEW, Protocol.WhereSet.OP_HASALL, "A", 100));
				//whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				//whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, keyword, 100));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				
				whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword, 100));
				break;	
			case Globals.DASH_VOC_INTEREST:
				whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword, 100));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, keyword, 10));
				
				break;
			case Globals.DASH_VOC_COMPLAIN_KEYWORD:
				whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword, 100));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, keyword, 10));
				break;
			case Globals.DASH_VOC_Top10_KEYWORD:
				whereList.add(new WhereSet(Globals.IDX_ALLVIEW, Protocol.WhereSet.OP_HASALL, keyword, 10));
				//whereList.add(new WhereSet("CHAR_KEYWORD", Protocol.WhereSet.OP_HASALL, keyword, 10));
				break;
			case Globals.DASH_VOC_TopCategory_KEYWORD:
				whereList.add(new WhereSet(Globals.IDX_ALLVIEW, Protocol.WhereSet.OP_HASALL, keyword, 10));
				//System.out.println("SNS값 확인 : "+vo.getCollection());
				if(vo.getCollection().equals("SNS")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
					whereList.add(new WhereSet("L_CATE_NAME", Protocol.WhereSet.OP_HASALL, "미분류"));
				}
				break;
			case Globals.DASH_VOC_COMPLAIN_DOCUMENT:
				// 불만문서
				whereList.add(new WhereSet(Globals.IDX_COMPLAIN, Protocol.WhereSet.OP_HASALL, Globals.COM_COMPLAIN_Y));
				whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword));
				break;
			case Globals.DASH_VOC_COMPLAIN_DOCUMENT_DETAIL:
				whereList.add(new WhereSet(Globals.IDX_ID, Protocol.WhereSet.OP_HASALL, vo.getId()));
				break;
			case Globals.DASH_VOE_LCLS:
			case Globals.DASH_VOE_LCLS_SHARE:
			case Globals.DASH_VOE_DEPT:
				// 주별 (전주대비)
				if(vo.getCondition().equals(Globals.FIELD_WEEK)){
					whereList.add(new WhereSet(Globals.IDX_WEEK, Protocol.WhereSet.OP_HASALL, vo.getCurrent()));
				// 월별 (전월대비)
				}else{
					whereList.add(new WhereSet(Globals.IDX_MONTH, Protocol.WhereSet.OP_HASALL, vo.getCurrent()));
				}
				break;
			case Globals.DASH_VOE_KEYWORD:
			case Globals.DASH_VOC_VOICE:
				if(vo.getCondition().equals("WEEK")){
					whereList.add(new WhereSet(Globals.FIELD_DAY, Protocol.WhereSet.OP_HASALL, keyword));
				}else if(vo.getCondition().equals("MONTH")){
					whereList.add(new WhereSet(Globals.FIELD_WEEK, Protocol.WhereSet.OP_HASALL, keyword));
				}
				break;
			case Globals.DASH_VOE_KEYWORD_OLD:
				String startDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", vo.getEndDate(), 0).replace("/", "");
				String condition = vo.getCondition();
				String old = "";
				
				if(condition.equals("WEEK")){
					old = DateUtil.addYearMonthDay("yyyyMMdd", startDate, Calendar.DAY_OF_MONTH, -1);
					whereList.add(new WhereSet(Globals.FIELD_DAY, Protocol.WhereSet.OP_HASALL, old));
				}else if(condition.equals("MONTH")){
					old = DateUtil.addYearMonthDay("yyyyMMdd", startDate, Calendar.DAY_OF_MONTH, -7);
					String week = "0" + DateUtil.getWeek("yyyyMMdd", old);
					old = old.substring(0,6) + week;
					whereList.add(new WhereSet(Globals.FIELD_WEEK, Protocol.WhereSet.OP_HASALL, old));
				}
				break;
			case Globals.DASH_VOC_VIP_ISSUE:
				whereList.add(new WhereSet(Globals.IDX_CST_LVL_CD, Protocol.WhereSet.OP_HASALL, Globals.COM_CST_LVL_VIP));
				whereList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, keyword));
				break;
			case Globals.DASH_VOC_VIP_ISSUE_KEYWORD:
			case Globals.DASH_VOC_ISSUE_DETAIL:
				whereList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASANY, keyword));
				break;
			case Globals.DASH_VOC_COUNTRYMINWON:
				whereList.add(new WhereSet(Globals.IDX_GU_NAME, Protocol.WhereSet.OP_HASALL, keyword));
				break;
			case Globals.DASH_VOC_INCREANDDECRE:
				whereList.add(new WhereSet(Globals.IDX_SENSITIVITY, Protocol.WhereSet.OP_HASALL, keyword, 100));
				break;
			case 96:
				whereList.add(new WhereSet("ID", Protocol.WhereSet.OP_HASALL, keyword , 0));	//keyword에 id들어있음
				if(vo.getCollection().equals("SNS")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
					whereList.add(new WhereSet("L_CATE_NAME", Protocol.WhereSet.OP_HASALL, "미분류"));
				}
				break;
			case 97:
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet("TITLE", Protocol.WhereSet.OP_HASANY, vo.getTitle(), 1000));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("CONTENT", Protocol.WhereSet.OP_HASANY, vo.getContent(), 100));
				whereList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
				whereList.add(new WhereSet("ID", Protocol.WhereSet.OP_HASALL, vo.getDoc_id(), 0));	//같은id제거하기
				break;
			case 98 :
				whereList.add(new WhereSet(Globals.IDX_GU_NAME, Protocol.WhereSet.OP_HASALL, keyword, 100));
				if(vo.getEmotion().equals("P")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Globals.IDX_SENSITIVITY, Protocol.WhereSet.OP_HASALL, "P", 100));
				}
				if(vo.getEmotion().equals("D")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Globals.IDX_SENSITIVITY, Protocol.WhereSet.OP_HASALL, "D", 100));
				}
				if(vo.getEmotion().equals("N")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Globals.IDX_SENSITIVITY, Protocol.WhereSet.OP_HASALL, "N", 100));
				}
				break;
			default:
				break;
		}
		
		whereSet = new WhereSet[whereList.size()];
		
		for(int i=0; i < whereList.size(); i++){
			whereSet[i] = (WhereSet) whereList.get(i);
		}
		return whereSet;
	}
	
	*//**
	 * FilterSet 설정.
	 * @return FilterSet[]
	 *//*
	private FilterSet[] createFilterSet(int con, DashBoardVo vo, String filter){
		String strDate = vo.getStartDate();
		String endDate = vo.getEndDate();
		FilterSet[] filterSet = null;
		ArrayList<FilterSet> filterList = new ArrayList<FilterSet>();

		switch (con) {
			case Globals.DASH_VOC_INCREANDDECRE:			
			case Globals.DASH_VOC_NEEDS:
				strDate = strDate.replace("/", "") + "000000";
				endDate = endDate.replace("/", "") + "235959";
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				if(!filter.isEmpty()){
					filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), Globals.FILTER_NEEDS_TYPE_CD, filter, 0));
				}
				break;
			case Globals.DASH_VOE_LCLS:
				strDate = strDate.replace("/", "") + "000000";
				endDate = endDate.replace("/", "") + "235959";
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				if(!filter.isEmpty()){
					filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), Globals.FILTER_LCLS_CD, filter, 0));
				}
				break;
			case Globals.DASH_VOC_INTEREST:
			case Globals.DASH_VOC_COMPLAIN_DOCUMENT:
			case Globals.DASH_VOC_COMPLAIN_DOCUMENT_REPORT:
			case Globals.DASH_VOC_VIP_ISSUE:
			case Globals.DASH_VOC_VIP_ISSUE_KEYWORD:
			case Globals.DASH_VOC_ISSUE_DETAIL:
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959";
				}else if(vo.getCondition().equals("MONTH")){
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
			case Globals.DASH_VOC_COMPLAIN_KEYWORD:
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959";
				}else if(vo.getCondition().equals("MONTH")){
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
			case Globals.DASH_VOC_Top10_KEYWORD:
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959";
				}else if(vo.getCondition().equals("MONTH")){
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
			case Globals.DASH_VOC_TopCategory_KEYWORD:
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959";
				}else if(vo.getCondition().equals("MONTH")){
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
			case Globals.DASH_VOC_ISSUE:
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959";
				}else if(vo.getCondition().equals("MONTH")){
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				System.out.println("이슈 클라우드 filter확인 : "+filter);
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_PARTIAL),"USE_MED_CD",filter, 0));
				break;
			case Globals.DASH_VOC_COUNTRYMINWON:
				//System.out.println("가지고 있는가 : "+vo.getCondition());
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959";
				}else if(vo.getCondition().equals("MONTH")){
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
					
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
			case 98 :
				if(vo.getCondition().equals("WEEK")){
					strDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0).replace("/", "") + "000000"; // 금주 시작일
					endDate = endDate.replace("/", "") + "235959"; 
				}else if(vo.getCondition().equals("MONTH")){
					System.out.println("값 확인 : "+vo.getEndDate());
					strDate = vo.getEndDate().substring(0, 6) + "01"  + "000000"; // 당월 시작일
					endDate = endDate.replace("/", "") + "235959";
				}
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
			case 99 :
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), "L_CATE_NAME", filter, 0));
				break;
			case Globals.DASH_VOC_CALLTEXT:
				
				strDate = endDate.replace("/", "") + "000000";
				endDate = endDate.replace("/", "") + "235959";
				
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{strDate, endDate}, 0));
				break;
		}
		
		filterSet = new FilterSet[filterList.size()];
		
		for(int i=0; i < filterList.size(); i++){
			filterSet[i] = (FilterSet) filterList.get(i);
		}
		
		return filterSet;
	}
	
	*//**
	 * GroupBySet 설정.
	 * @return GroupBySet[]
	 *//*
	private GroupBySet[] createGroupBySet(int con, DashBoardVo vo, String keyword){

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupList = new ArrayList<GroupBySet>();

		switch (con) {
			case Globals.DASH_VOC_NEEDS:
			case Globals.DASH_VOC_INCREANDDECRE:		
			case Globals.DASH_VOE_LCLS:
				if(vo.getCondition().equals(Globals.GROUP_WEEK)){
					groupList.add(new GroupBySet(Globals.GROUP_WEEK, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
				}else{
					groupList.add(new GroupBySet(Globals.GROUP_MONTH, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
				}
				break;
			case Globals.DASH_VOC_INTEREST:
				if(vo.getCondition().equals(Globals.GROUP_WEEK)){
					groupList.add(new GroupBySet(Globals.GROUP_DAY, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
				}else{
					groupList.add(new GroupBySet(Globals.GROUP_WEEK, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
				}
				break;
			case Globals.DASH_VOC_VIP_ISSUE_KEYWORD:
				groupList.add(new GroupBySet(Globals.GROUP_DAY, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
				break;
			case Globals.DASH_VOC_COMPLAIN_KEYWORD:
			case Globals.DASH_VOE_KEYWORD_OLD:
			case Globals.DASH_VOE_KEYWORD:
			case Globals.DASH_VOC_ISSUE:
			case Globals.DASH_VOC_VIP_ISSUE:
				groupList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
				break;
			case Globals.DASH_VOC_COMPLAIN_DOCUMENT_REPORT:
				groupList.add(new GroupBySet(Globals.GROUP_COMPLAIN, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", ""));
				break;
			case Globals.DASH_VOC_ISSUE_DETAIL:
				if(vo.getCondition().equals(Globals.GROUP_WEEK)){
					groupList.add(new GroupBySet(Globals.GROUP_DAY, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", "")); // 키워드 추이
					groupList.add(new GroupBySet(Globals.GROUP_WEEK, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", "")); // 전주/금주 대비
				}else{
					groupList.add(new GroupBySet(Globals.GROUP_WEEK, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", "")); // 키워드 추이
					groupList.add(new GroupBySet(Globals.GROUP_MONTH, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", "")); // 전월/ 당월 대비
				}
				groupList.add(new GroupBySet("KEYWORD", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "")); // 키워드 점유율
				break;
			case Globals.DASH_VOE_LCLS_SHARE:
				groupList.add(new GroupBySet(Globals.GROUP_LCLS_NM, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
				break;
			case Globals.DASH_VOE_DEPT:
				groupList.add(new GroupBySet(Globals.GROUP_DEPT_NM, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
				break;
			case Globals.DASH_VOC_Top10_KEYWORD:
				groupList.add(new GroupBySet("KEYWORD", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC 0 9", "")); // 전주/금주 대비
				break;
			case Globals.DASH_VOC_TopCategory_KEYWORD:
				groupList.add(new GroupBySet("L_CATE_NAME", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "")); // 전주/금주 대비
				break;
			case Globals.DASH_VOC_COUNTRYMINWON:
				groupList.add(new GroupBySet(Globals.GROUP_SENSITIVITY, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "ASC", "")); // 전주/금주 대비
				break;
			case 99 :
				groupList.add(new GroupBySet("KEYWORD", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), keyword, ""));
				break;
			case Globals.DASH_VOC_VOICE:
				groupList.add(new GroupBySet(Globals.GROUP_HOUR, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "ASC", ""));
				break;
			default:
				break;
		}
		
		groupBys = new GroupBySet[groupList.size()];
		
		for(int i=0; i < groupList.size(); i++){
			groupBys[i] = (GroupBySet) groupList.get(i);
		}
		return groupBys;
	}
	
	private OrderBySet[] createOrderBySet(){
		OrderBySet[] orderbySet = null;
		orderbySet = new OrderBySet[]{new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE)	};
		return orderbySet;
	}
	
	
}*/