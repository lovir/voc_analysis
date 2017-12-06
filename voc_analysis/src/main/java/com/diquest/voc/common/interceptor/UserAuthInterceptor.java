package com.diquest.voc.common.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.CommonService;
import com.diquest.voc.management.service.InterestKeywordService;
import com.diquest.voc.management.service.UserManagementService;
import com.diquest.voc.management.vo.InterestKeywordVo;
import com.diquest.voc.management.vo.UserManagementVo;

public class UserAuthInterceptor extends HandlerInterceptorAdapter{
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/** CommonService */
	@Resource(name = "commonService")
	private CommonService commonService;
	
	/** UserManagementService */
	@Resource(name = "userManagementService")
	private UserManagementService userManagementService;
	
	/** UserManagementService */
	@Resource(name = "interestService")
	private InterestKeywordService interestService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
		String reqUrl = request.getRequestURI().toString();
		
		//서울메트로는 포탈에서 SSO 가 없기 떄문에 별도 로그인 처리 없이, 사용자ID, 이름 값을 iFrame 출력 시에 전달 받아 처리한다.
		String portal_id = request.getParameter("portal_id") == null ? "" : request.getParameter("portal_id"); // 포탈ID
		String portal_nm = request.getParameter("portal_nm") == null ? "" : request.getParameter("portal_nm"); // 포탈 사용자명
		Map<String,String> userInfo = new HashMap<String,String>();
		userInfo.put("portal_id", portal_id);
		userInfo.put("portal_nm", portal_nm);
		if(portal_id != null && !"".equals(portal_id)){
			String login = null;
			try {
				log.debug("[VOC Analysis(Login) Portal ID Checker] Portal ID :" + portal_id);
				log.debug("[VOC Analysis(Login) Portal ID Checker] Portal Name :" + portal_nm);
				login = commonService.selectLogin_Metro(portal_id);
				
				//서울메트로 VOC에 최초 접속한 포탈 사용자에 대한 기본 정보를 저장한다.
				if(login == null) {
					int result = 0;
					try {
						UserManagementVo userVo = new UserManagementVo();
						userVo.setRegId(portal_id);
						userVo.setName(portal_nm);
						result = userManagementService.insertUserManagement(userVo);
						/*if(result > 0){	//최초 로그인 포털 이용자의 관심키워드를 디폴트 키워드로 저장한다.
							InterestKeywordVo interestVo = new InterestKeywordVo();
							interestVo.setEtc("");
							interestVo.setOrgNm("");
							interestVo.setUseYn("Y");
							interestVo.setRegId(portal_id);
							interestVo.setRegNm(portal_nm);
							for(String addKeyword : Globals.DEFAULT_INTEREST_KEYWORD){
								interestVo.setKeyword(addKeyword);
								interestService.insertInterestKeyword(interestVo);	
							}
						}*/
					} catch (Exception e) {
						log.error("Exception : " + e);
						throw e;
					}
				}
			} catch (Exception e) {
				log.error("Exception : " + e);
			}
			
		}
		else{
			log.debug("[VOC Analysis(Login) Portal ID Checker] : Login Fail ");
			return false;
		}
		//사용자 ID, 이름 전달 받아 DB에서 기존에 로그인 한 적이 있는 조회 기능 필요.
				//기존에 로그인 한 적이 없다면 deafult 키워드를 DB에 저장.
				
				/*
				// 포털대시보드는 로그인 로직을 타지 않음
				if(!reqUrl.contains("/dashBoard/portal")){
					//System.out.println("########## Login : "+request.getSession().getAttribute("login"));
					if(request.getSession().getAttribute("login") == null){
						try {
							if(request.getHeader("AJAX") != null && request.getHeader("AJAX").equals(Boolean.TRUE.toString())){
								response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
							}
							else{
								response.sendRedirect(request.getContextPath() + "/common/login.do?userID=test&password=test");
//								response.sendRedirect(request.getContextPath() + "/common/loginInit.do");
								
								//response.sendRedirect(request.getContextPath() + "/common/ssoLogin.do?guid=fda468aa-c087-41a3-b9e0-a227afe5d85d&uid=UzBJeE16STQ=");
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						return false;
					}
				}
				*/
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
		super.afterCompletion(request, response, handler, ex);
	}
	
}
