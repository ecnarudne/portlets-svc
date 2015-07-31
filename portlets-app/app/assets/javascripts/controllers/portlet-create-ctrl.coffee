'use strict'
angular.module('PortletCtrl',['Api'])
.controller(
    'PortletCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        "$document"
        "$window"
        ($scope,$log,$http,$cookies,portletApi,$location,$document,$window)->
            $log.debug('PortletCtrl controller called')
            $scope.portlet = {}
            $scope.selectedStocks =  []
            $scope.availableWeightage = 100
            $scope.size = 0
            $scope.isShown = false
            $scope.calcWeightage = () ->
                $scope.totalWeight = 0
                $scope.selectedStocks.forEach (s) -> 
                    console.log "in delete stock for each loop"
                    weight = 0
                    if s.weightage == undefined
                        weight = 0
                    else
                        weight = parseFloat(s.weightage)
                        $scope.totalWeight = $scope.totalWeight + weight
                    $scope.availableWeightage = 100 - $scope.totalWeight
            
            portletApi.getSectors(
                before: ->
                    $log.debug('Fetching sectors')
                success: (data, status, headers, config) ->
                    $scope.sectors = data;
                    console.debug 'sectors fetched :' + JSON.stringify data
                error: (data, status, headers, config) ->
                    $log.error('Something went wrong! ' + data)
                    $location.path("/portlet-create")
                
            )
            portletApi.getStockExchange(
                before: ->
                    $log.debug('Getting Stock Exchange data')
                success: (data, status, headers, config) ->
                    $scope.stockExchanges = data
                    console.log "stock data fetched is : " + JSON.stringify(data)    
                error: (data, status, headers, config) ->
                    $log.error('Something went wrong! ' + data)
            )

            $scope.revert = ->
                $scope.portlet = angular.copy(original)
                $scope.portlet_form.$setPristine()

            $scope.canRevert = ->
                return !angular.equals($scope.portlet, original) || !$scope.portlet_form.$pristine

            $scope.canSubmit = ->
                return $scope.portlet_form.$valid && !angular.equals($scope.portlet, original)

            $scope.addPortlet = () ->
                $scope.portlet.stocks = $scope.selectedStocks
                if $scope.size < 3 
                    alert 'Portlet must have at least 9 stocks'
                else
                    if $scope.totalWeight != 100
                        alert 'weight must be 100 %'
                    else
                        portletApi.addPortlet(
                            data: $scope.portlet
                            before: ->
                                $log.debug('submitting Portlet Data: ' + JSON.stringify $scope.portlet )
                            success: (data, status, headers, config) ->
                                console.log("Hi data submittes successfully")
                                $location.path "/page-portlet/" + data.id
                            error: (data, status, headers, config) ->
                                $log.error('Something went wrong! ' + data)
                                $location.path("/portlet-create")
                        )

            $scope.getStocks = () ->
                if $scope.portlet.stockExchange.name == undefined
                        alert "please select Exchange"
                else
                    portletApi.getStocks(
                        $scope.portlet.stockExchange.name
                        {
                            before: ->
                                $log.debug('submitting stock exchange data.')
                            success: (data, status, headers, config) ->
                                console.log "stock fetched succesfully."
                                $scope.stocks = data
                                $log.debug('Stock fetched: ' + JSON.stringify data)
                            error: (data, status, headers, config) ->
                                $log.error('Something went wrong! ' + data)
                            forbidden: (data, status, headers, config) ->
                                $log.error('Got error while Authentication Response: ' + data)
                        }
                    )
            
            $scope.addStock = (stock)->
                count =$scope.selectedStocks.filter((value) ->
                        value.stock == stock
                        ).length
                if count == 0
                    $scope.stockWithWeight = {}
                    $scope.stockWithWeight.stock = stock
                    $scope.selectedStocks.push $scope.stockWithWeight
                    $scope.isDisabled = true
                    $scope.showSelected = true
                else
                    alert 'This Stock is already added'
                $scope.size = $scope.selectedStocks.filter((value) ->
                        value.stock != ''
                        ).length
             
            $scope.deleteStock = (stock)->
                $scope.selectedStocks = $.grep($scope.selectedStocks, (x) ->
                                                            x.stock != stock
                                )
                console.log "Json array is :" + JSON.stringify $scope.selectedStocks
                $scope.size = $scope.selectedStocks.filter((value) -> value.stock != '').length
                console.log 'size after delete' + $scope.size
                if $scope.size == 0
                    $scope.isDisabled = false
                    $scope.showSelected = false
                    $scope.availableWeightage = 100
                $scope.calcWeightage()
            
            $scope.setWeightage = (stock,percentage) ->
                $scope.selectedStocks.forEach (s) -> s.weightage = percentage if s.stock == stock
                console.log "JSon array with weight is :" + JSON.stringify $scope.selectedStocks
                $scope.calcWeightage()
                if $scope.totalWeight > 100
                    alert 'Total weightage must 100%'

                console.log  'total weightage'+ $scope.totalWeight 
            
            $scope.showTable = (searchVal)->
                if searchVal.val == '' 
                    $scope.isShown = false
                else
                    $scope.isShown = true
    ]

)