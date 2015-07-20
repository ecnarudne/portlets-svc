'use strict'

appController = angular.module('LoginController', ['ngRoute', 'ionic','Api','ngCordova', 'ngCordovaOauth'])

appController.controller(
  'LoginController'
  [
    "$scope"
    "$cordovaOauth" 
    "$location"
    ($scope, $cordovaOauth, $localStorage, $location,$log ) ->
      console.log("u r in LoginController")
      $scope.data = {}
      $scope.login = ->
        $cordovaOauth.google('945704286859-9fpaqjv25ce7ofuscp7b62hs5qd0j6b6.apps.googleusercontent.com', [
          'https://www.googleapis.com/auth/userinfo.email'
          'https://www.googleapis.com/auth/plus.me'
          'https://www.googleapis.com/auth/userinfo.profile'
        ]).then ((result) ->
          $localStorage.accessToken = result.access_token
          $location.path '/portfolio'
          return
        ), (error) ->
          alert 'There was a problem signing in!  See the console for logs'
          console.log error
          return
        return

      ###portletApi.authGoogle(
                {
                  before: ->
                    $log.debug('authentication.')
                  success: (data, status, headers, config) ->
                    console.log 'oauth data: ' + data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while authentication.')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of authentication')
                }
            )###
  ])
