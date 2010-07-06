(ns bindings.authentication.delete-verify
  (:use conjure.core.binding.base)
  (:require [models.user :as user]
            [conjure.core.server.request :as request]))

(def-binding []
  (render-view (user/get-record (request/id-str))))