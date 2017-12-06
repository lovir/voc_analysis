package com.diquest.voc.relationAnalysis.service.impl;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

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
import com.diquest.voc.management.service.impl.CommonSelectBoxDAO;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.relationAnalysis.service.RelationAnalysisService;
import com.diquest.voc.relationAnalysis.vo.RelationAnalysisVo;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("relationAnalysisService")
public class RelationAnalysisServiceImpl implements RelationAnalysisService {
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;

	private static final boolean MARINER_LOGGABLE = false; // 검색엔진 로그 저장 여부
	private static final boolean MARINER_PRINT_QUERY = false; // 검색엔진 검색쿼리 출력 여부
	private static final boolean MARINER_DEBUG = true; // 검색엔진 디버그 사용 여부
	private static final boolean MARINER_PRITER = false; // 개인정보보호 필터(PRITER) 사용 여부

	// SELECTSET 분기
	private static final int SELECTSET_COUNT = 100; // 통계치용 건수 조회용
	private static final int SELECTSET_DETAIL = 101; // 상세페이지 조회용
	private static final int SELECTSET_ALIKE = 102; // 유사문서 조회용
	private static final int SELECTSET_LIST = 103; // 검색결과 리스트
	private static final int SELECTSET_LIST_COMPARE = 104; // 비교분석 검색결과 리스트

	// WHERESET 분기
	private static final int WHERESET_KEYWORD_PERIOD = 200; //(상위 Top10키워드 And x축 해당 기간) 조건
	private static final int WHERESET_KEYWORD_REPORT = 201; // 상위키워드 or 종합리포트 차트 생성조건 or 하단 voc검색결과 전체 건수
	private static final int WHERESET_DETAIL = 203; // 상세보기
	private static final int WHERESET_ALIKE = 204; // 유사문서
	private static final int WHERESET_REPDEPT = 205; // 처리주무부서 조회용 검색식
	// 전체 키워드 추출
	// 감성검색??? 주석처리되어있음
	// 감성분석결과

	// FILTERSET 분기
	
	// GROUPBYSET 분기
	private static final int GROUPBYSET_PERIOD = 402; // 기간별 그룹카운트 조회
	private static final int GROUPBYSET_VOC_CATEGORY = 403;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_DEPT = 404; // 민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_RELATION_KEYWORD = 406; //연관어 그룹셋
	private static final int GROUPBYSET_TREND_KEYWORD = 407; //트렌드 차트 출력용 키워드 그룹필터 설정

