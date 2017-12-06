package com.diquest.voc.management.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.diquest.voc.management.service.AlarmKeywordService;
import com.diquest.voc.management.vo.AlarmKeywordMonitoringVo;
import com.diquest.voc.management.vo.AlarmKeywordVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**  
 * @Class Name : AlarmKeywordController.java
 * @Description : AlarmKeywordController Class
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

@Controller
public class AlarmKeywordController {
	
	/** AlarmKeywordService */
	@Resource(name = "alarmKeywordService")
	private AlarmKeywordService alarmKeywordService;
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	private static String portal_id;	//메트로 포탈 사용자ID
	private static String portal_nm;	//메트로 포탈 사용자 이름
	
	/**
	 * 알람 설정(대상키워드 관리) 초기화면 표시
	 * @param model
	 * @return "/management/alarmSetting.do"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/alarmKeywordInit.do")
	public String init(Model model, HttpServletRequest request) {
		/*Map<String, String> map = (Map<String, String>)request.getSession().getAttribute("login");
		AlarmKeywordVo alarmKeywordVo = new AlarmKeywordVo();
		alarmKeywordVo.setRegId(map.get("userId"));
		alarmKeywordVo.setRegNm(map.get("userNm"));
		alarmKeywordVo.setOrgNm(map.get("depNm"));*/
		
		//서울메트로는 VOC에서 별도로 로그인 관리를 하지 않음.
		//태블로 포탈 페이지에서 iFrame으로 사용자 ID와 이름만 넘겨받아 연동.
		portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
				
		AlarmKeywordVo alarmKeywordVo = new AlarmKeywordVo();
		alarmKeywordVo.setRegId(portal_id);
		alarmKeywordVo.setRegNm(portal_nm);
		model.addAttribute("alarmKeywordVo", alarmKeywordVo);
		model.addAttribute("portal_id", portal_id);
		model.addAttribute("portal_nm", portal_nm);
		return "/management/alarmKeyword";
	}

	/**
	 * 알람 설정(대상키워드 관리) 신규등록
	 * @param alarmVo
	 * @return 수정결과
	 * @exception Exception
	 */
	@RequestMapping(value="/management/addAlarmKeyword.do", method=RequestMethod.POST)
	@ResponseBody
	public int addAlarmKeyword(@RequestBody AlarmKeywordVo alarmVo) throws Exception {
		alarmVo.setRegId(portal_id);
		alarmVo.setRegNm(portal_nm);
		int result = 100;
		try {
			if("Y".equals(alarmVo.getUseYn())){
				if(alarmKeywordService.checkAlarmRegYnCount(alarmVo) > 9){
					return result;
				}
			}
			result = alarmKeywordService.insertAlarmKeyword(alarmVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 알람 설정(대상키워드 관리) 수정
	 * @param alarmVo
	 * @return 등록결과
	 * @exception Exception
	 */
	@RequestMapping(value="/management/updateAlarmKeyword.do", method=RequestMethod.POST)
	@ResponseBody
	public int updateAlarmKeyword(@RequestBody AlarmKeywordVo alarmVo) throws Exception {
		alarmVo.setRegId(portal_id);
		alarmVo.setRegNm(portal_nm);
		int result = 100;
		try {
			if("Y".equals(alarmVo.getUseYn())){
				if(alarmKeywordService.checkAlarmRegYnCount(alarmVo) > 9){
					if("N".equals(alarmVo.getUseYnTemp())){//10번째 키워드는 수정이 되어야 한다. (Y일 경우 수정 가능)
						return result;
					}
				}
			}
			result = alarmKeywordService.updateAlarmKeyword(alarmVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 알람 설정(대상키워드 관리) 삭제
	 * @param alarmVo
	 * @return 삭제결과
	 * @exception Exception
	 */
	@RequestMapping(value="/management/deleteAlarmKeyword.do", method=RequestMethod.POST)
	@ResponseBody
	public int deleteAlarmKeyword(@RequestBody AlarmKeywordVo alarmVo) throws Exception {
		alarmVo.setRegId(portal_id);
		alarmVo.setRegNm(portal_nm);
		int result = 0;
		try {
			result = alarmKeywordService.deleteAlarmKeyword(alarmVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 알람 설정(대상키워드 관리) 상세
	 * @param alarmVo
	 * @return 상세
	 * @exception Exception
	 */
	@RequestMapping(value="/management/selectAlarmKeyword.do", method=RequestMethod.POST)
	@ResponseBody
	public AlarmKeywordVo selectAlarmKeyword(@RequestBody AlarmKeywordVo alarmVo) throws Exception {
		alarmVo.setRegId(portal_id);
		alarmVo.setRegNm(portal_nm);
		AlarmKeywordVo result = null;
		try {
			result = alarmKeywordService.selectAlarmKeyword(alarmVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 알람 설정(대상키워드 관리) 리스트
	 * 
	 * @param alarmVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value="/management/selectAlarmKeywordList.do", method=RequestMethod.POST)
	@ResponseBody
	public HashMap<String, Object> selectAlarmKeywordList(@RequestBody AlarmKeywordVo alarmVo) throws Exception {
		alarmVo.setRegId(portal_id);
		alarmVo.setRegNm(portal_nm);
		HashMap<String, Object> result = null;
		try {
			result = alarmKeywordService.selectAlarmKeywordList(alarmVo);
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return result;
	}

	/**
	 * 알람 키워드 모니터링 리스트
	 * @param monitoringVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value="/management/selectAlarmKeywordMonitoringList.do", method=RequestMethod.POST)
	public String selectAlarmKeywordMonitoringList(Model model, @ModelAttribute("monitoringVo") AlarmKeywordMonitoringVo vo) throws Exception {
		try {
			model.addAttribute("alarmViewResult", alarmKeywordService.selectAlarmKeywordMonitoringList(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/modal_layer_chart";
	}
	
	/**
	 * 사용자별 활성화 된 알람 키워드 갯수 조회 - 알람키워드 모니터링 조회 전 사용
	 * @param monitoringVo
	 * @return 리스트
	 * @exception Exception
	 */
	@RequestMapping(value="/management/checkAlarmRegYnCount.do", method=RequestMethod.POST)
	public String checkAlarmRegYnCount(Model model, @ModelAttribute("monitoringVo") AlarmKeywordMonitoringVo vo) throws Exception {
		AlarmKeywordVo alarmVo = new AlarmKeywordVo();
		alarmVo.setRegId(vo.getRegId());
		alarmVo.setRegNm(vo.getRegNm());
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			String alarmKeywordSize = gson.toJson(alarmKeywordService.checkAlarmRegYnCount(alarmVo));
			HashMap<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("alarmKeywordSize", alarmKeywordSize);
			model.addAttribute("jsonData", gson.toJson(jsonMap));
		} catch (Exception e) {
			log.error("Exception : " + e);
			throw e;
		}
		return "/common/ajax";
	}
}
