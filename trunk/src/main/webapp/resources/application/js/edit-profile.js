/**
 * Comportamiento para el template de edit-profile.
 *
 * User: Victor Del Rio
 */

function initialize() {

    $.each($(".direccion"), function( index, value ) {
        // Create the autocomplete object, restricting the search to geographical location types.
        autocomplete = new google.maps.places.Autocomplete(
            $(value)[0],
            {
                types: ['geocode'],
                componentRestrictions: { country: 'ar' }
            }
        );

        // When the user selects an address from the dropdown, populate the address fields in the form.
        google.maps.event.addListener(autocomplete, 'place_changed', function () {
            fillInAddress();
        });
    });
}

function fillInAddress() {

    // Get the place details from the autocomplete object.
//    var place = autocomplete.getPlace();

}

$(document).ready(function() {

    initialize();
	bindFormToFeedback("edit-profile-form");
});
