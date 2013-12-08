/**
 *
 *
 * User: Victor Del Rio
 */

$(document).ready(function() {

	$("#provincia").chosen();
	$("#ciudad").chosen();

	$("#provincia").change(function() {

		// TODO llamada ajax de las ciudades dada la provincia seleccionada

		$.each(result, function() {
			// TODO ver si hay una forma de mandar el toString para no concatenar strings
			$("#ciudad").append($("<option />").val(this.id).text(this.nombre + " (" + this.codigoPostal + ")"));
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
