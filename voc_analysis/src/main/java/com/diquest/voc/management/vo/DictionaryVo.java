package com.diquest.voc.management.vo;

import java.io.Serializable;

public class DictionaryVo implements Serializable{

	/** DictionaryVo **/
	private static final long serialVersionUID = -1824334439672523402L;

	/** 사전타입 */
	private int dicType;
	
	/** 사전명칭 */
	private String dicTitle;
	
	/** 인코딩 */
	private String encoding;
	
	/** 파일명 */
	private String filename;
	
	/** 파일위치 */
	private String filepath;
	
	public int getDicType() {
		return dicType;
	}
	public void setDicType(int dicType) {
		this.dicType = dicType;
	}
	public String getDicTitle() {
		return dicTitle;
	}
	public void setDicTitle(String dicTitle) {
		this.dicTitle = dicTitle;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
}
