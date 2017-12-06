// 페이지 표시
function paginationRenderer(totCnt, current) {

	var pageSize = 10;
	if($('#pageSize').length>0){
		pageSize = Number($('#pageSize option:selected').val());
	}
	var totalPageCount = Math.floor((totCnt-1)/pageSize) + 1;
	var firstPageNo = 1;
	var currentPageNo = current;
	var firstPageNoOnPageList = Math.floor((currentPageNo-1)/pageSize) * pageSize + 1;
	var lastPageNoOnPageList = firstPageNoOnPageList + pageSize - 1;
	if(lastPageNoOnPageList > totalPageCount){
		lastPageNoOnPageList = totalPageCount;
	}
	var lastPageNo = totalPageCount;
	var str = "";
	
	if(totCnt > 0){
		if(firstPageNoOnPageList > pageSize){
			str += "<span class='first'><a href='#' name='"+ firstPageNo +"'>맨 처음</a></span>";
			str += "<span class='prev'><a href='#' name='" + (firstPageNoOnPageList-1) + "'>이전</a></span>";
        }else{
			str += "<span class='first'><a href='#' name='"+ firstPageNo +"'>맨 처음</a></span>";
			str += "<span class='prev'><a href='#' name='" + firstPageNo + "'>이전</a></span>";
        }
	
		for(var i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++){
			if(i==currentPageNo){
				str += "<span class='on'>" + i + "</span>";
	    	}else{
	    		str += "<span><a href='#' name='" + i + "'>" + i + "</a></span>";
	    	}
	    }
	
		if(lastPageNoOnPageList < totalPageCount){
			str += "<span class='next'><a href='#' name='" + (firstPageNoOnPageList + pageSize) +"'>다음</a></span>";
			str += "<span class='last'><a href='#' name='" + lastPageNo + "'>맨 마지막</a></span>";
        }else{
			str += "<span class='next'><a href='#' name='" + lastPageNo + "'>다음</a></span>";
			str += "<span class='last'><a href='#' name='" + lastPageNo + "'>맨 마지막</a></span>";
        }
	}
	return str;
}

//페이징 처리 - Mail part
function paginationRenderer2(totCnt, current, pageSize) {

	var totalPageCount = Math.floor((totCnt-1)/pageSize) + 1;
	var firstPageNo = 1;
	var currentPageNo = current;
	var firstPageNoOnPageList = Math.floor((currentPageNo-1)/pageSize) * pageSize + 1;
	var lastPageNoOnPageList = firstPageNoOnPageList + pageSize - 1;
	if(lastPageNoOnPageList > totalPageCount){
		lastPageNoOnPageList = totalPageCount;
	}
	var lastPageNo = totalPageCount;
	var str = "";
	
	if(totCnt > 0){
		if(firstPageNoOnPageList > pageSize){
			str += "<span class='first'><a href='#' name='"+ firstPageNo +"'>맨 처음</a></span>";
			str += "<span class='prev'><a href='#' name='" + (firstPageNoOnPageList-1) + "'>이전</a></span>";
        }else{
			str += "<span class='first'><a href='#' name='"+ firstPageNo +"'>맨 처음</a></span>";
			str += "<span class='prev'><a href='#' name='" + firstPageNo + "'>이전</a></span>";
        }
	
		for(var i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++){
			if(i==currentPageNo){
				str += "<span class='on'>" + i + "</span>";
	    	}else{
	    		str += "<span><a href='#' name='" + i + "'>" + i + "</a></span>";
	    	}
	    }
	
		if(lastPageNoOnPageList < totalPageCount){
			str += "<span class='next'><a href='#' name='" + (firstPageNoOnPageList + pageSize) +"'>다음</a></span>";
			str += "<span class='last'><a href='#' name='" + lastPageNo + "'>맨 마지막</a></span>";
        }else{
			str += "<span class='next'><a href='#' name='" + lastPageNo + "'>다음</a></span>";
			str += "<span class='last'><a href='#' name='" + lastPageNo + "'>맨 마지막</a></span>";
        }
	}
	return str;
}
////////************************** 2017.11.06 추가 수정 START
//selectBox
function selectList(data){
	var str = "";
	var code;
	var codeName;
	//str += "<select id=\"selectList\">";
	str += "<option value=\"all\">전체</option>";
	if(data != null){
		for ( var i = 0, len = data.length; i < len; i++) {			
			var result = data[i];
			code = result.code;
			codeName = result.name;
			/*if(code==null){
				code = result.lcls;
				if(code==null){
					code = result.mcls;
				}
			}
			if(codeName==null){
				codeName = result.cnslCatNm;
			}*/
			/*str += "<option value="+code+">"+codeName+"</option>";*/
			str += "<option value='"+ code +"'>"+ codeName +"</option>";
		}
	}
	//str += "</select>";
	return str;
}
//selectBox - social
function selectListSocial(data){
	var str = "";
	var code;
	var codeName;
	//str += "<select id=\"selectList\">";
	str += "<option value=\"all\">전체</option>";
	if(data != null){
		for ( var i = 0, len = data.length; i < len; i++) {			
			var result = data[i];
			code = result.code;
			codeName = result.name;
			/*if(code==null){
				code = result.lcls;
				if(code==null){
					code = result.mcls;
				}
			}
			if(codeName==null){
				codeName = result.cnslCatNm;
			}*/
			/*str += "<option value="+code+">"+codeName+"</option>";*/
			str += "<option value='"+ codeName +"'>"+ codeName +"</option>";
		}
	}
	//str += "</select>";
	return str;
}

