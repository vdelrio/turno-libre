/**
 * Comportamiento para el feedback-panel.
 *
 * User: Victor Del Rio
 * Date: 16/11/13
 * Time: 14:47
 */

$(document).ready(function() {

	$(".change-password-form").submit(function(event) {

		var $form = $(this);

		$.ajax({
			type: $form.attr("method"),
			url: $form.attr("action"),
			data: $form.serialize(),
			success: function(data, textStatus) {

				showSuccessFeedback(data);
				$form.resetFields();
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				showErrorFeedback(XMLHttpRequest.responseText);
			}
		});
		event.preventDefault();
	});

});
