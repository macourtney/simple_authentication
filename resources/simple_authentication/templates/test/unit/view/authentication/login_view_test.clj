(ns unit.view.authentication.login-view-test
  (:use clojure.contrib.test-is
        views.authentication.login)
  (:require [conjure.core.server.request :as request]))

(def controller-name "authentication")
(def view-name "login")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (request/set-request-map request-map
    (is (render-view [] "/"))
    (is (render-view ["invalid user"] nil))))