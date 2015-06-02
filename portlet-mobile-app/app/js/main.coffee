'use strict'

angular.module('finlytics', ['ngRoute', 'ionic', 'finlytics-controller'])

.controller(
  'InitController'
  [
    "$scope"
    "$rootScope"
    ($scope,$rootScope) ->
      console.log("u r in InitController")
  ])
.run(($ionicPlatform) ->

  $ionicPlatform.ready ->
    if window.cordova and window.cordova.plugins and window.cordova.plugins.Keyboard
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar true
    if window.StatusBar
      StatusBar.styleLightContent()
      StatusBar.styleDefault()
    return
  return)
.config ($stateProvider, $urlRouterProvider) ->
  $stateProvider
    .state('app', 
      url: "/app"
      abstract: true
      templateUrl: "views/side-menu.html"
    )
    .state('sign-up',
      url: '/'
      templateUrl: 'views/sign-up.html'
      cache: false
      reload: true
    )
    .state('login',
      url: '/login'
      templateUrl: 'views/login.html'
    )
    .state('pwd-recovery',
      url: '/pwd-recovery'
      templateUrl: 'views/pwd-recovery.html'
      cache: false
      reload: true
    )
    .state('app.portlet',
      url: '/portlet-create'
      views: 'menuContent':
        templateUrl: 'views/portlet-create.html'
      cache: false
      reload: true
    )
    .state('app.search',
      url: '/search'
      views: 'menuContent':
        templateUrl: 'views/search.html'
      cache: false
      reload: true
    )
    
  $urlRouterProvider.otherwise '/'
  return
