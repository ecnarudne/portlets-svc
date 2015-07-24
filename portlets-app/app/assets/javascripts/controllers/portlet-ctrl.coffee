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
            portletApi.getPortletDetails(
                $routeParams.portletId  
                {
                  before: ->
                    $log.debug('Fetching portlet details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portlet = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  portlet Details.')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            portletApi.getPortletStatTable(
                $routeParams.portletId
                {
                  before: ->
                    $log.debug('Fetching data for StatTable.')
                  success: (data, status, headers, config) ->
                    stocks=[]
                    data.filter((stock)->
                      stockURL = undefined
                      stockURL = '/#/page-stock/' + stock.stats.stock.id
                      stockJson ={}
                      stockJson.COMPANY = "<a href= "  + stockURL + ">" + stock.stats.stock.name + "</a>"
                      stockJson.TICKER = stock.stats.stock.symbol
                      stockJson.ACTIVITY = stock.stats.activity
                      stockJson.WEIGHT = stock.buyWeight
                      stockJson.AVG_COST = stock.buyPrice
                      stockJson.PRICE = stock.stats.closePrice
                      stockJson.TOTAL_RETURN = stock.totalReturn
                      stockJson.DAILY_RETURN = stock.dailyReturn
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