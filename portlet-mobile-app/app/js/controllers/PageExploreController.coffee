'use strict'
angular.module('PageExploreCtrl',['ionic'])
.controller(
    'PageExploreCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$location"
        ($scope,$log,$http,$location)->
            $log.debug('PageExploreCtrl controller called')
    ]

)