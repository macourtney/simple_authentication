(ns unit.view.authentication.delete-user-view-test
  (:use clojure.contrib.test-is
        views.authentication.delete-user))

(def controller-name "authentication")
(def view-name "delete-user")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map)))