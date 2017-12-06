package com.diquest.voc.management.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.diquest.voc.common.vo.BaseVo;
import com.diquest.voc.management.service.CommonSelectBoxService;
import com.diquest.voc.management.vo.CommonSelectBoxVo;
import com.diquest.voc.management.vo.newSelectBoxVO;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("commonSelectBoxService")
public class CommonSelectBoxServiceImpl implements CommonSelectBoxService{
	
	/** SampleDAO */
	@Resource(name="commonSelectBoxDAO")
	private CommonSelectBoxDAO commonSelectBoxDAO;
	
	//서울 메트로 셀렉트 박스 시작
		//민원접수 채널 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocChannelList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocChannelList(CommonSelectBoxVo);
		}
		//소셜 채널 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> socialChannelList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.socialChannelList(CommonSelectBoxVo);
		}
		//민원접수 구분 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocRecTypeList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocRecTypeList(CommonSelectBoxVo);
		}
		//민원접수 유형(대 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocKindList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocKindList(CommonSelectBoxVo);
		}
		//민원접수 분야(민원 중 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocPartList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocPartList(CommonSelectBoxVo);
		}
		//민원접수 세부항목(민원 소 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocItemList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocItemList(CommonSelectBoxVo);
		}
		//콜센터 분야(콜센터 대 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocCallKindList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocCallKindList(CommonSelectBoxVo);
		}
		//콜센터 분야(콜센터 대 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocCallPartList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocCallPartList(CommonSelectBoxVo);
		}
		//민원접수 세부항목(콜센터 소 카테고리) 리스트 조회(DB.Table : VOC.COMMON_CODE)
		public List<CommonSelectBoxVo> vocCallItemList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocCallItemList(CommonSelectBoxVo);
		}
		//처리 부서 목룍 리스트 조회(DB.Table : metro_bigdb.TCNCOMDEPT)
		public List<CommonSelectBoxVo> metroDeptList() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.metroDeptList(CommonSelectBoxVo);
		}
		
		//민원접수 채널 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocChannel() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocChannel(CommonSelectBoxVo);
		}
		//소셜 채널 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo socialChannel() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.socialChannel(CommonSelectBoxVo);
		}
		//민원접수 구분 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocRecType() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocRecType(CommonSelectBoxVo);
		}
		//민원접수 유형(대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocKind() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocKind(CommonSelectBoxVo);
		}
		//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocPart() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocPart(CommonSelectBoxVo);
		}
		//민원접수 세부항목(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocItem() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocItem(CommonSelectBoxVo);
		}
		//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocCallKind() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocCallKind(CommonSelectBoxVo);
		}
		//콜센터 분야(콜센터 대 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocCallPart() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocCallPart(CommonSelectBoxVo);
		}
		//민원접수 세부항목(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE)
		public CommonSelectBoxVo vocCallItem() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.vocCallItem(CommonSelectBoxVo);
		}
		//처리 부서 목룍 조회(DB.Table : metro_bigdb.TCNCOMDEPT)
		public CommonSelectBoxVo metroDept() throws Exception{
			CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
			return commonSelectBoxDAO.metroDept(CommonSelectBoxVo);
		}
	
	
	/////////// 2017.11.07 추가 start
	//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
/*	public List<CommonSelectBoxVo> vocPartList(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocPartList(vo);
	}
	//민원접수 분야(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocItemList(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocItemList(vo);
	}
	//콜센터 분야(콜센터 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocCallPartList(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocCallPartList(vo);
	}
	//콜센터 분야(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우
	public List<CommonSelectBoxVo> vocCallItemList(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocCallItemList(vo);
	}*/
		
	//민원접수 채널 리스트 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocComboChannel() throws Exception{
		CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
		return commonSelectBoxDAO.vocChannelList(CommonSelectBoxVo);
	}
	
	//민원접수 구분 리스트 조회(DB.Table : VOC.COMMON_CODE)
	public List<CommonSelectBoxVo> vocComboRect() throws Exception{
		CommonSelectBoxVo CommonSelectBoxVo = new CommonSelectBoxVo();
		return commonSelectBoxDAO.vocRecTypeList(CommonSelectBoxVo);
	}
	//민원접수 분야(민원 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocComboPart(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocPartList(vo);
	}
	//민원접수 분야(민원 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocComboItem(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocItemList(vo);
	}
	//콜센터 분야(콜센터 중 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우 
	public List<CommonSelectBoxVo> vocComboCallPart(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocCallPartList(vo);
	}
	//콜센터 분야(콜센터 소 카테고리) 조회(DB.Table : VOC.COMMON_CODE) - 파라미터 있는 경우
	public List<CommonSelectBoxVo> vocComboCallItem(CommonSelectBoxVo vo) throws Exception{
		return commonSelectBoxDAO.vocCallItemList(vo);
	}
	/////////// 2017.11.07 추가 end
//서울 메트로 셀렉트 박스 종료

}
