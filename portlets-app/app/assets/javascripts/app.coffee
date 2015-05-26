do ->
  app = angular.module('portlets', ['portlets-router', 'portlets-controller', 'UserFactory', 'Api'])
  
  app.controller 'IndexCtrl', [
    '$http'
    '$scope'
    '$cookies'    
    ($http, $scope, $cookies) ->
      
      #value is hardcoaded so dont consider cookie is in use      
      if($cookies.cookieVal == undefined)
        console.log "Hello cookie is undefined"
        $scope.user = false
      else
        $scope.user = true

      console.log 'hello u r in IndexCtrl'
      
      return
  ]
  