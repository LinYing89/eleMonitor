
$(document).ready(function() {
	var linkages = $(".del-linkage");
	linkages.each(function(){
		$(this).click(function(){
			var r = confirm("确认删除连锁吗?");
			if (r == true) {
				var url = $(this).attr("href");
				window.location.href=url;
			} 
			return false;
		});
	});
	
	var effect = $(".del-effect");
	effect.each(function(){
		$(this).click(function(){
			var r = confirm("确认删除受控设备吗?");
			if (r == true) {
				var url = $(this).attr("href");
				window.location.href=url;
			} 
			return false;
		});
	});
	
	var aLinkage = $(".a-linkage");
	aLinkage.each(function(){
		$(this).click(function(){
			var linkageId = $(this).data("linkage-id");
			$("#btn-add-effect").attr("data-linkage-id", linkageId);
			var o = $("#btn-add-effect");
			console.info($("#btn-add-effect").data("linkage-id"));
		});
	});
})

$('#addLinkage').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#addLinkageModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加连锁");
		var deviceId = target.data('device-id')
		modal.find('form').attr('action', '/linkage/add/' + deviceId);
	} else {
		title.text("编辑连锁");
		var id = target.data('id')
		var compareSymbol = target.data('comparesymbol')
		var compareValue = target.data('comparevalue')
		var alarming = target.data('alarming')
		
		modal.find('#compareSymbol').val(compareSymbol)
		modal.find('#compareValue').val(compareValue)
		modal.find('#alarming').prop("checked", alarming);

		modal.find('form').attr('action', '/linkage/edit/' + id)
	}
});

$('#addEffect').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#addEffectModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加受控设备");
		var linkageId = target.data('linkage-id')
		modal.find('form').attr('action', '/effect/add/' + linkageId)
	} else {
		title.text("编辑受控设备");
		var id = target.data('id')
		var deviceId = target.data('device-id')
		var value = target.data('value')
		
		modal.find('#select-device').val(deviceId)
		modal.find('#select-value').val(value)

		modal.find('form').attr('action', '/effect/edit/' + id)
	}
});
