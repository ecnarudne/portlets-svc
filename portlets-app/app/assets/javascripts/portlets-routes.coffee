do ->
  app = angular.module('portlets-router', [ 'ngRoute' ])
  app.config [
    '$routeProvider'
    ($routeProvider) ->
      $routeProvider.when('/',
      
        templateUrl: '/assets/angular/ui/welcome.html'
        controller: 'InitCtrl').when('/login',
        
        templateUrl: '/assets/angular/ui/login.html'
        
        controller: 'AuthenticatedCtrl').when('/sign-up',
        
        templateUrl: '/assets/angular/ui/sign-up.html').when('/page-settings',
        
		    templateUrl: '/assets/angular/ui/page-settings.html').when('/forgot-password',

        templateUrl: '/assets/angular/ui/forgot-password.html').when('/portlet-create',
        
        templateUrl: '/assets/angular/ui/portlet-create.html'
        
        controller: 'PortletCtrl').when('/page-explore',
        
        templateUrl: '/assets/angular/ui/page-explore.html'
        
        controller: 'DiscoverCtrl').when('/portfolio',
        
        templateUrl: '/assets/angular/ui/portfolio.html'

        controller: 'PortfolioCtrl').when('/page-portlet/:portletId',
        
        templateUrl: '/assets/angular/ui/page-portlet.html'

        controller: 'PagePortletCtrl').when('/page-stock',
        
        templateUrl: '/assets/angular/ui/page-stock.html'

        controller: 'PageStockCtrl').when('/404',
        
        templateUrl: '/assets/angular/ui/error-400.html').when('/500',
        
        templateUrl: '/assets/angular/ui/error-500.html').otherwise redirectTo: '/404'
      return
  ]
  return