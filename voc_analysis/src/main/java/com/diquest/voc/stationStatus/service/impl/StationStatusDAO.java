package com.diquest.voc.stationStatus.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.diquest.voc.stationStatus.vo.StationStatusVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("stationStatusDAO")
public class StationStatusDAO extends EgovAbstractDAO {

    /**
	 * 지하철 호선 조회
	 */
	@SuppressWarnings("unchecked")
    public List<String> selectLine(String stationName) throws Exception {
        return list("stationStatusDAO.stationLine", stationName);
    }
	
	/**
	 * 처리 주무 부서 조회
	 */
	@SuppressWarnings("unchecked")
    public List<HashMap<String, Object>> selectMetroList(HashMap<String, Object> deptCodeList) throws Exception {
        return list("stationStatusDAO.metroList", deptCodeList);
    }
}
