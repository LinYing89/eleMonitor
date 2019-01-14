$(document).ready(function() {
	$.get('/station/find/treeNode/allStation/').done(function(treeData) {
		initTreeData(treeData);
		$('#tree-devices').treeview({
			data : treeData,
			enableLinks : true,
			showBorder : false,
			backColor : '#00000000',
			levels : 1,
			showTags : true,
			onNodeSelected : function(event, data) {
				if (data.type == "station") {
					$('#station_info').load("/station/info/"+ data.deviceId);
					if(data.nodes.length > 0){
						$('#substation_info').attr('hidden', false);
						var subData = data.nodes[0];
						$('#substation_info').load("/substation/info/"+ subData.deviceId);
					}else{
						$('#substation_info').attr('hidden', true);
					}
				} else {
					$('#substation_info').load("/substation/info/"+ data.deviceId);
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