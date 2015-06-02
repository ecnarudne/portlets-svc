'use strict'

appController = angular.module('TabMenuCtrl', ['ionic'])

appController.controller(
  'TabMenuCtrl'
  [
    "$scope"
    ($scope) ->
      console.log("u r in TabMenuCtrl")
      $scope.data = {}
  ])