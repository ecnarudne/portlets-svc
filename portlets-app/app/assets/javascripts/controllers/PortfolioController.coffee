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
                    $scope.portfolio = data
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
            portletApi.getMyPortlets(
                {
                  before: ->
                    $log.debug('Fetching table data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'MyPortlets fetched successfully.' + JSON.stringify(data)
                    $scope.portlets = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            
    ]

)