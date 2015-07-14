do ->
  app = angular.module('portlets', ['portlets-router', 'portlets-controller', 'UserFactory', 'Api'])
  
  app.controller 'IndexCtrl', [
    '$http'
    '$scope'
    '$cookies'
    '$window'
    '$location'
    'portletApi'
    ($http, $scope, $cookies,$window,$location,portletApi ) ->
      $scope.isSpecificPage = () ->
        console.log "in isSpecificPage function" 
        console.log "picURL: " + $cookies.pictureURL
        $scope.picURL = $cookies.pictureURL
        path = $location.path()
        if path == '/login' or path == '/sign-up' or path == '/'
          return true
        else 
          return false

      $scope.logout = () ->
        portletApi.logout(
                {
                  before: ->
                    console.log('Logging out')
                  success: (data, status, headers, config) ->
                    console.log 'MyPortlets fetched successfully.' + JSON.stringify(data)
                    $location.path("/")
                  error: (data, status, headers, config, statusText) ->
                    console.log('Got error while getting  graph data')                   
                  complete: (data, status, headers, config) ->
                    console.log('In complete of getPortfolioDetails()')
                }
            )
      ###return _.contains( ['/404', '/pages/500', '/login', '/sign-up', '/pages/signin1', '/pages/signin2', '/pages/signup', '/pages/signup1', '/pages/signup2', '/pages/lock-screen'], path )###
      
      console.log 'You are in Index Ctrl' 
      return

  ]
  