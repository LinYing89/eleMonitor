var map;
// 站点数组, 点击marker时遍历
var stations = new Array();
var stompClient;
var stationStateChart

$(document).ready(function() {

	try {
		initMap();
	} catch (e) {
	}

	initWebSocket();

	initStationChart2();

	$("#select_map_style").change(function() {
		var valueType = $(this).val();
		var mapStyle = {
			style : valueType
		}
		map.setMapStyle(mapStyle);
	});
});

// 删除站点
function delStation(stationId) {
	var r = confirm("确认删除站点吗?");
	if (r == true) {
		var url = '/station/del/' + stationId;
		window.location.href = url;
	}
}

function initMap() {
	map = new BMap.Map("container");
	// 创建地图实例
	var point = new BMap.Point(119.226, 34.613);
	// 创建点坐标
	map.centerAndZoom(point, 15);
	// 初始化地图，设置中心点坐标和地图级别
	map.enableScrollWheelZoom(true);

	// var ctrl = new BMap.NavigationControl();
	var opts = {
		anchor : BMAP_ANCHOR_BOTTOM_RIGHT
	}
	// var mapStyle={ style : "midnight" }
	// map.setMapStyle(mapStyle);
	// ctrl.anchor = BMAP_ANCHOR_BOTTOM_RIGHT;
	map.addControl(new BMap.NavigationControl(opts));
	map.addControl(new BMap.ScaleControl());
	map.addControl(new BMap.OverviewMapControl());
	map.addControl(new BMap.MapTypeControl());
	map.setCurrentCity("连云港"); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用

	// 测试维护
	addEngineer(new BMap.Point(119.236, 34.613), "张三");
	addEngineer(new BMap.Point(119.246, 34.613), "李四");
	// addMarker(point, 0);
	// addMarker(new BMap.Point(119.236, 34.613), 1);
	// addMarker(new BMap.Point(119.246, 34.613), 2);
	// map.openInfoWindow(infoWindow, point); // 打开信息窗口
}

function addMarker(point, state, title) { // 创建图标对象
	var img = initImage(state);

	var myIcon = initIcon(img);
	// 创建标注对象并添加到地图
	var marker = new BMap.Marker(point, {
		icon : myIcon
	});
	marker.setTitle(title);
	map.addOverlay(marker);

	// marker.addEventListener("click", function(e) {
	// var opts = {
	// width : 250, // 信息窗口宽度
	// height : 100, // 信息窗口高度
	// offset : new BMap.Size(0, -42), // 便宜, 防止盖住marker
	// title : $(this)[0].getTitle()
	// // 信息窗口标题
	// }
	// var infoWindow = new BMap.InfoWindow("状态:正常", opts); // 创建信息窗口对象
	// this.openInfoWindow(infoWindow);
	// });
	marker.addEventListener("dblclick", function(e) {
		var title = $(this)[0].getTitle();
		for (i in stations) {
			if (stations[i].name == title) {
				window.location.href = "/substation/" + stations[i].id + "/0";
				return;
			}
		}

	});
}

function addEngineer(point, title) {
	var img = "/img/engineer.png";
	var myIcon = new BMap.Icon(img, new BMap.Size(32, 32), {
		anchor : new BMap.Size(32, 32),
	});
	var marker = new BMap.Marker(point, {
		icon : myIcon
	});
	marker.setTitle(title);
	map.addOverlay(marker);
}

function initImage(state) {
	var img;
	if (state == 0) {
		img = "/img/marker_green.png";
	} else if (state == 1) {
		img = "/img/marker_yellow.png";
	} else if(state == 2){
		 img = "/img/marker_red.png";
//		img = "/img/alarm.gif";
	}else{
		img = "/img/marker_blue.png";
	}
	return img;
}

function initIcon(img) {
	return new BMap.Icon(img, new BMap.Size(32, 42), {
		// 指定定位位置。
		// 当标注显示在地图上时，其所指向的地理位置距离图标左上
		// 角各偏移10像素和25像素。您可以看到在本例中该位置即是
		// 图标中央下端的尖角位置。
		anchor : new BMap.Size(20, 42),
	// 设置图片偏移。
	// 当您需要从一幅较大的图片中截取某部分作为标注图标时，您
	// 需要指定大图的偏移位置，此做法与css sprites技术类似。
	// imageOffset: new BMap.Size(0, 0 - index * 25) // 设置图片偏移
	});
}

function initWebSocket() {
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");
		var topicDevState = "/topic/admin/stationState";
		stompClient.subscribe(topicDevState, handlerState);
	});

}

