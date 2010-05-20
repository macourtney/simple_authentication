(ns functional.authentication-controller-test
  (:use clojure.contrib.test-is
        controllers.authentication-controller)
  (:require [clojure.contrib.logging :as logging]
            [conjure.controller.util :as controller-util]
            [models.user :as user]))

(def controller-name "authentication")
(def request-map { :controller controller-name })

(deftest test-request-map-interceptor
  (is (request-map-interceptor request-map identity)))

(deftest test-login
  (is (controller-util/call-controller (assoc request-map :action "login"))))

(deftest test-logout
  (is (controller-util/call-controller (assoc request-map :action "logout"))))

(deftest test-create-user
  (is (controller-util/call-controller (assoc request-map :action "create-user"))))

(deftest test-save-user
  (let [test-user { :name "test-user", :password "password", :is_admin 0 }
        test-user-verify (assoc test-user :password-verify "password")
        save-user-request-map (assoc request-map :action "save-user")]
    (is (controller-util/call-controller  (assoc save-user-request-map :params { :user test-user-verify })))
    (let [saved-user (user/find-record test-user)]
      (is saved-user)
      (user/destroy-record saved-user)
      (is (not (user/find-record test-user))))
    (is (controller-util/call-controller 
          (assoc save-user-request-map :params { :user (assoc test-user-verify :name "a") })))
    (is (not (user/find-record test-user)))))

(deftest test-admin
  (is (controller-util/call-controller { :controller controller-name :action "admin" })))

(deftest test-delete-user
  (is (controller-util/call-controller { :controller controller-name :action "delete-user" })))

(deftest test-edit-user
  (is (controller-util/call-controller { :controller controller-name :action "edit-user" })))