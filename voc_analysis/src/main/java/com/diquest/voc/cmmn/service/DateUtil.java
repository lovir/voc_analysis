package com.diquest.voc.cmmn.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtil {

	/**
	 * 현재 날짜를 반환
	 * 입력받은 형식이 있으면 해당 형식으로 변환하여 반환
	 * @param dateType String
	 * @return String
	 */
	public static String getCurrentDate(String dateType) {
		Calendar aCalendar = Calendar.getInstance();
		
		int year = aCalendar.get(Calendar.YEAR);
		int month = aCalendar.get(Calendar.MONTH) + 1;
		int date = aCalendar.get(Calendar.DAY_OF_MONTH);
		int hour = aCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = aCalendar.get(Calendar.MINUTE);
		int second = aCalendar.get(Calendar.SECOND);

		String strDate = Integer.toString(year)
				+ ((month < 10) ? "0" + Integer.toString(month) : Integer
						.toString(month))
				+ ((date < 10) ? "0" + Integer.toString(date) : Integer
						.toString(date))
				+ ((hour < 10) ? "0" + Integer.toString(hour) : Integer
						.toString(hour))
				+ ((minute < 10) ? "0" + Integer.toString(minute) : Integer
						.toString(minute))
				+ ((second < 10) ? "0" + Integer.toString(second) : Integer
						.toString(second));
		// 입력받은 날짜형식이 있으면 해당 형식으로 변환
		if (!"".equals(dateType))
			strDate = convertDate(strDate, dateType);

		return strDate;
	}

	/**
	 * 입력받은 형식으로 날짜를 변환하여 반환
	 * @param strDate String
	 * @param sFormatStr String
	 * @return String
	 */
	public static String convertDate(String strDate, String sFormatStr) {
         
		/*strDate="20160505000000";*/
		//System.out.println("값 확인 "+ Integer.parseInt(strDate.substring(4, 6)));
		//System.out.println("값 확인2 : "+(Integer.parseInt(strDate.substring(4, 6)) - 1));
		Calendar cal = null;
		//System.out.println("convertDate startDate 확인 : "+strDate);
		cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(strDate.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(strDate.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(strDate.substring(6, 8)));
		cal.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(strDate.substring(8, 10)));
		cal.set(Calendar.MINUTE, Integer.parseInt(strDate.substring(10, 12)));
		cal.set(Calendar.SECOND, Integer.parseInt(strDate.substring(12, 14)));

		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr,
				Locale.getDefault());

		return sdf.format(cal.getTime());
	}

	/**
	 * 입력받은 값을 날짜에 추가하여 반환
	 * @param sFormatStr String
	 * @param strDate String
	 * @param int field
	 * @param int amount
	 * @return String
	 */
	public static String addYearMonthDay(String sFormatStr, String strDate,
			int field, int amount) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr,
				Locale.getDefault());
		try {
			cal.setTime(sdf.parse(strDate));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ strDate);
		}

		switch (field) {
		case Calendar.YEAR:
			cal.add(Calendar.YEAR, amount);
			break;
		case Calendar.MONTH:
			cal.add(Calendar.MONTH, amount);
			break;
		case Calendar.DATE:
			cal.add(Calendar.DAY_OF_MONTH, amount);
			break;
		default:
			break;
		}

		return sdf.format(cal.getTime());
	}

	/**
	 * 입력받은 기간 사이의 날짜값을 반환
	 * @param strDate String
	 * @param endDate String
	 * @param sFormatStr String
	 * @param period int
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getCategories(String strDate,String endDate, String sFormatStr, int period) {

		LinkedHashMap<String, String> cagegoriesMap = null;
		Calendar sCalendar = Calendar.getInstance();
		Calendar eCalendar = Calendar.getInstance();
		String format = "yyyyMMddHH";

		switch (period) {
		case Globals.TREND_PERIOD_HOUR:
			format = "yyyyMMddHH";
			break;
		case Globals.TREND_PERIOD_DAY:
		case Globals.TREND_PERIOD_WEEK:
			format = "yyyyMMdd";
			break;
		case Globals.TREND_PERIOD_MONTH:
		case Globals.TREND_PERIOD_QUARTER:
		case Globals.TREND_PERIOD_HALF:
			format = "yyyyMM";
			break;
		case Globals.TREND_PERIOD_YEAR:
			format = "yyyy";
			break;

		}

		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

		try {
			sCalendar.setTime(sdf.parse(convertDate(strDate, format)));
			eCalendar.setTime(sdf.parse(convertDate(endDate, format)));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ strDate);
		}

		switch (period) {
		case Globals.TREND_PERIOD_HOUR:
			cagegoriesMap = getHourCategories(sCalendar, eCalendar, sFormatStr);
			break;
		case Globals.TREND_PERIOD_DAY:
			cagegoriesMap = getDayCategories(sCalendar, eCalendar, sFormatStr);
			break;
		case Globals.TREND_PERIOD_WEEK:
			cagegoriesMap = getWeekCategories(sCalendar, eCalendar, sFormatStr);
			break;
		/*case Globals.KEYWORD_ISSUE_PERIOD_WEEK:
			cagegoriesMap = getWeekDateCategories(sCalendar, eCalendar, sFormatStr);
			break;*/
		case Globals.TREND_PERIOD_MONTH:
			cagegoriesMap = getMonthCategories(sCalendar, eCalendar, sFormatStr);
			break;
		case Globals.TREND_PERIOD_QUARTER:
			cagegoriesMap = getQuarterCategories(sCalendar, eCalendar,
					sFormatStr);
			break;
		case Globals.TREND_PERIOD_HALF:
			cagegoriesMap = getHalfCategories(sCalendar, eCalendar, sFormatStr);
			break;
		case Globals.TREND_PERIOD_YEAR:
			cagegoriesMap = getYearCategories(sCalendar, eCalendar, sFormatStr);
			break;
		}

		return cagegoriesMap;
	}

	/**
	 * 입력받은 기간 사이의 시간(Hour)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getHourCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		int date = 0;
		int hour = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> dayMap = new LinkedHashMap<String, String>();

		sCalendar.set(Calendar.HOUR_OF_DAY, 0);
		eCalendar.set(Calendar.HOUR_OF_DAY, 23);

		while (sCalendar.getTime().compareTo(eCalendar.getTime()) <= 0) {
			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH) + 1;
			date = sCalendar.get(Calendar.DAY_OF_MONTH);
			hour = sCalendar.get(Calendar.HOUR_OF_DAY);

			key.append(year)
					.append(((month < 10) ? "0" + Integer.toString(month)
							: Integer.toString(month)))
					.append(((date < 10) ? "0" + Integer.toString(date)
							: Integer.toString(date)))
					.append(((hour < 10) ? "0" + Integer.toString(hour)
							: Integer.toString(hour)));

			dayMap.put(key.toString(),
					convertDate(key.toString() + "0000", sFormatStr));
			if (!dayMap.isEmpty())
				sCalendar.add(Calendar.HOUR_OF_DAY, 1);

		}

		return dayMap;
	}

	/**
	 * 입력받은 기간 사이의 일(Day)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getDayCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		int date = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> dayMap = new LinkedHashMap<String, String>();

		while (sCalendar.getTime().compareTo(eCalendar.getTime()) <= 0) {
			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH) + 1;
			date = sCalendar.get(Calendar.DAY_OF_MONTH);

			key.append(year)
					.append(((month < 10) ? "0" + Integer.toString(month)
							: Integer.toString(month)))
					.append(((date < 10) ? "0" + Integer.toString(date)
							: Integer.toString(date)));

			dayMap.put(key.toString(),
					convertDate(key.toString() + "000000", sFormatStr));
			if (!dayMap.isEmpty())
				sCalendar.add(Calendar.DATE, 1);

		}

		return dayMap;
	}

	/**
	 * 입력받은 기간 사이의 주(Week)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getWeekCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> weekMap = new LinkedHashMap<String, String>();

		int sWeek = 0;

		int sDayOfWeek = sCalendar.get(Calendar.DAY_OF_WEEK); // 시작일을 주의 시작일로 변경
		sCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
		sCalendar.add(Calendar.DATE, (sDayOfWeek - 1) * -1);

		int eDayOfWeek = eCalendar.get(Calendar.DAY_OF_WEEK); // 종료일을 주의 마지막날로 변경
		eCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
		eCalendar.add(Calendar.DATE, 7 - eDayOfWeek);
		//System.out.println("sCalendar.getTime():"+sCalendar.getTime());
		//System.out.println("eCalendar.getTime():"+eCalendar.getTime());
		while (sCalendar.getTime().compareTo(eCalendar.getTime()) < 0) {

			sWeek = 0;
			sCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
			sCalendar.setMinimalDaysInFirstWeek(7);
			sWeek = sCalendar.get(java.util.Calendar.WEEK_OF_MONTH);
			if (sWeek == 0) {
				sCalendar.set(Calendar.YEAR, sCalendar.get(Calendar.YEAR));
				sCalendar.set(Calendar.MONTH, sCalendar.get(Calendar.MONTH));
				sCalendar.set(Calendar.DAY_OF_MONTH,
						sCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				sCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
				sCalendar.setMinimalDaysInFirstWeek(7);
				sWeek = sCalendar.get(java.util.Calendar.WEEK_OF_MONTH);
			}

			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH) + 1;

			key.append(year)
					.append(((month < 10) ? "0" + Integer.toString(month)
							: Integer.toString(month))).append("0")
					.append(sWeek);

			weekMap.put(key.toString(), month + "월 " + sWeek + "주");
			sCalendar.add(Calendar.DATE, 7);
		}

		return weekMap;
	}
	
	/**
	 * 입력받은 기간 사이의 주(Week)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getWeekDateCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		int day = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> weekMap = new LinkedHashMap<String, String>();

		int sWeek = 0;

		int sDayOfWeek = sCalendar.get(Calendar.DAY_OF_WEEK); // 시작일을 주의 시작일로 변경
		sCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
		sCalendar.add(Calendar.DATE, (sDayOfWeek - 1) * -1);

		int eDayOfWeek = eCalendar.get(Calendar.DAY_OF_WEEK); // 종료일을 주의 마지막날로 변경
		eCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
		eCalendar.add(Calendar.DATE, 7 - eDayOfWeek);
		//System.out.println("sCalendar.getTime():"+sCalendar.getTime());
		//System.out.println("eCalendar.getTime():"+eCalendar.getTime());
		while (sCalendar.getTime().compareTo(eCalendar.getTime()) < 0) {

			sWeek = 0;
			sCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
			sCalendar.setMinimalDaysInFirstWeek(7);
			sWeek = sCalendar.get(java.util.Calendar.WEEK_OF_MONTH);
			if (sWeek == 0) {
				sCalendar.set(Calendar.YEAR, sCalendar.get(Calendar.YEAR));
				sCalendar.set(Calendar.MONTH, sCalendar.get(Calendar.MONTH));
				sCalendar.set(Calendar.DAY_OF_MONTH,
						sCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				sCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
				sCalendar.setMinimalDaysInFirstWeek(7);
				sWeek = sCalendar.get(java.util.Calendar.WEEK_OF_MONTH);
			}

			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH) + 1;
			day = sCalendar.get(Calendar.DATE);

			key.append(year)
					.append(((month < 10) ? "0" + Integer.toString(month)
							: Integer.toString(month))).append(((day < 10) ? "0" + Integer.toString(day)
									: Integer.toString(day)));

			weekMap.put(key.toString(), month + "월" + sWeek + "주");
			sCalendar.add(Calendar.DATE, 7);
		}

		return weekMap;
	}

	/**
	 * 입력받은 기간 사이의 월(Month)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getMonthCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> monthMap = new LinkedHashMap<String, String>();

		while (sCalendar.getTime().compareTo(eCalendar.getTime()) <= 0) {
			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH) + 1;

			key.append(year).append(
					((month < 10) ? "0" + Integer.toString(month) : Integer
							.toString(month)));

			monthMap.put(key.toString(), year + "년 " + month + "월");
			if (!monthMap.isEmpty())
				sCalendar.add(Calendar.MONTH, 1);

		}

		return monthMap;
	}

	/**
	 * 입력받은 기간 사이의 분기(Quarter)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getQuarterCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> quarterMap = new LinkedHashMap<String, String>();

		/*int sMonth = sCalendar.get(Calendar.MONTH);
		int eMonth = eCalendar.get(Calendar.MONTH) + 1;

		sCalendar.add(Calendar.MONTH, -(sMonth % 3));
		eCalendar.add(Calendar.MONTH, (12 - eMonth) % 3);*/

		while (sCalendar.getTime().compareTo(eCalendar.getTime()) <= 0) {

			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH);
			String quarter = "";
			String text = "";

			if (month / 3 == 0) {
				quarter = "01";
				text = "1";
			} else if (month / 3 == 1) {
				quarter = "02";
				text = "2";
			} else if (month / 3 == 2) {
				quarter = "03";
				text = "3";
			} else {
				quarter = "04";
				text = "4";
			}

			key.append(year).append(quarter);

			quarterMap.put(key.toString(), year + "년 " + text + "분기");

			if (!quarterMap.isEmpty())
				sCalendar.add(Calendar.MONTH, 3);
		}

		return quarterMap;
	}

	/**
	 * 입력받은 기간 사이의 반기(Half)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getHalfCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		int month = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> halfMap = new LinkedHashMap<String, String>();

		while (sCalendar.getTime().compareTo(eCalendar.getTime()) <= 0) {
			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);
			month = sCalendar.get(Calendar.MONTH);
			String half = "";
			String text = "";

			if (month / 6 == 0) {
				half = "01";
				text = "상반기";
			} else {
				half = "02";
				text = "하반기";
			}

			key.append(year).append(half);

			halfMap.put(key.toString(), year + "년 " + text);

			if (!halfMap.isEmpty())
				sCalendar.add(Calendar.MONTH, 6);
		}
			
		return halfMap;
	}

	/**
	 * 입력받은 기간 사이의 년(Year)값을 반환
	 * @param sCalendar Calendar
	 * @param eCalendar Calendar
	 * @param sFormatStr String
	 * @return LinkedHashMap
	 */
	public static LinkedHashMap<String, String> getYearCategories(
			Calendar sCalendar, Calendar eCalendar, String sFormatStr) {
		int year = 0;
		StringBuffer key = null;

		LinkedHashMap<String, String> yearMap = new LinkedHashMap<String, String>();

		while (sCalendar.getTime().compareTo(eCalendar.getTime()) <= 0) {
			key = new StringBuffer();

			year = sCalendar.get(Calendar.YEAR);

			key.append(year);

			yearMap.put(key.toString(), Integer.toString(year) + "년");
			if (!yearMap.isEmpty())
				sCalendar.add(Calendar.YEAR, 1);
		}
		return yearMap;
	}

	/**
	 * 입력받은 날짜의 주(Week)값을 반환
	 * @param sFormatStr String
	 * @param strDate String
	 * @return int
	 */
	public static int getWeek(String sFormatStr, String strDate) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr,
				Locale.getDefault());
		try {
			cal.setTime(sdf.parse(strDate));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ strDate);
		}

		return cal.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 입력받은 주(Week)의 시작일을 반환
	 * @param year int
	 * @param month int
	 * @param week int
	 * @return String
	 */
	public static String getWeekStart(int year, int month, int week) {

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.WEEK_OF_MONTH, week);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd",
				Locale.getDefault());

		// Now get the first day of week.
		Date date = calendar.getTime();

		return sdf.format(calendar.getTime());
	}

	/**
	 * 현재날짜로부터 입력받은 일자만큼 과거의 날짜(Day)배열 반환
	 * @param sFormatStr String
	 * @param date int
	 * @return Map
	 */
	public static Map<String, Integer> getWeek(String sFormatStr, int date) {
		Map<String, Integer> weekMap = new HashMap<String, Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr);
		Calendar cal = Calendar.getInstance();
		/*
		 * cal.set(Calendar.YEAR, 2013); // 임시 cal.set(Calendar.MONTH,
		 * Calendar.JUNE); cal.set(Calendar.DATE, 15);
		 */

		int count = date;
		// 시작일부터
		cal.add(Calendar.DAY_OF_MONTH, -date);
		// 데이터 저장
		while (count > 0) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
			weekMap.put(sdf.format(cal.getTime()), 0);
			count--;
		}

		return weekMap;
	}

	/**
	 * 현재날짜로부터 입력받은 일자만큼 과거의 날짜(Hour)배열 반환
	 * @param sFormatStr String
	 * @param date int
	 * @return Map
	 */
	public static Map<String, Integer> getHour(String sFormatStr, int date) {
		Map<String, Integer> weekMap = new HashMap<String, Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		/*
		 * cal.set(Calendar.YEAR, 2013); // 임시 cal.set(Calendar.MONTH,
		 * Calendar.JUNE); cal.set(Calendar.DATE, date);
		 * cal.set(Calendar.HOUR_OF_DAY, 1); // 임시
		 */
		int count = 24;

		// 데이터 저장
		while (count > 0) {
			cal.add(Calendar.HOUR_OF_DAY, 1);
			weekMap.put(sdf.format(cal.getTime()), 0);
			count--;
		}

		return weekMap;
	}

	// 확인하고 삭제
	public static String getWeekDate(int date) {

		StringBuffer weekDate = new StringBuffer();
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.add(Calendar.DAY_OF_MONTH, date);

		int week = 0;
		int year = aCalendar.get(Calendar.YEAR);
		int month = aCalendar.get(Calendar.MONTH) + 1;

		aCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
		aCalendar.setMinimalDaysInFirstWeek(7);
		week = aCalendar.get(java.util.Calendar.WEEK_OF_MONTH);

		if (week == 0) {
			aCalendar.set(Calendar.YEAR, aCalendar.get(Calendar.YEAR));
			aCalendar.set(Calendar.MONTH, aCalendar.get(Calendar.MONTH));
			aCalendar.set(Calendar.DAY_OF_MONTH,
					aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			aCalendar.setFirstDayOfWeek(java.util.Calendar.SUNDAY);
			aCalendar.setMinimalDaysInFirstWeek(7);
			week = aCalendar.get(java.util.Calendar.WEEK_OF_MONTH);
		}

		weekDate.append(year)
				.append(((month < 10) ? "0" + Integer.toString(month) : Integer
						.toString(month))).append("0").append(week);

		return weekDate.toString();
	}

	/**
	 * 주의 시작일 반환
	 * @param sFormatStr String
	 * @param strDate String
	 * @return String
	 */
	public static String getFirstDayOfWeek(String sFormatStr, String strDate, int amount) {
		
		//오리지날 복구 <필>
		String startDate = addYearMonthDay(sFormatStr, strDate, Calendar.DAY_OF_MONTH, amount); // 전주 시작일
		//System.out.println("DateUtil startDate : "+startDate);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr,
				Locale.getDefault());
		try {
			cal.setTime(sdf.parse(startDate));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ strDate);
		}

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		
		return sdf.format(cal.getTime());

	}
}
