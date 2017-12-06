package com.diquest.voc.cmmn.service;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.Resource;

import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;

/**
 *  Class Name : Globals.java
 *  Description : 시스템 구동 시 프로퍼티를 통해 사용될 전역변수를 정의한다.
 *  Modification Information
 *
 *     수정일         수정자                   수정내용
 *   -------    --------    ---------------------------
 *   2014. 02. 27    lisa          최초 생성
 *
 *  @author lisa
 *  @since 2014. 02. 27
 *  @version 1.0
 *  @see
 *
 */

public class Globals {
	@Resource(name = "selectBoxService")
	CommonSelectBoxService selectBoxService;
	
	// 검색서버 연결정보
	public static final String MARINER_IP = EgovProperties.getProperty("mariner.ip");
	public static final int MARINER_PORT = Integer.parseInt(EgovProperties.getProperty("mariner.port"));
	public static final String MARINER_COLLECTION = EgovProperties.getProperty("mariner.collection");
	public static final String MARINER_COLLECTION2 = EgovProperties.getProperty("mariner.collection2");
	public static final String MARINER_DRAMA_COLLECTION = EgovProperties.getProperty("mariner.drama_collection");
	public static final String MARINER_DRAMA_COLLECTION2 = EgovProperties.getProperty("mariner.drama_collection2");
	public static final String MARINER_ADMIN_ID = EgovProperties.getProperty("mariner.id");
	public static final String MARINER_STOPWORD_FILEPATH = EgovProperties.getProperty("mariner.stopword");
	public static final int MARINER_POOLWAITTIMEOUT = Integer.parseInt(EgovProperties.getProperty("mariner.poolwaittimeout"));
	public static final int MARINER_MINPOOLSIZE = Integer.parseInt(EgovProperties.getProperty("mariner.minpoolsize"));
	public static final int MARINER_MAXPOLLSIZE = Integer.parseInt(EgovProperties.getProperty("mariner.minpoolsize"));
	
	public static final String ADLOGIN_URL = EgovProperties.getProperty("adLogin.url");
	public static final String SSOLOGIN_URL = EgovProperties.getProperty("ssoLogin.url"); // ?uid=VkV0Q01EVTMg&guid=fda468aa-c087-41a3-b9e0-a227afe5d85d&sid=4
			
	public static final String DICTIONARY_COLLECTION_SYSTEM = "";
	public static final String DICTIONARY_PROFILE_DEFAULT = "";

	public static final String DICTIONARY_DOWNLOAD_FILEPATH = EgovProperties.getProperty("dictionary.download.filepath");

	public static final String[] DEFAULT_INTEREST_KEYWORD = new String[]{"지하철" ,"서울교통공사", "장애"};	//서울메트로 포탈 이용자가 최초 VOC 접속 시 디폴트로 셋팅하는 관심키워드 목록
	public static final int USE_PRITER = 0;	//PRITER 개인정보 보호 사용 여부 1:사용, 0:미사용
	
	//공통코드 리스트 맵
	public static HashMap<String, String> metroDeptList = Globals.setVocRepLevelList();		//서울메트로 부서코드, 부서명 리스트
	public static final HashMap<String, String> metroVocRepLevel = setVocRepLevelList();	//답변만족도 등급
	
	// 대시보드
	public static final int DASH_VOC_INCREANDDECRE = 10; // VOC 긍/부정 VOC증감현황
	public static final int DASH_VOC_INTEREST = 11; // VOC 관심키워드 트렌드 분석 차트
	public static final int DASH_VOC_FIELD = 12; // VOC 유형별 분석
	public static final int DASH_VOC_STATION = 13; // 주요 역별 VOC 현황 분석 차트
	public static final int DASH_VOC_TOP10_KEYWORD = 14; // VOC Top10 키워드 분석
	public static final int DASH_VOC_TOPCATEGORY_KEYWORD = 15; // VOC TopCategory 10 키워드 분석
	public static final int DASH_VOC_ISSUE = 16; // VOC 이슈 클라우드
	public static final int DASH_VOC_ISSUE_DETAIL = 17; // VOC 이슈 클라우드(상세)
	
