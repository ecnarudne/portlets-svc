'use strict'
angular.module('PagePortletCtrl',['Api'])
.controller(
    'PagePortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('PagePortletCtrl controller called')
            plotGraph()
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

            portletApi.getPortletPageDetails(
                {
                  before: ->
                    $log.debug('Fetching page portlet data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portlet = data
                    $scope.createdOn = formatDate(new Date(data.createdOn))
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  portlet data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            portletApi.getStatTable(
                {
                  before: ->
                    $log.debug('Fetching data for StatTable.')
                  success: (data, status, headers, config) ->
                    ###$log.debug 'Data fetched successfully.' + JSON.stringify(data)###
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            createTable()
            
    ]

)