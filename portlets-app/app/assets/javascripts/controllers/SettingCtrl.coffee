'use strict'
angular.module('SettingCtrl',['Api'])
.controller(
    'SettingCtrl'
    [
        "$scope"
        "$log"
        "$http"
        "$cookies"
        "portletApi"
        "$location"
        ($scope,$log,$http,$cookies,portletApi,$location)->
            $log.debug('In SettingCtrl controller')
            portletApi.getPortfolioDetails(
                {
                  before: ->
                    $log.debug('Fetching Portfolio details.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.portfolio = data.owner
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting Portfolio details')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete')
                }
            )
            $scope.saveProfile= () ->
                console.log 'Data to submit' + JSON.stringify $scope.portfolio
                portletApi.saveProfile(
                    data: $scope.portfolio
                    before: ->
                        $log.debug('Submiting Portfolio details.')
                    success: (data, status, headers, config) ->
                        $log.debug 'User data submited successfully.' + JSON.stringify(data)
                        alert "Profile successfully saved."
                    error: (data, status, headers, config, statusText) ->
                        $log.error('Got error while saving Portfolio details')                   
                    complete: (data, status, headers, config) ->
                        $log.debug('In complete')
                )
    ])
