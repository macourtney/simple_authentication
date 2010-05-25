(ns bindings.authentication.edit-user
  (:use conjure.binding.base)
  (:require [clojure.contrib.logging :as logging]
            [models.user :as user]))

(defbinding [request-map]
  (let [params (:params request-map)
        user (:user params)]
    (logging/debug (str "params: " params))
    (render-view request-map 
      (if user
        (read-string user)
        (user/get-record (:id params))) 
      (:errors params))))