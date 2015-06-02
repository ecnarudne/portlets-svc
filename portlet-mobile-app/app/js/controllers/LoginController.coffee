'use strict'

appController = angular.module('LoginController', ['ngRoute', 'ionic'])

appController.controller(
  'LoginController'
  [
    "$scope"
    "$rootScope"
    ($scope,$rootScope) ->
      console.log("u r in LoginController")
      $scope.data = {}
      $scope.login = ->
        console.log 'LOGIN user: ' + $scope.data.email + ' - PW: ' + $scope.data.password
        return
  ])
