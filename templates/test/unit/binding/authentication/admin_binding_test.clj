(ns unit.binding.authentication.admin-binding-test
  (:use clojure.contrib.test-is)
  (:require [conjure.binding.util :as bind-util]))

(def controller-name "authentication")
(def action-name "admin")
(def request-map { :controller controller-name, :action action-name})

(deftest test-view
  (is (bind-util/call-binding controller-name action-name [request-map])))