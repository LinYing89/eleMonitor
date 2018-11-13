var stompClient;
var pause = false;
var filter = "";

$(document).ready(function() {
	initWebSocket();

	$("#pause").click(function() {
		pause = !pause;
		if(pause){
			$(this).text("启动");
		}else{
			$(this).text("暂停");
		}
	});
	
	$("#filter").click(function() {
		filter = $("#input-filter").val();
	});
});

function initWebSocket() {
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		console.info("stompClient connected");

		var topic = "/topic/netMessage";
		stompClient.subscribe(topic, handlerNetMessage);
	});
}

function handlerNetMessage(message) {
	if(pause){
		return;
	}
	var res = JSON.parse(message.body);
	if(filter != ""){
		var heads = res.head.split(" ");
		if(heads.length >= 6){
			var managerCode = (parseInt(heads[2], 16) << 32) | (parseInt(heads[3], 16) << 16) | (parseInt(heads[4], 16) << 8) | (parseInt(heads[5], 16));
			if(parseInt(filter) != managerCode){
				return;
			}
		}
	}
	
	var div = $('<div>' + res.time + " - " + res.receivedMsg + '</div>');
	var divInfo = $('<div class="ml-5"></div>')
	div.append(divInfo);
	if(res.code != 0){
		var p = $('<p class="m-0"><small class="text-danger">' + res.message + '</small></p>');
		divInfo.append(p);
	}
	var p = $('<p class="m-0"><small class="text-secondary">head:&#9;' + res.head + '</small></p>');
	divInfo.append(p);
	res.listOne.forEach(function(one){
		var pone = $('<p class="m-0"><small class="text-secondary">coll:&#9;' + one + '</small></p>');
		divInfo.append(pone);
	});
	$("#container").append(div);
}