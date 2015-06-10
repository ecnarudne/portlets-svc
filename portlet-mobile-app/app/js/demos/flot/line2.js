$(function () {
    
  var d1, d2, data, chartOptions

  d1 = [
    [1262304000000, 5], [1264982400000, 10], [1267401600000, -8], [1270080000000, -15], 
    [1272672000000, 25], [1275350400000, 20], [1277942400000, 34], [1280620800000, 12], 
    [1283299200000, 0.5], [1285891200000, -10], [1288569600000, 16], [1291161600000, 18]
  ]
 
  d2 = [
    [1262304000000, 7], [1264982400000, 12], [1267401600000, -10], [1270080000000, -18],
    [1272672000000, 15], [1275350400000, 18], [1277942400000, 20], [1280620800000, 10], 
    [1283299200000, 1], [1285891200000, -8], [1288569600000, 14], [1291161600000, 16]
  ]

  data = [{ 
    label: "High-Yield Dividends(%)", 
    data: d1
  }, {
    label: "S&P 500 ",
    data: d2
  }]
 
  chartOptions = {
    xaxis: {
      min: (new Date(2009, 12, 1)).getTime(),
      max: (new Date(2010, 11, 2)).getTime(),
      mode: "time",
      tickSize: [1, "month"],
      monthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
      tickLength: 0
    },
    yaxis: {

    },
    series: {
      lines: {
        show: true, 
        fill: '#ffffff',
        lineWidth: 3
      },
      points: {
        show: true,
        radius: 3,
        fill: true,
        fillColor: "#ffffff",   
        lineWidth: 2
      }
    },
    grid: { 
      color: "#ffffff",
      hoverable: true, 
      clickable: false, 
      borderWidth: 0 
    },
    legend: {
      show: true,
      position: "ne",
      backgroundColor: '#ffffff',
      backgroundOpacity: 0.5,
    },
    tooltip: true,
    tooltipOpts: {
      content: '%s: %y'
    },
    colors: mvpready_core.layoutColors
  }    

  var holder = $('#line-chart')

  if (holder.length) {
    $.plot(holder, data, chartOptions )
  }

})