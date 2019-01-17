$(document).ready(function() {
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
	$("#select-value-type").change(function(){
		var valueType = $(this).val();
		if(valueType=="ALARM"){
			$("#div-alarm-value").show();
		}else{
			$("#div-alarm-value").hide();
		}
	});
})

$('#editDeviceModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#addDeviceModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加设备");
		var collectorId = target.data('collector-id')
		modal.find('form').attr('action', '/device/' + collectorId)
		modal.find("#div-alarm-value").hide();
	} else {
		title.text("编辑设备");
		var id = target.data('id')
		var name = target.data('name')
		var placeId = target.data('place-id')
		var beginAddress = target.data('beginaddress')
		var dataLength = target.data('datalength')
		var byteOrder = target.data('byteorder')
		var coefficient = target.data('coefficient')
		var unit = target.data('unit')
		var valueFormat = target.data('valueformat')
		var valueType = target.data('valuetype')
		var alarmValue = target.data('alarmvalue')
		var category = target.data('category')
		var icon = target.data('icon')
		
		modal.find('#name').val(name)
		modal.find('#place-id').val(placeId)
		modal.find('#beginAddress').val(beginAddress)
		modal.find('#dataLength').val(dataLength)
		modal.find('#byteOrder').val(byteOrder)
		modal.find('#coefficient').val(coefficient)
		modal.find('#unit').val(unit)
		modal.find('#valueFormat').val(valueFormat)
		modal.find('#select-value-type').val(valueType)
		modal.find('#select-alarm-trigger-value').val(alarmValue)
		modal.find('#deviceCategory').val(category)
		iconPicker.setIcon(icon);
		
		if(valueType=="ALARM"){
			$("#div-alarm-value").show();
		}else{
			$("#div-alarm-value").hide();
		}

		modal.find('form').attr('action', '/device/edit/' + id)
	}
})
