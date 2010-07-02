(ns unit.binding.authentication.delete-user-binding-test
  (:use clojure.contrib.test-is)
  (:require [conjure.core.binding.util :as bind-util]))

(def controller-name "authentication")
(def action-name "delete-user")
(def request-map { :controller controller-name, :action action-name})

(deftest test-view
  (is (bind-util/call-binding controller-name action-name [request-map])))