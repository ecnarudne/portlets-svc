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
            #retrive cookies
            cookieVal = $cookies.cookieVal
            $scope.errorMessage = false
            if(cookieVal == undefined)
                $log.debug('User is not logged-in redirecting to login.')
            else
                $log.debug('User is already authenticated')
                $location.path("/login")

            $scope.user = {}
            original = angular.copy($scope.user)
            # Used to set ot reset form field after submitting
            $scope.revert = ->
                $scope.user = angular.copy(original)
                $scope.user_login.$setPristine()

            $scope.canRevert = ->
                return !angular.equals($scope.user, original) || !$scope.user_login.$pristine

            $scope.canSubmit = ->
                return $scope.user_login.$valid && !angular.equals($scope.user, original)

            $scope.userLogin = () ->
                portletApi.login(
                    data: $scope.user
                    before: ->
                        $log.debug('submitting user authentication Data')
                    success: (data, status, headers, config) ->
                        # Setting coockies
                        console.log("Hi data submittes successfully")
                        $cookies.cookieVal = data.value                        
                        $location.path("/sign-up")
                    error: (data, status, headers, config) ->
                        $log.error('Something went wrong! ' + data)
                        $location.path("/login")
                        #$scope.errorMessage = true
                    forbidden: (data, status, headers, config) ->
                        $log.error('Got error while Authentication Response: ' + data)
                        $scope.errorMessage = true
                        $location.path("/login")
                )

    ]

)