package com.diquest.voc.management.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.voc.management.service.MailReceiverService;
import com.diquest.voc.management.vo.MailReceiverVo;

@Service("mailReceiverService")
public class MailReceiverServiceImpl implements MailReceiverService{

	/** MailReceiverDAO */
	@Resource(name="mailReceiverDAO")
	private MailReceiverDAO mailReceiverDAO;

	/** CommonDAO */
	@Resource(name="commonDAO")
	private CommonDAO commonDAO;
	
	@Override
	public HashMap<String, Object> selectMailReceiverList(MailReceiverVo receiverVo)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if("Y".equals(receiverVo.getRegYn())){
			//등록된 사용자 조회
			map.put("resultList", mailReceiverDAO.selectMailReceiverList(receiverVo));
			map.put("totSize", mailReceiverDAO.selectMailReceiverListTotCnt(receiverVo));
		}else {
			//등록할 사용자 조회
			List<String> list = new ArrayList<String>();
			List<String> kbvodUserList = commonDAO.selectKbvocCutUser(receiverVo);
			List<MailReceiverVo> addUserListCnt = mailReceiverDAO.selectMailReceiverAddUserListCnt(receiverVo);
			
			// - 등록가능한 사용자 전체 조회 : 전체 카운트
			for (int i = 0; i < addUserListCnt.size(); i++) {
				MailReceiverVo temp = addUserListCnt.get(i);
				if(kbvodUserList.contains(temp.getRegId())){
					list.add(temp.getRegId());
				}
			} 
			if(list.size()>0){
				receiverVo.setRegIdArr(list);
			}
			List<MailReceiverVo> addUserList = mailReceiverDAO.selectMailReceiverAddUserList(receiverVo);
			map.put("resultList", addUserList);
			map.put("totSize", list.size());
		}
		
		return map; 
	}

	@Override
	public int insertMailAddUser(MailReceiverVo receiverVo) throws Exception {
		int result = 0;
		List<MailReceiverVo> resultList = mailReceiverDAO.selectMailAddUserList(receiverVo);
		for (int i = 0; i < resultList.size(); i++) {
			MailReceiverVo temp = resultList.get(i);
			mailReceiverDAO.insertMailAddUser(temp);
			result++;
		}
		return result;
	}
	
	@Override
	public int deleteMailReceiver(MailReceiverVo receiverVo) throws Exception {
		int result = 0;
		result = mailReceiverDAO.deleteMailReceiver(receiverVo);
		return result;
	}
	
	@Override
	public int insertMailUser(MailReceiverVo receiverVo) throws Exception {
		int result = 0;
		if((int)mailReceiverDAO.selectMailReceiverExist(receiverVo) == 0){
			mailReceiverDAO.insertMailAddUser(receiverVo);	
		}
		return result;
	}
	
	@Override
	public int updateMailUser(MailReceiverVo receiverVo) throws Exception {
		int result = 0;
		if((int)mailReceiverDAO.selectMailReceiverExist(receiverVo) > 0){
			mailReceiverDAO.updateMailAddUser(receiverVo);	
		}
		return result;
	}
	
	@Override
	public String selectMail(MailReceiverVo receiverVo) throws Exception{
		String returnStr;
		returnStr = mailReceiverDAO.selectMail(receiverVo);
		return returnStr;
	}
	
	@Override
	public int selectMailReceiverExist(MailReceiverVo receiverVo) throws Exception{
		return mailReceiverDAO.selectMailReceiverExist(receiverVo);
	}
	
}
