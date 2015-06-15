'use strict'
angular.module('PortletCtrl',['ionic'])
.controller(
    'PortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$location"
        ($scope,$log,$http,$location)->
            $log.debug('PortletCtrl controller called')
            plotPortletGraph()
            $scope.portletJson = [ { 'portlet':
                                  'name' : 'China internet'
                                  'volatility' : 'low'
                                  'totalReturn': 9.05
                                  'dailyReturn': 0.56
                                  'oneYearReturn': 2.53
                                  'createdBy': 'J.Liver more'
                                  'createdSince': '1992.03.17'
                                  'followers': 300
                                  'imgURL' : '../img/avatars/avatar-1-xs.jpg'
                                  'desc' : 'I    discovered how to create circular images using CSS3 the other day
                                                and thought it was totally awesome. The only drawback is that the
                                                image has to appear as a background image. You can’t really do this
                                                effect directly to an image that is displayed using an image tag.
                                                What this means is that no one will be able to actually click and
                                                drag the image onto their desktop, but that might be totally okay
                                                with you.' } ]
            $scope.portlet = $scope.portletJson[0].portlet
            $scope.stocksJson = [
                                {
                                  'company': 'Apple'
                                  'weight': '23'
                                  'return' : '45'
                                }
                                {
                                  'company': 'Infosys'
                                  'weight': '45'
                                  'return' : '45'
                                }
                                {
                                  'company': 'google'
                                  'weight': '78'
                                  'return' : '45'
                                }
                                {
                                  'company': 'IBM'
                                  'weight': '85'
                                  'return' : '45'
                                }
                              ]
            $scope.stocks = $scope.stocksJson
            
    ]

)