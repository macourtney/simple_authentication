(ns unit.view.authentication.access-denied-view-test
  (:use clojure.contrib.test-is
        views.authentication.access-denied))

(def controller-name "authentication")
(def view-name "access-denied")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map)))