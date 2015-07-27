$ ->
	$.get "/listportletstocks/1", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").text item.portlet.stock+'-'+item.stock+'-'+item.weightage
$ ->
	$.get "/listportlets", (data) ->
		$.each data, (index, p) ->
			$("#portlet").append $("<option>").attr("value", p.id).text(p.name)
