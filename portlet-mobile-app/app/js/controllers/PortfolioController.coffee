'use strict'
angular.module('PortfolioCtrl',['ionic'])
.controller(
    'PortfolioCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$location"
        ($scope,$log,$http,$location)->
            $log.debug('PortfolioCtrl controller called')
    ]

)