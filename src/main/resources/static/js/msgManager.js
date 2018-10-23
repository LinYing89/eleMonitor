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
		modal.find('#name').val(name)
		modal.find('#code').val(code)
		modal.find('#beginAddress').val(beginAddress)
		modal.find('#dataLength').val(dataLength)
		modal.find('#dataType').val(dataType)

		modal.find('form').attr('action', '/collector/edit/' + id)
	}
})