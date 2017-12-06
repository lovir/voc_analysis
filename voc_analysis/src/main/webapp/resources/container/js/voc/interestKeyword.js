$(function () {
	
	// 초기화
	$( window ).load(function() {
		search();
		$("#detail_02").prop("checked", true); // 활성 여부
	});
	
	// 검색 버튼 클릭
	$('#btnSearch').on('click', function(){
		var	keyword = $('#term').val();
		var pageSize = $('#pageSize option:selected').val();
		var currentPageNo = 1;
		var condition = $('#condition option:selected').val();
		
		var json = {"keyword":keyword,
					"condition":condition,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectInterestKeywordList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				
				$('#list_contents').html(getResultList(response.resultList, currentPageNo));
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
				$('#totCnt').html(response.totSize);
				$('input[id=selectAll]:checkbox').prop("checked", false);
				
			}
		});
	});
	
	// 검색어 입력 후 엔터
	$('#term').keypress(function(e){
		if(e.which==13){
			search();
		}
	});
	
	// 신규 등록 버튼 클릭
	$('#btnClear').on('click', function(){	
		$('#contents_regNm').text($('#regId').val()+'('+$('#regNm').val()+')');
		$('#contents_orgNm').val($('#orgNm').val());
		$('#keyword').val("");
		$("#detail_02").prop("checked", true);
		$('#etc').val("");
		$('#no').val("");
	});
	
	// 저장 버튼 클릭
	$('#btnSave').on('click', function(){
		
		var url = "";
		var msg = "";
		var	keyword = $('#keyword').val();
		var orgNm = $('#orgNm').val();
		var useYn = $('input:radio[name=useYn]:checked').val();
		var regNm = $('#regNm').val();
		var regId = $('#regId').val();
		var etc = $('#etc').val();
		var no = $('#no').val();
		
		var json = {"keyword":keyword,
					"orgNm":orgNm,
					"useYn":useYn,
					"regId":regId,
					"regNm":regNm,
					"etc":etc, 
					"no":no};
		
		if(no != undefined && no > 0){
			url = "/management/updateInterestKeyword.do";
			msg = "수정";
		}else{
			url = "/management/addInterestKeyword.do";
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
					if(parseInt(response) > 0) {
						alert(msg + " 되었습니다.");
						search();
					}else{
						alert("활성 가능한 갯수를 초과 하였습니다.");
					}
				}
	 		});
		}
	});
	
	// 대시보드 활성/비활성 클릭
	$('[name="btnApplyY"], [name="btnApplyN"]').live('click', function(){
		var url = "/management/updateInterestKeywordDashYn.do";
		var msg = "대시보드 적용이 비활성화";
		var dashYn = 'N';
		
		if($(this).attr("name") == 'btnApplyN'){
			msg = "대시보드 적용이 활성화";
			dashYn = 'Y';
		}

		var no = $(this).parent().parent().attr("id");
		
		var json = {"dashYn":dashYn, 
					"no":no};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + url,
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				if(parseInt(response) > 0) {
					alert(msg + " 되었습니다.");
					search();
				}else{
					alert("대시보드에 활성 가능한 갯수를 초과 하였습니다.");
				}
			}
 		});
		return false;
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
				url : getContextPath() + "/management/deleteInterestKeyword.do",
				dataType: 'json', 
				data: JSON.stringify(json), 
				contentType: 'application/json;charset=utf-8',
				mimeType: 'application/json',
				success : function(response) {
					if(parseInt(response) > 0) {
						alert("삭제되었습니다.");
						// 재검색
						search();
					}
				}
			});
		}
	});
	
	// 표시 건수 변경
	$('#pageSize').change(function() {
		var	keyword = $('#term').val();
		var pageSize = $('#pageSize option:selected').val();
		var currentPageNo = 1;
		
		var json = {"keyword":keyword,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectInterestKeywordList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#list_contents').html(getResultList(response.resultList, currentPageNo));
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
			}
		});
	});

	// 리스트 클릭
	$('#list_contents tr').live('click', function(){
		
		var json = {"no":$(this).attr("id")};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectInterestKeyword.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {

				$('#keyword').val(response.keyword);
				$('#no').val(response.no);
				$('input:radio[name=useYn]').each(function() {
					if($(this).attr('value') == response.useYn){
						$(this).prop("checked", true);
					}
				});
				$('#etc').val(response.etc);
				$('#contents_orgNm').html(response.orgNm);
				$('#contents_regNm').html(response.regNm+"("+response.regId+")");
				
			}
		});
		return true;
	});
	
	// 페이지 번호 클릭
	$('#paging span').live('click', function(){
		
		var	keyword = $('#term').val();
		var pageSize = $('#pageSize option:selected').val();
		var currentPageNo = $(this).find('a').attr('name');
		
		var json = {"keyword":keyword,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectInterestKeywordList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#list_contents').html(getResultList(response.resultList, currentPageNo));
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
				$('input[id=selectAll]:checkbox').prop("checked", false);
			}
		});
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
	
	// 유효성 체크
	function validate(){
		if($('#keyword').val().length <= 0){
			alert("키워드를 입력 해 주세요.");
			return false;
		}
		
		if($('input:radio[name=useYn]:checked').length <= 0){
			alert("활성여부를 선택 해 주세요.");
			return false;
		}
		
/*		if($('#etc').val().length <= 0){
			alert("비고를 입력 해 주세요.");
			return false;
		}*/
		return true;
	}
	
	function search(){
		var	keyword = $('#term').val();
		var pageSize = $('#pageSize option:selected').val();
		var currentPageNo = 1;
		var condition = $('#condition option:selected').val();
		
		var json = {"keyword":keyword,
					"condition":condition,
					"pageSize":pageSize, 
					"currentPageNo":currentPageNo};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectInterestKeywordList.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				
				$('#list_contents').html(getResultList(response.resultList, currentPageNo));
				$('#paging').html(paginationRenderer(response.totSize, currentPageNo));
				$('#totCnt').html(response.totSize);
				$('input[id=selectAll]:checkbox').prop("checked", false);
				
			}
		});
	}
	
	// 리스트 표시
	function getResultList(data, currentPageNo){
		var str = "";
			
		for ( var i = 0, len = data.length; i < len; i++) {			
			var result = data[i];
			
			str += "<tr id='"+ result.no + "'>";
			str += "<td><input type='checkbox' name='selectedNo' value='"+ result.no +"'/></td>";
			str += "<td>" + (((currentPageNo-1)*10)+(i+1)) + "</td>";
			str += "<td class='align_l'>" + result.keyword + "</td>";
			str += "<td class='align_l'>" + result.etc + "</td>";
			str += "<td class='align_l'>" + result.orgNm + "</td>";
			str += "<td>" + result.regNm + "(" + result.regId + ")</td>";
			str += "<td>" + result.regDate + "</td>";
			
			if(result.dashYn == 'Y'){
				str += "<td><a href='#' name='btnApplyY' class='btn b_blue ssmall'>적용 중</a></td>";
			}else{
				str += "<td><a href='#' name='btnApplyN' class='btn b_gray ssmall'>적용하기</a></td>";
			}
			
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