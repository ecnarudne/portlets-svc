'use strict'

appController = angular.module('SideMenuCtrl', ['ionic'])

appController.controller(
  'SideMenuCtrl'
  [
    "$scope"
    "$rootScope"
    ($scope,$rootScope) ->
      console.log("u r in SideMenuCtrl")
      $scope.data = {}
      $scope.navTitle = 'Entry Page';
      $scope.toggleMenu = ->
        $scope.sideMenuController.toggleLeft()
        console.log "Hello toggle menu called"
      
      $scope.recPass = ->
        console.log 'LOGIN user: ' + $scope.data.email
        return
  ])