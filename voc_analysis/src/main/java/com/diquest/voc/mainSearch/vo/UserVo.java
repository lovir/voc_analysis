package com.diquest.voc.mainSearch.vo;

import java.io.Serializable;

import com.diquest.voc.common.vo.BaseVo;

/**  
 * @Class Name : UserVo.java
 * @Description : UserVo Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2015.09.07           최초생성
 * 
 * @author 박소영
 * @since 2015. 09.07
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */
public class UserVo extends BaseVo implements Serializable{

	/** serialVersionUID **/
	private static final long serialVersionUID = 3365833293855524850L;

	/** 제외 키워드 */
	private String exclusion;
	/** 작성자  */
	private String regId;
	/** 문서 번호  */
	private String id;
	/** 본문  */
	private String content;
	/** 제목  */
	private String title;
	/** VOC 니즈유형 */
	private String needs;
	/** 니즈유형  */
	private String needsType;
	/** 고객ID  */
	private String userId;
	
	public String getExclusion() {
		return exclusion;
	}
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getNeeds() {
		return needs;
	}
	public void setNeeds(String needs) {
		this.needs = needs;
	}
	public String getNeedsType() {
		return needsType;
	}
	public void setNeedsType(String needsType) {
		this.needsType = needsType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
