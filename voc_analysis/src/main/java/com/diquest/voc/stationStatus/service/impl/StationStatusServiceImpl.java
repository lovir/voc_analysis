package com.diquest.voc.stationStatus.service.impl;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
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
import com.diquest.voc.stationStatus.service.*;
import com.diquest.voc.stationStatus.vo.StationStatusVo;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.impl.CommonSelectBoxDAO;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.openapi.service.DqXmlBuilderJ;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("stationStatusService")


public class StationStatusServiceImpl implements StationStatusService{
	
	/** stationStatusDAO */
	@Resource(name = "stationStatusDAO")
	private StationStatusDAO stationStatusDAO;
	
	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;
	
	private Gson gson = null;
	private static final boolean MARINER_LOGGABLE = true; // 검색엔진 로그 저장 여부
	private static final boolean MARINER_PRINT_QUERY = false; // 검색엔진 검색쿼리 출력 여부
	private static final boolean MARINER_DEBUG = true; // 검색엔진 디버그 사용 여부
	private static final boolean MARINER_PRITER = false; //개인정보보호 필터(PRITER) 사용 여부

	// SELECTSET 분기
	private static final int SELECTSET_STATUS = 100; // 역별현황 조회용
	private static final int SELECTSET_DETAIL = 101; // 상세페이지 조회용
	private static final int SELECTSET_LIST = 103; // 검색결과 리스트
	private static final int SELECTSET_COUNT = 104; // 통계치용 건수 조회용
	
	// WHERESET 분기
	private static final int WHERESET_STATUS = 200; 		// 역별현황 조회조건	
	private static final int WHERESET_STATUSDETAIL = 201; 	// 각 역별 키워드/긍부정 조회조건	
	private static final int WHERESET_KEYWORDS = 202; 		// 선택역 키워드리스트
	private static final int WHERESET_SEARCHKEYWORD = 203; 	// 키워드에 대한 하단 VOC 검색결과
	private static final int WHERESET_DETAIL = 204; 		// 상세보기
	private static final int WHERESET_METRODEPT = 205;		// 처리주무부서 그룹 카운트
	
	// FILTERSET 분기
	private static final int FILTERSET_STATUS = 300; 		// 역별현황
	private static final int FILTERSET_STATUSDETAIL = 301; // 각 역별 키워드/긍부정 조회조건	
	private static final int FILTERSET_LIST = 302; // VOC 리스트 검색
	// GROUPBYSET 분기
	private static final int GROUPBYSET_STATUS = 400; 		// 역별현황
	private static final int GROUPBYSET_STATUSDETAIL = 401; // 각 역별 키워드/긍부정 조회조건	
	private static final int GROUPBYSET_KEYWORDS = 402; 	// 선택역 키워드리스트
	private static final int GROUPBYSET_VOC_CATEGORY = 403;	//민원(MINWON), 콜센터(CALL), SMS(SMS)로 구분하는 그룹필드
	private static final int GROUPBYSET_RANKING = 404; 		// 종합랭킹, 상위키워드 10개 추출
	private static final int GROUPBYSET_METRODEPT = 405;	// 처리주무부서 그룹 카운트 
	
