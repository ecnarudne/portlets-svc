'use strict'
angular.module('PortletCtrl',['ionic','Api'])
.controller(
    'PortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        "$routeParams"
        ($scope,$log,$http,$cookies,portletApi,$location,$routeParams)->
            $log.debug('PagePortletCtrl controller called')
            plotPortletGraph()
            console.log 'route parameters: ' + $routeParams.portletId
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
            console.log 'Route parameters: ' + $routeParams.portletId
            portletApi.getPortletDetails(
                $routeParams.portletId = 5 
                {
                  before: ->
                    $log.debug('Fetching page portlet data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portlet = data
                    $scope.portlet.createdOn = formatDate(new Date(data.createdOn))
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  portlet data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            portletApi.getPortletStatTable(
                $routeParams.portletId = 1
                {
                  before: ->
                    $log.debug('Fetching data for StatTable.')
                  success: (data, status, headers, config) ->
                    $scope.stocks = data
                    $log.debug 'Portlets stocks are fetched successfully' + JSON.stringify(data)
                    ###data.filter((stock)->
                      stockJson ={}
                      stockJson.COMPANY = stock.stats.stock.name
                      stockJson.TICKER = stock.stats.stock.symbol
                      stockJson.ACTIVITY = stock.stats.activity
                      stockJson.WEIGHT = stock.buyWeight
                      stockJson.AVG_COST = stock.buyPrice
                      stockJson.PRICE = stock.stats.closePrice
                      stockJson.TOTAL_RETURN = stock.totalReturn
                      stockJson.DAILY_RETURN = stock.dailyReturn
                      stocks.push stockJson
                      )
                    console.log JSON.stringify stocks
                    createTable(stocks)###

                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )
            
    ]

)