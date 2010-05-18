(ns bindings.authentication.logout
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map))