var stompClient;

$(document).ready(function() {
	initWebSocket();
	
	$("#clean").click(function() {
		$("#container").empty();
	});
});

function initWebSocket() {
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");

		var topic = "/topic/sent";
		stompClient.subscribe(topic, handlerSent);
	});
}

function handlerSent(message) {
	var res = JSON.parse(message.body);
	
	var div = $('<div>' + res.time + " - " + res.sentMsg + '</div>');
	var divInfo = $('<div class="ml-5"></div>')
	div.append(divInfo);
	if(res.code != 0){
		var p = $('<p class="m-0"><small class="text-danger">' + res.message + '</small></p>');
		divInfo.append(p);
	}
	$("#container").append(div);
}