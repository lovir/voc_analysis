$(function () {

	// 초기화
	$( window ).load(function() {
		search();
	});
	
	// 저장 버튼 클릭
	$('#btnSave').on('click', function(){
		
		var url = "";
		var msg = "";
		var level1 = $('#level1').val();
		var level2 = $('#level2').val();
		var level3 = $('#level3').val();
		var no = $('#no').val();

		var json = {"level1":level1,
					"level2":level2,
					"level3":level3, 
					"no":no};
		
		
		if(no != undefined && no > 0){
			url = "/management/updateStandardAlarm.do";
			msg = "수정";
		}else{
			url = "/management/addStandardAlarm.do";
			msg = "등록";
		}
		
		
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
					alert(msg + "되었습니다.");
					search();
				}
			}
		});

	});
	
	// 검색
	function search(){
		
		var json = {};
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/management/selectStandardAlarm.do",
			dataType: 'json', 
			data: JSON.stringify(json), 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				if(response != undefined){
					$('#no').val(response.no);
					$('#level1').val(response.level1);
					$('#level2').val(response.level2);
					$('#level3').val(response.level3);
				}
			}
		});
	}
	
});