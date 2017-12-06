package com.diquest.voc.relationAnalysis.vo;

import java.util.ArrayList;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

public class RelationAnalysisVo extends CommonSelectBoxVo  {
	/** 사용자 아이피  */
	private static String userIp="";
	/** 사용자 아이디  */
	private static String userId="";
	private String login_Id;
	
	private String[] keywordArr;	//연관키워드 (5개까지)
	private String keyword;			//입력한 키워드
	private String exclusion;		//제외 키워드
	
	//조회 조건
	private String condition = "DAY";	//기본 일별
	private String startDate = "";
	private String endDate = "";
	
	//VOC 검색 결과 조건
	private String dq_docid = "";	//문서 ID
	private String title = "";	//문서 제목
	private String content = "";	//문서 내용
	private int pageSize = 10;	//페이지 사이즈
	private int currentPage = 1;	//현재 페이지
	
	private String pageType;	//연관도 분석, 연관도 비교분석 구분용.
	private String browserCheck = ""; //브라우저가 8이상인지 이하인지
	
	private ArrayList<String> highlightKeywordList = new ArrayList<String>();	//키워드 입력한에 "," 구분자로 구분해서 2번째 이후에 입력된 키워드들.
	
	//주제어 입력 란에 "," 구분으로 복수개의 키워드 입력 시 첫번째 키워드를 제외한 나머지 키워드를 연관도 차트에서 강조하기 위한 키워드 저장값 생성.
	public RelationAnalysisVo setMultiKeyword(RelationAnalysisVo inputVO){
		ArrayList<String> secondLayerKeywordList = new ArrayList<String>();
		
		if(inputVO.getKeyword() != null){
			String keywordString = inputVO.getKeyword();
			String[] keywordArr = keywordString.split(",");
			for(int i=0; i < keywordArr.length; i++){
				if(i == 0 ) inputVO.setKeyword(keywordArr[i].trim());
				else{
					if(!"".equals(keywordArr[i].trim()))
						secondLayerKeywordList.add(keywordArr[i].trim());
				}
			}
		}
		inputVO.setHighlightKeywordList(secondLayerKeywordList);
		return inputVO;
	}
	
	public ArrayList<String> getHighlightKeywordList() {
		return highlightKeywordList;
	}

	public void setHighlightKeywordList(ArrayList<String> highlightKeywordList) {
		this.highlightKeywordList = highlightKeywordList;
	}

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
	
	public String getBrowserCheck() {
		return browserCheck;
	}
	public void setBrowserCheck(String browserCheck) {
		this.browserCheck = browserCheck;
	}
	public String[] getKeywordArr() {
		return keywordArr;
	}
	public void setKeywordArr(String[] keywordArr) {
		this.keywordArr = keywordArr;
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
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getContent() {
		return content;
	}
	public String getDq_docid() {
		return dq_docid;
	}
	public void setDq_docid(String dq_docid) {
		this.dq_docid = dq_docid;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getLogin_Id() {
		return login_Id;
	}
	public void setLogin_Id(String login_Id) {
		this.login_Id = login_Id;
	}
	public String getExclusion() {
		return exclusion;
	}
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	
	
}
