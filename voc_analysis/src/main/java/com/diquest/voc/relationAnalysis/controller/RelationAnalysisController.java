package com.diquest.voc.relationAnalysis.controller;

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

import com.diquest.voc.keywordRanking.service.KeywordRankingService;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.relationAnalysis.service.RelationAnalysisService;
import com.diquest.voc.relationAnalysis.vo.RelationAnalysisVo;
import com.google.gson.Gson;

@Controller
public class RelationAnalysisController {
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());

	/** SearchService */
	@Resource(name = "relationAnalysisService")
	private RelationAnalysisService relationAnalysisService;

	@Resource(name = "keywordRankingService")
	private KeywordRankingService keywordRankingService;

	/** AlarmKeywordService */
	@Resource(name = "commonSelectBoxService")
	private CommonSelectBoxService commonSelectBoxService;

	private String portal_id; // 포탈 사용자 ID
	private String portal_nm; // 포탈 사용자 이름

	/**
	 * 연관도분석_연관정보 분석
	 * 
	 * @param model
	 * @return "/relationAnalysis/relationAnalysis";
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/relationAnalysisInit.do", method = { RequestMethod.POST, RequestMethod.GET })
	public String relationInformation(Model model, @ModelAttribute("relationVo") RelationAnalysisVo relationAnalysisVo, HttpServletRequest request) throws Exception {
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		relationAnalysisVo.setLogin_Id(portal_id);
		try {
			// - 관심키워드 조회시 필요한 값 셋팅
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("loginId", portal_id);

			List<String> keywordList = keywordRankingService.getInterestKeyword(paramMap);
			model.addAttribute("interestKeyword", keywordList); // - 관심 키워드
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/relationAnalysis/relationAnalysis";
	}

	/**
	 * 연관도분석_연관도 분석 결과(방사형)
	 * 
	 * @param model
	 * @return "/common/ajax";
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/radarChart.do")
	public String radarChart(Model model, @ModelAttribute("relationVo") RelationAnalysisVo relationAnalysisVo) throws Exception {
		relationAnalysisVo.setLogin_Id(portal_id);
		// - 1. 제외키워드는 키워드만 제외하는것이 아니라, 해당 키워드가 존재하는 문서를 제외한다.
		// 즉, 제외키워드에 따라 결과가 달라진다.
		try {
			HashMap<String, Object> radarResultMap = null;
			String type = "";
			radarResultMap = relationAnalysisService.getRadarGraph(relationAnalysisVo, type);
			if (radarResultMap.size() > 0) {
				Gson gson = new Gson();
				String result = gson.toJson(radarResultMap);
				model.addAttribute("jsonData", result);
				return "/relationAnalysis/d3";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/ajax";
	}

	/**
	 * 연관도분석_연관 키워드 트렌드 차트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/keywordTrend.do")
	public String relationKeywordTrend(Model model, @ModelAttribute("relationVo") RelationAnalysisVo relationAnalysisVo) throws Exception {
		relationAnalysisVo.setLogin_Id(portal_id);
		try {
			String type = "";
			LinkedHashMap<String, Integer> resultMap = relationAnalysisService.getRelationKeywordArr(relationAnalysisVo, type); // 연관키워드(상위 8(10)개 키워드 셋팅)
			if (resultMap != null) {
				List<String> keywordList = new ArrayList<String>(resultMap.keySet());
				relationAnalysisVo.setKeywordArr(keywordList.toArray(new String[keywordList.size()]));
			}
			Gson gson = new Gson();
			String relationKeywordTrend = gson.toJson(relationAnalysisService.getSynthesisReport(relationAnalysisVo)); // 연관 키워드 트렌드
			model.addAttribute("jsonData", relationKeywordTrend);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/ajax";
	}

	/**
	 * 연관도분석_하단 검색 결과(그룹+리스트)
	 * 
	 * @param model
	 * @return "/relationAnalysis/vocSearchResult"
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/vocSearchResult.do")
	public String vocSearchResult(Model model, @ModelAttribute("relationVo") RelationAnalysisVo relationAnalysisVo) throws Exception {
		relationAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", relationAnalysisService.getSearchResult(relationAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/relationAnalysis/vocSearchResult";
	}

	/**
	 * 연관도분석_하단 검색 결과 - Excel다운로드
	 * 
	 * @param model
	 * @return "/relationAnalysis/vocSearchResult"
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/excelVocSearchResult.do")
	public String ExcelVocSearchResult(Model model, @ModelAttribute("relationVo") RelationAnalysisVo relationAnalysisVo) throws Exception {
		relationAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", relationAnalysisService.getExcelResult(relationAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "docExcelView";
	}

	/**
	 * 연관도분석_하단 검색 결과(리스트만)
	 * 
	 * @param model
	 * @return "/relationAnalysis/vocSearchResultList";
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/vocSearchResultList.do")
	public String vocSearchResultList(Model model, @ModelAttribute("relationVo") RelationAnalysisVo relationAnalysisVo) throws Exception {
		relationAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("searchResultList", relationAnalysisService.getSearchResult(relationAnalysisVo)); // VOC 검색결과
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/relationAnalysis/vocSearchResultList";
	}

	/**
	 * 연관도분석_하단 검색 결과 상세페이지
	 * 
	 * @param model
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/detailView.do")
	public String detailView(Model model, @RequestParam(value = "dq_docid") String id) throws Exception { // ,required=false
		try {
			model.addAttribute("detailViewResult", relationAnalysisService.getdetailViewResult(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}

	/**
	 * 연관도분석_하단 검색 결과 유사문서보기
	 * 
	 * @param model
	 * @return "/common/modal_layer"
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/getAlikeSearch.do")
	public String alikeSearch(Model model, @ModelAttribute("relationAnalysisVo") RelationAnalysisVo relationAnalysisVo) throws Exception { // vo를 modal_alike.jsp 에서 쓰진 않음
		relationAnalysisVo.setLogin_Id(portal_id);
		try {
			model.addAttribute("alikeResult", relationAnalysisService.getAlikeSearchResult(relationAnalysisVo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/common/modal_layer";
	}
	
	/**
	 * 연관도분석_처리주무부서 리스트
	 * 
	 * @param model
	 * @return "/common/ajax"
	 * @exception Exception
	 */
	@RequestMapping(value = "/relationAnalysis/repDeptList.do")
	public String getRepDeptList(Model model,  @ModelAttribute("relationAnalysisVo") RelationAnalysisVo relationAnalysisVo) throws Exception {
		Gson gson = new Gson();
		String repDeptList = gson.toJson(relationAnalysisService.getRepDeptList(relationAnalysisVo));
		model.addAttribute("jsonData", repDeptList);
		return "/common/ajax";
	}
}