	public String getStationChart(StationStatusVo vo) throws Exception{

		/* 1. 각 조건별(키워드, 긍부정, 감정)로 arraylist에 파싱
		*  2. 각 arrayList 정렬
		*  3. LinkedHashMap에 삽입
		*/
		int stationLen = Integer.parseInt(vo.getStationPageSize());
		int startFlag = (Integer.parseInt(vo.getStationCurrentPageNo())-1)*Integer.parseInt(vo.getStationPageSize());		// 페이징을 위해 시작플래그
		int endFlag = startFlag + Integer.parseInt(vo.getStationPageSize());
		if(endFlag > Integer.parseInt(vo.getStationTotalSize()) ){
			endFlag = Integer.parseInt(vo.getStationTotalSize()) - 1;
		}
		
		HashMap<String, Object> stationChart = new HashMap<String, Object>();
		ArrayList<HashMap<String, String>> stationChartList = new ArrayList<HashMap<String, String>>(); // json으로 변환할 리스트
		ArrayList<HashMap<String, String>> stationList = new  ArrayList<HashMap<String, String>>();	// 역 건수
		ArrayList<HashMap<String, String>> keywordList = new ArrayList<HashMap<String, String>>();	// 키워드 리스트
		ArrayList<HashMap<String, String>> pnnList = new ArrayList<HashMap<String, String>>();		// 긍부정 리스트
		
		
		Result[] resultlist = null;
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_STATUS));
		query.setWhere(createWhereSet(WHERESET_STATUS, vo, "")); // case를 위한 번호, 키워드, VO
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_STATUS, vo));
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
				
				// 총 역 결과 리스트에 담기		
				for(int j = 0; j < groupResults[0].groupResultSize(); j++){

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("stationName", new String(groupResults[0].getId(j)).trim());
					map.put("stationCnt", Integer.toString(groupResults[0].getIntValue(j)));
					stationList.add(map);
				}

				// 결과값보다 노출 갯수가 많은 경우 - ex) 결과 값 / flag가 계산된 상태에서 노선 선택
				stationLen = Integer.parseInt(vo.getStationPageSize());
				if(stationList.size() < Integer.parseInt(vo.getStationPageSize())){
					stationLen = stationList.size();
				}
				if((endFlag-startFlag) > stationLen)				
					endFlag = (endFlag-startFlag) - (Integer.parseInt(vo.getStationPageSize()) - stationLen);
				
				/////////////// 역별로 쿼리 생성
				Result[] resultlistDetail = null;
				QuerySet querySetDetail = new QuerySet(endFlag-startFlag);
							
				for(int j = startFlag; j < endFlag; j++){
	
					Query queryDetail = new Query();
					queryDetail.setSelect(createSelectSet(SELECTSET_STATUS));
					queryDetail.setWhere(createWhereSet(WHERESET_STATUSDETAIL, vo, stationList.get(j).get("stationName"))); // case를 위한 번호, 키워드, VO
					queryDetail.setFilter(createFilterSet(vo));
					queryDetail.setGroupBy(createGroupBySet(GROUPBYSET_STATUSDETAIL, vo));
					queryDetail.setFrom(Globals.MARINER_COLLECTION);
					queryDetail.setResult(0, 0);// setResult은 페이징관련 인데, 우리는 데이터분석이므로 0,0으로 값세팅
					queryDetail.setSearch(true);
					queryDetail.setUserName(vo.getUserId()); //로그에 남길 키워드 명시
					queryDetail.setDebug(MARINER_DEBUG);
					queryDetail.setPrintQuery(MARINER_PRINT_QUERY);
					queryDetail.setLoggable(MARINER_LOGGABLE);
					queryDetail.setThesaurusOption((byte) Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
					queryDetail.setSearchOption((byte)(Protocol.SearchOption.CACHE | Protocol.SearchOption.BANNED | Protocol.SearchOption.STOPWORD));
					if(MARINER_PRITER){
						query.setResultModifier("priter"); 
						query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
					}
					querySetDetail.addQuery(queryDetail);
					//System.out.println("##### 역별현황분석 query2: " + queryParser.queryToString(queryDetail));
					//System.out.println(j+"번째 쿼리:");
				}
				
				CommandSearchRequest.setProps(Globals.MARINER_IP, Globals.MARINER_PORT, Globals.MARINER_POOLWAITTIMEOUT, Globals.MARINER_MINPOOLSIZE, Globals.MARINER_MAXPOLLSIZE);
				CommandSearchRequest commandDetail = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
				
				int returnCodeDetail = commandDetail.request(querySetDetail);
				
				// 쿼리결과에 문제가 없을경우는 1, 있으면 마이너스(-)
				if (returnCodeDetail >= 0) {
					ResultSet results = commandDetail.getResultSet();
					resultlistDetail = results.getResultList();
				}
				
				if (resultlistDetail != null) {
					
					// 역별 쿼리결과 갯수만큼		
					for(int j = 0 ; j < resultlistDetail.length; j++){
						if (resultlistDetail[j] != null && resultlistDetail[j].getGroupResultSize() != 0) {	
							Result resultDetail = resultlistDetail[j];										
							GroupResult[] groupResultsDetail = resultDetail.getGroupResults();	
							
							// 그룹갯수만큼
							for(int k = 0; k < 2; k++){	
								
								// 0 : keyword , 1 : 긍부정	그룹				
								if(k == 0){
									//System.out.println("**"+stationList.get(j).get("stationName")+"역 키워드:"+new String(groupResultsDetail[k].getId(0)));
									HashMap<String, String> map = new HashMap<String, String>();
									// 역명과 동일한 키워드일 경우 제외
									for(int m=0; m < groupResultsDetail[k].groupResultSize(); m++){
										if(!stationList.get(j).get("stationName").equals(new String(groupResultsDetail[k].getId(m)))){
											map.put("KEYWORD", new String(groupResultsDetail[k].getId(m)));
											break;
										}
									}
									keywordList.add(map);
								}
									
								else if(k == 1){
									HashMap<String, String> map = new HashMap<String, String>();
									
									String posCnt = "0";
									String negCnt = "0";
									String neuCnt = "0";
									
									for(int m=0; m < groupResultsDetail[k].groupResultSize(); m++){
										if(new String(groupResultsDetail[k].getId(m)).equals("긍정")){	
											posCnt = Integer.toString(groupResultsDetail[k].getIntValue(m));
											
											/*map.put("NAMEPOS", new String(groupResultsDetail[k].getId(m)));											
											map.put("COUNTPOS", Integer.toString(groupResultsDetail[k].getIntValue(m)));*/
											
										}else if(new String(groupResultsDetail[k].getId(m)).equals("부정")){
											negCnt = Integer.toString(groupResultsDetail[k].getIntValue(m));
											
											/*map.put("NAMENEG", new String(groupResultsDetail[k].getId(m)));
											map.put("COUNTNEG", Integer.toString(groupResultsDetail[k].getIntValue(m)));*/
											
										}else if(new String(groupResultsDetail[k].getId(m)).equals("중립")){
											neuCnt = Integer.toString(groupResultsDetail[k].getIntValue(m));
											
											/*map.put("NAMENEU", new String(groupResultsDetail[k].getId(m)));
											map.put("COUNTNEU", Integer.toString(groupResultsDetail[k].getIntValue(m)));*/
											
										}									
									}
									
									map.put("NAMEPOS", "긍정");
									map.put("COUNTPOS", posCnt);
									
									map.put("NAMENEG", "부정");
									map.put("COUNTNEG", negCnt);
									
									map.put("NAMENEU", "중립");
									map.put("COUNTNEU", neuCnt);
									
									pnnList.add(map);
									
								}
									
								
							}
						}
					}
				}
			}
		}		
		// 총 결과 저장
		if(stationList != null && stationList.size() > 0 && (endFlag-startFlag) > 0){
			
			for(int i = 0; i < (endFlag-startFlag); i++){
				// 역 이름, 키워드, 긍/부정 결과에 삽입 
				String stationName = stationList.get(i+startFlag).get("stationName");
				String stationCnt = stationList.get(i+startFlag).get("stationCnt");
				String keyword = "";
				String posCnt = "0";
				String negCnt = "0";
				String neuCnt = "0";
				int line = 0;
								
				keyword = keywordList.get(i).get("KEYWORD");
				posCnt = pnnList.get(i).get("COUNTPOS");
				negCnt = pnnList.get(i).get("COUNTNEG");
				neuCnt = pnnList.get(i).get("COUNTNEU");
				
				// 호선 구하기 
				//System.out.println("***********:"+stationStatusDAO.selectLine(stationName.substring(0, stationName.length()-1)));
				String stationNameTmp = "";
				// 대부분 역명에 '역'이 붙어서 출력되지만 안붙는 경우도 있음
				if(!stationName.substring(stationName.length()-1, stationName.length()).equals("역")){
					stationNameTmp = stationName;
				}else{
					stationNameTmp = stationName.substring(0, stationName.length()-1);
				}
				List<String> lineList = stationStatusDAO.selectLine(stationNameTmp);
				if(lineList == null && lineList.size() == 0){
					line = 0;
				}else{
					try{
						line = Integer.parseInt(lineList.get(0));
					}catch(Exception e){
						
					}
				}
				
				
				/*for(int j=0; j < keywordList.size(); j++){
					//if(stationName.equals(keywordList.get(j).get("NAME"))){
						keyword = keywordList.get(j).get("KEYWORD");
						break;
					//}
				}
				for(int j=0; j < pnnList.size(); j++){
				//	if(pnnList.get(j).get("NAME").equals("긍정"))
						posCnt = pnnList.get(j).get("COUNTPOS");	
				//	else if(pnnList.get(j).get("NAME").equals("부정"))
						negCnt = pnnList.get(j).get("COUNTNEG");
				//	else if(pnnList.get(j).get("NAME").equals("중립"))
						neuCnt = pnnList.get(j).get("COUNTNEU");
						
						break;
				//	}
				}*/
				/*for(int j=0; j < senseList.size(); j++){
					if(stationName.equals(senseList.get(j).get("NAME"))){
						senKeyword = senseList.get(j).get("SEN_KEYWORD");
						break;
					}
				}*/
				

				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("NAME", stationName);
				map.put("COUNT", stationCnt);
				map.put("KEYWORD", keyword);
				map.put("POSITIVE", posCnt);
				map.put("NEGATIVE", negCnt);
				map.put("NEUTRAL", neuCnt);
				map.put("LINE", Integer.toString(line));
				stationChartList.add(map);
			}
		}
		
		int endPage = stationList.size() / Integer.parseInt(vo.getStationPageSize());
		if (endPage % Integer.parseInt(vo.getStationPageSize()) > 0) {
			endPage++;
		}
		if (endPage == 0) {
			endPage = 1;
		}
		
		vo.setStationTotalSize(Integer.toString(stationList.size()));
		
		//// 페이징에 필요한 변수  
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("NAME", "endPage");
		map.put("COUNT",Integer.toString(endPage));
		stationChartList.add(0, map);
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("NAME", "totalSize");
		map2.put("COUNT", Integer.toString(stationList.size()));
		stationChartList.add(1, map2);
		
		// 그룹 결과 
		stationChart.put("stationChart", stationChartList);
		if(vo.getPageType().equals("stationStatus")){			// stationStatus에서 불러올 경우만  처리주무부서 호출
			stationChart.put("metroDept", getMetroDept(vo));
		}
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		/*System.out.println("-----------------------------------");
		System.out.println(gson.toJson(stationChartList));
		System.out.println("-----------------------------------");*/
		return gson.toJson(stationChart);
	}
	// 처리주무부서 결과 구하기 
	private ArrayList<HashMap<String, String>> getMetroDept(StationStatusVo vo) throws Exception{
		
		ArrayList<HashMap<String, String>> metroDeptList = new ArrayList<HashMap<String, String>>();	// 처리주무부서 리스트
		Result[] resultlist = null;
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_STATUS));
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
	
	// 역 이름이 순위권 내애 경우 결과 리스트에 넣는 역할
	private ArrayList<HashMap<String, String>> allJsonParse(String jsonStr, ArrayList<HashMap<String, String>> list){
		
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			
		JsonElement jelement = new JsonParser().parse(jsonStr);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    // 현재 json 역 이름
	    String stationName = jobject.get("name").toString().replace("\"", "");
	    // 순위권 내에 역 이름이 있다면 해당 결과들 결과 리스트에 삽입
	    if(list != null){
	    	for(int i = 0; i < list.size(); i++){
	    		if(list.get(i).get("stationName").equals(stationName)){
	    			resultList = jsonParse(jsonStr);
	    			HashMap<String, String> map = new HashMap<String, String>();	
	    			map.put("name", stationName);
	    			//resultList.add(map);
	    			resultList.add(0, map);
	    			break;
	    		}
	    		
	    	}
	    }
	    return resultList;
	}
	
	private ArrayList<HashMap<String, String>> jsonParse(String jsonStr){
		
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		
		JsonElement jelement = new JsonParser().parse(jsonStr);
	    JsonObject  jobject = jelement.getAsJsonObject();
	    JsonArray jarray = jobject.getAsJsonArray("subGroup");		// 2depth 배열전체
	    
	    for(int i=0; i < jarray.size();  i++){
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	jobject = jarray.get(i).getAsJsonObject();
	    	String name = jobject.get("name").toString();				// 2depth 필드명 
	    	String count = jobject.get("count").toString();
			map.put("name", name);
			map.put("count", count);
			resultList.add(map);
	    }

	    return resultList;
	}
	
	/*
	 * 선택 역 키워드 상위 10개 / 카운트 구하기
	 */
	public String getKeywordArr(StationStatusVo vo) throws Exception {
		
		Result[] resultlist = null;
		ArrayList<HashMap<String, String>> keywordList = new ArrayList<HashMap<String, String>>();
		
		QuerySet querySet = new QuerySet(1);
		Query query = new Query();
		query.setSelect(createSelectSet(SELECTSET_STATUS));
		query.setWhere(createWhereSet(WHERESET_KEYWORDS, vo, vo.getStationName())); // case를 위한 번호, 키워드, VO
		query.setFilter(createFilterSet(vo));
		query.setGroupBy(createGroupBySet(GROUPBYSET_KEYWORDS, vo));
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

		//System.out.println("##### 역별 키워드 query11: " + queryParser.queryToString(query));
		

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
				
				// 총 역 결과 리스트에 담기		
				int len = 10;
				if(groupResults[0].groupResultSize() < 10)
					len = groupResults[0].groupResultSize();
				for(int i = 0; i < len; i++){
					HashMap<String, String> map = new HashMap<String, String>();					
					if(!vo.getStationName().equals(new String(groupResults[0].getId(i)).trim())){
						map.put("NAME", new String(groupResults[0].getId(i)).trim());
						map.put("COUNT", Integer.toString(groupResults[0].getIntValue(i)));
						keywordList.add(map);
					}
				}
			}
		}
		
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		/*System.out.println("-----------------------------------");
		System.out.println(gson.toJson(stationChartList));
		System.out.println("-----------------------------------");*/
		return gson.toJson(keywordList);
	}
	
	/**
	 * VOC 검색결과
	 * 
	 * @param keyword
	 * @return 검색 결과 리스트
	 * @exception Exception
	 */
	public HashMap<String, Object> getSearchResult(StationStatusVo vo) throws Exception {
		
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
		query.setSelect(createSelectSet(SELECTSET_LIST));

		/*if (keywordRankingVo.getEmotion() != null && !keywordRankingVo.getEmotion().equals(""))
			query.setWhere(createWhereSet(10, keywordRankingVo.getKeyword(), keywordRankingVo));
		else*/
		
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, ""));
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
		query.setSelect(createSelectSet(SELECTSET_STATUS));
		/*if (keywordRankingVo.getEmotion() != null && !keywordRankingVo.getEmotion().equals(""))
			query.setWhere(createWhereSet(10, keywordRankingVo));
		else*/
		query.setWhere(createWhereSet(WHERESET_SEARCHKEYWORD, vo, ""));
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

		//System.out.println("##### 리스트 검색 query2: " + queryParser.queryToString(query));

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
		StationStatusVo vo = new StationStatusVo();
		vo.setDocId(id);
		query.setWhere(createWhereSet(WHERESET_DETAIL, vo, ""));
		query.setFrom(Globals.MARINER_COLLECTION);
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
		//System.out.println("##### 상세검색 query: " + queryParser.queryToString(query));
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
	 * VOC 검색결과
	 * 
	 * @param EmotionVo
	 * @return HashMap
	 * @exception Exception
	 */
	public HashMap<String, Object> getExcelResult(StationStatusVo vo) throws Exception {
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
	/**
	 * SelectSet 설정.
	 * 
	 * @return SelectSet[]
	 */
	private SelectSet[] createSelectSet(int flag) {

		SelectSet[] selectSet = null;
		
		switch (flag) {
		case SELECTSET_STATUS:
		case SELECTSET_COUNT:
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
	private WhereSet[] createWhereSet(int flag, StationStatusVo vo, String stationName) {

		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereSetList = new ArrayList<WhereSet>();
		
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
		if (!vo.getLine().isEmpty() && !vo.getLine().equals(Globals.COM_SELECT_ALL)) {			//노선
			if(vo.getLine().equals("18")){	// 1~8호선 선택시
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, "01", 0));
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
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}else{
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_LINE, Protocol.WhereSet.OP_HASALL, vo.getLine(), 0));
			}		
		}
		switch (flag) {
		case WHERESET_STATUS : // 역별 현황 검색 조건 			
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			break;
		case WHERESET_STATUSDETAIL : 	// 역별 현황
		case WHERESET_KEYWORDS:			// 각 역별 키워드
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_EXT_STATION, Protocol.WhereSet.OP_HASALL, stationName, 0));
			break;
		case WHERESET_SEARCHKEYWORD:			// 전체 검색
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			if(!vo.getSearchType().isEmpty() && !vo.getSearchType().equals(Globals.COM_SELECT_ALL)){
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 1000));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 100));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereSetList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASALL, vo.getKeyword(), 100));
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereSetList.add(new WhereSet(Globals.IDX_EXT_STATION, Protocol.WhereSet.OP_HASALL, vo.getStationName(), 1000));
				whereSetList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
			}
			break;
		case WHERESET_DETAIL :	// 상세보기
			if (!vo.getMetroDept().isEmpty() && !vo.getMetroDept().equals(Globals.COM_SELECT_ALL)) {	//처리 주무 부서(코드)
				whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereSetList.add(new WhereSet(Globals.IDX_CDREPDEPT, Protocol.WhereSet.OP_HASALL, vo.getMetroDept(), 0));
			}
			whereSetList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			whereSetList.add(new WhereSet(Globals.IDX_DQ_DOCID, Protocol.WhereSet.OP_HASALL, vo.getDocId(), 0));	//keyword로 수집문서ID 검색
			break;
		case WHERESET_METRODEPT:
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
	private FilterSet[] createFilterSet(StationStatusVo vo) {

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
		
		///// 역 이름 없는 부분 제외
		filterlist.add(new FilterSet(Protocol.FilterSet.OP_NOT, Globals.IDX_EXT_LINE, "NONE"));
		
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
	private GroupBySet[] createGroupBySet(int flag, StationStatusVo vo) { 

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupbysetList = new ArrayList<GroupBySet>();
		
	//	String[] groupList = { "SENSE_KEYWORD", "PNN", "SENSE_KIND" };	// 키워드, 긍/부정, 감정상태 
		String[] keyword = { "KEYWORD"};	
		String[] pnn = { "PNN", };	
		String[] senseKind = { "SENSE_KIND", };	
		
		switch (flag) {
		case GROUPBYSET_STATUS:	//역별 현황
			//groupbysetList.add(new GroupBySet("EXT_STATION", (byte) (Protocol.GroupBySet.OP_GROUP_IN_GROUP), "", "", groupList));				
			groupbysetList.add(new GroupBySet(Globals.GROUP_EXT_STATION, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
		//	groupbysetList.add(new GroupBySet(Globals.GROUP_CDREPDEPT, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));
		//	groupbysetList.add(new GroupBySet("EXT_STATION", (byte) (Protocol.GroupBySet.OP_GROUP_IN_GROUP), "", "", pnn));	
		//	groupbysetList.add(new GroupBySet("EXT_STATION", (byte) (Protocol.GroupBySet.OP_GROUP_IN_GROUP), "", "", senseKind));	

			break;
		case GROUPBYSET_STATUSDETAIL:	//역별 현황					
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
			groupbysetList.add(new GroupBySet(Globals.GROUP_PNN, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));				
			break;
		case GROUPBYSET_KEYWORDS:	//각 역별 키워드					
			groupbysetList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte) (Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "", ""));	
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