package com.diquest.voc.management.service;

import com.diquest.voc.management.vo.StandardAlarmVo;

/**  
 * @Class Name : StandardAlarmService.java
 * @Description : StandardAlarmService Class
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


public interface StandardAlarmService {

	/**
	 * 알람설정 (발생기준 관리)을 등록한다.
	 * @param vo - 등록 할 정보가 담긴 StandardAlarmVo
	 * @return 등록 결과
	 * @exception Exception
	 */
	public String insertStandardAlarm(StandardAlarmVo alarmVo) throws Exception;
	
	/**
	 * 알람설정 (발생기준 관리)을 수정한다.
	 * @param vo - 수정 할 정보가 담긴 StandardAlarmVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	public int updateStandardAlarm(StandardAlarmVo alarmVo) throws Exception;
	
	/**
	 * 알람 설정(발생기준 관리)을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 StandardAlarmVo
	 * @return 조회한 글(상세)
	 * @exception Exception
	 */
	public StandardAlarmVo selectStandardAlarm(StandardAlarmVo alarmVo) throws Exception;

}