function handlerState(obj) {
	var message = JSON.parse(obj.body);
	var id = message.stationId;
	var title = message.stationName;
	var state = message.code;
	var allOverlay = map.getOverlays();
	for (var i = 0; i < allOverlay.length - 1; i++) {
		if (allOverlay[i].getTitle() == title) {
			var img = initImage(state);
			var myIcon = initIcon(img);
			allOverlay[i].setIcon(myIcon);
			break;
		}
	}
	$.get("/station/find/stationstateCount").done(function(data) {
		var chartData = new Array();
		for ( var i in data) {
			var count = data[i];
			chartData.push(count);
		}

		setStationChartData(chartData);
	});
}

function mapStation(id, name) {
	this.id = id;
	this.name = name;
}

$('#editStationModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#editStationModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加站点");
		var msgManagerId = target.data('msg-manager-id');
		modal.find('form').attr('action', '/station/add');
	} else {
		title.text("编辑站点");
		var id = target.data('station-id');
		var name = target.data('name');
		var address = target.data('address');
		var lat = target.data('lat');
		var lng = target.data('lng');
		var tel = target.data('tel');
		var remark = target.data('remark');

		modal.find('#station_name').val(name);
		modal.find('#station_address').val(address);
		modal.find('#station_lat').val(lat);
		modal.find('#station_lng').val(lng);
		modal.find('#station_tel').val(tel);
		modal.find('#station_remark').val(remark);

		modal.find('form').attr('action', '/station/edit/' + id);
	}
});

function initStationChart2() {
	// var chart = $('#div-station-chart');
	var domChart = document.getElementById('div-station-chart');
	var normalCount = domChart.getAttribute('data-station-normal');
	var offlineCount = domChart.getAttribute('data-station-offline');
	var alarmCount = domChart.getAttribute('data-station-alarm');
	var unset = domChart.getAttribute('data-station-unset');

//	normalCount = 10;
//	offlineCount = 3;
//	alarmCount = 1;
//	unset = 1;

	stationStateChart = echarts.init(domChart);
	option = {
		// 全局调色盘。#28a745(success)
		color : [ '#dc3545', '#FF7F00', '#5CACEE', '#71C671' ],
		title : {
			subtext : '站点状态'
		},
		legend : {
			itemWidth : 10,
			itemHeight : 10,
			itemGap : 5,
			y : 'bottom',
			data : [ '异常', '离线', '正常', '未配置' ]
		},
		grid : {
			top : '20%',
			bottom : '13%',
			containLabel : true
		},
		xAxis : {
			type : 'value',
			minInterval: 1,
			boundaryGap : [ 0, 0.01 ]
		},
		yAxis : {
			type : 'category'
		},
		series : [ {
			name : '异常',
			type : 'bar',
			label : {
				normal : {
					show : true,
					position : 'inside'
				}
			},
			data : [ alarmCount ]
		}, {
			name : '离线',
			type : 'bar',
			label : {
				normal : {
					show : true,
					position : 'inside'
				}
			},
			data : [ offlineCount ]
		}, {
			name : '未配置',
			type : 'bar',
			label : {
				normal : {
					show : true,
					position : 'inside'
				}
			},
			data : [ unset ]
		}, {
			name : '正常',
			type : 'bar',
			label : {
				normal : {
					show : true,
					position : 'inside'
				}
			},
			data : [ normalCount ]
		} ]

	};
	stationStateChart.setOption(option);
}

function setStationChartData(dataValue) {
	// var option = {
	// series : [ {
	// data : data
	// } ]
	// };

	var option = {
		series : [ {
			data : [ dataValue[0] ]
		}, {
			data : [ dataValue[1] ]
		}, {
			data : [ dataValue[2] ]
		}, {
			data : [ dataValue[3] ]
		} ]
	};
	stationStateChart.setOption(option);
}

$('#stationInfoModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = target.data('name');
	var address = target.data('address');
	var lng = target.data('lng');
	var lat = target.data('lat');
	var tel = target.data('tel');
	var remark = target.data('remark');

	modal.find('#stationInfoModalTitle').text('');
	modal.find('#modal_address').text('');
	modal.find('#modal_lat').text('');
	modal.find('#modal_lng').text('');
	modal.find('#modal_tel').text('');
	modal.find('#modal_remark').text('');

	modal.find('#stationInfoModalTitle').text(title);
	modal.find('#modal_address').text(address);
	modal.find('#modal_lat').text(lat);
	modal.find('#modal_lng').text(lng);
	modal.find('#modal_tel').text(tel);
	modal.find('#modal_remark').text(remark);
});
