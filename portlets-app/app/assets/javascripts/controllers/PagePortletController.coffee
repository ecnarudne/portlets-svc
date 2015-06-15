'use strict'
angular.module('PagePortletCtrl',['Api'])
.controller(
    'PagePortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('PagePortletCtrl controller called')
            plotGraph()
            portletApi.getPortletPageDetails(
                {
                  before: ->
                    $log.debug('fetching page portlet data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.data = data[0]
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            
    ]

)