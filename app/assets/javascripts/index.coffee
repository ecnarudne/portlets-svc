$ ->
	$.get "/listusers", (data) ->
		$.each data, (index, user) ->
			$("#users").append $("<li>").text user.fullName
