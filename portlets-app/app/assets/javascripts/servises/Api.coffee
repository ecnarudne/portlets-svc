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
      domain = 'http://192.168.1.104:9000'
      
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
        post(actionUrl("/user/login"),request)
        return     
        
      api.prototype.addPortlet = (request) ->
        post(actionUrl("/addPortlet"),request)
        return
      
      api.prototype.getPortfolioDetails = (request) ->
        get(actionUrl("/page/portfolio/details"),request)
        return 

      api.prototype.getCategories = (request) ->
        get(actionUrl("/listcategories"),request)
        return
      
      api.prototype.getDiscoverPageDetails = (request) ->
        get(actionUrl("/page/discover/details"),request)
        return  

      api.prototype.getPortletPageDetails = (request) ->
        get(actionUrl("/page/portlet/details"),request)
        return

      api.prototype.getStocks = (request) ->
        get(actionUrl("/stocks/details"),request)
        return

      api.prototype.getStockExchange = (request) ->
        get(actionUrl("/stocks/exchange"),request)
        return

      api.prototype.getStockDetails = (request) ->
        post(actionUrl("/stock/details"),request)
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