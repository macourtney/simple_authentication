(ns functional.authentication-controller-test
  (:use clojure.contrib.test-is)
  (:require [clojure.contrib.logging :as logging]
            [conjure.core.controller.util :as controller-util]
            [conjure.core.migration.runner :as migration-runner]
            [conjure.core.server.request :as request]
            [conjure.core.server.server :as server]
            [fixture.user :as user-fixture]))

(server/init)

(use 'controllers.authentication-controller)

(require ['models.user :as 'user])

(use-fixtures :once user-fixture/fixture)

(def controller-name "authentication")
(def request-map { :controller controller-name })

(deftest test-request-map-interceptor
  (is (request-map-interceptor #(identity true))))

(deftest test-login
  (request/with-controller-action controller-name "login"
    (is (controller-util/call-controller))))

(deftest test-logout
  (request/with-controller-action controller-name "logout"
    (is (controller-util/call-controller))))

(deftest test-create-user
  (request/with-controller-action controller-name "create-user"
    (is (controller-util/call-controller))))

(deftest test-save-user
  (let [test-user { :name "test-user", :is_admin 0 }
        test-user-verify (merge test-user { :password "password", :password-verify "password"})
        save-user-request-map (assoc request-map :action "save-user")]
    (request/set-request-map (assoc save-user-request-map :params { :user test-user-verify })
      (is (controller-util/call-controller)))
    (let [saved-user (user/find-record test-user)]
      (is saved-user)
      (user/destroy-record saved-user)
      (is (not (user/find-record test-user))))
    (request/set-request-map (assoc save-user-request-map :params { :user (assoc test-user-verify :name "a") })
      (is (controller-util/call-controller)))
    (is (not (user/find-record test-user)))))

(deftest test-admin
  (request/with-controller-action controller-name "admin"
    (is (controller-util/call-controller))))

(deftest test-delete-user
  (request/with-controller-action controller-name "delete-user"
    (is (controller-util/call-controller))))

(deftest test-edit-user
  (request/with-controller-action-id controller-name "edit-user" "1"
    (is (controller-util/call-controller))))

(deftest test-access-denied
  (request/with-controller-action controller-name "access-denied"
    (is (controller-util/call-controller))))