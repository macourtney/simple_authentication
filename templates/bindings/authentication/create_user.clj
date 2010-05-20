(ns bindings.authentication.create-user
  (:use conjure.binding.base))

(defbinding [request-map]
  (render-view request-map (:errors (:params request-map))))