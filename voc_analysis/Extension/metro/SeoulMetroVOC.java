package m4.extension.seoulmetro;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.diquest.ir.dbwatcher.DbWatcher;
import com.diquest.ir.dbwatcher.DbWatcherExtension;
import com.diquest.ir.dbwatcher.dbcolumn.DbColumnValue;
import com.diquest.ir.dbwatcher.mapper.FieldMapper;
import com.diquest.jiana.core.JianaConst;
import com.diquest.jiana.morph.CottonBuffer;
import com.diquest.jiana.morph.JianaCotton;
import com.diquest.plot.result.PLOTResult;

import com.diquest.disa.DISA;
import com.diquest.disa.result.DisaSenResult;
import com.diquest.disa.result.NeEntity;

public class SeoulMetroVOC implements DbWatcherExtension {
	
	private DbWatcher watcher;
	private Connection con;
	private Map<String, Object> pstmtMap;
	
	private static String DISA_HOME = "./resources/disa/dcd";
	private static String PLOT_HOME = "./resources/plot/dcd";
	private static String JIANA_HOME = "./resources/jiana/dcd";
	
	private static DISA disaAnalyzer = null;
	private static JianaCotton jianaCotton = null;
	private String disaCategory = "";	//SEOULMETRO_MINWON, SUBWAY_STATION
	private static final String senseCategoryName = "SEOULMETRO_MINWON";
	private static final String stationCategoryName = "SUBWAY_STATION";
	
	private static ArrayList<String> pnnList = new ArrayList<String>();	//긍정,중집,부정
	private static ArrayList<String> senseKindList = new ArrayList<String>();	//감정범주
	private static ArrayList<String> senseScoreList = new ArrayList<String>();	//감성 점수
	private static ArrayList<String> disaOriginalKeywordList = new ArrayList<String>();	//Disa로 추출한 키워드의 원본 리스트
	
	private static HashMap<String, String[]> senseScoreMap = null;	//감성점수 저장용 맵
	
	private static ArrayList<String> disaErrorList = new ArrayList<String>();	//Disa 감성추출된 원문 위치 오류 리스트
	
	private static String station_query = "SELECT STATION_NAME, GROUP_CONCAT(LINE separator ';') FROM voc.STATION_INFO WHERE LINE IN ('01','02','03','04','05','06','07','08') GROUP BY	STATION_NAME";
	private static HashMap<String, String> stationTable = null;
	private static String ext_staion_name = "";	//추출 역명
	
	private static String getIR4Home() {	//get Mariner Env
		String propHome = System.getProperty("IR4_HOME");
		if (propHome != null && propHome.trim().length() != 0) {
				 return propHome;
		} else {
				 if ("1.4".equals(System.getProperty("java.specification.version"))) {
						throw new RuntimeException("Cannot read property : IR4_HOME");
				 } else {
						Object envHome = null;
						try {
							  Method method = System.class.getMethod("getenv", new Class[] { String.class });
							  envHome = method.invoke(null, new Object[] { "IR4_HOME" });
						} catch (IllegalArgumentException e) {
							 throw new RuntimeException("Cannot read property : IR4_HOME", e);
						} catch (IllegalAccessException e) {
							 throw new RuntimeException("Cannot read property : IR4_HOME", e);
						} catch (InvocationTargetException e) {
							 throw new RuntimeException("Cannot read property : IR4_HOME", e);
						} catch (SecurityException e) {
							 throw new RuntimeException("Cannot read property : IR4_HOME", e);
						} catch (NoSuchMethodException e) {
							 throw new RuntimeException("Cannot read property : IR4_HOME", e);
						}
						if (envHome != null && envHome instanceof String) {
							  propHome = (String) envHome;
							  return propHome;
						} else {
							  throw new RuntimeException("Cannot read property : IR4_HOME");
						}
				 }
		  }
	}
	
