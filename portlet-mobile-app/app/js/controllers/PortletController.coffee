'use strict'
angular.module('PortletCtrl',['ionic','Api'])
.controller(
    'PortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        "$routeParams"
        ($scope,$log,$http,$cookies,portletApi,$location,$routeParams)->
            $log.debug('PagePortletCtrl controller called')
            path = $location.path()
            pathArray = path.split('/')
            $scope.portletId = pathArray[pathArray.length-1]
            
            portletApi.getPortfolioGraphData(
                {
                  before: ->
                    $log.debug('Fetching graph data.')
                  success: (data, status, headers, config) ->
                    plotPortletGraph(data)
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  graph data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            console.log 'Route parameters: ' + $routeParams.portletId
            portletApi.getPortletDetails(
                $scope.portletId 
                {
                  before: ->
                    $log.debug('Fetching page portlet data.')
                    console.log 'Fetching page portlet data.'
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portlet = data
                    console.log 'portlet data.' +JSON.stringify(data)
                    $scope.portlet.dailyReturn = data.dailyReturn.toFixed(2)
                    $scope.portlet.annualReturn = data.annualReturn.toFixed(2)
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  portlet data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            portletApi.getPortletStatTable(
                $scope.portletId
                {
                  before: ->
                    $log.debug('Fetching data for StatTable.')
                  success: (data, status, headers, config) ->
                    $scope.stocks = data
                    $log.debug 'Portlets stocks are fetched successfully' + JSON.stringify(data)
                    console.log 'Portlets stocks are fetched successfully:' +JSON.stringify(data[0])
                    ###data.filter((stock)->
                      stockJson ={}
                      stockJson.COMPANY = stock.stats.stock.name
                      stockJson.TICKER = stock.stats.stock.symbol
                      stockJson.ACTIVITY = stock.stats.activity
                      stockJson.WEIGHT = stock.buyWeight
                      stockJson.AVG_COST = stock.buyPrice
                      stockJson.PRICE = stock.stats.closePrice
                      stockJson.TOTAL_RETURN = stock.totalReturn
                      stockJson.DAILY_RETURN = stock.dailyReturn
                      stocks.push stockJson
                      )
                    console.log JSON.stringify stocks
                    createTable(stocks)###

                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            
    ]

)