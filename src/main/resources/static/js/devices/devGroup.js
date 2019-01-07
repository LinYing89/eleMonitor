
$("#del-dev-group").click(function() {
	var r = confirm("确认删除设备组吗?");
	if (r == true) {
		var url = $(this).attr("href");
		window.location.href=url;
	} 
	return false;
});

$('#editDevGroupModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#addDevGroupModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加数据组");
		var substationId = target.data('substation-id')
		modal.find('form').attr('action', '/devGroup/' + substationId)
	} else {
		title.text("编辑数据组");
		var id = target.data('id')
		var name = target.data('name')
		var valueType = target.data('value-type')
		var lineTem = target.data('line-tem')
		
		modal.find('#name').val(name)
		modal.find('#valueType').val(valueType)
		modal.find('#lineTem').prop("checked", lineTem);

		modal.find('form').attr('action', '/devGroup/edit/' + id)
	}
})

$('#addDevice').on('show.bs.modal', function(event) {
	var modal = $(this);
	var target = $(event.relatedTarget);
	var substationId = $('#substationName').data('substation-id');
	modal.find('#deviceId').empty();
	$.get('/devGroup/' + substationId).done(function (data){
		for(var i in data){
			var device = data[i];
			var option = $('<option></option>');
			option.attr('value', device.id);
			option.text(device.name);
			modal.find('#deviceId').append(option);
		}
	});
})