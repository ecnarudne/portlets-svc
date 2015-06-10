$(function () {

	var data, chartOptions

	data = [
		{ label: "Ecommerce", data: Math.floor (Math.random() * 100 + 250) }, 
		{ label: "Travel", data: Math.floor (Math.random() * 100 + 350) }, 
		{ label: "Social Networks", data: Math.floor (Math.random() * 100 + 650) }, 
		{ label: "Finance", data: Math.floor (Math.random() * 100 + 50) },
		{ label: "Portals", data: Math.floor (Math.random() * 100 + 250) }
	]

	chartOptions = {		
		series: {
			pie: {
				show: true,  
				innerRadius: .5, 
				stroke: {
					width: 4
				}
			}
		}, 
		legend: {
			position: 'ne'
		}, 
		tooltip: true,
		tooltipOpts: {
			content: '%s: %y'
		},
		grid: {
			hoverable: true
		},
		colors: mvpready_core.layoutColors
	}


	var holder = $('#donut-chart')

	if (holder.length) {
		$.plot(holder, data, chartOptions )
	}

})