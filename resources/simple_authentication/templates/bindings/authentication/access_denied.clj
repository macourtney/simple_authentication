(ns bindings.authentication.access-denied
  (:use conjure.core.binding.base))

(defbinding [request-map]
  (render-view request-map))