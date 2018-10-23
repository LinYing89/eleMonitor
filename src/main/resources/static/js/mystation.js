$('#editStationModal').on('show.bs.modal', function(event) {
	var modal = $(this)
	var target = $(event.relatedTarget)
	
	var id = target.data('id')
	var name = target.data('name')
	var address = target.data('address')
	var lat = target.data('lat')
	var lng = target.data('lng')
	var tel = target.data('tel')
	var remark = target.data('remark')

	modal.find('#name').val(name)
	modal.find('#address').val(address)
	modal.find('#lat').val(lat)
	modal.find('#lng').val(lng)
	modal.find('#tel').val(tel)
	modal.find('#remark').val(remark)

	modal.find('form').attr('action', '/station/edit/' + id)
})