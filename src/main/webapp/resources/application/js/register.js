/**
 *
 *
 * User: Victor Del Rio
 */

$(document).ready(function() {

	var provincia = $("#provincia");
    var ciudad = $("#ciudad");

    provincia.chosen();
	ciudad.chosen();

    provincia.chosen().change(function() {

        var provinciaId = $(this).val();

        $.ajax({
            type: "GET",
            url: "/user/list-cities",
            data: {provinciaId: provinciaId},
            success: function(data, textStatus) {

                ciudad.empty();
                ciudad.append(data);
                ciudad.prop("disabled", false);
                ciudad.trigger("chosen:updated");
            }
        });
	});

	$(".register-form").submit(function(event) {

		var $form = $(this);

		$.ajax({
			type: $form.attr("method"),
			url: $form.attr("action"),
			data: $form.serialize(),
			success: function(data, textStatus) {
				window.location.replace(data);
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				showErrorFeedback(XMLHttpRequest.responseText);
			}
		});
		event.preventDefault();
	});

});
