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

            portletApi.getPortletPageDetails(
                {
                  before: ->
                    $log.debug('fetching page portlet data.')
                  success: (data, status, headers, config) ->
                    $log.debug 'Data fetched successfully.' + JSON.stringify(data)
                    $scope.data = data[0]
                  error: (data, status, headers, config, statusText) ->
                    $log.error('Got error while getting  table data')                   
                  complete: (data, status, headers, config) ->
                    $log.debug('In complete function')
                }
            )

            ###$('#table-1').dataTable
              'ajax': 'assets/javascripts/table_data.json'
              'columns': [
                {
                  'data': 'COMPANY'
                  'title': 'COMPANY'
                }
                {
                  'data': 'TICKER'
                  'title': 'TICKER'
                }
                {
                  'data': 'ACTIVITY'
                  'title': 'ACTIVITY'
                }
                {
                  'data': 'WEIGHT'
                  'title': 'WEIGHT'
                }
                {
                  'data': 'AVG COST'
                  'title': 'AVG COST'
                }
                {
                  'data': 'PRICE'
                  'title': 'PRICE'
                }
                {
                  'data': 'TOTAL RETURN'
                  'title': 'TOTAL RETURN'
                }
                {
                  'data': 'DAILY RETURN'
                  'title': 'DAILY RETURN'
                }
              ]
              'pageLength': 10
              'lengthChange': true
              'lengthMenu': [
                [
                  10
                  -1
                ]
                [
                  'VIEW LESS'
                  'VIEW ALL'
                ]
              ]
              'dom': '<"clearfix"r<"dataTables_wrapper"t>il>'
              'language':
                'lengthMenu': ' _MENU_ '
                'info': 'Showing _END_ of _TOTAL_ companies'
              'order': [
                3
                'desc'
              ]
              'columnDefs': [
                {
                  className: 'hidden-xs hidden-sm dt-body-right'
                  'targets': [
                    4
                    5
                    7
                  ]
                }
                {
                  className: 'hidden-xs hidden-sm'
                  'targets': [
                    0
                    2
                  ]
                }
                {
                  className: 'dt-body-right'
                  'targets': [
                    3
                    6
                  ]
                }
              ]
            table = $('#table-1').DataTable()
            $('#table-less').click ->
              table.page.len(10).draw false
              return
            $('#table-all').click ->
              table.page.len(-1).draw false
              return
            return###
            createTable()
            
    ]

)