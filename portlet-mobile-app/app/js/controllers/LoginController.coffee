'use strict'

appController = angular.module('LoginController', ['ngRoute', 'ngStorage' , 'ionic','Api','ngCordova', 'ngCordovaOauth'])

appController.controller(
  'LoginController'
  [
    "$scope"
    "$cordovaOauth" 
    "$location"
    "portletApi"
    "$log"
    "$http"
    "$cookies"
    "$localStorage"
    ($scope, $cordovaOauth, $location, portletApi,$log, $http, $cookies, $localStorage) ->
      console.log("u r in LoginController")
      $scope.login = ->
        $cordovaOauth.google('945704286859-9fpaqjv25ce7ofuscp7b62hs5qd0j6b6.apps.googleusercontent.com', [
          'https://www.googleapis.com/auth/userinfo.email'
          'https://www.googleapis.com/auth/plus.me'
          'https://www.googleapis.com/auth/userinfo.profile'
        ]).then ((result) ->
          $localStorage.accessToken = result.access_token
          token = result.access_token
          console.log "token From google server is : " + token
          portletApi.authGoogle(
            data : { "token" : token }
            {
              before: ->
                $log.debug('authentication.')
              success: (data, status, headers, config) ->
                console.log 'oauth data: ' + data
                $location.path '/portfolio'
              error: (data, status, headers, config, statusText) ->
                $log.error('Got error while authentication.')                   
              complete: (data, status, headers, config) ->
                $log.debug('In complete of authentication')
            }
          )
          return
        ), (error) ->
          alert 'There was a problem signing in!  See the console for logs'
          console.log error
          return
        return
  ])
