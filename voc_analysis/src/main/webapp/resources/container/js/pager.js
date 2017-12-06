pagingView = function(zone, options) {
	 var startIndex = options.startIndex;
	 var pagingHTML 		= "";
	 var list_function = options.func;//리스트 호출에 사용할 함수
	 var page 			= parseInt(Number(options.page)); // 현재 페이지
	 
	 var totalCount		 = parseInt(Number(options.total)); //실제 데이터 총 갯수
	 
	 var pageBlock		 = parseInt(Number(options.view)); //화면에 보여질 전체 갯수
	 
	 var navigatorNum    = 10; // 그룹 번호 갯수

	 var firstPageNum	 = 1;
	 
	 var lastPageNum		 = Math.floor((totalCount-1)/pageBlock) + 1;

	 var previewPageNum  = page == 1 ? 1 : page-1;

	 var nextPageNum		 = page == lastPageNum ? lastPageNum : page+1;

	 var indexNum		 = startIndex <= navigatorNum  ? 0 : parseInt((startIndex-1)/navigatorNum) * navigatorNum;
	
	 
	 if (totalCount > 1) {
		 //맨앞으로. 이전 버튼 생성 사용않는다면 지워도 무방.
		 if (startIndex > 1) {
			 pagingHTML += '<span class="first"><a href="#" onClick="javascipt:'+list_function+'('+firstPageNum+');">맨 처음</a></span>'; 
			 pagingHTML += '<span class="prev"><a href="#" onClick="javascipt:'+list_function+'('+previewPageNum+');">이전</a></span>';
		 }else{
			 pagingHTML += '<span class="first"><a href="#">맨 처음</a></span>'; 
			 pagingHTML += '<span class="prev"><a href="#">이전</a></span>';
		 }
		 
		//페이지 처리.
//		 pagingHTML += '';
		 
		 for (var i=1; i<=navigatorNum; i++) {

			 var pageNum = i + indexNum;

			 if (pageNum == startIndex){ 
				 pagingHTML += '<span class="on">'+pageNum+'</span>';
			 } else { 
				 pagingHTML += '<span><a href="#" onClick="javascipt:'+list_function+'('+pageNum+');">'+pageNum+'</a></span>';
			 }
			 
			 if (pageNum==lastPageNum){
				 break;
			 }
		 }

		//맨뒤로 다음 버튼 생성 사용않는다면 지워도 무방.
		 if (startIndex < lastPageNum) {
			 pagingHTML += '<span class="next"><a href="#" onClick="javascipt:'+list_function+'('+nextPageNum+');">다음</a></span>';
			 pagingHTML += '<span class="last"><a href="#" onClick="javascipt:'+list_function+'('+lastPageNum+');">맨 마지막</a></span>';
		 }else{
			 pagingHTML += '<span class="next"><a href="#" >다음</a></span>';
			 pagingHTML += '<span class="last"><a href="#">맨 마지막</a></span>';
		 }
	 }
	 $(zone).html(pagingHTML);
};
