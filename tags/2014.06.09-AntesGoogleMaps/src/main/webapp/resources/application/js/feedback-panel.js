/**
 * Comportamiento para el feedback-panel.
 *
 * User: Victor Del Rio
 */

$(document).ready(function() {

	$(".close-alert").click(function() {
		$(".feedback-msg-container").addClass("hidden");
	});

});

function bindFormToFeedback(formClass, reset) {

	$("." + formClass).submit(function(event) {

		var $form = $(this);

		$.ajax({
			type: $form.attr("method"),
			url: $form.attr("action"),
			data: $form.serialize(),
			success: function(data, textStatus) {

				showSuccessFeedback(data);
				if (reset) {
					$form.resetFields();
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				showErrorFeedback(XMLHttpRequest.responseText);
			}
		});
		event.preventDefault();
	});
}

function showSuccessFeedback(msg) {

	$(".feedback-msg").html(msg);
	$(".feedback-msg-container").removeClass("alert-danger");
	$(".feedback-msg-container").addClass("alert-success");
	$(".feedback-msg-container").removeClass("hidden");
}

function showErrorFeedback(msg) {

	$(".feedback-msg").html(msg);
	$(".feedback-msg-container").removeClass("alert-success");
	$(".feedback-msg-container").addClass("alert-danger");
	$(".feedback-msg-container").removeClass("hidden");
}
