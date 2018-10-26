$(document).ready(function() {
	$("#dev-coding").css("outline", "red solid");
	alarmBegin($("#dev-coding"));
	
	$('.clickable').click(function(){
		console.log(this);
		$('#temModal').modal('show')
	})
});

function alarmBegin(devObj) {
	
	var cssBorder = {outlineWidth: "0"};
	devObj.animate(cssBorder, 2000, rowBack2);

	function rowBack2() {
		if(cssBorder.outlineWidth <= '0') cssBorder.outlineWidth = '4';
		else if(cssBorder.outlineWidth === "4") cssBorder.outlineWidth = '0';
		devObj.animate(cssBorder, 2000, rowBack2);
	}
}