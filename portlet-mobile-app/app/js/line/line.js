function plotGraph(data1)
{
  $(function () {
    
  var d1, d2, data, chartOptions

 d1 = data1


  data = [{ 
    label: "Portlet Name", 
    data: d1
  }]
 
  chartOptions = {
    xaxis: {
      mode: "time",
      tickLength: 0,
    },
    yaxis: {

    },
    series: {
      lines: {
        show: true, 
        fill: false,
        lineWidth: 1,
      },
      points: {
        show: false,
        radius: 1,
        fill: true,
        fillColor: "#ffffff",   
        lineWidth: 1
      }
    },
    grid: { 
      color: "#ffffff",
      hoverable: true, 
      clickable: false, 
      borderWidth: 0 
    },
    legend: {
      show: false,
      position: "ne",
      backgroundColor: '#ffffff',
      backgroundOpacity: 0,
    },
    tooltip: true,
    tooltipOpts: {
      content: '%s: %y'
    },
    colors: ['#ffffff', mvpready_core.layoutColors]
  }    

  var holder = $('#profile-chart')

if (holder.length) {
      var chm = Object.create(chartOptions);
      $.plot(holder, data, chm);
    /*$.plot(holder, data, chartOptions )*/
      
    $("#whole").click(function () {
      var chm = Object.create(chartOptions);
      $.plot(holder, data, chm);
    });

    $("#nineties").click(function () {
      var chm = Object.create(chartOptions);
      chm.xaxis={
        mode: "time",
        min: (new Date(1990, 0, 1)).getTime(),
        max: (new Date(2000, 0, 1)).getTime(),
        tickLength: 0
      }
      $.plot(holder, data, chm);
    });

    $("#latenineties").click(function () {
      var chm = Object.create(chartOptions);
      chm.xaxis={
        mode: "time",
        minTickSize: [1, "year"],
        min: (new Date(1996, 0, 1)).getTime(),
        max: (new Date(2000, 0, 1)).getTime(),
        tickLength: 0
      }
      $.plot(holder, data, chm);
    });

    $("#ninetyninequarters").click(function () {
      var chm = Object.create(chartOptions);
      chm.xaxis={
        mode: "time",
        minTickSize: [1, "quarter"],
        min: (new Date(1999, 0, 1)).getTime(),
        max: (new Date(2000, 0, 1)).getTime(),
        tickLength: 0
      }
      $.plot(holder, data, chm);
    });

    $("#ninetynine").click(function () {
      var chm = Object.create(chartOptions);
      chm.xaxis={
        mode: "time",
        minTickSize: [1, "month"],
        min: (new Date(1999, 0, 1)).getTime(),
        max: (new Date(2000, 0, 1)).getTime(),
        tickLength: 0
      }
      $.plot(holder, data, chm);
    });

    $("#lastweekninetynine").click(function () {
      var chm = Object.create(chartOptions);
      chm.xaxis={
          mode: "time",
          minTickSize: [1, "day"],
          min: (new Date(1999, 11, 25)).getTime(),
          max: (new Date(2000, 0, 1)).getTime(),
          timeformat: "%a",
          tickLength: 0,
        }
      $.plot(holder, data, chm);
    });

    $("#oneyear").click(function () {
      var chm = Object.create(chartOptions);
      var oneYearEnd= new Date(2007, 0, 01);
      var oneYearStart = new Date(oneYearEnd);
      oneYearStart = new Date(oneYearStart.setYear(oneYearStart.getFullYear() - 3));
      chm.xaxis={
        mode: "time",
        minTickSize: [1, "month"],
        min: (oneYearStart).getTime(),
        max: (oneYearEnd).getTime(),
        tickLength: 0
      }
      $.plot(holder, data, chm);
    });

  }

})
}