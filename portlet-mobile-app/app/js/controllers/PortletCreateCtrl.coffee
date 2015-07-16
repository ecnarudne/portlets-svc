'use strict'

appController = angular.module('PortletCreateCtrl', ['ionic', 'Api'])

appController.controller(
    'PortletCreateCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        "$document"
        "$rootScope"
        ($scope,$log,$http,$cookies,portletApi,$location,$document,$rootScope)->
            $scope.spinner = false
            $scope.setPortletData = () ->
                portlet = {}
                portlet.name = $scope.name
                portlet.notes = $scope.notes
                portlet.sectorId = $scope.sectorId
                $rootScope.portlet = portlet
                $rootScope.portlet.selectedStocks = []
                $rootScope.availableWeightage = 100 

            portletApi.getCategories(
                before: ->
                    $log.debug('Fetching Categories')
                success: (data, status, headers, config) ->
                    $scope.sectors = data;
                    console.debug 'categories fetched :' + JSON.stringify data
                error: (data, status, headers, config) ->
                    $log.error('Something went wrong! ' + data)
                    
                forbidden: (data, status, headers, config) ->
                    $log.error('Got error while Authentication Response: ' + data)
                    
            )
            portletApi.getStockExchange(
                before: ->
                    $log.debug('Getting Stock Exchange data')
                success: (data, status, headers, config) ->
                    $scope.stockExchanges = data
                    console.log "stock data fetched is : " + JSON.stringify(data)    
                error: (data, status, headers, config) ->
                    $log.error('Something went wrong! ' + data)
                forbidden: (data, status, headers, config) ->
                    $log.error('Got error while fetching')
            )

            $scope.getStocks = () ->
                $rootScope.portlet.stockExchange = $scope.stockExchangeName
                console.log 'getting Stocks of : ' + $scope.stockExchangeName
                if $scope.stockExchangeName == ''
                        alert "please select Exchange"
                else
                    portletApi.getStocks(
                        $scope.stockExchangeName
                        {
                            before: ->
                                $scope.spinner = true
                                $log.debug('submitting stock exchange data.')
                            success: (data, status, headers, config) ->
                                console.log "stock fetched succesfully."
                                $rootScope.stocks = data
                                $log.debug('Stock fetched: ' + JSON.stringify data)
                            error: (data, status, headers, config) ->
                                $log.error('Something went wrong! ' + data)
                            complete: (data, status, headers, config) ->
                                $log.error('Got error while Authentication Response: ' + data)
                                $scope.spinner = false
                        }
            )
            console.log 'selected Stock are: ' + JSON.stringify $scope.selectedStocks
            $scope.addStock = (stock)->
                count =$rootScope.portlet.selectedStocks.filter((value) ->value.name == stock).length
                if count == 0
                    $rootScope.portlet.selectedStocks.push stock
                    $rootScope.isDisabled = true
                    $scope.showSelected = true
                    console.log 'Added Stocks: ' + JSON.stringify $rootScope.portlet.selectedStocks
                else
                    alert 'This Stock is already added'
                $scope.size = $rootScope.portlet.selectedStocks.filter((value) -> value.name != '').length

            $scope.setWeightage = (stock,percentage) ->
                $rootScope.portlet.selectedStocks.forEach (s) -> s.weightage = percentage if s.name == stock
                console.log "JSon array with weight is :" + JSON.stringify $scope.portlet.selectedStocks
                $scope.totalWeight = 0 
                $rootScope.portlet.selectedStocks.forEach (s) -> 
                    weight = 0
                    if s.weightage == undefined
                        weight = 0
                    else
                        weight = parseInt(s.weightage)
                    $scope.totalWeight = $scope.totalWeight + weight
                    $rootScope.availableWeightage = 100 - $scope.totalWeight
                    if $scope.totalWeight > 100
                        alert 'Total weightage must 100%'
                console.log  'total weightage'+ $scope.totalWeight 

            $scope.deleteStock = (stock)->
                $rootScope.portlet.selectedStocks = $.grep($rootScope.portlet.selectedStocks, (x) -> x.name != stock)
                console.log "Json array is :" + JSON.stringify $rootScope.portlet.selectedStocks
                $scope.size = $rootScope.portlet.selectedStocks.filter((value) -> value.name != '').length
                if $scope.size == 0
                    $rootScope.isDisabled = false
                    $scope.availableWeightage = 100
                $scope.totalWeight = 0
                $rootScope.portlet.selectedStocks.forEach (s) -> 
                    console.log "in delete stock for each loop"
                    weight = 0
                    if s.weightage == undefined or s.weightage == ''
                        weight = 0
                    else
                        weight = parseInt(s.weightage)
                        $scope.totalWeight = $scope.totalWeight + weight
                    $rootScope.availableWeightage = 100 - $scope.totalWeight

            $scope.addPortlet = () ->
                if $scope.size < 3 
                    alert 'Portlet must have at least 9 stocks'
                else
                    if $scope.totalWeight != 100
                        alert 'weight must be 100 %'
                    else
                        portletApi.addPortlet(
                            data: $rootScope.portlet
                            before: ->
                                $log.debug('submitting Portlet Data: ' + JSON.stringify $rootScope.portlet )
                            success: (data, status, headers, config) ->
                                console.log("Data submittes successfully")
                            error: (data, status, headers, config) ->
                                $log.error('Something went wrong! ' + data)
                                
                            forbidden: (data, status, headers, config) ->
                                $log.error('Got error while Authentication Response: ' + data)
                                $scope.errorMessage = true
                        )

])