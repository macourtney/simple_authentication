(ns bindings.authentication.delete-user
  (:use conjure.core.binding.base))

(defbinding [request-map]
  (render-view request-map))