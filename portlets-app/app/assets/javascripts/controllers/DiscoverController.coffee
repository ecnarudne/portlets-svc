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
            nlform = new NLForm(document.getElementById('nl-form'))

            portletApi.getDiscoverPageDetails(
                {
                  before: ->
                    $log.debug('Fetching Discover page details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Discover page details fetched successfully.' + JSON.stringify(data)
                    $scope.sectors = data[0].sectors
                    $scope.portlets = data[0].portlets
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while discover page details')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getDiscoverPageDetails()')
                }
            )
            
    ]

)