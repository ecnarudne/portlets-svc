'use strict'
angular.module('DiscoverCtrl',['Api'])
.controller(
    'DiscoverCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('DiscoverCtrl controller called')

            portletApi.getDiscoverPageDetails(
                {
                  before: ->
                    $log.debug('Fetching Discover page details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Discover page details fetched successfully.' + JSON.stringify(data)
                    $scope.data = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while discover page details')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getDiscoverPageDetails()')
                }
            )
            
    ]

)