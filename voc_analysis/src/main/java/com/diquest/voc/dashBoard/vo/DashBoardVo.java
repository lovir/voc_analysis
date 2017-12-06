package com.diquest.voc.dashBoard.vo;

import java.io.Serializable;

import com.diquest.voc.common.vo.BaseVo;
import com.diquest.voc.management.vo.CommonSelectBoxVo;

/**  
 * @Class Name : DashBoardVo.java
 * @Description : DashBoardVo Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2014.04.30           최초생성
 * 
 * @author 박소영
 * @since 2014. 04.30
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */
public class DashBoardVo extends CommonSelectBoxVo {
	private String login_Id;	//사용자 ID
	private String startDate = "";	//시작일
	private String endDate = "";	//종료일
	private String emotion = "";	//긍부정
	private String previous = "";	//전일/전주/전월
	private String current = "";	//금일/금주/금월

	private String prevStartDate = "";
	private String prevEndDate = "";
	
	private String keyword = "";
	private String channel = "";
	
	public String getLogin_Id() {
		return login_Id;
	}

	public void setLogin_Id(String login_Id) {
		this.login_Id = login_Id;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEmotion() {
		return emotion;
	}

	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public String getCurrent() {
		return current;
	}

	public void setCurrent(String current) {
		this.current = current;
	}

	public String getPrevStartDate() {
		return prevStartDate;
	}

	public void setPrevStartDate(String prevStartDate) {
		this.prevStartDate = prevStartDate;
	}

	public String getPrevEndDate() {
		return prevEndDate;
	}

	public void setPrevEndDate(String prevEndDate) {
		this.prevEndDate = prevEndDate;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
}
