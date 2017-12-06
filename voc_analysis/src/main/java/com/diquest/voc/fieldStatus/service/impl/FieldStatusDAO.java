package com.diquest.voc.fieldStatus.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.diquest.voc.fieldStatus.vo.FieldStatusVo;
import com.diquest.voc.stationStatus.vo.StationStatusVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("fieldStatusDAO")
public class FieldStatusDAO extends EgovAbstractDAO {

	/*
	 * 민원 채널 item 구하기
	 */
	@SuppressWarnings("unchecked")
    public List<String> selectItemName(FieldStatusVo vo) throws Exception {
        return list("fieldStatusDAO.fieldItemName", vo);
    }
	/*
	 * 콜센터 채널 item 구하기
	 */
	@SuppressWarnings("unchecked")
    public List<String> selectCallItemName(FieldStatusVo vo) throws Exception {
        return list("fieldStatusDAO.fieldCallItemName", vo);
    }
}
