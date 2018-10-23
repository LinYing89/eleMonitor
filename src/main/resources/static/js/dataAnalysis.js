$(document).ready(function() {
	$('.btn-chart').click(function() {
		changeChart($(this).data("chart-type"));
	})
});

function changeChart(type) {
	console.log(type);
	chartType = type;
	var option = {
		series: [{
				type: chartType
			},
			{
				type: chartType
			},
			{
				type: chartType
			}
		]
	};
	myChart.setOption(option);
	myChart2.setOption(option);
}