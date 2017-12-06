package com.diquest.voc.management.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.diquest.ir.client.dic.checker.DictionaryCheckResult;
import com.diquest.ir.client.dic.download.DictionaryDownloader;
import com.diquest.ir.client.dic.download.StopwordDownloader;
import com.diquest.ir.client.dic.download.ThesaurusDownloader;
import com.diquest.ir.client.dic.download.UserCnDicDownloader;
import com.diquest.ir.client.dic.download.UserDicDownloader;
import com.diquest.ir.client.dic.upload.DictionaryUploader;
import com.diquest.ir.client.dic.upload.StopwordUploader;
import com.diquest.ir.client.dic.upload.ThesaurusUploader;
import com.diquest.ir.client.dic.upload.UserCnDicUploader;
import com.diquest.ir.client.dic.upload.UserDicUploader;
import com.diquest.ir.common.msg.dictionary.ThesaurusConstant;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.management.service.CommonDictionaryService;
import com.diquest.voc.management.vo.DictionaryVo;

@Service("commonDictionaryService")
public class CommonDictionaryServiceImpl implements CommonDictionaryService{

	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	Connection conn = null;
	String id = Globals.MARINER_ADMIN_ID;
	String profileID = Globals.DICTIONARY_PROFILE_DEFAULT; // 전체
	String collectionID = Globals.DICTIONARY_COLLECTION_SYSTEM; // 전체
	
	@Resource(name = "dataSource")
	DataSource dataSource;
	
	@Override
	public HashMap<String, Object> downloadDictionary(DictionaryVo vo) throws Exception {
		HashMap<String,Object> map = new HashMap<>();
		try{
			String engType = "";
			
			conn = dataSource.getConnection();
			
			boolean supported = Charset.isSupported(vo.getEncoding());
			
			DictionaryDownloader downloader = null;
			switch(vo.getDicType()){
				case 2:
					engType = "stopword";
					downloader = new StopwordDownloader(conn, collectionID, profileID);
					break;
				case 3:
					engType = "userdic";
					downloader = new UserDicDownloader(conn);
					break;
				case 7:
					engType = "usercndic";
					downloader = new UserCnDicDownloader(conn);
					break;
				case 8:
					engType = "quasisynonym";
					downloader = new ThesaurusDownloader(conn, collectionID, profileID, ThesaurusConstant.QUASI_SYNONYM);
					break;
				default :
					downloader = null;
					break;
			}
			int counts = 0;
			String tempFilePath = Globals.DICTIONARY_DOWNLOAD_FILEPATH + File.separator + engType + ".System.default";
			String tempFileName = tempFilePath + '.' + vo.getEncoding();
			if(supported){
				File userPath = new File(Globals.DICTIONARY_DOWNLOAD_FILEPATH);
				if(!userPath.exists() || !userPath.isDirectory()){
					userPath.mkdirs();
				}
				counts = downloader.write(tempFileName, ",", vo.getEncoding());
				//counts = downloader.write(tempFileName, "\t", vo.getEncoding());
			}
			
			map.put("dicTitle", vo.getDicTitle());
			map.put("counts", counts);
			map.put("fileName", engType + ".System.default." + vo.getEncoding());
			map.put("filePath", Globals.DICTIONARY_DOWNLOAD_FILEPATH + File.separator + engType + ".System.default." + vo.getEncoding());
		}
		catch(Exception e){
			log.error("Exception : " + e);
			log.error("detail Message : " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		

		return map;
	}

	@Override
	public int uploadDictionary(DictionaryVo vo) throws Exception {

		String tempFilePath = vo.getFilepath();
		//String tempFileName = vo.getFilename();
		String fileEncoding = vo.getEncoding();
		int dicType = vo.getDicType();
		
		conn = dataSource.getConnection();
		
		File tempFile = new File(tempFilePath);
		if(!tempFile.exists())
			throw new Exception();

		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile), fileEncoding));
		DictionaryUploader uploader = null;
		String type = null;
		switch(dicType){
			case 2 :
				uploader = new StopwordUploader(conn, collectionID, profileID, reader, ",");
				type = "불용어 사전";
				break;
			case 3 :
				uploader = new UserDicUploader(conn, reader, ",");
				type = "사용자 사전";
				break;
			case 7 :
				uploader = new UserCnDicUploader(conn, reader, ",");
				type = "복합어 사전";
				break;
			case 8 :
				uploader = new ThesaurusUploader(conn, collectionID, profileID, ThesaurusConstant.QUASI_SYNONYM, reader, ",");
				type = "유의어 사전";
				break;
			default :
				uploader = null;
				type = "알수 없음";
				break;
		}
		DictionaryCheckResult result = uploader.upload();
		reader.close();
		if(tempFilePath != null){
			tempFile.delete();
		}
		
		return result.getNormal();
	}

}
