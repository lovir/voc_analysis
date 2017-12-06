/***************************************
    함수명 : keyEvent
    설명   : 단축키 이벤트 처리
****************************************/
var isCtrl = false;
$(document).keyup(function (e) {
    if(e.which == 17) isCtrl = false;
}).keydown(function(e) {
    if(e.which == 17) isCtrl = true;
    if(e.which == 37 && isCtrl == true) {
    	mediaPlayer.setDioBack($('#val_value').val());
        return false;
    }else if(e.which == 39 && isCtrl == true) {
       mediaPlayer.setDioForware($('#val_value').val());
        return false;
    }else if(e.which == 32 && isCtrl == true) {
    	var playBtn = $('#play_btn');
    	if(playBtn.attr("class") == "control_play_btn"){
    		playBtn.attr("class", "control_play_btn on");
    		playBtn.attr("onclick", "mediaPlayer.dioPause(this)");
    		mediaPlayer.dioPlay(playBtn);
    	}else{
    		playBtn.attr("class", "control_play_btn");
    		playBtn.attr("onclick", "mediaPlayer.dioPlay(this)");
    		mediaPlayer.dioPause(playBtn);
    	}
        return false;
        
    }else if(e.which == 38 && isCtrl == true) {
    	
    	mediaPlayer.setDioVol('+',10);
    	$(".volumbar").width(mediaPlayer.getVol());
        return false;
    }else if(e.which == 40 && isCtrl == true) {
    	mediaPlayer.setDioVol('-',10);
    	$(".volumbar").width(mediaPlayer.getVol());
        return false;
    }
});