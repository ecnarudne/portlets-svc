'use strict'
angular.module('AuthenticatedCtrl',['Api'])
.controller(
    'AuthenticatedCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('Authentication controller called')
            $scope.userLogin = () ->
                portletApi.login(
                    data: $scope.user
                    before: ->
                        $log.debug('submitting user authentication Data')
                    success: (data, status, headers, config) ->

                    error: (data, status, headers, config) ->
                        $log.error('Something went wrong! ' + data)
                        $location.path("/login")
                )

    ]

)