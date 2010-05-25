(ns unit.view.authentication.login-view-test
  (:use clojure.contrib.test-is
        views.authentication.login))

(def controller-name "authentication")
(def view-name "login")
(def request-map { :controller controller-name
                   :action view-name } )

(deftest test-view
  (is (render-view request-map [] "/"))
  (is (render-view request-map ["invalid user"] nil)))