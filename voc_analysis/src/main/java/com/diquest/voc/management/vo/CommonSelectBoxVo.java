package com.diquest.voc.management.vo;

import java.io.Serializable;

/**  
 * @Class Name : CommonSelectBoxVo.java
 * @Description : CommonSelectBoxVo Class
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
@SuppressWarnings("serial")
public class CommonSelectBoxVo implements Serializable{

	//서울 메트로 카테고리 시작
	private String condition = "";		// 날짜 타입
	private String vocChannel = "";			// 접수채널
	private String vocRecType = "";			// VOC종류
	private String metroDept = "";		// 처리부서
	private String repLevel = "";		// 만족도
	private String vocKind = "";			// 대분류
	private String vocPart = "";			// 중분류
	private String vocItem = "";			// 소분류
	
	private String name;   //카테고리를 넣기위해서 생성	
	private String code;
	
	//서울 메트로 카테고리 관련 코드
	/** COMMON_CODE.CODE_TYPE - 코드 자료형 */
	private String codeType;
	/** COMMON_CODE.P_CAT1_ID - 부모 1차 카테고리 코드 */
	private String pCat1Id;
	/** COMMON_CODE.P_CAT1_NAME - 부모 1차 카테고리 명칭 */
	private String pCat1Nm;
	/** COMMON_CODE.P_CAT2_ID - 부모 2차 카테고리 코드 */
	private String pCat2Id;
	/** COMMON_CODE.P_CAT2_NAME - 부모 2차 카테고리 명칭 */
	private String pCat2Nm;
	//서울 메트로 카테고리 종료
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getMetroDept() {
		return metroDept;
	}

	public void setMetroDept(String metroDept) {
		this.metroDept = metroDept;
	}

	public String getRepLevel() {
		return repLevel;
	}

	public void setRepLevel(String repLevel) {
		this.repLevel = repLevel;
	}

	public String getVocKind() {
		return vocKind;
	}

	public void setVocKind(String vocKind) {
		this.vocKind = vocKind;
	}

	public String getVocPart() {
		return vocPart;
	}

	public void setVocPart(String vocPart) {
		this.vocPart = vocPart;
	}

	public String getVocItem() {
		return vocItem;
	}

	public void setVocItem(String vocItem) {
		this.vocItem = vocItem;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getpCat1Id() {
		return pCat1Id;
	}

	public void setpCat1Id(String pCat1Id) {
		this.pCat1Id = pCat1Id;
	}

	public String getpCat1Nm() {
		return pCat1Nm;
	}

	public void setpCat1Nm(String pCat1Nm) {
		this.pCat1Nm = pCat1Nm;
	}

	public String getpCat2Id() {
		return pCat2Id;
	}

	public void setpCat2Id(String pCat2Id) {
		this.pCat2Id = pCat2Id;
	}

	public String getpCat2Nm() {
		return pCat2Nm;
	}

	public void setpCat2Nm(String pCat2Nm) {
		this.pCat2Nm = pCat2Nm;
	}

	public String getVocChannel() {
		return vocChannel;
	}

	public void setVocChannel(String vocChannel) {
		this.vocChannel = vocChannel;
	}

	public String getVocRecType() {
		return vocRecType;
	}

	public void setVocRecType(String vocRecType) {
		this.vocRecType = vocRecType;
	}
	
}
