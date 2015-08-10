'use strict'
angular.module('PagePortletCtrl',['Api'])
.controller(
    'PagePortletCtrl'
    [
        "$scope"
        "$log"
        "portletApi"
        "$location"
        "$routeParams"
        ($scope,$log,portletApi,$location,$routeParams)->
            $log.debug('PagePortletCtrl controller called')
            portletApi.getPortfolioGraphData(
                {
                  before: ->
                    $log.debug('Fetching graph data.')
                  success: (data, status, headers, config) ->
                    $scope.portlets = data
                    plotGraph(data)
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  graph data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete')
                }
            )
            console.log 'Route parameters: ' + $routeParams.portletId
            ###portletApi.getPortletDetails(
                $routeParams.portletId  
                {
                  before: ->
                    $log.debug('Fetching portlet details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portlet = data
                    $scope.portlet.dailyReturn = $scope.portlet.dailyReturn.toFixed(2)
                    $scope.portlet.annualReturn = $scope.portlet.annualReturn.toFixed(2)
                    $scope.portlet.totalReturn = $scope.portlet.totalReturn.toFixed(2)
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  portlet Details.')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )###
            portletApi.getPortletStatTable(
                5
                {
                  before: ->
                    $log.debug('Fetching data for StatTable.')
                  success: (data, status, headers, config) ->
                    stocks=[]
                    console.log "data :"  + JSON.stringify data[0]
                    data.filter((stock)->
                      console.log stock.closePrice
                      stockURL = undefined
                      stockURL = '/#/page-stock/' + stock.id
                      stockJson ={}
                      stockJson.COMPANY = "<a href= "  + stockURL + ">" + stock.stock.name + "</a>"
                      stockJson.TICKER = stock.stock.symbol
                      stockJson.ACTIVITY = stock.activity
                      stockJson.WEIGHT = 2
                      stockJson.AVG_COST = 500
                      stockJson.PRICE = stock.closePrice
                      stockJson.TOTAL_RETURN = 5.5
                      stockJson.DAILY_RETURN = 3.3
                      stocks.push stockJson
                      )
                    createTable(stocks)
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  StockStat data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            $scope.copyPortlet = (portletId) ->
              console.log 'portlet Id: ' + portletId
              $location.path("/copy-portlet/" + portletId)
    ]

)