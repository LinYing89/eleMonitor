var stompClient;
var pause = false;
var filter = "";

$(document).ready(function() {
	initWebSocket();

	$("#pause").click(function() {
		pause = !pause;
		if (pause) {
			$(this).text("启动");
		} else {
			$(this).text("暂停");
		}
	});

	$("#filter").click(function() {
		filter = $("#input-filter").val();
	});
	$("#clean").click(function() {
		$("#container").empty();
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
	if (pause) {
		return;
	}
	var res = JSON.parse(message.body);
	
	var div = null;
	res.listErrorResult.forEach(function(errorResult) {
		// 过滤条件
		if (filter != "") {
			var heads = errorResult.head.split(" ");
			if (heads.length >= 6) {
				var managerCode = (parseInt(heads[2], 16) << 32)
						| (parseInt(heads[3], 16) << 16)
						| (parseInt(heads[4], 16) << 8)
						| (parseInt(heads[5], 16));
				if (parseInt(filter) != managerCode) {
					return;
				}
			} else {
				return;
			}
		}
		
		var divInfo;
		if(div == null){
			div = $('<div>' + res.time + " - " + res.data + '</div>');
			divInfo = $('<div class="ml-5"></div>')
			div.append(divInfo);
		}

		var pData = $('<p class="m-0"><small class="text-primary">data: '
				+ errorResult.data + '</small></p>');
		divInfo.append(pData);

		if (errorResult.code != 0) {
			var p = $('<p class="m-0"><small class="text-danger">'
					+ errorResult.message + '</small></p>');
			divInfo.append(p);
		}
		var p = $('<p class="m-0"><small class="text-secondary">head: '
				+ errorResult.head + '</small></p>');
		divInfo.append(p);
		errorResult.listOne.forEach(function(one) {
			var pone = $('<p class="m-0"><small class="text-secondary">coll: '
					+ one + '</small></p>');
			divInfo.append(pone);
		});
	});

	// if(res.code != 0){
	// var p = $('<p class="m-0"><small class="text-danger">' + res.message +
	// '</small></p>');
	// divInfo.append(p);
	// }
	// var p = $('<p class="m-0"><small class="text-secondary">head:&#9;' +
	// res.head + '</small></p>');
	// divInfo.append(p);
	// res.listOne.forEach(function(one){
	// var pone = $('<p class="m-0"><small class="text-secondary">coll:&#9;' +
	// one + '</small></p>');
	// divInfo.append(pone);
	// });
	if(div != null){
		$("#container").append(div);
	}
}