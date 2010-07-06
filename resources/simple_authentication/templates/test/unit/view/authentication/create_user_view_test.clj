(ns unit.view.authentication.create-user-view-test
  (:use clojure.contrib.test-is
        views.authentication.create-user)
  (:require [conjure.core.server.request :as request]))

(def controller-name "authentication")
(def view-name "create-user")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (request/set-request-map request-map
    (is (render-view nil))
    (is (render-view {}))
    (is (render-view { :password "Error test." }))))