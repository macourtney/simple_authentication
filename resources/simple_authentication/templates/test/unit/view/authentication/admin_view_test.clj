(ns unit.view.authentication.admin-view-test
  (:use clojure.contrib.test-is
        views.authentication.admin)
  (:require [fixture.user :as user-fixture]
            [conjure.core.server.request :as request]))

(def controller-name "authentication")
(def view-name "admin")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (request/set-request-map request-map
    (is (render-view user-fixture/records))))