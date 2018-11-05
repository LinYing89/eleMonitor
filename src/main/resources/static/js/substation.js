var stompClient;
var userName = $("#span-user-name").text();

$(document).ready(function() {
	initWebSocket();

//	$("#dev-coding").css("outline", "red solid");
//	alarmBegin($("#dev-coding"));
	
	$(".btn-dev-on").click(function(){
		var id = $(this).data("id");
		var devCtrlData = {devId:id, option:1};
		stompClient.send("/app/ctrlDev", {}, JSON.stringify(devCtrlData));
	});
	
	$(".btn-dev-off").click(function(){
		var id = $(this).data("id");
		var devCtrlData = {devId:id, option:0};
		stompClient.send("/app/ctrlDev", {}, JSON.stringify(devCtrlData));
	});
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

function newEventColorAnim(devObj){
//	devObj.css({
//		"color" : "red"
//	});
	var cssBorder = {color: "black", fontWeight:"normal"};
	devObj.animate(cssBorder, 5000, rowBack);
	
	function rowBack(){
		$(this).css({fontWeight:"normal"});
	}
}

function initWebSocket() {
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");

		// var topicDevState = "/topic/" + userName + "/devState";
		var topicDevState = "/topic/admin/devState";
		stompClient.subscribe(topicDevState, handlerDevState);
		var topicDevEvent = "/topic/admin/devEvent";
		stompClient.subscribe(topicDevEvent, handlerDevEvent);
	});

}

function handlerDevState(message) {
	var devState = JSON.parse(message.body);
	var card;
	if (devState.haveDevGroup) {
		card = $("#card-group-" + devState.devGroupId);
		$("#value-group-" + devState.devId).text(devState.valueString);
		if(devState.onOff){
			changeGroupSwitchBtnActive(devState.value, devState.devId);
		}
	} else {
		card = $("#card-dev-" + devState.devId);
		if(devState.onOff){
			changeSwitchBtnActive(devState.value, devState.devId)
		}
	}
	if (devState.alarm) {
		alarmBegin(card);
	} else {
		alarmEnd(card);
	}
	
	$("#value-dev-" + devState.devId).text(devState.valueString);
}

function handlerDevEvent(message){
	var devEvent = JSON.parse(message.body);
	console.info(devEvent.message);
	var li = $('<li class="list-group-item">' + devEvent.timeFormat + ' ' + devEvent.message + '</li>');
	li.css({color:"#FF4500", fontWeight:"bold"});
	$("#ul-event").prepend(li);
	newEventColorAnim(li);
}

function changeSwitchBtnActive(devValue, devId){
	if(devValue == 1){
		$("#btn-dev-on-" + devId).addClass("active");
		$("#btn-dev-off-" + devId).removeClass("active");
	}else{
		$("#btn-dev-on-" + devId).removeClass("active");
		$("#btn-dev-off-" + devId).addClass("active");
	}
}

function changeGroupSwitchBtnActive(devValue, devId){
	if(devValue == 1){
		$("#btn-dev-on-group-" + devId).addClass("active");
		$("#btn-dev-off-group-" + devId).removeClass("active");
	}else{
		$("#btn-dev-on-group-" + devId).removeClass("active");
		$("#btn-dev-off-group-" + devId).addClass("active");
	}
}