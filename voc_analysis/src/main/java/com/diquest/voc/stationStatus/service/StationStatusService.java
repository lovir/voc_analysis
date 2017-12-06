package com.diquest.voc.stationStatus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.diquest.voc.stationStatus.vo.StationStatusVo;

public interface StationStatusService {

	public String getStationChart(StationStatusVo vo) throws Exception;
	public String getKeywordArr(StationStatusVo vo) throws Exception;
	public HashMap<String, Object> getSearchResult(StationStatusVo vo) throws Exception;
	public HashMap<String, String> getdetailViewResult(String id) throws Exception;
	public HashMap<String, Object> getExcelResult(StationStatusVo vo) throws Exception;
}
