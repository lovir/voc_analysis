package com.diquest.voc.management.vo;

import java.io.Serializable;

/**  
 * @Class Name : ThesaurusDictionaryVo.java
 * @Description : ThesaurusDictionaryVo Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.06.10           최초생성
 * 
 * @author 박소영
 * @since 2014. 06.10
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */
public class ThesaurusDictionaryVo implements Serializable{

	/** ThesaurusDictionaryVo serialVersionUID */
	private static final long serialVersionUID = 6919350465511530103L;

	/** 등록일 */
	private String regDate;
	/** 적용여부 */
	private boolean apply;
	/** 유의어수 */
	private String thesaurusCount;
	/** 색인어휘 추출 결과 */
	private String extractor;
	/** 형태소 분석결과(유의어) */
	private String morpheme;
	/** 형태소 분석결과(키워드) */
	private String chKeyword;
	/** 형태소 분석결과(키워드) 일치 여부 */
	private int chFlag;
	/** 형태소 분석결과(추가 키워드) */
	private String chAddKeyword;
	/** 형태소 분석결과(추가 키워드) 일치 여부 */
	private int chAddFlag;
	/** 유의어 리스트  */
	private String[] thesaurusKeywordList;
	/** 단방향 양방향  */
	private int way;
	/** 표시건수 */
	private int pageSize;
	/** 현재페이지  */
	private int currentPageNo;
	/** 선택된 키워드  */
	private String[] selectedKeyword;
	/** 정렬  */
	private String sort;
	/** 키워드 */
	private String keyword;
	/** 등록자 */
	private String regId;
	/** 등록자 */
	private String regNm;
	
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public boolean getApply() {
		return apply;
	}
	public void setApply(boolean apply) {
		this.apply = apply;
	}
	public String getChKeyword() {
		return chKeyword;
	}
	public void setChKeyword(String chKeyword) {
		this.chKeyword = chKeyword;
	}
	public int getChFlag() {
		return chFlag;
	}
	public void setChFlag(int chFlag) {
		this.chFlag = chFlag;
	}
	public String getChAddKeyword() {
		return chAddKeyword;
	}
	public void setChAddKeyword(String chAddKeyword) {
		this.chAddKeyword = chAddKeyword;
	}
	public int getChAddFlag() {
		return chAddFlag;
	}
	public void setChAddFlag(int chAddFlag) {
		this.chAddFlag = chAddFlag;
	}
	public String[] getThesaurusKeywordList() {
		return thesaurusKeywordList;
	}
	public void setThesaurusKeywordList(String[] thesaurusKeywordList) {
		this.thesaurusKeywordList = thesaurusKeywordList;
	}
	public int getWay() {
		return way;
	}
	public void setWay(int way) {
		this.way = way;
	}
	public String getThesaurusCount() {
		return thesaurusCount;
	}
	public void setThesaurusCount(String thesaurusCount) {
		this.thesaurusCount = thesaurusCount;
	}
	public String getExtractor() {
		return extractor;
	}
	public void setExtractor(String extractor) {
		this.extractor = extractor;
	}
	public String getMorpheme() {
		return morpheme;
	}
	public void setMorpheme(String morpheme) {
		this.morpheme = morpheme;
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
	public String[] getSelectedKeyword() {
		return selectedKeyword;
	}
	public void setSelectedKeyword(String[] selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	
}
