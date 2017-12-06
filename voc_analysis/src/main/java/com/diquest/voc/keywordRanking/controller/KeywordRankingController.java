package com.diquest.voc.keywordRanking.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.keywordRanking.vo.KeywordRankingVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.google.gson.Gson;

@Controller
public class KeywordRankingController {
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/** SearchService */
	@Resource(name = "keywordRankingService")
	private KeywordRankingService keywordRankingService;

	/** AlarmKeywordService */
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;

	/**
	 * 키워드랭킹분석_종합랭킹
	 * 
	 * @param model
	 * @return "/keywordRanking/synthesisRanking"
	 * @exception Exception
	 */
	private String portal_id;
	private String portal_nm;
	@RequestMapping(value = "/keywordRanking/synthesisRankingInit.do", method = {RequestMethod.POST, RequestMethod.GET})
	public String synthesisRanking(Model model, HttpServletRequest request) throws Exception {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		
		return "/keywordRanking/synthesisRanking";
	}

	/**
	 * 키워드랭킹분석_관심 키워드 랭킹
	 * 
	 * @param model
	 * @return "/keywordRanking/interestRanking";
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/interestRankingInit.do")
	public String interestRanking(Model model, HttpServletRequest request) throws Exception {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		return "/keywordRanking/interestRanking";
	}

	/**
	 * 키워드랭킹분석_리포트차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/reportChart.do")
	public String reportChart(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			if (keywordRankingVo.getPageType().equals("synthesis")) { // 종합 랭킹 페이지에서 호출시

				String[] keywordArr = keywordRankingService.getsynthesisTotalKeywordArr(keywordRankingVo); // 상위 10개 키워드 셋팅

				if (keywordArr != null) {

					keywordRankingVo.setKeywordArr(keywordArr);
				}
			} 
			else { // 관심키워드 랭킹 페이지에서 호출시
				// 관심키워드 조회시 필요한 값 셋팅
				HashMap<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("loginId", portal_id);
				List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출

				if (keywordList.size() > 0) {
					keywordRankingVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심 키워드 설정
				}
			}
			Gson gson = new Gson();
			String report = gson.toJson(keywordRankingService.getSynthesisReport(keywordRankingVo)); // 리포트 정보
			model.addAttribute("jsonData", report);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/ajax";
	}

	/**
	 * 키워드랭킹분석_워드 클라우드 차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/wordCloudChart.do")
	public String wordCloudChart(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			Gson gson = new Gson();
			String wordList = gson.toJson(keywordRankingService.getWordCloudChart(keywordRankingVo)); // 리포트 정보
			model.addAttribute("jsonData", wordList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		keywordRankingService.getWordCloudChart(keywordRankingVo);
		return "/common/ajax";
	}

	/**
	 * 키워드랭킹분석_종합랭킹분석
	 * 
	 * @param model
	 * @return "/keywordRanking/synthesisRanking_total"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/getTotalRanking.do")
	public String totalRanking(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			// 관심키워드 조회시 필요한 값 셋팅
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("loginId", portal_id); 

			LinkedHashMap<String, Object> synthesisRanking = new LinkedHashMap<String, Object>();
			if ("interest".equals(keywordRankingVo.getPageType())) {	//관심키워드 분석 인 경우

				List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap);
				if (keywordList.size() > 0) {
					keywordRankingVo.setInterestKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심키워드 셋팅
				}

				if (keywordRankingVo.getInterestKeywordArr() != null) {
					// 관심키워드 랭킹일 경우
					synthesisRanking = keywordRankingService.getInterestRanking(keywordRankingVo);
				}
				model.addAttribute("pageType", "Y");
			} 
			else {
				List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap);
				if (keywordList.size() > 0) {
					keywordRankingVo.setInterestKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심키워드 셋팅
				}
				// 종합랭킹일경우
				synthesisRanking = keywordRankingService.getSynthesisRanking(keywordRankingVo);
				model.addAttribute("pageType", "N");
			}

			// 첫 번째 존재하는 키워드 값 설정 - 하단부에 첫번째 검색 결과시 사용하기 위함
			String keyword = null;
			if (synthesisRanking.get("periodKeyword") != null) {
				LinkedHashMap<String, Object> totalPeriodKeyword = (LinkedHashMap<String, Object>) synthesisRanking.get("periodKeyword");
				for (String key : totalPeriodKeyword.keySet()) {
					if (totalPeriodKeyword.get(key) != null) {
						ArrayList<HashMap<String, Object>> periodRankingList = (ArrayList<HashMap<String, Object>>) totalPeriodKeyword.get(key);
						if (periodRankingList.size() > 0) {
							if (periodRankingList.get(0).get("keyword") != null) {
								keyword = (String) periodRankingList.get(0).get("keyword");
								if (keyword != null) {
									break;
								}
							}
						}
					}
				}
			}
			model.addAttribute("keyword", keyword);
			model.addAttribute("synthesisRanking", synthesisRanking);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/keywordRanking/synthesisRanking_total";
	}

	/**
	 * 키워드랭킹분석 공통_하단 검색 결과(그룹+리스트)
	 * 
	 * @param model
	 * @return "/keywordRanking/vocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/vocSearchResult.do")
	public String vocSearchResult(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			if ("interest".equals(keywordRankingVo.getPageType())) {
				// 관심키워드 조회시 필요한 값 셋팅
				HashMap<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("loginId", portal_id);
				List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출

				if (keywordList.size() > 0) {
					keywordRankingVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심 키워드 설정
				}
			}
			model.addAttribute("searchResultList", keywordRankingService.getSearchResult(keywordRankingVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/keywordRanking/vocSearchResult";
	}

/*	*//**
	 * 대쉬보드에서 이동한 관심 키워드랭킹분석 하단 검색 결과 (그룹+리스트)
	 * 
	 * @param model
	 * @return "/keywordRanking/vocSearchResult";
	 * @exception Exception
	 *//*
	@RequestMapping(value = "/keywordRanking/dashBoardSearchResult.do")
	public String dashBoardSearchResult(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			if (keywordRankingVo.getKeyword().equals("")) {
				if ("interest".equals(keywordRankingVo.getPageType())) {
					// 관심키워드 조회시 필요한 값 셋팅
					HashMap<String, String> paramMap = new HashMap<String, String>();
					paramMap.put("loginId", portal_id);
					List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출

					if (keywordList.size() > 0) {
						keywordRankingVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심 키워드 설정
					}
				}
			}
			model.addAttribute("searchResultList", keywordRankingService.getSearchResult(keywordRankingVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/keywordRanking/vocSearchResult";
	}
*/
	/**
	 * 키워드랭킹분석_하단 검색 결과(리스트만)
	 * 
	 * @param model
	 * @return "/keywordRanking/vocSearchResultList";
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/vocSearchResultList.do")
	public String vocSearchResultList(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			model.addAttribute("searchResultList", keywordRankingService.getSearchResult(keywordRankingVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/keywordRanking/vocSearchResultList";
	}

	/**
	 * 키워드랭킹분석 검색결과 Excel-다운로드
	 * 
	 * @param model
	 * @return "/keywordRanking/excelVocSearchResult";
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/excelVocSearchResult.do")
	public String excelVocSearchResult(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {
			model.addAttribute("searchResultList", keywordRankingService.getExcelResult(keywordRankingVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}

	/**
	 * 검색결과 선택한 문서 상세페이지
	 * 
	 * @param id
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/detailView.do")
	public String detailView(Model model, @RequestParam(value = "dq_docid") String id) throws Exception { 
		try {
			model.addAttribute("detailViewResult", keywordRankingService.getdetailViewResult(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}

	/**
	 * 검색결과 선택한 문서의 유사문서
	 * 
	 * @param keywordRankingVo
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/getAlikeSearch.do")
	public String alikeSearch(Model model, @ModelAttribute("searchVo") KeywordRankingVo keywordRankingVo) throws Exception {
		keywordRankingVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("alikeResult", keywordRankingService.getAlikeSearchResult(keywordRankingVo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}

	/**
	 * 키워드랭킹분석_이슈키워드랭킹_화면
	 * 
	 * @param model
	 * @return "/keywordRanking/issueKeywordRanking"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/issueKeywordRankingInit.do")
	public String issueKeywordRanking(Model model, HttpServletRequest request) throws Exception {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		return "/keywordRanking/issueKeywordRanking";
	}

	/**
	 * 이슈키워드_랭킹분석
	 * 
	 * @param model
	 * @return "/keywordRanking/issueRanking"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/issueRanking.do")
	public String issueRanking(Model model, @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		/*
		 * 1. 이슈키워드는 전주(전월,전년) 의 키워드 값이 없으면, 이슈 통계를 낼 수가 없다. 
		 * 2. 이슈키워드는 전주(전월,전년) 의 키워드 값이 있으나, 금주의 값과 일치하는 키워드가 없으면 통계를 낼 수가 없다. 
		 * 3. 이슈키워드는 전주(전월,전년) 의 키워드 값이 있고, 금주의 키워드에도 존재하는 키워드나, 만약 빈도수가 똑같으면 통계에 반영이 되지 않는다.
		 */
		keywordRankingVo.setLogin_Id(portal_id);
		
