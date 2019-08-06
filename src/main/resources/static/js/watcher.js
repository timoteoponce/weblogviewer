$(document).ready(function() {
	var fileId = $('#fileId').val();
	if (fileId) {
		console.log('file Id', fileId)
		$.get("../fetch/" + fileId, function(data) {
			$("#logContent").html("<span>" + data + "</span>");
		});
	}
});