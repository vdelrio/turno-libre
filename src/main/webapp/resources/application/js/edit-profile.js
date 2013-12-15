/**
 * Comportamiento para el template de edit-profile.
 *
 * User: Victor Del Rio
 */

$(document).ready(function() {

    var provincia = $(".provincia");
    var ciudad = $(".ciudad");

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
                ciudad.trigger("chosen:updated");
            }
        });
    });

	bindFormToFeedback("edit-profile-form");

});
