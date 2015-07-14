'use strict'

appController = angular.module('LoginController', ['ngRoute', 'ionic','Api'])

appController.controller(
  'LoginController'
  [
    "$scope"
    "$rootScope"
    "portletApi"
    "$log"
    ($scope,$rootScope,portletApi,$log ) ->
      console.log("u r in LoginController")
      $scope.data = {}
      $scope.login = ->
        console.log 'LOGIN user: ' + $scope.data.email + ' - PW: ' + $scope.data.password
        return
      portletApi.authGoogle(
                {
                  before: ->
                    $log.debug('authentication.')
                  success: (data, status, headers, config) ->
                    console.log 'oauth data: ' + data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while authentication.')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of authentication')
                }
            )
  ])
