package com.diquest.voc.openapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface OpenApiService {
	
	//자동 분류 추천 전체 누적 건수 조회
	public String selectTotalCategorizeCount() throws Exception;
	//서울시 일별 기상 관측 정보 전체 누적 건수 조회
	public String selectTotalWeatherObserveCount() throws Exception;
	//서울시 일별 기상 예측 정보 전체 누적 건수 조회
	public String selectTotalWeatherForecastCount() throws Exception;
	//서울시 일별 기상 관측 정보 오늘 수집 건수 조회
	public String selectTodayWeatherObserveCount() throws Exception;
	//서울시 일별 기상 예측 정보 오늘 수집 건수 조회
	public String selectTodayWeatherForecastCount() throws Exception;
	//자동 분류 추천 일자건수 조회
	public String selectDayCategorizeCount() throws Exception;
	//서울시 일별 기상 관측 정보 일자건수 조회
	public String selectDayWeatherObserveCount() throws Exception;
	//서울시 일별 기상 예측 정보 일자건수 조회
	public String selectDayWeatherForecastCount() throws Exception;
	//자동 분류 추천 일자별 리스트 조회
	public List<HashMap<String, String>> selectDayCategorizeList(HashMap<String, String> paramMap) throws Exception;
	//서울시 일별 기상 관측 정보 일자별 리스트 조회
	public List<HashMap<String, String>> selectDayWeatherObserveList(HashMap<String, String> paramMap) throws Exception;
	//서울시 일별 기상 예측 정보 일자별 리스트 조회
	public List<HashMap<String, String>> selectDayCWeatherForecastList(HashMap<String, String> paramMap) throws Exception;
	
	//이전 VOC 소스들. 서울메트로에서 사용 안함.
	/*
	public String openapitest(HashMap<String, String> paramMap) throws Exception;

	public ArrayList<HashMap<String, String>> openApiSearch(String call_date, String call_category, String send_receive_delimiter, String recognition_text, String agent_ext, String customer_phone_number, String call_duration, int currentPageNo, int pageSize);
	*/
}
