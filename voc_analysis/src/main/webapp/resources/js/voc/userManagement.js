$(function () {
	
	$( window ).load(function() {
		search('Y');
		search('N');
	});
	
	// 검색 버튼 클릭
	$('#btnSearchY, #btnSearchN').on('click', function(){

		var regYn = 'N';
		if($(this).attr('id') == 'btnSearchY'){
			regYn = 'Y';
		}
		search(regYn);
		
	});
	
	$('#termY, #termN').live('keypress', function(e){
		if(e.which==13){
			var regYn = 'N';
			if($(this).attr('id') == 'termY'){
				regYn = 'Y';
			}
			search(regYn);
		}
		
	});
	
	// 페이지 번호 클릭
	$('#pagingY span, #pagingN span').live('click', function(){
		
		var listType = '2';
		var regYn = 'N';
		if($(this).parent().attr('id') == 'pagingY'){
			listType = '1';
			regYn = 'Y';
		}
		var	keyword = $('#term' + regYn).val();
		var condition = $('#condition' + regYn +' option:selected').val();
		var pageSize = 10;
		var currentPageNo = $(this).find('a').attr('name');
		
		var json = {"keyword":keyword,
					"condition":condition,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo,
					"regYn":regYn,
					"listType":listType};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectUserManagementList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#list_contents' + regYn).html(getResultList(response.resultList, regYn, response.totSize, currentPageNo));
				$('#paging' + regYn).html(paginationRenderer(response.totSize, currentPageNo));
			}
		});
		
		return false;
	});

	// 삭제 버튼 클릭
	$('#btnSelectedDelete').on('click', function(){
		
		var selectedNo = new Array();
		$('[name="selectedNoY"]:checked').each(function(){
			selectedNo.push($(this).attr('value'));
		});
		
		if(selectedNo.length>0){
			var json = {"selectedNo":selectedNo,
						"regYn":'N'};
		
			// del
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/deleteUserManagement.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) > 0) {
						alert("삭제되었습니다.");
						// 재검색
						search('Y');
						search('N');
					}
				}
			});
		}
		$("#selectAllN").attr("checked", false);
		$("#selectAllY").attr("checked", false);
	});
	
	// 수신자로 등록 버튼 클릭
	$('#btnAdd').live('click', function(){
		var selectedNo = new Array();
		selectedNo.push($(this).attr('name'));
		
		var listType = "2";
		var regYn = "Y";
		var json = {"selectedId":selectedNo,
					"regYn":regYn,
					"listType":listType};
		// add
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/insertUserManagement.do",
			dataType: "json", 
			data: JSON.stringify(json),
			contentType: "application/json;charset=utf-8",
			mimeType: "application/json",
			success : function(response) {
				if(parseInt(response) > 0) {
					alert("등록되었습니다.");
					// 재검색
					search('Y');
					search('N');
				}
			}
		});
	});
	
	// 선택 등록 버튼 클릭
	$('#btnSelectedAdd').on('click', function(){
		
		var selectedNo = new Array();
		
		$('[name="selectedNoN"]:checked').each(function(){
			selectedNo.push($(this).attr('value'));
		});
		
		var listType = "2";
		
		if(selectedNo.length>0){
			var json = {"selectedId":selectedNo,
						"regYn":"Y",
						"listType":listType};
		
			// add
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/insertUserManagement.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) > 0) {
						alert("등록되었습니다.");
						// 재검색
						search('Y');
						search('N');
					}
				}
			});
		}
		
		$("#selectAllN").attr("checked", false);
		$("#selectAllY").attr("checked", false);
		
	});
	
	// 전체선택(사용자 목록)
	$('input[id=selectAllY]:checkbox').change(function () {
		if ($(this).attr("checked")) {
			$('[name="selectedNoY"]').each(function(){
				$(this).prop("checked", true);
			});
		}else{
			$('[name="selectedNoY"]').each(function(){
				$(this).prop("checked", false);
			});
		}
	});
	
	// 전체선택(사용자 등록)
	$('input[id=selectAllN]:checkbox').change(function () {
		if ($(this).attr("checked")) {
			$('[name="selectedNoN"]').each(function(){
				$(this).prop("checked", true);
			});
		}else{
			$('[name="selectedNoN"]').each(function(){
				$(this).prop("checked", false);
			});
		}
	});
	
	// 검색
	function search(regYn){
		
		var	keyword = $('#term' + regYn).val();
		var condition = $('#condition' + regYn +' option:selected').val();
		var currentPageNo = 1;
		var pageSize = 10;
		var listType = '1';
		if(regYn == 'N'){
			listType  = '2';
		}
		var json = {"keyword":keyword,
					"condition":condition,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo,
					"regYn":regYn,
					"listType":listType};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectUserManagementList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				
				$('#list_contents' + regYn).html(getResultList(response.resultList, regYn, response.totSize, currentPageNo));
				$('#paging' + regYn).html(paginationRenderer(response.totSize, currentPageNo));
				$('#totCnt' + regYn).html(response.totSize);
			}
		});
	}
	
	// 리스트 표시
	function getResultList(data, regYn, totSize, currentPageNo){
		var str = "";
			
		for ( var i = 0, len = data.length; i < len; i++) {			
			var result = data[i];
			
			str += "<tr id='"+ result.no + "'>";
			str += "<td><input type='checkbox' name='selectedNo" + regYn + "' value='"+ result.no +"'/></td>";
			str += "<td>" + (((currentPageNo-1)*10)+(i+1)) + "</td>";
//			str += "<td class='align_l'>" + result.org + "</td>";
//			str += "<td class='align_l'>" + result.name + "</td>";
//			str += "<td class='align_l'>" + result.email + "</td>";
//			str += "<td class='align_l'>" + result.phone + "</td>";
			if(result.org!=null) str += "<td>" + result.org + "</td>";
			else str += "<td>미기재</td>";
			if(result.name!=null) str += "<td>" + result.name + "</td>";
			else str += "<td>미기재</td>";
			if(result.email!=null) str += "<td class='align_l'>" + result.email + "</td>";
			else str += "<td class='align_l'>미기재</td>";
			if(result.phone!=null) str += "<td>" + result.phone + "</td>";
			else str += "<td>미기재</td>";
			if(regYn == 'Y'){
				str += "<td>" + result.regDate + "</td>";
			}else{
				str += "<td><a href='#' id='btnAdd' class='btn b_gray ssmall' name='"+ result.no +"'>수신자로 등록</a></td>";
			}
			
			str += "</tr>";
		
		}
		
		return str;
	}
	
});