'use strict'
angular.module('PortletCtrl',['Api'])
.controller(
    'PortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('PortletCtrl controller called')
            #retrive cookies
            cookieVal = $cookies.cookieVal
            $scope.errorMessage = false
            if(cookieVal == undefined)
                $log.debug('User is not logged-in redirecting to login.')
                $location.path("/login")
            else
                #$log.debug('User is already authenticated')
                #$location.path("/create")

            $scope.portlet = {}
            original = angular.copy($scope.portlet)
            # Used to set ot reset form field after submitting
            $scope.revert = ->
                $scope.portlet = angular.copy(original)
                $scope.portlet_form.$setPristine()

            $scope.canRevert = ->
                return !angular.equals($scope.portlet, original) || !$scope.portlet_form.$pristine

            $scope.canSubmit = ->
                return $scope.portlet_form.$valid && !angular.equals($scope.portlet, original)

            $scope.addPortlet = () ->
                portletApi.addPortlet(
                    data: $scope.portlet
                    before: ->
                        $log.debug('submitting user authentication Data')
                    success: (data, status, headers, config) ->
                        # Setting coockies
                        console.log("Hi data submittes successfully")
                        $cookies.cookieVal = data.value                        
                        $location.path("/portfolio")
                    error: (data, status, headers, config) ->
                        $log.error('Something went wrong! ' + data)
                        $location.path("/portlet-create")
                        #$scope.errorMessage = true
                    forbidden: (data, status, headers, config) ->
                        $log.error('Got error while Authentication Response: ' + data)
                        $scope.errorMessage = true
                        $location.path("/login")
                )

    ]

)