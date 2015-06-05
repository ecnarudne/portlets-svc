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
    .state('app.portlet-create',
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
    .state('app.discover',
      url: '/page-explore'
      views: 'menuContent':
        templateUrl: 'views/page-explore.html'
      cache: false
      reload: true
    )
    .state('app.portfolio',
      url: '/portfolio'
      views: 'menuContent':
        templateUrl: 'views/portfolio.html'
      cache: false
      reload: true
    )
    .state('app.portlet-create2',
      url: '/portlet-create2'
      views: 'menuContent':
        templateUrl: 'views/portlet-create2.html'
      cache: false
      reload: true
    )
    .state('app.portlet-create3',
      url: '/portlet-create3'
      views: 'menuContent':
        templateUrl: 'views/portlet-create3.html'
      cache: false
      reload: true
    )
    .state('app.portlet',
      url: '/portlet'
      views: 'menuContent':
        templateUrl: 'views/portlet.html'
      cache: false
      reload: true
    )
    
  $urlRouterProvider.otherwise '/'
  return
