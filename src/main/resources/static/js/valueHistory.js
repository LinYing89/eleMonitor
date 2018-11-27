
var myChart;

$(document).ready(function() {
	myChart = echarts.init(document.getElementById('div-chart'));
	initChart();
})

function initChart(){
	//指定图表的配置项和数据
	var option = {
		title : {
			text : '电缆测温'
		},
		legend : {},
		tooltip : {},
		dataset : {
			// 提供一份数据。
			source : [ [ 'tem', 'A相', 'B相', 'C相' ],
					[ '2001-08-10\n11:12:10', 23, 24, 22.7 ],
					[ '2001-08-10\n11:12:13', 25, 27, 32 ],
					[ '2001-08-10\n11:12:16', 33, 20, 22 ],
					[ '2001-08-10\n11:12:17', 31, 29, 27 ] ]
		},
		xAxis : {
			type : 'category',
		},
		yAxis : {},
		series : [ {
			type : "line"
		}, {
			type : "line"
		}, {
			type : "line"
		} ]
	};

	// 使用刚指定的配置项和数据显示图表。
	myChart.setOption(option);
}

