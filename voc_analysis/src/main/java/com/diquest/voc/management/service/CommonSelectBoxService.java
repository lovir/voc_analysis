package com.diquest.voc.management.service;

import java.util.HashMap;
import java.util.List;

import com.diquest.voc.common.vo.BaseVo;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.newSelectBoxVO;

import egovframework.rte.psl.dataaccess.util.EgovMap;


/**  
 * @Class Name : CommonSelectService.java
 * @Description : CommonSelectService Class
 * @Modification Information  
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2015.08.12           최초생성
 * 
 * @author 정석현
 * @since 2015. 08.12
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by DIQUEST All right reserved.
 */
public interface CommonSelectBoxService {
	
	//서울 메트로 셀렉트 박스 시작
	//민원접수 채널 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocChannelList() throws Exception;
	//소셜 채널 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> socialChannelList() throws Exception;
	//민원접수 구분 리스트(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocRecTypeList() throws Exception;
	//민원접수 유형(대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocKindList() throws Exception;
	//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocPartList() throws Exception;
	//민원접수 세부항목(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocItemList() throws Exception;
	//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocCallKindList() throws Exception;
	//콜센터 분야(콜센터 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocCallPartList() throws Exception;
	//민원접수 세부항목(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocCallItemList() throws Exception;
	//처리 부서 목룍 조회(DB.Table : metro_bigdb.TCNCOMDEPT)
	public List<CommonSelectBoxVo> metroDeptList() throws Exception;
			
	
	//////////////////
	
	//민원접수 채널 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocChannel() throws Exception;
	//소셜 채널 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo socialChannel() throws Exception;
	//민원접수 구분 리스트(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocRecType() throws Exception;
	//민원접수 유형(대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocKind() throws Exception;
	//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocPart() throws Exception;
	//민원접수 세부항목(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocItem() throws Exception;
	//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocCallKind() throws Exception;
	//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocCallPart() throws Exception;
	//민원접수 세부항목(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public CommonSelectBoxVo vocCallItem() throws Exception;
	//처리 부서 목룍 조회(DB.Table : metro_bigdb.TCNCOMDEPT)
	public CommonSelectBoxVo metroDept() throws Exception;
	
	
	/////////// 2017.11.07 추가 start
	//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	/*public List<CommonSelectBoxVo> vocPartList(CommonSelectBoxVo vo) throws Exception;
	//민원접수 분야(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocItemList(CommonSelectBoxVo vo) throws Exception;
	//콜센터 분야(콜센터 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocCallPartList(CommonSelectBoxVo vo) throws Exception;
	//민원접수 세부항목(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocCallItemList(CommonSelectBoxVo vo) throws Exception;
	//민원접수 채널 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocChannelList() throws Exception;
	//민원접수 구분 리스트(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocRecTypeList() throws Exception;*/
	public List<CommonSelectBoxVo> vocComboPart(CommonSelectBoxVo vo) throws Exception;
	//민원접수 분야(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocComboItem(CommonSelectBoxVo vo) throws Exception;
	//콜센터 분야(콜센터 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocComboCallPart(CommonSelectBoxVo vo) throws Exception;
	//민원접수 세부항목(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocComboCallItem(CommonSelectBoxVo vo) throws Exception;
	//민원접수 채널 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocComboChannel() throws Exception;
	//민원접수 구분 리스트(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocComboRect() throws Exception;
	/////////// 2017.11.07 추가 end
	
	//서울 메트로 셀렉트 박스 종료
}
