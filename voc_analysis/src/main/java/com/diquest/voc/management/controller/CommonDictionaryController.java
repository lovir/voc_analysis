package com.diquest.voc.management.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.diquest.ir.client.dic.checker.DictionaryCheckResult;
import com.diquest.ir.client.dic.checker.DictionaryChecker;
import com.diquest.ir.client.dic.checker.StopwordChecker;
import com.diquest.ir.client.dic.checker.ThesaurusChecker;
import com.diquest.ir.client.dic.checker.UserCnDicChecker;
import com.diquest.ir.client.dic.checker.UserDicChecker;
import com.diquest.ir.client.util.FileUploadRequestHandler;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.management.service.CommonDictionaryService;
import com.diquest.voc.management.vo.DictionaryVo;

/**  
 * @Class Name : CommonDictionaryController.java
 * @Description : CommonDictionaryController Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2015.08.31           최초생성
 * 
 * @author 박소영
 * @since 2015. 08.31
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */

@Controller
public class CommonDictionaryController {

	/** CommonDictionaryService */
	@Resource(name = "commonDictionaryService")
	private CommonDictionaryService commonDictionaryService;
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 사전 내려받기 초기화면 표시
	 * @param model
	 * @return "/management/downloadDictionaryInit"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/downloadDictionaryInit.do")
	public String downloadDictionaryInit(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo) {
		model.addAttribute("dicType", vo.getDicType());
		switch (vo.getDicType()) {
		case 2:
			model.addAttribute("dicTitle", Globals.COM_FILE_STOPWORD);
			break;
		case 3:
			model.addAttribute("dicTitle", Globals.COM_FILE_USERDIC);
			break;
		case 7:
			model.addAttribute("dicTitle", Globals.COM_FILE_USERCNDIC);
			break;
		case 8:
			model.addAttribute("dicTitle", Globals.COM_FILE_QUASISYNONYM);
			break;
		default :
			break;
		}
		
		return "/common/downloadDic";
	}
	
