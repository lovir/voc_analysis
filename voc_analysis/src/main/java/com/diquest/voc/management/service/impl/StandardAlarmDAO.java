package com.diquest.voc.management.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.StandardAlarmVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("standardAlarmDAO")
public class StandardAlarmDAO extends EgovAbstractDAO {

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 발생레벨 등록한다.
	 * @param vo - 등록할 정보가 담긴 StandardAlarmVo
	 * @return 등록 결과
	 * @exception Exception
	 */
	public String insertStandardAlarm(StandardAlarmVo vo) throws Exception {
		return String.valueOf(update("standardAlarmDAO.insertStandardAlarm", vo));
	}

	/**
	 * 발생레벨 수정한다.
	 * @param vo - 수정할 정보가 담긴 StandardAlarmVo
	 * @return void형
	 * @exception Exception
	 */
	public int updateStandardAlarm(StandardAlarmVo vo) throws Exception {
		return update("standardAlarmDAO.updateStandardAlarm", vo);
	}

	/**
	 * 발생레벨 조회한다.
	 * @param vo - 조회할 정보가 담긴 StandardAlarmVo
	 * @return 조회한 글
	 * @exception Exception
	 */
	public StandardAlarmVo selectStandardAlarm(StandardAlarmVo vo) throws Exception {
		return (StandardAlarmVo) selectByPk("standardAlarmDAO.selectStandardAlarm", vo);
	}
}
