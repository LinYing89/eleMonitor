$(document).ready(function() {
	$('#btn_search').click(function(){
		var cardNum = $('#input_card_num').val();
		var substationId = $(this).data('substation-id');
		var hrefHistory = "/doorCardHistory/history/" + substationId + "/1/" + cardNum;
		window.location.href=hrefHistory;
		return false;
	});
})