package com.diquest.voc.common.vo;

import java.io.Serializable;

public class BaseVo implements Serializable{

	/** BaseVo serialVersionUID */
	private static final long serialVersionUID = 2836220422791279650L;
	/** 사용자 아이피  */
	private static String userIp="";
	/** 사용자 아이디  */
	private static String userId="";
	/** 통합 검색어  */
	private String searchTerm;
	/** 조회 조건 */
	private String condition;
	/** 시작일 */
	private String startDate;
	/** 종료일 */
	private String endDate;
	/** 검색어  */
	private String term;
	/** 검색어(점유율 검색)  */
	private String shareTerm;
	/** 표시건수 */
	private String pageSize;
	/** 현재페이지  */
	private String currentPageNo;
	/** 비교분석  */
	private String compare;
	/** VOE 대분류  */
	private String lcls;
	/** VOE 중분류  */
	private String mcls;
	
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
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
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getShareTerm() {
		return shareTerm;
	}
	public void setShareTerm(String shareTerm) {
		this.shareTerm = shareTerm;
	}
	public String getPageSize() {
		if(pageSize.isEmpty()){
			pageSize = "10";
		}
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getCurrentPageNo() {
		if(currentPageNo.isEmpty()){
			currentPageNo = "1";
		}
		return currentPageNo;
	}
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	public String getCompare() {
		return compare;
	}
	public void setCompare(String compare) {
		this.compare = compare;
	}
	public String getLcls() {
		return lcls;
	}
	public void setLcls(String lcls) {
		this.lcls = lcls;
	}
	public String getMcls() {
		return mcls;
	}
	public void setMcls(String mcls) {
		this.mcls = mcls;
	}
	
}
