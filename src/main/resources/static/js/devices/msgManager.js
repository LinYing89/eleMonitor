$(document).ready(function() {
	initWebSocket();
	$("#btn-send-config").click(function(){
		//发送配置信息
		var msgManagerId = $(this).data("msg-manager-id")
		stompClient.send("/app/configDev", {}, msgManagerId);
	});
	
	$("#select-function-code").change(function(){
		var functionCode = $(this).val();
		functionCodeChanged(functionCode);
	});
	
	var collectors = $(".del-collector");
	collectors.each(function(){
		$(this).click(function(){
			var r = confirm("确认删除采集终端吗?");
			if (r == true) {
				var url = $(this).attr("href");
				window.location.href=url;
			} 
			return false;
		});
	});
});

function functionCodeChanged(functionCode){
	if(functionCode==1 || functionCode==2){
		$("#dataLengthHelp").text("读取的位数");
	}else{
		$("#dataLengthHelp").text("读取的字数");
	}
}

$('#editCollectorModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#editCollectorModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加采集终端");
		var msgManagerId = target.data('msg-manager-id')
		modal.find('form').attr('action', '/collector/add/' + msgManagerId)
	} else {
		title.text("编辑采集终端");
		var id = target.data('id')
		var busCode = target.data('buscode') // Extract info from data-*
		var functionCode = target.data('functioncode')
		var name = target.data('name')
		var code = target.data('code')
		var beginAddress = target.data('beginaddress')
		var dataLength = target.data('datalength')
		var dataType = target.data('datatype')
		
		modal.find('#busCode').val(busCode)
		modal.find('#select-function-code').val(functionCode)
		modal.find('#name').val(name)
		modal.find('#code').val(code)
		modal.find('#beginAddress').val(beginAddress)
		modal.find('#dataLength').val(dataLength)
		modal.find('#dataType').val(dataType)
		
		functionCodeChanged(functionCode);

		modal.find('form').attr('action', '/collector/edit/' + id)
	}
});

function initWebSocket(){
	var socket = new SockJS("/eleMonitor-dev");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame){
		console.info("stompClient connected");
	});
}
