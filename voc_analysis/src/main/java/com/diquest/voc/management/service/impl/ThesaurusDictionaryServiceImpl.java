package com.diquest.voc.management.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.diquest.ir.client.command.AdminServerClient;
import com.diquest.ir.client.command.CommandExtractor;
import com.diquest.ir.client.command.CommandMorphAnalyzer;
import com.diquest.ir.client.command.dictionary.CommandThesaurus;
import com.diquest.ir.client.util.PostPageNavigator;
import com.diquest.ir.common.database.entity.profile.thesaurus.Thesaurus;
import com.diquest.ir.common.database.handler.dictionary.ThesaurusDB;
import com.diquest.ir.common.exception.IRException;
import com.diquest.ir.common.msg.dictionary.DictionaryType;
import com.diquest.ir.common.msg.dictionary.ThesaurusConstant;
import com.diquest.ir.common.msg.morph.MorphResultT;
import com.diquest.ir.util.common.DateUtil;
import com.diquest.ir.util.common.StringUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.management.service.ThesaurusDictionaryService;
import com.diquest.voc.management.vo.ThesaurusDictionaryVo;

@Service("thesaurusDictionaryService")
public class ThesaurusDictionaryServiceImpl implements ThesaurusDictionaryService{

	Connection conn = null;
	String id = Globals.MARINER_ADMIN_ID;
	String masterServerIp = Globals.MARINER_IP;
	int masterServerPort = Globals.MARINER_PORT;
	String profileID = Globals.DICTIONARY_PROFILE_DEFAULT; // default는 빈값으로 넘김
	String collectionID = Globals.DICTIONARY_COLLECTION_SYSTEM; // System 은 빈값으로 넘김
	byte thesaurusID = ThesaurusConstant.QUASI_SYNONYM; // 유의어

	Object[] adminServer = new Object[]{masterServerIp, masterServerPort};
	AdminServerClient adminServerClient = new AdminServerClient((String)adminServer[0], ((Integer)adminServer[1]).intValue());
	
	@Resource(name = "dataSource")
	DataSource dataSource;
		
	@Override
	public HashMap<String, Object> selectThesaurusDictionaryList(
			ThesaurusDictionaryVo vo) throws Exception {
		int totalCount = 0;
		int sizeRows = 0;
		int startRowNumber = 0;
		int searchCount = 0;
		int rowsperPage = vo.getPageSize();
		int currentPage = vo.getCurrentPageNo();
		int currentEnd = 0;
		
		String sort = vo.getSort();
		String keyword = vo.getKeyword();
		
		Thesaurus [] thesaurusList = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<ThesaurusDictionaryVo> list = new ArrayList<ThesaurusDictionaryVo>();

		try {
			conn = dataSource.getConnection();
			if(keyword.equals(""))
				totalCount = ThesaurusDB.getCount(conn, collectionID, profileID, thesaurusID);
			else
				totalCount = ThesaurusDB.getCountSearch(conn, collectionID, profileID, thesaurusID, keyword);
			
			searchCount = totalCount;
			
			startRowNumber = PostPageNavigator.getStartNum(searchCount, currentPage, rowsperPage)+1;
			currentEnd = PostPageNavigator.getEndNum(searchCount, currentPage, rowsperPage)+1;
			sizeRows = searchCount > currentEnd ? rowsperPage : ((searchCount%rowsperPage > 0) ? (searchCount%rowsperPage): rowsperPage);
			
			//리스트 OR 검색리스트 가져오기
			if(keyword.equals(""))
				thesaurusList = ThesaurusDB.getListPage(conn, collectionID, profileID, thesaurusID, startRowNumber, sizeRows, sort);
			else
				thesaurusList = ThesaurusDB.getListSearchPage(conn, collectionID, profileID, thesaurusID, keyword, startRowNumber, sizeRows, sort);

			for(int i=0;i<thesaurusList.length;i++){
				
				ThesaurusDictionaryVo thesaurusVo = new ThesaurusDictionaryVo();
				thesaurusVo.setKeyword(thesaurusList[i].getKeyword());
				thesaurusVo.setRegDate(DateUtil.changeFormat(new Date(thesaurusList[i].getRegDate()),"yyyy/MM/dd"));
				thesaurusVo.setApply(thesaurusList[i].isApply());
				thesaurusVo.setThesaurusCount(Integer.toString(thesaurusList[i].getThesaurusCount()));
				list.add(thesaurusVo);
			}

		} catch (Exception e) {
			System.out.println("error : "+e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("error : "+e);
				}
			}
		}
		
		map.put("list", list);
		map.put("totSize", totalCount);
		
