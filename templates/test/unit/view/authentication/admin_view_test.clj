(ns unit.view.authentication.admin-view-test
  (:use clojure.contrib.test-is
        views.authentication.admin))

(def controller-name "authentication")
(def view-name "admin")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map)))