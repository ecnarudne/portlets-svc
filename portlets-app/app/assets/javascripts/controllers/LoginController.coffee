do ->
  app = angular.module('LoginCtrl', [])
  app.controller 'LoginCtrl', [
    '$http'
    '$scope'
    ($http, $scope) ->
      console.log 'hello u r in LoginCtrl'
      return
  ]
  return