(ns functional.authentication-controller-test
  (:use clojure.contrib.test-is
        controllers.authentication-controller)
  (:require [conjure.controller.util :as controller-util]))

(def controller-name "authentication")

(deftest test-login
  (is (controller-util/call-controller { :controller controller-name :action "login" })))

(deftest test-logout
  (is (controller-util/call-controller { :controller controller-name :action "logout" })))

(deftest test-create-user
  (is (controller-util/call-controller { :controller controller-name :action "create-user" })))

(deftest test-admin
  (is (controller-util/call-controller { :controller controller-name :action "admin" })))

(deftest test-delete-user
  (is (controller-util/call-controller { :controller controller-name :action "delete-user" })))

(deftest test-edit-user
  (is (controller-util/call-controller { :controller controller-name :action "edit-user" })))