'use strict'
angular.module('DiscoverCtrl',['Api'])
.controller(
    'DiscoverCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('DiscoverCtrl controller called')
            nlform = new NLForm(document.getElementById('nl-form'))

            formatDate = (date) ->
                d = new Date(date)
                month = '' + d.getMonth() + 1
                day = '' + d.getDate()
                year = d.getFullYear()
                if month.length < 2
                  month = '0' + month
                if day.length < 2
                  day = '0' + day
                [
                  year
                  month
                  day
                ].join '/'
            portletApi.getCategories(
                {
                  before: ->
                    $log.debug('Fetching categories page details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Categories fetched successfully.' + JSON.stringify(data)
                    $scope.categories = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while feching Categories')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('Categories fetched')
                }
            )

            portletApi.getPortlets(
                {
                  before: ->
                    $log.debug('Fetching Portlet lists.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Portlet list fetched successfully.' + JSON.stringify(data)
                    $scope.portlets = data

                    $scope.portlets.filter((portlet) ->
                                              portlet.lastRebalancedOn = formatDate(portlet.lastRebalancedOn)
                            )

                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while fetcching portlet list')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete of getPortlets')
                }
            )
            $scope.viewMore = (portletId) ->
              console.log 'portlet Id: ' + portletId
              $location.path("/page-portlet/" + portletId) 






            
    ]

)