	private static HashMap<String, String[]> senseScoreMapping(HashMap<String, String[]> map){
		/*
		감정분류	개체명	결정코드
		공포	$EMO_FEAR	-1
		분노	$EMO_ANGRY	-2
		슬픔	$EMO_SAD	-3
		지루함	$EMO_BORING	-4
		통증	$EMO_PAIN	-5
		혐오	$EMO_HATE	-6
		중성	$EMO_NEUTRALITY	0
		기쁨	$EMO_HAPPY	1
		흥미	$EMO_INTEREST	2
		놀람	$EMO_SURPRISE	3
		 */
		map = new HashMap<String, String[]>();
		map.put("$EMO_FEAR", new String[]{"공포", "-1", "부정"});
		map.put("$EMO_ANGRY", new String[]{"분노", "-2", "부정"});
		map.put("$EMO_SAD", new String[]{"슬픔", "-3", "부정"});
		map.put("$EMO_BORING", new String[]{"지루함", "-4", "부정"});
		map.put("$EMO_PAIN", new String[]{"통증", "-5", "부정"});
		map.put("$EMO_HATE", new String[]{"혐오", "-6", "부정"});
		map.put("$EMO_NEUTRALITY", new String[]{"중성", "0", "중립"});
		map.put("$EMO_HAPPY", new String[]{"기쁨", "1", "긍정"});
		map.put("$EMO_INTEREST", new String[]{"흥미", "2", "긍정"});
		map.put("$EMO_SURPRISE", new String[]{"놀람", "3", "긍정"});
		return map;
	}
	//DB에서 역이름 및 호선 정보 조회
	public void getStationList() throws SQLException {
		PreparedStatement pstmtSelect = null;
		ResultSet rs = null;
		stationTable = new HashMap<>();
		try {
			pstmtSelect = con.prepareStatement(station_query);
			rs = pstmtSelect.executeQuery();
			while(rs.next()) {
				HashMap<String, String>tempMap = new HashMap<String, String>();
				tempMap.put("STATION_NAME", rs.getString("STATION_NAME").trim());
				tempMap.put("LINE", rs.getString("LINE").trim());
				stationTable.put(rs.getString("STATION_NAME").trim(), rs.getString("LINE").trim());
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				rs.close();
			}
			if(pstmtSelect != null) {
				pstmtSelect.close();
			}
		}
	}
	public void start(DbWatcher watcher) throws SQLException {
		this.watcher = watcher;
		this.con = null;
		String ir4_home = getIR4Home();
		DISA_HOME = ir4_home + "/resources/disa/dcd";
		JIANA_HOME = ir4_home + "/resources/korean/data";
		PLOT_HOME = ir4_home + "/resources/plot/dcd";
		senseScoreMap = senseScoreMapping(senseScoreMap);
		try {
			con = watcher.createConnection();
			disaAnalyzer = new DISA();
			disaCategory = senseCategoryName;
			disaAnalyzer.init(DISA_HOME, PLOT_HOME, JIANA_HOME, disaCategory);
			jianaCotton = new JianaCotton();
			jianaCotton.init2(JIANA_HOME, JianaConst.CN_FLAG);
			getStationList();
			System.out.println("Extension start : " + new java.util.Date().toString());

		} catch (SQLException e) {
			try {
				if (con != null) {
					watcher.releaseConnection(con);
					con = null;
					disaAnalyzer.fine();
					jianaCotton.fine();
				}
			} catch (SQLException e2) {
			}

			System.out.println("start ()::::::" + e.toString());

			throw e;
		}
	}
	
	public void stop() throws SQLException {
		try{
			if (con != null) {
				watcher.releaseConnection(con);
				con = null;
			}
			if(disaAnalyzer != null) disaAnalyzer.fine();
			if(jianaCotton != null) jianaCotton.fine();
			System.out.println("Extension end : " + new java.util.Date().toString());
			if(disaErrorList.size() > 0){
				System.out.println("Disa Error List");
				System.out.println("-----------------------");
				for(String id : disaErrorList){
					System.out.println(id);
				}
				System.out.println("-----------------------");	
			}
		}
		catch(Exception e){
			e.printStackTrace();;
		}
	}

