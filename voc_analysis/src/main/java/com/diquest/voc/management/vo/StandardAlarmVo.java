package com.diquest.voc.management.vo;

import java.io.Serializable;

/**  
 * @Class Name : StandardAlarmVo.java
 * @Description : StandardAlarmVo Class
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
public class StandardAlarmVo implements Serializable{

	/** StandardAlarmVo의 serialVersionUID */
	private static final long serialVersionUID = -5257176054065776843L;
	
	/** No */
	private String no;
	/** 시작일 */
	private String startDate;
	/** 종료일 */
	private String endDate;
	/** 제외일 */
	private String exceptDate;
	/** 주말제외 */
	private String exceptWeekend;
	/** 발생레벨 - 상 */
	private String level1;
	/** 발생레벨 - 중 */
	private String level2;
	/** 발생레벨 - 하 */
	private String level3;
	/** 등록자 */
	private String regId;
	/** 등록일 */
	private String regDate;
	/** 이메일 */
	private String email;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getExceptDate() {
		return exceptDate;
	}
	public void setExceptDate(String exceptDate) {
		this.exceptDate = exceptDate;
	}
	public String getExceptWeekend() {
		return exceptWeekend;
	}
	public void setExceptWeekend(String exceptWeekend) {
		this.exceptWeekend = exceptWeekend;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
}
