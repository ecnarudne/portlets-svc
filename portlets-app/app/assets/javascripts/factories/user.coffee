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