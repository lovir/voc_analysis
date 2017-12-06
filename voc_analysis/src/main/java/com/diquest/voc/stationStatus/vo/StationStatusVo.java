package com.diquest.voc.stationStatus.vo;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

public class StationStatusVo extends CommonSelectBoxVo{
	private static String userIp="";
	private static String userId="";
	
	private String startDate = "";
	private String endDate = "";
	private String stationCurrentPageNo = "1";
	private String stationEndPage = "12";
	private String stationTotalSize = "0";
	private String stationName = "";
	private String stationPageSize = "12";
	private String line = "";
	private String keyword = "";
	private String searchType = "all";	// 전체 검색 : all, 키워드 클릭시: keyword
	private int currentPage = 1;
	private int pageSize = 10;
	private String docId = "";
	private String title = "";
	private String content = "";
	private String pageType = "";
	
	public static String getUserIp() {
		return userIp;
	}
	public static void setUserIp(String userIp) {
		StationStatusVo.userIp = userIp;
	}
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		StationStatusVo.userId = userId;
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
	
	public String getStationCurrentPageNo() {
		return stationCurrentPageNo;
	}
	public void setStationCurrentPageNo(String stationCurrentPageNo) {
		this.stationCurrentPageNo = stationCurrentPageNo;
	}
	public String getStationTotalSize() {
		return stationTotalSize;
	}
	public void setStationTotalSize(String stationTotalSize) {
		this.stationTotalSize = stationTotalSize;
	}
	public String getStationEndPage() {
		return stationEndPage;
	}
	public void setStationEndPage(String stationEndPage) {
		this.stationEndPage = stationEndPage;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getStationPageSize() {
		return stationPageSize;
	}
	public void setStationPageSize(String stationPageSize) {
		this.stationPageSize = stationPageSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

/*	
	*//** 사용자 아이피  *//*
	private static String userIp="";
	*//** 사용자 아이디  *//*
	private static String userId="";
	private String login_Id;
	private String pageType;
	
	private String[] keywordArr;	//상위키워드 (5개까지)
	private String[] interestKeywordArr ;	//관심 키워드	종합 랭킹 분석표에서 구분하기위함
	private String keyword;	//최상위 키워드
	
	//종합 랭킹 조회 조건
	private String  condition = "DAY";	//기본 일별
	private String[] vocRequest;	
	private String[] vocService;	
	private String[] vocHomepage;	
	
	private String startDate = "";
	private String endDate = "";
	
	private int rankingIndex = 0;	//종합랭킹분석 페이징네비에 사용

	//VOC 검색 결과 조건
	private String doc_id = "";	//문서 ID
	private String title = "";	//문서 제목
	private String content = "";	//문서 내용
	private int pageSize = 10;	//페이지 사이즈
	private int currentPage = 1;	//현재 페이지
	private String week_select = "";	//기준년도
	
	private String selectNeedsType = "";	//voc 니즈유형
	private String selectCharacterType = "";	//voc 성격유형
	private String selectBusinessType = "";	//업무유형
	private String selectUsesMediaType = "";	//매체유형
	private String selectUuserLevelType = "";	//고객레벨
	private String needsType = "";
	
	private String categoryTypeList="";
	private String minwonTypeList="";
	private String guTypeList="";
	private String r_chTypeList="";
	private String deptTypeList="";
	private String socialTypeList="";
	
	private String emotion="";
	
	private List<String> fieldKeyword;
	
	
	
	public String getSocialTypeList() {
		return socialTypeList;
	}
	public void setSocialTypeList(String socialTypeList) {
		this.socialTypeList = socialTypeList;
	}
	public String getDeptTypeList() {
		return deptTypeList;
	}
	public void setDeptTypeList(String deptTypeList) {
		this.deptTypeList = deptTypeList;
	}
	public List<String> getFieldKeyword() {
		return fieldKeyword;
	}
	public void setFieldKeyword(List<String> fieldKeyword) {
		this.fieldKeyword = fieldKeyword;
	}
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	public String getR_chTypeList() {
		return r_chTypeList;
	}
	public void setR_chTypeList(String r_chTypeList) {
		this.r_chTypeList = r_chTypeList;
	}
	public String getCategoryTypeList() {
		return categoryTypeList;
	}
	public void setCategoryTypeList(String categoryTypeList) {
		this.categoryTypeList = categoryTypeList;
	}
	public String getMinwonTypeList() {
		return minwonTypeList;
	}
	public void setMinwonTypeList(String minwonTypeList) {
		this.minwonTypeList = minwonTypeList;
	}
	public String getGuTypeList() {
		return guTypeList;
	}
	public void setGuTypeList(String guTypeList) {
		this.guTypeList = guTypeList;
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
	
	public String getSelectUuserLevelType() {
		return selectUuserLevelType;
	}
	public void setSelectUuserLevelType(String selectUuserLevelType) {
		this.selectUuserLevelType = selectUuserLevelType;
	}
	public String getWeek_select() {
		return week_select;
	}
	public void setWeek_select(String week_select) {
		this.week_select = week_select;
	}
	public String getSelectNeedsType() {
		return selectNeedsType;
	}
	public void setSelectNeedsType(String selectNeedsType) {
		this.selectNeedsType = selectNeedsType;
	}
	public String getSelectCharacterType() {
		return selectCharacterType;
	}
	public void setSelectCharacterType(String selectCharacterType) {
		this.selectCharacterType = selectCharacterType;
	}
	public String getSelectBusinessType() {
		return selectBusinessType;
	}
	public void setSelectBusinessType(String selectBusinessType) {
		this.selectBusinessType = selectBusinessType;
	}
	public String getSelectUsesMediaType() {
		return selectUsesMediaType;
	}
	public void setSelectUsesMediaType(String selectUsesMediaType) {
		this.selectUsesMediaType = selectUsesMediaType;
	}
	public String getNeedsType() {
		return needsType;
	}
	public void setNeedsType(String needsType) {
		if("전체".equals(needsType)){
			needsType = "";
		}
		this.needsType = needsType;
	}
	public String[] getVocRequest() {
		return vocRequest;
	}
	public void setVocRequest(String[] vocRequest) {
		this.vocRequest = vocRequest;
	}
	public String[] getVocService() {
		return vocService;
	}
	public void setVocService(String[] vocService) {
		this.vocService = vocService;
	}
	public String[] getVocHomepage() {
		return vocHomepage;
	}
	public void setVocHomepage(String[] vocHomepage) {
		this.vocHomepage = vocHomepage;
	}

	public String[] getKeywordArr() {
		return keywordArr;
	}
	public void setKeywordArr(String[] keywordArr) {
		this.keywordArr = keywordArr;
		if(keywordArr.length != 0) this.keyword = keywordArr[0];
		else this.keyword = "";
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	public int getRankingIndex() {
		return rankingIndex;
	}
	public void setRankingIndex(int rankingIndex) {
		this.rankingIndex = rankingIndex;
	}
	public String getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}
	public String getContent() {
		return content;
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
	public String[] getInterestKeywordArr() {
		return interestKeywordArr;
	}
	public void setInterestKeywordArr(String[] interestKeywordArr) {
		this.interestKeywordArr = interestKeywordArr;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
*/
	
}
