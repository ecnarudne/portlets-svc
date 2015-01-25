$ ->
	$.get "/listportlets", (data) ->
		$.each data, (index, item) ->
			$("#list").append(
				$("<li>").text(
					item.name+'-'+item.notes
				).append(' - ').append(
					$("<img>").attr("src", item.pictureUrl).attr("height", 20).attr("width", 20)
				)
			)
			$("#portlet1").append $("<option>").attr("value", item.id).text(item.name)
			$("#portlet2").append $("<option>").attr("value", item.id).text(item.name)
