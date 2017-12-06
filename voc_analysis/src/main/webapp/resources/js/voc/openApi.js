$(function () {
	/*// 페이지 번호 클릭
	$('#paging span').live('click', function(){
		
		var pageSize = $('#pageSize').val();
		var currentPageNo = $(this).find('a').attr('name');
		$('#currentPageNo').val(currentPageNo);
		if(currentPageNo != undefined){
			displayModalList();
		}
		
		return false;
	});*/
	
	//Open Api 리스트 클릭시 모달 팝업
	$('[name=channel_01]').click(function(){
		displayModalList();
	});
	
});

//모달 팝업 리스트 출력
function displayModalList(){
	// 현재 시간 구하기
	var d = new Date();
	var time = '['+d.getFullYear()+'년 '+(d.getMonth() + 1)+'월 '+d.getDate()+'일 '+d.getHours()+':'+d.getMinutes()+' 채널별 수집 현황]';
	
	$('#simplemodal-container').css({top:"120px"});
	
	var pageSize = $('#pageSize').val();
	var currentPageNo = $('#currentPageNo').val();
	$('#modal_title').html('자동 분류 추천 Open API 일별 현황 보기');
	
	var json = {"pageSize":pageSize, "currentPageNo":currentPageNo};
	var totalSize;
	$.ajax({
		type : "post",
		url : getContextPath()+"/openApi/selectDayCategorizeList.do",
		data : json,
		success : function(data) {
			var listData = $.parseJSON(data);
			var tableList = listData.resultList;
			totalSize = listData.totalSize;
			var tableStr = '<thead><tr><th>날짜</th><th>누적 로그</th><th>당일 로그</th></tr></thead><tbody>';
			for(var i=0; i<tableList.length; i++){
				tableStr += '<tr>';
				tableStr += '<td>'+tableList[i].DATE_STR+'</td>';
				tableStr += '<td>'+tableList[i].ACM_COUNT+'</td>';
				tableStr += '<td>'+tableList[i].LOG_COUNT+'</td>';
				tableStr += '</tr>';
			}
			tableStr += '</tbody></table>';
			$('#list_contents').html(tableStr);
			getPageNavi(currentPageNo, pageSize, totalSize );
		}
	});
	$('#currentTime').text(time);
	$('#totalSize').val(totalSize);
}

//페이지 네비게이션 출력
function getPageNavi(currentPageNo, pageSize, totalSize){
	var jsonData = {"currentPageNo":currentPageNo, "pageSize":pageSize, "totalSize" :totalSize};
	$.ajax({
		type : "post",
		async : true,
		url : getContextPath()+"/openApi/modalPaging.do",
		data : jsonData,
		success : function(data) {
			$('#paging_modal').html(data);		
		}
	});
}

//페이지 이동
function pageNavi(pageNo){
	$('input[name=currentPageNo]').val(pageNo);	
	displayModalList();
}