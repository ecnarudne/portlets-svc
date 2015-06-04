'use strict'

appController = angular.module('PortletCreateCtrl', ['ionic'])

appController.controller(
  'PortletCreateCtrl'
  [
    "$scope"
    ($scope) ->
      console.log("u r in PortletCreateCtrl")
      $scope.data = {}
      $scope.stocks = [
                        {
                          'name': 'Apple'
                          'ticker': 'APPL'
                        }
                        {
                          'name': 'Infosys'
                          'ticker': 'INFY'
                        }
                        {
                          'name': 'google'
                          'ticker': 'GOOGL'
                        }
                      ]
      $scope.signUp = ->
        console.log 'LOGIN user: ' + $scope.data.username + ' - PW: ' + $scope.data.password
        return
  ])