package com.diquest.voc.socialTrend.vo;

import java.util.ArrayList;

import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.relationAnalysis.vo.RelationAnalysisVo;


public class SocialTrendAnalysisVo extends CommonSelectBoxVo{
	
	private static String userIp="";
	private static String userId="";
	
	private String startDate = "";
	private String endDate = "";
	private int currentPage = 1;
	private int pageSize = 10;
	private int rankingIndex = 0;	//종합랭킹분석 페이징네비에 사용
	private String conditionType  ="DAY";
	private String socialChannel = "";
	/** 키워드 */
	private String keyword;
	/** 제외 키워드 */
	private String exclusion;
	private ArrayList<String> keywordList = new ArrayList<String>();
	private ArrayList<String> exclusionList = new ArrayList<String>();
	private String pageType = "";

	public String getExclusion() {
		return exclusion;
	}
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	public static String getUserIp() {
		return userIp;
	}
	public static void setUserIp(String userIp) {
		SocialTrendAnalysisVo.userIp = userIp;
	}
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		SocialTrendAnalysisVo.userId = userId;
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRankingIndex() {
		return rankingIndex;
	}
	public void setRankingIndex(int rankingIndex) {
		this.rankingIndex = rankingIndex;
	}
	public String getConditionType() {
		return conditionType;
	}
	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	public String getSocialChannel() {
		return socialChannel;
	}
	public void setSocialChannel(String socialChannel) {
		this.socialChannel = socialChannel;
	}
	public ArrayList<String> getKeywordList() {
		return keywordList;
	}
	public void setKeywordList(ArrayList<String> keywordList) {
		this.keywordList = keywordList;
	}
	public ArrayList<String> getExclusionList() {
		return exclusionList;
	}
	public void setExclusionList(ArrayList<String> exclusionList) {
		this.exclusionList = exclusionList;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	
}
