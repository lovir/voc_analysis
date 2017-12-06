$(function () {
	
	// 초기화
	$( window ).load(function() {
		fnSearch();
		return false;
	});
	
	// 검색어 입력 후 엔터
	$('#searchKeyword').keypress(function(e){
		if(e.which==13){
			fnSearch();
		}
	});
	
	// 전체 적용 버튼 클릭
	$('#btnApply').on('click', function(){

		var json = {};
	
		// del
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/applyUserDictionary.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				if(parseInt(response) > 0) {
					alert("적용 명령을 서버에 등록하였습니다. 적용 처리가 완료될때까지는 시간이 걸릴 수 있습니다.");
					// 재검색
					fnSearch();
				}
			}
		});
		return false;
	});
	
	// 선택 삭제 버튼 클릭
	$('#btnSelectedDelete').on('click', function(){
		
		var selectedKeyword = new Array();
		
		$('[name="selectedKeyword"]:checked').each(function(){
			selectedKeyword.push($(this).attr('value'));
		});

		if(selectedKeyword.length>0){
			var json = {"selectedKeyword":selectedKeyword};
		
			// del
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/deleteUserDictionary.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) > 0) {
						alert("삭제되었습니다.");
						// 재검색
						fnSearch();
					}
				}
			});
		}
		return false;
	});
	
	// 등록 버튼 클릭
	$('#btnAdd').on('click', function(){
		
		var keyword = $('#keyword').val();
		
		if(keyword.length <= 0){
			alert("검색어를 입력하세요.");
			return false;
		}else{
			var json = {"keyword":keyword};
			
			// 형태소, 색인어휘 분석
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/addUserKeyword.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) == 1) {
						alert("등록되었습니다.");
						// 재검색
						fnSearch();
					}else if(parseInt(response) == 2){
						alert("이미 존재하는 키워드 입니다!");
					}
				}
			});
			return false;
		}
	});
	
	// 키워드 입력시
	$('#keyword').on('input keyup blur', $(this), function(){

		var temp = $(this).val();
		temp = temp.replace(/\s/g, "");
		temp = temp.toUpperCase();
		
		if( temp != $(this).val() ){
			$(this).val(temp);
		}
		
		fnCheckByte(50, "키워드");
		
		var json = {"keyword":$(this).val()};
		
		// 형태소, 색인어휘 분석
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/extractorUserKeyword.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#morpheme').html(response.morpheme);
				$('#extractor').text(response.extractor);
			}
		});
		return false;
	});
	
	// 검색키워드 입력시
	$('#searchKeyword').on('input keyup blur', $(this), function(){

		var temp = $(this).val();
		temp = temp.replace(/\s/g, "");
		temp = temp.toUpperCase();
		
		if( temp != $(this).val() ){
			$(this).val(temp);
		}
		
		var json = {"keyword":$(this).val()};
		
		// 형태소, 색인어휘 분석
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/extractorUserKeyword.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#morpheme').html(response.morpheme);
				$('#extractor').text(response.extractor);
			}
		});
		return false;
	});
	
	// 보기 버튼 클릭(키워드 검색)
	$('#btnSearchKeyword').on('click', function(){

		var searchFlg = true;
		
		$('#keywordList td:first-child').each(function() {
			if($(this).text() == $('#viewKeyword').val()){
				searchFlg = false;
			}
		});
		
		if(searchFlg){
			var json = {"keyword":$('#viewKeyword').val()};
			
			// 형태소, 색인어휘 분석
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/extractorUserKeyword.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {				
					var str = "";
					str += "<tr>";
					str += "<td>" + $('#viewKeyword').val() + "</td>";
					str += "<td>" + response.morpheme + "</td>";
					str += "<td>"+ response.extractor + "</td>";
					str += "</tr>";
					
					$('#keywordList').append(str);
				}
			});
		}
		return false;
	});
	
	// 검색 버튼 클릭
	$('#btnSearch').on('click', function(){
		fnSearch();
		return false;
	});
	
	// 검색
	function fnSearch(){
		var pageSize = $('#pageSize option:selected').val();
		var currentPageNo = 1;
		var searchKeyword = $("#searchKeyword").val();
		var searchSort = $('#searchSort option:selected').val();
		
		var json = {"pageSize":pageSize, 
					"currentPageNo":currentPageNo,
					"keyword":searchKeyword,
					"sort":searchSort};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectUserDictionaryList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				
				var str = "";
				var data = response.list;
				
				for ( var i = 0, len = data.length; i < len; i++) {
					var result = data[i];
					str += "<tr id='"+ result.keyword + "'>";
					str += "<td><input type='checkbox' name='selectedKeyword' value='" + result.keyword + "'/></td>";
					str += "<td>" + result.keyword + "</td>";
					if(result.apply){
						str += "<td>적용</td>";
					}else{
						str += "<td>미적용</td>";
					}
					
					str += "<td>" + result.regDate + "</td>";
					str += "</tr>";
				}
				
				$('#userList').html(str);
				$('#userCount').html(response.totSize);
				totalPageNo = parseInt(response.totSize/pageSize)+1;
				$('#userCurrent').html(currentPageNo + "/" + totalPageNo + "페이지");
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
			}
		});
	}
	
	// 길이 제한 스크립트
	function fnCheckByte(ari_max, desc){
		var ls_str = $('#keyword').val(); // 이벤트가 일어난 컨트롤의 value 값
		var li_str_len = $('#keyword').val().length;  // 전체길이
		
		// 변수초기화
		var li_max = ari_max; // 제한할 글자수 크기
		var i = 0; // for문에 사용
		var li_byte = 0; // 한글일경우는 2 그밗에는 1을 더함
		var li_len = 0; // substring하기 위해서 사용
		var ls_one_char = ""; // 한글자씩 검사한다
		var ls_str2 = ""; // 글자수를 초과하면 제한할수 글자전까지만 보여준다.
		
		for(i=0; i< li_str_len; i++){
			// 한글자추출
			ls_one_char = ls_str.charAt(i);
		
			// 한글이면 2를 더한다.
			if (escape(ls_one_char).length > 4){
				li_byte = li_byte+2;
			// 그외의 경우는 1을 더한다.
			}else{
				li_byte++;
			}
		
			// 전체 크기가 li_max를 넘지않으면
			if(li_byte <= li_max){
				li_len = i + 1;
			}
		}
		
		// 전체길이를 초과하면
		if(li_byte > li_max)
		{
		alert("'"+desc+"' 은(는) " + li_max + " Byte(한글:2Byte, 기타:1Byte)를 초과 입력할수 없습니다. \n 초과된 내용은 자동으로 삭제 됩니다. ");
		ls_str2 = ls_str.substr(0, li_len);
		$('#keyword').val(ls_str2);
		}
		//$('#addKeyword').focus();
	}
});