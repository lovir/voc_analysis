<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
.overlay { 
	fill : none !important;
	pointer-events : all !important; 
}
	
.node {
	cursor : pointer !important;
}

line.link {
	fill : none !important;
	stroke : #9ecae1 !important; 
	stroke-width : 1.5px !important;
}

.node text.text_normal {
	pointer-events : none !important;
	font-family : "NanumGothicBold" !important;
	font_size : 13px !important;
	text-anchor : middle !important;
	font-weight : bold !important;
}

.node text.text_center {
	pointer-events : none !important;
	font-family : "NanumGothicBold" !important;
	font_size : 15px !important;
	text-anchor : middle !important;
	font-weight : bold !important;
}

.color_center {	/* 3depth 노드 안 텍스트 색상 */
	fill : #fff !important;
}

.color_city {
	fill : #a26eb3 !important;
}

.color_nature {
	fill : #6ba22c !important;
}

.color_nature1 {
	fill : #6ba22c !important;
	stroke-linecap : round !important;
}

.color_specialty {
	fill : #d55a76 !important;
}

.color_history {
	fill : #4083c6 !important;
}

.color_cultural {
	fill : #ce9514 !important;
}

.color_foaktale {
	fill : #d66f0d !important;
}

.color_festival {
	fill : #867bd4 !important;
}

.color_infrastructure {
	fill : #3ba5a5 !important;
}

.color_default {
	fill : #01DFD7 !important;
}


.node text.text_over {
	pointer-events : none !important;
	fill : #9e1ae1 !important;
	font : 12px sans-serif !important;
	font-weight : bold !important;
	text-anchor : middle !important;
}

.color_center_text {
	fill : black !important;
}
</style>

<title>Insert title here</title>
</head>
<body>
	<div id="radialChartView" style="border:1px solid;"></div>

	<script type="text/javascript" src="<c:url value='/resources/js/d3.js'/>"></script>
	
	<script type="text/javascript">
		var data = <c:out value="${jsonData}" escapeXml="false"/>;
		var chartWidth = 892; //표 크기
		var chartHeight = 531;	//표 크기
		
		var node, link, root, nodes, links, overLinks;
		
		var force = d3.layout.force()
		.on("tick", tick)
		/* .gravity(.4)
		.friction(0.9)
		.charge(-1000)
		.linkDistance(50) */
		.gravity(0.5) // 원들 가운데로 뭉침
		.friction(0.9)
		.charge(-1800) // 원들 뭉쳐짐
		.linkDistance(80) // 원들 사이의 거리
		.size([chartWidth, chartHeight]);
	
		var zoom = d3.behavior.zoom()
		.center([chartWidth / 2, chartHeight / 2])
		.scaleExtent([1, 8])
		.on("zoom", zoomed);
					
		var zoomScale = 1;

		var svg = d3.select("#radialChartView").append("svg")
		.attr("id", "svg1")
		.attr("width", chartWidth)
		.attr("height", chartHeight)
		.attr("overflow", "hidden")
		.append("g")
		.call(zoom)
		.append("g");
	  
		svg.append("rect")
		.attr("class", "overlay")
		.attr("width", chartWidth)
		.attr("height", chartHeight);
		
		//svg.attr("transform", "translate(-190.38523855296387, -118.4959586290625)scale(1.3195079107728946)");
		
		var pos_x = [100 	,0  	,0 		, 65 	, 100 	, 65	, -100 	, -65 	, -100 	, -65];
		var pos_y = [35 	,100 	,-100 	, 90 	, -35 	, -90 	, 35 	, 90 	, -35 	, -90];
		var pos = 0;
		
		root = data;
		root.fixed = true;
		root.x = chartWidth / 2;
		root.y = chartHeight / 2;
		init();
		
		function init() {
			nodes = flatten(root);
			if(nodes != null) {
				for(var i = 0; i < nodes.length; i++) {
					/* if(nodes[i].depth == 2) {	//pos_x, pos_y 값으로 10개 노드 위치 강제 지정.
						nodes[i].x = root.x + pos_x[pos];
						nodes[i].y = root.y + pos_y[pos];
						nodes[i].fixed = true;
						pos++;
					} */
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
			} else if(nodes.length >= 10 && nodes.length < 20 ) {
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
			//.on("mouseover", mouseOverData)	//d3차트가 2개 이상 존재할때 한쪽에만 적용되는 문제 발생.
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
					size = 25;
				}
				if(d.group.indexOf("center") > -1) {
					size = 35;
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
			
			node.append("title").text(function(d) { return d.name; });
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
			.call(zoom.center([chartWidth / 2, chartHeight / 2]).scale(zoomScale).event);
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
			.call(zoom.center([chartWidth / 2, chartHeight / 2]).scale(zoomScale).event);
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
				goMainCityGunView();
			} else {
				if(d.children == undefined) {
					$('#contentsId').val(d.contentsId);
					$('#contentsGb').val(d.contentsGb);			
					$('#subContentsGb').val(d.subContentsGb);
					$('#contentsName').val(d.name);
	
					goSubContentsView();
				}
			}
		}
	</script>
</body>
</html>