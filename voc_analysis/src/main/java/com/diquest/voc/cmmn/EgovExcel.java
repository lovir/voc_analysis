package com.diquest.voc.cmmn;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class EgovExcel extends AbstractExcelView {

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HashMap<String,Object> searchResult = new HashMap<String,Object>();
		searchResult = (HashMap<String, Object>) model.get("searchResultList");
		String excelName = null;
		HSSFSheet worksheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		if(searchResult.size()>0){
			ArrayList<HashMap<String,Object>> listResult = (ArrayList<HashMap<String, Object>>) searchResult.get("listResult");
			if(listResult.size()>0){
				
				HSSFCellStyle style = workbook.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBottomBorderColor(HSSFColor.BLACK.index);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setLeftBorderColor(HSSFColor.BLACK.index);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setRightBorderColor(HSSFColor.BLACK.index);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style.setTopBorderColor(HSSFColor.BLACK.index);
				HSSFFont font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setFontHeightInPoints((short)15);
				style.setFont(font);
				
				excelName = URLEncoder.encode("VOC검색결과", "UTF-8");
				worksheet = workbook.createSheet(excelName);
				worksheet.setColumnWidth(0, 10000);
				worksheet.setColumnWidth(1, 30000);
				row = worksheet.createRow(0);
				cell= row.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue("제목");
				
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue("내용");
				
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell.setCellValue("날짜");
				
				for (int i = 1; i < listResult.size()+1; i++) {
					HashMap<String, Object> temp = listResult.get(i-1);
					row = worksheet.createRow(i);
					row.createCell(0).setCellValue(temp.get("TITLE").toString());
					row.createCell(1).setCellValue(temp.get("CONTENT").toString());
					row.createCell(2).setCellValue(temp.get("REGDATE").toString());
				}
			}
		}
		response.setContentType("Application/Msexcel");
		response.setHeader("Content-Disposition", "ATTachment; Filename="+excelName+".xls");
	}
}
