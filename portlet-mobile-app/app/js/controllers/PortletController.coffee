'use strict'
angular.module('PortletCtrl',['ionic'])
.controller(
    'PortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$location"
        ($scope,$log,$http,$location)->
            $log.debug('PortletCtrl controller called')
            plotPortletGraph()
            
    ]

)