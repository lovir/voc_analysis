package com.diquest.voc.management.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.newSelectBoxVO;
import com.ibatis.sqlmap.client.SqlMapClient;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Repository("commonSelectBoxDAO")
public class CommonSelectBoxDAO extends SqlMapClientDaoSupport {
	
	/** Log Service */
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 정형분석(KBVOC)DB 접근
	 */
	@Resource(name="sqlMapClient2")
	public void setSuperlMapClient(SqlMapClient sqlMapClient){
		super.setSqlMapClient(sqlMapClient);
	}
	
//서울 메트로 리스트 조회 시작
	//민원접수 채널 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocChannelList(CommonSelectBoxVo vo) throws Exception {
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocChannelTypeList", vo);
	}
	//소셜 채널 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> socialChannelList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.socialChannelTypeList", vo);
	}
	//민원접수 구분 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocRecTypeList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocRecTypeList", vo);
	}
	//민원접수 유형(대 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocKindList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocKindTypeList", vo);
	}
	//민원접수 분야(민원 중 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocPartList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocPartTypeList", vo);
	}
	//민원접수 세부항목(민원 소 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocItemList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocItemTypeList", vo);
	}
	//콜센터 분야(콜센터 대 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocCallKindList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocCallKindTypeList", vo);
	}
	//콜센터 분야(콜센터 대 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocCallPartList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocCallPartTypeList", vo);
	}
	//민원접수 세부항목(콜센터 소 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> vocCallItemList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.vocCallItemTypeList", vo);
	}
	//처리 부서 목룍 리스트 조회(DB.Table : metro_bigdb.TCNCOMDEPT)
	@SuppressWarnings("unchecked")
	public List<CommonSelectBoxVo> metroDeptList(CommonSelectBoxVo vo) throws Exception{
		return getSqlMapClientTemplate().queryForList("commonSelectBoxDAO.metroDeptList", vo);
	}


	//민원접수 채널 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocChannel(CommonSelectBoxVo vo) throws Exception {
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocChannelType", vo);
	}
	//소셜 채널 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo socialChannel(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.socialChannelType", vo);
	}
	//민원접수 구분 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocRecType(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocRecType", vo);
	}
	//민원접수 유형(대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocKind(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocKindType", vo);
	}
	//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocPart(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocPartType", vo);
	}
	//민원접수 세부항목(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocItem(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocItemType", vo);
	}
	//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocCallKind(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocCallKindType", vo);
	}
	//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocCallPart(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocCallPartType", vo);
	}
	//민원접수 세부항목(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo vocCallItem(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.vocCallItemType", vo);
	}
	//민원접수 세부항목(민원+콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo Item(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.ItemType", vo);
	}
	//처리 부서 목룍 조회(DB.Table : metro_bigdb.TCNCOMDEPT)
	@SuppressWarnings("unchecked")
	public CommonSelectBoxVo metroDept(CommonSelectBoxVo vo) throws Exception{
		return (CommonSelectBoxVo) getSqlMapClientTemplate().queryForObject("commonSelectBoxDAO.metroDept", vo);
	}
	//처리 부서 조회(입력받은 코드값에 해당하는 부서만 조회)
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectMetroList(HashMap<String, Object> deptCodeList) throws Exception {
		return getSqlMapClientTemplate().queryForList("stationStatusDAO.metroList", deptCodeList);
	}
//서울 메트로 리스트 조회 종료
	
}
