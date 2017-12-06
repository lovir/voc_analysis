package com.diquest.voc.management.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.CommandSearchRequest;
import com.diquest.ir.common.msg.protocol.Protocol;
import com.diquest.ir.common.msg.protocol.query.FilterSet;
import com.diquest.ir.common.msg.protocol.query.GroupBySet;
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
import com.diquest.voc.management.service.AlarmKeywordService;
import com.diquest.voc.management.service.StandardAlarmService;
import com.diquest.voc.management.vo.AlarmKeywordMonitoringVo;
import com.diquest.voc.management.vo.AlarmKeywordVo;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.StandardAlarmVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service("alarmKeywordService")
public class AlarmKeywordServiceImpl implements AlarmKeywordService{

	private Gson gson = null;
	
	/** StandardAlarmService */
	@Resource(name = "standardAlarmService")
	private StandardAlarmService standardAlarmService;
	
	/** alarmKeywordDAO */
	@Resource(name="alarmKeywordDAO")
	private AlarmKeywordDAO alarmKeywordDAO;
	
	/** commonSelectBoxDAO */
	@Resource(name = "commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;

	@Override
	public int insertAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception {
		return alarmKeywordDAO.insertAlarmKeyword(alarmVo);
	}
	
	@Override
	public int updateAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception {
		return alarmKeywordDAO.updateAlarmKeyword(alarmVo);
	}
	
	@Override
	public int deleteAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception {
		return alarmKeywordDAO.deleteAlarmKeyword(alarmVo);
	}
	
	@Override
	public AlarmKeywordVo selectAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception {
		return (AlarmKeywordVo) alarmKeywordDAO.selectAlarmKeyword(alarmVo);
	}
	
	@Override
	public HashMap<String, Object> selectAlarmKeywordList(AlarmKeywordVo alarmVo) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", alarmKeywordDAO.selectAlarmKeywordList(alarmVo));
		map.put("totSize", alarmKeywordDAO.selectAlarmKeywordListTotCnt(alarmVo));
		
		return map; 
	}
	
	@Override
	public int checkAlarmRegYnCount(AlarmKeywordVo alarmVo) throws Exception {
		return alarmKeywordDAO.checkAlarmRegYnCount(alarmVo);
	}
	
