package com.diquest.voc.management.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.voc.management.service.UserManagementService;
import com.diquest.voc.management.vo.UserManagementVo;

@Service("userManagementService")
public class UserManagementServiceImpl implements UserManagementService{

	/** UserManagementDAO */
	@Resource(name="userManagementDAO")
	private UserManagementDAO userManagementDAO;

	/** CommonDAO */
	@Resource(name="commonDAO")
	private CommonDAO commonDAO;
	
	@Override
	public HashMap<String, Object> selectuserManagementList(UserManagementVo vo)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if("1".equals(vo.getListType())){
			List<String> list = new ArrayList<String>();
			List<UserManagementVo> userListCnt = userManagementDAO.selectUserManagementListCnt(vo);
			List<String> kbvodUserList = commonDAO.selectKbvocCutUser(vo);
			// - 사용자 전체 Count 구하기 : 페이징 처리 위함
			for (int i = 0; i < userListCnt.size(); i++) {
				UserManagementVo temp = userListCnt.get(i);
				if(kbvodUserList.contains(temp.getRegId())){
					list.add(temp.getRegId());
				}
			}
			if(list.size()>0){
				vo.setRegIdArr(list);
			}
			//사용자 관리 - 사용자로 등록된(사용자 목록) 조회
			List<UserManagementVo> userList = userManagementDAO.selectUserManagementList(vo);
			map.put("resultList", userList);
			map.put("totSize", list.size());
			
		}else{
			List<String> list = new ArrayList<String>();
			List<String> kbVocUserListCnt = commonDAO.selectAddUserManagementListTotCnt(vo);
			List<String> kbvodUserList = userManagementDAO.selectKbactCutUser(vo);
			for (int i = 0; i < kbvodUserList.size(); i++) {
				if(kbVocUserListCnt.contains(kbvodUserList.get(i))){
					list.add(kbvodUserList.get(i));
				}
			}
			if(list.size()>0){
				vo.setRegIdArr(list);
			}
			List<UserManagementVo> kbvocUserList = commonDAO.selectAddUserManagementList(vo);
						
			map.put("resultList", kbvocUserList);
			map.put("totSize", (kbVocUserListCnt.size() - list.size()));
		}
		
		return map; 
	}

	@Override
	public int insertUserManagement(UserManagementVo vo) throws Exception {
		int result = 0;
		/*List<UserManagementVo> userList = new ArrayList<UserManagementVo>();
		userList = commonDAO.addUserList(vo);
		for (int i = 0; i < userList.size(); i++) {
			UserManagementVo temp = userList.get(i);
			temp.setRegYn("Y");
			userManagementDAO.insertUserManagement(temp);
			result++;
		}*/
		try{
			userManagementDAO.insertUserManagement(vo);;
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		result = 1;
		return result;
	}

	@Override
	public int deleteUserManagement(UserManagementVo vo) throws Exception {
		return userManagementDAO.deleteUserManagement(vo);
	}

}
