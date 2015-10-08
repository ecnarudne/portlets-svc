'use strict'
angular.module('PortfolioCtrl',['ionic','Api'])
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
                    $log.debug('Fetching Portfolio details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Portfolio Details: ' + JSON.stringify(data)
                    $scope.portfolio = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting Portfolio details')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails')
                }
            )
            portletApi.getPortfolioGraphData(
                {
                  before: ->
                    $log.debug('Fetching graph data.')
                  success: (data, status, headers, config) ->
                    plotGraph(data)
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  graph data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            portletApi.getMyPortlets(
                {
                  before: ->
                    $log.debug('Fetching my portlets.')
                  success: (data, status, headers, config) ->
                    $log.debug 'MyPortlets: ' + JSON.stringify(data)
                    console.log 'MyPortlets: ' + JSON.stringify(data[0])
                    $scope.portlets = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  my portlets')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of get My portlets.')
                }
            )
    ]

)