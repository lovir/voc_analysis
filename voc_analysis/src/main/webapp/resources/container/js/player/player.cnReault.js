// 연속어 결과 목록 관련 처리 스크립트 함수 
/**
 * Description : 요청 음성파일에 대한 연속어 결과 조회 요청
 * @author : 셀바스 AI 홍석원
 * @date    : 2017-01-23
 * @returns : void
 * 
 */
function loadCnReaultList(){
	console.log("---------------");
	var url = '/demo/audio/recognitionResultList.do';
	var param = '?fileSeq='+audioInfo.file_id+'&sendReceiveDelimiter='+audioInfo.send_recv_flg;
	callAjax('POST', url+param, loadCnReaultListCallBack);
}

/**
 * Description : 요청 음성파일에 대한 연속어 결과 표시
 * @author : 셀바스 AI 홍석원
 * @date    : 2017-01-23
 * @returns : void
 * 
 */
function loadCnReaultListCallBack(ajax){
	var result = eval("("+ajax.responseText+")");
	var html = '';
	for(var cnIndex = 0; cnIndex < result.length; cnIndex++){
		html += '<p id="cnResult_'+(cnIndex + 1)+'"  title="'+(result[cnIndex].startPosition/1000)+'">';
		html += '<a href="#" onclick="mediaPlayer.dioSectionPlay(this,'+(result[cnIndex].startPosition/1000)+','+(result[cnIndex].endPosition/1000)+'); return false;">';
		html +=  result[cnIndex].recognitionText+'</a></p>';
	}
	
	$("#cnResultListContent").html(html); 
	
	//연속어 스크롤바 재설정
	$("#cnResultListContent").mCustomScrollbar({
		autoHideScrollbar:true,
		theme:"light-thin",
		advanced:{
			updateOnContentResize: true,
			autoScrollOnFocus: false
		} 
	});
	
	mediaPlayer.setCnResultCount(result.length - 0);
	
	$(".player_select").selectbox();
	
	var html = '<div class="slider_bg2"></div>';
	if ($(".select_g67").size() > 0) $(".select_g67").prev().prev().before(html);
}