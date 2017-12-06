package com.diquest.voc.management.vo;

import java.io.Serializable;
import java.util.List;

/**  
 * @Class Name : MailReceiverVo.java
 * @Description : MailReceiverVo Class
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
public class MailReceiverVo implements Serializable{

	/** MailReceiverVo의 serialVersionUID */
	private static final long serialVersionUID = -6708615262597572638L;
	
	/** No. */
	private String no;
	/** 조직명 */
	private String org = "";
	/** 이름 */
	private String name;
	/** 이메일 */
	private String email;
	/** 등록자 */
	private String regId;
	/** 등록일 */
	private String regDate;
	/** 등록여부 */
	private String regYn;
	/** 표시건수 */
	private int pageSize;
	/** 현재페이지  */
	private int currentPageNo;
	/** 선택된No  */
	private int[] selectedNo;
	/** 검색조건  */
	private String condition;
	/** 키워드 */
	private String keyword;
	/** 휴대폰번호 */
	private String phone;
	/** 사용자관리에서 삭제했는지 삭제여부 1:삭제/ 2:미삭제*/
	private String userDeleteFlag;
	/** 등록자 배열  */
	private List<String> regIdArr;
	
	public String getUserDeleteFlag() {
		return userDeleteFlag;
	}
	public void setUserDeleteFlag(String userDeleteFlag) {
		this.userDeleteFlag = userDeleteFlag;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getRegYn() {
		return regYn;
	}
	public void setRegYn(String regYn) {
		this.regYn = regYn;
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
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<String> getRegIdArr() {
		return regIdArr;
	}
	public void setRegIdArr(List<String> regIdArr) {
		this.regIdArr = regIdArr;
	}
}
