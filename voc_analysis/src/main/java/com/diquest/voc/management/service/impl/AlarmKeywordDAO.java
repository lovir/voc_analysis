package com.diquest.voc.management.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.AlarmKeywordMonitoringVo;
import com.diquest.voc.management.vo.AlarmKeywordVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("alarmKeywordDAO")
public class AlarmKeywordDAO extends EgovAbstractDAO {
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 알람키워드 등록
	 * @param vo - 등록할 정보가 담긴 AlarmKeywordVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	public int insertAlarmKeyword(AlarmKeywordVo vo) throws Exception {
		return update("alarmKeywordDAO.insertAlarmKeyword", vo);
	}

	/**
	 * 알람 키워드 정보 수정
	 * @param vo - 수정할 정보가 담긴 AlarmKeywordVO
	 * @return void형
	 * @exception Exception
	 */
	public int updateAlarmKeyword(AlarmKeywordVo vo) throws Exception {
		return update("alarmKeywordDAO.updateAlarmKeyword", vo);
	}

	/**
	 * 알람키워드 삭제
	 * @param vo - 삭제할 정보가 담긴 AlarmKeywordVO
	 * @return void형 
	 * @exception Exception
	 */
	public int deleteAlarmKeyword(AlarmKeywordVo vo) throws Exception {
		return delete("alarmKeywordDAO.deleteAlarmKeyword", vo);
	}

	/**
	 * 선택 된 키워드 조회
	 * @param vo - 조회할 정보가 담긴 AlarmKeywordVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	public AlarmKeywordVo selectAlarmKeyword(AlarmKeywordVo vo) throws Exception {
		return (AlarmKeywordVo) selectByPk("alarmKeywordDAO.selectAlarmKeyword", vo);
	}

	
	/**
	 * 등록된 알람 키워드를 조회한다.
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AlarmKeywordVo> selectAlarmKeywordList(AlarmKeywordVo vo) throws Exception {
		return list("alarmKeywordDAO.selectAlarmKeywordList", vo);
	}
	
	
	/**
	 * 등록된 알람 키워드의 Count를 조회한다.
	 * @param vo
	 * @return
	 */
	public int selectAlarmKeywordListTotCnt(AlarmKeywordVo vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("alarmKeywordDAO.selectAlarmKeywordListTotCnt", vo);
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return 글 목록
	 * @exception Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AlarmKeywordMonitoringVo> selectAlarmKeywordMonitoringList(AlarmKeywordMonitoringVo vo) throws Exception {
		return list("alarmKeywordDAO.selectAlarmKeywordMonitoringList", vo);
	}
	
	/**
	 * 활성화 된 알람키워드의 갯수를 조회한다.
	 * @return
	 */
	public int checkAlarmRegYnCount(AlarmKeywordVo vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("alarmKeywordDAO.checkAlarmRegYnCount", vo);
	}

}
