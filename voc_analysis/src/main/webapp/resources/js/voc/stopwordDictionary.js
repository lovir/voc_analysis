
$(function(){
	
	//체크박스 전체 선택/해제
	$('#stopwordListForm input[name=totalIdCheck]').click(function(){
		
		if ($(this).is(":checked") ){ 
			$("#stopwordListForm input:checkbox").attr("checked",true);
		}else{
			$("#stopwordListForm input:checkbox").attr("checked",false);
		}
	});
});

	//검색
	function fnSearchAction(){
		if($.trim($('#stopwordListForm input[name=keyword]').val()) == ''){
			alert("검색어를 입력하세요.");
			$('#stopwordListForm input[name=keyword]').focus();
			return false;
		}
		$("#stopwordListForm").attr('action',getContextPath()+'/management/stopwordDictionaryInit.do').submit();
	}
	
	//추가
	function fnKeywordAdd(){
		if($.trim($('#addKeyword').val()) == ''){
			alert("키워드를 입력하세요.");
			$('#addKeyword').focus();
			return false;
		}
		
		$.ajax({
			type : "post",
			url : getContextPath()+'/management/stopwordExistCheck.do',
			data : {"keyword" : $('#addKeyword').val()},
			success : function(data) {
				if(data>0){
					alert('이미 등록된 키워드입니다.');
				}else{
					$("#stopwordListForm").attr('action',getContextPath()+'/management/stopwordDictionaryInit.do').submit();
				}
			},
			error : function(result) {
				alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
			}
		});	
	}
	
	//선택 적용
	function fnKeywordApply(){
		if($('#stopwordListForm input[name=checkKeyword]:checked').length == 0){
			alert("적용하실 불용어를 선택하세요.");
			return false;
		}
		
		var selectedKeyword = new Array();
		
		$('[name="checkKeyword"]:checked').each(function(){
			selectedKeyword.push($(this).parent().attr('name'));
		});

		$('#stopwordListForm input[name=selectedKeyword]').val(selectedKeyword);
		
		if (confirm("선택한 불용어를 서버에 적용하시겠습니까?") == true){
			$.ajax({
				type : "post",
				url : getContextPath()+'/management/stopwordApply.do',
				data : $("#stopwordListForm").serialize(),
				success : function(data) {
					
					if(data>0){
						alert('적용 명령을 서버에 등록하였습니다. 적용 처리가 완료될때까지는 시간이 걸릴 수 있습니다.');
						$("#stopwordListForm").attr('action',getContextPath()+'/management/stopwordDictionaryInit.do').submit();
					}else{
						alert('적용 실패');
					}
				},
				error : function(result) {
					alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				}
			});	
		}
	}
	
	
	//선택 삭제
	function fnKeywordDelete(){
		if($('#stopwordListForm input[name=checkKeyword]:checked').length == 0){
			alert("삭제하실 불용어를 선택하세요.");
			return false;
		}
		
		var selectedKeyword = new Array();
		
		$('[name="checkKeyword"]:checked').each(function(){
			selectedKeyword.push($(this).parent().attr('name'));
		});

		$('#stopwordListForm input[name=selectedKeyword]').val(selectedKeyword);
		
		if (confirm("선택한 불용어를 삭제하시겠습니까?") == true){
			$.ajax({
				type : "post",
				url : getContextPath()+'/management/stopwordDelete.do',
				data : $("#stopwordListForm").serialize(),
				success : function(data) {
					alert($.trim(data));
					$("#stopwordListForm").attr('action',getContextPath()+'/management/stopwordDictionaryInit.do').submit();
				},
				error : function(result) {
					alert("code : " + request.status + "\r\nmessage : " + request.reponseText);
				}
			});	
		}
	}
	
	//페이지 이동
	function fnPageNavi(pageNo){
		$('#stopwordPageForm input[name=currentPage]').val(pageNo);
		$("#stopwordPageForm").attr('action',getContextPath()+'/management/stopwordDictionaryInit.do').submit();
	}
	