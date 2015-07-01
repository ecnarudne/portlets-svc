'use strict'
angular.module('Api', ['ngCookies'])
.factory(
  'portletApi'
  [
    "$http"
    "$log"
    "$cookies"
    "$location"
    ($http, $log, $cookies, $location) ->
      # Default domain to use
      domain = 'http://localhost:9000'
      
      # We need to set cookie after login. Hardcoaded cookie   
      
      $cookies.cookieVal =  'Please set it after login'
      cookieVal = $cookies.cookieVal

      actionUrl = (path) ->
          domain + path

      withDefaults = (options) ->
        defaults =
          data: {}
          success: ->
          error: ->
          forbidden: (data, status, headers, config) ->
            if(status==403)
              delete $cookies.cookieVal
              $location.path("/pages-500")
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
        dispatch($http.get(url, {headers: "x-cookie-value": $cookies.cookieVal}), options)

      post = (url, request) ->
        options = withDefaults(request)
        options.before()
        dispatch(
          $http.post(
            url
            JSON.stringify(options.data)
            headers:
              "x-cookie-value": $cookies.cookieVal
          )
          options
        )

      api = () ->
              
      api.prototype.login = (request) ->
        get(actionUrl("/authenticate/google"),request)
        return     
        
      api.prototype.addPortlet = (request) ->
        post(actionUrl("/addPortlet"),request)
        return
      
      api.prototype.getPortfolioDetails = (request) ->
        get(actionUrl("/myPortfolio"),request)
        return 

      api.prototype.getMyPortlets = (request) ->
        get(actionUrl("/listmyportlets"),request)
        return

      api.prototype.getCategories = (request) ->
        get(actionUrl("/listsectors"),request)
        return

      api.prototype.getPortlets = (request) ->
        get(actionUrl("/listportlets"),request)
        return
      
      api.prototype.getDiscoverPageDetails = (request) ->
        get(actionUrl("/page/discover/details"),request)
        return  

      api.prototype.getPortletDetails = (portletId,request) ->
        get(actionUrl("/portlet/" + portletId),request)
        return

      api.prototype.getPortletStatTable = (portletId,request) ->
        get(actionUrl("/listmystockstats/" + portletId),request)
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
        if($cookies.cookieVal == undefined)
          $log.debug('User is not logedin redirecting to sign-up.')
          $location.path("/sign-up")
          return false
        else
          return true

      new api()
  ]
)