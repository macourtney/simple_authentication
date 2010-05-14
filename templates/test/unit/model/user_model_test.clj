(ns unit.model.user-model-test
  (:use clojure.contrib.test-is
        models.user
        fixture.user))

(def model "user")

(use-fixtures :once fixture)

(deftest test-first-record
  (is (get-record 1)))