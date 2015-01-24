$ ->
	$.get "/listusers", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").append $("<a>").attr("href", item.profileLink).attr("target", "_blank").text item.fullName
