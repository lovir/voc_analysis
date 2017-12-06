package com.diquest.voc.management.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.management.service.StopwordDictionaryService;
import com.diquest.voc.management.vo.StopWordDictionaryVo;

@Service("stopwordDictionaryService")
public class StopwordDictionaryServiceImpl implements StopwordDictionaryService{

	/** stopWordDAO */
	@Resource(name="stopWordDAO")
	private StopWordDictionaryDAO stopWordDAO;

	/**
	 * 불용어 사전 조회.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 키워드
	 * @exception Exception
	 */
	public List getStopwordList(StopWordDictionaryVo vo) throws Exception{
		return  stopWordDAO.selectStopwordList(vo);
	}
	
	/**
	 * 불용어 사전 총갯수 조회.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 키워드
	 * @exception Exception
	 */
	public int getStopwordCnt(StopWordDictionaryVo vo) throws Exception{
		return stopWordDAO.selectStopwordCnt(vo);
	}

	/**
	 * 불용어 추가시 등록 유무 체크
	 * @param keyword
	 * @return 키워드
	 * @exception Exception
	 */
	public int getSelectStopword(String keyword) throws Exception{
		return stopWordDAO.selectStopword(keyword);
	}
	
	/**
	 * 불용어 추가
	 * @param keyword
	 * @return 키워드
	 * @exception Exception
	 */
	public void insertStopword(StopWordDictionaryVo vo) throws Exception{
		stopWordDAO.insertStopword(vo);
	}
	
	/**
	 * 불용어 사전 삭제.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 키워드
	 * @exception Exception
	 */
	public int deleteStopword(String[] DelIdList, String[] DelkeywordList) throws Exception{
		stopwordFileWriter(DelkeywordList, "");
		return stopWordDAO.deleteStopword(DelIdList);
	}

	/**
	 * 불용어 사전 적용.
	 * @param applyIdList
	 * @return 키워드
	 * @exception Exception
	 */
	public int applyStopword(String[] applyIdList, String[] applyKeywordList) throws Exception{
		stopwordFileWriter(applyKeywordList, "apply");
		return stopWordDAO.applyStopword(applyIdList);
	}
	
	/**
	 * 파일 생성.
	 * @param keywordList
	 * @param type
	 * @exception Exception
	 */
	private void stopwordFileWriter(String[] keywordList, String type) throws Exception{
		
		BufferedReader in = null;
		BufferedWriter out = null;
		
		ArrayList<String> newKeywordList = new ArrayList<String>();	//파일에 새로 쓸 키워드 리스트
		
		if(type.equals("apply")){	//적용일때만 실행
			for(String keyword : keywordList){
				newKeywordList.add(keyword);
			}
		}	
		
		try{
			//in = new BufferedReader(new FileReader(Globals.MARINER_STOPWORD_FILEPATH));
			in = new BufferedReader( new InputStreamReader(new FileInputStream(Globals.MARINER_STOPWORD_FILEPATH), "EUC-KR"));
			String existKeyword = null;
			while((existKeyword = in.readLine()) !=null){
				//적용할 키워드가 파일에 이미 있으면 안쓰기
				if(!existCheck(keywordList, existKeyword)){
					newKeywordList.add(existKeyword);
				}
			}
			//out = new BufferedWriter(new FileWriter(Globals.MARINER_STOPWORD_FILEPATH));	//def:false 새로쓰기
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Globals.MARINER_STOPWORD_FILEPATH), "EUC-KR"));
			for(String keyword : newKeywordList){
				out.write(keyword);
				out.newLine();
			}
			
		} catch (FileNotFoundException fne) {
			fne.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if(in != null) try{in.close();}catch(IOException e){}
			if(out != null) try{out.close();}catch(IOException e){}
		}
	}
	
	/**
	 * 파일에 키워드 존재 여부 체크.
	 * @param applykeywordList
	 * @param existKeyword
	 * @return boolean
	 * @exception Exception
	 */
	private boolean existCheck(String[] applykeywordList, String existKeyword){
		
		for(String applykeyword : applykeywordList){
			if(applykeyword.equals(existKeyword)){
				return true;
			}
		}
		return false;
	}

}