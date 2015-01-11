$ ->
	$.get "/allportlets", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").text item.name
