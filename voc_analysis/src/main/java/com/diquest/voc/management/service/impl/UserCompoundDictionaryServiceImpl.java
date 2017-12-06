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
import com.diquest.ir.client.command.dictionary.CommandUserCnDic;
import com.diquest.ir.client.util.PostPageNavigator;
import com.diquest.ir.common.database.entity.profile.usercndic.UserCnDic;
import com.diquest.ir.common.database.handler.dictionary.UserCnDicDB;
import com.diquest.ir.common.msg.dictionary.ThesaurusConstant;
import com.diquest.ir.common.msg.morph.MorphResultT;
import com.diquest.ir.util.common.DateUtil;
import com.diquest.ir.util.common.StringUtil;
import com.diquest.voc.cmmn.service.Globals;
import com.diquest.voc.management.service.UserCompoundDictionaryService;
import com.diquest.voc.management.vo.UserCompoundDictionaryVo;

@Service("userCompoundDictionaryService")
public class UserCompoundDictionaryServiceImpl implements UserCompoundDictionaryService{

	Connection conn = null;
	String id = Globals.MARINER_ADMIN_ID;
	String masterServerIp = Globals.MARINER_IP;
	int masterServerPort = Globals.MARINER_PORT;

	Object[] adminServer = new Object[]{masterServerIp, masterServerPort};
	AdminServerClient adminServerClient = new AdminServerClient((String)adminServer[0], ((Integer)adminServer[1]).intValue());
	
	@Resource(name = "dataSource")
	DataSource dataSource;
	
	@Override
	public HashMap<String, Object> selectUserCompoundDictionaryList(UserCompoundDictionaryVo vo) throws Exception{
	int totalCount = 0;
	int sizeRows = 0;
	int startRowNumber = 0;
	int searchCount = 0;
	int rowsperPage = vo.getPageSize();
	int currentPage = vo.getCurrentPageNo();
	int currentEnd = 0;
	
	String sort = vo.getSort();
	String keyword = vo.getKeyword();
	
	UserCnDic [] userDicList = null;
	HashMap<String, Object> map = new HashMap<String, Object>();
	List<UserCompoundDictionaryVo> list = new ArrayList<UserCompoundDictionaryVo>();

	try {
		conn = dataSource.getConnection();
		
		
		totalCount = UserCnDicDB.getCount(conn);
		searchCount = totalCount;
		if(!keyword.equals(""))
			searchCount = UserCnDicDB.getCountSearch(conn, keyword);
		startRowNumber = PostPageNavigator.getStartNum(searchCount, currentPage, rowsperPage)+1;
		currentEnd = PostPageNavigator.getEndNum(searchCount, currentPage, rowsperPage)+1;
		sizeRows = searchCount > currentEnd ? rowsperPage : ((searchCount%rowsperPage > 0) ? (searchCount%rowsperPage): rowsperPage);
		
		//리스트 OR 검색리스트 가져오기
		if(keyword.equals(""))
			userDicList = UserCnDicDB.getListPage(conn, startRowNumber, sizeRows, sort);
		else
			userDicList = UserCnDicDB.getListPageSearch(conn, keyword, startRowNumber, sizeRows, sort);
		
		for(int i=0;i<userDicList.length;i++){
			
			UserCompoundDictionaryVo userVo = new UserCompoundDictionaryVo();
			userVo.setKeyword(userDicList[i].getKeyword());
			userVo.setRegDate(DateUtil.changeFormat(new Date(userDicList[i].getRegDate()),"yyyy/MM/dd"));
			userVo.setApply(userDicList[i].isApply());
			list.add(userVo);
		}

	} catch (Exception e) {
		throw e;
	} finally {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	
	map.put("list", list);
	map.put("totSize", searchCount);
	
	return map;
	}

	@Override
	public void deleteUserCompoundDictionary(UserCompoundDictionaryVo vo) throws Exception {
		
		try {
			conn = dataSource.getConnection();
			
			//사용자 사전 - 키워드 삭제
			for(String keyword : vo.getSelectedKeyword()){
				UserCnDicDB.delete(conn, keyword);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
		

	}

	@Override
	public void applyUserCompoundDictionary() throws Exception {
		//사용자 사전 - 적용
		CommandUserCnDic commandUserDic = new CommandUserCnDic(adminServerClient, id);
		commandUserDic.apply();
	}

	@Override
	public UserCompoundDictionaryVo extractorUserCompoundKeyword(UserCompoundDictionaryVo vo)
			throws Exception {

		UserCompoundDictionaryVo userVo = new UserCompoundDictionaryVo();
		
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

		/*		if(result.length>0) extractor.append(", ");
			
		for(int i=0;i<remnant.length;i++){
			extractor.append(StringUtil.simpleHTML2Text(remnant[i]));
			if( i!=remnant.length-1 ) extractor.append(", ");
		}

		if(remnant.length>0) extractor.append(", ");*/
		
		if(additional.length>0) extractor.append(" (복합명사: ");
		
		for(int i=0;i<additional.length;i++){
			extractor.append(StringUtil.simpleHTML2Text(additional[i]));
			if( i!=additional.length-1 ) extractor.append(", ");
		}
		
		if(additional.length>0) extractor.append(")");
		
		userVo.setExtractor(extractor.toString());
		
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
		
		userVo.setMorpheme(morpheme.toString());
		
		return userVo;
	}

	@Override
	public int addUserCompoundKeyword(UserCompoundDictionaryVo vo) throws Exception {
		
		try {
			conn = dataSource.getConnection();
			
			//사용자 사전 편집 - 등록
			if(UserCnDicDB.get(conn, vo.getKeyword())==null){
				UserCnDicDB.put(conn, vo.getKeyword());
				return 1;
			}else{
				return 2;
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw e;
				}
			}
		}
	}


}
