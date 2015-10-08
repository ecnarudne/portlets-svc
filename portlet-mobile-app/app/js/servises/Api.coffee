'use strict'
angular.module('Api', ['ngCookies'])
.factory(
  'portletApi'
  [
    "$http"
    "$log"
    "$cookies"
    "$location"
    "$state"
    ($http, $log, $cookies, $location, $state) ->
      # Default domain to use
      domain = 'http://192.168.11.111:9000'
      
      actionUrl = (path) ->
          domain + path

      withDefaults = (options) ->
        defaults =
          data: {}
          success: ->
          error: ->
          forbidden: (data, status, headers, config) ->
            if(status==403)
              #$location.path("/pages-500")
            else
              options.error()
          complete: ->
          before: ->

        jQuery.extend(defaults, options)

      dispatch = (promise, options) ->
        promise.success(options.success).error(errorHandler(options)).finally(options.complete)

      errorHandler = (options) ->
        options.forbidden

      get = (url, request) ->
        if(!isLogin())
          return
        options = withDefaults(request)
        options.before()
        dispatch($http.get(url, {headers: "token": window.localStorage['token']}), options)

      post = (url, request) ->
        options = withDefaults(request)
        options.before()
        dispatch(
          $http.post(
            url
            JSON.stringify(options.data)
            headers:
              "token": window.localStorage['token']
          )
          options
        )
      postToken = (token,url, request) ->
        options = withDefaults(request)
        options.before()
        dispatch(
          $http.post(
            url
            JSON.stringify(options.data)
            headers:
              "token": token
          )
          options
        )  

      api = () ->
              
      api.prototype.login = (request) ->
        get(actionUrl("authenticate/google"),request)
        return     

      api.prototype.authGoogle = (token,request) ->
        postToken(token,actionUrl("/authbytoken"),request)
        return
        
      api.prototype.addPortlet = (request) ->
        post(actionUrl("/addPortlet"),request)
        return
      
      api.prototype.getPortfolioDetails = (request) ->
        get(actionUrl("/myPortfolio"),request)
        return 

      api.prototype.getPortfolioGraphData = (request) ->
        get(actionUrl("/myportfoliopxhist"),request)
        return

      api.prototype.getMyPortlets = (request) ->
        get(actionUrl("/listportlets"),request)
        return

      api.prototype.getCategories = (request) ->
        get(actionUrl("/listsectors"),request)
        return

      api.prototype.getPortlets = (request) ->
        get(actionUrl("/listportlets"),request)
        return
      
      api.prototype.getDiscoverPageDetails = (request) ->
        get(actionUrl("/page/discover/details"),request)
        return  getNewPortlets

      api.prototype.getNewPortlets = (request) ->
        get(actionUrl("/listrecentportlets/2"),request)
        return

      api.prototype.getTopPortlets = (request) ->
        get(actionUrl("/listopportlets/3"),request)
        return

      api.prototype.getPortletDetails = (portletId,request) ->
        get(actionUrl("/portlet/" + portletId),request)
        return

      api.prototype.getPortletStatTable = (portletId,request) ->
        get(actionUrl("/listportletstats/" + portletId),request)
        return

      api.prototype.getStocks = (exchange,request) ->
        get(actionUrl("/liststocksbyexchange/" + exchange ),request)
        return

      api.prototype.getStockExchange = (request) ->
        get(actionUrl("/listexchanges/"),request)
        return

      api.prototype.getStockDetails = (stockId,request) ->
        get(actionUrl("/stock/" + stockId),request)
        return

      api.prototype.getStockStat = (symbol,request) ->
        get(actionUrl("/stockstats/" + symbol),request)
        return
          
      isLogin = () ->
        if(window.localStorage['token'] == undefined)
          $log.debug('User is not logedin redirecting to sign-up.')
#          $location.path("/sign-up")
          $state.go("sign-up");
          return false
        else
          return true

      new api()
  ]
)