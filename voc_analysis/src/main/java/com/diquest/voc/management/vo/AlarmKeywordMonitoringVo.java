package com.diquest.voc.management.vo;

import java.io.Serializable;

/**  
 * @Class Name : AlarmKeywordMonitoringVo.java
 * @Description : AlarmKeywordMonitoringVo Class
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
public class AlarmKeywordMonitoringVo extends AlarmKeywordVo implements Serializable {
	
	/** AlarmKeywordMonitoringVo의 serialVersionUID */
	private static final long serialVersionUID = -603941511999469557L;
	
	/** 기간 */
	private int period;
	/** 선택된 키워드 */
	private String[] selectedKeyword;
	/** 등록자 */
	private String regId;
	/** 등록자 */
	private String regNm;
	
	
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegNm() {
		return regNm;
	}
	public void setRegNm(String regNm) {
		this.regNm = regNm;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}
	public String[] getSelectedKeyword() {
		return selectedKeyword;
	}
	public void setSelectedKeyword(String[] selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}

}
