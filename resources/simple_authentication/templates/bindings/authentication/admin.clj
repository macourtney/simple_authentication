(ns bindings.authentication.admin
  (:use conjure.core.binding.base)
  (:require [models.user :as user]))

(defbinding [request-map]
  (render-view request-map (user/find-records ["true"])))