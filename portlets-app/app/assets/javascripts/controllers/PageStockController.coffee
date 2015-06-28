'use strict'
angular.module('PageStockCtrl',['Api'])
.controller(
    'PageStockCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
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
                  before: ->
                    $log.debug('Fetching table data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.stock = data[0].stock
                    $scope.stockStat = data[0].stockStat
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
            )
            
    ]

)