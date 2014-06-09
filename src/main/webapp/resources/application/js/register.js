/**
 *
 *
 * User: Victor Del Rio
 */


function initialize() {

    // Create the autocomplete object, restricting the search to geographical location types.
    autocomplete = new google.maps.places.Autocomplete(
        document.getElementById('ciudad'),
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
    var place = autocomplete.getPlace();
    alert(place.formatted_address);

    // Get each component of the address from the place details and fill the corresponding field on the form.
//    for (var i = 0; i < place.address_components.length; i++) {
//
//        var addressType = place.address_components[i].types[0];
//
//    }
}

$(document).ready(function() {

//	var provincia = $("#provincia");
//    var ciudad = $("#ciudad");
//
//    provincia.chosen();
//	ciudad.chosen();
//
//    provincia.chosen().change(function() {
//
//        var provinciaId = $(this).val();
//
//        $.ajax({
//            type: "GET",
//            url: "/user/list-cities",
//            data: {provinciaId: provinciaId},
//            success: function(data, textStatus) {
//
//                ciudad.empty();
//                ciudad.append(data);
//                ciudad.prop("disabled", false);
//                ciudad.trigger("chosen:updated");
//            }
//        });
//	});

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
