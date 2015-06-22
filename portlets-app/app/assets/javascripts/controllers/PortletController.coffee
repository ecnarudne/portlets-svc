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
                portletApi.addPortlet(
                    data: $scope.portlet
                    before: ->
                        $log.debug('submitting user authentication Data')
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
            $scope.selectedStocks =  []

            $scope.size = 0
            $scope.isShown = false
            
  
            $scope.addStock = (stock , percentage, add)->
                console.log percentage
                if $scope.selectedStocks.indexOf(stock) <= -1
                    $scope.selectedStocks.push stock
                    $scope.isDisabled = true
                    $scope.showSelected = true

                $scope.size = $scope.selectedStocks.filter((value) ->
                        value != ''
                        ).length
                console.log $scope.size
                console.log $scope.selectedStocks

            $scope.deleteStock = (stock)->
                console.log 'in delete stock ' + stock
                if $scope.selectedStocks.indexOf(stock) <= -1
                    console.log 'stock is not present....'
                else 
                    $scope.selectedStocks.splice($scope.selectedStocks.indexOf(stock),1)
                    $scope.size = $scope.selectedStocks.filter((value) ->
                        value != ''
                        ).length

                

                                
                   
                
                

            $scope.showTable = (searchVal)->
                if searchVal.val == '' 
                    $scope.isShown = false
                else
                    $scope.isShown = true
    ]

)