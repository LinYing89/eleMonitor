var stompClient;
var userName = $("#span-user-name").text();

$(document).ready(function() {
	$("#dev-coding").css("outline", "red solid");
	alarmBegin($("#dev-coding"));
	
//	$('.clickable').click(function(){
//		console.log(this);
//		$('#temModal').modal('show')
//	})
});

function alarmBegin(devObj) {
	
	var cssBorder = {outlineWidth: "0"};
	devObj.animate(cssBorder, 2000, rowBack2);

	function rowBack2() {
		if(cssBorder.outlineWidth <= '0') cssBorder.outlineWidth = '4';
		else if(cssBorder.outlineWidth === "4") cssBorder.outlineWidth = '0';
		devObj.animate(cssBorder, 2000, rowBack2);
	}
}

function initWebSocket(){
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame){
		console.info("stompClient connected");
		
	});
	
	var topicDevState = "/topic/" + userName + "/devState";
	stompClient.subscribe(topicDevState, handlerDevState);
}

function handlerDevState(message){
	var devState = JSON.parse(message.body);
	if(devState.haveDevGroup){
		if(devState.alarm){
			var cardGroup = $("card-group-" + devState.devGroupId);
			alarmBegin(cardGroup);
		}
	}else{
		$("#value-dev-" + devState.devId).text(devState.value);
	}
}