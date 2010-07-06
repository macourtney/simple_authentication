(ns unit.binding.authentication.create-user-binding-test
  (:use clojure.contrib.test-is)
  (:require [conjure.core.binding.util :as bind-util]
            [conjure.core.server.request :as request]))

(def controller-name "authentication")
(def action-name "create-user")
(def request-map { :controller controller-name, :action action-name})

(deftest test-view
  (request/set-request-map request-map
    (is (bind-util/call-binding controller-name action-name []))))