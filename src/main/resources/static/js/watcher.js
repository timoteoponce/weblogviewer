$(document).ready(function() {
	var fileId = $('#fileId').val();
	if (fileId) {
		console.log('file Id', fileId)
		$.get("../fetch/" + fileId, function(data) {
			if (data) {
				$("#logContent").html("<span>" + data + "</span>");
			} else {
				$("#logContent").html("<span>Empty content</span>");
			}

		});
	}
});