		try {

			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("loginId", portal_id); 
			List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap);
			if (keywordList.size() > 0) {
				keywordRankingVo.setInterestKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심키워드
																												// 셋팅
			}
			LinkedHashMap<String, Object> issueRanking = new LinkedHashMap<String, Object>();
			// 이슈키워드랭킹
			issueRanking = keywordRankingService.getIssueRanking(keywordRankingVo);
			if (issueRanking.get("periodKeyword") != null) {
				model.addAttribute("pageType", "N");
				model.addAttribute("issueRanking", issueRanking);
				model.addAttribute("keyword", issueRanking.get("searchKeyword"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/keywordRanking/issueRanking";
	}

	/**
	 * 키워드랭킹분석_처리주무부서 리스트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/keywordRanking/repDeptList.do")
	public String getRepDeptList(Model model,  @ModelAttribute("keywordRankingVo") KeywordRankingVo keywordRankingVo) throws Exception {
		Gson gson = new Gson();
		if ("interest".equals(keywordRankingVo.getPageType())) {
			// 관심키워드 조회시 필요한 값 셋팅
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("loginId", portal_id);
			List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap); // 키워드 DB에서 추출

			if (keywordList.size() > 0) {
				keywordRankingVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()])); // 관심 키워드 설정
			}
		}
		String repDeptList = gson.toJson(keywordRankingService.getRepDeptList(keywordRankingVo));
		model.addAttribute("jsonData", repDeptList);
		return "/common/ajax";
	}

}
