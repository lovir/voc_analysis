package com.diquest.voc.management.service;

import java.util.HashMap;

import com.diquest.voc.management.vo.AlarmKeywordMonitoringVo;
import com.diquest.voc.management.vo.AlarmKeywordVo;

/**  
 * @Class Name : AlarmKeywordService.java
 * @Description : AlarmKeywordService Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.04.30           최초생성
 * 
 * @author 박소영
 * @since 2014. 04.30
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */


public interface AlarmKeywordService {

	/**
	 * 알람 설정(대상키워드 관리)을 등록한다.
	 * @param vo - 등록 할 정보가 담긴 AlarmKeywordVo
	 * @return 등록 결과
	 * @exception Exception
	 */
	public int insertAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception;
	
	/**
	 * 알람 설정(대상키워드 관리)을 수정한다.
	 * @param vo - 수정 할 정보가 담긴 AlarmKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	public int updateAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception;
	
	/**
	 * 알람 설정(대상키워드 관리)을 삭제한다.
	 * @param vo - 삭제 대상정보가 담긴 AlarmKeywordVo
	 * @return 삭제 결과
	 * @exception Exception
	 */
	public int deleteAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception;
	
	/**
	 * 알람 설정(대상키워드 관리)을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
	 * @return 조회한 글(상세)
	 * @exception Exception
	 */
	public AlarmKeywordVo selectAlarmKeyword(AlarmKeywordVo alarmVo) throws Exception;
	
	/**
	 * 알람 설정(대상키워드 관리) 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 AlarmKeywordVo
	 * @return 글 목록
	 * @exception Exception
	 */
	public HashMap<String, Object> selectAlarmKeywordList(AlarmKeywordVo alarmVo) throws Exception;
	
	/**
	 * 알람 키워드 모니터링 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 AlarmKeywordMonitoringVo
	 * @return 글 목록
	 * @exception Exception
	 */
	public HashMap<String, Object> selectAlarmKeywordMonitoringList(AlarmKeywordMonitoringVo monitoringVo) throws Exception;
	
	/**
	 * 활성화 된 알람키워드의 갯수를 조회한다.
	 * @return
	 * @throws Exception
	 */
	public int checkAlarmRegYnCount(AlarmKeywordVo alarmVo) throws Exception;
	
}
