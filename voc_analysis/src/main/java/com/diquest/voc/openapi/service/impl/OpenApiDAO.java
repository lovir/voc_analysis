package com.diquest.voc.openapi.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("openApiDAO")
public class OpenApiDAO extends EgovAbstractDAO {

	/**
	 * 자동 분류 추천 전체 누적 건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectTotalCategorizeCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectTotalCategorizeCount");
	}
	
	/**
	 * 서울시 일별 기상 관측 정보 전체 누적 건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectTotalWeatherObserveCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectTotalWeatherObserveCount");
	}
	
	/**
	 * 서울시 일별 기상 예측 정보 전체 누적 건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectTotalWeatherForecastCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectTotalWeatherForecastCount");
	}
	
	/**
	 * 서울시 일별 기상 관측 정보 오늘 수집 건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectTodayWeatherObserveCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectTodayWeatherObserveCount");
	}
	
	/**
	 * 서울시 일별 기상 예측 정보 오늘 수집 건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectTodayWeatherForecastCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectTodayWeatherForecastCount");
	}
	
	/**
	 * 자동 분류 추천 일자건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectDayCategorizeCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectDayCategorizeCount");
	}
	
	/**
	 * 서울시 일별 기상 관측 정보 일자건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectDayWeatherObserveCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectDayWeatherObserveCount");
	}
	
	/**
	 * 서울시 일별 기상 예측 정보 일자건수 조회
	 */
	@SuppressWarnings("unchecked")
	public String selectDayWeatherForecastCount() throws Exception {
		return (String) getSqlMapClientTemplate().queryForObject("openApiDAO.selectDayWeatherForecastCount");
	}
	/**
	 * 자동 분류 추천 일자별 리스트 조회
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectDayCategorizeList(HashMap<String, String> paramMap) throws Exception {
		return list("openApiDAO.selectDayCategorizeList", paramMap);
	}
	
	/**
	 * 서울시 일별 기상 관측 정보 일자별 리스트 조회
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectDayWeatherObserveList(HashMap<String, String> paramMap) throws Exception {
		return list("openApiDAO.selectDayWeatherObserveList", paramMap);
	}
	
	/**
	 * 서울시 일별 기상 예측 정보 일자별 리스트 조회
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectDayCWeatherForecastList(HashMap<String, String> paramMap) throws Exception {
		return list("openApiDAO.selectDayCWeatherForecastList", paramMap);
	}
	
}
