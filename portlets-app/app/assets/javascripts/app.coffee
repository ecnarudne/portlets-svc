do ->
  app = angular.module('portlets', ['portlets-router', 'portlets-controller', 'UserFactory', 'Api'])
  
  app.controller 'IndexCtrl', [
    '$http'
    '$scope'
    '$cookies'
    '$window'    
    ($http, $scope, $cookies,$window ) ->
      
      #value is hardcoaded so dont consider cookie is in use      
      $scope.user = false
      if($cookies.cookieVal == undefined)
        console.log "Hello cookie is undefined"
        $scope.user = false
      else
        $scope.user = true
      console.log 'hello u r in IndexCtrl' + $scope.user
      return
  ]
  