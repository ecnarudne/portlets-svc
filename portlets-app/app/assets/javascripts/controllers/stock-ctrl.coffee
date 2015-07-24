'use strict'
angular.module('PageStockCtrl',['Api'])
.controller(
    'PageStockCtrl'
    [
        "$scope"
        "$log"
        "portletApi"
        "$location"
        "$routeParams"
        ($scope,$log,portletApi,$location,$routeParams)->
            $log.debug('PageStockCtrl controller called')
            new (TradingView.MediumWidget)(
              'container_id': 'tv-medium-widget-fb788'
              'symbols': [ [
                'Apple'
                'AAPL '
              ] ]
              'gridLineColor': '#E9E9EA'
              'fontColor': '#83888D'
              'underLineColor': '#dbeffb'
              'trendLineColor': '#4bafe9'
              'width': '100%'
              'height': '400px'
              'chartOnly': true)
            
            portletApi.getStockDetails(
                stockId = $routeParams.stockId
                {
                  before: ->
                    $log.debug('Fetching table data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.stock = data
                    portletApi.getStockStat(
                      symbol = $scope.stock.symbol
                      {
                        before: ->
                          $log.debug('Fetching StockStat.')
                        success: (data, status, headers, config) ->
                          $log.debug 'Stock stat Data fetched successfully.' + JSON.stringify(data)
                          $scope.stockStat = data
                        error: (data, status, headers, config, statusText) ->
                          $log.error('Got error while getting  StockStat data')                   
                        complete: (data, status, headers, config) ->
                          $log.debug('In complete')
                      }
                    )
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  stock data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete')
                }
            )
            
    ]

)