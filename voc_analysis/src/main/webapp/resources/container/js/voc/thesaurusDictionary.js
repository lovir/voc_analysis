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
	
	// 유의어 사전 편집 추가 버튼 클릭
	$('#btnAddEditKeyword').on('click', function(){
		
		var addFlag = 0;
		
		if($('#keyword').val().length>0){
			if($('#addKeyword').val().length>0){
				$('#thesaurusKeywordList li').each(function(){
					if($('#addKeyword').val() == $(this).text()){
						addFlag = 1;
					}
				});
				
				if(addFlag>0){
					alert("추가하려는 키워드가 존재합니다.");
					$('#addKeyword').val("");
					$('#morpheme').text("");
					$('#extractor').text("");	
				}else{
					if(parseInt($('#chFlag').val()) > 0){ // 국민은행주식
						alert("키워드의 형태소분석결과가 서로 일치하지 않습니다.\n해당 키워드는 사용할 수없습니다.");
					}else{
						if(parseInt($('#chAddFlag').val()) > 0){ // 국민은행주식회사
							alert("추가된 유의어의 형태소 분석결과가 서로 일치하지 않습니다.\n해당 유의어를 삭제하시기 바랍니다.");
						}else{
							
							var json = {"keyword":$('#addKeyword').val()};
							
							// 형태소, 색인어휘 분석
							$.ajax({
								type : "POST", 
								url : getContextPath() + "/management/selectThesaurusAddKeyword.do",
								dataType: 'json', 
								data: JSON.stringify(json), 
								contentType: 'application/json;charset=utf-8',
								mimeType: 'application/json',
								success : function(response) {
									if(response.chAddFlag == 1){
										$('#thesaurusKeywordList').append("<li style='color:red;' value='1'>" + $('#addKeyword').val() + "<a href='#' id='del'><img src='" + getImgPath() + "btn_close.gif' width='5' height='5' alt='키워드 삭제' /></a></li>");
										$('#btnAdd').attr("style", "color:#d2d2d2 !important;");
										$("#btnAdd").attr('disabled', 'disabled');
										var chAddFlag = parseInt(response.chAddFlag) + parseInt($('#chAddFlag').val());
										$('#chAddFlag').val(chAddFlag);
									}else{
										$('#thesaurusKeywordList').append("<li  value='0'>" + $('#addKeyword').val() + "<a href='#' id='del'><img src='" + getImgPath() + "btn_close.gif' width='5' height='5' alt='키워드 삭제' /></a></li>");
										$('#btnAdd').attr("style", "color:#676767 !important;");
										$("#btnAdd").removeAttr("disabled");
									}
									$('#addKeyword').val("");
									$('#morpheme').text("");
									$('#extractor').text("");	
								}
							});
						}
					}
				}
			}else{
				alert("단어를 입력하세요.");
			}
		}else{
			alert("키워드를 선택하거나 추가하세요.");
		}
		
		return false;
	});

	// 등록 버튼 클릭
	$('#btnAdd').on('click', function(){
		
		var keyword = $('#keyword').val();
		var way = $('input:radio[name=way]:checked').val();
		var regDate = $('#regDate').val();
		var thesaurusKeywordList = new Array();
		
		$('#thesaurusKeywordList li').each(function(){
			thesaurusKeywordList.push($(this).text());
		});
		
		var json = {"keyword":keyword,
				"thesaurusKeywordList":thesaurusKeywordList,
				"way":way,
				"regDate":regDate};
		
		if($(this).attr('disabled') != 'disabled'){
			// 형태소, 색인어휘 분석
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/addThesaurusKeyword.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) > 0) {
						alert("등록되었습니다.");
						// 재검색
						fnSearch();
					}
				}
			});
		}
		return false;
	});
	
	// 전체 삭제 클릭
	$('#btnAllDel').on('click', function(){
		$('#thesaurusKeywordList li').remove();
		$('#btnAdd').attr("style", "color:#d2d2d2 !important;");
		$("#btnAdd").attr('disabled', 'disabled');
		return false;
	});
	
	// 클릭 된 유의어 삭제
	$('#del').live('click', function(){
		if($(this).parent().val() == '1'){
			$('#chAddFlag').val(parseInt($('#chAddFlag').val())-1);
		}
		$(this).parent().remove();
		if($('#thesaurusKeywordList li').length==0){
			$('#btnAdd').attr("style", "color:#d2d2d2 !important;");
			$("#btnAdd").attr('disabled', 'disabled');
		}else{
			if(parseInt($('#chAddFlag').val()) > 0){
				$('#btnAdd').attr("style", "color:#d2d2d2 !important;");
				$("#btnAdd").attr('disabled', 'disabled');
			}else{
				$('#btnAdd').attr("style", "color:#676767 !important;");
				$("#btnAdd").removeAttr("disabled");
			}
		}
		return false;
	});
	
	// 검색어 입력시
	$('#searchKeyword').on('input keyup blur', $(this), function(){

		var temp = $(this).val();
		temp = temp.replace(/\s/g, "");
		temp = temp.toUpperCase();
		
		if( temp != $(this).val() ){
			$(this).val(temp);
		}
	});
	
	// 키워드 입력시
	$('#addKeyword').on('input keyup blur', $(this), function(){
		
		var temp = $(this).val();
		temp = temp.replace(/\s/g, "");
		temp = temp.toUpperCase();
		
		if( temp != $(this).val() ){
			$(this).val(temp);
		}
		
		fnCheckByte(50, "키워드");
		
		if(fnAddEditKeywordWithLength()){
			var json = {"keyword":$('#addKeyword').val()};
			
			// 형태소, 색인어휘 분석
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/extractorThesaurusKeyword.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					$('#morpheme').html(response.morpheme);
					$('#extractor').text(response.extractor);
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
	
	// 유의어 사전 리스트 추가 버튼 클릭
	$('#btnAddKeyword').on('click', function(){
		
		// 유의어 사전 편집 키워드 표시
		var selectedKeyword = $('#searchKeyword').val();
		$('#keyword').val(selectedKeyword);
		$('#addKeyword').val("");
		$('#morpheme').text("");
		$('#extractor').text("");
		$('#thesaurusKeywordList').text("");
		$('#regDate').val("0");
		
		var json = {"keyword":selectedKeyword};
		
		// 유의어 사전 편집
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectThesaurusChKeyword.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#chKeyword').text(response.chKeyword);
				
				if(response.chFlag == 1){
					$('#chFlag').val('1');
					$('#chKeyword').attr("style", "color:red;");
					$('#btnAdd').attr("style", "color:#d2d2d2 !important;");
					$("#btnAdd").attr('disabled', 'disabled');
				}else{
					$('#chFlag').val('0');
					$('#chKeyword').removeAttr("style");
					$('#btnAdd').attr("style", "color:#676767 !important;");
					$("#btnAdd").removeAttr("disabled");
				}
				
				$('#addKeyword').val("");
				$('#morpheme').text("");
				$('#extractor').text("");	
				
				var data = response.thesaurusKeywordList;
				if(data != null){
					for ( var i = 0, len = data.length; i < len; i++) {
						var result = data[i];
						$('#thesaurusKeywordList').append("<li>" + result + "<a href='#' id='del'><img src='" + getImgPath() + "btn_close.gif' width='5' height='5' alt='키워드 삭제' /></a></li>");
					}
					$('#btnAdd').attr("style", "color:#676767 !important;");
					$("#btnAdd").removeAttr("disabled");
				}else{
					$('#btnAdd').attr("style", "color:#d2d2d2 !important;");
					$("#btnAdd").attr('disabled', 'disabled');
				}
			}
		});
		return false;
	});
	
	// 전체 적용 버튼 클릭
	$('#btnApply').on('click', function(){

		var json = {};
	
		// del
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/applyThesaurusDictionary.do",
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
				url : getContextPath() + "/management/deleteThesaurusDictionary.do",
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
	
	// 리스트 클릭
	$('#thesaurusList tr').live('click', function(){
		
		var json = {"keyword":$(this).attr("id")};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectThesaurusDictionary.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				
				$('#keyword').val(response.keyword);
				$('#chKeyword').text(response.chKeyword);
				$('#regDate').val(response.regDate);
				
				var data = response.thesaurusKeywordList;
				
				$('#thesaurusKeywordList').text("");
				for ( var i = 0, len = data.length; i < len; i++) {			
					var result = data[i];
					$('#thesaurusKeywordList').append("<li>" + result + "<a href='#' id='del'><img src='" + getImgPath() + "btn_close.gif' width='5' height='5' alt='키워드 삭제' /></a></li>");
				}
				$('#btnAdd').attr("style", "color:#676767 !important;");
				$("#btnAdd").removeAttr("disabled");
			}
		});
	});
	
	// 페이지 번호 클릭
	$('#paging span').live('click', function(){
		
		var searchKeyword = $("#searchKeyword").val();
		var searchSort = $('#searchSort option:selected').val();
		var pageSize = $('#pageSize option:selected').val();
		var currentPageNo = $(this).find('a').attr('name');
		
		var json = {"pageSize":pageSize, 
				"currentPageNo":currentPageNo,
				"keyword":searchKeyword,
				"sort":searchSort};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectThesaurusDictionaryList.do",
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
					str += "<td>" + result.thesaurusCount + "</td>";
					if(result.apply){
						str += "<td>적용</td>";
					}else{
						str += "<td>미적용</td>";
					}
					
					str += "<td>" + result.regDate + "</td>";
					str += "</tr>";
				}
				
				$('#thesaurusList').html(str);
				$('#thesaurusCount').html(response.totSize);
				totalPageNo = parseInt(response.totSize/pageSize)+1;
				$('#thesaurusCurrent').html(currentPageNo + "/" + totalPageNo + "페이지");
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
			}
		});;
		
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
			url : getContextPath() + "/management/selectThesaurusDictionaryList.do",
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
					str += "<td>" + result.thesaurusCount + "</td>";
					if(result.apply){
						str += "<td>적용</td>";
					}else{
						str += "<td>미적용</td>";
					}
					
					str += "<td>" + result.regDate + "</td>";
					str += "</tr>";
				}
				
				$('#thesaurusList').html(str);
				$('#thesaurusCount').html(response.totSize);
				totalPageNo = parseInt(response.totSize/pageSize)+1;
				$('#thesaurusCurrent').html(currentPageNo + "/" + totalPageNo + "페이지");
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
			}
		});
	}

	// 유사어사전, 추천어 사전 - 편집 추가
	function fnAddEditKeywordWithLength(){
		// 2010. 12. 24. 추천어로 comma(,)가 들어오면 입력이 안되도록 수정. by kkundi
		var keyword = $('#addKeyword').val();
		if(keyword.indexOf(',') > 0) {
			alert("키워드에 comma(',')가 포함될 수 없습니다.");
			$('#addKeyword').val("");
			return false;
		}
		return true;
	}

	// 길이 제한 스크립트
	function fnCheckByte(ari_max, desc){
		var ls_str = $('#addKeyword').val(); // 이벤트가 일어난 컨트롤의 value 값
		var li_str_len = $('#addKeyword').val().length;  // 전체길이
		
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
		$('#addKeyword').val(ls_str2);
		}
		//$('#addKeyword').focus();
	}
});