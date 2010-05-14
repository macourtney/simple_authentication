(ns plugins.simple-authentication.test.test-plugin
  (use clojure.contrib.test-is
      plugins.simple-authentication.plugin))

(deftest test-migration-up-content
  (is (migration-up-content)))

(deftest test-migration-down-content
  (is (migration-down-content)))