'use strict'
angular.module('DiscoverCtrl',['Api'])
.controller(
    'DiscoverCtrl'
    [
        "$scope"
        "$log"
        "portletApi"
        "$location"
        ($scope,$log,portletApi,$location)->
            $log.debug('DiscoverCtrl controller called')
            $scope.loadNLForm = ->
              nlform = new NLForm(document.getElementById('nl-form'))
            portletApi.getSectors(
                {
                  before: ->
                    $log.debug('Fetching Setcors list.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Sectors fetched successfully.' + JSON.stringify(data)
                    $scope.sectors = data
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while feching Sector list')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('Categories fetched')
                }
            )
            
            $scope.getAllPortlets = () ->
              portletApi.getAllPortlets(
                  {
                    before: ->
                      $log.debug('Fetching Portlet lists.')
                    success: (data, status, headers, config) ->
                      $log.debug 'Portlet list fetched successfully.' + JSON.stringify(data)
                      $scope.portlets = data
                    error: (data, status, headers, config, statusText) ->
                      $log.error('Got error while fetcching portlet list')                   
                    complete: (data, status, headers, config) ->
                      $log.debug('In complete of getPortlets')
                  }
              )
            $scope.getAllPortlets()  
            $scope.getPortlets =() ->
              if $scope.sectorId == ""
                $scope.getAllPortlets()
              else
                portletApi.getPortlets(
                    $scope.sectorId
                    {
                      before: ->
                        $log.debug('Fetching Portlet lists.')
                      success: (data, status, headers, config) ->
                        $log.debug 'Portlet list fetched successfully.' + JSON.stringify(data)
                        $scope.portlets = data
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