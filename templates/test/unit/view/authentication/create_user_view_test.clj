(ns unit.view.authentication.create-user-view-test
  (:use clojure.contrib.test-is
        views.authentication.create-user))

(def controller-name "authentication")
(def view-name "create-user")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map)))