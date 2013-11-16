/**
 * Custom jQuery functions.
 *
 * User: Victor Del Rio
 * Date: 04/11/13
 * Time: 20:19
 */

(function($){

	$.fn.extend({

		ajaxOnClick: function(url, type, success) {

			$(this).click(function (event) {

				event.preventDefault();
				ajaxRequest(url, type, success);
			});
		},

		changeTabOnClick: function(tabsContainer, selectedTabContent, menuContent) {

			$(this).click(function(event) {

				event.preventDefault();

				// Change the tab
				$("#" + tabsContainer + " > a").removeClass("active");
				$(this).addClass("active");

				// Change the content
				$("#" + menuContent + " > div").addClass("hidden");
				$("#" + selectedTabContent).removeClass("hidden");
			});
		},

		// Resets the fields of a form
		resetFields: function() {

			$(this).find("input:text, input:password, input:file, select, textarea").val("");
			$(this).find("input:radio, input:checkbox").removeAttr("checked").removeAttr("selected");
		}

	});

	function ajaxRequest(url, type, success) {

		$.ajax({
			type: type,
			url: url,
			success: function(data, textStatus) {
				$("#" + success).html(data);
			}
		});
	}

})(jQuery);
