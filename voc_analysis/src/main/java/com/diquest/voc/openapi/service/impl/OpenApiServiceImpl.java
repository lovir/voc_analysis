package com.diquest.voc.openapi.service.impl;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.collections.collection.SynchronizedCollection;
import org.springframework.stereotype.Service;

/*import com.diquest.ir.client.command.CommandSearchRequest;
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
import com.diquest.voc.cmmn.service.DateUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.dashBoard.vo.ContryMInwonTotalVO;
import com.diquest.voc.dashBoard.vo.CountryMinwonVO;
import com.diquest.voc.dashBoard.vo.DashBoardVo;
import com.diquest.voc.dashBoard.vo.TopCategoryVO;
import com.diquest.voc.dashBoard.vo.TopKeywordVO;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.service.impl.KeywordRankingDAO;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.openapi.service.DqXmlBuilderJ;*/
import com.diquest.voc.openapi.service.OpenApiService;
import com.diquest.voc.openapi.service.impl.OpenApiDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Service("openApiService")
public class OpenApiServiceImpl implements OpenApiService{
	@Resource(name="openApiDAO")
	private OpenApiDAO openApiDAO;
	
	private Gson gson = null;
	
	//자동 분류 추천 전체 누적 건수 조회
	public String selectTotalCategorizeCount() throws Exception {
		String totalAcmSize = openApiDAO.selectTotalCategorizeCount();
		return totalAcmSize;
	}
	//서울시 일별 기상 관측 정보 전체 누적 건수 조회
	public String selectTotalWeatherObserveCount() throws Exception {
		String totalAcmSize = openApiDAO.selectTotalWeatherObserveCount();
		return totalAcmSize;
	}
	//서울시 일별 기상 예측 정보 전체 누적 건수 조회
	public String selectTotalWeatherForecastCount() throws Exception {
		String totalAcmSize = openApiDAO.selectTotalWeatherForecastCount();
		return totalAcmSize;
	}
	//서울시 일별 기상 관측 정보 오늘 수집 건수 조회
	public String selectTodayWeatherObserveCount() throws Exception{
		String todayAcmSize = openApiDAO.selectTodayWeatherObserveCount();
		return todayAcmSize;
	}
	//서울시 일별 기상 예측 정보 오늘 수집 건수 조회
	public String selectTodayWeatherForecastCount() throws Exception{
		String todayAcmSize = openApiDAO.selectTodayWeatherForecastCount();
		return todayAcmSize;
	}
	//자동 분류 추천 일자건수 조회
	public String selectDayCategorizeCount() throws Exception{
		String totalSize = openApiDAO.selectDayCategorizeCount();
		return totalSize;
	}
	//서울시 일별 기상 관측 정보 일자건수 조회
	public String selectDayWeatherObserveCount() throws Exception{
		String totalSize = openApiDAO.selectDayWeatherObserveCount();
		return totalSize;
	}
	//서울시 일별 기상 예측 정보 일자건수 조회
	public String selectDayWeatherForecastCount() throws Exception{
		String totalSize = openApiDAO.selectDayWeatherForecastCount();
		return totalSize;
	}
	//자동 분류 추천 일자별 리스트 조회
	public List<HashMap<String, String>> selectDayCategorizeList(HashMap<String, String> paramMap) throws Exception {
		List<HashMap<String, String>> result = openApiDAO.selectDayCategorizeList(paramMap);
		return result;
	}
	//서울시 일별 기상 관측 정보 일자별 리스트 조회
	public List<HashMap<String, String>> selectDayWeatherObserveList(HashMap<String, String> paramMap) throws Exception {
		List<HashMap<String, String>> result = openApiDAO.selectDayWeatherObserveList(paramMap);
		return result;
	}
	//서울시 일별 기상 예측 정보 일자별 리스트 조회
	public List<HashMap<String, String>> selectDayCWeatherForecastList(HashMap<String, String> paramMap) throws Exception {
		List<HashMap<String, String>> result = openApiDAO.selectDayCWeatherForecastList(paramMap);
		return result;
	}
	
