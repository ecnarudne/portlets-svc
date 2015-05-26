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
                    $log.debug('Fetching table data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.data = data[0]
                    $log.debug 'table Item:     ' + JSON.stringify(data.tableItems)

                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            
    ]

)