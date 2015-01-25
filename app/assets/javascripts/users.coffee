$ ->
	$.get "/listusers", (data) ->
		$.each data, (index, item) ->
			$("#list").append $("<li>").append(
				$("<a>").attr("href", item.profileLink).attr("target", "_blank").text(item.fullName)
			).append(' - ').append(
				$("<img>").attr("src", item.profilePicture).attr("height", 20).attr("width", 20)
			).append(' - ').append(
				"googleId: "+item.googleId
			).append(' - ').append(
				"facebookId: "+item.facebookId
			)
