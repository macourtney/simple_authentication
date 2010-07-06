(ns bindings.authentication.edit-user
  (:use conjure.core.binding.base)
  (:require [clojure.contrib.logging :as logging]
            [conjure.core.server.request :as request]
            [models.user :as user]))

(def-binding []
  (let [params (request/parameters)
        user (:user params)]
    (render-view 
      (if user
        (read-string user)
        (user/get-record (:id params))) 
      (:errors params))))