$(document).ready(function() {
	var stationId = $('#tree-devices').data('station-id');
	$.get('/substation/allDevice/' + stationId).done(function(treeData) {
		initTreeData(treeData);

		$('#tree-devices').treeview({
			data : treeData,
			// enableLinks : true,
			showBorder : false,
			backColor : '#00000000',
			levels : 1,
			onNodeSelected : function(event, data) {
				console.info('?' + data.href);
				window.location.href = data.href;
			}
		});

	});
	
	initWebSocket();

	$(".btn-dev-on").click(function() {
		var id = $(this).data("id");
		var devCtrlData = {
			devId : id,
			option : 1
		};
		stompClient.send("/app/ctrlDev", {}, JSON.stringify(devCtrlData));
	});

	$(".btn-dev-off").click(function() {
		var id = $(this).data("id");
		var devCtrlData = {
			devId : id,
			option : 0
		};
		stompClient.send("/app/ctrlDev", {}, JSON.stringify(devCtrlData));
	});

	$(".btn-group-dev").each(function() {
		changeSwitchBtnActive($(this).data("value"), $(this).data("id"));
	});

	$(".btn-group-switch").each(function() {
		changeGroupSwitchBtnActive($(this).data("value"), $(this).data("id"));
	});

	$(".ele-card").each(function() {
		var alarm = $(this).data("dev-alarming");
		if (alarm) {
			alarmBegin($(this));
		}
	});
	$("#del-substation").click(function() {
		var r = confirm("确认删除站点?");
		if (r == true) {
			var url = $(this).attr("href");
			window.location.href = url;
		}
		return false;
	});
});

function initTreeData(treeData) {
	var deviceType = $('#tree-devices').data('device-type');
	if (null != deviceType && deviceType != undefined) {
		var deviceId = $('#tree-devices').data('device-id');
		for ( var i in treeData) {
			var station = treeData[i];
			if (deviceType == 'station') {
				if (isNodeToSelected(station, deviceId)) {
					return;
				}
			}
		}
	}
}

function isNodeToSelected(treeNode, deviceId) {
	if (treeNode.deviceId == deviceId) {
		eval("treeNode.state={}");
		eval("treeNode.state.selected=true");
		return true;
	} else {
		return false;
	}
}

function expandedNode(treeNode) {
	eval("treeNode.state={}");
	eval("treeNode.state.expanded=true");
}

$('#editSubstationModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget)
	var title = modal.find('#editSubstationModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加变电站");
		var stationId = target.data('station-id')
		modal.find('form').attr('action', '/substation/' + stationId)
	} else {
		title.text("编辑变电站");
		var id = target.data('substation-id')
		var name = target.data('name');

		modal.find('#substation_name').val(name);

		modal.find('form').attr('action', '/substation/edit/' + id)
	}
});

function alarmBegin(devObj) {
	if (devObj.data("alarming")) {
		return;
	}
	devObj.data("alarming", true);
	devObj.css({
		backgroundColor : "white"
	});
	var cssBorder = {
		backgroundColor : "red"
	};
	devObj.animate(cssBorder, 500, rowBack1);

	function rowBack1() {
		cssBorder.backgroundColor = 'white';
		devObj.animate(cssBorder, 500, rowBack2);
	}
	function rowBack2() {
		cssBorder.backgroundColor = 'red';
		devObj.animate(cssBorder, 500, rowBack1);
	}
}

function alarmEnd(devObj) {
	devObj.stop();
	devObj.data("alarming", false);
//	devObj.attr("data-alarming", false);
	devObj.css({
		backgroundColor : ""
	});
	// var cssBorder = {outlineWidth: "0"};
	// devObj.animate(cssBorder, 2000, rowBack2);
	//
	// function rowBack2() {
	// if(cssBorder.outlineWidth <= '0') cssBorder.outlineWidth = '4';
	// else if(cssBorder.outlineWidth === "4") cssBorder.outlineWidth = '0';
	// devObj.animate(cssBorder, 2000, rowBack2);
	// }
}

function newEventColorAnim(devObj) {
	var cssBorder = {
		color : "black",
		fontWeight : "normal"
	};
	devObj.animate(cssBorder, 5000, rowBack);

	function rowBack() {
		$(this).css({
			fontWeight : "normal"
		});
	}
}