	//이전 VOC 소스들. 서울메트로에서 사용 안함.
	/*
	*//** keywordRankingDAO *//*
	@Resource(name="keywordRankingDAO")
	private KeywordRankingDAO keywordRankingDAO;
	
	public List<String> getInterestKeyword(HashMap<String, String> paramMap) throws Exception {
		List<String> keywordList = keywordRankingDAO.selectInterest(paramMap);
		return keywordList;
	}
	private String engineHost = "101.0.1.140";	//mariner3 IP
	private int enginePort = 5555;					//mariner3 PORT
	
	DqXmlBuilderJ xmlBuilder = new DqXmlBuilderJ();
	
	public String openapitest(HashMap<String,String> paramMap) throws Exception{
		
		// Map에 담았던 정보를 푸는 곳
		String reqType = paramMap.get("reqType"); // 요청하는 데이터의 형태(통계형 데이터 or 검색형 데이터)
		String searchTerm = paramMap.get("searchTerm");	//검색어
		int pageno = Integer.parseInt(paramMap.get("pageno"));//현재 페이지 번호
		int resultSize = Integer.parseInt(paramMap.get("resultSize"));
		String searchTarget = paramMap.get("searchTarget");
		String statFiled = paramMap.get("statFiled");
		String startDate = paramMap.get("startDate");
		String endDate = paramMap.get("endDate");		
		String guName = paramMap.get("guName");
		String cateName = paramMap.get("cateName");
		String departName = paramMap.get("departName");
		String sensitivity = paramMap.get("sensitivity");
		
		System.out.println("타입 확인 : "+reqType);
		
		if(sensitivity.equals("p")){
			sensitivity="P";
		}else if(sensitivity.equals("n")){
			sensitivity="N";
		}else if(sensitivity.equals("d")){
			sensitivity="D";
		}
		//---------------------------------
		
		//------- Mariner API -------------
		String collection = "VOC";
		if(searchTarget.equals("sns") || searchTarget.equals("news") || searchTarget.equals("blog") || searchTarget.equals("tweet")){
			collection = "VOE";					//검색 컬렉션			
		}
		
		int startNo = (pageno-1)*resultSize;				//결과 개수 시작 값
		int endNo = ((pageno -1)*resultSize)+resultSize-1;	//결과 개수 종료 값
		
		Query query = null;									//Query
		
		SelectSet[] selectSet = null;						//SelectSet[]
		WhereSet[] whereSet = null;							//WhereSet[]
		FilterSet[] filterSet = null;						//FilterSet[]
		OrderBySet[] orderbySet = null;						//OrderBySet[]
		GroupBySet[] groupbySet = null;						//GroupBySet[]
		QuerySet querySet = null;							//QuerySet
		
		Result[] resultlist = null;							//Result[]
		Result result = null;								//Result
		
		ArrayList<WhereSet> whereList = new ArrayList<WhereSet>();				//동적인 WhereSet[] 생성 위해 사용 
		ArrayList<FilterSet> filterList = new ArrayList<FilterSet>();				//동적인 FilterSet[] 생성 위해 사용
		
		String chk ="N";									//check
		//---------------------------------
		
		//------- Result ------------------
		String xml = ""; //검색결과 XML
		//---------------------------------
		
		//SelectSet
		selectSet = new SelectSet[]{
			new SelectSet("TITLE", Protocol.SelectSet.NONE,200),
			new SelectSet("CONTENT", Protocol.SelectSet.NONE,100),
			new SelectSet("RES_CONTENT", Protocol.SelectSet.NONE),
			new SelectSet("REGDATE", Protocol.SelectSet.NONE),
			new SelectSet("L_CATE_NAME", Protocol.SelectSet.NONE),
			new SelectSet("M_CATE_NAME", Protocol.SelectSet.NONE),
			new SelectSet("S_CATE_NAME", Protocol.SelectSet.NONE),
			new SelectSet("GU_NAME", Protocol.SelectSet.NONE),
			new SelectSet("SENSITIVITY", Protocol.SelectSet.NONE),
			new SelectSet("DEPART", Protocol.SelectSet.NONE),
			new SelectSet("USE_MED_CD", Protocol.SelectSet.NONE)
		};
		
		//WhereSet
		
		if(reqType.equals("search")){
			if(!searchTerm.equals("") && !guName.equals("") && !cateName.equals("") && !departName.equals("") && !sensitivity.equals("")){
				if(!searchTerm.equals("")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				
					whereList.add(new WhereSet("TITLE", Protocol.WhereSet.OP_HASALL, searchTerm, 100));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("CONTENT", Protocol.WhereSet.OP_HASALL, searchTerm, 50));
				
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				}else{
					whereList.add(new WhereSet("ALLVIEW", Protocol.WhereSet.OP_HASALL, "A", 100));	
				}
				if(!guName.equals("")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("GU_NAME", Protocol.WhereSet.OP_HASALL, guName, 10));
				}
				if(!cateName.equals("")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("L_CATE_NAME", Protocol.WhereSet.OP_HASALL, cateName, 10));
				}
				if(!departName.equals("")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("DEPART", Protocol.WhereSet.OP_HASALL, departName, 10));
				}
				if(!sensitivity.equals("")){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("SENSITIVITY", Protocol.WhereSet.OP_HASALL, sensitivity, 10));
				}
				if(!searchTarget.equals("")){
					if(searchTarget.equals("minwon")){
						
						//String [] minwon = {"120","신문고","새올"};
						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));  
						//whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, minwon));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, "120",10));
						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, "신문고",10));
						whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, "새올",10));
					}
					if(searchTarget.equals("미추홀콜센터")){
						System.out.println("searchTarget 진입2");
						searchTarget = "120";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
					}
					if(searchTarget.equals("sinmun")){
						searchTarget = "신문고";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
					}
					if(searchTarget.equals("saeol")){
						searchTarget = "새올";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
					}if(searchTarget.equals("sns")){
						searchTarget = "sns";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));				
					}if(searchTarget.equals("news")){
						searchTarget = "news";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
					}if(searchTarget.equals("blog")){
						searchTarget = "blog";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
					}if(searchTarget.equals("tweet")){
						searchTarget = "tweet";
						whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
						whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
					}
					
				}
			}
		}else{
			if(!searchTerm.equals("")){
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			
				whereList.add(new WhereSet("TITLE", Protocol.WhereSet.OP_HASALL, searchTerm, 100));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet("CONTENT", Protocol.WhereSet.OP_HASALL, searchTerm, 50));
			
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			}else{
				whereList.add(new WhereSet("ALLVIEW", Protocol.WhereSet.OP_HASALL, "A", 100));	
			}
			if(!guName.equals("")){
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet("GU_NAME", Protocol.WhereSet.OP_HASALL, guName, 10));
			}
			if(!cateName.equals("")){
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet("L_CATE_NAME", Protocol.WhereSet.OP_HASALL, cateName, 10));
			}
			if(!departName.equals("")){
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet("DEPART", Protocol.WhereSet.OP_HASALL, departName, 10));
			}
			if(!sensitivity.equals("")){
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet("SENSITIVITY", Protocol.WhereSet.OP_HASALL, sensitivity, 10));
			}
			if(!searchTarget.equals("")){
				if(searchTarget.equals("minwon")){
					
					//String [] minwon = {"120","신문고","새올"};
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));  
					//whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, minwon));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, "120",10));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, "신문고",10));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, "새올",10));
				}
				if(searchTarget.equals("미추홀콜센터")){
					System.out.println("searchTarget 진입2");
					searchTarget = "120";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
				}
				if(searchTarget.equals("sinmun")){
					searchTarget = "신문고";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
				}
				if(searchTarget.equals("saeol")){
					searchTarget = "새올";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
				}if(searchTarget.equals("sns")){
					searchTarget = "sns";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));				
				}if(searchTarget.equals("news")){
					searchTarget = "news";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
				}if(searchTarget.equals("blog")){
					searchTarget = "blog";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
				}if(searchTarget.equals("tweet")){
					searchTarget = "tweet";
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet("USE_MED_CD", Protocol.WhereSet.OP_HASALL, searchTarget,10));
				}
				
			}
		}
		
		whereSet = new WhereSet[whereList.size()];
		for(int i = 0; i < whereList.size(); i++){
			whereSet[i] = (WhereSet)whereList.get(i);
		}
		
		whereList.clear();
		
		//FilterSet
		if(endDate.equals("")){
			endDate="20160630";
		}
		if((!startDate.equals("")) && (!endDate.equals(""))){			
			startDate = startDate+"000000";
			endDate = endDate+"235959";
			System.out.println("시작일 확인 : "+startDate);
			System.out.println("종료일 확인 : "+endDate);
			
			filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{startDate, endDate}, 0));
			
		}
		
		filterSet = new FilterSet[filterList.size()];
		for(int i = 0; i < filterList.size(); i++){
			filterSet[i] = (FilterSet)filterList.get(i);
		}

		
		//OrderBySet
		if(searchSort.equals("weight")){ //인기순
			orderbySet = new OrderBySet[]{new OrderBySet(true, searchSort, Protocol.OrderBySet.OP_NONE)};
		}
		if(regdate.equals("date")){ //판매순
			orderbySet = new OrderBySet[]{new OrderBySet(true, "REGDATE", Protocol.OrderBySet.OP_NONE)};
		}else{
			orderbySet = new OrderBySet[]{new OrderBySet(true, "REGDATE", Protocol.OrderBySet.OP_PREWEIGHT)};
		}
		
		//GroupBySet
			
			if(statFiled.equals("")){	
			groupbySet = new GroupBySet[]{
					new GroupBySet("L_CATE_NAME", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC 0 999", ""),
					//new GroupBySet("GU_NAME", (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NONE), "ASC", ""),
			};
			}else{
				if(statFiled.equals("year")){
					statFiled="YEAR";
				}else if(statFiled.equals("month")){
					statFiled="CHAR_MONTH";
				}else if(statFiled.equals("day")){
					statFiled="CHAR_DAY";
				}else if(statFiled.equals("yearmonth")){
					statFiled="CHAR_MONTH";
				}else if(statFiled.equals("category")){
					statFiled="L_CATE_NAME";
				}else if(statFiled.equals("depart")){
					statFiled="DEPART";
				}else if(statFiled.equals("guname")){
					statFiled="GU_NAME";
				}else if(statFiled.equals("sensitivity")){
					statFiled="SENSITIVITY";
				}else if(statFiled.equals("keyword")){
					statFiled="KEYWORD";
				}
				System.out.println("statFiled 확인 : "+statFiled);
				groupbySet = new GroupBySet[]{
				new GroupBySet(statFiled, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC 0 999", ""),
				};
			}
		//Query
		query = new Query();
		query.setSearchOption((byte)(Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED));
		query.setThesaurusOption((byte)(Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));
		query.setValue("equivSynonymWeight","1");			// 동의어 가중치를 설정 (default : 1)
		query.setValue("quasiSynonymWeight","0.01");		// 유의어 가중치를 설정 (설정한 가중치의 0.01, defailt: 0.01) 
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setFilter(filterSet);
		query.setOrderby(orderbySet);
		query.setGroupBy(groupbySet);
		query.setFrom(collection);		
		query.setResult(startNo, endNo);
		query.setLoggable(true);
		query.setDebug(true);
		query.setPrintQuery(true);
		query.setSearch(true);
		
		CommandSearchRequest.setProps(engineHost, enginePort, 9990000, 20, 20);
		
		//QuerySet
		querySet = new QuerySet(1);
		querySet.addQuery(query);
		QueryParser queryParser = new QueryParser();	
		System.out.println("##### query: " + queryParser.queryToString(query));

		//Result
		resultlist = requestSearch(querySet);
		int totalSize = 0;
		if(resultlist != null){
			result = resultlist[0];
			totalSize = result.getTotalSize();
		}
		
		xml = xmlBuilder.getProductXml(paramMap, selectSet, result); //결과 XML 생성
		
		return xml;
			
	}

	private Result[] requestSearch(QuerySet querySet) {
		
		CommandSearchRequest command = null;
		
		ResultSet results = null;
		Result[] resultlist = null;
		
		//SearchRequest
		int rs = -1;
		command = new CommandSearchRequest(engineHost, enginePort);
		try {
			rs = command.request(querySet);
			//System.out.println("rs:"+rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Result
		if(rs >= 0){
			results = command.getResultSet();
			resultlist = results.getResultList();
		}		
		return resultlist;	
	}
	
	
	public ArrayList<HashMap<String, String>> openApiSearch(String call_date, String call_category, 
			String send_receive_delimiter, String recognition_text, String agent_ext, 
			String customer_phone_number, String call_duration, int currentPageNo, int pageSize){
		
		WhereSet[] whereSet = null;							//WhereSet[]
		FilterSet[] filterSet = null;						//FilterSet[]
		
		String collection = "VOC_SEOUL";
		
		int startNo = (currentPageNo-1)*pageSize;				//결과 개수 시작 값
		int endNo = ((currentPageNo -1)*pageSize)+pageSize-1;	//결과 개수 종료 값
		
		Query query = null;									//Query
		QuerySet querySet = null;							//QuerySet
		CommandSearchRequest command = null;
		Result[] resultlist = null;							//Result[]
		Result result = null;								//Result
		ResultSet results = null;
		 음성인식 리스트 조회 조건 파라미터
		기간 --> call_meta_info : call_date -- where
		카테고리 --> call_meta_info : call_category -- filter
		통화내용 구분 --> call_recognition_results : send_receive_delimiter (0=전체, 1=송신, 2=수신) -- filter
		통화내용 --> call_recognition_results : recognition_text -- where
		-- 통화번호 구분 --> 대응 DB 없음(0=전체, 1=상담원, 2=고객)
		통화번호 --> 1 = agent_info : agent_ext -- filter
				   --> 2 = customer_info : customer_phone_number --filter
		통화길이 --> call_meat_info : call_duration -- filet
		 
		
		m.agent_seq AS agent_seq, 추가
		m.customer_seq AS customer_seq, 추가
		m.call_category AS call_category,
		m.call_date AS call_date,
		m.call_duration AS call_duration,
		m.call_recognition_status AS call_recognition_status, - 필드 추가
		a.agent_name AS agent_name, - 필드 추가
		a.agent_ext AS agent_ext, 
		c.customer_name AS customer_name, - 필드 추가
		c.customer_phone_number AS customer_phone_number - 필드 추가
		
		
		ArrayList<WhereSet> whereList = new ArrayList<WhereSet>();
		ArrayList<FilterSet> filterList = new ArrayList<FilterSet>();
		boolean flag = false;
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		
		//SelectSet
		SelectSet[] selectSet = new SelectSet[]{
			new SelectSet("AGENT_SEQ", Protocol.SelectSet.NONE),
			new SelectSet("CUSTOMER_SEQ", Protocol.SelectSet.NONE),
			new SelectSet("REGDATE", Protocol.SelectSet.NONE,200),
			new SelectSet("CONTENT", Protocol.SelectSet.NONE,100),
			new SelectSet("CALL_CATEGORY", Protocol.SelectSet.NONE),
			new SelectSet("L_CATE_NAME", Protocol.SelectSet.NONE),	
			new SelectSet("CALL_DURATION", Protocol.SelectSet.NONE),
			new SelectSet("CALL_RECOGNITION_STATUS", Protocol.SelectSet.NONE),
			new SelectSet("AGENT_NAME", Protocol.SelectSet.NONE),
			new SelectSet("AGENT_EXT", Protocol.SelectSet.NONE),
			new SelectSet("CUSTOMER_NAME", Protocol.SelectSet.NONE),
			new SelectSet("CUSTOMER_PHONE_NUMBER", Protocol.SelectSet.NONE)
		};
		
		// whereSet
		if(call_date != null & !call_date.equals("")){
			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("DAY", Protocol.WhereSet.OP_HASALL, call_date, 100));			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
			
			flag = true;
		}
		
		if(recognition_text != null && !recognition_text.equals("")){
			
			if(flag == true)
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
			
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("CONTENT", Protocol.WhereSet.OP_HASALL, recognition_text, 100));					
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));	
		
			flag = true;
		}
		
		if(flag == false){
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
			whereList.add(new WhereSet("ALLVIEW", Protocol.WhereSet.OP_HASALL, "A", 100));					
			whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));	
		}
		
		whereSet = new WhereSet[whereList.size()];
		for(int i = 0; i < whereList.size(); i++){
			whereSet[i] = (WhereSet)whereList.get(i);
		}
		
		//filterSet
		if((call_category != null && !call_category.equals(""))){			
			filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), "CALL_CATEGORY", call_category));			
		}
		
		if((send_receive_delimiter != null && !send_receive_delimiter.equals(""))){			
			filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), "CALL_RECOGNITION_STATUS", send_receive_delimiter));			
		}
		
		if((agent_ext != null && !agent_ext.equals(""))){			
			filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), "AGENT_EXT", agent_ext));			
		}
		
		if((customer_phone_number != null && !customer_phone_number.equals(""))){			
			filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), "CUSTOMER_PHONE_NUMBER", customer_phone_number));			
		}
		
		if((call_duration != null && !call_duration.equals(""))){	
			
			String[] duration = {"0", call_duration};
			filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), "CALL_DURATION", duration));			
		}
		
		filterSet = new FilterSet[filterList.size()];
		for(int i = 0; i < filterList.size(); i++){
			filterSet[i] = (FilterSet)filterList.get(i);
		}
		
		query = new Query();
		query.setSearchOption((byte)(Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED));
		query.setThesaurusOption((byte)(Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM));
		query.setSelect(selectSet);
		query.setWhere(whereSet);
		query.setFilter(filterSet);
		query.setFrom(collection);		
		query.setResult(startNo, endNo);
		query.setLoggable(true);
		query.setDebug(true);
		query.setPrintQuery(true);
		query.setSearch(true);
		
		querySet = new QuerySet(1);
		querySet.addQuery(query);

		command = new CommandSearchRequest(Globals.MARINER_IP, Globals.MARINER_PORT);
		int returnCode = -1;
		
		try {
			returnCode = command.request(querySet);
		} catch (IRException e) {		
			e.printStackTrace();
		}
		
		if(returnCode >= 0){
			results = command.getResultSet();
			resultlist = results.getResultList();
		}else{
			resultlist = new Result[1];
			resultlist[0]= new Result();
		}
		
		
		if(resultlist[0] != null){
		
			for(int i=0; i<resultlist[0].getTotalSize(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("AGENT_SEQ", new String(resultlist[0].getResult(i,0)));
				map.put("CUSTOMER_SEQ", new String(resultlist[0].getResult(i,1)));
				map.put("REGDATE", new String(resultlist[0].getResult(i,2)));
				String call_date_tmp = new String(resultlist[0].getResult(i,2));
				map.put("CALL_DATE", call_date_tmp.substring(0, 8));
				map.put("RECOGNITON_TEXT", new String(resultlist[0].getResult(i,3)));
				map.put("CALL_CATEGORY", new String(resultlist[0].getResult(i,4)));
				map.put("CATEGORY_NAME", new String(resultlist[0].getResult(i,5)));
				map.put("CALL_DURATION", new String(resultlist[0].getResult(i,6)));
				map.put("CALL_RECOGNITION_STATUS", new String(resultlist[0].getResult(i,7)));
				map.put("AGENT_NAME", new String(resultlist[0].getResult(i,8)));
				map.put("AGENT_EXT", new String(resultlist[0].getResult(i,9)));
				map.put("CUSTOMER_NAME", new String(resultlist[0].getResult(i,10)));
				map.put("CUSTOMER_PHONE_NUMBER", new String(resultlist[0].getResult(i,11)));
				
				resultList.add(map);
			}
		}
		return resultList;
		
	}*/
}