	// 트렌드 분석
	public static final int TREND_AL_REPORT = 20;	//트랜드 분석
	public static final int TREND_AL_RANKING_PERIOD = 21;		//키워드 랭킹
	public static final int TREND_AL_RANKING_PERIOD_OLD = 22;	//키워드 이전 랭킹
	public static final int TREND_AL_LIST = 23;	//검색결과 리스트 보기
	public static final int TREND_AL_DETAIL = 24;	//검색결과 상세보기
	public static final int TREND_AL_ALIKE = 25;	//유사문서 검색
	public static final int TREND_AL_EXCEL = 26;	//엑셀 다운로드
	public static final int TREND_AL_KEYWORD_PERCENTAGE = 27;	//결과 목록?? 쿼리

	public static final int TREND_PERIOD_DAY = 30;
	public static final int TREND_PERIOD_WEEK = 31;
	public static final int TREND_PERIOD_MONTH = 32;
	public static final int TREND_PERIOD_QUARTER = 33;
	public static final int TREND_PERIOD_HALF = 34;
	public static final int TREND_PERIOD_YEAR = 35;
	public static final int TREND_PERIOD_HOUR = 36;
	public static final int KEYWORD_ISSUE_PERIOD_WEEK = 37;
	
	//역별 현황분석
	public static final int STATION_LIST = 40;	//역별 리스트
	public static final int STATION_KEYWORD_RANKING = 41;	//역별 키워드 랭킹
	public static final int STATION_PNN_PERCENTAGE = 42;	//역별 긍부정 비율
	
	//분야별 현황분석
	public static final int FIELD_PERCENTAGE = 50;	//분류별 비율
	public static final int FIELD_TOP10_KEYWORD = 51;	//분류별 키워드 Top10
	public static final int FIELD_TERND = 52;	//분류별 쳐리 현황
	
	//감성 분석
	public static final int EMOTION_TREND = 60;	//감성분석 추세 차트
	public static final int EMOTION_NEG_DISTRIBUTION = 61;	//감성분석 부정감정 분포
	
	// 소셜 키워드 종합분석
	public static final int KEYWORD_SOCIAL_REPORT = 70;
	public static final int KEYWORD_SOCIAL_REPORT_TOP = 71;
	public static final int KEYWORD_SOCIAL_RANKING_PERIOD = 72;
	public static final int KEYWORD_SOCIAL_RANKING_PERIOD_OLD = 73;
	public static final int KEYWORD_SOCIAL_LIST = 74;
	public static final int KEYWORD_SOCIAL_DETAIL = 75;
	public static final int KEYWORD_SOCIAL_ALIKE = 76;
	public static final int KEYWORD_SOCIAL_EXCEL = 77;
	public static final int KEYWORD_SOCIAL_TOTAL_PERCENTAGE = 78;
	public static final int KEYWORD_SOCIAL_KEYWORD_PERCENTAGE = 79;
	
	//소셜
	
	//소셜 트렌드 종합분석
	public static final int TREND_SOCIAL_REPORT = 200;
	public static final int TREND_SOCIAL_RANKING_PERIOD = 201;
	public static final int TREND_SOCIAL_RANKING_PERIOD_OLD = 202;
	public static final int TREND_SOCIAL_LIST = 203;
	public static final int TREND_SOCIAL_DETAIL = 203;
	public static final int TREND_SOCIAL_ALIKE = 204;
	public static final int TREND_SOCIAL_EXCEL = 205;
	public static final int TREND_SOCIAL_TOTAL_PERCENTAGE = 206;
	public static final int TREND_SOCIAL_KEYWORD_PERCENTAGE = 207;
	
	//소셜 채널별 현황 분석
	public static final int CHANNEL_SITE_PERCENTAGE = 210;	//사이트 별 건수 비율
	public static final int CHANNEL_TOP10_KEYWORD = 211;	//채널별 별 top10 키워드
	
	//소셜 감성 분석
	public static final int EMOTION_CHANNEL_TREND = 220;	//감성 추세 분석
	public static final int EMOTION_CHANNERL_NEG_DISTRIBUTION = 221;	//채널별 별 부정 감성 분포
	// 고객명 검색
	public static final int USER_REPORT = 500;
	public static final int USER_REPORT_COMPLAIN = 501;
	
	public static final int USER_LIST = 530;
	public static final int USER_DETAIL = 531;
	public static final int USER_ALIKE = 532;
	public static final int USER_EXCEL = 533;
	
