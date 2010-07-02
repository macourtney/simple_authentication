(ns bindings.authentication.delete-verify
  (:use conjure.core.binding.base)
  (:require [models.user :as user]))

(defbinding [request-map]
  (render-view request-map (user/get-record (:id (:params request-map)))))