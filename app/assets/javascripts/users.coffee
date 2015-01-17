$ ->
	$.get "/listusers", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").append $("<a>").attr("href", item.profileLink).text item.fullName