	// 공통코드
	public static final int COM_PERIOD_DAY = 101; // 일
	public static final int COM_PERIOD_WEEK = 102; // 주
	public static final int COM_PERIOD_MONTH = 103; // 년
	public static final int COM_PERIOD_QUARTER = 104; //분기
	public static final int COM_PERIOD_HALF = 105; // 반기
	public static final int COM_PERIOD_YEAR = 106; // 년
	public static final int COM_PERIOD_HOUR = 107; // 시간
	public static final int COM_ALARM_KEYWORD_WEEK = 1; // 알람키워드 모니터링(지난 일주일)
	public static final int COM_ALARM_KEYWORD_PREV = 2; // 알람키워드 모니터링(전날)
	public static final int COM_ALARM_KEYWORD_CURRENT = 3; // 알람키워드 모니터링(오늘)
	
	public static final String COM_DASH_CURRENT_WEEK = "금주";
	public static final String COM_DASH_CURRENT_DAY = "금일"; // 금주
	public static final String COM_DASH_PREV_DAY = "전일"; // 금주
	public static final String COM_DASH_PREV_WEEK = "전주"; // 전주
	public static final String COM_DASH_PREV_MONTHS = "전월"; // 전월
	public static final String COM_DASH_CURRENT_MONTHS = "당월"; // 당월 #88C9B5  #DE7527 #7570B3
	
	//워드 클라우드 폰트 색상
	public static final String[] COM_WORD_CLOUD_COLOR = {"#1F77B4","#AEC7E8","#FF7F0E","#FFBB78","#2CA02C","#98DF8A","#FF9896","#9467BD","#C5B0D5","#8C564B","#C49C94","#E377C2","#F7B6D2","#7F7FF","#C7C7C7","#BCBD22","#DBDB8D","#17BECF","#9EDAE5", "#FF9900"};
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*public static final String[] COM_DASH_ISSUE_COLOR = {"#f49418", "#DD2222", "#2267DD", "#679a0d", "#a80f94"};*/ // 당월
	public static final String[] COM_DASH_ISSUE_COLOR = {"#DE7527", "#88C9B5", "#7570B3", "#679a0d", "#2267DD"};
	/*public static final String[] COM_DASH_VOC_COLOR = {"#eb6767", "#eb8767", "#eba967", "#ebc863"}*/; // VOC(긍,부정 VOC증감현황)
	public static final String[] COM_DASH_VOC_COLOR = {"#5D85F7", "#83D64A", "#FC8A29", "#ebc863"}; 
	public static final String[] COM_DASH_SOCIAL_COLOR = {"#7a9cee", "#ae7aee", "#d37aee", "#ec7aee", "#ee7ac2"}; // 제안
	public static final String[] COM_DASH_SOCIAL_SHARE_COLOR = {"#8cdcf0", "#f0d38c", "#f0c08c", "#dbf287", "#8cf0d8"}; // 포탈 제안 유형별
	public static final String COM_COMPLAIN_Y = "Y"; // 불만 문서
	public static final String COM_COMPLAIN_N = "N"; // 비불만 문서
	public static final String COM_COMPLAIN_Y_HANGUL = "불만"; // 불만지수 차트 표시용
	public static final String COM_COMPLAIN_N_HANGUL = "비불만"; // 불만지수 차트 표시용
	public static final String COM_SELECT_ALL = "all"; // 콤보박스 전체
	public static final String COM_CST_LVL_VIP = "02"; // VVIP
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final String COM_FILE_STOPWORD = "불용어 사전";
	public static final String COM_FILE_USERDIC = "사용자 사전";
	public static final String COM_FILE_USERCNDIC = "복합어 사전";
	public static final String COM_FILE_QUASISYNONYM = "유의어 사전";
	
	public static final String COM_USE_YN_Y = "Y"; // 활성
	public static final String COM_USE_YN_N = "N"; // 비활성
	
	// 마리너 출력용 필드
	//CONTENT_ORI 지워야 하나??? 검토
	public static final String FIELD_CONTENT_ORI = "CONTENT_ORI"; // 유사문서 검색용
		
