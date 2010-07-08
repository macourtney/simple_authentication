(ns unit.view.authentication.edit-user-view-test
  (:use clojure.contrib.test-is
        views.authentication.edit-user)
  (:require [fixture.user :as user]
            [conjure.core.server.request :as request]
            [conjure.core.server.server :as server]))

(server/init)

(def controller-name "authentication")
(def view-name "edit-user")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (request/set-request-map request-map
    (is (render-view (first user/records) []))
    (is (render-view (first user/records) ["Invalid password"]))))