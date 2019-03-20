$(document).ready(function() {
	try {
		initMap();
	} catch (e) {
	}
	
	$.get('/station/find/treeNode/allStation/').done(function(treeData) {
		initTreeData(treeData);
		$('#tree-devices').treeview({
			data : treeData,
			enableLinks : true,
			showBorder : false,
			color : '#ffffff',
			backColor : 'transparent',
			onhoverColor : '#6c757d',
			levels : 1,
			showTags : true,
			onNodeSelected : function(event, data) {
				if (data.type == "station") {
					$('#station_info').load("/station/info/"+ data.deviceId);
				}
				var stationNode;
				if (data.type == 'substation') {
					stationNode = $('#tree-devices').treeview('getParent',data);
				} else {
					stationNode = data;
				}
				var point = new BMap.Point(stationNode.lng,stationNode.lat);
				map.centerAndZoom(point,15);
			}
		});
//		$('#tree-devices .list-group-item .badge').css('background-color', '#dc3545');
//		$('#tree-devices .list-group-item .badge').addClass('badge-danger');
	});
});

function initTreeData(treeData) {
	var deviceId = $('#tree-devices').data('device-id');
	for ( var i in treeData) {
		var station = treeData[i];
		var lng = station.lng;
		var lat = station.lat;
		var state = station.stateCode;
		stations.push(new mapStation(station.deviceId, station.text));
		addMarker(new BMap.Point(lng, lat), state, station.text);

		isNodeToSelected(station, deviceId);
//		if (isNodeToSelected(station, deviceId)) {
//			return;
//		}
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