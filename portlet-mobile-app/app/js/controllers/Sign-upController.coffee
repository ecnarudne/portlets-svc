'use strict'

appController = angular.module('Sign-upController', ['ngRoute', 'validation.match', 'ionic'])

appController.controller(
  'Sign-upController'
  [
    "$scope"
    "$rootScope"
    ($scope,$rootScope) ->
      console.log("u r in Sign-upcontroller")
      $scope.data = {}
      $scope.signUp = ->
        console.log 'LOGIN user: ' + $scope.data.username + ' - PW: ' + $scope.data.password
        return
  ])