$(function () {
	$( window ).load(function() {
		
		//selectBox 접수채널 change 이벤트 
		$(document).on("change","#vocChannelList",function(){  
			var channel = $("#vocChannelList option:selected").val();	// 접수채널 값
			var type = "";		// 셀렉트박스 구분코드
			var code = "";		// 셀렉트박스 선택코드
			
			// 중분류, 소분류 disabled
			$("#vocRecTypeList").attr("disabled", true);
			$('#vocRecTypeList').find('option:first').attr('selected',true);
			$("#vocPartList").attr("disabled", true);
			$('#vocPartList').find('option:first').attr('selected',true);
			$("#vocItemList").attr("disabled", true);
			$('#vocItemList').find('option:first').attr('selected',true);
			
			// 접수 채널이 콜센터 : 만족도 비활성, voc 종류 비활성, 대분류 리로드
			if(channel == '101'){
				$('#repLevelList').find('option:first').attr('selected',true);
				$('#vocRecTypeList').find('option:first').attr('selected',true);
				$("#repLevelList").attr("disabled", true);
				$("#vocRecTypeList ").attr("disabled", true);
				type = "CHANNEL";
				code = "CALL_TYPE";
			}else{
				if(channel == 'all'){	//전체 선택시 "VOC종류", "만족도등급" 비활성화
					$('#vocRecTypeList').find('option:first').attr('selected',true);
					$("#vocRecTypeList ").attr("disabled", true);
					$('#repLevelList').find('option:first').attr('selected',true);
					$("#repLevelList ").attr("disabled", true);
				}
				else{
					$("#vocRecTypeList ").attr("disabled", false);
					$("#repLevelList").attr("disabled", false);
				}
				$("#vocKindList").attr("disabled", false);
				type = "CHANNEL";
				code = "CDVOCCHANNEL";
			}
		
			var formData = "type="+type+"&code="+code;
			// 대분류 load
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/common/selectOptionList.do",
				data : formData,
				//contentType: 'application/json;charset=utf-8',
				//mimeType: 'application/json',
				success : function(response) {				
					$('#vocKindList').html(selectList(response.vocKindList));	// 대분류
				    $('#vocKindList').find('option:first').attr('selected',true);
				}
			}); 
		
			
		}); 
		//selectBox 접수 대분류 change 이벤트 
		$(document).on("change","#vocKindList",function(){  	
			var channel = $("#vocChannelList  option:selected").val();	// 접수채널 값
			var type = "";															// 셀렉트 박스 구분값
			if(channel.length == 3) type = "CALL_KIND";
			else type = "MINWON_KIND";
			var code = $("#vocKindList option:selected").val(); 					// 대분류 코드(부모1차코드로 사용)

			$('#vocItemList').find('option:first').attr('selected',true);
			$("#vocItemList").attr("disabled", true);
			$("#vocPartList").attr("disabled", false);
			
			var formData = "type="+type+"&code="+code;
			// 중분류 load
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/common/selectOptionList.do",
				data : formData,
				//contentType: 'application/json;charset=utf-8',
				//mimeType: 'application/json',
				success : function(response) {		
					
					$('#vocPartList').html(selectList(response.vocPartList));	// 중분류
				    $('#vocPartList').find('option:first').attr('selected',true);
				}
			}); 
		}); 
		//selectBox 접수 중분류 change 이벤트 
		$(document).on("change","#vocPartList",function(){  		
			var channel = $("#vocChannelList  option:selected").val();	// 접수채널 값
			var type = "";															// 셀렉트 박스 구분값
			if(channel.length == 3) type = "CALL_PART";
			else type = "MINWON_PART";
			var code = $("#vocKindList option:selected").val(); 	// 대분류 코드(부모1차코드로 사용)
			var codePart = $("#vocPartList option:selected").val(); 	// 중분류 코드(부모2차코드로 사용)
			$("#vocItemList").attr("disabled", false);
			var formData = "type="+type+"&code="+code+"&codePart="+codePart;
			// 중분류 load
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/common/selectOptionList.do",
				data: formData,
				//contentType: 'application/json;charset=utf-8',
				//mimeType: 'application/json',
				success : function(response) {		
					
					$('#vocItemList').html(selectList(response.vocItemList));	// 소분류
				    $('#vocItemList').find('option:first').attr('selected',true);
				}
			}); 
		});
		
		
		
	});
});
////////************************** 2017.11.06 추가 수정 END