	public static final String FIELD_DQ_DOCID = "DQ_DOCID";	//검색 문서 아이디
	public static final String FIELD_TITLE = "TITLE";	//제목
	public static final String FIELD_CONTENT = "CONTENT";	//본문
	public static final String FIELD_YEAR = "YEAR";	//연도
	public static final String FIELD_HALF = "HALF";	//반기
	public static final String FIELD_QUARTER = "QUARTER";	//분기
	public static final String FIELD_MONTH = "MONTH";	//월
	public static final String FIELD_WEEK = "WEEK";		//주
	public static final String FIELD_DAY = "DAY";		//일
	public static final String FIELD_HOUR = "HOUR";		//시간
	public static final String FIELD_KEYWORD = "KEYWORD";	//추출 키워드
	public static final String FIELD_REGDATE = "REGDATE";	//등록시간
	public static final String FIELD_WEIGHT = "WEIGHT";		//검색 가중치
	public static final String FIELD_SENSE_KEYWORD = "SENSE_KEYWORD";	//감성 추츌 관련 원문 키워드
	public static final String FIELD_SENSE_KIND = "SENSE_KIND";	//감성 종류 (공포, 분노, 슬픔,지루함 , 통증, 협오, 중성, 기쁨, 흥미, 놀람)
	public static final String FIELD_SENSE_SCORE = "SENSE_SCORE";	//감성점수 (-1,0,1)
	public static final String FIELD_PNN = "PNN";	//긍부정중립 여부 (긍정,부정,중립)
	public static final String FIELD_EXT_STATION = "EXT_STATION";	//PLOT 추출 역명
	public static final String FIELD_VOC_CATEGORY = "VOC_CATEGORY";	//내부 VOC 민원(MINWON), 콜센터(CALL), SMS(SMS) 구분용
	// VOC용 출력 필드
	public static final String FIELD_VOCNO = "VOCNO";	//VOC ID
	public static final String FIELD_ID = "VOCNO";	//VOC ID
	public static final String FIELD_CDRECDEPT = "CDRECDEPT";	//접수부서코드
	public static final String FIELD_CDREPDEPT = "CDREPDEPT";	//답변부서코드
	public static final String FIELD_CDVOCCHANNEL = "CDVOCCHANNEL";	//민원접수채널 코드
	public static final String FIELD_CDVOCRECTYPE = "CDVOCRECTYPE";	//민원접수구분 코드
	public static final String FIELD_CDVOCKIND = "CDVOCKIND";	//민원접수유형 코드 ( 카테고리 대분류 )
	public static final String FIELD_CDVOCPART = "CDVOCPART";	//민원분야 ( 카테고리 중분류 )
	public static final String FIELD_CDVOCITEM = "CDVOCITEM";	//민원접수구분 코드 ( 카테고리 소분류 )
	public static final String FIELD_CDVOCPROCSTEP = "CDVOCPROCSTEP";	//답변부서코드
	public static final String FIELD_CDVOCREPLEVEL = "CDVOCREPLEVEL";	//답변만족도 등급
	public static final String FIELD_CNSL_GB = "CNSL_GB";	//상담구분(1234,5678, INTG)
	public static final String FIELD_COMSPOT = "COMSPOT";	//민원 발생 장소
	public static final String FIELD_LINE = "LINE";			//호선 01, 02, 03
	public static final String FIELD_REPCONT = "REPCONT";	//답변 내용
	public static final String FIELD_REPDATE = "REPDATE";	//수정일자(답변완료일자)
	public static final String FIELD_SEARCHCOUNT = "SEARCHCOUNT";	//조회 수
	// 소셜용 출력 필드
	public static final String FIELD_CHANNEL = "CHANNEL";	//수집 채널 (뉴스, 커뮤니티, SNS, 관계사이트)
	public static final String FIELD_GUBUN = "GUBUN";	//구분 (사이트 내 구분 있는 경우)
	public static final String FIELD_SITE = "SITE";	//수집 사이트명
	public static final String FIELD_URL = "URL";	//수집URL
	
	
	// 마리너 검색 색인 필드
	public static final String IDX_ALL = "ALL";	//전체 검색 용 필드
	public static final String IDX_DQ_DOCID = "DQ_DOCID";
	public static final String IDX_TITLE = "TITLE";
	public static final String IDX_CONTENT = "CONTENT";
	public static final String IDX_YEAR = "YEAR";
	public static final String IDX_HALF = "HALF";
	public static final String IDX_QUARTER = "QUARTER";
	public static final String IDX_MONTH = "MONTH";
	public static final String IDX_WEEK = "WEEK";
	public static final String IDX_DAY = "DAY";
	public static final String IDX_HOUR = "HOUR";
	public static final String IDX_KEYWORD = "KEYWORD";
	public static final String IDX_PNN = "PNN";	//긍정,부정,중립
	public static final String IDX_SENSE_KIND = "SENSE_KIND";	//감성 종류
	// VOC용 검색 색인 필드
	public static final String IDX_VOCNO = "VOCNO";	//VOC ID
	public static final String IDX_CDRECDEPT = "CDRECDEPT";	//접수부서코드
	public static final String IDX_CDREPDEPT = "CDREPDEPT";	//답변부서코드
	public static final String IDX_CDVOCCHANNEL = "CDVOCCHANNEL";	//민원접수채널 코드
	public static final String IDX_CDVOCRECTYPE = "CDVOCRECTYPE";	//민원접수구분 코드
	public static final String IDX_CDVOCKIND = "CDVOCKIND";	//민원접수유형 코드 ( 카테고리 대분류 )
	public static final String IDX_CDVOCPART = "CDVOCPART";	//민원분야 ( 카테고리 중분류 )
	public static final String IDX_CDVOCITEM = "CDVOCITEM";	//민원접수구분 코드 ( 카테고리 소분류 )
	public static final String IDX_CDVOCPROCSTEP = "CDVOCPROCSTEP";	//답변부서코드
	public static final String IDX_CDVOCREPLEVEL = "CDVOCREPLEVEL";	//답변만족도 등급
	public static final String IDX_CNSL_GB = "CNSL_GB";	//상담구분(1234,5678, INTG)
	public static final String IDX_COMSPOT = "COMSPOT";	//민원 발생 장소
	public static final String IDX_LINE = "LINE";			//호선 01, 02, 03
	public static final String IDX_REPCONT = "REPCONT";	//답변 내용
	public static final String IDX_REPDATE = "REPDATE";	//수정일자(답변완료일자)
	public static final String IDX_SEARCHCOUNT = "SEARCHCOUNT";	//조회 수
	public static final String IDX_VOC_CATEGORY = "VOC_CATEGORY";	//내부 VOC 민원(MINWON), 콜센터(CALL), SMS(SMS) 구분용
	public static final String IDX_EXT_STATION = "EXT_STATION";	//추출역명
	public static final String IDX_EXT_LINE = "EXT_LINE";	//추출역명의 호선 정보
	// 소셜용 검색 색인 필드
	public static final String IDX_CHANNEL = "CHANNEL";	//수집 채널 (뉴스, 커뮤니티, SNS, 관계사이트)
	public static final String IDX_GUBUN = "GUBUN";	//구분 (사이트 내 구분 있는 경우)
	public static final String IDX_SITE = "SITE";	//수집 사이트명
	public static final String IDX_URL = "URL";	//수집URL
	public static final String IDX_SENSITIVITY = "SENSITIVITY";
	
