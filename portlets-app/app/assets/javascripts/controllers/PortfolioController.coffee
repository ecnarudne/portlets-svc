'use strict'
angular.module('PortfolioCtrl',['Api'])
.controller(
    'PortfolioCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('PortfolioCtrl controller called')
            plotGraph()
            portletApi.getPortfolioDetails(
                {
                  before: ->
                    $log.debug('Fetching table data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.data = data[0]
                    $scope.portfolio = $scope.data.portfolio 
                    $scope.arrow = undefined
                    dailyReturn = parseFloat $scope.portfolio.dailyReturn
                    if dailyReturn < 0
                        $scope.arrow = 'fa-sort-down'
                    else
                        $scope.arrow = 'fa-sort-up'

                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            
    ]

)