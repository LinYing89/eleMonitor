var stompClient;
var userName = $("#span-user-name").text();

$(document).ready(function() {
	initWebSocket();

	// $("#dev-coding").css("outline", "red solid");
	// alarmBegin($("#dev-coding"));

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
	
	$(".btn-group-dev").each(function(){
		changeSwitchBtnActive($(this).data("value"), $(this).data("id"));
	});
	
	$(".btn-group-switch").each(function(){
		changeGroupSwitchBtnActive($(this).data("value"), $(this).data("id"));
	});
	
	$(".ele-card").each(function(){
		var alarm = $(this).data("dev-alarming");
		if(alarm){
			alarmBegin($(this));
		}
	});
});

$("#del-substation").click(function() {
	var r = confirm("确认删除站点?");
	if (r == true) {
		var url = $(this).attr("href");
		window.location.href = url;
	}
	return false;
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
	// devObj.css({
	// "color" : "red"
	// });
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
	var substationId = $("#v-pills-tab").data("substation-id");
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");

		// var topicDevState = "/topic/" + userName + "/devState";
		var topicDevState = "/topic/" + substationId + "/devState";
		stompClient.subscribe(topicDevState, handlerDevState);
		var topicDevEvent = "/topic/" + substationId + "/devEvent";
		stompClient.subscribe(topicDevEvent, handlerDevEvent);
	});

}

function handlerDevState(message) {
	var devState = JSON.parse(message.body);
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

function handlerDevEvent(message) {
	var devEvent = JSON.parse(message.body);
	console.info(devEvent.message);
	
	var tr = $('<tr></tr>');
	var td1 = $('<td>' + devEvent.timeFormat + '</td>');
	var td2 = $('<td>' + devEvent.message + '</td>');
	tr.prepend(td1);
	tr.prepend(td2);
	$("#tbody-event").prepend(tr);
	
//	var li = $('<li class="list-group-item">' + devEvent.timeFormat + ' '
//			+ devEvent.message + '</li>');
//	li.css({
//		color : "#FF4500",
//		fontWeight : "bold"
//	});
//	$("#ul-event").prepend(li);
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
//		$("#btn-dev-on-" + devId).removeClass("active");
//		$("#btn-dev-off-" + devId).addClass("active");
	}
}

function changeGroupSwitchBtnActive(devValue, devId) {
	if (devValue == 1) {
		$("#btn-dev-on-group-" + devId).attr("class", "btn active btn-outline-success btn-dev-on")
		$("#btn-dev-off-group-" + devId).attr("class", "btn btn-outline-success btn-dev-off")
		
//		$("#btn-dev-on-group-" + devId).addClass("active");
//		$("#btn-dev-off-group-" + devId).removeClass("active");
	} else {
		$("#btn-dev-on-group-" + devId).attr("class", "btn btn-outline-secondary btn-dev-on")
		$("#btn-dev-off-group-" + devId).attr("class", "btn active btn-outline-secondary btn-dev-off")
//		$("#btn-dev-on-group-" + devId).removeClass("active");
//		$("#btn-dev-off-group-" + devId).addClass("active");
	}
}