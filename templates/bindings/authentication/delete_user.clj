(ns bindings.authentication.delete-user
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map))