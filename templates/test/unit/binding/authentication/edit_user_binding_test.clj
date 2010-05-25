(ns unit.binding.authentication.edit-user-binding-test
  (:use clojure.contrib.test-is
        fixture.user)
  (:require [conjure.binding.util :as bind-util]))

(use-fixtures :once fixture)

(def controller-name "authentication")
(def action-name "edit-user")
(def request-map { :controller controller-name, :action action-name, :params { :id 1 } })

(deftest test-view
  (is (bind-util/call-binding controller-name action-name [request-map])))