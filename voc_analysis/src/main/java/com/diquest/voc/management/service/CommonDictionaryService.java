package com.diquest.voc.management.service;

import java.util.HashMap;

import com.diquest.voc.management.vo.DictionaryVo;

public interface CommonDictionaryService {

	/**
	 * 파일 조회
	 * @param alarmVo
	 * @return
	 * @throws Exception
	 */
	public HashMap downloadDictionary(DictionaryVo vo) throws Exception;
	
	/**
	 * 파일 업로드
	 * @param alarmVo
	 * @return
	 * @throws Exception
	 */
	public int uploadDictionary(DictionaryVo vo) throws Exception;
}
