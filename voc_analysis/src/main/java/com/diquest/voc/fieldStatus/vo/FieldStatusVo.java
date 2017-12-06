package com.diquest.voc.fieldStatus.vo;

import java.util.ArrayList;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

public class FieldStatusVo extends CommonSelectBoxVo{
	private static String userIp="";
	private static String userId="";
	
	private String startDate = "";
	private String endDate = "";
	private int pageSize = 10;
	private ArrayList<String> codeList = null;
	private String repCode = "";
	private int currentPage = 1;
	private String keyword = "";
	private String searchType = "all";	// all : 전체검색, rep : 분야 검색, keyword : 키워드 검색
	private String docId = "";

	
	public static String getUserIp() {
		return userIp;
	}
	public static void setUserIp(String userIp) {
		FieldStatusVo.userIp = userIp;
	}
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		FieldStatusVo.userId = userId;
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
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public ArrayList<String> getCodeList() {
		return codeList;
	}
	public void setCodeList(ArrayList<String> codeList) {
		this.codeList = codeList;
	}
	public String getRepCode() {
		return repCode;
	}
	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	
}