		return map;
	}

	@Override
	public void deleteThesaurusDictionary(ThesaurusDictionaryVo vo) throws Exception {
		try {
			conn = dataSource.getConnection();
			
			for(String keyword : vo.getSelectedKeyword()){
				ThesaurusDB.delete(conn, collectionID, profileID, thesaurusID, keyword);
			}

		} catch (Exception e) {
			System.out.println("error : "+e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("error : "+e);
				}
			}
		}
	}
	
	@Override
	public void applyThesaurusDictionary() throws IRException {
		//유의어 사전 - 적용
		try{
			CommandThesaurus commandThesaurus = new CommandThesaurus(adminServerClient, id, thesaurusID);
			commandThesaurus.apply(collectionID, profileID);
			
		}catch (IRException e) {
			System.out.println("error : "+e.getRawTraceText());
		} 
		
	}

	@Override
	public ThesaurusDictionaryVo selectThesaurusChKeyword(ThesaurusDictionaryVo vo) throws Exception {
		
		String chKeyword = "";
		int chFlag = 0;
		Thesaurus thesaurus = null;
		
		ThesaurusDictionaryVo thesaurusVo = new ThesaurusDictionaryVo();

		try {
			conn = dataSource.getConnection();
			
			//형태소 분석 - 키워드 추가시
			if(!vo.getKeyword().equals("")){
				CommandExtractor command = new CommandExtractor((String)adminServer[0], ((Integer)adminServer[1]).intValue());
				String[][] result = command.request("KOREAN", null, vo.getKeyword());
				
				if(result != null){
					for(int i=0;i<result.length;i++)
						for(int j=0;j<result[i].length;j++)
							chKeyword += result[i][j] + " ";
					chKeyword = chKeyword.trim();
					
					if(!vo.getKeyword().equals(chKeyword) && chKeyword.indexOf(vo.getKeyword()) < 0)
						chFlag = 1;
				}
			}
			
			thesaurusVo.setChKeyword(chKeyword);
			thesaurusVo.setChFlag(chFlag);

			// 등록 여부 판별해서 등록 되어 있으면 리스트 가져옴
			thesaurus = ThesaurusDB.get(conn, collectionID, profileID, thesaurusID, vo.getKeyword());
			
			if(thesaurus != null){
				thesaurus = ThesaurusDB.get(conn, collectionID, profileID, thesaurusID, vo.getKeyword());
				thesaurusVo.setThesaurusKeywordList(thesaurus.getKeywords());
			}
		} catch (Exception e) {
			System.out.println("error : "+e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("error : "+e);
				}
			}
		}

		return thesaurusVo;
	}

	@Override
	public ThesaurusDictionaryVo extractorThesaurusKeyword(ThesaurusDictionaryVo vo)
			throws Exception {

		ThesaurusDictionaryVo thesaurusVo = new ThesaurusDictionaryVo();
		
		// 색인어휘 추출 결과
		CommandExtractor command = new CommandExtractor((String)adminServer[0], ((Integer)adminServer[1]).intValue());
		String[][] results = command.request("KOREAN", null, vo.getKeyword());

		String[] result = results[0];
		String[] remnant = results[1];
		String[] additional = results[2];
		
		StringBuffer extractor = new StringBuffer();

		for(int i=0;i<result.length;i++){
			extractor.append(StringUtil.simpleHTML2Text(result[i]));
			if( i!=result.length-1 ) extractor.append(", ");
		}
		
		if(additional.length>0) extractor.append(" (복합명사: ");
		
		for(int i=0;i<additional.length;i++){
			extractor.append(StringUtil.simpleHTML2Text(additional[i]));
			if( i!=additional.length-1 ) extractor.append(", ");
		}
		
		if(additional.length>0) extractor.append(")");
		
		thesaurusVo.setExtractor(extractor.toString());
		
		// 형태소 분석 결과
		String clazz = "com.diquest.ir.core.index.morpher.korean.JianaCottonMorpher";
		String sentence = vo.getKeyword();
		
		CommandMorphAnalyzer commandMorph = new CommandMorphAnalyzer(adminServerClient);
		Properties prop = new Properties();
		prop.setProperty("user.language","ko");
		MorphResultT morphResult = commandMorph.request(sentence, clazz, prop);
		
		int termCount = morphResult.getTermCount();
		int[] lefts = morphResult.getLefts();
		int[] rights = morphResult.getRights();
		String[] names = morphResult.getTagNames();
		
		StringBuffer morpheme = new StringBuffer();
		for(int i=0;i<termCount;i++){
			if( i!=0){
				morpheme.append(", ");
			}
			morpheme.append(sentence.substring(lefts[i],rights[i]));
			morpheme.append(":<span class='font_ita'>");
			morpheme.append(names[i]);
			morpheme.append("</span>");
		}
		
		thesaurusVo.setMorpheme(morpheme.toString());
		
		return thesaurusVo;
	}

	@Override
	public int addThesaurusKeyword(ThesaurusDictionaryVo vo) throws Exception {

		String[] thesauruses = vo.getThesaurusKeywordList();
		String selectKeyword = vo.getKeyword();
		long regDate = Long.parseLong(vo.getRegDate());
		
		Thesaurus thesaurus = new Thesaurus(collectionID, profileID, thesaurusID, selectKeyword, thesauruses.length, thesauruses, regDate, false);

		try {
			conn = dataSource.getConnection();
			
			//단방향
			if(vo.getWay() == 1){
				if(thesaurus.getRegDate() != 0){		
						thesaurus.setApply(false);
						ThesaurusDB.update(conn, thesaurus);
				}else{
					ThesaurusDB.put(conn, collectionID, profileID, thesaurusID, thesaurus.getKeyword(), thesaurus.getKeywords());
					thesaurus = ThesaurusDB.get(conn, collectionID, profileID, thesaurusID, selectKeyword);
				}
			//양방향
			}else{
				//keywordList 만들기 
				String[] keywordList = new String[thesaurus.getThesaurusCount()+1];
				keywordList[0] = thesaurus.getKeyword();
				for(int i=1 ; i<keywordList.length ; i++)
					keywordList[i] = thesaurus.getKeywords()[i-1];
				
				for(int i=0; i<keywordList.length ; i++){
					//keyword, keyword list 쌍
					String singleKeyword = keywordList[i];
					String[] listKeyword = new String[keywordList.length-1];
					int count = 0;
					for(int j=0 ; j<keywordList.length ; j++){
						if(j!=i){
							listKeyword[count]=keywordList[j];
							count++;
						}
					}
					
					//각 키워드별로 존재하는지 확인 - 있으면 업데이트, 없으면 put
					if(ThesaurusDB.get(conn, collectionID, profileID, thesaurusID, singleKeyword) != null){
						ThesaurusDB.update(conn, collectionID, profileID, thesaurusID, singleKeyword, listKeyword);
					}else{
						ThesaurusDB.put(conn, collectionID, profileID, thesaurusID, singleKeyword, listKeyword);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("error : "+e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("error : "+e);
				}
			}
		}
		
		return 1;
	}

	@Override
	public ThesaurusDictionaryVo selectThesaurusDictionary(ThesaurusDictionaryVo vo)
			throws Exception {

		String chKeyword = "";
		int chFlag = 0;
		String selectKeyword = vo.getKeyword();
		
		Thesaurus thesaurus = null;
		ThesaurusDictionaryVo thesaurusVo = new ThesaurusDictionaryVo();

		try {
			conn = dataSource.getConnection();
			
			thesaurus = ThesaurusDB.get(conn, collectionID, profileID, thesaurusID, selectKeyword);
			
			thesaurusVo.setKeyword(thesaurus.getKeyword());
			//thesaurusVo.setRegDate(DateUtil.changeFormat(new Date(thesaurus.getRegDate()),"yyyy/MM/dd"));
			thesaurusVo.setRegDate(String.valueOf(thesaurus.getRegDate()));
			thesaurusVo.setApply(thesaurus.isApply());
			thesaurusVo.setThesaurusCount(Integer.toString(thesaurus.getThesaurusCount()));
			thesaurusVo.setThesaurusKeywordList(thesaurus.getKeywords());

			//형태소 분석 - 키워드 추가시	
			CommandExtractor command = new CommandExtractor((String)adminServer[0], ((Integer)adminServer[1]).intValue());
			String[][] result = command.request("KOREAN", null, vo.getKeyword());
			
			if(result != null){
				for(int i=0;i<result.length;i++)
					for(int j=0;j<result[i].length;j++)
						chKeyword += result[i][j] + " ";
				chKeyword = chKeyword.trim();
				
				if(!vo.getKeyword().equals(chKeyword) && chKeyword.indexOf(vo.getKeyword()) < 0)
					chFlag = 1;
			}
			
			thesaurusVo.setChKeyword(chKeyword);
			thesaurusVo.setChFlag(chFlag);
			
		} catch (Exception e) {
			System.out.println("error : "+e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("error : "+e);
				}
			}
		}
		return thesaurusVo;
	}

	@Override
	public ThesaurusDictionaryVo selectThesaurusAddKeyword(ThesaurusDictionaryVo vo)
			throws Exception{
		String chAddKeyword = "";
		int chAddFlag = 0;
		
		ThesaurusDictionaryVo thesaurusVo = new ThesaurusDictionaryVo();
	
		try {
			conn = dataSource.getConnection();
			
			CommandExtractor command = new CommandExtractor((String)adminServer[0], ((Integer)adminServer[1]).intValue());
			String[][] result = command.request("KOREAN", null, vo.getKeyword());
			
			if(result != null){			
				for(int i=0;i<result.length;i++)
					for(int j=0;j<result[i].length;j++)
						chAddKeyword += result[i][j] + " ";
				
				chAddKeyword = chAddKeyword.trim();
				
				if(!vo.getKeyword().equals(chAddKeyword) && chAddKeyword.indexOf(vo.getKeyword()) < 0)
					chAddFlag = 1;
			}
			
			thesaurusVo.setChAddKeyword(chAddKeyword);
			thesaurusVo.setChAddFlag(chAddFlag);
		} catch (Exception e) {
			System.out.println("error : "+e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("error : "+e);
				}
			}
		}
		return thesaurusVo;
	}

}
