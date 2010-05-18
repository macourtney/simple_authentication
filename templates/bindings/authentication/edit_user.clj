(ns bindings.authentication.edit-user
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map))