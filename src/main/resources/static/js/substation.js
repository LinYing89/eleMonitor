var stompClient;
var userName = $("#span-user-name").text();

$(document).ready(function() {
	initWebSocket();

	$("#dev-coding").css("outline", "red solid");
	alarmBegin($("#dev-coding"));
});

$("#del-substation").click(function() {
	var r = confirm("确认删除站点?");
	if (r == true) {
		var url = $(this).attr("href");
		window.location.href=url;
	} 
	return false;
});

function alarmBegin(devObj) {
	devObj.css({
		"background" : "#FF7256"
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

function alarmEnd(devObj) {
	devObj.css({
		"background" : ""
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

function initWebSocket() {
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");

		// var topicDevState = "/topic/" + userName + "/devState";
		var topicDevState = "/topic/admin/devState";
		stompClient.subscribe(topicDevState, handlerDevState);
	});

}

function handlerDevState(message) {
	var devState = JSON.parse(message.body);
	var card;
	if (devState.haveDevGroup) {
		card = $("#card-group-" + devState.devGroupId);
		$("#value-group-" + devState.devId).text(devState.value);
	} else {
		card = $("#card-dev-" + devState.devId);
	}
	if (devState.alarm) {
		alarmBegin(card);
	} else {
		alarmEnd(card);
	}
	$("#value-dev-" + devState.devId).text(devState.value);
}