#We need to use this factory to get current userId. 
do ->
  app = angular.module('UserFactory', [])
  app.factory 'User', ->
    id = false
    {
      getUserId: ->
        id
      setUserId: (val) ->
        id = val
        return
    }
  return