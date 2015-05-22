do ->
  app = angular.module('portlets-router', [ 'ngRoute' ])
  app.config [
    '$routeProvider'
    ($routeProvider) ->
      $routeProvider.when('/',
      
        templateUrl: '/assets/angular/ui/welcome.html'
        controller: 'InitCtrl').when('/login',
        
        templateUrl: '/assets/angular/ui/login.html'
        
        controller: 'LoginCtrl').when('/sign-up',
        
        templateUrl: '/assets/angular/ui/sign-up.html').when('/page-settings',
        
		    templateUrl: '/assets/angular/ui/page-settings.html').when('/forgot-password',

        templateUrl: '/assets/angular/ui/forgot-password.html').when('/portlet-create',
        
        templateUrl: '/assets/angular/ui/portlet-create.html').when('/page-explore',
        
        templateUrl: '/assets/angular/ui/page-explore.html').when('/portfolio',
        
        templateUrl: '/assets/angular/ui/portfolio.html').when('/page-portlet',
        
        templateUrl: '/assets/angular/ui/page-portlet.html').when('/404',
        
        templateUrl: '/assets/angular/ui/error-400.html').otherwise redirectTo: '/404'
      return
  ]
  return