	// 마리너 검색용 정렬
	public static final String SORT_REGDATE = "REGDATE";
	
	// 마리너 검색용 필터
	public static final String FILTER_REGDATE = "REGDATE";	//등록시간
	public static final String FILTER_YEAR = "YEAR";
	public static final String FILTER_HALF = "HALF";
	public static final String FILTER_QUARTER = "QUARTER";
	public static final String FILTER_MONTH = "MONTH";
	public static final String FILTER_WEEK = "WEEK";
	public static final String FILTER_DAY = "DAY";
	public static final String FILTER_HOUR = "HOUR";
	public static final String FILTER_SENSE_SCORE = "SENSE_SCORE";	//감성점수 (-1,0,1)
	// VOC 용 검색 필터
	public static final String FILTER_CDVOCCHANNEL = "CDVOCCHANNEL";	//민원접수채널 코드
	public static final String FILTER_CDVOCRECTYPE = "CDVOCRECTYPE";	//민원접수구분 코드
	public static final String FILTER_CDVOCKIND = "CDVOCKIND";	//민원접수유형 코드 ( 카테고리 대분류 )
	public static final String FILTER_CDVOCPART = "CDVOCPART";	//민원분야 ( 카테고리 중분류 )
	public static final String FILTER_CDVOCITEM = "CDVOCITEM";	//민원접수구분 코드 ( 카테고리 소분류 )
	public static final String FILTER_CDVOCPROCSTEP = "CDVOCPROCSTEP";	//답변부서코드
	public static final String FILTER_CDVOCREPLEVEL = "CDVOCREPLEVEL";	//답변만족도 등급
	public static final String FILTER_CNSL_GB = "CNSL_GB";	//상담구분(1234,5678, INTG)
	public static final String FILTER_COMSPOT = "COMSPOT";	//민원 발생 장소
	public static final String FILTER_LINE = "LINE";			//호선 01, 02, 03
	public static final String FILTER_REPDATE = "REPDATE";	//수정일자(답변완료일자)
	// 소셜 용 검색 필터
	public static final String FILTER_CHANNEL = "CHANNEL";	//수집 채널 (뉴스, 커뮤니티, SNS, 관계사이트)
	public static final String FILTER_GUBUN = "GUBUN";	//구분 (사이트 내 구분 있는 경우)
	public static final String FILTER_SITE = "SITE";	//수집 사이트명
	
