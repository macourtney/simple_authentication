(ns unit.view.authentication.edit-user-view-test
  (:use clojure.contrib.test-is
        views.authentication.edit-user))

(def controller-name "authentication")
(def view-name "edit-user")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map)))