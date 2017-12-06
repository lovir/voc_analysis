package com.diquest.voc.common.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.common.service.CommonService;
import com.diquest.voc.common.vo.CommonCodeVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("commonService")
public class CommonServiceImpl implements CommonService{

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/** CommonDao */
	@Resource(name="commonDao")
	private CommonDao commonDao;

	@Override
	public String adLogin(String userID, String userPass) throws Exception {

		String code = "";
		
		try {
			URL url = null;
			BufferedReader in = null;
			URLConnection con = null;

			StringBuffer returnMsg = new StringBuffer();
			String urlstr = Globals.ADLOGIN_URL + "?uid="
					+ java.net.URLEncoder.encode(userID.toUpperCase(), "UTF-8")
					//+ "&pwd=" + userPass;
					+ "&pwd=" + java.net.URLEncoder.encode(userPass, "UTF-8");
			try {
				url = new URL(urlstr);
				con = url.openConnection();
				con.connect();
				in = new BufferedReader(new InputStreamReader(
						con.getInputStream(), "UTF-8"));
				String tmp_msg = "";
				while ((tmp_msg = in.readLine()) != null) {
					if (!"".equals(tmp_msg)) {
						returnMsg.append(tmp_msg);
					}
				}
			} catch (MalformedURLException malformedurlexception) {
				log.debug(malformedurlexception.getMessage());
			} catch (IOException ioexception) {
				log.debug(ioexception.getMessage());
			} finally {
				try {
					if (in != null) {
						in.close();
					}
				} catch (Exception ex3) {
					log.debug(ex3.toString());
				}
			}
			
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
			JsonObject adLoginResult = gson.fromJson(returnMsg.toString(), JsonObject.class);
			
			try {
				log.debug("adLoginResult : " + adLoginResult);
				// 결과값이 존재할 경우
				if (adLoginResult != null) {
					JsonObject obj = adLoginResult.get("header").getAsJsonObject();
					
					code = obj.get("code").getAsString();
				}
			} catch (Exception e) {
				log.error(e.toString());
			}
		} catch (Exception e1) {
			log.error(e1.toString());
		}
		return code;
	}

	@Override
	public String ssoLogin(String uidTmp, String guId, String sId) {
		StringBuffer returnMsg = new StringBuffer();
		
		String uid = "";

		URL url = null;
		BufferedReader in = null;
		URLConnection con = null;
		byte[] tempByte1 = null;
		byte[] tempByte2 = null;
		
		try{
			BASE64Decoder decoder = new BASE64Decoder();
			tempByte1 = decoder.decodeBuffer(uidTmp);
			tempByte2  = decoder.decodeBuffer(new String(tempByte1));
			uid = new String(tempByte2);
	
			String urlstr = Globals.SSOLOGIN_URL + "?uid="+uid+"&guid="+guId+"&sid="+ sId;
			
			url = new URL(urlstr);
			con = url.openConnection();
			con.connect();
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String tmp_msg = "";
			while ((tmp_msg = in.readLine()) != null){
				if (!"".equals(tmp_msg)) {
					returnMsg.append(tmp_msg);
				}
			}

		}catch(Exception e){
			log.error("Exception : " + e);
		}finally {
			try{
				if (in != null){
					in.close();
				}
			}
			catch (Exception ex3){
				log.error("Exception : " + ex3);
			}
		}
		return returnMsg.toString();
	}
	
	@Override
	public EgovMap selectLogin(String userID) throws Exception {
		
		return commonDao.selectLogin(userID);
	}

	@Override
	public EgovMap selectIdLogin(String uidTmp) throws Exception {
		
		String uid = "";
		
		byte[] tempByte1 = null;
		byte[] tempByte2 = null;
		
		BASE64Decoder decoder = new BASE64Decoder();
		tempByte1 = decoder.decodeBuffer(uidTmp);
		tempByte2  = decoder.decodeBuffer(new String(tempByte1));
		uid = new String(tempByte2);
		return commonDao.selectLogin(uid);
	}
	
	@Override
	public String base64LoginCheker(String userID, String passWord) throws Exception {
		
		String pwd = "";
		
		BASE64Encoder encoder = new BASE64Encoder();
		pwd = new String(encoder.encode(passWord.getBytes()));
		System.out.println("pwd:"+pwd);
		return commonDao.selectLogin(userID, pwd);
	}
	
	public String selectLogin_Metro(String portal_id) throws Exception {
		return commonDao.selectLogin_Metro(portal_id);
	}

	@Override
	public List<CommonCodeVo> getVocCommonCode(HashMap<String, Object> paramMap) throws Exception {
		return commonDao.selectVocCommonCode(paramMap);
	}

}
