do ->
  app = angular.module('InitCtrl', [])
  app.controller 'InitCtrl', [
    '$http'
    '$scope'
    ($http, $scope) ->
      console.log 'hello u r in InitCtrl'
      return
  ]
  return