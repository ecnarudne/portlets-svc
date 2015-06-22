$ ->
	$.get "/listportletstocks/1", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").text item.portlet.name+'-'+item.stock+'-'+item.percent
$ ->
	$.get "/listportlets", (data) ->
		$.each data, (index, p) ->
			$("#portlet").append $("<option>").attr("value", p.id).text(p.name)
