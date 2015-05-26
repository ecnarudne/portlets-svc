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
            
            portletApi.getPortfolioDetails(
                {
                  before: ->
                    $log.debug('Fetching stops.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Stops fetched successfully.' + JSON.stringify(data)
                    $scope.data = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  stops. Number')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getStops')
                }
            )
            
    ]

)