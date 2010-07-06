(ns unit.binding.authentication.edit-user-binding-test
  (:use clojure.contrib.test-is
        fixture.user)
  (:require [conjure.core.binding.util :as bind-util]
            [conjure.core.server.request :as request]
            [conjure.core.server.server :as server]))

(server/init)

(use-fixtures :once fixture)

(def controller-name "authentication")
(def action-name "edit-user")
(def request-map { :controller controller-name, :action action-name, :params { :id 1 } })

(deftest test-view
  (request/set-request-map request-map
    (is (bind-util/call-binding controller-name action-name []))))