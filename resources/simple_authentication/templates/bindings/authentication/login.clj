(ns bindings.authentication.login
  (:use conjure.core.binding.base)
  (:require [conjure.core.server.request :as request]))

(def-binding []
  (let [params (request/parameters)
        errors (:errors params)]
    (render-view (if errors (read-string errors)) (:back-link params))))