	/**
	 * 조회조건과 기간선택으로 트렌드 챠트에 필요한 데이타 리스트 생성
	 * 
	 * @param relationVo
	 *            - 조회할 정보가 담긴 VO
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getSynthesisReport(RelationAnalysisVo relationVo) throws Exception {

		ArrayList<HashMap<String, Object>> keywordPeriodCountList = null;
		LinkedHashMap<String, String> periodMap = getCategories(relationVo); // - 종합리포트 챠트 x축(기간)
		LinkedList<String> dateStrList = new LinkedList<String>();
		for(Object periodKey : periodMap.keySet()){	//x축 핕터링용 날짜 문자열값 리스트
			dateStrList.add((String)periodKey);
		}
		//"," 구분으로 입력된 키워드 구분 저장.
		relationVo = relationVo.setMultiKeyword(relationVo);
		
		String[] keywordArr = relationVo.getKeywordArr(); // - 연관 키워드10개
		if (keywordArr != null) {
			Result[] resultlist = null;
			keywordPeriodCountList = new ArrayList<HashMap<String, Object>>();
			QuerySet querySet = new QuerySet(dateStrList.size());
			for (int i = 0; i < dateStrList.size(); i++) { // - x축 기간 만큼 쿼리 생성
				Query query = null;
				query = new Query();
				query.setSelect(createSelectSet(SELECTSET_COUNT));
				query.setWhere(createWhereSet(WHERESET_KEYWORD_PERIOD, dateStrList.get(i), relationVo));	//x축 기간별 검색식 구성
				query.setFilter(createFilterSet(relationVo));
				query.setGroupBy(createGroupBySet(GROUPBYSET_TREND_KEYWORD, "DESC",relationVo));	//키워드 그룹핑
				query.setFrom(Globals.MARINER_COLLECTION);
				query.setResult(0, 0);
				query.setSearch(true);
				query.setUserName(relationVo.getLogin_Id()); // 로그에 남길 키워드 명시
				query.setDebug(MARINER_DEBUG);
				query.setPrintQuery(MARINER_PRINT_QUERY);
				query.setLoggable(MARINER_LOGGABLE);
				query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
				query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));

				querySet.addQuery(query);
				QueryParser queryParser = new QueryParser();
				//System.out.println("##### 연관키워드 트랜드 분석2 query: " + queryParser.queryToString(query));
			}

			// - 검색 서버로 검색 정보 전송
			CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
			CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
			int returnCode = command.request(querySet);

			// - 쿼리결과에 문제가 없을경우는 1, 있으면 마이너스(-)
			if (returnCode >= 0) {
				ResultSet results = command.getResultSet();
				resultlist = results.getResultList();
			}
			if (resultlist != null) {
				HashMap<String, Object> keywordPeriodMap = new HashMap<String, Object>();
				for(String keyword : keywordArr){
					int[] keywordCount = new int[dateStrList.size()];
					Arrays.fill(keywordCount, 0);
					keywordPeriodMap.put(keyword, keywordCount);
				}
				for (int i = 0; i < resultlist.length; i++) {
					GroupResult[] groupResultlist = resultlist[i].getGroupResults();
					GroupResult xAxisKeywordGroupResult = groupResultlist[0];
					for(int j=0; j < xAxisKeywordGroupResult.groupResultSize(); j++){
						String tempKeywordGroup = new String(xAxisKeywordGroupResult.getId(j)).trim();
						if(tempKeywordGroup != null && !"".equals(tempKeywordGroup)){
							if(keywordPeriodMap.get(tempKeywordGroup) != null){
								int[] tempCountMap = (int[])keywordPeriodMap.get(tempKeywordGroup);
								tempCountMap[i] = xAxisKeywordGroupResult.getIntValue(j);
								keywordPeriodMap.put(tempKeywordGroup, tempCountMap);
							}
						}
					}
				}
				for(String keyword : keywordArr){
					int[] finalKeywordCountArr = (int[])keywordPeriodMap.get(keyword);
					
					HashMap<String, Object> periodCount = new HashMap<String, Object>(); // 챠트에 들어갈 키워드와 카운트 맵
					periodCount.put("name", keyword);
					periodCount.put("data", finalKeywordCountArr);
					keywordPeriodCountList.add(periodCount);
				}
			}
		}
		HashMap<String, Object> synthesisReportMap = new HashMap<String, Object>();
		synthesisReportMap.put("periodList", periodMap.values());
		if (keywordPeriodCountList != null) {
			synthesisReportMap.put("keywordPeriodCountList", keywordPeriodCountList);
		}
		return synthesisReportMap;
	}

	/**
	 * 차트 x축(카테고리) 추출 - 일자별, 주별, 월별 , 반기별, 년별 등
	 * 
	 * @param relationVo
	 * @return
	 */
	private LinkedHashMap<String, String> getCategories(RelationAnalysisVo relationVo) {
		LinkedHashMap<String, String> categoriesMap = null;
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if ("".equals(relationVo.getStartDate()) || "".equals(relationVo.getEndDate())) {
			relationVo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			relationVo.setStartDate(sdf.format(cal.getTime()));
		}
		String startDate = relationVo.getStartDate().replace("/", "") + "000000";
		String endDate = relationVo.getEndDate().replace("/", "") + "235959";
		String condition = relationVo.getCondition();
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
	 * 선택한 조건에 따라서 나온 연관 키워드 (10)개를 가져온다
	 * 
	 * @param ReportVo - 조회할 정보가 담긴 VO
	 * @return 연관 키워드 배열
	 * @exception Exception
	 */
	public LinkedHashMap<String, Integer> getRelationKeywordArr(RelationAnalysisVo relationVo, String type) throws Exception {
		// - type에 따라 연관키워드 갯수 달라짐
		// - compare : 8개 (연관 비교 분석)
		// - type이 ""(빈값) 이면 10개 (연관도 종합분석)
		int limit = 10;
		if ("compare".equals(type)) {
			limit = 8;
		}
		if (relationVo.getKeyword() == null || "".equals(relationVo.getKeyword())) {
			return null;
		}
		//"," 구분으로 입력된 키워드 구분 저장.
		relationVo = relationVo.setMultiKeyword(relationVo);
		
		LinkedHashMap<String, Integer> resultMap = null;
		Result[] resultlist = null;
		Result result = null;
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, relationVo.getKeyword(), relationVo));
		query.setFilter(createFilterSet(relationVo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RELATION_KEYWORD,"DESC 0 10",relationVo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setUserName(relationVo.getLogin_Id()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));

		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();
		//System.out.println("##### 연관키워드 트랜드 분석 키워드를 위한 query: " + queryParser.queryToString(query));

		// - 검색 서버로 검색 정보 전송
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
				resultMap = new LinkedHashMap<String, Integer>();
				int keywordCount = 0;
				for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
					if (keywordCount < limit) {
						String relationKeyword = new String(groupResultlist[0].getId(i)).trim();
						// - 연관검색어구할때 사용된 키워드 제외시키기
						if (!relationVo.getKeyword().toUpperCase().equals(relationKeyword.toUpperCase())) {

							resultMap.put(relationKeyword, groupResultlist[0].getIntValue(i));
							keywordCount++;
						}
					}
				}
			}
		}
		return resultMap;
	}

	/**
	 * 방사형그래프 데이터 생성 - 연관도 데이터 조회
	 * 
	 * @param relationVo
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, Object> getRadarGraph(RelationAnalysisVo relationVo, String type) throws Exception {
		if (relationVo.getKeyword() == null || "".equals(relationVo.getKeyword())) {
			return null;
		}
		HashMap<String, Object> root = new HashMap<String, Object>();
		ArrayList<Object> list = new ArrayList<Object>();
		String keywordTemp = ""; // - 같은 키워드 제외할 변수 (String 누적 함.)
		int relationKeywordLimit = 5;	//연관어 갯수 제한.
		//"," 구분으로 입력된 키워드 구분 저장.
		relationVo = relationVo.setMultiKeyword(relationVo);
		keywordTemp = keywordTemp.concat(relationVo.getKeyword().trim().toUpperCase()); // - 1dpet 키워드는 미리 저장 : 2dept 키워드 구할 때 제외해야 됨.
		String[] exclusionKeyword = makeExclusionKeywordArr(relationVo.getExclusion()); // 제외 키워드 배열 생성
		//제외어 들을 keywordTemp 에 저장
		for(int i=0; exclusionKeyword != null && i < exclusionKeyword.length; i++){
			keywordTemp = keywordTemp.concat("," + exclusionKeyword[i]);
		}
		// - 1depth - "name", "group" 설정

		root.put("name", relationVo.getKeyword().trim());
		root.put("group", "center_depth1");
		

		// - getRadarGraphData : keyword에 해당하는 연관키워드 조회 - 1depth 만 조회
		ArrayList<HashMap<String, String>> depth2 = getSecondDepthRadarGraphData(type, relationVo, relationVo.getKeyword(), keywordTemp, relationKeywordLimit);
		//여기 부턴 DRAMA 이용
		if (depth2 != null) {
			// - 1dept, 2dept 키워드 미리 keywordTemp에 저장 - 3dept에 1,2dept 키워드가 나오면 제외 함.
			for (int i = 0; i < depth2.size(); i++) {
				keywordTemp = keywordTemp.concat("," + depth2.get(i).get("name").toUpperCase());
			}
			
			//DRAMA 이용해서 3Depth 조회 해오기
			for (int j = 0; j < depth2.size(); j++) {
				HashMap<String, String> map = depth2.get(j);
				String name = map.get("name");
				ArrayList<HashMap<String, String>> depth3 = getThirdDepthRadarGraphData(type, name, keywordTemp, relationKeywordLimit); // - 3depth - "name" 설정
				for(int k=0; k < depth3.size(); k++){
					keywordTemp.concat("," + depth3.get(k).get("name").toUpperCase());
				}
				
				HashMap<String, Object> children = new HashMap<String, Object>();
				String keywordDepth = "keyword" + (j + 1);
				
				// - 2depth 설정 - "name", "group" 설정
				children.put("name", name);
				children.put("group", keywordDepth + "_depth2");
				// - 3depth - "group" 설정
				if (depth3 != null) {
					for (int i = 0; i < depth3.size(); i++) {
						depth3.get(i).put("group", keywordDepth + "_depth_etc");
					}
				} 
				else {
					depth3 = new ArrayList<HashMap<String, String>>();
				}

				children.put("children", depth3);
				list.add(children);
			}
			root.put("children", list);
		} else {
			root = new HashMap<String, Object>();
		}
		return root;
	}
	
	/**
	 * 3뎁스 연관키워드 구하기(사용한 키워드 제외) - 3뎁스는 DRAMA 컬렉션에서 연관어를 추출한다.
	 * 
	 * @param type
	 * @param keyword
	 *            - 검색 할 키워드
	 * @param keywordTemp
	 *            - 제외할 키워드 (String 키워드 누적 함.) ex) HTS, 로그인, MTS, 안녕 ....
	 * @param limit
	 *            - 화면에 보여질 갯수
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String, String>> getThirdDepthRadarGraphData(String type, String keyword, String keywordTemp, int relationKeywordLimit) throws Exception {
		ArrayList<HashMap<String, String>> list = new ArrayList<>();
		Result[] resultlist = null;
		Result result = null;
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		
		
		//더미 Query 구성
		SelectSet[] selectSet = new SelectSet[]{new SelectSet(Globals.FIELD_DQ_DOCID)};
		WhereSet[] whereSet = new WhereSet[]{new WhereSet(Globals.FIELD_DQ_DOCID,Protocol.WhereSet.OP_HASALL,"A")};
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setResult(0, 0);
		//Drama 추출 설정 부분
		query.setFaultless(true);
		query.setDebug(true);
		query.setPrintQuery(false);
		query.setLoggable(false);
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setSearch(false);
		query.setValue("dramaCollection", Globals.MARINER_DRAMA_COLLECTION);
		query.setValue("dramaField", "KEYWORD");
		query.setValue("dramaKeyword", keyword);
		query.setValue("dramaOption", "XQ");
		query.setValue("dramaResultSize", String.valueOf(relationKeywordLimit * 2));
		query.setResultModifier("drama");
		querySet.addQuery(query);
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		if(returnCode >= 0){
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			ArrayList<HashMap<String, Object>> relationKwdMapList = new ArrayList<HashMap<String, Object>>();
			ArrayList<String> relationKwdList = new ArrayList<String>();
			if(resultlist != null && resultlist.length > 0){
				result = resultlist[0];
				String dramaResult = result.getValue("dramaResult");
				String[] dramaResultArr = dramaResult.split("\t");
				String dramaResultSizeStr = dramaResultArr[0];	//총 연관어 갯수
				boolean setOpen = false;
				HashMap<String, Object> tempMap = new HashMap<String, Object>(); 
				for(int i=1; i < dramaResultArr.length; i++){
					if( i % 2 == 1){	//연관키워드
						setOpen = true;
						String relationKwd = dramaResultArr[i];
						tempMap.put("name", relationKwd);
					}
					else{	//연관도 점수
						setOpen = false;
						String relationScore = dramaResultArr[i];
						tempMap.put("score", Double.parseDouble(relationScore));
					}
					if(!setOpen){
						relationKwdMapList.add(tempMap);
						relationKwdList.add((String)tempMap.get("name"));
						tempMap = new HashMap<String, Object>();
					}
				}
			}
			int keywordCount = 0;
			for(int i=0; i < relationKwdList.size() && keywordCount < relationKeywordLimit; i++){
				String indexKeyword = relationKwdList.get(i);
				if(!isExistKeyword(keywordTemp, indexKeyword)){
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("name", indexKeyword);
					list.add(map);
					keywordCount++;
				}
			}
			
		}
		return list;
	}

	/**
	 * 연관키워드 구하기(사용한 키워드 제외) - 2뎁스는 검색조건을 적용한 결과의 키워드 그룹핑 결과 추출
	 * 
	 * @param type
	 * @param relationVo
	 * @param keyword
	 *            - 검색 할 키워드
	 * @param keywordTemp
	 *            - 제외할 키워드 (String 키워드 누적 함.) ex) HTS, 로그인, MTS, 안녕 ....
	 * @param limit
	 *            - 화면에 보여질 갯수
	 * @return
	 * @throws Exception
	 */
	public ArrayList<HashMap<String, String>> getSecondDepthRadarGraphData(String type, RelationAnalysisVo relationVo, String keyword, String keywordTemp, int relationKeywordLimit) throws Exception {
		// - type에 따라 연관키워드 갯수 달라짐
		// - compare : 8개 (연관 비교 분석)
		// - type이 ""(빈값) 이면 10개 (연관도 종합분석)
		/*if ("compare".equals(type)) {
			relationKeywordLimit = "8";
			limit = 8;
		}*/
		ArrayList<HashMap<String, String>> list = null;
		Result[] resultlist = null;
		Result result = null;
		
		Query query = new Query("","");
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, keyword, relationVo)); // 기존검색조건에 해당연관어의 연관어구하기
		query.setFilter(createFilterSet(relationVo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_RELATION_KEYWORD, "DESC 0 " + relationKeywordLimit,relationVo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		QueryParser queryParser = new QueryParser();
		//System.out.println("!!!!!!!!!!!!!!!! 연관도 분석 그림 query");
		//System.out.println(queryParser.whereSetToString(createWhereSet(WHERESET_KEYWORD_REPORT, keyword, relationVo)));
		//System.out.println(queryParser.filterSetToString(createFilterSet(relationVo)));
		//에러 부분
		//System.out.println("##### 연관도 분석 그림 2Dpeth query: " + queryParser.queryToString(query));

		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);
		
		
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
				list = new ArrayList<HashMap<String, String>>();
				int keywordCount = 0;
				for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
					if (keywordCount < relationKeywordLimit) {
						// - relationKeyword : 3dept 키워드 값
						String relationKeyword = new String(groupResultlist[0].getId(i)).trim();

						// - 1, 2dept 키워드에 나오는 값 제외
						if (!isExistKeyword(keywordTemp, relationKeyword)) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("name", relationKeyword);
							list.add(map);
							keywordCount++;
						}
					}
				}
			}
		}
		return list;
	}
	//루트 부터 마지막 키워드 까지 비교해서 키워드가 중복되는지 체크
	boolean isExistKeyword(String keywordTemp, String targetKwd){
		boolean exist = false;
		String[] keywordArr = keywordTemp.split(",");
		for(int i=0; i < keywordArr.length; i++){
			if(keywordArr[i].toUpperCase().trim().equals(targetKwd.toUpperCase())) return true;
		}
		return exist;
	}
	
	//제외어 배열 생성 함수
	String[] makeExclusionKeywordArr(String exclusionStr){
		String[] retunArr = null;
		if(exclusionStr != null){
			String[] exKwdArr = exclusionStr.split(",");
			ArrayList<String> exclusionList = new ArrayList<String>();
			for(int i=0; exKwdArr != null && i < exKwdArr.length; i++){
				if(!"".equals(exKwdArr[i])){
					exclusionList.add(exKwdArr[i]);
				}
			}
			if(exclusionList.size() > 0)
				retunArr = exclusionList.toArray(new String[exclusionList.size()]);		
		}
		return retunArr;
	}
	/**
	 * VOC 검색결과
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(RelationAnalysisVo relationVo) throws Exception {

		HashMap<String, Object> searchResult = new HashMap<String, Object>();

		ArrayList<HashMap<String, Object>> groupResult = null;
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		String share = "0";
		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();
		int startNo = (relationVo.getCurrentPage() - 1) * relationVo.getPageSize();
		int endNo = startNo + relationVo.getPageSize() - 1;
		
		//"," 구분으로 입력된 키워드 구분 저장.
		relationVo = relationVo.setMultiKeyword(relationVo);
		
		QuerySet querySet = new QuerySet(1);
		//검색결과 리스트 용 쿼리
		Query query = new Query(startTag, endTag);
		if(relationVo.getPageType().equals("compare"))	query.setSelect(createSelectSet(SELECTSET_LIST_COMPARE));	//연관도 비교 분석 인 경우
		else											query.setSelect(createSelectSet(SELECTSET_LIST));	//연관도 분석 인 경우
		query.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, relationVo.getKeyword(), relationVo));
		query.setFilter(createFilterSet(relationVo));
		//query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, ""));	//결과 리스트 탭 구분용(그룹 필터링용). 현재는 사용하지 않음.
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(startNo, endNo);
		query.setSearch(true);
		query.setUserName(relationVo.getLogin_Id()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		if (MARINER_PRITER) {
			query.setResultModifier("priter");
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}

		querySet.addQuery(query);

		QueryParser queryParser = new QueryParser();
		//System.out.println("##### 연관도분석 하단 결과리스트 검색결과: " + queryParser.queryToString(query));

		// - 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);

		// - resultlist[0]이 해당 기간에 속한 키워드에 관한 문서 검색결과 : result
		// - resultlist[1]이 해당 기간에 추출된 keyword 전체 결과 : TotalKeyWordResult
		Result searchListresult = null;	//검색결과 리스트 Result
		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if (resultlist != null) {
				searchListresult = resultlist[0];
			}
		}

		if (searchListresult != null) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			
			// - 리스트 결과 담기
			listResult = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < searchListresult.getRealSize(); i++) {
				HashMap<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put(Globals.FIELD_DQ_DOCID, new String(searchListresult.getResult(i, 0)));
				resultMap.put(Globals.FIELD_VOCNO, new String(searchListresult.getResult(i, 1)));
				String title = new String(searchListresult.getResult(i, 2));
				if(title == null || "".equals(title)) title = "제목없음";
				resultMap.put(Globals.FIELD_TITLE, new String(title));
				resultMap.put(Globals.FIELD_CONTENT, new String(searchListresult.getResult(i, 3)));
				resultMap.put(Globals.FIELD_CONTENT_ORI, new String(searchListresult.getResult(i, 4)));
				String date = "";
				if (searchListresult.getResult(i, 5) != null) {
					date = new String(searchListresult.getResult(i, 5));
					if (date.length() > 7) {
						date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
					}
				}
				resultMap.put(Globals.FIELD_REGDATE, date);
				resultMap.put(Globals.FIELD_WEIGHT, new String(searchListresult.getResult(i, 6)));
				listResult.add(resultMap);
			}

			searchResult.put("keyword", relationVo.getKeyword()); // 검색 키워드
			searchResult.put("groupResult", groupResult); // 카테고리
			searchResult.put("listResult", listResult); // 리스트
			searchResult.put("totalSize", nf.format(searchListresult.getTotalSize())); // 전체 건수
			searchResult.put("share", share); // 점유율
			int endPage = searchListresult.getTotalSize() / relationVo.getPageSize();
			if (endPage % relationVo.getPageSize() > 0) {
				endPage++;
			}
			if (endPage == 0) {
				endPage = 1;
			}
			searchResult.put("endPage", endPage); // 끝 페이지
			searchResult.put("currentPage", relationVo.getCurrentPage()); // 현재 페이지

			/** pageing setting */
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(relationVo.getCurrentPage());
			paginationInfo.setRecordCountPerPage(relationVo.getPageSize());
			paginationInfo.setPageSize(10);
			paginationInfo.setTotalRecordCount(searchListresult.getTotalSize());
			searchResult.put("paginationInfo", paginationInfo);
		}
		return searchResult;
	}

	/**
	 * VOC 검색결과 - Excel다운로드
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(RelationAnalysisVo relationVo) throws Exception {

		HashMap<String, Object> searchResult = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> listResult = null;
		Result[] resultlist = null;
		Result result = null;
		
		//"," 구분으로 입력된 키워드 구분 저장.
		relationVo = relationVo.setMultiKeyword(relationVo);
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_LIST));
		query.setWhere(createWhereSet(WHERESET_KEYWORD_REPORT, relationVo.getKeyword(), relationVo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_VOC_CATEGORY, relationVo.getKeyword(),relationVo));
		query.setFilter(createFilterSet(relationVo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 999);
		query.setSearch(true);
		query.setUserName(relationVo.getLogin_Id()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
		if (MARINER_PRITER) {
			query.setResultModifier("priter");
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}

		querySet.addQuery(query);

		// - 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if (resultlist != null) {
				result = resultlist[0];
				//if (result != null && result.getGroupResultSize() != 0) {			// 추후 로직 변경 대비
				if(result != null && result.getTotalSize() > 0){
					// - 리스트 결과 담기
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

	/**
	 * VOC 검색_상세페이지
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, String> getdetailViewResult(String id) throws Exception {

		HashMap<String, String> resultMap = null;
		Result[] resultlist = null;
		Result result = null;

		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_DETAIL));
		query.setWhere(createWhereSet(WHERESET_DETAIL, id, new RelationAnalysisVo()));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);
		query.setSearch(true);
		query.setDebug(true);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		if (MARINER_PRITER) {
			query.setResultModifier("priter");
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}

		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);

		// - 검색 서버로 검색 정보 전송
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
			// - 결과 담기
			resultMap = new HashMap<String, String>();
			for (int i = 0; i < result.getRealSize(); i++) {
				resultMap.put(Globals.FIELD_DQ_DOCID, new String(result.getResult(i, 0)));
				resultMap.put(Globals.FIELD_TITLE, new String(result.getResult(i, 1)));
				resultMap.put(Globals.FIELD_CONTENT, new String(result.getResult(i, 2)).replaceAll("\n", "<br/>"));
				resultMap.put(Globals.FIELD_REGDATE, new String(result.getResult(i, 3)));
				resultMap.put(Globals.FIELD_WEIGHT, new String(result.getResult(i, 4)));
				//민원 접수 채널 조회
				String vocChannelCode = new String(result.getResult(i, 5));
				CommonSelectBoxVo commonCode = new CommonSelectBoxVo();
				commonCode.setCode(vocChannelCode);
				String vocChannel = commonSelectBoxDAO.vocChannel(commonCode).getName();
				resultMap.put("VOCCHANNEL",vocChannel );
				//민원 접수 종류 조회 - 민원 쪽 결과 일때만 조회함. (콜센터,SMS에는 해당 카테고리 분류가 없음)
				String voc_category = new String(result.getResult(i, 15));
				if(voc_category != null && voc_category.equals("MINWON")){
					String vocRecTypeCode = new String(result.getResult(i, 6));
					commonCode = new CommonSelectBoxVo();
					commonCode.setCode(vocRecTypeCode);
					String vocRecType = commonSelectBoxDAO.vocRecType(commonCode).getName();	
					resultMap.put("VOCRECTYPE",vocRecType );
				}
				//민원 접수 분류 조회
				//접수 분류는 [민원(MINWON)], [콜센터(CALL),SMS(SMS)] 유형에 따라 조회 쿼리가 다르다.
				String kindCode = new String(result.getResult(i, 7));
				String partCode = new String(result.getResult(i, 8));
				String itemCode = new String(result.getResult(i, 9));
				String category = "";
				commonCode = new CommonSelectBoxVo();
				if(voc_category != null && voc_category.equals("MINWON")){	//민원 일때
					//VOC 대분류 코드값이 존재하는 경우에만 대분류명 조회
					try{
						if(kindCode != null && !"".equals(kindCode)){
							commonCode.setCode(kindCode);
							CommonSelectBoxVo resultVo = commonSelectBoxDAO.vocKind(commonCode);
							if(resultVo != null && !"".equals(resultVo.getName())){
								category = resultVo.getName();
								
								//VOC 중분류 코드값이 존재하는 경우에만 중분류명 조회
								if(partCode != null && !"".equals(partCode)){
									commonCode.setCode(partCode);
									commonCode.setpCat1Id(kindCode);
									resultVo = commonSelectBoxDAO.vocKind(commonCode);
									if(resultVo != null && !"".equals(resultVo.getName())){
										category += " - " + resultVo.getName();
										
										//VOC 소분류 코드값이 존재하는 경우에만 소분류명 조회
										if(itemCode != null && !"".equals(itemCode)){
											commonCode.setCode(itemCode);
											commonCode.setpCat1Id(kindCode);
											commonCode.setpCat2Id(partCode);
											resultVo = commonSelectBoxDAO.vocKind(commonCode);
											if(resultVo != null && !"".equals(resultVo.getName())){
												category += " - " + resultVo.getName();
											}
										}
									}
								}
							}
						}
					}
					catch(Exception e){
						throw e;
					}
				}
				else{	//콜센터, SMS 일때
					String kindName = "";
					String partName = "";
					String itemName = "";
					commonCode = new CommonSelectBoxVo();
					CommonSelectBoxVo resultVo = new CommonSelectBoxVo();
					//VOC 대분류 코드값이 존재하는 경우에만 대분류명 조회
					try{
						if(kindCode != null && !"".equals(kindCode)){
							commonCode.setCode(kindCode);
							resultVo = commonSelectBoxDAO.vocCallKind(commonCode);
							if(resultVo != null && !"".equals(resultVo.getName())){
								kindName = resultVo.getName();
								category = kindName;	
							}
						}
						//VOC 중분류 코드값이 존재하는 경우에만 중분류명 조회
						if(partCode != null && !"".equals(partCode)){
							commonCode.setCode(partCode);
							resultVo = commonSelectBoxDAO.vocCallPart(commonCode);
							if(resultVo != null && !"".equals(resultVo.getName())){
								partName = resultVo.getName();
								category += " - " + partName;
							}
							//VOC 소분류 코드값이 존재하는 경우에만 소분류명 조회
							if(itemCode != null && !"".equals(itemCode)){
								commonCode.setCode(itemCode);
								commonCode.setpCat2Id(partCode);
								resultVo = commonSelectBoxDAO.vocCallItem(commonCode);
								if(resultVo != null && !"".equals(resultVo.getName())){
									itemName = resultVo.getName();
									category += " - " + itemName;
								}
							}
						}
					}
					catch(Exception e){
						throw e;
					}
				}
				resultMap.put("CATEGORY", category);
				//처리주무부서 조회
				String repDeptCode = new String(result.getResult(i, 10));
				if(repDeptCode != null && !"".equals(repDeptCode)){
					commonCode = new CommonSelectBoxVo();
					commonCode.setCode(repDeptCode);
					String repDept = commonSelectBoxDAO.metroDept(commonCode).getName();	//new String(result.getResult(i, 10))
					resultMap.put("REPDEPT",repDept );
				}
				String ext_station = new String(result.getResult(i, 11));
				String[] extStationArr = ext_station.split(";");
				ArrayList<String> extStationList = new ArrayList<String>();
				for(int j=0; j < extStationArr.length; j++){
					if(extStationArr[j] != null && !"".equals(extStationArr[j]) && !"NONE".equals(extStationArr[j])){
						boolean isExist = false;
						for(int k=0; k < extStationList.size(); k++){
							if(extStationList.get(k).equals(extStationArr[j])) {
								isExist = true;
								break;
							}
						}
						if(!isExist) extStationList.add(extStationArr[j]);
					}
				}
				StringBuffer extStationSb = new StringBuffer();
				for(int j=0; j < extStationList.size(); j++){
					if(j > 0) extStationSb.append(", ");
					extStationSb.append(extStationList.get(j));
				}
				resultMap.put(Globals.FIELD_EXT_STATION, extStationSb.toString());
				String repLevel = (Globals.metroVocRepLevel.get(new String(result.getResult(i, 12))) == null ) ? "" : Globals.metroVocRepLevel.get(new String(result.getResult(i, 12)));
				resultMap.put("REPLEVEL",repLevel );
				resultMap.put(Globals.FIELD_REPCONT, new String(result.getResult(i, 13)));
				resultMap.put(Globals.FIELD_VOCNO, new String(result.getResult(i, 14)));
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
	public ArrayList<HashMap<String, Object>> getAlikeSearchResult(RelationAnalysisVo relationVo) throws Exception {

		ArrayList<HashMap<String, Object>> searchResult = null;
		Result[] resultlist = null;
		Result result = null;

		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_ALIKE));
		query.setWhere(createWhereSet(WHERESET_ALIKE, "", relationVo));
		query.setOrderby(createOrderBySet());
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 9);
		query.setSearch(true);
		query.setUserName(relationVo.getLogin_Id()); // 로그에 남길 키워드 명시
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte)(Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED | Protocol.SearchOption.PERCENT));
		if (MARINER_PRITER) {
			query.setResultModifier("priter");
			query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		}

		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(query);

		// - 검색 서버로 검색 정보 전송
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
			// - 리스트 결과 담기
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

	// 입력한 키워드는 연관검색어에서 제외
	public boolean overlapCheck(String[] keywordArr, String relationKeyword) {
		for (String keyword : keywordArr) {
			if (keyword.equals(relationKeyword)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 셀렉트 박스 - 처리주무부서 검색조건 구성
	 * 
	 * @param SocialRelationAnalysisVo
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String, Object> getRepDeptList(RelationAnalysisVo relationVo) throws Exception {
		LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>(); // 최종 리턴 맵
		ArrayList<HashMap<String, String>> metroDeptList = new ArrayList<HashMap<String, String>>();	// 처리주무부서 리스트, 셀렉트 박스 생성용
		Result[] resultlist = null;

		//처리 주무부서 쿼리 추가
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_COUNT));
		query.setWhere(createWhereSet(WHERESET_REPDEPT, "", relationVo)); // case를 위한 번호, 키워드, VO	
		
		query.setFilter(createFilterSet(relationVo));
		//처리주무부서 그룹필터처리
		query.setGroupBy(createGroupBySet(GROUPBYSET_DEPT, "", relationVo));
		query.setFrom(Globals.MARINER_COLLECTION);
		query.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
		query.setSearch(true);
		query.setDebug(MARINER_DEBUG);
		query.setPrintQuery(MARINER_PRINT_QUERY);
		query.setLoggable(MARINER_LOGGABLE);
		query.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE));
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();
		//System.out.println("이슈키워드 랭킹 처리주무부서 그룹 조회 쿼리 : " + queryParser.queryToString(query));
		
		// - 검색 서버로 검색 정보 전송
		CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
		CommandSearchRequest command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = command.request(querySet);
		if (returnCode >= 0) {
			ResultSet results = command.getResultSet();
			resultlist = results.getResultList();
			if (resultlist != null) {
				GroupResult[] deptGroupResult =  resultlist[0].getGroupResults();
				// 처리주무부서 그룹결과
				ArrayList<String> cdDeptList = new ArrayList<String>();	//부서코드 정렬순서 저장
				for(int j = 0; j < deptGroupResult[0].groupResultSize(); j++){
					String deptCode = new String(deptGroupResult[0].getId(j));
					if(deptCode != null && !"".equals(deptCode)){
						cdDeptList.add(deptCode);
					}
				}
				
				HashMap<String, Object> paramMap = new HashMap<String,Object>();
				paramMap.put("codeList", cdDeptList);
				if(cdDeptList.size() > 0){
					HashMap<String, Object> selectDeptCodeTable = new HashMap<String,Object>(); 
					List<HashMap<String, Object>> deptFullList = commonSelectBoxDAO.selectMetroList(paramMap);	//부서코드 리스트로 처리주무부서 리스트 조회
					for(HashMap<String, Object> tempMap : deptFullList){	//조회한 부서정보 MAP에 부서코드를 키값으로 저장
						selectDeptCodeTable.put((String)tempMap.get("CODE"), (String)tempMap.get("NAME"));
					}
					
					//처리주무부서 순서를 알파벳,가나다 순으로 정렬
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
					//처리주무부서 맵에 저장
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
			
		}
		returnMap.put("metroDeptList", metroDeptList);
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
		case SELECTSET_LIST: // VOC 검색 결과
			selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE), // 수집문서ID
					new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE), // 제목
					new SelectSet(Globals.FIELD_TITLE, (byte) (Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 120), // HIGHLIGHT
					new SelectSet(Globals.FIELD_CONTENT, (byte) (Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT), 400),
					new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE),
					new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE), 
					new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE), 
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
		case SELECTSET_DETAIL: // 상세페이지
			selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE), // 수집문서ID
					new SelectSet(Globals.FIELD_TITLE, Protocol.SelectSet.NONE), // 제목
					new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE), // 본문
					new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE), // 등록일
					new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE), // 가중치
					new SelectSet(Globals.FIELD_CDVOCCHANNEL, Protocol.SelectSet.NONE), // 접수채널
					new SelectSet(Globals.FIELD_CDVOCRECTYPE, Protocol.SelectSet.NONE), // 접수종류
					new SelectSet(Globals.FIELD_CDVOCKIND, Protocol.SelectSet.NONE), // 접수분류 - 대
					new SelectSet(Globals.FIELD_CDVOCPART, Protocol.SelectSet.NONE), // 접수분류 - 중
					new SelectSet(Globals.FIELD_CDVOCITEM, Protocol.SelectSet.NONE), // 접수분류 - 소
					new SelectSet(Globals.FIELD_CDREPDEPT, Protocol.SelectSet.NONE), // 답변 부서 코드
					new SelectSet(Globals.FIELD_EXT_STATION, Protocol.SelectSet.NONE), // 민원발생역
					new SelectSet(Globals.FIELD_CDVOCREPLEVEL, Protocol.SelectSet.NONE), // 답변만족도 등급
					new SelectSet(Globals.FIELD_REPCONT, Protocol.SelectSet.NONE), // 답변만족도 등급
					new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE), // VOC문서 번호
					new SelectSet(Globals.FIELD_VOC_CATEGORY, Protocol.SelectSet.NONE), // 내부 VOC 민원(MINWON), 콜센터(CALL), SMS(SMS) 구분용
			};
			break;
		case SELECTSET_ALIKE: // 유사문서
			selectSet = new SelectSet[] { new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE), // 수집문서ID
					new SelectSet(Globals.FIELD_TITLE, (byte) (Protocol.SelectSet.NONE)), // 제목
					new SelectSet(Globals.FIELD_CONTENT, (byte) (Protocol.SelectSet.NONE)), // 본문
					new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE), // 등록일
					new SelectSet(Globals.FIELD_WEIGHT, Protocol.SelectSet.NONE), // 가중치
					new SelectSet(Globals.FIELD_VOCNO, Protocol.SelectSet.NONE), // VOC문서 번호
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
	 * @param flag
	 * @param keyword
	 * @param relationVo
	 * @return
	 */
	private WhereSet[] createWhereSet(int flag, String keyword, RelationAnalysisVo relationVo) {
		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();

		String[] exclusionKeyword = makeExclusionKeywordArr(relationVo.getExclusion());	// 제외 키워드 배열 생성

		//WhereSet 구성 시작
		whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
		whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0)); // 전체 검색용
		if (!relationVo.getVocChannel().isEmpty() && !relationVo.getVocChannel().equals(Globals.COM_SELECT_ALL)) { // 접수채널
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCCHANNEL, Protocol.WhereSet.OP_HASALL, relationVo.getVocChannel(), 0));
		}
		if (!relationVo.getVocRecType().isEmpty() && !relationVo.getVocRecType().equals(Globals.COM_SELECT_ALL)) { // VOC종류
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCRECTYPE, Protocol.WhereSet.OP_HASALL, relationVo.getVocRecType(), 0));
		}
		if (!relationVo.getVocKind().isEmpty() && !relationVo.getVocKind().equals(Globals.COM_SELECT_ALL)) { // 접수종류 - 대
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, relationVo.getVocKind(), 0));
		}
		if (!relationVo.getVocPart().isEmpty() && !relationVo.getVocPart().equals(Globals.COM_SELECT_ALL)) { // 접수종류 - 중
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCPART, Protocol.WhereSet.OP_HASALL, relationVo.getVocPart(), 0));
		}
		if (!relationVo.getVocItem().isEmpty() && !relationVo.getVocItem().equals(Globals.COM_SELECT_ALL)) { // 접수종류 - 소
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCITEM, Protocol.WhereSet.OP_HASALL, relationVo.getVocItem(), 0));
		}
		if (!relationVo.getRepLevel().isEmpty() && !relationVo.getRepLevel().equals(Globals.COM_SELECT_ALL)) { // 만족도 등급
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDVOCREPLEVEL, Protocol.WhereSet.OP_HASALL, relationVo.getRepLevel(), 0));
		}
		if (!relationVo.getMetroDept().isEmpty() && !relationVo.getMetroDept().equals(Globals.COM_SELECT_ALL)) { // 처리 주무 부서(코드)
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, relationVo.getMetroDept(), 0));
		}
		whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
		switch (flag) {
		case WHERESET_KEYWORD_PERIOD: //(상위 Top10키워드 And x축 해당 기간) 조건  - 파라미터: condition, keywordArr, 헤당 날짜
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(relationVo.getCondition(), Protocol.WhereSet.OP_HASALL, keyword, 0)); //x축 한지점에 해당하는 기간 검색.
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASANY, relationVo.getKeywordArr()));	//Top10 키워드만 검색하기 위한 검색식
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			if (exclusionKeyword != null) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASANY, exclusionKeyword));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			break;
		
		case WHERESET_KEYWORD_REPORT: // 연관키워드 상위5개 구하기 or 연관 키워드 트렌드 챠트 조건 or 점유율 차트 구할때 전체건수에 대한 조건 or 하단 VOC검색결과 리스트 조건
			if (keyword != null && !"".equals(keyword)) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, keyword, 500));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, keyword, 100));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, keyword, 0)); // 추출 키워드 검색
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				if (exclusionKeyword != null) {
					whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
					whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASANY, exclusionKeyword));
				}
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}
		
			/*if (exclusionKeyword != null) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
				whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, exclusionKeyword));
			}*/
			break;
		case WHERESET_DETAIL: // 상세페이지 보기
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, keyword, 0)); // keyword에 dq_docid들어있음
			break;

		case WHERESET_ALIKE: // 유사문서검색 조건
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, relationVo.getTitle(), 1000));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, relationVo.getContent(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASANY, relationVo.getDq_docid(), 0)); // 같은id제거하기
			break;
		case WHERESET_REPDEPT :	//처리주무부서 조회용 검색식 - 처리주무부서 그룹핑용 검색식 이므로 WHERESET에서 CDREPDEPT는 제외한다.
			whereSetList = new ArrayList<>();
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_ALL, Protocol.WhereSet.OP_HASALL, "A", 0)); // 전체 검색용
			if (!relationVo.getVocChannel().isEmpty() && !relationVo.getVocChannel().equals(Globals.COM_SELECT_ALL)) { // 접수채널
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCCHANNEL, Protocol.WhereSet.OP_HASALL, relationVo.getVocChannel(), 0));
			}
			if (!relationVo.getVocRecType().isEmpty() && !relationVo.getVocRecType().equals(Globals.COM_SELECT_ALL)) { // VOC종류
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCRECTYPE, Protocol.WhereSet.OP_HASALL, relationVo.getVocRecType(), 0));
			}
			if (!relationVo.getVocKind().isEmpty() && !relationVo.getVocKind().equals(Globals.COM_SELECT_ALL)) { // 접수종류 - 대
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCKIND, Protocol.WhereSet.OP_HASALL, relationVo.getVocKind(), 0));
			}
			if (!relationVo.getVocPart().isEmpty() && !relationVo.getVocPart().equals(Globals.COM_SELECT_ALL)) { // 접수종류 - 중
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCPART, Protocol.WhereSet.OP_HASALL, relationVo.getVocPart(), 0));
			}
			if (!relationVo.getVocItem().isEmpty() && !relationVo.getVocItem().equals(Globals.COM_SELECT_ALL)) { // 접수종류 - 소
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCITEM, Protocol.WhereSet.OP_HASALL, relationVo.getVocItem(), 0));
			}
			if (!relationVo.getRepLevel().isEmpty() && !relationVo.getRepLevel().equals(Globals.COM_SELECT_ALL)) { // 만족도 등급
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDVOCREPLEVEL, Protocol.WhereSet.OP_HASALL, relationVo.getRepLevel(), 0));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, relationVo.getKeyword(), 500));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
			whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, relationVo.getKeyword(), 100));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, relationVo.getKeyword(), 1000)); // 추출 키워드 검색
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			if (exclusionKeyword != null) {
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, exclusionKeyword));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
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
	 * FilterSet 설정 1.
	 * 
	 * @return FilterSet[]
	 */
	private FilterSet[] createFilterSet(RelationAnalysisVo realtionVo) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if ("".equals(realtionVo.getStartDate()) || "".equals(realtionVo.getEndDate())) {
			realtionVo.setEndDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, -6);
			realtionVo.setStartDate(sdf.format(cal.getTime()));
		}
		String startDate = realtionVo.getStartDate().replace("/", "") + "000000";
		String endDate = realtionVo.getEndDate().replace("/", "") + "235959";
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
	 * GroupBySet 설정
	 * 
	 * @return GroupBySet[]
	 */
	private GroupBySet[] createGroupBySet(int flag, String option, RelationAnalysisVo relationAnalysisVo) { // flag = 필드 or 키워드갯수
		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupbysetList = new ArrayList<GroupBySet>();
		switch (flag) {
		case GROUPBYSET_PERIOD:	//기간별 그룹핑
			groupbysetList.add(new GroupBySet(option, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", ""));
			break;
		case GROUPBYSET_VOC_CATEGORY:	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
			groupbysetList.add(new GroupBySet(option, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", option));
			break;
		case GROUPBYSET_RELATION_KEYWORD:	//키워드 그룹핑
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), option, ""));
			break;
		case GROUPBYSET_DEPT:	//처리주무부서 셀렉트박스 생성용
			groupbysetList.add(new GroupBySet(Globals.GROUP_CDREPDEPT, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", relationAnalysisVo.getMetroDept(), ""));	
			break;
		case GROUPBYSET_TREND_KEYWORD:	//트렌드 차트 출력용 키워드 그룹필터 설정
			//groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), option, relationAnalysisVo.getKeywordArr()));
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), option));
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
		orderbySet = new OrderBySet[] { new OrderBySet(true, "REGDATE", Protocol.OrderBySet.OP_PREWEIGHT) };
		return orderbySet;
	}

}