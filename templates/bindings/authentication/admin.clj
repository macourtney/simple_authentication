(ns bindings.authentication.admin
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map))