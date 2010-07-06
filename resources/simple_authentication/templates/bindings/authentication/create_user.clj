(ns bindings.authentication.create-user
  (:use conjure.core.binding.base)
  (:require [conjure.core.server.request :as request]))

(def-binding []
  (render-view (:errors (request/parameters))))