	/**
	 * 다운로드 가능한 사전파일 확인
	 * @param model
	 * @return "/management/downloadDictionaryInit"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/downloadDictionary.do")
	public String downloadDictionary(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo, HttpServletRequest request) throws Exception {
		try {
			model.addAttribute("downloadResult", commonDictionaryService.downloadDictionary(vo));
		} catch (Exception e) {
			log.error("Exception : " + e);
			log.error("detail Message : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		String dicTitle = request.getParameter("dicTitle") == null ? "" : request.getParameter("dicTitle");
		String dicType = request.getParameter("dicType") == null ? "" : request.getParameter("dicType");
		model.addAttribute("dicTitle", dicTitle);
		model.addAttribute("dicType", dicType);
		return "/common/downloadDicProc";
	}
	
	/**
	 * 사전 파일 내려받기
	 * @param model
	 * @return "/management/downloadFile"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/downloadFile.do")
	public void downloadFile(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo, HttpServletResponse response) throws Exception {
		try {
			File uFile = new File(vo.getFilepath());
			int fSize = (int) uFile.length();
			
			if(fSize > 0){
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(uFile));
	
				String mimetype = "text/html";
				response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition", "attachment;filename=\"" + vo.getFilename() + "\"");
				response.setContentLength(fSize);
				
				FileCopyUtils.copy(in, response.getOutputStream());
				in.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
		} catch (Exception e) {
			log.error("downloadFile.do Exception : " + e);
			log.error("detail Message : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 사전 임시파일 삭제
	 * @param model
	 * @return "/management/delFile"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/delFile.do")
	public void delFile(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo, HttpServletResponse response) throws Exception {
		try {
			if(vo.getFilepath() != null)
				new File(vo.getFilepath()).delete();
		} catch (Exception e) {
			log.error("delFile.do Exception : " + e);
			log.error("detail Message : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 사전 올리기 초기화면 표시
	 * @param model
	 * @return "/management/downloadDictionaryInit"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/uploadDictionaryInit.do")
	public String uploadDictionaryInit(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo) {
		
		model.addAttribute("dicType", vo.getDicType());
		switch (vo.getDicType()) {
		case 2:
			model.addAttribute("dicTitle", Globals.COM_FILE_STOPWORD);
			break;
		case 3:
			model.addAttribute("dicTitle", Globals.COM_FILE_USERDIC);
			break;
		case 7:
			model.addAttribute("dicTitle", Globals.COM_FILE_USERCNDIC);
			break;
		case 8:
			model.addAttribute("dicTitle", Globals.COM_FILE_QUASISYNONYM);
			break;
		default :
			break;
		}
		
		return "/common/uploadDic";
	}
	
	// 
	/**
	 * 업로드 가능한 사전파일 확인
	 * @param model
	 * @return "/management/uploadDictionary"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/uploadFile.do")
	public String uploadFile(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo, HttpServletRequest request) throws Exception {
		
		DictionaryCheckResult result = null;
		String type = "";
		String fileName = "";
		String tempFilePath = "";
		String encoding = "";
		int dicType = 0;
		
		try {
			
			String contentType = null;
			com.diquest.ir.client.util.RequestHandler userRequest = new com.diquest.ir.client.util.RequestHandler(request, request.getSession().getServletContext());
			String type1 = userRequest.getHeader("Content-Type");
			String type2 = userRequest.getContentType();
			
			int errorCode = 0;
			String errorMessage = null;
			
			// If one value is null, choose the other value
			if (type1 == null && type2 != null) {
				contentType = type2;
			}
			else if (type2 == null && type1 != null) {
				contentType = type1;
			}
			// If neither value is null, choose the longer value
			else if (type1 != null && type2 != null) {
				contentType = (type1.length() > type2.length() ? type1 : type2);
			}
			FileUploadRequestHandler control = null;
			if (contentType != null && contentType.toLowerCase().startsWith("multipart/form-data")) {
				String tcoll = "system";
				String tprofile = "default";

				tempFilePath = Globals.DICTIONARY_DOWNLOAD_FILEPATH;
				File tmpFileDir = new File(tempFilePath);
				if(!tmpFileDir.exists()) {
					tmpFileDir.mkdirs();
				}
				tempFilePath = tempFilePath  + '/' + (tcoll)+'.'+(tprofile)+'.'+"super";
				try{
					control = new FileUploadRequestHandler(userRequest, null, tempFilePath);
					fileName = control.getFileName();
					encoding = control.getFileEncoding();
					dicType = control.getInteger("type", 0);
				}catch(IOException ee) {
					errorCode = -1;
					errorMessage = ee.getMessage();
					log.error("Exception : " + errorMessage);
					throw ee;
				}
			}
			
			switch(dicType){
				case 2:
					type = "불용어 사전";
					break;
				case 3:
					type = "사용자 사전";
					break;
				case 7:
					type = "복합어 사전";
					break;
				case 8:
					type = "유의어 사전";
					break;
				default :
					type = "알수 없음";
					break;
			}
		
			DictionaryChecker checker = null;
			switch(dicType){
				case 2 :
					checker = new StopwordChecker(control, ",", true);
					break;
				case 3 :
					checker = new UserDicChecker(control, ",", true);
					break;
				case 7 :
					checker = new UserCnDicChecker(control, ",", true);
					break;
				case 8 :	// quasi-synonym
					checker = new ThesaurusChecker(control, ",", true);
					break;
				default :
					checker = null;
					break;
			}
			result = checker.check();
			control.close();
			
		} catch (Exception e) {
			log.error("uploadFile.do Exception : " + e);
			log.error("detail Message : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		model.addAttribute("fileName", fileName);
		model.addAttribute("encoding", encoding);
		model.addAttribute("dicType", dicType);
		model.addAttribute("tempFilePath", tempFilePath);
		model.addAttribute("dicTitle", type);
		model.addAttribute("uploadResult", result);
		
		return "/common/uploadDicProc";
	}
	
	/**
	 * 업로드 완료
	 * @param model
	 * @return "/management/downloadDictionaryInit"
	 * @exception Exception
	 */
	@RequestMapping(value="/management/uploadDictionary.do")
	public String uploadDictionary(Model model, @ModelAttribute("dictionaryVo") DictionaryVo vo) throws Exception {
		try {
			model.addAttribute("uploadFlg", commonDictionaryService.uploadDictionary(vo));
		} catch (Exception e) {
			log.error("uploadDictionary.do Exception : " + e);
			log.error("detail Message : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return "/common/uploadDicProc";
	}
}
