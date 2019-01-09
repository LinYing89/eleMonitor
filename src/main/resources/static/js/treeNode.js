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