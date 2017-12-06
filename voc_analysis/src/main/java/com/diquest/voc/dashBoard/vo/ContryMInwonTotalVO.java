package com.diquest.voc.dashBoard.vo;

import java.util.Date;
import java.util.List;

public class ContryMInwonTotalVO {

	private List<CountryMinwonVO> cmlist;
	private String totalT;
	private String totalPosi;
	private String totalNega;
	private String totalNetu;
	private String nowdate;
	
	

	public String getNowdate() {
		return nowdate;
	}
	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	public List<CountryMinwonVO> getCmlist() {
		return cmlist;
	}
	public void setCmlist(List<CountryMinwonVO> cmlist) {
		this.cmlist = cmlist;
	}
	public String getTotalT() {
		return totalT;
	}
	public void setTotalT(String totalT) {
		this.totalT = totalT;
	}
	public String getTotalPosi() {
		return totalPosi;
	}
	public void setTotalPosi(String totalPosi) {
		this.totalPosi = totalPosi;
	}
	public String getTotalNega() {
		return totalNega;
	}
	public void setTotalNega(String totalNega) {
		this.totalNega = totalNega;
	}
	public String getTotalNetu() {
		return totalNetu;
	}
	public void setTotalNetu(String totalNetu) {
		this.totalNetu = totalNetu;
	}
	
	
}
