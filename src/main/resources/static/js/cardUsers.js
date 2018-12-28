$(document).ready(function() {
	// $('#limitTimeStart').selectpicker();
	// $('#limitTimeEnd').selectpicker();
	initDatePicker();
})

$('#editCardUser')
		.on(
				'show.bs.modal',
				function(event) {
					var modal = $(this)
					var target = $(event.relatedTarget)
					var id = target.data('id')
					if(null == id || id == 'undefined'){
						return;
					}
					var cardNum = target.data('card-num')
					var username = target.data('username')
					var limitTimeStart = target.data('limit-time-start')
					var limitTimeEnd = target.data('limit-time-end')

					modal.find('#username').val(username)
					modal.find('#cardNum').val(cardNum)

					modal.find('#limitTimeStart').val(limitTimeStart)
					modal.find('#limitTimeEnd').val(limitTimeEnd)

					$("#ul-authyority").empty();
					$
							.get('/card/user/authority/' + id)
							.done(
									function(data) {
										for ( var i in data) {
											var doorAuthority = data[i];
											var li = $('<div class="row mb-2"></div>');
											$("#ul-authyority").append(li);
											var label = $('<label class="col-3 col-form-label">'
													+ doorAuthority.device.name
													+ '</label>');
											li.append(label);
											var inputDeviceId = $('<input type="text" name="listDoorAuthority['
													+ i
													+ '].deviceId" value="'
													+ doorAuthority.device.id
													+ '" hidden>');
											li.append(inputDeviceId);
											var select = $('<select class="form-control col-9" name="listDoorAuthority['
													+ i
													+ '].doorCardAuthority"></select>');
											li.append(select);
											if (doorAuthority.doorCardAuthority == 'ENABLE') {
												var optionEnable = $('<option value="ENABLE" selected="selected">允许</option>');
												select.append(optionEnable);
												var optionDisable = $('<option value="DISABLE">禁止</option>');
												select.append(optionDisable);
											} else {
												var optionEnable = $('<option value="ENABLE">允许</option>');
												select.append(optionEnable);
												var optionDisable = $('<option value="DISABLE" selected="selected">禁止</option>');
												select.append(optionDisable);
											}

										}
									});

					modal.find('form').attr('action', '/card/edit/' + id);
				});

function initDatePicker() {
	$('.form_datetime').datetimepicker({
		format : 'yyyy-mm-dd hh:ii',
		language : 'zh-CN',
		weekStart : 0,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startView : 2,
		forceParse : 0
	});
}