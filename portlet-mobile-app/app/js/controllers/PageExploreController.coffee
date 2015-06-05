'use strict'
angular.module('PageExploreCtrl',['ionic'])
.controller(
    'PageExploreCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$location"
        ($scope,$log,$http,$location)->
            $scope.sectors = 	[
							      {
							        'name': 'Basic Material'
							        'total': 10
							      }
							      {
							        'name': 'Energy'
							        'total': 32
							      }
							      {
							        'name': 'Financial'
							        'total': 52
							      }
							      {
							        'name': 'HealthCare'
							        'total': 26
							      }
							      {
							        'name': 'Industrials'
							        'total': 15
							      }
							      {
							        'name': 'Real Estate'
							        'total': 85
							      }
							      {
							        'name': 'Technology'
							        'total': 150
							      }
							      {
							        'name': 'Telecommunication'
							        'total': 25
							      }
							      {
							        'name': 'Utilities'
							        'total': 7
							      }
							      {
							        'name': 'Consumer'
							        'total': 63
							      }
							 	]
            $log.debug('PageExploreCtrl controller called')
            $scope.portlets =   [
							      {
							        'volatility': 'high'
							        'returns': 5.6
							        'followers': 659
							        'lastUpdated': '17.03.1992'
							        'creator': 'Machindra'
							        'imageURL': '/assets/images/avatars/avatar-2-lg.jpg'
							        'portletName': 'High-Yield Dividends'
							        'desc': 'With high yields and a 10-year track record of not cutting dividends, these stocks separate themselves from the pack.'
							      }
							      {
							        'volatility': 'medium'
							        'returns': 9.6
							        'followers': 252
							        'lastUpdated': '17.03.92'
							        'creator': 'Ramdas'
							        'imageURL': '/assets/images/avatars/avatar-2-lg.jpg'
							        'portletName': 'Bear International Market'
							        'desc': 'Investors expecting a selloff in markets outside the US may find this motif worth a closer look.'
							      }
							      {
							        'volatility': 'moderate'
							        'returns': 9.6
							        'followers': 252
							        'lastUpdated': '17.03.92'
							        'creator': 'Ramdas'
							        'imageURL': '/assets/images/avatars/avatar-2-lg.jpg'
							        'portletName': 'Bear International Market'
							        'desc': 'Investors expecting a selloff in markets outside the US may find this motif worth a closer look.'
							      }
							      {
							        'volatility': 'low'
							        'returns': 9.6
							        'followers': 252
							        'lastUpdated': '17.03.92'
							        'creator': 'Ramdas'
							        'imageURL': '/assets/images/avatars/avatar-2-lg.jpg'
							        'portletName': 'Bear International Market'
							        'desc': 'Investors expecting a selloff in markets outside the US may find this motif worth a closer look.'
							      }
							      {
							        'volatility': 'high'
							        'returns': 9.6
							        'followers': 252
							        'lastUpdated': '17.03.92'
							        'creator': 'Ramdas'
							        'imageURL': '/assets/images/avatars/avatar-2-lg.jpg'
							        'portletName': 'Bear International Market'
							        'desc': 'Investors expecting a selloff in markets outside the US may find this motif worth a closer look.'
							      }
							      {
							        'volatility': 'very-high'
							        'returns': 9.6
							        'followers': 252
							        'lastUpdated': '17.03.92'
							        'creator': 'Ramdas'
							        'imageURL': '/assets/images/avatars/avatar-2-lg.jpg'
							        'portletName': 'Bear International Market'
							        'desc': 'Investors expecting a selloff in markets outside the US may find this motif worth a closer look.'
							      }
								]
    ]

)