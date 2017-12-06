$(function(){
	
	$( window ).load(function() {
		selectBox();
	});

	// 검색 폼 on/off
	$('.btn_search').click(function(){
		
		var type = $(this).attr('id');
		/* serachForm설정 A,B */
		var condition = $('#calendar_select'+type+' option:selected').val();
		var startDate = $('#startDate'+type).val();
		var endDate = $('#endDate'+type).val();
		var exclusion = $('#exclusion'+type).val();
		var keyword = $('#keywordTemp'+type).val();
		var needsType = $('select[name="selectNeedsType'+type+'"] option:selected').text();
		var selectBusinessType =  $('select[name="selectBusinessType'+type+'"] option:selected').val();
		var selectCharacterType =  $('select[name="selectCharacterType'+type+'"] option:selected').val();
		var selectNeedsType =  $('select[name="selectNeedsType'+type+'"] option:selected').val();
		var selectUsesMediaType =  $('select[name="selectUsesMediaType'+type+'"] option:selected').val();
		
		$('input[name="condition"]').val(condition);
		$('input[name="startDate"]').val(startDate);
		$('input[name="endDate"]').val(endDate);
		$('input[name="exclusion"]').val(exclusion);
		$('input[name="keyword"]').val(keyword);
		$('input[name="needsType"]').val(needsType);
		$('input[name="compareType"]').val(type);
		$('input[name="selectBusinessType"]').val(selectBusinessType);
		$('input[name="selectCharacterType"]').val(selectCharacterType);
		$('input[name="selectNeedsType"]').val(selectNeedsType);
		$('input[name="selectUsesMediaType"]').val(selectUsesMediaType);
		$('input[name="pageSize"]').val(10);
		
		if(!validate()){
			return false;
		}
		
		$.ajax({//d3 차트 - 연관도 분석
			type : "post",
			url : getContextPath()+"/relation/compareRadarChart.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				if(data.length > 4){
					var returnData = $.parseJSON(data);
					if(type=='A'){
						var path = "d3ChartA";
						var id = "chartA";
						$('#d3ChartA').html('');
						d3Chart(returnData, path, id);
					}else{
						var path = "d3ChartB";
						var id = "chartB";
						$('#d3ChartB').html('');
						d3Chart(returnData, path, id);
					}
				}else{
					if(type=='A'){
						$('#d3ChartA').html('');
						$('#d3ChartA').html('차트결과가 없습니다.');
					}else{
						$('#d3ChartB').html('');
						$('#d3ChartB').html('차트결과가 없습니다.');
					}
				}
			},
			error : function(result) {     
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		//연관키워드 트렌드
		$.ajax({
			type : "post",
			url : getContextPath()+"/relation/compareKeywordTrend.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				var returnData = $.parseJSON(data);
				if(returnData.keywordPeriodCountList != undefined){
					$('#reportChart'+type).addClass('p_20');
					$('#reportChart'+type).highcharts({
				        title: {
				            text: '',
				            x: -20 //center
				        },
						credits:{
							enabled:false
						},
				        xAxis: {
				            categories: returnData.periodList
				        },
				        yAxis: {
				            min: 0,
				            title: {
				                text: '건수'
				            },
				        },
				        legend: {
				            layout: 'horizontal',
				            align: 'center',
				            verticalAlign: 'bottom',
				            borderWidth: 0
				        },
				        series: returnData.keywordPeriodCountList
				    });
				}else{
					$('#reportChart'+type).removeClass('p_20');	
					$('#reportChart'+type).empty();
					$('#reportChart'+type).html("차트 결과가 없습니다.");
				}
			},
			error : function(result) {     
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		
		
		//하단 VOC검색결과
		$.ajax({
			type : "post",
			url : getContextPath()+"/relation/vocCompareSearchResult.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#search_result'+type).html(data);
			},
			error : function(result) {     
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
		return false;
	});
	
	//카테고리 클릭시
	$(document).on('click', '#tab_01A, #tab_01B', function(i){
		var type ='A';
		if($(this).attr("id")=='tab_01B'){
			type ='B';
		}
		// tab에 선택 클리어
		$('#searchCategory'+type+' li a').removeClass('on');
		// 선택된 tab on
		$(this).addClass('on');
		$('#searchForm input[name=needsType]').val($(this).attr("name"));
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize"+type+"]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList(type);
	});
	
	//페이지 문서수 변경
	$(document).on('change','#pageSizeA, #pageSizeB', function(){
		var type = "A";
		if($(this).attr("id")=='pageSizeB'){
			type = "B";
		}
		$('#searchForm input[name=pageSize]').val($("select[name=pageSize"+type+"]").val());
		$('#searchForm input[name=currentPage]').val('1');
		fnsearchList(type);
	});
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().children('input[name=content]').val());
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath()+"/relation/getAlikeSearch.do #alike",
			{"doc_id" : id, "title" : title, "content" : content},
			function(){
				$("#basic-modal-alike").modal({
					persist: false,
					focus: false,
					onClose: function () {
						   $('body').css('overflow','auto');
						   $.modal.close();
						}
				});
				$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
			});
		}
	});
	
	$('#keywordListA, #keywordListB').change(function() {
		var id = $(this).attr("id");
		var temp = $('#'+id+' option:selected').text();
		if(id=='keywordListA'){
			if(temp=='직접입력'){
				$('#keywordTempA').val('');
				$('#keywordTempA').attr("disabled", false);
			}else{
				$('#keywordTempA').attr("disabled", true);
				$('#keywordTempA').val(temp);
			}
		}else{
			if(temp=='직접입력'){
				$('#keywordTempB').val('');
				$('#keywordTempB').attr("disabled", false);
			}else{
				$('#keywordTempB').attr("disabled", true);
				$('#keywordTempB').val(temp);
			}
		}
	});
	
	// 초기화 >> A
	$('#searchResetA').click(function() {
		$("#startDateA").val('');
	    $("#endDateA").val('');
	    $('#calendar_selectA').find('option:first').attr('selected',true);
	    $('#selectTextA').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
	    $('#needsTypeListA > select').find('option:first').attr('selected',true);
	    $('#characterTypeListA > select').find('option:first').attr('selected',true);
	    $('#businessTypeListA > select').find('option:first').attr('selected',true);
	    $('#usesMediaTypeListA > select').find('option:first').attr('selected',true);
	    $("#exceptKeywordA").val('');
	    
	    $('#keywordListA option:eq(0)').attr('selected','selected');
	    $('#keywordTempA').val('');
		$('#keywordTempA').attr("disabled", false);
		$('#exclusionA').val('');
		$('#calendar_selectA').change();
	});
	
	// 초기화 >> B
	$('#searchResetB').click(function() {
		$("#startDateB").val('');
	    $("#endDateB").val('');
	    $('#calendar_selectB').find('option:first').attr('selected',true);
	    $('#selectTextB').html('<font color="red">\'일별\'</font>은 시작일부터 일주일 설정만 가능합니다.');
	    $('#needsTypeListB > select').find('option:first').attr('selected',true);
	    $('#characterTypeListB > select').find('option:first').attr('selected',true);
	    $('#businessTypeListB > select').find('option:first').attr('selected',true);
	    $('#usesMediaTypeListB > select').find('option:first').attr('selected',true);
	    $("#exceptKeywordB").val('');
	    $('#keywordListB option:eq(0)').attr('selected','selected');
	    $('#keywordTempB').val('');
		$('#keywordTempB').attr("disabled", false);
		$('#exclusionB').val('');
		$('#calendar_selectB').change();
	});
	
});


	//VOC검색결과 리스트 생성(리스트+페이징)
	function fnsearchList(type){
		
		$('input[name="compareType"]').val(type);
		var startDate = $('#startDate'+type).val();
		var endDate = $('#endDate'+type).val();
		var exclusion = $('#exclusion'+type).val();
		var keyword = $('#keywordTemp'+type).val();
		var selectBusinessType =  $('select[name="selectBusinessType'+type+'"] option:selected').val();
		var selectCharacterType =  $('select[name="selectCharacterType'+type+'"] option:selected').val();
		var selectNeedsType =  $('select[name="selectNeedsType'+type+'"] option:selected').val();
		var selectUsesMediaType =  $('select[name="selectUsesMediaType'+type+'"] option:selected').val();
		$('input[name="startDate"]').val(startDate);
		$('input[name="endDate"]').val(endDate);
		$('input[name="exclusion"]').val(exclusion);
		$('input[name="keyword"]').val(keyword);
		$('input[name="selectBusinessType"]').val(selectBusinessType);
		$('input[name="selectCharacterType"]').val(selectCharacterType);
		$('input[name="selectNeedsType"]').val(selectNeedsType);
		$('input[name="selectUsesMediaType"]').val(selectUsesMediaType);
		
		$.ajax({
			type : "post",
			url : getContextPath()+"/relation/vocCompareSearchResultList.do",
			data : $("#searchForm").serialize(),
			success : function(data) {
				$('#result_list'+type).html(data);
				$('#share'+type).text($('#result_list'+type+' input[name=share'+type+']').val()+'%');
			},
			error : function(result) {     
				 //통신 에러 발생시 처리
				alert("서버에서 알 수 없는 에러가 발생했습니다. 관리자에게 문의하세요.");
			}
		});
	}

	//상세보기
	function detailView(id){
		$("#basic-modal-detail").load(getContextPath()+"/relation/detailView.do #detail",
		{"doc_id" : id},
		function(){
			$("#basic-modal-detail").modal({
				persist: false,
				focus: false,
				onClose: function () {
					   $('body').css('overflow','auto');
					   $.modal.close();
					}
			});
			$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
		});
	}
	
	//유사문서
	$(document).on('click', '.result_doc', function(){
		var id =  $.trim($(this).attr("name"));
		var title = $.trim($(this).parent().parent().parent().children('input[name=title]').val());
		var content = $.trim($(this).parent().parent().parent().children('input[name=content]').val());
		if( content != ""){
			$("#basic-modal-alike").load(getContextPath()+"/relation/getAlikeSearch.do #alike",
			{"doc_id" : id, "title" : title, "content" : content},
			function(){
				$("#basic-modal-alike").modal({
					persist: false,
					focus: false,
					onClose: function () {
						   $('body').css('overflow','auto');
						   $.modal.close();
						}
				});
				$('body').css('overflow','hidden'); // 백그라운다 마우스휠 off
			});
		}
	});
	
	function selectBox(){
		
		$.ajax({
			type : "POST", 
			url : getContextPath() + "/relation/RelationAnalysisSelectOptionList.do",
			dataType: 'json', 
			contentType: 'application/json;charset=utf-8',
			mimeType: 'application/json',
			success : function(response) {
				$('#needsTypeListA').html(selectList(response.needsTypeList));//니즈유형
				$('#characterTypeListA').html(selectList(response.characterTypeList));//성격유형
				$('#businessTypeListA').html(selectList(response.businessTypeList));//업무유형
				$('#usesMediaTypeListA').html(selectList(response.usesMediaTypeList));//매체유형
				$('#needsTypeListA > select').attr("name","selectNeedsTypeA");
				$('#characterTypeListA > select').attr("name","selectCharacterTypeA");
				$('#businessTypeListA > select').attr("name","selectBusinessTypeA");
				$('#usesMediaTypeListA > select').attr("name","selectUsesMediaTypeA");
				
				$('#needsTypeListB').html(selectList(response.needsTypeList));//니즈유형
				$('#characterTypeListB').html(selectList(response.characterTypeList));//성격유형
				$('#businessTypeListB').html(selectList(response.businessTypeList));//업무유형
				$('#usesMediaTypeListB').html(selectList(response.usesMediaTypeList));//매체유형
				$('#needsTypeListB > select').attr("name","selectNeedsTypeB");
				$('#characterTypeListB > select').attr("name","selectCharacterTypeB");
				$('#businessTypeListB > select').attr("name","selectBusinessTypeB");
				$('#usesMediaTypeListB > select').attr("name","selectUsesMediaTypeB");
			}
		});
		
	}
	
	//페이지 이동
	function pageNavi(pageNo, obj){
		
		var type ='A';
		if(obj.parentNode.parentNode.id == "pagingB"){
			type ='B';
		}
		$('#searchForm input[name=currentPage]').val(pageNo);
		fnsearchList(type);
	}
	
	function validate(){

		if($('input[name="startDate"]').val().length <= 0){
			alert("시작일을 설정해 주세요.");
			return false;
		}
		
		if($('input[name="endDate"]').val().length <= 0){
			alert("종료일을 설정해 주세요.");
			return false;
		}
		
		if($('input[name="keyword"]').val().length <= 0){
			alert('키워드를 입력하세요');
			return false;
		}
		
		if(!$('input[name="exclusion"]').val() == ''){
			var exceptKeyword = $('input[name="exclusion"]').val();
			var exceptKeywordArr = exceptKeyword.split(",");
			if(exceptKeywordArr.length > 3){
				alert("제외키워드는 3개까지만 입력가능합니다.");
				return false;
			}
		}
		return true;
	}
	
	/* D3 관련 차트 */
	function d3Chart(data, path, id){
		d3.select(id).remove();
		var w = 600,//표 크기
		h = 700,//표 크기
		node,
		link,
		root,
		nodes,
		links,
		overLinks;
	
		var force = d3.layout.force()
		.on("tick", tick)
		.gravity(.4)
		.friction(0.9)
		.charge(-1000)
		.linkDistance(50)
		.size([w, h]);
	
		var zoom = d3.behavior.zoom()
		.center([w / 2, h / 2])
		.scaleExtent([1, 8])
		.on("zoom", zoomed);
					
		var zoomScale = 1;
	
		var svg = d3.select("#"+path).append("svg")
		.attr("id", id)
		.attr("width", w)
		.attr("height", h)
		.attr("overflow", "hidden")
		.append("g")
		.call(zoom)
		.append("g");
	  
		svg.append("rect")
		.attr("class", "overlay")
		.attr("width", w)
		.attr("height", h);
		
		
		var pos_x = [0  	,0 		, 45 	, 45	, -45 	, -45	, 50	, -50];
		var pos_y = [80 	,-80 	, 60 	, -60 	, 60 	, -60	, 0	, -0];
		
		var pos = 0;
		root = data;
		root.fixed = true;
		root.x = w / 2;
		root.y = h / 2;
		init();
		
		function init() {
			nodes = flatten(root);
			if(nodes != null) {
				for(var i = 0; i < nodes.length; i++) {
					if(nodes[i].depth == 2) {
						nodes[i].x = root.x + pos_x[pos];
						nodes[i].y = root.y + pos_y[pos];
						nodes[i].fixed = true;
						pos++;
					}
					if(nodes[i].depth == 4) {
						if(nodes[i].children) {
							nodes[i]._children = nodes[i].children;
							nodes[i].children = null;
						}
					}
				}
			}
			update();
		}
	
		function update() {
			nodes = flatten(root),
			links = d3.layout.tree().links(nodes);
			
			force
			.nodes(nodes)
			.links(links)
			.start();
		
			link = svg.selectAll("line")
			.data(links, function(d) { return d.target.id; });
			
			link.enter().insert("line", ".node")
			.attr("id", function(d) { return "link_" + d.source.id + "_" + d.target.id; })
			.attr("x1", function(d) { return d.source.x; })
			.attr("y1", function(d) { return d.source.y; })
			.attr("x2", function(d) { return d.target.x; })
			.attr("y2", function(d) { return d.target.y; });
			
			if(nodes.length < 10) {
				force.gravity(.0);	
			} else if(nodes.length >= 10 && nodes.length < 20) {
				force.gravity(.1);
			}
			
			link.exit().remove();
		  
			svg.selectAll("g.node").remove();
		  
			node = svg.selectAll("g.node")
			.data(nodes, function(d) {
				return d.id; 
			});
		
			node.enter().append("g")
			.attr("class", "node")
			.on("click", click)	  
			//.on("mouseover", mouseOverData)
			//.on("mouseout", mouseOutData)
			//.on("click", nodeMouseClickData)
			.call(force.drag);
		  	
			node.append("rect")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("x", function(d) { 
				var size = -25;
				var nameSize;
				if(d.group.indexOf("depth_etc") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * -7;
				}
				if(nameSize > size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("y", function(d) {
				var size;
				if(d.group.indexOf("depth_etc") > -1) {
					size = -7;
				}
				return size; 
			})
			.attr("width", function(d) {
				var size = 50;
				var nameSize;
				if(d.group.indexOf("depth_etc") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * 14;
				}
				if(nameSize < size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("height", function(d) { 
				var size;
				if(d.group.indexOf("depth_etc") > -1) {
					size = 22;
				}
				return size; 
			})
			.attr("fill", function(d) {
				if(d.group.indexOf("keyword1") > -1) {
					return "#a26eb3";				
				} else if(d.group.indexOf("keyword2") > -1) {
					return "#6ba22c";
				} else if(d.group.indexOf("keyword3") > -1) {
					return "#d55a76";
				} else if(d.group.indexOf("keyword4") > -1) {
					return "#4083c6";
				} else if(d.group.indexOf("keyword5") > -1) {
					return "#ce9514";
				} else if(d.group.indexOf("keyword6") > -1) {
					return "#d66f0d";
				} else if(d.group.indexOf("keyword7") > -1) {
					return "#867bd4";
				} else if(d.group.indexOf("keyword8") > -1) {
					return "#3ba5a5";
				} else if(d.group.indexOf("keyword9") > -1) {
					return "#01DFD7";				
				} else if(d.group.indexOf("center") > -1) {
					return "#000000";	
				}
			})
			.attr('rx', 10)
	        .attr('ry', 10);
			
			node.append("circle")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("x", function(d) { 
				var size = -30;
				var nameSize;
				if(d.group.indexOf("depth2") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * -7;
				}
				if(nameSize > size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("y", function(d) {
				var size;
				if(d.group.indexOf("depth2") > -1) {
					size = -30;
				}
				return size; 
			})
			.attr("width", function(d) {
				var size = 60;
				var nameSize;
				if(d.group.indexOf("depth2") > -1) {
					nameSize = d.name.replace(/ /gi, '').length * 14;
				}
				if(nameSize < size) {
					nameSize = size;
				} 
				return nameSize;
			})
			.attr("height", function(d) { 
				var size;
				if(d.group.indexOf("depth2") > -1) {
					size = 60;
				}
				return size; 
			})
			.attr("fill", function(d,i) {
				if(d.group.indexOf("depth2") > -1) {
					return "#ffffff";
				}
					return "#ffffff";
			})
			.attr("style",function(d) {
				if(d.group.indexOf("keyword1") > -1) {
					return "fill : #ffffff; stroke :#a26eb3;";				
				} else if(d.group.indexOf("keyword2") > -1) {
					return "fill : #ffffff; stroke :#6ba22c;";
				} else if(d.group.indexOf("keyword3") > -1) {
					return "fill : #ffffff; stroke :#d55a76;";
				} else if(d.group.indexOf("keyword4") > -1) {
					return "fill : #ffffff; stroke :#4083c6;";
				} else if(d.group.indexOf("keyword5") > -1) {
					return "fill : #ffffff; stroke :#ce9514;";
				} else if(d.group.indexOf("keyword6") > -1) {
					return "fill : #ffffff; stroke :#d66f0d;";
				} else if(d.group.indexOf("keyword7") > -1) {
					return "fill : #ffffff; stroke :#867bd4;";
				} else if(d.group.indexOf("keyword8") > -1) {
					return "fill : #ffffff; stroke :#3ba5a5;";
				} else if(d.group.indexOf("keyword9") > -1) {
					return "fill : #ffffff; stroke :#01DFD7;";				
				} else if(d.group.indexOf("center") > -1) {
					return "fill : #ffffff; stroke :#000000;";	
				}
			})
			.attr("stroke", function(d) {
				if(d.group.indexOf("keyword1") > -1) {
					return "#a26eb3";				
				} else if(d.group.indexOf("keyword2") > -1) {
					return "#6ba22c";
				} else if(d.group.indexOf("keyword3") > -1) {
					return "#d55a76";
				} else if(d.group.indexOf("keyword4") > -1) {
					return "#4083c6";
				} else if(d.group.indexOf("keyword5") > -1) {
					return "#ce9514";
				} else if(d.group.indexOf("keyword6") > -1) {
					return "#d66f0d";
				} else if(d.group.indexOf("keyword7") > -1) {
					return "#867bd4";
				} else if(d.group.indexOf("keyword8") > -1) {
					return "#3ba5a5";
				} else if(d.group.indexOf("keyword9") > -1) {
					return "#01DFD7";				
				} else if(d.group.indexOf("center") > -1) {
					return "#000000";	
				}
			})
			.attr('stroke-width', 3)
			.attr('rx', 10)
	        .attr('ry', 10)
	        .attr("r", function(d) {
	        	var size;
				if(d.group.indexOf("depth2") > -1) {
					size = 17;
				}
				if(d.group.indexOf("center") > -1) {
					size = 25;
				}
				return size;
			});
			
			node.append("image")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("group", function(d) {
				return d.id;
			}) 
			.attr("xlink:href", function(d) {
				if(d.group.indexOf("depth_etc") < 0) {
				}
				
			}) 
			.attr("x", function(d) { 
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size = -30;
				} else if(d.group.indexOf("depth2") > -1) {
					size = -15;
				} 
				return size;
			})
			.attr("y", function(d) {
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size = -30;
				} else if(d.group.indexOf("depth2") > -1) {
					size = -15
				} 
				return size; 
			})
			.attr("width", function(d) {
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size=0;
				} else if(d.group.indexOf("depth2") > -1) {
					size=0;
				} 
				return size;
			})
			.attr("height", function(d) { 
				var size;
				if(d.group.indexOf("depth1") > -1) {
					size=0;
				} else if(d.group.indexOf("depth2") > -1) {
					size=0;
				} 
				return size; 
			});
			
			node.append("text")
			.attr("id", function(d) {
				return d.group.substring(0, d.group.indexOf("_"));
			})
			.attr("class", function(d) {
				if(d.group.indexOf("center") > -1) {
					return "text_center color_center_text";	
				} else if(d.group.indexOf("keyword1") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#a26eb3")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_city";
					}				
				} else if(d.group.indexOf("keyword2") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#6ba22c")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_nature";
					}
				} else if(d.group.indexOf("keyword3") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#d55a76")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_specialty";
					}				
				} else if(d.group.indexOf("keyword4") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#4083c6")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_history";
					}
				} else if(d.group.indexOf("keyword5") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#ce9514")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_cultural";
					}				
				} else if(d.group.indexOf("keyword6") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#d66f0d")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_foaktale";
					}				
				} else if(d.group.indexOf("keyword7") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#867bd4")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_festival";
					}				
				} else if(d.group.indexOf("keyword8") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#3ba5a5")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_infrastructure";
					}				
				} else if(d.group.indexOf("keyword9") > -1) {
					overLinks = findPath(d);
					for(var i = 0; i < overLinks.length; i++) {
						var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
						d3.selectAll("line#link_" + id).transition()
						.style("stroke", "#01DFD7")
						.style("stroke-width", "1.5px");
					}
					if(d.group.indexOf("depth_etc") > -1) {
						return "text_normal color_center";	
					} else {
						return "text_normal color_default";
					}				
				}
			})
			.attr("style", "font-size: 13px;")
			.attr("dx", 0)
			.attr("dy", function(d) {
				if(d.group.indexOf("center") > -1) {
					return 5;
				} else if(d.group.indexOf("depth_etc") > -1) {
					return 8;
				} else {
					return 5;
				}
			})
			.text(function(d) {
				if(d.group.indexOf("depth2") > -1) {
					return d.name;
				} else {
					return d.name;
				}
			});
			
			node.append("title")
			.text(function(d) { return d.name; });
			node.exit().remove();
		}
	
		function mouseOverData(d) {
			
			if(d3.selectAll("text#" + d.group.substring(0, d.group.indexOf("_"))).style("opacity") == "1") {
				d3.select(this).select("rect").transition()
				.attr("x", function(d) { 
					var size = -35;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * -8;
					}
					if(nameSize > size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = -7;
					}
					return size; 
				})
				.attr("width", function(d) {
					var size = 70;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * 14 + 20;
					}
					if(nameSize < size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = 30;
					}
					return size; 
				})
				.attr('rx', 10)
		        .attr('ry', 10);
				
					  
				d3.select(this).select("text").transition()
				.attr("class", function(d) {
					if(d.group.indexOf("center") > -1) {
						return "text_center color_center_text";
					} else if(d.group.indexOf("keyword1") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#a26eb3")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_city";
						}
					} else if(d.group.indexOf("keyword2") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#6ba22c")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_nature";
						}
					} else if(d.group.indexOf("keyword3") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d55a76")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_specialty";
						}
					} else if(d.group.indexOf("keyword4") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#4083c6")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_history";
						}
					} else if(d.group.indexOf("keyword5") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#ce9514")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_cultural";
						}
					} else if(d.group.indexOf("keyword6") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d66f0d")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_foaktale";
						}
					} else if(d.group.indexOf("keyword7") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#867bd4")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_festival";
						}
					} else if(d.group.indexOf("keyword8") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#3ba5a5")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_infrastructure";
						}
					} else if(d.group.indexOf("keyword9") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#01DFD7")
							.style("stroke-width", "5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_default";
						}
					}
				})
				.attr("style", "font-size: 15px;")
				.attr("dx", function(d) {
					if(d.group.indexOf("depth_etc") > -1) {
						return 0;
					} else {
						return 0;
					}
				})
				//마우스 오버시 폰트 위치 변경 - 위/아래
				.attr("dy", function(d) {
					if(d.group.indexOf("center") > -1) {
						return 8;
					} else if(d.group.indexOf("depth_etc") > -1) {
						return 13;
					} else {
						return 8;
					}
				})
				.text(function(d) { 
					if(d.group.indexOf("depth2") > -1) {
						return d.name;
					} else {
						return d.name;
					}
				});
				
				d3.selectAll("g.node").sort(function (a) {
					if(a.id == d.id) {
						return 1;				
					}
				});
			}
		}
	
		function findPath(node) {
			var temp_links = [];
				
			var p_id = node.id;
			var p_depth = node.depth;
			while(p_depth > 1) {
				for(var i = 0; i < links.length; i++) {
					if(links[i].target.id == p_id) {
						temp_links.push(links[i]);
						p_id = links[i].source.id;
						p_depth--;
						break;
					}
				}
			}
			return temp_links;
		}
	
		function mouseOutData(d) {
			if(d3.selectAll("text#" + d.group.substring(0, d.group.indexOf("_"))).style("opacity") == "1") {
				d3.select(this).select("rect").transition()
				.attr("x", function(d) { 
					var size = -25;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * -7;
					}
					if(nameSize > size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = -7;
					}
					return size; 
				})
				.attr("width", function(d) {
					var size = 50;
					var nameSize;
					if(d.group.indexOf("depth_etc") > -1) {
						nameSize = d.name.replace(/ /gi, '').length * 14;
					}
					if(nameSize < size) {
						nameSize = size;
					} 
					return nameSize;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth_etc") > -1) {
						size = 22;
					}
					return size; 
				});
				
				d3.select(this).select("image").transition()
				.attr("x", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						//size = -30;
						size = -15;
					} 
					return size;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						size = -15;
					} 
					return size; 
				})
				.attr("width", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = 60;
					} else if(d.group.indexOf("depth2") > -1) {
						size = 30;
					} 
					return size;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = 60;
					} else if(d.group.indexOf("depth2") > -1) {
						size = 30;
					} 
					return size; 
				})
				.attr('rx', 10)
		        .attr('ry', 10);
				
				
				d3.select(this).select("image").transition()
				.attr("x", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						size = -20;
					} 
					return size;
				})
				.attr("y", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						size = -30;
					} else if(d.group.indexOf("depth2") > -1) {
						size = -20;
					} 
					return size; 
				})
				.attr("width", function(d) {
					var size;
					if(d.group.indexOf("depth1") > -1) {
						//size = 60;
						size=0;
					} else if(d.group.indexOf("depth2") > -1) {
						//size = 40;
						size=0;
					} 
					return size;
				})
				.attr("height", function(d) { 
					var size;
					if(d.group.indexOf("depth1") > -1) {
						//size = 60;
						size=0;
					} else if(d.group.indexOf("depth2") > -1) {
						//size = 40;
						size=0;
					} 
					return size; 
				});
				
				
				d3.select(this).select("text").transition()
				.attr("class", function(d) {
					if(d.group.indexOf("center") > -1) {
						return "text_center color_center_text";
						
					} else if(d.group.indexOf("keyword1") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#a26eb3")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_city";
						}
					} else if(d.group.indexOf("keyword2") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#6ba22c")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_nature";
						}
					} else if(d.group.indexOf("keyword3") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d55a76")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_specialty";
						}
					} else if(d.group.indexOf("keyword4") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#4083c6")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_history";
						}
					} else if(d.group.indexOf("keyword5") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#ce9514")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_cultural";
						}
					} else if(d.group.indexOf("keyword6") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#d66f0d")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_foaktale";
						}
					} else if(d.group.indexOf("keyword7") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#867bd4")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_festival";
						}
					} else if(d.group.indexOf("keyword8") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#3ba5a5")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_infrastructure";
						}
					} else if(d.group.indexOf("keyword9") > -1) {
						overLinks = findPath(d);
						for(var i = 0; i < overLinks.length; i++) {
							var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
							d3.selectAll("line#link_" + id).transition()
							.style("stroke", "#01DFD7")
							.style("stroke-width", "1.5px");
						}
						if(d.group.indexOf("depth_etc") > -1) {
							return "text_normal color_center";	
						} else {
							return "text_normal color_default";
						}
					}
				})
				.attr("style", "font-size: 13px;")
				.attr("dx", 0)
				.attr("dy", function(d) {
					if(d.group.indexOf("center") > -1) {
						return 5;
					} else if(d.group.indexOf("depth_etc") > -1) {
						return 8;
					} else {
						return 5;
					}
				})
				.text(function(d) {
					return d.name;
				});	
			}		
		}
	
		function tick() {
			link
			.attr("x1", function(d) { return d.source.x; })
			.attr("y1", function(d) { return d.source.y; })
			.attr("x2", function(d) { return d.target.x; })
			.attr("y2", function(d) { return d.target.y; });
		
			node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
		}
	
		function click(d) {
			if(d.depth > 2) {
				if(d.children) {
					d._children = d.children;
					d.children = null;
				} else {
					d.children = d._children;
					d._children = null;
				}
				update();
			}
		}
		
		function zoomed() {
			zoomScale = zoom.scale();
			svg.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
		}
		
		d3.select("#btn_zoomin").on("click", function() {
			zoomScale += 0.2;
			svg.transition()
			.duration(750)
			.call(zoom.center([w / 2, h / 2]).scale(zoomScale).event);
		});
	
		d3.select("#btn_zoomout").on("click", function() {	
			if(zoomScale == 1) {
				return;
			}
			zoomScale -= 0.2;
			if(zoomScale < 1) {
				zoomScale = 1;
			}
			svg.transition()
			.duration(750)
			.call(zoom.center([w / 2, h / 2]).scale(zoomScale).event);
		});
	
		d3.select("#btn_expand").on("click", function() {
			for(var i = 0; i < nodes.length; i++) {
				if(nodes[i]._children) {
					nodes[i].children = nodes[i]._children;
					nodes[i]._children = null;
				}
			}
			update();
		});
	
		d3.select("#btn_collapse").on("click", function() {
			for(var i = 0; i < nodes.length; i++) {
				if(nodes[i].depth > 2) {
					if(nodes[i].children) {
						nodes[i]._children = nodes[i].children;
						nodes[i].children = null;
					}
				}
			}
			update();
		});
	
		function flatten(root) {
			var nodes = [], i = 0, depth = 1;
		
			function recurse(node, depth) {
				if(node.children) {
					node.size = node.children.reduce(function(p, v) { return p + recurse(v, depth+1); }, 0);
				}
		    	if(!node.id) {
					node.id = ++i;
					node.depth = depth;
				}
				nodes.push(node);
				return node.size;
			}
		
			root.size = recurse(root, depth);
			root.depth = 1;
			return nodes;
		}
		
		function findGroupPath(node, group) {
			var temp_links = [];
				
			var p_depth = node.depth;
			while(p_depth > 1) {
				for(var i = 0; i < links.length; i++) {
					d3.selectAll("image#" + group).each(function(l){
						if(links[i].source.id == l.id || links[i].target.id == l.id) {
							temp_links.push(links[i]);
							p_id = links[i].source.id;
							p_depth--;
						}
					});
				}
			}
			return temp_links;
		}
		
		function nodeMouseClickData(d) {
			if(!(d.depth == 1 || d.children == undefined)) {
				d3.selectAll("line").transition().style("opacity", ".2");
				overLinks = findGroupPath(d, d.group.substring(0, d.group.indexOf("_")));
				for(var i = 0; i < overLinks.length; i++) {
					var id = overLinks[i].source.id + "_" + overLinks[i].target.id;
					d3.selectAll("line#link_" + id).transition().style("opacity", "1");
				}
				
				d3.selectAll("image").style("opacity", ".2");
				d3.selectAll("image#center").transition().style("opacity", "1");
				d3.selectAll("image#" + d.group.substring(0, d.group.indexOf("_"))).transition().style("opacity", "1");
				
				d3.selectAll("rect").style("opacity", ".2");
				d3.selectAll("rect#center").transition().style("opacity", "1");
				d3.selectAll("rect#" + d.group.substring(0, d.group.indexOf("_"))).transition().style("opacity", "1");
				
				d3.selectAll("text").style("opacity", ".2");
				d3.selectAll("text#center").transition().style("opacity", "1");
				d3.selectAll("text#" + d.group.substring(0, d.group.indexOf("_"))).transition().style("opacity", "1");
			}
			if(d.contentsGb == "CI") {
				if(d.contentsId == "CI00000001") {
					$('#cityGunValue').val("30");
				} else if(d.contentsId == "CI00000002") {
					$('#cityGunValue').val("31");
				} else if(d.contentsId == "CI00000003") {
					$('#cityGunValue').val("1");
				} else if(d.contentsId == "CI00000004") {
					$('#cityGunValue').val("17");
				} else if(d.contentsId == "CI00000005") {
					$('#cityGunValue').val("29");
				}
			} else {
				if(d.children == undefined) {
					$('#contentsId').val(d.contentsId);
					$('#contentsGb').val(d.contentsGb);			
					$('#subContentsGb').val(d.subContentsGb);
					$('#contentsName').val(d.name);
				}
			}
		}
	}
	/* D3 관련 차트 끝*/