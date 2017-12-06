package com.diquest.voc.openapi.service;

import java.io.UnsupportedEncodingException;

public class SearchUtil {
	public static String getParamStr(String param, String defaultValue) {
		if((param != null) && (!("").equals(param))) {
		
			try {
				return new String(param.getBytes("iso-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
		} else {
			param = defaultValue;
		}
		return param;
	}
	
	public static int getParamInt(String param, int defaultValue) {
		if((param != null) && (!("").equals(param))) {
			return Integer.parseInt(param);
		} else {
			return defaultValue;
		}
	}
	
	public static String [] getParamArrangement(String [] brand, String [] defaultValue){
		if(brand != null){
			return brand;
		} else{
			return defaultValue;
		}
	}

	public static int getPriceint(String parameter, String defalutValue) {
		if(parameter != null){
			return Integer.parseInt(parameter);
		}else{	
			return Integer.parseInt(defalutValue);
		}
	}
}
