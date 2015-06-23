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
            #retrive cookies
            cookieVal = $cookies.cookieVal
            $scope.errorMessage = false
            if(cookieVal == undefined)
                $log.debug('User is not logged-in redirecting to login.')
                $location.path("/login")
            else
                #$log.debug('User is already authenticated')
                #$location.path("/create")

            $scope.portlet = {}
            $scope.selectedStocks =  []
            $scope.stocks1 = []
            $scope.size = 0
            $scope.isShown = false
            original = angular.copy($scope.portlet)
            # Used to set ot reset form field after submitting
            portletApi.getCategories(
                before: ->
                    $log.debug('Fetching Categories')
                success: (data, status, headers, config) ->
                    $scope.categories = data;
                    console.debug 'categories fetched :' + JSON.stringify data
                error: (data, status, headers, config) ->
                    $log.error('Something went wrong! ' + data)
                    $location.path("/portlet-create")
                    #$scope.errorMessage = true
                forbidden: (data, status, headers, config) ->
                    $log.error('Got error while Authentication Response: ' + data)
                    $scope.errorMessage = true
                    $location.path("/login")
                
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
                                # Setting coockies
                                console.log("Hi data submittes successfully")
                                $cookies.cookieVal = data.value                        
                                $location.path("/page-portlet")
                            error: (data, status, headers, config) ->
                                $log.error('Something went wrong! ' + data)
                                $location.path("/portlet-create")
                                #$scope.errorMessage = true
                            forbidden: (data, status, headers, config) ->
                                $log.error('Got error while Authentication Response: ' + data)
                                $scope.errorMessage = true
                                $location.path("/login")
                            
                        )

            $scope.getStocks = () ->
                console.log "get stocks function is called" + $scope.portlet.stockExchange
                
                if $scope.portlet.stockExchange == undefined
                        alert "please select Exchange"
                else
                    portletApi.getStocks(
                        data: 1     # Hard coded Id of Stock Exchange
                        before: ->
                            $log.debug('submitting stock exchange data.')
                        success: (data, status, headers, config) ->
                            console.log "stock fetched succesfully."
                            $scope.stocks = data
                            
                        error: (data, status, headers, config) ->
                            $log.error('Something went wrong! ' + data)
                        forbidden: (data, status, headers, config) ->
                            $log.error('Got error while Authentication Response: ' + data)
                    )
            
            $scope.addStock = (stock)->
                if $scope.stocks1.indexOf(stock) <= -1
                    $scope.stocks1.push stock
                    $scope.stockWithWeight = {}
                    $scope.stockWithWeight.name = stock
                    $scope.selectedStocks.push $scope.stockWithWeight
                    $scope.isDisabled = true
                    $scope.showSelected = true
                else
                    alert 'This Stock is already added'

                $scope.size = $scope.selectedStocks.filter((value) ->
                        value.name != ''
                        ).length
                console.log $scope.size
                console.log $scope.stocks1


            $scope.deleteStock = (stock)->

                 $scope.selectedStocks = $.grep($scope.selectedStocks, (x) ->
                                                            x.name != stock
                                )
                 console.log "JSon array is :" + JSON.stringify $scope.selectedStocks
                 $scope.stocks1.splice($scope.selectedStocks.indexOf(stock),1)
                 $scope.size = $scope.selectedStocks.filter((value) ->
                        value.name != ''
                        ).length

                 console.log 'size after delete' + $scope.size
                 if $scope.size == 0
                    console.log 'in size if '
                    $scope.isDisabled = false
            
            $scope.setWeightage = (stock,percentage) ->
                $scope.selectedStocks.forEach (s) -> s.weightage = percentage if s.name == stock
                console.log "JSon array with weight is :" + JSON.stringify $scope.selectedStocks
                $scope.totalWeight = 0 
                $scope.selectedStocks.forEach (s) -> 
                    weight = 0
                    if s.weightage == undefined
                        weight = 0
                    else
                        weight = parseInt(s.weightage)
                    $scope.totalWeight = $scope.totalWeight + weight
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