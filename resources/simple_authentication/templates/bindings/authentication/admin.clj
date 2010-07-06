(ns bindings.authentication.admin
  (:use conjure.core.binding.base)
  (:require [models.user :as user]))

(def-binding []
  (render-view (user/find-records ["true"])))