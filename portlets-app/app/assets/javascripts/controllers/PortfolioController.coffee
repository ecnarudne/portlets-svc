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
            portletApi.getPortfolioDetails(
                {
                  before: ->
                    $log.debug('Fetching Portfolio details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portfolio = data
                    $scope.portfolio.dailyReturn = data.dailyReturn.toFixed(2)
                    $scope.portfolio.annualReturn = data.annualReturn.toFixed(2)
                    $cookies.pictureURL = $scope.portfolio.ownerProfilePicture
                    
                    $scope.arrow = undefined
                    dailyReturn = parseFloat $scope.portfolio.dailyReturn
                    if dailyReturn < 0
                        $scope.arrow = 'fa-sort-down'
                    else
                        $scope.arrow = 'fa-sort-up'

                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting Portfolio details')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            portletApi.getMyPortlets(
                {
                  before: ->
                    $log.debug('Fetching MyPortlets data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'MyPortlets fetched successfully.' + JSON.stringify(data)
                    $scope.portlets = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  MyPortlets.')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortfolioDetails()')
                }
            )
            
    ]

)