//태블로 포탈 iFrame 연동 관련 부분 - 시작
function getSize(){
	window.parent.postMessage( 
		document.body.scrollHeight+"|"+document.body.scrollWidth 
		,"*"
	)
}
window.onload = function() {
	getSize();
};

$(function () {
	var prevHeight = $('#content').height();
	$('#content').attrchange({
		trackValues: true, 
		callback: function (event) { 
			var curHeight = $(this).height();            
			if (prevHeight !== curHeight) {
				//$('#logger').text('height changed from ' + prevHeight + ' to ' + curHeight);
				
				prevHeight = curHeight;
				getSize();
			}
		}
	})
	
});
//태블로 포탈 iFrame 연동 관련 부분 - 종료
$(function () {

	//알람 모니터링 기간('지난일주일','전날','오늘) 클릭시 작동
	$('[name="util_01"]').click(function(idx){ 
		var portal_id = $('input[name="portal_id"]').val();
		var portal_nm = $('input[name="portal_nm"]').val();
		var inputData = {"regId":portal_id , "regNm":portal_nm};
		//활성화 된 알람키워드가 존재하는지 조회
		$.ajax({
			type : "post",
			url : getContextPath()+"/management/checkAlarmRegYnCount.do",
			data : inputData,
			success : function(data) {
				resultData = $.parseJSON(data);
				var alarmKeywordSize = resultData.alarmKeywordSize;
				
				//활성화 된 알람 키워드가 있는 경우에만 모달 팝업 노출
				if(alarmKeywordSize > 0){
					var	period = '1'; // 지난 일주일(기본)
					var json = {"period":period, "regId":portal_id , "regNm":portal_nm};
					
					$("#basic-modal-util_01").load(getContextPath() + "/management/selectAlarmKeywordMonitoringList.do",
							json,
						function(){
							$("#basic-modal-util_01").modal({
								persist: false,
								focus: false,
								onClose: function () {
									$('body').css('overflow','auto');
									$.modal.close();
								}
							});
							$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
						});
					$('#simplemodal-container').css({top:"120px"});
				}
				else{
					alert("활성화 된 알람키워드가 없습니다.\n알람키워드를 등록해 주세요.");
					$('body').css('overflow','auto');
					$.modal.close();
				}
			}
		});
		return false;
	});
	
	// 인쇄버튼 클릭
	$('.util_04').on('click', function(){
		window.print();
	});

	// 매뉴얼 클릭
	$('.util_05').on('click', function(){
		var win = window.open(getContextPath() + '/dashBoard/help.do', '_blank');
		win.focus();
	});
	
	// 검색 버튼 클릭
	$('#mainSearchBtn').on('click', function(){
		if($('#searchTerm').val().length<=0){
			alert("검색어 입력 해 주세요.");
			return false;
		}else{
			if($('#searchCondition option:selected').val() == '01'){
				$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/vocSearch.do");
			}else{
				$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/userSearch.do");
			}
			
			$("#headerForm").submit();
		}
	});
	
	// 검색어 입력 후 엔터
	$('#searchTerm').keypress(function(e){
		if(e.which==13){
			if($('#searchTerm').val().length<=0){
				alert("검색어 입력 해 주세요.");
				return false;
			}else{
				if($('#searchCondition option:selected').val() == '01'){
					$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/vocSearch.do");
				}else{
					$("#headerForm").attr('action', "<c:out value='${pageContext.request.contextPath}' />/mainSearch/userSearch.do");
				}
				
				$("#headerForm").submit();
			}
		}
	});
});