	@Override
	public HashMap<String, Object> selectAlarmKeywordMonitoringList(AlarmKeywordMonitoringVo monitoringVo) throws Exception {
		QuerySet querySet = null;
		Result[] resultList = null;
		CommandSearchRequest command = null;
		ArrayList<Query> queryList = new ArrayList<Query>();
		gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		
		// 모니터링 키워드 리스트
		List<AlarmKeywordMonitoringVo> alarmList = alarmKeywordDAO.selectAlarmKeywordMonitoringList(monitoringVo);
		QueryParser queryParser = new QueryParser();
		for(int i=0; i < alarmList.size(); i++){
			AlarmKeywordMonitoringVo vo = alarmList.get(i);
			vo.setPeriod(monitoringVo.getPeriod());
			Query query = createQuery(vo);
			//System.out.println("#### " + vo.getKeyword() + " : " + queryParser.queryToString(query));
			queryList.add(query); // 키워드 수만큼	
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

		return getAlarmList(resultList, alarmList, monitoringVo); 
	}

	/**
	 * categories 만들기 위한 날짜 맵 생성
	 * @return categories구성용 맵
	 */
	private LinkedHashMap<String, String> getCategoriesWeek(){
		//String endDate = DateUtil.getCurrentDate("yyyyMMdd");
		//String startDate = DateUtil.addYearMonthDay("yyyyMMdd", endDate, Calendar.DAY_OF_MONTH, -6);
		String endDate = DateUtil.getCurrentDate("yyyyMMdd");
		//String startDate = DateUtil.getFirstDayOfWeek("yyyyMMdd", endDate, 0);
		String startDate = DateUtil.addYearMonthDay("yyyyMMdd", endDate, Calendar.DAY_OF_MONTH, -6);
		return DateUtil.getCategories(startDate + "235959", endDate + "000000", "MM월 dd일", Globals.TREND_PERIOD_DAY);
	}
	
	/**
	 * categories 만들기 위한 날짜 맵 생성
	 * @return categories구성용 맵
	 */
	private LinkedHashMap<String, String> getCategoriesDay(String startDate, String endDate){
		//String endDate = DateUtil.getCurrentDate("yyyyMMdd");
		//String startDate = DateUtil.addYearMonthDay("yyyyMMdd", endDate, Calendar.DAY_OF_MONTH, -6);
		//String endDate = DateUtil.getCurrentDate("yyyyMMdd");
		//String startDate = DateUtil.addYearMonthDay("yyyyMMdd", endDate, Calendar.DAY_OF_MONTH, -1);
		return DateUtil.getCategories(startDate + "235959", endDate + "000000", "HH시", Globals.TREND_PERIOD_HOUR);
	}
	
	/**
	 * 검색결과 글목록을 화면 표시용으로 가공.
	 * @param result - 검색 결과 Result
	 * @return 글 목록
	 * @throws Exception 
	 */
	private HashMap<String, Object> getAlarmList(Result[] resultList, List<AlarmKeywordMonitoringVo> alarmList, AlarmKeywordMonitoringVo vo) throws Exception {
		List<Object> alarmkeywordList = new ArrayList<Object>();
		List<Object> alarmStateList = new ArrayList<Object>();
		List<Object> seriesList = new ArrayList<Object>();
		
		GroupResult[] groupResults = null;
		
		Map<String, Integer> dataMap = null;
		Map<String, Integer> countMap = null;
		Map<String, String> categoriesMap = new HashMap<String, String>();
		
		HashMap<String, String> alarmMap = null;
		HashMap<String, String> alarmStateMap = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		int day = 1;
		
		String endDate = DateUtil.getCurrentDate("yyyyMMdd");
		String startDate = DateUtil.addYearMonthDay("yyyyMMdd", endDate, Calendar.DAY_OF_MONTH, -1);
		
		// 알람 키워드, 목록
		for(int i = 0; i < alarmList.size(); i++) {
			dataMap = new HashMap<String, Integer>();
			countMap = new HashMap<String, Integer>();
			categoriesMap = new HashMap<String, String>();
			
			if(vo.getPeriod() == Globals.COM_ALARM_KEYWORD_WEEK){ // 지난주
				categoriesMap = getCategoriesWeek();
				
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
				day = dataMap.size();
			}else if(vo.getPeriod() == Globals.COM_ALARM_KEYWORD_CURRENT){ // 오늘
				categoriesMap = getCategoriesDay(endDate, endDate);
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
			}else{ // 어제
				categoriesMap = getCategoriesDay(startDate, startDate);
				for(String categories : categoriesMap.keySet()){
					dataMap.put(categories, 0);
				}
			}
			
			Result result = (Result)resultList[i];
			AlarmKeywordVo alarm = alarmList.get(i);
			
			if(result.getGroupResultSize() != 0){
				float count = 0.f;
				int totalCnt = 0;
				String cdVocItem = "";
				String cdVocChannel = "";
				
				alarmMap = new HashMap<String, String>();
				alarmStateMap = new HashMap<String, String>();
				groupResults = result.getGroupResults();
				for (int j = 0; j < groupResults.length; j++) {
					int rSize = groupResults[j].groupResultSize();
					if(j == 0){ // 날짜 그룹
						for (int k = 0; k < rSize; k++) {
							count += groupResults[j].getIntValue(k);
							dataMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					}
					else if(j == 1){ // 키워드 그룹
						for (int k = 0; k < rSize; k++) {
							totalCnt += groupResults[j].getIntValue(k);
							countMap.put(new String(groupResults[j].getId(k)), groupResults[j].getIntValue(k));
						}
					}
					else if(j == 2){ // VOC 소분류 그룹
						for (int k = 0; k < rSize; k++) {
							cdVocItem = new String(groupResults[j].getId(k));
							if(cdVocItem != null && !"".equals(cdVocItem)) break;
						}
					}
					else if(j == 3){ // 접수채널 그룹
						for (int k = 0; k < rSize; k++) {
							cdVocChannel = new String(groupResults[j].getId(k));
							if(cdVocChannel != null && !"".equals(cdVocChannel)) break;
						}
					}
				}
				
				HashMap<String, Object> seriesMap = new HashMap<String, Object>();
				seriesMap.put("name", alarm.getKeyword());
				seriesMap.put("data", new ArrayList<Integer>(dataMap.values()));
				seriesList.add(seriesMap);
				
				alarmMap.put("count", String.format("%.1f", count/day)); // 평균의 기준이 정해지면 수정
				alarmMap.put("keyword", alarm.getKeyword());
				float keywordCnt = 0.f;
				float share = 0.f;
				int yesterday = 0;
				int today = 0;
				
				if(countMap.containsKey(alarm.getKeyword())){
					keywordCnt = countMap.get(alarm.getKeyword());
				}
				
				share = (keywordCnt/totalCnt)*100;
				
				if(Float.isNaN(share)){
					share = 0;
				}
				
				alarmMap.put("share", String.format("%.3f", share));
				alarmkeywordList.add(alarmMap);	
				
				float percentage = 0.f;
				
				if(dataMap.containsKey(startDate)){
					yesterday = dataMap.get(startDate);
				}
				
				if(dataMap.containsKey(endDate)){
					today = dataMap.get(endDate);
				}
				
				if(today>0){
					percentage = ((today-yesterday)/today)*100;
					
					if(Float.isNaN(percentage)){
						percentage = 0;
					}
				}
				
				String vocItemName = "";
				String channelName = "";
				if(!"".equals(cdVocItem)){
					CommonSelectBoxVo commonSelectBoxVo = new CommonSelectBoxVo();
					commonSelectBoxVo.setCode(cdVocItem);
					CommonSelectBoxVo resultVo = commonSelectBoxDAO.Item(commonSelectBoxVo);
					vocItemName = resultVo.getName();
				}
				if(!"".equals(cdVocChannel)){
					CommonSelectBoxVo commonSelectBoxVo = new CommonSelectBoxVo();
					commonSelectBoxVo.setCode(cdVocChannel);
					CommonSelectBoxVo resultVo = commonSelectBoxDAO.vocChannel(commonSelectBoxVo);
					channelName = resultVo.getName();
				}
				alarmStateMap.put("yesterday", String.valueOf(yesterday));
//				alarmStateMap.put("today", String.valueOf(today));
//				alarmStateMap.put("percentage", String.valueOf(percentage));
				alarmStateMap.put("keyword", alarm.getKeyword());	//해당 알람 키워드
				
				alarmStateMap.put("vocItemName", vocItemName);	//가장 많은 소분류
				alarmStateMap.put("channelName", channelName);	//가장 많은 접수 채널
				
				alarmStateList.add(alarmStateMap);
			}
		}
		
		StandardAlarmVo alarmVo = new StandardAlarmVo();
		alarmVo = standardAlarmService.selectStandardAlarm(new StandardAlarmVo());
		
		map.put("alarmStateList", alarmStateList);
		map.put("resultList", alarmkeywordList);
		map.put("categories", gson.toJson(categoriesMap.values()));
		map.put("series", gson.toJson(seriesList));
		map.put("alarm", alarmVo);
		map.put("period", vo.getPeriod());
		
		return map;
	}
	
	/**
	 * Query 설정.
	 * @return Query
	 */
	private Query createQuery(AlarmKeywordMonitoringVo monitoringVo) throws Exception{
		// Query 설정
		Query query = new Query("", "");
		query.setSearch(true); // 검색 여부 설정
		//query.setResultCutOffSize(0); // 최대 검색 가능 문서 개수 제한
		//query.setMaxHighlight(100); // 검색 시 강조하는 키워드 최대 개수 제한
		query.setLogKeyword("".toCharArray()); // 로그에 남길 키워드 명시
		query.setLoggable(false); // queryLog를 남길지 여부
		//query.setLogExtractor("KOREAN".toCharArray()); // 분석결과 출력 위한 분석기 지정
		//query.setLogExtractorOption("".toCharArray()); // 분석기의 옵션 지정
		//query.setUserName(monitoringVo.getRegNm());
		//query.setExtData("extData"); // 검색로그에 부가 정보 출력
		//query.setRedirect("검색어"); // 바로가가 목록 설정
		//query.setRecommend("추천어"); // 추천어 목록 설정
		query.setDebug(true); // 에러 리포팅 여부
		query.setFaultless(true); // true로 설정하면 검색 중 에러 발생시 검색 중단하고 ErrorMessage 객체를 반환
		query.setPrintQuery(false); // 쿼리 문자열 출력 여부
		
		// 검색결과 옵션 설정
		// 불용어 사용함
		query.setSearchOption((byte)(Protocol.SearchOption.CACHE));
		
		// 유의어 사용함
		query.setThesaurusOption((byte) (Protocol.ThesaurusOption.QUASI_SYNONYM | Protocol.ThesaurusOption.EQUIV_SYNONYM));	
		
		// 대표 키워드 설정
		query.setSearchKeyword("");

		query.setResult(0, 0);

		// From 설정, 검색할 컬렉션을 선택
		query.setFrom(Globals.MARINER_COLLECTION);
		
		// SelectSet 설정
		query.setSelect(createSelectSet());
		
		// WhereSet 설정
		query.setWhere(createWhereSet(monitoringVo));
		
		// FilterSet 설정
		query.setFilter(createFilterSet(monitoringVo));
		
		// GroupBySet 설정
		query.setGroupBy(createGroupBySet(monitoringVo));
		
		return query;
		
	}
	
	/**
	 * SelectSet 설정.
	 * @return SelectSet[]
	 */
	private SelectSet[] createSelectSet(){
		SelectSet[] selectSet = null;
		
		selectSet = new SelectSet[]{
			new SelectSet(Globals.FIELD_DQ_DOCID, Protocol.SelectSet.NONE)
		};
		
		return selectSet;
	}
	
	/**
	 * WhereSet 설정.
	 * @return WhereSet[]
	 */
	private WhereSet[] createWhereSet(AlarmKeywordVo vo) throws Exception{
		WhereSet[] whereSet = null;
		ArrayList<WhereSet> whereList = new ArrayList<WhereSet>();
		
		whereList.add(new WhereSet(Globals.IDX_KEYWORD, Protocol.WhereSet.OP_HASALL, vo.getKeyword()));
				
		whereSet = new WhereSet[whereList.size()];
		
		for(int i=0; i < whereList.size(); i++){
			whereSet[i] = (WhereSet) whereList.get(i);
		}
		
		return whereSet;
	}
	
	/**
	 * FilterSet 설정.
	 * @return FilterSet[]
	 * @throws ParseException 
	 */
	private FilterSet[] createFilterSet(AlarmKeywordMonitoringVo monitoringVo) throws ParseException{
		String startDate = "";
		String endDate = "";
		String today = DateUtil.getCurrentDate("yyyyMMdd");
		
		FilterSet[] filterSet = null;
		ArrayList<FilterSet> filterList = new ArrayList<FilterSet>();
		
		switch (monitoringVo.getPeriod()) {
			case Globals.COM_ALARM_KEYWORD_PREV: // 전날
				startDate = DateUtil.addYearMonthDay("yyyyMMdd", today, Calendar.DAY_OF_MONTH, -1) + "000000";
				endDate = DateUtil.addYearMonthDay("yyyyMMdd", today, Calendar.DAY_OF_MONTH, -1) + "235959";
				break;
			case Globals.COM_ALARM_KEYWORD_CURRENT: // 오늘
				startDate = today + "000000";
				endDate = today + "235959";
				break;
			case Globals.COM_ALARM_KEYWORD_WEEK: // 지난 일주일
				startDate = DateUtil.addYearMonthDay("yyyyMMdd", today, Calendar.DAY_OF_MONTH, -6) + "000000";
				endDate = today + "235959";
				break;
			default:
				startDate = DateUtil.addYearMonthDay("yyyyMMdd", today, Calendar.DAY_OF_MONTH, -1) + "000000";
				endDate = today + "235959";
				break;
		}
		
		filterList.add(new FilterSet((byte)(Protocol.FilterSet.OP_RANGE), Globals.FILTER_REGDATE, new String[]{startDate, endDate}, 0));
		
		filterSet = new FilterSet[filterList.size()];
		
		for(int i=0; i < filterList.size(); i++){
			filterSet[i] = (FilterSet) filterList.get(i);
		}
		
		return filterSet;
	}
	
	/**
	 * GroupBySet 설정.
	 * @return GroupBySet[]
	 */
	private GroupBySet[] createGroupBySet(AlarmKeywordMonitoringVo monitoringVo){

		GroupBySet[] groupBys = null;
		ArrayList<GroupBySet> groupList = new ArrayList<GroupBySet>();

		switch (monitoringVo.getPeriod()) {
			case Globals.COM_ALARM_KEYWORD_PREV: // 전날
			case Globals.COM_ALARM_KEYWORD_CURRENT: // 오늘
				groupList.add(new GroupBySet(Globals.GROUP_HOUR, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
				break;
			case Globals.COM_ALARM_KEYWORD_WEEK: // 지난 일주일
			default:
				groupList.add(new GroupBySet(Globals.GROUP_DAY, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", ""));
				break;
		}
		
		groupList.add(new GroupBySet(Globals.GROUP_KEYWORD, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "")); // 키워드 점유율
		//서울메트로 용 추가
		groupList.add(new GroupBySet(Globals.GROUP_CDVOCITEM, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "0 1")); //키워드 별 가장 많은 VOC 소분류 추출용
		groupList.add(new GroupBySet(Globals.GROUP_CDVOCCHANNEL, (byte)(Protocol.GroupBySet.OP_COUNT | Protocol.GroupBySet.ORDER_COUNT), "DESC", "0 1")); //키워드 별 가장 많은 접수채널 추출용
		
		groupBys = new GroupBySet[groupList.size()];
		
		for(int i=0; i < groupList.size(); i++){
			groupBys[i] = (GroupBySet) groupList.get(i);
		}
		
		return groupBys;
	}
	
}
