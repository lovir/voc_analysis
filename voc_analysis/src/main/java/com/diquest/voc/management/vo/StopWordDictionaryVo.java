package com.diquest.voc.management.vo;

import java.io.Serializable;

public class StopWordDictionaryVo implements Serializable{

	/** StopWordDictionaryVo serialVersionUID */
	private static final long serialVersionUID = -6213642777712729838L;

	private String loginId;
	
	private String keyword;		//입력한 키워드
	
	private String condition;	//검색필드조건
	
	private int pageSize=10;	//페이지 사이즈
	
	private int currentPage = 1;	//현재 페이지
	
	private int firstRecordIndex = 0; //페이징에 사용(쿼리)
		
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getFirstRecordIndex() {
		return firstRecordIndex;
	}
	public void setFirstRecordIndex(int firstRecordIndex) {
		this.firstRecordIndex = firstRecordIndex;
	}
}
