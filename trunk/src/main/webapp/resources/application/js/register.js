/**
 *
 *
 * User: Victor Del Rio
 */


function initialize() {

    // Create the autocomplete object, restricting the search to geographical location types.
    autocomplete = new google.maps.places.Autocomplete(
        document.getElementById('direccion'),
        {
            types: ['geocode'],
            componentRestrictions: { country: 'ar' }
        }
    );

    // When the user selects an address from the dropdown, populate the address fields in the form.
    google.maps.event.addListener(autocomplete, 'place_changed', function () {
        fillInAddress();
    });
}

function fillInAddress() {

    // Get the place details from the autocomplete object.
//    var place = autocomplete.getPlace();

}

$(document).ready(function() {

    initialize();

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
