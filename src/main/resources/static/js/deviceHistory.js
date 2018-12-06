var mainChart;

$(document).ready(function() {
	// var mainChart = $("#div-main-chart");
	$('#devices-selectpicker').selectpicker();
	var charts = $(".div-chart");
	charts.each(function() {
		var chart = echarts.init($(this)[0]);
		initChart(chart, $(this).data("dev-name"));
		if ($(this).attr("id") == 'div-main-chart') {
			mainChart = chart;
		}
		var devId = $(this).data("dev-id");
		$.get("/valueHistory/dev/" + devId).done(function(data) {
			// 填入数据
			chart.setOption({
				dataset : {
					source : data
				}
			});

		});
	});

	$("#btn-create_charts").click(function() {
		var values = $('#devices-selectpicker').val();
		var ids = '';
		for (i in values) {
			ids = ids + values[i] + ' ';
		}
		$.get("/valueHistory/devs/" + ids).done(function(data) {
			mainChart.clear();
			// 填入数据
			mainChart.setOption({
				legend : {},
				tooltip : {},
				dataZoom : [ {
					type : "inside",
					start : 80,
					end : 100
				} ],
				dataset : {
					source : data.dataSet
				},
				xAxis : {
					type : 'category',
				},
				yAxis : {},
				series : data.listChartSeries

			});

		});
	});

	// myChart = echarts.init(document.getElementById('div-chart'));
	// initChart();
})

function initChart(chart, title) {
	// 指定图表的配置项和数据
	var option = {
		title : {
			text : title
		},
		legend : {},
		tooltip : {},
		dataZoom : [
		// { // 这个dataZoom组件，默认控制x轴。
		// type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
		// top:'bottom',
		// start: 80, // 左边在 10% 的位置。
		// end: 100 // 右边在 60% 的位置。
		// },
		{
			type : "inside",
			start : 80,
			end : 100
		} ],
		dataset : {
			// 这里指定了维度名的顺序，从而可以利用默认的维度到坐标轴的映射。
			// 如果不指定 dimensions，也可以通过指定 series.encode 完成映射，参见后文。
			// dimensions: ['time', 'name', 'value'],
			source : [
			// ['device', '2015', '2016'],
			// ['tem', 10, 20]
			]
		},
		xAxis : {
			type : 'category',
		},
		yAxis : {},
		series : [ {
			type : "line",
			seriesLayoutBy : 'row'
		}, {
			type : "line",
			seriesLayoutBy : 'row'
		} ]
	};

	// 使用刚指定的配置项和数据显示图表。
	chart.setOption(option);
}

// function initChart(chart, title){
// //指定图表的配置项和数据
// var option = {
// title : {
// text : title
// },
// legend : {},
// tooltip : {},
// dataset : {
// // 提供一份数据。
// source : [ [ 'tem', 'A相', 'B相', 'C相' ],
// [ '2001-08-10\n11:12:10', 23, 24, 22.7 ],
// [ '2001-08-10\n11:12:13', 25, 27, 32 ],
// [ '2001-08-10\n11:12:16', 33, 20, 22 ],
// [ '2001-08-10\n11:12:17', 31, 29, 27 ] ]
// },
// xAxis : {
// type : 'category',
// },
// yAxis : {},
// series : [ {
// type : "line"
// }, {
// type : "line"
// }, {
// type : "line"
// } ]
// };
//
// // 使用刚指定的配置项和数据显示图表。
// chart.setOption(option);
// }

