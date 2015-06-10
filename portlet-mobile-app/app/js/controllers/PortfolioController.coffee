'use strict'
angular.module('PortfolioCtrl',['ionic'])
.controller(
    'PortfolioCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$location"
        ($scope,$log,$http,$location)->
            $log.debug('PortfolioCtrl controller called')

            $scope.portfolio = [ {
								      'portlet':
								        'dailyReturn': 0.56
								        'yearlyReturn': 2.53
								        'portfolioValue': 2.49
								      
								      'tableItems': [
								        {
								          'portletReturn': 0.5
								          'totalReturn': -9.51
								          'dailyReturn': -9.51
								          'name': 'High-Yield Dividends'
								          'oneYearReturn': 5.8
								        }
								        {
								          'portletReturn': 0.5
								          'totalReturn': -9.51
								          'dailyReturn': -9.51
								          'name': 'High-Yield Dividends'
								          'oneYearReturn': 5.8
								        }
								        {
								          'portletReturn': 0.5
								          'totalReturn': -9.51
								          'dailyReturn': -9.51
								          'name': 'High-Yield Dividends'
								          'oneYearReturn': 5.8
								        }
								      ]
								    } ]


    ]

)