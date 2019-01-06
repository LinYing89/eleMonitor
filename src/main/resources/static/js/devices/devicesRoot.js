$(document).ready(function() {
	var substationId = $('#substationName').data('substation-id');
	$.get('/msgManager/allDevice/' + substationId).done(function(treeData) {
		initTreeData(treeData);

		$('#tree-devices').treeview({
			data : treeData,
			// enableLinks : true,
			showBorder : false,
			backColor : '#00000000',
			levels : 2,
			onNodeSelected : function(event, data) {
				console.info('?' + data.href);
				window.location.href = data.href;
			}
		});

	});
});

function initTreeData(treeData) {
	var deviceType = $('#tree-devices').data('device-type');
	if (null != deviceType && deviceType != undefined) {
		var deviceId = $('#tree-devices').data('device-id');
		for ( var i in treeData) {
			var msgManager = treeData[i];
			if (deviceType == 'msgManager') {
				if (isNodeToSelected(msgManager, deviceId)) {
					//expandedNode(msgManager);
					return;
				}
			} else if (deviceType == 'collector') {
				for ( var j in msgManager.nodes) {
					var collector = msgManager.nodes[j];
					if (isNodeToSelected(collector, deviceId)) {
						expandedNode(msgManager);
						return;
					}
				}
			} else if (deviceType == 'device') {
				for ( var j in msgManager.nodes) {
					var collector = msgManager.nodes[j];
					for ( var a in collector.nodes) {
						var device = collector.nodes[a];
						if (isNodeToSelected(device, deviceId)) {
							expandedNode(collector);
							return;
						}
					}

				}
			}
		}
	}
}

function isNodeToSelected(treeNode, deviceId) {
	if (treeNode.deviceId == deviceId) {
		eval("treeNode.state={}");
		eval("treeNode.state.selected=true");
		return true;
	} else {
		return false;
	}
}

function expandedNode(treeNode) {
	eval("treeNode.state={}");
	eval("treeNode.state.expanded=true");
}

function getTree() {
	var tree = [ {
		text : "Parent 1",
		href : "#",
		id : "id",
		nodes : [ {
			text : "Child 1",
			nodes : [ {
				text : "Grandchild 1"
			}, {
				text : "Grandchild 2"
			} ]
		}, {
			text : "Child 2"
		} ]
	}, {
		text : "Parent 2",
		nodes : [ {
			text : "node2 child"
		} ]
	}, {
		text : "Parent 3",
		nodes : []
	}, {
		text : "Parent 4"
	}, {
		text : "Parent 5"
	} ];
	return tree;
}