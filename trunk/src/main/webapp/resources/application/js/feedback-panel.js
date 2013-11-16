/**
 * Comportamiento para el feedback-panel.
 *
 * User: Victor Del Rio
 * Date: 16/11/13
 * Time: 14:47
 */

$(document).ready(function() {

	$(".close-alert").click(function() {
		$(".feedback-msg-container").addClass("hidden");
	});

});

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
