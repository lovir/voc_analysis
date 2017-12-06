package com.diquest.voc.fieldStatus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.diquest.voc.fieldStatus.vo.FieldStatusVo;
import com.diquest.voc.stationStatus.vo.StationStatusVo;

public interface FieldStatusService {

	public ArrayList<HashMap<String, String>> getFieldChart(FieldStatusVo vo) throws Exception;
	public String getFieldKeywords(FieldStatusVo vo) throws Exception;
	public HashMap<String, Object> getSynthesisReport(FieldStatusVo vo) throws Exception;
	public HashMap<String, Object> getSearchResult(FieldStatusVo vo) throws Exception;
	public HashMap<String, Object> getExcelResult(FieldStatusVo vo) throws Exception;
}
