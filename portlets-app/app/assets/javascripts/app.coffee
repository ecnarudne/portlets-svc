do ->
  app = angular.module('portlets', ['portlets-router', 'portlets-controller', 'UserFactory', 'Api'])
  app.controller 'IndexCtrl', [
    '$http'
    '$scope'
    'User'    
    ($http, $scope, User) ->
      User.setUserId false
      
      console.log 'User.getUserId ' + User.getUserId       
      $scope.user =  true
      console.log 'hello u r in IndexCtrl'
      
      return
  ]
  