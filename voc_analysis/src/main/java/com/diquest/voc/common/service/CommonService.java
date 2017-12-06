package com.diquest.voc.common.service;

import java.util.HashMap;
import java.util.List;

import com.diquest.voc.common.vo.CommonCodeVo;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface CommonService {

	/**
	 * AD로그인 인증
	 * @param userID - 아이디
	 * @param userPass - 패스워드
	 * @return 인증결과 (200 이외에 에러)
	 * @exception Exception
	 */
	public String adLogin(String userID, String userPass) throws Exception;
	
	/**
	 * SSO로그인 인증
	 * @param uId - 아이디
	 * @param guId - 패스워드
	 * @return 인증결과
	 * @exception Exception
	 */
	public String ssoLogin(String uId, String guId, String sId);
	
	/**
	 * 로그인 페이지를 통한 로그인 사용자정보
	 * @param userID - 아이디
	 * @param userPass - 패스워드
	 * @return 사용자정보
	 * @exception Exception
	 */
	public EgovMap selectLogin(String userID) throws Exception;
	
	/**
	 * SSO로그인시 아이디 사용자정보
	 * @param userID - 아이디
	 * @return 사용자정보
	 * @exception Exception
	 */
	public EgovMap selectIdLogin(String userID) throws Exception;

	/**
	 * base64 로그인 체커
	 * @param userID - 아이디
	 * @param passWord - 패스워드
	 * @return 승인 여부
	 * @exception Exception
	 */
	public String base64LoginCheker(String userID, String passWord) throws Exception;
	
	/**
	 * 서울메트로 포탈 사용자 정보
	 * @param portal_id - 포탈 아이디
	 * @param portal_nm - 포탈 사용자명
	 * @return 사용자명
	 * @exception Exception
	 */
	public String selectLogin_Metro(String portal_id) throws Exception;
	
	/**
	 * 공통 코드 조회
	 * 
	 * @param kind
	 *            - 코드 종류
	 * @return 코드 리스트
	 * @throws Exception
	 */
	public List<CommonCodeVo> getVocCommonCode(HashMap<String, Object> paramMap) throws Exception;
}
