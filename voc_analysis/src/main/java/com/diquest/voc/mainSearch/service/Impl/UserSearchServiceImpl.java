/*package com.diquest.voc.mainSearch.service.Impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.GroupBySet;
import com.diquest.ir.common.msg.protocol.query.OrderBySet;
import com.diquest.ir.common.msg.protocol.query.Query;
import com.diquest.ir.common.msg.protocol.query.QuerySet;
import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.query.WhereSet;
import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;
import com.diquest.ir.common.msg.protocol.result.ResultSet;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.BaseService;
import com.diquest.voc.mainSearch.service.UserSearchService;
import com.diquest.voc.mainSearch.vo.UserVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("userSearchService")
public class UserSearchServiceImpl extends BaseService implements UserSearchService{

	private Gson gson = null;
	
	*//** Log Service *//*
	Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public HashMap<String, Object> userReport(UserVo vo) throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		HashMap<String, Object> chart = new HashMap<String, Object>();
		List<Object> word = new ArrayList<Object>();
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String[] color = Globals.COM_DASH_ISSUE_COLOR;
		
		LinkedHashMap<String, String> categoriesMap = getCategories(vo); // x축

		queryList.add(createQuery(Globals.USER_REPORT, vo, vo.getTerm())); // 불만/비불만, 전체 문서에 대한 키워드
		
		// 불만 문서 전용 쿼리 날짜별 불만(Where)
		queryList.add(createQuery(Globals.USER_REPORT_COMPLAIN, vo, vo.getTerm())); // 불만/비불만, 전체 문서에 대한 키워드
		
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
	
		for(int i = 0; resultList != null && i < resultList.length; i++) {
			if(i == 0){
				Result result = resultList[i];
				
				float complainY = 0.f;
				float complainN = 0.f;
				float percentage= 0.f;
				GroupResult[] groupResults = null;
				
				if(result.getGroupResultSize() != 0){
					
					groupResults = result.getGroupResults();
					
					for (int j = 0; j < groupResults.length; j++) {
						
						if(j == 0){
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
						}else{
							int rSize = groupResults[j].groupResultSize();
							
							for (int k = 0; k < rSize; k++) {
								HashMap<String, Object> data = new HashMap<String, Object>();
								data.put("style", "color: " + color[k%5] +"; font-family: 'NanumGothicBold'; ");
								HashMap<String, Object> obj = new HashMap<String, Object>();
								int value = groupResults[j].getIntValue(k);
								obj.put("html", data);
								obj.put("link", "#");
								obj.put("text", new String(groupResults[j].getId(k)));
								obj.put("weight", value);
								word.add(obj);
							}
							chart.put("word", gson.toJson(word));
						}
						
					}
				}

				percentage = (complainY/(complainY+complainN))*100;
				if(Float.isNaN(percentage)){
					percentage = 0;
				}
				
				// 전체 문서수
				chart.put("totalSize", String.format("%.0f", complainY+complainN));
				// 불만 문서수
				chart.put("complainSize", (int)complainY);
				// 불만지수
				chart.put("percentage", String.format("%.1f", percentage));
			}else{
				chart.put("series", gson.toJson(getSeries(resultList[i], categoriesMap, vo.getTerm())));
			}
		}
		
		chart.put("categories", gson.toJson(categoriesMap.values()));
		
		return chart;
	}

	@Override
	public HashMap<String, Object> userList(UserVo vo) throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		HashMap<String, Object> chart = new HashMap<String, Object>();
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 본문 리스트 검색
		queryList.add(createQuery(Globals.USER_LIST, vo, ""));
		
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
		
		// 검색결과 가공
		chart.put("listResult", this.getList(resultList[0])); // 리스트
		chart.put("groupResult", this.getCategoryList(resultList[0])); // 카테고리 리스트
		chart.put("userIdResult", this.getUserIdList(resultList[0])); // 고객ID 리스트
		chart.put("paginationInfo", getPaging(resultList[0], vo)); // 페이징
		chart.put("totSize", resultList[0].getTotalSize()); // 페이징

		return chart;
	}

	@Override
	public HashMap<String, Object> userExcelList(UserVo vo) throws Exception {
		Result[] resultList = null;
		CommandSearchRequest command = null;
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(createQuery(Globals.USER_EXCEL, vo, ""));
		
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
	public HashMap<String, String> userDetailView(UserVo vo) throws Exception {

		Result[] resultList = null;
		CommandSearchRequest command = null;
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(createQuery(Globals.USER_DETAIL, vo, ""));
		
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
	public List<Object> userAlikeView(UserVo vo) throws Exception {

		Result[] resultList = null;
		CommandSearchRequest command = null;
		
		// QuerySet 인스턴스화
		QuerySet querySet = new QuerySet(1);
		querySet.addQuery(createQuery(Globals.USER_ALIKE, vo, ""));

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

	*//**
	 * 검색결과 카테고리 목록을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 카테고리 목록
	 *//*
	public List<HashMap<String, String>> getCategoryList(Result result) {
		GroupResult[] groupResults = null;
		List<HashMap<String, String>> categoryList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> categoryMap = null;
		
		if(result.getGroupResultSize() != 0){
			groupResults = result.getGroupResults();
			for (int j = 0; j < groupResults.length; j++) {
				int rSize = groupResults[j].groupResultSize();
				
				if(j == 0){
					for (int k = 0; k < rSize; k++) {
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
	
	*//**
	 * 검색결과 카테고리 목록을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 카테고리 목록
	 *//*
	public List<String> getUserIdList(Result result) {
		GroupResult[] groupResults = null;
		List<String> userIdList = new ArrayList<String>();
		
		if(result.getGroupResultSize() != 0){
			groupResults = result.getGroupResults();
			for (int j = 0; j < groupResults.length; j++) {
				int rSize = groupResults[j].groupResultSize();
				
				if(j == 1){
					for (int k = 0; k < rSize; k++) {
						userIdList.add(new String(groupResults[j].getId(k)));
					}
				}
			}
		}
		
		return userIdList;
	}
	
	*//**
	 * 검색결과 글목록을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 글 목록
	 *//*
	public List<HashMap<String, String>> getList(Result result) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = null;
		
		if(result.getRealSize() != 0){
			
			for (int j = 0; j < result.getRealSize(); j++) {
				map = new HashMap<String, String>();
				map.put(Globals.FIELD_TITLE, new String(result.getResult(j,0)));
				map.put(Globals.FIELD_CONTENT, new String(result.getResult(j,1)));
				map.put(Globals.FIELD_CONTENT_ORI, new String(result.getResult(j,2)));
				map.put(Globals.FIELD_REGDATE, new String(result.getResult(j,3)));
				map.put(Globals.FIELD_ID, new String(result.getResult(j,4)));

				int weight = Integer.parseInt(new String(result.getResult(j,5)));
				if(weight > 0){
					map.put(Globals.FIELD_WEIGHT, String.valueOf(weight / 100));
				}else{
					map.put(Globals.FIELD_WEIGHT, "0");
				}
				
				map.put(Globals.FIELD_USER_NM, new String(result.getResult(j,6)));
				map.put(Globals.FIELD_USER_NO, new String(result.getResult(j,7)));
				
				list.add(map);
			}
		}
		
		return list;
	}
	
	*//**
	 * 검색결과를 차트 표시용 Series로 가공.
	 * @param resultMap - 검색 결과 Result
	 * @param categoriesMap - 차트 x축 표시정보
	 * @param items - 검색 키워드 리스트
	 * @return Series 목록
	 *//*
	private List<Object> getSeries(Result result, LinkedHashMap<String, String> categoriesMap, String term){
		GroupResult[] groupResults = null;
		LinkedHashMap<String, Integer> dataMap = null;
		List<Object> list = new ArrayList<Object>();

			dataMap = new LinkedHashMap<String, Integer>();
			
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
				seriesMap.put("name", term);
				seriesMap.put("type", "column");
				seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
				list.add(0, seriesMap);

				HashMap<String, Object> seriesMap2 = new HashMap<String, Object>();
				seriesMap2.put("name", term);
				seriesMap2.put("type", "line");
				seriesMap2.put("data", new ArrayList<Integer>(dataMap.values()));
				
				list.add(1, seriesMap2);
			}
		
		return list;
	}
	
	*//**
	 * Query 설정.
	 * @return Query
	 *//*
	private Query createQuery(int con, UserVo vo, String period) throws Exception{
		
		char[] startTag = "<span class='result_keyword'>".toCharArray();
		char[] endTag = "</span>".toCharArray();
		Query query = null;
		
		// Query 설정
		if(con == Globals.USER_EXCEL){
			query = new Query();
		}else{
			query = new Query(startTag, endTag);
		}
		
		query.setSearch(true); // 검색 여부 설정
		query.setExtData(vo.getUserIp()); // 로그에 남길 키워드 명시
		query.setUserName(vo.getUserId()); // 로그에 남길 키워드 명시
		query.setLogKeyword("".toCharArray()); // 로그에 남길 키워드 명시
		query.setLoggable(true); // queryLog를 남길지 여부
		query.setLogExtractor("KOREAN".toCharArray()); // 분석결과 출력 위한 분석기 지정
		query.setLogExtractorOption("".toCharArray()); // 분석기의 옵션 지정
		query.setDebug(true); // 에러 리포팅 여부
		query.setFaultless(false); // true로 설정하면 검색 중 에러 발생시 검색 중단하고 ErrorMessage 객체를 반환
		query.setPrintQuery(true); // 쿼리 문자열 출력 여부
		query.setThesaurusOption((byte)Protocol.ThesaurusOption.EQUIV_SYNONYM | Protocol.ThesaurusOption.QUASI_SYNONYM);
		query.setResultModifier("priter"); //ResultModifier 로 세팅
		query.setValue("FILTER_FIELDS", Globals.FIELD_TITLE + "," + Globals.FIELD_CONTENT);
		
		// 대표 키워드 설정
		query.setSearchKeyword("");
		
		switch (con) {
		case Globals.USER_LIST: // 고객명 검색 분석
			int current = Integer.parseInt(vo.getCurrentPageNo());
			int pageSize = Integer.parseInt(vo.getPageSize());
			query.setResult((current - 1) * pageSize, (current * pageSize) -1);
			// 검색결과 옵션 설정
			// 불용어 사용함
			query.setSearchOption((byte)Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD);
			break;
		case Globals.USER_EXCEL: // 엑셀 다운로드
			query.setSearchOption((byte)Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD);
			query.setResult(0, 999);
		case Globals.USER_ALIKE: // 유사문서 TOP10
			query.setResult(0, 9);
			// 검색결과 옵션 설정
			// 불용어 사용함
			query.setSearchOption((byte)Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD | Protocol.SearchOption.BANNED | Protocol.SearchOption.PERCENT);
			// OrderBySet 설정
			query.setOrderby(createOrderBySet());
			break;
		default:
			// 검색결과 옵션 설정
			// 불용어 사용함
			query.setSearchOption((byte)Protocol.SearchOption.CACHE | Protocol.SearchOption.STOPWORD);
			query.setResult(0, 0);
			break;
		}

		// From 설정, 검색할 컬렉션을 선택
		query.setFrom(Globals.MARINER_COLLECTION);
		
		// SelectSet 설정
		query.setSelect(createSelectSet(con));
		
		// WhereSet 설정
		query.setWhere(createWhereSet(con, vo, period));
		
		if(Globals.USER_ALIKE != con){
			// FilterSet 설정
			query.setFilter(createFilterSet(con, vo));
			// GroupBySet 설정
			query.setGroupBy(createGroupBySet(con, vo));
		}
		
		return query;
		
	}
	
	*//**
	 * SelectSet 설정.
	 * @return SelectSet[]
	 *//*
	private SelectSet[] createSelectSet(int con){
		SelectSet[] selectSet = null;
		ArrayList<SelectSet> selectList = new ArrayList<SelectSet>();
		
		switch (con) {
		case Globals.USER_REPORT:
		case Globals.USER_REPORT_COMPLAIN: 
			selectList.add(new SelectSet("ID", Protocol.SelectSet.NONE));
			break;
		case Globals.USER_LIST:
		case Globals.USER_EXCEL:
			selectList.add(new SelectSet(Globals.FIELD_TITLE, Protocol.SelectSet.HIGHLIGHT));
			selectList.add(new SelectSet(Globals.FIELD_CONTENT,  (byte)(Protocol.SelectSet.SUMMARIZE | Protocol.SelectSet.HIGHLIGHT),250));
			selectList.add(new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_ID, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_USER_NM, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_USER_NO, Protocol.SelectSet.NONE));
			break;
		case Globals.USER_DETAIL:
			selectList.add(new SelectSet(Globals.FIELD_TITLE, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
			break;
		case Globals.USER_ALIKE:
			selectList.add(new SelectSet(Globals.FIELD_TITLE, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_CONTENT, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet(Globals.FIELD_REGDATE, Protocol.SelectSet.NONE));
			selectList.add(new SelectSet("WEIGHT", Protocol.SelectSet.NONE));
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
	private WhereSet[] createWhereSet(int con, UserVo vo, String keyword) throws Exception{
		
		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereList = new ArrayList<WhereSet>();
		
		switch (con) {
			case Globals.USER_LIST:
			case Globals.USER_EXCEL:
				whereList.add(new WhereSet(Globals.IDX_USER_NM, Protocol.WhereSet.OP_HASALL, vo.getTerm(), 100));
				
				if(vo.getUserId() != null && !vo.getUserId().equals(Globals.COM_SELECT_ALL)){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Globals.IDX_USER_NO, Protocol.WhereSet.OP_HASALL, vo.getUserId(), 100));
				}
				
				if(vo.getExclusion() != null && !vo.getExclusion().isEmpty()){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
					List<String> exclusions = Arrays.asList(vo.getExclusion().split(","));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, (String[])exclusions.toArray(), new int[]{500,500,500}));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, (String[])exclusions.toArray(), new int[]{100,100,100}));
					whereList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				}
				break;
			case Globals.USER_REPORT: // 고객명 검색 분석
				whereList.add(new WhereSet(Globals.FIELD_USER_NM, Protocol.WhereSet.OP_HASALL, vo.getTerm(), 100));
				
				if(vo.getUserId() != null && !vo.getUserId().equals(Globals.COM_SELECT_ALL)){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Globals.IDX_USER_NO, Protocol.WhereSet.OP_HASALL, vo.getUserId(), 100));
				}
				
				if(vo.getExclusion() != null && !vo.getExclusion().isEmpty()){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
					List<String> exclusions = Arrays.asList(vo.getExclusion().split(","));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, (String[])exclusions.toArray(), new int[]{500,500,500}));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, (String[])exclusions.toArray(), new int[]{100,100,100}));
					whereList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				}
				break;
			case Globals.USER_REPORT_COMPLAIN:
				whereList.add(new WhereSet(Globals.FIELD_USER_NM, Protocol.WhereSet.OP_HASALL, vo.getTerm(), 100)); // 고객명
				whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
				whereList.add(new WhereSet(Globals.IDX_COMPLAIN, Protocol.WhereSet.OP_HASALL, Globals.COM_COMPLAIN_Y)); // 불만문서
				
				if(vo.getUserId() != null && !vo.getUserId().equals(Globals.COM_SELECT_ALL)){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_AND));
					whereList.add(new WhereSet(Globals.IDX_USER_NO, Protocol.WhereSet.OP_HASALL, vo.getUserId(), 100));
				}
				
				if(vo.getExclusion() != null && !vo.getExclusion().isEmpty()){
					whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
					List<String> exclusions = Arrays.asList(vo.getExclusion().split(","));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
					whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, (String[])exclusions.toArray(), new int[]{500,500,500}));
					whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
					whereList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, (String[])exclusions.toArray(), new int[]{100,100,100}));
					whereList.add(new WhereSet (Protocol.WhereSet.OP_BRACE_CLOSE));
				}
				break;
			case Globals.USER_DETAIL:
				whereList.add(new WhereSet("ID", Protocol.WhereSet.OP_HASALL, vo.getId()));
				break;
			case Globals.USER_ALIKE:
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_OPEN));
				whereList.add(new WhereSet(Globals.IDX_TITLE, Protocol.WhereSet.OP_HASANY, vo.getTitle(), 100));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_OR));
				whereList.add(new WhereSet(Globals.IDX_CONTENT, Protocol.WhereSet.OP_HASANY, vo.getContent(), 10));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_BRACE_CLOSE));
				whereList.add(new WhereSet(Protocol.WhereSet.OP_NOT));
				whereList.add(new WhereSet(Globals.IDX_ID, Protocol.WhereSet.OP_HASALL, vo.getId(), 0));
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
	 * @throws ParseException 
	 *//*
	private FilterSet[] createFilterSet(int con, UserVo vo) throws ParseException{
		String strDate = "";
		String endDate = "";
		FilterSet[] filterSet = null;
		ArrayList<FilterSet> filterList = new ArrayList<FilterSet>();
		
		switch (con) {
			case Globals.USER_REPORT:
			case Globals.USER_LIST:
			case Globals.USER_EXCEL:
			case Globals.USER_REPORT_COMPLAIN:
				strDate = vo.getStartDate().replace("/", "") + "000000";
				endDate = vo.getEndDate().replace("/", "") + "235959";
				filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), "REGDATE", new String[]{strDate, endDate}, 0));
				
				if(vo.getNeeds() != null && !vo.getNeeds().equals(Globals.COM_SELECT_ALL)){
					filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_MATCH), Globals.FILTER_NEEDS_TYPE_CD, vo.getNeeds(), 0));
				}
				
				break;
			default:
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
	private GroupBySet[] createGroupBySet(int con, UserVo vo){

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupList = new ArrayList<GroupBySet>();
		
		switch (con) {
			case  Globals.USER_REPORT:
				groupList.add(new GroupBySet(Globals.GROUP_COMPLAIN, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", "")); // 불만/비불만 문서
				groupList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "DESC", "")); // 키워드 클라우드
				break;
			case Globals.USER_REPORT_COMPLAIN:
				groupList.add(new GroupBySet(vo.getCondition(), (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_NAME), "ASC", "")); // 불만문서 트렌드
				break;
			case Globals.USER_LIST:
			case Globals.USER_EXCEL:
				groupList.add(new GroupBySet(Globals.GROUP_NEEDS_TYPE_NM, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", vo.getNeedsType()));
				groupList.add(new GroupBySet(Globals.GROUP_USER_NO, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
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
	
	*//**
	 * OrderBySet 설정.
	 * @return OrderBySet[]
	 *//*
	private OrderBySet[] createOrderBySet(){
		
		OrderBySet[] orderbySet = null;
		orderbySet = new OrderBySet[]{new OrderBySet(true, "WEIGHT", Protocol.OrderBySet.OP_NONE)};
		return orderbySet;
	}
}
*/