function initWebSocket() {
	var substationId = $("#tree-devices").data("device-id");
	
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");
		var topicDevState = "/topic/" + substationId + "/devState";
		stompClient.subscribe(topicDevState, handlerDevState);
		//订阅电力数据
//		var topicDevState = "/topic/" + substationId + "/devEle";
//		stompClient.subscribe(topicDevState, handlerDevEle);
		var topicDevEvent = "/topic/" + substationId + "/devEvent";
		stompClient.subscribe(topicDevEvent, handlerDevEvent);
	});

}

//function handlerDevEle(message){
//	var devEle = JSON.parse(message.body);
//	
//}

function handlerDevState(message) {
	var devState = JSON.parse(message.body);
	if(devState.valueType == 'ELE'){
		//电力数据
		handlerEleValue(devState);
		return;
	}
	
	var card = null;
	if (devState.haveDevGroup) {
		card = $("#card-group-" + devState.devGroupId);
		$("#value-group-" + devState.devId).text(devState.valueString);
		if (devState.onOff) {
			changeGroupSwitchBtnActive(devState.value, devState.devId);
		}
	} else {
		card = $("#card-dev-" + devState.devId);
		if (devState.onOff) {
			changeSwitchBtnActive(devState.value, devState.devId)
		}
	}
	if (card != null) {
		if (devState.alarm) {
			alarmBegin(card);
		} else {
			alarmEnd(card);
		}
	}

	$("#value-dev-" + devState.devId).text(devState.valueString);
}

//电力数据处理
function handlerEleValue(devState){
	$("#" + devState.devId).text(devState.valueString);
	var phaseNum = devState.phaseNum;
	//找到tr父元素
	var tr = $("#" + devState.devId).parent().parent().parent();
	//找到tr元素下的对应相的电压电流因数值
	var pu = tr.find(".p" + phaseNum + "-u").text();
	var pi = tr.find(".p" + phaseNum + "-i").text();
	var pf = tr.find(".p" + phaseNum + "-f").text();
	var power = pu * pi * pf/ 1000;
	//保留两位小数, 四舍五入
	tr.find(".p" + phaseNum + "-p").text(power.toFixed(2));
}

function handlerDevEvent(message) {
	var devEvent = JSON.parse(message.body);
	console.info(devEvent.message);
	var tr = $('<tr></tr>');
	var td1 = $('<td>' + devEvent.timeFormat + '</td>');
	var td2 = $('<td>' + devEvent.message + '</td>');
	tr.prepend(td2);
	tr.prepend(td1);
	$("#tbody-event").prepend(tr);
	
//	var li = $('<li class="list-group-item">' + devEvent.timeFormat + ' '
//			+ devEvent.message + '</li>');
//	li.css({
//		color : "#FF4500",
//		fontWeight : "bold"
//	});
//	$("#ul-event").prepend(li);
	tr.css({
		color : "#FF4500",
		fontWeight : "bold"
	});
	newEventColorAnim(tr);
}

function changeSwitchBtnActive(devValue, devId) {
	if (devValue == 1) {
		$("#btn-dev-on-" + devId).attr("class", "btn active btn-outline-success btn_ctrl")
		$("#btn-dev-off-" + devId).attr("class", "btn btn-outline-success btn_ctrl")
//		$("#btn-dev-on-" + devId).addClass("active");
//		$("#btn-dev-off-" + devId).removeClass("active");
	} else {
		$("#btn-dev-on-" + devId).attr("class", "btn btn-outline-secondary btn_ctrl")
		$("#btn-dev-off-" + devId).attr("class", "btn active btn-outline-secondary btn_ctrl")
	}
}

function changeGroupSwitchBtnActive(devValue, devId) {
	if (devValue == 1) {
		$("#btn-dev-on-group-" + devId).attr("class", "btn active btn-outline-success btn-dev-on")
		$("#btn-dev-off-group-" + devId).attr("class", "btn btn-outline-success btn-dev-off")
	} else {
		$("#btn-dev-on-group-" + devId).attr("class", "btn btn-outline-secondary btn-dev-on")
		$("#btn-dev-off-group-" + devId).attr("class", "btn active btn-outline-secondary btn-dev-off")
	}
}
