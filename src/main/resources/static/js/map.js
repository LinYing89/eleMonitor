var map;
// 站点数组, 点击marker时遍历
var stations = new Array();
var stompClient;

$(document).ready(function() {

	$.get('/station/find/treeNode/allStation/').done(function(treeData) {
		initTreeData(treeData);
		$('#tree-devices').treeview({
			data : treeData,
			 enableLinks : true,
			showBorder : false,
			backColor : '#00000000',
			levels : 1,
			onNodeSelected : function(event, data) {
				console.info('?' + data.href);
//				var stationInfo = $('#station_info');
//				var url = "/station/info/" + data.deviceId;
//				stationInfo.load(url);
				$('#station_info').load("/station/info/" + data.deviceId);
				var stationNode;
				if(data.type == 'substation'){
					stationNode = $('#tree-devices').treeview('getParent', data);
				}else{
					stationNode = data;
				}
				var point = new BMap.Point(stationNode.lng, stationNode.lat);
				map.centerAndZoom(point, 15);
				// window.location.href = data.href;
			}
		});
	});

	try {
		initMap();
	} catch (e) {
	}

	initWebSocket();
	
	$("#del_station").click(function(){
		var r = confirm("确认删除站点吗?");
		if (r == true) {
			var url = $(this).attr("href");
			window.location.href=url;
		} 
		return false;
	});
});

function initTreeData(treeData) {
	var deviceId = $('#tree-devices').data('device-id');
	for ( var i in treeData) {
		var station = treeData[i];
		var lng = station.lng;
		var lat = station.lat;
		var state = $(this).data("stateCode");
		stations.push(new mapStation(station.deviceId, station.text));
		addMarker(new BMap.Point(lng, lat), state, station.text);
		
		if (isNodeToSelected(station, deviceId)) {
			return;
		}
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
	// ctrl.anchor = BMAP_ANCHOR_BOTTOM_RIGHT;
	map.addControl(new BMap.NavigationControl(opts));
	map.addControl(new BMap.ScaleControl());
	map.addControl(new BMap.OverviewMapControl());
	map.addControl(new BMap.MapTypeControl());
	map.setCurrentCity("连云港"); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用

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

function initImage(state) {
	var img;
	if (state == 0) {
		img = "/img/marker_green.png";
	} else if (state == 1) {
		img = "/img/marker_yellow.png";
	} else {
		// img = "img/marker_red.png";
		img = "/img/alarm.gif";
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

	if (state == 0) {
		$("#s" + id).css("color", "black");
	} else if (state == 1) {
		$("#s" + id).css("color", "yellow");
	} else {
		$("#s" + id).css("color", "red");
	}
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