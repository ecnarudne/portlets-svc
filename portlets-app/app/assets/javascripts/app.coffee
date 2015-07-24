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
      $scope.searchPortlets = (search) ->
        console.log search
        if search.length == 2
          $location.path("/search")

        if search.length >= 2
          portletApi.searchPortlets(
            partName = search
            {
              before: ->
              success: (data, status, headers, config) ->
                console.log 'MyPortlets fetched successfully.' + JSON.stringify(data)
                $scope.portlets = data
                
                if data[0] == undefined
                  $scope.show_message =true
                else 
                  $scope.show_message =false
                

              error: (data, status, headers, config, statusText) ->
                console.log('Got error while getting  graph data')                   
              complete: (data, status, headers, config) ->
                console.log('In complete of getPortfolioDetails()')
            }
          )
      $scope.viewMore = (portletId) ->
              console.log 'portlet Id: ' + portletId
              $location.path("/page-portlet/" + portletId) 
      ###return _.contains( ['/404', '/pages/500', '/login', '/sign-up', '/pages/signin1', '/pages/signin2', '/pages/signup', '/pages/signup1', '/pages/signup2', '/pages/lock-screen'], path )###
      
      console.log 'You are in Index Ctrl' 
      return

  ]
  