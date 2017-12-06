$(function () {
	
	// 초기화
	$( window ).load(function() {
		search();
	});
	
	// 등록 버튼 클릭
	$('#btnClear').on('click', function(){	
		$('#keyword').val("");
		/*$('#needsTypeList > select').find('option:first').attr('selected',true);*/
		$("#detail_02").prop("checked", true);
		$('#etc').val("");
		$('#no').val("");
	});
	
	// 저장 버튼 클릭
	$('#btnSave').on('click', function(){
		var url = "";
		var msg = "";
		var	keyword = $('#keyword').val();
		/*var needsType = $('#needsTypeList > select option:selected').val();*/
		var orgNm = $('#orgNm').val();
		var useYn = $('input:radio[name=useYn]:checked').val();
		var regNm = $('#regNm').val();
		var regId = $('#regId').val();
		var etc = $('#etc').val();
		var no = $('#no').val();
		var useYnTemp = $('#useYnTemp').val();
		var json = {"keyword":keyword,
					/*"needsType":needsType,*/
					"orgNm":orgNm,
					"useYn":useYn,
					"regId":regId,
					"regNm":regNm,
					"etc":etc, 
					"no":no,
					"useYnTemp":useYnTemp};
		if(no != undefined && no > 0){
			url = "/management/updateAlarmKeyword.do";
			msg = "수정";
		}else{
			url = "/management/addAlarmKeyword.do";
			msg = "등록";
		}
		
		if(validate()){
			// add
			$.ajax({
				type : "POST", 
				url : getContextPath() + url,
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) == 100){
						alert("활성 키워드는 최대 10개까지만 등록 가능합니다.");
						search();
					}else {
						if(parseInt(response) > 0) {
							alert(msg + "되었습니다.");
							search();
							$('#keyword').val("");
							/*$('#needsTypeList > select').find('option:first').attr('selected',true);*/
							$("#detail_02").prop("checked", true);
							$('#etc').val("");
							$('#no').val("");
							$('#useYnTemp').val("");
						}
					}
				}
			});
		}
	});
	
	// 삭제 클릭
	$('#btnDelete').on('click', function(){
		
		var selectedNo = new Array();
		
		$('[name="selectedNo"]:checked').each(function(){
			selectedNo.push($(this).attr('value'));
		});
		if(selectedNo.length>0){
			var json = {"selectedNo":selectedNo};
			
			// del
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/deleteAlarmKeyword.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) > 0) {
						alert("삭제되었습니다.");
						// 재검색
						search();
						$('#keyword').val("");
						/*$('#needsTypeList > select').find('option:first').attr('selected',true);*/
						$("#detail_02").prop("checked", true);
						$('#etc').val("");
						$('#no').val("");
						$('#useYnTemp').val("");
					}
				}
			});
		}
	});

	// 리스트 클릭
	$('#list_contents tr').live('click', function(){
		
		var json = {"no":$(this).attr("id")};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectAlarmKeyword.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#keyword').val(response.keyword);
				/*$('#needsTypeList > select > option[value='+response.needsType+']').attr('selected',true);*/
				$('#no').val(response.no);
				$('input:radio[name=useYn]').each(function() {
					if($(this).attr('value') == response.useYn){
						$(this).prop("checked", true);
					}
				});
				$('#etc').val(response.etc);
				$('#contents_orgNm').html(response.orgNm);
				$('#contents_regNm').html(response.regNm+"("+response.regId+")");
				$('#useYnTemp').val(response.useYn);
				
			}
		});
		return true;
	});
	
	// 페이지 번호 클릭
	$('#paging span').live('click', function(){
		
		var	keyword = $('#term').val();
		var pageSize = 10;
		var currentPageNo = $(this).find('a').attr('name');
		if(currentPageNo != undefined){
			var json = {"keyword":keyword,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo};
			
			$.ajax({
				type : "POST", 
				url : getContextPath() + "/management/selectAlarmKeywordList.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					
					$('#list_contents').html(getResultList(response.resultList, currentPageNo));
					$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
					$('input[id=selectAll]:checkbox').prop("checked", false);
					//input창 초기화
					$('#keyword').val("");
					/*$('#needsTypeList > select').find('option:first').attr('selected',true);*/
					$("#detail_02").prop("checked", true);
					$('#etc').val("");
					$('#no').val("");
					
				}
			});
		}
		
		return false;
	});
	
	// 전체선택
	$('input[id=selectAll]:checkbox').change(function () {
		if ($(this).attr("checked")) {
			$('[name="selectedNo"]').each(function(){
				$(this).prop("checked", true);
			});
		}else{
			$('[name="selectedNo"]').each(function(){
				$(this).prop("checked", false);
			});
		}
	});
	
	// 검색
	function search(){
		var pageSize = 10;
		var currentPageNo = 1;
		
		var json = {"pageSize":pageSize, 
					"currentPageNo":currentPageNo};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectAlarmKeywordList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#list_contents').html(getResultList(response.resultList, currentPageNo));
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
				$('#totCnt').html(response.totSize);
				$('input[id=selectAll]:checkbox').prop("checked", false);
				/*$('#needsTypeList').html(selectList(response.needsTypeList));*/
			}
		});
	}
	
	// 유효성 체크
	function validate(){

		if($('#keyword').val().length <= 0){
			alert("키워드를 입력 해 주세요.");
			return false;
		}
		
		/*if($('#needsTypeList').val().length <= 0){
			alert("VOC유형을 입력 해 주세요.");
			return false;
		}*/
		
		if($('input:radio[name=useYn]:checked').length <= 0){
			alert("활성여부를 선택 해 주세요.");
			return false;
		}
		
		return true;
	}
	
	// 리스트 표시
	function getResultList(data, currentPageNo){
		var str = "";
		for ( var i = 0, len = data.length; i < len; i++) {			
			var result = data[i];
			
			str += "<tr id='"+ result.no + "'>";
			str += "<td><input type='checkbox' name='selectedNo' value='"+ result.no +"'/></td>";
			str += "<td>" + (((currentPageNo-1)*10)+(i+1)) + "</td>";
			/*str += "<td>" + result.needsTypeName + "</td>";*/
			str += "<td>" + result.keyword + "</td>";
			if(result.etc != null) str += "<td class='align_l'>" + result.etc + "</td>";
			else str += "<td></td>";
			//str += "<td>" + result.orgNm + "</td>";
			str += "<td>" + result.regNm  + result.regId + "</td>";
			str += "<td>" + result.regDate + "</td>";
			if(result.useYn == 'Y'){
				str += "<td><span class='td_able'>활성</span></td>";
			}else{
				str += "<td><span class='td_disable'>비활성</span></td>";
			}
			str += "</tr>";
		}
		return str;
	}
	
});