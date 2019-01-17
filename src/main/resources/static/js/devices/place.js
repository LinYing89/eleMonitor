$(document).ready(function() {
	var place = $(".del-place");
	place.each(function(){
		$(this).click(function(){
			var r = confirm("确认删除位置吗?");
			if (r == true) {
				var url = $(this).attr("href");
				window.location.href=url;
			} 
			return false;
		});
	});
});

$('#editPlaceModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget) // Button that triggered the modal
	var title = modal.find('#editPlaceModalTitle');
	if (target.data('option') == 'add') {
		title.text("添加位置");
		modal.find('#name').val('')
		var substationId = target.data('substation-id')
		modal.find('form').attr('action', '/substation/add/place/' + substationId)
	} else {
		title.text("编辑位置");
		var id = target.data('id')
		var name = target.data('name')
		
		modal.find('#place-name').val(name)

		modal.find('form').attr('action', '/substation/edit/place/' + id)
	}
});