	private static int getIndexOf(String name, DbColumnValue[] columnValue) throws SQLException {
		for (int i = 0; i < columnValue.length; i++) {
			if (name.equals(columnValue[i].getName()))
				return i;
		}
		throw new SQLException("DBWatcherExtension : " + name + " 컬럼이 SELECT 되지 않았습니다.");
	}
	
	public FieldMapper getMapper(String fieldName, DbColumnValue[] columnValue) throws SQLException {
		if (fieldName.equals("YEAR")) {
			return new YEARMapper(columnValue);
		}
		else if (fieldName.equals("HALF")) {
			return new MntHalfMapper(columnValue);
		}
		else if (fieldName.equals("QUARTER")) {
			return new MntQuarterMapper(columnValue);
		}
		else if (fieldName.equals("MONTH")) {
			return new MntMonthMapper(columnValue);
		}
		else if (fieldName.equals("WEEK")) {
			return new MntWeekMapper(columnValue);
		}
		else if (fieldName.equals("DAY")) {
			return new MntDayMapper(columnValue);
		}
		else if (fieldName.equals("HOUR")) {
			return new MntHourMapper(columnValue);
		}
		else if (fieldName.equals("DAYOFWEEK")) {
			return new DayOfWeekMapper(columnValue);
		}
		else if (fieldName.equals("EXT_STATION")) {
			return new StationMapper(columnValue);
		} 
		//키워드 추출을 JIANA가 아닌 dramaDocumentExpander를 사용하도록 변경 2017.11.06
		/*else if (fieldName.equals("KEYWORD")) {
			return new JianaKeywordMapper(columnValue);
		} */
		else if (fieldName.equals("PNN")) {
			return new PnnMapper(columnValue);
		} 
		else if (fieldName.equals("SENSE_KIND")) {
			return new SenseKindMapper(columnValue);
		} 
		else if (fieldName.equals("SENSE_SCORE")) {
			return new SenseScoreMapper(columnValue);
		} 
		else if (fieldName.equals("SENSE_KEYWORD")) {
			return new SenseKeywordMapper(columnValue);
		}
		else if (fieldName.equals("EXT_LINE")) {	//추출한 역명에 대한 호선 정보 매칭
			return new ExtLineMapper(columnValue);
		} 
		return null;
	}
	
	private static class YEARMapper extends FieldMapper {
		private final int REGDATE;

		public YEARMapper(DbColumnValue[] columnValue) throws SQLException {
			super("YEAR");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {

			String regdate = value[REGDATE].getString();
			String mnt_Year = null;

			if (regdate != null && regdate.length() >= 4) {
				mnt_Year = regdate.substring(0, 4);
			} else {
				mnt_Year = "";
			}
			return mnt_Year;
		}
	}

	private static class MntHalfMapper extends FieldMapper {
		private final int REGDATE;

		public MntHalfMapper(DbColumnValue[] columnValue) throws SQLException {
			super("HALF");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Half = null;
			try {
				if (regdate != null && regdate.length() >= 6) {
					mnt_Year = regdate.substring(0, 4);

					int half = 0;
					half = (int) Math.ceil(Integer.parseInt(regdate.substring(4, 6)) / 6.0);

					mnt_Half = mnt_Year + "0" + half;
				} else {
					mnt_Half = "";
				}
			} catch (Exception e) {
				System.out.println("regdate : " + regdate);
				e.printStackTrace();

			}

			return mnt_Half;
		}
	}

	private static class MntQuarterMapper extends FieldMapper {
		private final int REGDATE;
		
		public MntQuarterMapper(DbColumnValue[] columnValue) throws SQLException {
			super("QUARTER");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Quarter = null;

			if (regdate != null && regdate.length() >= 6) {
				mnt_Year = regdate.substring(0, 4);

				int quarter = 0;
				quarter = (int) Math.ceil(Integer.parseInt(regdate.substring(4, 6)) / 3.0);

				mnt_Quarter = mnt_Year + "0" + quarter;
			} else {
				mnt_Quarter = "";
			}
			return mnt_Quarter;
		}
	}

	private static class MntMonthMapper extends FieldMapper {
		private final int REGDATE;
		
