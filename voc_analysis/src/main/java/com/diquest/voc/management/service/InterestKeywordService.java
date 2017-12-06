package com.diquest.voc.management.service;

import java.util.HashMap;
import java.util.List;

import com.diquest.voc.management.vo.InterestKeywordVo;

import egovframework.rte.psl.dataaccess.util.EgovMap;

/**  
 * @Class Name : InterestKeywordService.java
 * @Description : InterestKeywordService Class
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


public interface InterestKeywordService {

	/**
	 * 관심키워드 를 등록한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 등록 결과
	 * @exception Exception
	 */
	public int insertInterestKeyword(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 관심키워드 를 수정한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	public int updateInterestKeyword(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 관심키워드 를 삭제한다.
	 * @param vo - 삭제 대상정보가 담긴 InterestKeywordVo
	 * @return 삭제 결과
	 * @exception Exception
	 */
	public int deleteInterestKeyword(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 관심키워드 를 조회한다.
	 * @param vo - 조회 할 정보가 담긴 InterestKeywordVo
	 * @return 조회한 글(상세)
	 * @exception Exception
	 */
	public InterestKeywordVo selectInterestKeyword(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 관심키워드 관리 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 InterestKeywordVo
	 * @return 글 목록
	 * @exception Exception
	 */
	public HashMap<String, Object> selectInterestKeywordList(InterestKeywordVo interestVo) throws Exception;

	/**
	 * 대시보드 활성/비활성을 반영한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	public int updateInterestKeywordDashYn(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 키워드 활성/비활성을 반영한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	public int updateInterestKeywordUseYn(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 관심키워드 관리 목록을 조회한다.(차트 표시용) 
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<EgovMap> selectInterestKeywordListTop10(InterestKeywordVo interestVo) throws Exception;
	
	/**
	 * 관심키워드 관리 목록을 조회한다.(대시보드차트 표시용) 
	 * @return 글 목록
	 * @exception Exception
	 */
	public List<EgovMap> selectInterestKeywordListDashYn(InterestKeywordVo interestVo) throws Exception;
	
}
