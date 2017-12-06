package com.diquest.voc.openapi.service;
import java.util.HashMap;

import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.diquest.ir.common.msg.protocol.query.SelectSet;
import com.diquest.ir.common.msg.protocol.result.GroupResult;
import com.diquest.ir.common.msg.protocol.result.Result;

/**
 * 검색 결과를 이용하여  XML 형태를 생성하는 클래스
 * @author DIQUEST
 */
public class DqXmlBuilderJ {
	
	private String encodingType = "UTF-8"; //Default : UTF-8
	
	/**
	 * 상품검색 XML 생성하여 String 형태로 리턴
	 * @param paramMap : XML 출력 대상 parameter
	 * @param selectSet : 상품 검색 SelectSet[]
	 * @param result : 상품 검색 Result
	 * @return 결과 XML String
	 */
	public String getProductXml(HashMap paramMap, SelectSet[] selectSet, Result result) {
		
		String productXml = "";
		String[] recommend = null;
		//String[] redirect = null;
		String redirect = null;
		String[] drama = null;
		
		Document xml;
		Element searchElement = new Element("search"); //최상위 Element
		xml = new Document(searchElement);
		
		int totalSize = 0;		//검색 결과 전체 개수
		int recSize = 0;
		int redSize = 0;
		int dramaSize = 0;
		if(result != null){
			totalSize = result.getTotalSize();

			if(result.getRecommend() != null){		
				recommend = new String(result.getRecommend()).split("\t"); //추천어 리스트
				recSize = recommend.length>5?5:recommend.length;
			}
			if(result.getRedirect() != null){		
				//redirect = new String(result.getRedirect()).split("<br>"); //바로가기 리스트
				redirect = new String(result.getRedirect()); //바로가기 리스트
				//redSize = redirect.length>5?5:redirect.length;
			}
			if(result.getValue("dramaResult") != null){	
				drama = new String(result.getValue("dramaResult")).split("\t"); 
			}
		}
		System.out.println("searchTerm 확인 : "+paramMap.get("searchTerm").toString());
		OverCreateElement(searchElement, "searchTerm", paramMap.get("searchTerm").toString());	//검색어 Element 생성
		OverCreateElement(searchElement, "totalCount", Integer.toString(totalSize));			//검색 결과 전체 개수 Element 생성
		if(paramMap.get("reqType").equals("search")){
		OverCreateElement(searchElement, "pageno", paramMap.get("pageno").toString());			//현재 페이지 번호 Element 생성
		OverCreateElement(searchElement, "result", paramMap.get("resultSize").toString());			//리스트 출력 개수 Element 생성
		}
		//OverCreateElement(searchElement, "totalCount", Integer.toString(recSize));	//검색 결과 전체 개수 Element 생성	

		if(totalSize > 0){
			//GroupResult Print
			if(paramMap.get("reqType").equals("stat")){
				if(result.getGroupResultSize() != 0){
					GroupResult[] groupResultlist = null;
					groupResultlist = result.getGroupResults();
	
					Element DocElement = new Element("group"); //그룹 결과에 대한 상위 Element
					searchElement.addContent(DocElement);
					
					if(groupResultlist[0].groupResultSize() > 0){ //카테고리 그룹 Element 생성
						Element categoryElement = new Element("category");
						DocElement.addContent(categoryElement);
						for (int i = 0; i < groupResultlist[0].groupResultSize(); i++) {
							String tempCode = new String(groupResultlist[0].getId(i)).trim();
							System.out.println("tempCode : "+tempCode);
							//String tempName = new String(groupResultlist[1].getId(i)).trim();
							//System.out.println("tempName : "+tempName);
								
							if(!tempCode.equals("")){
								Element DocList = new Element("item");
								categoryElement.addContent(DocList);
								OverCreateElement(DocList, "name", tempCode); //카테고리 이름 Element 생성
								OverCreateElement(DocList, "count", Integer.toString(groupResultlist[0].getIntValue(i))); //결과 개수 Element 생성
							}
						}
					}
				}
			}
			else if(paramMap.get("reqType").equals("search")){				
				//Result Print
				Element DocElement = new Element("product"); //상품 리스트에 대한 상위 Element
				searchElement.addContent(DocElement); 
				
				//상품 리스트 Element 생성
				for (int i=0; i < result.getRealSize(); i++){
					Element DocList = new Element("item");
					DocElement.addContent(DocList); 
					
					for(int m=0 ; m < result.getNumField(); m++){
						String selectFieldName = new String(selectSet[m].getField());
						OverCreateElement(DocList, selectFieldName.toLowerCase(), new String(result.getResult(i,m))); //selectSet[] 에 따른 결과  Element 생성
					}
				}
			}
		}
			
		productXml = XMLBuild(xml); //결과 XML Document 를 String 형태로 변환
		
		return productXml;
		
	}
	
	
	/**
	 * 상위 Element 에 새로운 Element 를 생성
	 * @param DocElement : 상위 Element
	 * @param schema : 생성 Element 이름
	 * @param value : 생성 Element 값
	 */
	protected void OverCreateElement(Element DocElement, String schema, String value){
		Element schemaElement = new Element(schema);
		schemaElement.setText(value);
		DocElement.addContent(schemaElement);
	}
	
	/**
	 * 상위 Element 에 새로운 CDATA Element 를 생성
	 * @param DocElement : 상위 Element
	 * @param schema : 생성 Element 이름
	 * @param value : 생성 Element 값
	 */
	protected void OverCreateCDATAElement(Element DocElement, String schema, String value){
		Element schemaElement = new Element(schema);
		CDATA SchemaCDATAElement = new CDATA(schema);
		SchemaCDATAElement.setText(value);
		schemaElement.addContent(SchemaCDATAElement);
		DocElement.addContent(schemaElement);
	}
	
	/**
	 * 결과 XML Document 를 String 형태로 변환하여 리턴 
	 * @param xml : 결과 XML Document
	 * @return XML String
	 */
	private String XMLBuild(Document xml) {
		
		String str = "";
		XMLOutputter xo = new XMLOutputter();
		Format fo = xo.getFormat();				//출력형식받기
		fo.setEncoding(encodingType);			//인코딩 설정
		fo.setLineSeparator("\r\n");			//줄바꿈
		fo.setIndent("   ");					//들여쓰기
		fo.setTextMode(Format.TextMode.TRIM);	//Enter 무시
		xo.setFormat(fo);						//출력형식 재설정
		str = xo.outputString(xml);				//XML Document String 형태로 출력
		
		return str;

	}
	

	/**
	 * null 또는 빈값인지 체크하여 새로운 String 리턴
	 * @param obj : 체크 대상
	 * @param str : 변환할 String
	 * @return 대체 String
	 */
	private String null2String(Object obj, String str) {
		
		String val = "";
		
		if(obj == null){
			val = str;
		}else if(obj.toString().trim().equals("")){
			val = str;
		}else{
			val = obj.toString().trim();
		}
		
		return val;
		
	}

}