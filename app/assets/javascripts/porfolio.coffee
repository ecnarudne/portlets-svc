$ ->
	$.get "/myportlets", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").text item.portlet.name+'-'+item.stock+'-'+item.qty+'-'+item.buyPrice
$ ->
	$.get "/allportlets", (data) ->
		$.each data, (index, p) ->
			$("#portlet").append $("<option>").text p.name
			