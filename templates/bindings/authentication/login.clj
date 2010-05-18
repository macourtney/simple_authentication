(ns bindings.authentication.login
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map))