$(document).ready(function() {
	$("#select-roles-add").change(function(){
		var value = $(this).val();
		if(value=="1"){
			$("#div-user-station-select-add").hide();
		}else{
			$("#div-user-station-select-add").show();
		}
	});
	$("#select-roles-edit").change(function(){
		var value = $(this).val();
		if(value=="1"){
			$("#div-user-station-select-edit").hide();
		}else{
			$("#div-user-station-select-edit").show();
		}
	});
})

$('#editUserModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	
	var name = target.data('username');
	var personName = target.data('person-name');
	var tel = target.data('tel');
	var roleId = target.data('role-id');
	var enable = target.data('enable');
	var remark = target.data('remark');

	modal.find('#input-username').val(name);
	modal.find('#input-person-name').val(personName);
	modal.find('#input-tel').val(tel);
	modal.find('#select-roles-edit').val(roleId);
	modal.find('#check-enable').attr('checked', enable);
	modal.find('#input-remark').val(remark);
	
	if(roleId=="1"){
		$("#div-user-station-select-edit").hide();
	}else{
		$("#div-user-station-select-edit").show();
	}
});

function checkUser(){
	var password = $("#input-password").val();
	var ensurePassword = $("input-ensure-password").val();
	if(password != ensurePassword){
		return false;
	}
	return true;
}