import java.sql.SQLException;
import java.util.Calendar;

import com.diquest.ir.dbwatcher.DbWatcher;
import com.diquest.ir.dbwatcher.DbWatcherExtension;
import com.diquest.ir.dbwatcher.dbcolumn.DbColumnValue;
import com.diquest.ir.dbwatcher.mapper.FieldMapper;
import com.diquest.disa.DISA;

import com.diquest.disa.result.DisaSenResult;
import com.diquest.disa.result.NeEntity;
import java.util.List;

public class Extension implements DbWatcherExtension {
	
	private static String ip = "localhost";
//	private static String collectionId = "COMPLAIN";
//	private static String categoriId = "CATEGORY";
	private static String option = "2";
	public static String category = "국방부";
	public static String category2 = "서울시민원데이터";
	
	private static int port = 15555;
	private static int resultSize = 2;
	public static DISA analyzer = new DISA();
	
	public static String dicFolderJiana = "/home/diquest2/mariner3/resources/jiana/dcd";
	public static String dicFolderPLOT = "/home/diquest2/mariner3/resources/plot/dcd";
	public static String dicFolderDISA = "/home/diquest2/mariner3/resources/disa/dcd";
	
	@Override
	public void start(DbWatcher watcher) throws SQLException {
		//dicFolderDisa,dicFolderPLOT,dicFolderJiana,
		this.analyzer.init(dicFolderDISA,dicFolderPLOT,dicFolderJiana, category);

		
	}

	@Override
	public void stop() throws SQLException {

	}
	
	@Override
	public FieldMapper getMapper(String fieldName, DbColumnValue[] columnValue) throws SQLException {
		if (fieldName.equals("YEAR")) {
			return new YEARMapper(columnValue);
		}else if (fieldName.equals("HALF")) {
			return new MntHalfMapper(columnValue);
		}else if (fieldName.equals("QUARTER")) {
			return new MntQuarterMapper(columnValue);
		}else if (fieldName.equals("MONTH")) {
			return new MntMonthMapper(columnValue);
		}else if (fieldName.equals("WEEK")) {
			return new MntWeekMapper(columnValue);
		}else if (fieldName.equals("DAY")) {
			return new MntDayMapper(columnValue);
		}else if (fieldName.equals("HOUR")) {
			return new MntHourMapper(columnValue);
		}
		/*else if (fieldName.equals("SENSITIVITY") || fieldName.equals("SEN_P_KEYWORD") || fieldName.equals("SEN_N_KEYWORD")) {
			return new KeywordMapper(columnValue, fieldName);
		}*/
		else if (fieldName.equals("SENSITIVITY")) {
			return new KeywordMapper(columnValue);
		}
		else if (fieldName.equals("CHAR_KEYWORD")) {
			return new SenKeywordMapper(columnValue);
		}
		
		return null;
	}

	private static int getIndexOf(String name, DbColumnValue[] columnValue) throws SQLException {
		for (int i=0; i < columnValue.length; i++) {
			if (name.equals(columnValue[i].getName()))
				return i;
		}
		throw new SQLException("DBWatcherExtension : " + name + " :alivekim ");
	}
	
	private static class YEARMapper extends FieldMapper {
		private final int REGDATE;
		
