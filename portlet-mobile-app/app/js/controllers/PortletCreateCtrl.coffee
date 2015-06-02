'use strict'

appController = angular.module('PortletCreateCtrl', ['ionic'])

appController.controller(
  'PortletCreateCtrl'
  [
    "$scope"
    ($scope) ->
      console.log("u r in PortletCreateCtrl")
      $scope.data = {}
      $scope.signUp = ->
        console.log 'LOGIN user: ' + $scope.data.username + ' - PW: ' + $scope.data.password
        return
  ])