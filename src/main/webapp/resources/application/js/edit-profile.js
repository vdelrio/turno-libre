/**
 * Comportamiento para el template de edit-profile.
 *
 * User: Victor Del Rio
 */

$(document).ready(function() {

	$(".edit-profile-form").submit(function(event) {

		var $form = $(this);

		$.ajax({
			type: $form.attr("method"),
			url: $form.attr("action"),
			data: $form.serialize(),
			success: function(data, textStatus) {
				showSuccessFeedback(data);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				showErrorFeedback(XMLHttpRequest.responseText);
			}
		});
		event.preventDefault();
	});

});
