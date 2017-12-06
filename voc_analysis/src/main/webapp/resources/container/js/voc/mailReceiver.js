$(function () {
	
	// 초기화
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
		
		var regYn = 'N';
		if($(this).parent().attr('id') == 'pagingY'){
			regYn = 'Y';
		}
		var	keyword = $('#term' + regYn).val();
		var condition = $('#condition' + regYn +' option:selected').val();
		var pageSize = 10;
		
		if(regYn == 'Y'){
			pageSize = $('#pageSize option:selected').val();
		}else {
			pageSize = $('#pageSizeN option:selected').val();
		}
		var currentPageNo = $(this).find('a').attr('name');
		
		var json = {"keyword":keyword,
					"condition":condition,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo,
					"regYn":regYn};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectMailReceiverList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				
				$('#list_contents' + regYn).html(getResultList(response.resultList, regYn, currentPageNo, pageSize));
				$('#paging' + regYn).html(paginationRenderer2(response.totSize, currentPageNo, pageSize));
				
			}
		});
		
		return false;
	});
	
	// 선택 삭제 버튼 클릭
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
				url : getContextPath() + "/management/deleteMailReceiver.do",
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
			$("#selectAllN").attr("checked",false);
			$("#selectAllY").attr("checked",false);
		}
	});
	
	// 수신자로 등록 버튼 클릭
	$('#btnAdd').live('click', function(){
		
		var selectedNo = new Array();

		selectedNo.push($(this).attr('name'));
		
		var json = {"selectedNo":selectedNo,
					"regYn":'Y'};
		// add
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/insertMailAddUser.do",
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
	});
	
	// 선택 등록 클릭
	$('#btnSelectedAdd').on('click', function(){
		
		var selectedNo = new Array();
		
		$('[name="selectedNoN"]:checked').each(function(){
			selectedNo.push($(this).attr('value'));
		});
		
		if(selectedNo.length>0){
			var json = {"selectedNo":selectedNo,
					"regYn":'Y'};
		
			// add
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/insertMailAddUser.do",
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
			$("#selectAllN").attr("checked",false);
			$("#selectAllY").attr("checked",false);
		}
	});
	
	// 메일수신자에 등록 된 리스트 변경 (select Box 10,20)
	$('#pageSize').change(function() {
		search('Y');
	});
	
	// 메일수신자 등록 리스트 변경 (select Box 10,20)
	$('#pageSizeN').change(function() {
		search('N');
	});


	// 메일수신자 목록 전체선택
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
	
	// 메일 미수신자 목록 전체선택
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
		if(regYn == 'Y'){
			pageSize = $('#pageSize option:selected').val();
		}else {
			pageSize = $('#pageSizeN option:selected').val();
		}
		var json = {"keyword":keyword,
					"condition":condition,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo,
					"regYn":regYn};
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectMailReceiverList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#list_contents' + regYn).html(getResultList(response.resultList, regYn, currentPageNo, pageSize));
				$('#paging' + regYn).html(paginationRenderer2(response.totSize, currentPageNo, pageSize));
				$('#totCnt' + regYn).html(response.totSize);
			}
		});
	}

	// 리스트 표시
	function getResultList(data, regYn, currentPageNo, pageSize){
		var str = "";
			
		for ( var i = 0, len = data.length; i < len; i++) {			
			var result = data[i];
			str += "<tr id='"+ result.no + "'>";
			str += "<td><input type='checkbox' name='selectedNo" + regYn + "' value='"+ result.no +"'/></td>";
			str += "<td>" + (((currentPageNo-1)*pageSize)+(i+1)) + "</td>";
			if(result.org!=null) str += "<td>" + result.org + "</td>";
			else str += "<td>미기재</td>";
			if(result.name!=null) str += "<td>" + result.name+"("+result.regId+")" + "</td>";
			else str += "<td>미기재</td>";
			if(result.email!=null) str += "<td class='align_l'>" + result.email + "</td>";
			else str += "<td class='align_l'>미기재</td>";
			
			//str += "<td>" + result.org + "</td>";
			//str += "<td>" + result.name+"("+result.regId+")" + "</td>";
			//str += "<td class='align_l'>" + result.email + "</td>";
			
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