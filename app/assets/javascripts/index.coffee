$ ->
	$.get "/listusers", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").text item.fullName
