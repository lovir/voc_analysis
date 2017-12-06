package com.diquest.voc.dashBoard.vo;

import java.util.List;

public class Top10totalVO {
  private List<TopKeywordVO> toplist =null;
  private int Intsum;


 
public List<TopKeywordVO> getToplist() {
	return toplist;
}
public void setToplist(List<TopKeywordVO> toplist) {
	this.toplist = toplist;
}
public int getIntsum() {
	return Intsum;
}
public void setIntsum(int intsum) {
	Intsum = intsum;
}
  
  
}
