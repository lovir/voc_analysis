$(function () {
	
	if($("#conditionCheck").val() == "MONTH"){
		
		$("#month").prop("selected",true);
	}
	if($("#conditionCheck").val() == "WEEK"){
		
		$("#week").prop("selected",true);
	}
	
	if($("#collectionCkeck").val() == "VOC"){
		
		$("#voc").prop("selected",true);
	}
	if($("#collectionCkeck").val() == "SNS"){
		
		$("#sns").prop("selected",true);
	}
	

	$('#btnSearch').on('click', function(){
		$("#searchForm").removeAttr("target");
		$("#searchForm").attr('action', getContextPath() + "/dashBoard/search.do").submit();
	});
	
	// 상세 보기 클릭
	$('.complain_list a').on('click', function(){
		var docId = $(this).attr('id');
		var json = {
			"id" : docId
		};
		
		$("#basic-modal-complain_view").load(getContextPath() + "/dashBoard/dashBoardComplainDetailView.do #complain",
				json,
			function(){
				$("#basic-modal-complain_view").modal({
					persist: false,
					focus: false,
					onClose: function () {
						$('body').css('overflow','auto');
						$.modal.close();
					}
				});
				$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
			});
		return false;
	});
	
	// Issue Cloud #1 클릭
	$('#canvasBtn').on('click', function(){
		
		$(this).addClass('on');
		$('#cloudBtn').removeClass('on');

		$('#tags').empty();
		
		$('#tags').append('<canvas id="issueCanvas" width="628px" height="290px" ></canvas>');
		
		TagCanvas.Start('issueCanvas', 'jcloud-tags',{
			textFont : "NanumGothicBold",
			textColour: null,
			textHeight: 10,
			outlineThickness: 1,
			weight: true,
			weightFrom: "data-weight",
			weightMode: "both",
			reverse: true,
			depth: 0.8,
			maxSpeed: 0.05,
			minBrightness: 0.1,
			shadowBlur: 0,
			shape: "vcylinder",
			lock: "y",
			wheelZoom: false,
			active: true,
			freezeActive: true
		});
		
		return false;
	});
	
	// Issue Cloud #2 클릭
	$('#cloudBtn').on('click', function(){
		$(this).addClass('on');
		$('#canvasBtn').removeClass('on');

		$('#tags').empty();
		
		$('#tags').jQCloud(words, {
			height: 290,
			shape: 'elliptic'
		});
		
		return false;
	});
	
	$('#tags a').live('click', function(){
		fnDetailItem($(this).text());
		return false;
	});
	
});

// 이슈 클라우드 상세 모달
function fnDetailItem(term){
	
	var condition = $('#condition option:selected').val();
	var collectionCheck = $("#collectionCkeck").val();
	
	var json = {
		"term" : term,
		"condition" : "MONTH",
		"collection" : collectionCheck
	};
	
	$("#basic-modal-cloud_view").load(getContextPath() + "/dashBoard/dashBoardIssueDetailView.do",
			json,
		function(){
			$("#basic-modal-cloud_view").modal({
				persist: false,
				focus: false,
				onClose: function () {
					$('body').css('overflow','auto');
					$.modal.close();
				}
			});
			$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
		});
	return false;
}

function openBlankFrame( frameName, width, height ) {
	 var winprops = "";
	 
	 winprops    += "toolbar=no,menubar=no,scrollbars=yes,statusbar=no,resizable=yes";
	 winprops    += ",width="+width+",height="+height;
	 
	 window.open( "", frameName, winprops );
}

function regionSearch(emotion, guname, startDate, endDate,currentPage,pagesize){

	var emotion = emotion;
	var guname = guname;
	var startDate = startDate;
	var endDate = endDate;
	var currentPage = currentPage;
	var pagesize = pagesize;
	$("#emotion").val(emotion);
	$("#guname").val(guname);
	$("#startDate").val(startDate);
	$("#endDate").val(endDate);
	$("#pagesize").val(pagesize);
	
	var myForm = document.searchForm;
	var url = getContextPath()+"/dashBoard/regionSearch.do";
	window.open("" ,"popForm", "toolbar=no, width=1500, height=1227, directories=no, status=no, resizable=no"); 
	myForm.action =url; 
	myForm.method="post";
	myForm.target="popForm";
	myForm.submit();
}
