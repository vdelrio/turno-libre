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

                $.each(data, function() {
                    // TODO ver si hay una forma de mandar el toString para no concatenar strings
                    ciudad.append($("<option />").val(this.id).text(this.nombre + " (" + this.codigoPostal + ")"));
                    ciudad.prop("disabled", false);
                    ciudad.trigger("chosen:updated");
                });
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
