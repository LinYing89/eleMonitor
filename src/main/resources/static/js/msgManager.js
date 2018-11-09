
$(document).ready(function() {
	initWebSocket();
	$("#btn-send-config").click(function(){
		//发送配置信息
		var msgManagerId = $(this).data("msg-manager-id")
		stompClient.send("/app/configDev", {}, msgManagerId);
	});
	
	$("#form-add-msgmanager").submit(function(e){
		e.preventDefault();
		var urlAction = $("#form-add-msgmanager").attr("action");
		var form = new FormData($("#form-add-msgmanager")[0]);
		$.ajax({
            url:urlAction,
            type:"post",
            data:$("#form-add-msgmanager").serialize(),
            processData:false,
            success:function(data){
            	if(data.code != 0){
            		//失败显示信息
            		$("#manager-code-repeated").text(data.msg);
            	}else{
            		//成功重新加载
            		window.location.reload();
            	}
            }
        });
	});
	
	$("#select-function-code").change(function(){
		var functionCode = $(this).val();
		functionCodeChanged(functionCode);
	});
});

function functionCodeChanged(functionCode){
	if(functionCode==1 || functionCode==2){
		$("#dataLengthHelp").text("读取的位数");
	}else{
		$("#dataLengthHelp").text("读取的字数");
	}
}

$("#del-msg-manager").click(function() {
	var r = confirm("确认删除通信管理机?");
	if (r == true) {
		var url = $(this).attr("href");
		window.location.href=url;
	} 
	return false;
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

$('#editMsgManagerModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget)
	var title = modal.find('#editMsgManagerModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加通信管理机");
		var substationId = target.data('substation-id')
		modal.find('form').attr('action', '/msgManager/' + substationId)
	} else {
		title.text("编辑通信管理机");
		var id = target.data('id')
		var name = target.data('name')
		var code = target.data('code')
		var place = target.data('place')

		modal.find('#manager-name').val(name)
		modal.find('#manager-code').val(code)
		modal.find('#manager-place').val(place)

		modal.find('form').attr('action', '/msgManager/edit/' + id)
	}
});

$('#editCollectorModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#editCollectorModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加采集终端");
		var msgManagerId = target.data('msg-manager-id')
		modal.find('form').attr('action', '/collector/' + msgManagerId)
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
		// If necessary, you could initiate an AJAX request here (and then do
		// the
		// updating in a callback).
		// Update the modal's content. We'll use jQuery here, but you could use
		// a
		// data binding library or other methods instead.

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
	
//	var topicDevState = "/topic/" + userName + "/devState";
//	stompClient.subscribe(topicDevState, handlerDevState);
}