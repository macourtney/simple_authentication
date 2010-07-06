(ns unit.view.authentication.access-denied-view-test
  (:use clojure.contrib.test-is
        views.authentication.access-denied)
  (:require [conjure.core.server.request :as request]))

(def controller-name "authentication")
(def view-name "access-denied")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (request/set-request-map request-map
    (is (render-view))))