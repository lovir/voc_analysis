package com.diquest.voc.socialKeywordRanking.vo;

import java.util.List;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

@SuppressWarnings("serial")
public class SocialKeywordRankingVo extends CommonSelectBoxVo {
	private String login_Id;	//사용자 ID
	private String[] keywordArr;	//상위키워드 (5개까지)
	private String[] interestKeywordArr ;	//관심 키워드	종합 랭킹 분석표에서 구분하기위함
	private String keyword;	//최상위 키워드
	
	private int rankingIndex = 0;	//종합랭킹분석 페이징네비에 사용
	private String startDate = "";	//시작일
	private String endDate = "";	//종료일
	private String pageType = "";	//페이지 종류
	//VOC 검색 결과 조건
	private String dq_docid = "";	//문서 ID
	private String title = "";	//문서 제목
	private String content = "";	//문서 내용
	private int pageSize = 10;	//페이지 사이즈
	private int currentPage = 1;	//현재 페이지
	private String week_select = "";	//기준년도
	private String socialChannel = "";	// 소셜 채널
	
	public String getLogin_Id() {
		return login_Id;
	}
	public void setLogin_Id(String login_Id) {
		this.login_Id = login_Id;
	}
	public String[] getKeywordArr() {
		return keywordArr;
	}
	public void setKeywordArr(String[] keywordArr) {
		this.keywordArr = keywordArr;
	}
	public String[] getInterestKeywordArr() {
		return interestKeywordArr;
	}
	public void setInterestKeywordArr(String[] interestKeywordArr) {
		this.interestKeywordArr = interestKeywordArr;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getRankingIndex() {
		return rankingIndex;
	}
	public void setRankingIndex(int rankingIndex) {
		this.rankingIndex = rankingIndex;
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
	public String getDq_docid() {
		return dq_docid;
	}
	public void setDq_docid(String dq_docid) {
		this.dq_docid = dq_docid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getWeek_select() {
		return week_select;
	}
	public void setWeek_select(String week_select) {
		this.week_select = week_select;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public String getSocialChannel() {
		return socialChannel;
	}
	public void setSocialChannel(String socialChannel) {
		this.socialChannel = socialChannel;
	}
	
}
