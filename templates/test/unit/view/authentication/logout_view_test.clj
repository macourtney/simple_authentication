(ns unit.view.authentication.logout-view-test
  (:use clojure.contrib.test-is
        views.authentication.logout))

(def controller-name "authentication")
(def view-name "logout")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map)))