
var devices = $(".del-device");
devices.each(function(){
	$(this).click(function(){
		var r = confirm("确认删除数据节点吗?");
		if (r == true) {
			var url = $(this).attr("href");
			window.location.href=url;
		} 
		return false;
	});
});

$('#addDeviceModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#addDeviceModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加设备");
		var collectorId = target.data('collector-id')
		modal.find('form').attr('action', '/device/' + collectorId)
	} else {
		title.text("编辑设备");
		var id = target.data('id')
		var name = target.data('name')
		var beginAddress = target.data('beginaddress')
		var dataLength = target.data('datalength')
		var byteOrder = target.data('byteorder')
		var coefficient = target.data('coefficient')
		var unit = target.data('unit')
		var valueFormat = target.data('valueformat')
		var valueType = target.data('valuetype')
		
		modal.find('#name').val(name)
		modal.find('#beginAddress').val(beginAddress)
		modal.find('#dataLength').val(dataLength)
		modal.find('#byteOrder').val(byteOrder)
		modal.find('#coefficient').val(coefficient)
		modal.find('#unit').val(unit)
		modal.find('#valueFormat').val(valueFormat)
		modal.find('#valueType').val(valueType)

		modal.find('form').attr('action', '/device/edit/' + id)
	}
})