		public MntMonthMapper(DbColumnValue[] columnValue) throws SQLException {
			super("MONTH");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Month = null;

			if (regdate != null && regdate.length() >= 6) {
				mnt_Year = regdate.substring(0, 4);
				mnt_Month = mnt_Year + regdate.substring(4, 6);
			} else {
				mnt_Month = "";
			}

			return mnt_Month;
		}
	}

	private static class MntWeekMapper extends FieldMapper {
		private final int REGDATE;
		
		public MntWeekMapper(DbColumnValue[] columnValue) throws SQLException {
			super("WEEK");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String tmp_Year = null;
			String tmp_Month = null;
			String tmp_Day = null;

			String mnt_Year = null;
			String mnt_Month = null;
			String mnt_Week = null;

			if (regdate != null && regdate.length() >= 8) {
				tmp_Year = regdate.substring(0, 4);
				tmp_Month = regdate.substring(4, 6);
				tmp_Day = regdate.substring(6, 8);

				Calendar SCal = Calendar.getInstance();
				SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month) - 1, Integer.parseInt(tmp_Day));

				mnt_Year = tmp_Year;
				mnt_Month = tmp_Month;

				int week = 0;
				SCal.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
				SCal.setMinimalDaysInFirstWeek(7);
				week = SCal.get(java.util.Calendar.WEEK_OF_MONTH);

