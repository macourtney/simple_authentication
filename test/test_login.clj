(ns plugins.simple-authentication.test.test-login
  (use clojure.contrib.test-is
       plugins.simple-authentication.login))

(deftest test-is-admin?
  (is (is-admin? { :is_admin 1 }))
  (is (not (is-admin? { :is_admin 0 })))
  (is (not (is-admin? {})))
  (is (not (is-admin? nil))))