		public YEARMapper(DbColumnValue[] columnValue) throws SQLException {
			super("YEAR");
			REGDATE = getIndexOf("REGDATE", columnValue);
		}
		public String mapping(DbColumnValue[] value) {
			
			String pubdate = value[REGDATE].getString();
			String mnt_Year = null;

			if(pubdate!=null && pubdate.length()>=4){	
				mnt_Year = pubdate.substring(0, 4);
			}else {
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
			
			String pubdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Half = null;
			
			if(pubdate!=null && pubdate.length()>=6){
				mnt_Year = pubdate.substring(0,4);
			
				int half = 0;
				half = (int)Math.ceil(Integer.parseInt(pubdate.substring(4, 6))/6.0);
				
				mnt_Half = mnt_Year+"0"+half;
			}else{
				mnt_Half = "";
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
			
			String pubdate = value[REGDATE].getString();
					 
			String mnt_Year = null;
			String mnt_Quarter = null;
			
			if(pubdate!=null && pubdate.length()>=6){
				mnt_Year = pubdate.substring(0, 4);
			
				int quarter = 0;
				quarter = (int)Math.ceil(Integer.parseInt(pubdate.substring(4, 6))/3.0);
			
				mnt_Quarter = mnt_Year+"0"+quarter;
			}else{
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
			
			String pubdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Month = null;
		
			if(pubdate!=null && pubdate.length()>=6){	
				mnt_Year = pubdate.substring(0, 4);
				mnt_Month = mnt_Year+pubdate.substring(4, 6);
			}else{
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
			
			String pubdate = value[REGDATE].getString();
			String tmp_Year = null;
			String tmp_Month = null;
			String tmp_Day = null;

			String mnt_Year = null;
			String mnt_Month = null;
			String mnt_Week = null;

			if(pubdate!=null && pubdate.length()>=8){
				tmp_Year = pubdate.substring(0, 4);
				tmp_Month = pubdate.substring(4, 6);
				tmp_Day = pubdate.substring(6, 8);
			
				Calendar SCal = Calendar.getInstance();
				SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month)-1, Integer.parseInt(tmp_Day));
			
				mnt_Year = tmp_Year;
				mnt_Month = tmp_Month;			
		
				int week = 0;
				SCal.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
				SCal.setMinimalDaysInFirstWeek(7);
				week = SCal.get(java.util.Calendar.WEEK_OF_MONTH);
				
				if (week == 0)
				{
					int tmpMonth = Integer.parseInt(tmp_Month)-1;
					if (tmpMonth >= 10) {
						mnt_Month = "" + tmpMonth;
					} else {
						if (tmpMonth == 0) {
							mnt_Month = "12";
							mnt_Year = "" + (Integer.parseInt(mnt_Year)-1);
						}else {
							mnt_Month = "0" + tmpMonth;
						}
					}
					
					SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month)-2, Integer.parseInt(tmp_Day));
					int endDay = SCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
					SCal.set(Integer.parseInt(tmp_Year), Integer.parseInt(tmp_Month)-2, endDay);
					SCal.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
					SCal.setMinimalDaysInFirstWeek(7);
					week = SCal.get(java.util.Calendar.WEEK_OF_MONTH);
				}
				mnt_Week = mnt_Year+mnt_Month+"0"+week;
			}else{
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
			
			String pubdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Month = null;
			String mnt_Day = null;

			if(pubdate!= null && pubdate.length()>=8){
				mnt_Year = pubdate.substring(0, 4);
				mnt_Month = pubdate.substring(4, 6);
				mnt_Day = mnt_Year+mnt_Month+pubdate.substring(6, 8);
			}else{
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
			
			String pubdate = value[REGDATE].getString();
			String mnt_Year = null;
			String mnt_Month = null;
			String mnt_Day = null;
			String mnt_Hour = null;
			
			if(pubdate!=null && pubdate.length()>=10){
				mnt_Year = pubdate.substring(0, 4);
				mnt_Month = pubdate.substring(4, 6);
				mnt_Day = pubdate.substring(6, 8);
				mnt_Hour = mnt_Year+mnt_Month+mnt_Day+pubdate.substring(8, 10);
			}else{
				mnt_Hour = "";
			}			
			return mnt_Hour;
		}
	}
	
	private static class KeywordMapper extends FieldMapper {
		private final int RECOGNITION_TEXT;
		
		public KeywordMapper(DbColumnValue[] columnValue) throws SQLException {
			
			super("SENSITIVITY");
			RECOGNITION_TEXT = getIndexOf("RECOGNITION_TEXT", columnValue);
		}
		public String mapping(DbColumnValue[] value) {
			analyzer.setCategory(category);
			String contents = value[RECOGNITION_TEXT].getString();
			String sensirivity = "";
			int p = 0;
			int n = 0;
			String posKeyword = "";
			String negKeyword = "";
			//String senKeyword = "";
			
			char[] contentArray = contents.toCharArray();
			List<DisaSenResult> resultList = analyzer.analyze(contentArray);
		
			DisaSenResult disaSenResult = resultList.get(0);
			
			for(int i=0; i<disaSenResult.getEntitySize(); i++){
				NeEntity entity = disaSenResult.getEntity(i);
				/*if (	entity.type.equals ("$EMO_SAD") || entity.type.equals("$EMO_BORING") ||  
						entity.type.equals ("$EMO_PAIN") || entity.type.equals("$EMO_HATE") || 
						entity.type.equals ("$EMO_NEUTRALITY") || entity.type.equals("$EMO_HAPPY") || 
						entity.type.equals ("$EMO_INTEREST") || entity.type.equals("$EMO_SURPRISE") ) {
					
					System.out.println(entity.type + " - " + entity.lexical);
		
				}
				else */
				
				if (entity.type.equals ("$EMO_POSITIVE")) {	
					System.out.println(entity.type + " - " + entity.lexical);
					p++;
				}else if(entity.type.equals("$EMO_NEGATIVE")){
					System.out.println(entity.type + " - " + entity.lexical);
					n++;
				}
				
				
				/*if(entity.type.equals("$MINWON_SUBJECT")){
				//	System.out.println(entity.type + " - " + entity.lexical);
					senKeyword = entity.lexical + ",";
					
				}*/

			}
			
			if(p > n){
				sensirivity = "P";	// 긍정
			}else if(p < n){
				sensirivity = "D";	// 부정
			}else{
				sensirivity = "N";	// 중립
			}

			/*if(fieldName.equals("SENSITIVITY")){
				return sensirivity;
			}else if(fieldName.equals("SEN_P_KEYWORD")){			
				if(sensirivity.equals("P")){
					return posKeyword;
				}
			}else if(fieldName.equals("SEN_N_KEYWORD")){				
				if(sensirivity.equals("N")){
					return negKeyword;
				}
			}*/
			
			
			return sensirivity;
		}				
	}	
	private static class SenKeywordMapper extends FieldMapper {
		private final int RECOGNITION_TEXT;
		
		public SenKeywordMapper(DbColumnValue[] columnValue) throws SQLException {
			
			super("CHAR_KEYWORD");
			RECOGNITION_TEXT = getIndexOf("RECOGNITION_TEXT", columnValue);
		}
		public String mapping(DbColumnValue[] value) {
			analyzer.setCategory(category2);
			String contents = value[RECOGNITION_TEXT].getString();			
			String senKeyword = "";
			
			char[] contentArray = contents.toCharArray();
			List<DisaSenResult> resultList = analyzer.analyze(contentArray);
			DisaSenResult disaSenResult = resultList.get(0);
			
			for(int i=0; i<disaSenResult.getEntitySize(); i++){
				NeEntity entity = disaSenResult.getEntity(i);
				/*if (	entity.type.equals ("$EMO_SAD") || entity.type.equals("$EMO_BORING") ||  
						entity.type.equals ("$EMO_PAIN") || entity.type.equals("$EMO_HATE") || 
						entity.type.equals ("$EMO_NEUTRALITY") || entity.type.equals("$EMO_HAPPY") || 
						entity.type.equals ("$EMO_INTEREST") || entity.type.equals("$EMO_SURPRISE") ) {
					
					System.out.println(entity.type + " - " + entity.lexical);
		
				}
				else */
				
				/*if (entity.type.equals ("$EMO_POSITIVE")) {	
					
					p++;
				}else if(entity.type.equals("$EMO_NEGATIVE")){
				
					n++;
				}*/
				
				
				if(entity.type.equals("$MINWON_SUBJECT")){
					System.out.println(entity.type + " - " + entity.lexical);
					senKeyword = senKeyword + entity.lexical + ",";
					
				}

			}
			
			/*if(p > n){
				sensirivity = "P";	// 긍정
			}else if(p < n){
				sensirivity = "D";	// 부정
			}else{
				sensirivity = "N";	// 중립
			}*/

			/*if(fieldName.equals("SENSITIVITY")){
				return sensirivity;
			}else if(fieldName.equals("SEN_P_KEYWORD")){			
				if(sensirivity.equals("P")){
					return posKeyword;
				}
			}else if(fieldName.equals("SEN_N_KEYWORD")){				
				if(sensirivity.equals("N")){
					return negKeyword;
				}
			}*/
			
			
			return senKeyword;
		}	
	}
}
