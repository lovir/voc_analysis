package com.diquest.voc.management.vo;

import java.io.Serializable;

/**  
 * @Class Name : InterestKeywordVo.java
 * @Description : InterestKeywordVo Class
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
public class InterestKeywordVo implements Serializable{

	/** InterestKeywordVo의 Serializable */
	private static final long serialVersionUID = -2254482900563185657L;
	
	/** 키워드 */
	private String keyword;
	/** 조직명 */
	private String orgNm;
	/** 활성여부 */
	private String useYn;
	/** 등록자 */
	private String regId;
	/** 등록자 */
	private String regNm;
	/** 비고 */
	private String etc;
	/** 등록일 */
	private String regDate;
	/** No. */
	private int no;
	/** 표시건수 */
	private int pageSize;
	/** 현재페이지  */
	private int currentPageNo;
	/** 현재페이지  */
	private int[] selectedNo;
	/** 검색조건  */
	private String condition;
	/** 대시보드 활성여부 */
	private String dashYn;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
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
	public String getEtc() {
		return etc;
	}
	public void setEtc(String etc) {
		this.etc = etc;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPageNo() {
		return currentPageNo;
	}
	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	public int[] getSelectedNo() {
		return selectedNo;
	}
	public void setSelectedNo(int[] selectedNo) {
		this.selectedNo = selectedNo;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getDashYn() {
		return dashYn;
	}
	public void setDashYn(String dashYn) {
		this.dashYn = dashYn;
	}
	
}
