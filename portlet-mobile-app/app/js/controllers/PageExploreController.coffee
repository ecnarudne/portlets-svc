'use strict'
angular.module('PageExploreCtrl',['ionic','Api'])
.controller(
    'PageExploreCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('DiscoverCtrl controller called')
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
                    $scope.sectors = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while feching Categories')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('Categories fetched')
                }
            )
            
            $scope.getPortlets = () ->

                portletApi.getPortlets(
                    {
                      before: ->
                        $log.debug('Fetching Portlet list.')
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

            $scope.getPortlets()
            $scope.getNewPortlets = () ->
              portletApi.getNewPortlets(
                    {
                      before: ->
                        $log.debug('Fetching new portlets.')
                      success: (data, status, headers, config) ->
                        $log.debug 'New portlets fetched successfully.' + JSON.stringify(data)
                        $scope.portlets = data
                      error: (data, status, headers, config, statusText) ->
                        $log.error('Got error while feching new portlets')                   
                      complete: (data, status, headers, config) ->
                        $log.debug('New portlets are fetched')
                    }
                )
            $scope.getTopPortlets = () ->
                portletApi.getTopPortlets(
                      {
                        before: ->
                          $log.debug('Fetching top portlets.')
                        success: (data, status, headers, config) ->
                          $log.debug 'Top portlets fetched successfully.' + JSON.stringify(data)
                          $scope.portlets = data
                        error: (data, status, headers, config, statusText) ->
                          $log.error('Got error while feching top portlets')                   
                        complete: (data, status, headers, config) ->
                          $log.debug('Top portlets are fetched')
                      }
                  )
            $scope.viewMore = (portletId) ->
              	console.log 'portlet Id: ' + portletId
              	$location.path("/app/portlet/" + portletId )
    ]

)