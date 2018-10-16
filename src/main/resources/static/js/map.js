var map;

$(document).ready(function() {
	
	initMap();
	
	var address = $(".btn-station");
	address.each(function() {
		$(this).dblclick(function() {
			console.log($(this).text());
			//$(location).prop('href', 'ele/ele.html');
			window.location.href = "ele"
		});
		$(this).click(function() {
			console.log($(this).text());
			var lng = $(this).data("addr").lng;
			var lat = $(this).data("addr").lat;
			var point = new BMap.Point(lng, lat);
			map.centerAndZoom(point, 15);
		});
		
		//在地图上添加标记点
		var lng = $(this).data("addr").lng;
		var lat = $(this).data("addr").lat;
		addMarker(new BMap.Point(lng, lat), 1, $(this).text());
	})
});

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

//	addMarker(point, 0);
//	addMarker(new BMap.Point(119.236, 34.613), 1);
//	addMarker(new BMap.Point(119.246, 34.613), 2);
	// map.openInfoWindow(infoWindow, point); // 打开信息窗口
}

function addMarker(point, state, title) { // 创建图标对象
	var img;
	if (state == 0) {
		img = "img/marker_green.png";
	} else if (state == 1) {
		img = "img/marker_yellow.png";
	} else {
		img = "img/marker_red.png";
	}
	var myIcon = new BMap.Icon(img, new BMap.Size(32, 42), {
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
	// 创建标注对象并添加到地图
	var marker = new BMap.Marker(point, {
		icon : myIcon
	});
	marker.setTitle(title);
	map.addOverlay(marker);

	marker.addEventListener("click", function(e) {
		var opts = {
			width : 250, // 信息窗口宽度
			height : 100, // 信息窗口高度
			title : $(this).getTitle() // 信息窗口标题
		}
		var infoWindow = new BMap.InfoWindow("状态:正常", opts); // 创建信息窗口对象
		this.openInfoWindow(infoWindow);
	});
	marker.addEventListener("dblclick", function(e) {
		window.location.href = "ele"
	});
}