package com.diquest.voc.dashBoard.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.diquest.voc.management.vo.CommonSelectBoxVo;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("dashBoardDAO")
public class DashBoardDAO extends EgovAbstractDAO {

    /**
	 * 대분류 컬럼명 모두 가져오기
	 */
	@SuppressWarnings("unchecked")
    public List<HashMap<String, String>> selectKind() throws Exception {
        return getSqlMapClientTemplate().queryForList("dashBoardDAO.selectkind");
    }
    /**
	 * 대분류 카테고리 가져오기
	 */
	@SuppressWarnings("unchecked")
    public HashMap<String, Object> selectItem(String code) throws Exception {
        return (HashMap<String,Object>)getSqlMapClientTemplate().queryForObject("dashBoardDAO.selectItem", code);
    }
}
