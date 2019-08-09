$(document).ready(function() {
	var fileId = $('#fileId').val();
	if (fileId) {
		console.log('file Id', fileId)
		$.get("../fetch/" + fileId, function(data) {
			if (data) {
				$("#logContent").html("<span>" + data + "</span>");
			} else {
				$("#logContent").html("<span>Content can't be displayed, please <strong>download</strong> instead</span>");
			}
		}).fail(function(response){
			$("#logContent").html("<span>Content couldn't be fetched [status = " + response.status +"], , please <strong>download</strong> instead </span>");
		});
	}
});