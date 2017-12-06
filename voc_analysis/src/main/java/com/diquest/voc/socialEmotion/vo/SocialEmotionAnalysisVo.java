package com.diquest.voc.socialEmotion.vo;

import java.util.List;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

@SuppressWarnings("serial")
public class SocialEmotionAnalysisVo extends CommonSelectBoxVo {
	private String login_Id;	//사용자 ID
	
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
	
	private String pnnSelect = "";	//긍부정,중립 선택
	private String[] senseKindArr = null;	//감성종류
	
	//감성 추세 차트 내 막대 클릭 시 검색기간, 감성(긍정,부정,중립) 선택 관련 
	private String clickColumn = "";	//막대 클릭 여부
	private String clickCondition = "";	//조회구분 
	private String clickPnn = "";		//긍정,부정,중립 
	private String clickDateStr = "";	//검색기간 
	private String clickCategory = "";	//클릭한 막대차트의 x축 문자열값
	
	private String socialChannel = "";	// 소셜 채널
	
	public String getClickCategory() {
		return clickCategory;
	}
	public void setClickCategory(String clickCategory) {
		this.clickCategory = clickCategory;
	}
	public String getClickColumn() {
		return clickColumn;
	}
	public void setClickColumn(String clickColumn) {
		this.clickColumn = clickColumn;
	}
	public String getClickCondition() {
		return clickCondition;
	}
	public void setClickCondition(String clickCondition) {
		this.clickCondition = clickCondition;
	}
	public String getClickPnn() {
		return clickPnn;
	}
	public void setClickPnn(String clickPnn) {
		this.clickPnn = clickPnn;
	}
	public String getClickDateStr() {
		return clickDateStr;
	}
	public void setClickDateStr(String clickDateStr) {
		this.clickDateStr = clickDateStr;
	}
	public String[] getSenseKindArr() {
		return senseKindArr;
	}
	public void setSenseKindArr(String[] senseKindArr) {
		this.senseKindArr = senseKindArr;
	}
	public String getPnnSelect() {
		return pnnSelect;
	}
	public void setPnnSelect(String pnnSelect) {
		this.pnnSelect = pnnSelect;
	}
	public String getLogin_Id() {
		return login_Id;
	}
	public void setLogin_Id(String login_Id) {
		this.login_Id = login_Id;
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
