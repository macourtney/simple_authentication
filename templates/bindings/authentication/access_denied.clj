(ns bindings.authentication.access-denied
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map))