$ ->
	$.get "/admin/listmocks", (data) ->
		$.each data, (index, item) ->
			$("#list").append($("<li>").text(item))
			$("#nick").append $("<option>").attr("value", item).text(item)
