package com.diquest.voc.management.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.voc.management.service.StandardAlarmService;
import com.diquest.voc.management.vo.StandardAlarmVo;

@Service("standardAlarmService")
public class StandardAlarmServiceImpl implements StandardAlarmService{

	/** StandardAlarmDAO */
	@Resource(name="standardAlarmDAO")
	private StandardAlarmDAO standardAlarmDAO;

	@Override
	public String insertStandardAlarm(StandardAlarmVo alarmVo) throws Exception {
		return standardAlarmDAO.insertStandardAlarm(alarmVo);
	}
	
	@Override
	public int updateStandardAlarm(StandardAlarmVo alarmVo) throws Exception {
		return standardAlarmDAO.updateStandardAlarm(alarmVo);
	}

	@Override
	public StandardAlarmVo selectStandardAlarm(StandardAlarmVo alarmVo) throws Exception {
		return (StandardAlarmVo) standardAlarmDAO.selectStandardAlarm(alarmVo);
	}

}
