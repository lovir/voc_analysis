package com.diquest.voc.management.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.management.service.InterestKeywordService;
import com.diquest.voc.management.vo.InterestKeywordVo;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("interestService")
public class InterestKeywordServiceImpl implements InterestKeywordService{

	/** InterestKeywordDAO */
	@Resource(name="interestDAO")
	private InterestKeywordDAO interestDAO;

	/**
	 * 관심키워드 를 등록한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 등록 결과
	 * @exception Exception
	*/
	@Override
	public int insertInterestKeyword(InterestKeywordVo interestVo)
			throws Exception {
		
		int cnt = 0;
		// 활성 가능한 관심키워드 갯수를 조회
		int Totcnt = interestDAO.selectInterestKeywordListTotCnt(interestVo);
		
		// MAX 10개
		if(Totcnt<10 || (Totcnt==10 && interestVo.getUseYn().equals(Globals.COM_USE_YN_N))){
			cnt = interestDAO.insertInterestKeyword(interestVo);
		}
		return cnt;
	}
	
	/**
	 * 관심키워드 를 수정한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	@Override
	public int updateInterestKeyword(InterestKeywordVo interestVo)
			throws Exception {
		
		int cnt = 0;
		// 활성 가능한 관심키워드 갯수를 조회
		int Totcnt = interestDAO.selectInterestKeywordListTotCnt(interestVo);
		
		// MAX 10개
		if(Totcnt<10 || (Totcnt==10 && interestVo.getUseYn().equals(Globals.COM_USE_YN_N))){
			cnt = interestDAO.updateInterestKeyword(interestVo);
		}
		return cnt;
	}
	
	/**
	 * 관심키워드 를 삭제한다.
	 * @param vo - 삭제 대상정보가 담긴 InterestKeywordVo
	 * @return 삭제 결과
	 * @exception Exception
	 */
	@Override
	public int deleteInterestKeyword(InterestKeywordVo interestVo)
			throws Exception {
		return interestDAO.deleteInterestKeyword(interestVo);
	}
	
	/**
	 * 관심키워드 를 조회한다.
	 * @param vo - 조회 할 정보가 담긴 InterestKeywordVo
	 * @return 조회한 글(상세)
	 * @exception Exception
	 */
	@Override
	public InterestKeywordVo selectInterestKeyword(InterestKeywordVo interestVo)
			throws Exception {
		return (InterestKeywordVo) interestDAO.selectInterestKeyword(interestVo);
	}
	
	/**
	 * 관심키워드 관리 목록을 조회한다.
	 * @param vo - 조회 할 정보가 담긴 InterestKeywordVo
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public HashMap<String, Object> selectInterestKeywordList(InterestKeywordVo interestVo)
			throws Exception {

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", interestDAO.selectInterestKeywordList(interestVo));
		map.put("totSize", interestDAO.selectInterestKeywordListTotCnt(interestVo));

		return map; 
	}
	
	/**
	 * 대시보드 활성/비활성을 반영한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	@Override
	public int updateInterestKeywordDashYn(InterestKeywordVo interestVo)
			throws Exception {
		
		int cnt = 0;
		// 대시보드 활성 가능한 관심키워드 갯수를 조회
		int Totcnt = interestDAO.selectInterestKeywordListDashYnTotCnt(interestVo);
		
		// MAX 10개
		if(Totcnt<10 || (Totcnt==10 && interestVo.getDashYn().equals(Globals.COM_USE_YN_N))){
			cnt = interestDAO.updateInterestKeywordDashYn(interestVo);
		}
		
		return cnt;
	}
	
	/**
	 * 키워드 활성/비활성을 반영한다.
	 * @param vo - 등록 할 정보가 담긴 InterestKeywordVo
	 * @return 수정 결과
	 * @exception Exception
	 */
	@Override
	public int updateInterestKeywordUseYn(InterestKeywordVo interestVo)
			throws Exception {
		
		int cnt = 0;
		// 대시보드 활성 가능한 관심키워드 갯수를 조회
		int Totcnt = interestDAO.selectInterestKeywordListUseYnTotCnt(interestVo);
		
		// MAX 10개
		if(Totcnt<10 || (Totcnt==10 && interestVo.getUseYn().equals(Globals.COM_USE_YN_N))){
			cnt = interestDAO.updateInterestKeywordUseYn(interestVo);
		}
		
		return cnt;
	}
	
	/**
	 * 관심키워드 관리 목록을 조회한다.(차트 표시용) 
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<EgovMap> selectInterestKeywordListTop10(InterestKeywordVo interestVo)
			throws Exception {
		return interestDAO.selectInterestKeywordListTop10(interestVo);
	}
	
	/**
	 * 관심키워드 관리 목록을 조회한다.(대시보드차트 표시용) 
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<EgovMap> selectInterestKeywordListDashYn(InterestKeywordVo interestVo)
			throws Exception {
		return interestDAO.selectInterestKeywordListDashYn(interestVo);
	}
}
