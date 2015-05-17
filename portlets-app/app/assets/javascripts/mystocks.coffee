$ ->
	$.get "/listmystocks", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").text(item.portlet.name+'-'+item.stock+'-'+item.qty+'-'+item.buyPrice)
$ ->
	$.get "/listportlets", (data) ->
		$.each data, (index, p) ->
			$("#portlet").append $("<option>").text p.name
