var mainChart;
var chartDataSet = new Array(new Array());
var colors = [ '#FFC125', '#3CB371', '#CD0000', '#c23531','#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',  '#ca8622', '#bda29a','#6e7074', '#546570', '#c4ccd3'];

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
	});

	$("#btn-create-charts").click(function() {
		// 获取选中的设备的id
		var values = $('#devices-selectpicker').val();
		if (values.length == 0) {
			return;
		}

		// 获取时间段
		var startDateStr = $('#datetime_start').val();
		var endDateStr = $('#datetime_end').val();
		var interval = $('#select-datatime-interval').val();

		var startDate = new Date(startDateStr);
		var endDate = new Date(endDateStr);
		// 如果开始时间大于结束时间, 非法
		if (startDate >= endDate) {
			return;
		}

		$(this).attr('disabled', true);

		var ids = '';
		for (i in values) {
			ids = ids + values[i] + ' ';
		}

		var sendMsg = ids + "," + startDateStr + "," + endDateStr + "," + interval;
		mainChart.clear();
		chartDataSet = new Array(new Array());

		chartDataSet = new Array(); // 先声明一维
		for (var i = 0; i < values.length + 1; i++) { // 一维长度
			chartDataSet[i] = new Array(); // 再声明二维
		}
		var myseries = new Array();
		
		for (var i = 0; i < values.length; i++) { // 一维长度
			if (i < colors.length) {
				myseries[i] = initSerie("line", colors[i]);
			} else {
				myseries[i] = initSerie('line', null);
			}
		}
		mainChart.setOption({
			legend : {},
			tooltip : {},
			dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
				type : 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
				top : 'bottom',
				start : 0, // 左边在 10% 的位置。
				end : 100
			}, {
				type : "inside",
				start : 0,
				end : 100
			} ],
			grid: {
				left:'40',
				top: '30',
				right: '40',
		        bottom: '70',
		    },
			dataset : {
				source : [ [] ]
			},
			xAxis : {
				type : 'category',
			},
			yAxis : {},
			series : myseries
		});

		mainChart.showLoading();
		$.ajax({
			url: '/valueHistory/devs/' + sendMsg,
			success: function(result){onReceivedHistory(result)},
			error: function(){
				$("#btn-create-charts").attr('disabled', false);
				mainChart.hideLoading();
				alert('获取数据失败');
			}
		});
//		$.get('/valueHistory/devs/' + sendMsg).done(function (data) {
//			onReceivedHistory(data);
//		});
	});

	$("#btn-chart-line").click(function() {
		var newSeries = new Array();
		var series = mainChart.getOption().series;
		for (i in series) {
			newSeries[i] = initSerie("line", colors[i]);
		}
		mainChart.setOption({
			series : newSeries
		});
	});
	$("#btn-chart-bar").click(function() {
		var newSeries = new Array();
		var series = mainChart.getOption().series;
		for (i in series) {
			newSeries[i] = initSerie("bar", colors[i]);
		}
		mainChart.setOption({
			series : newSeries
		});
	});

	// myChart = echarts.init(document.getElementById('div-chart'));
	// initChart();
	initDatePicker();
});

//生成一个系列的样式
function initSerie(style, color){
	if(null == style){
		style = "line";
	}
	if(null == color){
		return {
			type : style,
			seriesLayoutBy : 'row'
		};
	}else{
		return {
			type : style,
			color: color,
			seriesLayoutBy : 'row'
		};
	}
}

function onReceivedHistory(historyData) {
	$("#btn-create-charts").attr('disabled', false);
	console.log('received');
	
	// 填入数据
	for (var i = 0; i < historyData.length; i++) {
		for (var j = 0; j < historyData[i].length; j++) {
			chartDataSet[i].push(historyData[i][j]);
		}
	}
	mainChart.setOption({
		dataset : {
			source : chartDataSet
		},
	// series : history.listChartSeries
	});
	mainChart.hideLoading();
	initTable();
}

function initTable(){
	var column = chartDataSet[0].length;
	var tableHistory = $('#table_value_history');
	tableHistory.empty();
	var caption = $('<caption>历史纪录报表</caption>');
	tableHistory.append(caption);
	var tHead = $('<thead></thead>');
	tableHistory.append(tHead);
	var tHead_tr = $('<tr></tr>');
	tHead.append(tHead_tr);
	for(var i=0; i<chartDataSet.length; i++){
		var tHead_tr_th = $('<th scope="col">' + chartDataSet[i][0] + '</th>');
		tHead_tr.append(tHead_tr_th);
	}
	
	var tBody = $('<tbody></tody>');
	tableHistory.append(tBody);
	for(var j=1; j<column; j++){
		var tBody_tr = $('<tr></tr>');
		tBody.append(tBody_tr);
		for(var i=0; i<chartDataSet.length; i++){
			var tBody_tr_td = $('<td>' + chartDataSet[i][j] + '</td>');
			tBody_tr.append(tBody_tr_td);
		}
	}
}

function initChart(chart, title) {
	// 指定图表的配置项和数据
	var option = {
		title : {
			text : title
		},
		legend : {},
		tooltip : {},
		dataZoom : [ { // 这个dataZoom组件，默认控制x轴。
			type : 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
			top : 'bottom',
			start : 0,
			end : 100
		}, {
			type : "inside",
			start : 0,
			end : 100
		} ],
		grid: {
			left:'40',
			top: '30',
			right: '40',
	        bottom: '70',
	    },
		dataset : {
			// 这里指定了维度名的顺序，从而可以利用默认的维度到坐标轴的映射。
			// 如果不指定 dimensions，也可以通过指定 series.encode 完成映射，参见后文。
			// dimensions: ['time', 'name', 'value'],
			source : chartDataSet
		},
		xAxis : {
			type : 'category',
		},
		yAxis : {},
		series : [ {
			type : "line",
			seriesLayoutBy : 'row',
			areaStyle : {}
		} ]
	};

	// 使用刚指定的配置项和数据显示图表。
	chart.setOption(option);
}

// 初始化日期选择器
function initDatePicker() {
	var myDate = new Date();// 获取系统当前时间
	var startYear = myDate.getFullYear();
	var startMonth = myDate.getMonth() + 1;
	if (startMonth == 1) {
		startYear = startYear - 1;
		startMonth = 12;
	} else {
		startMonth = startMonth - 1;
	}
	// 可选的起始日期, 往前一个月
	var startDateStr = startYear + "-" + startMonth + "-01"
	// 可选的结束日期, 当前日期
	var initEndDate = myDate.getFullYear() + "-" + (myDate.getMonth() + 1)
			+ "-" + myDate.getDate() + " 23:55"; // 获取当前日期
	// 初始化选择的开始日期, 前3天,包括今天
	myDate.setDate(myDate.getDate() - 2)
	var initStartDate = myDate.getFullYear() + "-" + (myDate.getMonth() + 1)
			+ "-" + myDate.getDate() + " 00:00";

	$("#datetime_start").val(initStartDate);
	$("#datetime_end").val(initEndDate);

	$('.form_datetime').datetimepicker({
		format : 'yyyy-mm-dd hh:ii',
		language : 'zh-CN',
		weekStart : 0,
		todayBtn : 1,
		autoclose : 1,
		todayHighlight : 1,
		startDate : startDateStr,
		startView : 2,
		endDate : initEndDate,
		forceParse : 0
	});
}