				if (week == 0) {
					int tmpMonth = Integer.parseInt(tmp_Month) - 1;
					if (tmpMonth >= 10) {
						mnt_Month = "" + tmpMonth;
					} else {
						if (tmpMonth == 0) {
							mnt_Month = "12";
							mnt_Year = "" + (Integer.parseInt(mnt_Year) - 1);
						} else {
							mnt_Month = "0" + tmpMonth;
						}
					}

					SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month) - 2, Integer.parseInt(tmp_Day));
					int endDay = SCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
					SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month) - 2, endDay);
					SCal.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
					SCal.setMinimalDaysInFirstWeek(7);
					week = SCal.get(java.util.Calendar.WEEK_OF_MONTH);
				}
				mnt_Week = mnt_Year + mnt_Month + "0" + week;
			} else {
				mnt_Week = "";
			}
			return mnt_Week;
		}
	}

	private static class MntDayMapper extends FieldMapper {
		private final int REGDATE;
		
		public MntDayMapper(DbColumnValue[] columnValue) throws SQLException {
			super("DAY");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Month = null;
			String mnt_Day = null;

			if (regdate != null && regdate.length() >= 8) {
				mnt_Year = regdate.substring(0, 4);
				mnt_Month = regdate.substring(4, 6);
				mnt_Day = mnt_Year + mnt_Month + regdate.substring(6, 8);
			} else {
				mnt_Day = "";
			}
			return mnt_Day;
		}
	}

	private static class MntHourMapper extends FieldMapper {
		private final int REGDATE;
		
		public MntHourMapper(DbColumnValue[] columnValue) throws SQLException {
			super("HOUR");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Month = null;
			String mnt_Day = null;
			String mnt_Hour = null;

			if (regdate != null && regdate.length() >= 10) {
				mnt_Year = regdate.substring(0, 4);
				mnt_Month = regdate.substring(4, 6);
				mnt_Day = regdate.substring(6, 8);
				mnt_Hour = mnt_Year + mnt_Month + mnt_Day + regdate.substring(8, 10);
			} else {
				mnt_Hour = "";
			}
			return mnt_Hour;
		}
	}

	private static class DayOfWeekMapper extends FieldMapper {
		private final int REGDATE;
		
		public DayOfWeekMapper(DbColumnValue[] columnValue) throws SQLException {
			super("DAYOFWEEK");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String regdate = value[REGDATE].getString();
			String tmp_Year = null;
			String tmp_Month = null;
			String tmp_Day = null;

			String mnt_DayOfWeek = null;

			if (regdate != null && regdate.length() >= 8) {
				tmp_Year = regdate.substring(0, 4);
				tmp_Month = regdate.substring(4, 6);
				tmp_Day = regdate.substring(6, 8);

				Calendar SCal = Calendar.getInstance();
				SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month) - 1, Integer.parseInt(tmp_Day));

				int dayofweek = 0;
				dayofweek = SCal.get(java.util.Calendar.DAY_OF_WEEK);

				mnt_DayOfWeek = Integer.toString(dayofweek);
			} else {
				mnt_DayOfWeek = "";
			}
			return mnt_DayOfWeek;
		}
	}
	
	private static class StationMapper extends FieldMapper {
		private final int CONTENT;

		public StationMapper(DbColumnValue[] columnValue) throws SQLException {
			super("EXT_STATION");
			CONTENT = getIndexOf("CONTENT", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			ext_staion_name = "";
			String content = value[CONTENT].getString();
			disaAnalyzer.setCategory(stationCategoryName);
			String[] sentenceArr = content.split(System.getProperty("line.separator"));
			ArrayList<String> stationList = new ArrayList<String>();
			for(String sentence : sentenceArr){
				char[] contentArray = sentence.toCharArray();
				List<DisaSenResult> resultList = disaAnalyzer.analyze(contentArray);
				DisaSenResult disaResult = resultList.get(0);
				for(int i=0; i < disaResult.getEntitySize();i++){
					NeEntity entity = disaResult.getEntity(i);
					if(entity.getType().equals("$STATION")){
						stationList.add(entity.lexical);
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			for(String station : stationList){
				sb.append(station + ";");
			}
			String stationString = sb.toString();
			ext_staion_name = stationString;
			if("".equals(stationString)) stationString = "NONE";
			return stationString;
		}
	}
	
	private static class ExtLineMapper extends FieldMapper {
		public ExtLineMapper(DbColumnValue[] columnValue) throws SQLException {
			super("EXT_LINE");
		}

		public String mapping(DbColumnValue[] value) {
			String station_name = ext_staion_name;
			StringBuffer lineSb = new StringBuffer();
			if(station_name != null && !"".equals(station_name)){
				String[] stationArr = station_name.split(";");
				for(String stationName : stationArr){
					if(stationName.charAt(stationName.length() -1) == '역' ){
						stationName = stationName.substring(0, stationName.length() -1 );
					}
					if(stationTable.get(stationName) != null){
						lineSb.append(stationTable.get(stationName) + ";");
					}
				}
			}
			else{
				lineSb.append("NONE");
			}
			String lineStr = lineSb.toString();
			return lineStr;
		}
	}
	
	private static class PnnMapper extends FieldMapper {
		private final int CONTENT;
		private final int DQ_DOCID;
		public PnnMapper(DbColumnValue[] columnValue) throws SQLException {
			super("PNN");
			CONTENT = getIndexOf("CONTENT", columnValue);
			DQ_DOCID = getIndexOf("DQ_DOCID", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String content = value[CONTENT].getString();
			String id = value[DQ_DOCID].getString();
			String[] sentenceArr = content.split(System.getProperty("line.separator"));
			disaAnalyzer.setCategory(senseCategoryName);
			pnnList.clear();
			senseKindList.clear();
			senseScoreList.clear();
			disaOriginalKeywordList.clear();
			sentenceArr = new String[]{ content };
			for(String sentence : sentenceArr){
				//sentence = sentence.concat("\r\n");
				//System.out.println("sentence : \"" + sentence +"\"");
				char[] contentArray = sentence.toCharArray();
				List<DisaSenResult> resultList = disaAnalyzer.analyze(contentArray);
				DisaSenResult disaResult = resultList.get(0);
				PLOTResult resultPLOT = disaAnalyzer.getPLOTResult();
				
				for(int i=0; i < disaResult.getEntitySize();i++){
					NeEntity entity = disaResult.getEntity(i);
					try{
						if(senseScoreMap.get(entity.getType()) != null){
							String[] selectSense = senseScoreMap.get(entity.getType());
							senseKindList.add(selectSense[0]);
							senseScoreList.add(selectSense[1]);
							pnnList.add(selectSense[2]);
							disaOriginalKeywordList.add(sentence.substring(entity.getStart(), entity.getEnd()));
						}
					}
					catch(Exception e){
						System.out.println("Disa Error DQ_DOCID : " + id);
						System.out.println("sentence : \"" + sentence + "\"");
						System.out.println("entity.lexical : \"" + entity.lexical + "\"");
						System.out.println("entity.getStart() : \"" + entity.getStart() + "\"");
						System.out.println("entity.getEnd() : \"" + entity.getEnd() + "\"");
						System.out.println("sentence.length() : \"" + sentence.length() + "\"");
						disaErrorList.add(id);
						e.printStackTrace();
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			for(String pnn : pnnList){
				sb.append(pnn + ";");
			}
			return sb.toString();
		}
	}
	
	private static class JianaKeywordMapper extends FieldMapper {
		private final int CONTENT;

		public JianaKeywordMapper(DbColumnValue[] columnValue) throws SQLException {
			super("KEYWORD");
			CONTENT = getIndexOf("CONTENT", columnValue);
		}

		public String mapping(DbColumnValue[] value) {
			String content = value[CONTENT].getString();
			CottonBuffer cb = new CottonBuffer();
			
			
			cb.init(content.toCharArray());
			jianaCotton.analyze(cb);
			ArrayList<String> jianaKeywordList = new ArrayList<String>();	//Jiana 로 추출한 키워드 리스트
			
			for(int i=0; i < cb.nTerm; i++){
				if(cb.termTag[i] == 1){	//명사일 경우
					String extractWord = new String(cb.input,cb.termStart[i],cb.termLength[i]);
					if(extractWord.length() > 1 && extractWord.matches("[a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")){	//한 글자 이하, 숫자와 특수문자 로만 이루어진 단어 제외
						jianaKeywordList.add(extractWord);	
					}
				}
			}				
			cb = null;
			
			StringBuffer sb = new StringBuffer();
			//분석 키워드에 감성추출 원본키워드 추가
			for(String disaKeyword: disaOriginalKeywordList){
				sb.append(disaKeyword + ";");
			}
			for(String keyword: jianaKeywordList){
				sb.append(keyword + ";");
			}
			
			return sb.toString();
		}
	}
	
	private static class SenseKindMapper extends FieldMapper {
		public SenseKindMapper(DbColumnValue[] columnValue) throws SQLException {
			super("SENSE_KIND");
		}

		public String mapping(DbColumnValue[] value) {
			StringBuffer sb = new StringBuffer();
			for(String sense : senseKindList){
				sb.append(sense + ";");
			}
			return sb.toString();
		}
	}
	
	private static class SenseScoreMapper extends FieldMapper {
		public SenseScoreMapper(DbColumnValue[] columnValue) throws SQLException {
			super("SENSE_SCORE");
		}

		public String mapping(DbColumnValue[] value) {
			StringBuffer sb = new StringBuffer();
			for(String score : senseScoreList){
				sb.append(score + ";");
			}
			return sb.toString();
		}
	}
	
	private static class SenseKeywordMapper extends FieldMapper {
		public SenseKeywordMapper(DbColumnValue[] columnValue) throws SQLException {
			super("SENSE_KEYWORD");
		}

		public String mapping(DbColumnValue[] value) {
			StringBuffer sb = new StringBuffer();
			for(String kwd : disaOriginalKeywordList){
				if(kwd != null && !"".equals(kwd)) sb.append(kwd + ";");
			}
			return sb.toString();
		}
	}
	
	
	public static void main(String[] args){
		String aaa = "1113240924";
		String bbb= "4-3러하";
		
		if(aaa.matches("[0-9]*")) System.out.println("aaaa");
		if(bbb.matches("[a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) System.out.println("bbbb");
		
		senseScoreMap = senseScoreMapping(senseScoreMap);
		System.out.println(senseScoreMap);
		
		String cc = "신도림역";
		String dd = "동대문역사공원";
		System.out.println(cc.charAt(cc.length()-1));
		System.err.println(cc.substring(0, cc.length() -1));
	}
}
