'use strict'

appController = angular.module('PortletCreateCtrl', ['ionic'])

appController.controller(
  'PortletCreateCtrl'
  [
    "$scope"
    ($scope) ->
      console.log("u r in PortletCreateCtrl")
      $scope.data = {}
      $scope.delete = false;
      $scope.stocks = [
        {
          'name': 'Apple'
          'ticker': 'APPL'
        }
        {
          'name': 'Infosys'
          'ticker': 'INFY'
        }
        {
          'name': 'google'
          'ticker': 'GOOGL'
        }
      ]

      $scope.stockWeightages = [
        {
          'name': 'Apple'
          'weightage': 0
        }
        {
          'name': 'Infosys'
          'weightage': 0
        }
        {
          'name': 'google'
          'weightage': 0
        }
      ]
      $scope.available = 100
      $scope.getAvailable = ->
        i = 0
        per = 0
        while i < $scope.stockWeightages.length
            weightage = $scope.stockWeightages[i].weightage
            if weightage == null or weightage == NaN or weightage == ''
              console.log 'in if'
              weightage = 0      
            per = parseInt(per + parseInt(weightage))
            console.log "while iteration"+ per
            i++
        $scope.available = 100 - per
        if 100 - per < 0
            alert "weightage must be 100 %"


      $scope.signUp = ->
        console.log 'LOGIN user: ' + $scope.data.username + ' - PW: ' + $scope.data.password
        return

      $scope.checkStocks = -> 
        if $scope.stockWeightages.length < 9
          alert 'Portlet should have at least 9 Stocks'
          

      $scope.showDelete = (stockWeightage) -> 
        console.log 'wedfe' + stockWeightage
        if $scope.delete == false
          $scope.delete = true
        else
          $scope.delete = false

        


  ])