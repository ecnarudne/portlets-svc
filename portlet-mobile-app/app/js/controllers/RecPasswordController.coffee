'use strict'

appController = angular.module('RecPasswordController', ['ngRoute', 'ionic'])

appController.controller(
  'RecPasswordController'
  [
    "$scope"
    "$rootScope"
    ($scope,$rootScope) ->
      console.log("u r in RecPasswordController")
      $scope.data = {}
      $scope.recPass = ->
        console.log 'LOGIN user: ' + $scope.data.email
        return
  ])
