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

//selectBox
function selectList(data){
	var str = "";
	var code;
	var codeName;
	str += "<select id=\"selectList\">";
	str += "<option value=\"all\">전체</option>";
	for ( var i = 0, len = data.length; i < len; i++) {			
		var result = data[i];
		code = result.cdTp;
		codeName = result.cdKnm;
		if(code==null){
			code = result.lcls;
			if(code==null){
				code = result.mcls;
			}
		}
		if(codeName==null){
			codeName = result.cnslCatNm;
		}
		/*str += "<option value="+code+">"+codeName+"</option>";*/
		str += "<option value='"+codeName+"'>"+code+"</option>";
	}
	str += "</select>";
	return str;
}
