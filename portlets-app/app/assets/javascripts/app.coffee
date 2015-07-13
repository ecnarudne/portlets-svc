do ->
  app = angular.module('portlets', ['portlets-router', 'portlets-controller', 'UserFactory', 'Api'])
  
  app.controller 'IndexCtrl', [
    '$http'
    '$scope'
    '$cookies'
    '$window'
    '$location'   
    ($http, $scope, $cookies,$window,$location ) ->
      $scope.isSpecificPage = () ->
        console.log "in isSpecificPage function" 
        path = $location.path()
        if path == '/login' or path == '/sign-up' 
          return true
        else 
          return false

      $scope.logout = () ->
        $location.path ('/login')
        $scope.isSpecificPage()
      ###return _.contains( ['/404', '/pages/500', '/login', '/sign-up', '/pages/signin1', '/pages/signin2', '/pages/signup', '/pages/signup1', '/pages/signup2', '/pages/lock-screen'], path )###
      
      console.log 'You are in Index Ctrl' 
      return
  ]
  