	// 마리너 검색용 그룹
	public static final String GROUP_YEAR = "YEAR";
	public static final String GROUP_HALF = "HALF";
	public static final String GROUP_QUARTER = "QUARTER";
	public static final String GROUP_MONTH = "MONTH";
	public static final String GROUP_WEEK = "WEEK";
	public static final String GROUP_DAY = "DAY";
	public static final String GROUP_HOUR = "HOUR";
	public static final String GROUP_KEYWORD = "KEYWORD";
	public static final String GROUP_SENSE_KEYWORD = "SENSE_KEYWORD";
	public static final String GROUP_SENSE_KIND = "SENSE_KIND";
	public static final String GROUP_SENSE_SCORE = "SENSE_SCORE";
	public static final String GROUP_PNN = "PNN";
	// VOC용 그룹
	public static final String GROUP_CDRECDEPT = "CDRECDEPT";	//접수부서코드
	public static final String GROUP_CDREPDEPT = "CDREPDEPT";	//답변부서코드
	public static final String GROUP_CDVOCCHANNEL = "CDVOCCHANNEL";	//민원접수채널 코드
	public static final String GROUP_CDVOCRECTYPE = "CDVOCRECTYPE";	//민원접수구분 코드
	public static final String GROUP_CDVOCKIND = "CDVOCKIND";	//민원접수유형 코드 ( 카테고리 대분류 )
	public static final String GROUP_CDVOCPART = "CDVOCPART";	//민원분야 ( 카테고리 중분류 )
	public static final String GROUP_CDVOCITEM = "CDVOCITEM";	//민원접수구분 코드 ( 카테고리 소분류 )
	public static final String GROUP_CDVOCPROCSTEP = "CDVOCPROCSTEP";	//답변부서코드
	public static final String GROUP_CDVOCREPLEVEL = "CDVOCREPLEVEL";	//답변만족도 등급
	public static final String GROUP_CNSL_GB = "CNSL_GB";	//상담구분(1234,5678, INTG)
	public static final String GROUP_LINE = "LINE";			//호선 01, 02, 03
	public static final String GROUP_REPCONT = "REPCONT";	//답변 내용
	public static final String GROUP_REPDATE = "REPDATE";	//수정일자(답변완료일자)
	public static final String GROUP_SEARCHCOUNT = "SEARCHCOUNT";	//조회 수
	public static final String GROUP_VOCKIND = "VOCKIND";	//VOC 대 분류
	public static final String GROUP_VOCPART = "VOCPART";	//VOC 중 분류
	public static final String GROUP_VOCITEM = "VOCITEM";	//VOC 소 분류
	public static final String GROUP_EXT_STATION = "EXT_STATION";	//PLOT 추출 역명
	// 소셜용 그룹
	public static final String GROUP_CHANNEL = "CHANNEL";	//수집 채널 (뉴스, 커뮤니티, SNS, 관계사이트)
	public static final String GROUP_SITE = "SITE";	//수집 사이트명
	
	// 코드 정보 조회용
	/*public static final String CODE_KIND_CATEGORY="CATEGORY";
	public static final String CODE_KIND_GU_AREA="AREA";*/
	
	//서울메트로 부서정보 조회 후 Map저장
	public HashMap<String, String> setMetroDeptList() throws Exception{
		HashMap<String, String> returnMap = new HashMap<>();
		List<CommonSelectBoxVo> deptList = selectBoxService.metroDeptList();
		for(CommonSelectBoxVo deptInfo: deptList){
			String name = deptInfo.getName();
			String code = deptInfo.getCode();
			if(code != null && !"".equals(code.trim())){
				if(name != null && !"".equals(name.trim())){
					returnMap.put(code.trim(), name.trim());
				}
			}
		}
		return returnMap;
	}
	
	//서울메트로 VOC 답변만족도 등급 리스트 Map저장
	public static HashMap<String, String> setVocRepLevelList(){
		HashMap<String, String> returnMap = new HashMap<>();
		returnMap.put("1", "매우만족");
		returnMap.put("2", "만족");
		returnMap.put("3", "보통");
		returnMap.put("4", "불만족");
		returnMap.put("5", "매우불만족");
		return returnMap;
	}
}
