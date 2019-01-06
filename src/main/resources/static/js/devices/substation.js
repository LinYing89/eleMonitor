$(document).ready(function() {

	$(".del-msg-manager").click(function() {
		var r = confirm("确认删除通信管理机?");
		if (r == true) {
			var url = $(this).attr("href");
			window.location.href=url;
		} 
		return false;
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
});

$('#editMsgManagerModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget)
	var title = modal.find('#editMsgManagerModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加通信管理机");
		var substationId = target.data('substation-id')
		modal.find('form').attr('action', '/msgManager/add/' + substationId)
	} else {
		title.text("编辑通信管理机");
		var id = target.data('id')
		var name = target.data('name')
		var code = target.data('code')
		var place = target.data('place')

		modal.find('#manager-name').val(name)
		modal.find('#manager-code').val(code)
		modal.find('#manager-place').val(place)

		modal.find('form').attr('action', '/msgManager/edit/' + code)
	}
});